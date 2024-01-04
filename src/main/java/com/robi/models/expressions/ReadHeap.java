package com.robi.models.expressions;
import com.robi.models.ADTs.IHeap;
import com.robi.models.ADTs.IMap;
import com.robi.models.exception.MyException;
import com.robi.models.values.IValue;
import com.robi.models.values.RefValue;
import com.robi.models.types.IType;
import com.robi.models.types.RefType;

public class ReadHeap implements IExpression 
{
    private IExpression exp;

    public ReadHeap(IExpression exp)
    {
        this.exp = exp;
    }

    public String toString()
    {
        return "rH(" + this.exp.toString() + ")";
    }

    public IValue evaluate(IMap<String, IValue> table, IHeap heap) throws MyException
    {
        IValue expVal = this.exp.evaluate(table, heap);
        if (!(expVal instanceof RefValue))
            throw new MyException("Expression is not a reference.");

        Integer address = ((RefValue)expVal).getAddress();
        if (!heap.contains(address))
            throw new MyException("Address not found in heap.");

        IValue value = heap.get(address);
        return value;
    }

    public IType typecheck(IMap<String, IType> typeEnv) throws MyException
    {
        IType type = this.exp.typecheck(typeEnv);
        if (type instanceof RefType)
        {
            RefType refType = (RefType)type;
            return refType.getInner();
        }
        else
            throw new MyException("Expression is not a reference.");
    }
}