package microservices.user.api.handlers;

import microservices.user.api.exceptions.ApiException;
import microservices.user.services.exceptions.ServiceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

public class ApiExceptionHandler {
    @ExceptionHandler(ApiException.class)
    public ResponseEntity<String> restExeptionHandler(final ApiException e){
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ServiceException.class)
    public ResponseEntity<String> notFoundExeptionHandler(ServiceException e){
        return new ResponseEntity<>("User does not exists.", HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> internalServerError(Exception e) {
        return new ResponseEntity<>("Something went wrong." + " reason: " + e, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
