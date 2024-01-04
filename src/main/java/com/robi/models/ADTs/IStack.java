package com.robi.models.ADTs;
import java.util.Collection;

public interface IStack<T> 
{
    void push(T elem);
    T pop();
    Boolean isEmpty();
    Collection<T> getAll();
}
