package Frame;

import Enum.*;
import Function.InputFunction;
import Bean.*;
import Function.ComponentInit;
import jxl.read.biff.BiffException;
import jxl.write.WriteException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

public class MainWindow {
	private final FontEnum fontEnum = new FontEnum();
	private final ComponentInit componentInit = new ComponentInit();
	private final InputFunction inputFunction = new InputFunction();
	private final WarmWindow warmWindow = new WarmWindow();
	private final OneSummaryWindow oneSummaryWindow = new OneSummaryWindow();
	private final SummaryWindow summaryWindow = new SummaryWindow();
	private final UpdateWindow updateWindow = new UpdateWindow();
	private final NoteWindow noteWindow = new NoteWindow();
	private JPanel showOneSummaryJPanel = new JPanel();

	public void showMainFrame(int mode, GlobalVariable globalVariable){
		//初始化
		globalVariable.mainFrame.removeAll();
		if(mode == ModeTypeEnum.CREATE.getVal()){
			globalVariable.ticketsNo.set(0);
			globalVariable.tickets = new TicketList();
			globalVariable.functionType = FunctionType.FEIZUHE.getLabel();
		}

		//图形化
		//输入框
		JPanel jPanel = new JPanel();
		BoxLayout layout=new BoxLayout(jPanel, BoxLayout.Y_AXIS);
		jPanel.setLayout(layout);
		//jPanel.setLayout(new GridLayout(8,5));
		//标题
		JTextField titleJF=new JTextField(30);
		titleJF.setFont(fontEnum.mainFont);

		//序列号
/*		JTextField ztSerialNumberJF=new JTextField(50);
		ztSerialNumberJF.setFont(fontEnum.mainFont);*/
		JTextArea sdSerialNumberJTA=new JTextArea(6,50);
		sdSerialNumberJTA.setFont(fontEnum.mainFont);
		sdSerialNumberJTA.setLineWrap(true);
		JTextField groupNumberJF=new JTextField(5);
		groupNumberJF.setEditable(false);
		groupNumberJF.setDisabledTextColor(Color.BLACK);
		groupNumberJF.setBackground(Color.WHITE);
		groupNumberJF.setFont(fontEnum.mainFont);

		//单价
		//无效状态
		Choice choicePrice = new Choice();
		for(PriceEnum priceEnum: PriceEnum.values()){
			choicePrice.add(String.valueOf(priceEnum.getVal()));
			choicePrice.setFont(fontEnum.mainFont);
		}
		choicePrice.select(String.valueOf(PriceEnum.TOW.getVal()));

		JTextField priceJF=new JTextField(String.valueOf(PriceEnum.TOW.getVal()),4);
		priceJF.setFont(fontEnum.mainFont);

		JPanel pricePanel = new JPanel();
		ButtonGroup priceGroup = new ButtonGroup();
		List<JRadioButton> priceList = new ArrayList<>();
		for (PriceEnum priceEnum : PriceEnum.values()) {
			JRadioButton priceButton = new JRadioButton(String.valueOf(priceEnum.getVal()));
			priceButton.setFont(fontEnum.mainButtonFont);
			priceButton.addActionListener(e -> {
				priceJF.setText(priceButton.getText());
				globalVariable.selectPrice = priceButton.getText();
			});
			priceGroup.add(priceButton);
			pricePanel.add(priceButton);
			priceList.add(priceButton);
			if(priceEnum.getVal() == PriceEnum.TOW.getVal()){
				priceButton.setSelected(true);
			}
		}

		//倍数
		JTextField timesJF=new JTextField(String.valueOf(TimesEnum.ONE.getVal()),4);
		timesJF.setFont(fontEnum.mainFont);
		//timesJF.setVisible(false);

		JComboBox<String> timesComboBox = new JComboBox<>();
		for(TimesEnum timesEnum: TimesEnum.values()){
			timesComboBox.addItem(timesEnum.getLabel());
			timesComboBox.setFont(fontEnum.mainFont);
		}

		//功能选择
		JPanel functionPanel = new JPanel();
		ButtonGroup btnGroup = new ButtonGroup();
		List<JRadioButton> functionList = new ArrayList<>();
		for (FunctionType functionType : FunctionType.values()) {
			JRadioButton functionButton = new JRadioButton(functionType.getLabel());
			functionButton.setFont(fontEnum.mainButtonFont);
			functionButton.addActionListener(e -> {
				globalVariable.functionType = functionButton.getText();
				if(!sdSerialNumberJTA.getText().equals("")){
					List<String> groupNumberList = inputFunction.getNumber(sdSerialNumberJTA.getText(),globalVariable.functionType);
					int groupNumber = Integer.parseInt(groupNumberList.get(groupNumberList.size()-1));
					groupNumberJF.setText(String.valueOf(groupNumber));
					globalVariable.inputSerialNumber = sdSerialNumberJTA.getText();
				}
			});
			//保留输入信息
			if(globalVariable.functionType.equals(functionType.getLabel())){
				functionButton.setSelected(true);
			}
			functionList.add(functionButton);
			btnGroup.add(functionButton);
			functionPanel.add(functionButton);
		}

		//类别
		JTextField typeTwoJF=new JTextField(TypeTwoEnum.TICAI.getLabel(),10);
		typeTwoJF.setFont(fontEnum.mainFont);
		typeTwoJF.setVisible(false);

		JPanel typeTwoPanel = new JPanel();
		ButtonGroup typeTwoGroup = new ButtonGroup();
		for(TypeTwoEnum typeTwoEnum: TypeTwoEnum.values()){
			JRadioButton typeTwoButton = new JRadioButton(typeTwoEnum.getLabel());
			typeTwoButton.setFont(fontEnum.mainButtonFont);
			typeTwoButton.addActionListener(e -> typeTwoJF.setText(typeTwoButton.getText()));
			typeTwoGroup.add(typeTwoButton);
			typeTwoPanel.add(typeTwoButton);
		}

		JComboBox<String> typeTwoComboBox = new JComboBox<>();
		for(TypeTwoEnum typeTwoEnumEnum: TypeTwoEnum.values()){
			typeTwoComboBox.addItem(typeTwoEnumEnum.getLabel());
			typeTwoComboBox.setFont(fontEnum.mainFont);
		}

		//类型
		JTextField typeJF=new JTextField(TypeEnum.ZHIXUAN.getLabel(),10);
		typeJF.setFont(fontEnum.mainFont);
		typeJF.setVisible(false);

		JComboBox<String> typeComboBox = new JComboBox<>();
		for(TypeEnum typeEnum: TypeEnum.values()){
			if(inputFunction.filterTpye(typeEnum.getLabel()))
				continue;
			typeComboBox.addItem(typeEnum.getLabel());
			typeComboBox.setFont(fontEnum.mainFont);
		}

		//功能按钮
		JButton confirm = componentInit.jButtonInit(new JButton(),"确定");
		JButton clean = componentInit.jButtonInit(new JButton(),"清空");
		JButton showTodaySummary = componentInit.jButtonInit(new JButton(),"查看汇总");

		//直选倍数按钮
		JPanel zhiXuanAndTimesPanel = new JPanel();
		for (ZhiXuanAndTimes zhiXuanAndTimes : ZhiXuanAndTimes.values()){
			Button typeAndTimesButton = new Button(zhiXuanAndTimes.getDesc());
			typeAndTimesButton.setFont(fontEnum.mainButtonFont);
			typeAndTimesButton.addActionListener(e -> {
				typeJF.setText(zhiXuanAndTimes.getType());
				typeComboBox.setSelectedItem(zhiXuanAndTimes.getType());
				timesComboBox.setSelectedItem(zhiXuanAndTimes.getTimesDesc());
				timesJF.setText(String.valueOf(zhiXuanAndTimes.getTimes()));
				confirm.doClick();
			});
			zhiXuanAndTimesPanel.add(typeAndTimesButton);
			zhiXuanAndTimesPanel = componentInit.addSpace(zhiXuanAndTimesPanel,1);
		}

		//组选倍数按钮
		JPanel zhuXuanAndTimesPanel = new JPanel();
		for (ZhuXuanAndTimes zhuXuanAndTimes : ZhuXuanAndTimes.values()){
			Button typeAndTimesButton = new Button(zhuXuanAndTimes.getDesc());
			typeAndTimesButton.setFont(fontEnum.mainButtonFont);
			typeAndTimesButton.addActionListener(e -> {
				typeJF.setText(zhuXuanAndTimes.getType());
				typeComboBox.setSelectedItem(zhuXuanAndTimes.getType());
				timesComboBox.setSelectedItem(zhuXuanAndTimes.getTimesDesc());
				timesJF.setText(String.valueOf(zhuXuanAndTimes.getTimes()));
				confirm.doClick();
			});
			zhuXuanAndTimesPanel.add(typeAndTimesButton);
			zhuXuanAndTimesPanel = componentInit.addSpace(zhuXuanAndTimesPanel,1);
		}

		//组三倍数按钮
		JPanel zhuSanAndTimesPanel = new JPanel();
		for (ZhuSanAndTimes zhuSanAndTimes : ZhuSanAndTimes.values()){
			Button typeAndTimesButton = new Button(zhuSanAndTimes.getDesc());
			typeAndTimesButton.setFont(fontEnum.mainButtonFont);
			typeAndTimesButton.addActionListener(e -> {
				typeJF.setText(zhuSanAndTimes.getType());
				typeComboBox.setSelectedItem(zhuSanAndTimes.getType());
				timesComboBox.setSelectedItem(zhuSanAndTimes.getTimesDesc());
				timesJF.setText(String.valueOf(zhuSanAndTimes.getTimes()));
				confirm.doClick();
			});
			zhuSanAndTimesPanel.add(typeAndTimesButton);
			zhuSanAndTimesPanel = componentInit.addSpace(zhuSanAndTimesPanel,1);
		}

		//组六陪数按钮
		JPanel zhuLiuAndTimesPanel = new JPanel();
		for (ZhuLiuAndTimes zhuLiuAndTimes : ZhuLiuAndTimes.values()){
			Button typeAndTimesButton = new Button(zhuLiuAndTimes.getDesc());
			typeAndTimesButton.setFont(fontEnum.mainButtonFont);
			typeAndTimesButton.addActionListener(e -> {
				typeJF.setText(zhuLiuAndTimes.getType());
				typeComboBox.setSelectedItem(zhuLiuAndTimes.getType());
				timesComboBox.setSelectedItem(zhuLiuAndTimes.getTimesDesc());
				timesJF.setText(String.valueOf(zhuLiuAndTimes.getTimes()));
				confirm.doClick();
			});
			zhuLiuAndTimesPanel.add(typeAndTimesButton);
			zhuLiuAndTimesPanel = componentInit.addSpace(zhuLiuAndTimesPanel,1);
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

		//第一行 标题
		JPanel titilePanel= componentInit.initJPanel(new JPanel(),"标题",titleJF);

		//第二行 功能选择 + 序列号 + 组数显示 + 重置按钮
		JPanel sdSerialNumberPanel= new JPanel();
		sdSerialNumberPanel.add(functionPanel);
		sdSerialNumberPanel	= componentInit.initJPanel(sdSerialNumberPanel,"序列号",sdSerialNumberJTA);
		Button reset = new Button("重置");
		reset.setFont(fontEnum.mainFont);
		sdSerialNumberPanel.add(groupNumberJF);
		sdSerialNumberPanel= componentInit.iniJPanel(sdSerialNumberPanel,"组");
		//sdSerialNumberPanel.add(reset);

		//第三行 单价 + 倍数 + 类型 + 类别
		JPanel priceAndTypePanel = new JPanel();
		priceAndTypePanel.add(typeTwoPanel);
		priceAndTypePanel = componentInit.addSpace(priceAndTypePanel,8);
		priceAndTypePanel = componentInit.iniJPanel(priceAndTypePanel,"单价");
		//priceAndTypePanel.add(pricePanel);
		priceAndTypePanel = componentInit.iniJPanel(priceAndTypePanel,"",priceJF);
		priceAndTypePanel = componentInit.iniJPanel(priceAndTypePanel,"元");
		priceAndTypePanel = componentInit.addSpace(priceAndTypePanel,8);
		priceAndTypePanel = componentInit.iniJPanel(priceAndTypePanel,"倍数",timesComboBox);
		priceAndTypePanel.add(timesJF);
		priceAndTypePanel = componentInit.iniJPanel(priceAndTypePanel,"倍");
		priceAndTypePanel = componentInit.addSpace(priceAndTypePanel,8);
		priceAndTypePanel = componentInit.iniJPanel(priceAndTypePanel,"类型",typeComboBox);
/*		JPanel priceAndTypePanel=jPanelInit.initJPanel("类型","单价","倍数",
				choicePrice,choiceType,choiceTimes,priceJF);*/
		//JPanel resultPanel=jPanelInit.initJPanel("结果",resultJF);

		//第四行 直选倍数按钮
		//第五行 组选倍数按钮
		//第六行 组三陪数按钮

		//第七行 提交按钮 + 清空按钮 + 查看今日汇总按钮 + 数字组合按钮
		JPanel buttonPanel=new JPanel();
		buttonPanel.add(confirm);
		buttonPanel.add(clean);
		buttonPanel.add(showTodaySummary);
		buttonPanel.setFont(fontEnum.mainButtonFont);

		//第八行 当前数据
		JPanel titlePanel = new JPanel();
		JLabel titleLabel =new JLabel("当前数据");
		titleLabel.setFont(fontEnum.oneSummaryPlainFont);
		titlePanel.add(titleLabel);

		//最后一行 备注按钮 + 修改按钮 + 删除按钮 + 下单按钮
		Button note = new Button("备注");
		Button update = new Button("修改");
		Button delete = new Button("删除");
		Button sumbit = new Button("下单");
		JPanel oneSummaryButtonPanel=new JPanel();
		oneSummaryButtonPanel.setFont(fontEnum.oneSummaryFont);
		oneSummaryButtonPanel.add(note);
		oneSummaryButtonPanel.add(update);
		oneSummaryButtonPanel.add(delete);
		oneSummaryButtonPanel.add(sumbit);
		oneSummaryButtonPanel.setVisible(false);

		//插入组件
		//jPanel.add(ztSerialNumberPanel);
		jPanel.add(titilePanel);
		jPanel.add(sdSerialNumberPanel);
		jPanel.add(priceAndTypePanel);
		jPanel.add(zhiXuanAndTimesPanel);
		jPanel.add(zhuXuanAndTimesPanel);
		jPanel.add(zhuLiuAndTimesPanel);
		jPanel.add(zhuSanAndTimesPanel);
		//jPanel.add(resultPanel);
		jPanel.add(buttonPanel);

		//当前数据框
		jPanel.add(titlePanel);
		titlePanel.setVisible(false);
		if(globalVariable.tickets.getTicketList() != null){
			showOneSummaryJPanel = oneSummaryWindow.showOneSummaryFrame(showOneSummaryJPanel,globalVariable);
		}
		//滚动条
		JScrollPane jScrollPane = new JScrollPane(showOneSummaryJPanel);
		jScrollPane.setBorder(null);

		//设置布局
		globalVariable.mainFrame.add(jPanel,BorderLayout.NORTH);
		globalVariable.mainFrame.add(jScrollPane,BorderLayout.CENTER);
		globalVariable.mainFrame.add(oneSummaryButtonPanel,BorderLayout.SOUTH);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		//窗口化
		//globalVariable.mainFrame.setBounds((screenSize.width - 1200) / 2, (screenSize.height - 700) / 2, 1200, 700);
		//全屏
		Rectangle bounds = new Rectangle(screenSize);
		globalVariable.mainFrame.setBounds(bounds);
		globalVariable.mainFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		globalVariable.mainFrame.setVisible(true);
		//注册监听器
		globalVariable.mainFrame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});

