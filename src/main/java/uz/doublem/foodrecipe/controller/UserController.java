package uz.doublem.foodrecipe.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CachePut;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import uz.doublem.foodrecipe.entity.User;
import uz.doublem.foodrecipe.payload.ResponseMessage;
import uz.doublem.foodrecipe.payload.user.UserEditDTO;
import uz.doublem.foodrecipe.repository.UserRepository;
import uz.doublem.foodrecipe.service.UserService;
import uz.doublem.foodrecipe.util.Util;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    @PreAuthorize("authentication.name == principal.username or hasRole('ROLE_ADMIN')")
    @PutMapping
    @CachePut(value = "users", key = "#userEditDTO.email")
    public ResponseEntity<?> editUser(@RequestBody UserEditDTO userEditDTO){
        ResponseMessage responseMessage = userService.editUser(Util.getCurrentUser(), userEditDTO);
        return ResponseEntity.status(responseMessage.getStatus()?200:400).body(responseMessage);
    }
    @PreAuthorize("authentication.name == principal.username")
    @PostMapping("/following/{followeeId}")
    public ResponseEntity<?> following(@PathVariable Integer followeeId){
        Integer id = Util.getCurrentUser().getId();
        System.out.println(id);
        ResponseMessage response = userService.following(id, followeeId);
        return ResponseEntity.status(response.getStatus()?200:400).body(response);
    }
    @PreAuthorize("authentication.name == principal.username")
    @PostMapping("/unfollowing/{unFolloweeId}")
    public ResponseEntity<?> unfollowing(@PathVariable Integer unFolloweeId){
        Integer id = Util.getCurrentUser().getId();
        System.out.println(id);
        ResponseMessage response = userService.unfollow(id, unFolloweeId);
        return ResponseEntity.status(response.getStatus()?200:400).body(response);
    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/byId")
    public ResponseEntity<?> getUser(@RequestParam Integer userId){
//        Integer id = Util.getCurrentUser().getId();
        System.out.println(userId);
        ResponseMessage res = userService.getUser(userId);
        return ResponseEntity.status(res.getStatus()?200:400).body(res);
    }
    @PreAuthorize("authentication.name == principal.username or hasRole('ROLE_ADMIN')")
    @GetMapping("/my-profile")
    public ResponseEntity<?> myProfile(){
        ResponseMessage user = userService.getUser(Util.getCurrentUser().getId());
        return ResponseEntity.status(user.getStatus()?200:400).body(user);
    }
    @PreAuthorize("authentication.name == principal.username")
    @GetMapping("/profile-content")
    public ResponseEntity<?> getUserContent( @RequestParam Integer userId,@RequestParam Integer page,@RequestParam Integer size){
        Integer id = Util.getCurrentUser().getId();
        ResponseMessage res = userService.getContentsByType(id,userId,page,size);
        return ResponseEntity.status(res.getStatus()?200:400).body(res);
    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/all")
    public ResponseEntity<?> getAllUser(@RequestParam Integer size, Integer page){
        ResponseMessage res = userService.getAllUsers(size,page);
        return ResponseEntity.status(res.getStatus()?200:400).body(res);
    }
}
