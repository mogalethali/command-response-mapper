package za.co.mixtelematics.service;

import com.mix.core.messages.Header;
import com.mix.flow.containers.RxFlowContainer;
import com.mix.flow.containers.TxFlowContainer;
import com.mix.json.documents.JsonBaseMessage;
import com.mix.json.documents.command.JsonBaseCommand;
import com.mix.json.documents.components.JsonAuxiliary;
import com.mix.json.documents.components.JsonCommandResponse;
import com.mix.json.documents.components.JsonDestination;
import com.mix.json.documents.components.JsonDevice;
import com.mix.json.documents.enums.SubCommandActions;
import com.mix.json.documents.response.JsonBaseResponse;
import com.mix.json.documents.response.JsonResponseList;
import com.mix.mule4.containers.FlowVariables;
import com.mix.seda.controller.FlowCollection;
import com.mix.util.enums.common.CommandStatus;
import jdk.nashorn.internal.ir.annotations.Immutable;
import lombok.extern.java.Log;
import org.mule.extension.file.api.LocalFileAttributes;
import org.mule.extension.http.api.HttpRequestAttributes;
import org.mule.runtime.api.util.MultiMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import za.co.mixtelematics.exception.CommandResponseMapperDuplicateException;
import za.co.mixtelematics.exception.CommandResponseMapperGeneralException;
import za.co.mixtelematics.exception.CommandResponseMapperNotFoundException;
import za.co.mixtelematics.model.CommandResponseMapper;
import za.co.mixtelematics.repository.CommandResponseMapperRepository;
import za.co.mixtelematics.util.CommandResponseMapperUtility;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.math.BigInteger;
import java.security.cert.Certificate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Log
@Service
public class CommandResponseMapperService implements ICommandResponseMapper {

    @Value( "${command.response.expiry.interval.minutes}" )
    private int expiryTimeInMinutes;

    @Value( "${command.response.removal.interval.minutes}" )
    private int removalTimeInMinutes;

    @Value("${seda.flow.name}")
    private String flowName;

    @Autowired
    private HttpServletRequest context;

    @Autowired
    private KafkaProducerService producerService;
    @Autowired
    private CommandResponseMapperRepository commandResponseMapperRepository;

    @Override
    public JsonResponseList getAllDevices(HttpServletRequest httpServletRequest)throws CommandResponseMapperNotFoundException{
        FlowCollection.countSedaMessage(flowName);
        List<CommandResponseMapper> mapperList =commandResponseMapperRepository.findAll();
        //log.info("printing here ==="+commandResponseMapperRepository.getCommandResponseMapperNewSequenceNumber("10938945"));
        if (mapperList !=null && !mapperList.isEmpty()){
            return resultToJsonResonseList(mapperList);
        }
        throw new CommandResponseMapperNotFoundException(" No reserved box number found");

    }

    public JsonResponseList getUserDevices(String boxNumber,String user) throws CommandResponseMapperNotFoundException{
        log.info("Processing get all device by boxNumber  : "+boxNumber + " and audit user :" + user);
        List<CommandResponseMapper>  mapperList = commandResponseMapperRepository.getCommandResponseMapperByBoxNumberAndAndAuditUser(boxNumber,user);
        if (mapperList !=null && !mapperList.isEmpty()){
            return resultToJsonResonseList(mapperList);
        }
        throw new CommandResponseMapperNotFoundException("BoxNumber :"+boxNumber + " and audit user :"+user + " not found");

    }

    public CommandResponseMapper getSequenceNumber(String boxNumber){
       //return commandResponseMapperRepository.getCommandResponseMapperByBoxNumber(boxNumber);
        return  null;
    }

    public JsonResponseList getSpecificDeviceDetails(String boxNumber)throws CommandResponseMapperNotFoundException{
        log.info("Processing get all device by boxNumber  : "+boxNumber);

        List<CommandResponseMapper> mapperList =commandResponseMapperRepository.getCommandResponseMapperByBoxNumber(boxNumber);
        if (mapperList !=null && !mapperList.isEmpty()){
            return resultToJsonResonseList(mapperList);
        }
        throw new CommandResponseMapperNotFoundException("BoxNumber :"+boxNumber + " not found");

    }


    public List<TxFlowContainer> deleteAllDevices()throws CommandResponseMapperNotFoundException{
        log.info("Processing remove all reserved, registered command");

        List<CommandResponseMapper> mapperList =commandResponseMapperRepository.findAll();
        if (mapperList !=null && !mapperList.isEmpty()){
            commandResponseMapperRepository.deleteAll();
            return  buildListOfCommandDelete(mapperList);
        }
        throw new CommandResponseMapperNotFoundException("No reserved command found to delete");

    }

