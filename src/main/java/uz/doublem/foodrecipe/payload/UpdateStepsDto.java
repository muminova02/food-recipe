package uz.doublem.foodrecipe.payload;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class UpdateStepsDto {
    private Integer stepId;
    private String description;
}
