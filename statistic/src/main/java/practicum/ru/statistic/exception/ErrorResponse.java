package practicum.ru.statistic.exception;

import java.time.LocalDateTime;

public class ErrorResponse {
    private String errors;
    private String message;
    private String reason;
    private ExceptionStatus status;
    LocalDateTime timestamp;

    public ErrorResponse(String name, String description, String reason, ExceptionStatus status, LocalDateTime timestamp) {
        this.errors = name;
        this.message = description;
        this.reason = reason;
        this.status = status;
        this.timestamp = timestamp;
    }

    public String getErrors() {
        return errors;
    }

    public String getMessage() {
        return message;
    }

    public String getReason() {
        return reason;
    }

    public ExceptionStatus getStatus() {
        return status;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}
