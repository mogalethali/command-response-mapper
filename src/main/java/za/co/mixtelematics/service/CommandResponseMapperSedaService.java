package za.co.mixtelematics.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mix.json.documents.JsonBaseMessage;
import com.mix.json.documents.command.JsonBaseCommand;
import com.mix.json.documents.response.JsonBaseResponse;
import com.mix.mule4.containers.FlowVariables;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import za.co.mixtelematics.config.CommandResponseMapperSedaConfig;
import za.co.mixtelematics.exception.CommandResponseMapperGeneralException;
import za.co.mixtelematics.model.SedaApiRequests;
import za.co.mixtelematics.util.CommandResponseMapperUtility;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Log
@Service
public class CommandResponseMapperSedaService {
    @Autowired
    private CommandResponseMapperSedaConfig commandResponseMapperSedaConfig;


    public Object getSingleSedaFlowStatus(HttpServletRequest httpServletRequest){

        FlowVariables flowVariables =(FlowVariables)CommandResponseMapperUtility.sedaApi(null,httpServletRequest, SedaApiRequests.GET_SINGLE_FLOW_STATUS.name());
       return  flowVariables;
    }

    public JsonBaseResponse getAllSedaFlowStatus(HttpServletRequest httpServletRequest){
        //FlowVariables flowVariables =(FlowVariables) CommandResponseMapperUtility.sedaApi(null,httpServletRequest, SedaApiRequests.GET_ALL_FLOW_STATUS.name());
        JsonBaseResponse jsonBaseResponsesonSedaManagement =(JsonBaseResponse) CommandResponseMapperUtility.sedaApi(null,httpServletRequest, SedaApiRequests.GET_ALL_FLOW_STATUS.name());;
        return  jsonBaseResponsesonSedaManagement;
    }

    public Object getSingleSedaFlowAndFolderStatus(HttpServletRequest httpServletRequest){
        return  CommandResponseMapperUtility.sedaApi(null,httpServletRequest, SedaApiRequests.GET_SINGLE_FLOW_AND_FOLDER_STATUS.name());

    }

    public Object resetAllSedaFlowStatus(HttpServletRequest httpServletRequest){
        return  CommandResponseMapperUtility.sedaApi(null,httpServletRequest, SedaApiRequests.RESET_STATUS_ON_ALL_FLOWS.name());

    }

    public Object resetSingleSedaFlowStatus(HttpServletRequest httpServletRequest){
        return  CommandResponseMapperUtility.sedaApi(null,httpServletRequest, SedaApiRequests.RESET_STATUS_ON_SINGLE_FLOW.name());

    }

    public Object getAllSedaFlowEventStatus(HttpServletRequest httpServletRequest){
        return  CommandResponseMapperUtility.sedaApi(null,httpServletRequest, SedaApiRequests.GET_ALL_FLOW_EVENT_STATUS.name());

    }
    public Object getSingleSedaFlowEventStatus(HttpServletRequest httpServletRequest){
        return  CommandResponseMapperUtility.sedaApi(null,httpServletRequest, SedaApiRequests.GET_SINGLE_FLOW_EVENT_STATUS.name());

    }

    public Object getSingleSedaFlowAndFolderEventStatus(HttpServletRequest httpServletRequest){
        return  CommandResponseMapperUtility.sedaApi(null,httpServletRequest, SedaApiRequests.GET_SINGLE_FLOW_AND_FOLDER_EVENT_STATUS.name());

    }
    public Object resetAllSeSedaFlowEventStatus(HttpServletRequest httpServletRequest){
        return  CommandResponseMapperUtility.sedaApi(null,httpServletRequest, SedaApiRequests.RESET_EVENT_STATUS_ON_ALL_FLOWS.name());

    }
    public Object resetSingleSedaFlowEventStatus(HttpServletRequest httpServletRequest){
        return  CommandResponseMapperUtility.sedaApi(null,httpServletRequest, SedaApiRequests.RESET_EVENT_STATUS_ON_SINGLE_FLOW.name());

    }

    public Object resetSingleSedaFlowAndFolderEventStatus(HttpServletRequest httpServletRequest){
        return CommandResponseMapperUtility.sedaApi(null,httpServletRequest,SedaApiRequests.RESET_EVENT_STATUS_ON_SINGLE_FLOW_AND_FOLDER.name());
    }
    public Object getAllSedaFlowEventConfiguration(HttpServletRequest httpServletRequest){
        return CommandResponseMapperUtility.sedaApi(null,httpServletRequest,SedaApiRequests.GET_ALL_CONFIGURATION.name());
    }

