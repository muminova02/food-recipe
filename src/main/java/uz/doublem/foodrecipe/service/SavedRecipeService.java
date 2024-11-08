package uz.doublem.foodrecipe.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uz.doublem.foodrecipe.entity.Recipe;
import uz.doublem.foodrecipe.entity.SavedRecipes;
import uz.doublem.foodrecipe.repository.SavedRecipesRepository;

import java.util.List;
import java.util.Objects;

@Service
public class SavedRecipeService {


    private  final SavedRecipesRepository savedReciepe;

    @Autowired
    public SavedRecipeService(SavedRecipesRepository savedReciepe) {
        this.savedReciepe = savedReciepe;
    }

    public SavedRecipes addRecipe(Recipe recipe) {
        return savedReciepe.save(recipe);
    }

    public void deleteRecipe(SavedRecipes recipe) {
        savedReciepe.delete(recipe);
    }


    public List<SavedRecipes> getSavedReciepeById(Integer id) {
        return Objects.requireNonNull(savedReciepe.findAllByOwner_Id(id));
    }

}
