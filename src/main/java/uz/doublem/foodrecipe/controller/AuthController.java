package uz.doublem.foodrecipe.controller;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import uz.doublem.foodrecipe.config.JwtProvider;
import uz.doublem.foodrecipe.entity.User;
import uz.doublem.foodrecipe.payload.*;
import uz.doublem.foodrecipe.repository.UserRepository;
import uz.doublem.foodrecipe.service.AuthService;
import uz.doublem.foodrecipe.service.SmsService;
import uz.doublem.foodrecipe.util.Util;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/sign-in")
    public ResponseMessage signIn(@RequestBody UserSignInDTO userSignInDTO){
        return authService.signIn(userSignInDTO);
    }
    @SneakyThrows
    @PostMapping("/sign-up")
    public ResponseMessage signUp(@RequestBody UserDTO userDTO){

        return authService.signUp(userDTO);
    }

    @PostMapping("/verify")
    public ResponseMessage verify(@RequestBody UserSignInDTO userSignInDTO){
       return authService.verify(userSignInDTO);
    }

    @PostMapping("/request-password-reset")
    public ResponseMessage requestPass(@RequestParam String email){
       return authService.requestPasswordReset(email);
    }

    @PostMapping("/reset-password")
    public ResponseMessage resetPass(@RequestBody ResetPasswordDTO resetPasswordDTO){
       return authService.resetPassword(resetPasswordDTO);
    }
}
