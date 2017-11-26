package models.cut;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Test;

import java.util.EnumSet;
import java.util.LinkedList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mutabilitydetector.unittesting.MutabilityAssert.assertImmutable;

public class CutTest {

    @Test
    public void IsImmutable() {
        assertImmutable(Cut.class);
    }

    @Test
    public void equalsContract() {
        EqualsVerifier.forClass(Cut.class).verify();
    }

    @Test
    public void containsTest() throws Exception {
        //!\ not exhaustive
        assertTrue(Cut.A().contains(TypeCut.A_SUM_XIT_EQUAL_PI));
        assertTrue(Cut.AB().contains(TypeCut.A_SUM_XIT_EQUAL_PI));
        assertTrue(Cut.ABCD().contains(TypeCut.A_SUM_XIT_EQUAL_PI));

        assertFalse(Cut.CD().contains(TypeCut.A_SUM_XIT_EQUAL_PI));
        assertFalse(Cut.B().contains(TypeCut.A_SUM_XIT_EQUAL_PI));
        assertFalse(Cut.C().contains(TypeCut.A_SUM_XIT_EQUAL_PI));
        assertFalse(Cut.D().contains(TypeCut.A_SUM_XIT_EQUAL_PI));

        assertTrue(Cut.AB().contains(TypeCut.A_SUM_XIT_EQUAL_PI));
        assertTrue(Cut.AB().contains(TypeCut.B_START_CLOSE_TO_RUN));
        assertFalse(Cut.AB().contains(TypeCut.C_PRECEDENCE));
        assertFalse(Cut.AB().contains(TypeCut.D_MANDATORY_SECTION));
    }

    @Test
    public void mirrorTest() throws Exception {
        assertEquals(Cut.AB(), Cut.CD().reverse());
        assertEquals(Cut.CD(), Cut.AB().reverse());
        assertEquals(Cut.NONE(), Cut.ABCD().reverse());
        assertEquals(Cut.ABCD(), Cut.NONE().reverse());

        EnumSet<TypeCut> all = EnumSet.allOf(TypeCut.class);
        all.remove(TypeCut.A_SUM_XIT_EQUAL_PI);
        assertEquals(new Cut(all), Cut.A().reverse());
    }

    @Test
    public void removableTest() throws Exception {
        LinkedList<Cut> cuts = new LinkedList<>();
        Cut cutA = new Cut(EnumSet.of(TypeCut.A_SUM_XIT_EQUAL_PI));
        cuts.add(cutA);

        assertTrue(cuts.contains(cutA));
        cuts.remove(cutA);

        assertTrue(cuts.isEmpty());
    }

    @Test
    public void setPropertyTest() throws Exception {
        Cut repeated = new Cut(EnumSet.of(TypeCut.A_SUM_XIT_EQUAL_PI, TypeCut.A_SUM_XIT_EQUAL_PI));
        Cut once = new Cut(EnumSet.of(TypeCut.A_SUM_XIT_EQUAL_PI));
        assertEquals(repeated, once);
    }

    @Test
    public void containsAnyTest() throws Exception {
        assertTrue(Cut.AB().containsAny(Cut.A()));
        assertTrue(Cut.AB().containsAny(Cut.B()));
        assertTrue(Cut.AB().containsAny(Cut.AB()));
        assertTrue(Cut.AB().containsAny(Cut.ABCD()));
        assertFalse(Cut.AB().containsAny(Cut.C()));
        assertFalse(Cut.AB().containsAny(Cut.D()));
        assertFalse(Cut.AB().containsAny(Cut.CD()));

        assertTrue(Cut.ABCD().containsAny(Cut.ABCD()));
    }

    @Test
    public void containsAllTest() throws Exception {
        assertTrue(Cut.AB().containsAll(Cut.A()));
        assertTrue(Cut.AB().containsAll(Cut.B()));
        assertTrue(Cut.AB().containsAll(Cut.AB()));
        assertFalse(Cut.AB().containsAll(Cut.ABCD()));
        assertFalse(Cut.AB().containsAll(Cut.C()));
        assertFalse(Cut.AB().containsAll(Cut.D()));
        assertFalse(Cut.AB().containsAll(Cut.CD()));

        assertTrue(Cut.ABCD().containsAll(Cut.AB()));
        assertTrue(Cut.ABCD().containsAll(Cut.CD()));
        assertTrue(Cut.ABCD().containsAll(Cut.ABCD()));
    }

    @Test
    public void fromStringTest() throws Exception {
        assertEquals(Cut.fromString("AB"), Cut.AB());
        assertEquals(Cut.fromString("ab"), Cut.AB());

        boolean invalidChar = false;
        try {
            Cut.fromString("E");
        } catch (Exception e) {
            invalidChar = true;
        }
        assertTrue(invalidChar);
    }
}
