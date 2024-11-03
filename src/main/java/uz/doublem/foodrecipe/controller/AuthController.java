package uz.doublem.foodrecipe.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uz.doublem.foodrecipe.entity.User;
import uz.doublem.foodrecipe.payload.LocationDTO;
import uz.doublem.foodrecipe.payload.UserDTO;
import uz.doublem.foodrecipe.util.Util;

@RestController
@RequestMapping("/auth")
public class AuthController {


    @PostMapping
    public ResponseEntity<?> signIn(){
        return null;
    }
    @GetMapping
    public ResponseEntity<?> signUp(UserDTO userDTO){
        User user = new User();
        user.setName(userDTO.name());
        user.setEmail(userDTO.email());
        user.setPassword_hash(Util.base64Encoder(userDTO.password()));
        return null;
    }
}
