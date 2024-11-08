package uz.doublem.foodrecipe.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import uz.doublem.foodrecipe.entity.Attachment;

import java.util.Optional;
import java.util.UUID;

public interface AttachmentRepository extends JpaRepository<Attachment, String> {
    Optional<Attachment> findByName(String name);

    @Query("SELECT a FROM Attachment a WHERE a.name = :nameOrId OR a.id = :nameOrId")
    Optional<Attachment> findByNameOrId(@Param("nameOrId") String nameOrId);
}
