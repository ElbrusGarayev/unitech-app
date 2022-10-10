package az.uni.unitechapp.exception;

import az.uni.unitechapp.exception.base.NotFoundException;

public class AccountNotFoundException extends NotFoundException {

    private static final long serialVersionUID = 1L;
    private static final String MESSAGE = "ACCOUNT_NOT_EXISTS_OR_INACTIVE";

    public AccountNotFoundException() {
        super(MESSAGE);
    }

}
