package za.co.mixtelematics;

import com.jayway.jsonpath.spi.json.GsonJsonProvider;
import com.mix.flow.containers.TxFlowContainer;
import com.mix.json.documents.command.JsonBaseCommand;
import com.mix.json.documents.components.JsonAuxiliary;
import com.mix.json.documents.components.JsonDestination;
import com.mix.json.documents.components.JsonDevice;
import com.mix.json.documents.response.JsonResponseList;
import com.mix.util.enums.common.CommandStatus;
import kafka.utils.Json;
import lombok.extern.java.Log;
import org.assertj.core.api.Assertions;
import org.hibernate.id.GUIDGenerator;
import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.json.GsonJsonParser;
import scala.math.BigInt;
import za.co.mixtelematics.exception.CommandResponseMapperGeneralException;
import za.co.mixtelematics.exception.CommandResponseMapperNotFoundException;
import za.co.mixtelematics.model.CommandResponseMapper;
import za.co.mixtelematics.repository.CommandResponseMapperRepository;
import za.co.mixtelematics.service.CommandResponseMapperService;
import za.co.mixtelematics.service.KafkaProducerService;

import javax.servlet.http.HttpServletRequest;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;

@RunWith(MockitoJUnitRunner.class)
@Log
public class CommandResponseMapperServiceLayerTest {
    private static final String BOX_NUMBER="boxNumber";
    private static final String AUDIT_USER="MixControl";
    private static final String MESSAGE_GUID="messageGuId";
    private static final BigInteger CORRELATION_ID= BigInteger.valueOf(2233L);
    @Mock
    CommandResponseMapperRepository repository;

    @Mock
    HttpServletRequest httpServletRequest;

    @Mock
    KafkaProducerService producerService;

    @InjectMocks
    CommandResponseMapperService mapperService;

    @Test
    public void  testGetUserDetailsByBoxNumberAndAuditUser_returnJsonResponseListSuccessfull(){
        Mockito.when(repository.getCommandResponseMapperByBoxNumberAndAndAuditUser(BOX_NUMBER,AUDIT_USER)).thenReturn(getCommandResponseMapper());
        JsonResponseList mapperList = mapperService.getUserDevices(BOX_NUMBER,AUDIT_USER);
        Assert.assertNotNull(mapperList);
        Assertions.assertThat(mapperList.getDestination().getUrl()).isSameAs("tcp://172.23.2.17:2507");

    }

    @Test(expected = NullPointerException.class)
    public void  testGetUserDetailsByBoxNumberAndAuditUser_returnNullPointerException(){
        Mockito.when(repository.getCommandResponseMapperByBoxNumberAndAndAuditUser(BOX_NUMBER,AUDIT_USER)).thenReturn(getCommandResponseMapper());
        JsonResponseList mapperList = mapperService.getUserDevices(BOX_NUMBER,AUDIT_USER);
        Assertions.assertThat(mapperList.getDevice().getSerialNumber()).isSameAs(BOX_NUMBER);
        Assertions.assertThat(mapperList.getDestination().getName()).isSameAs("mixControl");
    }

    @Test
    public void testGetAllDeviceFromCommandResponse_successfull(){
        Mockito.when(repository.findAll()).thenReturn(getCommandResponseMapper());

        JsonResponseList mapperList = mapperService.getAllDevices(httpServletRequest);
        Assert.assertTrue(mapperList!=null);
        Mockito.verify(repository).findAll();
        Assertions.assertThat(mapperList.getDestination().getName()).isSameAs("mixControl");

    }

    @Test
    public void testGetSpecificDeviceDetailsByBoxNumber_successfull() {
        Mockito.when(repository.getCommandResponseMapperByBoxNumber(BOX_NUMBER)).thenReturn(getCommandResponseMapper());
        JsonResponseList mapperList = mapperService.getSpecificDeviceDetails(BOX_NUMBER);
        Assert.assertTrue(mapperList!=null);
        Mockito.verify(repository).getCommandResponseMapperByBoxNumber(BOX_NUMBER);
    }

    @Test(expected = CommandResponseMapperNotFoundException.class)
    public void testGetDeviceNotFound()throws CommandResponseMapperNotFoundException{
        Mockito.when(repository.findAll()).thenThrow(new CommandResponseMapperNotFoundException("Device Not found"));
        JsonResponseList mapperList = mapperService.getAllDevices(httpServletRequest);
        Assert.assertTrue(mapperList!=null);
        Mockito.verify(repository).findAll();
    }

