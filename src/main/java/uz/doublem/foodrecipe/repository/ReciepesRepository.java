package uz.doublem.foodrecipe.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import uz.doublem.foodrecipe.entity.Recipe;

import java.util.List;

public interface ReciepesRepository extends JpaRepository<Recipe,Integer> {
        Page<Recipe> findByCategoryId(Integer categoryId, Pageable pageable);
}
