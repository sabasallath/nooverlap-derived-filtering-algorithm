package domains;

import javax.annotation.concurrent.Immutable;

/**
 * Immutable domain valid starting size of a model before and after the propagation phase.
 */
@Immutable
public class DomainSize {
    private final int initial;
    private final int finalSize;
    private final int finalSizeCpComparable;

    /**
     * @param initialSize Domain valid starting size of a model before the propagation phase.
     * @param finalSize  Domain valid starting size of a model after the propagation phase.
     * @param finalSizeCpComparable Domain valid starting size of a model after the propagation phase.
     */
    public DomainSize(int initialSize, int finalSize, int finalSizeCpComparable) {
        this.initial = initialSize;
        this.finalSize = finalSize;
        this.finalSizeCpComparable = finalSizeCpComparable;
    }

    /**
     * @return Domain valid starting size of a model before the propagation phase.
     */
    public int getInitial() {
        return initial;
    }

    /**
     * @return Domain valid starting size of a model after the propagation phase.
     */
    public int getReduced() {
        return finalSize;
    }

    /**
     * Use this function to compare domain size with the Cp model propagation reduced domain size.
     * Because the CP model only reduce the domain size bound.
     * @return Domain valid starting size between the min and max bound after the propagation.
     */
    public int getReducedBounded() {
        return finalSizeCpComparable;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DomainSize that = (DomainSize) o;
        return initial == that.initial &&
                finalSize == that.finalSize &&
                finalSizeCpComparable == that.finalSizeCpComparable;
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(initial, finalSize, finalSizeCpComparable);
    }

    @Override
    public String toString() {
        return "DomainSize{" + "initial=" + initial +
                ", finalSize=" + finalSize +
                ", finalSizeCpComparable=" + finalSizeCpComparable +
                '}';
    }
}
