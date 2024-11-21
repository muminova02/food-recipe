package uz.doublem.foodrecipe.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import uz.doublem.foodrecipe.entity.User;
import uz.doublem.foodrecipe.payload.ResponseMessage;
import uz.doublem.foodrecipe.repository.UserRepository;
import uz.doublem.foodrecipe.service.SavedRecipeService;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/saved-recipes")
public class SavedRecipeController {
    final SavedRecipeService savedRecipeService;
    final UserRepository userRepository;


    @PreAuthorize("authentication.name == principal.username")
    @GetMapping
    public ResponseEntity<?> getSavedRecipes(@RequestParam Integer size, @RequestParam Integer page ) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
       ResponseMessage res= savedRecipeService.getSavedRecipes(user,size, page);
        return ResponseEntity.status(res.getStatus()?200:400).body(res);
    }
    @PostMapping
    public ResponseEntity<?> createSavedRecipe(@RequestParam Integer recipeId ) {
         User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ResponseMessage res= savedRecipeService.createSavedRecipes(user,recipeId);

        return ResponseEntity.status(res.getStatus()?200:400).body(res);
    }


    @DeleteMapping
    public void deleteById(@RequestParam Integer recipeId ) {
         User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        savedRecipeService.unSaveRecipe(recipeId,user);
    }




}
