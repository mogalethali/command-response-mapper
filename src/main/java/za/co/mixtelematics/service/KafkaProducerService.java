package za.co.mixtelematics.service;

import com.mix.flow.containers.TxFlowContainer;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Log
@Service
public class KafkaProducerService {
    @Autowired
    private KafkaTemplate<String, TxFlowContainer> kafkaTemplate;
    @Value("${kafka.command.response.mapper.notifications}")
    private String commandResponsekafkaTopic;

    public void sendToKafkaTopic(TxFlowContainer txFlowContainer ) {
        log.info("Sending TxFlowContainer Json Serializer to "   + commandResponsekafkaTopic + " Topic");
        kafkaTemplate.send(commandResponsekafkaTopic, txFlowContainer);
    }

}