    public List<TxFlowContainer> deleteByBoxNumberAdnUser(String boxNumber, String user)throws CommandResponseMapperNotFoundException{
        List<CommandResponseMapper> mapperList = commandResponseMapperRepository.getCommandResponseMapperByBoxNumberAndAndAuditUser(boxNumber,user);
        if (mapperList !=null && !mapperList.isEmpty()){
            commandResponseMapperRepository.deleteCommandResponseMapperByBoxNumberAndAuditUser(boxNumber,user);
            return  buildListOfCommandDelete(mapperList);
        }
        throw new CommandResponseMapperNotFoundException("No reserved command found  to delete");
    }

    public List<TxFlowContainer> deleteByBoxNumberAndMessageGuId(String boxNumber, String messageGuid){
        List<CommandResponseMapper> mapperList = commandResponseMapperRepository.getCommandResponseMapperByBoxNumberAndMessageGuid(boxNumber,messageGuid);
        if (mapperList !=null && !mapperList.isEmpty()){
            commandResponseMapperRepository.deleteCommandResponseMapperByBoxNumberAndMessageGuid(boxNumber,messageGuid);
            return  buildListOfCommandDelete(mapperList);
        }
        throw new CommandResponseMapperNotFoundException("No reserved command found  to delete");
    }

    public List<TxFlowContainer> deleteDeviceByBoxNumber(String  boxNumber){
        List<CommandResponseMapper> mapperList = commandResponseMapperRepository.getCommandResponseMapperByBoxNumber(boxNumber);
        if (mapperList !=null && !mapperList.isEmpty()){
            commandResponseMapperRepository.deleteCommandResponseMapperByBoxNumber(boxNumber);
            return  buildListOfCommandDelete(mapperList);
        }
        throw new CommandResponseMapperNotFoundException("No reserved command found  to delete");
    }

    public void updateRegisteredByBoxNumber(String boxNumber, JsonBaseCommand baseCommand){

         int updated= commandResponseMapperRepository.updateRegisteredByBoxNumber(BigInteger.valueOf(baseCommand.getAuxiliary().getCorrelationId()),boxNumber,baseCommand.getAuditUser());
         if (updated>0){
             producerService.sendToKafkaTopic(CommandResponseMapperUtility.buildNotificationCommand(baseCommand.getAuditUser(),baseCommand.getDevice(),baseCommand.getAuxiliary().getCorrelationId(),SubCommandActions.ADD));
         }else
             throw new CommandResponseMapperNotFoundException("No record found to update");

    }

    public void reserveGenericDeviceByBoxNumber(String boxNumber, JsonBaseCommand jsonBaseCommand) throws CommandResponseMapperDuplicateException,CommandResponseMapperGeneralException {
        if (jsonBaseCommand.getDestination() !=null){
            boolean isMXcX =CommandResponseMapperUtility.isMXcx(boxNumber);
            commandResponseMapperRepository.save(processAddMapFromCommand(boxNumber,jsonBaseCommand, isMXcX));
            if (isMXcX){
                producerService.sendToKafkaTopic(CommandResponseMapperUtility.buildNotificationCommand(jsonBaseCommand.getAuditUser(),jsonBaseCommand.getDevice(),jsonBaseCommand.getAuxiliary().getCorrelationId(),SubCommandActions.ADD));
            }
        }else throw new CommandResponseMapperGeneralException("Either JsonDestination is null");


    }

    public void  reserveMxcxByBoxNumber(String boxNumber, JsonBaseCommand command){
        if (command.getDestination() !=null){
            commandResponseMapperRepository.save(processAddMapFromCommand(boxNumber,command,true));
            producerService.sendToKafkaTopic(CommandResponseMapperUtility.buildNotificationCommand(command.getAuditUser(),command.getDevice(),command.getAuxiliary().getCorrelationId(),SubCommandActions.ADD));

        }else throw new CommandResponseMapperGeneralException("Either JsonDestination is null");

    }

    public void reserveMix2000ByBoxNumber(String boxNumber, JsonBaseCommand command){
        if (command.getDestination() !=null){
            commandResponseMapperRepository.save(processAddMapFromCommand(boxNumber,command,false));
        }else throw new CommandResponseMapperGeneralException("Either JsonDestination is null");

    }


    public void receivedRoutingMessage(RxFlowContainer rxFlowContainer){
        validateParametersForRoutingOfMessage(rxFlowContainer);

    }

