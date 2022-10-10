package az.uni.unitechapp.exception;

import az.uni.unitechapp.exception.base.NotFoundException;

public class CurrencyNotFoundException extends NotFoundException {

    private static final long serialVersionUID = 1L;
    private static final String MESSAGE = "CURRENCY_NOT_FOUND";

    public CurrencyNotFoundException() {
        super(MESSAGE);
    }

}
