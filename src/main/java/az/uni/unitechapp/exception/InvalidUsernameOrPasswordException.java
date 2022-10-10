package az.uni.unitechapp.exception;

import az.uni.unitechapp.exception.base.InvalidStateException;

public class InvalidUsernameOrPasswordException extends InvalidStateException {

    private static final long serialVersionUID = 1L;
    private static final String MESSAGE = "INVALID_PIN_OR_PASSWORD";

    public InvalidUsernameOrPasswordException() {
        super(MESSAGE);
    }

}
