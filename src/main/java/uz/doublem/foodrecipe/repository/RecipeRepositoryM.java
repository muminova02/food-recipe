package uz.doublem.foodrecipe.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import uz.doublem.foodrecipe.entity.Recipe;

import java.util.List;

public interface RecipeRepositoryM extends JpaRepository<Recipe, Integer> {
    List<Recipe> findByIdIn(List<Integer> ids);

    @Query("SELECT r FROM Recipe r " +
            "WHERE (:title IS NULL OR LOWER(r.title) LIKE LOWER(CONCAT('%', :title, '%'))) " +
            "AND (:rate IS NULL OR " +
            "      (:rate = 5 AND r.averageRating = 5) OR " +
            "      (:rate = 4 AND r.averageRating >= 4 AND r.averageRating < 5) OR " +
            "      (:rate = 3 AND r.averageRating >= 3 AND r.averageRating < 4)) " +
            "AND (:category IS NULL OR r.category.name = :category) " +
            "ORDER BY " +
            "   CASE WHEN :time = 'Newest' THEN r.createdAt END DESC, " +
            "   CASE WHEN :time = 'Oldest' THEN r.createdAt END ASC, " +
            "   CASE WHEN :time = 'Popularity' THEN r.viewsCount END DESC")
    Page<Recipe> findAllBySearch(
            @Param("title") String title,
            @Param("rate") Integer rate,
            @Param("time") String time,
            @Param("category") String category,
            Pageable pageable);
}

