
package GUI;

import java.sql.SQLException;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.time.YearMonth;
import java.util.Objects;
import org.eclipse.swt.*;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import classes.Rentability;

/**
 * View to display the rentability of sites for given month and year
 *
 *
 */

public class RentabilityView {

	private Composite rentabilityView;
	private Composite selection;
	private Composite view;
	private Table rentabilityTable;
	private Composite header;

	/***
	 * 
	 * Constructor for the view Rentabolity
	 * 
	 * @param <type>Composite</type>composite not null, the parent composite
	 * @param <type>Display</type>display     not null, the parent display
	 * @throws SQLException
	 */
	public RentabilityView(Composite composite, Display display) throws SQLException {

		MyColor.setDisplay(display);

		rentabilityView = new Composite(composite, SWT.NONE);
		rentabilityView.setLayout(new RowLayout(SWT.VERTICAL));
		rentabilityView.setBackground(MyColor.bleuClair);
		addHeader("Analyse de la rentabilité");
		getSelection();

		LocalDate currentdate = LocalDate.now();
		Month month = currentdate.getMonth();
		int year = currentdate.getYear();
		getVue(month.toString() + " " + year);

		rentabilityView.pack();
	}

	/**
	 * Create the Selection composite
	 */
	public void getSelection() {
		if (!Objects.isNull(selection) && !selection.isDisposed()) {
			selection.dispose();
		}

		selection = new Composite(rentabilityView, SWT.NONE);

		RowLayout rowLayout = new RowLayout(SWT.VERTICAL);
		rowLayout.spacing = 15;
		selection.setLayout(rowLayout);
		selection.setBackground(MyColor.bleuClair);

		Composite selec1 = new Composite(selection, SWT.NONE);
		selec1.setLayout(new RowLayout(SWT.HORIZONTAL));
		selec1.setBackground(MyColor.bleuClair);

		Composite selec2 = new Composite(selection, SWT.NONE);
		selec2.setLayout(new RowLayout(SWT.HORIZONTAL));
		selec2.setBackground(MyColor.bleuClair);
		Label labelPeriode = new Label(selec2, SWT.NONE);
		labelPeriode.setText("Période : ");
		labelPeriode.setBackground(MyColor.bleuClair);
		Combo periode = new Combo(selec2, SWT.BORDER);
		LocalDate currentdate = LocalDate.now();
		Month month = currentdate.getMonth();
		int year = currentdate.getYear();
		periode.setText(month.toString() + " " + year);

		for (int i = 2020; i <= year + 1; i++) {
			for (int j = 1; j <= 12; j++) {
				periode.add(Month.of(j) + " " + i);
			}
		}

		periode.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				try {
					updateTable(periode.getText());
				} catch (SQLException e) {
					MessageBox msgBox = new MessageBox(rentabilityView.getShell(), SWT.ERROR);
					msgBox.setMessage("Erreur Base de donnée");
					msgBox.setText("erreur de liée à la base de données : \n" + e.getMessage());
					msgBox.open();
					e.printStackTrace();
				}
				view.pack();
			}
		});

		selec2.pack();

	}

	/**
	 * Create the composite Which contains the display of all rentabilities data by
	 * site
	 * 
	 * @param <type>String </type>periode format: "MONTH YEAR"
	 * @throws SQLException
	 */
	public void getVue(String periode) throws SQLException {
		if (!Objects.isNull(view) && !view.isDisposed()) {
			view.dispose();
		}
		RowLayout rowLayoutV = new RowLayout();
		rowLayoutV.type = SWT.VERTICAL;

		view = new Composite(rentabilityView, SWT.NONE);
		view.setLayout(rowLayoutV);
		view.setBackground(MyColor.gris);

		// table cration
		rentabilityTable = new Table(view, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL | SWT.FULL_SELECTION);
		rentabilityTable.setLayoutData(new RowData(888, 390));
		rentabilityTable.setLinesVisible(true);
		rentabilityTable.setHeaderVisible(true);

		// Initialize the table headers
		String[] titles = { "Chantier", "Chifffre d'affaire", "Total Couts Employés", "Livraisons", "Matériels",
				"Fournitures Sanitaires", "Comissions", "Coût de revient", "Marge Brut", "Pourcentage" };
		for (String title : titles) {
			TableColumn column = new TableColumn(rentabilityTable, SWT.NONE);
			column.setText(title);
		}

		// on remplit la table
		final TableColumn[] columns = rentabilityTable.getColumns();

		updateTable(periode);

		// on pack les colonnes
		for (TableColumn col : columns)
			col.pack();

		view.pack();
	}

	/**
	 * update the display rentabilities table for a given periode
	 * 
	 * @param <type>String </type>periode format: "MONTH YEAR"
	 * @throws SQLException
	 */
	public void updateTable(String periode) throws SQLException {
		rentabilityTable.removeAll();

		String[] d2 = periode.split(" ");
		YearMonth date1 = YearMonth.of(Integer.parseInt(d2[1]), Month.valueOf(d2[0]).getValue());
		Month monthFilter = date1.getMonth();
		Year yearFilter = Year.of(date1.getYear());

		java.util.List<Rentability> rentabilityList = Rentability.getRentabilityByDate(monthFilter, yearFilter);

		for (Rentability r : rentabilityList) {

			TableItem item = new TableItem(rentabilityTable, SWT.NONE);

			item.setText(0, r.getName());

			item.setText(1, String.format("%.2f", r.getTurnOver()));

			double totalEmployeCost = r.getEmployeeCost();
			DecimalFormat df = new DecimalFormat("0.00");
			item.setText(2, df.format(totalEmployeCost));

			// delivery//

			double total_delivery = r.getDelivery();

			item.setText(3, String.format("%.2f", total_delivery));

			// materiel

			double total_material = r.getMaterial();

			item.setText(4, String.format("%.2f",total_material));

			double total_fs = r.getFSCost();

			item.setText(5, String.format("%.2f",total_fs));

			// Comission

			double comission = r.getComission();

			item.setText(6, String.format("%.2f",comission));

			double priceCost = r.getCostPrice();
			double grossMargin = r.getGrossMargin();
			item.setText(7, String.format("%.2f",priceCost));
			item.setText(8, String.format("%.2f",grossMargin));

			if (totalEmployeCost != 0.0) {
				//System.out.println("hey ehy");
				item.setText(9, df.format(r.getPercent()) + "%");
			}

		}
		
		
		java.util.List<Rentability> totalList = Rentability.getTotalRentabilityByDate(monthFilter, yearFilter);
		if (!totalList.isEmpty()) {
			Rentability r = totalList.get(0);
			
			TableItem total = new TableItem(rentabilityTable, SWT.NONE);
	
			total.setText(0, "Total");
			
			total.setText(1, String.format("%.2f", r.getTurnOver()));
	
			double totalEmployeCost = r.getEmployeeCost();
			DecimalFormat df = new DecimalFormat("0.00");
			total.setText(2, df.format(totalEmployeCost));
	
			// delivery//
	
			double total_delivery = r.getDelivery();
			total.setText(3, String.format("%.2f", total_delivery));
	
			// materiel
	
			double total_material = r.getMaterial();
			total.setText(4, String.format("%.2f",total_material));
	
			double total_fs = r.getFSCost();
			total.setText(5, String.format("%.2f",total_fs));
	
			// Comission
	
			double comission = r.getComission();
			total.setText(6, String.format("%.2f",comission));
	
			double priceCost = r.getCostPrice();
			total.setText(7, String.format("%.2f",priceCost));
			
			double grossMargin = r.getGrossMargin();
			total.setText(8, String.format("%.2f",grossMargin));
	
			if (totalEmployeCost != 0.0) {
				total.setText(9, df.format(r.getPercent()) + "%");
			}
		}
	}

	
	/**
	 * Getter for the global composite of this view
	 * @return <type>Composite</type> rentabilityView
	 */
	public Composite getComposite() {
		return this.rentabilityView;
	}

	/**
	 * Add a header in header composite with title <param>header</param>
	 * 
	 * @param <type>String</type> header
	 */
	public void addHeader(String header) {
		if (!Objects.isNull(this.header) && !this.header.isDisposed())
			this.header.dispose();
		this.header = new Composite(this.rentabilityView, SWT.CENTER | SWT.BORDER);
		this.header.setBackground(MyColor.bleuFonce);
		FillLayout layout = new FillLayout();
		layout.marginWidth = 365;
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
