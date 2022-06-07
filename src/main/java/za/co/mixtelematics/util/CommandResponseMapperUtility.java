package za.co.mixtelematics.util;

import com.mix.flow.containers.TxFlowContainer;
import com.mix.json.documents.command.JsonBaseCommand;
import com.mix.json.documents.components.JsonAuxiliary;
import com.mix.json.documents.components.JsonDevice;
import com.mix.json.documents.enums.MasterCommandActions;
import com.mix.json.documents.enums.SubCommandActions;

import com.mix.mule4.containers.MuleContext;
import com.mix.mule4.exceptions.HttpBadRequestException;
import com.mix.mule4.utilities.JavaLauncherUtilities;
import com.mix.seda.api.ApiManagement;
import com.mix.util.enums.common.PlatformTypes;
import com.mix.util.utilities.common.PlatformTypeLookup;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import za.co.mixtelematics.exception.HttpErrorHandler;

import java.util.HashMap;
import java.util.Map;

@Log
public class CommandResponseMapperUtility
{
  @Autowired
  ApiManagement apiManagement;

  public static TxFlowContainer buildNotificationCommand(String auditUser, JsonDevice device, Long correlationId, SubCommandActions subCommand)
  {
    TxFlowContainer txFlowContainer = new TxFlowContainer();

    JsonBaseCommand notification = new JsonBaseCommand();
    notification.setDevice(device);
    notification.setAuditUser(auditUser);
    
    JsonAuxiliary auxiliary = new JsonAuxiliary();
    notification.setAuxiliary(auxiliary);
    auxiliary.setCorrelationId(correlationId);
    
    notification.setMasterCommand(MasterCommandActions.COMMAND_RESPONSE.toString());
    notification.setSubCommand(subCommand.toString());
    txFlowContainer.setJsonBaseMessage(notification);  
    
    return txFlowContainer;
  }

  public static boolean isMXcx(String boxNumber){
    boolean isMXcX=false;
    PlatformTypeLookup platformTypeLookup = new PlatformTypeLookup();
    PlatformTypes platformType = platformTypeLookup.getPlatformTypeFromBoxNumber(boxNumber);
    switch (platformType){
      case MIX2000:
        break;

      case MIX1000:
      case MXC3:
      case MXC:
        isMXcX = true;
        break;
    }
    log.info("Is MXCX Device? :"+isMXcX);
    return isMXcX;
  }

  public static Object sedaApi(Object payload, Object attributes, String path)
  {
    Object returnObject = null;
    MuleContext muleContext = null;

    try
    {
      muleContext = getMuleContext(payload, attributes, path, false);
      returnObject = ApiManagement.sedaApiEntry(muleContext);
    }
    catch (Exception exception)
    {
      log.info(exception.getMessage());
      returnObject = apiExceptionCommon(exception, muleContext);
    }

    return returnObject;
  }

  private static MuleContext getMuleContext(Object payload, Object attributes, String path, boolean enableMuleExtensions) throws HttpBadRequestException
  {
    MuleContext muleContext = JavaLauncherUtilities.createBaseMuleContext(path, enableMuleExtensions);
    Map<String ,String > addtin= new HashMap<>();
    addtin.put("flowName","01_commandResponseMapper");
    addtin.put("folder","IN");
    muleContext.setUriParameters(addtin);
    JavaLauncherUtilities.buildJavaContext(payload, attributes, muleContext);
    return muleContext;
  }

  private static Object apiExceptionCommon(Exception exception, MuleContext muleContext)
  {
    muleContext.setException(exception);
    return HttpErrorHandler.execute(muleContext);
  }

}
