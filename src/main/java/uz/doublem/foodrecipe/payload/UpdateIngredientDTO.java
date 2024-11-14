package uz.doublem.foodrecipe.payload;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class UpdateIngredientDTO {
    private Integer ingredientAndQuantityId;
    private String ingredientId;
    private String quantity;
}
