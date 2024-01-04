package com.robi.models.statements;
import com.robi.models.ADTs.IHeap;
import com.robi.models.ADTs.IMap;
import com.robi.models.exception.MyException;
import com.robi.models.expressions.IExpression;
import com.robi.models.state.PrgState;
import com.robi.models.values.IValue;
import com.robi.models.values.RefValue;
import com.robi.models.types.IType;
import com.robi.models.types.RefType;

public class WriteHeap implements IStatement
{
    private String varName;
    private IExpression exp;

    public WriteHeap(String varName, IExpression exp)
    {
        this.varName = varName;
        this.exp = exp;
    }

    public String toString()
    {
        return "wH(" + this.varName + ", " + this.exp.toString() + ")";
    }

    public PrgState execute(PrgState state) throws MyException
    {
        IMap<String, IValue> symTable = state.getSymTable();
        if (!symTable.contains(this.varName))
            throw new MyException("Variable " + this.varName + " is not defined.");

        IValue varVal = symTable.get(this.varName);
        if (!(varVal instanceof RefValue))
            throw new MyException("Variable " + this.varName + " is not a reference.");

        Integer address = ((RefValue)varVal).getAddress();
        IHeap heap = state.getHeap();
        if (!heap.contains(address))
            throw new MyException("Address " + address + " not found in heap.");

        IValue expVal = this.exp.evaluate(symTable, heap);
        if (!expVal.getType().equals(((RefValue)varVal).getLocationType()))
            throw new MyException("Expression type and reference variable type do not match.");

        heap.update(address, expVal);
        return null;
    }

    public IMap<String, IType> typecheck(IMap<String, IType> typeEnv) throws MyException
    {
        IType typevar = typeEnv.get(this.varName);
        IType typexp = this.exp.typecheck(typeEnv);
        if (typevar.equals(new RefType(typexp)))
            return typeEnv;
        else
            throw new MyException("WriteHeap: right hand side and left hand side have different types.");
    }
}
