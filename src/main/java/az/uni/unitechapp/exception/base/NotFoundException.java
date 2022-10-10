package az.uni.unitechapp.exception.base;

import lombok.Getter;

@Getter
public class NotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public NotFoundException(String message) {
        super(message);
    }

}
