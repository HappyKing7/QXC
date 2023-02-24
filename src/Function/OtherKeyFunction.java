package Function;

import java.util.HashMap;
import java.util.Map;

public class OtherKeyFunction {
	private static final CommonFunction commonFunction = new CommonFunction();

	public String yuan(String serialNumber,Map<String,String> otherMap){
		int position = 0;
		int endFlag = 0;
		for (int i = 0; i < serialNumber.length(); i++) {
			for(Map.Entry<String,String> entry : otherMap.entrySet()){
				if (String.valueOf(serialNumber.charAt(i)).equals(entry.getKey())) {
					position = i;
					endFlag = 1;
					break;
				}
			}
			if (endFlag == 1)
				break;
		}

		StringBuilder price = new StringBuilder();
		Map<String,String> C_NUM = getCNum();
		StringBuilder cPrice = new StringBuilder();
		for (int j = position-1; j >= 0 ; j--) {
			if(Character.isDigit(serialNumber.charAt(j))){
				price.append(serialNumber.charAt(j));
			}else if (C_NUM.get(String.valueOf(serialNumber.charAt(j)))!=null) {
				if(String.valueOf(serialNumber.charAt(j)).equals("三") || String.valueOf(serialNumber.charAt(j)).equals("六")){
					if (j - 1 >= 0 && !String.valueOf(serialNumber.charAt(j-1)).equals("组")){
						price.append(C_NUM.get(String.valueOf(serialNumber.charAt(j))));
					}
				}else
					cPrice.append(serialNumber.charAt(j));
			}else{
				if(!String.valueOf(serialNumber.charAt(j)).equals("(") && !String.valueOf(serialNumber.charAt(j)).equals("（"))
					break;
			}
		}

		if (cPrice.toString().equals("")){
			if (serialNumber.contains("角")){
				price = new StringBuilder((price).toString()).reverse();
				price = new StringBuilder(String.format("%.1f", Float.parseFloat(price.toString()) * 0.1));
				return  price.toString();
			}
			else
				return  new StringBuilder(price.toString()).reverse().toString();
		}
		else{
			return String.valueOf(commonFunction.toNumber(new StringBuilder(cPrice.toString()).reverse().toString()));
		}
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
		C_NUM.put("百","100");
		C_NUM.put("千","1000");
		C_NUM.put("万","10000");
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
