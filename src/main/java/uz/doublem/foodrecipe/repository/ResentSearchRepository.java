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

    @Query("SELECT r.recipe.id FROM RecentSearch r WHERE r.user.id = :userId ORDER BY r.createdAt DESC")
    Page<Integer> findRecipeIdsByUserId(@Param("userId") Integer userId, Pageable pageable);

    Optional<RecentSearch> findFirstByUserIdOrderByCreatedAtAsc(@Param("userId") Integer userId);


    Optional<RecentSearch> findByUserIdAndRecipeId(Integer userId, Integer recipeId);

}
