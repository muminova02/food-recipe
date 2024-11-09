package uz.doublem.foodrecipe.payload;

import lombok.Builder;

@Builder
public record ReviewDto_A (String comments,String saved,UserDtoReview attachment) {


}
