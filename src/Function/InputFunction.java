package Function;

import Bean.*;
import Enum.*;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

import javax.swing.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;

public class InputFunction {
	private static final Pattern pattern = Pattern.compile("-?[0-9]+(\\\\.[0-9]+)?");

	public Boolean filterTpye(String type){
		if(type.equals(TypeEnum.ALL.getLabel())){
			return true;
		}
		if(type.contains(TypeEnum.ZL.getLabel()) && type.length()>2){
			if(!type.contains("全包"))
				return true;
		}
		if(type.contains(TypeEnum.ZS.getLabel()) && type.length()>2){
			if(!type.contains("全包"))
				return true;
		}
		if(type.contains(TypeEnum.KD.getLabel()) && type.length()>2){
			return true;
		}
		if(type.contains(TypeEnum.HZ.getLabel()) && type.length()>2){
			return true;
		}
		if(type.contains(TypeEnum.FS.getLabel()) && type.length()>2){
			return true;
		}

		return false;
	}
	public List<String> getNumber(String serialNumber,String functionType){
		List<String> numberList = new ArrayList<>();
		StringBuilder number = new StringBuilder();

		for (int i = 0; i < serialNumber.length(); i++) {
			if (Character.isDigit(serialNumber.charAt(i))) {
				number.append(serialNumber.charAt(i));
			}else{
				if (!number.toString().equals("")){
					numberList.add(number.toString());
					number = new StringBuilder();
				}
			}

			if(i==serialNumber.length()-1 && !number.toString().equals("")){
				numberList.add(number.toString());
			}
		}

		if(functionType.equals("组合")){
			int groupNum = 1;
			for(String num : numberList){
				groupNum = groupNum * num.length();
			}
			numberList.add(String.valueOf(groupNum));
			return numberList;
		}

		numberList.add(String.valueOf(numberList.size()));
		return numberList;
	}

	public String getNumber(TicketList tickets, AtomicInteger alllistNo, AtomicInteger ticketsNo, String serialNumber,
							String type, String typeTwo, String inputPrice, String times, String functionType){
		if (serialNumber.equals("") || !pattern.matcher(times).matches()){
			return "fail";
		}

		List<String> numberList = new ArrayList<>();
		StringBuilder number = new StringBuilder();

		if(functionType.equals(FunctionType.DINGWEI.getLabel())){
			String[] serialNumbers;
			if(serialNumber.contains(" ")){
				serialNumbers = serialNumber.split(" ");
			}else if(serialNumber.contains(",")){
				serialNumbers = serialNumber.split(",");
			}else if(serialNumber.contains(".")){
				serialNumbers = serialNumber.split("\\.");
			}else if(serialNumber.contains("，")){
				serialNumbers = serialNumber.split("，");
			}else if(serialNumber.contains("。")){
				serialNumbers = serialNumber.split("。");
			}else {
				serialNumbers = serialNumber.split(" ");
			}
			for (String s : serialNumbers) {
				String oneSerialNumber = s;
				oneSerialNumber = oneSerialNumber.replaceAll("[^\\d]", "*");
				numberList.add(oneSerialNumber);
			}
		}else {
			for (int i = 0; i < serialNumber.length(); i++) {
				if (Character.isDigit(serialNumber.charAt(i))) {
					number.append(serialNumber.charAt(i));
				}else{
					if (!number.toString().equals("")){
						numberList.add(number.toString());
						number = new StringBuilder();
					}
				}

				if(i==serialNumber.length()-1 && !number.toString().equals("")){
					numberList.add(number.toString());
				}
			}
		}

		if (functionType.equals(FunctionType.ZUHE.getLabel())) {
			int j = 0;
			List<String> addOneNum = new ArrayList<>();
			List<String> addTwoNum = new ArrayList<>();
			while (j + 1 < numberList.size()) {
				if (j == 0) {
					String oneString = numberList.get(j);
					String twoString = numberList.get(j + 1);
					for (int k = 0; k < oneString.length(); k++) {
						for (int l = 0; l < twoString.length(); l++) {
							addOneNum.add(oneString.charAt(k) + String.valueOf(twoString.charAt(l)));
						}
					}
				} else {
					String thirdString = numberList.get(j + 1);
					for (String s : addOneNum) {
						for (int l = 0; l < thirdString.length(); l++) {
							addTwoNum.add(s + thirdString.charAt(l));
						}
					}
					addOneNum.clear();
					addOneNum.addAll(addTwoNum);
					addTwoNum.clear();
				}
				j = j + 1;
			}
			numberList = addOneNum;
		}

		StringBuilder serialNumberBuilder = new StringBuilder();
		for (String s : numberList) {
			serialNumberBuilder.append(s).append(" ");
		}
		serialNumber = serialNumberBuilder.toString();

		int group = numberList.size();
		float price = Float.parseFloat(inputPrice) * Integer.parseInt(times);

		tickets.setId(alllistNo.getAndIncrement());

		Ticket ticket = new Ticket();
		ticket.setId(ticketsNo.getAndIncrement());
		ticket.setSerialNumber(serialNumber);
		ticket.setGroupNum(group);
		ticket.setTimes(Integer.valueOf(times));
		ticket.setType(getType(type,serialNumber));
		ticket.setTypeTwo(typeTwo);
		ticket.setUnitPrice(price);
		ticket.setTotalPrice(group*price);

		if(tickets.getTicketList() == null){
			List<Ticket> ticketList = new ArrayList<>();
			ticketList.add(ticket);
			tickets.setTicketList(ticketList);
		}else{
			tickets.getTicketList().add(ticket);
		}

		return showSummaryWithNumber(ticket);
	}

