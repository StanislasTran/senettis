package GUI;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Month;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Objects;

import org.eclipse.swt.*;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

import classes.MABAssignment;
import classes.EmployeeAmortisation;
import classes.EmployeeCost;
import classes.Employee;

import classes.Status;

/**
 * 
 * Create the view to manage employee into the system
 *
 */
public class EmployeeView {

	private static Composite employeeView;
	private static Composite selection;
	private static Composite view;
	private static Composite validation;

	private static Menu menu;

	private static Employee selectedEmployee;
	private static EmployeeAmortisation selectedAmorti;

	private static Table tableGlobaleEmployee;
	private static Table tableCost;
	private static Table tableEmp;
	private static Table tableAmorti;

	private static ArrayList<TableEditor> editors;

	/***
	 * Constructor
	 * 
	 * @param <type>Composite</type>parent composite
	 * @param <type>Display</type>         parent display
	 */
	public EmployeeView(Composite composite, Display display) {

		MyColor.setDisplay(display);

		selectedEmployee = null;
		selectedAmorti = null;

		employeeView = new Composite(composite, SWT.NONE);
		RowLayout rowLayout = new RowLayout();
		rowLayout.type = SWT.VERTICAL;
		employeeView.setLayout(rowLayout);
		employeeView.setBackground(MyColor.bleuClair);

		compositeSelectionBoutons();
		displayEmployeeView();

		employeeView.pack();
		employeeView.getParent().pack();
	}

	/**
	 * create theview employee
	 */
	public static void newVueEmployee() {

		compositeSelectionBoutons();
		displayEmployeeView();

		employeeView.pack();
		employeeView.getParent().pack();
	}

