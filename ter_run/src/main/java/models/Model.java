package models;

import domains.DomainSize;
import jobs.Jobs;

/**
 * Common interface for MIP, LP and CP models.
 */
public interface Model {

    /**
     * @return Jobs of the model.
     */
    Jobs getJobs();

    /**
     * @return Type of the model.
     */
    TypeModel getType();

    /**
     * @return Solution of the model.
     */
    Solution getSolution();

    /**
     * @return Domain size of the model before and after reduction.
     */
    DomainSize dSize();
}

