package az.uni.unitechapp.exception;

import az.uni.unitechapp.exception.base.AlreadyExistsException;

public class PinAlreadyExistsException extends AlreadyExistsException {

    private static final long serialVersionUID = 1L;
    private static final String MESSAGE = "PIN_ALREADY_EXISTS";

    public PinAlreadyExistsException() {
        super(MESSAGE);
    }

}
