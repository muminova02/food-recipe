package uz.doublem.foodrecipe.payload.user;

import lombok.Data;

@Data
public class UserVerifyDTO {
    String name;
    String email;
    String password;
    String code;
}
