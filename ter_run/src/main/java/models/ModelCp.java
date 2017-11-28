package models;

import domains.DomainSize;
import ilog.concert.*;
import ilog.cp.IloCP;
import jobs.Jobs;
import models.cut.Cut;
import models.tmax.TMax;

import java.util.ArrayList;
import java.util.List;

/**
 * Constraint programming model to compare result with MIP and LP model
 * of the noOverlap global constraint.
 */
public class ModelCp implements Model {

    private final Jobs jobs;
    private final TypeModel type;
    private final TMax tMax;
    private final Boolean grid;
    private int initialDomainSize;
    private int reducedDomainSize;
    private String initialGrid;
    private final Solution solution;
    private long cpuTime;

    ModelCp(Jobs jobs, TMax tMax, Boolean grid) {
        this.grid = grid;
        this.jobs = jobs;
        this.type = TypeModel.CP;
        this.tMax = tMax;
        this.solution = solve();
    }

    /**
     * @return Solution of the model.
     */
    private Solution solve() {

        try {

        IloCP cp = new IloCP();
        cp.setOut(null);

        IloIntervalVar[] tasks = new IloIntervalVar[jobs.size()];
        List<IloIntExpr> ends = new ArrayList<>();
        initTasks(cp, tasks, ends, jobs);

            initialDomainSize = getDomainSize(tasks);
            initialGrid = getJobsData(tasks);

            if(!tMax.isFeasible()) {
                reducedDomainSize = 0;

                return new Solution(
                        type,
                        jobs,
                        Cut.NONE(),
                        tMax.get(),
                        tMax.isFeasible(),
                        tMax.isFeasible(),
                        tMax.getStatus(),
                        tMax.get(),
                        initialGrid,
                        "",
                        cpuTime);
            }

            IloNoOverlap disjunctive = cp.noOverlap(tasks);
            cp.add(disjunctive);

            IloIntVar makespan = cp.intVar(0, IloCP.IntMax);
            cp.add(cp.ge(makespan, cp.max(arrayFromList(ends))));
            cp.add(cp.le(makespan, tMax.get()));

            IloObjective objective = cp.minimize(makespan);
            cp.add(objective);

            /*
            From cplex documentation the inference has four level :
            -------------------------------------------------------------
            In the C++ API, the possible values of these parameters are :
            IloCP::Default,
            IloCP::Low,
            IloCP::Basic,
            IloCP::Medium and
            IloCP::Extended.
            -------------------------------------------------------------
             */
            cp.setParameter(IloCP.IntParam.NoOverlapInferenceLevel, IloCP.ParameterValues.Extended);

            long cpuTimeStart = System.currentTimeMillis();
            cp.propagate();
            this.cpuTime = System.currentTimeMillis() - cpuTimeStart;

            this.reducedDomainSize = getDomainSize(tasks, cp);

            StringBuilder sb = new StringBuilder("Solution:\n");
            if (grid) {
                for (IloIntervalVar task : tasks) {
                    sb.append(cp.getDomain(task)).append("\n");
                }
            } else {
                sb.append("option not enable");
            }
            String solutionGrid = sb.toString();

            return new Solution(
                    type,
                    jobs,
                    Cut.NONE(),
                    tMax.get(),
                    tMax.isFeasible(),
                    tMax.isFeasible(),
                    tMax.getStatus(),
                    tMax.get(),
                    initialGrid,
                    solutionGrid,
                    cpuTime);

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

    }

    public static void initTasks(IloCP cp, IloIntervalVar[] tasks, List<IloIntExpr> ends, Jobs jobs) throws IloException {
        for (int i = 0; i < jobs.size(); i++) {
            tasks[i] = cp.intervalVar(jobs.getP(i),"T"+i);
            tasks[i].setStartMin(jobs.getEst(i));
            // setStartMax is exclusive, must add 1.
            tasks[i].setStartMax(jobs.getLst(i) + 1);
            // setEndMax is exclusive, must add 1.
            tasks[i].setEndMax(jobs.getLct(i) + 1);
            ends.add(cp.endOf(tasks[i]));
        }
    }

    public static IloIntExpr[] arrayFromList(List<IloIntExpr> list) {
        return list.toArray(new IloIntExpr[list.size()]);
    }

    private String getJobsData(IloIntervalVar[] tasks) {
        StringBuilder initialTaskData = new StringBuilder("data\n");
        for (int i = 0; i < tasks.length; i++) {
            initialTaskData
                    .append(i)
                    .append(": [").append(jobs.getEst(i)).append(";").append(jobs.getLct(i)).append("]")
                    .append("(").append(jobs.getP(i)).append(")\n");
        }
        return initialTaskData.toString();
    }

    /**
     * Calculate the starting time domain size before solving it.
     * /!\ Domain calculation before solving the model != after.
     * @param tasks Interval variables of each task.
     * @return The domain size before solving the instance.
     */
    public static int getDomainSize(IloIntervalVar[] tasks) {
        int domainSize = 0;

        for (IloIntervalVar task : tasks) {
            // Domain calculation before solving the model != after
            domainSize += task.getStartMax() - task.getStartMin();
        }

        return domainSize;
    }


    /**
     * Calculate the starting time domain size after solving it.
     * /!\ Domain calculation before solving the model != after.
     * @param tasks Interval variables of each task.
     * @param cp the cp.ILOCP constraint solver.
     * @return The domain size after solving the instance.
     */
    public static int getDomainSize(IloIntervalVar[] tasks, IloCP cp) {
        int domainSize = 0;

        for (IloIntervalVar task : tasks) {
            // Domain calculation after solving the model != before, must add 1.
            domainSize += cp.getStartMax(task) - cp.getStartMin(task) + 1;
        }

        return domainSize;
    }

    @Override
    public DomainSize dSize() {
        return new DomainSize(initialDomainSize, reducedDomainSize, reducedDomainSize);
    }

    @Override
    public Jobs getJobs() {
        return jobs;
    }

    @Override
    public TypeModel getType() {
        return TypeModel.CP;
    }

    @Override
    public Solution getSolution() {
        return solution;
    }

}
