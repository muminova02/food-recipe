package uz.doublem.foodrecipe.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.doublem.foodrecipe.entity.User;
import uz.doublem.foodrecipe.payload.ResponseMessage;
import uz.doublem.foodrecipe.payload.ReviewDto;
import uz.doublem.foodrecipe.payload.ReviewLikeDtoAdd;
import uz.doublem.foodrecipe.repository.UserRepository;
import uz.doublem.foodrecipe.service.ReviewService_A;
import uz.doublem.foodrecipe.util.Util;


@RestController
@RequestMapping("/api/reviewA")
@RequiredArgsConstructor
public class ReviewController_A {
    private final ReviewService_A service;
    private final UserRepository userRepository;


    @GetMapping("/{recipeId}/{page}/{count}")
    public ResponseEntity<?> getReviewListByRecipeId(@PathVariable Integer recipeId, @PathVariable Integer page, @PathVariable Integer count) {
        User user = Util.getCurrentUser();
        ResponseMessage review = service.review(recipeId,user, page, count);
        return ResponseEntity.status(review.getStatus()?201:400).body(review);
    }

    @PostMapping("/rate-recipe")
    public ResponseEntity<?> rateRecipe(@RequestBody ReviewDto reviewDto) {
        User user = Util.getCurrentUser();
        ResponseMessage res = service.addReviewForRecipe(reviewDto,user);
        return ResponseEntity.status(res.getStatus()?201:400).body(res);
    }

    @PostMapping("/comment")
    public ResponseEntity<?> addComment(@RequestBody ReviewDto reviewDto) {
        User user = Util.getCurrentUser();
        ResponseMessage res = service.addCommentForRecipe(reviewDto,user);
        return ResponseEntity.status(res.getStatus()?201:400).body(res);
    }

    @PostMapping("/like-comment")
    public ResponseEntity<?> likeRecipe(@RequestBody ReviewLikeDtoAdd reviewLikeDtoAdd) {
        User user = Util.getCurrentUser();
       ResponseMessage res = service.reactionToComment(reviewLikeDtoAdd,user);
       return ResponseEntity.status(res.getStatus()?201:400).body(res);
    }


    @DeleteMapping("/{id}/comment")
    public ResponseEntity<?> deleteComment(@PathVariable Integer id) {
        User user = Util.getCurrentUser();
        ResponseMessage res = service.deleteComment(id, user);
        return ResponseEntity.status(res.getStatus()?201:400).body(res);
    }

    @GetMapping("/{id}/rate")
    public ResponseEntity<?> getRate(@PathVariable Integer id) {
        User user = Util.getCurrentUser();
        ResponseMessage res = service.getReviewRate(id,user);
        return ResponseEntity.status(res.getStatus()?200:400).body(res);
    }


}
