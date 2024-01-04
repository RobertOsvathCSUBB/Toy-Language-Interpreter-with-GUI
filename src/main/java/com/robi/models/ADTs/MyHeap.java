package com.robi.models.ADTs;
import com.robi.models.exception.MyException;
import com.robi.models.values.IValue;
import java.util.Collection;
import java.util.HashMap;
import java.util.Set;
import java.util.Map.Entry;

public class MyHeap implements IHeap
{
    private HashMap<Integer, IValue> data;
    private Integer nextFree;

    public MyHeap()
    {
        this.data = new HashMap<>();
        this.nextFree = 1;
    }

    public Integer nextFree()
    {
        return this.nextFree;
    }

    public void add(Integer key, IValue value) throws MyException
    {
        if (this.contains(key))
            throw new MyException("Key already exists in heap");
        this.data.put(key, value);
        this.nextFree = 1;
        while (this.data.containsKey(this.nextFree))
            this.nextFree += 1;
    }

    public IValue get(Integer key)
    {
        return this.data.get(key);
    }

    public void update(Integer key, IValue value)
    {
        this.data.put(key, value);
    }

    public void remove(Integer key)
    {
        this.data.remove(key);
    }

    public Boolean contains(Integer key)
    {
        return this.data.containsKey(key);
    }

    public Integer size()
    {
        return this.data.size();
    }

    public Boolean isEmpty()
    {
        return this.data.isEmpty();
    }

    public Collection<IValue> values()
    {
        return this.data.values();
    }

    public Set<Entry<Integer, IValue>> entrySet()
    {
        return this.data.entrySet();
    }

    public String toString()
    {
        String res = "{ ";
        for (Integer key : this.data.keySet())
            res += key.toString() + " -> " + this.data.get(key).toString() + "; ";
        res += "}";
        return res;
    }

    public void setContent(IHeap newHeap)
    {
        this.data = new HashMap<>();
        newHeap.entrySet().forEach(
            (Entry<Integer, IValue> e) -> {
                this.data.put(e.getKey(), e.getValue());
            }
        );
        this.nextFree = 1;
        while (this.data.containsKey(this.nextFree))
            this.nextFree += 1;
    }
}
