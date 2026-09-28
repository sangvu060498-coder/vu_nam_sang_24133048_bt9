package vn.iotstar.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import vn.iotstar.dto.UserDTO;
import vn.iotstar.entity.Role;
import vn.iotstar.entity.User;
import vn.iotstar.mapper.UserMapper;
import vn.iotstar.repository.ProductRepository;
import vn.iotstar.repository.RoleRepository;
import vn.iotstar.repository.UserRepository;
import vn.iotstar.service.CloudinaryService;
import vn.iotstar.service.UserService;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final ProductRepository productRepository;
    private final UserMapper userMapper;
    private final CloudinaryService cloudinaryService;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, ProductRepository productRepository, UserMapper userMapper, CloudinaryService cloudinaryService, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.productRepository = productRepository;
        this.userMapper = userMapper;
        this.cloudinaryService = cloudinaryService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Page<UserDTO> getUsers(String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        Page<User> usersPage;
        if (keyword != null && !keyword.trim().isEmpty()) {
            String kw = keyword.trim();
            usersPage = userRepository.findByUsernameContainingIgnoreCaseOrEmailContainingIgnoreCaseOrFullNameContainingIgnoreCase(
                    kw, kw, kw, pageable
            );
        } else {
            usersPage = userRepository.findAll(pageable);
        }

        return usersPage.map(user -> {
            UserDTO dto = userMapper.toDto(user);
            dto.setProductCount(productRepository.countByUserId(user.getId()));
            return dto;
        });
    }

    @Override
    public UserDTO findById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng với ID: " + id));
        UserDTO dto = userMapper.toDto(user);
        dto.setProductCount(productRepository.countByUserId(user.getId()));
        return dto;
    }

    @Override
    @Transactional
    public UserDTO createUser(UserDTO dto, MultipartFile imageFile) {
        if (userRepository.existsByUsernameIgnoreCase(dto.getUsername())) {
            throw new RuntimeException("Username đã tồn tại.");
        }
        if (userRepository.existsByEmailIgnoreCase(dto.getEmail())) {
            throw new RuntimeException("Email đã tồn tại.");
        }

        User user = userMapper.toEntity(dto);
        user.setPassword(passwordEncoder.encode("123456"));

        Role role = roleRepository.findById(dto.getRoleId())
                .orElseGet(() -> roleRepository.findByNameIgnoreCase("ROLE_USER")
                        .orElseThrow(() -> new RuntimeException("Role không hợp lệ")));
        user.setRole(role);

        if (imageFile != null && !imageFile.isEmpty()) {
            String imageUrl = cloudinaryService.uploadImage(imageFile, "users");
            user.setImages(imageUrl);
        } else if (user.getImages() == null || user.getImages().isEmpty()) {
            user.setImages("/images/avatar-default.png");
        }

        User savedUser = userRepository.save(user);
        return userMapper.toDto(savedUser);
    }

    @Override
    @Transactional
    public UserDTO updateUser(Long id, UserDTO dto, MultipartFile imageFile) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng với ID: " + id));

        user.setFullName(dto.getFullName());
        user.setEnabled(dto.isEnabled());

        if (dto.getRoleId() != null) {
            Role role = roleRepository.findById(dto.getRoleId())
                    .orElseThrow(() -> new RuntimeException("Role không hợp lệ"));
            user.setRole(role);
        }

        if (imageFile != null && !imageFile.isEmpty()) {
            String imageUrl = cloudinaryService.uploadImage(imageFile, "users");
            user.setImages(imageUrl);
        }

        User updatedUser = userRepository.save(user);
        return userMapper.toDto(updatedUser);
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("Người dùng không tồn tại.");
        }
        userRepository.deleteById(id);
    }

    @Override
    public long countUsers() {
        return userRepository.count();
    }

    @Override
    public long countUserProducts(Long userId) {
        return productRepository.countByUserId(userId);
    }
}
