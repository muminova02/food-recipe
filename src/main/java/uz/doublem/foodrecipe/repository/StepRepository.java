package uz.doublem.foodrecipe.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.doublem.foodrecipe.entity.Step;

import java.util.List;

public interface StepRepository extends JpaRepository<Step, Integer> {

    List<Step> findByRecipe_Id(Integer id);
}