    public Object getSingleSedaFlowEventConfiguration(HttpServletRequest httpServletRequest){
        return CommandResponseMapperUtility.sedaApi(null,httpServletRequest,SedaApiRequests.GET_SINGLE_FLOW_EVENT_CONFIGURATION.name());
    }
    public Object getSingleSedaFlowAndFolderEventConfiguration(HttpServletRequest httpServletRequest){
        return CommandResponseMapperUtility.sedaApi(null,httpServletRequest,SedaApiRequests.GET_SINGLE_FLOW_AND_FOLDER_EVENT_CONFIGURATION.name());
    }

    public Object resetAllSedaFlowEventConfiguration(HttpServletRequest httpServletRequest){
        return CommandResponseMapperUtility.sedaApi(null,httpServletRequest,SedaApiRequests.DELETE_ALL_CONFIGURATION.name());
    }


    public Object resetSingleSedaFlowEventConfiguration(HttpServletRequest httpServletRequest){
        return CommandResponseMapperUtility.sedaApi(null,httpServletRequest,SedaApiRequests.DELETE_ALL_CONFIGURATION.name());
    }
    public Object createSingleSedaFlowEventConfiguration(HttpServletRequest httpServletRequest, JsonBaseMessage jsonBaseMessage) throws  JsonProcessingException {
        if (jsonBaseMessage.getMessageGuid()!=null && jsonBaseMessage.getAuditUser()!=null){
            String JsonBody =commandResponseMapperSedaConfig.jsonFlowContainerUtility().prettySerialise(jsonBaseMessage);
            return CommandResponseMapperUtility.sedaApi(JsonBody,httpServletRequest,SedaApiRequests.ADD_EVENT_CONFIGURATION.name());

        }else
            throw new CommandResponseMapperGeneralException("MessageGuid  or audit user is null");

    }

    public Object updateSingleSedaFlowEventConfiguration(HttpServletRequest httpServletRequest, JsonBaseMessage jsonBaseMessage) throws  JsonProcessingException {
        if (jsonBaseMessage.getMessageGuid()!=null && jsonBaseMessage.getAuditUser()!=null){
            String JsonBody =commandResponseMapperSedaConfig.jsonFlowContainerUtility().prettySerialise(jsonBaseMessage);
            return CommandResponseMapperUtility.sedaApi(JsonBody,httpServletRequest,SedaApiRequests.UPDATE_EVENT_CONFIGURATION.name());

        }else
            throw new CommandResponseMapperGeneralException("MessageGuid  or audit user is null");

    }

    public Object deleteSingleSedaFlowAndFolderEventConfiguration(HttpServletRequest httpServletRequest){
        return CommandResponseMapperUtility.sedaApi(null,httpServletRequest,SedaApiRequests.DELETE_SINGLE_FLOW_AND_FOLDER_EVENT.name());
    }

    public Object getStatusOfAllAsynchProcesses(HttpServletRequest httpServletRequest){
        return CommandResponseMapperUtility.sedaApi(null,httpServletRequest,SedaApiRequests.GET_THE_ASYNCHRONOUS_PROCESSES.name());
    }

    public Object abortAsynchProcesses(HttpServletRequest httpServletRequest){
        return CommandResponseMapperUtility.sedaApi(null,httpServletRequest,SedaApiRequests.DELETE_THE_ASYNCHRONOUS_PROCESSES.name());
    }

    public Object moveFileAsynchProcesses(HttpServletRequest httpServletRequest, JsonBaseMessage jsonBaseMessage) throws  JsonProcessingException {
        if (jsonBaseMessage.getMessageGuid()!=null && jsonBaseMessage.getAuditUser()!=null){
            String JsonBody =commandResponseMapperSedaConfig.jsonFlowContainerUtility().prettySerialise(jsonBaseMessage);
            return CommandResponseMapperUtility.sedaApi(JsonBody,httpServletRequest,SedaApiRequests.CREATE_A_MOVE_ASYNCHRONOUS_PROCESS.name());

        }else
            throw new CommandResponseMapperGeneralException("MessageGuid  or audit user is null");

    }
}
