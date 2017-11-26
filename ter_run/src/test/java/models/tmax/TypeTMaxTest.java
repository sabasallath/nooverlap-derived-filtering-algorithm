package models.tmax;

import models.tmax.TypeTMax;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mutabilitydetector.unittesting.MutabilityAssert.assertImmutable;

public class TypeTMaxTest {

    @Test
    public void IsImmutable() {
        assertImmutable(TypeTMax.class);
    }

    @Test
    public void equalsContract() {
        EqualsVerifier.forClass(TypeTMax.class).verify();
    }

    @Test
    public void toStringTest() {
        assertEquals("lct", TypeTMax.LCT.toString());
        assertEquals("op10", TypeTMax.OP10.toString());
        assertEquals("op", TypeTMax.OPTIMAL.toString());
    }
}
