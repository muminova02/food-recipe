package uz.doublem.foodrecipe.payload;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResponseHomeRecipeDTO2 {
    private Integer id;
    private String title;
    private String cookingTime;
    private String imgUrl;
    private Double averageRating;
    private String ownerImage;
    private String ownerName;
    private Integer ownerId;
}
