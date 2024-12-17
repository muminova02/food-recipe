package uz.doublem.foodrecipe.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import uz.doublem.foodrecipe.entity.User;
import uz.doublem.foodrecipe.payload.RecipeId;
import uz.doublem.foodrecipe.payload.ResponseMessage;
import uz.doublem.foodrecipe.payload.user.UserEditDTO;
import uz.doublem.foodrecipe.repository.UserRepository;
import uz.doublem.foodrecipe.service.AttachmentService;
import uz.doublem.foodrecipe.service.UserService;
import uz.doublem.foodrecipe.util.Util;

import java.util.List;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    @PreAuthorize("authentication.name == principal.username or hasRole('ROLE_ADMIN')")
    @PutMapping
//    @CacheEvict(value = "users", key = "#userEditDTO.email")
    public ResponseEntity<?> editUser(@RequestBody UserEditDTO userEditDTO){
        ResponseMessage responseMessage = userService.editUser(Util.getCurrentUser(), userEditDTO);
        return ResponseEntity.status(responseMessage.getStatus()?200:400).body(responseMessage);
    }
    @PreAuthorize("authentication.name == principal.username")
    @PostMapping("/following")
    public ResponseEntity<?> following(@RequestBody RecipeId followeeId){
        Integer id = Util.getCurrentUser().getId();
        System.out.println(id);
        ResponseMessage response = userService.following(id,followeeId.getId());
        return ResponseEntity.status(response.getStatus()?200:400).body(response);
    }
    @PreAuthorize("authentication.name == principal.username")
    @PostMapping("/unfollowing")
    public ResponseEntity<?> unfollowing(@RequestBody RecipeId unFolloweeId){
        Integer id = Util.getCurrentUser().getId();
        System.out.println(id);
        ResponseMessage response = userService.unfollow(id, unFolloweeId.getId());
        return ResponseEntity.status(response.getStatus()?200:400).body(response);
    }
//    @PreAuthorize("hasRole('ROLE_ADMIN')")
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
    public ResponseEntity<?> getUserContent(@RequestParam Integer page,@RequestParam Integer size){
        Integer id = Util.getCurrentUser().getId();
        ResponseMessage res = userService.getContentsByType(id,page,size);
        return ResponseEntity.status(res.getStatus()?200:400).body(res);
    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/all")
    public ResponseEntity<?> getAllUser(@RequestParam Integer size, Integer page){
        ResponseMessage res = userService.getAllUsers(size,page);
        return ResponseEntity.status(res.getStatus()?200:400).body(res);
    }



    @PostMapping(value = "/upload-image",consumes = {
            MediaType.MULTIPART_FORM_DATA_VALUE
    })
    public ResponseEntity<?> upload(
            @Parameter(
                    description = "Select picture on format .jpg or .png or .svg or video on format .mp4, .avi, .mov",
                    content = @Content(mediaType = "multipart/form-data",
                            schema = @Schema(
                                    type = "string",
                                    format = "binary",
                                    example = "image.png, or video.mp4"
                            )))
            @RequestPart(name = "file", required = false) MultipartFile attachment)
    {
        User currentUser = Util.getCurrentUser();
        ResponseMessage res = userService.uploadImage(attachment, currentUser);
        return ResponseEntity.status(201).body(res);
    }

    @GetMapping("/following")
    public ResponseEntity<?> getFollowing(){
        User currentUser = Util.getCurrentUser();
        ResponseMessage res = userService.getFollowing(currentUser);
        return ResponseEntity.status(res.getStatus()?200:400).body(res);
    }

    @GetMapping("/follower")
    public ResponseEntity<?> getFollower(){
        User currentUser = Util.getCurrentUser();
        ResponseMessage res = userService.getFollower(currentUser);
        return ResponseEntity.status(res.getStatus()?200:400).body(res);
    }


//    @PreAuthorize("hasRole('ROLE_ADMIN')")
//    @DeleteMapping("/redis-cache/{email}")
//    @CacheEvict(value = "users", key = "#email")
//    public ResponseEntity<?> deleteUser(@PathVariable String email) {
//        return ResponseEntity.status(200).body(ResponseMessage.builder()
//                .text("redis cache evict"));
//    }
//    @PreAuthorize("hasRole('ROLE_ADMIN')")
//    @DeleteMapping("/redis-cache-all")
//    @CacheEvict(value = "users", allEntries = true)
//    public ResponseEntity<?> clearUsersCache() {
//        return ResponseEntity.ok("All users cache cleared successfully!");
//    }
}