		choicePrice.addItemListener(e -> {
			Choice choice = (Choice) e.getItemSelectable();
			priceJF.setText(choice.getSelectedItem());
			globalVariable.selectPrice = choice.getSelectedItem();
		});

		timesComboBox.addItemListener(e -> timesJF.setText(Objects.requireNonNull(timesComboBox.getSelectedItem()).toString().replace("倍","")));
		timesComboBox.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				timesComboBox.setPopupVisible(true);
			}
		});

		typeTwoComboBox.addItemListener(e -> typeTwoJF.setText(Objects.requireNonNull(typeTwoComboBox.getSelectedItem()).toString()));
		typeTwoComboBox.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				typeTwoComboBox.setPopupVisible(true);
			}
		});

		typeComboBox.addItemListener(e -> typeJF.setText(Objects.requireNonNull(typeComboBox.getSelectedItem()).toString()));
		typeComboBox.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				typeComboBox.setPopupVisible(true);
			}
		});

		reset.addActionListener(e -> sdSerialNumberJTA.setText(""));

		clean.addActionListener(e -> {
			//globalVariable.tickets = new TicketList();
			//showOneSummaryJPanel.removeAll();
			//showOneSummaryJPanel.revalidate();
			//showOneSummaryJPanel.repaint();
			globalVariable.mainFrame.revalidate();
			//titleJF.setText("");
			sdSerialNumberJTA.setText("");
			groupNumberJF.setText("");
			//choiceTypeTwo.select(TypeTwoEnum.TICAI.getLabel());
			//typeTwoJF.setText(TypeTwoEnum.TICAI.getLabel());
			choicePrice.select(String.valueOf(PriceEnum.TOW.getVal()));
			priceJF.setText(String.valueOf(PriceEnum.TOW.getVal()));
			timesComboBox.setSelectedItem(String.valueOf(TimesEnum.ONE.getLabel()));
			timesJF.setText(String.valueOf(TimesEnum.ONE.getVal()));
			typeJF.setText(TypeEnum.ZHIXUAN.getLabel());
			typeComboBox.setSelectedItem(TypeEnum.ZHIXUAN.getLabel());
			for (JRadioButton jRadioButton : functionList){
				if (jRadioButton.getText().equals(FunctionType.FEIZUHE.getLabel())){
					jRadioButton.setSelected(true);
				}
			}
			for (JRadioButton jRadioButton : priceList){
				if (jRadioButton.getText().equals(String.valueOf(PriceEnum.TOW.getVal()))){
					jRadioButton.setSelected(true);
				}
			}
			//titlePanel.setVisible(false);
			//oneSummaryButtonPanel.setVisible(false);
			//showMainFrame(ModeTypeEnum.CREATE.getVal(),globalVariable);
		});

