package uz.doublem.foodrecipe.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.doublem.foodrecipe.entity.View;

public interface ViewRepository extends JpaRepository<View, Integer> {
    Boolean existsByUser_IdAndRecipe_Id(Integer userId, Integer recipeId);
}
