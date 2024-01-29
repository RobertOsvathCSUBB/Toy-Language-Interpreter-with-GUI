package com.robi.models.statements;
import com.robi.models.ADTs.ILatchTable;
import com.robi.models.ADTs.IMap;
import com.robi.models.exception.MyException;
import com.robi.models.expressions.IExpression;
import com.robi.models.state.PrgState;
import com.robi.models.types.IType;
import com.robi.models.types.IntType;
import com.robi.models.values.IValue;
import com.robi.models.values.IntValue;

public class NewLatchStatement implements IStatement 
{
    private String var;
    private IExpression exp;

    public NewLatchStatement(String var, IExpression exp)
    {
        this.var = var;
        this.exp = exp;
    }

    public PrgState execute(PrgState state) throws MyException
    {
        IMap<String, IValue> symTable = state.getSymTable();
        if (!symTable.contains(var))
            throw new MyException("Variable not declared");

        ILatchTable latchTable = state.getLatchTable();
        IValue exprVal = this.exp.evaluate(symTable, state.getHeap());
        if (!exprVal.getType().equals(new IntType()))
        throw new MyException("Expression must be of type int");

        IValue varVal = symTable.get(var);
        if (!varVal.getType().equals(new IntType()))
            throw new MyException("Variable must be of type int");

        Integer newFreeLocation = latchTable.nextFree();
        symTable.update(var, new IntValue(newFreeLocation));
        IntValue num = (IntValue)exprVal;
        latchTable.add(newFreeLocation, num.getValue());

        return null;
    }

    public String toString()
    {
        return "newLatch(" + this.var + ", " + this.exp.toString() + ")";
    }

    public IMap<String, IType> typecheck(IMap<String, IType> typeEnv) throws MyException
    {
        IType typevar = typeEnv.get(this.var);
        IType typexp = this.exp.typecheck(typeEnv);
        if (typevar.equals(new IntType()))
        {
            if (typexp.equals(new IntType()))
            {
                return typeEnv;
            }
            else
                throw new MyException("Expression must be of type int.");
        }
        else
            throw new MyException("Variable must be of type int.");
    }
}