	public String numberWrap(String serialNumber,String splitStr, int num){
		StringBuilder output = new StringBuilder("<html>");
		String[] serialNumbers = serialNumber.split(splitStr);
		for (int i = 0; i < serialNumbers.length; i++) {
			output.append(serialNumbers[i]).append(" ");
			if(((i+1) % num) == 0){
				output.append("<br>");
			}
		}
		output.append("</html>");
		return output.toString();
	}

	public String showSummaryWithNumber(Ticket ticket){
		String output = ticket.getSerialNumber();
		if(ticket.getSerialNumber().length() <= 145){
			output = ticket.getSerialNumber();
		}else{
			output = numberWrap(output," ",10);
		}

		output = output + "-";
		output = output + "(";
		output = output + ticket.getGroupNum()+"组";
		output = output + ",";
		output = output + "单价"+ticket.getUnitPrice();
		output = output + ",";
		output = output + ticket.getTypeTwo();
		output = output + ",";
		output = output + ticket.getType();
		output = output + ",";
		output = output + "总"+ticket.getTotalPrice()+"元";
		output = output + ")";

		return output;
	}

	public String showSummaryWithoutNumber(Ticket ticket){
		String output = ticket.getGroupNum()+"组";
		output = output + ",";
		output = output + "单价"+ticket.getUnitPrice();
		output = output + ",";
		output = output + ticket.getTypeTwo();
		output = output + ",";
		output = output + ticket.getType();
		output = output + ",";
		output = output + "总"+ticket.getTotalPrice()+"元";
		return output;
	}

	public void createExcel(String filePath,TicketList tickets) throws IOException, WriteException {
		FileOutputStream os=new FileOutputStream(filePath);
		WritableWorkbook workbook = Workbook.createWorkbook(os);
		WritableSheet sheet = workbook.createSheet("汇总", 0);
		Label l0 = new Label(0, 0, "序列1");
		sheet.addCell(l0);
		float totalPrice = 0;
		for (int i = 0; i < tickets.getTicketList().size(); i++) {
			Ticket ticket = tickets.getTicketList().get(i);
			Label l1 = new Label(1, i, ticket.getSerialNumber());
			Label l2 = new Label(2, i, showSummaryWithoutNumber(ticket));
			totalPrice = totalPrice + ticket.getTotalPrice();
			sheet.addCell(l1);
			sheet.addCell(l2);
		}
		Label l3 = new Label(3,0,"总共"+ totalPrice +"元");
		Label l4 = new Label(4,0,"备注："+ tickets.getNote());
		Label l6 = new Label(6,0,"总计"+ totalPrice +"元");
		sheet.addCell(l3);
		sheet.addCell(l4);
		sheet.addCell(l6);

		workbook.write();
		workbook.close();
		os.close();
	}

