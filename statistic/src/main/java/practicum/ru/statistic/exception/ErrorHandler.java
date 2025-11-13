package practicum.ru.statistic.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleBadRequest(final BadRequestException e) {
        return new ErrorResponse("Некорректный запрос", e.getMessage(), "некорректный запрос",
                ExceptionStatus.BAD_REQUEST, LocalDateTime.now());
    }
}
