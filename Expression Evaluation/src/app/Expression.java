package app;

import java.io.*;
import java.util.*;
import java.util.regex.*;

import structures.Stack;

@SuppressWarnings("unused")
public class Expression {

	public static String delims = " \t*+-/(/)\\[\\]";

	/**
	 * Populates the vars list with simple variables, and arrays lists with arrays
	 * in the expression. For every variable (simple or array), a SINGLE instance is
	 * created and stored, even if it appears more than once in the expression. At
	 * this time, values for all variables and all array items are set to zero -
	 * they will be loaded from a file in the loadVariableValues method.
	 * 
	 * @param expr
	 *            The expression
	 * @param vars
	 *            The variables array list - already created by the caller
	 * @param arrays
	 *            The arrays array list - already created by the caller
	 */
	public static void makeVariableLists(String expr, ArrayList<Variable> vars, ArrayList<Array> arrays) {
		/** COMPLETE THIS METHOD **/
		/**
		 * DO NOT create new vars and arrays - they are already created before being
		 * sent in to this method - you just need to fill them in.
		 **/
		StringTokenizer tokenMaker = new StringTokenizer(expr, delims);
		String[] tokens = new String[tokenMaker.countTokens()];

		int index = 0;
		while (tokenMaker.hasMoreTokens()) {
			tokens[index] = tokenMaker.nextToken();
			index++;
		}
		System.out.println("Equation: " + expr + " has been tokenized to: " + Arrays.toString(tokens));
		index = 0;
		for (int i = 0; i < tokens.length; i++) {
			String token = tokens[i];
			if (!token.equals("") && !(token.charAt(0) >= '0' && token.charAt(0) <= '9')) {
				index = expr.indexOf(token, index) + token.length();
				if (index < expr.length() && expr.charAt(index) == '[' && !arrays.contains(new Array(token))) {
					arrays.add(new Array(token));
				} else if (!vars.contains(new Variable(token))) {
					vars.add(new Variable(token));
				}
			} else {
				continue;
			}
		}
	}
	
	/**
	 * Loads values for variables and arrays in the expression
	 * 
	 * @param sc
	 *            Scanner for values input
	 * @throws IOException
	 *             If there is a problem with the input
	 * @param vars
	 *            The variables array list, previously populated by
	 *            makeVariableLists
	 * @param arrays
	 *            The arrays array list - previously populated by makeVariableLists
	 */
	public static void loadVariableValues(Scanner sc, ArrayList<Variable> vars, ArrayList<Array> arrays)
			throws IOException {
		while (sc.hasNextLine()) {
			StringTokenizer st = new StringTokenizer(sc.nextLine().trim());
			int numTokens = st.countTokens();
			String tok = st.nextToken();
			Variable var = new Variable(tok);
			Array arr = new Array(tok);
			int vari = vars.indexOf(var);
			int arri = arrays.indexOf(arr);
			if (vari == -1 && arri == -1) {
				continue;
			}
			int num = Integer.parseInt(st.nextToken());
			if (numTokens == 2) { // scalar symbol
				vars.get(vari).value = num;
			} else { // array symbol
				arr = arrays.get(arri);
				arr.values = new int[num];
				
				while (st.hasMoreTokens()) {
					tok = st.nextToken();
					StringTokenizer stt = new StringTokenizer(tok, " (,)");
					int index = Integer.parseInt(stt.nextToken());
					int val = Integer.parseInt(stt.nextToken());
					arr.values[index] = val;
				}
			}
		}
	}

	/**
	 * Evaluates the expression.
	 * 
	 * @param vars
	 *            The variables array list, with values for all variables in the
	 *            expression
	 * @param arrays
	 *            The arrays array list, with values for all array items
	 * @return Result of evaluation
	 */

	private static boolean operatorHasPrecedence(String op1, String op2) {
		if(op2.equals("(") || op2.equals("") || op2.equals("[") || op2.equals("]")) {
			return false;//only if op1 and op2 are d and m will there be any precedence problems
		}if ((op1.equals("*") || op1.equals("/"))&&(op2.equals("+") || op2.equals("-"))) {
			return false;
		}else {
			return true;
		}
	}
	 
	 private static float eshaansMethod(String operand, float b, float a) {
		 
		 if(operand.equals("+")) {
			 return a + b;
		 } else if(operand.equals("-")) {
			 return a - b;
		 } else if(operand.equals("*")) {
			 return a * b;
		 } else if(operand.equals("/")) {
			 if (b == 0) {
				 throw new UnsupportedOperationException("Cannot divide by zero");
			 }
			 return a / b;
		 }
		 return 0;
	 }
	 
	 private static boolean isTheStringNumeric(String expressions) {
		 try {
			 int num = Integer.parseInt(expressions);
		 }catch(NumberFormatException e) {
			 return false;
		 }
		 return true;
	 }
	public static float evaluate(String expr, ArrayList<Variable> vars, ArrayList<Array> arrays) {
		  
		
		  String[] expression_tokens = expr.split("(?<=["+delims+"])|(?=["+delims+"])");
		  Stack<Float> variables = new Stack<Float>();
		  Stack<String> operators = new Stack<String>();
		  variables.clear();
		  operators.clear();
	 
		  for (int i = 0; i < expression_tokens.length; i++) {
			  String tokens = expression_tokens[i];
			  if (tokens.equals(" ")) {
				  continue;
			  }

			  if(isTheStringNumeric(tokens)) {
				  variables.push(Float.parseFloat(tokens));
				  
			  }

			  else if(tokens.equals("(") || tokens.equals("[")) {
				  operators.push(tokens);
			  }
			  
		  
			  else if(tokens.equals(")")){
				  while(!(operators.peek().equals("("))){
					  if(operators.peek().equals("[")) {
						  operators.pop();
					  }else {
					  variables.push(eshaansMethod(operators.pop(), variables.pop(), variables.pop()));
					  }
				  }
				  if(!operators.isEmpty()) {
					  if(operators.peek().equals("(")) {
						  operators.pop();
					  }
				  }
			  }
		  
			  else if (tokens.equals("]")) {
				  while (!(operators.peek().equals("["))) {
					  {
					  variables.push(eshaansMethod(operators.pop(), variables.pop(), variables.pop()));
					  }
				  }
				  if(!operators.isEmpty()) {
				  if(operators.peek().equals("[")) {
					  operators.pop();
				  }
				  }
				  String ar_name = operators.peek();
				  int index = arrays.indexOf(new Array(ar_name));
				  if(index >-1) {
					  operators.pop();
					  Array a1 = arrays.get(index);
					  int index_a1 = (int)Math.round(variables.pop());
					  float val = a1.values[index_a1];
					  variables.push(val);
				  }
			  }
	 
		  else if(tokens.equals("+") || tokens.equals("-") || tokens.equals("*") || tokens.equals("/")) {
				  while(!operators.isEmpty() && operatorHasPrecedence(tokens, operators.peek())) {
					 {
					  variables.push(eshaansMethod(operators.pop(), variables.pop(), variables.pop()));
					  }

				  }
					  operators.push(tokens);
			  }
			  
			  else if(arrays.contains(new Array(tokens))) {
				  operators.push(tokens);
			  }
			  
			  else {
				  Variable var = new Variable(tokens);
				  if(vars.contains(var)) {
				  int indexOfVar = vars.indexOf(var);
				  float val = vars.get(indexOfVar).value;
				  variables.push(val);
			  }
		  }

    }	
		  
		  while(!operators.isEmpty() && variables.size() > 1) {
				 variables.push(eshaansMethod(operators.pop(), variables.pop(), variables.pop()));
			 }
			  
		  return (variables.pop());

}
}