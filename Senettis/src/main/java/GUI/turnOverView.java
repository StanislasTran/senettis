package GUI;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.util.List;
import java.util.Objects;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Item;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

import classes.TurnOver;

public class turnOverView {
	private Composite turnOverView;
	private Composite selection;
	private Combo monthFilter;
	private Combo yearFilter;
	private Composite filterComposite;
	private Composite buttons;
	private Composite compositeTable;
	private Button saveButton;
	final int SITENAMECOLUMN = 0;
	final int MENAGECOLUMN = 1;
	final int VITRERIECOLUMN = 2;
	final int FOURNITURESCOLUMN = 3;
	final int MISESBLANCCOLUMN = 4;
	final int AUTRESCOLUMN = 5;
	final int CACOLUMN = 6;

	public turnOverView(Composite parent) throws SQLException {
		this.turnOverView = new Composite(parent, SWT.NONE);
		this.turnOverView.setLayout(new RowLayout(SWT.VERTICAL));
		selection();
		createMonthFilter();
		createYearFilter();
		compositeTable();
		

		yearFilter.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				Month month = Month.of(monthFilter.getSelectionIndex() + 1);
				Year year = Year.of(Integer.parseInt(yearFilter.getText()));
				try {
					compositeTable();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				compositeTable.layout(true, true);
				compositeTable.pack();
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				super.widgetDefaultSelected(e);
			}

		});

		monthFilter.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				try {

					compositeTable();
					compositeTable.layout(true, true);
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				super.widgetSelected(e);
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				super.widgetDefaultSelected(e);
			}

		});

	}

	private void addSaveButton(Table table, List<TurnOver> turnOvers) {
		
		if(!Objects.isNull(this.saveButton)&& !this.saveButton.isDisposed()) {
			this.saveButton.dispose();
			this.buttons.layout(true,true);
		}
		this.saveButton = new Button(this.buttons, SWT.NONE);
		this.saveButton.setText("Enregistrer");
		this.saveButton.pack();
		this.buttons.pack();
		this.saveButton.addSelectionListener(new SelectionAdapter() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				
				int i=0;
				for(TableItem item:table.getItems()) {
					
					
					
					turnOvers.get(i).setVitrerie(checkStringDouble(item.getText(VITRERIECOLUMN)));
					turnOvers.get(i).setMisesBlanc(checkStringDouble(item.getText(MISESBLANCCOLUMN)));
					turnOvers.get(i).setMenage(checkStringDouble(item.getText(MENAGECOLUMN)));
					turnOvers.get(i).setFournituresSanitaires(checkStringDouble(item.getText(FOURNITURESCOLUMN)));
					turnOvers.get(i).setAutres(checkStringDouble(item.getText(AUTRESCOLUMN)));
					turnOvers.get(i).setCA(checkStringDouble(item.getText(CACOLUMN)));
					
					System.out.println(turnOvers.get(i).getCa());
					
					System.out.println(i);
					
					try {
						
						System.out.println(turnOvers.get(i).exist());
						turnOvers.get(i).inserOrUpdateRow();
						//compositeTable();
						
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					i++;
					
				}
			}
			
		});
		

	}

	private void compositeTable() throws SQLException {
		if (!Objects.isNull(this.compositeTable) && !this.compositeTable.isDisposed()) {
			this.compositeTable.dispose();
			turnOverView.layout(true, true);
		}
		this.compositeTable = new Composite(turnOverView, SWT.NONE);
		turnOverView.layout(true, true);

		Month month = Month.of(this.monthFilter.getSelectionIndex() + 1);
		Year year = Year.of(Integer.parseInt(this.yearFilter.getText()));

		createTableListCA(month, year);
		compositeTable.pack();
		turnOverView.pack();
		turnOverView.getParent().pack();

	}

	private Table createTableListCA(Month month, Year year) throws SQLException {

		List<TurnOver> turnOvers = TurnOver.getListCAForAllSite(month, year);

		String[] titles = { "Nom de chantier", "Menage", "Vitrerie", "Fournitures Sainitaires", "Mise � Blanc",
				"autres", "CA  " };
		final Table table = new Table(this.compositeTable, SWT.FULL_SELECTION | SWT.HIDE_SELECTION|SWT.MULTI);
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		table.setLayoutData(new RowData(900, 800));

		for (String title : titles) {
			TableColumn column = new TableColumn(table, SWT.NONE);
			column.setText(title);
		}
		
		

		for (TurnOver turnOver : turnOvers) {
			

			TableItem item = new TableItem(table, SWT.NONE);
			item.setText(SITENAMECOLUMN, turnOver.getSiteName());
			item.setText(MENAGECOLUMN, "" + turnOver.getMenage());
			item.setText(VITRERIECOLUMN, "" + turnOver.getVitrerie());
			item.setText(FOURNITURESCOLUMN, "" + turnOver.getFournituresSanitaires());
			item.setText(MISESBLANCCOLUMN, "" + turnOver.getMisesBlanc());
			item.setText(AUTRESCOLUMN, "" + turnOver.getAutres());
			item.setText(CACOLUMN, "" + turnOver.getCa());
			

		}

		for (TableColumn column : table.getColumns()) {
			column.pack();
		}

		// editing the second column


		table.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				
				// Identify the selected row
				TableItem item = (TableItem) e.item;
				if (item == null)
					return;

				// The control that will be the editor must be a child of the Table
				textForColumn(MENAGECOLUMN, table, item);
				textForColumn(VITRERIECOLUMN, table,  item);
				textForColumn(FOURNITURESCOLUMN, table,  item);
				textForColumn(MISESBLANCCOLUMN, table,  item);
				textForColumn(AUTRESCOLUMN, table,  item);
				
				table.pack();
				turnOverView.pack();
				
			
				
			}
		});

		this.turnOverView.pack();
		table.pack();
		this.compositeTable.pack();
		this.turnOverView.getParent().pack();
		addSaveButton(table,turnOvers);
		
		return table;

	}

	
	public Text textForColumn(int index,Table table,TableItem item) {
		

		final TableEditor editor = new TableEditor(table);
		// Clean up any previous editor control
		Control oldEditor = editor.getEditor();
		if (oldEditor != null)
			oldEditor.dispose();

		// The editor must have the same size as the cell and must
		// not be any smaller than 50 pixels.
		editor.horizontalAlignment = SWT.LEFT;
		editor.grabHorizontal = true;
		editor.minimumWidth = 50;
		
		Text newEditor = new Text(table, SWT.NONE);
		newEditor.setText(item.getText(index));
		newEditor.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
				Text text = (Text) editor.getEditor();
				editor.getItem().setText(index, text.getText());
				
				editor.getItem().setText(CACOLUMN,computeCA(item));
		
			}

			

		});
		editor.setEditor(newEditor, item, index);
		return newEditor;

		
	}
	
	
	private String computeCA(TableItem item) {
		

		
		Double menage=checkStringDouble(item.getText(MENAGECOLUMN));
		Double vitrerie=checkStringDouble(item.getText(VITRERIECOLUMN));
		Double fournitures=checkStringDouble(item.getText(FOURNITURESCOLUMN));
		Double miseABlanc=checkStringDouble(item.getText(MISESBLANCCOLUMN));
		Double autres=checkStringDouble(item.getText(AUTRESCOLUMN));
		
		Double CA=menage+vitrerie+fournitures+miseABlanc+autres;
		return ""+CA;
	}
	
	private Double checkStringDouble(String value) {
		if(Objects.isNull(value))
			return 0.0;
		try {
			return Double.parseDouble(value);
		}catch( NumberFormatException exception) {
			return 0.0;
		}
		
		
	}
	private void selection() {
		if (!Objects.isNull(this.selection) && !this.selection.isDisposed()) {
			this.selection.dispose();
			turnOverView.layout(true, true);
		}

		this.selection = new Composite(turnOverView, SWT.NONE);
		RowLayout rowLayout = new RowLayout(SWT.VERTICAL);
		this.selection.setLayout(rowLayout);

		this.filterComposite = new Composite(this.selection, SWT.NONE);
		this.filterComposite.setLayout(new RowLayout(SWT.HORIZONTAL));

		this.buttons = new Composite(this.selection, SWT.NONE);
		this.buttons.setLayout(new RowLayout(SWT.HORIZONTAL));

	}

	/**
	 * Create the filter on Month
	 */
	private void createMonthFilter() {
		if (Objects.isNull(monthFilter) || monthFilter.isDisposed()) {
			Composite monthComposite = new Composite(this.filterComposite, SWT.NONE);
			monthComposite.setLayout(new RowLayout(SWT.HORIZONTAL));
			Label monthLabel = new Label(monthComposite, SWT.NONE);
			monthLabel.setText("Mois");
			this.monthFilter = new Combo(monthComposite, SWT.NONE);

			String[] frenchMonth = { "Janvier", "F�vrier", "Mars", "Avril", "Mai", "Juin", "Juillet", "Ao�t",
					"Septembre", "Octobre", "Novembre", "D�cembre" };

			for (String m : frenchMonth)
				this.monthFilter.add(m);
			int currentMonth = LocalDate.now().getMonth().getValue();
			this.monthFilter.select(currentMonth - 1);
			this.monthFilter.pack();
			// this.buttons.pack();
			this.selection.pack();

		}

	}

	// Year part
	private void createYearFilter() {
		if (Objects.isNull(yearFilter) || yearFilter.isDisposed()) {
			Composite yearComposite = new Composite(this.filterComposite, SWT.NONE);
			yearComposite.setLayout(new RowLayout(SWT.HORIZONTAL));
			Label yearLabel = new Label(yearComposite, SWT.NONE);
			yearLabel.setText("Ann�e");
			this.yearFilter = new Combo(yearComposite, SWT.NONE);

			int currentYear = Year.now().getValue();
			for (int i = currentYear - 2; i < currentYear + 3; i++)
				this.yearFilter.add("" + i);
			this.yearFilter.select(2);
			this.yearFilter.pack();
			this.filterComposite.pack();
			this.selection.pack();
		}

	}

	public Composite getRecurringCostView() {
		return this.turnOverView;
	}

}