package uz.doublem.foodrecipe.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull(message = "Comment is required")
    @Column(nullable = false)
    private String comment;

    // User who wrote the review; not serialized to JSON for privacy
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // Rating for the recipe, expected to be between 1 and 5
    @NotNull(message = "Rating is required")
    @Min(value = 1, message = "Rating must be at least 1")
    @Max(value = 5, message = "Rating must be at most 5")
    @Column(nullable = false)
    private Integer rating;

    // Number of likes and dislikes with default values
    @Builder.Default
    @Column(nullable = false)
    private Integer likeCount = 0;

    @Builder.Default
    @Column(nullable = false)
    private Integer dislikeCount = 0;

    // Reference to the associated Recipe entity, ensuring database consistency
    @NotNull(message = "Recipe ID is required")
    @Column(nullable = false)
    private String recipeId;  // Consider linking directly to a Recipe entity if it exists

    // Timestamp for when the review was created, automatically generated
    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private Timestamp createdAt;
}
