package uz.doublem.foodrecipe.payload.user;

import lombok.Data;

@Data
public class UserSignInDTO {
    String email;
    String password;
}