    private RxFlowContainer validateParametersForRoutingOfMessage(RxFlowContainer rxFlowContainer)throws  CommandResponseMapperGeneralException {
        try
        {
            JsonBaseMessage baseMessage = rxFlowContainer.getJsonBaseMessage();
            if (baseMessage != null) {
                JsonDevice device = baseMessage.getDevice();
                JsonAuxiliary auxiliary = baseMessage.getAuxiliary();

                if ((device != null) && (auxiliary != null))
                {
                    if ((device.getSerialNumber() != null) && (auxiliary.getCorrelationId() != null))
                        rxFlowContainer = determineRoutingOfMessage(rxFlowContainer, baseMessage, device.getSerialNumber(), auxiliary.getCorrelationId());
                    else
                        throw new CommandResponseMapperGeneralException("Either boxNumber or correlation Id is null");
                }
                else
                    throw new CommandResponseMapperGeneralException("Either JsonDevice or JsonAuxiliary is null");
            }
            else
                throw new CommandResponseMapperGeneralException("RxFlowContainer JsonBaseMessage is null");
        }
        catch (CommandResponseMapperGeneralException exception) {
            throw  new CommandResponseMapperGeneralException("Unknown received message "+ exception.getMessage());
        }

        return rxFlowContainer;
    }

    private RxFlowContainer  determineRoutingOfMessage(RxFlowContainer rxFlowContainer, JsonBaseMessage jsonBaseMessage, String boxNumber, long correlationId){
        CommandResponseMapper  responseMapper = commandResponseMapperRepository.getRoutingMapper(boxNumber,correlationId);
        if (responseMapper!=null){
            jsonBaseMessage.setMessageGuid(responseMapper.getMessageGuid());
            jsonBaseMessage.setAuditUser(responseMapper.getAuditUser());
            JsonDestination destination = new JsonDestination();
            jsonBaseMessage.setDestination(destination);
            JsonDestination replyTo = new JsonDestination();
            destination.setReplyTo(replyTo);
            replyTo.setDestinationType(responseMapper.getDestinationType());
            replyTo.setFileExtensionType(responseMapper.getFileExtensionType());
            replyTo.setRetry( responseMapper.getRetry()==1? true:false);
            replyTo.setRetryThreshold(responseMapper.getRetryThreshold());
            replyTo.setUrl(responseMapper.getUrl());
            replyTo.setName(responseMapper.getNameOrPath());
            replyTo.setJmsType(responseMapper.getJmsType());
            replyTo.setMessageType(responseMapper.getMessageType());
            replyTo.setConnectionId(responseMapper.getConnectionId());
            rxFlowContainer.addJsonMessage(jsonBaseMessage);
            commandResponseMapperRepository.updateRegisteredToResponded(boxNumber,correlationId);
        }else {
            throw new CommandResponseMapperNotFoundException(boxNumber + " received message using correlation id: " + correlationId + " not found - message discarded");
        }
        return rxFlowContainer;
    }


    private JsonResponseList resultToJsonResonseList(List<CommandResponseMapper> commandResponseMapperList){
        JsonResponseList jsonResponseList = new JsonResponseList();

        JsonDevice device = new JsonDevice();
        JsonDestination destination = new JsonDestination();
        JsonCommandResponse commandResponse = new JsonCommandResponse();
        for (CommandResponseMapper commandResponseMapper : commandResponseMapperList){
            JsonBaseResponse response = new JsonBaseResponse();
            getJsonBaseResponse(response, commandResponseMapper);
            getJsonDevice(device, commandResponseMapper);
            getJsonCommandResponse(commandResponse, commandResponseMapper);
            getJsonDestination(response, destination, commandResponseMapper);
            jsonResponseList.setCommandResponse(commandResponse);
            jsonResponseList.setDestination(destination);
            jsonResponseList.addDevice(device);
            jsonResponseList.addResponse(response);
        }
        return jsonResponseList;
    }

    private void getJsonBaseResponse(JsonBaseResponse response, CommandResponseMapper commandResponseMapper) {
        response.setAuditUser(commandResponseMapper.getAuditUser());
        response.setMessageGuid(commandResponseMapper.getMessageGuid());
    }

    private void getJsonDevice(JsonDevice device, CommandResponseMapper commandResponseMapper) {
        device.setSerialNumber(commandResponseMapper.getBoxNumber());
        device.setMobileMessageName(commandResponseMapper.getMobileMessageName());
    }

