package jobs;

import com.google.common.base.Objects;
import com.google.common.collect.ImmutableList;
import javax.annotation.concurrent.Immutable;
import java.util.LinkedList;
import java.util.OptionalInt;

/**
 * Job is an immutable data class that represent an ordered list of tasks.
 * Each task is characterize with it's
 * Starting date, Due date, Duration and Id.
 * Since the time is discrete a date is just a positive integer value.
 */
@Immutable
public final class Jobs {

    private final ImmutableList<Job> jobs;
    private final int est;
    private final int lct;
    private final int ect;
    private final int lst;
    private final int H;
    private final int p;

    /**
     * Constructor or the jobs data class.
     * @param jobs List of jobs.
     */
    public Jobs(LinkedList<Job> jobs) {
        int estL = Integer.MAX_VALUE;
        int lctL = -1;
        int ectL = Integer.MAX_VALUE;
        int lstL = -1;
        int HL = -1;
        int pL = 0;

        for (int i = 0; i < jobs.size(); i++) {
            Job job = jobs.get(i);

            if (job.getI() != i) {
                throw new RuntimeException("Invalid job index" +
                        ", required = " + i + "" +
                        ", found = " + job.getI());
            }

            if (job.getEst() < estL) {
                estL = job.getEst();
            }

            if (job.getLct() > lctL) {
                lctL = job.getLct();
                HL = job.getLct() + 1;
            }

            pL += job.getP();
            ectL = estL + pL;
            lstL = Math.max(lctL - pL, estL);
        }

        this.est = estL;
        this.lct = lctL;
        this.ect = ectL;
        this.lst = lstL;
        this.H = HL;
        this.p = pL;
        this.jobs = ImmutableList.copyOf(jobs);

    }

    /**
     * @return Earliest starting time of all jobs.
     */
    public int getEst() {
        return est;
    }

    /**
     * @return Last completion time of all jobs.
     */
    public int getLct() {
        return lct;
    }

    /**
     * @return Earliest completion time of all jobs.
     */
    public int getEct() {
        return ect;
    }

    /**
     * @return Latest starting time of all jobs.
     */
    public int getLst() {
        return lst;
    }

    /**
     * @return Horizon of all jobs.
     * Corresponding to the date after the last completion time of all jobs.
     */
    public int getH() {
        return H;
    }

    /**
     * @return Sum duration of all jobs.
     */
    public int getP() {
        return p;
    }

    /**
     * @return Number of jobs.
     */
    public int size() {
        return jobs.size();
    }

    /**
     * @param i Job id.
     * @return Earliest starting time of job i.
     */
    public int getEst(int i) {
        return jobs.get(i).getEst();
    }

    /**
     * @param i Job id.
     * @return Last completion time (inclusive) of job i.
     */
    public int getLct(int i) {
        return jobs.get(i).getLct();
    }

    /**
     * @param i Job id.
     * @return Earliest completion time of job i.
     */
    public int getEct(int i) {
        return jobs.get(i).getEct();
    }

    /**
     * @param i Job id.
     * @return Last starting time of job i.
     */
    public int getLst(int i) {
        return jobs.get(i).getLst();
    }

    /**
     * @param i Job id.
     * @return Duration of job i.
     */
    public int getP(int i) {
        return jobs.get(i).getP();
    }

    /**
     * @param i Job id.
     * @return Obligatory section earliest date if exists of job i.
     */
    public OptionalInt getOStart(int i) {
        return jobs.get(i).getOStart();
    }

    /**
     * @param i Job id.
     * @return Obligatory section latest date if exists of job i.
     */
    public OptionalInt getOEnd(int i) {
        return jobs.get(i).getOEnd();
    }

    /**
     * @param i Job id.
     * @return Obligatory section duration if exists of job i.
     */
    public OptionalInt getOP(int i) {
        return jobs.get(i).getOP();
    }

    /**
     * @param i Job id.
     * @return List of valid start date of job i.
     */
    public ImmutableList<Integer> getValidStart(int i) {
        return jobs.get(i).getValidStart();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Jobs jobs1 = (Jobs) o;
        return est == jobs1.est &&
                lct == jobs1.lct &&
                ect == jobs1.ect &&
                lst == jobs1.lst &&
                H == jobs1.H &&
                p == jobs1.p &&
                Objects.equal(jobs, jobs1.jobs);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(jobs, est, lct, ect, lst, H, p);
    }

    @Override
    public String toString() {
        StringBuilder res = new StringBuilder();
        for (Job job : jobs) {
            res.append(job.toString()).append("\n");
        }
        res.append("@:[").append(est).append("-");
        res.append(lst).append(", ");
        res.append(ect).append("-");
        res.append(lct).append("]");
        res.append("(").append(p).append(")\n");

        return res.toString();
    }

}
