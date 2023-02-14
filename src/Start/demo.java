package Start;

import Bean.*;
import Enum.*;
import Function.InputFunction;
import Function.KeyFunction;
import Function.QueryFunction;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

import javax.swing.*;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;


public class demo {
	private static final InputFunction inputFunction = new InputFunction();
	private static final InputFunction INPUT_FUNCTION = new InputFunction();
	private static final KeyFunction keyFunction = new KeyFunction();
	private static final QueryFunction queryFunction = new QueryFunction();
	public static void main(String[] args) throws IOException {
		//demo4();
			System.out.println(3000000 / 10000 /12);
		//System.out.println(demo3());
	}

	static void demo4() throws IOException {
		String typeTwoJF = "福彩";

		List<ZhongJiang> zhongJiangNumberList = new ArrayList<>();
		List<ShowSummaryList> showSummaryLists = inputFunction.readTodayExcel(INPUT_FUNCTION.readConfig(0),"F.xlsx");
		Map<String,Float> peiLvMap = queryFunction.readPeiLvExcel(INPUT_FUNCTION.readConfig(0));

		while (true){
			System.out.print("INPUT:");
			Scanner scanner = new Scanner(System.in);
			String kaiJiangNumberJF = scanner.nextLine();
			if (kaiJiangNumberJF.equals("end"))
				break;

			for(TypeEnum typeEnum : TypeEnum.values()){
				zhongJiangNumberList = queryFunction.findTypeTwoAndType(kaiJiangNumberJF, typeTwoJF,typeEnum.getLabel(),AllFlagEnum.ALL.getVal(),
						showSummaryLists,zhongJiangNumberList,peiLvMap);
			}


			for (ZhongJiang zhongJiang : zhongJiangNumberList){
				System.out.println(zhongJiang.getNo() + " " + zhongJiang.getDetail() + " " + zhongJiang.getSerialNumber()
						+ " " +
						(!zhongJiang.getMoney().equals("<html>0.00") ? zhongJiang.getMoney().replace("<html>","") : "赔率异常")
						+ " "  + zhongJiang.getNote());
			}

			zhongJiangNumberList = new ArrayList<>();
			System.out.println();
		}

	}

	static String demo3() throws IOException {
		GlobalVariable globalVariable = new GlobalVariable();
		globalVariable.setFilePath(INPUT_FUNCTION.readConfig(0));
		String type = "";
		String typeTwo = "体彩";
		String inputPrice = "2";
		String times = "1";
		globalVariable.functionType = FunctionType.AUTO.getLabel();
		String function = "1";

		String path = INPUT_FUNCTION.readConfig(0) + "测试.xlsx";
		Workbook wrb;
		try {
			wrb = Workbook.getWorkbook(new File(path));
		} catch (IOException | BiffException e) {
			e.printStackTrace();
			return null;
		}

		Sheet sheet = wrb.getSheet(0);
		List<String> inputList = new ArrayList<>();
		List<String> resultList2 = new ArrayList<>();
		for (int i = 0; i < sheet.getRows(); i++) {
			String input = sheet.getCell(0, i).getContents().trim();
			String result = sheet.getCell(1, i).getContents().trim();

			if (!input.equals(""))
				inputList.add(input);
			if(!result.equals(""))
				resultList2.add(result);
		}

		List<String> resultList1 = new ArrayList<>();
		if(function.equals("0")){
			String serialNumber = "";
			while(!serialNumber.equals("end")){
				System.out.print("INPUT:");
				Scanner scanner = new Scanner(System.in);
				serialNumber = scanner.nextLine();
				inputFunction.getNumber(globalVariable.tickets,globalVariable.alllistNo,globalVariable.ticketsNo, serialNumber,
						type,typeTwo,inputPrice,times,globalVariable.functionType,globalVariable.filePath);
				resultList1 = printTicket(globalVariable,resultList1,function);
				globalVariable.tickets = new TicketList();
			}
		} else if (function.equals("1")){
			for (String s : inputList){
				inputFunction.getNumber(globalVariable.tickets,globalVariable.alllistNo,globalVariable.ticketsNo, s,
						type,typeTwo,inputPrice,times,globalVariable.functionType,globalVariable.filePath);
				resultList1 = printTicket(globalVariable,resultList1,function);
				globalVariable.tickets = new TicketList();
			}
		}

		List<String> failList = new ArrayList<>();
		for (int i = 0; i < resultList1.size(); i++) {
			if (!resultList1.get(i).equals(resultList2.get(i)))
				failList.add(resultList1.get(i) + "  " + resultList2.get(i));
		}

		if (failList.size() != 0){
			for (int i = 0; i < failList.size(); i++) {
				System.out.println(failList.get(i));
			}
			return "测试失败";
		}

		return "测试通过";
	}

