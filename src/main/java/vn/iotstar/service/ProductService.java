package vn.iotstar.service;

import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;
import vn.iotstar.dto.ProductDTO;

public interface ProductService {
    Page<ProductDTO> getProducts(String keyword, int page, int size);
    Page<ProductDTO> getProductsByUser(Long userId, String keyword, int page, int size);
    ProductDTO findById(Long id);
    ProductDTO createProduct(ProductDTO dto, MultipartFile imageFile, String currentUsername);
    ProductDTO updateProduct(Long id, ProductDTO dto, MultipartFile imageFile);
    void deleteProduct(Long id);
    long countProducts();
}
