package com.robi.models.statements;
import com.robi.models.ADTs.IMap;
import com.robi.models.exception.MyException;
import com.robi.models.expressions.IExpression;
import com.robi.models.state.PrgState;
import com.robi.models.types.BoolType;
import com.robi.models.types.IType;

public class CondAssignStatement implements IStatement 
{
    private String var;
    private IExpression exp1;
    private IExpression exp2;
    private IExpression exp3;
    
    public CondAssignStatement(String var, IExpression exp1, IExpression exp2, IExpression exp3)
    {
        this.var = var;
        this.exp1 = exp1;
        this.exp2 = exp2;
        this.exp3 = exp3;
    }

    public PrgState execute(PrgState state) throws MyException
    {
        IStatement ifStatement = new IfStatement(this.exp1, new AssignStatement(this.var, this.exp2), new AssignStatement(this.var, this.exp3));
        state.getStack().push(ifStatement);
        return null;
    }

    public String toString()
    {
        return this.var + "=" + this.exp1.toString() + "?" + this.exp2.toString() + ":" + this.exp3.toString();
    }

    public IMap<String, IType> typecheck(IMap<String, IType> typeEnv) throws MyException
    {
        IType exp1Type = this.exp1.typecheck(typeEnv);
        if (!exp1Type.equals(new BoolType()))
            throw new MyException("Expression 1 must be of type bool");

        IType varType = typeEnv.get(var);
        IType exp2Type = this.exp2.typecheck(typeEnv);
        IType exp3Type = this.exp3.typecheck(typeEnv);
        if (!varType.equals(exp2Type) || !varType.equals(exp3Type))
            throw new MyException("Expressions 2 and 3 don't match the type of the variable");

        return typeEnv;
    }
}
