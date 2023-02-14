package Function;

import Bean.*;
import Enum.*;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;

public class QueryFunction {
	private static Map<String,Float> PEIlV_MAP = new HashMap<>();
	private String zhongJiangType = "";
	private String zuXuanType = "";
	private Set<String> zhongJiangDuplicate = new HashSet<>();
	private final InputFunction inputFunction = new InputFunction();
	private static final CommonFunction commonFunction = new CommonFunction();

	public Boolean filterType(String label){
		if(commonFunction.quanBaoAndKD(label)){
			return true;
		}else {
			if(label.contains(TypeEnum.HZ.getLabel()) && label.length()>2){
				return true;
			}
			if(label.contains(TypeEnum.FS.getLabel()) && label.length()>2){
				return true;
			}
		}
		return false;
	}

	public List<ZhongJiang> findTypeTwoAndType(String kaiJiangNumber,String typeTwo, String type,Integer allFlag,
								   List<ShowSummaryList> showSummaryLists,List<ZhongJiang> zhongJiangNumberList,
											   Map<String,Float> peiLvMap){
		PEIlV_MAP = peiLvMap;
		zhongJiangDuplicate = new HashSet<>();
		for (ShowSummaryList summaryLists : showSummaryLists) {
			for (int j = 0; j < summaryLists.getShowSummaryList().size(); j++) {
				ShowSummary showSummary = summaryLists.getShowSummaryList().get(j);
				String no = summaryLists.getNo();
				String number = showSummary.getSerialNumber();
				String excelType = showSummary.getDetail().split(",")[3];
				String excelTypeTwo = showSummary.getDetail().split(",")[2];
				String detail = showSummary.getDetail();
				String note = summaryLists.getNote();
				zhongJiangType = excelType;
				if (excelTypeTwo.equals(typeTwo)) {
					if (excelType.equals(type)) {
						if (type.equals(TypeEnum.DD.getLabel())) {
							DD(number, kaiJiangNumber, no, detail, note,zhongJiangNumberList);
						} else if (type.equals(TypeEnum.ZHIXUAN.getLabel())) {
							DW(number, kaiJiangNumber, no, detail, note,zhongJiangNumberList);
						} else if (type.equals(TypeEnum.ZUXUAN.getLabel())) {
							ZX(number, kaiJiangNumber, no, detail, type, note, zhongJiangNumberList);
						} else if (type.equals(TypeEnum.YMDW.getLabel()) || type.equals(TypeEnum.EMDW.getLabel())) {
							if (number.contains("*")) {
								DW(number, kaiJiangNumber, no, detail, note, zhongJiangNumberList);
							}
						} else if (type.equals(TypeEnum.SF.getLabel())) {
							SF(number, kaiJiangNumber, no, detail, note, zhongJiangNumberList);
						} else if (type.equals(TypeEnum.ZS.getLabel())) {
							ZS(number, kaiJiangNumber, no, detail, note, zhongJiangNumberList);
						} else if (type.equals(TypeEnum.ZL.getLabel())) {
							ZL(number, kaiJiangNumber, no, detail, note, zhongJiangNumberList);
						} else if (type.equals(TypeEnum.BZQB.getLabel())) {
							BZQB(number, kaiJiangNumber, no, detail, note, zhongJiangNumberList);
						} else if (type.equals(TypeEnum.ZLQB.getLabel())) {
							ZLQB(number, kaiJiangNumber, no, detail, note,  zhongJiangNumberList);
						} else if (type.equals(TypeEnum.ZSQB.getLabel())) {
							ZSQB(number, kaiJiangNumber, no, detail, note, zhongJiangNumberList);
						} else if (type.equals(TypeEnum.DZQB.getLabel())) {
							DZQB(number, kaiJiangNumber, no, detail, note, zhongJiangNumberList);
						} else if (type.equals(TypeEnum.HZ.getLabel())) {
							int sum = sumNumber(kaiJiangNumber);
							HZ(excelType, sum, number, no, detail, note, zhongJiangNumberList);
						} else if (type.equals(TypeEnum.KD.getLabel())) {
							int difference = findNumber(kaiJiangNumber);
							KD(excelType, difference, number, no, detail, note, zhongJiangNumberList);
						}
					} else if (type.equals(TypeEnum.ZS.getLabel())) {
						if(excelType.contains(TypeEnum.ZS.getLabel())){
							for (int i=TypeEnum.ZSEM.getVal();i <= TypeEnum.ZSQB.getVal();i++){
								if(Objects.equals(TypeEnum.getLabelByVal(i), excelType)){
									ZS(number, kaiJiangNumber, no, detail, note, zhongJiangNumberList);
								}
							}
							if(excelType.equals(TypeEnum.SFZS.getLabel())){
								ZS(number, kaiJiangNumber, no, detail, note, zhongJiangNumberList);
							}
						}else if(excelType.equals(TypeEnum.ZUXUAN.getLabel()) && allFlag==AllFlagEnum.NOTALL.getVal()){
							ZX(number, kaiJiangNumber, no, detail, type, note, zhongJiangNumberList);
						}
					} else if (type.equals(TypeEnum.ZL.getLabel())) {
						if(excelType.contains(TypeEnum.ZL.getLabel())){
							for (int i=TypeEnum.ZLSM.getVal();i <= TypeEnum.ZLQB.getVal();i++){
								if(Objects.equals(TypeEnum.getLabelByVal(i), excelType)){
									ZL(number, kaiJiangNumber, no, detail, note, zhongJiangNumberList);
								}
							}
							if(excelType.equals(TypeEnum.SFZL.getLabel())){
								ZL(number, kaiJiangNumber, no, detail, note, zhongJiangNumberList);
							}
						}else if(excelType.equals(TypeEnum.ZUXUAN.getLabel()) && allFlag==AllFlagEnum.NOTALL.getVal()){
							ZX(number, kaiJiangNumber, no, detail, type, note, zhongJiangNumberList);
						}
					} else if (type.equals(TypeEnum.KD.getLabel()) && excelType.contains(TypeEnum.KD.getLabel())) {
						int difference = findNumber(kaiJiangNumber);
						KD(excelType, difference, number, no, detail, note, zhongJiangNumberList);
					} else if (type.equals(TypeEnum.HZ.getLabel()) && excelType.contains(TypeEnum.HZ.getLabel())) {
						int sum = sumNumber(kaiJiangNumber);
						HZ(excelType, sum, number, no, detail, note, zhongJiangNumberList);
					}/*else if (type.equals(TypeEnum.ZUXUAN.getLabel()) && allFlag != AllFlagEnum.ALL.getVal()) {
						if (excelType.contains(TypeEnum.ZS.getLabel())) {
							ZS(number, kaiJiangNumber, summaryLists.getNo(), detail, zhongJiangNumberList);
						} else if (excelType.contains(TypeEnum.ZL.getLabel())) {
							ZL(number, kaiJiangNumber, summaryLists.getNo(), detail, zhongJiangNumberList);
						}
					}*/
				}
			}
		}

		return zhongJiangNumberList;
	}

