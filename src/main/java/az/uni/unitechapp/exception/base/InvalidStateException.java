package az.uni.unitechapp.exception.base;

import lombok.Getter;

@Getter
public class InvalidStateException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public InvalidStateException(String message) {
        super(message);
    }

}
