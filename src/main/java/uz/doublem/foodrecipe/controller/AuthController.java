package uz.doublem.foodrecipe.controller;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.doublem.foodrecipe.payload.*;
import uz.doublem.foodrecipe.payload.user.UserDTO;
import uz.doublem.foodrecipe.payload.user.UserSignInDTO;
import uz.doublem.foodrecipe.payload.user.UserVerifyDTO;
import uz.doublem.foodrecipe.service.AuthService;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/sign-in")
    public ResponseEntity<?> signIn(@RequestBody UserSignInDTO userSignInDTO){
        ResponseMessage responseMessage = authService.signIn(userSignInDTO);
        return ResponseEntity.status(responseMessage.getStatus()?200:400).body(responseMessage);
    }
    @SneakyThrows
    @PostMapping("/sign-up")
    public ResponseEntity<?> signUp(@RequestBody UserDTO userDTO){
        ResponseMessage responseMessage = authService.signUp(userDTO);
        return ResponseEntity.status(responseMessage.getStatus()?200:400).body(responseMessage);
    }

    @PostMapping("/verify")
    public ResponseEntity<?> verify(@RequestBody UserVerifyDTO userVerifyDTO){
        ResponseMessage verify = authService.verify(userVerifyDTO);
        return ResponseEntity.status(verify.getStatus()?200:400).body(verify);
    }

    @PostMapping("/request-password-reset")
    public ResponseEntity<?> requestPass(@RequestBody EmailDto email){

        ResponseMessage responseMessage = authService.requestPasswordReset(email);
        return ResponseEntity.status(responseMessage.getStatus()?200:400).body(responseMessage);
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPass(@RequestBody ResetPasswordDTO resetPasswordDTO){
        ResponseMessage responseMessage = authService.resetPassword(resetPasswordDTO);
        return ResponseEntity.status(responseMessage.getStatus()?200:400).body(responseMessage);
    }
}
