package application.model;

import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.Stack;
import java.math.BigDecimal;

/**
 * This class parses the input and performs the calculation.
 * @author Patrick Armstrong
 */
public class Logic {
	
	/**
	 * Used to parse the input.
	 * The text input is converted into an array of elements in
	 * a postfix form.
	 */
	class Element {
		public String token;
		public cat category;
		public int precedence;
		
		// sets the category and precedence
		public Element(String token) {
			this.token = token;
			char ch = this.token.charAt(0);
			if (ch == '(') {
				this.category = cat.LPAREN;
				this.precedence = 0;
				return;
			} else if (ch == ')') {
				this.category = cat.RPAREN;
				this.precedence = 0;
				return;
			} else if ("+-".indexOf(ch) != -1) {
				this.category = cat.OPERATOR;
				this.precedence = 1;
				return;
			} else if ("*/".indexOf(ch) != -1) {
				this.category = cat.OPERATOR;
				this.precedence = 2;
				return;
			}
			this.category = cat.OPERAND;
			this.precedence = 0;
		}
	}

	private String text;
	private String token;
	private String result;
	
	enum cat {
		LPAREN, RPAREN, OPERATOR, OPERAND;
	}
	
	public Logic() {
		text = "";
	}

	/**
	 * Converts the input into an array of elements in postfix form.
	 * Then it evaluates the postfix expression.
	 * It also returns an error message if an error has occurred
	 * @return String result
	 */
	public String parseText() {
		ArrayList<Element> out = convertToPostfix();
		if (out.get(0).token.equals("missing_operator")) {
			result = out.get(1).token;
		} else if (out.get(0).token.equals("missing_operand")) {
			result = out.get(1).token;
		} else if (out.get(0).token.equals("missing_lparen")) {
			result = out.get(1).token;
		} else if (out.get(0).token.equals("missing_rparen")) {
			result = out.get(1).token;
		} else {
			result = evaluatePostfix(out);
		}
		return result;
	}
	
	/**
	 * This method coverts an infix expression to a postfix expression. It 
	 * return the output as an array of Elements in postfix form. It works by
	 * getting tokens from an input string and categorizing them. Then it
	 * puts them into the output array or pushes them onto the stack. At the
	 * end of the algorithm, all values remaining in the stack are popped and outed.
	 * @return ArrayList<Element> out
	 */
	private ArrayList<Element> convertToPostfix() {
		ArrayList<Element> out = new ArrayList<Element>();
		Stack<Element> stack = new Stack<Element>();
		Element popped;
		Boolean lastTokenIsOperand = false;
		// converts an expression like 2(3) into 2*(3) 
		String infix = insertAsterisks();
		token = "";
		// loops until the end of the input string
		while (infix.length() != 0) {
			// get the token from string
			infix = getToken(infix);
			Element e = new Element(token);
			if (e.category == cat.OPERAND) {
				// if the last token is an operand then an error has occurred
				if (lastTokenIsOperand) {
					out.add(0, new Element("Syntax Error: Expected operator, found operand, " + e.token));
					out.add(0, new Element("missing_operator"));
					return out;
				}
				out.add(e);
				lastTokenIsOperand = true;
			} else if (e.category == cat.OPERATOR) {
				// if the last token is an operator then an error has occurred
				if (!lastTokenIsOperand) {
					out.add(0, new Element("Syntax Error: Expected operand, found operator, " + e.token));
					out.add(0, new Element("missing_operand"));
					return out;
				}
				/*
				 * Compares the top stack element to the temp element. If the
				 * temp element has a higher precedence, push it onto the stack. 
				 * Otherwise, pop and out top element
				 */
				while (!stack.isEmpty()) {
					if (e.precedence > stack.lastElement().precedence)
						break;
					out.add(stack.pop());
				}
				stack.push(e);
				lastTokenIsOperand = false;
			} else if (e.category == cat.LPAREN) {
				stack.push(e);
			} else if (e.category == cat.RPAREN) {
				// if the last token is not an operand then an error has occurred
				if (!lastTokenIsOperand) {
					out.add(0, new Element("Syntax Error: Expected operand, found operator, " + e.token));
					out.add(0, new Element("missing_operand"));
					return out;
				}
				Boolean foundParen = false;
				// pop and out elements from the stack until a left parentheses is found
				while (!stack.isEmpty()) {
					popped = stack.pop();
					if (popped.category == cat.LPAREN) {
						foundParen = true;
						break;
					}
					out.add(popped);
				}
				// if a parentheses is not found than an error has occurred
				if (foundParen == false) {
					out.add(0, new Element("Syntax Error: missing left parentheses"));
					out.add(0, new Element("missing_lparen"));
					return out;
				}
			}
		}
		// pops and outs all remaining elements from the stack
		while(!stack.isEmpty()) {
			popped = stack.pop();
			if (popped.category == cat.LPAREN) {
				out.add(0, new Element("Syntax Error: missing right parentheses"));
				out.add(0, new Element("missing_rparen"));
				return out;
			}
			out.add(popped);
		}
		return out;
	}
	
