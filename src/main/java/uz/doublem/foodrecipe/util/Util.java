package uz.doublem.foodrecipe.util;

import org.springframework.security.core.context.SecurityContextHolder;
import uz.doublem.foodrecipe.entity.User;
import uz.doublem.foodrecipe.payload.ResponseMessage;

import java.util.Base64;

public class Util {
       public static User getCurrentUser(){
           return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
       }
}
