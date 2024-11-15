package uz.doublem.foodrecipe.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import uz.doublem.foodrecipe.repository.UserRepository;

@EnableWebSecurity
@RequiredArgsConstructor
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class MySecurityConfig {
    private final MyFilter myFilter;
    private final UserRepository userRepository;

    @Bean
    public PasswordEncoder passwordEncoder(){
            return new BCryptPasswordEncoder();
        }
        @Bean
    public SecurityFilterChain mySecurity(HttpSecurity http) throws Exception {

               http.addFilterBefore(myFilter, UsernamePasswordAuthenticationFilter.class)
               .csrf((c)-> c.disable())
                       .cors((cr)-> cr.disable())
                       .userDetailsService(userDetailsService())
                       .authorizeRequests()
                       .requestMatchers("/auth/**","/swagger-ui/**",
                               "/v3/api-docs/**")
                       .permitAll()
                       .anyRequest()
                       .authenticated();

        return http.build();
    }
    @Bean
    public UserDetailsService userDetailsService(){
            UserDetailsService userDetails = (email)->
            userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("user not found!"));
        return userDetails;
    }
}
