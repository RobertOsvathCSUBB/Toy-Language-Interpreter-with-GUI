package com.robi.models.statements;
import com.robi.models.ADTs.IHeap;
import com.robi.models.ADTs.IList;
import com.robi.models.ADTs.IMap;
import com.robi.models.exception.MyException;
import com.robi.models.expressions.IExpression;
import com.robi.models.state.PrgState;
import com.robi.models.values.IValue;
import com.robi.models.types.IType;

public class PrintStatement implements IStatement {
    private IExpression exp;

    public PrintStatement(IExpression e)
    {
        this.exp = e;
    }

    public PrgState execute(PrgState state) throws MyException 
    {
        IList<IValue> out = state.getOut();
        try {
            IMap<String, IValue> symTable = state.getSymTable();
            IHeap heap = state.getHeap();
            IValue val = this.exp.evaluate(symTable, heap);
            out.add(val);
            return null;
        } catch (Exception e) {
            throw new MyException(e.getMessage());
        }
    }

    public String toString()
    {
        return "print(" + this.exp.toString() + ")";
    }

    public IMap<String, IType> typecheck(IMap<String, IType> typeEnv) throws MyException
    {
        this.exp.typecheck(typeEnv);
        return typeEnv;
    }
}
