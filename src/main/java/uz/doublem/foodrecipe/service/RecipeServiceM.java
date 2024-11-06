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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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



    public ResponseMessage  addRecipe(String json, List<MultipartFile> attachments, User curentUser) {
        ResponseMessage response = new ResponseMessage();
        try {
            if (attachments.isEmpty() || attachments.get(0) == null && attachments.get(1) == null && attachments.size() < 3) {
                return ResponseMessage.builder().status(false).text("faqat 1 ta Rasim va 1 ta Video yuboring").data(new RuntimeException("wrong with attachment")).build();
            }

            RecipeDTOAdd recipeDTO = objectMapper.readValue(json, RecipeDTOAdd.class);

            Recipe recipe = new Recipe();
            recipe.setDescription(recipeDTO.getDescription());
            recipe.setTitle(recipeDTO.getTitle());
            recipe.setAuthor(curentUser);
            recipe.setAverageRating(1);
            recipe.setCookingTime(recipeDTO.getCookingTime());
            Recipe saveRecipe = recipeRepositoryM.save(recipe);
            response.setText("Recipe SAVED IN STEP 1");
            Optional<Category> optionCategory = categoryRepository.findById(recipeDTO.getCategory_id());
            if (optionCategory.isEmpty()) {
                response.setStatus(false);
                response.setText(response.getText() + "Id ga mos category topilmadi");
                response.setData(recipeDTO.getCategory_id());
                return response;
            }
            recipe.setCategory(optionCategory.get());
            String title = recipe.getTitle();
            Integer id = recipe.getId();
            String fullTitle = title + "_&" + id;
            String link = "http://localhost:8080/api/recipe/link/" + fullTitle;
            recipe.setLink(link);
            MultipartFile multipartFile1 = attachments.get(0);
            Attachment attachment = attachmentService.save(multipartFile1);

            MultipartFile multipartFile2 = attachments.get(1);
            Attachment attachment2 = attachmentService.save(multipartFile2);
            if (attachment.getType().startsWith("image")) {
                saveRecipe.setImageUrl(attachment.getUrl());
                saveRecipe.setVideoUrl(attachment2.getUrl());
            }else {
                saveRecipe.setVideoUrl(attachment.getUrl());
                saveRecipe.setImageUrl(attachment2.getUrl());
            }
            Recipe saveRecipe2 = recipeRepositoryM.save(saveRecipe);
            response.setText(response.getText() + ", Step 2 >> Attachment VIDEO AND Audio added");

            if (!recipeDTO.getIngredientList().isEmpty()) {
               saveRecipe2 = saveIngredientsList(recipeDTO.getIngredientList(), saveRecipe2, response);
            }

            if (!recipeDTO.getStepsList().isEmpty()) {
                saveRecipe2 = saveStepsList(saveRecipe2, recipeDTO.getStepsList(), response);
            }

            response.setText(response.getText() + " , Finally successfully saved");
            response.setStatus(true);
            response.setData(recipeDTO);
            return response;

        } catch (JsonMappingException e) {
            throw new RuntimeException("Json Mapping Exception",e);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("json Processing Exception",e);
        }

    }

    private Recipe saveStepsList(Recipe saveRecipe2, List<StepsDTOAdd> stepDTOList, ResponseMessage response) {
        List<Step> stepsList = new ArrayList<>();
        stepDTOList.forEach(step -> {
            Step step1 = new Step();
            step1.setDescription(step.getText());
            step1.setStep_number(step.getStep_number());
            step1.setRecipe(saveRecipe2);
            stepRepository.save(step1);
            stepsList.add(step1);
        });
        saveRecipe2.setSteps(stepsList);
        response.setText(response.getText() + ", Step 4 >> Steps added");
        return recipeRepositoryM.save(saveRecipe2);
    }

    private Recipe saveIngredientsList(List<IngredientDTOAdd> ingredienTDTOList, Recipe saveRecipe2, ResponseMessage response) {
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
                ingreAndQuanRepo.save(ingredientAndQuantity);
                ingredientAndQuantityList.add(ingredientAndQuantity);
            }
        }
        saveRecipe2.setIngredientAndQuantities(ingredientAndQuantityList);
        response.setText(response.getText() + ", Step 3 >> Ingredient added ");
        return recipeRepositoryM.save(saveRecipe2);
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
}
