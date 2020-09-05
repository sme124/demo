package com.example.ssdemoapp.exception.error;

import java.io.Serializable;

/**
 * DTO for sending a parameterized error message.
 */
public class ParameterizedErrorDTO implements Serializable {

  private static final long serialVersionUID = 1L;
  private final String errorDesc;
  private int status;

  public ParameterizedErrorDTO(String errorDesc, int status) {
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
