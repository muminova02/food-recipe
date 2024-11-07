package uz.doublem.foodrecipe.controller;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uz.doublem.foodrecipe.config.JwtProvider;
import uz.doublem.foodrecipe.entity.User;
import uz.doublem.foodrecipe.payload.LocationDTO;
import uz.doublem.foodrecipe.payload.ResponseMessage;
import uz.doublem.foodrecipe.payload.UserDTO;
import uz.doublem.foodrecipe.repository.UserRepository;
import uz.doublem.foodrecipe.service.AuthService;
import uz.doublem.foodrecipe.service.SmsService;
import uz.doublem.foodrecipe.util.Util;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final SmsService smsService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final AuthService authService;
    @PostMapping("/sign-in")
    public ResponseMessage signIn(@RequestBody UserDTO userDTO){
        return authService.signIn(userDTO);
    }
    @SneakyThrows
    @PostMapping("/sign-up")
    public ResponseMessage signUp(@RequestBody UserDTO userDTO){

        return authService.signUp(userDTO);
    }

    @PostMapping("/verify")
    public ResponseMessage verify(@RequestBody UserDTO userDTO){
       return authService.verify(userDTO);
    }
}
