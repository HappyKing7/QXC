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
import java.util.stream.Collectors;

public class InputFunction {
	private static final Pattern pattern = Pattern.compile("-?[0-9]+(\\\\.[0-9]+)?");
	private static final KeyFunction keyFunction = new KeyFunction();
	private static final CommonFunction commonFunction = new CommonFunction();
	private static final OtherKeyFunction otherKeyFunction = new OtherKeyFunction();

	public Boolean filterTpye(String type){
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

	public String getNumber(TicketList tickets, AtomicInteger alllistNo, AtomicInteger ticketsNo, String serialNumber,
							String type, String typeTwo, String inputPrice, String times, String functionType,
							String filePath){
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

		tickets.setId(alllistNo.getAndIncrement());

		Ticket ticket = new Ticket();
		ticket.setId(ticketsNo.getAndIncrement());

		if(functionType.equals(FunctionType.AUTO.getLabel())){
			/*Map<String,String> KEY_MAP = null;keyFunction.readKeyExcel(filePath);
			List<List<String>> resultList = autoRecognitionKey(KEY_MAP,serialNumber);
			List<String> valueList =resultList.get(0);
			List<String> keyList = resultList.get(1);
			if(keyList.size() != 0){
				return autoRecognition1(serialNumber,serialNumberBuilder,group, price,inputPrice,times,type,typeTwo,
						KEY_MAP,keyList,valueList,ticket,tickets);
			}*/
			if (!serialNumber.contains("位"))
				return showSummaryWithNumber(autoRecognition2(filePath, serialNumberBuilder,serialNumber, group,typeTwo, times, price,tickets));
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
		} else if(type.equals(TypeEnum.HZ.getLabel())){
			return type + serialNumber.trim();
		} else if(type.equals(TypeEnum.KD.getLabel())){
			return type + serialNumber.trim();
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

	public String otherType(String key,String serialNumber){
		if (key.equals("元"))
			return KeyTypeEnum.TIMES.getLabel() + "--" + otherKeyFunction.yuan(serialNumber);

		return null;
	}

	public Ticket autoRecognition2(String filePath,StringBuilder stringBuilder, String input, int group, String typeTwo,
								 String times, float price, TicketList tickets){
		List<Map<String,String>> mapList = keyFunction.readKeyExcel(filePath);

		Map<String,String> typeTwoMap = mapList.get(0);
		Map<String,String> typeMap = mapList.get(1);
		Map<String,String> timesMap= mapList.get(2);
		Map<String,String> otherMap = mapList.get(3);

		List<Key> typeTwoList = new ArrayList<>();
		for(Map.Entry<String,String> entry : typeTwoMap.entrySet()){
			if (input.contains(entry.getKey())) {
				Key key = new Key();
				key.setKey(entry.getKey());
				key.setPosition(input.indexOf(entry.getKey()));
				typeTwoList.add(key);
			}
		}

		List<Key> typeList = new ArrayList<>();
		StringBuilder typeStr = new StringBuilder();
		int deleteFlag = 0;
		for(Map.Entry<String,String> entry : typeMap.entrySet()){
			if (input.contains(entry.getKey())) {
				if(!typeStr.toString().contains(entry.getValue())){
					if(entry.getValue().equals(TypeEnum.ZL.getLabel()) || entry.getValue().equals(TypeEnum.ZS.getLabel()))
						deleteFlag = 1;
					Key key = new Key();
					key.setKey(entry.getKey());
					key.setPosition(input.indexOf(entry.getKey()));
					typeList.add(key);
					typeStr.append(entry.getValue());
				}
			}
		}
		if(deleteFlag == 1){
			typeList.removeIf(key -> typeMap.get(key.getKey()).equals(TypeEnum.ZUXUAN.getLabel()));
		}

		List<Key> timesList = new ArrayList<>();
		StringBuilder timesStr = new StringBuilder();
		for(Map.Entry<String,String> entry : timesMap.entrySet()){
			if (input.contains(entry.getKey())) {
				if(entry.getKey().equals("三")){
					int position = input.indexOf(entry.getKey());
					if (position - 1 >= 0 && String.valueOf(input.charAt(position-1)).equals("组"))
						continue;
					if (position - 2 >= 0 && String.valueOf(input.charAt(position-1)).equals("列") &&
							String.valueOf(input.charAt(position-2)).equals("排"))
						continue;
				}

				if(entry.getKey().equals("六")){
					int position = input.indexOf(entry.getKey());
					if (position - 1 >= 0 && String.valueOf(input.charAt(position-1)).equals("组")){
						continue;
					}
				}

				if(!timesStr.toString().contains(entry.getValue())){
					Key key = new Key();
					key.setKey(entry.getKey());
					key.setPosition(input.indexOf(entry.getKey()));
					timesList.add(key);
					timesStr.append(entry.getValue());
				}
			}
		}

		List<Key> otherList = new ArrayList<>();
		for(Map.Entry<String,String> entry : otherMap.entrySet()){
			if (input.contains(entry.getKey())) {
				String inputStr = input;
				while(inputStr.contains(entry.getKey())){
					Key key = new Key();
					key.setKey(entry.getKey());
					key.setPosition(inputStr.indexOf(entry.getKey()));
					otherList.add(key);
					inputStr = inputStr.substring(0,key.getPosition()) + inputStr.substring(key.getPosition()+1);
				}
			}
		}

		if (typeTwoList.size()!=0)
			typeTwo = typeTwoMap.get(typeTwoList.get(0).getKey());

		List<Float> priceList = new ArrayList<>();
		if (otherList.size()!=0){
			if (otherList.size() == 1){
				String result = otherType(otherMap.get(otherList.get(0).getKey()),input);
				if(result.split("--")[0].equals(KeyTypeEnum.TIMES.getLabel())) {
					String money = result.split("--")[1];
					if (!otherKeyFunction.ifCNum(input)) {
						int i = stringBuilder.indexOf(" " + money + " ");
						int j = stringBuilder.indexOf(money + " ");
						if (i != -1) {
							stringBuilder.delete(i, i + money.length() + 1);
							group = group -1;
						}
						if (j == 0){
							stringBuilder.delete(j, j + money.length() + 1);
							group = group -1;
						}
					}
					price = Float.parseFloat(money);
				}
			}
			else {
				String inputStr = input;
				for (Key key : otherList){
					String result = otherType(otherMap.get(key.getKey()),inputStr);
					if(result.split("--")[0].equals(KeyTypeEnum.TIMES.getLabel())) {
						String money = result.split("--")[1];
						int i = stringBuilder.indexOf(money + " ");
						if (i != -1) {
							stringBuilder.delete(i, i + money.length() + 1);
							group = group -1;
						}
						priceList.add(Float.parseFloat(money));
					}
					int position = input.indexOf(key.getKey());
					inputStr = input.substring(0,position) + input.substring(position+1);
				}
			}

		}

		//删除倍
		if (input.contains("倍") && Character.isDigit(input.charAt(input.indexOf("倍")-1))){
			String inputStr = input;
			while (inputStr.contains("倍")){
				StringBuilder s = new StringBuilder();
				int p = inputStr.indexOf("倍") - 1;
				while(Character.isDigit(inputStr.charAt(p))){
					s.append(inputStr.charAt(p));
					p = p - 1;
				}
				s = new StringBuilder(new StringBuilder(s.toString()).reverse().toString());
				inputStr = inputStr.substring(0,inputStr.indexOf("倍")) + inputStr.substring(inputStr.indexOf("倍")+1);
				inputStr = inputStr.substring(0,inputStr.indexOf(s.toString())) + inputStr.substring(inputStr.indexOf(s.toString())+1);

				p = stringBuilder.indexOf(s.toString());
				stringBuilder.delete(p,p+s.length()+1);
				group = group -1;
			}
		}

		Ticket ticket = new Ticket();
		if (typeList.size() == 0){
			Key key = new Key();
			key.setKey(TypeEnum.ZHIXUAN.getLabel());
			key.setPosition(0);
			typeList.add(key);
		}

		if(typeList.size() == 1){
			ticket = new Ticket();
			if(timesList.size() != 0) {
				float money = price * Float.parseFloat(timesMap.get(timesList.get(0).getKey()));
				ticket = addTicket(ticket,typeMap.get(typeList.get(0).getKey()),typeTwo,money,stringBuilder,group,times,tickets);
			}
			else
				ticket = addTicket(ticket,typeMap.get(typeList.get(0).getKey()),typeTwo,price,stringBuilder,group,times,tickets);
		}else {
			if (timesList.size() <= 1 || otherList.size()!=0){
				if (timesList.size() == 1)
					price = price * Float.parseFloat(timesMap.get(timesList.get(0).getKey()));
				if (priceList.size() != 0)
					typeList = typeList.stream().sorted(Comparator.comparing(Key :: getPosition)).collect(Collectors.toList());
				for (int i = 0; i < typeList.size(); i++) {
					ticket = new Ticket();
					Key key = typeList.get(i);
					if (priceList.size() != 0)
						ticket = addTicket(ticket,typeMap.get(key.getKey()),typeTwo,priceList.get(i),stringBuilder,group,times,tickets);
					else
						ticket = addTicket(ticket,typeMap.get(key.getKey()),typeTwo,price,stringBuilder,group,times,tickets);
				}
			}else {
				typeList = typeList.stream().sorted(Comparator.comparing(Key :: getPosition)).collect(Collectors.toList());
				timesList = timesList.stream().sorted(Comparator.comparing(Key :: getPosition)).collect(Collectors.toList());

				for (int i = 0; i < typeList.size(); i++) {
					ticket = new Ticket();
					float money = price * Float.parseFloat(timesMap.get(timesList.get(i).getKey()));
					ticket = addTicket(ticket,typeMap.get(typeList.get(i).getKey()),typeTwo,money,stringBuilder,group,times,tickets);
				}
			}
		}
		return ticket;
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
				while(inputStr.contains(entry.getKey())){
					Key key = new Key();
					key.setKey(entry.getKey());
					key.setPosition(inputStr.indexOf(entry.getKey()));
					otherList.add(key);
					inputStr = inputStr.substring(0,key.getPosition()) + inputStr.substring(key.getPosition()+1);
				}
			}
		}

		String result = otherType(otherMap.get(otherList.get(0).getKey()),input);
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
		/*public List<List<String>> autoRecognitionKey(Map<String,String> KEY_MAP,String input){
			List<List<String>> resultList = new ArrayList<>();

			List<String> keyList = new ArrayList<>();
			StringBuilder inputStr = new StringBuilder();
			for (int i = 0; i < input.length(); i++) {
				if(!Character.isDigit(input.charAt(i))){
					if (!inputStr.toString().contains(TypeEnum.ZL.getLabel()) && !inputStr.toString().contains(TypeEnum.ZS.getLabel()))
						inputStr.append(input.charAt(i));
				}

				if(i+1<input.length()){
					if(Character.isDigit(input.charAt(i)) && String.valueOf(input.charAt(i+1)).equals("倍"))
						inputStr.append(input.charAt(i));

					if(Character.isDigit(input.charAt(i)) && KEY_MAP.get(String.valueOf(input.charAt(i+1))) != null){
						if (i+2 < input.length()){
							String zuXuan = input.charAt(i+1) + String.valueOf(input.charAt(i+2));
							if(zuXuan.equals(TypeEnum.ZL.getLabel()) || zuXuan.equals(TypeEnum.ZS.getLabel()) ||
									zuXuan.equals("组3") || zuXuan.equals("组6"))
								inputStr.append(zuXuan.replace("3", "三").replace("6", "六"));
							else
								inputStr.append(input.charAt(i)).append("倍");
						}else {
							inputStr.append(input.charAt(i)).append("倍");
						}
					}

					if(String.valueOf(input.charAt(i+1)).equals("元") || String.valueOf(input.charAt(i+1)).equals("米"))
						inputStr.append(input.charAt(i)).append("元");

					String threeD = input.charAt(i) + String.valueOf(input.charAt(i+1));
					if(threeD.equals("3D"))
						inputStr.append(input.charAt(i));
				}

				if(i-1>=0){
					if(Character.isDigit(input.charAt(i)) && KEY_MAP.get(String.valueOf(input.charAt(i-1))) != null){
						if (i-2 >= input.length()){
							String zuXuan = input.charAt(i-1) + String.valueOf(input.charAt(i-2));
							if(zuXuan.equals(TypeEnum.ZL.getLabel()) || zuXuan.equals(TypeEnum.ZS.getLabel()) ||
									zuXuan.equals("组3") || zuXuan.equals("组6"))
								continue;
							else
								inputStr.append(input.charAt(i)).append("倍");
						}else {
							if(!String.valueOf(input.charAt(i-1)).equals("组"))
								inputStr.append(input.charAt(i)).append("倍");
						}
					}
				}
			}

			String str = "";
			for (int j = 0; j < inputStr.length(); j++) {
				str = str + inputStr.charAt(j);
				for(Map.Entry<String,String> entry : KEY_MAP.entrySet()){
					if (str.contains(entry.getKey()))
						if(!keyList.contains(entry.getKey()))
							keyList.add(entry.getKey());
				}
			}

			if(keyList.contains(TypeEnum.ZL.getLabel())){
				keyList.remove("组");
				keyList.remove("六");
			}

			if(keyList.contains(TypeEnum.ZS.getLabel())){
				keyList.remove("组");
				keyList.remove("三");
			}

			List<String> valueList = new ArrayList<>();
			for (int k = 0; k < keyList.size(); k++) {
				if(!valueList.contains(KEY_MAP.get(keyList.get(k))))
					valueList.add(KEY_MAP.get(keyList.get(k)));
			}

			resultList.add(keyList);
			resultList.add(valueList);
			return resultList;
	}

		public String autoRecognition1(String serialNumber,StringBuilder serialNumberBuilder,Integer group,
								   float price, String inputPrice,  String times, String type, String typeTwo,
								   Map<String,String> KEY_MAP, List<String> keyList, List<String> valueList, Ticket ticket,
								   TicketList tickets){
			String splitSerialNumber = splitSerialNumber(serialNumber,0);
			if (!serialNumber.equals(splitSerialNumber)){
				group = getGroup(splitSerialNumber);
				serialNumberBuilder = new StringBuilder(splitSerialNumber);
			}

			if((keyList.contains("元") || keyList.contains("米")) && keyList.size() > 3){
				if(!otherKeyFunction.ifCNum(serialNumber)){
					serialNumberBuilder = new StringBuilder(splitSerialNumber(serialNumber,1));
					group = group - 1;
				}

			}

			int typeFlag = 0;
			float savePrice = 0;
			String tempType = "";
			for (String key : keyList) {
				String keyType = findType(key);
				if (keyType.equals(KeyTypeEnum.TIMES.getLabel())) {
					if(keyList.contains("元") || keyList.contains("米"))
						price = Float.parseFloat(key);
					else
						price = Float.parseFloat(inputPrice) * Integer.parseInt(key);
					savePrice = price;
				} else if (keyType.equals(KeyTypeEnum.LEIBIE.getLabel())) {
					typeTwo = KEY_MAP.get(key);
				} else if (keyType.equals(KeyTypeEnum.LEIXING.getLabel())) {
					if (typeFlag == 1)
						tempType = type;
					type = KEY_MAP.get(key);
					typeFlag = 1;
				} else if (keyType.equals(KeyTypeEnum.OTHER.getLabel())) {
					String result = otherType(key,serialNumber);
					if(result.split("--")[0].equals(KeyTypeEnum.TIMES.getLabel())){
						String money = result.split("--")[1];

						if(!otherKeyFunction.ifCNum(serialNumber)){
							int i = serialNumberBuilder.indexOf(money + " ");
							if (i != -1) {
								serialNumberBuilder.delete(i, i + money.length() + 1);
							}
						}

						price = Float.parseFloat(money);
					}
				}

				if(keyList.size() <= 5){
					if(!serialNumber.contains("直组各")){
						if (!tempType.equals("")){
							ticket = new Ticket();
							addTicket(ticket,tempType,typeTwo,price,serialNumberBuilder,group,times,tickets);
							typeFlag = 0;
						}else if(key.equals(keyList.get(keyList.size()-1))){
							ticket = new Ticket();
							addTicket(ticket,type,typeTwo,price,serialNumberBuilder,group,times,tickets);
							typeFlag = 0;
						}
					}
				}else{
					if((typeFlag == 1 && savePrice!=0)){
						ticket = new Ticket();
						addTicket(ticket,type,typeTwo,price,serialNumberBuilder,group,times,tickets);
						typeFlag = 0;
						savePrice = 0;
					}
					if(typeFlag == 1 && key.equals(keyList.get(keyList.size() - 1))){
						ticket = new Ticket();
						addTicket(ticket,type,typeTwo,price,serialNumberBuilder,group,times,tickets);
						typeFlag = 0;
					}
				}
		}

		if(serialNumber.contains("直组各")){
			ticket = new Ticket();
			addTicket(ticket,"直选",typeTwo,price,serialNumberBuilder,group,times,tickets);
			ticket = new Ticket();
			addTicket(ticket,type,"组选",price,serialNumberBuilder,group,times,tickets);
		}
		return showSummaryWithNumber(ticket);

		public String findType(String key){
			for (TypeTwoEnum typeTwoEnum : TypeTwoEnum.values()){
				if(typeTwoEnum.getLabel().equals(key))
					return KeyTypeEnum.LEIBIE.getLabel();
			}

			for (TimesEnum timesEnum : TimesEnum.values()){
				if (String.valueOf(timesEnum.getVal()).equals(key))
					return KeyTypeEnum.TIMES.getLabel();
			}

			for (TypeEnum typeEnum : TypeEnum.values()){
				if (typeEnum.getLabel().equals(key))
					return KeyTypeEnum.LEIXING.getLabel();
			}

			return KeyTypeEnum.OTHER.getLabel();
		}
	}

	public String splitSerialNumber(String serialNumber,Integer mode){
		if (mode == 0) {
			if ((!serialNumber.contains(",") || serialNumber.split(",").length - 1 > 1)
					&& (!serialNumber.contains("-") || serialNumber.split("-").length - 1 > 1)
					&& (!serialNumber.contains("，") || serialNumber.split("，").length - 1 > 1))
				return serialNumber;
		}
		List<String> numberList = getNumber(serialNumber.split(",")[0].split("-")[0].split("，")[0]
				, FunctionType.FEIZUHE.getLabel());
		StringBuilder result = new StringBuilder();
		if(mode == 0){
			for (int i = 0; i < numberList.size() - 1; i++)
				result.append(numberList.get(i)).append(" ");
		}else if(mode == 1){
			for (int i = 0; i < numberList.size() - 2; i++)
				result.append(numberList.get(i)).append(" ");
		}
		return result.toString();
	}

	public Integer getGroup(String serialNumber){
		return serialNumber.trim().split(" ").length;
	} */


}
