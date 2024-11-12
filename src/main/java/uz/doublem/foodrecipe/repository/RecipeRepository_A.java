package uz.doublem.foodrecipe.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import uz.doublem.foodrecipe.entity.Recipe;

import java.util.Optional;

public interface RecipeRepository_A extends JpaRepository<Recipe, Integer> {

    @Modifying
    @Transactional
    @Query("UPDATE Recipe r SET r.averageRating = :averageRating WHERE r.id = :recipeId")
    void updateAverageRating(@Param("recipeId") Integer recipeId, @Param("averageRating") Double averageRating);

}
