package Function;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class NoteFunction {
	public List<String> readNoteExcel(String filePath){
		String path = filePath + "备注.xlsx";
		Workbook wrb;
		try {
			wrb = Workbook.getWorkbook(new File(path));
		} catch (IOException | BiffException e) {
			e.printStackTrace();
			return null;
		}

		Sheet sheet = wrb.getSheet(0);
		List<String> noteList = new ArrayList<>();
		for (int i = 0; i < sheet.getRows(); i++) {
			String note = sheet.getCell(0, i).getContents().trim();
			if(!note.equals("")){
				noteList.add(note);
			}
		}
		return noteList;
	}
}
