package com.robi.models.ADTs;
import java.util.List;

public interface IList<T>
{
    void add(T elem);
    T getElem(int index);
    void setElem(int index, T elem);
    void remove(int index);
    Integer size();
    Boolean isEmpty();
    List<T> getAll();
}
