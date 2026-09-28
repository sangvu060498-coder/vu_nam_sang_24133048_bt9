package vn.iotstar.service;

import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;
import vn.iotstar.dto.UserDTO;

public interface UserService {
    Page<UserDTO> getUsers(String keyword, int page, int size);
    UserDTO findById(Long id);
    UserDTO createUser(UserDTO dto, MultipartFile imageFile);
    UserDTO updateUser(Long id, UserDTO dto, MultipartFile imageFile);
    void deleteUser(Long id);
    long countUsers();
    long countUserProducts(Long userId);
}
