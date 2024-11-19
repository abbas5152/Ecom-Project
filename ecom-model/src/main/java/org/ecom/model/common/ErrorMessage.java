package org.ecom.model.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class ErrorMessage {
   /**
    * HTTP Status Code
    */
   private int httpStatus;
   /**
    * API Error Code
    */
   private int apiErrorCode;

   /**
    * Localized Error Message
    */
   private String errorMessage;
}
