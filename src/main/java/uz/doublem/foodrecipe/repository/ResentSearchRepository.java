package uz.doublem.foodrecipe.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import uz.doublem.foodrecipe.entity.RecentSearch;

import java.util.List;
import java.util.Optional;

public interface ResentSearchRepository extends JpaRepository<RecentSearch, Integer> {


    Page<RecentSearch> findAllByUser_Id(@Param("userId") Integer userId, Pageable pageable);

    Optional<RecentSearch> findFirstByUserIdOrderByCreatedAtAsc(@Param("userId") Integer userId);


    Optional<RecentSearch> findByUserIdAndRecipeId(Integer userId, Integer recipeId);

}
