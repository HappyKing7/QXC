package Function;

import Bean.GlobalVariable;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AutoPretreatmentFunction {
	private static final InputFunction inputFunction = new InputFunction();
	private static final CommonFunction commonFunction = new CommonFunction();

	public void autoPretreatment(String[] serialNumbers, GlobalVariable globalVariable, String type, String typeTwo,
								 String inputPrice, String times, String str, Map<String,String> typeMap,
								 Map<String, String> otherMap, StringBuilder moneyKeys){
		StringBuilder yuan = new StringBuilder();

		int flag = 0;
		for(Map.Entry<String,String> entry : otherMap.entrySet()){
			if (str.contains(entry.getKey())){
				flag = 1;
				break;
			}
		}

		if (str.split("各").length - 1 == 1 && flag == 1) {
			int position = str.indexOf("各");
			for (int i = position + 1; i < str.length(); i++) {
				if (Character.isDigit(str.charAt(i)))
					yuan.append(str.charAt(i));
				else if (commonFunction.toNumber(String.valueOf(str.charAt(i))) != 0){
					yuan.append(str.charAt(i));
				}else {
					if (otherMap.get(String.valueOf(str.charAt(i))) != null) {
						break;
					}
				}
			}

			if (!yuan.toString().equals("")) {
				if (commonFunction.toNumber(yuan.toString()) == 0){
					str = str.replace(yuan + "米", "").replace(yuan + "元", "");
					serialNumbers = str.split("\n");
					yuan.append("元");
				}else {
					yuan = new StringBuilder(String.valueOf(commonFunction.toNumber(yuan.toString())) + "元") ;
				}
			}
		}

		StringBuilder saveStr = new StringBuilder();
		Pattern pattern1 = Pattern.compile("[0-9]+");
		for (String s1 : serialNumbers) {
			if (s1.equals(""))
				continue;

			Matcher matcher1 = pattern1.matcher(s1);
			if (matcher1.find()) {
				Pattern pattern2 = Pattern.compile("[0-9]+" + "(" + moneyKeys + ")");
				Matcher matcher2 = pattern2.matcher(s1);
				String s2 = "";
				while (matcher2.find()) {
					s2 = s1.replace(matcher2.group(), "");
				}

				String s3 = s2.trim().replaceAll("\\s*", "").replaceAll("[^(\\u4e00-\\u9fa5)]","");
				if (typeMap.get(s3.trim())!=null && typeMap.get(s3).contains("全包")){
					inputFunction.getNumber(globalVariable.tickets, globalVariable.alllistNo, globalVariable.ticketsNo, s1,
							type, typeTwo, inputPrice, times, globalVariable.functionType, globalVariable.filePath, globalVariable);
					saveStr = new StringBuilder();
					continue;
				}

				if (!s2.equals("")) {
					Matcher matcher3 = pattern1.matcher(s2);
					if (!matcher3.find()) {
						saveStr = new StringBuilder(s1);
						continue;
					}
				}

				if (s1.contains("3d") || s1.contains("3D")) {
					matcher1 = pattern1.matcher(s1.replace("3d","").replace("3D",""));
					if (!matcher1.find()){
						saveStr = new StringBuilder(s1 + " ");
						continue;
					}
				}

				Pattern pattern4 = Pattern.compile("[" + moneyKeys.toString().replace("|", "") + "]");
				Matcher matcher4 = pattern4.matcher(s1);
				saveStr.append(s1);
				if (!yuan.toString().equals("") && !matcher4.find())
					saveStr.append(" ").append(yuan);

				matcher2 = pattern2.matcher(saveStr);
				if ((ifDD(saveStr) && !matcher2.find()) || ifAllNumber(s1) ){
					saveStr.append(" ");
				}else {
					String ss = saveStr.toString();
					if (ifOnlyNumber(ss,typeMap) && !ss.equals(serialNumbers[serialNumbers.length-1]) && !ss.contains("位")
							&& !containOther(ss,otherMap) ){
						continue;
					}else {
						inputFunction.getNumber(globalVariable.tickets, globalVariable.alllistNo, globalVariable.ticketsNo, saveStr.toString(),
								type, typeTwo, inputPrice, times, globalVariable.functionType, globalVariable.filePath, globalVariable);
						saveStr = new StringBuilder();
					}

				}
			} else {
				if (saveStr.toString().equals(""))
					saveStr = new StringBuilder(s1);
				else
					saveStr.append(s1);
			}

			if (s1.equals(serialNumbers[serialNumbers.length-1]) && !saveStr.toString().equals(""))
				inputFunction.getNumber(globalVariable.tickets, globalVariable.alllistNo, globalVariable.ticketsNo, saveStr.toString(),
						type, typeTwo, inputPrice, times, globalVariable.functionType, globalVariable.filePath, globalVariable);
		}
	}

	public boolean ifDD(StringBuilder saveStr){
		if (saveStr.toString().contains("独胆"))
			return true;
		else if (saveStr.toString().contains("毒"))
			return true;
		else if (saveStr.toString().contains("独"))
			return true;
		else if (saveStr.toString().contains("胆"))
			return true;
		else
			return false;
	}

	public boolean ifAllNumber(String s){
		s = s.trim().replace(" ","");
		for (int i = 0; i < s.length(); i++) {
			if (!Character.isDigit(s.charAt(i)))
				return false;
		}
		return true;
	}

	public boolean ifOnlyNumber(String str, Map<String,String> typeMap){
		for(Map.Entry<String,String> entry : typeMap.entrySet()){
			if (str.contains(entry.getKey()))
				return false;
		}
		return true;
	}

	public boolean containOther(String str,Map<String, String> otherMap){
		for(Map.Entry<String,String> entry : otherMap.entrySet()){
			if (str.contains(entry.getKey()))
				return true;
		}
		return false;
	}
}
