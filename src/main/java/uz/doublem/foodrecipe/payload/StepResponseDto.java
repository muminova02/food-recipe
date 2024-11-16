package uz.doublem.foodrecipe.payload;

import lombok.*;
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class StepResponseDto {
    private Integer stepId;
    private String text;
    private Integer stepNumber;
    private Integer recipeId;
}
