package uz.doublem.foodrecipe.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import uz.doublem.foodrecipe.entity.Recipe;
import uz.doublem.foodrecipe.entity.User;
import uz.doublem.foodrecipe.payload.*;
import uz.doublem.foodrecipe.repository.UserRepository;
import uz.doublem.foodrecipe.service.RecipeServiceM;
import uz.doublem.foodrecipe.service.StepsService;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@Tag(name = "Recipe >> ")
@RequestMapping("/api/recipeM")
public class RecipeControllerM {

    private final RecipeServiceM recipeServiceM;
    private final StepsService stepsService;
    private final UserRepository userRepository;

    @Operation(summary = "Recipe qo'shish")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "201",
                    description = "If successfully created you get  status '201'",
                    content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseMessage.class))
                    }
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400",
                    description = "If get 400 status Please read response 'message'",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseMessage.class)))
    })


    @PostMapping(value = "/addOne",consumes = {"multipart/form-data"})
    private ResponseEntity<?> addRecipe(
        @Parameter(
                name = "json",
                description = "Recipe details in JSON format (excluding photo) ",
                required = true,
                schema = @Schema(implementation = RecipeDTOAdd.class, format = "json", type = "string")
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
        @RequestPart(name = "file", required = false) List<MultipartFile> attachments){
//        User curentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<User> byId = userRepository.findById(2L);

        ResponseMessage res = recipeServiceM.addRecipe(json,attachments,byId.get());
        return ResponseEntity.status(res.getStatus()?201:400).body(res);

    }

    @PostMapping("/category")
    private ResponseEntity<?> addCategoryList(@RequestBody List<CategoryDto> categoryDtoList){
       ResponseMessage res =  recipeServiceM.addCategoryList(categoryDtoList);
        return ResponseEntity.status(res.getStatus()?201:400).body(res);
    }

    @PostMapping(value = "/ingredient",consumes = {"multipart/form-data"})
    private ResponseEntity<?> addIngredientList(
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
            @RequestPart(name = "file") List<MultipartFile> attachments){
        ResponseMessage res =  recipeServiceM.addIngredientList(json,attachments);
        return ResponseEntity.status(res.getStatus()?201:400).body(res);
    }


    @Operation(summary = "Upload photo and Video to recipe ")
    @PostMapping(value = "/{id}/attachments",consumes = {
            MediaType.MULTIPART_FORM_DATA_VALUE
    })
    public ResponseEntity<?> upload(
            @PathVariable Integer id,
            @Parameter(
                    description = "Select picture on format .jpg or .png and .svg or video on format .mp4, .avi, .mov for video,",
                    content = @Content(mediaType = "multipart/form-data",
                            schema = @Schema(
                                    type = "string",
                                    format = "binary",
                                    example = "image.png, or video.mp4"
                            )))
            @RequestPart(name = "file", required = false) List<MultipartFile> attachments)
    {
        ResponseMessage res = recipeServiceM.upload(attachments,id);
        return ResponseEntity.status(res.getStatus()?200:400).body(res);
    }


    @PostMapping("/{id}/steps")
    private ResponseEntity<?> addStepsList(
            @PathVariable Integer id,
            @RequestBody List<StepsDTOAdd> stepsDTOAdds){
        ResponseMessage res =  recipeServiceM.addStepsToRecipe(stepsDTOAdds,id);
        return ResponseEntity.status(res.getStatus()?201:400).body(res);
    }


    @PostMapping("/{id}/ingredients")
    private ResponseEntity<?> addIngredientListToRecipe(
            @PathVariable Integer id,
            @RequestBody List<IngredientDTOAdd> ingredientDTOAdds){
        ResponseMessage res =  recipeServiceM.addIngredientAndQuantityListToRecipe(ingredientDTOAdds,id);
        return ResponseEntity.status(res.getStatus()?201:400).body(res);
    }




}
