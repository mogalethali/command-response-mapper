package za.co.mixtelematics.schedule;

import com.mix.flow.containers.TxFlowContainer;
import com.mix.json.documents.components.JsonDevice;
import com.mix.json.documents.enums.SubCommandActions;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import za.co.mixtelematics.model.CommandResponseMapper;
import za.co.mixtelematics.repository.CommandResponseMapperRepository;
import za.co.mixtelematics.service.KafkaProducerService;
import za.co.mixtelematics.util.CommandResponseMapperUtility;

import java.util.ArrayList;
import java.util.List;

@Log
@Component
public class CommandResponseMapperEventsProcessor {
    @Autowired
    private CommandResponseMapperRepository repository;
    @Autowired
    private KafkaProducerService producerService;

    @Scheduled(initialDelay =10000 ,fixedDelay = 10000)
    void process(){
        log.info("Get expired command to removed them");

        List<CommandResponseMapper> mapperList = repository.getExpiredCommands();
        if (mapperList!=null && !mapperList.isEmpty()){
            log.info("Removing expired command from command response mapper table");
            repository.removeExpiredCommand();
          buildListOfCommandDelete(mapperList);
        }

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
            txFlowList.add(CommandResponseMapperUtility.buildNotificationCommand(auditUser,device,correlationId, SubCommandActions.REMOVE));
            producerService.sendToKafkaTopic(CommandResponseMapperUtility.buildNotificationCommand(auditUser,device,correlationId,SubCommandActions.REMOVE));

        }
        return txFlowList;
    }
}