/*		ztSerialNumberJF.addCaretListener(new CaretListener() {
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
		});*/

		confirm.addActionListener(e -> {
			String serialNumber = sdSerialNumberJTA.getText();
			String inputPrice = String.format("%.2f", Float.valueOf(priceJF.getText()));
			String times = timesJF.getText();
			String type = typeJF.getText();
			String typeTwo = typeTwoJF.getText();
			String output = inputFunction.getNumber(globalVariable.tickets,globalVariable.alllistNo,globalVariable.ticketsNo,
					serialNumber,type,typeTwo,inputPrice,times,globalVariable.functionType);
			if(output == null || output.equals("fail")){
				warmWindow.warmWindow("请输入有效数字",fontEnum.warmInfoFont);
			}else {
				resultJF.setText(output);
				if(globalVariable.tickets.getTicketList() != null){
					titlePanel.setVisible(true);
					showOneSummaryJPanel = oneSummaryWindow.showOneSummaryFrame(showOneSummaryJPanel,globalVariable);
				}
				oneSummaryButtonPanel.setVisible(true);
				globalVariable.mainFrame.revalidate();
				JScrollBar vertical = jScrollPane.getVerticalScrollBar();
				vertical.setValue(vertical.getMaximum());
				//showMainFrame(ModeTypeEnum.UPDATE.getVal(),globalVariable);
			}
		});

		showTodaySummary.addActionListener(e -> {
			SimpleDateFormat sf= new SimpleDateFormat("yyyy-MM-dd");
			Date date = new Date();
			String fileName = sf.format(date)+".xlsx";
			if(!titleJF.getText().equals("")){
				fileName = sf.format(date) + " " + titleJF.getText()+".xlsx";
			}
			List<ShowSummaryList> showSummaryLists = inputFunction.readTodayExcel(globalVariable.filePath,fileName);
			if(showSummaryLists == null) {
				warmWindow.warmWindow("没有名为" + fileName +"的汇总文件",fontEnum.warmInfoFont);
			}else{
				summaryWindow.showSummary(showSummaryLists,fileName,globalVariable);
			}
		});

		sdSerialNumberJTA.addCaretListener(e -> {
			if(!sdSerialNumberJTA.getText().equals("")){
				Enumeration<AbstractButton> radioBatons=btnGroup.getElements();
				while (radioBatons.hasMoreElements()) {
					AbstractButton btn = radioBatons.nextElement();
					if(btn.isSelected()){
						globalVariable.functionType=btn.getText();
						break;
					}
				}
				List<String> groupNumberList = inputFunction.getNumber(sdSerialNumberJTA.getText(),globalVariable.functionType);
				inputFunction.setPrice(globalVariable,groupNumberList.get(0),priceList,priceJF);
				int groupNumber = Integer.parseInt(groupNumberList.get(groupNumberList.size()-1));
				groupNumberJF.setText(String.valueOf(groupNumber));
				globalVariable.inputSerialNumber = sdSerialNumberJTA.getText();
			}
		});

		note.addActionListener(e -> noteWindow.showNoteWindow(globalVariable));

		update.addActionListener(e -> {
			if(globalVariable.selectNo.equals("")){
				warmWindow.warmWindow("请先选择一条记录",fontEnum.warmInfoFont);
			}else{
				updateWindow.showUpdateFrame(globalVariable.selectNo,globalVariable,showOneSummaryJPanel);
				globalVariable.selectNo = "";
			}
		});

		delete.addActionListener(e -> {
			if(globalVariable.selectNo.equals("")){
				warmWindow.warmWindow("请先选择一条记录",fontEnum.warmInfoFont);
			}else {
				Ticket removeTicket = globalVariable.tickets.getTicketList().get(Integer.parseInt(globalVariable.selectNo));
				globalVariable.tickets.getTicketList().remove(removeTicket);
				showOneSummaryJPanel = oneSummaryWindow.showOneSummaryFrame(showOneSummaryJPanel,globalVariable);

				if(globalVariable.tickets.getTicketList().size() == 0){
					titlePanel.setVisible(false);
					showOneSummaryJPanel.removeAll();
					showOneSummaryJPanel.revalidate();
					oneSummaryButtonPanel.setVisible(false);
				}

				globalVariable.selectNo = "";
				showOneSummaryJPanel.repaint();
				globalVariable.mainFrame.revalidate();
			}
		});

		sumbit.addActionListener(e -> {
			try {
				String result = inputFunction.sumbit(globalVariable.filePath,globalVariable.tickets,titleJF.getText());
				if(!result.equals("成功")){
					warmWindow.warmWindow(result,fontEnum.warmInfoFont);
				}else{
					globalVariable.ticketsNo.set(0);
					globalVariable.tickets = new TicketList();
					globalVariable.selectPrice = "";

					sdSerialNumberJTA.setText("");
					groupNumberJF.setText("");
					//typeTwoComboBox.setSelectedItem(TypeTwoEnum.TICAI.getLabel());
					//typeTwoJF.setText(TypeTwoEnum.TICAI.getLabel());
					priceJF.setText(String.valueOf(PriceEnum.TOW.getVal()));
					timesComboBox.setSelectedItem(String.valueOf(TimesEnum.ONE.getLabel()));
					timesJF.setText(String.valueOf(TimesEnum.ONE.getVal()));
					typeJF.setText(TypeEnum.ZHIXUAN.getLabel());
					typeComboBox.setSelectedItem(TypeEnum.ZHIXUAN.getLabel());
					for (JRadioButton jRadioButton : functionList){
						if (jRadioButton.getText().equals(FunctionType.FEIZUHE.getLabel())){
							jRadioButton.setSelected(true);
						}
					}
					for (JRadioButton jRadioButton : priceList){
						if (jRadioButton.getText().equals(String.valueOf(PriceEnum.TOW.getVal()))){
							jRadioButton.setSelected(true);
						}
					}

					showOneSummaryJPanel.removeAll();
					showOneSummaryJPanel.revalidate();
					showOneSummaryJPanel.repaint();

					titlePanel.setVisible(false);
					oneSummaryButtonPanel.setVisible(false);

					globalVariable.mainFrame.revalidate();
					//mainWindow.showMainFrame(ModeTypeEnum.CREATE.getVal(),globalVariable);
				}
			} catch (IOException | WriteException | BiffException fileNotFoundException) {
				fileNotFoundException.printStackTrace();
			}
		});

	}
}
