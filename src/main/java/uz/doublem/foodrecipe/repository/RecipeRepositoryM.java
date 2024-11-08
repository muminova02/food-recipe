package uz.doublem.foodrecipe.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.doublem.foodrecipe.entity.Recipe;

import java.util.List;

public interface RecipeRepositoryM extends JpaRepository<Recipe, Integer> {
    List<Recipe> findByIdIn(List<Integer> ids);
}

