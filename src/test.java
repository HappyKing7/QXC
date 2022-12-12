import jxl.read.biff.BiffException;
import jxl.write.WriteException;

import javax.swing.*;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import java.awt.*;
import java.awt.event.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class test {
	private static Function function = new Function();
	private static JPanelInit jPanelInit = new JPanelInit();

	static Frame oneFrame = new Frame("单人汇总");
	static Frame mianFrame = new Frame("QXC");
	static AtomicInteger alllistNo =new AtomicInteger();
	static AtomicInteger ticketsNo =new AtomicInteger();
	static TicketList tickets = new TicketList();
	static String selectNo = "";
	static String filePath = "";

	static Font mainFont = new Font("",Font.PLAIN,15);
	static Font oneSummaryFont = new Font("",Font.PLAIN,20);
	static Font oneSummaryPlainFont = new Font("",Font.BOLD,25);
	static Font warmInfoFont = new Font("宋体",Font.BOLD,20);
	static Font updateFont = new Font("",Font.PLAIN,15);
	static Font todaySummaryFont = new Font("",Font.PLAIN,20);

	static void warmWindow(String warmInfo){
		Frame frame=new Frame("提示信息");
		JLabel jLabel = new JLabel(warmInfo,JLabel.CENTER);
		jLabel.setFont(warmInfoFont);
		jLabel.setForeground(Color.red);
		frame.add(jLabel,BorderLayout.CENTER);
		Toolkit kit = Toolkit.getDefaultToolkit();
		Dimension screenSize = kit.getScreenSize();
		frame.setBounds((screenSize.width - 1000) / 2, (screenSize.height - 150) / 2, 1000, 150);
		frame.setVisible(true);

		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				frame.dispose();
			}
		});
	}

	static void showMainFrame(int mode){
		//初始化
		mianFrame.removeAll();
		if(mode == ModeType.CREATE.getVal()){
			ticketsNo.set(0);
			tickets = new TicketList();
		}

		//图形化
		//输入框
		JPanel jPanel = new JPanel();
		jPanel.setLayout(new GridLayout(6,1));
		JTextField ztSerialNumberJF=new JTextField(50);
		ztSerialNumberJF.setFont(mainFont);
		JTextField sdSerialNumberJF=new JTextField(50);
		sdSerialNumberJF.setFont(mainFont);
		JTextField priceJF=new JTextField("2",20);
		priceJF.setFont(mainFont);
		JTextField typeJF=new JTextField("直选",20);
		priceJF.setFont(mainFont);
		typeJF.setVisible(false);
		JTextField resultJF=new JTextField(50);
		resultJF.setFont(mainFont);
		resultJF.setDisabledTextColor(Color.BLACK);
		resultJF.setEnabled(false);

		Choice choiceType = new Choice();
		for(TypeEnum typeEnum:TypeEnum.values()){
			choiceType.add(typeEnum.getLabel());
			choiceType.setFont(mainFont);
		}

		JPanel ztSerialNumberPanel=jPanelInit.initJPanel("粘贴序列号",ztSerialNumberJF);
		Button clean = new Button("清空");
		clean.setFont(mainFont);
		ztSerialNumberPanel.add(clean);

		JPanel sdSerialNumberPanel=jPanelInit.initJPanel("输入序列号",sdSerialNumberJF);
		Button confirm = new Button("确定");
		confirm.setFont(mainFont);
		sdSerialNumberPanel.add(confirm);

		JPanel typePanel=jPanelInit.initJPanel("类型","单价",choiceType,priceJF);
		JPanel resultPanel=jPanelInit.initJPanel("结果",resultJF);

		JPanel buttonPanel=new JPanel();
		Button showTodaySummary = new Button("查看今日汇总");
		buttonPanel.add(showTodaySummary);
		buttonPanel.setFont(mainFont);

		JPanel titlePanel = new JPanel();
		JLabel titleLabel =new JLabel("当前数据");
		titleLabel.setFont(oneSummaryPlainFont);
		titlePanel.add(titleLabel);

		jPanel.add(ztSerialNumberPanel);
		jPanel.add(sdSerialNumberPanel);
		jPanel.add(typePanel);
		jPanel.add(resultPanel);
		jPanel.add(buttonPanel);

		//当前汇总框
		JPanel showOneSummaryJPanel = new JPanel();
		if(tickets.getTicketList() != null){
			jPanel.add(titlePanel);
			showOneSummaryJPanel = showOneSummaryFrame(showOneSummaryJPanel);
		}
		mianFrame.add(showOneSummaryJPanel,BorderLayout.CENTER);

		//设置布局
		mianFrame.add(jPanel,BorderLayout.NORTH);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Rectangle bounds = new Rectangle(screenSize);
		mianFrame.setBounds(bounds);
		mianFrame.setVisible(true);

		//注册监听器
		mianFrame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				System.exit(0);
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
				showMainFrame(ModeType.CREATE.getVal());
			}
		});

		ztSerialNumberJF.addCaretListener(new CaretListener() {
			@Override
			public void caretUpdate(CaretEvent e) {
				if(!ztSerialNumberJF.getText().equals("")){
					String serialNumber = ztSerialNumberJF.getText();
					String inputPrice = priceJF.getText();
					String type = typeJF.getText();
					String output = function.getNumber(tickets,alllistNo,ticketsNo,serialNumber,type,inputPrice);;
					if(output.equals("fail")){
						warmWindow("请输入有效数字");
					}else {
						resultJF.setText(output);
						showMainFrame(ModeType.UPDATE.getVal());
					}
				}
			}
		});

		confirm.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String serialNumber = sdSerialNumberJF.getText();
				String inputPrice = priceJF.getText();
				String type = typeJF.getText();
				String output = function.getNumber(tickets,alllistNo,ticketsNo,serialNumber,type,inputPrice);
				if(output.equals("fail")){
					warmWindow("请输入有效数字");
				}else {
					resultJF.setText(output);
					showMainFrame(ModeType.UPDATE.getVal());
				}
			}
		});

		showTodaySummary.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				SimpleDateFormat sf= new SimpleDateFormat("yyyy-MM-dd");
				Date date = new Date();
				String nowDate= sf.format(date);
				List<ShowSummaryList> showSummaryLists = function.readTodayExcel(filePath,nowDate);
				if(showSummaryLists == null)
				{
					warmWindow(nowDate+"还没有汇总文件");
				}else{
					showTodaySummary(showSummaryLists);
				}
			}
		});
	}

	static JPanel showOneSummaryFrame(JPanel showOneSummaryPanel){
		showOneSummaryPanel.removeAll();
		JPanel panel = new JPanel(new GridLayout(tickets.getTicketList().size()+1,1));
		ButtonGroup btnGroup = new ButtonGroup();
		for (int i = 0; i < tickets.getTicketList().size(); i++) {
			JPanel resultPanel = new JPanel();
			JLabel resultLabel = new JLabel(function.showSummaryWithNumber(tickets.getTicketList().get(i)));
			resultLabel.setFont(oneSummaryFont);
			JRadioButton resultButton = new JRadioButton(String.valueOf(i));
			resultButton.setFont(oneSummaryFont);
			resultButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					selectNo = resultButton.getText();
				}
			});
			btnGroup.add(resultButton);
			resultPanel.add(resultButton);
			resultPanel.add(resultLabel);
			panel.add(resultPanel);
		}
		showOneSummaryPanel.add(panel);
		Button update = new Button("修改");
		Button delete = new Button("删除");
		Button sumbit = new Button("下单");
		JPanel buttonPanel=new JPanel();
		buttonPanel.setFont(oneSummaryFont);
		buttonPanel.add(update);
		buttonPanel.add(delete);
		buttonPanel.add(sumbit);
		panel.add(buttonPanel);

		showOneSummaryPanel.add(panel,BorderLayout.NORTH);

		update.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(selectNo.equals("")){
					warmWindow("请先选择一条记录");
				}else{
					showUpdateFrame(selectNo);
				}
			}
		});

		sumbit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					String result = function.sumbit(filePath,tickets);
					if(!result.equals("成功")){
						warmWindow(result);
					}else{
						ticketsNo.set(0);
						tickets = new TicketList();
						showMainFrame(ModeType.CREATE.getVal());
					}
				} catch (FileNotFoundException fileNotFoundException) {
					fileNotFoundException.printStackTrace();
				} catch (IOException ioException) {
					ioException.printStackTrace();
				} catch (WriteException writeException) {
					writeException.printStackTrace();
				} catch (BiffException biffException) {
					biffException.printStackTrace();
				}
			}
		});

		delete.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(selectNo.equals("")){
					warmWindow("请先选择一条记录");
				}else {
					Ticket removeTicket = tickets.getTicketList().get(Integer.valueOf(selectNo));
					tickets.getTicketList().remove(removeTicket);
					if(tickets.getTicketList().size() == 0){
						showMainFrame(ModeType.CREATE.getVal());
					}else{
						showMainFrame(ModeType.UPDATE.getVal());
					}
				}
			}
		});

		return showOneSummaryPanel;
	}

	static void showUpdateFrame(String ticketsNo){
		Ticket ticket = tickets.getTicketList().get(Integer.valueOf(ticketsNo));

		JPanel jPanel = new JPanel();
		jPanel.setLayout(new GridLayout(3,1));
		JTextField serialNumberJF=new JTextField(ticket.getSerialNumber(),100);
		serialNumberJF.setFont(updateFont);
		JTextField priceJF=new JTextField(String.valueOf(ticket.getUnitPrice()),20);
		priceJF.setFont(updateFont);
		JTextField typeJF=new JTextField(ticket.getType(),20);
		typeJF.setFont(updateFont);
		typeJF.setVisible(false);
		JTextField resultJF=new JTextField(30);
		resultJF.setDisabledTextColor(Color.BLACK);
		resultJF.setEnabled(false);

		Choice choiceType = new Choice();
		choiceType.select(ticket.getType());
		for(TypeEnum typeEnum:TypeEnum.values()){
			choiceType.add(typeEnum.getLabel());
			choiceType.setFont(updateFont);
		}
		JPanel serialNumberPanel=jPanelInit.initJPanel("序列号",serialNumberJF);
		JPanel typePanel=jPanelInit.initJPanel("类型","单价",choiceType,priceJF);
		JPanel updatePanel=new JPanel();
		Button update = new Button("修改");
		updatePanel.setFont(updateFont);
		updatePanel.add(update);

		//添加控件
		Frame frame=new Frame("修改序列号");
		jPanel.add(serialNumberPanel);
		jPanel.add(typePanel);
		jPanel.add(updatePanel);

		//设置布局
		Toolkit kit = Toolkit.getDefaultToolkit();
		Dimension screenSize = kit.getScreenSize();
		frame.setBounds((screenSize.width - 800) / 2, (screenSize.height - 200) / 2, 800, 170);
		frame.add(jPanel,BorderLayout.NORTH);
		frame.add(new JPanel(),BorderLayout.CENTER);
		frame.setVisible(true);

		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				frame.dispose();
			}
		});

		choiceType.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				Choice choice = (Choice) e.getItemSelectable();
				typeJF.setText(choice.getSelectedItem());
			}
		});

		update.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Ticket ticket = tickets.getTicketList().get(Integer.valueOf(ticketsNo));
				ticket.setSerialNumber(serialNumberJF.getText());
				ticket.setGroupNum(ticket.getSerialNumber().split(" ").length);
				ticket.setUnitPrice(Integer.valueOf(priceJF.getText()));
				ticket.setType(typeJF.getText());
				ticket.setTotalPrice(ticket.getUnitPrice() * ticket.getGroupNum());
				showMainFrame(ModeType.UPDATE.getVal());
			}
		});
	}

	static void 	showTodaySummary(List<ShowSummaryList> showSummaryLists){
		Frame frame = new Frame();
		JPanel panel =  new JPanel(new GridLayout(showSummaryLists.get(0).getSize(),1));
		for (int i = 0; i < showSummaryLists.size(); i++) {
			ShowSummaryList ssl = showSummaryLists.get(i);
			JPanel resultPanel = new JPanel(new GridLayout(ssl.getShowSummaryList().size(),5));
			JLabel firstTotalMoneyLabel = new JLabel(ssl.getTotalMoney());
			firstTotalMoneyLabel.setFont(todaySummaryFont);
			for (int j = 0; j < ssl.getShowSummaryList().size(); j++) {
				ShowSummary ss = ssl.getShowSummaryList().get(j);
				if(j==0){
					JLabel noLabel = new JLabel(ssl.getNo());
					noLabel.setFont(todaySummaryFont);
					JLabel firstSerialNumberLabel = new JLabel(ss.getSerialNumber());
					firstSerialNumberLabel.setFont(todaySummaryFont);
					JLabel firstDetailLabel = new JLabel(ss.getDetail());
					firstDetailLabel.setFont(todaySummaryFont);
					JLabel firstTotalPriceLabel = new JLabel(ssl.getTotalPrice());
					firstTotalPriceLabel.setFont(todaySummaryFont);

					resultPanel.add(noLabel);
					resultPanel.add(firstSerialNumberLabel);
					resultPanel.add(firstDetailLabel);
					resultPanel.add(firstTotalPriceLabel);
					if(i==0){
						resultPanel.add(firstTotalMoneyLabel);
					}else{
						JLabel emptyLabel0 = new JLabel();
						emptyLabel0.setFont(todaySummaryFont);
						resultPanel.add(emptyLabel0);
					}
				}else {
					JLabel serialNumberLabel = new JLabel(ss.getSerialNumber());
					serialNumberLabel.setFont(todaySummaryFont);
					JLabel detailLabel = new JLabel(ss.getDetail());
					detailLabel.setFont(todaySummaryFont);
					JLabel emptyLabel1 = new JLabel();
					emptyLabel1.setFont(todaySummaryFont);
					JLabel emptyLabel2 = new JLabel();
					emptyLabel2.setFont(todaySummaryFont);
					JLabel emptyLabel3 = new JLabel();
					emptyLabel2.setFont(todaySummaryFont);

					resultPanel.add(emptyLabel1);
					resultPanel.add(serialNumberLabel);
					resultPanel.add(detailLabel);
					resultPanel.add(emptyLabel2);
					resultPanel.add(emptyLabel3);
				}
				panel.add(resultPanel);
			}
		}
		JScrollPane jScrollPane = new JScrollPane(panel);
		frame.add(jScrollPane,BorderLayout.CENTER);
		Toolkit kit = Toolkit.getDefaultToolkit();
		Dimension screenSize = kit.getScreenSize();
		frame.setBounds((screenSize.width - 1020) / 2, (screenSize.height - 1000) / 2, 1020, 1000);
		frame.setVisible(true);

		//注册监听器
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				frame.dispose();
			}
		});
	}

	public static void main(String[] args) throws IOException {
		//配置
		filePath = function.readConfig(0);
		showMainFrame(ModeType.CREATE.getVal());
	}
}

//156*654-984-471365654
//156*654-984
//156*654

//12342 - 12 - 34 -32
//12342 - 12 - 34
//12342 - 1
//12342 - 12

//这个 123- 123- 32 -324

//8585-555-526
//555-828-09
//112-36-43