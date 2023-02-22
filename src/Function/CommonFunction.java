package Function;

import Bean.GlobalVariable;
import Enum.*;
import jxl.Sheet;
import jxl.Workbook;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.*;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommonFunction {
	private final FontEnum fontEnum = new FontEnum();
	private final ComponentInit componentInit = new ComponentInit();

	public Boolean quanBaoAndKD(String label){
		if(label.contains(TypeEnum.ZL.getLabel()) && label.length()>2){
			if(!label.contains("全包"))
				return true;
		}
		if(label.contains(TypeEnum.ZS.getLabel()) && label.length()>2){
			if(!label.contains("全包"))
				return true;
		}
		if(label.contains(TypeEnum.KD.getLabel()) && label.length()>2){
			return true;
		}
		return false;
	}

	public JRadioButton priceButtonSetting(PriceEnum priceEnum,ButtonGroup priceGroup,JPanel pricePanel, JTextField priceJF,
								   GlobalVariable globalVariable){
		JRadioButton priceButton = new JRadioButton(String.valueOf(priceEnum.getVal()));
		priceButton.setFont(fontEnum.mainButtonFont);
		priceButton.addActionListener(e -> {
			priceJF.setText(priceButton.getText());
			globalVariable.selectPrice = priceButton.getText();
		});
		priceGroup.add(priceButton);
		pricePanel.add(priceButton);
		return priceButton;
	}

	public JPanel typeAndTimesButton(JTextField typeJF, JTextField timesJF, String type, Integer times, String desc,
									 String timesDesc, JComboBox<String> typeComboBox, JComboBox<String> timesComboBox,
									 JButton confirm,JPanel jPanel){
		Button typeAndTimesButton = new Button(desc);
		typeAndTimesButton.setFont(fontEnum.mainButtonFont);
		typeAndTimesButton.addActionListener(e -> {
			typeJF.setText(type);
			typeComboBox.setSelectedItem(type);
			timesComboBox.setSelectedItem(timesDesc);
			timesJF.setText(String.valueOf(times));
			confirm.doClick();
		});
		jPanel.add(typeAndTimesButton);
		jPanel = componentInit.addSpace(jPanel,1);
		return jPanel;
	}

	public void comboBoxListener(JComboBox<String> comboBox,JTextField jTextField){
		if (Objects.requireNonNull(comboBox.getSelectedItem()).toString().contains("倍"))
			comboBox.addItemListener(e -> jTextField.setText(Objects.requireNonNull(comboBox.getSelectedItem()).
					toString().replace("倍","")));
		else
			comboBox.addItemListener(e -> jTextField.setText(Objects.requireNonNull(comboBox.getSelectedItem()).toString()));

		comboBox.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				comboBox.setPopupVisible(true);
			}
		});
	}

	public List<JPanel> updateWindowFirstJPanel(JTextField serialNumberJF,JTextField groupNumberJF,JPanel typeTwoPanel,
												JPanel pricePanel,JTextField priceJF,JComboBox<String> timesComboBox,
												JComboBox<String> typeComboBox){
		List<JPanel> updateWindowFirstJPanel = new ArrayList<>();
		JPanel serialNumberPanel= componentInit.initJPanel(new JPanel(),"序列号",serialNumberJF);
		serialNumberPanel.add(groupNumberJF);
		//第二行 单价 + 倍数 + 类别 + 类型
		JPanel priceAndTypePanel= componentInit.iniJPanel(new JPanel(),"类别",typeTwoPanel);
		priceAndTypePanel= componentInit.addSpace(priceAndTypePanel,2);
		priceAndTypePanel= componentInit.iniJPanel(priceAndTypePanel,"单价");
		priceAndTypePanel.add(pricePanel);
		priceAndTypePanel= componentInit.iniJPanel(priceAndTypePanel,"",priceJF);
		priceAndTypePanel= componentInit.iniJPanel(priceAndTypePanel,"元");
		priceAndTypePanel= componentInit.iniJPanel(priceAndTypePanel,"倍数",timesComboBox);
		priceAndTypePanel= componentInit.addSpace(priceAndTypePanel,2);
		priceAndTypePanel= componentInit.iniJPanel(priceAndTypePanel,"类型",typeComboBox);

		updateWindowFirstJPanel.add(serialNumberPanel);
		updateWindowFirstJPanel.add(priceAndTypePanel);
		return updateWindowFirstJPanel;
	}

	Map<Character,Integer> number=new HashMap<Character,Integer>(){{
		put('一',1);
		put('二',2);
		put('三',3);
		put('四',4);
		put('五',5);
		put('六',6);
		put('七',7);
		put('八',8);
		put('九',9);
	}};

	HashMap<Character,Integer> digit=new HashMap<Character,Integer>(){{
		put('十',10);
		put('百',100);
		put('千',1000);
		put('万',10000);
		put('亿',100000000);
	}};

	List<Integer> temp=new ArrayList<>();
	public int toNumber(String str){
		int res=0;

		if (str.indexOf("十") == 0){
			res = 10;
			str = str.replace("十","");
		}


		boolean ok=process(str);
		if(ok){
			for(int i:temp){
				res+=i;
			}
		}
		temp=new ArrayList<>();
		return res;
	}

	public boolean process(String input){
		if(input.equals(""))
			return true;
		else if(digit.containsKey(input.charAt(0))){
			if(temp.size()==0 || temp.get(temp.size()-1) >= digit.get(input.charAt(0))){
				return false;
			}
			int cur=0;
			while(temp.size()>=1 && temp.get(temp.size()-1) < digit.get(input.charAt(0))){
				cur+=temp.get(temp.size()-1);
				temp.remove(temp.size()-1);
			}
			temp.add(cur*digit.get(input.charAt(0)));
			return process(input.substring(1));
		}else if(number.containsKey(input.charAt(0))){
			temp.add(number.get(input.charAt(0)));
			return process(input.substring(1));
		}else if(input.charAt(0)=='零'){
			return process(input.substring(1));
		}else{
			return false;
		}
	}

	public StringBuilder getMoneyKeys(Map<String,String> otherMap){
		StringBuilder moneyKeys = new StringBuilder();
		for(Map.Entry<String,String> entry : otherMap.entrySet()){
			moneyKeys.append(entry.getKey()).append("|");
		}
		moneyKeys = new StringBuilder(moneyKeys.substring(0, moneyKeys.length() - 1));
		return moneyKeys;
	}

	public String dealMa(String serialNumber){
		if (serialNumber.contains("码") || serialNumber.contains("徂")){
			serialNumber = serialNumber.replace("徂","组");
			Pattern pattern = Pattern.compile("([一二两三四五六七八九]码[组徂]三)|([组徂]三[一二两三四五六七八九]码)");
			Matcher matcher = pattern.matcher(serialNumber);
			while (matcher.find()){
				serialNumber = serialNumber.replace(matcher.group(),TypeEnum.ZS.getLabel());
			}

			pattern = Pattern.compile("([一二两三四五六七八九]码[组徂]六)|([组徂]六[一二两三四五六七八九]码)");
			matcher = pattern.matcher(serialNumber);
			while (matcher.find()){
				serialNumber = serialNumber.replace(matcher.group(),TypeEnum.ZL.getLabel());
			}
		}
		return serialNumber;
	}

	public List<List<String>> getExpectData(Workbook wrb){
		Sheet sheet = wrb.getSheet(0);
		List<String> inputList = new ArrayList<>();
		List<String> resultList2 = new ArrayList<>();
		for (int i = 1; i < sheet.getRows(); i++) {
			String input = sheet.getCell(1, i).getContents().trim();
			String result = sheet.getCell(2, i).getContents().trim();

			if (!input.equals(""))
				inputList.add(input);
			if(!result.equals(""))
				resultList2.add(result);
		}

		List<List<String>> result = new ArrayList<>();
		result.add(inputList);
		result.add(resultList2);
		return result;
	}

	public void printFailList(List<String> resultList,List<String> resultList2){
		System.out.println(resultList.size());
		System.out.println(resultList2.size());
		for (String s : resultList) {
			System.out.println(s);
		}
		System.out.println();
		for (String s : resultList2) {
			System.out.println(s);
		}
	}
}
