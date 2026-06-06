package com.company.seat_system.common;

/**
 * Generic API response wrapper for all REST endpoints.
 * Provides a consistent JSON response format for success and error cases.
 *
 * @param <T> the type of data returned in the response
 * @param success whether the operation was successful
 * @param data the response data (null if error or no data to return)
 * @param message optional success/error message
 */
public record ApiResponse<T>(
        boolean success,
        T data,
        String message
) {

    /**
     * Creates a successful response with data.
     *
     * @param <T>  the type of data
     * @param data the response data
     * @return an ApiResponse with success=true and the provided data
     */
    public static <T> ApiResponse<T> ok(T data) {
        return new ApiResponse<>(true, data, null);
    }

    /**
     * Creates a successful response with a message but no data.
     *
     * @param message the success message
     * @return an ApiResponse with success=true and the provided message
     */
    public static ApiResponse<Void> ok(String message) {
        return new ApiResponse<>(true, null, message);
    }
}
