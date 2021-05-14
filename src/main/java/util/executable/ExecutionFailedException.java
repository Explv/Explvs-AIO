package util.executable;

public class ExecutionFailedException extends RuntimeException {
    public ExecutionFailedException(String message) {
        super(message);
    }
}
