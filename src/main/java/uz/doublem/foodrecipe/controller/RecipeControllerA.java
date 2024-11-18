package uz.doublem.foodrecipe.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.doublem.foodrecipe.payload.ResponseMessage;
import uz.doublem.foodrecipe.service.RecipeService_A;


@RestController
@RequiredArgsConstructor
@RequestMapping("/recipeA")
public class RecipeControllerA {

    final RecipeService_A service;


//    @GetMapping("/get")
//    public ResponseEntity<?> allSavedRecipes(@RequestBody UserDTO userDTO) {
//        ResponseMessage responseMessage = service.allSavedRecipes(userDTO);
//        return ResponseEntity.status(200).body(responseMessage);
//    }


    @GetMapping("/{id}/ingredient")
    public ResponseEntity<?> getRecipeIngredient(@PathVariable Integer id) {
        ResponseMessage res = service.recipeIngredient(id);
        return ResponseEntity.status(res.getStatus()?200:400).body(res);

    }


    @GetMapping("/{id}/share-link")
    public ResponseEntity<?> getRecipeLink(@PathVariable("id") Integer recipe_id) {
        ResponseMessage res = service.getRecipeLink(recipe_id);
        return ResponseEntity.status(res.getStatus()?200:400).body(res);
    }


    @GetMapping("/{id}/rating")
    public ResponseEntity<?> getRecipeRating(@PathVariable("id") Integer recipe_id) {
        ResponseMessage res = service.getRecipeRate(recipe_id);
        return ResponseEntity.status(res.getStatus()?200:400).body(res);
    }


    @GetMapping("/{id}/view-count")
    public ResponseEntity<?> getViewCount(@PathVariable("id") Integer recipe_id) {
        ResponseMessage res = service.getRecipeViewCount(recipe_id);
        return ResponseEntity.status(res.getStatus()?200:400).body(res);
    }






}
