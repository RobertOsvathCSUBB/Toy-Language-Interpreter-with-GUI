package com.robi.models.statements;
import com.robi.models.ADTs.IHeap;
import com.robi.models.ADTs.IMap;
import com.robi.models.exception.MyException;
import com.robi.models.expressions.IExpression;
import com.robi.models.state.PrgState;
import com.robi.models.values.IValue;
import com.robi.models.types.IType;

public class AssignStatement implements IStatement 
{
    private String id;
    private IExpression exp;
    
    public AssignStatement(String id, IExpression exp)
    {
        this.id = id;
        this.exp = exp;
    }

    public String toString()
    {
        return this.id + " = " + this.exp.toString() + "";
    }

    public PrgState execute(PrgState state) throws MyException
    {
        IMap<String, IValue> symTable = state.getSymTable();
        IHeap heap = state.getHeap();
        if (!symTable.contains(this.id))
            throw new MyException("Variable " + this.id + " is not defined.");
        try {
            IValue oldVal = symTable.get(this.id);
            IValue newVal = this.exp.evaluate(symTable, heap);
            if (!oldVal.getType().equals(newVal.getType()))
                throw new MyException("Declared type of variable " + this.id + " and type of the assigned expression do not match.");
            symTable.update(this.id, newVal);
            return null;
        } catch (Exception e) {
            throw new MyException(e.getMessage());
        }
    }

    public IMap<String, IType> typecheck(IMap<String, IType> typeEnv) throws MyException
    {
        IType typevar = typeEnv.get(this.id);
        IType typexp = this.exp.typecheck(typeEnv);
        if (typevar.equals(typexp))
            return typeEnv;
        else
            throw new MyException("Assignment: right hand side and left hand side have different types.");
    }
}
