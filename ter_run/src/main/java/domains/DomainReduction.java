package domains;

import com.google.common.base.Objects;
import models.*;

import javax.annotation.concurrent.Immutable;
import java.util.LinkedList;
import java.util.List;

/**
 * Class that contain a filtering algorithm
 * that remove invalid start domain from a model
 * and store the resulting reduced domain.
 */
@Immutable
public final class DomainReduction {

    private final int initialSize;
    private final int finalSize;
    private final int finalSizeCpComparable;
    private final Domain domain;
    private final Domain reduced;
    private final ModelCplexItf model;
    private final List<DomainSlot> nonValidStart;

    /**
     * @param model to reduce the starting domain size.
     */
    public DomainReduction(ModelCplexItf model) {
        this.model = model;
        this.domain = new Domain(model.getJobs());
        this.initialSize = domain.getSize();
        this.nonValidStart = getNonValidStart();
        this.reduced = domain.reduce(nonValidStart);
        this.finalSize = reduced.getSize();
        this.finalSizeCpComparable = reduced.getUpperMinusLowerBoundSize();
    }

    /**
     * @return List of non valid start domain.
     */
    private List<DomainSlot> getNonValidStart() {

        List<DomainSlot> nonValidStart = new LinkedList<>();

        for (int i = 0; i < model.getJobs().size(); i++)
            nonValidStart.addAll(filter_job(i));

        return nonValidStart;

    }

    /**
     * @param job Job to filter non valid start.
     * @return List of non valid start for the job input.
     */
    private List<DomainSlot> filter_job(int job) {

        List<DomainSlot> fixedInvalid = new LinkedList<>();

        for (int t = model.getJobs().getEst(job); t <= model.getJobs().getLst(job); t++) {

            boolean feasible = false;
            DomainSlot domainSlot = new DomainSlot(job, t);

            if (t < model.getTMax().get())
                feasible = model.resolveWithFixedStart(domainSlot).isFeasible();

            if (! feasible)
                fixedInvalid.add(domainSlot);
        }

        return fixedInvalid;
    }

    /**
     * @return Immutable domain valid starting size of a model before and after the propagation phase.
     */
    public DomainSize domainSize() {
        return new DomainSize(initialSize, finalSize, finalSizeCpComparable);
    }

    /**
     * @return List of invalid starting domain size for all jobs of the model.
     */
    public List<DomainSlot> getInvalidStart() {
        return nonValidStart;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DomainReduction that = (DomainReduction) o;
        return initialSize == that.initialSize &&
                finalSize == that.finalSize &&
                finalSizeCpComparable == that.finalSizeCpComparable &&
                Objects.equal(domain, that.domain) &&
                Objects.equal(reduced, that.reduced) &&
                Objects.equal(model, that.model) &&
                Objects.equal(nonValidStart, that.nonValidStart);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(initialSize, finalSize, finalSizeCpComparable, domain, reduced, model, nonValidStart);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("DomainReduction{");
        sb.append("initialSize=").append(initialSize);
        sb.append(", finalSize=").append(finalSize);
        sb.append(", finalSizeCpComparable=").append(finalSizeCpComparable);
        sb.append(", domain=").append(domain);
        sb.append(", reduced=").append(reduced);
        sb.append(", model=").append(model);
        sb.append(", nonValidStart=").append(nonValidStart);
        sb.append('}');
        return sb.toString();
    }
}
