package az.uni.unitechapp.exception;

public class InternalServerException extends RuntimeException {

    private static final String MESSAGE = "INTERNAL_SERVER_ERROR";
    private static final long serialVersionUID = -8009122393187904630L;


    public InternalServerException(Exception ex) {
        super(MESSAGE, ex);
    }

}
