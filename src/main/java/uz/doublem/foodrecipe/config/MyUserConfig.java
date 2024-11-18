package uz.doublem.foodrecipe.config;


import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import uz.doublem.foodrecipe.entity.User;
import uz.doublem.foodrecipe.repository.UserRepository;

@Component
@RequiredArgsConstructor
public class MyUserConfig implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    @Cacheable(value = "users", key = "#email")
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("user not found!"));
        System.out.println(user.getEmail());
        return user;
    }
}
