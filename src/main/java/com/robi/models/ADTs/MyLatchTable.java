package com.robi.models.ADTs;
import java.util.concurrent.ConcurrentHashMap;
import com.robi.models.exception.MyException;

public class MyLatchTable implements ILatchTable
{
    private ConcurrentHashMap<Integer, Integer> data;
    private Integer newFreeLocation;

    public MyLatchTable()
    {
        this.data = new ConcurrentHashMap<>();
        this.newFreeLocation = 1;
    }

    public Integer nextFree()
    {
        return this.newFreeLocation;
    }

    public void add(Integer key, Integer value) throws MyException
    {
        if (this.contains(key))
            throw new MyException("Key already exists in heap");
        this.data.put(key, value);
        this.newFreeLocation = 1;
        while (this.data.containsKey(this.newFreeLocation))
            this.newFreeLocation += 1;
    }

    public Integer get(Integer key)
    {
        return this.data.get(key);
    }

    public void update(Integer key, Integer value)
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
}