    @Test
    public void testDeleteAllDevices_successfull() {
        Mockito.when(repository.findAll()).thenReturn(getCommandResponseMapper());
        List<TxFlowContainer> txFlowContainer = mapperService.deleteAllDevices();
        Assert.assertNotNull(txFlowContainer);
        Assertions.assertThat(txFlowContainer.size()).isGreaterThan(0);
        Mockito.verify(repository).deleteAll();
    }
    @Test(expected = CommandResponseMapperNotFoundException.class)
    public void testDeleteAllDevicesNotFound()throws CommandResponseMapperNotFoundException {
        Mockito.when(repository.findAll()).thenReturn(new ArrayList<CommandResponseMapper>());
        List<TxFlowContainer>  txFlowContainers = mapperService.deleteAllDevices();
        Assert.assertNotNull(txFlowContainers);
        Mockito.verify(repository).deleteAll();
    }

    @Test
    public void testDeleteByBoxNumberAdnUser_successfull() throws CommandResponseMapperNotFoundException{
        Mockito.when(repository.getCommandResponseMapperByBoxNumberAndAndAuditUser(BOX_NUMBER,AUDIT_USER)).thenReturn(getCommandResponseMapper());
        List<TxFlowContainer> txFlowContainer = mapperService.deleteByBoxNumberAdnUser(BOX_NUMBER,AUDIT_USER);
        Assert.assertNotNull(txFlowContainer);
        Mockito.verify(repository).deleteCommandResponseMapperByBoxNumberAndAuditUser(BOX_NUMBER,AUDIT_USER);
    }

    @Test(expected = CommandResponseMapperNotFoundException.class)
    public void testDeleteByBoxNumberAdnUser_notFound()throws CommandResponseMapperNotFoundException {
        Mockito.when(repository.getCommandResponseMapperByBoxNumberAndAndAuditUser(BOX_NUMBER,AUDIT_USER)).thenReturn(new ArrayList<>());
        List<TxFlowContainer> txFlowContainer = mapperService.deleteByBoxNumberAdnUser(BOX_NUMBER,AUDIT_USER);
    }

    @Test
    public void testDeleteByBoxNumberAndMessageGuId_successful() {
        Mockito.when(repository.getCommandResponseMapperByBoxNumberAndMessageGuid(BOX_NUMBER,MESSAGE_GUID)).thenReturn(getCommandResponseMapper());
        List<TxFlowContainer> txFlowContainer = mapperService.deleteByBoxNumberAndMessageGuId(BOX_NUMBER,MESSAGE_GUID);
        Assert.assertNotNull(txFlowContainer);
        Mockito.verify(repository).deleteCommandResponseMapperByBoxNumberAndMessageGuid(BOX_NUMBER,MESSAGE_GUID);
    }

    @Test
    public void testDeleteDeviceByBoxNumber_successful() {
        Mockito.when(repository.getCommandResponseMapperByBoxNumber(BOX_NUMBER)).thenReturn(getCommandResponseMapper());
        List<TxFlowContainer> txFlowContainer = mapperService.deleteDeviceByBoxNumber(BOX_NUMBER);
        Assert.assertNotNull(txFlowContainer);
        Mockito.verify(repository).deleteCommandResponseMapperByBoxNumber(BOX_NUMBER);
    }

    @Test
    public void testUpdateRegisteredByBoxNumber_successful() {
        //Mockito.when(repository.save(Mockito.any(CommandResponseMapper.class))).thenReturn(addCommand());
        Mockito.when(repository.updateRegisteredByBoxNumber(CORRELATION_ID,BOX_NUMBER,AUDIT_USER)).thenReturn(1);
         mapperService.updateRegisteredByBoxNumber(BOX_NUMBER,getJsonBaseCommand());
        Mockito.verify(repository).updateRegisteredByBoxNumber(CORRELATION_ID,BOX_NUMBER,AUDIT_USER);
    }