	public String moneyAddBr(String moneyStr,int num){
		StringBuilder result = new StringBuilder();
		int count = moneyStr.split(" [+] ").length -1;
		if(count >= 6){
			String[] moneyStrs = moneyStr.split(" [+] ");
			for (int i = 0; i < moneyStrs.length; i++) {
				result.append(moneyStrs[i]).append(" ");
				if(((i+1) % num) == 0){
					result.append("<br>");
				}
			}
			result = new StringBuilder(result.toString().replace(" ", " + "));
			return result.toString();
		}
		return moneyStr;

	}

	public String[][] transformTableData(List<ZhongJiang> zhongJiangNumberList){
		String[][] tableData = new String[zhongJiangNumberList.size()+1][5];
		int row = 0;
		float totalMoney = 0;
		for (ZhongJiang zhongJiang : zhongJiangNumberList) {
			tableData[row][0] = zhongJiang.getNo();
			tableData[row][1] = zhongJiang.getDetail();
			tableData[row][2] = zhongJiang.getSerialNumber();
			if(!zhongJiang.getMoney().equals("<html>0.00")){
				if(!zhongJiang.getMoney().contains("+")){
					tableData[row][3] = zhongJiang.getMoney() + "</html>";
				}else {
					tableData[row][3] = moneyAddBr(zhongJiang.getMoney(),6) + " = " + zhongJiang.getTotalMoney() + "</html>";
				}
			}else {
				tableData[row][3] = "赔率异常";
			}
			tableData[row][4] = zhongJiang.getNote().replace("备注：","");
			totalMoney = totalMoney +  Float.parseFloat(zhongJiang.getTotalMoney());
			row++;
		}

		tableData[row][0] = "总金额";
		tableData[row][1] = "";
		tableData[row][2] = "";
		tableData[row][3] = String.format("%.2f", totalMoney);
		tableData[row][4] = "";
		return tableData;
	}

