package Frame;

import Enum.*;
import Start.*;
import Bean.*;

import jxl.read.biff.BiffException;
import jxl.write.WriteException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.IOException;

public class OneSummaryWindow {
	private static FontEnum fontEnum = new FontEnum();
	private static Function function = new Function();
	private static MainWindow mainWindow = new MainWindow();
	private static WarmWindow warmWindow = new WarmWindow();
	private static UpdateWindow updateWindow = new UpdateWindow();

	public JPanel showOneSummaryFrame(JPanel showOneSummaryPanel, GlobalVariable globalVariable){
		showOneSummaryPanel.removeAll();
		JPanel panel = new JPanel(new GridLayout(globalVariable.tickets.getTicketList().size()+2,1));
		ButtonGroup btnGroup = new ButtonGroup();
		int totalPrice = 0;
		for (int i = 0; i < globalVariable.tickets.getTicketList().size(); i++) {
			JPanel resultPanel = new JPanel();
			JLabel resultLabel = new JLabel(function.showSummaryWithNumber(globalVariable.tickets.getTicketList().get(i)));
			totalPrice = totalPrice + globalVariable.tickets.getTicketList().get(i).getTotalPrice();
			resultLabel.setFont(fontEnum.oneSummaryFont);
			JRadioButton resultButton = new JRadioButton(String.valueOf(i));
			resultButton.setFont(fontEnum.oneSummaryFont);
			resultButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					globalVariable.selectNo = resultButton.getText();
				}
			});
			btnGroup.add(resultButton);
			resultPanel.add(resultButton);
			resultPanel.add(resultLabel);
			panel.add(resultPanel);
		}
		JPanel totalPricePanel = new JPanel();
		JLabel totalPriceLabel = new JLabel("总共"+String.valueOf(totalPrice)+"元");
		totalPriceLabel.setFont(fontEnum.oneSummaryFont);
		totalPricePanel.add(totalPriceLabel);
		panel.add(totalPricePanel);

		showOneSummaryPanel.add(panel);
		Button update = new Button("修改");
		Button delete = new Button("删除");
		Button sumbit = new Button("下单");
		JPanel buttonPanel=new JPanel();
		buttonPanel.setFont(fontEnum.oneSummaryFont);
		buttonPanel.add(update);
		buttonPanel.add(delete);
		buttonPanel.add(sumbit);
		panel.add(buttonPanel);

		showOneSummaryPanel.add(panel,BorderLayout.NORTH);

		update.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(globalVariable.selectNo.equals("")){
					warmWindow.warmWindow("请先选择一条记录",fontEnum.warmInfoFont);
				}else{
					updateWindow.showUpdateFrame(globalVariable.selectNo,globalVariable);
				}
			}
		});

		delete.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(globalVariable.selectNo.equals("")){
					warmWindow.warmWindow("请先选择一条记录",fontEnum.warmInfoFont);
				}else {
					Ticket removeTicket = globalVariable.tickets.getTicketList().get(Integer.valueOf(globalVariable.selectNo));
					globalVariable.tickets.getTicketList().remove(removeTicket);
					if(globalVariable.tickets.getTicketList().size() == 0){
						mainWindow.showMainFrame(ModeTypeEnum.CREATE.getVal(),globalVariable);
					}else{
						mainWindow.showMainFrame(ModeTypeEnum.UPDATE.getVal(),globalVariable);
					}
				}
			}
		});

		sumbit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					String result = function.sumbit(globalVariable.filePath,globalVariable.tickets);
					if(!result.equals("成功")){
						warmWindow.warmWindow(result,fontEnum.warmInfoFont);
					}else{
						globalVariable.ticketsNo.set(0);
						globalVariable.tickets = new TicketList();
						globalVariable.selectPrice = "";
						mainWindow.showMainFrame(ModeTypeEnum.CREATE.getVal(),globalVariable);
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

		return showOneSummaryPanel;
	}
}