    @Test(expected = CommandResponseMapperNotFoundException.class)
    public void testUpdateRegisteredByBoxNumber_failedToUpdate() {
        Mockito.when(repository.updateRegisteredByBoxNumber(CORRELATION_ID,BOX_NUMBER,AUDIT_USER)).thenReturn(0);
        mapperService.updateRegisteredByBoxNumber(BOX_NUMBER,getJsonBaseCommand());
        Mockito.verify(repository).updateRegisteredByBoxNumber(CORRELATION_ID,BOX_NUMBER,AUDIT_USER);
    }

    @Test
    public void testReserveGenericDeviceByBoxNumber_successful() {
        Mockito.when(repository.save(Mockito.any(CommandResponseMapper.class))).thenReturn(addCommand());
        mapperService.reserveGenericDeviceByBoxNumber(BOX_NUMBER,getJsonBaseCommand());
        Mockito.verify(repository).save(Mockito.any(CommandResponseMapper.class));
    }

    @Test(expected = CommandResponseMapperGeneralException.class)
    public void testReserveGenericDeviceByBoxNumber_jsonDestinationNull()throws CommandResponseMapperGeneralException {
        mapperService.reserveGenericDeviceByBoxNumber(BOX_NUMBER,getJsonBaseCommandNull());
        Mockito.verify(repository).save(Mockito.any(CommandResponseMapper.class));
    }


    private List<CommandResponseMapper> getCommandResponseMapper(){
        List<CommandResponseMapper> mapperList =new ArrayList<>();
        mapperList.add (CommandResponseMapper.builder()
                .auditUser(AUDIT_USER)
                .boxNumber(BOX_NUMBER)
                .messageGuid(GUIDGenerator.GENERATOR_NAME)
                .expiryDateTimeUtc(LocalDateTime.now().plusMinutes(3))
                .removalDateTimeUtc(LocalDateTime.now().plusMinutes(12))
                .status(CommandStatus.RESERVED.name())
                .destinationType("JMS_SONIC_MQ")
                .fileExtensionType("XML")
                .retry( 1)
                .url("tcp://172.23.2.17:2507")
                .nameOrPath("mixControl")
                .jmsType("QUEUE")
                .messageType("SIMPLE")
                .mobileMessageName("RequestPosition")
                .correlationId(2233L)
                .build());
        return mapperList;
    }

    private CommandResponseMapper addCommand(){
        return CommandResponseMapper.builder()
                .auditUser(AUDIT_USER)
                .boxNumber(BOX_NUMBER)
                .messageGuid(GUIDGenerator.GENERATOR_NAME)
                .expiryDateTimeUtc(LocalDateTime.now().plusMinutes(3))
                .removalDateTimeUtc(LocalDateTime.now().plusMinutes(12))
                .status(CommandStatus.RESERVED.name())
                .destinationType("JMS_SONIC_MQ")
                .fileExtensionType("XML")
                .retry( 1)
                .url("tcp://172.23.2.17:2507")
                .nameOrPath("mixControl")
                .jmsType("QUEUE")
                .messageType("SIMPLE")
                .mobileMessageName("RequestPosition")
                .correlationId(2233L)
                .build();
    }

    private JsonBaseCommand getJsonBaseCommand(){
        JsonBaseCommand jsonBaseCommand = new JsonBaseCommand();
        JsonAuxiliary auxiliary = new JsonAuxiliary();
        JsonDestination destination = new JsonDestination();
        destination.setDestinationType("JMS_SONIC_MQ");
        destination.setName("mixControl");
        destination.setUrl("tcp://172.23.2.17:2507");
        destination.setJmsType("QUEUE");
        destination.setRetry(true);
        destination.setMessageType("SIMPLE");
        destination.setFileExtensionType("XML");


        JsonDevice device = new JsonDevice();
        device.setMobileMessageName("RequestPosition");

        jsonBaseCommand.setDevice(device);
        jsonBaseCommand.setDestination(destination);
        jsonBaseCommand.setAuxiliary(auxiliary);
        auxiliary.setCorrelationId(2233L);
        jsonBaseCommand.setAuditUser(AUDIT_USER);
        jsonBaseCommand.setMessageGuid(GUIDGenerator.GENERATOR_NAME);
        return  jsonBaseCommand;
    }

    private JsonBaseCommand getJsonBaseCommandNull() {
        JsonBaseCommand jsonBaseCommand = new JsonBaseCommand();
        return jsonBaseCommand;
    }

}
