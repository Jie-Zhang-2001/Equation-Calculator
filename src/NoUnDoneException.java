
public class NoUnDoneException extends Exception {
	public NoUnDoneException() {
		super("\nThere's nothing to redo.\n");
	}
}
