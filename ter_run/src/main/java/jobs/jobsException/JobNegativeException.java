package jobs.jobsException;

public final class JobNegativeException extends Exception {

    public JobNegativeException() {
        super();
    }

    public JobNegativeException(String s) {
        super(s);
    }

    public JobNegativeException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public JobNegativeException(Throwable throwable) {
        super(throwable);
    }
}
