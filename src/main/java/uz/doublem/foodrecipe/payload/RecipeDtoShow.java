package uz.doublem.foodrecipe.payload;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RecipeDtoShow {
    private Integer id;
    private String title;
    private String description;
    private String imageUrl;
    private String author;
    private Double averageRating;
}
