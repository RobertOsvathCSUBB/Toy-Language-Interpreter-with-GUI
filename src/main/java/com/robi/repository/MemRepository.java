package com.robi.repository;
import java.util.ArrayList;
import java.util.List;
import com.robi.models.exception.MyException;
import com.robi.models.state.PrgState;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.PrintWriter;
import java.io.IOException;

public class MemRepository implements IRepository 
{
    private List<PrgState> states;
    private String logFilePath;

    public MemRepository()
    {
        this.states = new ArrayList<PrgState>();
    }

    public void setLogFile(String path)
    {
        this.logFilePath = path;
    }

    public void add(PrgState state)
    {
        this.states.add(state);
    }

    public void logPrgStateExec(PrgState state) throws MyException
    {
        try 
        {
            PrintWriter logFileWriter = new PrintWriter(new BufferedWriter(new FileWriter(this.logFilePath, true)));
            logFileWriter.println("\n------------------------\n");
            logFileWriter.println(state.toString());
            logFileWriter.println("\n------------------------\n");
            logFileWriter.close();
        }
        catch (IOException e){
            throw new MyException("IOException: " + e.getMessage());
        }
    }

    public List<PrgState> getPrgList()
    {
        return this.states;
    }

    public void setPrgList(List<PrgState> states)
    {
        this.states = states;
    }
}
