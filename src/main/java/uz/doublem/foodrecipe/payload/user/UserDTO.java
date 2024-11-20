package uz.doublem.foodrecipe.payload.user;

import uz.doublem.foodrecipe.enums.Role;

public record UserDTO(String name, String email, String password, Role role) {
}
