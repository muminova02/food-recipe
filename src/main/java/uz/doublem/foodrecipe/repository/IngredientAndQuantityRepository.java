package uz.doublem.foodrecipe.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.doublem.foodrecipe.entity.IngredientAndQuantity;

public interface IngredientAndQuantityRepository extends JpaRepository<IngredientAndQuantity, Integer> {
}
