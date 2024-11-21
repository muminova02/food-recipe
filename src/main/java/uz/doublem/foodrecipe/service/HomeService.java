package uz.doublem.foodrecipe.service;

import jakarta.validation.constraints.Email;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;
import uz.doublem.foodrecipe.entity.Attachment;
import uz.doublem.foodrecipe.entity.Recipe;
import uz.doublem.foodrecipe.entity.User;
import uz.doublem.foodrecipe.enums.Role;
import uz.doublem.foodrecipe.payload.HomeDTO;
import uz.doublem.foodrecipe.payload.ResponseHomeRecipeDTO;
import uz.doublem.foodrecipe.payload.ResponseMessage;
import uz.doublem.foodrecipe.repository.CategoryRepository;
import uz.doublem.foodrecipe.repository.ReciepesRepository;
import uz.doublem.foodrecipe.repository.UserRepository;

import java.util.List;

@RequiredArgsConstructor
@Component
public class HomeService {
    private final CategoryRepository categoryRepository;
    private final ReciepesRepository reciepesRepository;
    private final UserRepository userRepository;
    public ResponseMessage homePage(User user){
        String name = user.getName();
        HomeDTO homeDTO = new HomeDTO();
        homeDTO.setName(name);
        homeDTO.setEmail(user.getEmail());
        homeDTO.setAttachment(user.getImageUrl());
        homeDTO.setCategories(categoryRepository.findAll());
        return ResponseMessage.builder().data(homeDTO).status(true).build();
    }
    public ResponseMessage homePageOauth2(String email, OAuth2User principal){
        User user = userRepository.findByEmail(email).orElseGet(() -> {
            User newUser = new User();
            newUser.setEmail(email);
            newUser.setName((String) principal.getAttributes().get("name"));
            newUser.setRole(Role.USER);
            newUser.setVerified(true);
            newUser.setImageUrl((String) principal.getAttributes().get("picture"));
            userRepository.save(newUser);
            return newUser;
        });
        HomeDTO homeDTO = new HomeDTO();
        homeDTO.setName(user.getName());
        homeDTO.setEmail(user.getEmail());
        homeDTO.setAttachment(user.getImageUrl());
        homeDTO.setCategories(categoryRepository.findAll());

  return       ResponseMessage.builder().data(homeDTO).status(true).build();
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
