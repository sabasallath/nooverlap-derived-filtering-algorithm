package io.solvingInstance;

import io.DisplayConst;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.List;
import java.util.StringJoiner;

/**
 * A solving instances purpose is to capture and aggregate
 * all initial and resulting data of a list of models that are solved.
 * It is use in combination with the Relaxation classes.
 */
public class SolvingInstances {

    private List<SolvingInstance> si;

    public SolvingInstances() {
        this.si = new LinkedList<>();
    }

    /**
     * Add a solving instance of a particular model to the solving instances.
     * @param solvingInstance Solving instance of one model to add.
     */
    public void add(SolvingInstance solvingInstance) {
        si.add(solvingInstance);
    }

    /**
     * Display the solving instances.
     */
    public void display() {
        System.out.println(toString());
    }

    @Override
    public String toString() {
        StringJoiner sj = new StringJoiner("\n");
        sj.add(DisplayConst.SEPARATOR.toString());
        sj.add("Overview of relaxation");
        sj.add(DisplayConst.SEPARATOR.toString());
        sj.add(getHelper());
        sj.add(DisplayConst.SEPARATOR.toString());
        if (! si.isEmpty() && ! si.get(0).getMembers().isEmpty()) {
            sj.add(si.get(0).getHeaders());
            for (SolvingInstance sic : si) {
                sj.add(sic.getMembers().toString());
            }
        } else {
            sj.add("Empty !");
        }
        return sj.toString();
    }

    /**
     * @return An header to help understand abbreviations of instance members types.
     */
    private String getHelper() {
        StringBuilder sb = new StringBuilder();
        if (! si.isEmpty() && ! si.get(0).getMembers().isEmpty()) {
            SolvingInstance simc = si.get(0);
            for (SolvingInstanceMember sim : simc.getMembers()) {
                sb.append(sim.getType().helper()).append("\n");
            }
            return sb.toString();
        } else {
            return "Empty Helper !";
        }
    }

    /**
     * Write the solving instances to a file.
     * @param s Filename of the file to write.
     */
    public void write(String s) {
        String s_t_txt = s + ".txt";
        try {
            PrintWriter writer = new PrintWriter(s_t_txt, "UTF-8");
            writer.print(s + "\n");
            writer.print(toString());
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