    private void getJsonCommandResponse(JsonCommandResponse commandResponse, CommandResponseMapper commandResponseMapper) {
        commandResponse.setExpiryDateTimeUtc(commandResponseMapper.getExpiryDateTimeUtc().toString());
        commandResponse.setRemovalDateTimeUtc(commandResponseMapper.getRemovalDateTimeUtc().toString());
        commandResponse.setConnectionId(commandResponseMapper.getConnectionId());
        commandResponse.setCorrelationId(commandResponseMapper.getCorrelationId().longValue());
        commandResponse.setCommandStatus(commandResponseMapper.getStatus());
    }

    private void getJsonDestination(JsonBaseResponse response, JsonDestination destination, CommandResponseMapper commandResponseMapper) {
        destination.setDestinationType(commandResponseMapper.getDestinationType());
        destination.setName(commandResponseMapper.getNameOrPath());
        destination.setUrl(commandResponseMapper.getUrl());
        destination.setJmsType(commandResponseMapper.getJmsType());
        destination.setFileExtensionType(commandResponseMapper.getFileExtensionType());
        // destination.setRetry(commandResponseMapper.getRetry());
        destination.setRetryThreshold(commandResponseMapper.getRetryThreshold());
        destination.setMessageType(commandResponseMapper.getMessageType());
        destination.setPassword(commandResponseMapper.getPassword());
        destination.setUserName(commandResponseMapper.getUserName());
        destination.setConnectionId(commandResponseMapper.getConnectionId());
        response.setDestination(destination);
    }


    private CommandResponseMapper processAddMapFromCommand(String boxNumber,JsonBaseCommand command,boolean isMXcX){
        deleteMappingsRespondedOrExpired(boxNumber,command.getAuditUser());
        return  CommandResponseMapper.builder()
               .auditUser(command.getAuditUser())
               .boxNumber(boxNumber)
               .messageGuid(command.getMessageGuid())
               .expiryDateTimeUtc(LocalDateTime.now().plusMinutes(expiryTimeInMinutes))
               .removalDateTimeUtc(LocalDateTime.now().plusMinutes(removalTimeInMinutes))
               .status(isMXcX? CommandStatus.REGISTERED.name():CommandStatus.RESERVED.name())
               .destinationType(command.getDestination().getDestinationType())
               .fileExtensionType(command.getDestination().getFileExtensionType())
               .retry( command.getDestination().getRetry()? 1 :0)
                //Nulable fields
                .retryThreshold(command.getDestination().getRetryThreshold()==null? null: command.getDestination().getRetryThreshold())
                .url(command.getDestination().getUrl()==null? null:command.getDestination().getUrl())
                .nameOrPath(command.getDestination().getName()!=null? command.getDestination().getName():null)
                .jmsType(command.getDestination().getJmsType()!=null?command.getDestination().getJmsType():null)
                .messageType(command.getDestination().getMessageType()!=null? command.getDestination().getMessageType():null)
                .mobileMessageName(command.getDevice().getMobileMessageName()!=null? command.getDevice().getMobileMessageName():null)
                .connectionId(command.getDestination().getConnectionId()!=null?command.getDestination().getConnectionId():null)
                .userName(command.getDestination().getUserName()!=null?command.getDestination().getUserName():null)
                .password(command.getDestination().getPassword()!=null? command.getDestination().getPassword():null)
                .correlationId(2233L)
                .build();
    }


    private void deleteMappingsRespondedOrExpired(String boxNumber, String auditUser) {
        commandResponseMapperRepository.deleteMappingsRespondedOrExpired(boxNumber,auditUser);
    }


    private List<TxFlowContainer> buildListOfCommandDelete(List<CommandResponseMapper> mapperList){
        List<TxFlowContainer> txFlowList = new ArrayList<>();
        JsonDevice device;
        Long correlationId;
        String auditUser;
        for (CommandResponseMapper responseMapper: mapperList) {
            device = new JsonDevice();
            device.setSerialNumber(responseMapper.getBoxNumber());
            device.setMobileMessageName(responseMapper.getMobileMessageName());
            auditUser =responseMapper.getAuditUser();
            correlationId =responseMapper.getCorrelationId()!=null? responseMapper.getCorrelationId():null;
            txFlowList.add(CommandResponseMapperUtility.buildNotificationCommand(auditUser,device,correlationId,SubCommandActions.REMOVE));
            producerService.sendToKafkaTopic(CommandResponseMapperUtility.buildNotificationCommand(auditUser,device,correlationId,SubCommandActions.REMOVE));

        }
        return txFlowList;
    }

}
