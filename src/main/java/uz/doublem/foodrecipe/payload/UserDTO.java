package uz.doublem.foodrecipe.payload;

import java.time.LocalDateTime;

public record UserDTO(String name, String email, String password) {
}
