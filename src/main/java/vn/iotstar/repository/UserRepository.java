package vn.iotstar.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import vn.iotstar.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    Optional<User> findByEmailIgnoreCase(String email);
    Optional<User> findByUsernameOrEmail(String username, String email);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    boolean existsByUsernameIgnoreCase(String username);
    boolean existsByEmailIgnoreCase(String email);

    @Query("""
        SELECT u FROM User u
        WHERE lower(u.username) LIKE lower(concat('%', :keyword, '%'))
           OR lower(u.email) LIKE lower(concat('%', :keyword, '%'))
           OR lower(u.fullName) LIKE lower(concat('%', :keyword, '%'))
    """)
    Page<User> search(@Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT count(p) FROM Product p WHERE p.user.id = :userId")
    long countProductsByUserId(@Param("userId") Long userId);

    Page<User> findByUsernameContainingIgnoreCaseOrEmailContainingIgnoreCaseOrFullNameContainingIgnoreCase(
            String username, String email, String fullName, Pageable pageable);
}
