package Frame;

import Enum.*;
import Function.ComponentInit;
import Function.InputFunction;
import Bean.*;
import Function.UpdateExcelFunction;
import jxl.read.biff.BiffException;
import jxl.write.WriteException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.List;

public class SummaryWindow {
	private static FontEnum fontEnum = new FontEnum();
	private static WarmWindow warmWindow = new WarmWindow();
	private static ComponentInit componentInit = new ComponentInit();
	private static InputFunction inputFunction = new InputFunction();
	private static UpdateExcelWindow updateExcelWindow = new UpdateExcelWindow();
	private static UpdateExcelFunction updateExcelFunction = new UpdateExcelFunction();
	private static String selectOneNo = "";
	private static String selectTwoNo = "";

	static void showSummary(List<ShowSummaryList> showSummaryLists, String fileName, GlobalVariable globalVariable){
		Frame frame = new Frame("汇总");
		JLabel title = new JLabel(fileName+"数据",JLabel.CENTER);
		title.setFont(fontEnum.todaySummaryTitleFont);

		JPanel panel =  new JPanel();
		ButtonGroup btnGroup = new ButtonGroup();
		int selectNo = 1;
		for (int i = 0; i < showSummaryLists.size(); i++) {
			ShowSummaryList ssl = showSummaryLists.get(i);
			JPanel resultPanel = new JPanel(new GridLayout(ssl.getShowSummaryList().size(),4));

			JLabel firstTotalMoneyLabel = new JLabel("     " +ssl.getTotalMoney());
			firstTotalMoneyLabel.setFont(fontEnum.todaySummaryFont);
			String no = "";
			for (int j = 0; j < ssl.getShowSummaryList().size(); j++) {
				JRadioButton selectButton = new JRadioButton(String.valueOf(i) + " " + String.valueOf(j));
				selectButton.setFont(fontEnum.oneSummaryFont);
				selectButton.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						String[] strings = selectButton.getText().split("     ");
						selectOneNo = String.valueOf(Integer.valueOf(strings[0].split(" ")[0])-1);
						selectTwoNo = strings[0].split(" ")[1];
					}
				});
				btnGroup.add(selectButton);

