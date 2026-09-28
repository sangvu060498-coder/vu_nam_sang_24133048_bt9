package vn.iotstar.controller;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vn.iotstar.dto.UserDTO;
import vn.iotstar.repository.RoleRepository;
import vn.iotstar.service.UserService;

@Controller
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final RoleRepository roleRepository;

    public UserController(UserService userService, RoleRepository roleRepository) {
        this.userService = userService;
        this.roleRepository = roleRepository;
    }

    @GetMapping
    public String listUsers(
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "5") int size,
            Model model
    ) {
        Page<UserDTO> userPage = userService.getUsers(keyword, page, size);
        model.addAttribute("userPage", userPage);
        model.addAttribute("keyword", keyword);
        model.addAttribute("totalUsers", userService.countUsers());
        return "user/list";
    }

    @GetMapping("/create")
    public String createUserForm(Model model) {
        model.addAttribute("userDTO", new UserDTO());
        model.addAttribute("roles", roleRepository.findAll());
        return "user/form";
    }

    @PostMapping("/create")
    public String createUser(
            @Valid @ModelAttribute("userDTO") UserDTO dto,
            BindingResult bindingResult,
            @RequestParam(value = "imageFile", required = false) MultipartFile imageFile,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("roles", roleRepository.findAll());
            return "user/form";
        }

        try {
            userService.createUser(dto, imageFile);
            redirectAttributes.addFlashAttribute("successMessage", "Thêm mới người dùng thành công!");
            return "redirect:/users";
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("roles", roleRepository.findAll());
            return "user/form";
        }
    }

    @GetMapping("/edit/{id}")
    public String editUserForm(@PathVariable("id") Long id, Model model) {
        UserDTO dto = userService.findById(id);
        model.addAttribute("userDTO", dto);
        model.addAttribute("roles", roleRepository.findAll());
        return "user/form";
    }

    @PostMapping("/edit/{id}")
    public String updateUser(
            @PathVariable("id") Long id,
            @ModelAttribute("userDTO") UserDTO dto,
            @RequestParam(value = "imageFile", required = false) MultipartFile imageFile,
            RedirectAttributes redirectAttributes
    ) {
        try {
            userService.updateUser(id, dto, imageFile);
            redirectAttributes.addFlashAttribute("successMessage", "Cập nhật người dùng thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/users";
    }

    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        try {
            userService.deleteUser(id);
            redirectAttributes.addFlashAttribute("successMessage", "Xóa người dùng thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/users";
    }
}
