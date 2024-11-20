package uz.doublem.foodrecipe.payload.user;

import lombok.Data;

@Data
public class UserVerifyDTO {
    String email;
    String code;
}
