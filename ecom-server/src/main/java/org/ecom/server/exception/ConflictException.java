package org.ecom.server.exception;

import org.ecom.model.common.ErrorCode;
import lombok.Getter;

@Getter
public class ConflictException extends BaseException {
   public ConflictException(ErrorCode errorCode, String errorMessage, String... args) {
      super(errorCode, errorMessage, args);
   }
}
