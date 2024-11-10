package uz.doublem.foodrecipe.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import uz.doublem.foodrecipe.entity.Review;

import java.util.List;

public interface ReviewRepository_A extends JpaRepository<Review, Integer> {


    Page<Review> findAllByRecipe_Id(Integer id, Pageable pageable);



}
