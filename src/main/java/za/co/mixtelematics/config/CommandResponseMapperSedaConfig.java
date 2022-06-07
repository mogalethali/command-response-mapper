package za.co.mixtelematics.config;

import com.mix.json.documents.seda.DomainProperties;
import com.mix.seda.api.ApiCommon;
import com.mix.seda.api.ApiManagement;
import com.mix.seda.containers.GeneralProperties;
import com.mix.seda.containers.InstructionProperties;
import com.mix.seda.containers.TimerProperties;
import com.mix.seda.controller.FlowCollection;
import com.mix.seda.events.NotificationController;
import com.mix.util.http.mule.StandardQueryParameters;
import com.mix.util.utilities.json.JsonFlowContainerUtility;
import com.mix.util.utilities.loggers.LoggerHelper;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
@Configuration
@Log
public class CommandResponseMapperSedaConfig {
    @Autowired
    private Environment env;
    LoggerHelper loggerHelper;



    @Bean(initMethod = "initialise")
    public JsonFlowContainerUtility jsonFlowContainerUtility(){
            JsonFlowContainerUtility jsonFlowContainerUtility =new JsonFlowContainerUtility();
            return jsonFlowContainerUtility;
    }

    @Bean(initMethod = "initialise")
    public LoggerHelper loggerHelper(){
        LoggerHelper loggerHelper = new LoggerHelper();
        this.loggerHelper= loggerHelper;
        return loggerHelper;
    }

    @Bean()
    public NotificationController notificationController(){
        NotificationController notificationController = new NotificationController();
        notificationController.setMonitoringApplicationEnabled(Boolean.parseBoolean(env.getProperty("seda.events.monitoring.application.enabled")));
        notificationController.setEmailEnabled(Boolean.parseBoolean(env.getProperty("seda.events.email.enabled")));
        notificationController.setSmsEnabled(Boolean.parseBoolean(env.getProperty("seda.events.sms.enabled")));
        notificationController.setMonitoringApplicationUrl(env.getProperty("seda.events.monitoring.application.url"));
        notificationController.setEmailUrl(env.getProperty("seda.events.email.url"));
        notificationController.setLogger(loggerHelper);
        notificationController.setSmsUrl(env.getProperty("seda.events.sms.url"));
        return notificationController;
    }

    @Bean()
    public DomainProperties domainProperties(){
        DomainProperties domainProperties = new DomainProperties();
        domainProperties.setDomainName("DEVELOPER");
        domainProperties.setSedaCoreName(env.getProperty("seda.core.name"));
        domainProperties.setApplicationName(env.getProperty("spring.application.name"));
        domainProperties.setSandboxName(env.getProperty("SANDBOX_NAME"));
        domainProperties.setServerPhysicalName(env.getProperty("physical.server.name"));
        domainProperties.setServerLogicalName(env.getProperty("logical.server.name"));
        domainProperties.setSedaDirectoryPrefix(env.getProperty("app.data.home"));
        return domainProperties;

    }

    @Bean
    public TimerProperties timerProperties(){
        TimerProperties timerProperties= new TimerProperties();
        timerProperties.setHeartbeatIntervalMilliseconds(Integer.parseInt(env.getProperty("seda.heartbeat.period.milliseconds")));
        timerProperties.setLoggingIntervalInSeconds(Integer.parseInt(env.getProperty("seda.api.logging.rate.seconds")));
        timerProperties.setRefreshPropertiesAndEventsIntervalInSeconds(Integer.parseInt(env.getProperty("seda.events.refresh.properties.period.seconds")));
        timerProperties.setHistoryEventIntervalMilliseconds(Integer.parseInt(env.getProperty("seda.events.history.period.milliseconds")));
        timerProperties.setSnapshotEventIntervalMilliseconds(Integer.parseInt(env.getProperty("seda.events.snapshot.period.milliseconds")));
        return timerProperties;
    }

    @Bean
    public InstructionProperties instructionProperties(){
        InstructionProperties instructionProperties = new InstructionProperties(env.getProperty("seda.data.folders"),env.getProperty("seda.startup.instructions"),env.getProperty("seda.events.sms.msisdn.list"),env.getProperty("seda.events.email.address.list"));
        return instructionProperties;
    }


    @Bean
    public GeneralProperties generalProperties(){
        GeneralProperties generalProperties = new GeneralProperties(loggerHelper,env.getProperty("mix.db.jdbcDriver"),env.getProperty("mix.db.mix360.jdbcConnection"));
        return generalProperties;

    }

    @Bean
    public StandardQueryParameters standardQueryParameters(){
        StandardQueryParameters standardQueryParameters = new StandardQueryParameters();
        standardQueryParameters.setLogger(loggerHelper);
        return standardQueryParameters;

    }

    @Bean
    public void apiManagement(){
        ApiManagement apiManagement = new ApiManagement();
        apiManagement.setLogger(loggerHelper);
        apiManagement.setJsonFlowContainerUtility(jsonFlowContainerUtility());

    }

    @Bean
    public ApiCommon apiCommon(){
        ApiCommon apiCommon = new ApiCommon();
        apiCommon.setDomainProperties(domainProperties());
        return apiCommon;

    }

    @Bean(initMethod = "initialise",destroyMethod = "onDestroy")
    public FlowCollection flowCollection(){
        FlowCollection flowCollection =new FlowCollection();
        flowCollection.setGeneralProperties(generalProperties());
        flowCollection.setInstructionProperties(instructionProperties());
        flowCollection.setTimerProperties(timerProperties());
        return flowCollection;
    }
}
