package io.solvingInstance;

import java.util.List;
import java.util.StringJoiner;

/**
 * A solving instance purpose is to capture and aggregate
 * all initial and resulting data of a model that is solve.
 * Typically a solving instance will aggregate data from the
 * MIP, LP and CP of one model that is solve.
 * This in order to display a resume of all this data in a
 * easy understanding and compact way.
 */
public class SolvingInstance {

    private final List<SolvingInstanceMember> members;

    /**
     * Build a Solving instance from all the members contained in the members parameter.
     * @param members List of solving instance members previously build.
     */
    public SolvingInstance(List<SolvingInstanceMember> members) {
        this.members = members;
    }

    List<SolvingInstanceMember> getMembers() {
        return members;
    }

    /**
     * @return Return a header from the previously added instance members.
     */
    String getHeaders() {
        StringJoiner sj = new StringJoiner(", ", "[", "]");
        members.forEach(e -> sj.add(e.getHeader()));
        return sj.toString();
    }

    @Override
    public String toString() {
        StringJoiner sb = new StringJoiner(", ");
        members.forEach(e -> sb.add(e.toString()));
        return sb.toString();
    }
}
