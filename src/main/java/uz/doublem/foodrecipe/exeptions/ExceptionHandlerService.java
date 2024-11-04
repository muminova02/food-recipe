package uz.doublem.foodrecipe.exeptions;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import uz.doublem.foodrecipe.payload.ResponseMessage;

@ControllerAdvice
public class ExceptionHandlerService {
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handelException(Exception e) {
        ResponseMessage response = ResponseMessage
                .builder()
                .status(false)
                .data(e.getMessage())
                .text("something went wrong")
                .build();

        return ResponseEntity.status(400).body(response);
    }
}
