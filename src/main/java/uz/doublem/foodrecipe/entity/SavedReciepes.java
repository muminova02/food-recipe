package uz.doublem.foodrecipe.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class SavedReciepes {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "user_id",nullable = false)
    private User owner;
    @ManyToOne
    private Recipe recipe;
}
