package uz.doublem.foodrecipe.payload;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class UpdateRecipeDto {
    private Integer recipeId;
    private String recipeName;
    private String recipeDescription;
    private String cookingTime;

}
