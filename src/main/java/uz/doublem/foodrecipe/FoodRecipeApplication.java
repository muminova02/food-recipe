package uz.doublem.foodrecipe;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class FoodRecipeApplication {

    public static void main(String[] args) {
        SpringApplication.run(FoodRecipeApplication.class, args);
    }

}
