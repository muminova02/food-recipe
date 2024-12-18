package uz.doublem.foodrecipe.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import uz.doublem.foodrecipe.entity.*;
import uz.doublem.foodrecipe.payload.*;
import uz.doublem.foodrecipe.repository.*;

import java.util.*;

import static uz.doublem.foodrecipe.util.Util.getResponseMes;

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
    private final NotificationService notificationService;
    private final ViewRepository viewRepository;
    private final UserRepository userRepository;
    private final SavedRecipesRepository savedRecipesRepository;

    @Value("${server.base-url}")
    private String baseUrl;

    public ResponseMessage  addRecipe(String json, MultipartFile attachments, User currentUser) {
            ResponseMessage response = new ResponseMessage();
            try {
//                if (attachments.isEmpty() || attachments.size() < 2) {
//                    return ResponseMessage.builder().status(false)
//                            .text("1 ta Rasim va 1 ta Video yuboring")
//                            .data(new RuntimeException("wrong with attachment")).build();
//                }

                RecipeDTOAdd recipeDTO = objectMapper.readValue(json, RecipeDTOAdd.class);
//                if (currentUser.getLocation() == null){
//                    return getResponseMes(false,"First, specify your location. To do this, you can go to the edit user section ",currentUser);
//                }
                Recipe recipe = new Recipe();
                boolean b = saveRecipeOnly(recipeDTO, recipe, currentUser);
                if (!b) {
                    return ResponseMessage.builder()
                            .status(false)
                            .text("Category topilmadi")
                            .data(recipeDTO.getCategory_id()).build();
                }
                response.setText("Recipe SAVED IN STEP 1");
                addAttachmentsToRecipe(attachments, recipe);
                if (recipe.getImageUrl()==null){
                    response.setText(response.getText() + ", Step 2 >> Attachment added");
                }
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
                try {
                    notificationService.createNotification(recipe, currentUser);
                    notificationService.createNotificationTwo(recipe, currentUser);
                }catch (Exception e){
                    System.out.println(e.getMessage());
                }
                response.setText(response.getText() + " >> Finally successfully saved");
                response.setStatus(true);
                response.setData(recipe.getId());
                return response;

            } catch (Exception e) {
                throw new RuntimeException("Exception during recipe processing", e);
            }
        }


        private boolean saveRecipeOnly(RecipeDTOAdd recipeDTO, Recipe recipe, User currentUser){
            recipe.setTitle(recipeDTO.getTitle());
            recipe.setDescription(recipeDTO.getDescription());
            recipe.setAuthor(currentUser);
            recipe.setAverageRating(1.);
            recipe.setCookingTime(recipeDTO.getCookingTime());
            recipe.setViewsCount(0L);
            Optional<Category> optionCategory = categoryRepository.findById(recipeDTO.getCategory_id());
            if (optionCategory.isEmpty()) {
                return false;
            }
            recipe.setCategory(optionCategory.get());
            recipeRepositoryM.save(recipe);
            String title = recipe.getTitle();
            Integer id = recipe.getId();
            String fullTitle = title + "_&" + id;
            String link = baseUrl+"/recipeM/link/" + fullTitle;
            recipe.setLink(link);
            recipe.setVideoUrl(recipeDTO.getVideoUrl());
            recipeRepositoryM.save(recipe);
            return true;
        }


