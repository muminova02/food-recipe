package uz.doublem.foodrecipe.payload;


import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class RecipeDTOaddOnly {
    private String title;
    private String description;
    private Integer category_id;
    private String cookingTime;
}
