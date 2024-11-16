package uz.doublem.foodrecipe.payload.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import uz.doublem.foodrecipe.entity.Location;
import uz.doublem.foodrecipe.entity.User;
import uz.doublem.foodrecipe.enums.Role;

import java.time.LocalDateTime;
import java.util.Set;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserValidDTO {
    private Integer id;
    private String name;
    private String email;
    private String role;
}
