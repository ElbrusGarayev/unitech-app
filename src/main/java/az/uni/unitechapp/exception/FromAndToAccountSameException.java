package az.uni.unitechapp.exception;

import az.uni.unitechapp.exception.base.InvalidStateException;

public class FromAndToAccountSameException extends InvalidStateException {

    private static final long serialVersionUID = 1L;
    private static final String MESSAGE = "FROM_AND_TO_ACCOUNT_SAME";

    public FromAndToAccountSameException() {
        super(MESSAGE);
    }

}
