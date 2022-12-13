package Frame;

import Enum.*;
import Start.*;
import Bean.*;

import javax.swing.*;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import java.awt.*;
import java.awt.event.*;

public class UpdateWindow {
	private static FontEnum fontEnum = new FontEnum();
	private static Function function = new Function();
	private static JPanelInit jPanelInit = new JPanelInit();
	private static MainWindow mainWindow = new MainWindow();

	static void showUpdateFrame(String ticketsNo, GlobalVariable globalVariable){
		Ticket ticket = globalVariable.tickets.getTicketList().get(Integer.valueOf(ticketsNo));

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

		Choice choicePrice = new Choice();
		for(PriceEnum priceEnum: PriceEnum.values()){
			choicePrice.add(String.valueOf(priceEnum.getVal()));
			choicePrice.setFont(fontEnum.mainFont);
		}

		JTextField timesJF=new JTextField("1",10);
		timesJF.setFont(fontEnum.mainFont);
		timesJF.setVisible(false);

		Choice choiceTimes = new Choice();
		for(TimesEnum timesEnum: TimesEnum.values()){
			choiceTimes.add(timesEnum.getLabel());
			choiceTimes.setFont(fontEnum.mainFont);
		}
		//类型
		JTextField typeJF=new JTextField(ticket.getType(),10);
		typeJF.setFont(fontEnum.updateFont);
		typeJF.setVisible(false);

		Choice choiceType = new Choice();
		choiceType.select(ticket.getType());
		for(TypeEnum typeEnum: TypeEnum.values()){
			choiceType.add(typeEnum.getLabel());
			choiceType.setFont(fontEnum.updateFont);
		}
		choiceType.select(ticket.getType());
		//结果
		JTextField resultJF=new JTextField(30);
		resultJF.setDisabledTextColor(Color.BLACK);
		resultJF.setEnabled(false);

		//第一行 序列号 + 组数
		JPanel serialNumberPanel=jPanelInit.initJPanel("序列号",serialNumberJF);
		serialNumberPanel.add(groupNumberJF);
		//第二行 单价 + 倍数 + 类型
		JPanel priceAndTypePanel=jPanelInit.initJPanel("类型","单价","倍数",
				choicePrice,choiceType,choiceTimes,priceJF);
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
		Toolkit kit = Toolkit.getDefaultToolkit();
		Dimension screenSize = kit.getScreenSize();
		frame.setBounds((screenSize.width - 1000) / 2, (screenSize.height - 200) / 2, 1000, 170);
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

		update.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Ticket ticket = globalVariable.tickets.getTicketList().get(Integer.valueOf(ticketsNo));
				ticket.setSerialNumber(serialNumberJF.getText());
				ticket.setGroupNum(ticket.getSerialNumber().split(" ").length);
				ticket.setUnitPrice(Integer.valueOf(priceJF.getText())*Integer.valueOf(timesJF.getText()));
				ticket.setType(typeJF.getText());
				ticket.setTotalPrice(ticket.getUnitPrice() * ticket.getGroupNum());
				frame.dispose();
				mainWindow.showMainFrame(ModeTypeEnum.UPDATE.getVal(),globalVariable);
			}
		});

		serialNumberJF.addCaretListener(new CaretListener() {
			@Override
			public void caretUpdate(CaretEvent e) {
				if(!serialNumberJF.equals("")){
					int groupNumber = function.getNumber(serialNumberJF.getText());
					groupNumberJF.setText(String.valueOf(groupNumber));
				}
			}
		});
	}
}
