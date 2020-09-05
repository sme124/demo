package com.example.ssdemoapp.exception.error;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ResponseEntity.BodyBuilder;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

/**
 * Controller advice to translate the server side exceptions to client-friendly json structures.
 */
@Slf4j
@RestControllerAdvice
public class ExceptionTranslator {

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public Map<String, String> handleValidationExceptions(
      MethodArgumentNotValidException ex) {
    log.info("handleValidationExceptions :: ", ex);
    Map<String, String> errors = new HashMap<>();
    ex.getBindingResult().getAllErrors().forEach((error) -> {
      String fieldName = ((FieldError) error).getField();
      String errorMessage = error.getDefaultMessage();
      errors.put(fieldName, errorMessage);
    });
    return errors;
  }

  @ExceptionHandler(CustomParameterizedException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ResponseBody
  public ParameterizedErrorDTO processParameterizedValidationError(CustomParameterizedException ex) {
    log.info("processParameterizedValidationError :: ", ex);
    return ex.getErrorDTO();
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorDTO> processRuntimeException(Exception ex) {
    log.info("processRuntimeException :: ", ex);
    BodyBuilder builder;
    ErrorDTO errorDTO;
    ResponseStatus responseStatus = AnnotationUtils.findAnnotation(ex.getClass(), ResponseStatus.class);
    if (responseStatus != null) {
      builder = ResponseEntity.status(responseStatus.value());
      errorDTO = new ErrorDTO(responseStatus.reason(), responseStatus.value().value());
    } else {
      builder = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR);
      errorDTO = new ErrorDTO(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value());
    }
    return builder.body(errorDTO);
  }
}
