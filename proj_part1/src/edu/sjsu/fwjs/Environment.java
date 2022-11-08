package edu.sjsu.fwjs;

import java.util.Map;

import javax.management.RuntimeErrorException;

import java.util.HashMap;

public class Environment {
    private Map<String,Value> env = new HashMap<String,Value>();
    private Environment outerEnv;

    /**
     * Constructor for global environment
     */
    public Environment() {}

    /**
     * Constructor for local environment of a function
     */
    public Environment(Environment outerEnv) {
        this.outerEnv = outerEnv;
    }

    /**
     * Handles the logic of resolving a variable.
     * If the variable name is in the current scope, it is returned.
     * Otherwise, search for the variable in the outer scope.
     * If we are at the outermost scope (AKA the global scope)
     * null is returned (similar to how JS returns undefined.
     */
    public Value resolveVar(String varName) {
        // YOUR CODE HERE
    	Value var = null;
    	Environment currentEnv = this;
    	
    	// check if var is in any scope until it is found
    	while((var = currentEnv.env.get(varName)) == null &&
    			currentEnv.outerEnv != null) {
    		// var not found and hasn't reached global scope
    		// reach outer environment scope
    		currentEnv = currentEnv.outerEnv;
    	}
    	
    	// if var not found it returns NullVal 
    	if(var == null) {
    		return new NullVal();
    	}
    	
        return var;
    	
    }

    /**
     * Used for updating existing variables.
     * If a variable has not been defined previously in the current scope,
     * or any of the function's outer scopes, the var is stored in the global scope.
     */
    public void updateVar(String key, Value v) {
        // YOUR CODE HERE
    	Environment scope = this;
        Value oldv = null;
        //search all scopes for variable
    	while ((scope.outerEnv != null) && (oldv == null)) {
            scope = scope.outerEnv;
            oldv=scope.env.get(key);
        }
        //if not found, create new variable at outermost scope
        if (oldv == null){
            scope.env.put(key, v);
        }
        //if found, replace w new value
        else {
            scope.env.replace(key, oldv, v);
        }

    }

    /**
     * Creates a new variable in the local scope.
     * If the variable has been defined in the current scope previously,
     * a RuntimeException is thrown.
     */
    public void createVar(String key, Value v) {
        // YOUR CODE HERE
    	if (this.env.containsKey(key)) {
    		throw new RuntimeException(); 
    	}
    	else {
    		this.env.put(key, v);
    	}
    }
}
