package jobs;


import com.google.common.collect.ImmutableList;
import jdk.nashorn.internal.ir.annotations.Immutable;
import jdk.nashorn.internal.scripts.JO;
import jobs.jobsException.JobEndbeforeStartException;
import jobs.jobsException.JobInvalidDurationException;
import jobs.jobsException.JobInvalidRankException;
import jobs.jobsException.JobNegativeException;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Immutable
public final class JobsVilimExemple {

    public static ImmutableList<Jobs> jobsList() {
        List<Jobs> jobsList = new LinkedList<>();

        try {
            LinkedList<Job> jobs = new LinkedList<>();
            // Edge finding
            // 4 30 4
            // 5 13 3
            // 5 13 3
            // 14 18 5
            jobs.add(new Job(4, 30, 4, 0));
            jobs.add(new Job(5, 13, 3, 1));
            jobs.add(new Job(5, 13, 3, 2));
            jobs.add(new Job(14, 18, 5, 3));
            Jobs jobs1 = new Jobs(jobs);

            // Not First, Not Last
            // 0 25 11
            // 1 27 10
            // 4 20 2
            jobs = new LinkedList<>();
            jobs.add(new Job(0, 25, 11, 0));
            jobs.add(new Job(1, 27, 10, 1));
            jobs.add(new Job(4, 20, 2, 2));
            Jobs jobs2 = new Jobs(jobs);

            // Overload Checking
            // 0 13 5
            // 1 14 5
            // 2 12 6
            jobs = new LinkedList<>();
            jobs.add(new Job(0, 13, 5, 0));
            jobs.add(new Job(1, 14, 5, 1));
            jobs.add(new Job(2, 12, 6, 2));
            Jobs jobs3 = new Jobs(jobs);

            // Precedence
            // 0 13 5
            // 1 14 5
            // 7 17 2
            jobs = new LinkedList<>();
            jobs.add(new Job(0, 13, 5, 0));
            jobs.add(new Job(1, 14, 5, 1));
            jobs.add(new Job(7, 17, 2, 2));
            Jobs jobs4 = new Jobs(jobs);

            jobsList.add(jobs1);
            jobsList.add(jobs2);
            jobsList.add(jobs3);
            jobsList.add(jobs4);

            return ImmutableList.copyOf(jobsList);

        } catch (JobInvalidDurationException
                | JobEndbeforeStartException
                | JobInvalidRankException
                | JobNegativeException e) {
            e.printStackTrace();
        }

        return ImmutableList.copyOf(jobsList);
    }
}
