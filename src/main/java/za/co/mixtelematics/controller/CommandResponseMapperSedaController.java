package za.co.mixtelematics.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mix.json.documents.JsonBaseMessage;
import com.mix.json.documents.command.JsonBaseCommand;
import com.mix.json.documents.response.JsonBaseResponse;
import com.mix.json.documents.response.JsonResponseList;
import com.mix.json.documents.seda.JsonSedaManagement;
import com.mix.mule4.containers.FlowVariables;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import za.co.mixtelematics.service.CommandResponseMapperSedaService;

import javax.servlet.http.HttpServletRequest;

@Log
@RestController
@ApiOperation(value = "Seda api Management controller", tags = "Seda api Management controller" )
@RequestMapping("/mix360/seda_api/")
public class CommandResponseMapperSedaController {

    @Autowired
    private CommandResponseMapperSedaService mapperSedaService;

    @GetMapping("/status/flows/{flowName}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "GET Single Seda api Status",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = JsonResponseList.class))}),
            @ApiResponse(responseCode = "412", description = "Precondition failed")
    })
    @Operation(summary = "GET Single Seda Flow's Status")
    @CircuitBreaker(name = "command-response-mapper")
    public Object getSingleSedaFlowStatus(HttpServletRequest httpServletRequest,
                                                    @RequestParam String messageGuid,
                                                    @RequestParam String auditUser,
                                                    @PathVariable("flowName") String flowName) {
        return mapperSedaService.getSingleSedaFlowStatus(httpServletRequest);
    }

    @GetMapping("/status/flows")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "GET All Seda Flow's Status",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = JsonResponseList.class))}),
            @ApiResponse(responseCode = "412", description = "Precondition failed")
    })
    @Operation(summary = "GET All Seda Flow's Status")
    @CircuitBreaker(name = "command-response-mapper")
    public JsonBaseResponse getAllSedaFlowStatus(HttpServletRequest httpServletRequest,
                                                 @RequestParam String messageGuid,
                                                 @RequestParam String auditUser){
        return mapperSedaService.getAllSedaFlowStatus(httpServletRequest);

    }


    @GetMapping("/status/flows/{flowName}/folders/{folderName}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "GET Single Seda Flow and Folder's Status",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = JsonResponseList.class))}),
            @ApiResponse(responseCode = "412", description = "Precondition failed")
    })
    @Operation(summary = "GET Single Seda Flow and Folder's Status")
    @CircuitBreaker(name = "command-response-mapper")
    public Object getSingleSedaFlowAndFolderStatus(HttpServletRequest httpServletRequest,
                                       @RequestParam String messageGuid,
                                       @RequestParam String auditUser){
        return mapperSedaService.getSingleSedaFlowAndFolderStatus(httpServletRequest);

    }

    @DeleteMapping("/status/flows")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "RESET All Seda Flow's Status",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = JsonResponseList.class))}),
            @ApiResponse(responseCode = "412", description = "Precondition failed")
    })
    @Operation(summary = "RESET All Seda Flow's Status")
    @CircuitBreaker(name = "command-response-mapper")
    public Object resetAllSedaFlowStatus(HttpServletRequest httpServletRequest,
                                                   @RequestParam String messageGuid,
                                                   @RequestParam String auditUser){
        return mapperSedaService.resetAllSedaFlowStatus(httpServletRequest);

    }


    @DeleteMapping("/status/flows/{flowName}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "RESET Single Seda Flow's Status",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = JsonResponseList.class))}),
            @ApiResponse(responseCode = "412", description = "Precondition failed")
    })
    @Operation(summary = "RESET Single Seda Flow's Status")
    @CircuitBreaker(name = "command-response-mapper")
    public Object resetSingleSedaFlowStatus(HttpServletRequest httpServletRequest,
                                         @RequestParam String messageGuid,
                                         @RequestParam String auditUser){
        return mapperSedaService.resetSingleSedaFlowStatus(httpServletRequest);

    }


    @GetMapping("/events/status/flows")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "GET All Seda Flow's Event Status",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = JsonResponseList.class))}),
            @ApiResponse(responseCode = "412", description = "Precondition failed")
    })
    @Operation(summary = "GET All Seda Flow's Event Status")
    @CircuitBreaker(name = "command-response-mapper")
    public Object getAllSedaFlowEventStatus(HttpServletRequest httpServletRequest,
                                            @RequestParam String messageGuid,
                                            @RequestParam String auditUser){
        return mapperSedaService.getAllSedaFlowEventStatus(httpServletRequest);

    }

    @GetMapping("/events/status/flows/{flowName}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "GET Single Seda Flow's Event Status",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = JsonResponseList.class))}),
            @ApiResponse(responseCode = "412", description = "Precondition failed")
    })
    @Operation(summary = "GET Single Seda Flow's Event Status")
    @CircuitBreaker(name = "command-response-mapper")
    public Object getSingleSedaFlowEventStatus(HttpServletRequest httpServletRequest,
                                               @RequestParam String messageGuid,
                                               @RequestParam String auditUser,
                                               @PathVariable("flowName") String flowName){
        return mapperSedaService.getSingleSedaFlowEventStatus(httpServletRequest);

    }

    @GetMapping("/events/status/flows/{flowName}/folders/{folder}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "GET Single Seda Flow and Folder's Event Status",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = JsonResponseList.class))}),
            @ApiResponse(responseCode = "412", description = "Precondition failed")
    })
    @Operation(summary = "GET Single Seda Flow and Folder's Event Status")
    @CircuitBreaker(name = "command-response-mapper")
    public Object getSingleSedaFlowAndFolderEventStatus(HttpServletRequest httpServletRequest,
                                                        @RequestParam String messageGuid,
                                                        @RequestParam String auditUser,
                                                        @PathVariable("flowName") String flowName,
                                                        @PathVariable("folder") String folder){
        return mapperSedaService.getSingleSedaFlowAndFolderEventStatus(httpServletRequest);

    }

    @DeleteMapping("/events/status/flows")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "RESET All Seda Flow's Event Status",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = JsonResponseList.class))}),
            @ApiResponse(responseCode = "412", description = "Precondition failed")
    })
    @Operation(summary = "RESET All Seda Flow's Event Status")
    @CircuitBreaker(name = "command-response-mapper")
    public Object resetAllSeSedaFlowEventStatus(HttpServletRequest httpServletRequest,
                                            @RequestParam String messageGuid,
                                            @RequestParam String auditUser){
        return mapperSedaService.resetAllSeSedaFlowEventStatus(httpServletRequest);

    }

    @DeleteMapping("/events/status/flows/{flowName}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "RESET Single Seda Flow's Event Status",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = JsonResponseList.class))}),
            @ApiResponse(responseCode = "412", description = "Precondition failed")
    })
    @Operation(summary = "RESET Single Seda Flow's Event Status")
    @CircuitBreaker(name = "command-response-mapper")
    public Object resetSingleSedaFlowEventStatus(HttpServletRequest httpServletRequest,
                                                 @RequestParam String messageGuid,
                                                 @RequestParam String auditUser,
                                                 @PathVariable("flowName") String flowName){
        return mapperSedaService.resetSingleSedaFlowEventStatus(httpServletRequest);

    }
    @DeleteMapping("/events/status/flows/{flowName}/folders/{folder}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "RESET Single Seda Flow and Folder's Event Status",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = JsonResponseList.class))}),
            @ApiResponse(responseCode = "412", description = "Precondition failed")
    })
    @Operation(summary = "RESET Single Seda Flow and Folder's Event Status")
    @CircuitBreaker(name = "command-response-mapper")
    public Object resetSingleSedaFlowAndFolderEventStatus(HttpServletRequest httpServletRequest,
                                                          @RequestParam String messageGuid,
                                                          @RequestParam String auditUser,
                                                          @PathVariable("flowName") String flowName,
                                                          @PathVariable("folder") String folder){
        return mapperSedaService.resetSingleSedaFlowAndFolderEventStatus(httpServletRequest);
    }


    @GetMapping("/events/configuration/flows")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = " GET All Seda Flow's Event Configuration",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = JsonResponseList.class))}),
            @ApiResponse(responseCode = "412", description = "Precondition failed")
    })
    @Operation(summary = " GET All Seda Flow's Event Configuration")
    @CircuitBreaker(name = "command-response-mapper")
    public Object getAllSedaFlowEventConfiguration(HttpServletRequest httpServletRequest,
                                                        @RequestParam String messageGuid,
                                                        @RequestParam String auditUser){
        return mapperSedaService.getAllSedaFlowEventConfiguration(httpServletRequest);

    }
    @GetMapping("/events/configuration/flows/{flowName}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = " GET Single Seda Flow's Event Configuration",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = JsonResponseList.class))}),
            @ApiResponse(responseCode = "412", description = "Precondition failed")
    })
    @Operation(summary = " GET Single Seda Flow's Event Configuration")
    @CircuitBreaker(name = "command-response-mapper")
    public Object getSingleSedaFlowEventConfiguration(HttpServletRequest httpServletRequest,
                                                   @RequestParam String messageGuid,
                                                   @RequestParam String auditUser,
                                                   @PathVariable("flowName") String flowName){
        return mapperSedaService.getSingleSedaFlowEventConfiguration(httpServletRequest);

    }

    @GetMapping("/events/configuration/flows/{flowName}/folders/{folder}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "GET Single Seda Flow and Folder's Event Configuration",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = JsonResponseList.class))}),
            @ApiResponse(responseCode = "412", description = "Precondition failed")
    })
    @Operation(summary = "GET Single Seda Flow and Folder's Event Configuration")
    @CircuitBreaker(name = "command-response-mapper")
    public Object getSingleSedaFlowAndFolderEventConfiguration(HttpServletRequest httpServletRequest,
                                                          @RequestParam String messageGuid,
                                                          @RequestParam String auditUser,
                                                          @PathVariable("flowName") String flowName,
                                                          @PathVariable("folder") String folder){
        return mapperSedaService.getSingleSedaFlowAndFolderEventConfiguration(httpServletRequest);
    }

    @DeleteMapping("/events/configuration/flows")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "DELETE All Seda Flow's Event Configuration",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = JsonResponseList.class))}),
            @ApiResponse(responseCode = "412", description = "Precondition failed")
    })
    @Operation(summary = "DELETE All Seda Flow's Event Configuration")
    @CircuitBreaker(name = "command-response-mapper")
    public Object resetAllSedaFlowEventConfiguration(HttpServletRequest httpServletRequest,
                                                               @RequestParam String messageGuid,
                                                               @RequestParam String auditUser){
        return mapperSedaService.resetAllSedaFlowEventConfiguration(httpServletRequest);
    }

    @DeleteMapping("/events/configuration/flows/{flowName}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "DELETE Single Seda Flow's Event Configuration",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = JsonResponseList.class))}),
            @ApiResponse(responseCode = "412", description = "Precondition failed")
    })
    @Operation(summary = "DELETE Single Seda Flow's Event Configuration")
    @CircuitBreaker(name = "command-response-mapper")
    public Object resetSingleSedaFlowEventConfiguration(HttpServletRequest httpServletRequest,
                                                        @RequestParam String messageGuid,
                                                        @RequestParam String auditUser,
                                                        @PathVariable("flowName") String flowName){
        return mapperSedaService.resetSingleSedaFlowEventConfiguration(httpServletRequest);
    }

    @PostMapping("/events/configuration/flows/{flowName}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = " POST Create a Single Seda Flow's Event",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = JsonResponseList.class))}),
            @ApiResponse(responseCode = "412", description = "Precondition failed")
    })
    @Operation(summary = " POST Create a Single Seda Flow's Event")
    @CircuitBreaker(name = "command-response-mapper")
    public Object createSingleSedaFlowEventConfiguration(HttpServletRequest httpServletRequest,
                                                        @RequestBody JsonBaseMessage jsonBaseMessage,
                                                        @PathVariable("flowName") String flowName) throws JsonProcessingException {
        return mapperSedaService.createSingleSedaFlowEventConfiguration(httpServletRequest,jsonBaseMessage);
    }

    @PatchMapping("/events/configuration/flows/{flowName}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "PUT Update a Single Seda Flow's Event",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = JsonResponseList.class))}),
            @ApiResponse(responseCode = "412", description = "Precondition failed")
    })
    @Operation(summary = " PUT Update a Single Seda Flow's Event")
    @CircuitBreaker(name = "command-response-mapper")
    public Object updateSingleSedaFlowEventConfiguration(HttpServletRequest httpServletRequest,
                                                         @RequestBody JsonBaseMessage jsonBaseMessage,
                                                         @PathVariable("flowName") String flowName) throws JsonProcessingException {
        return mapperSedaService.updateSingleSedaFlowEventConfiguration(httpServletRequest,jsonBaseMessage);
    }

    @DeleteMapping("/events/configuration/flows/{flowName}/folders/{folder}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "DELETE Single Seda Flow and Folder's Event Configuration",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = JsonResponseList.class))}),
            @ApiResponse(responseCode = "412", description = "Precondition failed")
    })
    @Operation(summary = " DELETE Single Seda Flow and Folder's Event Configuration")
    @CircuitBreaker(name = "command-response-mapper")
    public Object deleteSingleSedaFlowAndFolderEventConfiguration(HttpServletRequest httpServletRequest,
                                                                  @RequestParam String messageGuid,
                                                                  @RequestParam String auditUser,
                                                                  @PathVariable("flowName") String flowName,
                                                                  @PathVariable("folder") String folder) {
        return mapperSedaService.deleteSingleSedaFlowAndFolderEventConfiguration(httpServletRequest);
    }
    @GetMapping("/asynchronous")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "GET Status of all Asynch Processes",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = JsonResponseList.class))}),
            @ApiResponse(responseCode = "412", description = "Precondition failed")
    })
    @Operation(summary = " GET Status of all Asynch Processes")
    @CircuitBreaker(name = "command-response-mapper")
    public Object getStatusOfAllAsynchProcesses(HttpServletRequest httpServletRequest,
                                                                  @RequestParam String messageGuid,
                                                                  @RequestParam String auditUser)  {
        return mapperSedaService.getStatusOfAllAsynchProcesses(httpServletRequest);
    }

    @DeleteMapping("/asynchronous")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "ABORT Executing Asynch Process",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = JsonResponseList.class))}),
            @ApiResponse(responseCode = "412", description = "Precondition failed")
    })
    @Operation(summary = " ABORT Executing Asynch Process")
    @CircuitBreaker(name = "command-response-mapper")
    public Object abortAsynchProcesses(HttpServletRequest httpServletRequest,
                                                @RequestParam String messageGuid,
                                                @RequestParam String auditUser)  {
        return mapperSedaService.abortAsynchProcesses(httpServletRequest);
    }

    @PostMapping("/asynchronous/move")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "POST Move Files Asynchronously",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = JsonResponseList.class))}),
            @ApiResponse(responseCode = "412", description = "Precondition failed")
    })
    @Operation(summary = "POST Move Files Asynchronously")
    @CircuitBreaker(name = "command-response-mapper")
    public Object moveFileAsynchProcesses(HttpServletRequest httpServletRequest,
                                          @RequestBody JsonBaseMessage jsonBaseMessage,  @RequestParam String messageGuid,
                                          @RequestParam String auditUser) throws JsonProcessingException {
        return mapperSedaService.moveFileAsynchProcesses(httpServletRequest,jsonBaseMessage);
    }

}

