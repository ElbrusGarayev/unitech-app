package az.uni.unitechapp.exception;

import az.uni.unitechapp.exception.base.NotFoundException;

public class UserNotFoundException extends NotFoundException {

    private static final long serialVersionUID = 1L;
    private static final String MESSAGE = "USER_NOT_FOUND";

    public UserNotFoundException() {
        super(MESSAGE);
    }

}
