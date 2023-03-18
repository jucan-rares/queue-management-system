package exception;

public class InputValidationFailedException extends RuntimeException {

    public InputValidationFailedException(String message) {
        super(message);
    }
}
