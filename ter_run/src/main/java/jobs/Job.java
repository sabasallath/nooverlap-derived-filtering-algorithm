package jobs;

import com.google.common.base.Objects;
import com.google.common.collect.ImmutableList;
import jobs.jobsException.JobEndbeforeStartException;
import jobs.jobsException.JobInvalidDurationException;
import jobs.jobsException.JobInvalidRankException;
import jobs.jobsException.JobNegativeException;

import javax.annotation.concurrent.Immutable;
import java.util.LinkedList;
import java.util.List;
import java.util.OptionalInt;

/**
 * Job is an immutable data class that represent a task.
 * Each task is characterize with it's
 * Starting date, Due date, Duration and Id.
 */
@Immutable
public final class Job {
    private final int p;
    private final int est;
    private final int lct;
    private final int i;
    private final int lst;
    private final int ect;

    /**
     * Job constructor.
     * @param est Earliest starting time.
     * @param lct Last completion time.
     *            Note that lct is not exclusive
     *            as it use to be in scheduling problem convention.
     * @param p Duration.
     * @param i Id.
     * @throws JobEndbeforeStartException The job end before it's start.
     * @throws JobInvalidDurationException The duration does not fit in the time window.
     * @throws JobInvalidRankException The rank is lower than 0.
     * @throws JobNegativeException Some input are negative.
     */
    public Job(int est, int lct, int p, int i) throws
            JobEndbeforeStartException,
            JobInvalidDurationException,
            JobInvalidRankException,
            JobNegativeException {

        this.est = est;
        this.lct = lct;
        this.p = p;
        this.i = i;
        this.lst = lct - p + 1;
        this.ect = est + p - 1;
        checkData();
    }

    private void checkData() throws
            JobEndbeforeStartException,
            JobInvalidDurationException,
            JobInvalidRankException,
            JobNegativeException {

        if (i < 0) {
            throw new JobInvalidRankException("Wrong Job input data" +
                    ", found("+ i +" > 0)" +
                    ", expected(i >= 0)");
        } else if (est < 0) {
            throw new JobNegativeException("Wrong Job input data" +
                    ", found(est(" + est + ") < 0)" +
                    ", expected(est(x) >= 0)");
        } else if (lct < 0) {
            throw new JobNegativeException("Wrong Job input data" +
                    ", found(lct(" + lct + ") < 0)" +
                    ", expected(lct(x) >= 0)");
        } else if (p <= 0) {
                throw new JobInvalidDurationException("Wrong Job input data" +
                        ", found(p(" + p + ") <= 0" +
                        ", expected(p(x) > 0)");
        } else if (est > lct) {
            throw new JobEndbeforeStartException("Wrong Job input data" +
                    ", found(est(" + est + ") > lst(" + lct  + "))" +
                    ", expected(est(x) < lst(x))");
        } else if (lct - est + 1 < p) {
            throw new JobInvalidDurationException("Wrong Job input data" +
                    ", found(lct(" + lct + ") - est(" + est + ") + 1 < " + p + ")" +
                    ", expected(lct(x) - est(x) > " + p + ")");
        }
    }

    /**
     * @return Id.
     */
    public int getI() {
        return i;
    }

    /**
     * @return Duration.
     */
    public int getP() {
        return p;
    }

    /**
     * @return Earliest starting time.
     */
    public int getEst() {
        return est;
    }

    /**
     * @return Last completion time (inclusive).
     */
    public int getLct() {
        return lct;
    }

    /**
     * @return Latest starting time.
     */
    public int getLst() {
        return lst;
    }

    /**
     * @return Earliest completion time.
     */
    public int getEct() {
        return ect;
    }

    /**
     * @return Obligatory section earliest date if exists.
     */
    public OptionalInt getOStart() {
        return getOP().isPresent() ?
                OptionalInt.of(lst) :
                OptionalInt.empty();
    }

    /**
     * @return Obligatory section latest date if exists.
     */
    public OptionalInt getOEnd() {
        return getOP().isPresent() ?
                OptionalInt.of(ect) :
                OptionalInt.empty();
    }

    /**
     * @return Obligatory section duration if it exists.
     */
    public OptionalInt getOP() {
        int op = ect - lst + 1;
        if (op <= 0) {
            return OptionalInt.empty();
        }
        return OptionalInt.of(op);
    }

    /**
     * @return List of valid start.
     */
    public ImmutableList<Integer> getValidStart() {
        List<Integer> l = new LinkedList<>();
        for (int i = est; i <= lst; i++) {
            l.add(i);
        }
        return ImmutableList.copyOf(l);
    }

    @Override
    public String toString() {
        return i + ":" + "[" + est + "-" + lst + ", "+ ect + "-" + lct + "](" + p + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Job job = (Job) o;
        return p == job.p &&
                est == job.est &&
                lct == job.lct &&
                i == job.i &&
                lst == job.lst &&
                ect == job.ect;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(p, est, lct, i, lst, ect);
    }
}
