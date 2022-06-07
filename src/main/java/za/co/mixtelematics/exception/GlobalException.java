package za.co.mixtelematics.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalException {
    @ExceptionHandler(CommandResponseMapperNotFoundException.class)
    public ResponseEntity<ExceptionResponse> resourceNotFound(CommandResponseMapperNotFoundException exception){
        ExceptionResponse response = new ExceptionResponse();
        response.setErrorCode(HttpStatus.NOT_FOUND.name());
        response.setErrorMessage(exception.getMessage());
        response.setTimestamp(LocalDateTime.now());
        return new ResponseEntity<ExceptionResponse>(response,HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(CommandResponseMapperDuplicateException.class)
    public  ResponseEntity<ExceptionResponse> resourceDuplicate(CommandResponseMapperDuplicateException exception){
        ExceptionResponse response = new ExceptionResponse();
        response.setErrorCode(HttpStatus.CONFLICT.name());
        response.setErrorMessage(exception.getMessage());
        response.setTimestamp(LocalDateTime.now());
        return new ResponseEntity<ExceptionResponse>(response,HttpStatus.CONFLICT);
    }

    @ExceptionHandler(CommandResponseMapperGeneralException.class)
    public  ResponseEntity<ExceptionResponse> generalError(CommandResponseMapperGeneralException exception){
        ExceptionResponse response = new ExceptionResponse();
        response.setErrorCode(HttpStatus.PRECONDITION_FAILED.name());
        response.setErrorMessage(exception.getMessage());
        response.setTimestamp(LocalDateTime.now());
        return new ResponseEntity<ExceptionResponse>(response,HttpStatus.PRECONDITION_FAILED);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ResponseEntity<ExceptionResponse> handleValidationError(MethodArgumentNotValidException exception) {
        ExceptionResponse response = new ExceptionResponse();
        response.setErrorCode(HttpStatus.BAD_REQUEST.name());
        response.setErrorMessage(exception.getMessage());
        response.setTimestamp(LocalDateTime.now());
        return new ResponseEntity<ExceptionResponse>(response,HttpStatus.BAD_REQUEST);
    }
}
