package uz.doublem.foodrecipe.payload;

import lombok.Data;

@Data
public class UserSignInDTO {
    String name;
    String email;
    String password;
    String code;
}
