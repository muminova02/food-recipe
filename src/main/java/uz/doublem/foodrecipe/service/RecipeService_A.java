package uz.doublem.foodrecipe.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import uz.doublem.foodrecipe.entity.*;
import uz.doublem.foodrecipe.payload.*;
import uz.doublem.foodrecipe.repository.*;
import uz.doublem.foodrecipe.util.Util;

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
    private final CategoryRepository categoryRepository;



//    public ResponseMessage allSavedRecipes(UserDTO userDTO) {
//        User user = userRepository.findByEmail(userDTO.email())
//                .orElseThrow(() -> new RuntimeException("User not found"));
//
//        List<SavedRecipes> savedRecipes = savedRecipeRepository.findByUserId(user.getId())
//                .orElse(Collections.emptyList());
//
//        if (savedRecipes.isEmpty()) {
//            return ResponseMessage.builder()
//                    .text("Recipes not found")
//                    .status(false)
//                    .data(ResponseEntity.notFound().build())
//                    .build();
//        }
//
//        List<SavedRecipesReturnDTO> savedRecipesReturnDTOS = savedRecipes.stream()
//                .map(this::convertToSavedRecipesReturnDTO)
//                .collect(Collectors.toList());
//
//        return ResponseMessage.builder()
//                .text("success")
//                .status(true)
//                .data(savedRecipesReturnDTOS)
//                .build();
//    }



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
        if (ingredients.isEmpty()) {
            return Util.getResponseMes(false,"recipe has not Ingredients",id);
        }
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



    public ResponseMessage getRecipeLink( Integer recipe_id) {
        Recipe recipe = checkForRecipeId(recipe_id);
        RecipeDTO_A build = RecipeDTO_A.builder().link(recipe.getLink()).build();
        return ResponseMessage.builder().text("success").status(true).data(build).build();
    }



    public ResponseMessage getRecipeRate(Integer recipe_id) {
        Recipe recipe = checkForRecipeId(recipe_id);
        RecipeDTO_A build = RecipeDTO_A.builder().averageReiting(recipe.getAverageRating()).build();
        return ResponseMessage.builder().text("success").status(true).data(build).build();
    }


    public ResponseMessage getRecipeViewCount(Integer recipe_id) {
        Recipe recipe = checkForRecipeId(recipe_id);
        RecipeDTO_A build = RecipeDTO_A.builder().viewCount(recipe.getViewsCount()).build();
        return ResponseMessage.builder().text("success").status(true).data(build).build();
    }

    private Recipe checkForRecipeId(Integer recipe_id) {
        return recipeRepository.findById(recipe_id).orElseThrow(() -> new RuntimeException("Recipe id do not found"));
    }


    public ResponseMessage getAllRecipe() {
        List<Category> all = categoryRepository.findAll();
        return ResponseMessage.builder().text("this all category").status(true).data(all).build();
    }

    public ResponseMessage getIngredients() {
        List<Ingredient> all = ingredientRepository.findAll();
        return ResponseMessage.builder().text("this all ingredients").status(true).data(all).build();
    }



}
