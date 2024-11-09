package uz.doublem.foodrecipe.payload.search;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SearchDto {
    private String title;
    private String time;
    private Integer rate;
    private String category;
}
