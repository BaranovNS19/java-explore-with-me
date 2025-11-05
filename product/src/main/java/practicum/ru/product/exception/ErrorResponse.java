package practicum.ru.product.exception;

public class ErrorResponse {
    private String name;
    private String description;

    public ErrorResponse(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}