//        private void addAttachmentsToRecipe(List<MultipartFile> attachments, Recipe recipe) {
//            Attachment attachment = attachmentService.save(attachments.get(0));
//            Attachment attachment2 = attachmentService.save(attachments.get(1));
//
//            if (attachment.getType().startsWith("image")) {
//                recipe.setImageUrl(attachment.getUrl());
//                recipe.setVideoUrl(attachment2.getUrl());
//            } else {
//                recipe.setVideoUrl(attachment.getUrl());
//                recipe.setImageUrl(attachment2.getUrl());
//            }
//            recipeRepositoryM.save(recipe);
//        }

    private void addAttachmentsToRecipe(MultipartFile attachments, Recipe recipe) {
        Attachment attachment = attachmentService.save(attachments);
        recipe.setImageUrl(attachment.getUrl());
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

    public ResponseMessage upload(MultipartFile attachments,Integer recipeId) {

//        if (attachments.isEmpty() || attachments.size() < 2) {
//            return ResponseMessage.builder().status(false)
//                    .text("1 ta Rasim va 1 ta Video yuboring")
//                    .data(new RuntimeException("wrong with attachment")).build();
//        }
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
    public ResponseMessage addRecipeOnly(RecipeDTOaddOnly recipeDTOaddOnly,User user) {
        if (user.getLocation() == null){
            throw new RuntimeException("First, specify your location. To do this, you can go to the edit user section ");
        }
        Recipe recipe = new Recipe();
        RecipeDTOAdd recipeDTOAdd = RecipeDTOAdd.builder()
                .title(recipeDTOaddOnly.getTitle())
                .description(recipeDTOaddOnly.getDescription())
                .cookingTime(recipeDTOaddOnly.getCookingTime())
                .category_id(recipeDTOaddOnly.getCategory_id())
                .build();

        boolean b = saveRecipeOnly(recipeDTOAdd, recipe, user);
        if (b) {
            return getResponseMes(true,"recipe add successfully",recipeDTOaddOnly);
        }
        return getResponseMes(false,"recipe not added",recipeDTOaddOnly);
    }

    public ResponseMessage getRecipe(Integer id, User user) {
        Optional<Recipe> byId = recipeRepositoryM.findById(id);
        if (byId.isEmpty()) {
            return getResponseMes(false,"recipe not found",id);
        }
        Recipe recipe = byId.get();
        Boolean isSaved= savedRecipesRepository.existsByRecipe_IdAndOwner_Id(id,user.getId());
        Boolean isFollow;
        User author = recipe.getAuthor();
        if (user.getId().equals(author.getId())) {
            isFollow = null;
        }else {
            isFollow = userRepository.existsByUserIdAndFollowerId(user.getId(), author.getId());
            isFollow = isFollow != null && isFollow;
        }
        Location location;
        String locationString = null;
        if (author.getLocation()!=null){
            location = author.getLocation();
            locationString = location.getCountry() +", " + location.getCity();
        }
        RecipeResponceDTO build = RecipeResponceDTO.builder()
                    .id(recipe.getId())
                    .title(recipe.getTitle())
                    .description(recipe.getDescription())
                    .imageUrl(recipe.getImageUrl())
                    .videoUrl(recipe.getVideoUrl())
                    .averageRating(recipe.getAverageRating())
                    .viewCount(recipe.getViewsCount())
                    .isSaved(isSaved)
                    .isFollow(isFollow)
                    .author(author.getName())
                    .authorId(author.getId())
                    .authorLocation(locationString)
                    .authorImageUrl(author.getImageUrl()!=null? author.getImageUrl():null)
                    .cookingTime(recipe.getCookingTime())
                    .build();
        incrementCountView(recipe,user);
        return getResponseMes(true,"get recipe successfully",build);
    }

    private void incrementCountView(Recipe recipe, User user) {
        if (!viewRepository.existsByUser_IdAndRecipe_Id(user.getId(),recipe.getId())){
            View view = new View();
            view.setRecipe(recipe);
            view.setUser(user);
            viewRepository.save(view);
            recipe.setViewsCount(recipe.getViewsCount()+1);
            recipeRepositoryM.save(recipe);
        }
    }

    public Integer checkRecipeLink(String url) {
        String[] split = url.split("_&");
        try {
            Integer recipeId = Integer.valueOf(split[1]);
            Optional<Recipe> byId = recipeRepositoryM.findById(recipeId);
            if (byId.isPresent()) {
                Recipe recipe = byId.get();
                if (recipe.getTitle().equals(split[0])) {
                    return recipeId;
                }else {
                    return -1;
                }
            }
        }catch (Exception e) {
            return -1;
        }
        return -1;
    }

    public ResponseMessage updateRecipeOnly(UpdateRecipeDto updateRecipeDto, User user) {
        updateRecipeDto.setUserId(user.getId());
        if (updateRecipeDto.getRecipeId()==null||user.getId()==null) {
            return getResponseMes(false,"recipe or user should not be null",updateRecipeDto);
        }
        Optional<Recipe> recipeOptional = recipeRepositoryM.findById(updateRecipeDto.getRecipeId());
        if (recipeOptional.isEmpty()) {
            return getResponseMes(false,"recipe not found with this id : ",updateRecipeDto);
        }
        Recipe recipe = recipeOptional.get();
        if (!recipe.getAuthor().getId().equals(user.getId())) {
            return getResponseMes(false,"this user have not authorization change this recipe",updateRecipeDto);
        }
        if (updateRecipeDto.getRecipeName()!=null) {
            recipe.setTitle(updateRecipeDto.getRecipeName());
        }
        if (updateRecipeDto.getRecipeDescription()!=null) {
            recipe.setDescription(updateRecipeDto.getRecipeDescription());
        }
        if (updateRecipeDto.getCookingTime()!=null) {
            recipe.setCookingTime(updateRecipeDto.getCookingTime());
        }
        if (updateRecipeDto.getVideoUrl()!=null) {
            recipe.setVideoUrl(updateRecipeDto.getVideoUrl());
        }
        recipeRepositoryM.save(recipe);
        return getResponseMes(true,"successfully updated recipe ",updateRecipeDto);
    }

    public ResponseMessage updateRecipeSteps(List<UpdateStepsDto> updateStepsDto, Integer recipeId, User user) {
        if (!recipeRepositoryM.isRecipeOwnedByUser(recipeId, user.getId())) {
            return getResponseMes(false,"this user have not authorization change this recipe", recipeId);
        }
        updateStepsDto.forEach(stepsDto -> {
            Optional<Step> byId = stepRepository.findById(stepsDto.getStepId());
            if (byId.isPresent()) {
                Step step = byId.get();
                step.setDescription(stepsDto.getDescription());
                stepRepository.save(step);
            }
            else {
                throw new RuntimeException("step not found with this id : " + stepsDto.getStepId());
            }
        });
        return getResponseMes(true,"successfully updated steps ",HttpStatus.RESET_CONTENT);
    }

    public ResponseMessage updateIngredients(List<UpdateIngredientDTO> updateIngredientDTOs, Integer recipeId, User user) {
        if (!recipeRepositoryM.isRecipeOwnedByUser(recipeId, user.getId())) {
            return getResponseMes(false,"this user have not authorization change this recipe", recipeId);
        }
        updateIngredientDTOs.forEach(updateIngredientDTO -> {
            check(updateIngredientDTO);
            Optional<IngredientAndQuantity> byId = ingreAndQuanRepo.findById(updateIngredientDTO.getIngredientAndQuantityId());
            if (byId.isPresent()) {
                IngredientAndQuantity ingredientAndQuantity = byId.get();
                Optional<Ingredient> byId1 = ingredientRepository.findById(updateIngredientDTO.getIngredientId()!=null?updateIngredientDTO.getIngredientId():-1);
                byId1.ifPresent(ingredientAndQuantity::setIngredient);
                ingredientAndQuantity.setQuantity(updateIngredientDTO.getQuantity());
                ingreAndQuanRepo.save(ingredientAndQuantity);
            }
        });
        return getResponseMes(true,"successfully updated ingredients ",HttpStatus.RESET_CONTENT);
    }

    private void check(Object o){
        if(o==null){
            throw new RuntimeException("object is null" + o.getClass().getName());
        }
    }

    public ResponseMessage changeAttachments(List<MultipartFile> attachments, Integer id) {
        Optional<Recipe> byId = recipeRepositoryM.findById(id);
        if (byId.isEmpty()) {
            return getResponseMes(false,"recipe not found with this id : ",id);
        }
        Recipe recipe = byId.get();
        attachments.forEach(multipartFile -> {
            String attachmentUrl;
            if (Objects.requireNonNull(multipartFile.getContentType()).startsWith("image")) {
                attachmentUrl = recipe.getImageUrl();
            }else {
                attachmentUrl = recipe.getVideoUrl();
            }
            String[] split = attachmentUrl.split("/");
            String imageId = split[split.length - 1];
            attachmentService.update(imageId,multipartFile);
        });
        return getResponseMes(true,"successfully changed attachment ",HttpStatus.RESET_CONTENT);
    }

    @Transactional
    public ResponseMessage deleteRecipe(Integer id, User user) {
        if (!recipeRepositoryM.isRecipeOwnedByUser(id,user.getId())) {
            return getResponseMes(false,"you have not authorization delete this recipe", id);
        }
        recipeRepositoryM.deleteById(id);
        return getResponseMes(true,"recipe delete successfully",id);
    }

    @Transactional
    public ResponseMessage deleteStep(Integer id, Integer recipeId, User user) {
        if (!recipeRepositoryM.isRecipeOwnedByUser(recipeId,user.getId())) {
            return getResponseMes(false,"you have not authorization delete this recipe", recipeId);
        }
        stepRepository.deleteById(id);
        return getResponseMes(true,"ingredient delete successfully",id);
    }

    @Transactional
    public ResponseMessage deleteIngredient(Integer id, Integer recipeId, User user) {
        if (!recipeRepositoryM.isRecipeOwnedByUser(recipeId,user.getId())) {
            return getResponseMes(false,"you have not authorization delete this recipe", recipeId);
        }
        ingreAndQuanRepo.deleteById(id);
        return getResponseMes(true,"ingredient delete successfully",id);
    }

    @Transactional
    public ResponseMessage deleteAttachment(String id, Integer recipeId, User user) {
        if (!recipeRepositoryM.isRecipeOwnedByUser(recipeId,user.getId())) {
            return getResponseMes(false,"you have not authorization delete this recipe", recipeId);
        }

        Optional<Recipe> byId = recipeRepositoryM.findById(recipeId);
        Recipe recipe = byId.get();
        String[] split = recipe.getImageUrl().split("/");
        String idUrl = split[split.length - 1];
        if (idUrl.equals(id)) {
            attachmentService.delete(id);
            recipe.setImageUrl(null);
        }
        String[] videoUrl = recipe.getVideoUrl().split("/");
        String idVUrl = videoUrl[videoUrl.length - 1];
        if (idVUrl.equals(id)) {
            attachmentService.delete(id);
            recipe.setVideoUrl(null);
        }
        recipeRepositoryM.save(recipe);
        return getResponseMes(true,"recipe attachment delete successfully",recipeId);
    }

    public ResponseMessage editCategory(CategoryEditDto categoryDto) {
        categoryRepository.updateName(categoryDto.getId(),categoryDto.getName());
        return getResponseMes(true,"update name successfully",categoryDto);
    }

    public ResponseMessage editIngredient(IngredientEditDto ingredientEditDto) {
        ingredientRepository.updateName(ingredientEditDto.getIngredientId(),ingredientEditDto.getIngredientName());
        return getResponseMes(true,"update ingredient successfully",ingredientEditDto);
    }


    @Transactional
    public ResponseMessage deleteCategory(Integer id) {
        categoryRepository.deleteById(id);
        return getResponseMes(true,"deleted successfully",id);
    }

    @Transactional
    public ResponseMessage deleteIngredientAdmin(Integer id) {
        ingredientRepository.deleteById(id);
        return getResponseMes(true,"deleted successfully",id);
    }

    public ResponseMessage getSteps(Integer id) {
        if (!recipeRepositoryM.existsById(id)) {
            return getResponseMes(false,"recipe not found with this id : ",id);
        }
        List<Step> steps = stepRepository.findByRecipe_Id(id);
        if (steps.isEmpty()) {
            return getResponseMes(true,"recipe have not yet steps ", Collections.emptyList());
        }
        List<StepResponseDto> stepDto = steps.stream().map(step ->
                StepResponseDto.builder()
                        .stepId(step.getId())
                        .stepNumber(step.getStep_number())
                        .text(step.getDescription())
                        .recipeId(id)
                        .build()
        ).toList();
        return getResponseMes(true,"steps list",stepDto);
    }

    public ResponseMessage getAllRecipes() {
        List<Recipe> all = recipeRepositoryM.findAll();

        System.out.println(all.size());

        if(all.isEmpty()){
            return getResponseMes(true,"recipes does not exist ",Collections.emptyList());
        }
        List<RecipeDtoAdminShow> recipes = all.stream().map(allRecipes ->
                RecipeDtoAdminShow.builder()
                        .link(allRecipes.getLink())
                        .category(allRecipes.getCategory())
                        .createdAt(allRecipes.getCreatedAt())
                        .viewsCount(allRecipes.getViewsCount())
                        .title(allRecipes.getTitle())
                        .imageUrl(allRecipes.getImageUrl())
                        .videoUrl(allRecipes.getVideoUrl())
                        .author(allRecipes.getAuthor().getName())
                        .cookingTime(allRecipes.getCookingTime())
                        .description(allRecipes.getDescription())
                        .averageRating(allRecipes.getAverageRating())
                        .id(allRecipes.getId())
                        .build()
        ).toList();


        return getResponseMes(true," all recipes",recipes);
    }

}
