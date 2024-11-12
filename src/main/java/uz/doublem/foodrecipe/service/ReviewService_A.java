package uz.doublem.foodrecipe.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import uz.doublem.foodrecipe.entity.LikeReview;
import uz.doublem.foodrecipe.entity.Recipe;
import uz.doublem.foodrecipe.entity.Review;
import uz.doublem.foodrecipe.entity.User;
import uz.doublem.foodrecipe.payload.*;
import uz.doublem.foodrecipe.repository.LikeReviewRepository;
import uz.doublem.foodrecipe.repository.RecipeRepository_A;
import uz.doublem.foodrecipe.repository.ReviewRepository_A;
import uz.doublem.foodrecipe.repository.SavedRecipeRepository_A;
import uz.doublem.foodrecipe.util.Util;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewService_A {


    private final ReviewRepository_A reviewRepository;
    private final RecipeRepository_A recipeRepository;
    private final SavedRecipeRepository_A recipeRepositoryA;
    private final LikeReviewRepository likeReviewRepository;


    public ResponseMessage review(Integer recipeId, User user, Integer page, Integer count) {
        if (!recipeRepository.existsById(recipeId)) {
            return Util.getResponseMes(false,"recipe not found",recipeId);
        }

        Pageable pageRequest = PageRequest.of(page, count);

        Page<Review> reviewPage = reviewRepository.findAllByRecipe_Id(recipeId, pageRequest);

        Long countSaved = recipeRepositoryA.countByRecipe_Id(recipeId);

        List<ReviewResponceDto> dtoList = getReviewDtoAs(reviewPage,user.getId());
        Long commentsCount = reviewRepository.countNonNullCommentsByUserAndRecipe(user.getId(), recipeId);
        ReviewDto_A responseReviewCommentPage = ReviewDto_A.builder()
                .recipeId(recipeId)
                .savedCount(countSaved)
                .commentsCount(commentsCount)
                .reviews(dtoList)
                .build();

        return ResponseMessage.builder()
                .text("success")
                .status(true)
                .data(responseReviewCommentPage)
                .build();
    }


    private List<ReviewResponceDto> getReviewDtoAs(Page<Review> reviewPage, Integer userId) {

        List<ReviewResponceDto> dtoList = reviewPage.stream()
                .map(review -> {
                    User user = review.getUser();
                    ReviewResponceDto dto = new ReviewResponceDto();
                    dto.setAttachment(new UserDtoReview(user.getName(), user.getImageUrl()));
                    dto.setComment(review.getComment());
                    dto.setCreated_at(review.getCreatedAt().toString());
                    dto.setLikes_count(review.getLikeCount());
                    dto.setDislikes_count(review.getDislikeCount());
                    Boolean b = likeReviewRepository.fetchLikeReviewById(userId, review.getId());
                    dto.setHasLikedUser(b);
                    return dto;
                })
                .collect(Collectors.toList());
        return dtoList;

    }


    public ResponseMessage addReviewForRecipe(ReviewDto reviewDto, User user) {
        Integer recipeId = reviewDto.getRecipeId();
        Optional<Recipe> byId = recipeRepository.findById(recipeId);
        Review review = new Review();
        if (byId.isEmpty()) {
            return Util.getResponseMes(false,"Recipe not found",recipeId);
        }
        Optional<Review> optionReview = reviewRepository.findByRecipe_IdAndUser_id(recipeId, user.getId());
        if (optionReview.isPresent()){
            review = optionReview.get();
            if (reviewDto.getRating() != null) {
                review.setRating(reviewDto.getRating());
                fixedAverageRateForRecipe(recipeId);
            }
            reviewRepository.save(review);
            return Util.getResponseMes(true,"Rated to Recipe, and not create Review becouse Review Already created",recipeId);
        }
        if (reviewDto.getRating() != null) {
            review.setRating(reviewDto.getRating());
        }
        review.setRecipe(byId.get());
        review.setUser(user);
        review.setDislikeCount(0);
        review.setLikeCount(0);
        reviewRepository.save(review);
        fixedAverageRateForRecipe(recipeId);
        return Util.getResponseMes(true,"NEW Review create and rate set to Recipe ",review);
    }

    private void fixedAverageRateForRecipe(Integer recipeId) {
        Double averageRating = reviewRepository.findAverageRatingByRecipeId(recipeId);
        if (averageRating != null) {
            recipeRepository.updateAverageRating(recipeId,averageRating);
        }
    }

    public ResponseMessage addCommentForRecipe(ReviewDto reviewDto, User user) {
        Integer recipeId = reviewDto.getRecipeId();
        Optional<Recipe> byId = recipeRepository.findById(recipeId);
        Review review = new Review();
        if (byId.isEmpty()) {
            return Util.getResponseMes(false,"Recipe not found with this id: ",recipeId);
        }
        Optional<Review> optionReview = reviewRepository.findByRecipe_IdAndUser_id(recipeId, user.getId());
        if (optionReview.isPresent()){
            review = optionReview.get();
            if (reviewDto.getComment() != null) {
            review.setComment(reviewDto.getComment());
            }
            reviewRepository.save(review);
            return Util.getResponseMes(true,"commit set to Recipe, and not create Review becouse Review Already created, return reviewId: ",review.getId());
        }
        if (reviewDto.getComment() != null) {
            review.setComment(reviewDto.getComment());
        }
        review.setRecipe(byId.get());
        review.setUser(user);
        review.setDislikeCount(0);
        review.setLikeCount(0);
        reviewRepository.save(review);
        return Util.getResponseMes(true,"NEW Review create and rate set to Recipe, responce return reviewId ",review.getId());
    }

    public ResponseMessage reactionToComment(ReviewLikeDtoAdd reviewLikeDtoAdd, User user) {
        Optional<Review> byId = reviewRepository.findById(reviewLikeDtoAdd.getReviewId());
        if (byId.isEmpty()) {
            return Util.getResponseMes(false,"review not found with this id: ",reviewLikeDtoAdd.getReviewId());
        }
        Optional<LikeReview> optionLikeReview = likeReviewRepository.findByReview_IdAndUser_Id(reviewLikeDtoAdd.getReviewId(), user.getId());
        if (optionLikeReview.isEmpty()) {
            LikeReview likeReview = new LikeReview();
            likeReview.setReview(byId.get());
            likeReview.setUser(user);
            likeReview.setIsLike(reviewLikeDtoAdd.getHasLiked());
            likeReviewRepository.save(likeReview);
            return Util.getResponseMes(true,"create and reacted to Review", reviewLikeDtoAdd);
        }
        LikeReview likeReview = optionLikeReview.get();
        likeReview.setIsLike(!likeReview.getIsLike());
        likeReviewRepository.save(likeReview);
        return Util.getResponseMes(true,"not create, change like to Review", reviewLikeDtoAdd);
    }
}
