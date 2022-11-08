package edu.sjsu.fwjs;

import java.util.ArrayList;
import java.util.List;

/**
 * FWJS expressions.
 */
public interface Expression {
    /**
     * Evaluate the expression in the context of the specified environment.
     */
    public Value evaluate(Environment env);
}

// NOTE: Using package access so that all implementations of Expression
// can be included in the same file.

/**
 * FWJS constants.
 */
class ValueExpr implements Expression {
    private Value val;
    public ValueExpr(Value v) {
        this.val = v;
    }
    public Value evaluate(Environment env) {
        return this.val;
    }
}

/**
 * Expressions that are a FWJS variable.
 */
class VarExpr implements Expression {
    private String varName;
    public VarExpr(String varName) {
        this.varName = varName;
    }
    public Value evaluate(Environment env) {
        return env.resolveVar(varName);
    }
}

/**
 * A print expression.
 */
class PrintExpr implements Expression {
    private Expression exp;
    public PrintExpr(Expression exp) {
        this.exp = exp;
    }
    public Value evaluate(Environment env) {
        Value v = exp.evaluate(env);
        System.out.println(v.toString());
        return v;
    }
}
/**
 * Binary operators (+, -, *, etc).
 * Currently only numbers are supported.
 */
class BinOpExpr implements Expression {
    private Op op;
    private Expression e1;
    private Expression e2;
    public BinOpExpr(Op op, Expression e1, Expression e2) {
        this.op = op;
        this.e1 = e1;
        this.e2 = e2;
    }

    @SuppressWarnings("incomplete-switch")
    public Value evaluate(Environment env) {
        // YOUR CODE HERE
    	IntVal n1= (IntVal) e1.evaluate(env);
    	IntVal n2= (IntVal) e2.evaluate(env);
    	Value result=null;
    	if (op.equals(Op.ADD)) {
    		result= new IntVal (n1.toInt() + n2.toInt());
    	}
    	else if (op.equals(Op.SUBTRACT)) {
    		result= new IntVal (n1.toInt() - n2.toInt());
    	}
    	else if (op.equals(Op.MULTIPLY)) {
    		result= new IntVal (n1.toInt() * n2.toInt());
    	}
    	else if (op.equals(Op.DIVIDE)) {
    		result= new IntVal (n1.toInt() / n2.toInt());
    	}
    	else if (op.equals(Op.MOD)) {
    		result= new IntVal (n1.toInt() % n2.toInt());
    	}
    	else if (op.equals(Op.GT)) {
    		result= new BoolVal (n1.toInt() > n2.toInt());
    	}
    	else if (op.equals(Op.GE)) {
    		result= new BoolVal (n1.toInt() >= n2.toInt());
    	}
    	else if (op.equals(Op.LT)) {
    		result= new BoolVal (n1.toInt() < n2.toInt());
    	}
    	else if (op.equals(Op.LE)) {
    		result= new BoolVal (n1.toInt() <= n2.toInt());
    	}
    	else if (op.equals(Op.EQ)) {
    		result= new BoolVal (n1.toInt() == n2.toInt());
    	}
        return result;
    }
}

/**
 * If-then-else expressions.
 * Unlike JS, if expressions return a value.
 */
class IfExpr implements Expression {
    private Expression cond;
    private Expression thn;
    private Expression els;
    public IfExpr(Expression cond, Expression thn, Expression els) {
        this.cond = cond;
        this.thn = thn;
        this.els = els;
    }
    public Value evaluate(Environment env) {
        // YOUR CODE HERE
        if(((BoolVal)cond.evaluate(env)).toBoolean()) {
        	return thn.evaluate(env);
        } else if(els != null) {
        	return els.evaluate(env);
        }
        
		return new NullVal();
}
}

/**
 * While statements (treated as expressions in FWJS, unlike JS).
 */
class WhileExpr implements Expression {
    private Expression cond;
    private Expression body;
    public WhileExpr(Expression cond, Expression body) {
        this.cond = cond;
        this.body = body;
    }
    public Value evaluate(Environment env) {
        // YOUR CODE HERE
    	BoolVal cond1 = (BoolVal)cond.evaluate(env);
    	while (cond1.toBoolean() == true) {
    		body.evaluate(env);
            cond1 = (BoolVal) cond.evaluate(env);
    	}
        return new IntVal(0);
    }
}

/**
 * Sequence expressions (i.e. 2 back-to-back expressions).
 */
class SeqExpr implements Expression {
    private Expression e1;
    private Expression e2;
    public SeqExpr(Expression e1, Expression e2) {
        this.e1 = e1;
        this.e2 = e2;
    }
    public Value evaluate(Environment env) {
        // YOUR CODE HERE
        //evaluate e1 and return evaluated e2
        e1.evaluate(env);
        return e2.evaluate(env);
    }
}

/**
 * Declaring a variable in the local scope.
 */
class VarDeclExpr implements Expression {
    private String varName;
    private Expression exp;
    public VarDeclExpr(String varName, Expression exp) {
        this.varName = varName;
        this.exp = exp;
    }
    public Value evaluate(Environment env) {
        // YOUR CODE HERE
    	Value value = exp.evaluate(env);
    	env.createVar(varName, value);
    	return value;
        
    }
}

/**
 * Updating an existing variable.
 * If the variable is not set already, it is added
 * to the global scope.
 */
class AssignExpr implements Expression {
    private String varName;
    private Expression e;
    public AssignExpr(String varName, Expression e) {
        this.varName = varName;
        this.e = e;
    }
    public Value evaluate(Environment env) {
        // YOUR CODE HERE
         //update var in environment
         env.updateVar(varName, e.evaluate(env));
         // Returns the updated var from environment
         return env.resolveVar(varName);
    }
}

/**
 * A function declaration, which evaluates to a closure.
 */
class FunctionDeclExpr implements Expression {
    private List<String> params;
    private Expression body;
    public FunctionDeclExpr(List<String> params, Expression body) {
        this.params = params;
        this.body = body;
    }
    public Value evaluate(Environment env) {
        // YOUR CODE HERE
    	Value closure = new ClosureVal (params, body, env);
        return closure;
    }
}

/**
 * Function application.
 */
class FunctionAppExpr implements Expression {
    private Expression f;
    private List<Expression> args;
    public FunctionAppExpr(Expression f, List<Expression> args) {
        this.f = f;
        this.args = args;
    }
    public Value evaluate(Environment env) {
        // YOUR CODE HERE
        ClosureVal val = (ClosureVal) f.evaluate(env);	// Evaluate f to get ClosureVal
    	List<Value> evalArgs = new ArrayList<Value>();	// List of evaluated values.
    	
    	for(int i = 0; i < args.size(); i++) {
    		evalArgs.add(args.get(i).evaluate(env));
    	}

    	return val.apply(evalArgs);
    }
}

