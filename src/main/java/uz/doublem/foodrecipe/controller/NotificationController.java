package uz.doublem.foodrecipe.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import uz.doublem.foodrecipe.entity.User;
import uz.doublem.foodrecipe.payload.ResponseMessage;
import uz.doublem.foodrecipe.repository.UserRepository;
import uz.doublem.foodrecipe.service.NotificationService;
import uz.doublem.foodrecipe.util.Util;

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



}
