package org.ecom.server.controller;

import java.util.Locale;

import org.ecom.server.exception.InvalidRequestException;
import org.ecom.server.exception.ResourceNotFoundException;
import org.ecom.server.exception.UnknownException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import org.ecom.model.common.ErrorMessage;
import lombok.AllArgsConstructor;

@RestControllerAdvice
@AllArgsConstructor
public class ExceptionHandlerController {

   @Autowired
   private MessageSource messageSource;

   /**
    * This Exception is raised when the Service Layer while validating the input
    * runs into an error.
    *
    * @param ex Exception from within the Service Layer related to Invalid Request
    * @param locale Locale used to resolve the error code to the appropriate error message
    * @return The ErrorMessage response object
    */
   @ExceptionHandler(value = { InvalidRequestException.class})
   @ResponseStatus(value = HttpStatus.BAD_REQUEST)
   public ErrorMessage invalidRequestException(InvalidRequestException ex, Locale locale) {
      return new ErrorMessage(400, ex.getErrorCode().getApiErrorCode(),
            messageSource.getMessage(ex.getErrorCode().getErrorCode(), ex.getArguments(), locale));
   }

   /**
    * This exception is raised when a resource is not found.
    *
    * @param ex ResourceNotFoundException
    * @param locale Locale used to resolve the error code to the appropriate error message
    * @return The ErrorMessage response object
    */
   @ExceptionHandler(value = { ResourceNotFoundException.class})
   @ResponseStatus(value = HttpStatus.NOT_FOUND)
   public ErrorMessage resourceNotFoundException(ResourceNotFoundException ex, Locale locale) {
      System.out.println(ex.getMessage());
      System.out.println(ex.getErrorCode());
      //System.out.println(ex.ge);
      return new ErrorMessage(404, ex.getErrorCode().getApiErrorCode(),
            messageSource.getMessage(ex.getErrorCode().getErrorCode(), ex.getArguments(), locale));
   }

   /**
    * This exception is raised when external API calls to Pulse Iotc returns exceptions.
    *
    * @param ex UnknownException
    * @param locale Locale used to resolve the error code to the appropriate error message
    * @return The ErrorMessage response object
    */
   @ExceptionHandler(value = { UnknownException.class })
   @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
   public ErrorMessage unknownException(UnknownException ex, Locale locale) {
      return new ErrorMessage(500, ex.getErrorCode().getApiErrorCode(),
            messageSource.getMessage(ex.getErrorCode().getErrorCode(), null, locale));
   }
}
