package uz.doublem.foodrecipe.payload;

import lombok.*;
import lombok.experimental.FieldDefaults;
import uz.doublem.foodrecipe.entity.Attachment;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AttachmentDto
{
    String id;

    String url;


    public AttachmentDto(Attachment entity)
    {
        if (entity == null)
            return;
        this.id = entity.getId();
        this.url = entity.getUrl();
    }
}
