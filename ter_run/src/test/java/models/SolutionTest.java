package models;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Test;

import static org.mutabilitydetector.unittesting.MutabilityAssert.assertImmutable;

public class SolutionTest {

    @Test
    public void IsImmutable() {
        assertImmutable(Solution.class);
    }

    @Test
    public void equalsContract() {
        EqualsVerifier.forClass(Solution.class).withIgnoredFields("cpuTime").verify();
    }

}
