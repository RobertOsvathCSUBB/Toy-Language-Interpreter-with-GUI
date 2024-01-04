package com.robi.models.expressions;
import com.robi.models.ADTs.IHeap;
import com.robi.models.ADTs.IMap;
import com.robi.models.exception.MyException;
import com.robi.models.types.BoolType;
import com.robi.models.values.BoolValue;
import com.robi.models.values.IValue;
import com.robi.models.types.IType;

public class LogicExpression implements IExpression 
{
    private IExpression left;
    private IExpression right;
    private String op;

    public LogicExpression(IExpression left, IExpression right, String op)
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
            if (!(v1.getType() instanceof BoolType) || !(v2.getType() instanceof BoolType))
                throw new MyException("Invalid expression: " + v1.toString() + " " + op + " " + v2.toString());
            if (this.op.equals("and")) {
                BoolValue res = new BoolValue(((BoolValue)v1).getValue() && ((BoolValue)v2).getValue());
                return res;
            }
            if (this.op.equals("or")) {
                BoolValue res = new BoolValue(((BoolValue)v1).getValue() || ((BoolValue)v2).getValue());
                return res;
            }
            throw new MyException("Invalid operator");
        } catch (Exception e) {
            throw new MyException(e.getMessage());
        }
    }

    public String toString()
    {
        return left.toString() + " " + op + " " + right.toString();
    }   
    
    public IType typecheck(IMap<String, IType> typeEnv) throws MyException
    {
            IType type1, type2;
            type1 = left.typecheck(typeEnv);
            type2 = right.typecheck(typeEnv);
            if (type1.equals(new BoolType()))
            {
                if (type2.equals(new BoolType()))
                {
                    return new BoolType();
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
