package com.robi.models.statements;
import com.robi.models.ADTs.IHeap;
import com.robi.models.ADTs.IMap;
import com.robi.models.ADTs.IStack;
import com.robi.models.exception.MyException;
import com.robi.models.expressions.IExpression;
import com.robi.models.state.PrgState;
import com.robi.models.types.BoolType;
import com.robi.models.values.BoolValue;
import com.robi.models.values.IValue;
import com.robi.models.types.IType;

public class WhileStatement implements IStatement 
{
    private IExpression exp;
    private IStatement stmt;

    public WhileStatement(IExpression exp, IStatement stmt)
    {
        this.exp = exp;
        this.stmt = stmt;
    }

    public String toString()
    {
        return "while (" + this.exp.toString() + ") {" + this.stmt.toString() + "}";
    }

    public PrgState execute(PrgState state) throws MyException
    {
        try
        {
            IMap<String, IValue> symTable = state.getSymTable();
            IHeap heap = state.getHeap();
            IValue expVal = this.exp.evaluate(symTable, heap);
            
            if (!expVal.getType().equals(new BoolType()))
                throw new MyException("Expression is not boolean");

            BoolValue boolVal = (BoolValue)expVal;
            IStack<IStatement> stack = state.getStack();
            if (boolVal.getValue())
            {
                stack.push(this);
                stack.push(this.stmt);
            }
            return null;
        }
        catch (Exception e)
        {
            throw new MyException(e.getMessage());
        }
    }

    public IMap<String, IType> typecheck(IMap<String, IType> typeEnv) throws MyException
    {
        IType typexp = this.exp.typecheck(typeEnv);
        if (typexp.equals(new BoolType()))
        {
            this.stmt.typecheck(typeEnv.clone());
            return typeEnv;
        }
        else
            throw new MyException("The condition of WHILE isn't of type bool");
    }
}
