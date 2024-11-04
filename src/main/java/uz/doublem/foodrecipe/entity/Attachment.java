package uz.doublem.foodrecipe.entity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;


@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)

@Entity
public class Attachment
{
    private static final Logger LOGGER = LoggerFactory.getLogger(Attachment.class);

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @JsonIgnore
    String name;

    @JsonIgnore
    String path;


    @Column(unique = true)
    String url;

    @JsonIgnore
    String type;

    @PreRemove
    private void deleteFile()
    {
        if (this.path != null)
        {
            try
            {
                Files.delete(Paths.get(path));
            } catch (IOException e)
            {
                LOGGER.error("id:{} , Error deleting photo: {}", this.id, e.getMessage());
            }
        }
    }
}

