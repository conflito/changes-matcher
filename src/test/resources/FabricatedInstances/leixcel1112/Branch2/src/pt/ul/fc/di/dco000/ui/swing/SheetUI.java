package pt.ul.fc.di.dco000.ui.swing;

import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.AbstractTableModel;

import pt.ul.fc.di.dco000.ui.SheetUIDelegate;


public class SheetUI extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4963439250026949013L;
	
	private final JPanel contentPane;
	private final JTable table;

	private final SheetUIDelegate sheetUIDelegate;
	
	private final FileController fileController;
	private final EditController editController;
	private final InsertController insertController;
	
	
	

	/**
	 * Create the frame.
	 * @param sheetUIDelegate 
	 */
	public SheetUI(final SheetUIDelegate sheetUIDelegate) {
		this.sheetUIDelegate = sheetUIDelegate;
		sheetUIDelegate.setSheetUI(this);
		fileController = new FileController(sheetUIDelegate, this);
		editController = new EditController(sheetUIDelegate, this);
		insertController = new InsertController(sheetUIDelegate, this);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);		
		
		final JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		createFileMenu(menuBar);
		createEditMenu(menuBar);
		createInsertMenu(menuBar);
		
		table = new JTable();
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setCellSelectionEnabled(true);
		table.setModel(new SheetTableModel(sheetUIDelegate));
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

		table.setDefaultEditor(Object.class, new CellEditorUI(sheetUIDelegate));
		
		final JScrollPane scrollPane = new JScrollPane(table);
		final JTable rowTable = new RowNumberTable(table);

		table.addMouseListener(new MouseAdapter() {
			
			@Override
			public void mouseReleased(MouseEvent event) {
				final JTable table = (JTable) event.getSource();
				sheetUIDelegate.selectCell(table.getSelectedRow(), table.getSelectedColumn());
			}
		});
		
		scrollPane.setRowHeaderView(rowTable);
		scrollPane.setCorner(JScrollPane.UPPER_LEFT_CORNER,
		    rowTable.getTableHeader());

		contentPane.add(scrollPane, BorderLayout.CENTER);
		
		
		sheetUIDelegate.refreshTitle();
	}

	private void createInsertMenu(JMenuBar menuBar) {
		final JMenu insertMenu = new JMenu("Insert");
		menuBar.add(insertMenu);
		
		final JMenuItem insertRowBeforeMenuItem = new JMenuItem("Insert Row Before");
		insertMenu.add(insertRowBeforeMenuItem);
		insertRowBeforeMenuItem.addActionListener(insertController.insertRowBeforeController());
		
		final JMenuItem insertRowAfterMenuItem = new JMenuItem("Insert Row After");
		insertMenu.add(insertRowAfterMenuItem);
		insertRowAfterMenuItem.addActionListener(insertController.insertRowAfterController());
		
		insertMenu.add(new JSeparator());
		
		final JMenuItem insertColumnBeforeMenuItem = new JMenuItem("Insert Column Before");
		insertMenu.add(insertColumnBeforeMenuItem);
		insertColumnBeforeMenuItem.addActionListener(insertController.insertColumnBeforeController());
		
		final JMenuItem insertColumnAfterMenuItem = new JMenuItem("Insert Column After");
		insertMenu.add(insertColumnAfterMenuItem);
		insertColumnAfterMenuItem.addActionListener(insertController.insertColumnAfterController());
		
		insertMenu.add(new JSeparator());
		
		final JMenuItem insertSheetAtTheEnd = new JMenuItem("Insert Sheet...");
		insertMenu.add(insertSheetAtTheEnd);
		insertSheetAtTheEnd.addActionListener(insertController.insertSheetAtTheEndController());
	}

	private void createEditMenu(JMenuBar menuBar) {
		final JMenu editMenu = new JMenu("Edit");
		menuBar.add(editMenu);
		
		final JMenuItem editDeleteRowMenuItem = new JMenuItem("Delete Row");
		editMenu.add(editDeleteRowMenuItem);
		editDeleteRowMenuItem.addActionListener(editController.deleteRowController());
		
		final JMenuItem editDeleteColumnMenuItem = new JMenuItem("Delete Column");
		editMenu.add(editDeleteColumnMenuItem);
		editDeleteColumnMenuItem.addActionListener(editController.deleteColumnController());
		
		editMenu.add(new JSeparator());
		
		final JMenuItem editSheetNameMenuItem = new JMenuItem("Rename sheet...");
		editMenu.add(editSheetNameMenuItem);
		editSheetNameMenuItem.addActionListener(editController.editSheetNameController());

	}

	private void createFileMenu(JMenuBar menuBar) {
		final JMenu fileMenu = new JMenu("File");
		menuBar.add(fileMenu);
		
		final JMenuItem fileNewMenuItem = new JMenuItem("New Workbook...");
		fileMenu.add(fileNewMenuItem);
		fileNewMenuItem.addActionListener(fileController.newWorkbookController());
		
	
		// Export sub-menu
		final JMenu fileExportMenu = new JMenu("Export");
		fileMenu.add(fileExportMenu);
		
		final JMenuItem fileExportAHTMLMenuItem = new JMenuItem("as HTML...");
		fileExportMenu.add(fileExportAHTMLMenuItem);
		fileExportAHTMLMenuItem.addActionListener(fileController.exportAsHTMLController());
		
		final JMenuItem fileExportOpenOfficeMenuItem = new JMenuItem("as Open Office...");
		fileExportMenu.add(fileExportOpenOfficeMenuItem);
		fileExportOpenOfficeMenuItem.addActionListener(fileController.exportAsOpenOfficeController());

		fileMenu.add(new JSeparator());
		
		// Quit
		final JMenuItem fileQuitMenuItem = new JMenuItem("Quit");
		fileQuitMenuItem.addActionListener(fileController.quitController());
		fileMenu.add(fileQuitMenuItem);
	}

	public void refreshSheetStructure() {
		((AbstractTableModel) table.getModel()).fireTableStructureChanged();
	}
	
	public void refreshSheetNewline(int arg) {
		((AbstractTableModel) table.getModel()).fireTableRowsInserted(arg, arg);
	}

	public void refreshSheetNewcol(int arg) {
		((AbstractTableModel) table.getModel()).fireTableStructureChanged();
		/*
		 * arg must be ignored, but let's keep it in case we come up
		 * with a better solution
		 */
		
		
	}
	
}
