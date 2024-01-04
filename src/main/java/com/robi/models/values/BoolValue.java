package com.robi.models.values;
import com.robi.models.types.IType;
import com.robi.models.types.BoolType;

public class BoolValue implements IValue 
{
    private Boolean value;

    public BoolValue(Boolean value)
    {
        this.value = value;
    }

    @Override
    public Object clone()
    {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            return new BoolValue(this.value);
        }
    }

    public Boolean getValue()
    {
        return this.value;
    }

    public String toString()
    {
        return Boolean.toString(this.value);
    }

    public IType getType()
    {
        return new BoolType();
    }

    @Override
    public boolean equals(Object another)
    {
        return this.value.equals(((BoolValue)another).getValue());
    }
}
