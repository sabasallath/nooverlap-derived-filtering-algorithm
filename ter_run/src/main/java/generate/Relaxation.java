package generate;

import com.google.common.collect.ImmutableList;
import domains.DomainSize;
import io.*;
import io.solvingInstance.SolvingInstance;
import io.solvingInstance.SolvingInstanceMember;
import io.solvingInstance.SolvingInstances;
import io.solvingInstance.TypeSIM;
import jobs.Jobs;
import models.*;
import models.cut.Cut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.concurrent.Immutable;
import java.io.File;
import java.time.LocalDateTime;
import java.util.*;

@Immutable
public class Relaxation {

    private final static Logger logger = LoggerFactory.getLogger(SolutionGrids.class);
    private final List<File> files;
    private final ImmutableList<Cut> cuts;
    private final SolvingInstances res;

    public Relaxation(ImmutableList<Cut> cuts, List<File> files) {

        this.cuts = cuts;
        this.files = files;
        this.res = generate();
        this.res.display();
        write();
    }

    private SolvingInstances generate() {
        SolvingInstances si = new SolvingInstances();

        for (File file : files) {
            Jobs jobs = new DataReader(file).read();
            logger.info("Relaxation of file " + file.getName());

            for (Cut cut : cuts) {

                logger.info("Relaxation of file " + file.getName()
                        + ", for cut " + cut.toString());

                /* Solving the CP instance problem */
                Model modelCp = ModelFactory.cp(jobs);
                Solution solutionCp = modelCp.getSolution();
                final DomainSize domainSizeCp = modelCp.dSize();

                /* Solving the LP instance problem */
                Model modelLp = ModelFactory.lp(jobs);
                final Solution solutionLp = modelLp.getSolution();
                final DomainSize domainSizeLp = modelLp.dSize();

                /* Solving the MIPS instance problem */
                Model modelMips = ModelFactory.mip(jobs);
                final Solution solutionMips = modelMips.getSolution();
                final DomainSize domainSizeMips = modelMips.dSize();

                List<SolvingInstanceMember> members = new LinkedList<>();
                members.add(new SolvingInstanceMember(TypeSIM.CUT, cut));
                members.add(new SolvingInstanceMember(TypeSIM.NAME, file.getName()));
                members.add(new SolvingInstanceMember(TypeSIM.CP_MAKESPAN, solutionCp.getObjValue()));
                members.add(new SolvingInstanceMember(TypeSIM.CP_DOMAIN_INITIAL, domainSizeCp.getInitial()));

                // Uncomment to add lp and mip initial domain size (same as cp)
                //members.add(new SolvingInstanceMember(TypeSIM.LP_DOMAIN_INITIAL, domainSizeLp.getInitial()));
                //members.add(new SolvingInstanceMember(TypeSIM.MIPS_DOMAIN_INITIAL, domainSizeMips.getInitial()));

                members.add(new SolvingInstanceMember(TypeSIM.CP_DOMAIN_REDUCED, domainSizeCp.getReduced()));
                members.add(new SolvingInstanceMember(TypeSIM.LP_OBJECTIVE, solutionLp.getObjValue()));
                members.add(new SolvingInstanceMember(TypeSIM.MIPS_OBJECTIVE, solutionMips.getObjValue()));
                members.add(new SolvingInstanceMember(TypeSIM.LP_DOMAIN_REDUCED_BOUNDED, domainSizeLp.getReducedBounded()));
                members.add(new SolvingInstanceMember(TypeSIM.MIPS_DOMAIN_REDUCED_BOUNDED, domainSizeMips.getReducedBounded()));
                members.add(new SolvingInstanceMember(TypeSIM.LP_DOMAIN_REDUCED, domainSizeLp.getReduced()));
                members.add(new SolvingInstanceMember(TypeSIM.MIPS_DOMAIN_REDUCED, domainSizeMips.getReduced()));

                // Uncomment to add cpu time to the relaxation table
                // members.add(new SolvingInstanceMember(TypeSIM.CP_CPU, solutionCp.getCpuTime()));
                // members.add(new SolvingInstanceMember(TypeSIM.LP_CPU, solutionLp.getCpuTime()));
                // members.add(new SolvingInstanceMember(TypeSIM.MIPS_CPU, solutionMips.getCpuTime()));

                si.add(new SolvingInstance(members));
            }
        }

        return si;
    }

    private void write() {
        final String strFolder = "output/relaxation/";
        File folder = new File(strFolder);
        if(folder.exists()) {
            res.write(strFolder + "relaxation_" + LocalDateTime.now());
        } else if (folder.mkdir()) {
            res.write(strFolder + "relaxation_" + LocalDateTime.now());
        } else {
            throw new RuntimeException("Unable to write folder" + folder);
        }
    }

}
