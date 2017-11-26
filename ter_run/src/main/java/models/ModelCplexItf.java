package models;

import domains.DomainSlot;
import models.displayer.DisplayerCplexItf;
import models.tmax.TMax;

/**
 * Common interface for MIP and LP models.
 * Modeling of the noOverlap global constraint.
 * A solution of this model is a solution of the noOverlap.
 */
public interface ModelCplexItf extends Model, DisplayerCplexItf {

    /**
     * @param ds Force this starting domain slot to be used when solving.
     * @return Solution of the model with a fixed starting time slot.
     */
    Solution resolveWithFixedStart(DomainSlot ds);

    /**
     * @return Tmax used in this model.
     */
    TMax getTMax();

}
