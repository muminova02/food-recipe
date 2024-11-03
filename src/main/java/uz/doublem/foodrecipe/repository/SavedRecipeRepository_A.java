package uz.doublem.foodrecipe.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import uz.doublem.foodrecipe.entity.SavedReciepes;

import java.util.List;
import java.util.Optional;

public interface SavedRecipeRepository_A extends JpaRepository<SavedReciepes, Integer> {


    @Query(value = "select t from SavedReciepes t where t.owner=?1")
    Optional<List<SavedReciepes>> findByUserId(Integer id);
}
