package com.robi.view;
import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.List;
import com.robi.models.ADTs.MyHeap;
import com.robi.models.ADTs.MyLatchTable;
import com.robi.models.ADTs.MyList;
import com.robi.models.ADTs.MyMap;
import com.robi.models.ADTs.MyStack;
import com.robi.models.expressions.ArithExpression;
import com.robi.models.expressions.ReadHeap;
import com.robi.models.expressions.RelationalExpression;
import com.robi.models.expressions.ValueExpression;
import com.robi.models.expressions.VarExpression;
import com.robi.models.state.PrgState;
import com.robi.models.statements.AssignStatement;
import com.robi.models.statements.AwaitStatement;
import com.robi.models.statements.CloseStatement;
import com.robi.models.statements.CompStatement;
import com.robi.models.statements.CondAssignStatement;
import com.robi.models.statements.CountDownStatement;
import com.robi.models.statements.ForkStatement;
import com.robi.models.statements.IStatement;
import com.robi.models.statements.IfStatement;
import com.robi.models.statements.NewLatchStatement;
import com.robi.models.statements.NewStatement;
import com.robi.models.statements.OpenStatement;
import com.robi.models.statements.PrintStatement;
import com.robi.models.statements.ReadStatement;
import com.robi.models.statements.VarDeclaration;
import com.robi.models.statements.WhileStatement;
import com.robi.models.statements.WriteHeap;
import com.robi.models.types.BoolType;
import com.robi.models.types.IType;
import com.robi.models.types.IntType;
import com.robi.models.types.RefType;
import com.robi.models.types.StringType;
import com.robi.models.values.IValue;
import com.robi.models.values.IntValue;
import com.robi.models.values.StringValue;

