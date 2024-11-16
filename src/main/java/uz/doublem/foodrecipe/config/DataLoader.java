package uz.doublem.foodrecipe.config;


import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import uz.doublem.foodrecipe.entity.User;
import uz.doublem.foodrecipe.enums.Role;
import uz.doublem.foodrecipe.repository.UserRepository;

import java.io.File;

@Component

public class DataLoader implements CommandLineRunner
{

    private static final Logger log = LoggerFactory.getLogger(DataLoader.class);
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    @Value("${photo.upload.path}")
    String photoPath;

    public DataLoader(UserRepository userRepository, PasswordEncoder passwordEncoder)
    {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    public void run(String... args) throws Exception
    {
        createPhotoPath();
        securityUser();
    }

    private void securityUser()
    {
        String s = "admin@gmail.com";
        if (!userRepository.existsByEmail(s))
        {
            User user = new User();
            user.setRole(Role.ADMIN);
            user.setEmail(s);
            user.setName("admin");
            user.setPassword_hash(passwordEncoder.encode("root123"));
            user.setVerificationCode("1234");
            user.setVerified(true);
            User u = new User();
            u.setName("frenky");
            u.setVerified(true);
            u.setRole(Role.USER);
            u.setPassword_hash(passwordEncoder.encode("root123"));
            u.setEmail("frenky@gmail.com");
            u.setVerificationCode("1234");
            User us = new User();
            us.setName("john");
            us.setVerified(true);
            us.setPassword_hash(passwordEncoder.encode("root123"));
            us.setEmail("john@gmail.com");
            us.setRole(Role.CHEF);
            us.setVerificationCode("1234");
            userRepository.save(us);
            userRepository.save(user);
            userRepository.save(u);
        }

    }

    private void createPhotoPath()
    {
        File directory = new File(photoPath);
        if (!directory.exists())
            directory.mkdirs();
    }

}
