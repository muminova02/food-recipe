package uz.doublem.foodrecipe.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import uz.doublem.foodrecipe.entity.Category;

public interface CategoryRepository extends JpaRepository<Category,Integer> {
    @Modifying
    @Transactional
    @Query("UPDATE Category c SET c.name = :name WHERE c.id = :id")
    void updateName(@Param("id") Integer id, @Param("name") String name);

}
