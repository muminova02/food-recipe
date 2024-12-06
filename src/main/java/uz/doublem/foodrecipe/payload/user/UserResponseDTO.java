package uz.doublem.foodrecipe.payload.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.doublem.foodrecipe.entity.Location;
import uz.doublem.foodrecipe.enums.Role;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponseDTO implements Serializable {
    private Integer id;
    private String name;
    private String email;
    private String description;
    private Role role;
    private Integer following_count =0;
    private Integer followers_count =0;
    private Boolean verified = false;
    private String imageUrl;
    private Location location;
}
