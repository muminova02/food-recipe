package uz.doublem.foodrecipe.util;

import uz.doublem.foodrecipe.payload.ResponseMessage;

import java.util.Base64;

public interface Util {
    public static String base64Encoder(String password){
        byte[] byteArr = Base64.getEncoder().encode(password.getBytes());
        return new String(byteArr);
    }

    static ResponseMessage getResponseMes(boolean b, String message,Object obj){
        return ResponseMessage.builder().status(b).text(message).data(obj).build();
    }
}
