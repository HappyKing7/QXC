package Start;

import Bean.*;
import Enum.*;
import Function.*;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;

public class test {
	private static final InputFunction inputFunction = new InputFunction();
	private static final InputFunction INPUT_FUNCTION = new InputFunction();
	private static final KeyFunction keyFunction = new KeyFunction();
	private static final AutoPretreatmentFunction autoPretreatmentFunction = new AutoPretreatmentFunction();
	private static final CommonFunction commonFunction = new CommonFunction();
	public static void main(String[] args) throws IOException {
		//手动测试 自动测试 √ ×
		//02双飞 20
		String function = "自动测试";
		String inputSerialNumber = "体311二十一元";//类型和号码要加一个空格隔开 “02双飞-20元” ”764，4单1组“ "57 福彩双飞20"
		Integer spaceSwitchMode = 1;//0关,1开 ，

		System.out.println(startTest(function,inputSerialNumber,spaceSwitchMode));
	}

	static String startTest(String function,String inputSerialNumber,Integer spaceSwitchMode) throws IOException {
		GlobalVariable globalVariable = new GlobalVariable();
		globalVariable.setFilePath(INPUT_FUNCTION.readConfig(0));
		globalVariable.setSpaceSwitchMode(spaceSwitchMode);
		String type = "";
		String typeTwo = "体彩";
		String inputPrice = "2";
		String times = "1";
		globalVariable.functionType = FunctionType.AUTO.getLabel();

		List<String> resultList1 = new ArrayList<>();
		switch (function) {
			case "自动测试": {
				String path = INPUT_FUNCTION.readConfig(0) + "测试.xlsx";
				Workbook wrb;
				try {
					wrb = Workbook.getWorkbook(new File(path));
				} catch (IOException | BiffException e) {
					e.printStackTrace();
					return null;
				}

				List<List<String>> result = commonFunction.getExpectData(wrb);
				List<String> inputList = result.get(0);
				List<String> resultList2 = result.get(1);

				Map<String, String> otherMap = keyFunction.readKeyExcel(globalVariable.filePath).get(3);
				StringBuilder moneyKeys = commonFunction.getMoneyKeys(otherMap);
				for (String s : inputList) {
					s = commonFunction.dealMa(s);
					setCommonFunction1(s,globalVariable,type,typeTwo,inputPrice,times, otherMap,moneyKeys);
					globalVariable.typeTwo = "";
				}
				printTicket(globalVariable, resultList1, function);

				List<String> failList = new ArrayList<>();
				if (resultList1.size() != resultList2.size()) {
					commonFunction.printFailList(resultList1, resultList2);
					return "测试失败";
				}

				for (int i = 0; i < resultList1.size(); i++) {
					if (!resultList1.get(i).equals(resultList2.get(i)))
						failList.add(resultList1.get(i) + "  " + resultList2.get(i));
				}
				if (failList.size() != 0) {
					System.out.println(failList.size());
					for (String s : failList) {
						System.out.println(s);
					}
					return "测试失败";
				}

				System.out.println(resultList1.size() + " : " + resultList2.size());
				return "测试通过";
			}
			case "手动测试": {
				String path = INPUT_FUNCTION.readConfig(0) + "测试.xlsx";
				Workbook wrb;
				try {
					wrb = Workbook.getWorkbook(new File(path));
				} catch (IOException | BiffException e) {
					e.printStackTrace();
					return null;
				}
				Sheet sheet = wrb.getSheet(0);
				Set<String> resultList2 = new HashSet<>();
				for (int i = 1; i < sheet.getRows(); i++) {
					String result = sheet.getCell(2, i).getContents().trim();
					if (!result.equals(""))
						resultList2.add(result);
				}

				inputSerialNumber = commonFunction.dealMa(inputSerialNumber);
				Map<String, String> otherMap = keyFunction.readKeyExcel(globalVariable.filePath).get(3);
				StringBuilder moneyKeys = commonFunction.getMoneyKeys(otherMap);
				setCommonFunction1(inputSerialNumber,globalVariable,type,typeTwo,inputPrice,times, otherMap,moneyKeys);
				printTicket(globalVariable, resultList1, function);

				int ifSuccess = 0;
				for (String s : resultList1) {
					if (!resultList2.contains(s)) {
						System.out.println(s);
						ifSuccess = 1;
						break;
					}
				}

				if (ifSuccess == 1)
					return "测试不通过";
				else
					return "测试成功";
			}
		}
		return null;
	}

	static void printTicket(GlobalVariable globalVariable, List<String> resultList, String function){
		List<Ticket> ticketList =  globalVariable.tickets.getTicketList();
		for (Ticket ticket : ticketList) { //123 654打三倍直,123 654直选两倍组选一倍,123 456直组,123.456，五直五组,毒5-100米
			if (!function.equals("自动测试")){
				System.out.print(ticket.getSerialNumber() + " ");
				System.out.print("(" + ticket.getGroupNum() + "组" + "," + "单价" + ticket.getUnitPrice() + ",");
				System.out.print(ticket.getTypeTwo() + "," + ticket.getType() + ",");
				System.out.println("总" + ticket.getTotalPrice() + ")");
			}
			resultList.add(ticket.getSerialNumber() + " " + "(" + ticket.getGroupNum() + "组" + "," + "单价" + ticket.getUnitPrice() + "," +
					ticket.getTypeTwo() + "," + ticket.getType() + "," + "总" + ticket.getTotalPrice() + ")");
		}
		if (!function.equals("自动测试"))
			System.out.println();
	}

	public static void setCommonFunction1(String inputSerialNumber, GlobalVariable globalVariable, String type, String typeTwo,
										  String inputPrice, String times, Map<String, String> otherMap, StringBuilder moneyKeys){
		String[] serialNumbers = inputSerialNumber.split("\n");
		if (serialNumbers.length == 1)
			inputFunction.getNumber(globalVariable.tickets, globalVariable.alllistNo, globalVariable.ticketsNo, inputSerialNumber,
					type, typeTwo, inputPrice, times, globalVariable.functionType, globalVariable.filePath, globalVariable);
		else {
			setCommonFunction2(serialNumbers,globalVariable,type,typeTwo,inputPrice,times,inputSerialNumber,otherMap,moneyKeys);
		}
	}

	public static void setCommonFunction2(String[] serialNumbers, GlobalVariable globalVariable, String type, String typeTwo,
										 String inputPrice, String times, String str, Map<String, String> otherMap, StringBuilder moneyKeys){
		if (serialNumbers.length == 1) {
			inputFunction.getNumber(globalVariable.tickets, globalVariable.alllistNo, globalVariable.ticketsNo, str,
					type, typeTwo, inputPrice, times, globalVariable.functionType, globalVariable.filePath, globalVariable);
		} else {
			autoPretreatmentFunction.autoPretreatment(serialNumbers,globalVariable,type,typeTwo,inputPrice,times,str,otherMap,moneyKeys);
		}
	}
}
