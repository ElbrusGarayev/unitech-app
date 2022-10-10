package az.uni.unitechapp.exception;

import az.uni.unitechapp.exception.base.NotFoundException;

public class TransactionNotFoundException extends NotFoundException {

    private static final long serialVersionUID = 1L;
    private static final String MESSAGE = "TRANSACTION_NOT_FOUND";

    public TransactionNotFoundException() {
        super(MESSAGE);
    }

}
