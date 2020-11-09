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
import org.eclipse.swt.widgets.Item;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

import classes.Affectation;
import classes.Employe;

public class VueAffectation {

	private Composite vueAffectation;
	private Composite selection;
	private Composite mainComposite;
	private Composite rightComposite;
	private Composite leftComposite;

	/**
	 * Constructeur utilisé pour constuire la vue Affectation
	 * 
	 * @param composite
	 * @throws SQLException
	 */
	public VueAffectation(Composite composite) throws SQLException {
		this.vueAffectation = new Composite(composite, SWT.NONE);
		this.vueAffectation.setLayout(new RowLayout(SWT.VERTICAL));
		createVueAffectation(composite);
	}

	private void createVueAffectation(Composite composite) throws SQLException {

		compositeSelection(this.vueAffectation);
		this.mainComposite = new Composite(this.vueAffectation, SWT.NONE);
		this.mainComposite.setLayout(new RowLayout(SWT.HORIZONTAL));

		this.leftComposite = new Composite(this.mainComposite, SWT.NONE);
		this.rightComposite = new Composite(this.mainComposite, SWT.NONE);

		Couleur.setDisplay(composite.getDisplay());

		TabFolder tabFolder = new TabFolder(leftComposite, SWT.BORDER);
		tabFolder.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent event) {
				selection.dispose();
				disposeAllChildren(getRightComposite());

				packAll();
			}
		});

		TabItem tabEmploye = new TabItem(tabFolder, SWT.NULL);
		tabEmploye.setText("Affectation par Employé");

		TabItem tabChantier = new TabItem(tabFolder, SWT.NULL);
		tabChantier.setText("Affectation par Chantier");
		createTableEmployeStats(tabEmploye, this.vueAffectation);
		createTableChantierStats(tabChantier);

		mainComposite.pack();
		tabFolder.pack();
		leftComposite.pack();
		rightComposite.pack();
		vueAffectation.pack();
		vueAffectation.getParent().pack();

	}

	/**
	 * Créer la table qui contient la liste des employés et les stats associées à
	 * ces employés et l'ajoute à un TableItem
	 * 
	 * @param <type> TablItem </type> onglet de type TabItem qui contient la liste
	 *               des employés
	 *
	 * @throws SQLException
	 */

	private void createTableEmployeStats(TabItem tabEmploye, Composite composite) throws SQLException {

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

			public void widgetSelected(SelectionEvent e) {

				if (table.getSelectionIndex() != -1) {

					int employeId = Integer.parseInt(table.getSelection()[0].getText());

					try {
						compositeSelectionAjouter(employeId);
						compositeEmployeAffectationDisplay(employeId);

					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				} else {
					System.out.println("erreur");
				}
				System.out.println("ok");
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
		table.pack();

	}

	/**
	 * 
	 * @param composite
	 */
	public void compositeSelection(Composite composite) {
		this.selection = new Composite(composite, SWT.NONE);
		RowLayout rowLayout = new RowLayout();
		this.selection.setLayout(rowLayout);
		this.selection.setBackground(Couleur.PeterRiver);

		this.selection.pack();
	}

	/**
	 * Ajoute un tableau contenant la liste des chantiers affectés à un employé à un
	 * Composite
	 * 
	 * @param employeId <type>int</type> Identifiant de l'employé affectés aux
	 *                  chantiers à afficher
	 * @param composite </type> Comppsite </type> Composite qui contient le tableau
	 *                  affichant la liste des Chantiers
	 * @throws SQLException
	 */
	public void compositeEmployeAffectationDisplay(int employeId) throws SQLException {
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
				
				compositeSelectionModifier(affectationsId.get(table.getSelectionIndex()),employeId);
				packAll();
			}

			public void widgetDefaultSelected(SelectionEvent e) {
			}

		});
		for (TableColumn col : columns)
			col.pack();

		table.pack();
		packAll();

		this.leftComposite.pack();

	}

	/**
	 * Getter for vueAffectation composite
	 * 
	 * @return <type> Composite </type>
	 */
	public Composite getVueAffectation() {

		return this.vueAffectation;
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

	public void packAll() {
		this.leftComposite.pack();
		this.rightComposite.pack();
		this.mainComposite.pack();

		this.vueAffectation.pack();
		this.vueAffectation.getParent().pack();

	}

	/**
	 * Ajoute le boutton "ajouter Affectation" lorsque l'utilisateur clique sur un
	 * employé
	 * 
	 * @param composite
	 * @param produitId
	 */

	public void compositeSelectionAjouter(int employeId) {
		this.selection.dispose();
		this.selection = new Composite(this.vueAffectation, SWT.NONE);
		this.selection.setLayout(new RowLayout(SWT.HORIZONTAL));

		Button boutonAjout = new Button(this.selection, SWT.CENTER);
		boutonAjout.setText("Ajouter Affectation");

		boutonAjout.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				try {
					ajouterAffectation(employeId);
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

	}

	/**
	 * 
	 * @param employeId
	 */
	public void compositeSelectionModifier(int AffectationId,int employeId) {
		compositeSelectionAjouter(employeId);
		if(this.selection.getChildren().length==2) {
			this.selection.getChildren()[1].dispose();
		}
		
			Button boutonModifier = new Button(this.selection, SWT.CENTER);
			boutonModifier.setText("Modifier Affectation");
		

		boutonModifier.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				try {
					ModifierEmployeAffectation(AffectationId);
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

	private void ModifierEmployeAffectation(int affectationId) throws SQLException {

		Affectation affectation = Affectation.getAffectation(affectationId);
		this.mainComposite.dispose();
		this.selection.dispose();

		Composite modifComposite = new Composite(this.vueAffectation, SWT.NONE);

		modifComposite.setLayout(new RowLayout(SWT.VERTICAL));
		Employe employe = Employe.getEmployeById(affectation.getIdEmploye());

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
						createVueAffectation(getVueAffectation().getParent());
					} catch (SQLException e2) {
						// TODO Auto-generated catch block
						e2.printStackTrace();
					}

					System.out.println("faiiit");

				}
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}

		});

		table.pack();

		buttonValide.pack();
		labelNom.pack();
		nbHeureComposite.pack();
		tableComposite.pack();
		modifComposite.pack();
		table.pack();
		this.vueAffectation.pack();
		this.vueAffectation.getParent().pack();

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

	public void ajouterAffectation(int employeId) throws SQLException {
		this.mainComposite.dispose();
		this.selection.dispose();

		Composite ajoutComposite = new Composite(this.vueAffectation, SWT.NONE);

		ajoutComposite.setLayout(new RowLayout(SWT.VERTICAL));
		Employe employe = Employe.getEmployeById(employeId);

		Label labelNom = new Label(ajoutComposite, SWT.NONE);
		labelNom.setText(employe.getNom() + " " + employe.getPrenom());

		Composite nbHeureComposite = new Composite(ajoutComposite, SWT.NONE);

		nbHeureComposite.setLayout(new RowLayout(SWT.VERTICAL));
		Label nbHeureLabel = new Label(nbHeureComposite, SWT.NONE);
		nbHeureLabel.setText("Nombre d'heures");
		Text nbHeureTexte = new Text(nbHeureComposite, SWT.NONE);

		nbHeureTexte.pack();
		Composite tableComposite = new Composite(ajoutComposite, SWT.NONE);
		Table table = VueChantier.getTableAllChantier(ajoutComposite);

		table.setLayoutData(new RowData(400, 100));

		Button buttonValide = new Button(ajoutComposite, SWT.CENTER);
		buttonValide.setText("Valider");
		buttonValide.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				if (table.getSelection().length == 1) {
					Affectation affectation = new Affectation(Integer.parseInt(table.getSelection()[0].getText(3)),
							employeId, Double.parseDouble(nbHeureTexte.getText()), "Publié");
					try {
						affectation.insertDatabase();
						ajoutComposite.dispose();

						getVueAffectation().pack();
						getVueAffectation().getParent().pack();
						createVueAffectation(getVueAffectation().getParent());
						System.out.println("faiiit");
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

		buttonValide.pack();
		labelNom.pack();
		nbHeureComposite.pack();
		tableComposite.pack();
		ajoutComposite.pack();
		table.pack();
		this.vueAffectation.pack();
		this.vueAffectation.getParent().pack();

	}
}
