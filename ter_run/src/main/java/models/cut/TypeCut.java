package models.cut;

import javax.annotation.concurrent.Immutable;

/**
 * This class provides an enumeration of different type of cut whose purpose is to reinforce the linear model.
 */
@Immutable
public enum TypeCut {
    /**
     * Sum of x_it is equal to the duration of a job i
     */
    A_SUM_XIT_EQUAL_PI("A"),

    /**
     * If i is executed at time t, then i can only start in the interval [t - p_i + 1, t]
     */
    B_START_CLOSE_TO_RUN("B"),

    /**
     * If job i start at t, job j does'nt start in interval [t, t + p_i]
     */
    C_PRECEDENCE("C"),

    /**
     * If t belongs to the obligatory section of a job i,
     * x_it = 1 and sum of y_it = 1 on the obligatory section
     * and y_it = 0 outside of the obligatory section.
     */
    D_MANDATORY_SECTION("D");

    private final String s;

    TypeCut(String s) {
        this.s = s;
    }

    @Override
    public String toString() {
        return s;
    }

    /**
     * Convert a character to a corresponding TypeCut.
     * @param c Character input.
     * @return TypeCut of the entering character.
     * @throws Exception If the character does'nt match any TypeCut
     */
    public static TypeCut fromChar(Character c) throws Exception {
        switch (c) {
            case 'A':
                return A_SUM_XIT_EQUAL_PI;
            case 'B':
                return B_START_CLOSE_TO_RUN;
            case 'C':
                return C_PRECEDENCE;
            case 'D':
                return D_MANDATORY_SECTION;
            default:
                throw new Exception("Invalid character" + c);
        }
    }
}
