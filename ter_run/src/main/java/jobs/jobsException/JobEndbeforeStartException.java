package jobs.jobsException;

public final class JobEndbeforeStartException extends Exception {

    public JobEndbeforeStartException() {
        super();
    }

    public JobEndbeforeStartException(String s) {
        super(s);
    }

    public JobEndbeforeStartException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public JobEndbeforeStartException(Throwable throwable) {
        super(throwable);
    }
}
