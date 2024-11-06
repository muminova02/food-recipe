package uz.doublem.foodrecipe.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Recipe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String title;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Timestamp createdAt;

    // Assuming `Category` and `User` are entities
    @ManyToOne
    @JoinColumn(name = "category_id", referencedColumnName = "id")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "author_id", referencedColumnName = "id")
    private User author;

    private String link;

    @Column(name = "average_rating")
    private Integer averageRating;

    private String description;

    @Column(name = "cooking_time")
    private String cookingTime;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "video_url")
    private String videoUrl;

    @OneToMany(mappedBy = "recipe",fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<View> views;

    @OneToMany(mappedBy = "recipe", cascade = CascadeType.REMOVE,orphanRemoval = true,fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<IngredientAndQuantity> ingredientAndQuantities;

    @OneToMany(mappedBy = "recipe", cascade = CascadeType.REMOVE,orphanRemoval = true,fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Step> steps;
}
