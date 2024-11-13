package uz.doublem.foodrecipe.payload;

import lombok.Builder;
import lombok.Data;
import uz.doublem.foodrecipe.entity.User;
import uz.doublem.foodrecipe.entity.View;

import java.util.List;
@Builder
public record RecipeDTO_A(String link, Double averageReiting,Long viewCount) {




}
