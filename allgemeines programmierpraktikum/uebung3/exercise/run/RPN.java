package uebung3.exercise.run;

import java.util.Stack;
import uebung3.exercise.algebra.number.NormRational;

public class RPN {

	public static void main(String[] args) throws Exception {
		if (args.length < 1) {
			return;
		}
		
		Stack<NormRational> stack = new Stack<NormRational>();
		
		for (int i = 0; i < args.length; ++i) {
			String parse = args[i];
			
			// eliminate whitespaces
			parse.trim();
			
			if (parse.length() < 1)
				continue;
			
			if (parse.length() == 1) {
				char c = parse.charAt(0);
				
				// alternative for * because some shells interprete *
				if (c == '&')
					c = '*';

				if (c == '+' || c == '-' || c == '/' || c == '*') {
					if (stack.size() < 2)
						throw new Exception("Malformed input, missing arguments");
					
					NormRational R = stack.pop();
					NormRational L = stack.pop();
					
					// evaluate operation, push result
					stack.push(eval(L, R, c));
				}
				else if (Character.isDigit(c))
					stack.push(new NormRational(Long.parseLong(parse), 1));
				else
					throw new Exception("Can't parse character: " + c);
			}
			else {
				Long L = Long.parseLong(parse);
				
				// no negative numbers
				if (L < 0)
					throw new Exception("Signed integers not allowed");
				
				stack.push(new NormRational(L, 1));
			}
		}
		
		if (stack.size() != 1)
			throw new Exception("Malformed input, missing operands");
		
		System.out.println("Result: " + stack.pop());
	}
	
	private static NormRational eval(NormRational L, NormRational R, char op) throws Exception {
		switch (op) {
			case '+':
				return NormRational.add(L, R);
			case '-':
				return NormRational.sub(L, R);
			case '*':
				return NormRational.mult(L, R);
			case '/':
				return NormRational.div(L, R);
			default:
				throw new Exception("Can't evaluate character: " + op);
		}
	}
}
