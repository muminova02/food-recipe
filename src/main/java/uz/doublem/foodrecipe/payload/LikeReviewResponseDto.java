package uz.doublem.foodrecipe.payload;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.relational.core.sql.In;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class LikeReviewResponseDto {
    private Integer Id;
    private Integer reviewId;
    private Integer userId;
    private Boolean isLike;
}
