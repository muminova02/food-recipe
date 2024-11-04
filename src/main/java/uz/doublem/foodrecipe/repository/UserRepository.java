package uz.doublem.foodrecipe.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import uz.doublem.foodrecipe.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {
        Optional<User> findByEmail(String email);

    @Query("SELECT COUNT(e) > 0 FROM User e WHERE e.email = :email")
    boolean existsByEmail(String email);
}
