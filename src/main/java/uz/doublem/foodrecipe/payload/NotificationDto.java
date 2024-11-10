package uz.doublem.foodrecipe.payload;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.swing.text.StyledEditorKit;
import java.sql.Time;
import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NotificationDto {
    private Integer userId;
    private Integer recipeId;
    private String title;
    private String text;
    private Boolean hasRead;
    private String createdAt;

}
