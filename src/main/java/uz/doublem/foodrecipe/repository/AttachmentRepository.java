package uz.doublem.foodrecipe.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.doublem.foodrecipe.entity.Attachment;

import java.util.Optional;
import java.util.UUID;

public interface AttachmentRepository extends JpaRepository<Attachment, String> {
    Optional<Attachment> findByName(String name);
}
