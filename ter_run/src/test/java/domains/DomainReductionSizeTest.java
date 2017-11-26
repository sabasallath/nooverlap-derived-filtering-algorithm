package domains;


import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Test;
import static org.mutabilitydetector.unittesting.MutabilityAssert.assertImmutable;

public class DomainReductionSizeTest {

    @Test
    public void IsImmutable() {
        assertImmutable(DomainSlot.class);
    }

    @Test
    public void equalsContract() {
        EqualsVerifier.forClass(DomainSlot.class).verify();
    }

    @Test
    public void DomainReductionSizeEquals() throws Exception {

    }
}
