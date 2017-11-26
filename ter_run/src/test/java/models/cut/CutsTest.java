package models.cut;

import com.google.common.collect.ImmutableList;
import org.junit.Test;

import java.util.EnumSet;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static org.mutabilitydetector.unittesting.MutabilityAssert.assertImmutable;

public class CutsTest {

    @Test
    public void IsImmutable() {
        assertImmutable(Cuts.class);
    }

    @Test
    public void equalsContract() {
        assertEquals(Cuts.getInstance(), Cuts.getInstance());
        assertEquals(Cuts.getInstance().hashCode(), Cuts.getInstance().hashCode());

        ImmutableList<Cut> sets = Cuts.getInstance().getSets();
        ImmutableList<Cut> setsWithout = Cuts.getInstance().getSetsWithout(Cut.A());
        assertNotEquals(sets, setsWithout);
        assertNotEquals(sets.hashCode(), setsWithout.hashCode());

        assertEquals(sets, Cuts.getInstance().getSetsWith(new Cut(EnumSet.allOf(TypeCut.class))));
    }

    @Test
    public void getSetsWithoutTest() throws Exception {
        ImmutableList<Cut> list = Cuts.getInstance().getSets();
        List<Cut> mList = new LinkedList<>();
        mList.addAll(list);
        mList.remove(Cut.A());
        mList.remove(
                new Cut(EnumSet.of(
                        TypeCut.A_SUM_XIT_EQUAL_PI,
                        TypeCut.B_START_CLOSE_TO_RUN)
                ));
        mList.remove(
                new Cut(EnumSet.of(
                        TypeCut.A_SUM_XIT_EQUAL_PI,
                        TypeCut.C_PRECEDENCE)
                ));
        mList.remove(
                new Cut(EnumSet.of(
                        TypeCut.A_SUM_XIT_EQUAL_PI,
                        TypeCut.D_MANDATORY_SECTION)
                ));
        mList.remove(
                new Cut(EnumSet.of(
                        TypeCut.A_SUM_XIT_EQUAL_PI,
                        TypeCut.B_START_CLOSE_TO_RUN,
                        TypeCut.C_PRECEDENCE)
                ));
        mList.remove(
        new Cut(EnumSet.of(
                TypeCut.A_SUM_XIT_EQUAL_PI,
                TypeCut.B_START_CLOSE_TO_RUN,
                TypeCut.D_MANDATORY_SECTION)
        ));
        mList.remove(
        new Cut(EnumSet.of(
                TypeCut.A_SUM_XIT_EQUAL_PI,
                TypeCut.C_PRECEDENCE,
                TypeCut.D_MANDATORY_SECTION)
        ));
        mList.remove(Cut.ABCD());

        assertEquals(mList, Cuts.getInstance().getSetsWithout(Cut.A()));
    }

    @Test
    public void getSetsWithTest() throws Exception {
        assertEquals(Cuts.getInstance().getSetsWith(Cut.AB()), Cuts.getInstance().getSetsWithout(Cut.CD()));
        assertEquals(Cuts.getInstance().getSetsWith(Cut.ABCD()), Cuts.getInstance().getSetsWithout(Cut.NONE()));
        assertEquals(Cuts.getInstance().getSetsWith(Cut.ABCD()), Cuts.getInstance().getSetsWith(Cut.NONE().reverse()));
    }

    @Test
    public void fromStringTest() throws Exception {
        assertEquals(Cuts.fromString("ABCD"), Cuts.getInstance().getSetsWith(Cut.ABCD()));

        boolean invalidCuts = false;
        try {
            Cuts.fromString("ABCDE");
        } catch (Exception e) {
            invalidCuts = true;
        }
        assertTrue(invalidCuts);
    }
}
