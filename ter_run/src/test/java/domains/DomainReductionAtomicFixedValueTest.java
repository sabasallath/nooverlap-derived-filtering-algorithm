package domains;

import com.google.common.collect.ImmutableList;
import jobs.Jobs;
import jobs.JobsAtomicExemple;
import models.*;
import models.cut.Cut;
import models.tmax.TMax;
import models.tmax.TypeTMax;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;


public class DomainReductionAtomicFixedValueTest {

    private ImmutableList<Jobs> jobsAtomicExemple;

    @Before
    public void init(){
        jobsAtomicExemple = JobsAtomicExemple.jobsList();
    }


    @Test
    public void cut_1ValueTest() throws Exception {
        // cut_1.txt
        // 0 4 3
        // 0 4 3
        Jobs jobs = jobsAtomicExemple.get(0);
        final DomainSize dMips = ModelFactory.build(jobs, TypeModel.MIP, new TMax(jobs, TypeTMax.OPTIMAL), Cut.NONE()).dSize();
        final DomainSize dLp = ModelFactory.build(jobs, TypeModel.LP, new TMax(jobs, TypeTMax.OPTIMAL), Cut.NONE()).dSize();
        final DomainSize dCp = ModelFactory.build(jobs, TypeModel.CP, new TMax(jobs, TypeTMax.OPTIMAL), Cut.NONE()).dSize();

        // Initial Size
        assertEquals(6, dMips.getInitial());
        assertEquals(6, dLp.getInitial());
        assertEquals(6, dCp.getInitial());

        // Reduced Size
        assertEquals(0, dMips.getReduced());
        assertEquals(0, dLp.getReduced());
        assertEquals(0, dCp.getReduced());
    }

    @Test
    public void cut_7ValueTest() throws Exception {
        // todo reprendre cut 2 avec 1 4 2
        // cut_7.txt
        // 0 2 2
        Jobs jobs = jobsAtomicExemple.get(6);
        final DomainSize dMips = ModelFactory.build(jobs, TypeModel.MIP, new TMax(jobs, TypeTMax.OPTIMAL), Cut.NONE()).dSize();
        final DomainSize dLp = ModelFactory.build(jobs, TypeModel.LP, new TMax(jobs, TypeTMax.OPTIMAL), Cut.NONE()).dSize();
        final DomainSize dCp = ModelFactory.build(jobs, TypeModel.CP, new TMax(jobs, TypeTMax.OPTIMAL), Cut.NONE()).dSize();

        // Initial Size
        assertEquals(2, dMips.getInitial());
        assertEquals(2, dLp.getInitial());
        assertEquals(2, dCp.getInitial());

        // Reduced Size
        assertEquals(1, dMips.getReduced());
        assertEquals(1, dLp.getReduced());
        assertEquals(1, dCp.getReduced());

        assertEquals(1, dMips.getReducedBounded());
        assertEquals(1, dLp.getReducedBounded());
        assertEquals(1, dCp.getReducedBounded());
    }

    @Test
    public void cut_2ValueTest() throws Exception {
        // todo reprendre cut 2 avec 1 4 2
        // cut_2.txt
        // 0 4 2
        // 0 4 2
        Jobs jobs = jobsAtomicExemple.get(1);
        final DomainSize dMips = ModelFactory.build(jobs, TypeModel.MIP, new TMax(jobs, TypeTMax.OPTIMAL), Cut.NONE()).dSize();
        final DomainSize dLp = ModelFactory.build(jobs, TypeModel.LP, new TMax(jobs, TypeTMax.OPTIMAL), Cut.NONE()).dSize();
        final DomainSize dCp = ModelFactory.build(jobs, TypeModel.CP, new TMax(jobs, TypeTMax.OPTIMAL), Cut.NONE()).dSize();

        // Initial Size
        assertEquals(8, dMips.getInitial());
        assertEquals(8, dLp.getInitial());
        assertEquals(8, dCp.getInitial());

        // Reduced Size
        assertEquals(6, dMips.getReducedBounded());
        assertEquals(6, dLp.getReducedBounded());
        assertEquals(6, dCp.getReducedBounded());

        // Reduced Size
        assertEquals(4, dMips.getReduced());
        assertEquals(4, dLp.getReduced());
        assertEquals(6, dCp.getReduced());
    }

