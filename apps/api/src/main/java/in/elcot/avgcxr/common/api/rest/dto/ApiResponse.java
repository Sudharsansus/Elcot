package in.elcot.avgcxr.common.api.rest.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ApiResponse<T>(
    boolean success,
    T data,
    ApiError error,
    String message,
    LocalDateTime timestamp
) {
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(true, data, null, null, LocalDateTime.now());
    }

    public static <T> ApiResponse<T> success(T data, String message) {
        return new ApiResponse<>(true, data, null, message, LocalDateTime.now());
    }

    public static <T> ApiResponse<T> error(String code, String message) {
        return new ApiResponse<>(false, null, ApiError.of(code, message), null, LocalDateTime.now());
    }

    public static <T> ApiResponse<T> error(String code, String message, java.util.List<ApiError> details) {
        return new ApiResponse<>(false, null, ApiError.of(code, message, details), null, LocalDateTime.now());
    }

    public static <T> ApiResponse<T> error(ApiError error) {
        return new ApiResponse<>(false, null, error, null, LocalDateTime.now());
    }

    public static <T> ApiResponse<T> paginated(T data, int page, int size, long total) {
        return new ApiResponse<>(true, data, null,
                String.format("Page %d of %d (total: %d)", page, (int) Math.ceil((double) total / size), total),
                LocalDateTime.now());
    }
}
