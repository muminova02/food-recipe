package uz.doublem.foodrecipe.controller;


import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import uz.doublem.foodrecipe.entity.Attachment;
import uz.doublem.foodrecipe.payload.AttachmentDto;
import uz.doublem.foodrecipe.payload.ResponseMessage;
import uz.doublem.foodrecipe.service.AttachmentService;


import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "Photo - Фото")
@RequestMapping("/api/photo")
public class AttachmentController
{
    private final AttachmentService attachmentService;

    @Operation(summary = "Upload photo  or Video to server - Загрузить фото на сервер")
    @PostMapping(consumes = {
            MediaType.MULTIPART_FORM_DATA_VALUE
    })
    public ResponseEntity<?> upload(
            @Parameter(
                    description = "Select picture on format .jpg or .png or .svg or video on format .mp4, .avi, .mov",
                    content = @Content(mediaType = "multipart/form-data",
                            schema = @Schema(
                                    type = "string",
                                    format = "binary",
                                    example = "image.png, or video.mp4"
                            )))
            @RequestPart(name = "file", required = false) List<MultipartFile> photo)
    {
        return ResponseEntity.status(201).body(attachmentService.upload(photo));
    }

    @GetMapping("/{name}")
    @Operation(summary = "Show a photo or video - Показать фото")
    @Hidden
    public ResponseEntity<byte[]> getAttachmentByName(@PathVariable(name = "name") String name)
    {
        return attachmentService.findByName(name);
    }




    @GetMapping("/{id}")
    public ResponseEntity<byte[]> getAttachmentById(@PathVariable(name = "id") String  id)
    {
        return attachmentService.findById(id);
    }




    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Attachment updated successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AttachmentDto.class)))})
    @Operation(summary = "Replace attachment to another")
    @Parameters({
            @Parameter(name = "id", description = "Show 'id' in url path for which attachment is updated ", required = true),
    })
    @PutMapping(value = "/{id}", consumes = {"multipart/form-data"})
    public ResponseEntity<?> updateAttachment(
            @PathVariable(name = "id")
            String id,

            @Parameter(
                    content = @Content(mediaType = "multipart/form-data",
                            schema = @Schema(
                                    type = "string",
                                    format = "binary",
                                    example = "image.png, or video.mp4"
                            )))
            @RequestPart(name = "new-attachment", required = false) MultipartFile newAttachment)
    {
        return ResponseEntity.status(200).body(attachmentService.update(id, newAttachment));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete photo or video from table and also file system")
    public ResponseEntity<?> deleteAttachment(
            @PathVariable
            @Parameter(description = "select 'id' which attachment must deleted ")
            String  id)
    {
        return ResponseEntity.status(200).body(attachmentService.delete(id));
    }
}

