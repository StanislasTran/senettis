package GUI;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.eclipse.swt.*;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

import classes.Comission;
//import javafx.embed.swt.FXCanvas ;
import classes.Product;
import classes.Site;
import classes.Status;

public class ViewComission {
	private Composite mainView;
	private Composite selection;
	private Composite comissionView;
	private Button boutonCreer;
	private Button buttonRemove;

	/**
	 * Constructor
	 * 
	 * @param composite <type>Composite</type>
	 * @throws SQLException
	 */
	public ViewComission(Composite composite) throws SQLException {
		this.comissionView = new Composite(composite, SWT.BORDER);
		Couleur.setDisplay(composite.getDisplay());
		comissionView.setBackground(Couleur.bleuClair);

		RowLayout rowLayoutV = new RowLayout(SWT.VERTICAL);
		comissionView.setLayout(rowLayoutV);

		compositeSelection();
		addCreateButton();
		mainView();
		comissionViewDisplay();

	}

	private void mainView() {
		if (!Objects.isNull(mainView) && !mainView.isDisposed()) {
			mainView.dispose();
			this.comissionView.layout(true, true);
		}
		this.mainView = new Composite(this.comissionView, SWT.NONE);
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
		table.setLayoutData(new RowData(400, 600));

		table.setLinesVisible(true);
		table.setHeaderVisible(true);

		List<Integer> idList = new ArrayList<Integer>();
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
	 */
	public void createComission() {
		this.comissionView.setLayout(new RowLayout(SWT.VERTICAL));

		if (!Objects.isNull(mainView) && !mainView.isDisposed()) {
			mainView.dispose();
			comissionView.layout(true, true);

		}
		mainView = new Composite(this.comissionView, SWT.CENTER);
		RowLayout mainRowLayout = new RowLayout(SWT.VERTICAL);
		mainRowLayout.marginWidth = 50;
		mainView.setLayout(mainRowLayout);
		mainView.setBackground(Couleur.bleuClair);
		comissionView.setBackground(Couleur.bleuClair);

		comissionView.layout(true, true);

		addHeader("Creation Comission");

		RowLayout rowLayout = new RowLayout();
		rowLayout.marginHeight = 30;
		rowLayout.marginWidth = 20;
		rowLayout.spacing = 5;
		rowLayout.type = SWT.HORIZONTAL;

		// Month part

		Composite monthComposite = new Composite(mainView, SWT.NONE);
		monthComposite.setLayout(new RowLayout(SWT.HORIZONTAL));
		Label monthLabel = new Label(monthComposite, SWT.NONE);
		monthLabel.setText("Moi début");
		Combo comboMonth = new Combo(monthComposite, SWT.NONE);

		String[] frenchMonth = { "Janvier", "Février", "Mars", "Avril", "Mai", "Juin", "Juillet", "Août", "Septembre",
				"Octobre", "Novembre", "Décembre" };

		for (String m : frenchMonth)
			comboMonth.add(m);
		int currentMonth = LocalDate.now().getMonthValue();

		comboMonth.select(currentMonth - 1);

		// Year part
		Composite yearComposite = new Composite(mainView, SWT.NONE);
		yearComposite.setLayout(new RowLayout(SWT.HORIZONTAL));
		Label yearLabel = new Label(yearComposite, SWT.NONE);
		yearLabel.setText("Année début");
		Combo comboYear = new Combo(yearComposite, SWT.NONE);

		int currentYear = Year.now().getValue();
		for (int i = currentYear - 2; i < currentYear + 3; i++)
			comboYear.add("" + i);
		comboYear.select(2);

		// TAble part
		Composite tableComposite = new Composite(mainView, SWT.NONE);
		Table table = VueChantier.getTableAllChantier(tableComposite);

		// Comission part

		Composite compositeComission = new Composite(mainView, SWT.BACKGROUND);
		compositeComission.setLayout(rowLayout);
		compositeComission.setBackground(Couleur.bleuClair);
		Label labelComission = new Label(compositeComission, SWT.NONE);
		labelComission.setBackground(Couleur.bleuClair);
		labelComission.setText("Comission");
		labelComission.setBounds(10, 10, 20, 25);
		final Text textComission = new Text(compositeComission, SWT.BORDER);
		textComission.setText("");
		textComission.setBounds(10, 30, 30, 25);

		// Validation button

		Composite compositeButtons = new Composite(mainView, SWT.CENTER);
		compositeButtons.setBackground(Couleur.bleuClair);
		compositeButtons.setLayout(rowLayout);
		Button validationButton = new Button(compositeButtons, SWT.BACKGROUND);
		validationButton.setText("Valider");
		validationButton.setBounds(10, 60, 100, 25);
		validationButton.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent arg0) {

				String comission = textComission.getText().replace(",", ".");
				String siteId = "";
				try {
					siteId = "" + Site.getSiteIdByName(table.getSelection()[0].getText(0));
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
							dialog.setMessage("La comission  a bien été enregistré");
							dialog.open();

							// view change

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

					compositeSelection();
					addCreateButton();
					comissionViewDisplay();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
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
		selection.pack();
		tableComposite.pack();
		table.pack();
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
		this.selection.setBackground(Couleur.bleuClair);

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

				createComission();

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
	public void addHeader(String header) {
		if (!this.selection.isDisposed())
			this.selection.dispose();
		this.selection = new Composite(this.comissionView, SWT.CENTER | SWT.BORDER);
		this.selection.setBackground(Couleur.bleuFonce);
		FillLayout layout = new FillLayout();
		layout.marginWidth = 100;
		this.selection.setLayout(layout);

		Label HeadLabel = new Label(this.selection, SWT.TITLE);

		HeadLabel.setText(header);
		Font fontTitle = new Font(HeadLabel.getDisplay(), "Arial", 12, SWT.BOLD);
		HeadLabel.setForeground(Couleur.bleuClair);
		HeadLabel.setFont(fontTitle);
		HeadLabel.setBackground(Couleur.bleuFonce);
		this.selection.pack();
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
					// TODO Auto-generated catch block
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
