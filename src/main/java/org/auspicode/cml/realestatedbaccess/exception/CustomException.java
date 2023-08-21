package org.auspicode.cml.realestatedbaccess.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatusCode;

import java.time.ZonedDateTime;

@Getter
@Setter
public class CustomException {

  private final String message;
  private final HttpStatusCode status;
  private final ZonedDateTime timestamp;
  private final String detail;

  public CustomException(String message, HttpStatusCode status, String cause) {
    this.timestamp = ZonedDateTime.now();
    this.message = message;
    this.status = status;
    this.detail = cause;
  }
}
