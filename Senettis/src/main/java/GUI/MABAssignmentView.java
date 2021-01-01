package GUI;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.eclipse.swt.SWT;
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
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

import classes.MABAssignment;
import classes.Employee;
import classes.Site;
import classes.Status;

public class MABAssignmentView {

	private Composite affectationView;
	private Composite selection;
	private Composite mainComposite;
	private Composite rightComposite;
	private Composite leftComposite;
	private Composite rightColumn;
	private Combo monthFilter;
	private Combo yearFilter;
	private Button createButton;
	private Button modifyButton;
	private Composite filterComposite;
	private Composite buttons;
	private Composite header;
	private Button removeButton;

	/**
	 * Constructor
	 * 
	 * @param composite
	 * @throws SQLException
	 */
	public MABAssignmentView(Composite composite) throws SQLException {
		MyColor.setDisplay(composite.getDisplay());
		this.affectationView = new Composite(composite, SWT.NONE);
		this.affectationView.setLayout(new RowLayout(SWT.VERTICAL));
		this.affectationView.setBackground(MyColor.bleuClair);
		this.rightColumn = composite;
		buildHome();
	}

	/**
	 * Build the affectationView home
	 * 
	 * @param composite
	 * @throws SQLException
	 */
	private void buildHome() throws SQLException {

		addHeader("Gestion des mises à blanc");
		// selection part

		selection(this.affectationView);
		createMonthFilter();
		createYearFilter();

		this.mainComposite = new Composite(this.affectationView, SWT.NONE);
		this.mainComposite.setLayout(new RowLayout(SWT.HORIZONTAL));

		this.leftComposite = new Composite(this.mainComposite, SWT.NONE);
		// this.rightComposite = new Composite(this.mainComposite, SWT.NONE);

		// TabFolder creation and fill

		TabFolder tabFolder = new TabFolder(leftComposite, SWT.BORDER);
		tabFolder.setBackground(MyColor.bleuClair);
		mainComposite.setBackground(MyColor.bleuClair);

		tabFolder.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				cleanRightComposite();
				cleanButtons();

			}

		});

		TabItem tabEmploye = new TabItem(tabFolder, SWT.NULL);

		tabEmploye.setText("Affectation par Employé");

		TabItem tabChantier = new TabItem(tabFolder, SWT.NULL);
		tabChantier.setText("Affectation par Chantier");

		this.monthFilter.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				Month month = Month.of(monthFilter.getSelectionIndex() + 1);
				Year year = Year.of(Integer.parseInt(yearFilter.getText()));
				try {

					cleanButtons();
					cleanRightComposite();
					createTableEmployeStats(tabEmploye, month, year);
					createTableChantierStats(tabChantier, month, year);
					createRightComposite();
					// disposeAllChildren(rightComposite);
					mainComposite.layout(true, true);
				} catch (SQLException e1) {
					MessageBox msgBox = new MessageBox(affectationView.getShell(), SWT.ERROR);
					msgBox.setMessage("Erreur Base de donnée");
					msgBox.setText("erreur de liée à la base de données : \n" + e1.getMessage());
					msgBox.open();
					e1.printStackTrace();
				}
			}

		});

		this.yearFilter.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				Month month = Month.of(monthFilter.getSelectionIndex() + 1);
				Year year = Year.of(Integer.parseInt(yearFilter.getText()));
				try {
					cleanButtons();
					cleanRightComposite();
					createTableEmployeStats(tabEmploye, month, year);
					createTableChantierStats(tabChantier, month, year);
					disposeAllChildren(rightComposite);
					mainComposite.layout(true, true);
				} catch (SQLException e1) {
					MessageBox msgBox = new MessageBox(affectationView.getShell(), SWT.ERROR);
					msgBox.setMessage("Erreur Base de donnée");
					msgBox.setText("erreur de liée à la base de données : \n" + e1.getMessage());
					msgBox.open();
					e1.printStackTrace();
				}
			}

		});

		Month month = Month.of(monthFilter.getSelectionIndex() + 1);
		Year year = Year.of(Integer.parseInt(yearFilter.getText()));
		createTableEmployeStats(tabEmploye, month, year);
		createTableChantierStats(tabChantier, month, year);

		mainComposite.pack();
		tabFolder.pack();
		this.selection.pack();
		leftComposite.pack();
		// rightComposite.pack();
		affectationView.pack();
		this.rightColumn.pack();

	}

	/****
	 * 
	 * 
	 * Table with stats for tabItem creation
	 *
	 */

	private void createRightComposite() {

		if (!Objects.isNull(this.rightComposite) && !this.rightComposite.isDisposed()) {

			this.rightComposite.dispose();
			this.mainComposite.layout(true, true);

		}
		this.rightComposite = new Composite(this.mainComposite, SWT.NONE);
		this.mainComposite.layout(true, true);
		this.rightComposite.pack();

	}

	/**
	 * addd a table which contain stats for all Employee in a <param>
	 * <type>TabItem</type> tabEmploye</param>
	 * 
	 * @param tabEmploye
	 * @throws SQLException
	 */
	private void createTableEmployeStats(TabItem tabEmploye, Month month, Year year) throws SQLException {

		final Table table = new Table(tabEmploye.getParent(),
				SWT.BORDER | SWT.MULTI | SWT.V_SCROLL | SWT.FULL_SELECTION);
		table.setLayoutData(new RowData(600, 800));

		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		String[] titles = { "EmployeId", "Nom", "Prenom", "Nombre de chantier différents", "Nombre d'heures total" };

		for (String title : titles) {
			TableColumn column = new TableColumn(table, SWT.NONE);
			column.setText(title);
		}

		ResultSet result = MABAssignment.getEmployeStats(month, year);
		final TableColumn[] columns = table.getColumns();
		while (result.next()) {
			TableItem item = new TableItem(table, SWT.NONE);
			item.setText(0, Integer.toString(result.getInt("EmployeId")));
			item.setText(1, result.getString("Nom"));
			item.setText(2, result.getString("Prenom"));

			if (Objects.isNull(result.getString("nb_chantier")))
				item.setText(3, result.getString("nb_heure"));
			else
				item.setText(3, result.getString("nb_chantier"));

			if (Objects.isNull(result.getString("nb_heure")))
				item.setText(4, "0");
			else
				item.setText(4, result.getString("nb_heure"));

		}

		table.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				if (table.getSelectionIndex() != -1) {

					int employeId = Integer.parseInt(table.getSelection()[0].getText());

					try {
						if (!Objects.isNull(modifyButton) && !modifyButton.isDisposed()) {
							modifyButton.dispose();
						}

						addAjouterForEmployee(employeId);
						EmployeAffectationDisplay(employeId);

					} catch (SQLException e1) {
						MessageBox msgBox = new MessageBox(affectationView.getShell(), SWT.ERROR);
						msgBox.setMessage("Erreur Base de donnée");
						msgBox.setText("erreur de liée à la base de données : \n" + e1.getMessage());
						msgBox.open();
						e1.printStackTrace();
					}
				} else {
					System.out.println("erreur");
				}

			}

		});

		for (TableColumn col : columns)
			col.pack();

		tabEmploye.setControl(table);
		table.pack();
	}

	/**
	 * Créer la table qui contient la liste des chantiers et les stats associées à
	 * ces chantiers et l'ajoute à un TableItem
	 * 
	 * @param <type> TableItem </type> onglet de type Table Item qui contient la
	 *               liste des Chantiers
	 *
	 * @throws SQLException
	 */
	private void createTableChantierStats(TabItem tabChantier, Month month, Year year) throws SQLException {

		final Table table = new Table(tabChantier.getParent(),
				SWT.BORDER | SWT.MULTI | SWT.V_SCROLL | SWT.FULL_SELECTION);
		table.setLayoutData(new RowData(900, 800));

		table.setLinesVisible(true);
		table.setHeaderVisible(true);

		String[] titles = { "ChantierId", "Nom", "Nombre de chantiers différents", "Nombre d'heures total" };

		for (String title : titles) {
			TableColumn column = new TableColumn(table, SWT.NONE);
			column.setText(title);
		}

		ResultSet result = MABAssignment.getChantierStats(month, year);
		final TableColumn[] columns = table.getColumns();
		while (result.next()) {
			TableItem item = new TableItem(table, SWT.NONE);
			item.setText(0, "" + result.getInt("ChantierId"));
			item.setText(1, result.getString("Nom"));

			item.setText(3, result.getString("nb_Employe"));
			if (Objects.isNull(result.getString("nb_heure")))
				item.setText(4, "" + "Inconnu");
			else
				item.setText(4, result.getString("nb_heure"));

		}

		for (TableColumn col : columns)
			col.pack();

		tabChantier.setControl(table);

		table.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				if (table.getSelectionIndex() != -1) {

					int siteId = Integer.parseInt(table.getSelection()[0].getText());

					addButtonAjouterForSite(siteId);

					try {
						siteAffectationDisplay(siteId);
					} catch (SQLException e1) {
						MessageBox msgBox = new MessageBox(affectationView.getShell(), SWT.ERROR);
						msgBox.setMessage("Erreur Base de donnée");
						msgBox.setText("erreur de liée à la base de données : \n" + e1.getMessage());
						msgBox.open();
						e1.printStackTrace();
					}
				} else {
					System.out.println("erreur");
				}

			}

		});
		table.pack();

	}

	/***
	 * 
	 * 
	 * Right Composite management
	 * 
	 * 
	 */

	/**
	 * ADD the list of Site affected to an Employee selected by its employeeId
	 * 
	 * @param employeId <type>int</type>
	 * 
	 * @throws SQLException
	 */
	public void EmployeAffectationDisplay(int employeId) throws SQLException {

		this.cleanRightComposite();
		Month month = Month.of(this.monthFilter.getSelectionIndex() + 1);
		Year year = Year.of(Integer.parseInt(this.yearFilter.getText()));
		ResultSet result = MABAssignment.getEmployeAffectationPublished(employeId, month, year);
		createRightComposite();
		final Table table = new Table(this.rightComposite, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL | SWT.FULL_SELECTION);
		table.setLayoutData(new RowData(900, 800));

		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		String[] titles = { "ChantierId", "Nom", "Adresse", "Nombre d'heure" };

		for (String title : titles) {
			TableColumn column = new TableColumn(table, SWT.NONE);
			column.setText(title);
		}

		final TableColumn[] columns = table.getColumns();
		List<Integer> affectationsId = new ArrayList<Integer>();
		while (result.next()) {

			TableItem item = new TableItem(table, SWT.NONE);
			item.setText(0, "" + result.getInt("ChantierId"));
			item.setText(1, result.getString("Nom"));
			if (Objects.isNull(result.getString("Adresse")))
				item.setText(2, "inconnue");
			else
				item.setText(2, result.getString("Adresse"));

			if (Objects.isNull(result.getString("Nombre_heures")))
				item.setText(3, "Inconnu");
			else
				item.setText(3, result.getString("Nombre_heures"));
			affectationsId.add(result.getInt("AffectationId"));

		}

		table.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {

				AddModifButtonEmployee(affectationsId.get(table.getSelectionIndex()));
				addRemoveForEmployee(affectationsId.get(table.getSelectionIndex()), employeId);

			}

			public void widgetDefaultSelected(SelectionEvent e) {
			}

		});
		for (TableColumn col : columns)
			col.pack();

		table.pack();

		this.affectationView.pack();
		this.rightComposite.pack();
		this.rightColumn.pack();

	}

	/**
	 * display in a <type>table</type> the list of employe affected to a siteId
	 * 
	 * @param siteId
	 * @throws SQLException
	 */
	public void siteAffectationDisplay(int siteId) throws SQLException {
		cleanRightComposite();
		Month month = Month.of(this.monthFilter.getSelectionIndex() + 1);
		Year year = Year.of(Integer.parseInt(this.yearFilter.getText()));
		ResultSet result = MABAssignment.getSiteAffectationPublished(siteId, month, year);
		createRightComposite();
		disposeAllChildren(this.rightComposite);

		final Table table = new Table(this.rightComposite, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL | SWT.FULL_SELECTION);
		table.setLayoutData(new RowData(900, 800));

		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		String[] titles = { "Nom", "Prenom", "Nombre d'heure", "Matricule" };

		for (String title : titles) {
			TableColumn column = new TableColumn(table, SWT.NONE);
			column.setText(title);
		}

		final TableColumn[] columns = table.getColumns();
		List<Integer> affectationsId = new ArrayList<Integer>();
		while (result.next()) {

			TableItem item = new TableItem(table, SWT.NONE);

			item.setText(0, result.getString("Nom"));
			item.setText(1, result.getString("Prenom"));
			if (Objects.isNull(result.getString("Nombre_heures")))
				item.setText(2, "Inconnu");
			else
				item.setText(2, result.getString("Nombre_heures"));

			affectationsId.add(result.getInt("AffectationId"));
			item.setText(3, result.getString("Numero_matricule"));
		}

		table.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {

				AddModifButtonSite(affectationsId.get(table.getSelectionIndex()), siteId);
				addRemoveForSite(affectationsId.get(table.getSelectionIndex()), siteId);

			}

			public void widgetDefaultSelected(SelectionEvent e) {
			}

		});
		for (TableColumn col : columns)
			col.pack();

		table.pack();

		this.affectationView.pack();
		this.rightComposite.pack();
		this.rightColumn.pack();

	}

	/***
	 * 
	 * 
	 * GETTERS AND SETTERS
	 * 
	 */

	/**
	 * Getter for vueAffectation composite
	 * 
	 * @return <type> Composite </type>
	 */
	public Composite getVueAffectation() {

		return this.affectationView;
	}

	/**
	 * Getter for vueAffectation composite
	 * 
	 * @return <type> Composite </type>
	 */
	public Composite getRightComposite() {

		return this.rightComposite;
	}

	/**
	 * Applique la fonction dispose sur tous les composite fils d'un composite
	 * 
	 * @param <type>Composite</type> composite possedant les composites enfant à
	 *                               dispose
	 */
	public void disposeAllChildren(Composite composite) {
		for (Control control : composite.getChildren()) {
			control.dispose();
		}
	}

	/***
	 * 
	 * Selection composite management
	 * 
	 */

	/**
	 * initialize the composite selection in the <param> composite</param>
	 * 
	 * @param composite
	 */
	public void selection(Composite composite) {
		this.selection = new Composite(composite, SWT.NONE);
		this.selection.setBackground(MyColor.bleuClair);
		RowLayout rowLayout = new RowLayout(SWT.VERTICAL);
		this.selection.setLayout(rowLayout);

		this.filterComposite = new Composite(this.selection, SWT.NONE);
		this.filterComposite.setLayout(new RowLayout(SWT.HORIZONTAL));
		this.filterComposite.setBackground(MyColor.bleuClair);

		this.buttons = new Composite(this.selection, SWT.NONE);
		this.buttons.setBackground(MyColor.bleuClair);
		this.buttons.setLayout(new RowLayout(SWT.HORIZONTAL));

		this.selection.pack();
		this.filterComposite.pack();
		this.buttons.pack();
		composite.pack();
	}

	/**
	 * Add "Modifier" when the user click on an employee
	 * 
	 * @param employeId
	 */
	public void AddModifButtonEmployee(int affectationId) {
		if (!Objects.isNull(this.modifyButton) && !this.modifyButton.isDisposed()) {
			this.modifyButton.dispose();
		}

		this.modifyButton = new Button(this.buttons, SWT.CENTER);
		this.buttons.layout(true, true);
		this.modifyButton.setText("Modifier Affectation");

		this.modifyButton.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				try {
					modifyEmployeeAffectation(affectationId);
				} catch (SQLException e1) {
					MessageBox msgBox = new MessageBox(affectationView.getShell(), SWT.ERROR);
					msgBox.setMessage("Erreur Base de donnée");
					msgBox.setText("erreur de liée à la base de données : \n" + e1.getMessage());
					msgBox.open();
					e1.printStackTrace();
				}
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});

		this.modifyButton.moveBelow(this.createButton);
		this.selection.moveAbove(this.mainComposite);
		this.modifyButton.pack();
		this.selection.pack();

	}

	/**
	 * Add "Modifier" when the user click on a site
	 * 
	 * @param employeId
	 */
	public void AddModifButtonSite(int affectationId, int siteId) {
		if (!Objects.isNull(modifyButton) && !modifyButton.isDisposed()) {
			modifyButton.dispose();
			buttons.layout(true, true);

		}
		this.modifyButton = new Button(this.buttons, SWT.CENTER);
		buttons.layout(true, true);
		this.modifyButton.setText("Modifier Affectation");

		this.modifyButton.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				try {
					modifySiteAffectation(affectationId);
				} catch (SQLException e1) {
					MessageBox msgBox = new MessageBox(affectationView.getShell(), SWT.ERROR);
					msgBox.setMessage("Erreur Base de donnée");
					msgBox.setText("erreur de liée à la base de données : \n" + e1.getMessage());
					msgBox.open();
					e1.printStackTrace();
				}
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});

		this.modifyButton.pack();
		this.selection.pack();

	}

	/**
	 * 
	 * add the button "Ajouter" for site
	 * 
	 * @param chantierId
	 */
	private void addButtonAjouterForSite(int chantierId) {

		if (!Objects.isNull(this.createButton) && !this.createButton.isDisposed()) {
			createButton.dispose();
			buttons.layout(true, true);
		}
		this.createButton = new Button(this.buttons, SWT.CENTER);
		buttons.layout(true, true);

		this.createButton.setText("Ajouter Affectation");

		if (!Objects.isNull(this.modifyButton) && !this.modifyButton.isDisposed()) {
			this.createButton.moveAbove(this.modifyButton);
			buttons.layout(true, true);
		}

		this.createButton.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				try {
					ajouterChantierAffectation(chantierId);
				} catch (SQLException e1) {
					MessageBox msgBox = new MessageBox(affectationView.getShell(), SWT.ERROR);
					msgBox.setMessage("Erreur Base de donnée");
					msgBox.setText("erreur de liée à la base de données : \n" + e1.getMessage());
					msgBox.open();
					e1.printStackTrace();
				}
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});

		this.selection.moveAbove(this.mainComposite);
		this.createButton.pack();
		this.selection.pack();
		this.affectationView.pack();
		this.leftComposite.pack();
		this.rightColumn.pack();
	}

	/**
	 * Add the button "Ajouter" when the user click on an employee the button
	 * "Ajouter" Enable the user to access to the affectation creation page
	 * 
	 * @param composite
	 * @param produitId
	 */

	public void addAjouterForEmployee(int employeId) {

		if (!Objects.isNull(this.createButton) && !this.createButton.isDisposed()) {
			createButton.dispose();
			buttons.layout(true, true);
		}
		this.createButton = new Button(this.buttons, SWT.CENTER);
		createButton.setBackground(MyColor.bleuClair);
		buttons.layout(true, true);
		this.createButton.setText("Ajouter Affectation");

		this.createButton.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				try {
					ajouterAffectationEmploye(employeId);
				} catch (SQLException e1) {
					MessageBox msgBox = new MessageBox(affectationView.getShell(), SWT.ERROR);
					msgBox.setMessage("Erreur Base de donnée");
					msgBox.setText("erreur de liée à la base de données : \n" + e1.getMessage());
					msgBox.open();
					e1.printStackTrace();
				}
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});
		if (!Objects.isNull(this.modifyButton) && !this.modifyButton.isDisposed()) {
			this.createButton.moveAbove(this.modifyButton);
			buttons.layout(true, true);
			System.out.println("oui");
		}

		this.selection.moveAbove(this.mainComposite);
		this.createButton.pack();
		this.selection.pack();
		this.affectationView.pack();
		this.leftComposite.pack();
		this.rightColumn.pack();

	}

	/**
	 * Add the button "Supprimer affectation"
	 * 
	 * @param affectationId
	 */
	public void addRemoveForSite(int affectationId, int siteId) {

		if (!Objects.isNull(this.removeButton) && !this.removeButton.isDisposed()) {
			removeButton.dispose();
			buttons.layout(true, true);
		}
		this.removeButton = new Button(this.buttons, SWT.CENTER);
		buttons.layout(true, true);
		this.removeButton.setText("Supprimer Affectation");

		this.removeButton.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				try {
					remove(affectationId);
					siteAffectationDisplay(siteId);
				} catch (SQLException e1) {
					MessageBox msgBox = new MessageBox(affectationView.getShell(), SWT.ERROR);
					msgBox.setMessage("Erreur Base de donnée");
					msgBox.setText("erreur de liée à la base de données : \n" + e1.getMessage());
					msgBox.open();
					e1.printStackTrace();
				}
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});

		if (!Objects.isNull(this.modifyButton) && !this.modifyButton.isDisposed()) {
			this.removeButton.moveBelow(this.modifyButton);
			buttons.layout(true, true);

		} else {
			if (!Objects.isNull(this.createButton) && !this.createButton.isDisposed()) {
				this.removeButton.moveBelow(this.createButton);
				buttons.layout(true, true);

			}
		}

		this.selection.moveAbove(this.mainComposite);
		this.removeButton.pack();
		this.selection.pack();
		this.affectationView.pack();
		this.leftComposite.pack();
		this.rightColumn.pack();

	}

	/**
	 * Add the button "Supprimer affectation"
	 * 
	 * @param affectationId
	 */
	public void addRemoveForEmployee(int affectationId, int employeId) {

		if (!Objects.isNull(this.removeButton) && !this.removeButton.isDisposed()) {
			removeButton.dispose();
			buttons.layout(true, true);
		}
		this.removeButton = new Button(this.buttons, SWT.CENTER);
		buttons.layout(true, true);
		this.removeButton.setText("Supprimer Affectation");

		this.removeButton.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				try {
					remove(affectationId);
					EmployeAffectationDisplay(employeId);
				} catch (SQLException e1) {
					MessageBox msgBox = new MessageBox(affectationView.getShell(), SWT.ERROR);
					msgBox.setMessage("Erreur Base de donnée");
					msgBox.setText("erreur de liée à la base de données : \n" + e1.getMessage());
					msgBox.open();
					e1.printStackTrace();
				}
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});

		if (!Objects.isNull(this.modifyButton) && !this.modifyButton.isDisposed()) {
			this.removeButton.moveBelow(this.modifyButton);
			buttons.layout(true, true);

		} else {
			if (!Objects.isNull(this.createButton) && !this.createButton.isDisposed()) {
				this.removeButton.moveBelow(this.createButton);
				buttons.layout(true, true);

			}
		}

		this.selection.moveAbove(this.mainComposite);
		this.removeButton.pack();
		this.selection.pack();
		this.affectationView.pack();
		this.leftComposite.pack();
		this.rightColumn.pack();

	}

	protected void remove(int affectationId) throws SQLException {
		MABAssignment.getAffectation(affectationId).remove();

	}

	/**
	 * Create the filter on Month
	 */
	private void createMonthFilter() {
		if (Objects.isNull(monthFilter) || monthFilter.isDisposed()) {
			Composite monthComposite = new Composite(this.filterComposite, SWT.NONE);
			monthComposite.setLayout(new RowLayout(SWT.HORIZONTAL));
			monthComposite.setBackground(MyColor.bleuClair);
			Label monthLabel = new Label(monthComposite, SWT.NONE);
			monthLabel.setText("Mois");
			monthLabel.setBackground(MyColor.bleuClair);
			this.monthFilter = new Combo(monthComposite, SWT.NONE);

			String[] frenchMonth = { "Janvier", "Février", "Mars", "Avril", "Mai", "Juin", "Juillet", "Août",
					"Septembre", "Octobre", "Novembre", "Décembre" };

			for (String m : frenchMonth)
				this.monthFilter.add(m);
			int currentMonth = LocalDate.now().getMonth().getValue();
			this.monthFilter.select(currentMonth - 1);
			this.monthFilter.pack();
			this.buttons.pack();
			this.selection.pack();

		}

	}

	// Year part
	private void createYearFilter() {
		if (Objects.isNull(yearFilter) || yearFilter.isDisposed()) {
			Composite yearComposite = new Composite(this.filterComposite, SWT.NONE);

			yearComposite.setLayout(new RowLayout(SWT.HORIZONTAL));
			yearComposite.setBackground(MyColor.bleuClair);
			Label yearLabel = new Label(yearComposite, SWT.NONE);
			yearLabel.setText("Année");
			yearLabel.setBackground(MyColor.bleuClair);
			this.yearFilter = new Combo(yearComposite, SWT.NONE);

			int currentYear = Year.now().getValue();
			for (int i = currentYear - 2; i < currentYear + 3; i++)
				this.yearFilter.add("" + i);
			this.yearFilter.select(2);
			this.yearFilter.pack();
		}

	}

	/***
	 * 
	 * ADD forms
	 * 
	 */

	/***
	 * 
	 * Display the form to add an Affectation to a Site
	 * 
	 * @param <type>int</type>chantierId
	 * @throws SQLException
	 */

	private void ajouterChantierAffectation(int siteId) throws SQLException {
		this.mainComposite.dispose();
		this.selection.dispose();
		addHeader("Création d'une affectation");
		Composite ajoutComposite = new Composite(this.affectationView, SWT.NONE);
		ajoutComposite.setBackground(MyColor.bleuClair);
		ajoutComposite.setLayout(new RowLayout(SWT.VERTICAL));
		Site site = Site.getSiteById(siteId);

		// Employe name part

		Label labelTitle = new Label(ajoutComposite, SWT.NONE);
		labelTitle.setText("Chantier : " + site.getName() + " :  " + site.getAdresse());
		labelTitle.setBackground(MyColor.bleuClair);
		// nbHeures part

		Composite nbHeureComposite = new Composite(ajoutComposite, SWT.NONE);

		nbHeureComposite.setLayout(new RowLayout(SWT.VERTICAL));
		nbHeureComposite.setBackground(MyColor.bleuClair);

		Label nbHeureLabel = new Label(nbHeureComposite, SWT.NONE);
		nbHeureLabel.setText("Nombre d'heures");
		nbHeureLabel.setBackground(MyColor.bleuClair);
		Text nbHeureTexte = new Text(nbHeureComposite, SWT.NONE);

		Table table = EmployeeView.getAllEmployerForAffectation(ajoutComposite);

		table.setLayoutData(new RowData(400, 100));

		// Month part

		Composite monthComposite = new Composite(ajoutComposite, SWT.NONE);
		monthComposite.setLayout(new RowLayout(SWT.HORIZONTAL));
		monthComposite.setBackground(MyColor.bleuClair);
		Label monthLabel = new Label(monthComposite, SWT.NONE);
		monthLabel.setText("Mois");
		monthLabel.setBackground(MyColor.bleuClair);
		Combo comboMonth = new Combo(monthComposite, SWT.NONE);

		String[] frenchMonth = { "Janvier", "Février", "Mars", "Avril", "Mai", "Juin", "Juillet", "Août", "Septembre",
				"Octobre", "Novembre", "Décembre" };

		for (String m : frenchMonth)
			comboMonth.add(m);
		int currentMonth = LocalDate.now().getMonth().getValue();
		comboMonth.select(currentMonth - 1);

		// Year part
		Composite yearComposite = new Composite(ajoutComposite, SWT.NONE);
		yearComposite.setLayout(new RowLayout(SWT.HORIZONTAL));
		yearComposite.setBackground(MyColor.bleuClair);
		Label yearLabel = new Label(yearComposite, SWT.NONE);
		yearLabel.setText("Année");
		yearLabel.setBackground(MyColor.bleuClair);
		Combo comboYear = new Combo(yearComposite, SWT.NONE);

		int currentYear = Year.now().getValue();
		for (int i = currentYear - 2; i < currentYear + 3; i++)
			comboYear.add("" + i);
		comboYear.select(2);

		Composite buttonComposite = new Composite(ajoutComposite, SWT.NONE);
		buttonComposite.setLayout(new RowLayout(SWT.HORIZONTAL));
		buttonComposite.setBackground(MyColor.bleuClair);
		// ValidationButton part

		Button buttonValide = new Button(buttonComposite, SWT.CENTER);
		buttonValide.setText("Valider");
		buttonValide.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				String checkEmployeId = "";
				String checkNbHours = "";
				Integer checkMonth = -1;
				String checkYear = "";
				Boolean isChecked = false;

				try {

					checkEmployeId = table.getSelection()[0].getText(3);
					checkNbHours = nbHeureTexte.getText().trim().replace(",", ".");
					checkMonth = comboMonth.getSelectionIndex() + 1;
					checkYear = comboYear.getText();

					isChecked = checkAffectation("" + siteId, checkEmployeId, checkNbHours, checkMonth, checkYear);
				} catch (IllegalArgumentException | ArrayIndexOutOfBoundsException argException) {
					MessageBox dialog = new MessageBox(affectationView.getShell(), SWT.ICON_ERROR | SWT.OK);
					dialog.setText("Erreur Création :");
					if (argException.getClass() == IllegalArgumentException.class)
						dialog.setMessage("Une erreur est survenue lors de la création de l'affectation. " + '\n'
								+ argException.getMessage());
					if (argException.getClass() == ArrayIndexOutOfBoundsException.class)
						dialog.setMessage("Veuillez sélectionner un employé");
					dialog.open();
				}

				if (isChecked) {

					Integer idEmploye = Integer.parseInt(checkEmployeId);
					Double nbHeure = Double.parseDouble(checkNbHours);
					Month month = Month.of(checkMonth);
					Year year = Year.of(Integer.parseInt(checkYear));
					Status status = Status.PUBLISHED;
					if (table.getSelection().length == 1) {
						MABAssignment affectation = new MABAssignment(siteId, idEmploye, nbHeure, month, year, status);
						try {
							affectation.insertDatabase();
							ajoutComposite.dispose();

							getVueAffectation().pack();
							getVueAffectation().getParent().pack();
							buildHome();

							MessageBox dialog = new MessageBox(affectationView.getShell(), SWT.ICON_WORKING | SWT.OK);
							dialog.setText("Succes");
							dialog.setMessage("L'affectation crée a bien été enregistrée");
							dialog.open();
						} catch (SQLException sqlException) {
							MessageBox dialog = new MessageBox(affectationView.getShell(), SWT.ICON_ERROR | SWT.OK);
							dialog.setText("Erreur Création :");
							dialog.setMessage("Une erreur est survenue lors de la création de l'affectation. " + '\n'
									+ sqlException.getMessage());
							dialog.open();
						}

					}
				}
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}

		});

		Button buttonCancel = new Button(buttonComposite, SWT.CENTER);
		buttonCancel.setText("Retour");
		buttonCancel.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				try {
					ajoutComposite.dispose();
					buildHome();
				} catch (SQLException e1) {
					MessageBox msgBox = new MessageBox(affectationView.getShell(), SWT.ERROR);
					msgBox.setMessage("Erreur Base de donnée");
					msgBox.setText("erreur de liée à la base de données : \n" + e1.getMessage());
					msgBox.open();
					e1.printStackTrace();
				}

			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}

		});

		nbHeureTexte.pack();

		this.affectationView.pack();
		this.rightColumn.pack();

	}

	/**
	 * Display the form to add an affectation to an Employee
	 * 
	 * @param <type>int</type> employeId
	 * @throws SQLException
	 */
	public void ajouterAffectationEmploye(int employeeId) throws SQLException {
		this.mainComposite.dispose();
		this.selection.dispose();
		addHeader("Création d'une affectation");
		Composite ajoutComposite = new Composite(this.affectationView, SWT.NONE);
		ajoutComposite.setBackground(MyColor.bleuClair);
		ajoutComposite.setLayout(new RowLayout(SWT.VERTICAL));
		Employee employee = Employee.getEmployeById(employeeId);

		// Employe name part

		Label labelName = new Label(ajoutComposite, SWT.NONE);
		labelName.setText("Employé : " + employee.getSurname() + "  " + employee.getFirstName());
		labelName.setBackground(MyColor.bleuClair);
		// nbHeures part

		Composite nbHeureComposite = new Composite(ajoutComposite, SWT.NONE);

		nbHeureComposite.setLayout(new RowLayout(SWT.VERTICAL));

		Label nbHeureLabel = new Label(nbHeureComposite, SWT.NONE);
		nbHeureLabel.setBackground(MyColor.bleuClair);
		nbHeureComposite.setBackground(MyColor.bleuClair);
		nbHeureLabel.setText("Nombre d'heures");
		Text nbHeureTexte = new Text(nbHeureComposite, SWT.NONE);

		Label selectionner = new Label(ajoutComposite, SWT.NONE);
		selectionner.setText("Selectionnez un chantier :");
		selectionner.setBackground(MyColor.bleuClair);

		Table table = SiteView.getTableAllChantier(ajoutComposite, 400, 400);

		table.setLayoutData(new RowData(400, 100));

		// Month part

		Composite monthComposite = new Composite(ajoutComposite, SWT.NONE);
		monthComposite.setLayout(new RowLayout(SWT.HORIZONTAL));
		Label monthLabel = new Label(monthComposite, SWT.NONE);
		monthLabel.setText("Mois");
		monthComposite.setBackground(MyColor.bleuClair);
		monthLabel.setBackground(MyColor.bleuClair);
		Combo comboMonth = new Combo(monthComposite, SWT.NONE);

		String[] frenchMonth = { "Janvier", "Février", "Mars", "Avril", "Mai", "Juin", "Juillet", "Août", "Septembre",
				"Octobre", "Novembre", "Décembre" };

		for (String m : frenchMonth)
			comboMonth.add(m);
		int currentMonth = LocalDate.now().getMonthValue();

		comboMonth.select(currentMonth - 1);

		// Year part
		Composite yearComposite = new Composite(ajoutComposite, SWT.NONE);
		yearComposite.setLayout(new RowLayout(SWT.HORIZONTAL));
		yearComposite.setBackground(MyColor.bleuClair);

		Label yearLabel = new Label(yearComposite, SWT.NONE);
		yearLabel.setText("Année");
		yearLabel.setBackground(MyColor.bleuClair);
		Combo comboYear = new Combo(yearComposite, SWT.NONE);

		int currentYear = Year.now().getValue();
		for (int i = currentYear - 2; i < currentYear + 3; i++)
			comboYear.add("" + i);
		comboYear.select(2);

		Composite buttonComposite = new Composite(ajoutComposite, SWT.NONE);
		buttonComposite.setBackground(MyColor.bleuClair);

		buttonComposite.setLayout(new RowLayout(SWT.HORIZONTAL));
		// ValidationButton part

		Button buttonValide = new Button(buttonComposite, SWT.CENTER);
		buttonValide.setText("Valider");
		buttonValide.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				String checkNbHours = "";
				Integer checkMonth = -1;
				String checkYear = "";
				String checkSiteId = "";
				Boolean isChecked = false;

				try {

					checkSiteId = "" + Site.getSiteIdByName(table.getSelection()[0].getText(0));
					checkNbHours = nbHeureTexte.getText().trim().replace(",", ".");
					checkMonth = comboMonth.getSelectionIndex() + 1;
					checkYear = comboYear.getText();

					isChecked = checkAffectation(checkSiteId, "" + employeeId, checkNbHours, checkMonth, checkYear);
				} catch (IllegalArgumentException | ArrayIndexOutOfBoundsException | SQLException argException) {
					MessageBox dialog = new MessageBox(affectationView.getShell(), SWT.ICON_ERROR | SWT.OK);
					dialog.setText("Erreur Création :");
					if (argException.getClass() == IllegalArgumentException.class)
						dialog.setMessage("Une erreur est survenue lors de la création de l'afféctation. " + '\n'
								+ argException.getMessage());
					if (argException.getClass() == ArrayIndexOutOfBoundsException.class)
						dialog.setMessage("Veuillez sélectionner un chantier");
					dialog.open();
				}

				if (isChecked) {

					Integer siteId = Integer.parseInt(checkSiteId);
					Double nbHeure = Double.parseDouble(checkNbHours);
					Month month = Month.of(checkMonth);
					Year year = Year.of(Integer.parseInt(checkYear));
					Status status = Status.PUBLISHED;
					if (table.getSelection().length == 1) {
						MABAssignment affectation = new MABAssignment(siteId, employeeId, nbHeure, month, year, status);
						try {
							affectation.insertDatabase();
							ajoutComposite.dispose();

							getVueAffectation().pack();
							getVueAffectation().getParent().pack();
							buildHome();

							MessageBox dialog = new MessageBox(affectationView.getShell(), SWT.ICON_WORKING | SWT.OK);
							dialog.setText("Succes");
							dialog.setMessage("L'affectation a été crée a bien été enregistrée");
							dialog.open();
						} catch (SQLException sqlException) {
							MessageBox dialog = new MessageBox(affectationView.getShell(), SWT.ICON_ERROR | SWT.OK);
							dialog.setText("Erreur Création :");

							if (sqlException.getMessage().contentEquals("PRIMARY")) {
								dialog.setMessage("Une erreur est survenue lors de la création de l'affectation. "
										+ '\n' + "Cette affectation existe déjà les doublons ne sont pas autorisés");
							} else {

								dialog.setMessage("Une erreur est survenue lors de la création de l'affectation. "
										+ '\n' + sqlException.getMessage());
							}

							dialog.open();
						}

					}
				}

			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}

		});

		Button buttonCancel = new Button(buttonComposite, SWT.CENTER);
		buttonCancel.setText("Retour");
		buttonCancel.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				try {
					ajoutComposite.dispose();
					buildHome();
				} catch (SQLException e1) {
					MessageBox msgBox = new MessageBox(affectationView.getShell(), SWT.ERROR);
					msgBox.setMessage("Erreur Base de donnée");
					msgBox.setText("erreur de liée à la base de données : \n" + e1.getMessage());
					msgBox.open();
					e1.printStackTrace();
				}

			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}

		});

		table.pack();
		nbHeureTexte.pack();
		buttonValide.pack();
		labelName.pack();
		nbHeureComposite.pack();

		ajoutComposite.pack();

		this.affectationView.pack();
		this.affectationView.getParent().pack();

	}

	/**
	 * 
	 * Modification Forms
	 * 
	 */

	/**
	 * display the modify forms in the main composite with data from
	 * <param>affectationId</param>
	 * 
	 * @param affectationId
	 * @throws SQLException
	 */
	private void modifyEmployeeAffectation(int affectationId) throws SQLException {
		addHeader("Modification d'une affectation");
		MABAssignment affectation = MABAssignment.getAffectation(affectationId);
		this.mainComposite.dispose();
		this.selection.dispose();

		Composite modifComposite = new Composite(this.affectationView, SWT.NONE);
		modifComposite.setBackground(MyColor.bleuClair);
		modifComposite.setLayout(new RowLayout(SWT.VERTICAL));
		Employee employe = Employee.getEmployeById(affectation.getIdEmploye());

		Label labelNom = new Label(modifComposite, SWT.NONE);
		labelNom.setText("Employé : " + employe.getSurname() + " " + employe.getFirstName());
		labelNom.setBackground(MyColor.bleuClair);

		Label labelSite = new Label(modifComposite, SWT.NONE);
		labelSite.setText("Chantier : " + Site.getSiteById(affectation.getIdChantier()).getName());
		labelSite.setBackground(MyColor.bleuClair);
		Composite nbHeureComposite = new Composite(modifComposite, SWT.NONE);
		nbHeureComposite.setBackground(MyColor.bleuClair);

		nbHeureComposite.setLayout(new RowLayout(SWT.VERTICAL));
		Label nbHeureLabel = new Label(nbHeureComposite, SWT.NONE);
		nbHeureLabel.setText("Nombre d'heures");
		nbHeureLabel.setBackground(MyColor.bleuClair);
		Text nbHeureTexte = new Text(nbHeureComposite, SWT.NONE);
		nbHeureTexte.setText(affectation.getNombreHeures() + "");

		Composite buttonComposite = new Composite(modifComposite, SWT.NONE);
		buttonComposite.setBackground(MyColor.bleuClair);
		buttonComposite.setLayout(new RowLayout(SWT.HORIZONTAL));

		Button buttonValide = new Button(buttonComposite, SWT.CENTER);
		buttonValide.setText("Valider");

		buttonValide.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				String checkNbHours = "";

				String checkSiteId = "";
				boolean isChecked = false;

				try {

					checkSiteId = "" + affectation.getIdChantier();

					checkNbHours = nbHeureTexte.getText().trim().replace(",", ".");

					isChecked = checkAffectation(checkSiteId, "" + affectation.getIdEmploye(), checkNbHours,
							affectation.getMonth().getValue(), "" + affectation.getYear().getValue());
				} catch (IllegalArgumentException | ArrayIndexOutOfBoundsException argException) {
					MessageBox dialog = new MessageBox(affectationView.getShell(), SWT.ICON_ERROR | SWT.OK);
					dialog.setText("Erreur Création :");
					if (argException.getClass() == IllegalArgumentException.class)
						dialog.setMessage("Une erreur est survenue lors de la mise à jour de l'affectation. " + '\n'
								+ argException.getMessage());
					if (argException.getClass() == ArrayIndexOutOfBoundsException.class)
						dialog.setMessage("Veuillez sélectionner un chantier");
					dialog.open();
				}

				if (isChecked) {

					affectation.setIdChantier(Integer.parseInt(checkSiteId));
					affectation.setNombreHeures(Double.parseDouble(checkNbHours));

					try {

						affectation.update();

						modifComposite.dispose();
						getVueAffectation().pack();
						getVueAffectation().getParent().pack();
						buildHome();

						MessageBox dialog = new MessageBox(affectationView.getShell(), SWT.ICON_WORKING | SWT.OK);
						dialog.setText("Succes");
						dialog.setMessage("L'affectation a été crée a bien été enregistrée");
						dialog.open();
					} catch (SQLException sqlException) {
						MessageBox dialog = new MessageBox(affectationView.getShell(), SWT.ICON_ERROR | SWT.OK);
						dialog.setText("Erreur Création :");
						dialog.setMessage("Une erreur est survenue lors de mise à jour de l'affectation. " + '\n'
								+ sqlException.getMessage());
						dialog.open();
					}

				}

			}

		});

		Button buttonCancel = new Button(buttonComposite, SWT.CENTER);
		buttonCancel.setText("Retour");

		buttonCancel.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				try {
					modifComposite.dispose();

					buildHome();
				} catch (SQLException e1) {
					MessageBox msgBox = new MessageBox(affectationView.getShell(), SWT.ERROR);
					msgBox.setMessage("Erreur Base de donnée");
					msgBox.setText("erreur de liée à la base de données : \n" + e1.getMessage());
					msgBox.open();

				}

			}

		});

		buttonCancel.pack();
		buttonValide.pack();
		labelNom.pack();
		buttonComposite.pack();
		nbHeureComposite.pack();

		modifComposite.pack();

		this.affectationView.pack();
		this.affectationView.getParent().pack();

	}

	/**
	 * display the form to modify the affectation with <param> affectationId</param>
	 * 
	 * @param affectationId
	 * @throws SQLException
	 */
	private void modifySiteAffectation(int affectationId) throws SQLException {
		addHeader("Modification d'une affectation");
		MABAssignment affectation = MABAssignment.getAffectation(affectationId);
		this.mainComposite.dispose();
		this.selection.dispose();

		Composite modifComposite = new Composite(this.affectationView, SWT.NONE);
		modifComposite.setBackground(MyColor.bleuClair);
		modifComposite.setLayout(new RowLayout(SWT.VERTICAL));
		Site site = Site.getSiteById(affectation.getIdEmploye());

		Label labelNom = new Label(modifComposite, SWT.NONE);
		labelNom.setText("Chantier : " + site.getName() + " : " + site.getAdresse());
		labelNom.setBackground(MyColor.bleuClair);

		Label labelEmploye = new Label(modifComposite, SWT.NONE);
		labelEmploye.setText("Employé : " + Employee.getEmployeById(affectation.getIdEmploye()).getSurname());
		labelEmploye.setBackground(MyColor.bleuClair);

		Composite nbHeureComposite = new Composite(modifComposite, SWT.NONE);

		nbHeureComposite.setLayout(new RowLayout(SWT.VERTICAL));
		Label nbHeureLabel = new Label(nbHeureComposite, SWT.NONE);
		nbHeureLabel.setText("Nombre d'heures");
		nbHeureComposite.setBackground(MyColor.bleuClair);
		nbHeureLabel.setBackground(MyColor.bleuClair);
		Text nbHeureTexte = new Text(nbHeureComposite, SWT.NONE);
		nbHeureTexte.setText(affectation.getNombreHeures() + "");

		nbHeureTexte.pack();

		Composite buttonComposite = new Composite(modifComposite, SWT.NONE);
		buttonComposite.setLayout(new RowLayout(SWT.HORIZONTAL));
		buttonComposite.setBackground(MyColor.bleuClair);
		Button buttonValide = new Button(buttonComposite, SWT.CENTER);
		buttonValide.setText("Valider");
		buttonValide.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				String checkNbHours = "";

				String checkEmployeId = "";
				boolean isChecked = false;

				try {

					checkEmployeId = "" + affectation.getIdChantier();
					checkNbHours = nbHeureTexte.getText().trim().replace(",", ".");
					isChecked = checkAffectation(affectation.getIdChantier() + "", checkEmployeId, checkNbHours,
							affectation.getMonth().getValue(), "" + affectation.getYear().getValue());
				} catch (IllegalArgumentException | ArrayIndexOutOfBoundsException argException) {
					MessageBox dialog = new MessageBox(affectationView.getShell(), SWT.ICON_ERROR | SWT.OK);
					dialog.setText("Erreur Création :");
					if (argException.getClass() == IllegalArgumentException.class)
						dialog.setMessage("Une erreur est survenue lors de la création de l'afféctation. " + '\n'
								+ argException.getMessage());
					if (argException.getClass() == ArrayIndexOutOfBoundsException.class)
						dialog.setMessage("Veuillez sélectionner un employé");
					dialog.open();
				}

				if (isChecked) {

					affectation.setIdChantier(Integer.parseInt(checkEmployeId));
					affectation.setNombreHeures(Double.parseDouble(checkNbHours));

					try {
						affectation.update();

						modifComposite.dispose();
						getVueAffectation().pack();
						getVueAffectation().getParent().pack();
						buildHome();

						MessageBox dialog = new MessageBox(affectationView.getShell(), SWT.ICON_WORKING | SWT.OK);
						dialog.setText("Succes");
						dialog.setMessage("L'affectation a été crée a bien été enregistrée");
						dialog.open();
					} catch (SQLException sqlException) {
						MessageBox dialog = new MessageBox(affectationView.getShell(), SWT.ICON_ERROR | SWT.OK);
						dialog.setText("Erreur Création :");
						dialog.setMessage("Une erreur est survenue lors de la création de l'affectation. " + '\n'
								+ sqlException.getMessage());
						dialog.open();

					}

				}

			}

		});

		Button buttonCancel = new Button(buttonComposite, SWT.CENTER);
		buttonCancel.setText("Retour");
		buttonCancel.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				try {
					modifComposite.dispose();
					buildHome();
				} catch (SQLException e1) {
					MessageBox msgBox = new MessageBox(affectationView.getShell(), SWT.ERROR);
					msgBox.setMessage("Erreur Base de donnée");
					msgBox.setText("erreur de liée à la base de données : \n" + e1.getMessage());
					msgBox.open();
				}

			}

		});

		buttonValide.pack();
		labelNom.pack();
		nbHeureComposite.pack();

		modifComposite.pack();

		this.affectationView.pack();
		this.affectationView.getParent().pack();

	}

	/**
	 * Dispose and create a new empty rightComposite
	 */
	private void cleanRightComposite() {
		if (!Objects.isNull(this.rightComposite) && !this.rightComposite.isDisposed()) {
			this.rightComposite.dispose();
			this.mainComposite.layout(true, true);
		}
		// this.rightComposite = new Composite(this.mainComposite, SWT.NONE);
		this.mainComposite.layout(true, true);
		this.mainComposite.pack();
		// this.rightComposite.pack();
		this.affectationView.pack();
		this.rightColumn.pack();
	}

	/**
	 * dispose all buttons
	 */
	private void cleanButtons() {
		if (!Objects.isNull(createButton) && !createButton.isDisposed()) {
			createButton.dispose();
			buttons.layout(true, true);

		}

		if (!Objects.isNull(modifyButton) && !modifyButton.isDisposed()) {
			modifyButton.dispose();
			buttons.layout(true, true);

		}

		this.buttons.pack();
		this.selection.pack();
		this.affectationView.pack();
		this.rightColumn.pack();

	}

	/*******************
	 * 
	 * Check functions
	 * 
	 *****************/

	/**
	 * Check if the number of hours is valid
	 * 
	 * @param nbHours
	 * @return
	 */
	public boolean checkAffectation(String siteId, String employeId, String nbHours, Integer month, String year) {

		if (Objects.isNull(siteId))
			throw new IllegalArgumentException("Veuillez selectionner un chantier valide");
		try {
			Integer.parseInt(siteId);
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException("Veuillez selectionner un chantier valide");
		}

		if (Objects.isNull(employeId))
			throw new IllegalArgumentException("Veuillez selectionner un employé valide");
		try {
			Integer.parseInt(employeId);
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException("Veuillez selectionner un employé valide");
		}
		if (Objects.isNull(nbHours))
			throw new IllegalArgumentException("L'attribut nombre d'heure ne peut pas être null");

		try {
			Double priceCheck = Double.parseDouble(nbHours);

			if (priceCheck < 0)
				throw new IllegalArgumentException("Le  nombre d'heures doit être supérieur à 0");
		} catch (NumberFormatException parseDoubleException) {
			throw new IllegalArgumentException(
					"Le nombre d'heure entré n'est pas valide, veuillez entrer une valeur numérique");
		}

		if (Objects.isNull(month))
			throw new IllegalArgumentException("le champ mois ne peut pas être null");
		else {
			try {

				Month.of(month);
			} catch (DateTimeException dateTimeException) {
				throw new IllegalArgumentException("la valeur entrée dans le champ mois est incorrecte");
			}
		}

		if (Objects.isNull(year))
			throw new IllegalArgumentException("le champ mois ne peut pas être null");
		else {
			try {

				Year.of(Integer.parseInt(year));
			} catch (DateTimeException | NumberFormatException exceptionYear) {
				throw new IllegalArgumentException(
						"la valeur entrée dans le champ Année est incorrecte, Veuillez selectionner une Année valide");
			}
		}

		return true;
	}

	/**
	 * Add a header in header composite with title <param>header</param>
	 * 
	 * @param <type>String</type> header
	 */
	public void addHeader(String header) {
		if (!Objects.isNull(this.header) && !this.header.isDisposed())
			this.header.dispose();
		this.header = new Composite(this.affectationView, SWT.CENTER | SWT.BORDER);
		this.header.setBackground(MyColor.bleuFonce);
		FillLayout layout = new FillLayout();
		layout.marginWidth = 205;
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
