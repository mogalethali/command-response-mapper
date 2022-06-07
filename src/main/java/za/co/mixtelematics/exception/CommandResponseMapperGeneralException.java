package za.co.mixtelematics.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.PRECONDITION_FAILED)
public class CommandResponseMapperGeneralException extends RuntimeException{
    public CommandResponseMapperGeneralException(String exception){
        super(exception);
    }
}
