package domains;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.ListMultimap;
import jobs.Jobs;

import javax.annotation.concurrent.Immutable;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;

/**
 * Valid domain start of all jobs.
 */
@Immutable
final class Domain {
    private final Jobs jobs;
    private final ImmutableSet<DomainSlot> domains;

    Domain(Jobs jobs) {
        this.jobs = jobs;
        final HashSet<DomainSlot> domainSlotSet = new HashSet<>();
        for (int i = 0; i < jobs.size(); i++) {
            for(int t : jobs.getValidStart(i)) {
                domainSlotSet.add(new DomainSlot(i, t));
            }
        }

        this.domains = ImmutableSet.copyOf(domainSlotSet);
    }

    private Domain(Jobs jobs, HashSet<DomainSlot> domainSlotSet) {
        this.jobs = jobs;
        this.domains = ImmutableSet.copyOf(domainSlotSet);
    }

    public int getSize() {
        return domains.size();
    }

    /**
     * @return Reduced invalid starting domain size when only the bounds are reduced.
     */
    int getUpperMinusLowerBoundSize() {

        ListMultimap<Integer, Integer> setPerJobs = LinkedListMultimap.create();
        for (DomainSlot domain : domains) {
            setPerJobs.put(domain.getI(), domain.getT());
        }

        int sumD = 0;
        for (int i = 0; i < jobs.size(); i++) {
            List<Integer> ts = setPerJobs.get(i);
            if(! ts.isEmpty()) {
                sumD += Collections.max(ts) - Collections.min(ts) + 1;
            }
        }

        return sumD;
    }

    /**
     * @param invalidStarts List of invalid start for all jobs.
     * @return new reduced domain.
     */
    Domain reduce(List<DomainSlot> invalidStarts) {

        HashSet<DomainSlot> reducedSet = new HashSet<>();
        reducedSet.addAll(domains);

        for (DomainSlot invalidStart : invalidStarts) {
            reducedSet.remove(invalidStart);
        }

        return new Domain(jobs, reducedSet);
    }

    /**
     * @return The invalid start domain of the model.
     */
    ImmutableSet<DomainSlot> getDomains() {
        return domains;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Domain domain = (Domain) o;
        return Objects.equals(jobs, domain.jobs) &&
                Objects.equals(domains, domain.domains);
    }

    @Override
    public int hashCode() {
        return Objects.hash(jobs, domains);
    }


}
