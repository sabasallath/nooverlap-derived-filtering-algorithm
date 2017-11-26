package jobs;

import com.google.common.collect.ImmutableList;
import jdk.nashorn.internal.ir.annotations.Immutable;
import jobs.jobsException.JobEndbeforeStartException;
import jobs.jobsException.JobInvalidDurationException;
import jobs.jobsException.JobInvalidRankException;
import jobs.jobsException.JobNegativeException;
import org.junit.Test;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static org.mutabilitydetector.unittesting.MutabilityAssert.assertImmutable;

@Immutable
public final class JobsAtomicExemple {

    public static ImmutableList<Jobs> jobsList() {
        List<Jobs> jobsList = new ArrayList<>();

        try {
            // cut_1.txt
            // 0 4 3
            // 0 4 3
            LinkedList<Job> jobs = new LinkedList<>();
            jobs.add(new Job(0, 4, 3, 0));
            jobs.add(new Job(0, 4, 3, 1));
            jobsList.add(new Jobs(jobs));

            // cut_2.txt
            // 0 4 2
            // 0 4 2
            jobs = new LinkedList<>();
            jobs.add(new Job(0, 4, 2, 0));
            jobs.add(new Job(0, 4, 2, 1));
            jobsList.add(new Jobs(jobs));

            // cut_3.txt
            // 1 4 2
            // 1 4 2
            // 0 5 1
            jobs = new LinkedList<>();
            jobs.add(new Job(1, 4, 2, 0));
            jobs.add(new Job(1, 4, 2, 1));
            jobs.add(new Job(0, 5, 1, 2));
            jobsList.add(new Jobs(jobs));

            // cut_4.txt
            // 0 1 2
            // 0 1 1
            jobs = new LinkedList<>();
            jobs.add(new Job(0, 1, 2, 0));
            jobs.add(new Job(0, 1, 1, 1));
            jobsList.add(new Jobs(jobs));

            // cut_5.txt
            // 0 1 1
            // 0 1 1
            jobs = new LinkedList<>();
            jobs.add(new Job(0, 1, 1, 0));
            jobs.add(new Job(0, 1, 1, 1));
            jobsList.add(new Jobs(jobs));

            // cut_6.txt
            // 1 4 2
            // 1 4 2
            // 0 4 1
            jobs = new LinkedList<>();
            jobs.add(new Job(1, 4, 2, 0));
            jobs.add(new Job(1, 4, 2, 1));
            jobs.add(new Job(0, 4, 1, 2));
            jobsList.add(new Jobs(jobs));

            // cut_7.txt
            // 0 2 2
            jobs = new LinkedList<>();
            jobs.add(new Job(0, 2, 2, 0));
            jobsList.add(new Jobs(jobs));

        } catch (JobInvalidDurationException
                | JobEndbeforeStartException
                | JobInvalidRankException
                | JobNegativeException e) {
            e.printStackTrace();
        }

        return ImmutableList.copyOf(jobsList);
    }
}
