package exceptions;

public class LoadGameException extends Exception{
    private static final long serialVersionUID = 1L;

    public LoadGameException() {
        super("There was an error while loading the game.");
    }

    public LoadGameException(String message) {
        super(message);
    }

    public LoadGameException(Throwable cause) {
        super(cause);
    }

    public LoadGameException(String message, Throwable cause) {
        super(message, cause);
    }

    public LoadGameException(String message, Throwable cause,
                                     boolean enableSuppression, boolean writeableStackTrace) {
        super(message, cause, enableSuppression, writeableStackTrace);
    }
}