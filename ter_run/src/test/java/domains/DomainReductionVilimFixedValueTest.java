package domains;

import com.google.common.collect.ImmutableList;
import jobs.Jobs;
import jobs.JobsVilimExemple;
import models.*;
import models.cut.Cut;
import models.tmax.TMax;
import models.tmax.TypeTMax;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;


public class DomainReductionVilimFixedValueTest {

    private ImmutableList<Jobs> jobsVilimList;

    @Before
    public void init(){
        jobsVilimList = JobsVilimExemple.jobsList();
    }

    @Test
    public void edgefindingValueTest() throws Exception {
        // Edge finding
        // 4 30 4
        // 5 13 3
        // 5 13 3
        // 14 18 5
        Jobs jobs = jobsVilimList.get(0);
        final DomainSize dMips = ModelFactory.build(
                jobs,
                TypeModel.MIP,
                new TMax(jobs, TypeTMax.OPTIMAL),
                Cut.AB()).dSize();
        final DomainSize dLp = ModelFactory.build(
                jobs,
                TypeModel.LP,
                new TMax(jobs, TypeTMax.OPTIMAL),
                Cut.AB()).dSize();
        final DomainSize dCp = ModelFactory.cp(jobs).dSize();

        // Initial Size
        assertEquals(39, dMips.getInitial());
        assertEquals(39, dLp.getInitial());
        assertEquals(39, dCp.getInitial());

        // Reduced Size
        assertEquals(6, dMips.getReduced());
        assertEquals(6, dLp.getReduced());

        // Reduced Size
        assertEquals(10, dCp.getReduced());
        assertEquals(10, dMips.getReducedBounded());
        assertEquals(10, dLp.getReducedBounded());
    }

    //todo
    // Not First, Not Last
    // 0 25 11
    // 1 27 10
    // 4 20 2

    // Overload Checking
    // 0 13 5
    // 1 14 5
    // 2 12 6

    // Precedence
    // 0 13 5
    // 1 14 5
    // 7 17 2
}
