package Frame;

import Enum.*;
import Function.*;
import Bean.*;
import jxl.read.biff.BiffException;
import jxl.write.WriteException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.List;

public class UpdateExcelWindow {
	private final FontEnum fontEnum = new FontEnum();
	private final WarmWindow warmWindow = new WarmWindow();
	private final InputFunction inputFunction = new InputFunction();
	private final CommonFunction commonFunction = new CommonFunction();
	private final SummaryWindow summaryWindow = new SummaryWindow();
	private final ComponentInit componentInit = new ComponentInit();
	private final UpdateExcelFunction updateExcelFunction = new UpdateExcelFunction();

	public JFrame showUpdateExcel(Integer selectOneNo, Integer selectTwoNo, String fileName,
								List<ShowSummaryList> showSummaryLists, GlobalVariable globalVariable,Frame summaryFrame){
		ShowSummaryList fssl = showSummaryLists.get(0);
		ShowSummaryList ssl = showSummaryLists.get(selectOneNo);
		ShowSummary ss = ssl.getShowSummaryList().get(selectTwoNo);

		String no = ssl.getNo();
		String totalMoney = ssl.getTotalMoney();
		String totalPrice = ssl.getTotalPrice();
		String note = ssl.getNote();
		String[] details = ss.getDetail().split(",");
		String serialNumber = ss.getSerialNumber();

		JPanel jPanel = new JPanel();
		jPanel.setLayout(new GridLayout(4,1));

		//序列号
		JTextField serialNumberJF=new JTextField(serialNumber,50);
		serialNumberJF.setFont(fontEnum.updateFont);
		JTextField groupNumberJF=new JTextField(details[0].replace("组",""),5);
		groupNumberJF.setFont(fontEnum.mainFont);

		//单价
		JTextField priceJF=new JTextField(details[1].replace("单价", ""),10);
		priceJF.setFont(fontEnum.updateFont);

		JPanel pricePanel = new JPanel();
		ButtonGroup priceGroup = new ButtonGroup();
		for (PriceEnum priceEnum : PriceEnum.values()) {
			commonFunction.priceButtonSetting(priceEnum,priceGroup,pricePanel,priceJF,globalVariable);
		}

		//倍数
		JTextField timesJF=new JTextField("1",10);
		timesJF.setFont(fontEnum.mainFont);
		timesJF.setVisible(false);

		JComboBox<String> timesComboBox = new JComboBox<>();
		for(TimesEnum timesEnum: TimesEnum.values()){
			timesComboBox.addItem(timesEnum.getLabel());
			timesComboBox.setFont(fontEnum.mainFont);
		}

		//类别
		JTextField typeTwoJF=new JTextField(details[2],10);
		typeTwoJF.setFont(fontEnum.mainFont);
		typeTwoJF.setVisible(false);

		JPanel typeTwoPanel = new JPanel();
		ButtonGroup typeTwoGroup = new ButtonGroup();
		for(TypeTwoEnum typeTwoEnum: TypeTwoEnum.values()){
			JRadioButton typeTwoButton = new JRadioButton(typeTwoEnum.getLabel());
			typeTwoButton.setFont(fontEnum.mainButtonFont);
			typeTwoButton.addActionListener(e -> typeTwoJF.setText(typeTwoButton.getText()));
			if(typeTwoButton.getText().equals(details[2])){
				typeTwoButton.setSelected(true);
			}
			typeTwoGroup.add(typeTwoButton);
			typeTwoPanel.add(typeTwoButton);
		}

		//类型
		JTextField typeJF=new JTextField(details[3],10);
		typeJF.setFont(fontEnum.updateFont);
		typeJF.setVisible(false);

		JComboBox<String> typeComboBox = new JComboBox<>();
		for(TypeEnum typeEnum: TypeEnum.values()){
			if(inputFunction.filterType(typeEnum.getLabel()))
				continue;
			typeComboBox.addItem(typeEnum.getLabel());
			typeComboBox.setFont(fontEnum.mainFont);
		}
		typeComboBox.setSelectedItem(details[3]);

		//备注
		JTextField noteJF=new JTextField(note.replace("备注：",""),30);
		noteJF.setFont(fontEnum.updateFont);

		List<JPanel> updateWindowFirstJPanelList = commonFunction.updateWindowFirstJPanel(serialNumberJF,groupNumberJF,
				typeTwoPanel,pricePanel,priceJF,timesComboBox,typeComboBox);
		//第一行 序列号 + 组数
		JPanel serialNumberPanel= updateWindowFirstJPanelList.get(0);
		//第二行 单价 + 倍数 + 类别 + 类型
		JPanel priceAndTypePanel= updateWindowFirstJPanelList.get(1);
		//第三行 备注
		JPanel notePanel = componentInit.iniJPanel(new JPanel(),"备注",noteJF);

		JComboBox<String> noteComboBox = componentInit.noteQuicklySelect(globalVariable);

		//第四行 按钮
		JPanel updatePanel=new JPanel();
		Button update = new Button("修改");
		updatePanel.setFont(fontEnum.updateFont);
		updatePanel.add(update);

		//添加控件
		JFrame frame=new JFrame("修改序列号");
		jPanel.add(serialNumberPanel);
		if(selectTwoNo == 0){
			notePanel.add(noteComboBox);
			jPanel.add(notePanel);
		}
		jPanel.add(priceAndTypePanel);
		jPanel.add(updatePanel);

		//设置布局
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		if(selectTwoNo == 0)
			frame.setBounds((screenSize.width - 1000) / 2, (screenSize.height - 500) / 2, 1000, 300);
		else
			frame.setBounds((screenSize.width - 1000) / 2, (screenSize.height - 200) / 2, 1000, 200);
		frame.add(jPanel,BorderLayout.NORTH);
		frame.add(new JPanel(),BorderLayout.CENTER);
		frame.setVisible(true);

		//注册监听器
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				frame.dispose();
			}
		});

		commonFunction.comboBoxListener(timesComboBox,timesJF);
		commonFunction.comboBoxListener(typeComboBox,typeJF);
		commonFunction.comboBoxListener(noteComboBox,noteJF);

		update.addActionListener(e -> {
			float oldDetailPrice = inputFunction.moneyRemoveChinese(details[4],"总","元");
			float oldTotalPrice = inputFunction.moneyRemoveChinese(ssl.getTotalPrice(),"总共","元");
			float oldTotalMoney = inputFunction.moneyRemoveChinese(fssl.getTotalMoney(),"总计","元");
			float updateUnitPrice = Float.parseFloat(priceJF.getText())*Integer.parseInt(timesJF.getText());
			float updateTotalPrice = updateUnitPrice * Integer.parseInt(groupNumberJF.getText());

			ssl.setNote("备注：" + noteJF.getText());
			ssl.setTotalPrice(inputFunction.moneyAddChinese(String.format("%.2f",oldTotalPrice - oldDetailPrice + updateTotalPrice),"总共","元"));
			fssl.setTotalMoney(inputFunction.moneyAddChinese(String.format("%.2f",oldTotalMoney - oldDetailPrice + updateTotalPrice),"总计","元"));

			ss.setSerialNumber(serialNumberJF.getText());
			ss.setDetail(groupNumberJF.getText() + "组" + "," + "单价" + updateUnitPrice + "," +
					typeTwoJF.getText() + "," + typeJF.getText() + "," + inputFunction.moneyAddChinese(String.format("%.2f",updateTotalPrice),"总","元"));

			UpdateExcel updateExcel = new UpdateExcel();
			updateExcel.setNo(no);
			updateExcel.setTowNo(selectTwoNo);
			updateExcel.setTotalPrice(ssl.getTotalPrice());
			updateExcel.setTotalMoney(fssl.getTotalMoney());
			updateExcel.setNote(ssl.getNote());
			updateExcel.setSs(ss);
			globalVariable.updateExcelMap.put(no,updateExcel);

			try {
				String filePath = globalVariable.filePath+fileName;
				String result = updateExcelFunction.updateExcel(filePath,globalVariable);
				if(!result.equals("success")){
					warmWindow.warmWindow(result,fontEnum.warmInfoFont);
					return;
				}
			} catch (BiffException | IOException | WriteException biffException) {
				biffException.printStackTrace();
			}
			frame.dispose();
			summaryFrame.dispose();
			summaryWindow.showSummary(showSummaryLists,fileName,globalVariable);
		});

		serialNumberJF.addCaretListener(e -> {
			if(!serialNumberJF.getText().equals("")){
				List<String> groupNumberList = inputFunction.getNumber(serialNumberJF.getText(),globalVariable.functionType);
				int groupNumber = Integer.parseInt(groupNumberList.get(groupNumberList.size()-1));
				groupNumberJF.setText(String.valueOf(groupNumber));
			}
		});

		return frame;
	}
}
