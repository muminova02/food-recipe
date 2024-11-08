package uz.doublem.foodrecipe.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.doublem.foodrecipe.entity.Ingredient;


public interface IngredientRepository extends JpaRepository<Ingredient, Integer> {




}
