package uz.doublem.foodrecipe;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
//@EnableCaching
@EnableFeignClients
public class FoodRecipeApplication {

    public static void main(String[] args) {
        SpringApplication.run(FoodRecipeApplication.class, args);
    }

}
