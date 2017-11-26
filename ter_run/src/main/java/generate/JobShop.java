package generate;

import customNoOverlap.CustomNoOverlapConstraint;
import ilog.concert.*;
import ilog.cp.IloCP;
import models.ModelCp;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Jobshop class using the custom noOverlap constraint to solve the jopshop instance.
 */
public class JobShop {

    private final String filename;
    private double objValue;
    private String domainBeforePropagation;
    private String domainAfterPropagation;
    private int domainSizeBeforePropagation;
    private int domainSizeAfterPropagation;

    static class JobShopDataReader {

        private StreamTokenizer st;

        public JobShopDataReader(String filename) throws IOException {
            FileInputStream stream = new FileInputStream(filename);
            Reader r = new BufferedReader(new InputStreamReader(stream, "UTF8"));
            st = new StreamTokenizer(r);
        }

        public int next() throws IOException {
            st.nextToken();
            return (int) st.nval;
        }
    }

    static class IntervalVarList extends ArrayList<IloIntervalVar> {
        public IloIntervalVar[] toArray() {
            return this.toArray(new IloIntervalVar[this.size()]);
        }
    }

    public JobShop(String filename) {
        this(filename, false, IloCP.IntMax);
    }

    public JobShop(String filename, Boolean use_custom_constraint, int tMaxValue) {
        this.filename = filename;
        this.domainBeforePropagation = "";
        this.domainAfterPropagation = "";
        this.domainSizeBeforePropagation = -1;
        this.domainSizeAfterPropagation = -1;

        int nbJobs, nbMachines;

        IloCP cp = new IloCP();
        try {
            JobShopDataReader data = new JobShopDataReader(filename);
            nbJobs = data.next();
            nbMachines = data.next();

            List<IloIntExpr> ends = new ArrayList<>();
            IntervalVarList[] machines = new IntervalVarList[nbMachines];
            for (int j = 0; j < nbMachines; j++)
                machines[j] = new IntervalVarList();

            for (int i = 0; i < nbJobs; i++) {
                IloIntervalVar prec = cp.intervalVar();
                for (int j = 0; j < nbMachines; j++) {
                    int m, d;
                    m = data.next();
                    d = data.next();
                    IloIntervalVar ti = cp.intervalVar(d, "J"+i+"_T"+j+"("+m+"_"+d+")");
                    machines[m].add(ti);
                    if (j > 0) {
                        cp.add(cp.endBeforeStart(prec, ti));
                    }
                    prec = ti;
                }
                ends.add(cp.endOf(prec));
            }

            for (int j = 0; j < nbMachines; j++) {
                if (use_custom_constraint) {
                    cp.add(new CustomNoOverlapConstraint(cp, machines[j].toArray(), tMaxValue));
                } else {
                    cp.add(cp.noOverlap(machines[j].toArray()));
                }
            }

            cp.setParameter(IloCP.IntParam.NoOverlapInferenceLevel, IloCP.ParameterValues.Extended);

            IloIntVar makespan = cp.intVar(0, IloCP.IntMax);
            cp.add(cp.eq(makespan, cp.max(ModelCp.arrayFromList(ends))));
            cp.add(cp.le(makespan, tMaxValue));
            IloObjective objective = cp.minimize(makespan);
            cp.add(objective);

//            domainBeforePropagation = domain(cp, machines);
//            domainSizeBeforePropagation = totalDomainSize(machines);

            cp.propagate();

            domainSizeAfterPropagation = totalDomainSize(machines, cp);
            domainAfterPropagation = domain(cp, machines);

            if (! use_custom_constraint) {
                if (cp.solve()) {
                    objValue = cp.getObjValue();
                }
            }

        } catch (IloException | IOException e) {
            e.printStackTrace();
        }
    }

    private static String domain(IloCP cp, IntervalVarList[] machines) throws IloException {
        StringBuilder sb = new StringBuilder("Domain before propagation\n");
        for (IntervalVarList machine : machines) {
            for (IloIntervalVar iloIntervalVar : machine) {
               sb.append(cp.getDomain(iloIntervalVar)).append("\n");
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    private static int totalDomainSize(IntervalVarList[] machines, IloCP cp) {
        int taskSize = 0;
        for (IntervalVarList machine : machines) {
            taskSize += ModelCp.getDomainSize(machine.toArray(), cp);
        }
        return taskSize;
    }

    public double getObjValue() {
        return objValue;
    }

    public String getFilename() {
        return filename;
    }

    public String getDomainBeforePropagation() {
        return domainBeforePropagation;
    }

    public String getDomainAfterPropagation() {
        return domainAfterPropagation;
    }

    public int getDomainSizeBeforePropagation() {
        return domainSizeBeforePropagation;
    }

    public int getDomainSizeAfterPropagation() {
        return domainSizeAfterPropagation;
    }
}
