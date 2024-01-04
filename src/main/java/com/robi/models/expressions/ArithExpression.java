package com.robi.models.expressions;
import com.robi.models.ADTs.IHeap;
import com.robi.models.ADTs.IMap;
import com.robi.models.exception.MyException;
import com.robi.models.types.IntType;
import com.robi.models.values.IValue;
import com.robi.models.values.IntValue;
import com.robi.models.types.IType;

public class ArithExpression implements IExpression
{
    private IExpression left;
    private IExpression right;
    private String op;
    
    public ArithExpression(IExpression left, IExpression right, String op)
    {
        this.left = left;
        this.right = right;
        this.op = op;
    }

    public IValue evaluate(IMap<String, IValue> table, IHeap heap) throws MyException
    {
        try {
            IValue v1, v2;
            v1 = left.evaluate(table, heap);
            v2 = right.evaluate(table, heap);
            if (!(v1.getType().equals(new IntType())) || !(v2.getType().equals(new IntType())))
                throw new MyException("Invalid expression: " + v1.toString() + " " + op + " " + v2.toString());
            if (this.op.equals("+")) {
                IntValue res = new IntValue(((IntValue)v1).getValue() + ((IntValue)v2).getValue());
                return res;
            }
            if (this.op.equals("-")) {
                IntValue res = new IntValue(((IntValue)v1).getValue() - ((IntValue)v2).getValue());
                return res;
            }
            if (this.op.equals("*")) {
                IntValue res = new IntValue(((IntValue)v1).getValue() * ((IntValue)v2).getValue());
                return res;
            }
            if (this.op.equals("/")) {
                if (((IntValue)v2).getValue() == 0)
                    throw new MyException("Division by zero");
                IntValue res = new IntValue(((IntValue)v1).getValue() / ((IntValue)v2).getValue());
                return res;
            }
            throw new MyException("Invalid operator");
        } catch (Exception e) {
            throw new MyException("Invalid expression");
        }
    }

    public String toString()
    {
        return left.toString()+ " " + op + " " + right.toString();
    }

    public IType typecheck(IMap<String, IType> typeEnv) throws MyException
    {
            IType type1, type2;
            type1 = left.typecheck(typeEnv);
            type2 = right.typecheck(typeEnv);
            if (type1.equals(new IntType()))
            {
                if (type2.equals(new IntType()))
                {
                    return new IntType();
                }
                else
                {
                    throw new MyException("Second operand is not an integer");
                }
            }
            else 
            {
                throw new MyException("First operand is not an integer");
            }
    }
}
