package com.robi.models.types;
import com.robi.models.values.IValue;
import com.robi.models.values.RefValue;

public class RefType implements IType 
{
    private IType inner;

    public RefType(IType i)
    {
        this.inner = i;
    }

    public IType getInner()
    {
        return this.inner;
    }

    public boolean equals(Object another)
    {
        if (another instanceof RefType)
        {
            return ((RefType)another).getInner().equals(this.inner);
        }
        else
        {
            return false;
        }
    }

    public String toString()
    {
        return "Ref(" + this.inner.toString() + ")";
    }

    public IValue getDefaultValue()
    {
        return new RefValue(0, this.inner);
    }
}
