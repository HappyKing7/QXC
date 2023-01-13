package Frame;

import Enum.*;
import Start.*;
import Bean.*;
import Start.ComponentInit;

import javax.swing.*;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class UpdateWindow {
	private static FontEnum fontEnum = new FontEnum();
	private static Function function = new Function();
	private static ComponentInit componentInit = new ComponentInit();
	private static MainWindow mainWindow = new MainWindow();
	private static OneSummaryWindow oneSummaryWindow = new OneSummaryWindow();
	private JPanel showOneSummaryJPanel = new JPanel();

	public void showUpdateFrame(String ticketsNo, GlobalVariable globalVariable,JPanel showOneSummaryPanel,JPanel titlePanel){
		Ticket ticket = globalVariable.tickets.getTicketList().get(Integer.valueOf(ticketsNo));
		showOneSummaryJPanel = showOneSummaryPanel;
		JPanel jPanel = new JPanel();
		jPanel.setLayout(new GridLayout(3,1));

		//序列号
		JTextField serialNumberJF=new JTextField(ticket.getSerialNumber(),50);
		serialNumberJF.setFont(fontEnum.updateFont);
		JTextField groupNumberJF=new JTextField(String.valueOf(ticket.getGroupNum()),5);
		groupNumberJF.setFont(fontEnum.mainFont);

		//单价
		JTextField priceJF=new JTextField(String.valueOf(ticket.getUnitPrice()),10);
		priceJF.setFont(fontEnum.updateFont);

		JPanel pricePanel = new JPanel();
		ButtonGroup priceGroup = new ButtonGroup();
		for (PriceEnum priceEnum : PriceEnum.values()) {
			JRadioButton priceButton = new JRadioButton(String.valueOf(priceEnum.getVal()));
			priceButton.setFont(fontEnum.mainButtonFont);
			priceButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					priceJF.setText(priceButton.getText());
					globalVariable.selectPrice = priceButton.getText();
				}
			});
			priceGroup.add(priceButton);
			pricePanel.add(priceButton);
		}

		//倍数
		JTextField timesJF=new JTextField("1",10);
		timesJF.setFont(fontEnum.mainFont);
		timesJF.setVisible(false);

		JComboBox timesComboBox = new JComboBox();
		for(TimesEnum timesEnum: TimesEnum.values()){
			timesComboBox.addItem(timesEnum.getLabel());
			timesComboBox.setFont(fontEnum.mainFont);
		}

		//类别
		JTextField typeTwoJF=new JTextField(ticket.getTypeTwo(),10);
		typeTwoJF.setFont(fontEnum.mainFont);
		typeTwoJF.setVisible(false);

		JPanel typeTwoPanel = new JPanel();
		List<JRadioButton> typeTwoList = new ArrayList<>();
		ButtonGroup typeTwoGroup = new ButtonGroup();
		for(TypeTwoEnum typeTwoEnum: TypeTwoEnum.values()){
			JRadioButton typeTwoButton = new JRadioButton(typeTwoEnum.getLabel());
			typeTwoButton.setFont(fontEnum.mainButtonFont);
			typeTwoButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					typeTwoJF.setText(typeTwoButton.getText());
				}
			});
			if(typeTwoButton.getText().equals(ticket.getTypeTwo())){
				typeTwoButton.setSelected(true);
			}
			typeTwoList.add(typeTwoButton);
			typeTwoGroup.add(typeTwoButton);
			typeTwoPanel.add(typeTwoButton);
		}

		//类型
		JTextField typeJF=new JTextField(ticket.getType(),10);
		typeJF.setFont(fontEnum.updateFont);
		typeJF.setVisible(false);

		JComboBox typeComboBox = new JComboBox();
		for(TypeEnum typeEnum: TypeEnum.values()){
			if(typeEnum.getLabel().equals(TypeEnum.ALL.getLabel())){
				continue;
			}
			if(typeEnum.getLabel().equals(TypeEnum.KD.getLabel())){
				continue;
			}
			if(typeEnum.getLabel().equals(TypeEnum.HZ.getLabel())){
				continue;
			}
			if(typeEnum.getLabel().equals(TypeEnum.FS.getLabel())){
				continue;
			}
			typeComboBox.addItem(typeEnum.getLabel());
			typeComboBox.setFont(fontEnum.mainFont);
		}
		typeComboBox.setSelectedItem(ticket.getType());

		//结果
		JTextField resultJF=new JTextField(30);
		resultJF.setDisabledTextColor(Color.BLACK);
		resultJF.setEnabled(false);

		//第一行 序列号 + 组数
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
		//第三行 按钮
		JPanel updatePanel=new JPanel();
		Button update = new Button("修改");
		updatePanel.setFont(fontEnum.updateFont);
		updatePanel.add(update);

		//添加控件
		Frame frame=new Frame("修改序列号");
		jPanel.add(serialNumberPanel);
		jPanel.add(priceAndTypePanel);
		jPanel.add(updatePanel);

		//设置布局
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
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

		timesComboBox.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				timesJF.setText(timesComboBox.getSelectedItem().toString().replace("倍",""));
			}
		});
		timesComboBox.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				timesComboBox.setPopupVisible(true);
			}
		});

		typeComboBox.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				typeJF.setText(typeComboBox.getSelectedItem().toString());
			}
		});
		typeComboBox.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				typeComboBox.setPopupVisible(true);
			}
		});

		update.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Ticket ticket = globalVariable.tickets.getTicketList().get(Integer.valueOf(ticketsNo));
				ticket.setSerialNumber(serialNumberJF.getText());
				ticket.setGroupNum(ticket.getSerialNumber().split(" ").length);
				ticket.setUnitPrice(Float.valueOf(priceJF.getText())*Integer.valueOf(timesJF.getText()));
				ticket.setTypeTwo(typeTwoJF.getText());
				ticket.setType(typeJF.getText());
				ticket.setTotalPrice(ticket.getUnitPrice() * ticket.getGroupNum());
				frame.dispose();
				showOneSummaryJPanel = oneSummaryWindow.showOneSummaryFrame(showOneSummaryJPanel,globalVariable);
				globalVariable.mainFrame.revalidate();
				//mainWindow.showMainFrame(ModeTypeEnum.UPDATE.getVal(),globalVariable);
			}
		});

		serialNumberJF.addCaretListener(new CaretListener() {
			@Override
			public void caretUpdate(CaretEvent e) {
				if(!serialNumberJF.equals("")){
					List<String> groupNumberList = function.getNumber(serialNumberJF.getText(),globalVariable.functionType);
					int groupNumber = Integer.valueOf(groupNumberList.get(groupNumberList.size()-1));
					groupNumberJF.setText(String.valueOf(groupNumber));
				}
			}
		});
	}
}