	/**
	 * converts an expression like 2(3) into 2*(3)
	 * @return String correct.toString()
	 */
	private String insertAsterisks() {
		StringBuilder correct = new StringBuilder(String.valueOf(text.charAt(0)));
		for (int i = 1; i < text.length(); ++i) {
			char prev = text.charAt(i-1);
			//I'm sorry for this
			if ((text.charAt(i) == '(' && (prev >= '0' && prev <= '9'))
					|| (prev == ')' && ((text.charAt(i) >= '0' && text.charAt(i) <= '9') 
							|| prev == '(' || text.charAt(i) == '('))) {
				correct.append("*");
				correct.append(text.charAt(i));
			} else
				correct.append(text.charAt(i));
		}
		return correct.toString();
	}
	
	
	/**
	 * separates operands from operators
	 * @param infix
	 * @return String infix
	 */
	private String getToken(String infix) {
		token = "";
		String delims = "+-*/()";
		char c = infix.charAt(0);
		if (delims.indexOf(c) != -1) {
			token = String.valueOf(infix.charAt(0));
			return infix.substring(1);
		}
		for (char ch : infix.toCharArray()) {
			if (delims.indexOf(ch) != -1)
				return infix.substring(infix.indexOf(ch));
			else
				token = token + String.valueOf(ch);
		}
		return "";
	}
	
	/**
	 * Performs the calculation and return the result as an element.
	 * Also catches some errors.
	 * @param out
	 * @return Element stack.pop().token
	 */
	private String evaluatePostfix(ArrayList<Element> out) {
		Stack<Element> stack = new Stack<Element>();
		try {
			for (Element e : out) {
				if (e.category == cat.OPERAND)
					stack.push(e);
				else {
					Element second = stack.pop();
					Element first = stack.pop();
					switch (e.token) {
					case "+":
						stack.push(new Element(new BigDecimal(first.token).add(new BigDecimal(second.token)).toString()));
						break;
					case "-":
						stack.push(new Element(new BigDecimal(first.token).subtract(new BigDecimal(second.token)).toString()));
						break;
					case "*":
						stack.push(new Element(new BigDecimal(first.token).multiply(new BigDecimal(second.token)).toString()));
						break;
					case "/":
						stack.push(new Element(new BigDecimal(first.token).divide(new BigDecimal(second.token)).toString()));
						break;
				}
			}
		}
		} catch(NumberFormatException exception) {
			return "Error: too many decimal points";
		} catch (ArithmeticException exception) {
			return "Error: cannot divide by zero";
		} catch (EmptyStackException exception) {
			return "Syntax Error: missing operand at end of expression";
		}
		return stack.pop().token;
	}
	
	
	/**
	 * adds a char to end of text after button is clicked
	 * @param ch
	 * @return String text
	 */
	public String appendText(String ch) {
		text = text + ch;
		return text;
	}
	
	/**
	 * 
	 * @return String text
	 */
	public String getText() {
		return text;
	}
	
	/**
	 * 
	 * @param str
	 */
	public void setText(String str) {
		text = str;
	}

	/**
	 * 
	 * @return String result
	 */
	public String getResult() {
		return result;
	}

	/**
	 * 
	 * @param result
	 */
	public void setResult(String result) {
		this.result = result;
	}
}