	public String addExcel(Workbook wrb,File file,TicketList tickets) throws WriteException, IOException {
		Sheet readSheet = wrb.getSheet(0);
		int sheetRow = readSheet.getRows();
		String numberNo = "";
		for (int i = sheetRow; i > 0; i--) {
			String no = readSheet.getCell(0,i-1).getContents();

			if(!no.equals("")){
				no =no.split("序列")[1];
				numberNo = no;
				break;
			}
		}

		WritableWorkbook workbook;
		try {
			workbook = Workbook.createWorkbook(file, wrb);
		} catch (IOException e) {
			e.printStackTrace();
			return e.getMessage();
		}
		WritableSheet writableSheet = workbook.getSheet(0);
		Label l0 = new Label(0,sheetRow+1,"序列"+ (Integer.parseInt(numberNo) + 1));
		writableSheet.addCell(l0);
		float totalPrice = 0;
		int ticketsNo = 0;
		for (int i = sheetRow+1; i < sheetRow + 1 + tickets.getTicketList().size(); i++) {
			Ticket ticket = tickets.getTicketList().get(ticketsNo);
			Label l1 = new Label(1,i,ticket.getSerialNumber());
			Label l2 = new Label(2,i,showSummaryWithoutNumber(ticket));
			totalPrice = totalPrice + ticket.getTotalPrice();
			writableSheet.addCell(l1);
			writableSheet.addCell(l2);
			ticketsNo = ticketsNo + 1;
		}
		Label l3 = new Label(3,sheetRow+1,"总共" + totalPrice +"元");
		Label l4 = new Label(4,sheetRow+1,"备注：" + tickets.getNote());
		String totalMoney =  readSheet.getCell(6,0).getContents().replace("总计","").replace("元","");
		totalMoney = String.valueOf((Float.parseFloat(totalMoney) + totalPrice));

		Label l6 = new Label(6,0,"总计"+ totalMoney + "元");
		writableSheet.addCell(l3);
		writableSheet.addCell(l4);
		writableSheet.addCell(l6);

		workbook.write();
		workbook.close();
		wrb.close();
		return "成功";
	}

	public String readConfig(int i) throws IOException {
		Path path = Paths.get("C:\\config.txt");
		List<String> lines = Files.readAllLines(path);
		return lines.get(i);
	}

	public String sumbit(String filePath,TicketList tickets,String fileName) throws IOException, WriteException, BiffException {
		SimpleDateFormat sf= new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date();
		String nowDate= sf.format(date);

		if(fileName.equals("")){
			filePath =filePath + nowDate + ".xlsx";
		}else {
			filePath =filePath + fileName + ".xlsx";
		}

		if (tickets.getNote() == null){
			tickets.setNote("");
		}
		File file = new File(filePath);
		if(file.exists()&&file.isFile()){
			Workbook wrb = Workbook.getWorkbook(file);
			if(wrb.getSheet("汇总") == null) {
				wrb.close();
				createExcel(filePath,tickets);
			}else{
				String result = addExcel(wrb,file,tickets);
				if(!result.equals("成功")){
					return result;
				}
			}
		}else{
			createExcel(filePath,tickets);
		}
		return "成功";
	}

