package uz.doublem.foodrecipe.payload;

import lombok.*;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SavedResponseDto {
    private Integer id;
    private String title;
    private String description;
    private String author;
    private String time;
    private Double averageRating;
    private String imageUrl;


}
