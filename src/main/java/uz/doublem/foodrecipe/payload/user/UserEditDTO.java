package uz.doublem.foodrecipe.payload.user;

import lombok.Data;

@Data
public class UserEditDTO {
    private Integer id;
    private String name;
    private String email;
    private String role;
    private String country;
    private String city;
    private String description;
}
