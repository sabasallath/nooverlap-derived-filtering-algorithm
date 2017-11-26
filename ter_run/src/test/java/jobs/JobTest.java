package jobs;
import com.google.common.collect.ImmutableList;
import jobs.jobsException.JobEndbeforeStartException;
import jobs.jobsException.JobInvalidDurationException;
import jobs.jobsException.JobInvalidRankException;
import jobs.jobsException.JobNegativeException;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Test;

import java.util.OptionalInt;

import static org.junit.Assert.*;
import static org.mutabilitydetector.unittesting.MutabilityAssert.assertImmutable;


public final class JobTest {

    @Test public void IsImmutable() {
        assertImmutable(Job.class);
    }

    @Test
    public void equalsContract() {
        EqualsVerifier.forClass(Job.class).verify();
    }

    @Test
    public void jobEquals(){
        try {
            Job j1 = new Job(0, 2, 1, 0);
            Job j2 = new Job(0, 2, 1, 0);
            Job j3a = new Job(1, 2, 1, 0);
            Job j3b = new Job(0, 1, 1, 0);
            Job j3c = new Job(0, 2, 2, 0);
            Job j3d = new Job(0, 2, 1, 1);

            assertEquals(j1, j2);
            assertNotEquals(j1, j3a);
            assertNotEquals(j1, j3b);
            assertNotEquals(j1, j3c);
            assertNotEquals(j1, j3d);
        } catch (Exception e) {
            assertTrue(false);
        }
    }

    @Test
    public void jobException() {

        boolean invalidRank = false;
        try {
            new Job(0, 1, 1, -1);
        } catch (JobInvalidRankException e) {
            invalidRank = true;
        } catch (Exception e) {
            assertTrue(false);
        }
        assertTrue(invalidRank);

        boolean nonNegativeEst = false;
        try {
            new Job(-1, 1, 1, 0);
        } catch (JobNegativeException e) {
            nonNegativeEst = true;
        } catch (Exception e) {
            assertTrue(false);
        }
        assertTrue(nonNegativeEst);

        boolean nonNegativeLct = false;
        try {
            new Job(0, -1, 1, 0);
        } catch (JobNegativeException e) {
            nonNegativeLct = true;
        } catch (Exception e) {
            assertTrue(false);
        }
        assertTrue(nonNegativeLct);

        boolean invalidDurationException = false;
        try {
            new Job(0, 1, 3, 0);
        } catch (JobInvalidDurationException e) {
            invalidDurationException = true;
        } catch (Exception e) {
            assertTrue(false);
        }
        assertTrue(invalidDurationException);

        boolean invalidDurationExceptionNegativOrZero = false;
        try {
            new Job(0, 1, -1, 0);
        } catch (JobInvalidDurationException e) {
            invalidDurationExceptionNegativOrZero = true;
        } catch (Exception e) {
            assertTrue(false);
        }
        assertTrue(invalidDurationExceptionNegativOrZero);

        boolean endBeforeStartCatch = false;
        try {
            new Job(3, 0, 1, 0);
        } catch (JobEndbeforeStartException e) {
            endBeforeStartCatch = true;
        } catch (Exception e) {
            assertTrue(false);
        }
        assertTrue(endBeforeStartCatch);
    }

    @Test
    public void jobGetter() {
        try {

            Job j0 = new Job(0, 2, 1, 0);
            Job j1 = new Job(0, 2, 2, 0);
            Job j2 = new Job(0, 2, 3, 0);

            assertEquals(0, j0.getI());
            assertEquals(0, j0.getEst());
            assertEquals(2, j0.getLct());
            assertEquals(0, j0.getEst());
            assertEquals(2, j0.getLst());
            assertEquals(1, j0.getP());
            final ImmutableList<Object> j0ExpectedValidStart = ImmutableList.builder()
                    .add(0)
                    .add(1)
                    .add(2)
                    .build();
            assertEquals(j0ExpectedValidStart, j0.getValidStart());

            assertEquals(0, j1.getI());
            assertEquals(0, j1.getEst());
            assertEquals(2, j1.getLct());
            assertEquals(0, j1.getEst());
            assertEquals(1, j1.getLst());
            assertEquals(2, j1.getP());
            final ImmutableList<Object> j1ExpectedValidStart = ImmutableList.builder()
                    .add(0)
                    .add(1)
                    .build();
            assertEquals(j1ExpectedValidStart, j1.getValidStart());

            assertEquals(0, j2.getI());
            assertEquals(0, j2.getEst());
            assertEquals(2, j2.getLct());
            assertEquals(0, j2.getEst());
            assertEquals(0, j2.getLst());
            assertEquals(3, j2.getP());
            final ImmutableList<Object> j2ExpectedValidStart = ImmutableList.builder()
                    .add(0)
                    .build();
            assertEquals(j2ExpectedValidStart, j2.getValidStart());


        } catch (Exception e) {
            e.printStackTrace();
            assertTrue(false);
        }
    }

    @Test
    public void jobOTest() {
        try {
            Job j0 = new Job(0, 2, 2, 0);
            assertEquals(OptionalInt.of(1), j0.getOStart());
            assertEquals(OptionalInt.of(1), j0.getOEnd());
            assertEquals(OptionalInt.of(1), j0.getOP());

            Job j1 = new Job(0, 4, 4, 0);
            assertEquals(OptionalInt.of(1), j1.getOStart());
            assertEquals(OptionalInt.of(3), j1.getOEnd());
            assertEquals(OptionalInt.of(3), j1.getOP());

            Job j3 = new Job(0, 4, 5, 0);
            assertEquals(OptionalInt.of(0), j3.getOStart());
            assertEquals(OptionalInt.of(4), j3.getOEnd());
            assertEquals(OptionalInt.of(5), j3.getOP());

            // start and end has no meaning
            Job j2 = new Job(0, 2, 1, 0);
            assertEquals(OptionalInt.empty(), j2.getOStart());
            assertEquals(OptionalInt.empty(), j2.getOEnd());
            assertEquals(OptionalInt.empty(), j2.getOP());

            // start and end has no meaning
            Job j4 = new Job(0, 5, 3, 0);
            assertEquals(OptionalInt.empty(), j4.getOStart());
            assertEquals(OptionalInt.empty(), j4.getOEnd());
            assertEquals(OptionalInt.empty(), j4.getOP());

            // start and end has no meaning
            Job j5 = new Job(0, 6, 3, 0);
            assertEquals(OptionalInt.empty(), j5.getOStart());
            assertEquals(OptionalInt.empty(), j5.getOEnd());
            assertEquals(OptionalInt.empty(), j5.getOP());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}