package domains;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.mutabilitydetector.unittesting.MutabilityAssert.assertImmutable;

public class DomainSlotTest {

    @Test public void IsImmutable() {
        assertImmutable(DomainSlot.class);
    }

    @Test
    public void equalsContract() {
        EqualsVerifier.forClass(DomainSlot.class).verify();
    }

    @Test
    public void domainSlotEquals() throws Exception {
        DomainSlot slotA_00 = new DomainSlot(0, 0);
        DomainSlot slotB_00 = new DomainSlot(0, 0);
        DomainSlot slotD_10 = new DomainSlot(1, 0);
        DomainSlot slotE_01 = new DomainSlot(0, 1);

        assertEquals(slotA_00, slotB_00);
        assertNotEquals(slotA_00, slotD_10);
        assertNotEquals(slotA_00, slotE_01);
        assertNotEquals(slotD_10, slotE_01);
    }
}
