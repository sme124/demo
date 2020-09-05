package com.example.ssdemoapp.exception.error;

import java.io.Serializable;

/**
 * DTO for transferring error message with a list of field errors.
 */
public class ErrorDTO implements Serializable {

  private static final long serialVersionUID = 1L;

  private final String errorDesc;
  private int status;

  public ErrorDTO(String errorDesc, int status) {
    this.errorDesc = errorDesc;
    this.status = status;
  }

  public String getErrorDesc() {
    return errorDesc;
  }

  public int getStatus() {
    return status;
  }
}
