package uz.doublem.foodrecipe.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.relational.core.sql.In;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.doublem.foodrecipe.entity.*;
import uz.doublem.foodrecipe.payload.RecipeDTO_A;
import uz.doublem.foodrecipe.payload.ResponseMessage;
import uz.doublem.foodrecipe.payload.UserDTO;
import uz.doublem.foodrecipe.repository.IngridentsRepository_A;
import uz.doublem.foodrecipe.repository.RecipeRepository_A;
import uz.doublem.foodrecipe.repository.SavedRecipeRepository_A;
import uz.doublem.foodrecipe.repository.UserRepository;
import uz.doublem.foodrecipe.service.RecipeService_A;

import java.util.Collections;
import java.util.List;
import java.util.Optional;


@RestController
@RequiredArgsConstructor
@RequestMapping("/recipeA")
public class RecipeControllerA {

    final RecipeService_A service;




    @GetMapping("/saved")
    public ResponseEntity<?> allSavedRecipes(@RequestBody UserDTO userDTO) {
        ResponseMessage responseMessage = service.allSavedRecipes(userDTO);
        return ResponseEntity.status(200).body(responseMessage);
    }



    @GetMapping("/ingrident/{id}")
    public ResponseEntity<?> recipeIngrident(@PathVariable Integer id) {
        ResponseMessage responseMessage = service.recipeIngredient(id);
        return ResponseEntity.status(200).body(responseMessage);
    }


    @GetMapping("/ingrident/share/{recipe_id}")
    public ResponseEntity<?> ingridentShare(@PathVariable("recipe_id") Integer recipe_id) {
        ResponseMessage responseMessage = service.ingridentShare(recipe_id);
        return ResponseEntity.status(200).body(responseMessage);
    }


    @GetMapping("/ingrident/more/rate/{recipe_id}")
    public ResponseEntity<?> ingridentMoreRate(@PathVariable("recipe_id") Integer recipe_id) {
        return ResponseEntity.status(200).body(service.ingridentMoreRate(recipe_id));
    }


    @GetMapping("/ingrident/reviews/{recipe_id}")
    public ResponseEntity<?> ingridentMoreReviews(@PathVariable("recipe_id") Integer recipe_id) {
    return ResponseEntity.status(200).body(service.ingridentMoreReviews(recipe_id));
    }









}
