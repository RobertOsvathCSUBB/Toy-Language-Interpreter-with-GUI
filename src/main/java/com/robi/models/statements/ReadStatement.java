package com.robi.models.statements;
import java.io.BufferedReader;
import java.io.IOException;
import com.robi.models.ADTs.IMap;
import com.robi.models.exception.MyException;
import com.robi.models.expressions.IExpression;
import com.robi.models.state.PrgState;
import com.robi.models.types.IntType;
import com.robi.models.types.StringType;
import com.robi.models.values.IValue;
import com.robi.models.values.IntValue;
import com.robi.models.values.StringValue;
import com.robi.models.types.IType;

public class ReadStatement implements IStatement 
{
    private IExpression exp;
    private String varName;

    public ReadStatement(IExpression exp, String varName)
    {
        this.exp = exp;
        this.varName = varName;
    }

    public String toString()
    {
        return "readFile(" + this.exp.toString() + ", " + this.varName + ")";
    }

    public PrgState execute(PrgState state) throws MyException
    {
        IMap<String, IValue> symTable = state.getSymTable();
        if (!symTable.contains(this.varName)) {
            throw new MyException("Undefined variable");
        }

        IValue val = symTable.get(this.varName);
        if (!val.getType().equals(new IntType())) {
            throw new MyException("Variable is not of type int");
        }

        IValue expVal = this.exp.evaluate(state.getSymTable(), state.getHeap());
        if (!expVal.getType().equals(new StringType())) {
            throw new MyException("Expression is not of type string");
        }
        
        IValue filename = state.getSymTable().get(((StringValue)expVal).getValue());
        if (!filename.getType().equals(new StringType())) {
            throw new MyException("Filename is not of type string");
        }

        BufferedReader fileIn = state.getFileTable().get((StringValue)filename);
        if (fileIn == null) {
            throw new MyException("File not opened");
        }

        try {
            String line = fileIn.readLine();
            if (line == null) {
                symTable.update(this.varName, new IntType().getDefaultValue());
            }
            else {
                symTable.update(this.varName, new IntValue(Integer.parseInt(line)));
            }   
        }
        catch (IOException e) {
            throw new MyException("Error reading from file: " + e.getMessage());
        }

        return null;
    }

    public IMap<String, IType> typecheck(IMap<String, IType> typeEnv) throws MyException
    {
        IType typexp = this.exp.typecheck(typeEnv);
        if (typexp.equals(new StringType()))
            return typeEnv;
        else
            throw new MyException("Read file: expression is not a string.");
    }
}
