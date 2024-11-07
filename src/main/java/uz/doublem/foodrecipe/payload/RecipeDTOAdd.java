package uz.doublem.foodrecipe.payload;


import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RecipeDTOAdd {

    String title;
    String description;
    Integer category_id;
    String cookingTime;

    List<IngredientDTOAdd> ingredientList;

    List<StepsDTOAdd> stepsList;


}
