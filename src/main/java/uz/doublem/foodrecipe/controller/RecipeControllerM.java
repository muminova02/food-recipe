package uz.doublem.foodrecipe.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import uz.doublem.foodrecipe.entity.Ingredient;
import uz.doublem.foodrecipe.entity.Recipe;
import uz.doublem.foodrecipe.entity.User;
import uz.doublem.foodrecipe.payload.*;
import uz.doublem.foodrecipe.repository.UserRepository;
import uz.doublem.foodrecipe.service.RecipeServiceM;
import uz.doublem.foodrecipe.service.StepsService;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@Tag(name = "Recipe >> ")
@RequestMapping("/recipeM")
public class RecipeControllerM {

    private final RecipeServiceM recipeServiceM;

    @Operation(summary = "Recipe qo'shish")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "201",
                    description = "If successfully created you get  status '201' and we returned RecipeID",
                    content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseMessage.class))
                    }
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400",
                    description = "If get 400 status Please read response 'message'",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseMessage.class)))
    })

    @PreAuthorize("hasRole('ROLE_CHEF')")
    @PostMapping(value = "/addOne", consumes = {"multipart/form-data"})
    public ResponseEntity<?> addRecipe(
            @Parameter(
                    name = "json",
                    description = "Recipe details in JSON format (excluding photo) ",
                    required = true,
                    schema = @Schema(implementation = RecipeDTOAdd.class, format = "json", type = "string")
            )
            @RequestPart(value = "json") String json,
            @Parameter(
                    description = "Select picture on format .jpg or .png or .svg ",
                    content = @Content(mediaType = "multipart/form-data",
                            schema = @Schema(
                                    type = "string",
                                    format = "binary",
                                    example = "image.png"
                            )))
            @RequestPart(name = "file", required = false) MultipartFile attachments) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ResponseMessage res = recipeServiceM.addRecipe(json, attachments, user);
        return ResponseEntity.status(res.getStatus() ? 201 : 400).body(res);

    }
    @PreAuthorize("hasRole('ROLE_CHEF')")
    @PostMapping("/addOnly")
    public ResponseEntity<?> addRecipeOnly(@RequestBody RecipeDTOaddOnly recipeDTOaddOnly) {
               User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ResponseMessage res = recipeServiceM.addRecipeOnly(recipeDTOaddOnly, user);
        return ResponseEntity.status(res.getStatus() ? 201 : 400).body(res);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/category")
    public ResponseEntity<?> addCategoryList(@RequestBody List<CategoryDto> categoryDtoList) {
        ResponseMessage res = recipeServiceM.addCategoryList(categoryDtoList);
        return ResponseEntity.status(res.getStatus() ? 201 : 400).body(res);
    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping(value = "/ingredient", consumes = {"multipart/form-data"})
    public ResponseEntity<?> addIngredientList(
            @Parameter(
                    name = "json",
                    description = "Ingredient add in JSON format (excluding photo)",
                    required = true,
                    schema = @Schema(implementation = IngredientDTO.class, format = "json", type = "string")
            )
            @RequestPart(value = "json") String json,
            @Parameter(
                    description = "Select picture on format .jpg or .png or .svg or video on format .mp4, .avi, .mov",
                    content = @Content(mediaType = "multipart/form-data",
                            schema = @Schema(
                                    type = "string",
                                    format = "binary",
                                    example = "image.png, or video.mp4"
                            )))
            @RequestPart(name = "file") List<MultipartFile> attachments) {
        ResponseMessage res = recipeServiceM.addIngredientList(json, attachments);
        return ResponseEntity.status(res.getStatus() ? 201 : 400).body(res);
    }


//    @Operation(summary = "Upload photo and Video to recipe ")
//    @PostMapping(value = "/{id}/attachments", consumes = {
//            MediaType.MULTIPART_FORM_DATA_VALUE
//    })
//    public ResponseEntity<?> upload(
//            @PathVariable Integer id,
//            @Parameter(
//                    description = "Select picture on format .jpg or .png and .svg or video on format .mp4, .avi, .mov for video,",
//                    content = @Content(mediaType = "multipart/form-data",
//                            schema = @Schema(
//                                    type = "string",
//                                    format = "binary",
//                                    example = "image.png, or video.mp4"
//                            )))
//            @RequestPart(name = "file", required = false) List<MultipartFile> attachments) {
//        ResponseMessage res = recipeServiceM.upload(attachments, id);
//        return ResponseEntity.status(res.getStatus() ? 200 : 400).body(res);
//    }

    @PreAuthorize("hasRole('ROLE_CHEF')")
    @PostMapping("/{id}/steps")
    public ResponseEntity<?> addStepsList(
            @PathVariable Integer id,
            @RequestBody List<StepsDTOAdd> stepsDTOAdds) {
        ResponseMessage res = recipeServiceM.addStepsToRecipe(stepsDTOAdds, id);
        return ResponseEntity.status(res.getStatus() ? 201 : 400).body(res);
    }

    @PreAuthorize("hasRole('ROLE_CHEF')")
    @PostMapping("/{id}/ingredients")
    public ResponseEntity<?> addIngredientListToRecipe(
            @PathVariable Integer id,
            @RequestBody List<IngredientDTOAdd> ingredientDTOAdds) {
        ResponseMessage res = recipeServiceM.addIngredientAndQuantityListToRecipe(ingredientDTOAdds, id);
        return ResponseEntity.status(res.getStatus() ? 201 : 400).body(res);
    }


    @GetMapping("/{id}")
    public ResponseEntity<?> getRecipe(@PathVariable Integer id) {
               User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ResponseMessage res = recipeServiceM.getRecipe(id, user);
        return ResponseEntity.status(res.getStatus() ? 200 : 400).body(res);
    }

    @GetMapping("/link/{url}")
    public ResponseEntity<?> getSharLink(@PathVariable(name = "url") String url) {
        Integer recipeId = recipeServiceM.checkRecipeLink(url);
        URI redirectUri = URI.create("/recipeM/" + recipeId);

        return ResponseEntity.status(HttpStatus.SEE_OTHER).location(redirectUri).build();
    }
    @PreAuthorize("hasRole('ROLE_CHEF')")
    @PutMapping("/edit-only")
    public ResponseEntity<?> updateRecipe(@RequestBody UpdateRecipeDto updateRecipeDto) {
               User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ResponseMessage res = recipeServiceM.updateRecipeOnly(updateRecipeDto, user);
        return ResponseEntity.status(res.getStatus() ? 200 : 400).body(res);
    }
    @PreAuthorize("hasRole('ROLE_CHEF')")
    @PutMapping("/{id}/edit-steps")
    public ResponseEntity<?> updateSteps(@PathVariable("id") Integer recipeId, @RequestBody List<UpdateStepsDto> updateStepsDto) {
               User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ResponseMessage res = recipeServiceM.updateRecipeSteps(updateStepsDto, recipeId, user);
        return ResponseEntity.status(res.getStatus() ? 200 : 400).body(res);
    }
    @PreAuthorize("hasRole('ROLE_CHEF')")
    @PutMapping("/{id}/edit-ingredients")
    public ResponseEntity<?> updateIngredients(@PathVariable("id") Integer recipeId, @RequestBody List<UpdateIngredientDTO> updateIngredientDTOs) {
               User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ResponseMessage res = recipeServiceM.updateIngredients(updateIngredientDTOs, recipeId, user);
        return ResponseEntity.status(res.getStatus() ? 200 : 400).body(res);
    }
    @PreAuthorize("hasRole('ROLE_CHEF')")
    @Operation(summary = "Change photo or Video for recipe ")
    @PutMapping(value = "/{id}/attachments", consumes = {
            MediaType.MULTIPART_FORM_DATA_VALUE
    })
    public ResponseEntity<?> changeAttachments(
            @PathVariable Integer id,
            @Parameter(
                    description = "Select picture on format .jpg or .png and .svg or video on format .mp4, .avi, .mov for video,",
                    content = @Content(mediaType = "multipart/form-data",
                            schema = @Schema(
                                    type = "string",
                                    format = "binary",
                                    example = "image.png, or video.mp4"
                            )))
            @RequestPart(name = "file", required = false) List<MultipartFile> attachments) {
        ResponseMessage res = recipeServiceM.changeAttachments(attachments, id);
        return ResponseEntity.status(res.getStatus() ? 200 : 400).body(res);
    }

    @PreAuthorize("hasRole('ROLE_CHEF')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRecipe(@PathVariable Integer id) {
               User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ResponseMessage res = recipeServiceM.deleteRecipe(id, user);
        return ResponseEntity.status(res.getStatus() ? 200 : 400).body(res);
    }

    @PreAuthorize("hasRole('ROLE_CHEF')")
    @DeleteMapping("/{recipeId}/step/{id}")
    public ResponseEntity<?> deleteStep(@PathVariable Integer recipeId, @PathVariable Integer id) {
       User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ResponseMessage res = recipeServiceM.deleteStep(id, recipeId, user);
        return ResponseEntity.status(res.getStatus() ? 200 : 400).body(res);
    }

    @PreAuthorize("hasRole('ROLE_CHEF')")
    @DeleteMapping("/{recipeId}/ingredient/{id}")
    public ResponseEntity<?> deleteIngredient(@PathVariable Integer recipeId, @PathVariable Integer id) {
       User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ResponseMessage res = recipeServiceM.deleteIngredient(id, recipeId, user);
        return ResponseEntity.status(res.getStatus() ? 200 : 400).body(res);
    }
    @PreAuthorize("hasRole('ROLE_CHEF')")
    @DeleteMapping("/{recipeId}/attachment/{id}")
    public ResponseEntity<?> deleteAttachment(@PathVariable Integer recipeId, @PathVariable String id) {
       User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ResponseMessage res = recipeServiceM.deleteAttachment(id, recipeId, user);
        return ResponseEntity.status(res.getStatus() ? 200 : 400).body(res);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/category")
    public ResponseEntity<?> editCategory(@RequestBody CategoryEditDto categoryDto) {
        ResponseMessage res = recipeServiceM.editCategory(categoryDto);
        return ResponseEntity.status(res.getStatus() ? 200 : 400).body(res);
    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/ingredient")
    public ResponseEntity<?> editIngredient(@RequestBody IngredientEditDto ingredientEditDto) {
        ResponseMessage res = recipeServiceM.editIngredient(ingredientEditDto);
        return ResponseEntity.status(res.getStatus() ? 200 : 400).body(res);
    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/category/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable Integer id) {
        ResponseMessage res = recipeServiceM.deleteCategory(id);
        return ResponseEntity.status(res.getStatus()?200:400).body(res);
    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/ingredient/{id}")
    public ResponseEntity<?> deleteIngredient(@PathVariable Integer id) {
        ResponseMessage res = recipeServiceM.deleteIngredientAdmin(id);
        return ResponseEntity.status(res.getStatus()?200:400).body(res);
    }


    @GetMapping("/{id}/steps")
    public ResponseEntity<?> getSteps(@PathVariable Integer id) {
        ResponseMessage res = recipeServiceM.getSteps(id);
        return ResponseEntity.status(res.getStatus()?200:400).body(res);
    }


    @GetMapping("/get-all-recipes")
    public ResponseEntity<?> getAllRecipes() {
        ResponseMessage allRecipes = recipeServiceM.getAllRecipes();
        return ResponseEntity.status(allRecipes.getStatus()?200:400).body(allRecipes);
    }
}