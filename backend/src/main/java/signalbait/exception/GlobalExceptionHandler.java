package signalbait.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import signalbait.dto.ErrorResponse;
// @RestControllerAdvice provides centralized exception handling
// for REST controllers and automatically treats return values
// from exception handlers as HTTP response bodies (e.g. JSON).
//
// @ControllerAdvice provides the same centralized controller advice
// mechanism, but does not automatically apply @ResponseBody behavior.
// For a REST API like SignalBait, @RestControllerAdvice is appropriate.
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ErrorResponse handleValidationError(
            MethodArgumentNotValidException exception) {

        return new ErrorResponse(
            400,
            "Validation failed",
            "Request contains invalid data."
        );
    }
}