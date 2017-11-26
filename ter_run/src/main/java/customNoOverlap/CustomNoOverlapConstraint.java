package customNoOverlap;

import domains.DomainReduction;
import domains.DomainSlot;
import ilog.concert.IloException;
import ilog.concert.IloIntVar;
import ilog.concert.IloIntervalVar;
import ilog.cp.IloCP;
import ilog.cp.IloCustomConstraint;
import jobs.Job;
import jobs.Jobs;
import jobs.jobsException.JobEndbeforeStartException;
import jobs.jobsException.JobInvalidDurationException;
import jobs.jobsException.JobInvalidRankException;
import jobs.jobsException.JobNegativeException;
import models.ModelFactory;

import java.util.LinkedList;
import java.util.List;

/**
 * Custom noOverlap constraint to be used with the jobShop class.
 */
public class CustomNoOverlapConstraint extends IloCustomConstraint {

    private final Jobs jobs;
    private IloIntVar[][] start;

    private LinkedList<Job> getJobs(IloIntervalVar[] vars, int tMaxValue) {
        LinkedList<Job> jobList = new LinkedList<>();
        for (int i = 0; i < vars.length; i++) {
            IloIntervalVar iloIntervalVar = vars[i];
            try {
                jobList.add(new Job(0, tMaxValue, iloIntervalVar.getSizeMin(), i));
            } catch (JobEndbeforeStartException | JobInvalidDurationException | JobInvalidRankException | JobNegativeException e) {
                e.printStackTrace();
            }
        }
        return jobList;
    }

    public CustomNoOverlapConstraint(IloCP cp, IloIntervalVar[] vars, int tMaxValue) throws IloException {
        super(cp);

        LinkedList<Job> jobList = getJobs(vars, tMaxValue);

        this.jobs = new Jobs(jobList);
        this.start = new IloIntVar[vars.length][];

        for (int i = 0; i < vars.length; i++) {
            start[i] = cp.intVarArray(tMaxValue + 1, 0, 1);
            for (IloIntVar v : start[i]) {
                addVar(v);
            }
        }
    }

    @Override
    public void execute() {

        System.out.println("Execute custom constraint");

        DomainReduction dr = new DomainReduction(ModelFactory.lp(jobs));
        List<DomainSlot> invalidStart = dr.getInvalidStart();

        for (DomainSlot domainSlot : invalidStart) {
            removeValue(start[domainSlot.getI()][domainSlot.getT()], 1);
        }
    }

    /*
    Brouillon :
    accès aux variables de chaques modèles et/ou copie des valiables
    solve de la première tâche
    récupération du domaine
    (mise à jour du domaine des autre tache -> propagate)
     etc...

     cad
     */
}
