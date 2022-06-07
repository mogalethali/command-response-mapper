package za.co.mixtelematics;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import za.co.mixtelematics.model.CommandResponseMapper;
import za.co.mixtelematics.repository.CommandResponseMapperRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNull;
import java.util.List;

@ExtendWith(SpringExtension.class)
@Transactional
@SpringBootTest(classes = CommandResponseMapperApplication.class)
public class CommandResponseMapperRespositoryTest {
    @Autowired
    CommandResponseMapperRepository repository;

    @Test
    void testGetAllDevices_successful() {
        List<CommandResponseMapper> mapperList =repository.findAll();
       assertThat(mapperList).isEmpty();
    }
}
