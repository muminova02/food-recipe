package uz.doublem.foodrecipe.entity;

import jakarta.persistence.*;

import java.sql.Timestamp;
@Entity
public class RecentSearch {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Timestamp created_at;
    @ManyToOne
    private User user;
    @ManyToOne
    private Recipe recipe;
}
