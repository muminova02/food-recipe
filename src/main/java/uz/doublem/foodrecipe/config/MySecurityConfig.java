package uz.doublem.foodrecipe.config;

import jakarta.validation.groups.Default;
import lombok.RequiredArgsConstructor;
import lombok.With;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@EnableWebSecurity
@RequiredArgsConstructor
@Configuration
@EnableMethodSecurity
public class MySecurityConfig {
    private final MyFilter myFilter;
    @Bean
    public PasswordEncoder passwordEncoder(){
            return new BCryptPasswordEncoder();
        }
        @Bean
    public SecurityFilterChain mySecurity(HttpSecurity http) throws Exception {

               http
                       .addFilterBefore(myFilter, UsernamePasswordAuthenticationFilter.class)
                       .csrf((c) -> c.disable())
                       .cors((cr) -> cr.disable())
                       .authorizeHttpRequests((auth) -> auth
                               .requestMatchers("/api/**", "/auth/**", "/swagger-ui/**", "/v3/api-docs/**", "/attachment/**").permitAll()
                               .anyRequest().authenticated()
                       );
        return http.build();
    }

}
