package GUI;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Objects;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import classes.Affectation;

public class VueAffectation {

	private Composite vueAffectation;
	private Composite selection;
	private Composite mainComposite;
	private Composite rightComposite;
	private Composite leftComposite;

	/**
	 * Constructeur utilis� pour constuire la vue Affectation
	 * 
	 * @param composite
	 * @throws SQLException
	 */
	public VueAffectation(Composite composite) throws SQLException {
		this.vueAffectation = new Composite(composite, SWT.NONE);
		this.vueAffectation.setLayout(new RowLayout(SWT.VERTICAL));

		compositeSelection(this.vueAffectation);
		this.mainComposite = new Composite(this.vueAffectation, SWT.NONE);
		this.mainComposite.setLayout(new RowLayout(SWT.HORIZONTAL));

		this.leftComposite = new Composite(this.mainComposite, SWT.NONE);
		this.rightComposite = new Composite(this.mainComposite, SWT.NONE);

		Couleur.setDisplay(composite.getDisplay());

		TabFolder tabFolder = new TabFolder(leftComposite, SWT.BORDER);

		TabItem tabEmploye = new TabItem(tabFolder, SWT.NULL);
		tabEmploye.setText("Affectation par Employ�");

		TabItem tabChantier = new TabItem(tabFolder, SWT.NULL);
		tabChantier.setText("Affectation par Chantier");
		createTableEmployeStats(tabEmploye, this.vueAffectation);
		createTableChantierStats(tabChantier);

		selection.pack();
		mainComposite.pack();
		tabFolder.pack();
		leftComposite.pack();
		rightComposite.pack();
		vueAffectation.pack();
		vueAffectation.getParent().pack();

	}

	/**
	 * Cr�er la table qui contient la liste des employ�s et les stats associ�es �
	 * ces employ�s et l'ajoute � un TableItem
	 * 
	 * @param <type> TablItem </type> onglet de type TabItem qui contient la liste
	 *               des employ�s
	 *
	 * @throws SQLException
	 */

	private void createTableEmployeStats(TabItem tabEmploye, Composite composite) throws SQLException {

		final Table table = new Table(tabEmploye.getParent(),
				SWT.BORDER | SWT.MULTI | SWT.V_SCROLL | SWT.FULL_SELECTION);
		table.setLayoutData(new RowData(900, 800));

		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		String[] titles = { "EmployeId", "Nom", "Prenom", "Nombre d'employ�s diff�rents", "Nombre d'heures total" };

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
			item.setText(3, result.getString("nb_chantier"));
			item.setText(4, result.getString("nb_heure"));

		}

		table.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {

				if (table.getSelectionIndex() != -1) {
					System.out.println(table.getSelection()[0]);
					int employeId = Integer.parseInt(table.getSelection()[0].getText());

					try {
						compositeEmployeAffectationDisplay(employeId);
						composite.pack();
						rightComposite.pack();
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
	 * Cr�er la table qui contient la liste des chantiers et les stats associ�es �
	 * ces chantiers et l'ajoute � un TableItem
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
		String[] titles = { "ChantierId", "Nom", "CA", "Nombre de chantiers diff�rents", "Nombre d'heures total" };

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
		selection = new Composite(composite, SWT.NONE);
		RowLayout rowLayout = new RowLayout();
		rowLayout.marginWidth = 20;
		selection.setLayout(rowLayout);
		selection.setBackground(Couleur.PeterRiver);

		Button boutonCreer = new Button(selection, SWT.CENTER);

		boutonCreer.setText("Cr�er");

		boutonCreer.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {

			}
		});

		boutonCreer.pack();
		selection.pack();
	}

	/**
	 * Ajoute un tableau contenant la liste des chantiers affect�s � un employ� � un
	 * Composite
	 * 
	 * @param employeId <type>int</type> Identifiant de l'employ� affect�s aux
	 *                  chantiers � afficher
	 * @param composite </type> Comppsite </type> Composite qui contient le tableau
	 *                  affichant la liste des Chantiers
	 * @throws SQLException
	 */
	public void compositeEmployeAffectationDisplay(int employeId) throws SQLException {
		ResultSet result = Affectation.getEmployeAffectation(employeId);

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
			System.out.println(item.getText(1));
		}

		for (TableColumn col : columns)
			col.pack();

		table.pack();
		this.rightComposite.pack();
		this.rightComposite.getParent().pack();
		this.rightComposite.getParent().getParent().pack();
		this.rightComposite.getParent().getParent().getParent().pack();
		
		this.leftComposite.pack();
		this.mainComposite.pack();
		
	
	}

	/**
	 * Getter for vueAffectation composite
	 * 
	 * @return <type> Composite </type>
	 */
	public Composite getVueAffectation() {

		return this.vueAffectation;
	}

}
