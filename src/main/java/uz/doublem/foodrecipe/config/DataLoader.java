package uz.doublem.foodrecipe.config;


import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import uz.doublem.foodrecipe.entity.User;
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
//            user.setRole("ADMIN");
            user.setEmail(s);
            user.setPassword_hash(passwordEncoder.encode("root123"));
            userRepository.save(user);
        }

    }

    private void createPhotoPath()
    {
        File directory = new File(photoPath);
        if (!directory.exists())
            directory.mkdirs();
    }

}
