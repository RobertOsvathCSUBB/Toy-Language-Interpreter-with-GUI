package com.robi.models.ADTs;
import java.util.Collection;
import java.util.Map.Entry;

import com.robi.models.exception.MyException;

public interface ILatchTable 
{
    Integer nextFree();
    void add(Integer key, Integer value) throws MyException;
    Integer get(Integer key);
    void update(Integer key, Integer value);
    void remove(Integer key);
    Boolean contains(Integer key);
    Integer size();
    Boolean isEmpty();
    Collection<Entry<Integer, Integer>> entrySet();
}
