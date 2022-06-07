/*
 * File Name:       HttpErrorHandler.java
 *
 * Project:         MiX360 SEDA API
 *
 * Target System:   Mule 4 EE ESB
 *
 * Compiler:        Java 8
 *
 * Date:            10 January 2021
 *
 * Copyright:       Mix Telematics Ltd
 *
 *                  The copyright to the computer program(s) herein is the property of Mix Telematics.
 *                  The program(s) may be used/or copied only with the written permission of
 *                  Mix Telematics or in accordance with the terms and conditions stipulated in the
 *                  agreement/contract under which the program have been supplied.
 *
 *@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
 *@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
 *
 */

package za.co.mixtelematics.exception;

import com.mix.json.documents.response.JsonBaseResponse;
import com.mix.mule4.containers.FlowVariables;
import com.mix.mule4.containers.MuleContext;
import com.mix.mule4.exceptions.HttpBadRequestException;
import com.mix.mule4.exceptions.HttpConflictException;
import com.mix.mule4.exceptions.HttpInternalServerException;
import com.mix.mule4.exceptions.HttpNotFoundException;
import com.mix.mule4.exceptions.HttpNotImplementedException;
import com.mix.mule4.exceptions.HttpOtherException;
import com.mix.mule4.exceptions.HttpServiceUnavailableException;
import com.mix.util.constants.common.MuleConstants;
import com.mix.util.enums.http.HttpStatusCodes;
import com.mix.util.enums.loggers.LoggerTargetFiles;
import com.mix.util.utilities.json.JsonFlowContainerUtility;
import com.mix.util.utilities.loggers.LoggerHelper;

/**
 * 
 * @author Don
 *
 */
public class HttpErrorHandler implements MuleConstants
{
  //
  // CONSTANTS
  // ---------
  //
  private static String HTTP_ERROR_HANDLER = "HttpErrorHandler";

  private static final String HEADER = "{ " + "\"type\" : \"response\", \"errorMessage\" : \"";
  private static final String TRAILER = "\" }";
  

  //
  // FIELDS
  // ------
  //
  
  private static JsonFlowContainerUtility jsonFlowContainerUtility;
  private static LoggerHelper logger;
  
  //
  // METHODS
  // -------
  //
  
  /**
   * 
   * @param muleContext as MuleContext
   * @return payload as FlowVariables
   * @throws HttpBadRequestException
   */
  public static FlowVariables execute(MuleContext muleContext) 
  {
    FlowVariables flowVariables = muleContext.getFlowVariables();
    if (flowVariables == null)
    {
      flowVariables = new FlowVariables();
      muleContext.setFlowVariables(flowVariables);
    }//if

    Throwable throwable =  muleContext.getException();
    
    HttpStatusCodes httpStatusCode = null;
    if (throwable instanceof HttpNotFoundException)
      httpStatusCode = HttpStatusCodes.ErrorNotFound;
    else if (throwable instanceof HttpBadRequestException)
      httpStatusCode = HttpStatusCodes.BadRequests;
    else if (throwable instanceof HttpConflictException)
      httpStatusCode = HttpStatusCodes.Conflict;
    else if (throwable instanceof HttpServiceUnavailableException)
      httpStatusCode = HttpStatusCodes.ServiceUnavailable;
    else if (throwable instanceof HttpNotImplementedException)
      httpStatusCode = HttpStatusCodes.NotImplemented;
    else if (throwable instanceof HttpInternalServerException)
      httpStatusCode = HttpStatusCodes.InternalServerError;
    else if (throwable instanceof HttpOtherException)
      httpStatusCode = HttpStatusCodes.get(((HttpOtherException)throwable).getHttpStatusCode());
    else
      httpStatusCode = HttpStatusCodes.InternalServerError;

    flowVariables.insertHttpStatus(httpStatusCode);
    getResponse(throwable, flowVariables);
    
    return muleContext.getFlowVariables();
  }//execute
  
  
  /**
   * 
   * @param throwable
   * @param flowVariables
   */
  private static void getResponse(Throwable throwable, FlowVariables flowVariables)
  {
    JsonBaseResponse response = new JsonBaseResponse(null, null);
    response.setMessage(throwable.getMessage());
    
    // serialise
    try
    {
      String jsonBody = jsonFlowContainerUtility.prettySerialise(response);
      logger.error(LoggerTargetFiles.Application, HTTP_ERROR_HANDLER, "Json error response:\n" + jsonBody);
      flowVariables.setPayload(jsonBody);
    }//try
    catch (Exception exception)
    {
      flowVariables.insertHttpStatus(HttpStatusCodes.InternalServerError);
      flowVariables.setPayload(HEADER + "Exception in error handler: " + exception.getMessage() + TRAILER);
    }//catch
    
  }//getResponse
  

  //
  // PROPERTIES
  // ----------
  //
  
  public static void setLogger(LoggerHelper logger)
  {
    HttpErrorHandler.logger = logger;
  }
  
  public static void setJsonFlowContainerUtility(JsonFlowContainerUtility jsonFlowContainerUtility)
  {
    HttpErrorHandler.jsonFlowContainerUtility = jsonFlowContainerUtility;
  }

}//HttpErrorHandler
