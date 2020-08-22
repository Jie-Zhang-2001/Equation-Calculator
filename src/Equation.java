import java.util.Stack;
public class Equation extends EquationStack{
	private String equation;
	private String prefix= "";
	private String postfix = "";
	private String binary="";
	private String hex = "";
	private boolean balanced;
	
	public Equation() {
		
	}
	
	public Equation(String equation) {
		this.equation = equation;
	}
	
	public void setEquation(String x) {
		equation = x;
	}
	public String getEquation() {
		return equation;
	}
	public int prec(char x) {
		if(x == '$') {
			return 0;
		}else if(x == '('){
			return 1;
		}else if(x == '+' || x == '-') {
			return 2;
		}else {
			return 3;
		}
	}
	
	public boolean isOp(char i) {
		if(i == '+' || i == '-' || i == '*' ||
				  i == '/' || i == '%') {
			return true;
		}
		return false;
	}
	public String getPostfix(String equation) {
		postfix = "";
		char topOp;
		Stack<Character> OpStack = new Stack<>();
		OpStack.push('$');
		for(int i = 0; i < equation.length();i++) {
			if(Character.isDigit(equation.charAt(i))) {
				postfix += equation.charAt(i);
			}
			if(equation.charAt(i) == '(') {
				if(i != 0 && !isOp(OpStack.peek()) && OpStack.peek() != '(') {
					postfix += " ";
				}
				OpStack.push(equation.charAt(i));
			}
			if(equation.charAt(i) == ')') {
				postfix += " ";
				topOp = OpStack.pop();
				while(topOp != '(') {
					postfix += topOp;
					topOp = OpStack.pop();
				}
			}
			if(isOp(equation.charAt(i))) {
				postfix += " ";
				topOp = OpStack.peek();
				while(prec(topOp) >= prec(equation.charAt(i))) {
					postfix +=  OpStack.pop();
					topOp = OpStack.peek();
				}
				OpStack.push(equation.charAt(i));
			}
		}
		topOp = OpStack.pop();
		while(topOp != '$') {
			postfix += " " + topOp;
			topOp = OpStack.pop();
		}
		return postfix;
	}
	
	public String getPrefix() {
		String rev = "";
		prefix = "";
		for(int i = equation.length()-1; i >= 0; i--) {
			if(equation.charAt(i) == '(') {
				rev += ')';
			}else if(equation.charAt(i) == ')') {
				rev += '(';
			}else {
				rev += equation.charAt(i);
			}
		}
	  String pre = this.getPostfix(rev); 
	  for(int i = pre.length()-1; i >= 0; i--) {
		  prefix += pre.charAt(i); } 
	  return prefix; 
	}

	 
	public boolean isBalanced() {
		EquationStack Parentheses = new EquationStack();
		int i = 0;
		while(i < equation.length()) {
			if(equation.substring(i,i+1).equals("(")) {
				Parentheses.push("(");
				i++;
			}else if(equation.substring(i,i+1).equals(")")){
				if(Parentheses.empty()) {
					balanced = false;
					return balanced;
				}
				Parentheses.pop();
				i++;
			}else {
				i++;
			}
		}
		if(Parentheses.empty()) {
			balanced = true;
		}
		return balanced;
	}
	
	public double evaluate() {
		EquationStack OperandStack = new EquationStack();
		double value = 0,operand1,operand2,increment;
		int index,count = 0;
		this.getPostfix(this.equation);
		for(int i = 0; i < postfix.length(); i++) {
			if(Character.isDigit(postfix.charAt(i))){
				if(Character.isDigit(postfix.charAt(i+1))){
					count = 0;
					index = i+1;
					while(index < postfix.length() && postfix.charAt(index) != ' ') {
						count++;
						index++;
					}
					index = i;
					increment = count;
					while(count >= 0) {
						value += ((double) postfix.charAt(index) - (double) '0') * Math.pow(10, count) ;
						index++;
						count--;
					}
					OperandStack.push(value);
					value = 0;
					i += increment;
				}else {
					value = (double) postfix.charAt(i) - (double) '0';
					OperandStack.push(value);
					value = 0;
					
				}
			}else if(postfix.charAt(i) == ' '){
			}else {
				operand2 = (double) OperandStack.pop();
				operand1 = (double) OperandStack.pop();
				if(postfix.charAt(i) == '+')
					OperandStack.push(operand1 + operand2);
				else if(postfix.charAt(i) == '-')
					OperandStack.push(operand1 - operand2);
				else if(postfix.charAt(i) == '*')
					OperandStack.push(operand1 * operand2);
				else if(postfix.charAt(i) =='%')
					OperandStack.push(operand1 % operand2);
				else if(postfix.charAt(i) == '/') {
					if (operand2 == 0) 
						throw new ArithmeticException();
					OperandStack.push(operand1 / operand2);		
				}
			}
		}
		return Math.round((double)OperandStack.pop()*1000.0) /1000.0;
	}
	
	public String decToBin(int number) throws EquationNotBalancedException{
		binary = "";
		while(number > 0) {
			binary = number%2 + binary;
			number /= 2;
		}
		return binary;
	}
	
	public String decToHex(int number) throws EquationNotBalancedException {
		if(!this.isBalanced()) {
			throw new EquationNotBalancedException();
		}
		hex = "";
		while(number > 0) {
			if(number % 16 >= 10) {
				hex = String.valueOf((char)(55 + number%16)) + hex;
				number /= 16;
			}else {
				hex = number % 16 + hex;
				number/= 16;
				}
			}
		return hex;
	}
	
}
