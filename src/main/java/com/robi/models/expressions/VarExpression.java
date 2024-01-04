package com.robi.models.expressions;
import com.robi.models.ADTs.IHeap;
import com.robi.models.ADTs.IMap;
import com.robi.models.values.IValue;
import com.robi.models.types.IType;

public class VarExpression implements IExpression 
{
    private String id;
    
    public VarExpression(String id)
    {
        this.id = id;
    }

    public IValue evaluate(IMap<String, IValue> table, IHeap heap)
    {
        return table.get(id);
    }

    public String toString()
    {
        return id;
    }    

    public IType typecheck(IMap<String, IType> typeEnv)
    {
        return typeEnv.get(id);
    }
}
