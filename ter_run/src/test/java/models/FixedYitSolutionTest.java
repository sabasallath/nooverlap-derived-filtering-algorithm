package models;

import domains.DomainSlot;
import jobs.Jobs;
import jobs.JobsVilimExemple;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;


public class FixedYitSolutionTest {

    @Test
    public void solveMipsLpWithFixedYitTest() {

        Jobs jobs = JobsVilimExemple.jobsList().get(1);

        ModelCplexItf modelMips = ModelFactory.mip(jobs);
        Solution iMips = modelMips.getSolution();
        Solution rsMips = modelMips.resolveWithFixedStart(new DomainSlot(2, 18));

        ModelCplexItf modelLp = ModelFactory.lp(jobs);
        Solution iLp = modelLp.getSolution();
        Solution rsLp = modelLp.resolveWithFixedStart(new DomainSlot(2, 18));

        final double delta = 0.0000001;
        assertEquals(rsMips.getObjValue(), rsLp.getObjValue(), delta);
        assertEquals(rsMips.getObjValue(), 2.0, delta);

        assertNotEquals(iMips, rsMips);
        assertNotEquals(iLp, rsLp);
        assertEquals(iMips.getObjValue(), iLp.getObjValue(), delta);
        assertEquals(iMips.getObjValue(), 0.0, delta);

    }
}
