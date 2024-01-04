package com.robi.models.statements;
import com.robi.models.ADTs.IHeap;
import com.robi.models.ADTs.IMap;
import com.robi.models.exception.MyException;
import com.robi.models.expressions.IExpression;
import com.robi.models.state.PrgState;
import com.robi.models.types.RefType;
import com.robi.models.values.IValue;
import com.robi.models.values.RefValue;
import com.robi.models.types.IType;

public class NewStatement implements IStatement 
{
    private String var;
    private IExpression expr;

    public NewStatement(String var, IExpression expr)
    {
        this.var = var;
        this.expr = expr;
    }

    public PrgState execute(PrgState state) throws MyException
    {
        IMap<String, IValue> symTable = state.getSymTable();
        if (!symTable.contains(var))
            throw new MyException("Variable not declared");
        
        IHeap heap = state.getHeap();
        IValue exprVal = this.expr.evaluate(symTable, heap);
        IValue varVal = symTable.get(var);
        if (!varVal.getType().equals(new RefType(exprVal.getType())))
            throw new MyException("Variable and expression types do not match");

        Integer addr = heap.nextFree();
        heap.add(addr, exprVal);
        ((RefValue)varVal).setAddress(addr);
        symTable.update(var, varVal);

        return null;
    }

    public String toString()
    {
        return "new(" + this.var + ", " + this.expr.toString() + ")";
    }

    public IMap<String, IType> typecheck(IMap<String, IType> typeEnv) throws MyException
    {
        IType typevar = typeEnv.get(this.var);
        IType typexp = this.expr.typecheck(typeEnv);
        if (typevar.equals(new RefType(typexp)))
            return typeEnv;
        else
            throw new MyException("New: right hand side and left hand side have different types.");
    }
}
