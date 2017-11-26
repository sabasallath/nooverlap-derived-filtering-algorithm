package models;
import domains.DomainSlot;
import ilog.concert.IloException;
import ilog.concert.IloNumVar;
import ilog.concert.IloObjective;
import jobs.Jobs;
import domains.DomainReduction;
import domains.DomainSize;
import models.cut.Cut;
import models.cut.TypeCut;
import models.displayer.ModelDisplayer;
import models.displayer.ModelDisplayerFactory;
import models.displayer.SelectData;
import models.tmax.TMax;
import wrapper.CplexWrapper;
import wrapper.linearExpr.LinearExpr;
import wrapper.linearExpr.LinearExprFactory;
import java.util.OptionalInt;

/**
 * Modeling of the noOverlap global constraint.
 * A solution of this model is a solution of the noOverlap.
 */
final class ModelCplex implements ModelCplexItf {

    private static LinearExprFactory lFactory = new LinearExprFactory();
    private final Boolean grid;
    private CplexWrapper cplex;
    private Jobs jobs;
    private TypeModel type;
    private Cut cut;
    private int n;
    private int H;
    private IloNumVar[][] xit;
    private IloNumVar[][] yit;
    private IloNumVar[] st;
    private ModelDisplayer modelDisplayer;
    private Solution solution;
    private TMax tMax;

    ModelCplex(Jobs jobs, TypeModel type, Cut cut, TMax tMax) {
        this(jobs, type, cut, tMax, false);
    }

    ModelCplex(Jobs jobs, TypeModel type, Cut cut, TMax tMax, Boolean grid) {
        try {
            this.cplex = new CplexWrapper(type);
        } catch (IloException e) {
            throw new RuntimeException(e);
        }

        this.jobs = jobs;
        this.type = type;
        this.cut = cut;
        this.n = jobs.size();
        this.H = jobs.getH();
        this.xit = new IloNumVar[n][];
        this.yit = new IloNumVar[n][];
        this.st = new IloNumVar[H];

        this.tMax = tMax;
        this.grid = grid;

        init_timetables();
        addConstraints();
        addObjective();
        setDisplayGrid(grid);

        this.solution = solve();
    }

