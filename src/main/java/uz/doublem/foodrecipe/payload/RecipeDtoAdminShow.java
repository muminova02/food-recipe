package uz.doublem.foodrecipe.payload;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.doublem.foodrecipe.entity.*;

import java.sql.Timestamp;
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecipeDtoAdminShow {
    private Integer id;
    private String title;

    private Timestamp createdAt;

    private Category category;

    private String author;

    private String link;

    private Double averageRating;

    private String description;

    private String cookingTime;

    private String imageUrl;

    private String videoUrl;

    private Long viewsCount;

}