	public String moneyRed(String number,String zhongJiangNumber){
		number = number.replace("<html>","<html> ").replace("</html>"," </html>");
		String[] numbers =  number.split(" ");
		StringBuilder result = new StringBuilder();
		String redZhongJiangNumber = "<span style='color: red'>" + zhongJiangNumber + "</span>";
		for (String s : numbers) {
			if (s.equals(zhongJiangNumber)) {
				result.append(" ").append(redZhongJiangNumber);
			} else {
				result.append(" ").append(s);
			}
		}
		return result.toString().trim();
	}

	public String chooseType(){
		if(!zhongJiangType.equals(TypeEnum.ZUXUAN.getLabel()))
			return zhongJiangType;
		return zuXuanType;
		/*if(zuXuanType.equals(TypeEnum.ZS.getLabel())){
			return TypeEnum.getLabelByVal(19 + zhongJiangNumber.length());
		}

		if(zuXuanType.equals(TypeEnum.ZL.getLabel())){
			return TypeEnum.getLabelByVal(10 + zhongJiangNumber.length());
		}

		zuXuanType = " ";
		return zhongJiangType;*/
	}

	public void addNumber(String number,String no, String detail, String zhongJiangNumber, String note,
						  List<ZhongJiang> zhongJiangNumberList){
		ZhongJiang zhongJiang = new ZhongJiang();
		zhongJiang.setSortNo(Integer.valueOf(no.split("序列")[1]));
		zhongJiang.setNo(no);
		zhongJiang.setDetail(detail);
		zhongJiang.setNote(note);

		Float price = Float.valueOf(detail.split(",")[1].replace("单价",""));
		//String money = String.format("%.2f", price * PEIlV_MAP.get(getPeiLvType(zhongJiangNumber)));

		String money = String.format("%.2f", price * PEIlV_MAP.get(inputFunction.getType(chooseType(),zhongJiangNumber)));
		if(zhongJiangDuplicate.contains(no + " " + number + " " + detail)){
			zhongJiang = zhongJiangNumberList.get(zhongJiangNumberList.size()-1);
			/*zhongJiang.setSerialNumber(zhongJiang.getSerialNumber()
					.replace(zhongJiangNumber,"<span style='color: red'>" + zhongJiangNumber + "</span>"));*/
			zhongJiang.setSerialNumber(moneyRed(zhongJiang.getSerialNumber(),zhongJiangNumber));
			zhongJiang.setMoney(zhongJiang.getMoney() + " + "  + money);
			zhongJiang.setTotalMoney(String.format("%.2f", Float.parseFloat(zhongJiang.getTotalMoney()) + Float.parseFloat(money)));
		}else{
			zhongJiangDuplicate.add(no + " " + number + " " + detail);

			if(zhongJiangNumber.equals("")){
				zhongJiang.setSerialNumber(zhongJiangNumber);
			}else {
				if(number.length()>90){
					int numberLength = number.split(" ")[0].length();
					if(numberLength < 8){
						number = inputFunction.numberWrap(number," ",(10-numberLength)*2);
					}else {
						number = inputFunction.numberWrap(number," ",5);
					}
					zhongJiang.setSerialNumber(moneyRed(number,zhongJiangNumber));
					/*zhongJiang.setSerialNumber(number.replace(zhongJiangNumber,"<span style='color: red'>" +
							zhongJiangNumber + "</span>"));*/
				}else {
					/*zhongJiang.setSerialNumber("<html>" + number.replace(zhongJiangNumber,"<span style='color: red'>" +
							zhongJiangNumber + "</span>") + "</html>");*/
					zhongJiang.setSerialNumber("<html>" + moneyRed(number,zhongJiangNumber) + "</html>");
				}

			}
			zhongJiang.setTotalMoney(money);
			zhongJiang.setMoney("<html>" + money);
			zhongJiangNumberList.add(zhongJiang);
		}
	}

	public void DD(String number, String kaiJiangNumber, String no, String detail, String note,
				   List<ZhongJiang> zhongJiangNumberList){
		String[] numbers = number.split(" ");
		for (String s : numbers) {
			for (int j = 0; j < kaiJiangNumber.length(); j++) {
				if (s.contains(String.valueOf(kaiJiangNumber.charAt(j)))) {
					addNumber(number, no, detail, s, note, zhongJiangNumberList);
					break;
				}
			}
		}
	}

