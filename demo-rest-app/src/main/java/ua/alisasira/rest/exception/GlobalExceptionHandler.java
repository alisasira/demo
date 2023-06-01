package ua.alisasira.rest.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> errorHandler(Exception e, HttpServletRequest req, HttpServletResponse res) {

        if (e instanceof ResponseStatusException) {
            ResponseEntity.BodyBuilder responseEntityBuilder = ResponseEntity
                    .status(((ResponseStatusException) e).getStatus());
            return responseEntityBuilder.build();
        }

        if (e instanceof IllegalStateException) {
            ResponseEntity.BodyBuilder responseEntityBuilder = ResponseEntity
                    .status(HttpStatus.BAD_REQUEST);
            return responseEntityBuilder.build();
        }

        ResponseEntity.BodyBuilder responseEntityBuilder = ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR);
        return responseEntityBuilder.build();
    }
}