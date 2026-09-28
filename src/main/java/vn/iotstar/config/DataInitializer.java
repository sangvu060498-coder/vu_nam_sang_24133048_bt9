package vn.iotstar.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import vn.iotstar.entity.Role;
import vn.iotstar.entity.User;
import vn.iotstar.repository.RoleRepository;
import vn.iotstar.repository.UserRepository;

@Component
public class DataInitializer implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(RoleRepository roleRepository, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        Role adminRole = roleRepository.findByNameIgnoreCase("ROLE_ADMIN")
                .orElseGet(() -> roleRepository.save(new Role("ROLE_ADMIN")));

        Role userRole = roleRepository.findByNameIgnoreCase("ROLE_USER")
                .orElseGet(() -> roleRepository.save(new Role("ROLE_USER")));

        if (!userRepository.existsByUsernameIgnoreCase("admin")) {
            User admin = User.builder()
                    .username("admin")
                    .email("admin@gmail.com")
                    .password(passwordEncoder.encode("123456"))
                    .fullName("Vũ Nam Sang (Admin - 24133048)")
                    .images("/images/avatar-default.png")
                    .enabled(true)
                    .role(adminRole)
                    .build();
            userRepository.save(admin);
            log.info("Default Admin User created: admin / 123456");
        }

        if (!userRepository.existsByUsernameIgnoreCase("vunamsang")) {
            User userSang = User.builder()
                    .username("vunamsang")
                    .email("24133048@student.hcmute.edu.vn")
                    .password(passwordEncoder.encode("123456"))
                    .fullName("Vũ Nam Sang (MSSV: 24133048)")
                    .images("/images/avatar-default.png")
                    .enabled(true)
                    .role(userRole)
                    .build();
            userRepository.save(userSang);
            log.info("Student User created: vunamsang / 123456 (MSSV: 24133048)");
        }

        if (!userRepository.existsByUsernameIgnoreCase("user01")) {
            User user1 = User.builder()
                    .username("user01")
                    .email("user01@gmail.com")
                    .password(passwordEncoder.encode("123456"))
                    .fullName("Vũ Nam Sang (24133048)")
                    .images("/images/avatar-default.png")
                    .enabled(true)
                    .role(userRole)
                    .build();
            userRepository.save(user1);
            log.info("Default User created: user01 / 123456");
        }
    }
}
