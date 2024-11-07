package uz.doublem.foodrecipe.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.doublem.foodrecipe.entity.Step;

public interface StepRepository extends JpaRepository<Step, Integer> {
}
