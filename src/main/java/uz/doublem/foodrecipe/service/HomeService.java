package uz.doublem.foodrecipe.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import uz.doublem.foodrecipe.entity.Attachment;
import uz.doublem.foodrecipe.entity.Recipe;
import uz.doublem.foodrecipe.entity.User;
import uz.doublem.foodrecipe.payload.HomeDTO;
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
        Attachment attachment = user.getAttachment();
        HomeDTO homeDTO = new HomeDTO();
        homeDTO.setName(name);
        homeDTO.setEmail(user.getEmail());
        homeDTO.setAttachment(attachment);
        homeDTO.setCategories(categoryRepository.findAll());
        return ResponseMessage.builder().data(homeDTO).status(true).build();
    }

    public ResponseMessage getRecipesByCategoryId(Integer id){
        List<Recipe> byCategoryId = reciepesRepository.getByCategoryId(id);
        return ResponseMessage.builder().text("recipes by category id").status(true).data(byCategoryId).build();
    }
}
