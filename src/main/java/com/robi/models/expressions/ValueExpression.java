package com.robi.models.expressions;
import com.robi.models.ADTs.IHeap;
import com.robi.models.ADTs.IMap;
import com.robi.models.values.IValue;
import com.robi.models.types.IType;

public class ValueExpression implements IExpression 
{
    private IValue value;
    
    public ValueExpression(IValue value)
    {
        this.value = value;
    }

    public IValue evaluate(IMap<String, IValue> table, IHeap heap) 
    {
        return value;
    }

    public String toString()
    {
        return value.toString();
    }

    public IType typecheck(IMap<String, IType> typeEnv)
    {
        return value.getType();
    }
}
