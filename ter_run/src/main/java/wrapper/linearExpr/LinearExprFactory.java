package wrapper.linearExpr;

import ilog.cplex.IloCplex;
import models.TypeModel;


/**
 * LinearExpr factory used to hide LinearIntExpr and LinearNumExpr specific call.
 */
public class LinearExprFactory {

    /**
     * Build an encapsulated IloLinearIntExpr or IloLinearNumExpr into a
     * LinearExpr hiding specific call of the subclass.
     * @param typeModel MIP or LP type
     * @param cplex IloCplex or CplexWrapper instance
     * @return LinearExpr encapsulation of IP or LP linear expression.
     */
    public LinearExpr build(TypeModel typeModel, IloCplex cplex) {

        LinearExpr linearExpr;

        switch (typeModel) {
            case MIP:
                    linearExpr = new LinearIntExpr(cplex);
                break;
            case LP:
                    linearExpr = new LinearNumExpr(cplex);
                    break;
            default:
                throw new IllegalArgumentException("models.TypeModel");
        }

        return linearExpr;
    }
}
