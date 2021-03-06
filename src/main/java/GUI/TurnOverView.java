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
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

import classes.Rentability;
import classes.TurnOver;

/**
 * View used to manage turnOver into the system
 *
 */
public class TurnOverView {
	private Composite header;
	private Composite turnOverView;
	private Composite selection;
	private Combo monthFilter;
	private Combo yearFilter;
	private Composite filterComposite;
	private Composite buttons;
	private Composite compositeTable;
	private Button saveButton;
	private Button lastButton;
	final int SITENAMECOLUMN = 0;
	final int MENAGECOLUMN = 1;
	final int VITRERIECOLUMN = 2;
	final int FOURNITURESCOLUMN = 3;
	final int MISESBLANCCOLUMN = 4;
	final int AUTRESCOLUMN = 5;
	final int CACOLUMN = 6;

	/**
	 * Constructor
	 * 
	 * @param <type>Composite</type>parent composite which will contains this view
	 * @throws SQLException
	 */
	public TurnOverView(Composite parent) throws SQLException {
		this.turnOverView = new Composite(parent, SWT.NONE);
		this.turnOverView.setLayout(new RowLayout(SWT.VERTICAL));
		addHeader("Gestion du Chiffre d'Affaire");
		selection();
		turnOverView.setBackground(MyColor.bleuClair);
		createMonthFilter();
		createYearFilter();
		compositeTable();

		yearFilter.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				try {
					compositeTable();
				} catch (SQLException e1) {
					MessageBox msgBox = new MessageBox(turnOverView.getShell(), SWT.ERROR);
					msgBox.setMessage("Erreur Base de donnée");
					msgBox.setText("erreur liée à la base de données : \n" + e1.getMessage());
					msgBox.open();

				}
				compositeTable.layout(true, true);
				compositeTable.pack();
			}

		});

		monthFilter.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				try {

					compositeTable();
					compositeTable.layout(true, true);
				} catch (SQLException e1) {
					MessageBox msgBox = new MessageBox(turnOverView.getShell(), SWT.ERROR);
					msgBox.setMessage("Erreur Base de donnée");
					msgBox.setText("erreur liée à la base de données : \n" + e1.getMessage());
					msgBox.open();
				}
				super.widgetSelected(e);
			}

		});

	}

	/**
	 * Add the button to save turnOvers data into the database
	 * 
	 * @param <type>Table</type>table               the table which contains
	 *                                              turnOvers data
	 * @param <type>List<turnOvers></type>turnOvers list of turnOvers Data before
	 *                                              the update
	 */
	private void addSaveButton(Table table, List<TurnOver> turnOvers) {

		if (!Objects.isNull(this.saveButton) && !this.saveButton.isDisposed()) {
			this.saveButton.dispose();
			this.buttons.layout(true, true);
		}
		this.saveButton = new Button(this.buttons, SWT.NONE);
		this.buttons.layout(true, true);
		this.saveButton.setText("Enregistrer");
		this.saveButton.pack();
		this.buttons.pack();
		this.saveButton.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				boolean erreurs = false;

				int i = 0;
				for (TableItem item : table.getItems()) {
					try {
						if (i<table.getItemCount()-1) {
							turnOvers.get(i).setGlazing(checkStringDouble(item.getText(VITRERIECOLUMN)));
							turnOvers.get(i).setMisesBlanc(checkStringDouble(item.getText(MISESBLANCCOLUMN)));
							turnOvers.get(i).setCleaning(checkStringDouble(item.getText(MENAGECOLUMN)));
							turnOvers.get(i).setFS(checkStringDouble(item.getText(FOURNITURESCOLUMN)));
							turnOvers.get(i).setAutres(checkStringDouble(item.getText(AUTRESCOLUMN)));
							turnOvers.get(i).setTurnOver(checkStringDouble(item.getText(CACOLUMN)));

							System.out.println(turnOvers.get(i).exist());
							turnOvers.get(i).inserOrUpdateRow();
						}

					} catch (Exception e1) {
						erreurs = true;
						MessageBox dialog = new MessageBox(turnOverView.getShell(), SWT.ICON_ERROR | SWT.OK);
						dialog.setText("Erreur de saisie");
						dialog.setMessage("Erreur : " + e1.getMessage());
						dialog.open();
						break;
					}
					i++;

				}
				if (!erreurs) { // on affiche le message de succes si on a eu aucune erreur
					MessageBox dialog = new MessageBox(turnOverView.getShell(), SWT.ICON_WORKING | SWT.OK);
					dialog.setText("Succès");
					dialog.setMessage("Données enregistrées avec succès");
					dialog.open();
				}
			}

		});

	}

	/**
	 * Add a button to reuse data from previous month
	 * 
	 * @param <type>Table</type>table              table which contains actual data
	 * @param <type>list<turnOver></type>turnOvers list with forme data
	 */
	private void addLastButton(Table table, List<TurnOver> turnOvers) {

		if (!Objects.isNull(this.lastButton) && !this.lastButton.isDisposed()) {
			this.lastButton.dispose();
			this.buttons.layout(true, true);
		}
		this.lastButton = new Button(this.buttons, SWT.NONE);
		this.buttons.layout(true, true);
		this.lastButton.setText("Récupérer données précédentes");

		this.lastButton.pack();
		this.lastButton.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				boolean erreurs = false;

				try {

					Table t = compositeTableLast();
					addSaveButton(t, turnOvers);
					addLastButton(t, turnOvers);
				} catch (Exception e1) {
					erreurs = true;
					MessageBox dialog = new MessageBox(turnOverView.getShell(), SWT.ICON_ERROR | SWT.OK);
					dialog.setText("Erreur de saisie");
					dialog.setMessage("Erreur : " + e1.getMessage());
					dialog.open();

				}

				if (!erreurs) { // on affiche le message de succes si on a eu aucune erreur

				}
			}

		});

	}

	/**
	 * Create the composite which contains the table with the list of turnOvers
	 * 
	 * @throws SQLException
	 */
	private void compositeTable() throws SQLException {
		if (!Objects.isNull(this.compositeTable) && !this.compositeTable.isDisposed()) {
			this.compositeTable.dispose();
			turnOverView.layout(true, true);
		}

		RowLayout rowLayoutV = new RowLayout();
		rowLayoutV.type = SWT.VERTICAL;

		compositeTable = new Composite(turnOverView, SWT.NONE);
		compositeTable.setLayout(rowLayoutV);
		compositeTable.setBackground(MyColor.bleuClair);
		
		turnOverView.layout(true, true);

		Month month = Month.of(this.monthFilter.getSelectionIndex() + 1);
		Year year = Year.of(Integer.parseInt(this.yearFilter.getText()));

		createTableListTurnOver(month, year);
		
		turnOverView.pack();
		turnOverView.getParent().pack();

	}

	/**
	 * update the table with the last month data
	 * 
	 * @return <type>Table</type> table with previous month data
	 * @throws SQLException
	 */
	private Table compositeTableLast() throws SQLException {
		if (!Objects.isNull(this.compositeTable) && !this.compositeTable.isDisposed()) {
			this.compositeTable.dispose();
			turnOverView.layout(true, true);
		}
		RowLayout rowLayoutV = new RowLayout();
		rowLayoutV.type = SWT.VERTICAL;

		compositeTable = new Composite(turnOverView, SWT.NONE);
		compositeTable.setLayout(rowLayoutV);
		compositeTable.setBackground(MyColor.bleuClair);
		
		turnOverView.layout(true, true);

		int month = this.monthFilter.getSelectionIndex() + 1;
		int year = Integer.parseInt(this.yearFilter.getText());
		if (month == 1) {
			return createTableListTurnOver(Month.of(12), Year.of(year - 1));
		} else

			return createTableListTurnOver(Month.of(month - 1), Year.of(year));
	}

	/**
	 * Create the table with the list of turnOvers
	 * 
	 * @throws SQLException
	 */

	private Table createTableListTurnOver(Month month, Year year) throws SQLException {

		List<TurnOver> turnOvers = TurnOver.getListCAForAllSite(month, year);

		String[] titles = { "Nom de chantier", "Menage", "Vitrerie", "Fournitures Sainitaires", "Mise à Blanc",
				"autres", "CA  " };
		final Table table = new Table(this.compositeTable, SWT.BORDER | SWT.FULL_SELECTION | SWT.HIDE_SELECTION | SWT.MULTI | SWT.V_SCROLL);
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		table.setLayoutData(new RowData(700, 500));

		for (String title : titles) {
			TableColumn column = new TableColumn(table, SWT.NONE);
			column.setText(title);
		}

		for (TurnOver turnOver : turnOvers) {

			TableItem item = new TableItem(table, SWT.NONE);
			item.setText(SITENAMECOLUMN, turnOver.getSiteName());
			item.setText(MENAGECOLUMN, String.format("%.2f",turnOver.getCleaning()));
			item.setText(VITRERIECOLUMN, String.format("%.2f",turnOver.getGlazing()));
			item.setText(FOURNITURESCOLUMN, String.format("%.2f",turnOver.getFS()));
			item.setText(MISESBLANCCOLUMN, String.format("%.2f",turnOver.getMisesBlanc()));
			item.setText(AUTRESCOLUMN, String.format("%.2f", turnOver.getOthers()));
			item.setText(CACOLUMN, String.format("%.2f",turnOver.getTurnOver()));

		}

		//total
		List<TurnOver> totalList = TurnOver.getTotalListCAForAllSite(month, year);
		if (!totalList.isEmpty()) {
			TurnOver turnOver = totalList.get(0);
			TableItem item = new TableItem(table, SWT.NONE);
			item.setText(SITENAMECOLUMN, turnOver.getSiteName());
			item.setText(MENAGECOLUMN, String.format("%.2f",turnOver.getCleaning()));
			item.setText(VITRERIECOLUMN, String.format("%.2f",turnOver.getGlazing()));
			item.setText(FOURNITURESCOLUMN, String.format("%.2f",turnOver.getFS()));
			item.setText(MISESBLANCCOLUMN, String.format("%.2f",turnOver.getMisesBlanc()));
			item.setText(AUTRESCOLUMN, String.format("%.2f", turnOver.getOthers()));
			item.setText(CACOLUMN, String.format("%.2f",turnOver.getTurnOver()));
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
				textForColumn(VITRERIECOLUMN, table, item);
				textForColumn(FOURNITURESCOLUMN, table, item);
				textForColumn(MISESBLANCCOLUMN, table, item);
				textForColumn(AUTRESCOLUMN, table, item);

			}
		});

		table.pack();
		compositeTable.pack();
		addSaveButton(table, turnOvers);
		addLastButton(table, turnOvers);
		return table;

	}

	/***
	 * add textEditor to modify data into the table
	 * 
	 * @param <type>int</type>   index the column index
	 * @param <type>Table</type> table which contains turnOvers data
	 * @param <type>Item</type>  item which contain row data
	 * @return <type>Text</type> newEditor, the text field to update a field
	 */
	public Text textForColumn(int index, Table table, TableItem item) {

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

				editor.getItem().setText(CACOLUMN, computeTurnOver(item));

			}

		});
		editor.setEditor(newEditor, item, index);
		return newEditor;

	}

	/**
	 * Compute the TurnOver for a given row
	 * 
	 * @param <type>Item</type>item which contains a row data
	 * @return <type>String</type> The turnOver updated
	 */
	private String computeTurnOver(TableItem item) {

		try {

			Double menage = checkStringDouble(item.getText(MENAGECOLUMN));
			Double vitrerie = checkStringDouble(item.getText(VITRERIECOLUMN));
			Double fournitures = checkStringDouble(item.getText(FOURNITURESCOLUMN));
			Double miseABlanc = checkStringDouble(item.getText(MISESBLANCCOLUMN));
			Double autres = checkStringDouble(item.getText(AUTRESCOLUMN));

			Double CA = menage + vitrerie + fournitures + miseABlanc + autres;
			return "" + CA;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	/**
	 * Check if the string value is valid
	 * 
	 * @param <type>String</type>value
	 * @return <type>double</type>value
	 * @throws Exception
	 */
	private double checkStringDouble(String value) throws Exception {
		if (Objects.isNull(value))
			return 0.0;
		try {
			value = value.replace(",",".");
			return Double.parseDouble(value);
		} catch (Exception exception) {
			throw new Exception("Merci de saisir une valeur correcte.");
		}

	}

	/**
	 * update the selection composite
	 */
	private void selection() {
		if (!Objects.isNull(this.selection) && !this.selection.isDisposed()) {
			this.selection.dispose();
			turnOverView.layout(true, true);
		}

		this.selection = new Composite(turnOverView, SWT.NONE);
		RowLayout rowLayout = new RowLayout(SWT.VERTICAL);
		this.selection.setLayout(rowLayout);
		this.selection.setBackground(MyColor.bleuClair);
		this.filterComposite = new Composite(this.selection, SWT.NONE);
		this.filterComposite.setLayout(new RowLayout(SWT.HORIZONTAL));
		this.filterComposite.setBackground(MyColor.bleuClair);
		this.buttons = new Composite(this.selection, SWT.NONE);
		this.buttons.setLayout(new RowLayout(SWT.HORIZONTAL));
		this.buttons.setBackground(MyColor.bleuClair);
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
			monthComposite.setBackground(MyColor.bleuClair);
			monthLabel.setBackground(MyColor.bleuClair);
			String[] frenchMonth = { "Janvier", "Février", "Mars", "Avril", "Mai", "Juin", "Juillet", "Août",
					"Septembre", "Octobre", "Novembre", "Décembre" };

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
			yearComposite.setBackground(MyColor.bleuClair);
			yearLabel.setBackground(MyColor.bleuClair);
			yearLabel.setText("Année");
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

	/**
	 * Getter for the attribute turnOverView
	 * 
	 * @return <type>Composite</type> turnOverview
	 */
	public Composite getRecurringCostView() {
		return this.turnOverView;
	}

	/**
	 * Add a header in header composite with title <param>header</param>
	 * 
	 * @param <type>String</type> header
	 */
	public void addHeader(String header) {
		if (!Objects.isNull(this.header) && !this.header.isDisposed())
			this.header.dispose();
		this.header = new Composite(this.turnOverView, SWT.CENTER | SWT.BORDER);
		this.header.setBackground(MyColor.bleuFonce);
		FillLayout layout = new FillLayout();
		layout.marginWidth = 258;
		this.header.setLayout(layout);

		Label HeadLabel = new Label(this.header, SWT.TITLE);

		HeadLabel.setText("\n" + header + "\n \n");
		Font fontTitle = new Font(HeadLabel.getDisplay(), "Arial", 12, SWT.BOLD);
		HeadLabel.setForeground(MyColor.bleuClair);
		HeadLabel.setFont(fontTitle);
		HeadLabel.setBackground(MyColor.bleuFonce);
		this.header.pack();
		HeadLabel.pack();

	}
}
