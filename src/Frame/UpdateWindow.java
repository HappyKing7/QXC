package Frame;

import Enum.*;
import Function.*;
import Bean.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.Objects;

public class UpdateWindow {
	private final FontEnum fontEnum = new FontEnum();
	private final InputFunction inputFunction = new InputFunction();
	private final CommonFunction commonFunction = new CommonFunction();
	private final OneSummaryWindow oneSummaryWindow = new OneSummaryWindow();
	private JPanel showOneSummaryJPanel = new JPanel();

	public JFrame showUpdateFrame(String ticketsNo, GlobalVariable globalVariable,JPanel showOneSummaryPanel){
		Ticket ticket = globalVariable.tickets.getTicketList().get(Integer.parseInt(ticketsNo));
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
		JTextField typeTwoJF=new JTextField(ticket.getTypeTwo(),10);
		typeTwoJF.setFont(fontEnum.mainFont);
		typeTwoJF.setVisible(false);

		JPanel typeTwoPanel = new JPanel();
		ButtonGroup typeTwoGroup = new ButtonGroup();
		for(TypeTwoEnum typeTwoEnum: TypeTwoEnum.values()){
			JRadioButton typeTwoButton = new JRadioButton(typeTwoEnum.getLabel());
			typeTwoButton.setFont(fontEnum.mainButtonFont);
			typeTwoButton.addActionListener(e -> typeTwoJF.setText(typeTwoButton.getText()));
			if(typeTwoButton.getText().equals(ticket.getTypeTwo())){
				typeTwoButton.setSelected(true);
			}
			typeTwoGroup.add(typeTwoButton);
			typeTwoPanel.add(typeTwoButton);
		}

		//类型
		JTextField typeJF=new JTextField(ticket.getType(),10);
		typeJF.setFont(fontEnum.updateFont);
		typeJF.setVisible(false);

		JComboBox<String> typeComboBox = new JComboBox<>();
		for(TypeEnum typeEnum: TypeEnum.values()){
			if(inputFunction.filterType(typeEnum.getLabel()))
				continue;
			typeComboBox.addItem(typeEnum.getLabel());
			typeComboBox.setFont(fontEnum.mainFont);
		}
		typeComboBox.setSelectedItem(ticket.getType());

		//结果
		JTextField resultJF=new JTextField(30);
		resultJF.setDisabledTextColor(Color.BLACK);
		resultJF.setEnabled(false);

		List<JPanel> updateWindowFirstJPanelList = commonFunction.updateWindowFirstJPanel(serialNumberJF,groupNumberJF,
				typeTwoPanel,pricePanel,priceJF,timesComboBox,typeComboBox);
		//第一行 序列号 + 组数
		JPanel serialNumberPanel= updateWindowFirstJPanelList.get(0);
		//第二行 单价 + 倍数 + 类别 + 类型
		JPanel priceAndTypePanel= updateWindowFirstJPanelList.get(1);
		//第三行 按钮
		JPanel updatePanel=new JPanel();
		Button update = new Button("修改");
		updatePanel.setFont(fontEnum.updateFont);
		updatePanel.add(update);

		//添加控件
		JFrame frame=new JFrame("修改序列号");
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

		timesComboBox.addItemListener(e -> timesJF.setText(Objects.requireNonNull(timesComboBox.getSelectedItem()).toString().replace("倍","")));
		timesComboBox.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				timesComboBox.setPopupVisible(true);
			}
		});

		typeComboBox.addItemListener(e -> typeJF.setText(Objects.requireNonNull(typeComboBox.getSelectedItem()).toString()));
		typeComboBox.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				typeComboBox.setPopupVisible(true);
			}
		});

		update.addActionListener(e -> {
			Ticket ticket1 = globalVariable.tickets.getTicketList().get(Integer.parseInt(ticketsNo));
			ticket1.setSerialNumber(serialNumberJF.getText());
			ticket1.setGroupNum(ticket1.getSerialNumber().split(" ").length);
			ticket1.setUnitPrice(Float.parseFloat(priceJF.getText())*Integer.parseInt(timesJF.getText()));
			ticket1.setTypeTwo(typeTwoJF.getText());
			ticket1.setType(typeJF.getText());
			ticket1.setTotalPrice(ticket1.getUnitPrice() * ticket1.getGroupNum());
			frame.dispose();
			showOneSummaryJPanel = oneSummaryWindow.showOneSummaryFrame(showOneSummaryJPanel,globalVariable);
			globalVariable.mainFrame.revalidate();
			//mainWindow.showMainFrame(ModeTypeEnum.UPDATE.getVal(),globalVariable);
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
