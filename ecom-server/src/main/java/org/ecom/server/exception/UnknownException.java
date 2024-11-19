package org.ecom.server.exception;

import org.ecom.model.common.ErrorCode;

public class UnknownException extends BaseException {
   public UnknownException(ErrorCode errorCode, String errorMessage) {
      super(errorCode, errorMessage, null);
   }
}
