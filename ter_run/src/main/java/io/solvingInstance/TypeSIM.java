package io.solvingInstance;

import models.cut.TypeCut;

/**
 * Type of value that can be added in a Solving instance.
 */
public enum TypeSIM {
    /**
     * Name.
     */
    NAME("n"),

    /**
     * Cp Makespan.
     */
    CP_MAKESPAN("mCp"),

    /**
     * Lp objective.
     */
    LP_OBJECTIVE("zLP"),

    /**
     * Mips objective.
     */
    MIPS_OBJECTIVE("zMP"),

    /**
     * Cp domains Initial Size.
     */
    CP_DOMAIN_INITIAL("diCP"),

    /**
     * Lp domains Initial Size.
     */
    LP_DOMAIN_INITIAL("diLP"),

    /**
     * Mips domains Initial Size.
     */
    MIPS_DOMAIN_INITIAL("diMP"),

    /**
     * Cp domains Reduced Size.
     */
    CP_DOMAIN_REDUCED("drCP"),

    /**
     * Lp domains Reduced Size.
     */
    LP_DOMAIN_REDUCED("drLP"),

    /**
     * arc coherence domains Reduction.
     */
    MIPS_DOMAIN_REDUCED("arc"),

    /**
     * Mips domains Reduced Size, edge only.
     */
    CP_CPU("cpuCP"),

    /**
     * Lp domais Reduced size, edge only.
     */
    LP_CPU("cpuLP"),

    /**
     * Cpu time to solve Cp.
     */
    MIPS_CPU("cpuMP"),

    /**
     * Cpu time to solve LP.
     */
    CUT("cut"),

    /**
     * Cpu time to solve MIPS.
     */
    MIPS_DOMAIN_REDUCED_BOUNDED("drMp"),

    /**
     * cut use in the model.
     */
    LP_DOMAIN_REDUCED_BOUNDED("dbLP");

    private final String s;

    TypeSIM(String s) {
        this.s = s;
    }

    /**
     * Format string instance members in a consistent way.
     * @param in Unformatted string instance member.
     * @return Formatted string instance member.
     */
    public String format(String in) {
        switch (this) {
            case NAME:
                return String.format("%10.10s", in);
            case CUT:
                return String.format("%6.6s", in);
            default:
                return String.format("%4.4s", in);
        }
    }

    /**
     * Format integer instance members in a consistent way.
     * @param i Unformatted integer member.
     * @return Formatted integer member.
     */
    public String format(int i) {
        return String.format("%4d", i);
    }

    /**
     * Format double instance members in a consistent way.
     * @param d Unformatted double member.
     * @return Formatted double member.
     */
    public String format(double d) {
        return String.format("%4.0f", d);
    }

    /**
     * Format long instance members in a consistent way.
     * @param l Unformatted long member.
     * @return Formatted long member.
     */
    public String format(long l) {
        return String.format("%4d", l);

    }

    @Override
    public String toString() {
        return s;
    }

    public String format(TypeCut cut) {
        return format(cut.toString());
    }

    /**
     * Used to build the help header of a solving instance result..
     * @return An helper who correlate abbreviation for SI member and it's meaning.
     */
    public String helper() {
        switch (this) {
            case NAME :
                return "n = name";
            case CP_MAKESPAN :
                return "mCp = Cp Makespan";
            case LP_OBJECTIVE :
                return "zLP = Lp objective";
            case MIPS_OBJECTIVE :
                return "zMP = Mips objective";
            case CP_DOMAIN_INITIAL :
                return "diCP = Cp domains Initial Size";
            case LP_DOMAIN_INITIAL :
                return "diLP = Lp domains Initial Size";
            case MIPS_DOMAIN_INITIAL :
                return "diMP = Mips domains Initial Size";
            case CP_DOMAIN_REDUCED :
                return "drCP = Cp domains Reduced Size";
            case LP_DOMAIN_REDUCED :
                return "drLP = Lp domains Reduced Size";
            case MIPS_DOMAIN_REDUCED :
                return "arc = arc coherence domains Reduction";
            case MIPS_DOMAIN_REDUCED_BOUNDED:
                return "drMP = Mips domains Reduced Size, edge only";
            case LP_DOMAIN_REDUCED_BOUNDED:
                return "dbLP = Lp domais Reduced size, edge only";
            case CP_CPU :
                return "cpuCP = Cpu time to solve Cp";
            case LP_CPU :
                return "cpuLP = Cpu time to solve LP";
            case MIPS_CPU :
                return "cpuMP = Cpu time to solve MIPS";
            case CUT:
                return "cut = cut use in the model";
            default:
                throw new IllegalArgumentException();
        }
    }
}
