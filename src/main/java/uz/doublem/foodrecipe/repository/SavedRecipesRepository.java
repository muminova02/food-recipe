package uz.doublem.foodrecipe.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import uz.doublem.foodrecipe.entity.Recipe;
import uz.doublem.foodrecipe.entity.SavedRecipes;

import java.util.List;
import java.util.Optional;

public interface SavedRecipesRepository extends JpaRepository<SavedRecipes, Long> {

     //   List<SavedRecipes> findAllByOwner_Id(Integer owner_id);

        Page<SavedRecipes> findAllByOwner_Id(Integer owner_id, Pageable pageable);

        Optional<SavedRecipes> findByOwnerIdAndRecipeId(Integer owner_id, Integer recipe_id);


}
