package Function;

import Bean.GlobalVariable;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AutoPretreatmentFunction {
	private static final InputFunction inputFunction = new InputFunction();

	public void autoPretreatment(String[] serialNumbers, GlobalVariable globalVariable, String type, String typeTwo,
								 String inputPrice, String times, String str, Map<String, String> otherMap, StringBuilder moneyKeys){
		StringBuilder yuan = new StringBuilder();
		if (str.split("各").length - 1 == 1) {
			int position = str.indexOf("各");
			for (int i = position + 1; i < str.length(); i++) {
				if (Character.isDigit(str.charAt(i)))
					yuan.append(str.charAt(i));
				else {
					if (otherMap.get(String.valueOf(str.charAt(i))) != null) {
						break;
					}
				}
			}

			if (!yuan.toString().equals("")) {
				str = str.replace(yuan + "米", "").replace(yuan + "元", "");
				serialNumbers = str.split("\n");
				yuan.append("元");
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

				if (!s2.equals("")) {
					Matcher matcher3 = pattern1.matcher(s2);
					if (!matcher3.find()) {
						saveStr = new StringBuilder(s1);
						continue;
					}
				}

				if (s1.contains("3d") || s1.contains("3D")) {
					saveStr = new StringBuilder(s1 + " ");
					continue;
				}

				Pattern pattern4 = Pattern.compile("[" + moneyKeys.toString().replace("|", "") + "]");
				Matcher matcher4 = pattern4.matcher(s1);
				saveStr.append(s1);
				if (!yuan.toString().equals("") && !matcher4.find())
					saveStr.append(" ").append(yuan);
				inputFunction.getNumber(globalVariable.tickets, globalVariable.alllistNo, globalVariable.ticketsNo, saveStr.toString(),
						type, typeTwo, inputPrice, times, globalVariable.functionType, globalVariable.filePath, globalVariable);
				saveStr = new StringBuilder();
			} else {
				saveStr = new StringBuilder(s1);
			}
		}
	}
}
