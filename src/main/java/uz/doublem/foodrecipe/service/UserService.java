package uz.doublem.foodrecipe.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import uz.doublem.foodrecipe.entity.Location;
import uz.doublem.foodrecipe.entity.Recipe;
import uz.doublem.foodrecipe.entity.User;
import uz.doublem.foodrecipe.enums.Role;
import uz.doublem.foodrecipe.payload.ResponseMessage;
import uz.doublem.foodrecipe.payload.SavedResponseDto;
import uz.doublem.foodrecipe.payload.profile.ProfileDto;
import uz.doublem.foodrecipe.payload.user.*;
import uz.doublem.foodrecipe.repository.LocationRepository;
import uz.doublem.foodrecipe.repository.RecipeRepositoryM;
import uz.doublem.foodrecipe.repository.UserRepository;
import static uz.doublem.foodrecipe.util.Util.*;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final LocationRepository locationRepository;
    private final RecipeRepositoryM recipeRepositoryM;
    public ResponseMessage editUser(User currentUser,UserEditDTO editDTO){
        User user = userRepository.findById(currentUser.getId()).orElseThrow(()->new RuntimeException("user not found!"));
        Optional<Location> optionalLocation = locationRepository.findByCountryAndCity(editDTO.getCountry(), editDTO.getCity());
        user.setName(editDTO.getName());
        user.setRole(Role.valueOf(editDTO.getRole()));
        user.setDescription(editDTO.getDescription()!=null?editDTO.getDescription():"");
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

    public ResponseMessage getUser(Integer userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("user not found!"));
        Integer countRecipe = recipeRepositoryM.countAllByAuthor_Id(user.getId());
        ProfileDto build = ProfileDto.builder()
                .userId(user.getId())
                .description(user.getDescription())
                .userName(user.getName())
                .userRole(user.getRole().name())
                .authorImg(user.getImageUrl())
                .recipeNumber(countRecipe)
                .followersCount(user.getFollowers_count())
                .followingCount(user.getFollowing_count())
                .build();
        return getResponseMes(true,"user details for profile",build);
    }

    public ResponseMessage getContentsByType(String type, Integer id, Integer userId, Integer page, Integer size) {
        if (type.equals("recipe")){
            if (id==null || userId == null){
                throw new RuntimeException("id or userId is null");
            }
            if (id.equals(userId)){
                PageRequest pageRequest = PageRequest.of(page, size);
                Page<Recipe> allByAuthorId = recipeRepositoryM.findAllByAuthor_Id(userId, pageRequest);
                List<SavedResponseDto> recipeList = allByAuthorId.getContent().stream().map(recipe ->{
                   return SavedResponseDto.builder()
                            .id(recipe.getId())
                            .description(recipe.getDescription())
                            .title(recipe.getTitle())
                            .author(recipe.getAuthor().getName())
                            .imageUrl(recipe.getImageUrl())
                            .averageRating(recipe.getAverageRating())
                            .time(recipe.getCookingTime())
                            .build();
                }).toList();
            }
        }
        return null;
    }

    public ResponseMessage getAllUsers(Integer size, Integer page) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<User> all = userRepository.findAll(pageRequest);
        List<UserValidDTO> userDTOList = all.get().map(user -> {
            return UserValidDTO.builder()
                    .id(user.getId())
                    .role(user.getRole().name())
                    .name(user.getName())
                    .email(user.getEmail())
                    .build();
        }).toList();
        return getResponseMes(true,"All user list",userDTOList);
    }
}
