package uz.doublem.foodrecipe.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import uz.doublem.foodrecipe.entity.Recipe;
import uz.doublem.foodrecipe.entity.SavedRecipes;
import uz.doublem.foodrecipe.service.SavedRecipeService;

import java.util.List;

@RestController
@RequestMapping("/saved-recipes")
public class SavedRecipeController {
    final SavedRecipeService savedRecipeService;

    @Autowired
    public SavedRecipeController(SavedRecipeService savedRecipeService) {
        this.savedRecipeService = savedRecipeService;
    }

    @PostMapping
    public SavedRecipes save(@RequestBody Recipe recipe) {
        return savedRecipeService.addRecipe(recipe);
    }

    @GetMapping
    public List<SavedRecipes> getAllRecipesById(Integer ownerId) {
        return savedRecipeService.getSavedReciepeById(ownerId);
    }
    @DeleteMapping
    public void deleteById(SavedRecipes id) {
        savedRecipeService.deleteRecipe(id);
    }




}
