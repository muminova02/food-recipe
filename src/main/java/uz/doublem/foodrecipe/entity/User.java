package uz.doublem.foodrecipe.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.w3c.dom.Text;
import uz.doublem.foodrecipe.enums.Role;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@Getter
@Setter
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
//    @Lob
    private String description;
    @Enumerated(EnumType.STRING)
    private Role role;
    @JsonIgnore
    private String password_hash;
    private Integer following_count =0;
    private Integer followers_count =0;
    private String verificationCode;
    private String resetPasswordCode;
    private Boolean verified = false;
    private String imageUrl;

    @OneToMany( orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonIgnore
    @ToString.Exclude
    private Set<User> followers;
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @ToString.Exclude
    private Location location;
    private LocalDateTime verificationCodeGeneratedTime;
    private LocalDateTime resetPasswordCodeGeneratedTime;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_" + role.name()));
        return authorities;
    }

    @Override
    public String getPassword() {
        return password_hash;
    }

    @Override
    public String getUsername() {
        return email;
    }



    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }

    public Boolean follow(User userToFollow) {
        if (!this.followers.contains(userToFollow)) {
            this.followers.add(userToFollow);
            this.following_count++;
            userToFollow.followers_count++;
            return true;
        }
        return false;
    }


    private void addFollower(User user) {
        if (!this.followers.contains(user)) {
            this.followers.add(user);
        }
    }

    public Boolean unfollow(User userToUnfollow) {
        if (this.followers.contains(userToUnfollow)) {
            this.followers.remove(userToUnfollow);
//            userToUnfollow.removeFollower(this);
            this.following_count--;
            userToUnfollow.followers_count--;
            return true;
        }
        return false;
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

    @Override
    public String toString() {
        return "User{id=" + id + ", name='" + name + "', email='" + email + "', role=" + role + "}";
    }
}