    @Test
    public void cut_3ValueTest() throws Exception {
        // cut_3.txt
        // 1 4 2
        // 1 4 2
        // 0 5 1
        Jobs jobs = jobsAtomicExemple.get(2);
        final DomainSize dMips = ModelFactory.build(jobs, TypeModel.MIP, new TMax(jobs, TypeTMax.OPTIMAL), Cut.NONE()).dSize();
        final DomainSize dLp = ModelFactory.build(jobs, TypeModel.LP, new TMax(jobs, TypeTMax.OPTIMAL), Cut.NONE()).dSize();
        final DomainSize dCp = ModelFactory.build(jobs, TypeModel.CP, new TMax(jobs, TypeTMax.OPTIMAL), Cut.NONE()).dSize();

        // Initial Size
        assertEquals(12, dMips.getInitial());
        assertEquals(12, dLp.getInitial());
        assertEquals(12, dCp.getInitial());


        // TODO CHECK THIS
        // Reduced Size
        assertEquals(7, dMips.getReducedBounded());
        assertEquals(11, dLp.getReducedBounded());
        assertEquals(7, dCp.getReducedBounded());

        // Reduced Size
        assertEquals(5, dMips.getReduced());
        assertEquals(7, dLp.getReduced());
        assertEquals(7, dCp.getReduced());
        // TODO END
    }

    //todo warning TypeCut.Ab for lp
    @Test
    public void cut_6ValueTest() throws Exception {
        // cut_6.txt
        // 1 4 2
        // 1 4 2
        // 0 4 1
        Jobs jobs = jobsAtomicExemple.get(5);
        final DomainSize dMips = ModelFactory.build(jobs, TypeModel.MIP, new TMax(jobs, TypeTMax.OPTIMAL), Cut.NONE()).dSize();
        final DomainSize dLp = ModelFactory.build(
                jobs,
                TypeModel.LP,
                new TMax(
                        jobs,
                        TypeTMax.OPTIMAL),
                Cut.AB()).dSize();
        final DomainSize dCp = ModelFactory.build(jobs, TypeModel.CP, new TMax(jobs, TypeTMax.OPTIMAL), Cut.NONE()).dSize();

        // Initial Size
        assertEquals(11, dMips.getInitial());
        assertEquals(11, dLp.getInitial());
        assertEquals(11, dCp.getInitial());

        // Reduced Size
        assertEquals(7, dMips.getReducedBounded());
        assertEquals(7, dLp.getReducedBounded());
        assertEquals(7, dCp.getReducedBounded());

        // Reduced Size
        assertEquals(5, dMips.getReduced());
        assertEquals(5, dLp.getReduced());
        assertEquals(7, dCp.getReduced());
    }


    @Test
    public void cut_4ValueTest() throws Exception {
        // cut_4.txt
        // 0 1 2
        // 0 1 1
        Jobs jobs = jobsAtomicExemple.get(3);
        final DomainSize dMips = ModelFactory.build(jobs, TypeModel.MIP, new TMax(jobs, TypeTMax.OPTIMAL), Cut.NONE()).dSize();
        final DomainSize dLp = ModelFactory.build(jobs, TypeModel.LP, new TMax(jobs, TypeTMax.OPTIMAL), Cut.NONE()).dSize();
        final DomainSize dCp = ModelFactory.build(jobs, TypeModel.CP, new TMax(jobs, TypeTMax.OPTIMAL), Cut.NONE()).dSize();

        // Initial Size
        assertEquals(3, dMips.getInitial());
        assertEquals(3, dLp.getInitial());
        assertEquals(3, dCp.getInitial());

        // Reduced Size
        assertEquals(0, dMips.getReduced());
        assertEquals(0, dLp.getReduced());
        assertEquals(0, dCp.getReduced());
    }

    @Test
    public void cut_5ValueTest() throws Exception {
        // cut_5.txt
        // 0 1 1
        // 0 1 1
        Jobs jobs = jobsAtomicExemple.get(4);
        final DomainSize dMips = ModelFactory.build(jobs, TypeModel.MIP, new TMax(jobs, TypeTMax.OPTIMAL), Cut.NONE()).dSize();
        final DomainSize dLp = ModelFactory.build(jobs, TypeModel.LP, new TMax(jobs, TypeTMax.OPTIMAL), Cut.NONE()).dSize();
        final DomainSize dCp = ModelFactory.build(jobs, TypeModel.CP, new TMax(jobs, TypeTMax.OPTIMAL), Cut.NONE()).dSize();

        // Initial Size
        assertEquals(4, dMips.getInitial());
        assertEquals(4, dLp.getInitial());
        assertEquals(4, dCp.getInitial());

        // Reduced Size
        assertEquals(4, dMips.getReduced());
        assertEquals(4, dLp.getReduced());
        assertEquals(4, dCp.getReduced());
    }
}
