package uz.doublem.foodrecipe.payload;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class IngredientEditDto {
    private Integer ingredientId;
    private String ingredientName;
}
