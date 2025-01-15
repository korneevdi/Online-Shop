package ag.shop.manager.client;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.validation.FieldError;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class BadRequestException extends RuntimeException {

    private final List<FieldError> errors;

    public BadRequestException(List<FieldError> errors) {
        this.errors = errors;
    }

    public BadRequestException(String message, List<FieldError> errors) {
        super(message);
        this.errors = errors;
    }

    public BadRequestException(String message, Throwable cause, List<FieldError> errors) {
        super(message, cause);
        this.errors = errors;
    }

    public BadRequestException(Throwable cause, List<FieldError> errors) {
        super(cause);
        this.errors = errors;
    }

    public BadRequestException(String message, Throwable cause, boolean enableSuppression,
                               boolean writableStackTrace, List<FieldError> errors) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.errors = errors;
    }
}
