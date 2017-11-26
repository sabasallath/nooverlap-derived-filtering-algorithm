package models.displayer;

/**
 * Model displayer encapsulation common for MIP, LP or CP model.
 */
public interface ModelDisplayer {

    /**
     * Get initial data visualisation for MIP, LP or CP model.
     * @return Data visualisation before the solving of the model.
     */
    String getInitial();

    /**
     * Get initial data visualisation for MIP, LP or CP model.
     * @return Data visualisation after the solving of the model.
     */
    String getSolution();

}
