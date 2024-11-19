package org.ecom.server.exception;

import org.ecom.model.common.ErrorCode;
import lombok.Getter;

@Getter
public class ResourceNotFoundException extends BaseException {
   public ResourceNotFoundException(ErrorCode errorCode, String errorMessage, String... args) {
      super(errorCode, errorMessage, args);
   }
}
