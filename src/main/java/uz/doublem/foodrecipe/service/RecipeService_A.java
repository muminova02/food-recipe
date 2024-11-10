package uz.doublem.foodrecipe.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import uz.doublem.foodrecipe.entity.*;
import uz.doublem.foodrecipe.payload.*;
import uz.doublem.foodrecipe.repository.*;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecipeService_A {

    final UserRepository userRepository;
    final SavedRecipeRepository_A savedRecipeRepository;
    final IngridentsRepository_A ingridentsRepository;
    final RecipeRepository_A recipeRepository;
    final IngredientRepository ingredientRepository;




    public ResponseMessage allSavedRecipes(UserDTO userDTO) {
        User user = userRepository.findByEmail(userDTO.email())
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<SavedRecipes> savedRecipes = savedRecipeRepository.findByUserId(user.getId())
                .orElse(Collections.emptyList());

        if (savedRecipes.isEmpty()) {
            return ResponseMessage.builder()
                    .text("Recipes not found")
                    .status(false)
                    .data(ResponseEntity.notFound().build())
                    .build();
        }

        List<SavedRecipesReturnDTO> savedRecipesReturnDTOS = savedRecipes.stream()
                .map(this::convertToSavedRecipesReturnDTO)
                .collect(Collectors.toList());

        return ResponseMessage.builder()
                .text("success")
                .status(true)
                .data(savedRecipesReturnDTOS)
                .build();
    }

    private SavedRecipesReturnDTO convertToSavedRecipesReturnDTO(SavedRecipes savedRecipe) {
        Recipe recipe = savedRecipe.getRecipe();
        User author = recipe.getAuthor();

        return SavedRecipesReturnDTO.builder()
                .title(recipe.getTitle())
                .author(author.getName())
                .cookingTime(recipe.getCookingTime())
                .averageRating(recipe.getAverageRating())
                .build();
    }



    public ResponseMessage recipeIngredient(Integer id) {
        List<IngredientAndQuantity> ingredients = ingridentsRepository.findByRecipe_Id(id)
                .orElseThrow(() -> new RuntimeException("Recipe id not found"));

        List<IngredientReturnDTO> ingredientReturnDTOS = ingredients.stream()
                .map(ingredientAndQuantity -> ingredientRepository.findById(ingredientAndQuantity.getIngredient().getId())
                        .map(ingredient -> {
                            IngredientReturnDTO dto = new IngredientReturnDTO();
                            dto.setQuantity(ingredientAndQuantity.getQuantity());
                            dto.setName(ingredient.getName());
                            dto.setIcon(ingredient.getIcon());
                            return dto;
                        })
                        .orElse(null)
                )
                .filter(Objects::nonNull)  // Filter out nulls for missing ingredients
                .collect(Collectors.toList());

        if (ingredientReturnDTOS.isEmpty()) {
            return ResponseMessage.builder()
                    .text("Ingredients not found")
                    .status(false)
                    .data(ResponseEntity.notFound().build())
                    .build();
        }

        return ResponseMessage.builder()
                .text("success")
                .status(true)
                .data(ingredientReturnDTOS)
                .build();
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