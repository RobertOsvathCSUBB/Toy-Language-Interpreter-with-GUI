package com.robi.models.state;
import com.robi.models.statements.IStatement;
import com.robi.models.ADTs.*;
import com.robi.models.exception.MyException;
import com.robi.models.values.IValue;
import com.robi.models.values.StringValue;
import java.io.BufferedReader;

public class PrgState
{
    private IStack<IStatement> exeStack;
    private IMap<String, IValue> symTable;
    private IList<IValue> out;
    private IMap<StringValue, BufferedReader> fileTable;
    private IHeap heap;
    private ILatchTable latchTable;
    private IStatement originalPrg;
    private Integer uniqueId;
    static Integer ID_COUNT = 0;

    public PrgState(IStack<IStatement> stk, IMap<String, IValue> map, IList<IValue> out, IMap<StringValue, BufferedReader> ft, IHeap hp, ILatchTable lt, IStatement prg) {
        this.exeStack = stk;
        this.symTable = map;
        this.out = out;
        this.fileTable = ft;
        this.heap = hp;
        this.latchTable = lt;
        this.originalPrg = prg;
        this.uniqueId = PrgState.getId();
        this.exeStack.push(prg);
    }

    public Integer getID() {
        return this.uniqueId;
    }

    public IStack<IStatement> getStack() {
        return this.exeStack;
    }

    public void setStack(IStack<IStatement> stk) {
        this.exeStack = stk;
    }

    public IMap<String, IValue> getSymTable() {
        return this.symTable;
    }

    public void setSymTable(IMap<String, IValue> map) {
        this.symTable = map;
    }

    public IList<IValue> getOut() {
        return this.out;
    }

    public void setOut(IList<IValue> out) {
        this.out = out;
    }

    public IMap<StringValue, BufferedReader> getFileTable() {
        return this.fileTable;
    }

    public void setFileTable(IMap<StringValue, BufferedReader> ft) {
        this.fileTable = ft;
    }

    public IHeap getHeap() {
        return this.heap;
    }

    public void setHeap(IHeap hp) {
        this.heap = hp;
    }

    public ILatchTable getLatchTable() {
        return this.latchTable;
    }

    public void setLatchTable(ILatchTable lt) {
        this.latchTable = lt;
    }

    public IStatement getOriginalPrg() {
        return this.originalPrg;
    }

    public void setOriginalPrg(IStatement prg) {
        this.originalPrg = prg;
    }

    public Boolean isNotCompleted() {
        return !this.exeStack.isEmpty();
    }

    public PrgState oneStep() throws MyException {
        if (this.exeStack.isEmpty()) {
            throw new MyException("Stack is empty");
        }
        IStatement crtStmt = this.exeStack.pop();
        return crtStmt.execute(this);
    }

    private static synchronized Integer getId() 
    {
        return ++PrgState.ID_COUNT;
    }

    public String toString()
    {
        return "ID: " + this.uniqueId + "\nExeStack:\n" + this.exeStack.toString() + "\nSymTable:\n" + this.symTable.toString() + "\nOut:\n" + this.out.toString() 
        + "\nFileTable:\n" + this.fileTable.toString() + "\nHeap:\n" + this.heap.toString() + "\nLatchTable:\n" + this.latchTable.toString() + "\n\n";
    }
}