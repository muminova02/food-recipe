package uz.doublem.foodrecipe.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.doublem.foodrecipe.entity.Notification;
import uz.doublem.foodrecipe.entity.User;
import uz.doublem.foodrecipe.payload.ResponseMessage;
import uz.doublem.foodrecipe.repository.NotificationRepository;
import uz.doublem.foodrecipe.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final NotificationRepository notificationRepository;
    private UserRepository userRepository;

    public ResponseMessage getNotification(User user) {
        ResponseMessage responseMessage = new ResponseMessage();
        List<Notification> allByUserId = notificationRepository.findAllByUser_IdAndHasReadIsFalse(user.getId());
        if(allByUserId.isEmpty() ){
            responseMessage.setStatus(false);
            responseMessage.setText("You do not have any notifications");
        }
        responseMessage.setStatus(true);
        responseMessage.setData(allByUserId);
        return responseMessage;
    }

}
