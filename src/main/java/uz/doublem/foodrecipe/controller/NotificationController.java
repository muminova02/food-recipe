package uz.doublem.foodrecipe.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.doublem.foodrecipe.entity.User;
import uz.doublem.foodrecipe.payload.ResponseMessage;
import uz.doublem.foodrecipe.repository.UserRepository;
import uz.doublem.foodrecipe.service.NotificationService;
import uz.doublem.foodrecipe.util.Util;

import java.net.URI;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/notification")
public class NotificationController {
    final NotificationService notificationService;
    final UserRepository userRepository;

    @GetMapping
    public ResponseEntity<?> getNotifications(@RequestParam Integer size, @RequestParam Integer page,@RequestParam String read) {
        User currentUser = Util.getCurrentUser();
        ResponseMessage notification = notificationService.getNotification(currentUser,size,page,read);
        return ResponseEntity.status(notification.getStatus()?200:400).body(notification);
    }

    @GetMapping("/read-notification/{id}")
    public ResponseEntity<Void> getRecipeInSearchResult(@PathVariable Integer id) {
       Integer recipeId =  notificationService.updateNotification(id);
        URI redirectUri = URI.create("/api/recipeM/" + recipeId);
        return ResponseEntity.status(HttpStatus.SEE_OTHER).location(redirectUri).build();
    }

}
