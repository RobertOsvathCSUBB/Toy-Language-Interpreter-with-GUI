package com.robi.models.types;
import com.robi.models.values.IValue;

public interface IType 
{
    boolean equals(Object another);
    String toString();
    IValue getDefaultValue();
}
