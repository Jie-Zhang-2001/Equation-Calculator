import java.util.Stack;

public class HistoryStack extends Stack<Equation> {
	private Equation undo;
	private int count;

	public HistoryStack() {

	} 
	public Equation push(Equation newEquation) { 
		  super.push(newEquation);
		  return newEquation;}
	  
	  
	  public Equation peek() { return super.peek(); }
	  
	  public Equation pop() { return super.pop(); }
	  
	 
	  public int size() {
		  return super.size();
	  }
	public void undo() {
		undo = this.pop();
	}

	public Equation getUndo() {
		return undo;
	}

	public void redo() throws NoUnDoneException {
		if (undo == null) {
			throw new NoUnDoneException();
		}
		this.push(undo);
		System.out.println("\nRedoing equation: '" + undo.getEquation() + "'\n");
		undo = null;
	}

	public Equation getEquation(int position) throws InvalidPositionException {
		if (position <= 0 || position > this.size()) {
			throw new InvalidPositionException();
		} else {
			return this.get(position - 1);
		}
	}

	public void clear() {
		while (!this.empty()) {
			undo();
		}
		undo = null;
	}

	public String toString() {
		String x = "";
		try {
			count = this.size();
			while (count > 0) {
				Equation item = this.get(count - 1);
				if (item.isBalanced()) {
					x += String.format("%-5s%-40s%-40s%-40s%-10s%20s%20s", count, item.getEquation(), item.getPrefix(),
							item.getPostfix(item.getEquation()), item.evaluate(),
							item.decToBin((int) Math.rint(item.evaluate())),
							item.decToHex((int) Math.rint(item.evaluate())));
					x += "\n";
					count--;
				} else {
					x += (String.format("%-5s%-40s%-40s%-40s%-10s%20s%20s", count, item.getEquation(), "N/A", "N/A",
							"0.0000", "0", "0"));
					x += "\n";
					count--;
				}
			}
		} catch (EquationNotBalancedException ex) {
			System.out.println(ex.getMessage());
		}
		return x;
	}

}
