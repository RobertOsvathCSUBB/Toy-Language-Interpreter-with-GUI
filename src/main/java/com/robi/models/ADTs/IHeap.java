package com.robi.models.ADTs;
import java.util.Collection;
import java.util.Set;
import java.util.Map.Entry;
import com.robi.models.exception.MyException;
import com.robi.models.values.IValue;

public interface IHeap 
{
    Integer nextFree();
    void add(Integer key, IValue value) throws MyException;
    IValue get(Integer key);
    void update(Integer key, IValue value);
    void remove(Integer key);
    Boolean contains(Integer key);
    Integer size();
    Boolean isEmpty();
    Collection<IValue> values();
    Set<Entry<Integer, IValue>> entrySet();
    void setContent(IHeap newHeap);
}
