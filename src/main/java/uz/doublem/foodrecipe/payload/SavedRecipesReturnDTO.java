package uz.doublem.foodrecipe.payload;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SavedRecipesReturnDTO {
    private String title;
    private String author;
    private Double averageRating;
    private String cookingTime;

}
