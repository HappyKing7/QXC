package Start;

import Bean.ShowSummary;
import Bean.ShowSummaryList;
import Bean.ZhongJiang;
import Enum.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class QueryFunction {

	public String[][] findTypeTwoAndType(String kaiJiangNumber,String typeTwo, String type,
								   List<ShowSummaryList> showSummaryLists){
		List<ZhongJiang> zhongJiangNumberList = new ArrayList<>();
		for (int i = 0; i < showSummaryLists.size(); i++) {
			ShowSummaryList summaryLists = showSummaryLists.get(i);
			for (int j = 0; j < summaryLists.getShowSummaryList().size(); j++) {
				ShowSummary showSummary = summaryLists.getShowSummaryList().get(j);
				String number = showSummary.getSerialNumber();
				String excelType = showSummary.getDetail().split(",")[3];
				String excelTypeTwo = showSummary.getDetail().split(",")[2];
				String detail = showSummary.getDetail();
				if (excelTypeTwo.equals(typeTwo)){
					if(excelType.equals(type)){
						if(type.equals(TypeEnum.DD.getLabel())){
							DD(number,kaiJiangNumber,summaryLists.getNo(),detail,zhongJiangNumberList);
						}else if(type.equals(TypeEnum.ZHIXUAN.getLabel())){
							DW(number,kaiJiangNumber,summaryLists.getNo(),detail,zhongJiangNumberList);
						}else if (type.equals(TypeEnum.ZUXUAN.getLabel())){
							ZX(number,kaiJiangNumber,summaryLists.getNo(),detail,zhongJiangNumberList);
						}else if(type.equals(TypeEnum.YMDW.getLabel()) || type.equals(TypeEnum.EMDW.getLabel())){
							if(!number.contains("*")){
								continue;
							}else {
								DW(number,kaiJiangNumber,summaryLists.getNo(),detail,zhongJiangNumberList);
							}
						}else if (type.equals(TypeEnum.SF.getLabel())){
							SF(number,kaiJiangNumber,summaryLists.getNo(),detail,zhongJiangNumberList);
						}else if(type.equals(TypeEnum.ZS.getLabel())){
							ZS(number,kaiJiangNumber,summaryLists.getNo(),detail,zhongJiangNumberList);
						}else if (type.equals(TypeEnum.ZL.getLabel())){
							ZL(number,kaiJiangNumber,summaryLists.getNo(),detail,zhongJiangNumberList);
						}else if (type.equals(TypeEnum.BZQB.getLabel())){
							BZQB(number,kaiJiangNumber,summaryLists.getNo(),detail,zhongJiangNumberList);
						}else if (type.equals(TypeEnum.ZLQB.getLabel())){
							ZLQB(number,kaiJiangNumber,summaryLists.getNo(),detail,zhongJiangNumberList);
						}else if (type.equals(TypeEnum.ZSQB.getLabel())){
							ZSQB(number,kaiJiangNumber,summaryLists.getNo(),detail,zhongJiangNumberList);
						}
					}else if (type.equals(TypeEnum.ZUXUAN.getLabel())){
						if(excelType.contains(TypeEnum.ZS.getLabel())){
							ZS(number,kaiJiangNumber,summaryLists.getNo(),detail,zhongJiangNumberList);
						}else if(excelType.contains(TypeEnum.ZL.getLabel())){
							ZL(number,kaiJiangNumber,summaryLists.getNo(),detail,zhongJiangNumberList);
						}
					}else if (type.equals(TypeEnum.ZS.getLabel()) && excelType.contains(TypeEnum.ZS.getLabel())){
						ZS(number,kaiJiangNumber,summaryLists.getNo(),detail,zhongJiangNumberList);
					}else if (type.equals(TypeEnum.ZL.getLabel()) && excelType.contains(TypeEnum.ZL.getLabel())){
						ZL(number,kaiJiangNumber,summaryLists.getNo(),detail,zhongJiangNumberList);
					}else if (type.equals(TypeEnum.KD.getLabel()) && excelType.contains(TypeEnum.KD.getLabel())){
						int difference = findNumber(kaiJiangNumber);
						KD(excelType,difference,number,summaryLists.getNo(),detail,zhongJiangNumberList);
					}else if (type.equals(TypeEnum.HZ.getLabel()) && excelType.contains(TypeEnum.HZ.getLabel())){
						int sum = sumNumber(kaiJiangNumber);
						HZ(excelType,sum,number,summaryLists.getNo(),detail,zhongJiangNumberList);
					}
				}
				/*else if(excelType.contains("组六") && type.contains("组六")){
					ZX(number,kaiJiangNumber,summaryLists.getNo(),zhongJiangNumberList);
				}else if(excelType.contains("组三") && type.contains("组三")){
					ZS(number,kaiJiangNumber,summaryLists.getNo(),zhongJiangNumberList);
				}*/
			}
		}

		String[][] tableData = new String[zhongJiangNumberList.size()][3];
		int row = 0;
		for (ZhongJiang zhongJiang : zhongJiangNumberList) {
			tableData[row][0] = zhongJiang.getNo();
			tableData[row][1] = zhongJiang.getSerialNumber();
			tableData[row][2] = zhongJiang.getDetail();
			row++;
		}

		return tableData;
	}

	public void addNumber(String number,String no, String detail, List<ZhongJiang> zhongJiangNumberList){
		ZhongJiang zhongJiang = new ZhongJiang();
		zhongJiang.setNo(no);
		zhongJiang.setSerialNumber(number);
		zhongJiang.setDetail(detail);
		zhongJiangNumberList.add(zhongJiang);
	}

	public void DD(String number, String kaiJiangNumber, String no, String detail, List<ZhongJiang> zhongJiangNumberList){
		String[] numbers = number.split(" ");
		for (int i = 0; i < numbers.length; i++) {
			for (int j = 0; j < kaiJiangNumber.length(); j++) {
				if(numbers[i].contains(String.valueOf(kaiJiangNumber.charAt(j)))){
					addNumber(number,no,detail,zhongJiangNumberList);
					return;
				}
			}
		}
	}

	public void DW(String number, String kaiJiangNumber, String no, String detail, List<ZhongJiang> zhongJiangNumberList){
		String[] numbers = number.split(" ");
		for (int i = 0; i < numbers.length; i++) {
			String num = numbers[i];
			int flag = 0;

			//默认位数相同
			if (num.length() != kaiJiangNumber.length()){
				continue;
			}

			for (int j = 0; j < num.length(); j++) {
				if(num.charAt(j) != kaiJiangNumber.charAt(j) && num.charAt(j) != '*'){
					flag = 1;
					break;
				}
			}

			if (flag == 0){
				addNumber(number,no,detail,zhongJiangNumberList);
				break;
			}
		}
	}

	public void SF(String number, String kaiJiangNumber, String no, String detail, List<ZhongJiang> zhongJiangNumberList){
		String[] numbers = number.split(" ");
		for (int i = 0; i < numbers.length; i++) {
			int times = 0;
			String num = kaiJiangNumber;
			for (int j = 0; j < numbers[i].length(); j++) {
				for (int k = 0; k < num.length(); k++) {
					if(num.charAt(k) == numbers[i].charAt(j)){
						num = num.substring(0,k) + num.substring(k+1);
						times = times + 1;
						break;
					}
				}
				if(times == 2){
					addNumber(number,no,detail,zhongJiangNumberList);
					return;
				}
			}
		}
	}

	public void ZX(String number, String kaiJiangNumber, String no, String detail, List<ZhongJiang> zhongJiangNumberList){
		int a = Integer.valueOf(String.valueOf(kaiJiangNumber.charAt(0)));
		int b = Integer.valueOf(String.valueOf(kaiJiangNumber.charAt(1)));
		int c = Integer.valueOf(String.valueOf(kaiJiangNumber.charAt(2)));
		if((a!=b && a!=c && b!=c)){
			ZL(number,kaiJiangNumber,no,detail,zhongJiangNumberList);
		}else if((a==b || a==c || b==c) && !(a==b && a==c && b==c)){
			ZS(number,kaiJiangNumber,no,detail,zhongJiangNumberList);
		}else {
			return;
		}

	}


	public void ZL(String number, String kaiJiangNumber, String no, String detail, List<ZhongJiang> zhongJiangNumberList){
		int a = Integer.valueOf(String.valueOf(kaiJiangNumber.charAt(0)));
		int b = Integer.valueOf(String.valueOf(kaiJiangNumber.charAt(1)));
		int c = Integer.valueOf(String.valueOf(kaiJiangNumber.charAt(2)));
		if(a==b || a==c || b==c){
			return;
		}

		String[] numbers = number.split(" ");
		for (int i = 0; i < numbers.length; i++) {
			int times = 0;
			for (int j = 0; j < numbers[i].length(); j++) {
				if(kaiJiangNumber.contains(String.valueOf(numbers[i].charAt(j)))){
					times = times + 1;
				}
			}
			if(times >= 3){
				addNumber(number,no,detail,zhongJiangNumberList);
				return;
			}
		}
	}

	public String removeDuplicate(String num){
		Set<String> nums = new HashSet();
		for (int i = 0; i < num.length(); i++) {
			nums.add(String.valueOf(num.charAt(i)));
		}

		String result = "";
		for (String n : nums){
			result = result + n;
		}
		return result;
	}

	public void ZS(String number, String kaiJiangNumber, String no, String detail, List<ZhongJiang> zhongJiangNumberList){
		int a = Integer.valueOf(String.valueOf(kaiJiangNumber.charAt(0)));
		int b = Integer.valueOf(String.valueOf(kaiJiangNumber.charAt(1)));
		int c = Integer.valueOf(String.valueOf(kaiJiangNumber.charAt(2)));
		if((a!=b && a!=c && b!=c) || (a==b && a==c && b==c)){
			return;
		}

		String[] numbers = number.split(" ");
		for (int i = 0; i < numbers.length; i++) {
			int times = 0;
			kaiJiangNumber = removeDuplicate(kaiJiangNumber);
			for (int j = 0; j < kaiJiangNumber.length(); j++) {
				if(numbers[i].contains(String.valueOf(kaiJiangNumber.charAt(j)))){
					times = times + 1;
				}
			}
			if(times == 3 || times == 2){
				addNumber(number,no,detail,zhongJiangNumberList);
				return;
			}
		}
	}

	public void BZQB(String number,String kaiJiangNumber, String no, String detail, List<ZhongJiang> zhongJiangNumberList) {
		String first = String.valueOf(kaiJiangNumber.charAt(0));
		String second = String.valueOf(kaiJiangNumber.charAt(1));
		String thrid = String.valueOf(kaiJiangNumber.charAt(2));
		if(first.equals(second) && first.equals(thrid)){
			addNumber(number,no,detail,zhongJiangNumberList);
		}
	}

	public void ZLQB(String number,String kaiJiangNumber, String no, String detail, List<ZhongJiang> zhongJiangNumberList) {
		String first = String.valueOf(kaiJiangNumber.charAt(0));
		String second = String.valueOf(kaiJiangNumber.charAt(1));
		String thrid = String.valueOf(kaiJiangNumber.charAt(2));
		if(!first.equals(second) && !first.equals(thrid) && !second.equals(thrid)){
			addNumber(number,no,detail,zhongJiangNumberList);
		}
	}

	public void ZSQB(String number,String kaiJiangNumber, String no, String detail, List<ZhongJiang> zhongJiangNumberList) {
		String first = String.valueOf(kaiJiangNumber.charAt(0));
		String second = String.valueOf(kaiJiangNumber.charAt(1));
		String thrid = String.valueOf(kaiJiangNumber.charAt(2));
		if(!first.equals(second) && !first.equals(thrid) && !second.equals(thrid)){
			return;
		}
		if(first.equals(second) && first.equals(thrid) && second.equals(thrid)){
			return;
		}
		addNumber(number,no,detail,zhongJiangNumberList);
	}

	public Integer findNumber(String kaiJiangNumber){
		int a = Integer.valueOf(String.valueOf(kaiJiangNumber.charAt(0)));
		int b = Integer.valueOf(String.valueOf(kaiJiangNumber.charAt(1)));
		int c = Integer.valueOf(String.valueOf(kaiJiangNumber.charAt(2)));
		List<Integer> numbers = new ArrayList<>();

		if (a>=b && a>=c){
			numbers.add(a);
			if(b<c){
				numbers.add(b);
			}else {
				numbers.add(c);
			}
		}else if(b>=a && b>=c){
			numbers.add(b);
			if(a<c){
				numbers.add(a);
			}else {
				numbers.add(c);
			}
		}else {
			numbers.add(c);
			if(a<b){
				numbers.add(a);
			}else {
				numbers.add(b);
			}
		}

		return numbers.get(0) - numbers.get(1);
	}

	public void KD(String excelType, Integer difference, String number, String no, String detail, List<ZhongJiang> zhongJiangNumberList) {
		int typeDifference = Integer.valueOf(excelType.split(TypeEnum.KD.getLabel())[1]);
		if(typeDifference == difference){
			addNumber(number,no,detail,zhongJiangNumberList);
		}
	}

	public Integer sumNumber(String kaiJiangNumber){
		int a = Integer.valueOf(String.valueOf(kaiJiangNumber.charAt(0)));
		int b = Integer.valueOf(String.valueOf(kaiJiangNumber.charAt(1)));
		int c = Integer.valueOf(String.valueOf(kaiJiangNumber.charAt(2)));

		return a+b+c;
	}

	public void HZ(String excelType, Integer sum, String number, String no, String detail, List<ZhongJiang> zhongJiangNumberList) {
		int typeSum = Integer.valueOf(excelType.split(TypeEnum.HZ.getLabel())[1]);
		if(typeSum == sum){
			addNumber(number,no,detail,zhongJiangNumberList);
		}
	}
}