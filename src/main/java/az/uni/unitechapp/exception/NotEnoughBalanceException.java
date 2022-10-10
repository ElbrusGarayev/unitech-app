package az.uni.unitechapp.exception;

import az.uni.unitechapp.exception.base.InvalidStateException;

public class NotEnoughBalanceException extends InvalidStateException {

    private static final long serialVersionUID = 1L;
    private static final String MESSAGE = "NOT_ENOUGH_BALANCE";

    public NotEnoughBalanceException() {
        super(MESSAGE);
    }

}
