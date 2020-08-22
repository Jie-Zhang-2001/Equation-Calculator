import java.util.Scanner;
public class Calculator {
	public void printMenu() {
		System.out.println("[A] Add a new equation\n[F] Change equation from history"
				  + "\n[B] Print previous equation\n[P] Print full history\n[U] Undo\n"
				  + "[R] Redo\n[C] Clear history\n[Q] Quit\n");
	}
	public void printHeader() {
		System.out.println();
		System.out.println(String.format("%-5s%-40s%-40s%-40s%-10s%20s%20s", "#", 
				  "Equation", "Pre-Fix", "Post-Fix", "Answer", "Binary", "Hexadecimal"));
		System.out.println("---------------------------------------------------------------------------"
				+ "------------------------------------------------------------------------------------"
				+ "------------------");
	}
	public static void main(String[] args) {
		Calculator calc = new Calculator();
		HistoryStack hs = new HistoryStack();
		Scanner stdin = new Scanner(System.in);
		int positionToChange;
		Equation newEquation = new Equation();
		String changeOption,replaceWith,addOn;
		boolean quit = false,change = false;
		char choice;
		int changeEquation;
		String equation,modified,moreChange;
		do {
		try {
			calc.printMenu();
			System.out.print("Select an option: ");
			choice = stdin.next().charAt(0);
			choice = Character.toUpperCase(choice);
			switch(choice) {
			case 'A':
				stdin.nextLine();
				System.out.print("\nPlease enter an equation(in-fix notation): ");
				equation = stdin.nextLine();
				Equation e = new Equation(equation);
				hs.push(e);
				if(e.isBalanced()) {
					System.out.println("This equation is balanced and the answer is " + e.evaluate() + "\n");
				}else {
					System.out.println("This equation is not balanced.\n");
				}
				break;
			case'B':
				if(hs.size()>=1) {
					calc.printHeader();
					Equation pe = hs.peek();
					if(pe.isBalanced()) {
						System.out.println(String.format("%-5s%-40s%-40s%-40s%-10s%20s%20s", hs.size(), 
								pe.getEquation(), pe.getPrefix(), pe.getPostfix(pe.getEquation()), pe.evaluate(), pe.decToBin((int)Math.rint(pe.evaluate())), pe.decToHex((int)Math.rint(pe.evaluate()))));
					}else {
						System.out.println(String.format("%-5s%-40s%-40s%-40s%-10s%20s%20s", hs.size(), 
								pe.getEquation(), "N/A", "N/A", "0.0000", "0", "0"));
					}
					System.out.println();
				}else {
					System.out.println("\nNo hisotry\n");
				}
				break;
			case'P':
				calc.printHeader();
				System.out.println(hs.toString());
				break;
			case'F':
				System.out.print("\nWhich equation would you like to change? ");
				changeEquation = stdin.nextInt();
				System.out.println("\nEquation at position " + changeEquation + ":" + hs.getEquation(changeEquation).getEquation());
				newEquation = new Equation(hs.getEquation(changeEquation).getEquation());
				while(!change) {
					System.out.print("What would you like to do to the equation (Replace / remove / add)? ");
					changeOption = stdin.next();
					changeOption = changeOption.toLowerCase();
					switch(changeOption) {
					case"replace":
						System.out.print("What position would you like to change? ");
						positionToChange = stdin.nextInt();
						System.out.print("What would you like to replace it with? ");
						replaceWith = stdin.next();
						modified = newEquation.getEquation().substring(0,positionToChange-1) + replaceWith +  newEquation.getEquation().substring(positionToChange);
						newEquation.setEquation(modified);
						System.out.println("\nEquation: " + newEquation.getEquation());
						break;
					
					case"add":
						System.out.print("What position would you like to add something? ");
						positionToChange = stdin.nextInt();
						System.out.print("What would you like to add? ");
						addOn = stdin.next();
						modified = newEquation.getEquation().substring(0,positionToChange-1) + addOn +  newEquation.getEquation().substring(positionToChange-1); 
						newEquation.setEquation(modified);
						System.out.println("\nEquation: " + newEquation.getEquation());
						break;
					case"remove":
						System.out.print("What position would you like to remove? ");
						positionToChange = stdin.nextInt();
						modified = newEquation.getEquation().substring(0,positionToChange-1) + newEquation.getEquation().substring(positionToChange);
						newEquation.setEquation(modified);
						System.out.println("\nEquation: " + newEquation.getEquation());
						break;
					}
					System.out.print("Would you like to make more changes? ");
					moreChange = stdin.next();
					moreChange = moreChange.toLowerCase();
					if(moreChange.equals("n") || moreChange.equals("no")) {
						change = true;
						if(newEquation.isBalanced()) {
							System.out.println("The equation is balanced and the answer is: " + newEquation.evaluate() + "\n");
						}else {
							System.out.println("The equation is not balanced\n");
						}
						hs.push(newEquation);
					}
				}
				break;
			case'U':
				if(!hs.empty()) {
					hs.undo();
					System.out.println("\nEquation: '" + hs.getUndo().getEquation() + "' undone.\n");
				}else {
					System.out.println("\nThere's nothing to undo.\n");
				}
				break;
			case'R':
					hs.redo();
				break;
			case 'Q':
				quit = true;
				System.out.println("Program terminating normally....");
				break;
			case'C':
				hs.clear();
				System.out.println("\nResetting calculator.\n");
				break;
			default:
				System.out.println("Enter a valid choice!");
			}
		}catch(InvalidPositionException ex) {
			System.out.println(ex.getMessage());
		}catch(EquationNotBalancedException ex1) {
			System.out.println(ex1.getMessage());
		}catch(NoUnDoneException ex2) {
			System.out.println(ex2.getMessage());
		}
		} while(!quit);
		stdin.close();
		
	}
}
