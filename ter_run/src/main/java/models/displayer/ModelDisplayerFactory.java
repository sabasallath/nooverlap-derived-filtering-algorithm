package models.displayer;

import models.Model;
import models.ModelCp;
import models.ModelCplexItf;

/**
 * ModelDisplayer factory to return a visualisation
 * according to the model the displayer is build from.
 */
public class ModelDisplayerFactory {

    /**
     * ModelDisplayer encapsulation of the underlying visualisation
     * of the data of the model according to the type of the model.
     * @param model Model to display.
     * @return A displayer accordingly to the type of the model.
     */
    public static ModelDisplayer build(Model model) {

        ModelDisplayer modelDisplayer;

        switch (model.getType()) {
            case MIP:
            case LP:
                if (model instanceof ModelCplexItf) {
                    modelDisplayer = new ModelCplexDisplayer((ModelCplexItf) model);
                } else {
                    throw new IllegalArgumentException("Not an instance of ModelCplex");
                }
                break;
            case CP:
                if (model instanceof ModelCp) {
                    modelDisplayer = new ModelCpDisplayer((ModelCp) model);
                } else {
                    throw new IllegalArgumentException("Not an instance of ModelCp");
                }
                break;
            default:
                throw new IllegalArgumentException("models.TypeModel");
        }

        return modelDisplayer;
    }
}
