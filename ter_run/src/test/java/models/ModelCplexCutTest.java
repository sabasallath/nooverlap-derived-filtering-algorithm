package models;

import jobs.Jobs;
import jobs.JobsAtomicExemple;
import models.cut.Cut;
import org.junit.Ignore;
import org.junit.Test;


public class ModelCplexCutTest {

    @Ignore
    @Test
    public void precedenceTest() throws Exception {
        final Jobs jobs = JobsAtomicExemple.jobsList().get(0);
        Model modelLp = ModelFactory.lp(jobs, Cut.C());
        Solution sLp = modelLp.getSolution();

        //Model modelLpForced = ModelFactory.build(jobs, TypeModel.LP, TypeTMax.OPTIMAL, Cut.C(), new DomainSlot(0, 0));
        //DomainSize drLpForcedSize = new DomainReduction(jobs, TypeModel.LP, new TMax(jobs, TypeTMax.OPTIMAL), Cut.C()).domainSize();

        //System.out.println(drLpForcedSize);
    }
}
