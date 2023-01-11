package Start;

import Bean.GlobalVariable;
import java.io.IOException;
import Enum.*;
import Frame.*;

public class test {
	private static final MainWindow mainWindow = new MainWindow();
	private static final Function function = new Function();

	public static void main(String[] args) throws IOException {
		GlobalVariable globalVariable = new GlobalVariable();
		globalVariable.setFilePath(function.readConfig(0));
		mainWindow.showMainFrame(ModeTypeEnum.CREATE.getVal(),globalVariable);
	}
}

//156*654-984-471365654
//156*654-984
//156*654

//12342 - 12 - 34 -32
//12342 - 12 - 34
//12342 - 1
//12342 - 12

//这个 123- 123- 32 -324

//8585-555-526
//555-828-09
//112-36-43

//123 456 789
//123 456 789 137 347

//*4* 11x