	public List<ShowSummaryList> readTodayExcel(String filePath, String fileName){
		String path = filePath + fileName;
		Workbook wrb;
		try {
			wrb = Workbook.getWorkbook(new File(path));
		} catch (IOException | BiffException e) {
			e.printStackTrace();
			return null;
		}
		Sheet sheet = wrb.getSheet(0);
		List<ShowSummaryList> showSummaryList = new ArrayList<>();
		List<ShowSummary> showSummaries = new ArrayList<>();
		String serialNo = "";
		String totalPrices = "" ;
		String excelNote = "" ;
		int size = 0;
		for (int i = 0; i < sheet.getRows(); i++) {
			ShowSummary showSummary = new ShowSummary();
			String no = sheet.getCell(0, i).getContents().trim();
			String serialNumber = sheet.getCell(1, i).getContents().trim();
			String detail = sheet.getCell(2, i).getContents().trim();
			String totalPrice = sheet.getCell(3, i).getContents().trim();
			String note = sheet.getCell(4, i).getContents().trim();

			if(!no.equals("")) {
				serialNo = no;
			}
			if(!totalPrice.equals("")){
				totalPrices = totalPrice;
			}
			if(!note.equals("")) {
				excelNote = note;
			}

			if(!detail.equals(""))
			{
				showSummary.setSerialNumber(serialNumber);
				showSummary.setDetail(detail);
				showSummaries.add(showSummary);
			}

			if(detail.equals("") || i==(sheet.getRows()-1)){
				ShowSummaryList summaryList = new ShowSummaryList();
				summaryList.setNo(serialNo);
				summaryList.setShowSummaryList(showSummaries);
				summaryList.setTotalPrice(totalPrices);
				summaryList.setNote(excelNote);
				showSummaryList.add(summaryList);
				showSummaries = new ArrayList<>();
				serialNo = "";
				totalPrices = "";
				excelNote = "";
				size = size + 1;
			}
		}

		showSummaryList.get(0).setSize(sheet.getRows() - size + 1);
		showSummaryList.get(0).setTotalMoney(sheet.getCell(6, 0).getContents().trim());
		return showSummaryList;
	}

	public void setPrice(GlobalVariable globalVariable,String firstNumber, List<JRadioButton> priceList,JTextField priceJF){
		String price = null;
		for(JRadioButton jRadioButton : priceList){
			if(firstNumber.length() == 3){
				if (jRadioButton.getText().equals(String.valueOf(PriceEnum.TOW.getVal()))){
					jRadioButton.setSelected(true);
					price = jRadioButton.getText();
				}
			}else {
				if (jRadioButton.getText().equals(String.valueOf(PriceEnum.TEN.getVal()))){
					jRadioButton.setSelected(true);
					price = jRadioButton.getText();
				}
			}
		}
		priceJF.setText(price);
		globalVariable.selectPrice = price;
	}

	public String getType(String type,String serialNumber){
		String number = serialNumber.split(" ")[0];
		int typeLength = number.split(" ")[0].length();

		if(type.equals(TypeEnum.ZS.getLabel())){
			if(typeLength==2){
				if(number.charAt(0) == number.charAt(1)){
					return TypeEnum.ZSEM.getLabel();
				}else {
					return TypeEnum.SFZS.getLabel();
				}
			}else if (typeLength==3){
				if(number.charAt(0) != number.charAt(1) && number.charAt(0) != number.charAt(2)
						&& number.charAt(1) != number.charAt(2)){
					return TypeEnum.ZSSANM.getLabel();
				}else {
					return TypeEnum.ZS.getLabel();
				}
			}else {
				return TypeEnum.getLabelByVal(19 + typeLength);
			}
		}else if (type.equals(TypeEnum.ZL.getLabel())){
			if(typeLength==2){
				return TypeEnum.SFZL.getLabel();
			}else if (typeLength==3){
				return TypeEnum.ZL.getLabel();
			}else {
				return TypeEnum.getLabelByVal(10 + typeLength);
			}
		}
		return type;
	}

	public Float moneyRemoveChinese(String s,String chinese1,String chinese2){
		return Float.parseFloat(s.replace(chinese1,"").replace(chinese2,""));
	}

	public String moneyAddChinese(String s,String chinese1,String chinese2){
		return chinese1 + s + chinese2;
	}
}
