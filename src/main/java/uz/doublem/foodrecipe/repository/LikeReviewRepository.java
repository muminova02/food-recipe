package uz.doublem.foodrecipe.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import uz.doublem.foodrecipe.entity.LikeReview;

import java.util.Optional;

public interface LikeReviewRepository extends JpaRepository<LikeReview, Integer> {
    @Query("SELECT lr.isLike FROM LikeReview lr WHERE lr.user.id = :userId AND lr.review.id = :reviewId")
    Boolean fetchLikeReviewById(@Param("userId") Integer userId, @Param("reviewId") Integer reviewId);

    Optional<LikeReview> findByReview_IdAndUser_Id(Integer id, Integer userId);
}
