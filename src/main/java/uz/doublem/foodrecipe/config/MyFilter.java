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
    @Lazy
    private UserDetailsService userDetailsService;
    @Autowired
    private JwtProvider jwtProvider;
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
    System.out.println(email);
    setAuthenticationToContext(email);
}}
       catch (Exception e){

       }
       filterChain.doFilter(request,response);
    }
    public void setAuthenticationToContext(String username){
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        UsernamePasswordAuthenticationToken authUser = new UsernamePasswordAuthenticationToken(
                userDetails,
                    null,
userDetails.getAuthorities()
        );
        SecurityContextHolder.getContext().setAuthentication(authUser);
    }

    public void setAuthenticationToContext(String email, String password){
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);
        if (!userDetails.getPassword().matches(password)){
            throw new BadCredentialsException("Wrong password or username!");
        }
         setAuthenticationToContext(email);
    }
}
