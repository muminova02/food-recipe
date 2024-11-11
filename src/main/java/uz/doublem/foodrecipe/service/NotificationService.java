package uz.doublem.foodrecipe.service;


import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import uz.doublem.foodrecipe.entity.Notification;
import uz.doublem.foodrecipe.entity.Recipe;
import uz.doublem.foodrecipe.entity.User;
import uz.doublem.foodrecipe.payload.NotificationDto;
import uz.doublem.foodrecipe.payload.ResponseMessage;
import uz.doublem.foodrecipe.repository.NotificationRepository;
import uz.doublem.foodrecipe.repository.UserRepository;

import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final NotificationRepository notificationRepository;
    private UserRepository userRepository;

    public ResponseMessage getNotification(User user, Integer size, Integer page,String read) {
        PageRequest pageRequest = PageRequest.of(page, size);
        ResponseMessage responseMessage = new ResponseMessage();
        List<NotificationDto> list = List.of();
        if (Objects.equals(read, "READ")){
            Page<Notification> readIsTrue = notificationRepository.findAllByUser_IdAndHasReadIsTrue(user.getId(), pageRequest);
            list = set(readIsTrue);
            responseMessage.setText("This is your readed notifications");
        } else if (Objects.equals(read, "UNREAD")){
            Page<Notification> allByUserId = notificationRepository.findAllByUser_IdAndHasReadIsFalse(user.getId(), pageRequest);
             list = set(allByUserId);
            responseMessage.setText("This is your unread notifications");

        }else if (Objects.equals(read, "ALL")){
            Page<Notification> all = notificationRepository.findAllByUser_Id(user.getId(), pageRequest);
            responseMessage.setText("This is your all notifications");
            list = set(all);
        }else {
            responseMessage.setText("You do not have any notifications");
        }
        responseMessage.setData(list);
        return responseMessage;
    }

    private List<NotificationDto> set(Page<Notification> allByUserId) {
        return allByUserId.get().map(notification -> {
                    Recipe recipe = notification.getRecipe();
                    return NotificationDto.builder()
                            .title(recipe.getTitle())
                            .text(recipe.getDescription())
                            .recipeId(recipe.getId())
                            .createdAt(recipe.getCreatedAt().toString())
                            .build();
                }
        ).toList();
    }

    public void createNotification(Recipe recipe,User user) {
        Notification notification = new Notification();
        notification.setRecipe(recipe);
        notification.setUser(user);
        notification.setHasRead(false);
        notification.setText(recipe.getDescription());
        notificationRepository.save(notification);
    }



}
