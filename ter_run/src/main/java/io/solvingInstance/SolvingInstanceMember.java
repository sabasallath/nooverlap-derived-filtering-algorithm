package io.solvingInstance;

import models.cut.Cut;

/**
 * Member of a solving instance.
 * Contains it's type and it's value.
 */
public class SolvingInstanceMember {

    private final TypeSIM type;
    private final String value;

    /**
     * Solving instance constructor for string value.
     * @param type Type of the solving instance member.
     * @param value String value of the solving instance member.
     */
    public SolvingInstanceMember(TypeSIM type, String value) {
        this.type = type;
        this.value = type.format(value);

    }

    /**
     * Solving instance constructor for integer value.
     * @param type Type of the solving instance member.
     * @param i Integer value of the solving instance member.
     */
    public SolvingInstanceMember(TypeSIM type, int i) {
        this.type = type;
        this.value = type.format(i);
    }

    /**
     * Solving instance constructor for double value.
     * @param type Type of the solving instance member.
     * @param d Double value of the solving instance member.
     */
    public SolvingInstanceMember(TypeSIM type, double d) {
        this.type = type;
        this.value = type.format(d);
    }

    /**
     * Solving instance constructor for long value.
     * @param type Type of the solving instance member.
     * @param l Long value of the solving instance member.
     */
    public SolvingInstanceMember(TypeSIM type, long l) {
        this.type = type;
        this.value = type.format(l);
    }


    /**
     * Solving instance constructor for cut value.
     * @param type Type of the solving instance member.
     * @param cut Cut of the solving instance member.
     */
    public SolvingInstanceMember(TypeSIM type, Cut cut) {
        this.type = type;
        this.value = type.format(cut.toString());
    }

    /**
     * Type of the solving instance member.
     * @return The type of the solving instance member.
     */
    TypeSIM getType() {
        return type;
    }

    /**
     * String abbreviation of the type of the solving instance member.
     * @return String abbreviation of the type of the solving instance member.
     */
    String getHeader() {
        return type.format(type.toString());
    }


    /**
     * Value of the solving instance member.
     */
    @Override
    public String toString() {
        return value;
    }
}