				ShowSummary ss = ssl.getShowSummaryList().get(j);
				if(j==0){
					JLabel noLabel = new JLabel(ssl.getNo() + "     " + ssl.getTotalPrice());
					selectButton.setText(String.valueOf(i+1)+ " " + String.valueOf(j)
							+ "     " + ssl.getNo() + "     " + ssl.getTotalPrice());
					noLabel.setFont(fontEnum.todaySummaryFont);
					JLabel firstSerialNumberLabel = new JLabel();
					if(ss.getSerialNumber().length()<= 30){
						firstSerialNumberLabel.setText(ss.getSerialNumber());
					}else {
						firstSerialNumberLabel.setText(inputFunction.numberWrap(ss.getSerialNumber()," ",5));
					}
					firstSerialNumberLabel.setFont(fontEnum.todaySummaryFont);
					JLabel firstDetailAndNoteLabel = new JLabel(ss.getDetail() + "     " + ssl.getNote());
					firstDetailAndNoteLabel.setFont(fontEnum.todaySummaryFont);
/*					JLabel firstTotalPriceLabel = new JLabel(ssl.getTotalPrice());
					firstTotalPriceLabel.setFont(fontEnum.todaySummaryFont);*/
					JLabel firstNoteLabel = new JLabel(ssl.getNote());
					firstNoteLabel.setFont(fontEnum.todaySummaryFont);

					resultPanel.add(selectButton);
					resultPanel.add(firstSerialNumberLabel);
					//resultPanel.add(noLabel);
					resultPanel.add(firstDetailAndNoteLabel);
					//resultPanel.add(firstTotalPriceLabel);
					if(i==0){
						resultPanel.add(firstTotalMoneyLabel);
					}else{
						resultPanel.add(new JLabel());
					}
				}else {
					JLabel serialNumberLabel = new JLabel();
					if(ss.getSerialNumber().length()<= 30){
						serialNumberLabel.setText(ss.getSerialNumber());
					}else {
						serialNumberLabel.setText(inputFunction.numberWrap(ss.getSerialNumber()," ",5));
					}
					serialNumberLabel.setFont(fontEnum.todaySummaryFont);
					JLabel detailLabel = new JLabel(ss.getDetail());
					detailLabel.setFont(fontEnum.todaySummaryFont);

					selectButton.setText(String.valueOf(i+1)+ " " + String.valueOf(j)
							+ "     " + ssl.getNo());
					resultPanel.add(selectButton);
					//resultPanel.add(new JLabel());
					resultPanel.add(serialNumberLabel);
					resultPanel.add(detailLabel);
					resultPanel.add(new JLabel());
				}
				panel.add(resultPanel);
				selectNo = selectNo + 1;
			}
			panel.add(new JLabel(" "));
		}

		Button update = new Button("修改");
		Button delete = new Button("删除");
		JPanel buttonPanel=new JPanel();
		buttonPanel.add(update);
		buttonPanel = componentInit.addSpace(buttonPanel,1);
		buttonPanel.add(delete);
		buttonPanel.setFont(fontEnum.oneSummaryFont);
		//panel.add(buttonPanel);

		BoxLayout layout=new BoxLayout(panel, BoxLayout.Y_AXIS);
		panel.setLayout(layout);
		JScrollPane jScrollPane = new JScrollPane(panel);
		frame.add(jScrollPane,BorderLayout.CENTER);
		frame.add(title,BorderLayout.NORTH);
		frame.add(buttonPanel,BorderLayout.SOUTH);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		/*frame.setBounds((screenSize.width - 1500) / 2, (screenSize.height - 800) / 2, 1500, 800);
		frame.setVisible(true);*/
		Rectangle bounds = new Rectangle(screenSize);
		frame.setBounds(bounds);
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		frame.setVisible(true);

		//注册监听器
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				frame.dispose();
			}
		});

		update.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(selectOneNo.equals("")){
					warmWindow.warmWindow("请先选择一条记录",fontEnum.warmInfoFont);
				}else {
					updateExcelWindow.showUpdateExcel(Integer.valueOf(selectOneNo),Integer.valueOf(selectTwoNo),
							fileName, showSummaryLists,globalVariable,frame);
					selectOneNo = "";
					selectTwoNo = "";
				}
			}
		});

		delete.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String filePath = globalVariable.filePath+ "/" + fileName + ".xlsx";
				try {
					if(selectOneNo.equals("")){
						warmWindow.warmWindow("请先选择一条记录",fontEnum.warmInfoFont);
					}else {
						String result = updateExcelFunction.deleteExcel(filePath,Integer.valueOf(selectOneNo),Integer.valueOf(selectTwoNo),
								showSummaryLists);
						if(result.contains("success")){
							if(result.contains("delete")){
								warmWindow.warmWindow("汇总excel没有数据，已经成功删除",fontEnum.warmInfoFont);
							}
							selectOneNo = "";
							selectTwoNo = "";
							frame.dispose();
							List<ShowSummaryList> showSummaryLists = inputFunction.readTodayExcel(globalVariable.filePath,fileName);
							showSummary(showSummaryLists,fileName,globalVariable);
						}else {
							if(result.equals("delete fail"))
								warmWindow.warmWindow("汇总excel没有数据，删除失败，请手动删除" + fileName + ".xlsx"
										,fontEnum.warmInfoFont);
							else
								warmWindow.warmWindow(result,fontEnum.warmInfoFont);
						}
					}
				} catch (BiffException biffException) {
					biffException.printStackTrace();
				} catch (IOException ioException) {
					ioException.printStackTrace();
				} catch (WriteException writeException) {
					writeException.printStackTrace();
				}
			}
		});
	}
}
