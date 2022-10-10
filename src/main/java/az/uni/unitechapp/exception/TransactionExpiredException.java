package az.uni.unitechapp.exception;

import az.uni.unitechapp.exception.base.InvalidStateException;

public class TransactionExpiredException extends InvalidStateException {

    private static final long serialVersionUID = 1L;
    private static final String MESSAGE = "TRANSACTION_EXPIRED";

    public TransactionExpiredException() {
        super(MESSAGE);
    }

}
