package com.robi.models.values;
import com.robi.models.types.IType;
import com.robi.models.types.StringType;

public class StringValue implements IValue 
{
    private String value;

    public StringValue(String value) {
        this.value = value;
    }
    
    @Override
    public Object clone()
    {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            return new StringValue(this.value);
        }
    }

    public String getValue()
    {
        return this.value;
    }

    public String toString()
    {
        return this.value;
    }

    public IType getType()
    {
        return new StringType();
    }

    @Override
    public boolean equals(Object another)
    {
        return this.value.equals(((StringValue)another).getValue());
    }
}
