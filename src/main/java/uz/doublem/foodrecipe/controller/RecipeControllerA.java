package uz.doublem.foodrecipe.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.relational.core.sql.In;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.doublem.foodrecipe.entity.*;
import uz.doublem.foodrecipe.payload.RecipeDTO_A;
import uz.doublem.foodrecipe.payload.UserDTO;
import uz.doublem.foodrecipe.repository.IngridentsRepository_A;
import uz.doublem.foodrecipe.repository.RecipeRepository_A;
import uz.doublem.foodrecipe.repository.SavedRecipeRepository_A;
import uz.doublem.foodrecipe.repository.UserRepository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;


@RestController
@RequiredArgsConstructor
@RequestMapping("/recipeA")
public class RecipeControllerA {

      final UserRepository userRepository;
      final SavedRecipeRepository_A savedRecipeRepository;
      final IngridentsRepository_A ingridentsRepository;
      final RecipeRepository_A recipeRepository;




    @GetMapping("/saved")
    public ResponseMessage allSavedRecipes(@RequestBody UserDTO userDTO) {
        User user = userRepository.findByEmail(userDTO.email())
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<SavedReciepes> savedRecipes = savedRecipeRepository.findByUserId(user.getId())
                .orElse(Collections.emptyList());

        if (savedRecipes.isEmpty()) {
            return ResponseMessage.builder().message("Recipes do not found").success(false).data(ResponseEntity.notFound()).build();
        }

        return ResponseMessage.builder().message("success").success(true).data(ResponseEntity.ok().body(savedRecipes)).build();
    }



    @GetMapping("/ingrident/{id}")
    public ResponseMessage recipeIngrident(@PathVariable Integer id) {
       List<Ingredient> ingredients = ingridentsRepository.findByRecipeId(id).orElseThrow(() -> new RuntimeException("Recipe id do not found"));
       if (ingredients.isEmpty()) {
           return ResponseMessage.builder().message("Ingredients do not found").success(false).data(ResponseEntity.notFound()).build();
       }
       return ResponseMessage.builder().message("success").success(true).data(ResponseEntity.ok().body(ingredients)).build();
    }


    @GetMapping("/ingrident/share/{recipe_id}")
    public ResponseMessage ingridentShare(@PathVariable("recipe_id") Integer recipe_id) {
        Recipe recipe = recipeRepository.findById(recipe_id).orElseThrow(() -> new RuntimeException("Recipe id do not found"));
        RecipeDTO_A build = RecipeDTO_A.builder().link(recipe.getLink()).build();
        return ResponseMessage.builder().message("success").success(true).data(ResponseEntity.ok().body(build)).build();
    }


    @GetMapping("/ingrident/more/rate/{recipe_id}")
    public ResponseMessage ingridentMoreRate(@PathVariable("recipe_id") Integer recipe_id) {
        Recipe recipe = recipeRepository.findById(recipe_id).orElseThrow(() -> new RuntimeException("Recipe id do not found"));
        RecipeDTO_A build = RecipeDTO_A.builder().averageReiting(recipe.getAverageRating()).build();
        return ResponseMessage.builder().message("success").success(true).data(ResponseEntity.ok().body(build)).build();
    }


    @GetMapping("/ingrident/reviews/{recipe_id}")
    public ResponseMessage ingridentMoreReviews(@PathVariable("recipe_id") Integer recipe_id) {
        Recipe recipe = recipeRepository.findById(recipe_id).orElseThrow(() -> new RuntimeException("Recipe id do not found"));
        RecipeDTO_A build = RecipeDTO_A.builder().views(recipe.getViews()).build();
        return ResponseMessage.builder().message("success").success(true).data(ResponseEntity.ok().body(build)).build();
    }









}
