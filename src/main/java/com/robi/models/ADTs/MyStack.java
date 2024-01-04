package com.robi.models.ADTs;
import java.util.Collection;
import java.util.Stack;

public class MyStack<T> implements IStack<T> 
{
    private Stack<T> stack;

    public MyStack()
    {
        this.stack = new Stack<T>();
    }

    public void push(T elem)
    {
        this.stack.push(elem);
    }

    public T pop()
    {
        return this.stack.pop();
    }

    public Boolean isEmpty()
    {
        return this.stack.isEmpty();
    } 

    public Collection<T> getAll()
    {
        return this.stack;
    }

    public String toString()
    {
        MyStack<T> aux = new MyStack<T>();
        for (T elem : this.stack)
            aux.push(elem);
        String res = "{ ";
        while (!aux.isEmpty())
            res += aux.pop().toString() + "; ";
        res+= "}";
        return res;
    }
}
