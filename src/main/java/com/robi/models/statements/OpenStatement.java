package com.robi.models.statements;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import com.robi.models.ADTs.IMap;
import com.robi.models.exception.MyException;
import com.robi.models.expressions.IExpression;
import com.robi.models.state.PrgState;
import com.robi.models.types.StringType;
import com.robi.models.values.IValue;
import com.robi.models.values.StringValue;
import com.robi.models.types.IType;

public class OpenStatement implements IStatement
{
    private IExpression exp;

    public OpenStatement(IExpression exp)
    {
        this.exp = exp;
    }

    public String toString()
    {
        return "openRFile(" + this.exp.toString() + ")";
    }

    public PrgState execute(PrgState state) throws MyException
    {
        try {
            IValue expVal = this.exp.evaluate(state.getSymTable(), state.getHeap());
            if (!expVal.getType().equals(new StringType())) {
                throw new MyException("Expression is not a string");
            }

            IMap<StringValue, BufferedReader> fileTable = state.getFileTable();
            if (fileTable.contains((StringValue) expVal)) {
                throw new MyException("File already opened");
            }

            try {
                BufferedReader file = new BufferedReader(new FileReader(((StringValue) expVal).getValue()));
                fileTable.add((StringValue) expVal, file);
            } 
            catch (FileNotFoundException e) {
                throw new MyException("File does not exist: " + e.getMessage());
            } 
        }
        catch (MyException e) {
            throw new MyException(e.getMessage());
        }
        
        return null;
    }

    public IMap<String, IType> typecheck(IMap<String, IType> typeEnv) throws MyException
    {
        IType typexp = this.exp.typecheck(typeEnv);
        if (typexp.equals(new StringType()))
            return typeEnv;
        else
            throw new MyException("Expression is not a string");
    }
}
