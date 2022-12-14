package exceptions;

import logic.Cell;

public class OccupiedCellException extends InvalidOperationException {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public OccupiedCellException() {
        super("La celda está ocupada.");
    }

    public OccupiedCellException(Cell pos) {
        super(String.format("La celda %s está ocupada.", pos.toString()));
    }

    public OccupiedCellException(String message) {
        super(message);
    }

    public OccupiedCellException(Throwable cause) {
        super(cause);
    }

    public OccupiedCellException(String message, Throwable cause) {
        super(message, cause);
    }

    public OccupiedCellException(
        String message,
        Throwable cause,
        boolean enableSuppression,
        boolean writeableStackTrace
    ) {
        super(message, cause, enableSuppression, writeableStackTrace);
    }
}
