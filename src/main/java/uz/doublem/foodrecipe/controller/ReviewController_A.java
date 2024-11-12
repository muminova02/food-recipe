package uz.doublem.foodrecipe.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uz.doublem.foodrecipe.payload.ResponseMessage;
import uz.doublem.foodrecipe.service.ReviewService_A;

@RestController
@RequestMapping("/api/reviewA")
@RequiredArgsConstructor
public class ReviewController_A {
    final ReviewService_A service;


    @GetMapping("/{recipeId}/{page}/{count}")
    public ResponseEntity<?> review(@PathVariable Integer recipeId, @PathVariable Integer page, @PathVariable Integer count) {
        ResponseMessage review = service.review(recipeId, page, count);
        return ResponseEntity.status(review.getStatus()?201:400).body(review);
    }




}
