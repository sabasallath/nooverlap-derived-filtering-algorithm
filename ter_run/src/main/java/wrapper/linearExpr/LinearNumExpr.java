package wrapper.linearExpr;

import ilog.concert.*;
import ilog.cplex.IloCplex;

/**
 * LinearExpr implementation for linear integer expression
 */
public class LinearNumExpr implements LinearExpr {
    private final IloLinearNumExpr iloLinearNumExpr;

    public LinearNumExpr(IloCplex cplex) {
        try {
            this.iloLinearNumExpr = cplex.linearNumExpr();
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

        if (addable instanceof IloNumVar) {
            IloNumVar addableNumVar = (IloNumVar) addable;
            try {
                iloLinearNumExpr.addTerm((double) coef, addableNumVar);
            } catch (IloException e) {
                e.printStackTrace();
            }
        } else {
            throw new IllegalArgumentException("Wrong term type");
        }
    }

    @Override
    public IloLinearNumExpr expr() {
        return iloLinearNumExpr;
    }

}
