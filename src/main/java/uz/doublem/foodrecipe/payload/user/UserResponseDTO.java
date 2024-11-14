package uz.doublem.foodrecipe.payload.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.doublem.foodrecipe.enums.Role;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponseDTO {
    private Integer id;
    private String name;
    private String email;
    private Role role;
    private String country;
    private String city;
}
