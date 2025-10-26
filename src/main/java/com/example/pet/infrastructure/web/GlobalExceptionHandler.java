package com.example.pet.infrastructure.web;

import java.util.stream.Stream;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.method.ParameterValidationResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * Global exception handler that enhances Problem Details responses with detailed validation
 * error information. Extends ResponseEntityExceptionHandler to properly integrate with Spring's
 * ProblemDetails support.
 */
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    /**
     * Handles validation errors for @Valid @RequestBody and returns Problem Details with
     * field-level error information.
     *
     * @param ex      the validation exception
     * @param headers the HTTP headers
     * @param status  the HTTP status
     * @param request the current web request
     * @return ResponseEntity with Problem Details including validation errors
     */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {

        ProblemDetail problemDetail =
                ProblemDetail.forStatusAndDetail(
                        HttpStatus.BAD_REQUEST, "Validation failed for one or more fields");

        problemDetail.setTitle("Bad Request");
        problemDetail.setProperty(
                "errors",
                ex.getBindingResult().getFieldErrors().stream()
                        .map(ValidationError::fromFieldError)
                        .toList());

        return ResponseEntity.badRequest().body(problemDetail);
    }

    /**
     * Handles validation errors for method parameters (e.g., @PathVariable, @RequestParam) and
     * returns Problem Details with validation error information.
     *
     * @param ex      the validation exception
     * @param headers the HTTP headers
     * @param status  the HTTP status
     * @param request the current web request
     * @return ResponseEntity with Problem Details including validation errors
     */
    @Override
    protected ResponseEntity<Object> handleHandlerMethodValidationException(
            HandlerMethodValidationException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {

        ProblemDetail problemDetail =
                ProblemDetail.forStatusAndDetail(
                        HttpStatus.BAD_REQUEST, "Validation failed for method parameters");

        problemDetail.setTitle("Bad Request");

        problemDetail.setProperty(
                "errors",
                ex.getParameterValidationResults().stream()
                        .flatMap(GlobalExceptionHandler::getValidationErrorStream)
                        .toList());

        return ResponseEntity.badRequest().body(problemDetail);
    }

    private static Stream<ValidationError> getValidationErrorStream(
            ParameterValidationResult result) {
        String parameterName = result.getMethodParameter().getParameterName();
        String field = parameterName != null ? parameterName : "unknown";

        return result.getResolvableErrors().stream()
                .map(
                        error ->
                                new ValidationError(
                                        field, result.getArgument(), error.getDefaultMessage()));
    }

    /**
     * Represents a single field validation error.
     *
     * @param field         the name of the field that failed validation
     * @param rejectedValue the value that was rejected
     * @param message       the validation error message
     */
    public record ValidationError(String field, Object rejectedValue, String message) {

        private static ValidationError fromFieldError(FieldError fieldError) {
            return new ValidationError(
                    fieldError.getField(),
                    fieldError.getRejectedValue(),
                    fieldError.getDefaultMessage());
        }
    }
}
