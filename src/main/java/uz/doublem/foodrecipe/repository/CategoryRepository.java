package uz.doublem.foodrecipe.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.doublem.foodrecipe.entity.Category;

public interface CategoryRepository extends JpaRepository<Category,Integer> {

}
