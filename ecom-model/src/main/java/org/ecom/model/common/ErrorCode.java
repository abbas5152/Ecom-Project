package org.ecom.model.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ErrorCode {
   private int apiErrorCode;
   private String errorCode;
}
