package transport.core;

public class ReductionImpossibleException extends TransportException {
    private static final long serialVersionUID = 1L;

    public ReductionImpossibleException(String message) {
        super(message);
    }
}
