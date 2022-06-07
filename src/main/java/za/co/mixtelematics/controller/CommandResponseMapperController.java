package za.co.mixtelematics.controller;

import com.mix.flow.containers.RxFlowContainer;
import com.mix.flow.containers.TxFlowContainer;
import com.mix.json.documents.command.JsonBaseCommand;
import com.mix.json.documents.response.JsonResponseList;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;


import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.java.Log;
import org.mule.extension.http.api.HttpRequestAttributes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import za.co.mixtelematics.exception.CommandResponseMapperDuplicateException;
import za.co.mixtelematics.exception.CommandResponseMapperNotFoundException;
import za.co.mixtelematics.model.CommandResponseMapper;
import za.co.mixtelematics.service.CommandResponseMapperService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Stream;

@Log
@RestController
@ApiOperation(value = "Command Response Mapper  controller", tags = "Command Response Mapper  controller" )
@RequestMapping("/command/response")
public class CommandResponseMapperController {

    @Autowired
    private CommandResponseMapperService responseMapperService;

    @GetMapping("/devices/{boxNumber}/users/{user}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Retrieve specific device detail for user using boxnumber",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = JsonResponseList.class))}),
            @ApiResponse(responseCode = "412", description = "Precondition failed")
    })
    @Operation(summary = "Get a Specific Device and User's details")
    @CircuitBreaker(name = "command-response-mapper")
    public JsonResponseList getUserDevices(@PathVariable("boxNumber") String boxNumber, @PathVariable("user") String user) throws CommandResponseMapperNotFoundException {

        return responseMapperService.getUserDevices(boxNumber,user);
    }


    @GetMapping("/devices")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "All device detail",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = JsonResponseList.class))}),
            @ApiResponse(responseCode = "412", description = "Precondition failed"),
            @ApiResponse(responseCode = "404", description = "Not found")
    })
    @Operation( summary= "Get All Device/Audit User details")
    @CircuitBreaker(name = "command-response-mapper")
    public JsonResponseList getAllDevices(HttpServletRequest httpServletRequest )throws CommandResponseMapperNotFoundException {


        log.info("=====httpServletRequest+++++====="+ httpServletRequest);

        return responseMapperService.getAllDevices(httpServletRequest);
    }

    @GetMapping("/devices/{boxNumber}/mxcx")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Retrieve sequence number", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = JsonResponseList.class))}),
            @ApiResponse(responseCode = "412", description = "Precondition failed")

    })
    @Operation(summary = "Get the Non-Transactional MXcX Sequence Number")
    @CircuitBreaker(name = "command-response-mapper")
    public CommandResponseMapper getSequenceNumber(@PathVariable("boxNumber") String boxNumber) {

        return responseMapperService.getSequenceNumber(boxNumber);
    }

    @GetMapping("/devices/{boxNumber}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Get specific device detail",content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = JsonResponseList.class))}),
            @ApiResponse(responseCode = "412", description = "Precondition failed")
    })
    @Operation(summary = "Get a Specific Device's details")
    @CircuitBreaker(name = "command-response-mapper")
    public JsonResponseList getSpecificDeviceDetails(@PathVariable("boxNumber") String boxNumber) {

        return responseMapperService.getSpecificDeviceDetails(boxNumber);
    }

    @DeleteMapping("/devices/{boxNumber}/users/{user}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Successful deletion", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = TxFlowContainer.class))}),
            @ApiResponse(responseCode = "412", description = "Precondition failed")
    })
    @Operation(summary = "Delete Specific Device with Audit User")
    @CircuitBreaker(name = "command-response-mapper")
    public List<TxFlowContainer> deleteByBoxNummberAdnUser(@PathVariable("boxnumber") String boxNumber, @PathVariable("user") String user) {
       return responseMapperService.deleteByBoxNumberAdnUser(boxNumber,user);

    }

    @DeleteMapping("/devices/{boxNumber}/{messageGuid}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Delete specific device with messageGui",
                    content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = TxFlowContainer.class))}),
            @ApiResponse(responseCode = "412", description = "Precondition failed")
    })
    @Operation(summary = "Delete Specific Device with Message GUIDr")
    @CircuitBreaker(name = "command-response-mapper")
    public List<TxFlowContainer> deleteByBoxNumberAndMessageGuIdS(@PathVariable("boxNumber") String boxNumber, @PathVariable("messageGuid") String messageGuid) {
        return  responseMapperService.deleteByBoxNumberAndMessageGuId(boxNumber,messageGuid);
    }

    @DeleteMapping("/devices")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Delete all  device",
                    content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = TxFlowContainer.class))}),
            @ApiResponse(responseCode = "412", description = "Precondition failed")
    })
    @Operation(summary = "Delete All Device details")
    @CircuitBreaker(name = "command-response-mapper")
    public List<TxFlowContainer> deleteAllDevices() {
        return  responseMapperService.deleteAllDevices();

    }

    @DeleteMapping("/devices/{boxNumber}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Delete by box number  device",
                    content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = TxFlowContainer.class))}),
            @ApiResponse(responseCode = "412", description = "Precondition failed")
    })
    @Operation(summary = "Delete Specific Device's by box number")
    @CircuitBreaker(name = "command-response-mapper")
    public List<TxFlowContainer> deleteDeviceByBoxNummber(@PathVariable("boxNumber") String boxNumber) {
        return responseMapperService.deleteDeviceByBoxNumber(boxNumber);
    }

    @PostMapping("/devices/{boxNumber}/mix2000")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Reserve  by box number",
                    content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = String.class))}),
            @ApiResponse(responseCode = "412", description = "Precondition failed")
    })
    @Operation(summary = "Reserve MiX2000 Device")
    @CircuitBreaker(name = "command-response-mapper")
    public void reserveMix2000ByBoxNumber(@PathVariable("boxNumber") String boxNumber,
                                            @NotNull(message = "Jsonbase command can't be null")
                                            @RequestBody  JsonBaseCommand jsonBaseCommand) {
         responseMapperService.reserveMix2000ByBoxNumber(boxNumber,jsonBaseCommand);
    }

    @PostMapping("/devices/{boxNumber}/mxcx")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Reserve  by box number",
                    content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = String.class))}),
            @ApiResponse(responseCode = "412", description = "Precondition failed")
    })
    @Operation(summary = "Reserve MXcX Device")
    @CircuitBreaker(name = "command-response-mapper")
    public void reserveMxcxByBoxNumber(@PathVariable("boxNumber") String boxNumber,
                                         @NotNull(message = "Jsonbase command can't be null")
                                         @RequestBody  JsonBaseCommand jsonBaseCommand) {
         responseMapperService.reserveMxcxByBoxNumber(boxNumber,jsonBaseCommand);
    }

    @PostMapping("/devices/{boxNumber}/{isResponse}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Reserve mxcx legacy pathway by box number",
                    content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = String.class))}),
            @ApiResponse(responseCode = "412", description = "Precondition failed")
    })
    @Operation(summary = "Post MXcX Command in the Legacy Pathway")
    @CircuitBreaker(name = "command-response-mapper")
    public String reserveMxcxLegacy(@PathVariable("boxNumber") String boxNumber, @PathVariable("isResponse") String isResponse) {
        return null;
    }

    @PostMapping("/devices/{boxNumber}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Reserve Generic Device by box number",
                    content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = String.class))}),
            @ApiResponse(responseCode = "412", description = "Precondition failed")
    })
    @Operation(summary = "Reserve Generic Device")
    @CircuitBreaker(name = "command-response-mapper")
    public void reserveGenericDeviceByBoxNumber(@PathVariable("boxNumber") String boxNumber,
                                                  @NotNull(message = "Jsonbase command can't be null")
                                                  @RequestBody @Valid JsonBaseCommand jsonBaseCommand)throws CommandResponseMapperDuplicateException {
         responseMapperService.reserveGenericDeviceByBoxNumber(boxNumber,jsonBaseCommand);
    }


    @PostMapping("/devices")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Post Routable Message Received",
                    content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = String.class))}),
            @ApiResponse(responseCode = "412", description = "Precondition failed")
    })
    @Operation(summary = "Post Routable Message Received")
    @CircuitBreaker(name = "command-response-mapper")
    public void reserveRoutableMessagesReceived(@NotEmpty(message = "Rx Flow cann't be null")
                                                  @RequestBody
                                                  @Valid
                                                  RxFlowContainer rxFlowContainer) {

         responseMapperService.receivedRoutingMessage(rxFlowContainer);
    }


    @PatchMapping("/devices/{boxNumber}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = " Patch a Specific Device to Registered",
                    content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = String.class))}),
            @ApiResponse(responseCode = "412", description = "Precondition failed")
    })
    @Operation(summary = " Patch a Specific Device to Registered")
    @CircuitBreaker(name = "command-response-mapper")
    public void updateRegisteredByBoxNumber (@PathVariable("boxNumber") String boxNumber,
                                          @Valid @NotNull(message = "Json base Command can't be null")
                                          @RequestBody JsonBaseCommand jsonBaseCommand){
         responseMapperService.updateRegisteredByBoxNumber(boxNumber,jsonBaseCommand);
    }

}
