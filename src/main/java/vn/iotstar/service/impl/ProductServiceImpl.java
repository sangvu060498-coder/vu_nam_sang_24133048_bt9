package vn.iotstar.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import vn.iotstar.dto.ProductDTO;
import vn.iotstar.entity.Product;
import vn.iotstar.entity.User;
import vn.iotstar.mapper.ProductMapper;
import vn.iotstar.repository.ProductRepository;
import vn.iotstar.repository.UserRepository;
import vn.iotstar.service.CloudinaryService;
import vn.iotstar.service.CloudinaryUploadResult;
import vn.iotstar.service.ProductService;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final ProductMapper productMapper;
    private final CloudinaryService cloudinaryService;

    public ProductServiceImpl(ProductRepository productRepository, UserRepository userRepository, ProductMapper productMapper, CloudinaryService cloudinaryService) {
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.productMapper = productMapper;
        this.cloudinaryService = cloudinaryService;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductDTO> getProducts(String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(Math.max(page, 0), Math.max(size, 1), Sort.by(Sort.Direction.DESC, "id"));
        String kw = (keyword == null) ? "" : keyword.trim();
        Page<Product> productPage = productRepository.search(kw, pageable);
        return productPage.map(productMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductDTO> getProductsByUser(Long userId, String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(Math.max(page, 0), Math.max(size, 1), Sort.by(Sort.Direction.DESC, "id"));
        Page<Product> productPage = productRepository.findByUserId(userId, pageable);
        return productPage.map(productMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductDTO findById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy sản phẩm với ID: " + id));
        return productMapper.toDto(product);
    }

    @Override
    @Transactional
    public ProductDTO createProduct(ProductDTO dto, MultipartFile imageFile, String currentUsername) {
        User user = userRepository.findByUsernameOrEmail(currentUsername, currentUsername)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy thông tin người dùng tạo sản phẩm."));

        Product product = productMapper.toEntity(dto);
        product.setUser(user);

        if (imageFile != null && !imageFile.isEmpty()) {
            CloudinaryUploadResult result = cloudinaryService.upload(imageFile);
            product.setImageUrl(result.url() + "|" + result.publicId());
        }

        Product savedProduct = productRepository.save(product);
        return productMapper.toDto(savedProduct);
    }

    @Override
    @Transactional
    public ProductDTO updateProduct(Long id, ProductDTO dto, MultipartFile imageFile) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy sản phẩm với ID: " + id));

        product.setName(dto.getName());
        product.setPrice(dto.getPrice());
        product.setDescription(dto.getDescription());

        if (imageFile != null && !imageFile.isEmpty()) {
            String oldImage = product.getImageUrl();
            if (oldImage != null && oldImage.contains("|")) {
                String publicId = oldImage.substring(oldImage.indexOf('|') + 1);
                cloudinaryService.delete(publicId);
            }

            CloudinaryUploadResult result = cloudinaryService.upload(imageFile);
            product.setImageUrl(result.url() + "|" + result.publicId());
        }

        Product updatedProduct = productRepository.save(product);
        return productMapper.toDto(updatedProduct);
    }

    @Override
    @Transactional
    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Sản phẩm không tồn tại."));

        String image = product.getImageUrl();
        if (image != null && image.contains("|")) {
            String publicId = image.substring(image.indexOf('|') + 1);
            cloudinaryService.delete(publicId);
        }

        productRepository.delete(product);
    }

    @Override
    @Transactional(readOnly = true)
    public long countProducts() {
        return productRepository.count();
    }
}
