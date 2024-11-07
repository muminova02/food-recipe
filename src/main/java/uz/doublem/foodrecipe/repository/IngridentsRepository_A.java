package uz.doublem.foodrecipe.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import uz.doublem.foodrecipe.entity.Ingredient;

import java.util.List;
import java.util.Optional;

public interface IngridentsRepository_A extends JpaRepository<Ingredient,Integer> {

    /*@Query(value = "select i from Ingredient i where i.recipe=?1")
    Optional<List<Ingredient>> findByRecipeId(int id);*/

    Optional<List<Ingredient>> findByRecipe_Id(Integer id);
}
