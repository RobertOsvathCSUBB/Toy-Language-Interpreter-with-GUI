package com.robi.models.statements;
import com.robi.models.ADTs.IMap;
import com.robi.models.exception.MyException;
import com.robi.models.state.PrgState;
import com.robi.models.types.IType;

public class NOPStatement implements IStatement 
{
    public PrgState execute(PrgState state) throws MyException 
    {
        return null;
    }

    public String toString()
    {
        return "";
    }    

    public IMap<String, IType> typecheck(IMap<String, IType> typeEnv) throws MyException
    {
        return typeEnv;
    }
}
