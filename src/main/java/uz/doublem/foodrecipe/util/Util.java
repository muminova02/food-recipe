package uz.doublem.foodrecipe.util;

import java.util.Base64;

public class Util {
    public static String base64Encoder(String password){
        byte[] byteArr = Base64.getEncoder().encode(password.getBytes());
        return new String(byteArr);
    }
}