public class Interpreter 
{
    public static List<PrgState> generatePrograms()
    {
        IStatement program1 = new CompStatement(
            new VarDeclaration("v", new IntType()),
            new CompStatement(
                new AssignStatement("v", new ValueExpression(new IntValue(2))),
                new PrintStatement(new VarExpression("v")))
        );

        IStatement program2 = new CompStatement(
            new VarDeclaration("a", new IntType()),
            new CompStatement(
                new AssignStatement("a",
                    new ArithExpression(
                        new ValueExpression(new IntValue(2)),
                        new ArithExpression(new ValueExpression(new IntValue(3)), new ValueExpression(new IntValue(5)), "*"),
                        "+")),
                new CompStatement(
                    new VarDeclaration("b", new IntType()),
                    new CompStatement(
                        new AssignStatement("b", 
                            new ArithExpression(
                                new ArithExpression(
                                    new VarExpression("a"),
                                    new ArithExpression(
                                        new ValueExpression(new IntValue(4)),
                                        new ValueExpression(new IntValue(2)),
                                        "/"),
                                    "-"),
                                new ValueExpression(new IntValue(7)),
                                "+")
                            ),
                        new PrintStatement(new VarExpression("b"))
                    )
                )
                )
        );

        IStatement program3 = new CompStatement(
            new VarDeclaration("a", new BoolType()),
            new CompStatement(
                new AssignStatement("a",
                    new RelationalExpression(new ValueExpression(new IntValue(2)), new ValueExpression(new IntValue(3)), "<")),
                new CompStatement(
                    new VarDeclaration("v", new IntType()),
                    new CompStatement(
                        new IfStatement(
                            new VarExpression("a"),
                            new AssignStatement("v", new ValueExpression(new IntValue(2))),
                            new AssignStatement("v", new ValueExpression(new IntValue(3)))
                        ),
                        new PrintStatement(new VarExpression("v"))
                    )
                )
            )
        );

        IStatement program4 = new CompStatement(
            new VarDeclaration("varf", new StringType()),
            new CompStatement(
                new AssignStatement("varf", new ValueExpression(new StringValue("test.in"))),
                new CompStatement(
                    new OpenStatement(new VarExpression("varf")),
                    new CompStatement(
                        new VarDeclaration("varc", new IntType()),
                        new CompStatement(
                            new ReadStatement(new ValueExpression(new StringValue("varf")), "varc"),
                            new CompStatement(
                                new PrintStatement(new VarExpression("varc")),
                                new CompStatement(
                                    new ReadStatement(new ValueExpression(new StringValue("varf")), "varc"),
                                    new CompStatement(
                                        new PrintStatement(new VarExpression("varc")),
                                        new CloseStatement(new ValueExpression(new StringValue("varf")))
                                    )
                                )
                            )
                        )
                    )
                )
            )
        );

        IStatement program5 = new CompStatement(
            new VarDeclaration("v", new RefType(new IntType())),
            new CompStatement(
                new NewStatement("v", new ValueExpression(new IntValue(20))),
                new CompStatement(
                    new PrintStatement(new ReadHeap(new VarExpression("v"))),
                    new CompStatement(
                        new WriteHeap("v", new ValueExpression(new IntValue(30))),
                        new PrintStatement(new ArithExpression(
                            new ReadHeap(new VarExpression("v")),
                            new ValueExpression(new IntValue(5)),
                            "+"
                            )
                        )
                    )
                )
            )
        );

        IStatement program6 = new CompStatement(
            new VarDeclaration("v", new RefType(new IntType())),
            new CompStatement(
                new NewStatement("v", new ValueExpression(new IntValue(20))),
                new CompStatement(
                    new VarDeclaration("a", new RefType(new RefType(new IntType()))),
                    new CompStatement(
                        new NewStatement("a", new VarExpression("v")),
                        new CompStatement(
                            new NewStatement("v", new ValueExpression(new IntValue(30))),
                            new PrintStatement(new ReadHeap(new ReadHeap(new VarExpression("a"))))
                        )
                    )
                )
            )
        );

        IStatement program7 = new CompStatement(
            new VarDeclaration("a", new IntType()),
            new CompStatement(
                new AssignStatement("a", new ValueExpression(new IntValue(1))),
                new WhileStatement(
                    new RelationalExpression(new VarExpression("a"), new ValueExpression(new IntValue(4)), "<="),
                    new CompStatement(
                        new PrintStatement(new VarExpression("a")),
                        new AssignStatement("a", new ArithExpression(new VarExpression("a"), new ValueExpression(new IntValue(1)), "+"))
                    )
                )
            )
        );

        IStatement program8 = new CompStatement(
                new VarDeclaration("v", new IntType()),
                new CompStatement(
                    new VarDeclaration("a", new RefType(new IntType())),
                    new CompStatement(
                        new AssignStatement("v", new ValueExpression(new IntValue(10))),
                        new CompStatement(
                            new NewStatement("a", new ValueExpression(new IntValue(22))),
                            new CompStatement(
                                new ForkStatement(
                                    new CompStatement(
                                        new WriteHeap("a", new ValueExpression(new IntValue(30))),
                                        new CompStatement(
                                            new AssignStatement("v", new ValueExpression(new IntValue(32))),
                                            new CompStatement(
                                                new PrintStatement(new VarExpression("v")),
                                                new PrintStatement(new ReadHeap(new VarExpression("a")))
                                            )
                                        )
                                    )
                                ),
                                new CompStatement(
                                    new PrintStatement(new VarExpression("v")),
                                    new PrintStatement(new ReadHeap(new VarExpression("a")))
                                )
                            )
                        )
                    )
                )
        );

        IStatement fork1 = new CompStatement(
            new WriteHeap("v3", new ArithExpression(new ReadHeap(new VarExpression("v3")), new ValueExpression(new IntValue(10)), "*")),
            new CompStatement(
                new PrintStatement(new ReadHeap(new VarExpression("v3"))),
                new CountDownStatement("cnt")
            )
        );

        IStatement fork2 = new CompStatement(
            new WriteHeap("v2", new ArithExpression(new ReadHeap(new VarExpression("v2")), new ValueExpression(new IntValue(10)), "*")),
            new CompStatement(
                new PrintStatement(new ReadHeap(new VarExpression("v2"))),
                new CompStatement(
                    new CountDownStatement("cnt"),
                    new ForkStatement(fork1)
                )
            )
        );

        IStatement fork3 = new CompStatement(
            new WriteHeap("v1", new ArithExpression(new ReadHeap(new VarExpression("v1")), new ValueExpression(new IntValue(10)), "*")),
            new CompStatement(
                new PrintStatement(new ReadHeap(new VarExpression("v1"))),
                new CompStatement(
                    new CountDownStatement("cnt"),
                    new ForkStatement(fork2)
                )
            )
        );

        IStatement program9 = new CompStatement(
            new VarDeclaration("v1", new RefType(new IntType())),
            new CompStatement(
                new VarDeclaration("v2",new RefType(new IntType())),
                new CompStatement(
                    new VarDeclaration("v3", new RefType(new IntType())),
                    new CompStatement(
                        new VarDeclaration("cnt", new IntType()),
                        new CompStatement(
                            new NewStatement("v1", new ValueExpression(new IntValue(2))),
                            new CompStatement(
                                new NewStatement("v2", new ValueExpression(new IntValue(3))),
                                new CompStatement(
                                    new NewStatement("v3", new ValueExpression(new IntValue(4))),
                                    new CompStatement(
                                        new NewLatchStatement("cnt", new ReadHeap(new VarExpression("v2"))),
                                        new CompStatement(
                                            new ForkStatement(fork3),
                                            new CompStatement(
                                                new AwaitStatement("cnt"),
                                                new CompStatement(
                                                    new PrintStatement(new ValueExpression(new IntValue(100))),
                                                    new CompStatement(
                                                        new CountDownStatement("cnt"),
                                                        new PrintStatement(new ValueExpression(new IntValue(100)))
                                                    )
                                                )
                                            )
                                        )
                                    )
                                )
                            )
                        )
                    )
                )
            )
        );

        IStatement program10 = new CompStatement(
            new VarDeclaration("a", new RefType(new IntType())),
            new CompStatement(
                new VarDeclaration("b", new RefType(new IntType())),
                new CompStatement(
                    new VarDeclaration("v", new IntType()),
                    new CompStatement(
                        new NewStatement("a", new ValueExpression(new IntValue(0))),
                        new CompStatement(
                            new NewStatement("b", new ValueExpression(new IntValue(0))),
                            new CompStatement(
                                new WriteHeap("a", new ValueExpression(new IntValue(1))),
                                new CompStatement(
                                    new WriteHeap("b", new ValueExpression(new IntValue(2))),
                                    new CompStatement(
                                        new CondAssignStatement("v", new RelationalExpression(new ReadHeap(new VarExpression("a")), new ReadHeap(new VarExpression("b")), "<"), new ValueExpression(new IntValue(100)), new ValueExpression(new IntValue(200))),
                                        new CompStatement(
                                            new PrintStatement(new VarExpression("v")),
                                            new CompStatement(
                                                new CondAssignStatement("v", new RelationalExpression(new ArithExpression(new ReadHeap(new VarExpression("b")), new ValueExpression(new IntValue(2)), "-"), new ReadHeap(new VarExpression("a")), ">"), new ValueExpression(new IntValue(100)), new ValueExpression(new IntValue(200))),
                                                new PrintStatement(new VarExpression("v"))
                                            )
                                        )
                                    )
                                )
                            )
                        )
                    )
                )
            )
        );

        List<PrgState> programs = new ArrayList<PrgState>();

        try {
            program1.typecheck(new MyMap<String, IType>());
            PrgState state1 = new PrgState(new MyStack<IStatement>(), new MyMap<String, IValue>(), new MyList<IValue>(), new MyMap<StringValue, BufferedReader>(), new MyHeap(), new MyLatchTable(), program1);
            programs.add(state1);
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }

        try {
            program2.typecheck(new MyMap<String, IType>());
            PrgState state2 = new PrgState(new MyStack<IStatement>(), new MyMap<String, IValue>(), new MyList<IValue>(), new MyMap<StringValue, BufferedReader>(), new MyHeap(), new MyLatchTable(), program2);
            programs.add(state2);
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }

        try {
            program3.typecheck(new MyMap<String, IType>());
            PrgState state3 = new PrgState(new MyStack<IStatement>(), new MyMap<String, IValue>(), new MyList<IValue>(), new MyMap<StringValue, BufferedReader>(), new MyHeap(), new MyLatchTable(), program3);
            programs.add(state3);
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }

        try {
            program4.typecheck(new MyMap<String, IType>());
            PrgState state4 = new PrgState(new MyStack<IStatement>(), new MyMap<String, IValue>(), new MyList<IValue>(), new MyMap<StringValue, BufferedReader>(), new MyHeap(), new MyLatchTable(), program4);
            programs.add(state4);
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }

        try {
            program5.typecheck(new MyMap<String, IType>());
            PrgState state5 = new PrgState(new MyStack<IStatement>(), new MyMap<String, IValue>(), new MyList<IValue>(), new MyMap<StringValue, BufferedReader>(), new MyHeap(), new MyLatchTable(), program5);
            programs.add(state5);
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }

        try {
            program6.typecheck(new MyMap<String, IType>());
            PrgState state6 = new PrgState(new MyStack<IStatement>(), new MyMap<String, IValue>(), new MyList<IValue>(), new MyMap<StringValue, BufferedReader>(), new MyHeap(), new MyLatchTable(), program6);
            programs.add(state6);
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }

        try {
            program7.typecheck(new MyMap<String, IType>());
            PrgState state7 = new PrgState(new MyStack<IStatement>(), new MyMap<String, IValue>(), new MyList<IValue>(), new MyMap<StringValue, BufferedReader>(), new MyHeap(), new MyLatchTable(), program7);
            programs.add(state7);
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }

        try {
            program8.typecheck(new MyMap<String, IType>());
            PrgState state8 = new PrgState(new MyStack<IStatement>(), new MyMap<String, IValue>(), new MyList<IValue>(), new MyMap<StringValue, BufferedReader>(), new MyHeap(), new MyLatchTable(), program8);
            programs.add(state8);
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }

        try {
            program9.typecheck(new MyMap<String, IType>());
            PrgState state9 = new PrgState(new MyStack<IStatement>(), new MyMap<String, IValue>(), new MyList<IValue>(), new MyMap<StringValue, BufferedReader>(), new MyHeap(), new MyLatchTable(), program9);
            programs.add(state9);
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }

        try {
            program10.typecheck(new MyMap<String, IType>());
            PrgState state10 = new PrgState(new MyStack<IStatement>(), new MyMap<String, IValue>(), new MyList<IValue>(), new MyMap<StringValue, BufferedReader>(), new MyHeap(), new MyLatchTable(), program10);
            programs.add(state10);
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
        
        return programs;
    }    
}
