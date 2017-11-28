package generate;

import io.DisplayConst;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Bound Custom constraint jobshop with the optimal value of
 * with the JobShop with no custom Constraint.
 */
public class JobShopWithBound {

    private final static Logger logger = LoggerFactory.getLogger(JobShopWithBound.class);
    private final String filename;

    public JobShopWithBound(String filename) {
        this.filename = filename;
        String overview = generate();
        System.out.println(overview);
    }

    private String generate() {

        logger.info("JobShop of file " + filename);
        StringBuilder sb = new StringBuilder()
                .append("Jobshop, file : ").append(filename).append("\n")
                .append(DisplayConst.SEPARATOR.toString()).append("\n");

        logger.info("Solving not Bounded Jobshop with cplex NoOverlap constraint");
        JobShop notBounded = new JobShop(filename);
        int objValue = (int) notBounded.getObjValue();

        sb.append("Not bounded objective value = ").append(objValue).append("\n");

        logger.info("Solving  Bounded Jobshop with cplex NoOverlap constraint");
        JobShop bounded = new JobShop(filename, false, objValue);

        logger.info("Solving Bounded Jobshop with custom NoOverlap constraint");
        JobShop custom = new JobShop(filename, true, objValue);

//        sb.append("Bounded domain size before propagation = ")
//                .append(bounded.getDomainSizeBeforePropagation()).append("\n")
//                .append("Domain before propagation :").append("\n")
//                .append(bounded.getDomainBeforePropagation()).append("\n");

        sb.append("Bounded domain size after propagation = ")
                .append(bounded.getDomainSizeAfterPropagation()).append("\n")
                .append("Domain after propagation :").append("\n")
                .append(bounded.getDomainAfterPropagation()).append("\n");

        sb.append("Custom domain size after propagation = ")
                .append(custom.getDomainSizeAfterPropagation()).append("\n")
                .append("Domain after propagation :").append("\n")
                .append(custom.getDomainAfterPropagation()).append("\n");

        return sb.toString();
    }

}
