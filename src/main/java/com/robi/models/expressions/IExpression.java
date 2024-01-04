package com.robi.models.expressions;
import com.robi.models.exception.MyException;
import com.robi.models.values.IValue;
import com.robi.models.ADTs.IHeap;
import com.robi.models.ADTs.IMap;
import com.robi.models.types.IType;

public interface IExpression
{
    IValue evaluate(IMap<String, IValue> table, IHeap heap) throws MyException;
    IType typecheck(IMap<String, IType> typeEnv) throws MyException;
}