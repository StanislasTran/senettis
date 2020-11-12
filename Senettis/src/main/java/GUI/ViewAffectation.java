package GUI;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

import classes.Affectation;
import classes.Employee;
import classes.Site;

public class ViewAffectation {

	private Composite affectationView;
	private Composite selection;
	private Composite mainComposite;
	private Composite rightComposite;
	private Composite leftComposite;
	private Composite rightColumn;

	/**
	 * Constructor
	 * 
	 * @param composite
	 * @throws SQLException
	 */
	public ViewAffectation(Composite composite) throws SQLException {
		Couleur.setDisplay(composite.getDisplay());
		this.affectationView = new Composite(composite, SWT.NONE);
		this.affectationView.setLayout(new RowLayout(SWT.VERTICAL));
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

		selection(this.affectationView);

		this.mainComposite = new Composite(this.affectationView, SWT.NONE);
		this.mainComposite.setLayout(new RowLayout(SWT.HORIZONTAL));

		this.leftComposite = new Composite(this.mainComposite, SWT.NONE);
		this.rightComposite = new Composite(this.mainComposite, SWT.NONE);

		// TabFolder creation and fill

		TabFolder tabFolder = new TabFolder(leftComposite, SWT.BORDER);

		tabFolder.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent event) {
				selection.dispose();
				disposeAllChildren(getRightComposite());
				getVueAffectation().pack();

			}
		});

		TabItem tabEmploye = new TabItem(tabFolder, SWT.NULL);
		tabEmploye.setText("Affectation par Employé");

		TabItem tabChantier = new TabItem(tabFolder, SWT.NULL);
		tabChantier.setText("Affectation par Chantier");

		createTableEmployeStats(tabEmploye);
		createTableChantierStats(tabChantier);

		mainComposite.pack();
		tabFolder.pack();
		leftComposite.pack();
		rightComposite.pack();
		affectationView.pack();
		this.rightColumn.pack();

	}

	/****
	 * 
	 * 
	 * Table with stats for tabItem creation
	 *
	 */

	/**
	 * addd a table which contain stats for all Employee in a <param>
	 * <type>TabItem</type> tabEmploye</param>
	 * 
	 * @param tabEmploye
	 * @throws SQLException
	 */
	private void createTableEmployeStats(TabItem tabEmploye) throws SQLException {

		final Table table = new Table(tabEmploye.getParent(),
				SWT.BORDER | SWT.MULTI | SWT.V_SCROLL | SWT.FULL_SELECTION);
		table.setLayoutData(new RowData(900, 800));

		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		String[] titles = { "EmployeId", "Nom", "Prenom", "Nombre d'employés différents", "Nombre d'heures total" };

		for (String title : titles) {
			TableColumn column = new TableColumn(table, SWT.NONE);
			column.setText(title);
		}

		ResultSet result = Affectation.getEmployeStats();
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
				item.setText(4, "Inconnu");
			else
				item.setText(4, result.getString("nb_heure"));

		}

		table.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				if (table.getSelectionIndex() != -1) {

					int employeId = Integer.parseInt(table.getSelection()[0].getText());

					try {
						compositeSelectionAjouterForEmployee(employeId);
						EmployeAffectationDisplay(employeId);

					} catch (SQLException e1) {
						// TODO Auto-generated catch block
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
	private void createTableChantierStats(TabItem tabChantier) throws SQLException {

		final Table table = new Table(tabChantier.getParent(),
				SWT.BORDER | SWT.MULTI | SWT.V_SCROLL | SWT.FULL_SELECTION);
		table.setLayoutData(new RowData(900, 800));

		table.setLinesVisible(true);
		table.setHeaderVisible(true);

		String[] titles = { "ChantierId", "Nom", "CA", "Nombre de chantiers différents", "Nombre d'heures total" };

		for (String title : titles) {
			TableColumn column = new TableColumn(table, SWT.NONE);
			column.setText(title);
		}

		ResultSet result = Affectation.getChantierStats();
		final TableColumn[] columns = table.getColumns();
		while (result.next()) {
			TableItem item = new TableItem(table, SWT.NONE);
			item.setText(0, "" + result.getInt("ChantierId"));
			item.setText(1, result.getString("Nom"));
			if (result.getDouble("CA") == 0)
				item.setText(2, "" + "Inconnu");
			else
				item.setText(2, "" + result.getDouble("CA"));

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
						// TODO Auto-generated catch block
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
		ResultSet result = Affectation.getEmployeAffectation(employeId);

		disposeAllChildren(this.rightComposite);

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

				AddModifButtonEmployee(affectationsId.get(table.getSelectionIndex()), employeId);

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
		ResultSet result = Affectation.getSiteAffectation(siteId);
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

	/**
	 * Ajoute une selection sur la la ligne contenant un chantierId égal au
	 * paramètre chantier Id
	 * 
	 * @param table
	 * @param idChantier
	 */
	private void setSelectionOnChantierId(Table table, int idChantier) {

		for (int i = 0; i < table.getItems().length; i++)
			if (Integer.parseInt((table.getItem(i).getText(3))) == idChantier)
				table.setSelection(i);

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
		RowLayout rowLayout = new RowLayout();
		this.selection.setLayout(rowLayout);
		this.selection.setBackground(Couleur.PeterRiver);

		this.selection.pack();
	}

	/**
	 * Add the button "Ajouter"and "Modifier" when the user click on an employee
	 * 
	 * @param employeId
	 */
	public void AddModifButtonEmployee(int affectationId, int employeId) {
		compositeSelectionAjouterForEmployee(employeId);
		if (this.selection.getChildren().length == 2) {
			this.selection.getChildren()[1].dispose();
		}

		Button boutonModifier = new Button(this.selection, SWT.CENTER);
		boutonModifier.setText("Modifier Affectation");

		boutonModifier.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				try {
					modifyEmployeeAffectation(affectationId);
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});

		this.selection.moveAbove(this.mainComposite);
		boutonModifier.pack();
		this.selection.pack();

	}

	/**
	 * Add the button "Ajouter"and "Modifier" when the user click on an employee
	 * 
	 * @param employeId
	 */
	public void AddModifButtonSite(int affectationId, int siteId) {
		addButtonAjouterForSite(siteId);
		if (this.selection.getChildren().length == 2) {
			this.selection.getChildren()[1].dispose();
		}

		Button boutonModifier = new Button(this.selection, SWT.CENTER);
		boutonModifier.setText("Modifier Affectation");

		boutonModifier.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				try {
					modifySiteAffectation(affectationId);
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});

		this.selection.moveAbove(this.mainComposite);
		boutonModifier.pack();
		this.selection.pack();

	}

	/**
	 * 
	 * add the button "Ajouter" for site
	 * 
	 * @param chantierId
	 */
	private void addButtonAjouterForSite(int chantierId) {

		if (this.selection.isDisposed())
			this.selection = new Composite(this.affectationView, SWT.NONE);
		this.selection.setLayout(new RowLayout(SWT.HORIZONTAL));

		if (this.selection.getChildren().length > 0)
			selection.getChildren()[0].dispose();
		Button boutonAjout = new Button(this.selection, SWT.CENTER);
		boutonAjout.setText("Ajouter Affectation");

		boutonAjout.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				try {
					ajouterChantierAffectation(chantierId);
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});

		this.selection.moveAbove(this.mainComposite);
		boutonAjout.pack();
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

	public void compositeSelectionAjouterForEmployee(int employeId) {

		if (this.selection.isDisposed())
			this.selection = new Composite(this.affectationView, SWT.NONE);
		this.selection.setLayout(new RowLayout(SWT.HORIZONTAL));

		if (this.selection.getChildren().length > 0)
			selection.getChildren()[0].dispose();
		Button boutonAjout = new Button(this.selection, SWT.CENTER);
		boutonAjout.setText("Ajouter Affectation");

		boutonAjout.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				try {
					ajouterAffectationEmploye(employeId);
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});

		this.selection.moveAbove(this.mainComposite);
		boutonAjout.pack();
		this.selection.pack();
		this.affectationView.pack();
		this.leftComposite.pack();
		this.rightColumn.pack();

	}

	/***
	 * 
	 * ADD forms
	 * 
	 */

	/***
	 * 
	 * @param chantierId
	 * @throws SQLException
	 */

	private void ajouterChantierAffectation(int siteId) throws SQLException {
		this.mainComposite.dispose();
		this.selection.dispose();

		Composite ajoutComposite = new Composite(this.affectationView, SWT.NONE);

		ajoutComposite.setLayout(new RowLayout(SWT.VERTICAL));
		Site site = Site.getChantierById(siteId);

		// Employe name part

		Label labelTitle = new Label(ajoutComposite, SWT.NONE);
		labelTitle.setText(site.getNom() + " :  " + site.getAdresse());

		// nbHeures part

		Composite nbHeureComposite = new Composite(ajoutComposite, SWT.NONE);

		nbHeureComposite.setLayout(new RowLayout(SWT.VERTICAL));
		Label nbHeureLabel = new Label(nbHeureComposite, SWT.NONE);
		nbHeureLabel.setText("Nombre d'heures");
		Text nbHeureTexte = new Text(nbHeureComposite, SWT.NONE);

		Composite tableComposite = new Composite(ajoutComposite, SWT.NONE);
		Table table = VueEmploye.getAllEmployer(ajoutComposite);

		table.setLayoutData(new RowData(400, 100));

		// ValidationButton part

		Button buttonValide = new Button(ajoutComposite, SWT.CENTER);
		buttonValide.setText("Valider");
		buttonValide.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				if (table.getSelection().length == 1) {
					Affectation affectation = new Affectation(siteId,
							Integer.parseInt(table.getSelection()[0].getText(4)),
							Double.parseDouble(nbHeureTexte.getText()), "Publié");
					try {
						affectation.insertDatabase();
						ajoutComposite.dispose();

						getVueAffectation().pack();
						getVueAffectation().getParent().pack();
						buildHome();

					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}

				}
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}

		});

		nbHeureTexte.pack();
		/*
		 * buttonValide.pack(); labelTitle.pack(); nbHeureComposite.pack();
		 * tableComposite.pack(); ajoutComposite.pack(); table.pack();
		 */

		this.affectationView.pack();
		this.rightColumn.pack();

	}

	/**
	 * 
	 * @param employeId
	 * @throws SQLException
	 */
	public void ajouterAffectationEmploye(int employeeId) throws SQLException {
		this.mainComposite.dispose();
		this.selection.dispose();

		Composite ajoutComposite = new Composite(this.affectationView, SWT.NONE);

		ajoutComposite.setLayout(new RowLayout(SWT.VERTICAL));
		Employee employee = Employee.getEmployeById(employeeId);

		// Employe name part

		Label labelName = new Label(ajoutComposite, SWT.NONE);
		labelName.setText(employee.getNom() + "  " + employee.getPrenom());

		// nbHeures part

		Composite nbHeureComposite = new Composite(ajoutComposite, SWT.NONE);

		nbHeureComposite.setLayout(new RowLayout(SWT.VERTICAL));
		Label nbHeureLabel = new Label(nbHeureComposite, SWT.NONE);
		nbHeureLabel.setText("Nombre d'heures");
		Text nbHeureTexte = new Text(nbHeureComposite, SWT.NONE);

		Composite tableComposite = new Composite(ajoutComposite, SWT.NONE);
		Table table = VueChantier.getTableAllChantier(ajoutComposite);

		table.setLayoutData(new RowData(400, 100));

		// ValidationButton part

		Button buttonValide = new Button(ajoutComposite, SWT.CENTER);
		buttonValide.setText("Valider");
		buttonValide.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				if (table.getSelection().length == 1) {
					Affectation affectation = new Affectation(Integer.parseInt(table.getSelection()[0].getText(3)),
							employeeId, Double.parseDouble(nbHeureTexte.getText()), "Publié");
					try {
						affectation.insertDatabase();
						ajoutComposite.dispose();

						getVueAffectation().pack();
						getVueAffectation().getParent().pack();
						buildHome();

					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}

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
		tableComposite.pack();
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

		Affectation affectation = Affectation.getAffectation(affectationId);
		this.mainComposite.dispose();
		this.selection.dispose();

		Composite modifComposite = new Composite(this.affectationView, SWT.NONE);

		modifComposite.setLayout(new RowLayout(SWT.VERTICAL));
		Employee employe = Employee.getEmployeById(affectation.getIdEmploye());

		Label labelNom = new Label(modifComposite, SWT.NONE);
		labelNom.setText(employe.getNom() + " " + employe.getPrenom());

		Composite nbHeureComposite = new Composite(modifComposite, SWT.NONE);

		nbHeureComposite.setLayout(new RowLayout(SWT.VERTICAL));
		Label nbHeureLabel = new Label(nbHeureComposite, SWT.NONE);
		nbHeureLabel.setText("Nombre d'heures");

		Text nbHeureTexte = new Text(nbHeureComposite, SWT.NONE);
		nbHeureTexte.setText(affectation.getNombreHeures() + "");

		nbHeureTexte.pack();
		Composite tableComposite = new Composite(modifComposite, SWT.NONE);
		Table table = VueChantier.getTableAllChantier(modifComposite);
		setSelectionOnChantierId(table, affectation.getIdChantier());

		table.setLayoutData(new RowData(400, 100));

		Button buttonValide = new Button(modifComposite, SWT.CENTER);
		buttonValide.setText("Valider");
		buttonValide.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				if (table.getSelection().length == 1) {
					Affectation affectation;
					try {
						affectation = Affectation.getAffectation(affectationId);

						affectation.setIdChantier(Integer.parseInt(table.getSelection()[0].getText(3)));
						affectation.setNombreHeures(Double.parseDouble(nbHeureTexte.getText()));

						affectation.update();
						modifComposite.dispose();

						getVueAffectation().pack();
						getVueAffectation().getParent().pack();
						buildHome();
					} catch (SQLException e2) {
						// TODO Auto-generated catch block
						e2.printStackTrace();
					}

					System.out.println("faiiit");

				}
			}

		});

		table.pack();

		buttonValide.pack();
		labelNom.pack();
		nbHeureComposite.pack();
		tableComposite.pack();
		modifComposite.pack();
		table.pack();
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

		Affectation affectation = Affectation.getAffectation(affectationId);
		this.mainComposite.dispose();
		this.selection.dispose();

		Composite modifComposite = new Composite(this.affectationView, SWT.NONE);

		modifComposite.setLayout(new RowLayout(SWT.VERTICAL));
		Site site = Site.getChantierById(affectation.getIdEmploye());

		Label labelNom = new Label(modifComposite, SWT.NONE);
		labelNom.setText(site.getNom() + " : " + site.getAdresse());

		Composite nbHeureComposite = new Composite(modifComposite, SWT.NONE);

		nbHeureComposite.setLayout(new RowLayout(SWT.VERTICAL));
		Label nbHeureLabel = new Label(nbHeureComposite, SWT.NONE);
		nbHeureLabel.setText("Nombre d'heures");

		Text nbHeureTexte = new Text(nbHeureComposite, SWT.NONE);
		nbHeureTexte.setText(affectation.getNombreHeures() + "");

		nbHeureTexte.pack();
		Composite tableComposite = new Composite(modifComposite, SWT.NONE);
		Table table = VueEmploye.getAllEmployer(modifComposite);
		setSelectionOnChantierId(table, affectation.getIdChantier());

		table.setLayoutData(new RowData(400, 100));

		Button buttonValide = new Button(modifComposite, SWT.CENTER);
		buttonValide.setText("Valider");
		buttonValide.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				if (table.getSelection().length == 1) {
					Affectation affectation;
					try {
						affectation = Affectation.getAffectation(affectationId);

						affectation.setIdEmploye(Integer.parseInt(table.getSelection()[0].getText(4)));
						affectation.setNombreHeures(Double.parseDouble(nbHeureTexte.getText()));

						affectation.update();
						modifComposite.dispose();

						getVueAffectation().pack();
						getVueAffectation().getParent().pack();
						buildHome();
					} catch (SQLException e2) {
						// TODO Auto-generated catch block
						e2.printStackTrace();
					}

					System.out.println("faiiit");

				}
			}

		});

		table.pack();

		buttonValide.pack();
		labelNom.pack();
		nbHeureComposite.pack();
		tableComposite.pack();
		modifComposite.pack();
		table.pack();
		this.affectationView.pack();
		this.affectationView.getParent().pack();

	}

}
