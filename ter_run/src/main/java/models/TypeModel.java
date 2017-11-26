package models;

/**
 * Type of the model.
 * MIP and LP programs are build with the CplexWrapper.
 * CP programs are build with IloCP.
 */
public enum TypeModel {
    /**
     * Mixed Integer Program.
     */
    MIP("mip"),

    /**
     * Linear Program.
     */
    LP("lp"),

    /**
     * Constraint Program
     */
    CP("cp");

    private final String s;

    TypeModel(String s) {
        this.s = s;
    }

    @Override
    public String toString() {
        return s;
    }
}