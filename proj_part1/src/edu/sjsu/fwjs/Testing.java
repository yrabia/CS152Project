package edu.sjsu.fwjs;

import java.util.ArrayList;
import java.util.List;

public class Testing {
	public static void main(String[] args) {
		
		Environment env = new Environment();
	    List<String> params = new ArrayList<String>();
	    params.add("x");
	    params.add("y");
	    FunctionDeclExpr f = new FunctionDeclExpr(params,
	            new BinOpExpr(Op.DIVIDE,
	                    new VarExpr("x"),
	                    new VarExpr("y")));
	    List<Expression> argm = new ArrayList<Expression>();
	    argm.add(new ValueExpr(new IntVal(8)));
	    argm.add(new ValueExpr(new IntVal(2)));
	    FunctionAppExpr app = new FunctionAppExpr(f,argm);
	    Value result = app.evaluate(env);
	    System.out.print(result);
}
}
