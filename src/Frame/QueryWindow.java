package Frame;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.table.*;

import Bean.*;
import Enum.*;
import Function.InputFunction;
import Function.QueryFunction;
import Function.ComponentInit;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

public class QueryWindow {
	private final FontEnum fontEnum = new FontEnum();
	private final ComponentInit componentInit = new ComponentInit();
	private final InputFunction inputFunction = new InputFunction();
	private final QueryFunction queryFunction = new QueryFunction();
	private final WarmWindow warmWindow = new WarmWindow();

	public void showQueryWindow(GlobalVariable globalVariable){
		JFrame jFrame = new JFrame("中奖查询系统");

		//excel文件名
		JTextField fileNameJF=new JTextField(30);
		fileNameJF.setFont(fontEnum.mainFont);
		Button fileSelectButton = new Button("选择文件");
		fileSelectButton.setFont(fontEnum.mainButtonFont);

		//开奖号码
		JTextField kaiJiangNumberJF=new JTextField(10);
		kaiJiangNumberJF.setFont(fontEnum.mainFont);

		//类别
		JTextField typeTwoJF=new JTextField(TypeTwoEnum.TICAI.getLabel(),10);
		typeTwoJF.setFont(fontEnum.mainFont);
		typeTwoJF.setVisible(false);
		JPanel typeTwoPanel = new JPanel();
		List<JRadioButton> typeTwoList = new ArrayList<>();
		ButtonGroup typeTwoGroup = new ButtonGroup();
		for(TypeTwoEnum typeTwoEnum: TypeTwoEnum.values()){
			JRadioButton typeTwoButton = new JRadioButton(typeTwoEnum.getLabel());
			typeTwoButton.setFont(fontEnum.mainButtonFont);
			typeTwoButton.addActionListener(e -> typeTwoJF.setText(typeTwoButton.getText()));
			if(typeTwoButton.getText().equals(TypeTwoEnum.TICAI.getLabel())){
				typeTwoButton.setSelected(true);
			}
			typeTwoList.add(typeTwoButton);
			typeTwoGroup.add(typeTwoButton);
			typeTwoPanel.add(typeTwoButton);
		}

		//类型
		JTextField typeJF=new JTextField(TypeEnum.ALL.getLabel(),10);
		typeJF.setFont(fontEnum.mainFont);
		typeJF.setVisible(false);
		JComboBox<String> typeComboBox = new JComboBox<>();
		for(TypeEnum typeEnum: TypeEnum.values()){
			if(queryFunction.filterType(typeEnum.getLabel())){
				continue;
			}
			typeComboBox.addItem(typeEnum.getLabel());
			typeComboBox.setFont(fontEnum.mainFont);
		}
		typeComboBox.setSelectedItem(TypeEnum.ALL.getLabel());

		//中奖号码
		JLabel zhongJiangNumberTitle=new JLabel("中奖号码");
		zhongJiangNumberTitle.setFont(fontEnum.oneSummaryPlainFont);

		String[] columnNames = {"序列号","详情","中奖号码","中奖金额","备注"};
		String[][] tableData =  new String[0][0];

		DefaultTableModel defaultTableModel = new DefaultTableModel(tableData,columnNames);
		//JTable jTable = new JTable(defaultTableModel);
		JTable jTable = new JTable(defaultTableModel){
			public boolean isCellEditable(int row, int column){
				return false;
			}
		};

		//表头
		JTableHeader jTableHeader = jTable.getTableHeader();
		jTableHeader.setFont(fontEnum.tableFont);

		int[] width={110,500,900,700,400};
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
		Button copyButton = new Button("复制");
		JPanel buttonPanel = new JPanel();
		buttonPanel.add(confirmButton);
		buttonPanel.add(resetButton);
		//buttonPanel.add(copyButton);
		buttonPanel.setFont(fontEnum.mainButtonFont);

		//添加控件
		JPanel northJPanel = new JPanel();
		BoxLayout layout=new BoxLayout(northJPanel, BoxLayout.Y_AXIS);
		northJPanel.setLayout(layout);

		//第一行 文件名
		JPanel fileNamePanel = componentInit.iniJPanel(new JPanel(),"文件名",fileNameJF);
		fileNamePanel.add(fileSelectButton);
		northJPanel.add(fileNamePanel);

		//第二行 开奖号码+类别
		JPanel kaiJiangAndTypePanel = componentInit.iniJPanel(new JPanel(),"类型",typeComboBox);
		kaiJiangAndTypePanel = componentInit.addSpace(kaiJiangAndTypePanel,1);
		kaiJiangAndTypePanel = componentInit.iniJPanel(kaiJiangAndTypePanel,"类别",typeTwoPanel);
		kaiJiangAndTypePanel = componentInit.addSpace(kaiJiangAndTypePanel,1);
		kaiJiangAndTypePanel = componentInit.initJPanel(kaiJiangAndTypePanel,"开奖号码",kaiJiangNumberJF);
		kaiJiangAndTypePanel = componentInit.addSpace(kaiJiangAndTypePanel,2);
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

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		//窗口设置
		/*jFrame.setBounds((screenSize.width - 1700) / 2, (screenSize.height - 800) / 2, 1700, 800);
		jFrame.setVisible(true);*/

		//全屏
		Rectangle bounds = new Rectangle(screenSize);
		jFrame.setBounds(bounds);
		jFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		jFrame.setVisible(true);

		//注册监听器
		jFrame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});

		fileSelectButton.addActionListener(e -> {
			FileFilter fileFilter = new FileFilter() {
				@Override
				public boolean accept(File f) {
					String fileName = f.getName();
					return f.isDirectory() || fileName.endsWith(".xls") || fileName.endsWith(".xlsx");
				}

				@Override
				public String getDescription() {
					return null;
				}
			};

			JFileChooser jFileChooser = new JFileChooser(new File(globalVariable.filePath));
			jFileChooser.addChoosableFileFilter(fileFilter);
			jFileChooser.setFileFilter(fileFilter);
			jFileChooser.showOpenDialog(jFileChooser);
			File selectedFile = jFileChooser.getSelectedFile();
			if(selectedFile != null)
				fileNameJF.setText(selectedFile.getName().replace(".xlsx",""));
		});

		typeComboBox.addItemListener(e -> typeJF.setText(Objects.requireNonNull(typeComboBox.getSelectedItem()).toString()));
		typeComboBox.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				typeComboBox.setPopupVisible(true);
			}
		});

		confirmButton.addActionListener(e -> {
			if(kaiJiangNumberJF.getText().length()<3){
				warmWindow.warmWindow("请输入正确的开奖号码",fontEnum.warmInfoFont);
				return;
			}
			//获取打奖和赔率的excel数据
			SimpleDateFormat sf= new SimpleDateFormat("yyyy-MM-dd");
			Date date = new Date();
			String fileName = sf.format(date)+".xlsx";
			if(!fileNameJF.getText().equals("")){
				fileName = fileNameJF.getText()+".xlsx";
			}

			List<ShowSummaryList> showSummaryLists = inputFunction.readTodayExcel(globalVariable.filePath,fileName);

			//校验文件是否存在
			if(showSummaryLists == null) {
				warmWindow.warmWindow(globalVariable.filePath + fileName + "不存在"
						,fontEnum.warmInfoFont);
				return;
			}
			Map<String,Float> peiLvMap = queryFunction.readPeiLvExcel(globalVariable.filePath);
			if(peiLvMap == null) {
				warmWindow.warmWindow(globalVariable.filePath + "赔率.xlsx不存在"
						,fontEnum.warmInfoFont);
				return;
			}

			//获取中奖号码
			List<ZhongJiang> zhongJiangNumberList = new ArrayList<>();
			if(typeJF.getText().equals(TypeEnum.ALL.getLabel())){
				for(TypeEnum typeEnum : TypeEnum.values()){
					zhongJiangNumberList = queryFunction.findTypeTwoAndType(kaiJiangNumberJF.getText(),
							typeTwoJF.getText(),typeEnum.getLabel(),AllFlagEnum.ALL.getVal(),
							showSummaryLists,zhongJiangNumberList,peiLvMap);
				}
				//zhongJiangNumberList = zhongJiangNumberList.stream().sorted(Comparator.comparing(ZhongJiang ::getSortNo)).collect(Collectors.toList());
			}else {
				zhongJiangNumberList = queryFunction.findTypeTwoAndType(kaiJiangNumberJF.getText(),
						typeTwoJF.getText(),typeJF.getText(),AllFlagEnum.NOTALL.getVal(),
						showSummaryLists,zhongJiangNumberList,peiLvMap);
			}

			String[][] tableData1 = queryFunction.transformTableData(zhongJiangNumberList);
			defaultTableModel.setDataVector(tableData1,columnNames);

			//自动换行
			jTable.setDefaultRenderer(Object.class, tcr);
			for (String[] tableDatum : tableData1) {
				if (tableDatum[2].length() >= 110 || tableDatum[3].length() >= 100) {
					jTable.setDefaultRenderer(Object.class, new TableCellTextAreaRenderer());
					break;
				}
			}

			//列宽
			TableColumnModel tableColumnModel1 = jTable.getColumnModel();
			for (int i = 0; i < tableColumnModel1.getColumnCount(); i++) {
				TableColumn tableColumn = tableColumnModel1.getColumn(i);
				tableColumn.setPreferredWidth(width[i]);
				/*if( i == 3){
					tableColumn.setCellRenderer(new TableColumnTextAreaRenderer());
				}*/
			}
		});

		resetButton.addActionListener(e -> {
			kaiJiangNumberJF.setText("");
			typeTwoJF.setText(TypeTwoEnum.TICAI.getLabel());
			for (JRadioButton jRadioButton : typeTwoList){
				if (jRadioButton.getText().equals(TypeTwoEnum.TICAI.getLabel())){
					jRadioButton.setSelected(true);
				}
			}
			typeJF.setText(TypeEnum.ALL.getLabel());
			typeComboBox.setSelectedItem(TypeEnum.ALL.getLabel());
		});

		copyButton.addActionListener(e -> queryFunction.copyConText(jTable));

		jTable.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if (e.getClickCount() >= 2){
					queryFunction.copyConText(jTable);
				}
			}
		});
	}

	class TableColumnTextAreaRenderer extends JLabel implements TableCellRenderer {
		public TableColumnTextAreaRenderer() {
			setFont(fontEnum.tableFont);
			setHorizontalAlignment(JLabel.CENTER);
			setVerticalAlignment(JLabel.TOP);
		}

		public Component getTableCellRendererComponent(JTable table, Object value,
													   boolean isSelected, boolean hasFocus, int row, int column) {
			for (int i = 0; i < table.getColumnCount(); i++) {
				setText("" + table.getValueAt(row, i));
			}

			setText(value == null ? "" : value.toString());
			return this;
		}
	}

	//jTable换行
	class TableCellTextAreaRenderer extends JLabel implements TableCellRenderer {
		public TableCellTextAreaRenderer() {
			setFont(fontEnum.tableFont);
			setHorizontalAlignment(JLabel.CENTER);
			setVerticalAlignment(JLabel.CENTER);
		}

		public Component getTableCellRendererComponent(JTable table, Object value,
																boolean isSelected, boolean hasFocus, int row, int column) {
			for (int i = 0; i < table.getColumnCount(); i++) {
				setText("" + table.getValueAt(row, i));
			}

			int count = value.toString().split("<br>").length -1;
			if(count != 0 && !value.toString().contains("+")){
				if(count <= 10){
					table.setRowHeight(row,(count*60));
				}
				if(count > 10 && count <= 1000){
					table.setRowHeight(row,(count*31));
				}
				if(count > 1000){
					table.setRowHeight(row,(count*25));
				}

				/*if(count > 100){
					String s = value.toString().split(" ")[0].replace("<html>","")
							.split("<")[0];
					if(s.length() >= 9){
						table.setRowHeight(row,(count*25));
					}}*/
			}

			setText(value == null ? "" : value.toString());
			return this;
		}
	}
}
