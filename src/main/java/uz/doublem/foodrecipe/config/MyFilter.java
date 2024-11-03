package uz.doublem.foodrecipe.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Base64;
import java.util.concurrent.ExecutionException;

@Configuration
@RequiredArgsConstructor
public class MyFilter extends OncePerRequestFilter {
    private final JwtProvider jwtProvider;

    @Lazy
    private final UserDetailsService userDetailsService;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorization = request.getHeader("Authorization");
       try {


        if (authorization.startsWith("Basic ")){
            authorization.substring(6);
            byte[] decode = Base64.getDecoder().decode(authorization);
            String auth = new String(decode);
            String[] split = auth.split(":");
            setAuthenticationToContext(split[0],split[1]);
        }
if (authorization.startsWith("Bearer ")){
    authorization.substring(7);
    String email =jwtProvider.getSubject(authorization);
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
