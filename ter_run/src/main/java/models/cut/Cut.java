package models.cut;

import com.google.common.base.Objects;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;

import javax.annotation.concurrent.Immutable;
import java.util.*;

/**
 * This class provides a cut which is an immutable set of TypeCut whose purpose is to reinforce the linear model.
 * The combination of those cuts make the relaxed linear program (LP) behave like the integer program (IP).
 */
@Immutable
public final class Cut implements Iterable<TypeCut> {

    private final ImmutableSet<TypeCut> typeCutSet;

    /**
     * Constructor of the Cut class.
     * @param typeCutSet Set of typeCut.
     */
    public Cut(EnumSet<TypeCut> typeCutSet) {
        this.typeCutSet = Sets.immutableEnumSet(typeCutSet);
    }

    /**
     * @param typeCut Type of the cut.
     * @return Return true if it contains the given typeCut, else false
     */
    public boolean contains(TypeCut typeCut) {
        return typeCutSet.contains(typeCut);
    }

    /**
     * @param in Input cut
     * @return Return true if the given parameter contains any of the typeCut contained in this current Cut.
     */
    public boolean containsAny(Cut in) {
        for (TypeCut i : in) {
            if (this.contains(i)) {
                return true;
            }
        }

        return false;
    }

    /**
     * @param in Input cut.
     * @return Return true if the given parameter contains all of the typeCut contained in this current Cut.
     */
    public boolean containsAll(Cut in) {
        for (TypeCut i : in) {
            if (! this.contains(i)) {
                return false;
            }
        }

        return true;
    }

    /**
     * @return Return a new Cut object who contains the difference between a set of all TypeCut.
     * and this current Cut
     */
    public Cut reverse() {
        EnumSet<TypeCut> all = EnumSet.allOf(TypeCut.class);
        this.forEach(all::remove);
        return new Cut(all);
    }

    /**
     * @return Return an empty cut.
     */
    public static Cut NONE() {
        return new Cut(EnumSet.noneOf(TypeCut.class));
    }

    /**
     * @return Return a cut that contains only the A typeCut.
     */
    public static Cut A() {
        return new Cut(EnumSet.of(TypeCut.A_SUM_XIT_EQUAL_PI));
    }

    /**
     * @return Return a cut that contains only the B typeCut.
     */
    public static Cut B() {
        return new Cut(EnumSet.of(TypeCut.B_START_CLOSE_TO_RUN));
    }

    /**
     * @return Return a cut that contains only the C typeCut.
     */
    public static Cut C() {
        return new Cut(EnumSet.of(TypeCut.C_PRECEDENCE));
    }

    /**
     * @return Return a cut that contains only the D typeCut.
     */
    public static Cut D() {
        return new Cut(EnumSet.of(TypeCut.D_MANDATORY_SECTION));
    }

    /**
     * @return Return a cut that contains the A and the B typeCut.
     */
    public static Cut AB() {
        return new Cut(EnumSet.of(TypeCut.A_SUM_XIT_EQUAL_PI, TypeCut.B_START_CLOSE_TO_RUN));
    }

    /**
     * @return Return a cut that contains the C and the D typeCut.
     */
    public static Cut CD() {
        return new Cut(EnumSet.of(TypeCut.C_PRECEDENCE, TypeCut.D_MANDATORY_SECTION));
    }

    /**
     * @return Return a cut that contains the A, B, C and D typeCut.
     */
    public static Cut ABCD() {
        return new Cut(EnumSet.of(
                TypeCut.A_SUM_XIT_EQUAL_PI,
                TypeCut.B_START_CLOSE_TO_RUN,
                TypeCut.C_PRECEDENCE,
                TypeCut.D_MANDATORY_SECTION));
    }

    /**
     * Convert a string to a corresponding Cut.
     * @param strCut String input.
     * @return Cut of the corresponding input string.
     */
    public static Cut fromString(String strCut) {
        if (isMatches(strCut)) {
            List<TypeCut> tclist = new LinkedList<>();
            for (int i = 0; i < strCut.length(); i++) {
                try {
                    TypeCut cut = TypeCut.fromChar(Character.toUpperCase(strCut.charAt(i)));
                    tclist.add(cut);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            return new Cut(EnumSet.copyOf(tclist));

        } else {
            throw new RuntimeException("Wrong cuts option, available cuts are :\n" + Cuts.getInstance().getSets().toString());
        }

    }

    /**
     * @param strCut String input.
     * @return True if the input is a valid fromString input
     */
    public static boolean isMatches(String strCut) {
        return strCut.matches("[a-dA-D]*");
    }

    @Override
    public Iterator<TypeCut> iterator() {
        return typeCutSet.iterator();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cut typeCuts = (Cut) o;
        return Objects.equal(typeCutSet, typeCuts.typeCutSet);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(typeCutSet);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        this.forEach(e -> sb.append(e.toString()));
        return sb.toString();
    }
}