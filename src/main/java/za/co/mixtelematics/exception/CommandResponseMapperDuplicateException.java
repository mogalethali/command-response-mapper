package za.co.mixtelematics.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT)
public class CommandResponseMapperDuplicateException extends RuntimeException{
}
