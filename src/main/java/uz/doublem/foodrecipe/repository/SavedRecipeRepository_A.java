package uz.doublem.foodrecipe.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import uz.doublem.foodrecipe.entity.SavedRecipes;

import java.util.List;
import java.util.Optional;

public interface SavedRecipeRepository_A extends JpaRepository<SavedRecipes, Integer> {


    @Query(value = "select t from SavedRecipes t where t.owner=?1")
    Optional<List<SavedRecipes>> findByUserId(Integer id);

    Optional<List<SavedRecipes>> findByRecipe_Id(Integer id);

    Long countByRecipe_Id(Integer id);
}
