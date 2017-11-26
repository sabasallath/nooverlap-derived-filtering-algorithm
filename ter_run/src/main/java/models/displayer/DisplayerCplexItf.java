package models.displayer;

import jobs.Jobs;

/**
 * Model displayer encapsulation common for MIP and LP model.
 */
public interface DisplayerCplexItf {

    boolean notNull(SelectData sd, int i, int t);

    double getValue(SelectData sd, int i, int t);

    double getValue(int t);

    /**
     *
     * @return Jobs from the displayer.
     */
    Jobs getJobs();
}
