package uz.doublem.foodrecipe.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import uz.doublem.foodrecipe.entity.Location;
import uz.doublem.foodrecipe.entity.User;
import uz.doublem.foodrecipe.enums.Role;
import uz.doublem.foodrecipe.payload.ResponseMessage;
import uz.doublem.foodrecipe.payload.user.SubscriptionResponseDTO;
import uz.doublem.foodrecipe.payload.user.UserEditDTO;
import uz.doublem.foodrecipe.payload.user.UserResponseDTO;
import uz.doublem.foodrecipe.repository.LocationRepository;
import uz.doublem.foodrecipe.repository.UserRepository;
import uz.doublem.foodrecipe.util.Util;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final LocationRepository locationRepository;
    public ResponseMessage editUser(User currentUser,UserEditDTO editDTO){
        User user = userRepository.findById(currentUser.getId()).orElseThrow(()->new RuntimeException("user not found!"));
        Optional<Location> optionalLocation = locationRepository.findByCountryAndCity(editDTO.getCountry(), editDTO.getCity());
        user.setName(editDTO.getName());
        user.setRole(Role.valueOf(editDTO.getRole()));
        if (optionalLocation.isEmpty()){
            Location location = new Location();
            location.setCountry(editDTO.getCountry());
            location.setCity(editDTO.getCity());
            user.setLocation(location);
            locationRepository.save(location);
        } else {
           Location loc = optionalLocation.get();
            user.setLocation(loc);
        }
            userRepository.save(user);
            UserResponseDTO dto = UserResponseDTO.builder()
                    .id(user.getId())
                    .role(user.getRole())
                    .email(user.getEmail())
                    .city(user.getLocation().getCity())
                    .country(user.getLocation().getCountry()).name(user.getName()).build();
            return ResponseMessage.builder().text("succesfully edited").status(true).data(dto).build();

    }

    public ResponseMessage following(Integer followerId,Integer followeeId){
        User follower = userRepository.findById(followerId).orElseThrow(() -> new RuntimeException("user not found!"));
        User followee = userRepository.findById(followeeId).orElseThrow(() -> new RuntimeException("user not found!"));
        Boolean follow = follower.follow(followee);
        System.out.println(follower);
        System.out.println(followee);
        if (!follow){
            return ResponseMessage.builder().text("Didn't feel like subscribing to this user")
                    .data(SubscriptionResponseDTO.builder()
                            .followerID(follower.getId())
                            .followeeID(followee.getId()).build())
                    .status(false)
                    .build();
        }
        userRepository.save(follower);
        userRepository.save(followee);
        return ResponseMessage.builder().text("you have successfully subscribed")
                .data(SubscriptionResponseDTO.builder()
                .followerID(follower.getId())
                        .followeeID(followee.getId()).build())
                .status(true)
                .build();
    }

    public ResponseMessage unfollow(Integer userID, Integer unfollowId){
        User follower = userRepository.findById(userID).orElseThrow(() -> new RuntimeException("user not found!"));
        User followee = userRepository.findById(unfollowId).orElseThrow(() -> new RuntimeException("user not found!"));
        Boolean unfollow = follower.unfollow(followee);
        if (!unfollow){
            return ResponseMessage.builder().text("there is no such user among your subscriptions")
                    .data(SubscriptionResponseDTO.builder()
                            .followerID(follower.getId())
                            .followeeID(followee.getId()).build())
                    .status(false)
                    .build();
        }
        userRepository.save(follower);
        userRepository.save(followee);
        return ResponseMessage.builder().text("you have unsubscribed from the user")
                .data(SubscriptionResponseDTO.builder()
                        .followerID(follower.getId())
                        .followeeID(followee.getId()).build())
                .status(true)
                .build();
    }
}
