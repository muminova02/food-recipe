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

        @Query("SELECT u FROM User u JOIN u.followers f WHERE f.id = :userId")
        List<User> findFollowersByUserId(@Param("userId") Integer userId);

        @Query("SELECT f FROM User u JOIN u.followers f WHERE u.id = :userId")
        List<User> findFollowingByUserId(@Param("userId") Integer userId);


        Boolean existsByIdAndFollowers_Id(Integer userId, Integer followerId);
}

