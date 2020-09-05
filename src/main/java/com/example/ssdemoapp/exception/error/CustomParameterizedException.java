package com.example.ssdemoapp.exception.error;

/**
 * Custom, parameterized exception, which can be translated on the client side.
 * For example:
 *
 * <pre>
 * throw new CustomParameterizedException(&quot;myCustomError&quot;, &quot;hello&quot;, &quot;world&quot;);
 * </pre>
 *
 */
public class CustomParameterizedException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  private final String errorDesc;
  private final int status;

  public CustomParameterizedException(String errorDesc, int status) {
    super(errorDesc);
    this.errorDesc = errorDesc;
    this.status = status;
  }

  public CustomParameterizedException(Throwable cause, String errorDesc, int status) {
    super(errorDesc, cause);
    this.errorDesc = errorDesc;
    this.status = status;
  }

  public ParameterizedErrorDTO getErrorDTO() {
    return new ParameterizedErrorDTO(errorDesc, status);
  }

}