	static List<String> printTicket(GlobalVariable globalVariable,List<String> resultList,String function){
		List<Ticket> ticketList =  globalVariable.tickets.getTicketList();
		for (Ticket ticket : ticketList) { //123 654打三倍直,123 654直选两倍组选一倍,123 456直组,123.456，五直五组,毒5-100米
			if (function.equals("0")){
				System.out.print(ticket.getSerialNumber() + " ");
				System.out.print("(" + ticket.getGroupNum() + "组" + "," + "单价" + ticket.getUnitPrice() + ",");
				System.out.print(ticket.getTypeTwo() + "," + ticket.getType() + ",");
				System.out.println("总" + ticket.getTotalPrice() + ")");
			}
			resultList.add(ticket.getSerialNumber() + " " + "(" + ticket.getGroupNum() + "组" + "," + "单价" + ticket.getUnitPrice() + "," +
					ticket.getTypeTwo() + "," + ticket.getType() + "," + "总" + ticket.getTotalPrice() + ")");
		}
		if (function.equals("0"))
			System.out.println();
		return resultList;
	}
	static void demo1(){
		JFrame jFrame = new JFrame();

		String[] columnNames = {"SHUZI","ZIMU","YINGYU"};
/*		//String[][] tableData = {{"","a","one"},{"2","b","two"},{"3","c","three"}};
		//String[][] tableData = {{"<html><span style='color: red'>1471</span> 1473 1477 1481 1483 1487 1491 1493 1497 1571 1573 1577 1581 1583 1587 1591 1593 1597 1671 1673 1677 1681 1683 1687 1691 1693 1697 2471 2473 2477 2481 2483 2487 2491 2493 2497 2571 2573 2577 2581 2583 2587 2591 2593 2597 2671 2673 2677 2681 2683 2687 2691 2693 2697 3471 3473 3477 3481 3483 3487 3491 3493 3497 3571 3573 3577 3581 3583 3587 3591 3593 3597 3671 3673 3677 3681 3683 3687 3691 3693 3697</html>","a","one"},{"2","b","two"},{"3","c","three"}};

		DefaultTableModel defaultTableModel = new DefaultTableModel(tableData,columnNames);
		JTable jTable = new JTable(defaultTableModel);

		JScrollPane jScrollPane = new JScrollPane(jTable);
		jFrame.add(jScrollPane,BorderLayout.CENTER);*/
		JLabel jLabel = new JLabel("<html><span style='color: red'>1471</span></html>");
		jLabel.setHorizontalAlignment(JLabel.CENTER);
		jLabel.setVerticalAlignment(JLabel.CENTER);
		jFrame.add(jLabel,BorderLayout.NORTH);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		jFrame.setBounds((screenSize.width - 1500) / 2, (screenSize.height - 800) / 2, 1500, 800);
		jFrame.setVisible(true);

		jFrame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
	}

	static Map<String,String> demo2(){
		String path = "E:\\Users\\WangXin\\Desktop\\关键字.xlsx";
		Workbook wrb;
		try {
			wrb = Workbook.getWorkbook(new File(path));
		} catch (IOException | BiffException e) {
			e.printStackTrace();
			return null;
		}

		Sheet sheet = wrb.getSheet(0);
		Map<String,String> KEY_MAP = new HashMap<>();
		for (int i = 0; i < sheet.getRows(); i++) {
			String key = sheet.getCell(0, i).getContents().trim();
			for (int j = 1; j < sheet.getRow(i).length; j++) {
				KEY_MAP.put(sheet.getRow(i)[j].getContents(),key);
			}
		}


		for(Map.Entry<String,String> entry : KEY_MAP.entrySet()){
			System.out.println(entry.getKey() + ":" + entry.getValue());
		}
		return KEY_MAP;
	}
	//514
	//511
}
