package com.robi.models.statements;
import com.robi.models.state.PrgState;
import com.robi.models.types.IType;
import com.robi.models.ADTs.IMap;
import com.robi.models.exception.MyException;

public interface IStatement
{
    PrgState execute(PrgState state) throws MyException;
    IMap<String, IType> typecheck(IMap<String, IType> typeEnv) throws MyException;
}
