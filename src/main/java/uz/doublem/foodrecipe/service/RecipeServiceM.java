package uz.doublem.foodrecipe.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import uz.doublem.foodrecipe.entity.*;
import uz.doublem.foodrecipe.payload.*;
import uz.doublem.foodrecipe.repository.*;
import uz.doublem.foodrecipe.util.Util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import static uz.doublem.foodrecipe.util.Util.*;

import static java.util.Collections.*;


@Service
@RequiredArgsConstructor
public class RecipeServiceM {

    private final ObjectMapper objectMapper;
    private final CategoryRepository categoryRepository;
    private final AttachmentService attachmentService;
    private final RecipeRepositoryM recipeRepositoryM;
    private final IngredientRepository ingredientRepository;
    private final IngredientAndQuantityRepository ingreAndQuanRepo;
    private final StepRepository stepRepository;



    public ResponseMessage  addRecipe(String json, List<MultipartFile> attachments, User currentUser) {
            ResponseMessage response = new ResponseMessage();
            try {
                if (attachments.isEmpty() || attachments.size() < 2) {
                    return ResponseMessage.builder().status(false)
                            .text("1 ta Rasim va 1 ta Video yuboring")
                            .data(new RuntimeException("wrong with attachment")).build();
                }

                RecipeDTOAdd recipeDTO = objectMapper.readValue(json, RecipeDTOAdd.class);

                Recipe recipe = Recipe.builder()
                        .title(recipeDTO.getTitle())
                        .description(recipeDTO.getDescription())
                        .author(currentUser)
                        .averageRating(1.)
                        .cookingTime(recipeDTO.getCookingTime())
                        .build();

                Optional<Category> optionCategory = categoryRepository.findById(recipeDTO.getCategory_id());
                if (optionCategory.isEmpty()) {
                    return ResponseMessage.builder()
                            .status(false)
                            .text("Category topilmadi")
                            .data(recipeDTO.getCategory_id()).build();
                }
                recipe.setCategory(optionCategory.get());
                recipeRepositoryM.save(recipe);

                addAttachmentsToRecipe(attachments, recipe);

                if (!recipeDTO.getIngredientList().isEmpty()) {
                    if (saveIngredientsList(recipeDTO.getIngredientList(), recipe)) {
                        response.setText(response.getText() + ", Step 3 >> Ingredient added ");
                    }
                }
                if (!recipeDTO.getStepsList().isEmpty()) {
                    if (saveStepsList(recipe, recipeDTO.getStepsList())) {
                        response.setText(response.getText() + ", Step 4 >> Steps added");
                    }else {
                        response.setText(response.getText() + ", Step 4 >> Steps not add");
                    }
                }

                response.setText(response.getText() + " >> Finally successfully saved");
                response.setStatus(true);
                response.setData(recipeDTO);
                return response;

            } catch (Exception e) {
                throw new RuntimeException("Exception during recipe processing", e);
            }
        }

        private void addAttachmentsToRecipe(List<MultipartFile> attachments, Recipe recipe) {
            Attachment attachment = attachmentService.save(attachments.get(0));
            Attachment attachment2 = attachmentService.save(attachments.get(1));

            if (attachment.getType().startsWith("image")) {
                recipe.setImageUrl(attachment.getUrl());
                recipe.setVideoUrl(attachment2.getUrl());
            } else {
                recipe.setVideoUrl(attachment.getUrl());
                recipe.setImageUrl(attachment2.getUrl());
            }
            recipeRepositoryM.save(recipe);
        }


    private boolean saveStepsList(Recipe saveRecipe2, List<StepsDTOAdd> stepDTOList) {
        List<Step> stepsList = new ArrayList<>();
        stepDTOList.forEach(step -> {
            Step step1 = new Step();
            step1.setDescription(step.getText());
            step1.setStep_number(step.getStep_number());
            step1.setRecipe(saveRecipe2);
            stepsList.add(step1);
        });
        stepRepository.saveAll(stepsList);
        saveRecipe2.setSteps(stepsList);
        recipeRepositoryM.save(saveRecipe2);
        return true;
    }

    private boolean saveIngredientsList(List<IngredientDTOAdd> ingredienTDTOList, Recipe saveRecipe2) {
        List<IngredientAndQuantity> ingredientAndQuantityList = new ArrayList<>();
        for (IngredientDTOAdd ingredientDTOAdd : ingredienTDTOList) {
            Integer ingredientId = ingredientDTOAdd.getIngredientId();
            Optional<Ingredient> ingredient = ingredientRepository.findById(ingredientId);
            if (ingredient.isPresent()) {
                IngredientAndQuantity ingredientAndQuantity = IngredientAndQuantity
                        .builder()
                        .ingredient(ingredient.get())
                        .quantity(ingredientDTOAdd.getIngredientQuantity())
                        .recipe(saveRecipe2)
                        .build();
                ingredientAndQuantityList.add(ingredientAndQuantity);
            }
        }
        ingreAndQuanRepo.saveAll(ingredientAndQuantityList);
        saveRecipe2.setIngredientAndQuantities(ingredientAndQuantityList);
        recipeRepositoryM.save(saveRecipe2);
        return true;
    }


