package Function;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileFunction {
	public List<Object> openFile(String filePath) throws BiffException, IOException {
		List<Object> resultList = new ArrayList<>();

		File file = new File(filePath);

		Workbook wrb = Workbook.getWorkbook(file);
		Sheet readSheet = wrb.getSheet(0);

		WritableWorkbook workbook;
		try {
			workbook = Workbook.createWorkbook(file, wrb);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		WritableSheet writableSheet = workbook.getSheet(0);

		resultList.add(wrb);			//0
		resultList.add(readSheet);		//1
		resultList.add(workbook);		//2
		resultList.add(writableSheet);	//3
		resultList.add(file);			//4

		return resultList;
	}

	public void closeFile(List<Object> resultList) throws WriteException, IOException {
		Workbook wrb = (Workbook) resultList.get(0);
		WritableWorkbook workbook = (WritableWorkbook) resultList.get(2);

		wrb.close();
		workbook.close();
	}

	public String fileWarm(String filePath){
		return "请关闭 " + filePath + " 后重试";
	}
}
