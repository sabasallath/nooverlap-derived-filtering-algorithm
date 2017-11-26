package models.cut;

import com.google.common.collect.ImmutableList;
import jdk.nashorn.internal.ir.annotations.Immutable;
import org.apache.commons.math3.util.Combinations;

import java.util.*;

/**
 * This class provides an immutable singleton encapsulation of Cut set.
 */
@Immutable
public final class Cuts implements Iterable<Cut> {

    private static final Cuts cuts = new Cuts();
    private static ImmutableList<Cut> lCut;

    private Cuts() {

        LinkedList<Cut> mMutableSet = new LinkedList<>();

        final EnumSet<TypeCut> typeCutsEnumSet = EnumSet.allOf(TypeCut.class);

        List<TypeCut> values = new ArrayList<>(typeCutsEnumSet.size());
        values.addAll(typeCutsEnumSet);

        List<List<TypeCut>> lltc = new LinkedList<>();
        for (int i = 0; i < typeCutsEnumSet.size() + 1; i++) {
            Combinations nk = new Combinations(typeCutsEnumSet.size(), i);
            for (int[] aNk : nk) {
                List<TypeCut> ltc = new LinkedList<>();
                for (int v : aNk) {
                    ltc.add(values.get(v));
                }
                lltc.add(ltc);
            }
        }

        for (List<TypeCut> list : lltc) {
            EnumSet<TypeCut> temp = EnumSet.noneOf(TypeCut.class);
            temp.addAll(list);
            mMutableSet.add(new Cut(EnumSet.copyOf(temp)));
        }
        lCut = ImmutableList.copyOf(mMutableSet);
    }

    /**
     * @return The unique instance of the Cuts class.
     */
    public static Cuts getInstance() {
        return cuts;
    }

    /**
     * Return All cut Combinatorial Set
     * such as A, B, C, AB, AC, BC, ...
     * @return All TypeCut Combinatorial Set
     */
    public ImmutableList<Cut> getSets() {
        return lCut;
    }

    /**
     * @param in Cut to not consider in the set.
     * @return All cut combination set that does not contains the in Cut
     */
    public ImmutableList<Cut> getSetsWithout(Cut in) {
        List<Cut> mutableList = new LinkedList<>();
        for (Cut c : lCut) {
            if (! c.containsAny(in)) {
                mutableList.add(c);
            }
        }
        return ImmutableList.copyOf(mutableList);
    }

    /**
     * @param in Cut to consider in the set.
     * @return All cut combination Set that contains the in Cut
     */
    public ImmutableList<Cut> getSetsWith(Cut in) {
        return getSetsWithout(in.reverse());
    }

    /**
     * Convert a string to a corresponding Cuts.
     * @param strCuts String input.²
     * @return Cuts of the corresponding input string.
     * @throws Exception if the string input does not match any cut.
     */
    public static ImmutableList<Cut> fromString(String strCuts) throws Exception {
        return Cuts.getInstance().getSetsWith(Cut.fromString(strCuts));
    }

    @Override
    public boolean equals(Object o) {
        return this == o;
    }

    @Override
    public int hashCode() {
        return com.google.common.base.Objects.hashCode((int) (TypeCut.values().length * 31));
    }

    @Override
    public Iterator<Cut> iterator() {
        return lCut.iterator();
    }

    @Override
    public String toString() {
        StringJoiner sj = new StringJoiner(", ", "{", "}");
        this.forEach(e -> sj.add(e.toString()));
        return sj.toString();
    }
}
