package io;

import jobs.Job;
import jobs.Jobs;
import jobs.jobsException.JobEndbeforeStartException;
import jobs.jobsException.JobInvalidDurationException;
import jobs.jobsException.JobInvalidRankException;
import jobs.jobsException.JobNegativeException;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

/**
 * Read jobs data from a One machine - Due date - Release date instance.
 * Format of input file :
 * With N the number of jobs,
 * est_i the earliest starting time of job i,
 * lct_i the latest completion time of job i,
 * p_i the duration of job i. The input format is :
 *                                       <br>
 * -- Beginning of the file --           <br>
 * N                                     <br>
 * est_1 lct_1 p_1                       <br>
 * est_2 lct_2 p_2                       <br>
 * est_i lct_i p_i                       <br>
 * ...                                   <br>
 * est_n lct_n p_n                       <br>
 * -- End of the file --                 <br>
 *                                       <br>
 * The DataReader instance will crash as soon as he detect
 * a malformed input file to avoid invalid data computation
 */
public class DataReader {

    private final Scanner sc;
    private final String filename;
    private LinkedList<Job> jobs;
    private List<Integer> current;
    private int nbLine;

    public DataReader(File file) {
        try {
            this.sc = new Scanner(file, "UTF-8");
            this.jobs = new LinkedList<>();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
        this.current = new LinkedList<>();
        this.nbLine = 0;
        this.filename = file.getName();
    }

    private int next() {
        if (current.size() == 0 && sc.hasNextLine()) {
            nbLine++;

            try {
                for (String s : sc.nextLine().split(" ")) {
                    current.add(Integer.parseInt(s));
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
                throw new RuntimeException("Invalid character format or empty line at line " + nbLine + ".");
            }

            if (nbLine == 1) {
                checkNbArg(1);
            } else {
                checkNbArg(3);
            }
            return current.remove(0);
        } else if (current.size() != 0) {
            return current.remove(0);
        } else {
            throw new RuntimeException("Invalid input file format at line " + nbLine + ".");
        }
    }

    private void checkNbArg(int i) {
        if (current.size() != i) {
            throw new RuntimeException("Invalid input file format at line " + nbLine +
                    ". Wrong number of element, found(" + current.size() + "), expected(" + i + ")");
        }
    }

    private void checkEndOfFile(int nbJobs) {
        if (sc.hasNextLine()) {
            throw new RuntimeException("Potentially invalid file format at line " + ++nbLine +
                    ". expected(" + ++nbJobs + ") lines, found(" + ++nbJobs + ") lines");
        }
    }

    /**
     * Read jobs data from a One machine - Due date - Release date instance.
     * @return Jobs data.
     */
    public Jobs read() {
        int nbJobs = next();
        for (int i = 0; i < nbJobs; i++) {
            try {
                jobs.add(new Job(next(), next(), next(), i));
            } catch (JobInvalidDurationException
                    | JobEndbeforeStartException
                    | JobInvalidRankException
                    | JobNegativeException e) {
                e.printStackTrace();
                throw new RuntimeException("Invalid job input at line " + nbLine + " of file " + filename + ".");
            }
        }

        checkEndOfFile(nbJobs);
        return new Jobs(jobs);
    }
}