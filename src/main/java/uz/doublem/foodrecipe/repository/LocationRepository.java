package uz.doublem.foodrecipe.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.doublem.foodrecipe.entity.Location;

import java.util.Optional;

public interface LocationRepository extends JpaRepository<Location,Integer> {
    Optional<Location> findByCountryAndCity(String country,String city);
}
