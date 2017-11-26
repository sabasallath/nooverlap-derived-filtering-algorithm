package wrapper.linearExpr;

import ilog.concert.*;
import ilog.cplex.IloCplex;

/**
 * LinearExpr implementation for linear integer expression
 */
public class LinearIntExpr implements LinearExpr {

    private final IloLinearIntExpr iloLinearIntExpr;

    public LinearIntExpr(IloCplex cplex) {
        try {
            this.iloLinearIntExpr = cplex.linearIntExpr();
        } catch (IloException e) {
            e.printStackTrace();
            throw new RuntimeException("Unable to initialize linear expression");
        }
    }

    @Override
    public void addTerm(IloAddable addable) {
        addTerm(1, addable);
    }

    @Override
    public void addTerm(int coef, IloAddable addable) {
        // Prevent adding null value to Expr
        if (addable == null) return;

        if (addable instanceof IloIntVar) {
            IloIntVar addableIntVar = (IloIntVar) addable;
            try {
                iloLinearIntExpr.addTerm(coef, addableIntVar);
            } catch (IloException e) {
                e.printStackTrace();
            }
        } else {
            throw new IllegalArgumentException("Wrong term type");
        }
    }

    @Override
    public IloNumExpr expr() {
        return iloLinearIntExpr;
    }
}
