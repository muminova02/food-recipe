package uz.doublem.foodrecipe.payload.profile;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ProfileDto {
    private Integer userId;
    private Integer recipeNumber;
    private Integer followersCount;
    private Integer followingCount;
    private String userName;
    private String userRole;
    private Boolean isFollow;
    private String description;
    private String authorImg;
    private String country;
    private String city;
}
