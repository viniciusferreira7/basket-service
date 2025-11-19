package dev.java.ecommerce.basket_service.config;

import dev.java.ecommerce.basket_service.controller.response.ErrorResponse;
import dev.java.ecommerce.basket_service.exception.BadRequest;
import dev.java.ecommerce.basket_service.exception.EntityNotFound;
import feign.FeignException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ApplicationControllerAdvice {

    @ExceptionHandler(EntityNotFound.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleEntityNotFound(
            EntityNotFound exception,
            HttpServletRequest request
    ) {
        return ErrorResponse.builder()
                .timestamp(Instant.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .error("Not found")
                .message(exception.getMessage())
                .path(request.getRequestURI())
                .build();
    }

    @ExceptionHandler(BadRequest.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleBadRequest(
            BadRequest exception,
            HttpServletRequest request
    ) {
        return ErrorResponse.builder()
                .timestamp(Instant.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .error("Bad Request")
                .message(exception.getMessage())
                .path(request.getRequestURI())
                .build();
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleIllegalArgumentException(
            IllegalArgumentException exception,
            HttpServletRequest request
    ) {
        return ErrorResponse.builder()
                .timestamp(Instant.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .error("Bad Request")
                .message(exception.getMessage())
                .path(request.getRequestURI())
                .build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleArgumentNotValidException(
            MethodArgumentNotValidException exception,
            HttpServletRequest request
    ) {
        Map<String, String> fieldErrors = new HashMap<>();

        exception.getBindingResult().getAllErrors().forEach((error) -> {
            fieldErrors.put(((FieldError) error).getField(), error.getDefaultMessage());
        });

        return ErrorResponse.builder()
                .timestamp(Instant.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .error("Bad Request")
                .fieldErrors(fieldErrors)
                .message("Validation error")
                .path(request.getRequestURI())
                .build();
    }

    /**
     * Handles Feign BadRequest exceptions (4xx client errors)
     * These occur when the external API returns a 4xx error
     */
    @ExceptionHandler(FeignException.BadRequest.class)
    @ResponseStatus(HttpStatus.BAD_GATEWAY)
    public ErrorResponse handleFeignBadRequest(
            FeignException.BadRequest exception,
            HttpServletRequest request
    ) {
        return ErrorResponse.builder()
                .timestamp(Instant.now())
                .status(HttpStatus.BAD_GATEWAY.value())
                .error("Bad Request from External Service")
                .message("The external service returned a bad request: " + exception.getMessage())
                .path(request.getRequestURI())
                .build();
    }

    /**
     * Handles Feign NotFound exceptions (404)
     * These occur when the requested resource doesn't exist in the external API
     */
    @ExceptionHandler(FeignException.NotFound.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleFeignNotFound(
            FeignException.NotFound exception,
            HttpServletRequest request
    ) {
        return ErrorResponse.builder()
                .timestamp(Instant.now())
                .status(HttpStatus.NOT_FOUND.value())
                .error("Not Found")
                .message("The requested resource was not found in the external service")
                .path(request.getRequestURI())
                .build();
    }

    /**
     * Handles Feign Unauthorized exceptions (401)
     * These occur when authentication fails with the external API
     */
    @ExceptionHandler(FeignException.Unauthorized.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorResponse handleFeignUnauthorized(
            FeignException.Unauthorized exception,
            HttpServletRequest request
    ) {
        return ErrorResponse.builder()
                .timestamp(Instant.now())
                .status(HttpStatus.UNAUTHORIZED.value())
                .error("Unauthorized")
                .message("Authentication failed with the external service")
                .path(request.getRequestURI())
                .build();
    }

    /**
     * Handles Feign Forbidden exceptions (403)
     * These occur when the request is not allowed by the external API
     */
    @ExceptionHandler(FeignException.Forbidden.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorResponse handleFeignForbidden(
            FeignException.Forbidden exception,
            HttpServletRequest request
    ) {
        return ErrorResponse.builder()
                .timestamp(Instant.now())
                .status(HttpStatus.FORBIDDEN.value())
                .error("Forbidden")
                .message("Access to the external service resource is forbidden")
                .path(request.getRequestURI())
                .build();
    }

    /**
     * Handles Feign TooManyRequests exceptions (429)
     * These occur when rate limiting is applied by the external API
     */
    @ExceptionHandler(FeignException.TooManyRequests.class)
    @ResponseStatus(HttpStatus.TOO_MANY_REQUESTS)
    public ErrorResponse handleFeignTooManyRequests(
            FeignException.TooManyRequests exception,
            HttpServletRequest request
    ) {
        return ErrorResponse.builder()
                .timestamp(Instant.now())
                .status(HttpStatus.TOO_MANY_REQUESTS.value())
                .error("Too Many Requests")
                .message("Rate limit exceeded when calling external service. Please try again later.")
                .path(request.getRequestURI())
                .build();
    }

    /**
     * Handles Feign server errors (5xx)
     * These occur when the external API experiences server issues
     */
    @ExceptionHandler(FeignException.InternalServerError.class)
    @ResponseStatus(HttpStatus.BAD_GATEWAY)
    public ErrorResponse handleFeignInternalServerError(
            FeignException.InternalServerError exception,
            HttpServletRequest request
    ) {
        return ErrorResponse.builder()
                .timestamp(Instant.now())
                .status(HttpStatus.BAD_GATEWAY.value())
                .error("External Service Error")
                .message("The external service encountered an internal error. Please try again later.")
                .path(request.getRequestURI())
                .build();
    }

    /**
     * Handles ServiceUnavailable exceptions (503)
     * These occur when the external API is temporarily unavailable
     */
    @ExceptionHandler(FeignException.ServiceUnavailable.class)
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    public ErrorResponse handleFeignServiceUnavailable(
            FeignException.ServiceUnavailable exception,
            HttpServletRequest request
    ) {
        return ErrorResponse.builder()
                .timestamp(Instant.now())
                .status(HttpStatus.SERVICE_UNAVAILABLE.value())
                .error("Service Unavailable")
                .message("The external service is temporarily unavailable. Please try again later.")
                .path(request.getRequestURI())
                .build();
    }

    /**
     * Generic handler for any other Feign exceptions
     * Catches any FeignException that doesn't match the specific handlers above
     */
    @ExceptionHandler(FeignException.class)
    @ResponseStatus(HttpStatus.BAD_GATEWAY)
    public ErrorResponse handleFeignException(
            FeignException exception,
            HttpServletRequest request
    ) {
        return ErrorResponse.builder()
                .timestamp(Instant.now())
                .status(HttpStatus.BAD_GATEWAY.value())
                .error("External Service Error")
                .message("An error occurred while calling external service: " + exception.getMessage())
                .path(request.getRequestURI())
                .build();
    }
}
