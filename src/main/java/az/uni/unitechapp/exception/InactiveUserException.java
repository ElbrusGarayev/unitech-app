package az.uni.unitechapp.exception;

import az.uni.unitechapp.exception.base.InvalidStateException;

public class InactiveUserException extends InvalidStateException {

    private static final long serialVersionUID = 1L;
    private static final String MESSAGE = "INACTIVE_USER";

    public InactiveUserException() {
        super(MESSAGE);
    }

}
