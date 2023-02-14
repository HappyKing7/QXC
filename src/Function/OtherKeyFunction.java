package Function;

import java.util.HashMap;
import java.util.Map;

public class OtherKeyFunction {
	public String yuan(String serialNumber){
		int position = 0;
		for (int i = 0; i < serialNumber.length(); i++) {
			if (String.valueOf(serialNumber.charAt(i)).equals("元") || String.valueOf(serialNumber.charAt(i)).equals("米")
					|| String.valueOf(serialNumber.charAt(i)).equals("块")){
				position = i;
				break;
			}
		}

		StringBuilder price = new StringBuilder();
		Map<String,String> C_NUM = getCNum();
			for (int j = position-1; j >= 0 ; j--) {
			if(Character.isDigit(serialNumber.charAt(j)))
				price.append(serialNumber.charAt(j));
			else if (C_NUM.get(String.valueOf(serialNumber.charAt(j)))!=null) {
				if(String.valueOf(serialNumber.charAt(j)).equals("三") || String.valueOf(serialNumber.charAt(j)).equals("六")){
					if (j - 1 >= 0 && !String.valueOf(serialNumber.charAt(j-1)).equals("组"))
						price.append(C_NUM.get(String.valueOf(serialNumber.charAt(j))));
				}else
					price.append(C_NUM.get(String.valueOf(serialNumber.charAt(j))));
			}else {
				if(!String.valueOf(serialNumber.charAt(j)).equals("(") && !String.valueOf(serialNumber.charAt(j)).equals("（"))
					break;
			}
		}

		return  new StringBuilder(price.toString()).reverse().toString();
	}

	public Map<String,String> getCNum(){
		Map<String,String> C_NUM = new HashMap<>();
		C_NUM.put("一","1");
		C_NUM.put("二","2");
		C_NUM.put("三","3");
		C_NUM.put("四","4");
		C_NUM.put("五","5");
		C_NUM.put("六","6");
		C_NUM.put("七","7");
		C_NUM.put("八","8");
		C_NUM.put("九","9");
		C_NUM.put("十","10");
		return C_NUM;
	}

	public boolean ifCNum(String serialNumber){
		String[] serialNumbers = serialNumber.split(" ");
		serialNumber = serialNumbers[serialNumbers.length-1];
		if (serialNumber.contains("一"))
			return true;
		if (serialNumber.contains("二"))
			return true;
		if (serialNumber.contains("三") && !String.valueOf(serialNumber.charAt(serialNumber.indexOf("三")-1)).equals("组"))
			return true;
		if (serialNumber.contains("四"))
			return true;
		if (serialNumber.contains("五"))
			return true;
		if (serialNumber.contains("六") && !String.valueOf(serialNumber.charAt(serialNumber.indexOf("六")-1)).equals("组"))
			return true;
		if (serialNumber.contains("七"))
			return true;
		if (serialNumber.contains("八"))
			return true;
		if (serialNumber.contains("九"))
			return true;
		if (serialNumber.contains("十"))
			return true;
		return false;
	}
}
