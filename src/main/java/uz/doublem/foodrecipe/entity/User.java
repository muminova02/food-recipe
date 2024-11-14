package uz.doublem.foodrecipe.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "users")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    @Column(unique = true, nullable = false)
    private String email;
    @JsonIgnore
    private String password_hash;
    private Integer following_count =0;
    private Integer followers_count =0;
    private String verificationCode;
    private String resetPasswordCode;
    private Boolean verified = false;
    private String imageUrl;
    @OneToMany(cascade = CascadeType.REMOVE,orphanRemoval = true,fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<User> followers;
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @ToString.Exclude
    private Location location;
    private LocalDateTime verificationCodeGeneratedTime;
    private LocalDateTime resetPasswordCodeGeneratedTime;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getPassword() {
        return password_hash;
    }

    @Override
    public String getUsername() {
        return email;
    }

    public Boolean follow(User userToFollow) {
        if (!this.followers.contains(userToFollow)) {
            this.followers.add(userToFollow);
            userToFollow.addFollower(this);
            this.following_count++;
            userToFollow.following_count++;
            return true;
        }
        return false;
    }

    public Boolean unfollow(User userToUnfollow) {
        if (this.followers.contains(userToUnfollow)) {
            this.followers.remove(userToUnfollow);
            userToUnfollow.removeFollower(this);
            this.following_count--;
            userToUnfollow.following_count--;
            return true;
        }
        return false;
    }
    private void addFollower(User user) {
        this.followers.add(user);
    }

    private void removeFollower(User user) {
        this.followers.remove(user);
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id != null && id.equals(user.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    public String getImageUrl() {
        return null;
    }
}
