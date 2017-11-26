package jobs;

import com.google.common.collect.ImmutableList;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Test;

import java.util.LinkedList;

import static org.junit.Assert.*;
import static org.mutabilitydetector.unittesting.MutabilityAssert.assertImmutable;

public final class JobsTest {

    @Test public void IsImmutable() {
        assertImmutable(Jobs.class);
    }

    @Test
    public void equalsContract() {
        EqualsVerifier.forClass(Jobs.class).verify();
    }

    @Test
    public void jobsEquals(){
        LinkedList<Job> jobs_1 = new LinkedList<>();
        LinkedList<Job> jobs_2 = new LinkedList<>();

        assertEquals(new Jobs(jobs_1), new Jobs(jobs_2));

        try {
            jobs_2.add(new Job(4, 30, 4, 0));
            jobs_2.add(new Job(5, 13, 3, 1));
            jobs_2.add(new Job(5, 13, 3, 2));
            jobs_2.add(new Job(14, 18, 5, 3));
            assertNotEquals(new Jobs(jobs_1), new Jobs(jobs_2));

            jobs_1.add(new Job(4, 30, 4, 0));
            assertNotEquals(new Jobs(jobs_1), new Jobs(jobs_2));

            jobs_1.add(new Job(5, 13, 3, 1));
            assertNotEquals(new Jobs(jobs_1), new Jobs(jobs_2));

            jobs_1.add(new Job(5, 13, 3, 2));
            assertNotEquals(new Jobs(jobs_1), new Jobs(jobs_2));

            jobs_1.add(new Job(14, 18, 5, 3));
            assertEquals(new Jobs(jobs_1), new Jobs(jobs_2));

        } catch (Exception e) {
            assertTrue(false);
        }
    }

    @Test
    public void jobs_equality_reverse_order() {

        LinkedList<Job> jobs_1 = new LinkedList<>();
        LinkedList<Job> jobs_2 = new LinkedList<>();

        assertEquals(jobs_1, jobs_2);

        try {
            jobs_2.add(new Job(4, 30, 4, 0));
            jobs_2.add(new Job(5, 13, 3, 1));
            jobs_2.add(new Job(5, 13, 3, 2));
            jobs_2.add(new Job(14, 18, 5, 3));

            // Test equality when jobs is added in reverse order
            jobs_1.add(new Job(14, 18, 5, 0));
            jobs_1.add(new Job(5, 13, 3, 1));
            jobs_1.add(new Job(5, 13, 3, 2));
            jobs_1.add(new Job(4, 30, 4, 3));
        } catch (Exception e) {
            assertTrue(false);
        }

        assertNotEquals(new Jobs(jobs_2), new Jobs(jobs_1));
    }

    @Test
    public void jobsGetterVilim() {
        final ImmutableList<Jobs> jobsVilim = JobsVilimExemple.jobsList();

        final Jobs jobs0 = jobsVilim.get(0);
        assertEquals(4, jobs0.size());
        assertEquals(4, jobs0.getEst());
        assertEquals(30, jobs0.getLct());
        assertEquals(15, jobs0.getP());
        assertEquals(4 + 15, jobs0.getEct());
        assertEquals(15, jobs0.getLst());
        assertEquals(31, jobs0.getH());

        int validStartSum = 0;
        for (int i = 0; i < jobs0.size(); i++) {
            validStartSum += jobs0.getValidStart(i).size();
        }

        // For each job Sum
        // lct - d + 1 - est + 1
        // == lst - est + 1
        // == lct - d - est + 2
        assertEquals(30 - 4 - 4 + 2
                        + 13 - 5 - 3 + 2
                        + 13 - 5 - 3 + 2
                        + 18 - 14 - 5 + 2, validStartSum);

        //todo others vilim exemple
    }

    @Test
    public void jobsGetterAtomic() throws Exception {
        final ImmutableList<Jobs> jobsAtom = JobsAtomicExemple.jobsList();

        final Jobs jobs0 = jobsAtom.get(0);
        assertEquals(2, jobs0.size());
        assertEquals(0, jobs0.getEst());
        assertEquals(4, jobs0.getLct());
        assertEquals(6, jobs0.getP());
        assertEquals(6, jobs0.getEct());
        // Notice lst could not be < est
        // so lst == est instead of -2
        assertEquals(0, jobs0.getLst());
        assertEquals(5, jobs0.getH());

        int validStartSum = 0;
        for (int i = 0; i < jobs0.size(); i++) {
            validStartSum += jobs0.getValidStart(i).size();
        }

        assertEquals((4 - 3 + 2) * 2, validStartSum);
    }
}
