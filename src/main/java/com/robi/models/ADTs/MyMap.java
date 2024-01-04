package com.robi.models.ADTs;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map.Entry;

public class MyMap<T1, T2> implements IMap<T1, T2>
{
    private HashMap<T1, T2> map;

    public MyMap()
    {
        this.map = new HashMap<>();
    }

    public void add(T1 key, T2 value)
    {
        this.map.put(key, value);
    }

    public T2 get(T1 key)
    {
        return this.map.get(key);
    }

    public void update(T1 key, T2 value)
    {
        this.map.put(key, value);
    }

    public void remove(T1 key)
    {
        this.map.remove(key);
    }

    public Boolean contains(T1 key)
    {
        return this.map.containsKey(key);
    }

    public Integer size()
    {
        return this.map.size();
    }

    public Boolean isEmpty()
    {
        return this.map.isEmpty();
    }

    public String toString()
    {
        String res = "{ ";
        for (T1 key : this.map.keySet())
            res += key.toString() + " -> " + this.map.get(key).toString() + "; ";
        res += "}";
        return res;
    }

    public Collection<T2> values()
    {
        return this.map.values();
    }

    public Collection<T1> keys()
    {
        return this.map.keySet();
    }

    public Collection<Entry<T1, T2>> entrySet()
    {
        return this.map.entrySet();
    }

    public IMap<T1, T2> clone()
    {
        MyMap<T1, T2> res = new MyMap<T1, T2>();
        for (Entry<T1, T2> e : this.map.entrySet())
            res.add(e.getKey(), e.getValue());
        return res;
    }
}
