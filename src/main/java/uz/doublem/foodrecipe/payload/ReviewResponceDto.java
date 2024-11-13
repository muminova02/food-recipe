package uz.doublem.foodrecipe.payload;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReviewResponceDto {

    private Integer reviewId;
    private UserDtoReview attachment;
    private String comment;
    private Integer likes_count;
    private Integer dislikes_count;
    private Boolean hasLikedUser;
    private String created_at;

}
