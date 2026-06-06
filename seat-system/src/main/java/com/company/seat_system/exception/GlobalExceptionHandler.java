package com.company.seat_system.exception;

import com.company.seat_system.common.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Global exception handler for all REST controllers.
 * Handles various exception types and returns consistent error responses.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles BusinessException errors.
     * Returns a BAD_REQUEST status with error message.
     *
     * @param exception the BusinessException to handle
     * @return an ApiResponse with success=false and error message
     */
    @ExceptionHandler(BusinessException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<Void> handleBusinessException(BusinessException exception) {
        return new ApiResponse<>(false, null, exception.getMessage());
    }

    /**
     * Handles validation errors for request parameters.
     * Returns a BAD_REQUEST status with the first validation error message.
     *
     * @param exception the MethodArgumentNotValidException to handle
     * @return an ApiResponse with success=false and validation error message
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<Void> handleValidationException(MethodArgumentNotValidException exception) {
        String message = exception.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .findFirst().orElse("輸入資料有誤");
        return new ApiResponse<>(false, null, message);
    }

    /**
     * Handles all other unexpected exceptions.
     * Returns an INTERNAL_SERVER_ERROR status with error message.
     *
     * @param exception the Exception to handle
     * @return an ApiResponse with success=false and system error message
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiResponse<Void> handleGeneralException(Exception exception) {
        return new ApiResponse<>(false, null, "系統錯誤：" + exception.getMessage());
    }
}
