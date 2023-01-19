package Start;

import Bean.GlobalVariable;
import Frame.*;
import Function.InputFunction;

import java.io.IOException;

public class query {
	private static final QueryWindow queryWindow = new QueryWindow();
	private static final InputFunction INPUT_FUNCTION = new InputFunction();

	public static void main(String[] args) throws IOException {
		GlobalVariable globalVariable = new GlobalVariable();
		globalVariable.setFilePath(INPUT_FUNCTION.readConfig(0));
		queryWindow.showQueryWindow(globalVariable);
	}
}