    public ResponseMessage addCategoryList(List<CategoryDto> categoryDtoList) {
        ResponseMessage response = new ResponseMessage();
        if (categoryDtoList ==null || categoryDtoList.isEmpty()){
            response.setText("Category not exist");
            response.setStatus(false);
            response.setData(HttpStatus.NO_CONTENT);
            return response;
        }
        List<Category> list = categoryDtoList.stream().map(categoryDto -> {
                    Category category = new Category();
                    category.setName(categoryDto.getCategoryName());
                    categoryRepository.save(category);
                    return category;
                }
        ).toList();
        response.setStatus(true);
        response.setData(list);
        response.setText("Category added");

        return response;
    }

    public ResponseMessage addIngredientList(String json, List<MultipartFile> attachments) {
        try {
            if (attachments== null || attachments.isEmpty()) {
                throw new RuntimeException("Ingredient Icon bo'lishi kerak");
            }
            IngredientDTO ingredientDTOList = objectMapper.readValue(json, IngredientDTO.class);
            if (ingredientDTOList == null ||
                    ingredientDTOList.getIngredientsName() == null ||
                    ingredientDTOList.getIngredientsName().isEmpty() || ingredientDTOList.getIngredientsName().size()!=attachments.size()) {
                throw new RuntimeException("ingredient null bo'lmasligi kerak, icon va ingredientlar soni teng bo'lishi kerak");
            }
            ResponseMessage response = new ResponseMessage();
            List<String> iconUrlList = new ArrayList<>();
            attachments.forEach(multipartFile -> {
                if (multipartFile == null) {
                    throw new RuntimeException("IMAGE NOT NULL");
                }
                Attachment icon = attachmentService.save(multipartFile);
                iconUrlList.add(icon.getUrl());
            });
            for (int i = 0; i < ingredientDTOList.getIngredientsName().size(); i++) {
                Ingredient ingredient = new Ingredient();
                ingredient.setName(ingredientDTOList.getIngredientsName().get(i));
                ingredient.setIcon(iconUrlList.get(i));
                ingredientRepository.save(ingredient);
            }
            response.setStatus(true);
            response.setText("ingredientlar saqlandi");
            response.setData(ingredientDTOList.getIngredientsName());
            return response;
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Ingredient json Mapping Exception",e);
        }
    }

    public ResponseMessage upload(List<MultipartFile> attachments,Integer recipeId) {

        if (attachments.isEmpty() || attachments.size() < 2) {
            return ResponseMessage.builder().status(false)
                    .text("1 ta Rasim va 1 ta Video yuboring")
                    .data(new RuntimeException("wrong with attachment")).build();
        }
        Optional<Recipe> byId = recipeRepositoryM.findById(recipeId);
        if (byId.isPresent()) {
            addAttachmentsToRecipe(attachments,byId.get());
        }else {
            ResponseMessage.builder().status(false).text("recipe not found").data(recipeId).build();
        }

        return ResponseMessage.builder().text("attachments added to Recipe").status(true).data(attachments).build();
    }

    public ResponseMessage addStepsToRecipe(List<StepsDTOAdd> stepsDTOAdds, Integer recipeId) {
        var byId = recipeRepositoryM.findById(recipeId);
        if (byId.isPresent()) {
            if (!saveStepsList(byId.get(),stepsDTOAdds)) {
               return getResponseMes(false,"steps not added to recipe",stepsDTOAdds);

            }
        }else {
            ResponseMessage.builder().status(false).text("recipe not found").data(recipeId).build();
        }
        return getResponseMes(true,"steps add to recipe",stepsDTOAdds);

    }

    public ResponseMessage addIngredientAndQuantityListToRecipe(List<IngredientDTOAdd> ingredientDTOAdds, Integer recipeId) {
        Optional<Recipe> byId = recipeRepositoryM.findById(recipeId);
        if (byId.isPresent()) {
            if (!saveIngredientsList(ingredientDTOAdds,byId.get())) {
                return getResponseMes(false,"ingredient not added to Recipe",ingredientDTOAdds);
            }
        }else {
            ResponseMessage.builder().status(false).text("recipe not found").data(recipeId).build();
        }
        return getResponseMes(true,"ingredients add to recipe",ingredientDTOAdds);
    }



}
