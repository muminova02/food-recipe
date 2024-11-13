package uz.doublem.foodrecipe.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.doublem.foodrecipe.entity.Review;
import uz.doublem.foodrecipe.entity.User;
import uz.doublem.foodrecipe.payload.ResponseMessage;
import uz.doublem.foodrecipe.payload.ReviewDto;
import uz.doublem.foodrecipe.payload.ReviewDto_A;
import uz.doublem.foodrecipe.payload.ReviewLikeDtoAdd;
import uz.doublem.foodrecipe.repository.UserRepository;
import uz.doublem.foodrecipe.service.ReviewService_A;

import java.util.Optional;

@RestController
@RequestMapping("/api/reviewA")
@RequiredArgsConstructor
public class ReviewController_A {
    private final ReviewService_A service;
    private final UserRepository userRepository;


    @GetMapping("/{recipeId}/{page}/{count}")
    public ResponseEntity<?> getReviewListByRecipeId(@PathVariable Integer recipeId, @PathVariable Integer page, @PathVariable Integer count) {
        //        User curentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<User> byId = userRepository.findById(2);
        ResponseMessage review = service.review(recipeId,byId.get(), page, count);
        return ResponseEntity.status(review.getStatus()?201:400).body(review);
    }

    @PostMapping("/rate-recipe")
    public ResponseEntity<?> rateRecipe(@RequestBody ReviewDto reviewDto) {
        //        User curentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<User> byId = userRepository.findById(2);
        ResponseMessage res = service.addReviewForRecipe(reviewDto,byId.get());
        return ResponseEntity.status(res.getStatus()?201:400).body(res);
    }

    @PostMapping("/comment")
    public ResponseEntity<?> addComment(@RequestBody ReviewDto reviewDto) {
        //        User curentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<User> byId = userRepository.findById(2);
        ResponseMessage res = service.addCommentForRecipe(reviewDto,byId.get());
        return ResponseEntity.status(res.getStatus()?201:400).body(res);
    }

    @PostMapping("/like-recipe")
    public ResponseEntity<?> likeRecipe(@RequestBody ReviewLikeDtoAdd reviewLikeDtoAdd) {
        //        User curentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<User> byId = userRepository.findById(2);
       ResponseMessage res = service.reactionToComment(reviewLikeDtoAdd,byId.get());
       return ResponseEntity.status(res.getStatus()?201:400).body(res);
    }


}
