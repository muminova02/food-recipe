package uz.doublem.foodrecipe.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import uz.doublem.foodrecipe.entity.Recipe;
import uz.doublem.foodrecipe.entity.Review;
import uz.doublem.foodrecipe.payload.ResponseMessage;
import uz.doublem.foodrecipe.payload.ReviewDto_A;
import uz.doublem.foodrecipe.repository.RecipeRepository_A;
import uz.doublem.foodrecipe.repository.ReviewRepository_A;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReviewService_A {


    final ReviewRepository_A reviewRepository;
    final RecipeRepository_A recipeRepository;


    public ResponseMessage review(Integer recipeId,Integer page,Integer count) {
        Recipe recipe = recipeRepository.findById(recipeId).orElseThrow(() -> new RuntimeException("User not found"));
        Pageable pageRequest = PageRequest.of(page, count);
        Page<Review> byRecipeId = reviewRepository.findAllByRecipe_Id(recipeId, pageRequest);
        List<Review> content = byRecipeId.getContent();
        ArrayList<ReviewDto_A> reviewDtoAList=new ArrayList<>();




    }


}
