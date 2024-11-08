package uz.doublem.foodrecipe.payload;

import jdk.dynalink.linker.LinkerServices;
import lombok.Data;
import uz.doublem.foodrecipe.entity.Attachment;
import uz.doublem.foodrecipe.entity.Category;

import java.util.List;

@Data
public class HomeDTO {
    String name;
    String email;
    Attachment attachment;
    List<Category> categories;
}
