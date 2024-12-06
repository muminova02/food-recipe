package uz.doublem.foodrecipe.service;


import lombok.RequiredArgsConstructor;
import org.hibernate.engine.spi.Resolution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import uz.doublem.foodrecipe.entity.Attachment;
import uz.doublem.foodrecipe.payload.AttachmentDto;
import uz.doublem.foodrecipe.payload.ResponseMessage;
import uz.doublem.foodrecipe.repository.AttachmentRepository;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@Service
@RequiredArgsConstructor
public class AttachmentService
{
    private final ImageUpload imageUpload;

//    @Value("${photo.upload.path}")
//    private String photoUploadPath;

//    @Value("${server.base-url}")
//    private String baseUrl;

    private final AttachmentRepository attachmentRepository;

    private static final Logger logger = LoggerFactory.getLogger(AttachmentService.class);

    public Attachment save(MultipartFile file)
    {
        if (file.getContentType() != null && !(file.getContentType().equals("image/png") ||
                file.getContentType().equals("image/svg+xml") ||
                file.getContentType().equals("image/jpeg") ||
                file.getContentType().equals("video/mp4") ||
                file.getContentType().equals("video/avi") ||
                file.getContentType().equals("video/quicktime"))) // video/quicktime mov uchun MIME turi
        {
            throw new RuntimeException("Unsupported image or video type: " + file.getContentType());
        }


        try
        {
            Attachment attachment = attachmentRepository.save(new Attachment());
            saveToFile(file, attachment);
            logger.info("Attachment saved to database{}", attachment);
            return attachmentRepository.save(attachment);
        } catch (IOException e)
        {
            throw new RuntimeException("Attachment save failed: "+ file.getOriginalFilename(), e);
        }
    }

//    public ResponseEntity<byte[]> findById(String id)
//    {
//        try
//        {
//            Attachment attachment = attachmentRepository.findById(id).orElseThrow(() -> new RuntimeException("Photo or video not found with: " + id));
//
//            return getResponseEntity(attachment);
//
//        } catch (IOException e)
//        {
//            logger.error(e.getMessage());
//            throw new RuntimeException("Photo or video not found with: " + id);
//        }
//    }

    private static ResponseEntity<byte[]> getResponseEntity(Attachment attachment) throws IOException {
        Path attachmentPath = Paths.get(attachment.getPath());
        byte[] fileBytes = Files.readAllBytes(attachmentPath);

        switch (attachment.getType()) {
            case "image/png" -> {
                return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(fileBytes);
            }
            case "image/jpeg" -> {
                return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(fileBytes);
            }
            case "image/svg+xml" -> {
                HttpHeaders headers = new HttpHeaders();
                headers.add(HttpHeaders.CONTENT_TYPE, "image/svg+xml");
                return new ResponseEntity<>(fileBytes, headers, HttpStatus.OK);
            }
            case "video/mp4" -> {
                return ResponseEntity.ok().contentType(MediaType.valueOf("video/mp4")).body(fileBytes);
            }
            case "video/avi" -> {
                return ResponseEntity.ok().contentType(MediaType.valueOf("video/avi")).body(fileBytes);
            }
            case "video/quicktime" -> { // 'mov' uchun MIME turi
                HttpHeaders headers = new HttpHeaders();
                headers.add(HttpHeaders.CONTENT_TYPE, "video/quicktime");
                return new ResponseEntity<>(fileBytes, headers, HttpStatus.OK);
            }
            default -> {
                throw new RuntimeException("Unsupported file type: " + attachment.getType());
            }
        }
    }


    public Attachment getEmpty()
    {
        return attachmentRepository.save(new Attachment());
    }

//    public ResponseEntity<byte[]> findByName(String name)
//    {
//        try
//        {
//            Attachment attachment = attachmentRepository.findByName(name).orElseThrow(() -> new RuntimeException("Attachment not found: " + name));
//
//            return getResponseEntity(attachment);
//
//        } catch (IOException e)
//        {
//            logger.error(e.getMessage());
//            throw new RuntimeException("Attachment not found: " + name, e);
//        }
//    }

    public ResponseEntity<byte[]> findByName(String name)
    {
        try
        {
            Attachment attachment = attachmentRepository.findByName(name).orElseThrow(() -> new RuntimeException("Attachment not found: " + name));

            return getResponseEntity(attachment);

        } catch (IOException e)
        {
            logger.error(e.getMessage());
            throw new RuntimeException("Attachment not found: " + name, e);
        }
    }

