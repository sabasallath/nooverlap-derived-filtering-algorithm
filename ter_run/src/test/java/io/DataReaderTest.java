package io;

import jobs.JobsAtomicExemple;
import jobs.JobsVilimExemple;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.File;

public final class DataReaderTest {


    @Test
    public void vilimFiles() {

        String[] fileStr = {
                "input/input_relaxation/vilim/vilim_edgefinding.txt",
                "input/input_relaxation/vilim/vilim_notfirstlast.txt",
                "input/input_relaxation/vilim/vilim_overloadchecking.txt",
                "input/input_relaxation/vilim/vilim_precedence.txt"
        };

        for (int i = 0; i < fileStr.length; i++) {
            String filename = fileStr[i];
            DataReader data = new DataReader(new File(filename));
            assertEquals(JobsVilimExemple.jobsList().get(i), data.read());
        }
    }

    @Test
    public void atomicFiles() {

        String[] fileStr = {
                "input/input_relaxation/cut/cut_1.txt",
                "input/input_relaxation/cut/cut_2.txt",
                "input/input_relaxation/cut/cut_3.txt",
                "input/input_relaxation/cut/cut_4.txt",
                "input/input_relaxation/cut/cut_5.txt",
                "input/input_relaxation/cut/cut_6.txt",
        };

        for (int i = 0; i < fileStr.length; i++) {
            String filename = fileStr[i];
            DataReader data = new DataReader(new File(filename));
            assertEquals(JobsAtomicExemple.jobsList().get(i), data.read());
        }
    }

}
