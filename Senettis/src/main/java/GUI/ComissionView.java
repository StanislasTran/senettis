package GUI;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.util.ArrayList;
import java.util.Objects;
import org.eclipse.swt.*;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import classes.Comission;
import classes.Site;
import classes.Status;
import org.eclipse.swt.graphics.*;

/**
 * View to manage comission into the system
 *
 */


public class ComissionView {
	private Composite mainView;
	private Composite selection;
	private Composite comissionView;
	private Button boutonCreer;
	private Button buttonRemove;
	private Composite header;

	
	
	/**
	 * Constructor
	 * 
	 * @param composite <type>Composite</type>
	 * @throws SQLException
	 */
	public ComissionView(Composite composite) throws SQLException {
		this.comissionView = new Composite(composite, SWT.BORDER);
		MyColor.setDisplay(composite.getDisplay());
		comissionView.setBackground(MyColor.bleuClair);

		RowLayout rowLayoutV = new RowLayout(SWT.VERTICAL);
		comissionView.setLayout(rowLayoutV);
		addHeader("Gestion des comissions", 127);
		compositeSelection();

		addCreateButton();
		mainView();
		comissionViewDisplay();

	}

	
	/**
	 * create the mainView Composite
	 */
	private void mainView() {
		if (!Objects.isNull(mainView) && !mainView.isDisposed()) {
			mainView.dispose();
			this.comissionView.layout(true, true);
		}
		this.mainView = new Composite(this.comissionView, SWT.BORDER);
		this.comissionView.layout(true, true);
		if (!Objects.isNull(selection) && !selection.isDisposed())
			mainView.moveBelow(selection);

		mainView.pack();
		comissionView.pack();

	}

