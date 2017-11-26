package io;

/**
 * Strings constants use to display separator in a consistent way.
 */
public enum DisplayConst {
    /**
     * Big separator.
     */
    SEPARATOR("................................................................."),

    /**
     * Smaller separator.
     */
    SHORTSEPARATOR("---------------------------");

    private final String s;

    DisplayConst(String s) {
        this.s = s;
    }

    @Override
    public String toString(){
        return s;
    }
}
