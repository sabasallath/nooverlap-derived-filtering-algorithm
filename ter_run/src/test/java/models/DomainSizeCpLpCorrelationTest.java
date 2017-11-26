package models;

import jobs.Jobs;

import jobs.JobsAtomicExemple;
import jobs.JobsVilimExemple;
import models.cut.Cut;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class DomainSizeCpLpCorrelationTest {

    @Test
    public void sizeCorrelationVilimTest() {
        JobsVilimExemple.jobsList().forEach(this::test);
    }

    @Test
    public void sizeCorrelationAtomicTest() {
        JobsAtomicExemple.jobsList().forEach(this::test);
    }

    public void test(Jobs jobs) {
        int domainInitialSizeCP = ModelFactory.cp(jobs).dSize().getInitial();
        int domainInitialSizeLP = ModelFactory.lp(jobs, Cut.NONE()).dSize().getInitial();
        int domainInitialSizeMips = ModelFactory.mip(jobs, Cut.NONE()).dSize().getInitial();

        int domainReducedSizeCP = ModelFactory.cp(jobs).dSize().getReducedBounded();
        int domainReducedSizeMips = ModelFactory.mip(jobs, Cut.NONE()).dSize().getReducedBounded();

        assertEquals(domainInitialSizeCP, domainInitialSizeLP);
        assertEquals(domainInitialSizeCP, domainInitialSizeMips);
        assertEquals(domainReducedSizeCP, domainReducedSizeMips);
    }
}