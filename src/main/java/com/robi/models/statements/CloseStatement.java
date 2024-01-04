package com.robi.models.statements;
import java.io.BufferedReader;
import com.robi.models.ADTs.IMap;
import com.robi.models.exception.MyException;
import com.robi.models.expressions.IExpression;
import com.robi.models.state.PrgState;
import com.robi.models.types.StringType;
import com.robi.models.values.IValue;
import com.robi.models.values.StringValue;
import com.robi.models.types.IType;

public class CloseStatement implements IStatement
{
    private IExpression exp;

    public CloseStatement(IExpression exp)
    {
        this.exp = exp;
    }

    public String toString()
    {
        return "closeRFile(" + this.exp.toString() + ")";
    }

    public PrgState execute(PrgState state) throws MyException
    {
        IValue expVal = this.exp.evaluate(state.getSymTable(), state.getHeap());
        if (!expVal.getType().equals(new StringType())) {
            throw new MyException("Expression is not a string");
        }

        IValue filename = state.getSymTable().get(((StringValue)expVal).getValue());
        if (!filename.getType().equals(new StringType())) {
            throw new MyException("Filename is not a string");
        }

        IMap<StringValue, BufferedReader> fileTable = state.getFileTable();
        BufferedReader file = fileTable.get((StringValue)filename);
        if (file == null) {
            throw new MyException("File not opened");
        }

        try {
            file.close();
        }
        catch (Exception e) {
            throw new MyException("Error closing file: " + e.getMessage());
        }

        fileTable.remove((StringValue)filename);

        return null;
    }

    public IMap<String, IType> typecheck(IMap<String, IType> typeEnv) throws MyException
    {
        IType typexp = this.exp.typecheck(typeEnv);
        if (typexp.equals(new StringType()))
            return typeEnv;
        else
            throw new MyException("Close file: expression is not a string.");
    }
}
