package wrapper.linearExpr;

import ilog.concert.IloAddable;
import ilog.concert.IloNumExpr;

/**
 * LinearNumExpr and LinearIntExpr wrapper used to hide MIPS or LP dedicated call.
 */
public interface LinearExpr {

    /**
     * Add a term to the linear expression.
     * Null value will be safely ignore.
     * @param addable term
     */
    void addTerm(IloAddable addable);

    /**
     * Add a term to the linear expression
     * with a coefficient.
     * Null value will be safely ignore.
     * @param coef coefficient
     * @param addable term
     */
    void addTerm(int coef, IloAddable addable);

    /**
     * @return The underlying class object representation
     * that as been encapsulated in this LinearExpr class.
     */
    IloNumExpr expr();
}
