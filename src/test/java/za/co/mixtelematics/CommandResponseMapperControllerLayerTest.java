package za.co.mixtelematics;

import com.fasterxml.jackson.databind.introspect.TypeResolutionContext;
import com.mix.json.documents.response.JsonResponseList;
import com.mix.util.enums.common.CommandStatus;
import org.hibernate.id.GUIDGenerator;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import za.co.mixtelematics.controller.CommandResponseMapperController;

import za.co.mixtelematics.model.CommandResponseMapper;
import za.co.mixtelematics.service.CommandResponseMapperService;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@RunWith(SpringRunner.class)
@WebMvcTest(CommandResponseMapperController.class)
public class CommandResponseMapperControllerLayerTest {
    private static final String BOX_NUMBER="boxNumber";
    private static final String AUDIT_USER="auditUser";

    @MockBean
    CommandResponseMapperService mapperService;
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    HttpServletRequest httpServletRequest;

    @Test
    public void testGetAllDevices()throws Exception {
        Mockito.when(mapperService.getAllDevices(httpServletRequest)).thenReturn(new JsonResponseList());

        mockMvc.perform(get("/command/response/devices")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
                //.andExpect(MockMvcResultMatchers.jsonPath("$.employees").exists())
                //.andExpect(MockMvcResultMatchers.jsonPath("$.employees[*].employeeId").isNotEmpty());
    }

//    @Test
//    public void testGetAllDevicesNotFound()throws Exception {
//        Mockito.when(mapperService.getAllDevices()).thenReturn(null);
//        mockMvc.perform(get("/command/response/devices")
//                .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isNotFound());
//        // then
//        //Assert.assertThat(andExpect(status().isNotFound())).isEqualTo(HttpStatus.NOT_FOUND.value());
//    }


    private List<CommandResponseMapper> getCommandResponseMapper(){
        List<CommandResponseMapper> mapperList =new ArrayList<>();
        mapperList.add (CommandResponseMapper.builder().auditUser(AUDIT_USER).boxNumber(BOX_NUMBER).messageGuid(GUIDGenerator.GENERATOR_NAME).expiryDateTimeUtc(LocalDateTime.now().plusMinutes(3)).removalDateTimeUtc(LocalDateTime.now().plusMinutes(12)).status(CommandStatus.RESERVED.name()).destinationType("JMS").fileExtensionType("xml").retry( 1).retryThreshold(1).url("localhost:8080").nameOrPath("mixControl").jmsType("xml").messageType("json").mobileMessageName("RequestPosition").connectionId("hfkffghfj").userName("Thali").password("Mix123").correlationId(2233L).build());
        return mapperList;
    }

    private CommandResponseMapper addCommand(){
        return CommandResponseMapper.builder().auditUser(AUDIT_USER).boxNumber(BOX_NUMBER).messageGuid(GUIDGenerator.GENERATOR_NAME).expiryDateTimeUtc(LocalDateTime.now().plusMinutes(3)).removalDateTimeUtc(LocalDateTime.now().plusMinutes(12)).status(CommandStatus.RESERVED.name()).destinationType("JMS").fileExtensionType("xml").retry( 1).retryThreshold(1).url("localhost:8080").nameOrPath("mixControl").jmsType("xml").messageType("json").mobileMessageName("RequestPosition").connectionId("hfkffghfj").userName("Thali").password("Mix123").correlationId(2233L).build();
    }

}
