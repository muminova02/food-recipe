package uz.doublem.foodrecipe.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import uz.doublem.foodrecipe.entity.Attachment;
import uz.doublem.foodrecipe.entity.Location;
import uz.doublem.foodrecipe.entity.Recipe;
import uz.doublem.foodrecipe.entity.User;
import uz.doublem.foodrecipe.enums.Role;
import uz.doublem.foodrecipe.payload.FollowDTO;
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
    private final PasswordEncoder passwordEncoder;
    private final AttachmentService attachmentService;
    public ResponseMessage editUser(User currentUser,UserEditDTO editDTO){
        User user = userRepository.findById(currentUser.getId()).orElseThrow(()->new RuntimeException("user not found!"));
        user.setName(editDTO.getName()==null?user.getName():editDTO.getName());
        user.setRole(editDTO.getRole()==null?Role.CHEF:Role.valueOf(editDTO.getRole()));
        user.setDescription(editDTO.getDescription()==null?user.getDescription():editDTO.getDescription());
        user.setEmail(editDTO.getEmail()==null?user.getEmail():editDTO.getEmail());
        if (editDTO.getPassword()!=null||(!user.getPassword().equals(editDTO.getPassword()))) {
            String encode = passwordEncoder.encode(editDTO.getPassword());
            user.setPassword_hash(encode);
        }
        Optional<Location> optionalLocation = locationRepository.findByCountryAndCity(editDTO.getCountry(), editDTO.getCity());
        if (optionalLocation.isEmpty()){
            Location location = new Location();
            location.setCountry(editDTO.getCountry());
            location.setCity(editDTO.getCity());
            locationRepository.save(location);
            user.setLocation(location);
        } else {
            Location loc = optionalLocation.get();
            user.setLocation(loc);
        }
            userRepository.save(user);
            UserResponseDTO dto = UserResponseDTO.builder()
                    .id(user.getId())
                    .role(user.getRole())
                    .email(user.getEmail())
                    .location(user.getLocation())
                    .name(user.getName())
                    .description(user.getDescription())
                    .verified(user.getVerified())
                    .imageUrl(user.getImageUrl())
                    .followers_count(user.getFollowers_count())
                    .following_count(user.getFollowing_count())
                    .build();
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
                .userName(user.getName())
                .userRole(user.getRole().name())
                .authorImg(user.getImageUrl())
                .recipeNumber(countRecipe)
                .followersCount(user.getFollowers_count())
                .followingCount(user.getFollowing_count())
                .country(user.getLocation() == null ? "" : user.getLocation().getCountry())
                .city(user.getLocation() == null ?  "" : user.getLocation().getCity())
                .build();
        return getResponseMes(true,"user details for profile",build);
    }

    public ResponseMessage getContentsByType(Integer id, Integer userId, Integer page, Integer size) {
            if (id==null || userId == null){
                throw new RuntimeException("id or userId is null");
            }
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

        return getResponseMes(true,"recipe list for user",recipeList);
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

    public ResponseMessage uploadImage(MultipartFile attachment, User currentUser) {
        Attachment save = attachmentService.save(attachment);
        currentUser.setImageUrl(save.getUrl());
        userRepository.save(currentUser);
        return getResponseMes(true,"saved image",save.getUrl());
    }

    public ResponseMessage getFollowing(User currentUser) {
        List<User> following = userRepository.findFollowingByUserId(currentUser.getId());
        if (following.isEmpty()){
            return ResponseMessage.builder().status(true).text("no following").build();
        }
        List<FollowDTO> followDTOS =  following.stream().map(user -> {
            return FollowDTO.builder().userId(user.getId()).imgUrl(user.getImageUrl()).name(user.getName()).build();
        }).toList();
        return ResponseMessage.builder().status(true).data(followDTOS).build();
    }

    public ResponseMessage getFollower(User currentUser) {
        List<User> follower = userRepository.findFollowersByUserId(currentUser.getId());
        if (follower.isEmpty()){
            return ResponseMessage.builder().status(true).text("no follower").build();
        }
        List<FollowDTO> followDTOS =  follower.stream().map(user -> {
            return FollowDTO.builder().userId(user.getId()).imgUrl(user.getImageUrl()).name(user.getName()).build();
        }).toList();
        return ResponseMessage.builder().status(true).data(followDTOS).build();
    }
}
