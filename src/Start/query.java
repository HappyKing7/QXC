package Start;

import Bean.GlobalVariable;
import Frame.*;

import java.io.IOException;

public class query {
	private static final QueryWindow queryWindow = new QueryWindow();
	private static final Function function = new Function();

	public static void main(String[] args) throws IOException {
		GlobalVariable globalVariable = new GlobalVariable();
		globalVariable.setFilePath(function.readConfig(0));
		queryWindow.showQueryWindow(globalVariable);
	}
}
