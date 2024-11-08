package uz.doublem.foodrecipe.service;


import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import uz.doublem.foodrecipe.entity.RecentSearch;
import uz.doublem.foodrecipe.entity.Recipe;
import uz.doublem.foodrecipe.payload.RecipeDtoShow;
import uz.doublem.foodrecipe.payload.ResponseMessage;
import uz.doublem.foodrecipe.repository.RecipeRepositoryM;
import uz.doublem.foodrecipe.repository.ResentSearchRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SearchService {

    private final ResentSearchRepository resentSearchRepository;
    private final RecipeRepositoryM recipeRepositoryM;
    public ResponseMessage getResentSearch(Integer size, Integer page, Integer userId) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Integer> recipeIdsByUserId = resentSearchRepository.findRecipeIdsByUserId(userId, pageRequest);

        List<Integer> list = recipeIdsByUserId.get().toList();
        List<Recipe> recipes = recipeRepositoryM.findByIdIn(list);
        List<RecipeDtoShow> recipeDtoShowList = recipes.stream()
                .map(recipe -> {
                    return RecipeDtoShow.builder()
                            .id(recipe.getId())
                            .title(recipe.getTitle())
                            .description(recipe.getDescription())
                            .imageUrl(recipe.getImageUrl())
                            .author(recipe.getAuthor().getName())
                            .averageRating(recipe.getAverageRating())
                            .build();
                })
                .toList();

        return ResponseMessage.builder()
                .data(recipeDtoShowList)
                .status(true)
                .text("This is the recent search recipe list page for user " + userId)
                .build();
    }

    public ResponseMessage getSearchResult(RecipeDtoShow recipeDtoShow) {

        return ResponseMessage.builder().text("test").status(true).build();
    }
}
