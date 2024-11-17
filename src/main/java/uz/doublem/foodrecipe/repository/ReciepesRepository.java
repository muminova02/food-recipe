package uz.doublem.foodrecipe.repository;


import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.support.PageableExecutionUtils;
import uz.doublem.foodrecipe.entity.Recipe;

import java.util.List;

public interface ReciepesRepository extends JpaRepository<Recipe,Integer> {
        Page<Recipe> findByCategoryId(Integer categoryId, Pageable pageable);

        Page<Recipe> findByOrderByCreatedAtAscAverageRatingDesc(Pageable pageable);
}
