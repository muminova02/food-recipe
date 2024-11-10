package uz.doublem.foodrecipe.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import uz.doublem.foodrecipe.entity.Notification;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Integer> {
    Page<Notification> findAllByUser_IdAndHasReadIsFalse(Integer id, Pageable pageable);
    Page<Notification> findAllByUser_IdAndHasReadIsTrue(Integer id, Pageable pageable);
    Page<Notification> findAllByUser_Id(Integer id, Pageable pageable);

}
