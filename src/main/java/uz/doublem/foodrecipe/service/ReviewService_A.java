package uz.doublem.foodrecipe.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import uz.doublem.foodrecipe.entity.Recipe;
import uz.doublem.foodrecipe.entity.Review;
import uz.doublem.foodrecipe.entity.SavedRecipes;
import uz.doublem.foodrecipe.entity.User;
import uz.doublem.foodrecipe.payload.ResponseMessage;
import uz.doublem.foodrecipe.payload.ReviewDto_A;
import uz.doublem.foodrecipe.payload.UserDtoReview;
import uz.doublem.foodrecipe.repository.RecipeRepository_A;
import uz.doublem.foodrecipe.repository.ReviewRepository_A;
import uz.doublem.foodrecipe.repository.SavedRecipeRepository_A;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewService_A {


    final ReviewRepository_A reviewRepository;
    final RecipeRepository_A recipeRepository;
    final SavedRecipeRepository_A recipeRepositoryA;


    public ResponseMessage review(Integer recipeId, Integer page, Integer count) {
        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new RuntimeException("Recipe not found"));

        Pageable pageRequest = PageRequest.of(page, count);

        // Fetch reviews with pagination
        Page<Review> reviewPage = reviewRepository.findAllByRecipe_Id(recipeId, pageRequest);

        // Get the count of saved recipes (optimized query method in the repository)
        long countSaved = recipeRepositoryA.countByRecipe_Id(recipeId);

        // Map reviews to DTOs using streams
        List<ReviewDto_A> dtoList = getReviewDtoAs(reviewPage, (int) countSaved);

        // Return success response with reviews DTO list
        return ResponseMessage.builder()
                .text("success")
                .status(true)
                .data(dtoList)
                .build();
    }


    private static List<ReviewDto_A> getReviewDtoAs(Page<Review> reviewPage, int countSaved) {
        List<ReviewDto_A> dtoList = reviewPage.stream()
                .map(review -> {
                    User user = review.getUser();
                    ReviewDto_A dto = new ReviewDto_A();
                    dto.setSaved(countSaved);  // casting long to int for countSaved
                    dto.setAttachment(new UserDtoReview(user.getName(), user.getAttachment().getUrl()));
                    dto.setComment(review.getComment());
                    dto.setCreated_at(review.getCreatedAt().toString());
                    dto.setLikes_count(review.getLikeCount());
                    dto.setDislikes_count(review.getDislikeCount());
                    return dto;
                })
                .collect(Collectors.toList());
        return dtoList;
    }













}
