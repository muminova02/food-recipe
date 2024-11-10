package uz.doublem.foodrecipe.payload;

import lombok.Data;

@Data
public class ResetPasswordDTO {
    String email;
    String code;
    String newPassword;
}