	/**
	 * create selection composite for amortissment
	 * 
	 */
	public static void amortiSelection() {

		if (!Objects.isNull(selection) && !selection.isDisposed()) {
			selection.dispose();
		}

		String s;
		int addSize;
		s = "Gestion des amortissement employés";
		addSize = (595 - 20) / 2;

		selection = new Composite(employeeView, SWT.CENTER);

		FillLayout fillLayout = new FillLayout();
		fillLayout.type = SWT.VERTICAL;
		selection.setLayout(fillLayout);
		selection.setBackground(MyColor.bleuClair);

		Composite selection1 = new Composite(selection, SWT.BORDER);

		FillLayout fillLayout2 = new FillLayout();
		fillLayout2.type = SWT.VERTICAL;
		fillLayout2.marginWidth = addSize;
		selection1.setLayout(fillLayout2);

		Label l1 = new Label(selection1, SWT.NONE);
		l1.setText("");
		l1.setBackground(MyColor.bleuFonce);

		selection1.setBackground(MyColor.bleuFonce);
		Label HeadLabel = new Label(selection1, SWT.TITLE);
		HeadLabel.setText(s);
		Font fontTitle = new Font(HeadLabel.getDisplay(), "Arial", 12, SWT.BOLD);
		HeadLabel.setForeground(MyColor.bleuClair);
		HeadLabel.setFont(fontTitle);
		HeadLabel.setBackground(MyColor.bleuFonce);

		Label l2 = new Label(selection1, SWT.NONE);
		l2.setText("");
		l2.setBackground(MyColor.bleuFonce);

		selection1.pack();

		Composite selection2 = new Composite(selection, SWT.NONE);
		RowLayout rowLayout = new RowLayout();
		rowLayout.marginWidth = 20;
		rowLayout.marginTop = 6;
		selection2.setLayout(rowLayout);
		selection2.setBackground(MyColor.bleuClair);

		RowLayout rl = new RowLayout();
		rl.type = SWT.HORIZONTAL;

		Button boutonCreation = new Button(selection2, SWT.CENTER);
		boutonCreation.setText("Créer");
		boutonCreation.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				// selectedEmploye = null;
				selectedAmorti = null;
				vueCreationA(0);
			}
		});

		if (selectedAmorti != null) {
			Button modifButton = new Button(selection2, SWT.CENTER);
			modifButton.setText("Modifier");
			modifButton.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent arg0) {
					vueCreationA(1);
				}
			});

			Button removeButton = new Button(selection2, SWT.CENTER);
			removeButton.setText("Supprimer");
			removeButton.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent arg0) {
					try {
						EmployeeAmortisation ae = EmployeeAmortisation
								.getAmmortissementEmployeById(selectedAmorti.getAmmortissementEmployeId());
						MessageBox dialog = new MessageBox(employeeView.getShell(),
								SWT.ICON_QUESTION | SWT.YES | SWT.NO);
						dialog.setText("Suppression Amortissement Employe");
						dialog.setMessage("Voulez vous supprimer le coût à amortir de l'employé "
								+ Employee.getEmployeById(ae.getEmployeId()).getSurname() + " "
								+ Employee.getEmployeById(ae.getEmployeId()).getFirstName() + " ?");
						int buttonID = dialog.open();
						switch (buttonID) {
						case SWT.YES:
							ae.setStatus("Archivé");
							ae.updateDatabase();
							updateTableAmorti();
						}
					} catch (Exception e) {

						MessageBox dialog = new MessageBox(employeeView.getShell(), SWT.ICON_ERROR | SWT.OK);
						dialog.setText("Erreur");
						dialog.setMessage("Une erreur est survenue. " + '\n' + e.getMessage());
						dialog.open();
					}
				}
			});
		}

		Button cancelButton = new Button(selection2, SWT.CENTER);
		cancelButton.setText("Retour");
		cancelButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				selectedEmployee = null;
				selectedAmorti = null;
				completeCostView();
			}
		});

		selection2.pack();
		selection.pack();
	}

	/**
	 * create the view for amortissment
	 */
	public static void amortissmentView() {

		amortiSelection();

		/// VUE

		if (!Objects.isNull(view) && !view.isDisposed()) {
			view.dispose();
		}

		view = new Composite(employeeView, SWT.NONE);
		RowLayout rowLayoutH = new RowLayout();
		rowLayoutH.type = SWT.VERTICAL;
		view.setLayout(rowLayoutH);
		view.setBackground(MyColor.bleuClair);

		Composite tables = new Composite(view, SWT.NONE);
		tables.setLayout(new RowLayout(SWT.HORIZONTAL));
		tables.setBackground(MyColor.bleuClair);

		// creation de la table des produits
		Composite compoEmp = new Composite(tables, SWT.NONE);
		compoEmp.setLayout(new RowLayout(SWT.VERTICAL));
		compoEmp.setBackground(MyColor.bleuClair);

		Label emp = new Label(compoEmp, SWT.NONE);
		emp.setText("Choisir un employé :");
		emp.setBackground(MyColor.bleuClair);

		tableEmp = new Table(compoEmp, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL | SWT.FULL_SELECTION);
		tableEmp.setLayoutData(new RowData(300, 380));
		tableEmp.setLinesVisible(true);
		tableEmp.setHeaderVisible(true);
		tableEmp.layout(true, true);

		String[] titles = { "Id", "Nom", "Prenom", "Matricule" };
		for (String title : titles) {
			TableColumn column = new TableColumn(tableEmp, SWT.NONE);
			column.setText(title);
		}

		updateTableEmp();
		compoEmp.pack();

		// creation de la table amorti
		Composite compoAmorti = new Composite(tables, SWT.NONE);
		compoAmorti.setLayout(new RowLayout(SWT.VERTICAL));
		compoAmorti.setBackground(MyColor.bleuClair);

		Label amorti = new Label(compoAmorti, SWT.NONE);
		amorti.setText("Coûts liés à cet employé :");
		amorti.setBackground(MyColor.bleuClair);

		tableAmorti = new Table(compoAmorti, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL | SWT.FULL_SELECTION);
		tableAmorti.setLayoutData(new RowData(500, 380));
		tableAmorti.setLinesVisible(true);
		tableAmorti.setHeaderVisible(true);
		tableAmorti.layout(true, true);

		// on met les noms des colonnes
		String[] titles2 = { "Id", "Début", "Fin", "Montant par mois", "Valeur totale", "Type", "Description" };
		for (String title : titles2) {
			TableColumn column = new TableColumn(tableAmorti, SWT.NONE);
			column.setText(title);
		}

		if (selectedEmployee != null) {

			updateTableAmorti();
			compoAmorti.pack();
		}

		view.pack();
		employeeView.pack();
		employeeView.getParent().pack();
	}

	// i = 0 -> creation , 1 -> modif
	public static void vueCreationA(int i) {

		vueFormulaireAmorti(i);

		if (!Objects.isNull(selection) && !selection.isDisposed()) {
			selection.dispose();
		}
		String s;
		int addSize;
		if (i == 1) {
			s = "Modification d'un coût à amortir";
			addSize = view.getSize().x;
			addSize = (addSize - 235) / 2;
		} else {
			s = "Création d'un coût à amortir";
			addSize = view.getSize().x;
			addSize = (addSize - 210) / 2;
		}

		selection = new Composite(employeeView, SWT.CENTER);

		FillLayout fillLayout = new FillLayout();
		fillLayout.type = SWT.VERTICAL;
		fillLayout.marginWidth = addSize;
		selection.setLayout(fillLayout);

		Label l1 = new Label(selection, SWT.NONE);
		l1.setText("");
		l1.setBackground(MyColor.bleuFonce);

		selection.setBackground(MyColor.bleuFonce);
		Label HeadLabel = new Label(selection, SWT.TITLE);
		HeadLabel.setText(s);
		Font fontTitle = new Font(HeadLabel.getDisplay(), "Arial", 12, SWT.BOLD);
		HeadLabel.setForeground(MyColor.bleuClair);
		HeadLabel.setFont(fontTitle);
		HeadLabel.setBackground(MyColor.bleuFonce);

		Label l2 = new Label(selection, SWT.NONE);
		l2.setText("");
		l2.setBackground(MyColor.bleuFonce);

		selection.pack();

		vueFormulaireAmorti(i);

		employeeView.pack();
		employeeView.getParent().pack();
	}

	/*
	 * create the amortissment form
	 */
	public static void vueFormulaireAmorti(int j) {
		if (!Objects.isNull(view) && !view.isDisposed()) {
			view.dispose();
		}
		view = new Composite(employeeView, SWT.NONE);
		FillLayout fillLayoutH = new FillLayout();
		fillLayoutH.type = SWT.HORIZONTAL;
		view.setLayout(fillLayoutH);

		FillLayout fillLayoutV = new FillLayout();
		fillLayoutV.type = SWT.VERTICAL;
		fillLayoutV.marginWidth = 10;
		Composite colonne1 = new Composite(view, SWT.BORDER);
		Composite colonne2 = new Composite(view, SWT.BORDER);
		colonne1.setBackground(MyColor.bleuClair);
		colonne2.setBackground(MyColor.bleuClair);
		colonne1.setLayout(fillLayoutV);
		colonne2.setLayout(fillLayoutV);

		FillLayout fillLayoutH5 = new FillLayout();
		fillLayoutH5.marginHeight = 30;
		fillLayoutH5.spacing = 5;
		fillLayoutH5.type = SWT.HORIZONTAL;

		Composite compositeEmploye = new Composite(colonne1, SWT.NONE);
		compositeEmploye.setBackground(MyColor.bleuClair);
		compositeEmploye.setLayout(fillLayoutH5);

		Label labelEmploye = new Label(compositeEmploye, SWT.NONE);
		labelEmploye.setText("Employé* : ");
		labelEmploye.setBackground(MyColor.bleuClair);
		// Titre
		Combo employes = new Combo(compositeEmploye, SWT.BORDER);
		System.out.println("je suis dans le formulaire");
		if (j == 1) {
			try {
				if (selectedEmployee.getNameString().length() > 25) {
					employes.setText(selectedEmployee.getNameString().substring(0, 23) + "..." + ";id:"
							+ ((Integer) selectedEmployee.getEmployeId()).toString());
				} else {
					employes.setText(selectedEmployee.getNameString() + " ; id :"
							+ ((Integer) selectedEmployee.getEmployeId()).toString());
				}
			} catch (Exception e1) {
				e1.printStackTrace();
				System.out.println("erreur pour recuperer les employes");
				MessageBox dialog = new MessageBox(employeeView.getShell(), SWT.ICON_ERROR | SWT.OK);
				dialog.setText("Erreur");
				dialog.setMessage("Une erreur est survenue. " + '\n' + e1.getMessage());
				dialog.open();
			}
		} else {
			employes.setText("Selectionner ...");
		}
		try {
			for (Employee e : Employee.getAllEmploye()) {
				if (e.getStatus() == "Publié") {
					if (e.getNameString().length() > 25) {
						employes.add(e.getNameString().substring(0, 23) + "..." + ";id:"
								+ ((Integer) e.getEmployeId()).toString());
					} else {
						employes.add(e.getNameString() + " ; id :" + ((Integer) e.getEmployeId()).toString());
					}
				}
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
			System.out.println("erreur pour recuperer les employes");
			MessageBox dialog = new MessageBox(employeeView.getShell(), SWT.ICON_ERROR | SWT.OK);
			dialog.setText("Erreur");
			dialog.setMessage("Une erreur est survenue. " + '\n' + e1.getMessage());
			dialog.open();
		}

		Composite compositePeriode = new Composite(colonne1, SWT.NONE);
		compositePeriode.setBackground(MyColor.bleuClair);
		compositePeriode.setLayout(fillLayoutH5);

		Label labelPeriode = new Label(compositePeriode, SWT.NONE);
		labelPeriode.setText("Début* : ");
		labelPeriode.setBackground(MyColor.bleuClair);

		Combo periode = new Combo(compositePeriode, SWT.BORDER);
		LocalDate currentdate = LocalDate.now();
		Month month = currentdate.getMonth();
		int year = currentdate.getYear();
		if (j == 0) {
			periode.setText(month.toString() + " " + year);
		} else {
			periode.setText(Month.of(selectedAmorti.getMoisD()) + " " + selectedAmorti.getAnneeD().toString());
		}

		for (int i = 2020; i <= year + 1; i++) {
			for (int i2 = 1; i2 <= 12; i2++) {
				periode.add(Month.of(i2) + " " + i);
			}
		}

		Composite compositeDuree = new Composite(colonne1, SWT.NONE);
		compositeDuree.setBackground(MyColor.bleuClair);
		compositeDuree.setLayout(fillLayoutH5);

		Label labelDuree = new Label(compositeDuree, SWT.NONE);
		labelDuree.setBackground(MyColor.bleuClair);
		labelDuree.setText("Durée (en mois)* : ");

		final Text textDuree = new Text(compositeDuree, SWT.BORDER);
		if (j == 1) {
			textDuree.setText(selectedAmorti.getDuree().toString());
		} else {
			textDuree.setText("");
		}

		Composite compoTest = new Composite(colonne1, SWT.NONE);
		compoTest.setBackground(MyColor.bleuClair);
		compoTest.setLayout(fillLayoutH5);

		Label labelTest = new Label(compoTest, SWT.NONE);
		labelTest.setBackground(MyColor.bleuClair);
		labelTest.setText("");

		Composite compositeValeur = new Composite(colonne2, SWT.NONE);
		compositeValeur.setBackground(MyColor.bleuClair);
		compositeValeur.setLayout(fillLayoutH5);

		Label labelValeur = new Label(compositeValeur, SWT.NONE);
		labelValeur.setBackground(MyColor.bleuClair);
		labelValeur.setText("Montant total* : ");

		final Text textValeur = new Text(compositeValeur, SWT.BORDER);
		if (j == 1) {
			textValeur.setText(selectedAmorti.getValeur().toString());
		} else {
			textValeur.setText("");
		}

		Composite compositeType = new Composite(colonne2, SWT.NONE);
		compositeType.setBackground(MyColor.bleuClair);
		compositeType.setLayout(fillLayoutH5);

		Label labelType = new Label(compositeType, SWT.NONE);
		labelType.setText("Type* : ");
		labelType.setBackground(MyColor.bleuClair);

		Combo type = new Combo(compositeType, SWT.BORDER);
		if (j == 1) {
			type.setText(selectedAmorti.getType());
		} else {
			type.setText("Prêt");
		}
		type.add("Prêt");
		type.add("Saisie Arret");

		// valeur
		Composite compositeDesc = new Composite(colonne2, SWT.NONE);
		compositeDesc.setBackground(MyColor.bleuClair);
		compositeDesc.setLayout(fillLayoutH5);

		Label labelDesc = new Label(compositeDesc, SWT.NONE);
		labelDesc.setBackground(MyColor.bleuClair);
		labelDesc.setText("Description : ");

		final Text textDesc = new Text(compositeDesc, SWT.BORDER);
		if (j == 1) {
			textDesc.setText(selectedAmorti.getDescription());
		} else {
			textDesc.setText("");
		}

		Composite compositeButtons = new Composite(colonne2, SWT.CENTER);
		compositeButtons.setBackground(MyColor.bleuClair);
		compositeButtons.setLayout(fillLayoutH5);

		Button buttonValidation = new Button(compositeButtons, SWT.BACKGROUND);
		buttonValidation.setText("Valider");

		buttonValidation.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent arg0) {

				try {
					String id = employes.getText().split(";")[1].replace(" ", "");
					Integer employeId = Integer.parseInt(id.substring(3, id.length()));

					String[] debut = periode.getText().split(" ");
					System.out.println(debut[0] + " " + debut[1]);

					int moisD = Month.valueOf(debut[0]).getValue();
					int anneeD = Integer.parseInt(debut[1]);

					int duree = Integer.parseInt(textDuree.getText());

					int moisF = moisD + duree;
					int anneeF = anneeD;

					while (moisF > 12) {
						moisF -= 12;
						anneeF += 1;
					}

					Double valeurTotale = Double.parseDouble(textValeur.getText());

					EmployeeAmortisation ae;
					if (selectedAmorti != null) {
						ae = new EmployeeAmortisation(selectedAmorti.getAmmortissementEmployeId(), employeId, moisD,
								anneeD, moisF, anneeF, textDesc.getText(), valeurTotale / duree, duree, valeurTotale,
								type.getText(), "Publié");
						ae.updateDatabase();
						MessageBox dialog = new MessageBox(employeeView.getShell(), SWT.ICON_INFORMATION | SWT.OK);
						dialog.setText("Modification réussie");
						dialog.setMessage("Ce cout a bien été modifié dans la base de données.");
						dialog.open();
					} else {
						ae = new EmployeeAmortisation(employeId, moisD, anneeD, moisF, anneeF, valeurTotale / duree,
								textDesc.getText(), duree, valeurTotale, type.getText(), "Publié");
						ae.insertDatabase();
						MessageBox dialog = new MessageBox(employeeView.getShell(), SWT.ICON_INFORMATION | SWT.OK);
						dialog.setText("Création réussie");
						dialog.setMessage("Ce cout a bien été ajouté dans la base de données.");
						dialog.open();
					}
					amortissmentView();
				} catch (Throwable e) {
					e.printStackTrace();
					System.out.println("erreur dans la creation");
					MessageBox dialog = new MessageBox(employeeView.getShell(), SWT.ICON_ERROR | SWT.OK);
					dialog.setText("Erreur Création");
					dialog.setMessage(
							"Une erreur est survenue lors de la création du cout à amortir. " + '\n' + e.getMessage());
					dialog.open();
				}
			}
		});

		Button buttonAnnulation = new Button(compositeButtons, SWT.BACKGROUND);
		buttonAnnulation.setText("Annuler");
		buttonAnnulation.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				selectedAmorti = null;
				selectedEmployee = null;
				amortissmentView();
			}
		});

		view.pack();
	}

	/**
	 * update the Employee table
	 */
	public static void updateTableEmp() {
		if (!Objects.isNull(tableEmp)) {
			tableEmp.removeAll();
		}

		final TableColumn[] columns = tableEmp.getColumns();

		try {
			for (Employee e : Employee.getAllEmploye()) {

				if (e.getStatus().equals("Publié")) {
					TableItem item = new TableItem(tableEmp, SWT.NONE);
					item.setText(0, ((Integer) e.getEmployeId()).toString());
					item.setText(1, e.getSurname());
					item.setText(2, e.getFirstName());
					item.setText(3, e.getMatricule());
				}
			}
		} catch (SQLException e1) {
			System.out.println("erreur dans la table des couts employes");
			MessageBox dialog = new MessageBox(employeeView.getShell(), SWT.ICON_ERROR | SWT.OK);
			dialog.setText("Erreur");
			dialog.setMessage("Une erreur est survenue. " + '\n' + e1.getMessage());
			dialog.open();
		}

		for (TableColumn col : columns)
			col.pack();

		tableEmp.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				if (tableEmp.getSelectionIndex() != -1) {

					try {
						selectedEmployee = Employee
								.getEmployeById(Integer.parseInt(tableEmp.getSelection()[0].getText(0)));
					} catch (NumberFormatException | SQLException e1) {
						System.out.println("erreur pour recuperer l'employe selectionne");
						MessageBox dialog = new MessageBox(employeeView.getShell(), SWT.ICON_ERROR | SWT.OK);
						dialog.setText("Erreur");
						dialog.setMessage("Une erreur est survenue. " + '\n' + e1.getMessage());
						dialog.open();
					}

					updateTableAmorti();

				} else {

					selectedEmployee = null;
					selectedAmorti = null;
					amortiSelection();
					updateTableAmorti();

				}
			}
		});

		view.pack();
	}

	/**
	 * update amortissment Table
	 */
	public static void updateTableAmorti() {
		if (!Objects.isNull(tableAmorti)) {
			tableAmorti.removeAll();
		}
		if (selectedEmployee == null) {
			return;
		}

		final TableColumn[] columns = tableAmorti.getColumns();

		try {
			for (EmployeeAmortisation a : EmployeeAmortisation.getAllAmmortissementEmploye()) {

				if (a.getStatus().equals("Publié") && selectedEmployee.getEmployeId() == a.getEmployeId()) {
					TableItem item = new TableItem(tableAmorti, SWT.NONE);
					item.setText(0, a.getAmmortissementEmployeId().toString());
					item.setText(1, a.getMoisD().toString() + '/' + a.getAnneeD().toString());
					item.setText(2, a.getMoisF().toString() + '/' + a.getAnneeF().toString());
					item.setText(3, a.getMontantParMois().toString());
					item.setText(4, a.getValeur().toString());
					item.setText(5, a.getType());
					item.setText(6, a.getDescription());
				}
			}
		} catch (SQLException e1) {
			System.out.println("erreur dans la table des couts amorti employes");
			MessageBox dialog = new MessageBox(employeeView.getShell(), SWT.ICON_ERROR | SWT.OK);
			dialog.setText("Erreur");
			dialog.setMessage("Une erreur est survenue. " + '\n' + e1.getMessage());
			dialog.open();
		}

		for (TableColumn col : columns)
			col.pack();

		tableAmorti.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				if (tableAmorti.getSelectionIndex() != -1) {

					try {
						selectedAmorti = EmployeeAmortisation.getAmmortissementEmployeById(
								Integer.parseInt(tableAmorti.getSelection()[0].getText(0)));
					} catch (NumberFormatException | SQLException e1) {
						System.out.println("erreur pour recuperer l'amorti selectionne");
						MessageBox dialog = new MessageBox(employeeView.getShell(), SWT.ICON_ERROR | SWT.OK);
						dialog.setText("Erreur");
						dialog.setMessage("Une erreur est survenue. " + '\n' + e1.getMessage());
						dialog.open();
					}

					amortiSelection();
					doMenu(tableAmorti);
				} else {

					selectedAmorti = null;
					amortiSelection();

					menu.dispose();
					menu = new Menu(employeeView.getShell(), SWT.POP_UP);
					tableAmorti.setMenu(menu);
				}
			}
		});

		tableAmorti.pack();
		view.pack();
		view.layout(true, true);
	}

	/**
	 * create employeeCost composite
	 */

	public static void completeCostView() {

		costSelection();
		costView();

		employeeView.pack();
		employeeView.getParent().pack();

	}

	/**
	 * Create the selection composite for cost
	 */

	public static void costSelection() {
		if (!Objects.isNull(selection) && !selection.isDisposed()) {
			selection.dispose();
		}

		String s;
		int addSize;
		s = "Gestion des coûts employés";
		addSize = (1000 - 150) / 2;

		selection = new Composite(employeeView, SWT.CENTER);

		FillLayout fillLayout = new FillLayout();
		fillLayout.type = SWT.VERTICAL;
		selection.setLayout(fillLayout);
		selection.setBackground(MyColor.bleuClair);

		Composite selection1 = new Composite(selection, SWT.BORDER);

		FillLayout fillLayout2 = new FillLayout();
		fillLayout2.type = SWT.VERTICAL;
		fillLayout2.marginWidth = addSize;
		selection1.setLayout(fillLayout2);

		// juste pour creer un espace
		Label l1 = new Label(selection1, SWT.NONE);
		l1.setText("");
		l1.setBackground(MyColor.bleuFonce);

		selection1.setBackground(MyColor.bleuFonce);
		Label HeadLabel = new Label(selection1, SWT.TITLE);
		HeadLabel.setText(s);
		Font fontTitle = new Font(HeadLabel.getDisplay(), "Arial", 12, SWT.BOLD);
		HeadLabel.setForeground(MyColor.bleuClair);
		HeadLabel.setFont(fontTitle);
		HeadLabel.setBackground(MyColor.bleuFonce);

		// juste pour creer un espace
		Label l2 = new Label(selection1, SWT.NONE);
		l2.setText("");
		l2.setBackground(MyColor.bleuFonce);

		selection1.pack();

		Composite selection2 = new Composite(selection, SWT.NONE);
		RowLayout rowLayout = new RowLayout();
		rowLayout.marginWidth = 20;
		rowLayout.marginTop = 6;
		selection2.setLayout(rowLayout);
		selection2.setBackground(MyColor.bleuClair);

		FillLayout fillLayoutH5 = new FillLayout();
		fillLayoutH5.type = SWT.HORIZONTAL;

		Composite compositePeriode = new Composite(selection2, SWT.NONE);
		compositePeriode.setLayout(fillLayoutH5);
		compositePeriode.setBackground(MyColor.bleuClair);

		Label labelPeriode = new Label(compositePeriode, SWT.NONE);
		labelPeriode.setText("Periode : ");
		labelPeriode.setBackground(MyColor.bleuClair);

		Combo periode = new Combo(compositePeriode, SWT.BORDER);
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
				updateCostTable(periode.getText());
				view.pack();
			}
		});

		Button previousMonthButton = new Button(selection2, SWT.CENTER);
		previousMonthButton.setText("Récupérer les valeurs précédentes");
		previousMonthButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				try {
					String[] monthYear = periode.getText().split(" ");
					Integer month = Month.valueOf(monthYear[0]).getValue();
					Integer year = Integer.parseInt(monthYear[1]);
					// je calcule le mois et l'annee que je dois recuperer dans la base de données
					if (month == 1) {
						month = 12;
						year -= 1;
					} else {
						month -= 1;
					}

					for (EmployeeCost ce : EmployeeCost.getAllCoutEmploye()) {
						if (month == ce.getMois() && (Integer.compare(year, ce.getAnnee()) == 0)) {
							EmployeeCost newCE = ce;
							if (ce.getMois() < 12) {
								newCE.setMois(ce.getMois() + 1);
								newCE.setAnnee(ce.getAnnee());
							} else {
								newCE.setMois(1);
								newCE.setAnnee(ce.getAnnee() + 1);
							}
							try {
								newCE.insertDatabase();
							} catch (Exception e) {
							}
						}
					}
				} catch (SQLException e) {

					MessageBox dialog = new MessageBox(employeeView.getShell(), SWT.ICON_ERROR | SWT.OK);
					dialog.setText("Erreur");
					dialog.setMessage("Une erreur est survenue. " + '\n' + e.getMessage());
					dialog.open();
				}
				updateCostTable(periode.getText());
				view.pack();
			}
		});

		Button manageCostButton = new Button(selection2, SWT.CENTER);
		manageCostButton.setText("Gérer les couts à amortir");
		manageCostButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				selectedEmployee = null;
				selectedAmorti = null;
				amortissmentView();
			}
		});

		Label space = new Label(selection2, SWT.NONE);
		space.setText("                                         ");
		space.setBackground(MyColor.bleuClair);

		Button cancelButton = new Button(selection2, SWT.CENTER);
		cancelButton.setText("    Retour aux employés    ");
		cancelButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				selectedEmployee = null;
				selectedAmorti = null;
				newVueEmployee();
			}
		});

		Button validationButton = new Button(selection2, SWT.CENTER);
		validationButton.setText("     Sauvegarder     ");
		validationButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {

				for (int ligne = 0; ligne < tableCost.getItems().length; ligne++) {

					Integer employeId = Integer.parseInt(tableCost.getItem(ligne).getText(0));
					Boolean empty = true;
					try {

						String[] periode = tableCost.getItem(ligne).getText(4).split("/");

						int mois = Integer.parseInt(periode[0]);
						int annee = Integer.parseInt(periode[1]);

						EmployeeCost ce = new EmployeeCost(employeId, mois, annee, "Publié");
						System.out.println("on a cree ");

						if (!tableCost.getItem(ligne).getText(5).isBlank()) {
							ce.setSalaireNet(Double.parseDouble(tableCost.getItem(ligne).getText(5)));
							empty = false;
						} else {
							ce.setSalaireNet(0.0);
						}

						if (!tableCost.getItem(ligne).getText(6).isBlank()) {
							ce.setSalaireBrut(Double.parseDouble(tableCost.getItem(ligne).getText(6)));
							empty = false;
						} else {
							ce.setSalaireBrut(0.0);
						}

						if (!tableCost.getItem(ligne).getText(7).isBlank()) {
							ce.setNombreHeures(Double.parseDouble(tableCost.getItem(ligne).getText(7)));
							empty = false;
						} else {
							ce.setNombreHeures(0.0);
						}

						if (!tableCost.getItem(ligne).getText(8).isBlank()) {
							ce.setChargesP(Double.parseDouble(tableCost.getItem(ligne).getText(8)));
							empty = false;
						} else {
							ce.setChargesP(0.0);
						}

						if (!tableCost.getItem(ligne).getText(9).isBlank()) {
							ce.setMasseS(Double.parseDouble(tableCost.getItem(ligne).getText(9)));
							empty = false;
						} else {
							ce.setMasseS(0.0);
						}

						if (!tableCost.getItem(ligne).getText(10).isBlank()) {
							ce.setRemboursementTransport(Double.parseDouble(tableCost.getItem(ligne).getText(10)));
							empty = false;
						} else {
							ce.setRemboursementTransport(0.0);
						}

						if (!tableCost.getItem(ligne).getText(11).isBlank()) {
							ce.setRemboursementTelephone(Double.parseDouble(tableCost.getItem(ligne).getText(11)));
							empty = false;
						} else {
							ce.setRemboursementTelephone(0.0);
						}

						if (!tableCost.getItem(ligne).getText(12).isBlank()) {
							ce.setMutuelle(Double.parseDouble(tableCost.getItem(ligne).getText(12)));
							empty = false;
						} else {
							ce.setMutuelle(0.0);
						}

						if (!tableCost.getItem(ligne).getText(13).isBlank()) {
							ce.setPaniers(Double.parseDouble(tableCost.getItem(ligne).getText(13)));
							empty = false;
						} else {
							ce.setPaniers(0.0);
						}

						if (!tableCost.getItem(ligne).getText(14).isBlank()) {
							ce.setPrets(Double.parseDouble(tableCost.getItem(ligne).getText(14)));
							empty = false;
						} else {
							ce.setPrets(0.0);
						}

						if (!tableCost.getItem(ligne).getText(15).isBlank()) {
							ce.setSaisieArret(Double.parseDouble(tableCost.getItem(ligne).getText(15)));
							empty = false;
						} else {
							ce.setSaisieArret(0.0);
						}

						System.out.println("on a tout rempli");

						if (!empty) {// si au moins un champs est renseigné
							try {

								if (ce.updateDatabaseFromEmployeId() == 0) {
									ce.insertDatabase();
								}
							} catch (Exception e) {

								ce.insertDatabase();
							}
						}
					} catch (SQLException e) {

						MessageBox dialog = new MessageBox(employeeView.getShell(), SWT.ICON_ERROR | SWT.OK);
						dialog.setText("Erreur");
						dialog.setMessage("Une erreur est survenue. " + '\n' + e.getMessage());
						dialog.open();
					}
				}

				try {
					String[] moisAnnee = tableCost.getItem(0).getText(4).split("/");
					String periode = Month.of(Integer.parseInt(moisAnnee[0])).toString() + " "
							+ ((Integer) Integer.parseInt(moisAnnee[1])).toString();

					MessageBox dialog = new MessageBox(employeeView.getShell(), SWT.ICON_INFORMATION | SWT.OK);
					dialog.setText("Modification réussie");
					dialog.setMessage("Les couts employé ont bien été modifiés dans la base de données.");
					dialog.open();
					updateCostTable(periode);
					view.pack();
				} catch (Exception e) {

					MessageBox dialog = new MessageBox(employeeView.getShell(), SWT.ICON_ERROR | SWT.OK);
					dialog.setText("Erreur");
					dialog.setMessage("Une erreur est survenue. " + '\n' + e.getMessage());
					dialog.open();
				}
			}
		});

		selection2.pack();
		selection.pack();
	}

	/**
	 * costView composite creation
	 */
	public static void costView() {
		if (!Objects.isNull(view) && !view.isDisposed()) {
			view.dispose();
		}

		view = new Composite(employeeView, SWT.NONE);
		RowLayout rowLayoutH = new RowLayout();
		rowLayoutH.type = SWT.VERTICAL;
		view.setLayout(rowLayoutH);
		view.setBackground(MyColor.bleuClair);

		LocalDate currentdate = LocalDate.now();
		Month month = currentdate.getMonth();
		int year = currentdate.getYear();

		// compoValidation();
		updateCostTable(month.toString() + " " + year);
		view.pack();
	}

	/**
	 * Validation composite creation
	 */
	public static void compoValidation() {
		if (!Objects.isNull(validation) && !validation.isDisposed()) {
			validation.dispose();
		}

		validation = new Composite(view, SWT.NONE);
		RowLayout rowLayout = new RowLayout();
		rowLayout.marginWidth = 22;
		rowLayout.marginTop = 6;
		rowLayout.spacing = 20;
		validation.setLayout(rowLayout);
		validation.setBackground(MyColor.gris);

		Button cancelButton = new Button(validation, SWT.CENTER);
		cancelButton.setText("    Retour aux employés    ");
		cancelButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				selectedEmployee = null;
				selectedAmorti = null;
				newVueEmployee();
			}
		});

		Button validationButton = new Button(validation, SWT.CENTER);
		validationButton.setText("     Sauvegarder     ");
		validationButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {

				for (int ligne = 0; ligne < tableCost.getItems().length; ligne++) {

					Integer employeId = Integer.parseInt(tableCost.getItem(ligne).getText(0));
					Boolean empty = true;
					try {

						String[] periode = tableCost.getItem(ligne).getText(4).split("/");

						int mois = Integer.parseInt(periode[0]);
						int annee = Integer.parseInt(periode[1]);

						EmployeeCost ce = new EmployeeCost(employeId, mois, annee, "Publié");

						if (!tableCost.getItem(ligne).getText(5).isBlank()) {
							ce.setSalaireNet(Double.parseDouble(tableCost.getItem(ligne).getText(5)));
							empty = false;
						} else {
							ce.setSalaireNet(0.0);
						}

						if (!tableCost.getItem(ligne).getText(6).isBlank()) {
							ce.setSalaireBrut(Double.parseDouble(tableCost.getItem(ligne).getText(6)));
							empty = false;
						} else {
							ce.setSalaireBrut(0.0);
						}

						if (!tableCost.getItem(ligne).getText(7).isBlank()) {
							ce.setNombreHeures(Double.parseDouble(tableCost.getItem(ligne).getText(7)));
							empty = false;
						} else {
							ce.setNombreHeures(0.0);
						}

						if (!tableCost.getItem(ligne).getText(8).isBlank()) {
							ce.setChargesP(Double.parseDouble(tableCost.getItem(ligne).getText(8)));
							empty = false;
						} else {
							ce.setChargesP(0.0);
						}

						if (!tableCost.getItem(ligne).getText(9).isBlank()) {
							ce.setMasseS(Double.parseDouble(tableCost.getItem(ligne).getText(9)));
							empty = false;
						} else {
							ce.setMasseS(0.0);
						}

						if (!tableCost.getItem(ligne).getText(10).isBlank()) {
							ce.setRemboursementTransport(Double.parseDouble(tableCost.getItem(ligne).getText(10)));
							empty = false;
						} else {
							ce.setRemboursementTransport(0.0);
						}

						if (!tableCost.getItem(ligne).getText(11).isBlank()) {
							ce.setRemboursementTelephone(Double.parseDouble(tableCost.getItem(ligne).getText(11)));
							empty = false;
						} else {
							ce.setRemboursementTelephone(0.0);
						}

						if (!tableCost.getItem(ligne).getText(12).isBlank()) {
							ce.setMutuelle(Double.parseDouble(tableCost.getItem(ligne).getText(12)));
							empty = false;
						} else {
							ce.setMutuelle(0.0);
						}

						if (!tableCost.getItem(ligne).getText(13).isBlank()) {
							ce.setPaniers(Double.parseDouble(tableCost.getItem(ligne).getText(13)));
							empty = false;
						} else {
							ce.setPaniers(0.0);
						}

						if (!tableCost.getItem(ligne).getText(14).isBlank()) {
							ce.setPrets(Double.parseDouble(tableCost.getItem(ligne).getText(14)));
							empty = false;
						} else {
							ce.setPrets(0.0);
						}

						if (!tableCost.getItem(ligne).getText(15).isBlank()) {
							ce.setSaisieArret(Double.parseDouble(tableCost.getItem(ligne).getText(15)));
							empty = false;
						} else {
							ce.setSaisieArret(0.0);
						}

						if (!empty) {
							try {

								if (ce.updateDatabaseFromEmployeId() == 0) {
									ce.insertDatabase();
								}
							} catch (Exception e) {

								ce.insertDatabase();
							}
						}
					} catch (SQLException e) {

						MessageBox dialog = new MessageBox(employeeView.getShell(), SWT.ICON_ERROR | SWT.OK);
						dialog.setText("Erreur");
						dialog.setMessage("Une erreur est survenue. " + '\n' + e.getMessage());
						dialog.open();
					}
				}

				try {
					String[] moisAnnee = tableCost.getItem(0).getText(4).split("/");
					String periode = Month.of(Integer.parseInt(moisAnnee[0])).toString() + " "
							+ ((Integer) Integer.parseInt(moisAnnee[1])).toString();

					MessageBox dialog = new MessageBox(employeeView.getShell(), SWT.ICON_INFORMATION | SWT.OK);
					dialog.setText("Modification réussie");
					dialog.setMessage("Les couts employé ont bien été modifiés dans la base de données.");
					dialog.open();
					updateCostTable(periode);
					view.pack();
				} catch (Exception e) {

					MessageBox dialog = new MessageBox(employeeView.getShell(), SWT.ICON_ERROR | SWT.OK);
					dialog.setText("Erreur");
					dialog.setMessage("Une erreur est survenue. " + '\n' + e.getMessage());
					dialog.open();
				}
			}
		});
		validation.pack();
	}

	/**
	 * update costTable
	 * 
	 * @param <type>String</type> periode
	 */
	public static void updateCostTable(String periode) {

		if (!Objects.isNull(tableCost) && !tableCost.isDisposed()) {
			tableCost.dispose();
		}

		tableCost = new Table(view, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL | SWT.FULL_SELECTION);
		tableCost.setLayoutData(new RowData(1045, 500));
		tableCost.setLinesVisible(true);
		tableCost.setHeaderVisible(true);

		String[] titles = { "Id", "Nom", "Prenom", "Matricule", "Période", "salaire Net", "salaire Brut",
				"nombreHeures", "charges patronales", "masse salariale", "Transport", "Telephone", "mutuelle",
				"paniers", "prets", "saisie arret" };
		for (String title : titles) {
			TableColumn column = new TableColumn(tableCost, SWT.NONE);
			column.setText(title);
		}

		final TableColumn[] columns = tableCost.getColumns();

		String[] monthYear = periode.split(" ");
		String month = monthYear[0];
		String year = monthYear[1];
		Month monthInt = Month.valueOf(month);

		try {
			for (Employee e : Employee.getAllEmploye()) {
				if (e.getStatus().equals("Publié")) {
					TableItem item = new TableItem(tableCost, SWT.NONE);
					item.setText(0, Integer.toString(e.getEmployeId()));
					item.setText(1, e.getSurname());
					item.setText(2, e.getFirstName());
					item.setText(3, e.getMatricule());
					item.setText(4, Integer.toString(monthInt.getValue()) + '/' + year);

					try {

						EmployeeCost ce = EmployeeCost.getCoutEmployeByEmployeId(e.getEmployeId(), monthInt.getValue(),
								Integer.parseInt(year));
						if (ce.getStatus().equals("Publié")) {
							item.setText(5, ce.getSalaireNet().toString());
							item.setText(6, ce.getSalaireBrut().toString());
							item.setText(7, ce.getNombreHeures().toString());
							item.setText(8, ce.getChargesP().toString());
							item.setText(9, ce.getMasseS().toString());
							item.setText(10, ce.getRemboursementTransport().toString());
							item.setText(11, ce.getRemboursementTelephone().toString());
							item.setText(12, ce.getMutuelle().toString());
							item.setText(13, ce.getPaniers().toString());

							Double pret = 0.0;
							Double saisie = 0.0;
							for (EmployeeAmortisation ae : EmployeeAmortisation
									.getAmmortissementEmployeByEmployeId(e.getEmployeId())) {
								System.out.println("dans la boucle");
								if (ae.getStatus().equals("Publié")) {

									YearMonth debut = YearMonth.of(ae.getAnneeD(), ae.getMoisD());
									YearMonth fin = YearMonth.of(ae.getAnneeF(), ae.getMoisF());
									YearMonth now = YearMonth.of(Integer.parseInt(year), monthInt.getValue());

									if (debut.equals(now) || (debut.isBefore(now) && fin.isAfter(now))) {
										if (ae.getType().equals("Prêt")) {
											pret += ae.getMontantParMois();
										} else {
											saisie += ae.getMontantParMois();
										}
									}
								}
							}
							item.setText(14, pret.toString());
							item.setText(15, saisie.toString());
						}
					} catch (Exception e2) {
						item.setText(14, "" + 0);
						item.setText(15, "" + 0);
					}
				}

			}

			tableCost.layout(true, true);
			view.layout(true, true);

		} catch (SQLException e1) {
			System.out.println("erreur dans la table des couts employes");
			MessageBox dialog = new MessageBox(employeeView.getShell(), SWT.ICON_ERROR | SWT.OK);
			dialog.setText("Erreur");
			dialog.setMessage("Une erreur est survenue. " + '\n' + e1.getMessage());
			dialog.open();
		}

		for (TableColumn col : columns)
			col.pack();

		tableCost.addSelectionListener(getListener());

		view.pack();
	}

	/**
	 * manage table editor
	 * 
	 * @return <type>SelectionAdapter</type> contain editor
	 */
	public static SelectionAdapter getListener() {
		editors = new ArrayList<TableEditor>();

		// CREATION DES EDITOR POUR CHAQUE COLONNE
		final TableEditor editorSN = new TableEditor(tableCost);
		editorSN.horizontalAlignment = SWT.LEFT;
		editorSN.grabHorizontal = true;
		editors.add(editorSN);

		final TableEditor editorSB = new TableEditor(tableCost);
		editorSB.horizontalAlignment = SWT.LEFT;
		editorSB.grabHorizontal = true;
		editors.add(editorSB);

		final TableEditor editorNH = new TableEditor(tableCost);
		editorNH.horizontalAlignment = SWT.LEFT;
		editorNH.grabHorizontal = true;
		editors.add(editorNH);

		final TableEditor editorCP = new TableEditor(tableCost);
		editorCP.horizontalAlignment = SWT.LEFT;
		editorCP.grabHorizontal = true;
		editors.add(editorCP);

		final TableEditor editorTr = new TableEditor(tableCost);
		editorTr.horizontalAlignment = SWT.LEFT;
		editorTr.grabHorizontal = true;
		editors.add(editorTr);

		final TableEditor editorTe = new TableEditor(tableCost);
		editorTe.horizontalAlignment = SWT.LEFT;
		editorTe.grabHorizontal = true;
		editors.add(editorTe);

		final TableEditor editorMu = new TableEditor(tableCost);
		editorMu.horizontalAlignment = SWT.LEFT;
		editorMu.grabHorizontal = true;
		editors.add(editorMu);

		final TableEditor editorPa = new TableEditor(tableCost);
		editorPa.horizontalAlignment = SWT.LEFT;
		editorPa.grabHorizontal = true;
		editors.add(editorPa);

		SelectionAdapter listener = new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				// on dispose les anciens editors
				if (editorSN.getEditor() != null) {
					editorSN.getEditor().dispose();
				}
				if (editorSB.getEditor() != null) {
					editorSB.getEditor().dispose();
				}
				if (editorNH.getEditor() != null) {
					editorNH.getEditor().dispose();
				}
				if (editorCP.getEditor() != null) {
					editorCP.getEditor().dispose();
				}
				if (editorTr.getEditor() != null) {
					editorTr.getEditor().dispose();
				}
				if (editorTe.getEditor() != null) {
					editorTe.getEditor().dispose();
				}
				if (editorMu.getEditor() != null) {
					editorMu.getEditor().dispose();
				}
				if (editorPa.getEditor() != null) {
					editorPa.getEditor().dispose();
				}

				TableItem item = (TableItem) e.item;
				if (item == null) {
					return;
				}

				Text newEditorSN = new Text(tableCost, SWT.NONE);
				newEditorSN.setText(item.getText(5));
				newEditorSN.addModifyListener(new ModifyListener() {
					public void modifyText(ModifyEvent me) {
						if (!(newEditorSN.getText().isEmpty())) {// pour ne pas tester quand l'utilisateur est en train
																	// de modifier
							try {
								item.setText(5, newEditorSN.getText());
							} catch (Exception e) {
								System.out.println("erreur dans la modif");
								MessageBox dialog = new MessageBox(employeeView.getShell(), SWT.ICON_ERROR | SWT.OK);
								dialog.setText("Erreur Editor");
								dialog.setMessage("Saisie invalide." + '\n' + e.getMessage());
								dialog.open();
							}
						}
					}
				});
				editorSN.setEditor(newEditorSN, item, 5);

				Text newEditorSB = new Text(tableCost, SWT.NONE);
				newEditorSB.setText(item.getText(6));
				newEditorSB.addModifyListener(new ModifyListener() {
					public void modifyText(ModifyEvent me) {
						if (!(newEditorSB.getText().isEmpty())) {// pour ne pas tester quand l'utilisateur est en train
																	// de modifier
							try {
								item.setText(6, newEditorSB.getText());

								Double cp;
								if (!item.getText(8).isBlank()) {
									cp = Double.parseDouble(item.getText(8));
								} else {
									cp = 0.0;
								}

								Double sb;
								if (!newEditorSB.getText().isBlank()) {
									sb = Double.parseDouble(newEditorSB.getText());
								} else {
									sb = 0.0;
								}

								item.setText(9, Double.toString(sb + cp));
							} catch (Exception e) {

								MessageBox dialog = new MessageBox(employeeView.getShell(), SWT.ICON_ERROR | SWT.OK);
								dialog.setText("Erreur Editor");
								dialog.setMessage("Saisie invalide." + '\n' + e.getMessage());
								dialog.open();
							}
						}
					}
				});
				editorSB.setEditor(newEditorSB, item, 6);

				Text newEditorNH = new Text(tableCost, SWT.NONE);
				newEditorNH.setText(item.getText(7));
				newEditorNH.addModifyListener(new ModifyListener() {
					public void modifyText(ModifyEvent me) {
						if (!(newEditorNH.getText().isEmpty())) {

							try {
								item.setText(7, newEditorNH.getText());
							} catch (Exception e) {

								MessageBox dialog = new MessageBox(employeeView.getShell(), SWT.ICON_ERROR | SWT.OK);
								dialog.setText("Erreur Editor");
								dialog.setMessage("Saisie invalide." + '\n' + e.getMessage());
								dialog.open();
							}
						}
					}
				});
				editorNH.setEditor(newEditorNH, item, 7);

				Text newEditorCP = new Text(tableCost, SWT.NONE);
				newEditorCP.setText(item.getText(8));
				newEditorCP.addModifyListener(new ModifyListener() {
					public void modifyText(ModifyEvent me) {
						if (!(newEditorCP.getText().isEmpty())) {// pour ne pas tester quand l'utilisateur est en train
																	// de modifier
							try {
								item.setText(8, newEditorCP.getText());

								Double sb;
								if (!item.getText(6).isBlank()) {
									sb = Double.parseDouble(item.getText(6));
								} else {
									sb = 0.0;
								}

								Double cp;
								if (!newEditorCP.getText().isBlank()) {
									cp = Double.parseDouble(newEditorCP.getText());
								} else {
									cp = 0.0;
								}

								item.setText(9, Double.toString(sb + cp));

							} catch (Exception e) {
								System.out.println("erreur dans la modif");
								MessageBox dialog = new MessageBox(employeeView.getShell(), SWT.ICON_ERROR | SWT.OK);
								dialog.setText("Erreur Editor");
								dialog.setMessage("Saisie invalide." + '\n' + e.getMessage());
								dialog.open();
							}
						}
					}
				});
				editorCP.setEditor(newEditorCP, item, 8);

				Text newEditorTr = new Text(tableCost, SWT.NONE);
				newEditorTr.setText(item.getText(10));
				newEditorTr.addModifyListener(new ModifyListener() {
					public void modifyText(ModifyEvent me) {
						if (!(newEditorTr.getText().isEmpty())) {// pour ne pas tester quand l'utilisateur est en train
																	// de modifier
							try {
								item.setText(10, newEditorTr.getText());
							} catch (Exception e) {
								System.out.println("erreur dans la modif");
								MessageBox dialog = new MessageBox(employeeView.getShell(), SWT.ICON_ERROR | SWT.OK);
								dialog.setText("Erreur Editor");
								dialog.setMessage("Saisie invalide." + '\n' + e.getMessage());
								dialog.open();
							}
						}
					}
				});
				editorTr.setEditor(newEditorTr, item, 10);

				Text newEditorTe = new Text(tableCost, SWT.NONE);
				newEditorTe.setText(item.getText(11));
				newEditorTe.addModifyListener(new ModifyListener() {
					public void modifyText(ModifyEvent me) {
						if (!(newEditorTe.getText().isEmpty())) {// pour ne pas tester quand l'utilisateur est en train
																	// de modifier
							try {
								item.setText(11, newEditorTe.getText());
							} catch (Exception e) {
								System.out.println("erreur dans la modif");
								MessageBox dialog = new MessageBox(employeeView.getShell(), SWT.ICON_ERROR | SWT.OK);
								dialog.setText("Erreur Editor");
								dialog.setMessage("Saisie invalide." + '\n' + e.getMessage());
								dialog.open();
							}
						}
					}
				});
				editorTe.setEditor(newEditorTe, item, 11);

				Text newEditorMu = new Text(tableCost, SWT.NONE);
				newEditorMu.setText(item.getText(12));
				newEditorMu.addModifyListener(new ModifyListener() {
					public void modifyText(ModifyEvent me) {
						if (!(newEditorMu.getText().isEmpty())) {// pour ne pas tester quand l'utilisateur est en train
																	// de modifier
							try {
								item.setText(12, newEditorMu.getText());
							} catch (Exception e) {
								System.out.println("erreur dans la modif");
								MessageBox dialog = new MessageBox(employeeView.getShell(), SWT.ICON_ERROR | SWT.OK);
								dialog.setText("Erreur Editor");
								dialog.setMessage("Saisie invalide." + '\n' + e.getMessage());
								dialog.open();
							}
						}
					}
				});
				editorMu.setEditor(newEditorMu, item, 12);

				Text newEditorPa = new Text(tableCost, SWT.NONE);
				newEditorPa.setText(item.getText(13));
				newEditorPa.addModifyListener(new ModifyListener() {
					public void modifyText(ModifyEvent me) {
						if (!(newEditorPa.getText().isEmpty())) {// pour ne pas tester quand l'utilisateur est en train
																	// de modifier
							try {
								item.setText(13, newEditorPa.getText());
							} catch (Exception e) {
								System.out.println("erreur dans la modif");
								MessageBox dialog = new MessageBox(employeeView.getShell(), SWT.ICON_ERROR | SWT.OK);
								dialog.setText("Erreur Editor");
								dialog.setMessage("Saisie invalide." + '\n' + e.getMessage());
								dialog.open();
							}
						}
					}
				});
				editorPa.setEditor(newEditorPa, item, 13);

				if (tableCost.getSelectionIndex() != -1) {// on a cliquer sur une ligne non vide
					doMenu(tableCost);

				} else {

					menu.dispose();
					menu = new Menu(employeeView.getShell(), SWT.POP_UP);
					tableGlobaleEmployee.setMenu(menu);

				}
			}
		};

		return listener;
	}

	/**
	 * Recover data from former who leave and come back into the companny
	 */
	public static void vueRecupEmploye() {
		if (!Objects.isNull(selection) && !selection.isDisposed()) {
			selection.dispose();
		}
		selection = new Composite(employeeView, SWT.CENTER);

		FillLayout fillLayout = new FillLayout();
		fillLayout.type = SWT.VERTICAL;
		fillLayout.marginWidth = 26;
		selection.setLayout(fillLayout);

		Label l1 = new Label(selection, SWT.NONE);
		l1.setText("");
		l1.setBackground(MyColor.bleuFonce);

		selection.setBackground(MyColor.bleuFonce);
		Label HeadLabel = new Label(selection, SWT.TITLE);
		HeadLabel.setText("Récupération d'un Employe");
		Font fontTitle = new Font(HeadLabel.getDisplay(), "Arial", 12, SWT.BOLD);
		HeadLabel.setForeground(MyColor.bleuClair);
		HeadLabel.setFont(fontTitle);
		HeadLabel.setBackground(MyColor.bleuFonce);

		Label l2 = new Label(selection, SWT.NONE);
		l2.setText("");
		l2.setBackground(MyColor.bleuFonce);

		selection.pack();

		if (!Objects.isNull(view) && !view.isDisposed()) {
			view.dispose();
		}

		view = new Composite(employeeView, SWT.NONE);
		FillLayout fillLayoutH = new FillLayout();
		fillLayoutH.type = SWT.HORIZONTAL;
		view.setLayout(fillLayoutH);

		FillLayout fillLayoutV = new FillLayout();
		fillLayoutV.type = SWT.VERTICAL;
		fillLayoutV.marginWidth = 10;
		Composite colonne1 = new Composite(view, SWT.BORDER);
		colonne1.setBackground(MyColor.bleuClair);
		colonne1.setLayout(fillLayoutV);

		FillLayout fillLayoutH5 = new FillLayout();
		fillLayoutH5.marginHeight = 30;
		fillLayoutH5.spacing = 5;
		fillLayoutH5.type = SWT.HORIZONTAL;

		Composite compositeTitre = new Composite(colonne1, SWT.NONE);
		compositeTitre.setBackground(MyColor.bleuClair);
		compositeTitre.setLayout(fillLayoutH5);

		Label labelTitre = new Label(compositeTitre, SWT.NONE);
		labelTitre.setBackground(MyColor.bleuClair);
		labelTitre.setText("Merci de renseigner un matricule ou un nom\net un prénom.");

		Composite compositeNom = new Composite(colonne1, SWT.NONE);
		compositeNom.setBackground(MyColor.bleuClair);
		compositeNom.setLayout(fillLayoutH5);

		Label labelNom = new Label(compositeNom, SWT.NONE);
		labelNom.setBackground(MyColor.bleuClair);
		labelNom.setText("Nom :                            ");

		final Text textNom = new Text(compositeNom, SWT.BORDER);
		textNom.setText("");

		Composite compositePrenom = new Composite(colonne1, SWT.NONE);
		compositePrenom.setBackground(MyColor.bleuClair);
		compositePrenom.setLayout(fillLayoutH5);

		Label labelPrenom = new Label(compositePrenom, SWT.NONE);
		labelPrenom.setBackground(MyColor.bleuClair);
		labelPrenom.setText("Prénom : ");

		final Text textPrenom = new Text(compositePrenom, SWT.BORDER);
		textPrenom.setText("");

		Composite compositeMatricule = new Composite(colonne1, SWT.NONE);
		compositeMatricule.setBackground(MyColor.bleuClair);
		compositeMatricule.setLayout(fillLayoutH5);

		Label labelMatricule = new Label(compositeMatricule, SWT.NONE);
		labelMatricule.setBackground(MyColor.bleuClair);
		labelMatricule.setText("Matricule : ");

		final Text textMatricule = new Text(compositeMatricule, SWT.BORDER);
		textMatricule.setText("");

		Composite compositeBoutons = new Composite(colonne1, SWT.CENTER);
		compositeBoutons.setBackground(MyColor.bleuClair);
		compositeBoutons.setLayout(fillLayoutH5);

		Button buttonValidation = new Button(compositeBoutons, SWT.BACKGROUND);
		buttonValidation.setText("Valider");
		buttonValidation.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent arg0) {

				try {
					if (!textMatricule.getText().isBlank()) {
						Employee.retrieveByMatricule(textMatricule.getText());
					} else if (!textNom.getText().isBlank() && !textPrenom.getText().isBlank()) {
						Employee.retrieveBySurnameFirstName(textNom.getText(), textPrenom.getText());
					} else {
						throw new Error("Merci d'indiquer un nom et pr�nom ou un matricule.");
					}
					System.out.println("on a recuperer employe !!");
					MessageBox dialog = new MessageBox(employeeView.getShell(), SWT.ICON_INFORMATION | SWT.OK);
					dialog.setText("Récupération réussie");
					dialog.setMessage("L'employé a été rajouté à la base de donn�es.");
					dialog.open();
					newVueEmployee();
				} catch (Throwable e) {
					e.printStackTrace();
					System.out.println("erreur dans la recup");
					MessageBox dialog = new MessageBox(employeeView.getShell(), SWT.ICON_ERROR | SWT.OK);
					dialog.setText("Erreur R�cup�ration");
					dialog.setMessage("Une erreur est survenue. " + '\n' + e.getMessage());
					dialog.open();
				}
			}
		});

		Button cancelButton = new Button(compositeBoutons, SWT.BACKGROUND);
		cancelButton.setText("Annuler");
		cancelButton.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				selectedEmployee = null;
				selectedAmorti = null;
				newVueEmployee();
			}
		});

		view.pack();
		employeeView.pack();
		employeeView.getParent().pack();
	}

	/***
	 * display creation form
	 */
	public static void vueEmployeFormulaire(int i) {
		formulaireEmploye(i);
		titreEmploye(i);
		formulaireEmploye(i);

		employeeView.pack();
		employeeView.getParent().pack();

	}

	/***
	 * add header into selection
	 * 
	 */
	public static void titreEmploye(int i) {
		if (!Objects.isNull(selection) && !selection.isDisposed()) {
			selection.dispose();
		}

		String s;
		int addSize;
		if (i == 1) {
			s = "Modification d'un employé";
			addSize = view.getSize().x;
			addSize = (addSize - 198) / 2;
		} else {
			s = "Creation d'un Employe";
			addSize = view.getSize().x;
			addSize = (addSize - 174) / 2;
		}

		selection = new Composite(employeeView, SWT.CENTER);

		FillLayout fillLayout = new FillLayout();
		fillLayout.type = SWT.VERTICAL;
		fillLayout.marginWidth = addSize;
		selection.setLayout(fillLayout);

		// juste pour creer un espace
		Label l1 = new Label(selection, SWT.NONE);
		l1.setText("");
		l1.setBackground(MyColor.bleuFonce);

		selection.setBackground(MyColor.bleuFonce);
		Label HeadLabel = new Label(selection, SWT.TITLE);
		HeadLabel.setText(s);
		Font fontTitle = new Font(HeadLabel.getDisplay(), "Arial", 12, SWT.BOLD);
		HeadLabel.setForeground(MyColor.bleuClair);
		HeadLabel.setFont(fontTitle);
		HeadLabel.setBackground(MyColor.bleuFonce);

		// juste pour creer un espace
		Label l2 = new Label(selection, SWT.NONE);
		l2.setText("");
		l2.setBackground(MyColor.bleuFonce);

		selection.pack();
	}

	/***
	 * create the creation form
	 */
	public static void formulaireEmploye(int i) {
		if (!Objects.isNull(view) && !view.isDisposed()) {
			view.dispose();
		}
		view = new Composite(employeeView, SWT.NONE);
		FillLayout fillLayoutH = new FillLayout();
		fillLayoutH.type = SWT.HORIZONTAL;
		view.setLayout(fillLayoutH);

		showFormulaire(i);

		view.pack();
	}

	/***
	 * display modification or creation form type donne en argument
	 * 
	 * @param <type>int</type> i : 1 pour une creation, 2 pour une modification les
	 *                         autres arguments correspondent aux valeurs a afficher
	 * 
	 */
	public static void showFormulaire(int i) {
		String titre, nom, prenom, mail, telephone, numeroMatricule, pointure, taille, dateArrivee;
		if (i == 0) {
			titre = "";
			nom = "";
			prenom = "";
			mail = "";
			telephone = "";
			numeroMatricule = "";
			pointure = "";
			taille = "";
			dateArrivee = "";
		} else {
			titre = selectedEmployee.getTitle();
			nom = selectedEmployee.getSurname();
			prenom = selectedEmployee.getFirstName();
			mail = selectedEmployee.getMail();
			telephone = selectedEmployee.getPhone();
			numeroMatricule = selectedEmployee.getMatricule().toString();
			pointure = selectedEmployee.getShoeSize();
			taille = selectedEmployee.getSize();
			dateArrivee = selectedEmployee.getArrivalDate();
		}

		FillLayout fillLayoutV = new FillLayout();
		fillLayoutV.type = SWT.VERTICAL;
		fillLayoutV.marginWidth = 10;
		Composite column1 = new Composite(view, SWT.BORDER);
		Composite column2 = new Composite(view, SWT.BORDER);
		column1.setBackground(MyColor.bleuClair);
		column2.setBackground(MyColor.bleuClair);
		column1.setLayout(fillLayoutV);
		column2.setLayout(fillLayoutV);

		FillLayout fillLayoutH5 = new FillLayout();
		fillLayoutH5.marginHeight = 30;
		fillLayoutH5.spacing = 5;
		fillLayoutH5.type = SWT.HORIZONTAL;

		// title
		Composite compositeTitle = new Composite(column1, SWT.NONE);
		compositeTitle.setBackground(MyColor.bleuClair);
		compositeTitle.setLayout(fillLayoutH5);

		Label labelTitle = new Label(compositeTitle, SWT.NONE);
		labelTitle.setBackground(MyColor.bleuClair);
		labelTitle.setText("Titre : ");

		Combo comboTitle = new Combo(compositeTitle, SWT.BORDER);
		if (titre.equals("M")) {
			comboTitle.setText("M");
		} else if (titre.equals("Mme")) {
			comboTitle.setText("Mme");
		} else {
			comboTitle.setText("Selectionner ...");
		}
		comboTitle.add("M");
		comboTitle.add("Mme");

		Composite compositeName = new Composite(column1, SWT.NONE);
		compositeName.setBackground(MyColor.bleuClair);
		compositeName.setLayout(fillLayoutH5);

		Label labelName = new Label(compositeName, SWT.NONE);
		labelName.setBackground(MyColor.bleuClair);
		labelName.setText("Nom* : ");

		final Text textName = new Text(compositeName, SWT.BORDER);
		textName.setText(nom);

		Composite compositeSurname = new Composite(column1, SWT.NONE);
		compositeSurname.setBackground(MyColor.bleuClair);
		compositeSurname.setLayout(fillLayoutH5);

		Label surnameLabel = new Label(compositeSurname, SWT.NONE);
		surnameLabel.setBackground(MyColor.bleuClair);
		surnameLabel.setText("Prenom* : ");

		final Text surnameText = new Text(compositeSurname, SWT.BORDER);
		surnameText.setText(prenom);

		// Mail
		Composite compositeMail = new Composite(column1, SWT.NONE);
		compositeMail.setBackground(MyColor.bleuClair);
		compositeMail.setLayout(fillLayoutH5);

		Label labelMail = new Label(compositeMail, SWT.NONE);
		labelMail.setBackground(MyColor.bleuClair);
		labelMail.setText("Email : ");

		final Text textMail = new Text(compositeMail, SWT.BORDER);
		textMail.setText(mail);

		// Telephone
		Composite compositePhone = new Composite(column1, SWT.NONE);
		compositePhone.setBackground(MyColor.bleuClair);
		compositePhone.setLayout(fillLayoutH5);

		Label phoneLabel = new Label(compositePhone, SWT.NONE);
		phoneLabel.setBackground(MyColor.bleuClair);
		phoneLabel.setText("Telephone : ");

		final Text phoneText = new Text(compositePhone, SWT.BORDER);
		phoneText.setText(telephone);

		// numeroMatricule
		Composite compositeNumeroMatricule = new Composite(column2, SWT.NONE);
		compositeNumeroMatricule.setBackground(MyColor.bleuClair);
		compositeNumeroMatricule.setLayout(fillLayoutH5);

		Label labelNumeroMatricule = new Label(compositeNumeroMatricule, SWT.NONE);
		labelNumeroMatricule.setBackground(MyColor.bleuClair);
		labelNumeroMatricule.setText("Numero de Matricule* : ");

		final Text textNumeroMatricule = new Text(compositeNumeroMatricule, SWT.BORDER);
		textNumeroMatricule.setText(numeroMatricule);

		// Pointure
		Composite compositePointure = new Composite(column2, SWT.NONE);
		compositePointure.setBackground(MyColor.bleuClair);
		compositePointure.setLayout(fillLayoutH5);

		Label labelPointure = new Label(compositePointure, SWT.NONE);
		labelPointure.setBackground(MyColor.bleuClair);
		labelPointure.setText("Pointure : ");

		final Text textPointure = new Text(compositePointure, SWT.BORDER);
		textPointure.setText(pointure);

		// Taille
		Composite compositeTaille = new Composite(column2, SWT.NONE);
		compositeTaille.setBackground(MyColor.bleuClair);
		compositeTaille.setLayout(fillLayoutH5);

		Label labelTaille = new Label(compositeTaille, SWT.NONE);
		labelTaille.setBackground(MyColor.bleuClair);
		labelTaille.setText("Taille : ");

		final Text textTaille = new Text(compositeTaille, SWT.BORDER);
		textTaille.setText(taille);

		// DateArrivee
		Composite compositeDateArrivee = new Composite(column2, SWT.NONE);
		compositeDateArrivee.setBackground(MyColor.bleuClair);
		compositeDateArrivee.setLayout(fillLayoutH5);

		Label labelDateArrivee = new Label(compositeDateArrivee, SWT.NONE);
		labelDateArrivee.setBackground(MyColor.bleuClair);
		labelDateArrivee.setText("Date d'arrivée : ");

		final Text textDateArrivee = new Text(compositeDateArrivee, SWT.BORDER);
		textDateArrivee.setText(dateArrivee);

		// Boutons
		Composite compositeBoutons = new Composite(column2, SWT.CENTER);
		compositeBoutons.setBackground(MyColor.bleuClair);
		compositeBoutons.setLayout(fillLayoutH5);

		Button validationButton = new Button(compositeBoutons, SWT.BACKGROUND);
		validationButton.setText("Valider");

		validationButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {

				try {
					validerEmploye(comboTitle.getText().trim(), textName.getText().trim(), surnameText.getText().trim(),
							textNumeroMatricule.getText().trim(), textMail.getText().trim(), phoneText.getText().trim(),
							textPointure.getText().trim(), textTaille.getText().trim(),
							textDateArrivee.getText().trim());
				} catch (Throwable e) {
					e.printStackTrace();

					MessageBox dialog = new MessageBox(employeeView.getShell(), SWT.ICON_ERROR | SWT.OK);
					dialog.setText("Erreur Création/Modification");
					dialog.setMessage("Une erreur est survenue lors de la création/modification de l'employé. " + '\n'
							+ e.getMessage());
					dialog.open();
				}
			}
		});

		Button cancelButton = new Button(compositeBoutons, SWT.BACKGROUND);
		cancelButton.setText("Annuler");
		cancelButton.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				selectedEmployee = null;
				selectedAmorti = null;
				newVueEmployee();
			}
		});

	}

	/***
	 * create an employee
	 * 
	 * @param titre
	 * @param textNom
	 * @param textPrenom
	 * @param textNumeroMatricule
	 * @param textMail
	 * @param textTelephone
	 * @param textPointure
	 * @param textTaille
	 * @param textDateArrivee
	 * @param textNombreHeures
	 * @param textRemboursementTransport
	 * @param textRemboursementTelephone
	 * @param textSalaire
	 */
	public static void validerEmploye(String titre, String textNom, String textPrenom, String textNumeroMatricule,
			String textMail, String textTelephone, String textPointure, String textTaille, String textDateArrivee) {

		if (textNom.isEmpty() || textPrenom.isEmpty() || textNumeroMatricule.isEmpty()) {
			throw new Error("Merci d'indiquer au moins le nom, le prénom et le numéro de matricule de l'employé.");
		}

		Employee employe = new Employee(titre, textNom, textPrenom, textNumeroMatricule);
		employe.setStatus("Publié");

		if (!(textMail.isEmpty())) {
			employe.setMail(textMail);
		}
		if (!(textTelephone.isEmpty())) {
			employe.setTelephone(textTelephone);
		}
		if (!(textPointure.isEmpty())) {
			employe.setShoeSize(textPointure);
		}
		if (!(textTaille.isEmpty())) {
			employe.setSize(textTaille);
		}

		// date
		if (!(textDateArrivee.isEmpty())) {
			employe.setArrivalDate(textDateArrivee);

		}

		try {
			String t, texte;
			if (selectedEmployee != null) {

				employe.setEmployeId(selectedEmployee.getEmployeId());
				employe.updateDatabase();
				t = "Modification réussie";
				texte = "L'employé a bien été modifié dans la base de données.";
			} else {
				employe.insertDatabase();
				t = "Création réussie";
				texte = "L'employé a bien été ajouté à la base de données.";
			}

			MessageBox dialog = new MessageBox(employeeView.getShell(), SWT.ICON_INFORMATION | SWT.OK);
			dialog.setText(t);
			dialog.setMessage(texte);
			dialog.open();
			newVueEmployee();
		} catch (SQLException e) {

			MessageBox dialog = new MessageBox(employeeView.getShell(), SWT.ICON_ERROR | SWT.OK);
			dialog.setText("Erreur Création");
			dialog.setMessage(
					"Une erreur est survenue lors de la création/modification de l'employé. " + '\n' + e.getMessage());
			dialog.open();
		}
	}

	/**
	 * create button into selection Button
	 */
	public static void compositeSelectionBoutons() {
		if (!Objects.isNull(selection) && !selection.isDisposed()) {
			selection.dispose();
		}

		String s;
		int addSize;
		s = "Gestion des employés";
		addSize = (847 - 145) / 2;

		selection = new Composite(employeeView, SWT.CENTER);

		FillLayout fillLayout = new FillLayout();
		fillLayout.type = SWT.VERTICAL;
		selection.setLayout(fillLayout);
		selection.setBackground(MyColor.bleuClair);

		Composite selection1 = new Composite(selection, SWT.BORDER);

		FillLayout fillLayout2 = new FillLayout();
		fillLayout2.type = SWT.VERTICAL;
		fillLayout2.marginWidth = addSize;
		selection1.setLayout(fillLayout2);

		Label l1 = new Label(selection1, SWT.NONE);
		l1.setText("");
		l1.setBackground(MyColor.bleuFonce);

		selection1.setBackground(MyColor.bleuFonce);
		Label HeadLabel = new Label(selection1, SWT.TITLE);
		HeadLabel.setText(s);
		Font fontTitle = new Font(HeadLabel.getDisplay(), "Arial", 12, SWT.BOLD);
		HeadLabel.setForeground(MyColor.bleuClair);
		HeadLabel.setFont(fontTitle);
		HeadLabel.setBackground(MyColor.bleuFonce);

		Label l2 = new Label(selection1, SWT.NONE);
		l2.setText("");
		l2.setBackground(MyColor.bleuFonce);

		selection1.pack();

		Composite selection2 = new Composite(selection, SWT.NONE);
		RowLayout rowLayout = new RowLayout();
		rowLayout.marginWidth = 20;
		rowLayout.marginTop = 6;
		selection2.setLayout(rowLayout);
		selection2.setBackground(MyColor.bleuClair);

		Button boutonCreer = new Button(selection2, SWT.CENTER);
		boutonCreer.setText("Créer");
		boutonCreer.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				selectedEmployee = null;
				vueEmployeFormulaire(0);
			}
		});

		if (selectedEmployee != null) {

			Button boutonModifier = new Button(selection2, SWT.CENTER);
			boutonModifier.setText("Modifier");
			boutonModifier.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent arg0) {
					vueEmployeFormulaire(1);
				}
			});

			Button boutonSupprimer = new Button(selection2, SWT.CENTER);
			boutonSupprimer.setText("Supprimer");
			boutonSupprimer.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent arg0) {
					try {
						if (selectedEmployee == null) {
							throw new Error("selectedEmploye est vide");
						}
						Employee e = Employee.getEmployeById(selectedEmployee.getEmployeId());

						MessageBox dialog = new MessageBox(employeeView.getShell(),
								SWT.ICON_QUESTION | SWT.YES | SWT.NO);
						dialog.setText("Suppression Employe");
						dialog.setMessage("Voulez vous supprimer l'employé " + e.getSurname() + " " + e.getFirstName()
								+ " ?\nToutes les affectations et les couts liés à cet employé seront supprimés.");
						int buttonID = dialog.open();

						switch (buttonID) {
						case SWT.YES:
							suppEmploye();
						}

					} catch (NumberFormatException | SQLException e) {

						MessageBox dialog = new MessageBox(employeeView.getShell(), SWT.ICON_ERROR | SWT.OK);
						dialog.setText("Erreur Supression");
						dialog.setMessage(
								"Une erreur est survenue lors de la supression de l'employé. " + '\n' + e.getMessage());
						dialog.open();

					}

				}
			});

			if (!tableGlobaleEmployee.isDisposed()) {

				Button boutonAnciennete = new Button(selection2, SWT.CENTER);
				boutonAnciennete.setText("Mettre à jour l'ancienneté prise en compte");
				boutonAnciennete.addSelectionListener(new SelectionAdapter() {
					@Override
					public void widgetSelected(SelectionEvent arg0) {
						if (selectedEmployee == null) {
							throw new Error("selectedEmploye est vide");
						}

						tableGlobaleEmployee.getSelection()[0].setForeground(9, MyColor.noir);
						try {
							int employeeId = selectedEmployee.getEmployeId();
							int newAnciennetePC = Employee.getEmployeById(employeeId).getComputeSeniority();
							Employee.updateAnciennete(Employee.getEmployeById(employeeId).getComputeSeniority(),
									employeeId);
							tableGlobaleEmployee.getSelection()[0].setForeground(9, MyColor.noir);
							tableGlobaleEmployee.getSelection()[0].setForeground(11, MyColor.noir);
							tableGlobaleEmployee.getSelection()[0].setText(11, "" + newAnciennetePC);

						} catch (SQLException e) {
							MessageBox msgBox = new MessageBox(view.getShell(), SWT.ERROR);
							msgBox.setMessage("Erreur Base de donnée");
							msgBox.setText("erreur de liée à la base de données : \n" + e.getMessage());
							msgBox.open();
							e.printStackTrace();
						}
					}
				});
			}

		}

		Button costButton = new Button(selection2, SWT.CENTER);
		costButton.setText("Gérer les couts employés");
		costButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				selectedEmployee = null;
				completeCostView();
			}
		});

		Button boutonAncienEmploye = new Button(selection2, SWT.CENTER);
		boutonAncienEmploye.setText("Récupérer ancien employé");
		boutonAncienEmploye.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				vueRecupEmploye();
			}

		});
		selection2.pack();

		selection.pack();
	}

	/**
	 * display Enployee view
	 */
	public static void displayEmployeeView() {
		if (!Objects.isNull(view) && !view.isDisposed()) {
			view.dispose();
		}

		RowLayout rowLayoutV = new RowLayout();
		rowLayoutV.type = SWT.VERTICAL;

		view = new Composite(employeeView, SWT.NONE);
		view.setLayout(rowLayoutV);
		view.setBackground(MyColor.bleuClair);

		tableGlobaleEmployee = new Table(view, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL | SWT.FULL_SELECTION);
		tableGlobaleEmployee.setLayoutData(new RowData(847, 450));
		tableGlobaleEmployee.setLinesVisible(true);
		tableGlobaleEmployee.setHeaderVisible(true);

		String[] titles = { "Titre  ", "Nom          ", "Prenom       ", "Email                                    ",
				"Téléphone    ", "Matricule", "Pointure", "Taille", "Date d'ancienneté", "Ancienneté", "Id DB",
				"Ancienneté prise en compte" };
		for (String title : titles) {
			TableColumn column = new TableColumn(tableGlobaleEmployee, SWT.NONE);
			column.setText(title);
		}

		final TableColumn[] columns = tableGlobaleEmployee.getColumns();
		for (TableColumn col : columns)
			col.pack();

		tableGlobaleEmployee.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				if (tableGlobaleEmployee.getSelectionIndex() != -1) {// on a cliquer sur une ligne non vide
					try {
						selectedEmployee = Employee
								.getEmployeById(Integer.parseInt(tableGlobaleEmployee.getSelection()[0].getText(10)));
					} catch (NumberFormatException | SQLException e1) {

						MessageBox dialog = new MessageBox(employeeView.getShell(), SWT.ICON_ERROR | SWT.OK);
						dialog.setText("Erreur");
						dialog.setMessage("Une erreur est survenue. " + '\n' + e1.getMessage());
						dialog.open();
					}

					compositeSelectionBoutons();

					doMenu(tableGlobaleEmployee);

				} else {

					selectedEmployee = null;

					menu.dispose();
					menu = new Menu(employeeView.getShell(), SWT.POP_UP);
					tableGlobaleEmployee.setMenu(menu);

					compositeSelectionBoutons();
				}
			}
		});

		updateTable();

		view.pack();

	}

	/***
	 * 
	 * fill the table with employee
	 * 
	 * 
	 */
	public static void updateTable() {

		tableGlobaleEmployee.removeAll();

		try {
			for (Employee e : Employee.getAllEmploye()) {

				if (e.getStatus().contentEquals("Publié")) {
					TableItem item = new TableItem(tableGlobaleEmployee, SWT.NONE);
					item.setText(0, e.getTitle());
					item.setText(1, e.getSurname());
					item.setText(2, e.getFirstName());
					item.setText(3, e.getMail());
					item.setText(4, e.getPhone());
					item.setText(5, e.getMatricule());
					item.setText(6, e.getShoeSize());
					item.setText(7, e.getSize());
					item.setText(10, Integer.toString(e.getEmployeId()));

					if (e.getArrivalDate() != null && !e.getArrivalDate().equals("")) {
						item.setText(8, e.getArrivalDate());

						Integer anciennete = e.getComputeSeniority();
						if (anciennete == 0) {
							item.setText(9, "moins d'un an");
						} else if (anciennete == 1) {
							item.setText(9, anciennete + " an");
						} else {
							item.setText(9, anciennete + " ans");
						}

						item.setText(11, "" + e.getSeniority());
						boolean mustBeRed = anciennete >= 3 && e.getSeniority() < 3;

						if (mustBeRed) {
							item.setForeground(9, MyColor.rouge);
							item.setForeground(11, MyColor.rouge);

						}

					}

				}
			}
		} catch (SQLException e) {

			MessageBox dialog = new MessageBox(employeeView.getShell(), SWT.ICON_ERROR | SWT.OK);
			dialog.setText("Erreur");
			dialog.setMessage("Une erreur est survenue. " + '\n' + e.getMessage());
			dialog.open();
		}

	}

	/***
	 * 
	 * chante the employee status into the database to "Archivé"
	 */
	public static void suppEmploye() throws SQLException {
		if (selectedEmployee == null) {
			throw new Error("selectedEmploye est vide");
		}

		Employee e = Employee.getEmployeById(selectedEmployee.getEmployeId());
		e.setStatus("Archivé");
		e.updateDatabase();

		for (MABAssignment a : MABAssignment.getAllAffectation()) {
			if (a.getIdEmploye() == e.getEmployeId()) {
				a.setStatus(Status.ARCHIVED);
				a.updateDatabase();
			}
		}

		for (EmployeeCost ce : EmployeeCost.getAllCoutEmploye()) {
			if (ce.getEmployeId() == e.getEmployeId()) {
				ce.setStatus("Archivé");
				ce.updateDatabase();
			}
		}

		

		compositeSelectionBoutons();
		updateTable();

		selectedEmployee = null;
		compositeSelectionBoutons();
	}

	
	/***
	 *add the menu 
	 * 
	 * @param table
	 */

	public static void doMenu(Table table) {
		menu = new Menu(employeeView.getShell(), SWT.POP_UP);
		table.setMenu(menu);

		if (selectedEmployee != null || selectedAmorti != null) {
			// pour modifier
			MenuItem update = new MenuItem(menu, SWT.PUSH);
			update.setText("Modifier l'element");
			update.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent arg0) {
					if (table.getSelection().length != 0) {
						if (selectedAmorti != null) {
							vueCreationA(1);
						} else if (selectedEmployee != null) {
							vueEmployeFormulaire(1);
						}
					}
				}
			});
		}

		
		MenuItem delete = new MenuItem(menu, SWT.PUSH);
		delete.setText("Supprimer l'element");
		delete.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				try {
					if (selectedAmorti != null) {
						EmployeeAmortisation ae = EmployeeAmortisation
								.getAmmortissementEmployeById(selectedAmorti.getAmmortissementEmployeId());
						MessageBox dialog = new MessageBox(employeeView.getShell(),
								SWT.ICON_QUESTION | SWT.YES | SWT.NO);
						dialog.setText("Suppression Amortissement Employe");
						dialog.setMessage("Voulez vous supprimer le coût à amortir de l'employé "
								+ Employee.getEmployeById(ae.getEmployeId()).getSurname() + " "
								+ Employee.getEmployeById(ae.getEmployeId()).getFirstName() + " ?");
						int buttonID = dialog.open();
						switch (buttonID) {
						case SWT.YES:
							ae.setStatus("Archivé");
							ae.updateDatabase();
							updateTableAmorti();
						}
					} else if (selectedEmployee != null) {
						Employee e = Employee.getEmployeById(selectedEmployee.getEmployeId());
						MessageBox dialog = new MessageBox(employeeView.getShell(),
								SWT.ICON_QUESTION | SWT.YES | SWT.NO);
						dialog.setText("Suppression Employe");
						dialog.setMessage("Voulez vous supprimer l'employé " + e.getSurname() + " " + e.getFirstName()
								+ " ?\nToutes les affectations et les couts liés à cet employé seront supprimés.");
						int buttonID = dialog.open();
						switch (buttonID) {
						case SWT.YES:
							suppEmploye();
						}
					} else {

						TableItem item = (TableItem) tableCost.getSelection()[0];
						MessageBox dialog = new MessageBox(employeeView.getShell(),
								SWT.ICON_QUESTION | SWT.YES | SWT.NO);
						dialog.setText("Suppression Cout Employe");
						dialog.setMessage("Voulez vous supprimer les couts de l'employé" + item.getText(1) + " "
								+ item.getText(2) + " ?");
						int buttonID = dialog.open();
						switch (buttonID) {
						case SWT.YES:
							int id = Integer.parseInt(item.getText(0));
							EmployeeCost ce = EmployeeCost.getCoutEmployeById(id);
							ce.setStatus("Archivé");
							ce.updateDatabase();

							String[] moisAnnee = item.getText(4).split("/");
							String periode = Month.of(Integer.parseInt(moisAnnee[0])).toString() + " "
									+ ((Integer) Integer.parseInt(moisAnnee[1])).toString();
							updateCostTable(periode);
							view.pack();
						}
					}
					selectedEmployee = null;
					selectedAmorti = null;
					menu.dispose();
					menu = new Menu(employeeView.getShell(), SWT.POP_UP);
					table.setMenu(menu);
				} catch (Exception e) {

					MessageBox dialog = new MessageBox(employeeView.getShell(), SWT.ICON_ERROR | SWT.OK);
					dialog.setText("Erreur");
					dialog.setMessage("Une erreur est survenue. " + '\n' + e.getMessage());
					dialog.open();
				}
			}
		});

	}

	/**
	 * add all Employee into the composite
	 * @param composite
	 * @return <type> Table</type> table which contain affectation
	 */
	public static Table getAllEmployerForAffectation(Composite composite) {

		Table table = new Table(composite, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL | SWT.FULL_SELECTION);
		table.setLayoutData(new RowData(847, 450));
		table.setLinesVisible(true);
		table.setHeaderVisible(true);


		String[] titles = { "Nom", "Prenom", "N° de matricule", "Id DB" };

		for (String title : titles) {
			TableColumn column = new TableColumn(table, SWT.NONE);
			column.setText(title);
		}

		
		final TableColumn[] columns = table.getColumns();
		try {
			for (Employee e : Employee.getAllEmploye()) {
				// on verifie le status
				if (e.getStatus().contentEquals("Publié")) {
					TableItem item = new TableItem(table, SWT.NONE);

					item.setText(0, e.getSurname());
					item.setText(1, e.getFirstName());
					item.setText(2, e.getMatricule());
					item.setText(3, "" + e.getEmployeId());

				}
			}
		} catch (SQLException e) {

			MessageBox dialog = new MessageBox(composite.getShell(), SWT.ICON_ERROR | SWT.OK);
			dialog.setText("Erreur");
			dialog.setMessage("Une erreur est survenue. " + '\n' + e.getMessage());
			dialog.open();
		}

		
		for (TableColumn col : columns)
			col.pack();

		return table;
	}

	
	public Composite getComposite() {
		return EmployeeView.employeeView;

	}

	/***
	 * Getter for selection
	 * 
	 * @return <type> Composite</type> selection
	 */
	public Composite getSelection() {
		return selection;
	}

	/***
	 *Getter for composite view
	 * 
	 * @return <type>Composite</type>view
	 */
	public Composite getView() {
		return view;
	}

}
