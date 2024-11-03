package uz.doublem.foodrecipe.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uz.doublem.foodrecipe.entity.SavedReciepes;
import uz.doublem.foodrecipe.entity.User;
import uz.doublem.foodrecipe.payload.RecipeDTO_A;
import uz.doublem.foodrecipe.payload.UserDTO;
import uz.doublem.foodrecipe.repository.SavedRecipeRepository_A;
import uz.doublem.foodrecipe.repository.UserRepository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/recipeA")
public class RecipeControllerA {
      final UserRepository userRepository;
      final SavedRecipeRepository_A savedRecipeRepository;

    public RecipeControllerA(UserRepository userRepository, SavedRecipeRepository_A savedRecipeRepository) {
        this.userRepository = userRepository;
        this.savedRecipeRepository = savedRecipeRepository;
    }

    @GetMapping("/saved")
    public ResponseEntity<?> allSavedRecipes(@RequestBody UserDTO userDTO) {
        User user = userRepository.findByEmail(userDTO.email())
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<SavedReciepes> savedRecipes = savedRecipeRepository.findByUserId(user.getId())
                .orElse(Collections.emptyList());

        if (savedRecipes.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(savedRecipes);
    }



    @GetMapping("/ingrident")
    public ResponseEntity<?> recipeIngrident(@RequestBody RecipeDTO_A recipeDTO_a) {
    return null;

    }







}
