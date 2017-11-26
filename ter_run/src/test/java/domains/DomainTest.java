package domains;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import jobs.Jobs;
import jobs.JobsAtomicExemple;
import jobs.JobsVilimExemple;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Before;
import org.junit.Test;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.mutabilitydetector.unittesting.MutabilityAssert.assertImmutable;

public class DomainTest {

    private ImmutableList<Jobs> jobsVilimList;
    private ImmutableList<Jobs> jobsAtomicExemple;

    @Test public void IsImmutable() {
        assertImmutable(Domain.class);
    }

    @Test
    public void equalsContract() {
        EqualsVerifier.forClass(Domain.class).verify();
    }

    @Before
    public void init(){
        jobsVilimList = JobsVilimExemple.jobsList();
        jobsAtomicExemple = JobsAtomicExemple.jobsList();
    }

    @Test
    public void domainEquals() throws Exception {
        Domain dA_J0 = new Domain(jobsVilimList.get(0));
        Domain dB_J0 = new Domain(jobsVilimList.get(0));
        Domain dC_J1 = new Domain(jobsVilimList.get(1));

        assertEquals(dA_J0, dB_J0);
        assertNotEquals(dA_J0, dC_J1);
    }

    @Test
    public void domainReduceAtomicExemple1() throws Exception {
        Jobs jobs = jobsAtomicExemple.get(0);
        Domain initial = new Domain(jobs);
        Domain initialCpy = new Domain(jobs);
        assertEquals(initial, initialCpy);

        List<DomainSlot> validStarSlots = new LinkedList<>();
        for (int i = 0; i < jobs.size(); i++) {
            for (Integer v : jobs.getValidStart(i)) {
                validStarSlots.add(new DomainSlot(i, v));
            }
        }
        assertEquals(validStarSlots.size(), 6);

        ImmutableSet<DomainSlot> actualSet = initial.getDomains();
        HashSet<DomainSlot> expectedSet = new HashSet<>();
        expectedSet.addAll(validStarSlots);

        assertEquals(ImmutableSet.copyOf(expectedSet), actualSet);

        // All invalid Start
        List<DomainSlot> invalidStartA = new LinkedList<>();
        invalidStartA.add(new DomainSlot(0, 0));
        invalidStartA.add(new DomainSlot(0, 1));
        invalidStartA.add(new DomainSlot(0, 2));
        invalidStartA.add(new DomainSlot(1, 0));
        invalidStartA.add(new DomainSlot(1, 1));
        invalidStartA.add(new DomainSlot(1, 2));

        assertEquals(initial, initialCpy);
        assertNotEquals(initial.reduce(invalidStartA), initial);
        assertEquals(initial.reduce(invalidStartA).getSize(), 0);

        // 4 invalid Start
        List<DomainSlot> invalidStartB = new LinkedList<>();
        invalidStartB.add(new DomainSlot(0, 0));
        invalidStartB.add(new DomainSlot(0, 1));
        invalidStartB.add(new DomainSlot(0, 2));
        invalidStartB.add(new DomainSlot(1, 0));

        assertEquals(initial, initialCpy);
        assertNotEquals(initial.reduce(invalidStartB), initial);
        assertEquals(initial.reduce(invalidStartB).getSize(), 2);

        // Double reduction
        assertEquals(initial.reduce(invalidStartB), initial.reduce(invalidStartB).reduce(invalidStartB));
    }
}
