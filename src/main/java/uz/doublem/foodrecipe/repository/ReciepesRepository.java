package uz.doublem.foodrecipe.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.doublem.foodrecipe.entity.Recipe;

import java.util.List;

public interface ReciepesRepository extends JpaRepository<Recipe,Integer> {
        List<Recipe> getByCategoryId(Integer id);;
}
