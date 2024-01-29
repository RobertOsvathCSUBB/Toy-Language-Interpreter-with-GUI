package com.robi.models.statements;
import com.robi.models.ADTs.ILatchTable;
import com.robi.models.ADTs.IMap;
import com.robi.models.exception.MyException;
import com.robi.models.state.PrgState;
import com.robi.models.types.IType;
import com.robi.models.types.IntType;
import com.robi.models.values.IValue;
import com.robi.models.values.IntValue;

public class CountDownStatement implements IStatement 
{
    private String var;
    
    public CountDownStatement(String var)
    {
        this.var = var;
    }

    public PrgState execute(PrgState state) throws MyException
    {
        IMap<String, IValue> symTable = state.getSymTable();
        if (!symTable.contains(var))
            throw new MyException("Variable not declared.");

        IValue varVal = symTable.get(var);
        if (!varVal.getType().equals(new IntType()))
            throw new MyException("Variable must be of type int.");

        Integer foundIndex = ((IntValue)varVal).getValue();
        ILatchTable latchTable = state.getLatchTable();
        if (!latchTable.contains(foundIndex))
            throw new MyException("Variable not in latch table");

        Integer num = latchTable.get(foundIndex);
        if (num > 0)
        {
            latchTable.update(foundIndex, num - 1);
            state.getOut().add(new IntValue(state.getID()));
        }
        else
            state.getOut().add(new IntValue(state.getID()));

        return null;
    }

    public String toString()
    {
        return "countDown(" + this.var + ")";
    }

    public IMap<String, IType> typecheck(IMap<String, IType> typeEnv) throws MyException
    {
        IType typevar = typeEnv.get(this.var);
        if (typevar.equals(new IntType()))
        {
            return typeEnv;
        }
        else
            throw new MyException("Variable must be of type int.");
    }
}