	public void DW(String number, String kaiJiangNumber, String no, String detail,  String note,
				   List<ZhongJiang> zhongJiangNumberList){
		String[] numbers = number.split(" ");
		for (String num : numbers) {
			int flag = 0;

			//默认位数相同
			if (num.length() != kaiJiangNumber.length()) {
				continue;
			}

			for (int j = 0; j < num.length(); j++) {
				if (num.charAt(j) != kaiJiangNumber.charAt(j) && num.charAt(j) != '*') {
					flag = 1;
					break;
				}
			}

			if (flag == 0) {
				addNumber(number, no, detail, num, note, zhongJiangNumberList);
			}
		}
	}

	public void SF(String number, String kaiJiangNumber, String no, String detail, String note,
				   List<ZhongJiang> zhongJiangNumberList){
		String[] numbers = number.split(" ");
		for (String s : numbers) {
			int times = 0;
			String num = kaiJiangNumber;
			for (int j = 0; j < s.length(); j++) {
				for (int k = 0; k < num.length(); k++) {
					if (num.charAt(k) == s.charAt(j)) {
						num = num.substring(0, k) + num.substring(k + 1);
						times = times + 1;
						break;
					}
				}
				if (times == 2) {
					addNumber(number, no, detail, s, note, zhongJiangNumberList);
					break;
				}
			}
		}
	}

	public void ZX(String number, String kaiJiangNumber, String no, String detail, String type, String note,
				   List<ZhongJiang> zhongJiangNumberList){
		int a = Integer.parseInt(String.valueOf(kaiJiangNumber.charAt(0)));
		int b = Integer.parseInt(String.valueOf(kaiJiangNumber.charAt(1)));
		int c = Integer.parseInt(String.valueOf(kaiJiangNumber.charAt(2)));
		if((a!=b && a!=c && b!=c) && !type.equals(TypeEnum.ZS.getLabel())){
			ZL(number,kaiJiangNumber,no,detail,note,zhongJiangNumberList);
		}else if((a==b || a==c || b==c) && !(a==b && b==c) && !type.equals(TypeEnum.ZL.getLabel())){
			ZS(number,kaiJiangNumber,no,detail,note,zhongJiangNumberList);
		}
	}


	public void ZL(String number, String kaiJiangNumber, String no, String detail, String note,
				   List<ZhongJiang> zhongJiangNumberList){
		int a = Integer.parseInt(String.valueOf(kaiJiangNumber.charAt(0)));
		int b = Integer.parseInt(String.valueOf(kaiJiangNumber.charAt(1)));
		int c = Integer.parseInt(String.valueOf(kaiJiangNumber.charAt(2)));
		if((a==b || a==c || b==c) || detail.contains("全包")){
			return;
		}

		String[] numbers = number.split(" ");
		for (String s : numbers) {
			if(s.length() == 2){
				if(kaiJiangNumber.contains(String.valueOf(s.charAt(0))) &&
						kaiJiangNumber.contains(String.valueOf(s.charAt(1))) ){
					zuXuanType = TypeEnum.ZL.getLabel();
					addNumber(number, no, detail, s, note, zhongJiangNumberList);
				}
			}else{
				int times = 0;
				String rKaiJiangNumber = removeDuplicate(s);
				for (int j = 0; j < rKaiJiangNumber.length(); j++) {
					if (kaiJiangNumber.contains(String.valueOf(rKaiJiangNumber.charAt(j)))) {
						times = times + 1;
						if(times == 3){
							break;
						}
					}
				}
				if (times == 3) {
					zuXuanType = TypeEnum.ZL.getLabel();
					addNumber(number, no, detail, s, note, zhongJiangNumberList);
				}
			}
		}
	}

	public String removeDuplicate(String num){
		Set<String> nums = new HashSet<>();
		for (int i = 0; i < num.length(); i++) {
			nums.add(String.valueOf(num.charAt(i)));
		}

		StringBuilder result = new StringBuilder();
		for (String n : nums){
			result.append(n);
		}
		return result.toString();
	}

