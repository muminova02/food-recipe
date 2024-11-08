package uz.doublem.foodrecipe.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.doublem.foodrecipe.entity.Recipe;

public interface RecipeRepositoryM extends JpaRepository<Recipe, Integer> {

}
