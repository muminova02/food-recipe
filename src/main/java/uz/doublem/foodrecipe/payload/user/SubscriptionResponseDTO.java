package uz.doublem.foodrecipe.payload.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class SubscriptionResponseDTO {
    private Integer followerID;
    private Integer followeeID;
}
