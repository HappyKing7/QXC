package Function;

import Bean.*;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class UpdateExcelFunction {
	private static final InputFunction inputFunction = new InputFunction();

	public String updateExcel(String filePath, GlobalVariable globalVariable) throws BiffException, IOException, WriteException {
		Map<String,UpdateExcel> updateExcelMap = globalVariable.updateExcelMap;
		File file = new File(filePath);

		Workbook wrb = Workbook.getWorkbook(file);
		Sheet readSheet = wrb.getSheet(0);

		WritableWorkbook workbook;
		try {
			workbook = Workbook.createWorkbook(file, wrb);
		} catch (IOException e) {
			e.printStackTrace();
			return e.getMessage();
		}
		WritableSheet writableSheet = workbook.getSheet(0);

		for (int i = 0; i < readSheet.getRows(); i++) {
			String no = readSheet.getCell(0,i).getContents();
			UpdateExcel updateExcel = updateExcelMap.get(no);
			if (no.equals("") || updateExcel == null)
				continue;

			updateExcelDate(writableSheet,1,i+updateExcel.getTowNo(),updateExcel.getSs().getSerialNumber());
			updateExcelDate(writableSheet,2,i+updateExcel.getTowNo(),updateExcel.getSs().getDetail());
			updateExcelDate(writableSheet,3,i,updateExcel.getTotalPrice());
			updateExcelDate(writableSheet,4,i,updateExcel.getNote());
			updateExcelDate(writableSheet,6,0,updateExcel.getTotalMoney());
		}

		workbook.write();
		workbook.close();
		wrb.close();
		return "success";
	}

	public void updateExcelDate(WritableSheet writableSheet,Integer column,Integer row,String context)
			throws WriteException {
		Label l =  new Label(column,row,context);
		writableSheet.addCell(l);
	}

	public String deleteExcel(String filePath, Integer selectOneNo, Integer selectTwoNo, List<ShowSummaryList> showSummaryLists)
			throws BiffException, IOException, WriteException {
		ShowSummaryList fssl = showSummaryLists.get(0);
		ShowSummaryList ssl = showSummaryLists.get(selectOneNo);
		ShowSummary ss = ssl.getShowSummaryList().get(selectTwoNo);

		String no = ssl.getNo();
		String[] details = ss.getDetail().split(",");
		String totalMoney = String.format("%.2f", inputFunction.moneyRemoveChinese(fssl.getTotalMoney(),"总计","元")
				- inputFunction.moneyRemoveChinese(details[4],"总","元"));
		String totalPrice = String.format("%.2f", inputFunction.moneyRemoveChinese(ssl.getTotalPrice(),"总共","元")
				- inputFunction.moneyRemoveChinese(details[4],"总","元"));
		String note = ssl.getNote();
		String serialNumber = ss.getSerialNumber();

		File file = new File(filePath);

		Workbook wrb = Workbook.getWorkbook(file);
		Sheet readSheet = wrb.getSheet(0);

		WritableWorkbook workbook;
		try {
			workbook = Workbook.createWorkbook(file, wrb);
		} catch (IOException e) {
			e.printStackTrace();
			return e.getMessage();
		}
		WritableSheet writableSheet = workbook.getSheet(0);

		int changeNoFlag = 0;
		for (int i = 0; i < readSheet.getRows(); i++) {
			String excelNo = readSheet.getCell(0, i).getContents();
			if (excelNo.equals("") || !excelNo.equals(ssl.getNo()))
				continue;

			//是否序列号下第一行
			if (selectTwoNo == 0) {
				//是否删除整个序列号
				if (ssl.getShowSummaryList().size() != 1) {
					updateExcelDate(writableSheet, 0, i + selectTwoNo + 1, no);
					updateExcelDate(writableSheet, 4, i + selectTwoNo + 1, note);
					updateExcelDate(writableSheet, 3, i + selectTwoNo + 1, inputFunction.moneyAddChinese(totalPrice, "总共", "元"));
				}
				{
					changeNoFlag = i;
				}
			} else {
				updateExcelDate(writableSheet, 3, i, inputFunction.moneyAddChinese(totalPrice, "总共", "元"));
			}
			writableSheet.removeRow(i + selectTwoNo);
			if (ssl.getShowSummaryList().size() == 1)
				writableSheet.removeRow(i + selectTwoNo);
			updateExcelDate(writableSheet, 6, 0, inputFunction.moneyAddChinese(totalMoney, "总计", "元"));

		}

		if(ssl.getShowSummaryList().size() == 1){
			//修改序列号
			for (int i = changeNoFlag; i < writableSheet.getRows(); i++) {
				String excelNo = writableSheet.getCell(0, i).getContents();
				if (excelNo.equals(""))
					continue;

				excelNo = String.valueOf(Integer.parseInt(excelNo.replace("序列",""))-1);
				updateExcelDate(writableSheet,0,i,"序列"+excelNo);
			}
		}


		workbook.write();
		workbook.close();
		wrb.close();

		if(totalMoney.equals("0.00")){
			if(file.delete())
				return "delete success";
			else
				return "delete fail";
		}
		return "update success";
	}
}
