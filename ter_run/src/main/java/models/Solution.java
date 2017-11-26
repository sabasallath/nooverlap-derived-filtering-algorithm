package models;

import jobs.Jobs;
import models.cut.Cut;

import javax.annotation.concurrent.Immutable;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Solution class to have a common interface for
 * retrieving useful data from MIP, LP and CP model
 * before and after being solve.
 */
@Immutable
public final class Solution {

    private final Jobs jobs;

    private final TypeModel typeModel;
    private final String cut;
    private final long cpuTime;
    private final boolean feasible;
    private final String status;
    private final double objValue;
    private final String initialGrid;
    private final String solutionGrid;
    private final boolean cpFeasible;
    private final double cpMakespan;

    public Solution(TypeModel typeModel,
                    Jobs jobs,
                    Cut cut,
                    double cpMakespan,
                    boolean cpFeasible,
                    boolean feasible,
                    String status,
                    double objValue,
                    String initialGrid,
                    String solvedGrid,
                    long cpuTime) {

        this.typeModel = typeModel;
        this.jobs = jobs;
        this.cut = cut.toString();
        this.cpMakespan = cpMakespan;
        this.cpFeasible = cpFeasible;
        this.feasible = feasible;
        this.status = status;
        this.objValue = objValue;
        this.initialGrid = initialGrid;
        this.solutionGrid = solvedGrid;
        this.cpuTime = cpuTime;
    }

    public double getObjValue() {
        return objValue;
    }

    public String getStatus() {
        return status;
    }

    public String getInitialGrid() {
        return initialGrid;
    }

    public String getSolution() {
        return solutionGrid;
    }

    public TypeModel getTypeModel() {
        return typeModel;
    }

    public long getCpuTime() {
        return cpuTime;
    }

    public String getCut() {
        return cut;
    }

    public boolean isFeasible() {
        return feasible;
    }

    public void writeSolution(String s) {
        String s_t_txt = s.substring(0, s.lastIndexOf(".")) + "_" + typeModel + "_" + cut + ".txt";
        try {
            PrintWriter writer = new PrintWriter(s_t_txt, "UTF-8");
            writer.print(s + "\n" + getTypeModel().toString() + "\n");
            writer.print("Cut : " + cut + "\n");
            writer.print(jobs.toString());
            writer.print(initialGrid + "\n");
            writer.print(solutionGrid + "\n");
            writer.print("cpMakespan : " + cpMakespan + "\n");
            writer.print("cpFeasible : " + cpFeasible + "\n");
            writer.print("Status : " + status + "\n");
            writer.print("z : " + getObjValue() + "\n");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getSolutionGrid() {
        return solutionGrid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Solution solution = (Solution) o;
        return feasible == solution.feasible &&
                Double.compare(solution.objValue, objValue) == 0 &&
                cpFeasible == solution.cpFeasible &&
                Double.compare(solution.cpMakespan, cpMakespan) == 0 &&
                com.google.common.base.Objects.equal(jobs, solution.jobs) &&
                typeModel == solution.typeModel &&
                com.google.common.base.Objects.equal(cut, solution.cut) &&
                com.google.common.base.Objects.equal(status, solution.status) &&
                com.google.common.base.Objects.equal(initialGrid, solution.initialGrid) &&
                com.google.common.base.Objects.equal(solutionGrid, solution.solutionGrid);
    }

    @Override
    public int hashCode() {
        return com.google.common.base.Objects.hashCode(jobs, typeModel, cut, feasible, status, objValue, initialGrid, solutionGrid, cpFeasible, cpMakespan);
    }

    @Override
    public String toString() {
        return "Solution{" +
                "jobs=" + jobs +
                ", typeModel=" + typeModel +
                ", initialGrid='" + initialGrid + '\'' +
                ", cpFeasible='" + cpFeasible + '\'' +
                ", cpMakespan='" + cpMakespan + '\'' +
                ", status='" + status + '\'' +
                ", objValue=" + objValue +
                ", solutionGrid='" + solutionGrid + '\'' +
                ", cpFeasible ='" + cpFeasible + '\'' +
                ", feasible=" + feasible +
                '}';
    }
}
