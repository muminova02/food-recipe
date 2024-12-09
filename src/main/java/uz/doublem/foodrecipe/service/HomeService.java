package uz.doublem.foodrecipe.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import uz.doublem.foodrecipe.config.JwtProvider;
import uz.doublem.foodrecipe.entity.Recipe;
import uz.doublem.foodrecipe.entity.User;
import uz.doublem.foodrecipe.enums.Role;
import uz.doublem.foodrecipe.payload.HomeDTO;
import uz.doublem.foodrecipe.payload.ResponseHomeRecipeDTO;
import uz.doublem.foodrecipe.payload.ResponseHomeRecipeDTO2;
import uz.doublem.foodrecipe.payload.ResponseMessage;
import uz.doublem.foodrecipe.repository.CategoryRepository;
import uz.doublem.foodrecipe.repository.ReciepesRepository;
import uz.doublem.foodrecipe.repository.UserRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class HomeService {
    private final CategoryRepository categoryRepository;
    private final ReciepesRepository reciepesRepository;
    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;
    public ResponseMessage homePage(User user){
        String name = user.getName();
        HomeDTO homeDTO = new HomeDTO();
        homeDTO.setName(name);
        homeDTO.setEmail(user.getEmail());
        homeDTO.setAttachment(user.getImageUrl());
        homeDTO.setCategories(categoryRepository.findAll());
        return ResponseMessage.builder().data(homeDTO).status(true).build();
    }

//    public ResponseMessage homePageOauth2(String email, OAuth2User principal) {
//        User user = userRepository.findByEmail(email).orElseGet(() -> {
//            User newUser = new User();
//            newUser.setEmail(email);
//            newUser.setName((String) principal.getAttributes().get("name"));
//            newUser.setRole(Role.USER);
//            newUser.setVerified(true);
//            newUser.setImageUrl((String) principal.getAttributes().get("picture"));
//            userRepository.save(newUser);
//            return newUser;
//        });
//
//        HomeDTO homeDTO = new HomeDTO();
//        homeDTO.setName(user.getName());
//        homeDTO.setEmail(user.getEmail());
//        homeDTO.setAttachment(user.getImageUrl());
//        homeDTO.setCategories(categoryRepository.findAll());
//        Map<String,Object> tokenAndDto = new HashMap<>();
//        String token = jwtProvider.generateToken(user);
//        tokenAndDto.put(token,homeDTO);
//        return ResponseMessage.builder().data(tokenAndDto).status(true).text("userDto with Token").build();
//    }

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
        List<ResponseHomeRecipeDTO2> list = newRecipes.stream().map(r -> ResponseHomeRecipeDTO2.builder().id(r.getId())
                .title(r.getTitle())
                .imgUrl(r.getImageUrl())
                .cookingTime(r.getCookingTime())
                .averageRating(r.getAverageRating())
                .ownerId(r.getAuthor().getId())
                .ownerImage(r.getAuthor().getImageUrl())
                .ownerName(r.getAuthor().getName())
                .build()).toList();
        return ResponseMessage.builder().data(list).status(true).text("new recipes ordered").build();
    }



}
