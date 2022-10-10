package az.uni.unitechapp.exception.base;

import lombok.Getter;

@Getter
public class AccessDeniedException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private final String code;

    public AccessDeniedException(String code, String message) {
        super(message);
        this.code = code;
    }

}