	public void ZS(String number, String kaiJiangNumber, String no, String detail, String note,
				   List<ZhongJiang> zhongJiangNumberList){
		int a = Integer.parseInt(String.valueOf(kaiJiangNumber.charAt(0)));
		int b = Integer.parseInt(String.valueOf(kaiJiangNumber.charAt(1)));
		int c = Integer.parseInt(String.valueOf(kaiJiangNumber.charAt(2)));
		if(((a!=b && a!=c && b!=c) || (a == c && b == c)) || detail.contains("全包")){
			return;
		}

		String[] numbers = number.split(" ");
		for (String s : numbers) {
			if(s.length()==2){
				String first = String.valueOf(s.charAt(0));
				String second = String.valueOf(s.charAt(1));
				if(kaiJiangNumber.contains(first) && kaiJiangNumber.contains(second)){
					zuXuanType = TypeEnum.ZS.getLabel();
					addNumber(number, no, detail, s, note, zhongJiangNumberList);
				}
			}else if (s.length()==3){
				String first = String.valueOf(s.charAt(0));
				String second = String.valueOf(s.charAt(1));
				String third = String.valueOf(s.charAt(2));
				if(first.equals(second) || first.equals(third) || second.equals(third)){
					char[] charK = kaiJiangNumber.toCharArray();
					char[] charS = s.toCharArray();
					Arrays.sort(charK);
					Arrays.sort(charS);
					if(String.valueOf(charK).equals(String.valueOf(charS))){
						zuXuanType = TypeEnum.ZS.getLabel();
						addNumber(number, no, detail, s, note, zhongJiangNumberList);
					}
				}else if(!detail.contains(TypeEnum.ZUXUAN.getLabel())){
					int times = 0;
					if(kaiJiangNumber.contains(first)){
						times = times + 1;
					}
					if(kaiJiangNumber.contains(second)){
						times = times + 1;
					}
					if(kaiJiangNumber.contains(third)){
						times = times + 1;
					}
					if(times == 2){
						zuXuanType = TypeEnum.ZS.getLabel();
						addNumber(number, no, detail, s, note, zhongJiangNumberList);
					}
				}
			}else{
				if(detail.contains(TypeEnum.ZUXUAN.getLabel()) && s.length()>=3)
					break;
				int times = 0;
				String rKaiJiangNumber = removeDuplicate(kaiJiangNumber);
				for (int j = 0; j < rKaiJiangNumber.length(); j++) {
					if (s.contains(String.valueOf(rKaiJiangNumber.charAt(j)))) {
						times = times + 1;
						if(times == 2){
							break;
						}
					}
				}
				if (times == 2) {
					zuXuanType = TypeEnum.ZS.getLabel();
					addNumber(number, no, detail, s, note, zhongJiangNumberList);
				}
			}
		}
	}

	public void BZQB(String number,String kaiJiangNumber, String no, String detail, String note,
					 List<ZhongJiang> zhongJiangNumberList) {
		String first = String.valueOf(kaiJiangNumber.charAt(0));
		String second = String.valueOf(kaiJiangNumber.charAt(1));
		String thrid = String.valueOf(kaiJiangNumber.charAt(2));
		if(first.equals(second) && first.equals(thrid)){
			addNumber(number,no,detail,number,note,zhongJiangNumberList);
		}
	}

	public void ZLQB(String number,String kaiJiangNumber, String no, String detail, String note,
					 List<ZhongJiang> zhongJiangNumberList) {
		String first = String.valueOf(kaiJiangNumber.charAt(0));
		String second = String.valueOf(kaiJiangNumber.charAt(1));
		String thrid = String.valueOf(kaiJiangNumber.charAt(2));
		if(!first.equals(second) && !first.equals(thrid) && !second.equals(thrid)){
			addNumber(number,no,detail,number,note,zhongJiangNumberList);
		}
	}

	public void ZSQB(String number,String kaiJiangNumber, String no, String detail, String note,
					 List<ZhongJiang> zhongJiangNumberList) {
		String first = String.valueOf(kaiJiangNumber.charAt(0));
		String second = String.valueOf(kaiJiangNumber.charAt(1));
		String thrid = String.valueOf(kaiJiangNumber.charAt(2));
		if(!first.equals(second) && !first.equals(thrid) && !second.equals(thrid)){
			return;
		}
		if(first.equals(second) && first.equals(thrid)){
			return;
		}
		addNumber(number,no,detail,number,note,zhongJiangNumberList);
	}

	public void DZQB(String number,String kaiJiangNumber, String no, String detail, String note,
					 List<ZhongJiang> zhongJiangNumberList) {
		String first = String.valueOf(kaiJiangNumber.charAt(0));
		String second = String.valueOf(kaiJiangNumber.charAt(1));
		String thrid = String.valueOf(kaiJiangNumber.charAt(2));
		if(first.equals(second) && first.equals(thrid)){
			BZQB(number, kaiJiangNumber, no, detail, note, zhongJiangNumberList);
		}else {
			ZSQB(number, kaiJiangNumber, no, detail, note, zhongJiangNumberList);
		}
	}

