package Frame;

import javax.swing.*;
import javax.swing.table.*;

import Bean.*;
import Enum.*;
import Start.*;

import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class QueryWindow {
	private final FontEnum fontEnum = new FontEnum();
	private final JPanelInit jPanelInit = new JPanelInit();
	private final Function function = new Function();
	private final QueryFunction queryFunction = new QueryFunction();

	public void showQueryWindow(GlobalVariable globalVariable){
		JFrame jFrame = new JFrame("中奖查询系统");

		//excel文件名
		JTextField fileNameJF=new JTextField(80);
		fileNameJF.setFont(fontEnum.mainFont);

		//开奖号码
		JTextField kaiJiangNumberJF=new JTextField(30);
		kaiJiangNumberJF.setFont(fontEnum.mainFont);

		//类别
		JTextField typeTwoJF=new JTextField(TypeTwoEnum.TICAI.getLabel(),10);
		typeTwoJF.setFont(fontEnum.mainFont);
		typeTwoJF.setVisible(false);
		JComboBox typeTwoComboBox = new JComboBox();
		for(TypeTwoEnum typeTwoEnumEnum: TypeTwoEnum.values()){
			typeTwoComboBox.addItem(typeTwoEnumEnum.getLabel());
			typeTwoComboBox.setFont(fontEnum.mainFont);
		}

		//类型
		JTextField typeJF=new JTextField(TypeEnum.ZHIXUAN.getLabel(),10);
		typeJF.setFont(fontEnum.mainFont);
		typeJF.setVisible(false);
		JComboBox typeComboBox = new JComboBox();
		for(TypeEnum typeEnum: TypeEnum.values()){
			if(typeEnum.getLabel().contains(TypeEnum.ZL.getLabel()) && typeEnum.getLabel().length()>2){
				if(!typeEnum.getLabel().contains("全包"))
					continue;
			}
			if(typeEnum.getLabel().contains(TypeEnum.ZS.getLabel()) && typeEnum.getLabel().length()>2){
				if(!typeEnum.getLabel().contains("全包"))
					continue;
			}
			if(typeEnum.getLabel().contains(TypeEnum.KD.getLabel()) && typeEnum.getLabel().length()>2){
				continue;
			}
			if(typeEnum.getLabel().contains(TypeEnum.KD.getLabel()) && typeEnum.getLabel().length()>2){
				continue;
			}
			if(typeEnum.getLabel().contains(TypeEnum.HZ.getLabel()) && typeEnum.getLabel().length()>2){
				continue;
			}
			if(typeEnum.getLabel().contains(TypeEnum.FS.getLabel()) && typeEnum.getLabel().length()>2){
				continue;
			}
			typeComboBox.addItem(typeEnum.getLabel());
			typeComboBox.setFont(fontEnum.mainFont);
		}
		typeComboBox.setSelectedItem(TypeEnum.ZHIXUAN.getLabel());

		//中奖号码
		JLabel zhongJiangNumberTitle=new JLabel("中奖号码");
		zhongJiangNumberTitle.setFont(fontEnum.oneSummaryPlainFont);

		String[] columnNames = {"序列号","中奖号码","详情"};
		String[][] tableData =  new String[0][0];

		DefaultTableModel defaultTableModel = new DefaultTableModel(tableData,columnNames);
		JTable jTable = new JTable(defaultTableModel){
			public boolean isCellEditable(int row, int column){
				return false;
			}
		};

		//表头
		JTableHeader jTableHeader = jTable.getTableHeader();
		jTableHeader.setFont(fontEnum.tableFont);

		int[] width={150,1300,500};
		TableColumnModel tableColumnModel = jTable.getColumnModel();
		for (int i = 0; i < tableColumnModel.getColumnCount(); i++) {
			TableColumn tableColumn = tableColumnModel.getColumn(i);
			tableColumn.setPreferredWidth(width[i]);
		}
		//内容
		DefaultTableCellRenderer tcr = new DefaultTableCellRenderer();
 		tcr.setHorizontalAlignment(JLabel.CENTER);
		jTable.setDefaultRenderer(Object.class, tcr);

		jTable.setRowHeight(50);
		jTable.setFont(fontEnum.tableFont);
		JScrollPane jScrollPane = new JScrollPane(jTable);

		//按钮
		Button confirmButton = new Button("确定");
		Button resetButton = new Button("重置");
		JPanel buttonPanel = new JPanel();
		buttonPanel.add(confirmButton);
		buttonPanel.add(resetButton);
		buttonPanel.setFont(fontEnum.mainButtonFont);

		//添加控件
		JPanel northJPanel = new JPanel();
		BoxLayout layout=new BoxLayout(northJPanel, BoxLayout.Y_AXIS);
		northJPanel.setLayout(layout);

		//第一行 文件名
		JPanel fileNamePanel = jPanelInit.iniJPanel(new JPanel(),"文件名",fileNameJF);
		northJPanel.add(fileNamePanel);

		//第二行 开奖号码+类别
		JPanel kaiJiangAndTypePanel = jPanelInit.iniJPanel(new JPanel(),"类别",typeTwoComboBox);
		kaiJiangAndTypePanel = jPanelInit.addSpace(kaiJiangAndTypePanel,1);
		kaiJiangAndTypePanel = jPanelInit.iniJPanel(kaiJiangAndTypePanel,"类型",typeComboBox);
		kaiJiangAndTypePanel = jPanelInit.addSpace(kaiJiangAndTypePanel,1);
		kaiJiangAndTypePanel = jPanelInit.initJPanel(kaiJiangAndTypePanel,"开奖号码",kaiJiangNumberJF);
		kaiJiangAndTypePanel = jPanelInit.addSpace(kaiJiangAndTypePanel,2);
		kaiJiangAndTypePanel.add(buttonPanel);
		northJPanel.add(kaiJiangAndTypePanel);

		//第三行 按钮
		northJPanel.add(buttonPanel);

		//第四行 中奖号码
		JPanel zhongJiangNumberTitlePanel = new JPanel();
		zhongJiangNumberTitlePanel.add(zhongJiangNumberTitle);
		northJPanel.add(zhongJiangNumberTitlePanel);

		//添加组件
		jFrame.add(northJPanel, BorderLayout.NORTH);
		jFrame.add(jScrollPane, BorderLayout.CENTER);

		//窗口设置
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		jFrame.setBounds((screenSize.width - 1500) / 2, (screenSize.height - 800) / 2, 1500, 800);
		jFrame.setVisible(true);

		//注册监听器
		jFrame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				jFrame.dispose();
			}
		});

		typeTwoComboBox.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				typeTwoJF.setText(typeTwoComboBox.getSelectedItem().toString());
			}
		});
		typeTwoComboBox.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				typeTwoComboBox.setPopupVisible(true);
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

		confirmButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				SimpleDateFormat sf= new SimpleDateFormat("yyyy-MM-dd");
				Date date = new Date();
				String fileName = sf.format(date);
				if(!fileNameJF.getText().equals("")){
					fileName = fileNameJF.getText();
				}
				List<ShowSummaryList> showSummaryLists = function.readTodayExcel(globalVariable.filePath,fileName);
				String[][] tableData = queryFunction.findTypeTwoAndType(kaiJiangNumberJF.getText(),
						typeTwoJF.getText(),typeJF.getText(),showSummaryLists);

				defaultTableModel.setDataVector(tableData,columnNames);

				//列宽
				TableColumnModel tableColumnModel = jTable.getColumnModel();
				for (int i = 0; i < tableColumnModel.getColumnCount(); i++) {
					TableColumn tableColumn = tableColumnModel.getColumn(i);
					tableColumn.setPreferredWidth(width[i]);
				}

				//自动换行
				jTable.setDefaultRenderer(Object.class, tcr);
				for (int i = 0; i < tableData.length; i++) {
					if(tableData[i][1].length() >= 110){
						jTable.setDefaultRenderer(Object.class, new TableCellTextAreaRenderer());
						break;
					}
				}
			}
		});

		resetButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				kaiJiangNumberJF.setText("");
				typeTwoJF.setText(TypeTwoEnum.TICAI.getLabel());
				typeTwoComboBox.setSelectedItem(TypeTwoEnum.TICAI.getLabel());
				typeJF.setText(TypeEnum.ZHIXUAN.getLabel());
				typeComboBox.setSelectedItem(TypeEnum.ZHIXUAN.getLabel());
			}
		});
	}

	//jTable换行
	class TableCellTextAreaRenderer extends JTextArea implements TableCellRenderer {
		public TableCellTextAreaRenderer() {
			setLineWrap(true);
			setWrapStyleWord(true);
			setFont(fontEnum.tableFont);
		}

		public Component getTableCellRendererComponent(JTable table, Object value,
													   boolean isSelected, boolean hasFocus, int row, int column) {
			//147
			for (int i = 0; i < table.getColumnCount(); i++) {
				setText("" + table.getValueAt(row, i));
			}

			if(value.toString().length() >= 110){
				int times = 0;
				int length = value.toString().length();
				while(length > 0){
					length = length/10;
					times++;
				}
				times = times - 1;
				table.setRowHeight(row,value.toString().length()/times);
			}

			setText(value == null ? "" : value.toString());
			return this;
		}
	}
}
