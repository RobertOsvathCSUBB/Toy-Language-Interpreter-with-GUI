package com.robi.models.statements;
import com.robi.models.ADTs.IMap;
import com.robi.models.exception.MyException;
import com.robi.models.state.PrgState;
import com.robi.models.types.IType;
import com.robi.models.values.IValue;

public class VarDeclaration implements IStatement
{
    private String name;
    private IType type;

    public VarDeclaration(String name, IType type)
    {
        this.name = name;
        this.type = type;
    }

    public String toString()
    {
        return this.type.toString() + " " + this.name + "";
    }

    public PrgState execute(PrgState state) throws MyException
    {
        IMap<String, IValue> symTable = state.getSymTable();
        if (symTable.contains(this.name))
            throw new MyException("Variable already declared!");
        symTable.add(this.name, this.type.getDefaultValue());
        return null;
    }

    public IMap<String, IType> typecheck(IMap<String, IType> typeEnv) throws MyException
    {
        typeEnv.add(this.name, this.type);
        return typeEnv;
    }
}
