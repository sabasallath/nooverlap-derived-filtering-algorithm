package models.tmax;

import javax.annotation.concurrent.Immutable;

@Immutable
public enum TypeTMax {
    /**
     * Optimal value.
     */
    OPTIMAL("op"),

    /**
     * Optimal value + 10 %.
     */
    OP10("op10"),

    /**
     * Return the inclusive lct (Last completion time) of all jobs.
     */
    LCT("lct");

    private final String s;

    TypeTMax(String s) {
        this.s = s;
    }

    @Override
    public String toString() {
        return s;
    }
}
