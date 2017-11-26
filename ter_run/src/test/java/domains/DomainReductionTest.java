package domains;

import com.google.common.collect.ImmutableList;
import jobs.Jobs;
import jobs.JobsAtomicExemple;
import jobs.JobsVilimExemple;
import models.*;
import models.cut.Cut;
import models.tmax.TMax;
import models.tmax.TypeTMax;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.LinkedList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.mutabilitydetector.unittesting.MutabilityAssert.assertImmutable;


@Ignore
public final class DomainReductionTest {

    private ImmutableList<Jobs> jobsVilimList;
    private ImmutableList<Jobs> jobsAtomicExemple;


    @Test
    public void IsImmutable() {
        assertImmutable(DomainReduction.class);
    }

    @Test
    public void equalsContract() {
        EqualsVerifier.forClass(DomainReduction.class).verify();
    }

    @Before
    public void init(){
        jobsVilimList = JobsVilimExemple.jobsList();
        jobsAtomicExemple = JobsAtomicExemple.jobsList();
    }

    @Test
    public void domainReductionMipsAtomicExemple1() throws Exception {
        Jobs jobs = jobsAtomicExemple.get(0);

        DomainSize mpDomain = ModelFactory.build(jobs, TypeModel.MIP, new TMax(jobs, TypeTMax.LCT), Cut.AB()).dSize();


        LinkedList<DomainSlot> expectedIDomainList = new LinkedList<>();
        for (int i = 0; i < jobs.size(); i++) {
            for(int t : jobs.getValidStart(i)) {
                   expectedIDomainList.add(new DomainSlot(i, t));
            }
        }

        assertEquals(expectedIDomainList.size(), mpDomain.getInitial());
        assertEquals(0, mpDomain.getReduced());

    }

    @Test
    public void domainReductionMipsCpCorrelationAtomicExemple1() throws Exception {
        Jobs jobs = jobsAtomicExemple.get(0);

        TMax tMax = new TMax(jobs, TypeTMax.LCT);
        final DomainSize dMips = ModelFactory.build(jobs, TypeModel.MIP, tMax, Cut.AB()).dSize();
        final DomainSize dCp = ModelFactory.build(jobs, TypeModel.CP, tMax, Cut.NONE()).dSize();

        assertEquals(dCp.getInitial(), dMips.getInitial());
        assertEquals(dCp.getReduced(), dMips.getReduced());

    }

    @Test
    public void domainReductionMipsVilimPrecedence() throws Exception {
        Jobs jobs = jobsVilimList.get(1);

        TMax tmax = new TMax(jobs, TypeTMax.LCT);
        DomainSize dMips = ModelFactory.build(jobs, TypeModel.MIP, tmax, Cut.AB()).dSize();

        LinkedList<DomainSlot> expectedIDomainList = new LinkedList<>();
        for (int i = 0; i < jobs.size(); i++) {
            for(int t : jobs.getValidStart(i)) {
                expectedIDomainList.add(new DomainSlot(i, t));
            }
        }
        assertEquals(expectedIDomainList.size(), dMips.getInitial());


        final DomainSize dCp = ModelFactory.build(jobs, TypeModel.CP, tmax, Cut.NONE()).dSize();

        assertNotEquals(dCp.getInitial(), dCp.getReduced());
        assertNotEquals(dMips.getInitial(), dMips.getReduced());
        assertEquals(dCp.getInitial(), dMips.getInitial());

    }

    @Test
    public void mipsReductionVilimValue() throws Exception {
        Jobs jobs = jobsAtomicExemple.get(0);

        final DomainSize dMips = ModelFactory.build(jobs, TypeModel.MIP, new TMax(jobs, TypeTMax.LCT), Cut.AB()).dSize();
        assertNotEquals(dMips.getInitial(), dMips.getReduced());

    }


}
