package uz.doublem.foodrecipe.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import uz.doublem.foodrecipe.entity.Attachment;
import uz.doublem.foodrecipe.entity.Recipe;
import uz.doublem.foodrecipe.entity.User;
import uz.doublem.foodrecipe.payload.HomeDTO;
import uz.doublem.foodrecipe.payload.ResponseHomeRecipeDTO;
import uz.doublem.foodrecipe.payload.ResponseMessage;
import uz.doublem.foodrecipe.repository.CategoryRepository;
import uz.doublem.foodrecipe.repository.ReciepesRepository;

import java.util.List;

@RequiredArgsConstructor
@Component
public class HomeService {
    private final CategoryRepository categoryRepository;
    private final ReciepesRepository reciepesRepository;
    public ResponseMessage homePage(User user){
        String name = user.getName();
        HomeDTO homeDTO = new HomeDTO();
        homeDTO.setName(name);
        homeDTO.setEmail(user.getEmail());
        homeDTO.setAttachment(user.getImageUrl());
        homeDTO.setCategories(categoryRepository.findAll());
        return ResponseMessage.builder().data(homeDTO).status(true).build();
    }

    public ResponseMessage getRecipesByCategoryId(Integer id,Integer size, Integer page){
        PageRequest pageRequest = PageRequest.of(page,size);
        Page<Recipe> byCategoryId = reciepesRepository.findByCategoryId(id, pageRequest);
        List<ResponseHomeRecipeDTO> list = byCategoryId.stream().map(r -> ResponseHomeRecipeDTO.builder().id(r.getId())
                .title(r.getTitle())
                .imgUrl(r.getImageUrl())
                .cookingTime(r.getCookingTime())
                .averageRating(r.getAverageRating())
                .build()).toList();
        return ResponseMessage.builder().text("recipes by category id").status(true).data(list).build();
    }

    public ResponseMessage getNewRecipes(Integer size,Integer page){
        PageRequest pageRequest = PageRequest.of(page,size);
        Page<Recipe> newRecipes = reciepesRepository.findByOrderByCreatedAtAscAverageRatingDesc(pageRequest);
        List<ResponseHomeRecipeDTO> list = newRecipes.stream().map(r -> ResponseHomeRecipeDTO.builder().id(r.getId())
                .title(r.getTitle())
                .imgUrl(r.getImageUrl())
                .cookingTime(r.getCookingTime())
                .averageRating(r.getAverageRating())
                .build()).toList();
        return ResponseMessage.builder().data(list).status(true).text("new recipes ordered").build();
    }

}
