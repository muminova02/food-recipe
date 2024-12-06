package uz.doublem.foodrecipe.config;

import feign.Body;
import feign.Headers;
import feign.Param;
import feign.RequestLine;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

//@Component
@FeignClient(name = "imgurClient", url = "https://api.imgur.com")
public interface ImgurClient {
    @PostMapping(value = "/3/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    Map<String, Object> uploadImage(@RequestPart("image") MultipartFile image,
                                    @RequestHeader("Authorization") String authorizationHeader);
}
