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
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;

public class InputFunction {
	private static final Pattern pattern = Pattern.compile("-?[0-9]+(\\\\.[0-9]+)?");
	private static final KeyFunction keyFunction = new KeyFunction();
	private static final CommonFunction commonFunction = new CommonFunction();
	private static final AutoRecognitionFunction autoRecognitionFunction = new AutoRecognitionFunction();
	private static final OtherKeyFunction otherKeyFunction = new OtherKeyFunction();

	public Boolean filterType(String type){
		if(type.equals(TypeEnum.ALL.getLabel())){
			return true;
		}
		if (commonFunction.quanBaoAndKD(type)){
			return true;
		}else {
			if(type.contains(TypeEnum.HZ.getLabel()) && type.length()>2){
				if(!type.equals(TypeEnum.HZDAN.getLabel()) && !type.equals(TypeEnum.HZS.getLabel()) &&
						!type.equals(TypeEnum.HZDA.getLabel()) && !type.equals(TypeEnum.HZX.getLabel()))
					return true;
			}
			if(type.contains(TypeEnum.FS.getLabel()) && type.length()>2){
				return true;
			}
		}
		return false;
	}

	public List<String> splitNumber(String serialNumber){
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
		return numberList;
	}

	public List<String> getNumber(String serialNumber,String functionType){
		List<String> numberList = splitNumber(serialNumber);
		if(functionType.equals(FunctionType.ZUHE.getLabel())){
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

	public String getNumber(TicketList tickets, AtomicInteger allListNo, AtomicInteger ticketsNo, String serialNumber,
							String type, String typeTwo, String inputPrice, String times, String functionType,
							String filePath,GlobalVariable globalVariable){
		if (!pattern.matcher(times).matches()){
			return "fail";
		}

		if(serialNumber.equals("") && !ifQB(type)){
			return "fail";
		}

		List<String> numberList = new ArrayList<>();

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
			if (numberList.get(0).split("[*]").length-1 == 1)
				type = TypeEnum.EMDW.getLabel();
			else
				type = TypeEnum.YMDW.getLabel();
		}else {
			numberList = splitNumber(serialNumber);
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

		int group = numberList.size();
		float price = Float.parseFloat(inputPrice) * Integer.parseInt(times);

		tickets.setId(allListNo.getAndIncrement());

		Ticket ticket = new Ticket();
		ticket.setId(ticketsNo.getAndIncrement());

		if(functionType.equals(FunctionType.AUTO.getLabel())){
			if (!serialNumber.contains("位"))
				return showSummaryWithNumber(autoRecognition2(filePath, serialNumberBuilder,serialNumber, group,typeTwo,
						times, price,tickets,globalVariable));
			else
				return showSummaryWithNumber(autoRecognition3(filePath, serialNumber, typeTwo, tickets));
		}
		ticket = addTicket(ticket,type,typeTwo,price,serialNumberBuilder,group,times,tickets);
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

	public String showSummaryCommonContext(String output,Ticket ticket){
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
	public String showSummaryWithNumber(Ticket ticket){
		String output = ticket.getSerialNumber();
		if(ticket.getSerialNumber().length() <= 145){
			output = ticket.getSerialNumber();
		}else{
			output = numberWrap(output," ",10);
		}

		output = output + "-";
		output = output + "(";
		output = output + ticket.getGroupNum()+"注";
		output = showSummaryCommonContext(output,ticket);
		output = output + ")";

		return output;
	}

	public String showSummaryWithoutNumber(Ticket ticket){
		String output = ticket.getGroupNum()+"注";
		output = showSummaryCommonContext(output,ticket);
		return output;
	}

	public WritableSheet addSheetCell(Ticket ticket,int i,WritableSheet sheet) throws WriteException {
		Label l1 = new Label(1, i, ticket.getSerialNumber());
		Label l2 = new Label(2, i, showSummaryWithoutNumber(ticket));
		sheet.addCell(l1);
		sheet.addCell(l2);
		return sheet;
	}

	public void addSheetCell(Label l3,Label l4,Label l6,WritableSheet writableSheet, WritableWorkbook workbook)
			throws WriteException, IOException {
		writableSheet.addCell(l3);
		writableSheet.addCell(l4);
		writableSheet.addCell(l6);

		workbook.write();
		workbook.close();
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
			sheet = addSheetCell(ticket,i,sheet);
			totalPrice = totalPrice + ticket.getTotalPrice();
		}
		Label l3 = new Label(3,0,"总共"+ totalPrice +"元");
		Label l4 = new Label(4,0,"备注："+ tickets.getNote());
		Label l6 = new Label(6,0,"总计"+ totalPrice +"元");
		addSheetCell(l3,l4,l6,sheet,workbook);
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
			writableSheet = addSheetCell(ticket,i,writableSheet);
			totalPrice = totalPrice + ticket.getTotalPrice();
			ticketsNo = ticketsNo + 1;
		}
		Label l3 = new Label(3,sheetRow+1,"总共" + totalPrice +"元");
		Label l4 = new Label(4,sheetRow+1,"备注：" + tickets.getNote());
		String totalMoney =  readSheet.getCell(6,0).getContents().replace("总计","").replace("元","");
		totalMoney = String.valueOf((Float.parseFloat(totalMoney) + totalPrice));

		Label l6 = new Label(6,0,"总计"+ totalMoney + "元");
		addSheetCell(l3,l4,l6,writableSheet,workbook);
		wrb.close();
		return "成功";
	}

	public String readConfig(int i) throws IOException {
		Path path = Paths.get("C:\\config.txt");
		List<String> lines = Files.readAllLines(path);
		return lines.get(i);
	}

	public String submit(String filePath,TicketList tickets,String fileName) throws IOException, WriteException, BiffException {
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

		if(type.equals(TypeEnum.ZS.getLabel())){ //
			if(typeLength==2){
				if(number.charAt(0) == number.charAt(1)){
					return TypeEnum.DZ.getLabel();
				}else {
					return TypeEnum.ZSEM.getLabel();
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
			if (typeLength==1){
				return TypeEnum.DD.getLabel();
			}
			if(typeLength==2){
				if(number.charAt(0) == number.charAt(1)){
					return TypeEnum.DZ.getLabel();
				}else {
					return TypeEnum.SF.getLabel();
				}
			}else if (typeLength==3){
				return TypeEnum.ZL.getLabel();
			}else {
				return TypeEnum.getLabelByVal(10 + typeLength);
			}
		} else if(type.equals(TypeEnum.HZ.getLabel())){
			return type + serialNumber.trim();
		} else if(type.equals(TypeEnum.KD.getLabel())){
			return type + serialNumber.trim();
		}else if (type.equals(TypeEnum.FS.getLabel())){
			return TypeEnum.getLabelByVal(71 + typeLength);
		}
		return type;
	}

	public Float moneyRemoveChinese(String s,String chinese1,String chinese2){
		return Float.parseFloat(s.replace(chinese1,"").replace(chinese2,""));
	}

	public String moneyAddChinese(String s,String chinese1,String chinese2){
		return chinese1 + s + chinese2;
	}

	public Boolean ifQB(String type){
		if (type.equals(TypeEnum.ZSQB.getLabel()))
			return true;
		if (type.equals(TypeEnum.ZLQB.getLabel()))
			return true;
		if (type.equals(TypeEnum.BZQB.getLabel()))
			return true;
		if (type.equals(TypeEnum.DZQB.getLabel()))
			return true;
		if (type.equals(TypeEnum.HZDA.getLabel()))
			return true;
		if (type.equals(TypeEnum.HZX.getLabel()))
			return true;
		if (type.equals(TypeEnum.HZDAN.getLabel()))
			return true;
		if (type.equals(TypeEnum.HZS.getLabel()))
			return true;
		return false;
	}

	public Ticket addTicket(Ticket ticket,String type,String typeTwo,float price,StringBuilder serialNumberBuilder,
							int group,String times,TicketList tickets){
		if(ifQB(type)){
			ticket.setSerialNumber("0123456789");
			if(type.contains(TypeEnum.HZ.getLabel()))
				ticket.setSerialNumber("");
			ticket.setGroupNum(1);
			ticket.setTimes(1);
			ticket.setType(type);
			ticket.setTypeTwo(typeTwo);
			ticket.setUnitPrice(price);
			ticket.setTotalPrice(price);
		}
		else{
			ticket.setSerialNumber(serialNumberBuilder.toString());
			ticket.setGroupNum(group);
			ticket.setTimes(Integer.valueOf(times));
			ticket.setType(type);
			ticket.setTypeTwo(typeTwo);
			ticket.setUnitPrice(price);
			ticket.setTotalPrice(Float.parseFloat(String.format("%.2f",group*price)));
		}

		if(tickets.getTicketList() == null){
			List<Ticket> ticketList = new ArrayList<>();
			ticketList.add(ticket);
			tickets.setTicketList(ticketList);
		}else{
			tickets.getTicketList().add(ticket);
		}

		return ticket;
	}

	public String otherType(String key,String serialNumber,Map<String,String> otherMap){
		if (key.equals("元"))
			return KeyTypeEnum.TIMES.getLabel() + "--" + otherKeyFunction.yuan(serialNumber,otherMap);

		return null;
	}

	public void addOtherTypeList(String inputStr,Map.Entry<String,String> entry,List<Key> otherList){
		while(inputStr.contains(entry.getKey())){
			Key key = new Key();
			key.setKey(entry.getKey());
			key.setPosition(inputStr.indexOf(entry.getKey()));
			if (String.valueOf(inputStr.charAt(inputStr.indexOf(entry.getKey())-2)).equals("各")){
				key = new Key();
				key.setKey(entry.getKey());
				key.setPosition(inputStr.indexOf(entry.getKey())-1);
			}
			otherList.add(key);
			inputStr = inputStr.substring(0,key.getPosition()) + inputStr.substring(key.getPosition()+1);
		}
	}

	public Ticket autoRecognition2(String filePath,StringBuilder stringBuilder, String input, int group, String typeTwo,
								 String times, float price, TicketList tickets, GlobalVariable globalVariable){
		List<Map<String,String>> mapList = keyFunction.readKeyExcel(filePath);

		Map<String,String> typeTwoMap = mapList.get(0);
		Map<String,String> typeMap = mapList.get(1);
		Map<String,String> timesMap = mapList.get(2);
		Map<String,String> otherMap = mapList.get(3);

		List<Key> typeTwoList = autoRecognitionFunction.getTypeTwoList(input,stringBuilder,group,typeTwoMap);
		group = autoRecognitionFunction.getGroup(typeTwoList);
		stringBuilder = autoRecognitionFunction.getStringBuilder(typeTwoList);
		if (typeTwoList.size()==1)
			globalVariable.typeTwo = typeTwoMap.get(typeTwoList.get(0).getKey());
		else if (typeTwoList.size()==2){
			if (typeTwoMap.get(typeTwoList.get(0).getKey()).equals(typeTwoMap.get(typeTwoList.get(1).getKey())))
				globalVariable.typeTwo = typeTwoMap.get(typeTwoList.get(0).getKey());
		}

		List<Key> typeList = autoRecognitionFunction.getTypeList(input,globalVariable,otherMap,typeMap);
		StringBuilder inputBuilder = autoRecognitionFunction.getStringBuilder(typeList);
		input = inputBuilder.toString();

		List<Key> timesList = autoRecognitionFunction.getTimesList(input,otherMap,timesMap);

		List<Key> otherList = new ArrayList<>();
		for(Map.Entry<String,String> entry : otherMap.entrySet()){
			if (input.contains(entry.getKey())) {
				addOtherTypeList(input,entry,otherList);
			}
		}

		if (typeTwoList.size()!=0)
			typeTwo = typeTwoMap.get(typeTwoList.get(0).getKey());
		if (!globalVariable.typeTwo.equals(""))
			typeTwo = globalVariable.typeTwo;

		//识别倍数和单价
		TimesAndPrice timesAndPrice = autoRecognitionFunction.getTimesAndPrice(input,stringBuilder,group,price
				,otherList,otherMap);
		List<Float> priceList = timesAndPrice.getPriceList();
		int priceFlag = timesAndPrice.getPriceFlag();
		group = timesAndPrice.getGroup();
		price = timesAndPrice.getPrice();

		//删除倍
		if (input.contains("倍") && Character.isDigit(input.charAt(input.indexOf("倍")-1))){
			List<String> deleteBeiList = autoRecognitionFunction.deleteBei(input,stringBuilder,group);
			group = Integer.parseInt(deleteBeiList.get(0));
			stringBuilder = new StringBuilder(deleteBeiList.get(1));
		}

		//无类型时根据号码长度确定类型
		if (typeList.size() == 0){
			typeList = autoRecognitionFunction.getTypeByNumberLength(stringBuilder,typeList);
		}

		//设置单价
		price = autoRecognitionFunction.setPrice(stringBuilder,priceFlag,price,typeList,typeMap);

		//添加数据
		return autoRecognitionFunction.addTicket(priceFlag,stringBuilder,typeTwo,group,times,price,tickets,typeList,
				timesList,otherList,priceList,timesMap,typeMap);
	}

	public Ticket autoRecognition3(String filePath, String input,String typeTwo, TicketList tickets){
		List<Map<String,String>> mapList = keyFunction.readKeyExcel(filePath);
		Map<String,String> otherMap = mapList.get(3);

		String inputStr = input;
		List<DingWeiKey> dingWeiList = new ArrayList<>();
		while(inputStr.contains("位")) {
			DingWeiKey dingWeiKey = new DingWeiKey();
			int position = inputStr.indexOf("位");
			dingWeiKey.setWeiShu(String.valueOf(inputStr.charAt(position-1)));
			for (int i = position; i < inputStr.length(); i++) {
				if (Character.isDigit(inputStr.charAt(i))){
					dingWeiKey.setNumber(String.valueOf(inputStr.charAt(i)));
					break;
				}
			}
			dingWeiList.add(dingWeiKey);
			inputStr = inputStr.substring(0, position) + inputStr.substring(position + 1);
		}

		List<Key> otherList = new ArrayList<>();
		for(Map.Entry<String,String> entry : otherMap.entrySet()){
			if (input.contains(entry.getKey())) {
				inputStr = input;
				addOtherTypeList(inputStr,entry,otherList);
			}
		}

		String result = otherType(otherMap.get(otherList.get(0).getKey()),input,otherMap);
		String money = result.split("--")[1];

		String number = "百十个";
		int ma = 0;
		for (DingWeiKey dingWeiKey : dingWeiList){
			if (number.contains(dingWeiKey.getWeiShu())){
				number = number.replace(dingWeiKey.getWeiShu(),dingWeiKey.getNumber());
				ma = ma + 1;
			}
		}
		number = number.replace("百","*").replace("十","*").replace("个","*");

		String type;
		if(ma == 1){
			type = TypeEnum.YMDW.getLabel();
		}else {
			type = TypeEnum.EMDW.getLabel();
		}

		Ticket ticket = new Ticket();
		ticket = addTicket(ticket,type,typeTwo,Float.parseFloat(money),new StringBuilder(number),1,"1",tickets);


		return ticket;
	}
}
