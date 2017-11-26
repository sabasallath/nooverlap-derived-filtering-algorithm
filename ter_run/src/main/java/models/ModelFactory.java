package models;

import jobs.Jobs;
import models.cut.Cut;
import models.tmax.TMax;
import models.tmax.TypeTMax;

/**
 * Model factory to simplify and abstract the Model creation.
 */
public final class ModelFactory {

    private static Cut defaultCut = Cut.AB();
    private static TypeTMax defaultTMax = TypeTMax.OPTIMAL;

    /**
     * Build a CP model with default Tmax and no cut.
     * @param jobs Jobs to model.
     * @return Model encapsulating a Cp model.
     */
    public static Model cp(Jobs jobs) {
        return build(jobs, TypeModel.CP, new TMax(jobs, defaultTMax), Cut.NONE());
    }

    /**
     * Build a MIP with the default cut and default Tmax.
     * @param jobs Jobs to model.
     * @return ModelCplexItf encapsulating a MIP model.
     */
    public static ModelCplexItf mip(Jobs jobs) {
        return mip(jobs, defaultCut);
    }

    /**
     * Build a LP with the default cut and default Tmax.
     * @param jobs Jobs to model.
     * @return ModelCplexItf encapsulating a LP model.
     */
    public static ModelCplexItf lp(Jobs jobs) {
        return lp(jobs, defaultCut);
    }

    /**
     * Build a MIP with the default Tmax.
     * @param jobs Jobs to model.
     * @param cut Cut to be use in the model.
     * @return ModelCplexItf encapsulating a MIP model.
     */
    public static ModelCplexItf mip(Jobs jobs, Cut cut) {
        return buildCplex(jobs, TypeModel.MIP, new TMax(jobs, defaultTMax), cut);
    }

    /**
     * Build a LP with the default Tmax.
     * @param jobs Jobs to model.
     * @param cut Cut to be use in the model.
     * @return ModelCplexItf encapsulating a LP model.
     */
    public static ModelCplexItf lp(Jobs jobs, Cut cut) {
        return buildCplex(jobs, TypeModel.LP, new TMax(jobs, defaultTMax), cut);
    }

    /**
     * Build a ModelCplexItf with all options available.
     * Note that grid generation is disabled.
     * @param jobs Jobs to model.
     * @param typeModel Type of the model to build.
     * @param tMax Tmax model.
     * @param cut Cut to be use in the model.
     * @return ModelCplexItf encapsulating a LP or Mip model.
     */
    private static ModelCplexItf buildCplex(Jobs jobs, TypeModel typeModel, TMax tMax, Cut cut) {
        ModelCplexItf model;

        switch (typeModel) {
            case MIP:
                model = new ModelCplex(jobs, typeModel, cut, tMax);
                break;
            case LP:
                model = new ModelCplex(jobs, typeModel, cut, tMax);
                break;
            default:
                throw new IllegalArgumentException("models.TypeModel :" + typeModel.toString());
        }

        return model;
    }

    /**
     * Build a MIP with the default Tmax and grid generation.
     * Encapsulate the resulting MIP model in higher abstraction
     * in order to treat MIP, LP and CP model
     * as undifferentiated model.
     * @param jobs Jobs to model.
     * @param cut Cut to be use in the model.
     * @return Model encapsulating a MIP model.
     */
    public static Model mipsGrid(Jobs jobs, Cut cut) {
        return build(jobs, TypeModel.MIP, new TMax(jobs, defaultTMax), cut, true);
    }

    /**
     * Build a LP with the default Tmax and grid generation.
     * Encapsulate the resulting LP model in higher abstraction
     * in order to treat MIP, LP and CP model as undifferentiated model.
     * @param jobs Jobs to model.
     * @param cut Cut to be use in the model.
     * @return Model encapsulating a MIP model.
     */
    public static Model lpGrid(Jobs jobs, Cut cut) {
        return build(jobs, TypeModel.LP, new TMax(jobs, defaultTMax), cut, true);
    }

    /**
     * Build a CP with the default Tmax, no cut and grid generation.
     * Encapsulate the resulting CP model in a higher abstraction
     * in order to treat MIP, LP and CP model as undifferentiated model.
     * @param jobs Jobs to model.
     * @return Model encapsulating a MIP model.
     */
    public static Model cpGrid(Jobs jobs) {
        return build(jobs, TypeModel.CP, new TMax(jobs, defaultTMax), Cut.NONE(), true);
    }

    /**
     * Build a Model with all option available
     * except for the grid generation disabled by default.
     * Encapsulate the resulting MIP, LP or CP model in a higher abstraction.
     * in order to treat MIP, LP and CP model as undifferentiated model.
     * @param jobs Jobs to model.
     * @param typeModel Type of the model to build.
     * @param tMax Tmax model.
     * @param cut Cut to be use in the model.
     * @return Model encapsulating a MIP, LP or CP model.
     */
    public static Model build(Jobs jobs, TypeModel typeModel, TMax tMax, Cut cut) {
        return build(jobs, typeModel, tMax, cut, false);
    }

    /**
     * Build a Model with all option available.
     * Encapsulate the resulting MIP, LP or CP model in a higher abstraction.
     * in order to treat MIP, LP and CP model as undifferentiated model.
     * @param jobs Jobs to model.
     * @param typeModel Type of the model to build.
     * @param tMax Tmax model.
     * @param cut Cut to be use in the model.
     * @param grid Enable or disabled the grid generation.
     * @return Model encapsulating a MIP model.
     */
    public static Model build(Jobs jobs, TypeModel typeModel, TMax tMax, Cut cut, Boolean grid) {

        Model model;

        switch (typeModel) {
            case MIP:
                model = new ModelCplex(jobs, typeModel, cut, tMax, grid);
                break;
            case LP:
                model = new ModelCplex(jobs, typeModel, cut, tMax, grid);
                break;
            case CP:
                // No cut in CP model since it does'nt use it.
                model = new ModelCp(jobs, tMax, grid);
                break;
            default:
                throw new IllegalArgumentException("models.TypeModel :" + typeModel.toString());
        }

        return model;
    }
}