    public ResponseMessage update(String id, MultipartFile file)
    {
        ResponseMessage response = new ResponseMessage();
        Attachment fromDb = attachmentRepository.findById(id).orElseThrow(() -> new RuntimeException("Photo not found by id: " + id));

        if (file.getContentType() != null && !(file.getContentType().equals("image/png") ||
                file.getContentType().equals("image/svg+xml") ||
                file.getContentType().equals("image/jpeg") ||
                file.getContentType().equals("video/mp4") ||
                file.getContentType().equals("video/avi") ||
                file.getContentType().equals("video/quicktime"))) // video/quicktime mov uchun MIME turi
        {
            throw new RuntimeException("Unsupported image or video type: " + file.getContentType() + " >>> only photo or video");
        }
        try
        {
//            if (fromDb.getPath() != null && !fromDb.getPath().isEmpty())
//                deleteFromFile(fromDb.getPath());

            saveToFile(file, fromDb);
            response.setText("Updated");
            response.setStatus(true);
            response.setData(new AttachmentDto(attachmentRepository.save(fromDb)));
            return response;
        } catch (IOException e)
        {
            throw new RuntimeException("Update failed: " + fromDb.getPath(), e);
        }

    }

//    private void saveToFile(MultipartFile file, Attachment attachment) throws IOException
//    {
//        String originalFileName = file.getOriginalFilename();
//
//        Path filePath = Paths.get(photoUploadPath +File.separator+ attachment.getId() + originalFileName.substring(originalFileName.lastIndexOf(".")));
//
//        file.transferTo(filePath);
//
//        attachment.setName(originalFileName);
//        attachment.setPath(filePath.toFile().getAbsolutePath());
//        attachment.setType(file.getContentType());
//        attachment.setUrl(baseUrl + "/attachment/id/" + attachment.getId());
//    }

    private void saveToFile(MultipartFile file, Attachment attachment) throws IOException
    {
        String originalFileName = file.getOriginalFilename();

        String url = imageUpload.uploadImage(file);
        attachment.setName(originalFileName);
        attachment.setPath(url);
        attachment.setType(file.getContentType());
        attachment.setUrl(url);
    }

//    public void deleteFromFile(String filePath) throws IOException
//    {
//        try
//        {
//            if (filePath != null)
//                Files.delete(Paths.get(filePath));
//        } catch (IOException e)
//        {
//            logger.error(e.getMessage());
//            throw new IOException(e);
//        }
//    }
    public void deleteFromFile(String filePath) throws IOException
    {
        try
        {
            if (filePath != null)
                Files.delete(Paths.get(filePath));
        } catch (IOException e)
        {
            logger.error(e.getMessage());
            throw new IOException(e);
        }
    }


    public ResponseMessage upload(List<MultipartFile> attachment)
    {
        if (attachment == null || attachment.isEmpty())
            throw new RuntimeException("Photo or video is null or empty");

        ResponseMessage response = new ResponseMessage();
        List<Attachment> attachments = new ArrayList<>();
        attachment.forEach(i -> attachments.add(save(i)));
        response.setData(attachments);
        response.setText("Uploaded attachment successfully");
        response.setStatus(true);
        return response;
    }
    @Transactional
    public ResponseMessage delete(String id)
    {
        ResponseMessage response = new ResponseMessage();
        Attachment attachment = attachmentRepository.findById(id).orElseThrow(() -> new RuntimeException("Photo not found by id: " + id));

        try
        {
            attachmentRepository.deleteById(attachment.getId());
            response.setText("Deleted");
        } catch (Exception e)
        {
            Attachment attachment11 = new Attachment();
            attachment11.setId(id);
            attachmentRepository.save(attachment11);
        }

//        try
//        {
//            deleteFromFile(attachment.getPath());
//            response.setText("Deleted on file");
//        } catch (IOException e)
//        {
//            logger.error(e.getMessage());
//            throw new RuntimeException("Delete failed: " + attachment.getPath());
//        }
        response.setStatus(true);
        response.setData(id);
        return response;
    }

    public ResponseEntity<byte[]> findByNameOrId(String nameOrId) {
        try
        {
            Attachment attachment = attachmentRepository.findByNameOrId(nameOrId).orElseThrow(() -> new RuntimeException("Attachment not found: " + nameOrId));

            return getResponseEntity(attachment);

        } catch (IOException e)
        {
            logger.error(e.getMessage());
            throw new RuntimeException("Attachment not found: " + nameOrId, e);
        }
    }
}