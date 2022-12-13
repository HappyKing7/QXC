package Start;

import Bean.*;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
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

public class Function {
	private static Pattern pattern = Pattern.compile("-?[0-9]+(\\\\.[0-9]+)?");

	public int getNumber(String serialNumber){
		List<String> numberList = new ArrayList<>();
		String number = "";
		for (int i = 0; i < serialNumber.length(); i++) {
			if (Character.isDigit(serialNumber.charAt(i))) {
				number = number + serialNumber.charAt(i);
			}else{
				if (!number.equals("")){
					numberList.add(number);
					number = "";
				}
			}

			if(i==serialNumber.length()-1 && !number.equals("")){
				numberList.add(number);
			}
		}

		return numberList.size();
	}

	public String getNumber(TicketList tickets, AtomicInteger alllistNo, AtomicInteger ticketsNo, String serialNumber, String type, String inputPrice, String times){
		if (serialNumber.equals("") || !pattern.matcher(inputPrice).matches() || !pattern.matcher(times).matches()){
			return "fail";
		}

		List<String> numberList = new ArrayList<>();
		String number = "";
		for (int i = 0; i < serialNumber.length(); i++) {
			if (Character.isDigit(serialNumber.charAt(i))) {
				number = number + serialNumber.charAt(i);
			}else{
				if (!number.equals("")){
					numberList.add(number);
					number = "";
				}
			}

			if(i==serialNumber.length()-1 && !number.equals("")){
				numberList.add(number);
			}
		}

		serialNumber = "";
		for (int j = 0; j < numberList.size(); j++) {
			serialNumber = serialNumber + numberList.get(j) + " ";
		}
		int group = numberList.size();
		Integer price = Integer.valueOf(inputPrice) * Integer.valueOf(times);

		tickets.setId(alllistNo.getAndIncrement());

		Ticket ticket = new Ticket();
		ticket.setId(ticketsNo.getAndIncrement());
		ticket.setSerialNumber(serialNumber);
		ticket.setGroupNum(group);
		ticket.setTimes(Integer.valueOf(times));
		ticket.setType(type);
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

	public String showSummaryWithNumber(Ticket ticket){
		String output = ticket.getSerialNumber();
		output = output + "  ";
		output = output + "(";
		output = output + ticket.getGroupNum()+"组";
		output = output + ",";
		output = output + "单价"+ticket.getUnitPrice();
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
		output = output + ticket.getType();
		output = output + ",";
		output = output + "总"+ticket.getTotalPrice()+"元";
		return output;
	}

	public boolean checkInput(){

		return false;
	}

	public void createExcel(String filePath,TicketList tickets) throws IOException, WriteException {
		FileOutputStream os=new FileOutputStream(filePath);
		WritableWorkbook workbook = Workbook.createWorkbook(os);
		WritableSheet sheet = workbook.createSheet("汇总", 0);
		Label l0 = new Label(0, 0, "序列1");
		sheet.addCell(l0);
		int totalPrice = 0;
		for (int i = 0; i < tickets.getTicketList().size(); i++) {
			Ticket ticket = tickets.getTicketList().get(i);
			Label l1 = new Label(1, i, ticket.getSerialNumber());
			Label l2 = new Label(2, i, showSummaryWithoutNumber(ticket));
			totalPrice = totalPrice + ticket.getTotalPrice();
			sheet.addCell(l1);
			sheet.addCell(l2);
		}
		Label l3 = new Label(3,0,"总共"+String.valueOf(totalPrice)+"元");
		Label l4 = new Label(5,0,"总计"+String.valueOf(totalPrice)+"元");
		sheet.addCell(l3);
		sheet.addCell(l4);

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

		WritableWorkbook workbook = null;
		try {
			workbook = Workbook.createWorkbook(file, wrb);
		} catch (IOException e) {
			e.printStackTrace();
			return e.getMessage();
		}
		WritableSheet writableSheet = workbook.getSheet(0);
		Label l0 = new Label(0,sheetRow+1,"序列"+String.valueOf(Integer.valueOf(numberNo)+1));
		writableSheet.addCell(l0);
		int totalPrice = 0;
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
		Label l3 = new Label(3,sheetRow+1,"总共"+String.valueOf(totalPrice)+"元");
		String totalMoney =  readSheet.getCell(5,0).getContents().replace("总计","").replace("元","");
		totalMoney = String.valueOf(Integer.valueOf(totalMoney) + Integer.valueOf(totalPrice));

		Label l4 = new Label(5,0,"总计"+String.valueOf(totalMoney)+"元");
		writableSheet.addCell(l3);
		writableSheet.addCell(l4);

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

	public String sumbit(String filePath,TicketList tickets) throws IOException, WriteException, BiffException {
		SimpleDateFormat sf= new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date();
		String nowDate= sf.format(date);
		filePath =filePath + nowDate +".xlsx";
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

	public List<ShowSummaryList> readTodayExcel(String filePath, String todayDate){
		String path = filePath + todayDate +".xlsx";
		Workbook wrb = null;
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
		Integer size = 0;
		for (int i = 0; i < sheet.getRows(); i++) {
			ShowSummary showSummary = new ShowSummary();
			String no = sheet.getCell(0, i).getContents().trim();
			String serialNumber = sheet.getCell(1, i).getContents().trim();
			String detail = sheet.getCell(2, i).getContents().trim();
			String totalPrice = sheet.getCell(3, i).getContents().trim();

			if(!no.equals("")) {
				serialNo = no;
			}
			if(!totalPrice.equals("")){
				totalPrices = totalPrice;
			}

			if(!serialNumber.equals(""))
			{
				showSummary.setSerialNumber(serialNumber);
				showSummary.setDetail(detail);
				showSummaries.add(showSummary);
			}

			if(serialNumber.equals("") || i==(sheet.getRows()-1)){
				ShowSummaryList summaryList = new ShowSummaryList();
				summaryList.setNo(serialNo);
				summaryList.setShowSummaryList(showSummaries);
				summaryList.setTotalPrice(totalPrices);
				showSummaryList.add(summaryList);
				showSummaries = new ArrayList<>();
				serialNo = "";
				size = size + 1;
				continue;
			}
		}

		showSummaryList.get(0).setSize(sheet.getRows() - size + 1);
		showSummaryList.get(0).setTotalMoney(sheet.getCell(5, 0).getContents().trim());
		return showSummaryList;
	}
}
