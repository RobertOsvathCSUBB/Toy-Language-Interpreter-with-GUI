package com.robi.models.exception;

public class MyException extends Exception
{
    public MyException(String message)
    {
        super(message);
    }
    
    public MyException()
    {
        super();
    }
}
