package models.cut;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Test;

import static org.mutabilitydetector.unittesting.MutabilityAssert.assertImmutable;

public class TypeCutTest {

    @Test
    public void IsImmutable() {
        assertImmutable(TypeCut.class);
    }

    @Test
    public void equalsContract() {
        EqualsVerifier.forClass(TypeCut.class).verify();
    }

}
