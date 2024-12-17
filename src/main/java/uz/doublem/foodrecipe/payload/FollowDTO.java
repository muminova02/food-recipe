package uz.doublem.foodrecipe.payload;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FollowDTO {
    private Integer userId;
    private String name;
    private String imgUrl;
}
