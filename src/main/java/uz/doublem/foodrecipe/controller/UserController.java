package uz.doublem.foodrecipe.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.doublem.foodrecipe.payload.ResponseMessage;
import uz.doublem.foodrecipe.payload.user.UserEditDTO;
import uz.doublem.foodrecipe.service.UserService;
import uz.doublem.foodrecipe.util.Util;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    @PutMapping
    public ResponseEntity<?> editUser(@RequestBody UserEditDTO userEditDTO){
        ResponseMessage responseMessage = userService.editUser(Util.getCurrentUser(), userEditDTO);
     return ResponseEntity.status(responseMessage.getStatus()?200:400).body(responseMessage);
    }
    @PostMapping("/following/{followeeId}")
    public ResponseEntity<?> following(@PathVariable Integer followeeId){
        Integer id = Util.getCurrentUser().getId();
        System.out.println(id);
        ResponseMessage response = userService.following(id, followeeId);
        return ResponseEntity.status(response.getStatus()?200:400).body(response);
    }
    @PostMapping("/unfollowing/{unFolloweeId}")
    public ResponseEntity<?> unfollowing(@PathVariable Integer unFolloweeId){
        Integer id = Util.getCurrentUser().getId();
        System.out.println(id);
        ResponseMessage response = userService.unfollow(id, unFolloweeId);
        return ResponseEntity.status(response.getStatus()?200:400).body(response);
    }
}