	public Integer findNumber(String kaiJiangNumber){
		int a = Integer.parseInt(String.valueOf(kaiJiangNumber.charAt(0)));
		int b = Integer.parseInt(String.valueOf(kaiJiangNumber.charAt(1)));
		int c = Integer.parseInt(String.valueOf(kaiJiangNumber.charAt(2)));
		List<Integer> numbers = new ArrayList<>();

		if (a>=b && a>=c){
			numbers.add(a);
			numbers.add(Math.min(b, c));
		}else if(b>=a && b>=c){
			numbers.add(b);
			numbers.add(Math.min(a, c));
		}else {
			numbers.add(c);
			numbers.add(Math.min(a, b));
		}

		return numbers.get(0) - numbers.get(1);
	}

	public void KD(String excelType, Integer difference, String number, String no, String detail, String note,
				   List<ZhongJiang> zhongJiangNumberList) {
		int typeDifference = Integer.parseInt(number);
		if(typeDifference == difference){
			addNumber(number,no,detail,number,note,zhongJiangNumberList);
		}
	}

	public Integer sumNumber(String kaiJiangNumber){
		int a = Integer.parseInt(String.valueOf(kaiJiangNumber.charAt(0)));
		int b = Integer.parseInt(String.valueOf(kaiJiangNumber.charAt(1)));
		int c = Integer.parseInt(String.valueOf(kaiJiangNumber.charAt(2)));

		return a+b+c;
	}

	public void HZ(String excelType, Integer sum, String number, String no, String detail, String note,
				   List<ZhongJiang> zhongJiangNumberList) {
		if(excelType.equals(TypeEnum.HZX.getLabel())){
			if(sum>=0 && sum <=13)
				addNumber(number,no,detail,"",note,zhongJiangNumberList);
		}else if(excelType.equals((TypeEnum.HZDA.getLabel()))){
			if(sum>=14 && sum <=27)
				addNumber(number,no,detail,"",note,zhongJiangNumberList);
		}else if(excelType.equals((TypeEnum.HZDAN.getLabel()))){
			if(sum % 2 != 0)
				addNumber(number,no,detail,"",note,zhongJiangNumberList);
		}else if(excelType.equals((TypeEnum.HZS.getLabel()))){
			if(sum % 2 == 0)
				addNumber(number,no,detail,"",note,zhongJiangNumberList);
		}else{
			int typeSum = Integer.parseInt(number);
			if(typeSum == sum){
				addNumber(number,no,detail,number,note,zhongJiangNumberList);
			}
		}
	}

	public Map<String,Float> readPeiLvExcel(String filePath){
		String path = filePath + "赔率.xlsx";
		Workbook wrb;
		try {
			wrb = Workbook.getWorkbook(new File(path));
		} catch (IOException | BiffException e) {
			e.printStackTrace();
			return null;
		}

		Sheet sheet = wrb.getSheet(0);
		Map<String,Float> PEIlV_MAP = new HashMap<>();
		for (int i = 0; i < sheet.getRows(); i++) {
			String type = sheet.getCell(0, i).getContents().trim();
			if(!type.equals("")){
				float peiLv = 0;
				if(!sheet.getCell(2, i).getContents().equals("") && !sheet.getCell(1, i).getContents().equals("")){
					peiLv = Float.parseFloat(sheet.getCell(2, i).getContents().trim())
							/ Float.parseFloat(sheet.getCell(1, i).getContents().trim());
				}
				PEIlV_MAP.put(type,peiLv);
			}
		}

		return PEIlV_MAP;
	}

	public void copyConText(JTable jTable){
		int row = jTable.getSelectedRow();
		int column = jTable.getSelectedColumn();
		String context = jTable.getValueAt(row,column).toString();
		if(column == 3)
			context = context.replace("<html>","").replace("</html>","");
		if(column == 2){
			context = context.replace("<html> ","");
			context = context.replace("<span style='color: red'>","");
			context = context.replace("</span>","");
			context = context.replace("<br>","");
			context = context.replace(" </html>","");
			context = context.replace("<html>","");
			context = context.replace("</html>","");
		}
		StringSelection contextSelection = new StringSelection(context);
		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		clipboard.setContents(contextSelection, null);
	}
}