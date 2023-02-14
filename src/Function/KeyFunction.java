package Function;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KeyFunction {
	public List<Map<String,String>> readKeyExcel(String filePath) {
		String path = filePath + "关键字.xlsx";
		Workbook wrb;
		try {
			wrb = Workbook.getWorkbook(new File(path));
		} catch (IOException | BiffException e) {
			e.printStackTrace();
			return null;
		}

		List<Map<String,String>> mapList = new ArrayList<>();
		for (int i = 0; i < 4; i++) {
			Sheet sheet = wrb.getSheet(i);
			Map<String,String> KEY_MAP = new HashMap<>();
			for (int j = 0; j < sheet.getRows(); j++) {
				String key = sheet.getCell(0, j).getContents().trim();
				for (int k = 1; k < sheet.getRow(j).length; k++) {
					KEY_MAP.put(sheet.getRow(j)[k].getContents(),key);
				}
			}
			mapList.add(KEY_MAP);
		}

		return mapList;
	}
}
