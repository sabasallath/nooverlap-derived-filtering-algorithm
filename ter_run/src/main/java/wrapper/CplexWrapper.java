package wrapper;

import com.google.common.io.ByteStreams;
import ilog.concert.IloException;
import ilog.concert.IloNumVar;
import ilog.cplex.IloCplex;
import io.LoggerPrintStream;
import models.TypeModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.PrintStream;
import java.io.UnsupportedEncodingException;

/**
 * Cplex wrapper used to hide IP or LP specific call.
 */
public final class CplexWrapper extends IloCplex {
    private final TypeModel type;
    private static Logger logger = LoggerFactory.getLogger(CplexWrapper.class);

    /**
     * Return a IloCplex instance of the given type.
     * Cplex output and warnings are redirected to a logger.
     * To display cplex output set log level to trace.
     * @param type Type of the instance : Integer Program (IP) or Linear Program (LP).
     * @throws IloException see cplex documentation.
     */
    public CplexWrapper(TypeModel type) throws IloException {
        super();
        this.type = type;
        logit();

    }

    /**
     * This constructor is not used.
     * Return a IloCplex instance of the given type.
     * Cplex output and warnings are redirected to a logger.
     * To display cplex output set log level to trace.
     * @param s see cplex documentation
     * @param strings see cplex documentation
     * @param type Type of the instance : Integer Program (IP) or Linear Program (LP).
     * @throws IloException see cplex documentation.
     */
    public CplexWrapper(String s, String[] strings, TypeModel type) throws IloException {
        super(s, strings);
        this.type = type;
        logit();
    }

    /**
     * Redirect annoying output and normal warning
     * (such as invalid MIP start) to logs.
     * /!\ Can also hide also more serious warning.
     * To display cplex output set log level to trace.
     */
    private void logit() {

        if (logger.isTraceEnabled()) {
            try {
                this.setWarning(new PrintStream(new LoggerPrintStream(logger), true, "utf-8"));
                this.setOut(new PrintStream(new LoggerPrintStream(logger), true, "utf-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        } else {
            this.setWarning(ByteStreams.nullOutputStream());
            this.setOut(ByteStreams.nullOutputStream());
        }
    }

    /**
     * Return an IloNumVar that encapsulate a IloIntVar or a IloNumVar
     * accordingly to the model type of the calling cplex instance.
     * If the instance is a IP it will return an IloIntVar.
     * If the instance is a LP it will return an IloNumVar.
     * @param lb lower bound
     * @param ub upper bound
     * @return IloNumVar encapsulation of IloIntVar or IloNumVar
     */
    public IloNumVar var(int lb, int ub) {
        switch (type) {
            case MIP:
                try {
                    return super.intVar(lb, ub);
                } catch (IloException e) {
                    e.printStackTrace();
                }
                break;
            case LP:
                try {
                    return super.numVar(lb, ub);
                } catch (IloException e) {
                    e.printStackTrace();
                }
                break;
            default:
                throw new IllegalArgumentException();
        }
        throw new IllegalArgumentException();
    }
}