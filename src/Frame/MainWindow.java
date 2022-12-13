package Frame;

import Enum.*;
import Start.*;
import Bean.*;

import javax.swing.*;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class MainWindow {
	private static FontEnum fontEnum = new FontEnum();
	private static JPanelInit jPanelInit = new JPanelInit();
	private static Function function = new Function();
	private static WarmWindow warmWindow = new WarmWindow();
	private static OneSummaryWindow oneSummaryWindow = new OneSummaryWindow();
	private static TodaySummaryWindow todaySummaryWindow = new TodaySummaryWindow();

	public void showMainFrame(int mode, GlobalVariable globalVariable){
		//初始化
		globalVariable.mainFrame.removeAll();
		if(mode == ModeTypeEnum.CREATE.getVal()){
			globalVariable.ticketsNo.set(0);
			globalVariable.tickets = new TicketList();
		}

		//图形化
		//输入框
		JPanel jPanel = new JPanel();
		jPanel.setLayout(new GridLayout(4,1));
		//序列号
		JTextField ztSerialNumberJF=new JTextField(50);
		ztSerialNumberJF.setFont(fontEnum.mainFont);
		JTextField sdSerialNumberJF=new JTextField(50);
		sdSerialNumberJF.setFont(fontEnum.mainFont);
		JTextField groupNumberJF=new JTextField(5);
		groupNumberJF.setFont(fontEnum.mainFont);
		//单价
		JTextField priceJF=new JTextField("2",10);
		priceJF.setFont(fontEnum.mainFont);

		Choice choicePrice = new Choice();
		for(PriceEnum priceEnum: PriceEnum.values()){
			choicePrice.add(String.valueOf(priceEnum.getVal()));
			choicePrice.setFont(fontEnum.mainFont);
		}
		choicePrice.select(String.valueOf(PriceEnum.TOW));

		JTextField timesJF=new JTextField("1",10);
		timesJF.setFont(fontEnum.mainFont);
		timesJF.setVisible(false);

		Choice choiceTimes = new Choice();
		for(TimesEnum timesEnum: TimesEnum.values()){
			choiceTimes.add(timesEnum.getLabel());
			choiceTimes.setFont(fontEnum.mainFont);
		}
		//类型
		JTextField typeJF=new JTextField("直选",10);
		typeJF.setFont(fontEnum.mainFont);
		typeJF.setVisible(false);

		Choice choiceType = new Choice();
		for(TypeEnum typeEnum: TypeEnum.values()){
			choiceType.add(typeEnum.getLabel());
			choiceType.setFont(fontEnum.mainFont);
		}
		//结果
		JTextField resultJF=new JTextField(50);
		resultJF.setFont(fontEnum.mainFont);
		resultJF.setDisabledTextColor(Color.BLACK);
		resultJF.setEnabled(false);

/*		JPanel ztSerialNumberPanel=jPanelInit.initJPanel("粘贴序列号",ztSerialNumberJF);
		Button clean = new Button("清空");
		clean.setFont(mainFont);
		ztSerialNumberPanel.add(clean);*/

		//第一行 序列号 + 组数显示 + 清空按钮
		JPanel sdSerialNumberPanel=jPanelInit.initJPanel("输入序列号",sdSerialNumberJF);
		Button clean = new Button("清空");
		clean.setFont(fontEnum.mainFont);
		sdSerialNumberPanel.add(groupNumberJF);
		sdSerialNumberPanel.add(clean);

		//第二行 单价 + 类型
		JPanel priceAndTypePanel=jPanelInit.initJPanel("类型","单价","倍数",
				choicePrice,choiceType,choiceTimes,priceJF);
		//JPanel resultPanel=jPanelInit.initJPanel("结果",resultJF);

		//第三行 提交按钮 + 查看今日汇总按钮
		JPanel buttonPanel=new JPanel();
		Button confirm = new Button("确定");
		Button showTodaySummary = new Button("查看今日汇总");
		buttonPanel.add(confirm);
		buttonPanel.add(showTodaySummary);
		buttonPanel.setFont(fontEnum.mainButtonFont);

		//第四行 当前数据
		JPanel titlePanel = new JPanel();
		JLabel titleLabel =new JLabel("当前数据");
		titleLabel.setFont(fontEnum.oneSummaryPlainFont);
		titlePanel.add(titleLabel);

		//保留输入信息
		if(globalVariable.tickets.getTicketList() != null){
			Ticket ticket = globalVariable.tickets.getTicketList().get(globalVariable.tickets.getTicketList().size()-1);
			sdSerialNumberJF.setText(ticket.getSerialNumber());
			priceJF.setText(String.valueOf(ticket.getTotalPrice() / ticket.getGroupNum() / ticket.getTimes()));
			choicePrice.select(globalVariable.selectPrice);
			timesJF.setText(String.valueOf(ticket.getTimes()));
			choiceTimes.select(TimesEnum.getValByVal(Integer.valueOf(timesJF.getText())));
			typeJF.setText(ticket.getType());
			choiceType.select(ticket.getType());
			groupNumberJF.setText(String.valueOf(ticket.getGroupNum()));
		}

		//插入组件
		//jPanel.add(ztSerialNumberPanel);
		jPanel.add(sdSerialNumberPanel);
		jPanel.add(priceAndTypePanel);
		//jPanel.add(resultPanel);
		jPanel.add(buttonPanel);

		//当前汇总框
		JPanel showOneSummaryJPanel = new JPanel();
		if(globalVariable.tickets.getTicketList() != null){
			jPanel.add(titlePanel);
			showOneSummaryJPanel = oneSummaryWindow.showOneSummaryFrame(showOneSummaryJPanel,globalVariable);
		}
		globalVariable.mainFrame.add(showOneSummaryJPanel,BorderLayout.CENTER);

		//设置布局
		globalVariable.mainFrame.add(jPanel,BorderLayout.NORTH);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Rectangle bounds = new Rectangle(screenSize);
		globalVariable.mainFrame.setBounds(bounds);
		globalVariable.mainFrame.setVisible(true);

		//注册监听器
		globalVariable.mainFrame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});

		choicePrice.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				Choice choice = (Choice) e.getItemSelectable();
				priceJF.setText(choice.getSelectedItem());
				globalVariable.selectPrice = choice.getSelectedItem();
			}
		});

		choiceTimes.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				Choice choice = (Choice) e.getItemSelectable();
				timesJF.setText(String.valueOf(TimesEnum.getValByLabel(choice.getSelectedItem())));
			}
		});

		choiceType.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				Choice choice = (Choice) e.getItemSelectable();
				typeJF.setText(choice.getSelectedItem());
			}
		});

		clean.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				showMainFrame(ModeTypeEnum.CREATE.getVal(),globalVariable);
			}
		});

		ztSerialNumberJF.addCaretListener(new CaretListener() {
			@Override
			public void caretUpdate(CaretEvent e) {
				if(!ztSerialNumberJF.getText().equals("")){
					String serialNumber = ztSerialNumberJF.getText();
					String inputPrice = priceJF.getText();
					String times = timesJF.getText();
					String type = typeJF.getText();
					String output = function.getNumber(globalVariable.tickets,globalVariable.alllistNo,globalVariable.ticketsNo,
							serialNumber,type,inputPrice,times);
					if(output.equals("fail")){
						warmWindow.warmWindow("请输入有效数字",fontEnum.warmInfoFont);
					}else {
						resultJF.setText(output);
						showMainFrame(ModeTypeEnum.UPDATE.getVal(),globalVariable);
					}
				}
			}
		});

		confirm.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String serialNumber = sdSerialNumberJF.getText();
				String inputPrice = priceJF.getText();
				String times = timesJF.getText();
				String type = typeJF.getText();
				String output = function.getNumber(globalVariable.tickets,globalVariable.alllistNo,globalVariable.ticketsNo,
						serialNumber,type,inputPrice,times);
				if(output.equals("fail")){
					warmWindow.warmWindow("请输入有效数字",fontEnum.warmInfoFont);
				}else {
					resultJF.setText(output);
					showMainFrame(ModeTypeEnum.UPDATE.getVal(),globalVariable);
				}
			}
		});

		showTodaySummary.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				SimpleDateFormat sf= new SimpleDateFormat("yyyy-MM-dd");
				Date date = new Date();
				String nowDate= sf.format(date);
				List<ShowSummaryList> showSummaryLists = function.readTodayExcel(globalVariable.filePath,nowDate);
				if(showSummaryLists == null)
				{
					warmWindow.warmWindow(nowDate+"还没有汇总文件",fontEnum.warmInfoFont);
				}else{
					todaySummaryWindow.showTodaySummary(showSummaryLists,globalVariable);
				}
			}
		});

		sdSerialNumberJF.addCaretListener(new CaretListener() {
			@Override
			public void caretUpdate(CaretEvent e) {
				if(!sdSerialNumberJF.equals("")){
					int groupNumber = function.getNumber(sdSerialNumberJF.getText());
					groupNumberJF.setText(String.valueOf(groupNumber));
				}
			}
		});
	}
}
