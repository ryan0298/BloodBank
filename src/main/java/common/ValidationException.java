package common;

/**
 *
 * @author Shariar (Shawn) Emami
 */
public class ValidationException extends RuntimeException {

    public ValidationException() {
        super();
    }

    public ValidationException(String message) {
        super(message);
    }

    public ValidationException(Throwable t) {
        super(t);
    }

    public ValidationException(String message, Throwable t) {
        super(message, t);
    }
}
