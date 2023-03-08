package Function;

import Bean.*;
import Enum.*;
import Start.input;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AutoRecognitionFunction {
	private static final InputFunction inputFunction = new InputFunction();
	private static final CommonFunction commonFunction = new CommonFunction();
	private static final OtherKeyFunction otherKeyFunction = new OtherKeyFunction();

	public Integer getGroup(List<Key> typeTwoList){
		String groupBuilder = typeTwoList.get(typeTwoList.size()-1).getKey();
		typeTwoList.remove(typeTwoList.size()-1);
		return Integer.valueOf(groupBuilder);
	}

	public StringBuilder getStringBuilder(List<Key> typeTwoList){
		StringBuilder stringBuilder = new StringBuilder(typeTwoList.get(typeTwoList.size()-1).getKey());
		typeTwoList.remove(typeTwoList.size()-1);
		return stringBuilder;
	}

	public List<Key> getTypeTwoList(String input, StringBuilder stringBuilder, int group , Map<String,String> typeTwoMap){
		List<Key> typeTwoList = new ArrayList<>();
		for(Map.Entry<String,String> entry : typeTwoMap.entrySet()){
			if (input.contains(entry.getKey())) {
				Key key = new Key();
				key.setKey(entry.getKey());
				key.setPosition(input.indexOf(entry.getKey()));
				if (typeTwoMap.get(entry.getKey()).equals(TypeTwoEnum.FUCAI.getLabel())){
					if (input.contains("3D") || input.contains("3d")){
						input = input.replace("3D","").replace("3d","");
						String s = stringBuilder.toString();
						group = group - 1;
						if (s.contains("3 "))
							s = s.replace("3 ","");
						else
							s = s.replace(" 3 ","");
						stringBuilder = new StringBuilder(s);
					}

				}
				typeTwoList.add(key);
			}
		}

		Key key = new Key();
		key.setKey(stringBuilder.toString());
		typeTwoList.add(key);

		key = new Key();
		key.setKey(String.valueOf(group));
		typeTwoList.add(key);
		return typeTwoList;
	}

	public List<Key> getTypeList(String input, GlobalVariable globalVariable, Map<String,String> otherMap, Map<String,String> typeMap){
		List<Key> typeList = new ArrayList<>();
		StringBuilder typeStr = new StringBuilder();
		StringBuilder inputBuilder = new StringBuilder(input);
		for(Map.Entry<String,String> entry : typeMap.entrySet()){
			if (inputBuilder.toString().contains(entry.getKey())) {
				if(!typeStr.toString().contains(entry.getValue())){
					Key key = new Key();
					key.setKey(entry.getKey());
					key.setPosition(inputBuilder.indexOf(entry.getKey()));

					if (Character.isDigit(inputBuilder.charAt(inputBuilder.length()-1)) && !inputBuilder.toString().contains("倍")) {
						int otherFlag = 0;
						for(Map.Entry<String,String> entry1 : otherMap.entrySet()){
							if (inputBuilder.toString().contains(entry1.getKey())) {
								otherFlag = 1;
								break;
							}
						}
						if (otherFlag == 0)
							inputBuilder.append("倍");
					}

					if (key.getPosition()-1 >= 0 && globalVariable.spaceSwitchMode == 1){
						if (Character.isDigit(inputBuilder.charAt(key.getPosition()-1))){
							int savePosition = key.getPosition()-1;
							StringBuilder n = new StringBuilder();
							for (int i = savePosition; i >= 0; i--) {
								if (Character.isDigit(inputBuilder.charAt(i)))
									n.append(inputBuilder.charAt(i));
								else
									break;
							}
							if ((typeMap.get(entry.getKey()).equals(TypeEnum.ZHIXUAN.getLabel()) || typeMap.get(entry.getKey()).equals(TypeEnum.ZUXUAN.getLabel()))
									&& n.length() <=2){
								if (key.getPosition()+1 < inputBuilder.length()){
									String nextStr = String.valueOf(inputBuilder.charAt(key.getPosition()+1));
									if (typeMap.get(entry.getKey()).equals(TypeEnum.ZUXUAN.getLabel()) && (nextStr.equals("三") || nextStr.equals("六")))
										continue;
								}
								inputBuilder = new StringBuilder(inputBuilder.substring(0, key.getPosition()) + "倍" + inputBuilder.substring(key.getPosition()));
							}
						}
					}
					typeList.add(key);
					typeStr.append(entry.getValue());
				}
				if((entry.getValue().equals(TypeEnum.ZL.getLabel()) || entry.getValue().equals(TypeEnum.ZS.getLabel()))
						&& (inputBuilder.toString().split("组").length-1 == 1  ||
						(inputBuilder.toString().contains(TypeEnum.ZS.getLabel()) && inputBuilder.toString().contains(TypeEnum.ZL.getLabel())))) {
					for (int i = typeList.size()-1; i >= 0; i--) {
						Key key = typeList.get(i);
						if (typeMap.get(key.getKey()).equals(TypeEnum.ZUXUAN.getLabel()))
							typeList.remove(key);
					}
				}
			}
		}

		Key key = new Key();
		key.setKey(inputBuilder.toString());
		typeList.add(key);
		return typeList;
	}

	public List<Key> getTimesList(String input, Map<String,String> otherMap, Map<String,String> timesMap){
		List<Key> timesList = new ArrayList<>();
		StringBuilder timesStr = new StringBuilder();
		List<String> tempTimesList = new ArrayList<>();
		for(Map.Entry<String,String> entry : timesMap.entrySet()){
			if (input.contains(entry.getKey())) {
				int position = input.indexOf(entry.getKey());

				if(entry.getKey().equals("三")){

					if (position - 1 >= 0 && String.valueOf(input.charAt(position-1)).equals("组"))
						continue;
					if (position - 2 >= 0 && String.valueOf(input.charAt(position-1)).equals("列") &&
							String.valueOf(input.charAt(position-2)).equals("排"))
						continue;
				}

				if(entry.getKey().equals("六")){
					if (position - 1 >= 0 && String.valueOf(input.charAt(position-1)).equals("组")){
						continue;
					}
				}

				if(!timesStr.toString().contains(entry.getValue())){
					Key key = new Key();
					key.setPosition(input.indexOf(entry.getKey()));
					StringBuilder num = new StringBuilder(entry.getKey());
					position = key.getPosition();
					if (position+1<input.length()-1 && otherMap.get(String.valueOf(input.charAt(position+1)))!=null){
						continue;
					}
					if (commonFunction.toNumber(num.toString()) != 0){
						List<Integer> indexList = commonFunction.findKetPosition(entry.getKey(),input);
						if (indexList.size() > 1){
							for (Integer index : indexList){
								key = new Key();
								num = new StringBuilder(entry.getKey());
								position = index;
								key.setPosition(index);
								if (addTimeList(input, otherMap, timesList, key, num, position)) break;
							}
						}else {
							if (addTimeList(input, otherMap, timesList, key, num, position)) continue;
						}
					}else {
						if (Character.isDigit(input.charAt(position-1)) && input.contains("倍")){
							List<Integer> indexList = commonFunction.findKetPosition(entry.getKey(),input);
							if (indexList.size() > 1){
								for (Integer index : indexList){
									key = new Key();
									num = new StringBuilder(entry.getKey());
									num = new StringBuilder(num.toString().replace("倍", ""));
									position = index;
									key.setPosition(index);
									addTimesList(input, timesMap, timesList, tempTimesList, key, num, position);
								}
							}else {
								num = new StringBuilder(num.toString().replace("倍", ""));
								addTimesList(input, timesMap, timesList, tempTimesList, key, num, position);
							}
						}else {
							key.setKey(num.toString());
							timesList.add(key);
						}
					}
					timesStr.append(entry.getValue());
				}
			}
		}

		if (tempTimesList.size()!=0){
			for (String s : tempTimesList)
				timesMap.put(s,s);
		}

		for (Key key : timesList){
			if (timesMap.get(key.getKey())==null)
				timesMap = addTimesMap(key,timesMap);
		}

		return timesList;
	}

	public TimesAndPrice getTimesAndPrice(String input,StringBuilder stringBuilder,Integer group,float price,
										  List<Key> otherList,Map<String,String> otherMap){
		TimesAndPrice timesAndPrice = new TimesAndPrice();
		List<Float> priceList = new ArrayList<>();
		int geFlag = 0;
		int priceFlag = 0;
		if (otherList.size()!=0){
			if (otherList.size() == 1){
				String result = inputFunction.otherType(otherMap.get(otherList.get(0).getKey()),input,otherMap);
				if(result.split("--")[0].equals(KeyTypeEnum.TIMES.getLabel())) {
					String money = result.split("--")[1];
					String saveMoney = money;

					if (money.contains("0.")){
						int i = stringBuilder.indexOf(" " + 0 + " ");
						int j = stringBuilder.indexOf(0 + " ");
						if (i != -1) {
							if (Math.abs(stringBuilder.indexOf("0") - i) < Math.abs(stringBuilder.lastIndexOf("0") - i)){
								stringBuilder.delete(stringBuilder.indexOf("0"),stringBuilder.indexOf("0")+2);
							}else {
								stringBuilder.delete(stringBuilder.lastIndexOf("0"),stringBuilder.lastIndexOf("0")+2);
							}
							group = group -1;
						}
						if (j == 0){
							if (Math.abs(stringBuilder.indexOf("0") - j) < Math.abs(stringBuilder.lastIndexOf("0") - j)){
								stringBuilder.delete(stringBuilder.indexOf("0"),stringBuilder.indexOf("0")+2);
							}else {
								stringBuilder.delete(stringBuilder.lastIndexOf("0"),stringBuilder.lastIndexOf("0")+2);
							}
							group = group -1;
						}
					}

					if (money.contains(".")){
						money = money.replace(".","");
						int moneyTemp = Integer.parseInt(money);
						money = String.valueOf(moneyTemp);
					}

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

					price = Float.parseFloat(saveMoney);
					priceFlag = 1;
				}
			} else {
				String inputStr = input;
				for (Key key : otherList){
					String result = inputFunction.otherType(otherMap.get(key.getKey()),inputStr,otherMap);
					String money;
					if(result.split("--")[0].equals(KeyTypeEnum.TIMES.getLabel())) {
						if (result.split("--").length == 1)
							money = String.valueOf(priceList.get(priceList.size()-1));
						else {
							money = result.split("--")[1];
							int i = stringBuilder.indexOf(" " + money + " ");
							if (i != -1) {
								stringBuilder.delete(i, i + money.length() + 1);
								group = group -1;
							}
						}
						priceList.add(Float.parseFloat(money));
					}
					if (geFlag == 0 && String.valueOf(input.charAt(input.indexOf(key.getKey())-2)).equals("各")){
						geFlag = 1;
						continue;
					}
					int position = input.indexOf(key.getKey());
					inputStr = input.substring(0,position) + input.substring(position+1);
				}
			}
		}

		timesAndPrice.setPriceFlag(priceFlag);
		timesAndPrice.setGroup(group);
		timesAndPrice.setPrice(price);
		timesAndPrice.setPriceList(priceList);
		return timesAndPrice;
	}
	public List<String> deleteBei(String input,StringBuilder stringBuilder,Integer group){
		List<String> resultList = new ArrayList<>();
		String inputStr = input;
		while (inputStr.contains("倍")){
			StringBuilder s = new StringBuilder();
			int p = inputStr.indexOf("倍") - 1;
			while(p>=0 && Character.isDigit(inputStr.charAt(p))){
				s.append(inputStr.charAt(p));
				p = p - 1;
			}
			s = new StringBuilder(new StringBuilder(s.toString()).reverse().toString());
			inputStr = inputStr.substring(0,inputStr.indexOf("倍")) + inputStr.substring(inputStr.indexOf("倍")+1);
			if (Math.abs(inputStr.indexOf(s.toString()) - p) < Math.abs(inputStr.lastIndexOf(s.toString()) - p)){
				inputStr = inputStr.substring(0,inputStr.indexOf(s.toString())) + inputStr.substring(inputStr.indexOf(s.toString())+1);
				p = stringBuilder.indexOf(s.toString());
			}else{
				inputStr = inputStr.substring(0,inputStr.lastIndexOf(s.toString())) + inputStr.substring(inputStr.lastIndexOf(s.toString()));
				p = stringBuilder.lastIndexOf(s.toString());
			}

			stringBuilder.delete(p,p+s.length()+1);
			group = group -1;
		}

		resultList.add(String.valueOf(group));
		resultList.add(stringBuilder.toString());
		return resultList;
	}

	public List<Key> getTypeByNumberLength(StringBuilder stringBuilder, List<Key> typeList){
		Key key = new Key();
		key.setPosition(0);
		if (stringBuilder.toString().split(" ")[0].length()==1){
			key.setKey(TypeEnum.DD.getLabel());
		}else if (stringBuilder.toString().split(" ")[0].length()==2){
			key.setKey(TypeEnum.SF.getLabel());
		}else if (stringBuilder.toString().split(" ")[0].length()==3){
			key.setKey(TypeEnum.ZHIXUAN.getLabel());
		}else{
			key.setKey(TypeEnum.ZL.getLabel());
		}
		typeList.add(key);
		return typeList;
	}

	public Float setPrice(StringBuilder stringBuilder,Integer priceFlag,float price,List<Key> typeList,
						  Map<String,String> typeMap){
		if (stringBuilder.toString().split(" ")[0].length() == 3 && priceFlag == 0){
			for (Key key : typeList){
				String type = typeMap.get(key.getKey());
				if (!type.equals(TypeEnum.ZHIXUAN.getLabel()) && !type.equals(TypeEnum.ZUXUAN.getLabel()) &&
						!type.equals(TypeEnum.ZL.getLabel()))
					price = PriceEnum.TEN.getVal();
				else
					price = PriceEnum.TOW.getVal();
			}
		}else if (stringBuilder.toString().split(" ")[0].length() != 3 && priceFlag == 0){
			price = PriceEnum.TEN.getVal();
		}
		return price;
	}

	public Ticket addTicket(Integer priceFlag,StringBuilder stringBuilder,String typeTwo,int group,String times,float price,TicketList tickets,
							List<Key> typeList,List<Key> timesList,List<Key> otherList,List<Float> priceList,
							Map<String,String> timesMap,Map<String,String> typeMap){
		Ticket ticket = new Ticket();
		if(typeList.size() == 1){
			ticket = new Ticket();
			if(timesList.size() != 0) {
				float money;
				if (priceFlag == 0){
					money = price * Float.parseFloat(timesMap.get(timesList.get(0).getKey()));
				}else {
					money = Float.parseFloat(timesMap.get(timesList.get(0).getKey()));
				}
				ticket = inputFunction.addTicket(ticket,typeMap.get(typeList.get(0).getKey()),typeTwo,money,
						stringBuilder,group,times,tickets);
			}
			else{
				if (priceList.size() == 0)
					ticket = inputFunction.addTicket(ticket,typeMap.get(typeList.get(0).getKey()),typeTwo,price,
							stringBuilder,group,times,tickets);
				else {
					ticket = inputFunction.addTicket(ticket,typeMap.get(typeList.get(0).getKey()),typeTwo,
							priceList.get(0), stringBuilder,group,times,tickets);
				}
			}
		}else {
			if (timesList.size()>1 && otherList.size()!=0){
				typeList = typeList.stream().sorted(Comparator.comparing(Key :: getPosition)).collect(Collectors.toList());
				timesList = timesList.stream().sorted(Comparator.comparing(Key :: getPosition)).collect(Collectors.toList());
				Float money;
				for (int i = 0; i < typeList.size(); i++) {
					money = price * Float.parseFloat(timesMap.get(timesList.get(i).getKey()));
					ticket = new Ticket();
					Key key = typeList.get(i);
					ticket = inputFunction.addTicket(ticket,typeMap.get(key.getKey()),typeTwo,money,stringBuilder,group,times,tickets);
				}
			}else if (timesList.size() <= 1){
				if (timesList.size() == 1)
					price = price * Float.parseFloat(timesMap.get(timesList.get(0).getKey()));
				if (priceList.size() != 0)
					typeList = typeList.stream().sorted(Comparator.comparing(Key :: getPosition)).collect(Collectors.toList());
				for (int i = 0; i < typeList.size(); i++) {
					ticket = new Ticket();
					Key key = typeList.get(i);
					if (priceList.size() != 0)
						ticket = inputFunction.addTicket(ticket,typeMap.get(key.getKey()),typeTwo,priceList.get(i),
								stringBuilder,group,times,tickets);
					else
						ticket = inputFunction.addTicket(ticket,typeMap.get(key.getKey()),typeTwo,price,stringBuilder,
								group,times,tickets);
				}
			}else {
				typeList = typeList.stream().sorted(Comparator.comparing(Key :: getPosition)).collect(Collectors.toList());
				timesList = timesList.stream().sorted(Comparator.comparing(Key :: getPosition)).collect(Collectors.toList());

				for (int i = 0; i < typeList.size(); i++) {
					ticket = new Ticket();
					float money = price * Float.parseFloat(timesMap.get(timesList.get(i).getKey()));
					ticket = inputFunction.addTicket(ticket,typeMap.get(typeList.get(i).getKey()),typeTwo,money,
							stringBuilder,group,times,tickets);
				}
			}
		}
		return ticket;
	}

	public Map<String,String> addTimesMap(Key key,Map<String,String> timesMap){
		timesMap.put(key.getKey(),String.valueOf(commonFunction.toNumber(key.getKey())));
		return timesMap;
	}

	public void addTimesList(String input, Map<String, String> timesMap, List<Key> timesList, List<String> tempTimesList, Key key, StringBuilder num, int position) {
		for (int i = position - 1; i >= 0; i--) {
			if (!Character.isDigit(input.charAt(i)))
				break;
			num.append(input.charAt(i));
		}
		key.setKey(new StringBuilder(num.toString()).reverse().toString());
		timesList.add(key);
		if (timesMap.get(num.toString()) == null)
			tempTimesList.add(key.getKey());
	}

	public boolean addTimeList(String input, Map<String, String> otherMap, List<Key> timesList, Key key, StringBuilder num, int position) {
		for (int i = position + 1; i < input.length(); i++) {
			if (commonFunction.toNumber(String.valueOf(input.charAt(i))) == 0)
				break;
			num.append(input.charAt(i));
		}

		int bPosition = position - 1;
		int aPosition = position + num.length();
		if (bPosition < 0 || aPosition >= input.length()) {
			key.setKey(num.toString());
			timesList.add(key);
			return true;
		}
		if (otherMap.get(String.valueOf(input.charAt(aPosition))) != null &&
				otherMap.get(String.valueOf(input.charAt(aPosition))).equals("元"))
			return true;
		if (commonFunction.toNumber(String.valueOf(input.charAt(bPosition))) == 0 && commonFunction.toNumber(String.valueOf(input.charAt(aPosition))) == 0) {
			key.setKey(num.toString());
			timesList.add(key);
		}
		return false;
	}
}
