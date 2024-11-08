package uz.doublem.foodrecipe.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import uz.doublem.foodrecipe.entity.*;
import uz.doublem.foodrecipe.payload.RecipeDTO_A;
import uz.doublem.foodrecipe.payload.ResponseMessage;
import uz.doublem.foodrecipe.payload.UserDTO;
import uz.doublem.foodrecipe.repository.IngridentsRepository_A;
import uz.doublem.foodrecipe.repository.RecipeRepository_A;
import uz.doublem.foodrecipe.repository.SavedRecipeRepository_A;
import uz.doublem.foodrecipe.repository.UserRepository;

import java.util.Collections;
import java.util.List;
@Service
@RequiredArgsConstructor
public class RecipeService_A {

    final UserRepository userRepository;
    final SavedRecipeRepository_A savedRecipeRepository;
    final IngridentsRepository_A ingridentsRepository;
    final RecipeRepository_A recipeRepository;




    public ResponseMessage allSavedRecipes(UserDTO userDTO) {
        User user = userRepository.findByEmail(userDTO.email())
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<SavedRecipes> savedRecipes = savedRecipeRepository.findByUserId(user.getId())
                .orElse(Collections.emptyList());

        if (savedRecipes.isEmpty()) {
            return ResponseMessage.builder().text("Recipes do not found").status(false).data(ResponseEntity.notFound()).build();
        }

        return ResponseMessage.builder().text("success").status(true).data(savedRecipes).build();
    }




    public ResponseMessage recipeIngrident(Integer id) {
        List<Ingredient> ingredients =  ingridentsRepository.findByRecipe_Id(id).orElseThrow(() -> new RuntimeException("Recipe id do not found"));
        if (ingredients.isEmpty()) {
            return ResponseMessage.builder().text("Ingredients do not found").status(false).data(ResponseEntity.notFound()).build();
        }
        return ResponseMessage.builder().text("success").status(true).data(ingredients).build();
    }



    public ResponseMessage ingridentShare( Integer recipe_id) {
        Recipe recipe = recipeRepository.findById(recipe_id).orElseThrow(() -> new RuntimeException("Recipe id do not found"));
        RecipeDTO_A build = RecipeDTO_A.builder().link(recipe.getLink()).build();
        return ResponseMessage.builder().text("success").status(true).data(build).build();
    }



    public ResponseMessage ingridentMoreRate(Integer recipe_id) {
        Recipe recipe = recipeRepository.findById(recipe_id).orElseThrow(() -> new RuntimeException("Recipe id do not found"));
        RecipeDTO_A build = RecipeDTO_A.builder().averageReiting(recipe.getAverageRating()).build();
        return ResponseMessage.builder().text("success").status(true).data(build).build();
    }



    public ResponseMessage ingridentMoreReviews(Integer recipe_id) {
        Recipe recipe = recipeRepository.findById(recipe_id).orElseThrow(() -> new RuntimeException("Recipe id do not found"));
        RecipeDTO_A build = RecipeDTO_A.builder().views(recipe.getViews()).build();
        return ResponseMessage.builder().text("success").status(true).data(build).build();
    }



}
