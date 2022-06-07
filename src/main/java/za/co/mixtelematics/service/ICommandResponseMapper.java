package za.co.mixtelematics.service;

import com.mix.json.documents.command.JsonBaseCommand;
import com.mix.json.documents.response.JsonResponseList;
import za.co.mixtelematics.exception.CommandResponseMapperNotFoundException;
import za.co.mixtelematics.model.CommandResponseMapper;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface ICommandResponseMapper {
    JsonResponseList getAllDevices(HttpServletRequest httpServletRequest) throws CommandResponseMapperNotFoundException;
}
