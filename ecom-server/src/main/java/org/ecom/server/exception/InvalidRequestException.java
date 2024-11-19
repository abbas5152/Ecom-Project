package org.ecom.server.exception;

import org.ecom.model.common.ErrorCode;
import lombok.Getter;

@Getter
public class InvalidRequestException extends BaseException {
   public InvalidRequestException(ErrorCode errorCode, String errorMessage, String... arguments) {
      super(errorCode, errorMessage, arguments);
   }
}
