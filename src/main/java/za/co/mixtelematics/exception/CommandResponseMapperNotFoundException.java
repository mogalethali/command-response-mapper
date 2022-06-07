package za.co.mixtelematics.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class CommandResponseMapperNotFoundException extends  RuntimeException{
    public CommandResponseMapperNotFoundException(String exception){
        super(exception);
    }


}
