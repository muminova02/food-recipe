package uz.doublem.foodrecipe.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import uz.doublem.foodrecipe.entity.Review;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository_A extends JpaRepository<Review, Integer> {


    Page<Review> findAllByRecipe_Id(Integer id, Pageable pageable);

    @Query("SELECT COUNT(r) FROM Review r WHERE r.user.id = :userId AND r.recipe.id = :recipeId AND r.comment IS NOT NULL")
    Long countNonNullCommentsByUserAndRecipe(@Param("userId") Integer userId, @Param("recipeId") Integer recipeId);

    Optional<Review> findByRecipe_IdAndUser_id(Integer recipeId, Integer userId);

    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.recipe.id = :recipeId AND r.rating IS NOT NULL")
    Double findAverageRatingByRecipeId(@Param("recipeId") Integer recipeId);

}
