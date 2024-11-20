package uz.doublem.foodrecipe.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Base64;

@Configuration
@RequiredArgsConstructor
@Component
public class MyFilter extends OncePerRequestFilter {
    @Autowired
    private JwtProvider jwtProvider;
    private final MyUserConfig myUserConfig;
//    private final UserDetailsService myUserConfig;
    @Lazy
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorization = request.getHeader("Authorization");
       try {

if (authorization.startsWith("Bearer ")){
    String substring = authorization.substring(7);
    String email =jwtProvider.getSubject(substring);
    setAuthenticationToContext(email);
}}
       catch (Exception e){

       }
       filterChain.doFilter(request,response);
    }
    public void setAuthenticationToContext(String email){
        UserDetails userDetails = myUserConfig.loadUserByUsername(email);
        System.out.println("contextga joynalnmoqda...");
        UsernamePasswordAuthenticationToken authUser = new UsernamePasswordAuthenticationToken(
                userDetails,
                    null,
userDetails.getAuthorities()
        );
        SecurityContextHolder.getContext().setAuthentication(authUser);
    }

//    public void setAuthenticationToContext(String email, String password){
//        UserDetails userDetails = userDetailsService.loadUserByUsername(email);
//        if (!userDetails.getPassword().matches(password)){
//            throw new BadCredentialsException("Wrong password or username!");
//        }
//         setAuthenticationToContext(email);
//    }
}
