package models.tmax;

import ilog.concert.*;
import ilog.cp.IloCP;
import jobs.Jobs;
import models.ModelCp;

import java.util.ArrayList;
import java.util.List;

/**
 * A Cp model use to determine the optimal value of a model
 * and to prefix other model with this value if necessary.
 */
public final class TMax {

    private final TypeTMax typeTMax;
    private boolean feasible;
    private double maxValue;
    private final Jobs jobs;
    private String status;

    /**
     * Tmax model that will compute the optimal value of the model.
     * @param jobs Jobs of the model.
     * @param typeTMax Type of the model.
     */
    public TMax(Jobs jobs, TypeTMax typeTMax) {
        this.jobs = jobs;
        this.typeTMax = typeTMax;
        this.feasible = true;
        solve();
    }

    /**
     * A Tmax model model that skip the solving step.
     * @param jobs Jobs of the model.
     * @param maxValue The precomputed optimal value of the model.
     */
    public TMax(Jobs jobs, int maxValue) {
        this.jobs = jobs;
        this.maxValue = maxValue;
        this.typeTMax = TypeTMax.OPTIMAL;
    }

    private void solve() {
        try {
            IloCP cp = new IloCP();
            cp.setOut(null);

            IloIntervalVar[] tasks = new IloIntervalVar[jobs.size()];
            List<IloIntExpr> ends = new ArrayList<>();
            ModelCp.initTasks(cp, tasks, ends, jobs);

            IloNoOverlap disjunctive = cp.noOverlap(tasks);
            cp.add(disjunctive);

            IloIntVar makespan = cp.intVar(0, IloCP.IntMax);

            cp.add(cp.eq(makespan, cp.max(ModelCp.arrayFromList(ends))));
            IloObjective objective = cp.minimize(makespan);
            cp.add(objective);

            cp.setParameter(IloCP.IntParam.NoOverlapInferenceLevel, IloCP.ParameterValues.Extended);

            feasible = cp.solve();
            this.status = cp.getStatus().toString();

            if (feasible) {
                this.maxValue = cp.getObjValue();
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public boolean isFeasible() {
        return feasible;
    }

    /**
     * @return An optimal value of the model according to the type of tMax.
     */
    public double get() {
        switch (typeTMax) {
            case OPTIMAL:
                return maxValue;
            case OP10:
                return maxValue + (maxValue * 10) / 100;
            case LCT:
                return jobs.getLct();
            default:
                throw new IllegalArgumentException();
        }
    }

    /**
     * @return Cpo status after solving.
     */
    public String getStatus() {
        return status;
    }


    /**
     * @return Type of the model.
     */
    public TypeTMax getType() {
        return typeTMax;
    }
}
