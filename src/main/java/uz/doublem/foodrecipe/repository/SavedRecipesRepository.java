package uz.doublem.foodrecipe.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.doublem.foodrecipe.entity.Recipe;
import uz.doublem.foodrecipe.entity.SavedRecipes;

import java.util.List;

public interface SavedRecipesRepository extends JpaRepository<SavedRecipes, Long> {

        List<SavedRecipes> findAllByOwner_Id(Integer owner_id);
        <S extends SavedRecipesRepository> S save(Recipe entity);


}
