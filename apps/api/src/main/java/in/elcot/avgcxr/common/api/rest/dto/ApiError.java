package in.elcot.avgcxr.common.api.rest.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.LocalDateTime;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ApiError(
    String code,
    String message,
    String field,
    List<ApiError> details,
    LocalDateTime timestamp
) {
    public static ApiError of(String code, String message) {
        return new ApiError(code, message, null, null, LocalDateTime.now());
    }

    public static ApiError ofField(String field, String message) {
        return new ApiError("VALIDATION_ERROR", message, field, null, LocalDateTime.now());
    }

    public static ApiError of(String code, String message, List<ApiError> details) {
        return new ApiError(code, message, null, details, LocalDateTime.now());
    }
}
