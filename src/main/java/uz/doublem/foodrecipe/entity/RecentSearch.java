package uz.doublem.foodrecipe.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;
@Entity
public class RecentSearch {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @CreationTimestamp
    private Timestamp created_at;


    @ManyToOne
    private User user;

    @ManyToOne
    private Recipe recipe;
}