    /**
     * @return Generate the solution of the model.
     */
    private Solution solve() {

        try {
            String initialGrid = "";

            long cpuTimeStart = System.currentTimeMillis();
            final boolean modelSolvable = cplex.solve();
            long cpuTime = System.currentTimeMillis() - cpuTimeStart;

            double objValue = -1;
            boolean feasible = false;
            String solvedGrid = "NO SOLUTION";
            if (modelSolvable) {
                objValue = cplex.getObjValue();
                feasible = !(objValue > 0);
                if (grid) {
                    solvedGrid = modelDisplayer.getSolution();
                } else {
                    solvedGrid = "solved";
                }
            }

            return new Solution(
                    type,
                    jobs,
                    cut,
                    tMax.get(),
                    tMax.isFeasible(),
                    feasible,
                    cplex.getStatus().toString(),
                    objValue,
                    initialGrid,
                    solvedGrid,
                    cpuTime);

        } catch(IloException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public DomainSize dSize() {
        return new DomainReduction(this).domainSize();
    }

    /**
     * Initialisation of the execution, starting time table and st table.
     */
    private void init_timetables() {

        for (int t = 0; t < H; t++) {
            st[t] = cplex.var(0, n);
        }

        for (int i = 0; i < n; i++) {
            xit[i] = new IloNumVar[H];
            yit[i] = new IloNumVar[H];

            for (int t = 0; t < H; t++) {
                xit[i][t] = null;
                yit[i][t] = null;
            }
        }

        for (int i = 0; i < n; i++) {
            for (int t = jobs.getEst(i); t <= jobs.getLct(i); t++) {
                if (t < tMax.get() || (! tMax.isFeasible())) {
                    xit[i][t] = cplex.var(0, 1);
                } else {
                    xit[i][t] = cplex.var(0, 0);
                }
            }
            for (int t = jobs.getEst(i); t <= jobs.getLst(i); t++) {
                if (t < tMax.get() || (! tMax.isFeasible())) {
                    yit[i][t] = cplex.var(0, 1);
                } else {
                    yit[i][t] = cplex.var(0, 0);
                }
            }
        }
    }

    /**
     * Add all the constraints to the model.
     * Note that two constraints are implicit due to null value :
     * i can only be executed  and can only start in his authorized range.
     */
    private void addConstraints() {

        try {
            contraintOneExecution();
            constraintUniqueStart();
            constraintOneStart();
            constraintExecuteAfterStart();

            if (cut.contains(TypeCut.A_SUM_XIT_EQUAL_PI))
                cutA();

            if(cut.contains(TypeCut.B_START_CLOSE_TO_RUN))
                cutB();

            if(cut.contains(TypeCut.C_PRECEDENCE))
                cutC();

            if(cut.contains(TypeCut.D_MANDATORY_SECTION))
                cutD();

            constraintBoundXitYit();
            constraintBoundSt();

        } catch(IloException e){
            e.printStackTrace();
        }
    }

    /*
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
     * Constraints
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
     */

    /**
     * Add objective to the model.
     */
    private void addObjective() {
        try {
            IloObjective objective = cplex.minimize(cplex.sum(st));
            cplex.add(objective);
        } catch(IloException e){
        e.printStackTrace();
    }
    }

    /**
     * st >= 0
     * @throws IloException See cplex documentation.
     */
    private void constraintBoundSt() throws IloException {
        for (IloNumVar aSt : st) {
            cplex.addGe(aSt, 0);
        }
    }

    /**
     * xit ≥ 0, xit ≤ 1, yit ≥ 0, yit ≤ 1
     * @throws IloException See cplex documentation.
     */
    private void constraintBoundXitYit() throws IloException {
        for (int i = 0; i < n; i++) {
            for (int t = 0; t < H; t++) {
                if (xit[i][t] != null) {
                    cplex.addLe(xit[i][t], 1);
                    cplex.addGe(xit[i][t], 0);
                }
                if (yit[i][t] != null) {
                    cplex.addLe(yit[i][t], 1);
                    cplex.addGe(yit[i][t], 0);
                }
            }
        }
    }

    /**
     * If i start at t, i must be executed on interval [t, p + p_i[
     */
    private void constraintExecuteAfterStart() throws IloException {
        for (int t = 0; t < H; t++) {
            for (int i = 0; i < n; i++) {
                if(yit[i][t] != null) {
                    for (int tt = t; tt < t + jobs.getP(i); tt++) {
                        cplex.add(cplex.le(yit[i][t], xit[i][tt]));
                    }
                }
            }
        }
    }

    /**
     * At most one task start at each date
     */
    private void constraintOneStart() throws IloException {
        for (int t = 0; t < H; t++) {
            LinearExpr count_starting_overlap_at_t = lFactory.build(type, cplex);
            for (int i = 0; i < n; i++) {
                count_starting_overlap_at_t.addTerm(yit[i][t]);
            }
            cplex.addLe(count_starting_overlap_at_t.expr(), 1);
        }
    }

    /**
     * i must start at a unique moment
     */
    private void constraintUniqueStart() throws IloException {
        for (int i = 0; i < n; i++) {
            LinearExpr count_job_start = lFactory.build(type, cplex);
            for (int t = 0; t < H; t++) {
                count_job_start.addTerm(yit[i][t]);
            }
            cplex.addEq(count_job_start.expr(), 1);
        }
    }

    /**
     * At most one task i execute at time t
     */
    private void contraintOneExecution() throws IloException {
        for (int t = 0; t < H; t++) {
            LinearExpr count_overlap_at_t = lFactory.build(type, cplex);
            for (int i = 0; i < n; i++) {
                count_overlap_at_t.addTerm(xit[i][t]);
            }
            count_overlap_at_t.addTerm(-1, st[t]);
            cplex.addLe(count_overlap_at_t.expr(), 1);
        }
    }

    /**
     * If t belongs to the obligatory section of a job i,
     * x_it = 1 and sum of y_it = 1 on the obligatory section
     * and y_it = 0 outside of the obligatory section.
     */
    private void cutD() throws IloException {
        for (int i = 0; i < n; i++) {
            OptionalInt oStart = jobs.getOStart(i);
            OptionalInt oEnd = jobs.getOEnd(i);
            OptionalInt op = jobs.getOP(i);
            if(oStart.isPresent() && oEnd.isPresent() && op.isPresent()) {
                int oStartInt = oStart.getAsInt();
                int oEndInt = oEnd.getAsInt();
                int opInt = op.getAsInt();

                for (int t = oStartInt; t <= oEndInt; t++) {
                    xit[i][t].setLB(1.0);
                }

                // Sum of y_it = 1 on the obligatory section
                int tt = Math.max(oStartInt - (jobs.getP(i) - opInt), jobs.getEst(i));
                LinearExpr start_close_sum = lFactory.build(type, cplex);
                for (int t = tt; t <= oStartInt; t++) {
                    start_close_sum.addTerm(yit[i][t]);
                }
                cplex.addEq(start_close_sum.expr(), 1.0);

                // Y_it = 0 outside of the obligatory section
                for (int t = oStartInt + 1; t <= jobs.getLst(i); t++) {
                    yit[i][t].setUB(0);
                }
            }
        }
    }

    /**
     * If job i start at t, job j does'nt start in interval [t, t + p_i]
     */
    private void cutC() throws IloException {
        for (int i = 0; i < n; i++) {
            for (int t = 0; t < H; t++) {
                if (yit[i][t] != null) {
                    for (int ii = 0; ii < n; ii++) {
                        if (ii != i) {
                            LinearExpr dont_start_sum = lFactory.build(type, cplex);
                            for (int tt = t; tt < t + jobs.getP(i) && tt < H; tt++) {
                                dont_start_sum.addTerm(yit[ii][tt]);
                            }
                            cplex.addLe(dont_start_sum.expr(), cplex.diff(1.0, yit[i][t]));
                        }
                    }
                }
            }
        }
    }

    /**
     * If i is executed at time t, then i can only start in the interval [t - p_i + 1, t]
     */
    private void cutB() throws IloException {
        for (int i = 0; i < n; i++) {
            for (int t = 0; t < H; t++) {
                if (xit[i][t] != null) {
                    LinearExpr count_start_if_run = lFactory.build(type, cplex);
                    for (int tt = Math.max(0, t - jobs.getP(i) + 1); tt <= t; tt++) {
                        count_start_if_run.addTerm(yit[i][tt]);
                    }
                    cplex.addGe(count_start_if_run.expr(), xit[i][t]);
                }
            }
        }
    }

    /**
     * Sum of x_it is equal to the duration of a job i
     */
    private void cutA() throws IloException {
        for (int i = 0; i < n; i++) {
            LinearExpr count_total_time_job = lFactory.build(type, cplex);
            for (int t = 0; t < H; t++) {
                count_total_time_job.addTerm(xit[i][t]);
            }
            cplex.addEq(count_total_time_job.expr(), jobs.getP(i));
        }
    }

    /*
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
     * Solve with fixed start
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
     */

    @Override
    public Solution resolveWithFixedStart(DomainSlot ds) {
        setForcedStart(ds);
        solution = solve();
        unSetForcedStart(ds);
        return solution;
    }

    /**
     * Start the model with this domain slot start date forced to 1.
     * @param domainSlot Domain slot to be forced to 1.
     */
    private void setForcedStart(DomainSlot domainSlot) {
        try {
            yit[domainSlot.getI()][domainSlot.getT()].setLB(1.0);
        } catch (IloException e) {
            e.printStackTrace();
        }
    }

    /**
     * Unset the previously forced domain slot start.
     * @param domainSlot Domain slot to be unset.
     */
    private void unSetForcedStart(DomainSlot domainSlot) {
        try {
            yit[domainSlot.getI()][domainSlot.getT()].setLB(0.0);
        } catch (IloException e) {
            e.printStackTrace();
        }
    }

    /*
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
     * Getters
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
     */

    @Override
    public Jobs getJobs() {
        return jobs;
    }


    @Override
    public TypeModel getType() {
        return type;
    }

    /**
     * @return The solution of the model.
     */
    public Solution getSolution() {
        return solution;
    }

    /**
     * @return Tmax of the model.
     */
    public TMax getTMax() {
        return tMax;
    }

    /*
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
     * Displayer utils
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
     */

    /**
     * When reducing the domain the model displayer is not set for performance reason.
     * @param grid Generate or not the display grid.
     */
    private void setDisplayGrid(Boolean grid) {
        if (grid) {
            this.modelDisplayer = ModelDisplayerFactory.build(this);
        } else {
            this.modelDisplayer = null;
        }
    }

    @Override
    public boolean notNull(SelectData sd, int i, int t) {
        switch (sd) {
            case XIT:
                return xit[i][t] != null;
            case YIT:
                return yit[i][t] != null;
            default:
                throw new IllegalArgumentException("");
        }
    }

    @Override
    public double getValue(SelectData sd, int i, int t) {
        switch (sd) {
            case XIT:
                try {
                    return cplex.getValue(xit[i][t]);
                } catch (IloException e) {
                    e.printStackTrace();
                }
                break;
            case YIT:
                try {
                    return cplex.getValue(yit[i][t]);
                } catch (IloException e) {
                    e.printStackTrace();
                }
                break;
            default:
                throw new IllegalArgumentException("");
        }
        return -1;
    }

    @Override
    public double getValue(int t) {
        try {
            return cplex.getValue(st[t]);
        } catch (IloException e) {
            e.printStackTrace();
        }
        return -1;
    }

}
