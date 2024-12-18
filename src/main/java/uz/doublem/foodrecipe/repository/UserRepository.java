package uz.doublem.foodrecipe.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import uz.doublem.foodrecipe.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Integer> {
        Boolean existsByEmail(String email);

        Optional<User> findByEmail(String email);

        @Query(value = "SELECT u.* FROM users u " +
                "JOIN users_followers uf ON u.id = uf.user_id " +
                "WHERE uf.followers_id = :userId", nativeQuery = true)
        List<User> findFollowersByUserId(@Param("userId") Integer userId);

        @Query(value = "SELECT u.* FROM users u " +
                "JOIN users_followers uf ON u.id = uf.followers_id " +
                "WHERE uf.user_id = :userId", nativeQuery = true)
        List<User> findFollowingByUserId(@Param("userId") Integer userId);

        @Query(value = "SELECT CASE WHEN COUNT(*) > 0 THEN TRUE ELSE FALSE END " +
                "FROM users_followers " +
                "WHERE user_id = :userId AND followers_id = :followerId", nativeQuery = true)
        Boolean existsByUserIdAndFollowerId(@Param("userId") Integer userId, @Param("followerId") Integer followerId);

}

