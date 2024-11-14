package uz.doublem.foodrecipe.util;

import org.springframework.security.core.context.SecurityContextHolder;
import uz.doublem.foodrecipe.entity.User;
import uz.doublem.foodrecipe.payload.ResponseMessage;

import java.util.Base64;

public interface Util {
        static User getCurrentUser(){
           return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
       }

       static ResponseMessage getResponseMes(boolean b, String message, Object obj){
                return ResponseMessage.builder().status(b).data(obj).text(message).build();
       }
}
