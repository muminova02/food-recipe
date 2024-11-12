package uz.doublem.foodrecipe.payload;


import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RecipeDTOAdd {

    String title;
    String description;
    Integer category_id;
    String cookingTime;

    List<IngredientDTOAdd> ingredientList;

    List<StepsDTOAdd> stepsList;


}
