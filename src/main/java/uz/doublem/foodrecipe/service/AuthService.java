package uz.doublem.foodrecipe.service;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import uz.doublem.foodrecipe.config.JwtProvider;
import uz.doublem.foodrecipe.entity.User;
import uz.doublem.foodrecipe.payload.ResponseMessage;
import uz.doublem.foodrecipe.payload.UserDTO;
import uz.doublem.foodrecipe.repository.UserRepository;
import uz.doublem.foodrecipe.util.Util;

import java.time.LocalDateTime;
@Component
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final SmsService smsService;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    @SneakyThrows
    @PostMapping
    public ResponseMessage signUp(UserDTO userDTO){
        if (userRepository.existsByEmail(userDTO.email())) {
            return ResponseMessage.builder()
                    .status(false).
                    text("username already in use!")
                    .data(new RuntimeException()).build();
        }
        User user = new User();
        user.setName(userDTO.name());
        user.setEmail(userDTO.email());
        user.setPassword_hash(passwordEncoder.encode(userDTO.password()));
        user.setGeneretedCodeTime(LocalDateTime.now());
        String code = smsService.generateCode();
        user.setCode(code);
        user.setVerified(false);
        //smsService.sendSmsToUser(userDTO.email(), code);
        userRepository.save(user);
        return ResponseMessage.builder()
                .status(true).
                text("confirm your account, check your mail")
                .data(userDTO).build();
    }

    public ResponseMessage signIn(UserDTO userDTO){
        User user = userRepository.findByEmail(userDTO.email()).orElseThrow(() -> new RuntimeException("user not found!"));
        if (!passwordEncoder.matches(userDTO.password(),user.getPassword_hash())){
            throw new RuntimeException("wrong password or username!");
        }
        if (!user.getVerified()){
                if (user.getCode()!= null){
                    smsService.sendSmsToUser(user.getEmail(), smsService.generateCode());
                    return ResponseMessage.builder().status(false).data(new RuntimeException())
                            .text("The confirmation code has been sent to you again").build();
                }
            return ResponseMessage.builder().status(false).data(new RuntimeException())
                    .text("Please confirm your account first!").build();
        }
        String token = jwtProvider.generateToken(user);
        return ResponseMessage.builder().status(true).data(token).text("your token " + userDTO.name()).build();
    }

    public ResponseMessage verify(UserDTO userDTO){
        LocalDateTime time = LocalDateTime.now();
        User user = userRepository.findByEmail(userDTO.email()).orElseThrow(() -> new RuntimeException("user not found!"));
        LocalDateTime generetedCodeTime = user.getGeneretedCodeTime();
        if (!user.getVerified()){
            if (user.getCode()==null) {
              return   ResponseMessage.builder()
                        .status(false)
                        .data(new RuntimeException())
                        .text("confimation code not found").build();
                            }
        }
        if (generetedCodeTime.plusMinutes(5).isBefore(time)){
            return   ResponseMessage.builder()
                    .status(false)
                    .data(new RuntimeException())
                    .text("confirmation time expired").build();
        }
        if (!user.getCode().equalsIgnoreCase(userDTO.code())|| !user.getEmail().equalsIgnoreCase(userDTO.email())){
            return ResponseMessage.builder().status(false).data(new RuntimeException())
                    .text("incorrect code")
                    .build();
        }
        user.setVerified(true);
        userRepository.save(user);
        return ResponseMessage.builder().status(true).data(user).build();
    }
}