	/**
	 * 
	 * Add a table which contains all Product from the database in the mainView
	 * entered in parameter
	 * 
	 * @param <type>Composite </type>
	 * @throws SQLException
	 */
	public void comissionViewDisplay() throws SQLException {

		ResultSet result = Comission.getComissionsResultSet();

		this.mainView();
		RowLayout rowLayoutV = new RowLayout();
		rowLayoutV.type = SWT.VERTICAL;

		final Table table = new Table(mainView, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL | SWT.FULL_SELECTION);

		this.mainView.setLayout(new RowLayout());
		table.setSize(200, 50);
		table.setLayoutData(new RowData(410, 300));

		table.setLinesVisible(true);
		table.setHeaderVisible(true);

		java.util.List<Integer> idList = new ArrayList<Integer>();
		table.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {

				if (table.getSelectionIndex() != -1) {
					int comissionId = idList.get(table.getSelectionIndex());
					cleanRemoveButton();
					addButtonRemove(comissionId);
				} else {
					System.out.println("erreur");
				}
			}

		});

		String[] titles = { "ChantierId", "Nom du chantier", "Date de Début", "Comission" };

		for (String title : titles) {
			TableColumn column = new TableColumn(table, SWT.NONE);
			column.setText(title);
		}

		final TableColumn[] columns = table.getColumns();

		while (result.next()) {
			idList.add(result.getInt("comissionId"));
			TableItem item = new TableItem(table, SWT.NONE);
			item.setText(0, Integer.toString(result.getInt("ChantierId")));
			if (!Objects.isNull(result.getString("Nom")))
				item.setText(1, result.getString("Nom"));
			else
				item.setText(1, "");

			item.setText(2, +result.getInt("MoisDebut") + " - " + result.getInt("AnneeDebut"));

			item.setText(3, "" + result.getDouble("Comission"));

		}

		for (TableColumn col : columns)
			col.pack();
		table.pack();
		mainView.pack();
		comissionView.pack();
		comissionView.getParent().pack();

	}

	/**
	 * add a <type>Composite </type> composite to create a Product in the database
	 * 
	 * @param composite
	 * @throws SQLException
	 */
	public void createComission() throws SQLException {
		addHeader("Creation Comission", 150);
		this.comissionView.setLayout(new RowLayout(SWT.VERTICAL));

		if (!Objects.isNull(mainView) && !mainView.isDisposed()) {
			mainView.dispose();
			comissionView.layout(true, true);

		}
		mainView = new Composite(this.comissionView, SWT.NONE);
		comissionView.layout(true, true);

		// mainRowLayout.marginWidth = 50;
		GridLayout mainLayout = new GridLayout();
		mainView.setLayout(mainLayout);
		mainView.setBackground(MyColor.bleuClair);
		comissionView.setBackground(MyColor.bleuClair);

		comissionView.layout(true, true);

		// Month part

		Composite monthComposite = new Composite(mainView, SWT.NONE);
		monthComposite.setLayout(new RowLayout(SWT.HORIZONTAL));
		monthComposite.setBackground(MyColor.bleuClair);
		Label monthLabel = new Label(monthComposite, SWT.NONE);
		monthLabel.setText("Moi début");
		monthLabel.setBackground(MyColor.bleuClair);
		Combo comboMonth = new Combo(monthComposite, SWT.NONE);

		String[] frenchMonth = { "Janvier", "Février", "Mars", "Avril", "Mai", "Juin", "Juillet", "Août", "Septembre",
				"Octobre", "Novembre", "Décembre" };

		for (String m : frenchMonth)
			comboMonth.add(m);
		int currentMonth = LocalDate.now().getMonthValue();

		comboMonth.select(currentMonth - 1);

		// Year part
		Composite yearComposite = new Composite(mainView, SWT.NONE);
		yearComposite.setBackground(MyColor.bleuClair);
		yearComposite.setLayout(new RowLayout(SWT.HORIZONTAL));
		Label yearLabel = new Label(yearComposite, SWT.NONE);
		yearLabel.setBackground(MyColor.bleuClair);
		yearLabel.setText("Année début");
		Combo comboYear = new Combo(yearComposite, SWT.NONE);

		int currentYear = Year.now().getValue();
		for (int i = currentYear - 2; i < currentYear + 3; i++)
			comboYear.add("" + i);
		comboYear.select(2);

		// Comission part

		Composite compositeComission = new Composite(mainView, SWT.BACKGROUND);
		compositeComission.setLayout(new RowLayout());
		compositeComission.setBackground(MyColor.bleuClair);
		Label labelComission = new Label(compositeComission, SWT.NONE);
		labelComission.setBackground(MyColor.bleuClair);
		labelComission.setText("Comission");
		labelComission.setBounds(10, 10, 20, 25);
		final Text textComission = new Text(compositeComission, SWT.BORDER);
		textComission.setText("");
		textComission.setBounds(10, 30, 30, 25);

		/// SITE PART
		Label siteLabel = new Label(mainView, SWT.NONE);
		siteLabel.setText("Selectionnez un chantier : ");
		siteLabel.setBackground(MyColor.bleuClair);
		List sites = new List(mainView, SWT.MULTI | SWT.BORDER | SWT.V_SCROLL);

		ArrayList<Site> allSite = (ArrayList<Site>) Site.getAllSite();

		for (Site s : allSite)
			sites.add(s.getName());

		GridData gridData = new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.VERTICAL_ALIGN_FILL);

		gridData.verticalSpan = 4;

		int listHeight = sites.getItemHeight() * 12;

		Rectangle trim = sites.computeTrim(0, 0, 0, listHeight);

		gridData.heightHint = trim.height;

		sites.setLayoutData(gridData);

		// Validation button

		Composite compositeButtons = new Composite(mainView, SWT.NONE);
		compositeButtons.setBackground(MyColor.bleuClair);
		compositeButtons.setLayout(new RowLayout());
		Button validationButton = new Button(compositeButtons, SWT.BACKGROUND);
		validationButton.setText("Valider");
		validationButton.setBounds(10, 60, 100, 25);
		validationButton.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent arg0) {

				String comission = textComission.getText().replace(",", ".");
				String siteId = "";
				try {

					siteId = "" + Site.getSiteIdByName(sites.getSelection()[0]);
				} catch (SQLException e) {
					MessageBox dialog = new MessageBox(comissionView.getShell(), SWT.ICON_ERROR | SWT.OK);
					dialog.setText("Erreur Création :");
					dialog.setMessage("Une erreur est survenue lors de la création de la comission. " + '\n'
							+ e.getMessage() + "erreuto de base de donnée, contactez l'admin");
					dialog.open();

				} catch (ArrayIndexOutOfBoundsException exceptionArray) {

				}
				boolean isChecked = false;
				try {
					isChecked = checkProduct(siteId, comission);
				} catch (IllegalArgumentException argException) {

					MessageBox dialog = new MessageBox(comissionView.getShell(), SWT.ICON_ERROR | SWT.OK);
					dialog.setText("Erreur Création :");
					dialog.setMessage("Une erreur est survenue lors de la création de la comission. " + '\n'
							+ argException.getMessage());
					dialog.open();
				}

				if (isChecked) {

					Double comissionCheck = Double.parseDouble(comission);
					Month startMonth = Month.of(comboMonth.getSelectionIndex() + 1);
					Year startYear = Year.of(Integer.parseInt(comboYear.getText()));
					Integer siteIdChecked = Integer.parseInt(siteId);
					if (isChecked) {

						try {
							new Comission(comissionCheck, siteIdChecked, startMonth, startYear, Status.PUBLISHED)
									.insert();

							// validationMessageBox

							MessageBox dialog = new MessageBox(comissionView.getShell(), SWT.ICON_WORKING | SWT.OK);
							dialog.setText("Succes");
							dialog.setMessage("La comission  a bien été enregistrée");
							dialog.open();

							// view change

							addHeader("Gestion des comissions", 127);
							compositeSelection();
							addCreateButton();
							comissionViewDisplay();

						} catch (SQLException sqlException) {
							MessageBox dialog = new MessageBox(comissionView.getShell(), SWT.ICON_ERROR | SWT.OK);
							dialog.setText("Erreur Création :");
							dialog.setMessage(
									"Une erreur est survenue lors de l'insertion du produit dans la base de données. "
											+ '\n' + sqlException.getMessage());
							dialog.open();

						}

						compositeComission.dispose();

						compositeButtons.dispose();
						mainView.pack();
						mainView.getParent().pack();
						selection.pack();

					}
				}

			}

		});

		Button buttonCancel = new Button(compositeButtons, SWT.BACKGROUND);
		buttonCancel.setText("Annuler");
		buttonCancel.setBounds(10, 60, 100, 25);
		buttonCancel.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent arg0) {

				try {
					addHeader("Gestion des comissions", 127);
					compositeSelection();
					addCreateButton();
					comissionViewDisplay();
				} catch (SQLException e) {
					MessageBox msgBox = new MessageBox(comissionView.getShell(), SWT.ERROR);
					msgBox.setMessage("Erreur Base de donnée");
					msgBox.setText("erreur de liée à la base de données : \n" + e.getMessage());
					msgBox.open();
				}

				compositeComission.dispose();

				compositeButtons.dispose();
				mainView.pack();
				mainView.getParent().pack();

			}

		});

		if (!Objects.isNull(selection) && !selection.isDisposed()) {
			mainView.moveBelow(selection);
		}

		labelComission.pack();
		validationButton.pack();

		mainView.pack();

		// tableComposite.pack();

		validationButton.pack();
		buttonCancel.pack();
		compositeButtons.pack();
		comissionView.pack();
		comissionView.getParent().pack();

	}

	public void setVueProduit(Composite composite) {
		this.comissionView = composite;
	}

	/********************************************
	 * 
	 * 
	 * Selection Management
	 * 
	 * 
	 ********************************************/

	/**
	 * Create the selection area which is used to display a header title or buttons
	 * to interact The selection area is displayed in the composite entered in
	 * parameter
	 * 
	 * @param <type> Composite </type> composite
	 */
	public void compositeSelection() {

		if (!Objects.isNull(selection) && !selection.isDisposed()) {
			this.selection.dispose();
			this.comissionView.layout(true, true);

		}
		this.selection = new Composite(comissionView, SWT.NONE);
		comissionView.layout(true, true);
		RowLayout rowLayout = new RowLayout();
		rowLayout.marginWidth = 20;
		this.selection.setLayout(rowLayout);
		this.selection.setBackground(MyColor.bleuClair);

		selection.pack();
		comissionView.pack();
		this.comissionView.getParent().pack();

	}

	/**
	 * Add the button "Créer" which enables to access the creation form
	 * 
	 * @param composite
	 */
	private void addCreateButton() {
		if (!Objects.isNull(boutonCreer) && !boutonCreer.isDisposed()) {
			this.boutonCreer.dispose();
			this.selection.layout(true, true);

		}
		this.boutonCreer = new Button(this.selection, SWT.CENTER);
		this.selection.layout(true, true);
		boutonCreer.setText("Créer");

		boutonCreer.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {

				selection.dispose();
				mainView.dispose();
				comissionView.layout(true, true);

				try {
					createComission();
				} catch (SQLException e) {
					MessageBox msgBox = new MessageBox(comissionView.getShell(), SWT.ERROR);
					msgBox.setMessage("Erreur Base de donnée");
					msgBox.setText("erreur de liée à la base de données : \n" + e.getMessage());
					msgBox.open();
					e.printStackTrace();
				}

			}
		});

		this.boutonCreer.pack();
		this.selection.pack();
		this.comissionView.getParent().pack();
		this.comissionView.pack();

	}

	/**
	 * Add a header in Selection composite with title <param>header</param>
	 * 
	 * @param <type>String</type> header
	 */
	public void addHeader(String header, int size) {
		if (!Objects.isNull(this.header) && !this.header.isDisposed())
			this.header.dispose();
		this.header = new Composite(this.comissionView, SWT.CENTER | SWT.BORDER);
		this.header.setBackground(MyColor.bleuFonce);
		FillLayout layout = new FillLayout();
		layout.marginWidth = size;
		this.header.setLayout(layout);

		Label HeadLabel = new Label(this.header, SWT.TITLE);

		HeadLabel.setText("\n" + header + "\n\n");
		Font fontTitle = new Font(HeadLabel.getDisplay(), "Arial", 12, SWT.BOLD);
		HeadLabel.setForeground(MyColor.bleuClair);
		HeadLabel.setFont(fontTitle);
		HeadLabel.setBackground(MyColor.bleuFonce);
		this.header.pack();
		HeadLabel.pack();

	}

	/**
	 * this function is used to optimized the refresh of Selection composite
	 */
	public void cleanRemoveButton() {

		if (!Objects.isNull(this.buttonRemove) && !buttonRemove.isDisposed()) {

			this.buttonRemove.dispose();// dispose the actual remove button
			selection.layout(true, true);
		}
	}

	/**
	 * 
	 * @param comissionId
	 */
	public void addButtonRemove(int comissionId) {
		if (!Objects.isNull(this.buttonRemove) && !buttonRemove.isDisposed()) {

			this.buttonRemove.dispose();// dispose the actual remove button
			selection.layout(true, true);
		}
		this.buttonRemove = new Button(this.selection, SWT.PUSH);
		this.buttonRemove.setText("Supprimer");
		this.selection.layout(true, true);

		this.buttonRemove.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {

				try {
					Comission.removeById(comissionId);
					compositeSelection();
					addCreateButton();
					comissionViewDisplay();

				} catch (SQLException e) {
					MessageBox msgBox = new MessageBox(comissionView.getShell(), SWT.ERROR);
					msgBox.setMessage("Erreur Base de donnée");
					msgBox.setText("erreur de liée à la base de données : \n" + e.getMessage());
					msgBox.open();
					e.printStackTrace();
					e.printStackTrace();
				}

			}
		});

		buttonRemove.pack();
		selection.pack();
	}

	/*******************
	 * 
	 * Check functions
	 * 
	 *****************/

	public boolean checkProduct(String siteId, String comission) {

		if (Objects.isNull(siteId))
			throw new IllegalArgumentException("Veuillez selectionner un chantier");
		else if (siteId.isBlank())
			throw new IllegalArgumentException("Veuillez selectionner un chantier\"");

		try {
			Double comissionCheck = Double.parseDouble(comission);
			if (comissionCheck <= 0)
				throw new IllegalArgumentException("Le  prix doit être supérieur à 0");
		} catch (NumberFormatException parseDoubleException) {
			throw new IllegalArgumentException(
					"La comission entrée n'est pas valide, veuillez entrer une valeur numérique");
		}

		if (Objects.isNull(comission))
			throw new IllegalArgumentException("L'attribut comission ne peut pas être null");
		else if (comission.isBlank())
			throw new IllegalArgumentException("L'attribut comission ne peut pas être null");

		try {
			Double comissionCheck = Double.parseDouble(comission);
			if (comissionCheck <= 0 || comissionCheck > 100)
				throw new IllegalArgumentException("Le  prix doit être comprise entre  0 et 100");
		} catch (NumberFormatException parseDoubleException) {
			throw new IllegalArgumentException(
					"La comission entrée n'est pas valide, veuillez entrer une valeur numérique");
		}

		return true;
	}

	/*****
	 * 
	 * Getter
	 * 
	 * 
	 */

	/**
	 * getter for vueProduit
	 * 
	 * @return <type> Composite</type> vueProduit
	 */
	public Composite getVueProduit() {
		return this.comissionView;
	}

}
