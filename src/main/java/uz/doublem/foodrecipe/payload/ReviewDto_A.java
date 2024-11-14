package uz.doublem.foodrecipe.payload;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReviewDto_A {

        private Integer recipeId;
        private Long savedCount;
        private Long commentsCount;

        List<ReviewResponceDto> reviews;


}
