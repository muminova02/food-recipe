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

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
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
        // Проверяем, не подписан ли уже пользователь
        if (!this.followers.contains(userToFollow)) {
            // Добавляем пользователя в followers текущего пользователя
            this.followers.add(userToFollow);

            // Добавляем текущего пользователя в список подписчиков у userToFollow
            userToFollow.addFollower(this);

            // Увеличиваем счетчик подписок
            this.following_count++;
            userToFollow.followers_count++;

            // Возвращаем true, если подписка успешна
            return true;
        }
        // Если уже подписан, возвращаем false
        return false;
    }

    private void addFollower(User user) {
        if (!this.followers.contains(user)) {
            this.followers.add(user);
        }
    }

    public Boolean unfollow(User userToUnfollow) {
        // Проверяем, был ли этот пользователь в списке подписчиков
        if (this.followers.contains(userToUnfollow)) {
            // Удаляем пользователя из followers текущего пользователя
            this.followers.remove(userToUnfollow);

            // Удаляем текущего пользователя из списка подписчиков у userToUnfollow
            userToUnfollow.removeFollower(this);

            // Уменьшаем счетчик подписок
            this.following_count--;
            userToUnfollow.followers_count--;

            // Возвращаем true, если отписка успешна
            return true;
        }
        // Если не был подписан, возвращаем false
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
