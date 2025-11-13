package practicum.ru.product.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class ErrorHandler {
    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFoundData(final NotFoundException e) {
        return new ErrorResponse("Данные не найдены", e.getMessage(), "не удалось найти данные",
                ExceptionStatus.NOT_FOUND, LocalDateTime.now());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleBusiness(final BusinessException e) {
        return new ErrorResponse("Логическая ошибка", e.getMessage(), "не удалось найти данные",
                ExceptionStatus.NOT_FOUND, LocalDateTime.now());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleBadRequest(final BadRequestException e) {
        return new ErrorResponse("Некорректный запрос", e.getMessage(), "некорректный запрос",
                ExceptionStatus.BAD_REQUEST, LocalDateTime.now());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleConflict(final ConflictException e) {
        return new ErrorResponse("Конфликт", e.getMessage(), "Конфликт", ExceptionStatus.CONFLICT,
                LocalDateTime.now());
    }
}
