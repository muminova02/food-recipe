package uz.doublem.foodrecipe.payload;


import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class IngredientDTOAdd {
    Integer ingredientId;
    String ingredientQuantity;
}
