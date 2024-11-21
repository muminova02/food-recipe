package uz.doublem.foodrecipe.controller;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;
import uz.doublem.foodrecipe.entity.User;
import uz.doublem.foodrecipe.enums.Role;
import uz.doublem.foodrecipe.payload.ResponseMessage;
import uz.doublem.foodrecipe.repository.UserRepository;
import uz.doublem.foodrecipe.service.HomeService;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/home")
public class HomeController {
    private final HomeService homeService;
    private final UserRepository userRepository;
    @GetMapping()
    @Transactional
    public ResponseEntity<?> home(){
        User principal =(User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = principal.getEmail();
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found!"));
        ResponseMessage responseMessage = homeService.homePage(user);
        return ResponseEntity.status(responseMessage.getStatus()?200:400).body(responseMessage);
    }

    @GetMapping("/category/{id}/recipes")
    public ResponseEntity<?> getRecipesByCategory(@PathVariable Integer id, @RequestParam Integer size,@RequestParam Integer page){
        ResponseMessage responseMessage = homeService.getRecipesByCategoryId(id,size,page);
        return ResponseEntity.status(responseMessage.getStatus()?200:400).body(responseMessage);
    }

    @GetMapping("/oauth2")
    @Transactional
    public ResponseEntity<?> homeOauth2(OAuth2AuthenticationToken authentication) {
        Map<String, Object> attributes = authentication.getPrincipal().getAttributes();
        String email = (String) attributes.get("email");
        ResponseMessage responseMessage = homeService.homePageOauth2(email, authentication.getPrincipal());
        return ResponseEntity.ok(responseMessage);
    }

    @GetMapping("/new-recipes")
    public ResponseEntity<?> getNewRecipes(@RequestParam Integer size,@RequestParam Integer page){
        ResponseMessage newRecipes = homeService.getNewRecipes(size,page);
        return ResponseEntity.status(newRecipes.getStatus()?200:400).body(newRecipes);
    }


}
