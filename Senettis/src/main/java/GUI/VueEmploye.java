package GUI;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Objects;

import org.eclipse.swt.*;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

import classes.Affectation;
import classes.Employee;
import classes.SickLeave;

public class VueEmploye {

	private Display display;
	private Composite vueEmploye;
	private Composite selection;
	private Composite vue;
	private Object selectedItem;
	private Menu menu;
	private Table table;

	// Creation VueEmploye --------------------------------------------------
	/***
	 * Utilisé depuis Home pour créer une vueEmploye
	 * 
	 * @param composite : le composite contenuColonneGauche qui va contenir vueEmploye
	 * @param display
	 */
	public VueEmploye(Composite composite, Display display) {
		this.display = display;
		Couleur.setDisplay(display); // pour utiliser les couleurs du fichier couleur

		vueEmploye = new Composite(composite, SWT.NONE);
		RowLayout rowLayout = new RowLayout();
		rowLayout.type = SWT.VERTICAL;
		vueEmploye.setLayout(rowLayout);

		tableViewSelection(0);
		tableViewVue(0);

		vueEmploye.pack();
		vueEmploye.getParent().pack();
	}


	public void tableViewSelection(int mode) {//mode 0 -> employe //mode 1 -> sl 
		if (!Objects.isNull(selection) && !selection.isDisposed()) {
			selection.dispose();
		}

		selection = new Composite(vueEmploye, SWT.NONE);
		RowLayout rowLayout = new RowLayout();
		rowLayout.marginWidth = 20;
		rowLayout.marginTop = 6;
		selection.setLayout(rowLayout);
		selection.setBackground(Couleur.gris);

		//employee mode
		if (mode == 0) {
			Button boutonCreer = new Button(selection, SWT.CENTER);
			boutonCreer.setText("Ajouter un employé");
			boutonCreer.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent arg0) {
					formViewVue(0,0,0);
					formViewSelection(0,0);
					formViewVue(0,0,0);

					vueEmploye.pack();
					vueEmploye.getParent().pack();
				}
			});

			Button boutonSLCreation = new Button(selection, SWT.CENTER);
			boutonSLCreation.setText("Ajouter un arrêt Maladie");
			boutonSLCreation.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent arg0) {
					formViewVue(1,0,0);
					formViewSelection(1,0);
					formViewVue(1,0,0);

					vueEmploye.pack();
					vueEmploye.getParent().pack();

				}
			});

			Button boutonSLTable = new Button(selection, SWT.CENTER);
			boutonSLTable.setText("Gérer les arrêts Maladie");
			boutonSLTable.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent arg0) {
					selectedItem = null;
					if (!menu.isDisposed()) {
						menu.dispose();
						menu = new Menu(vueEmploye.getShell(), SWT.POP_UP);
						table.setMenu(menu);
					}
					
					
					tableViewSelection(1);
					tableViewVue(1);

					vueEmploye.pack();
					vueEmploye.getParent().pack();

				}
			});

			if (selectedItem != null) {
				// Bouton Modifier
				Button boutonModifier = new Button(selection, SWT.CENTER);
				boutonModifier.setText("Modifier l'employé");
				boutonModifier.addSelectionListener(new SelectionAdapter() {
					@Override
					public void widgetSelected(SelectionEvent arg0) {
						formViewVue(0,1,0);
						formViewSelection(0,1);
						formViewVue(0,1,0);

						vueEmploye.pack();
						vueEmploye.getParent().pack();
					}
				});

				// Bouton Supprimer
				Button boutonSupprimer = new Button(selection, SWT.CENTER);
				boutonSupprimer.setText("Supprimer l'employé");
				boutonSupprimer.addSelectionListener(new SelectionAdapter() {
					@Override
					public void widgetSelected(SelectionEvent arg0) {
						try {
							if (selectedItem == null) {
								throw new Error("selectedItem est vide");
							}

							Employee e = Employee.getEmployeById(((Employee) selectedItem).getEmployeId());

							//on demande validation
							MessageBox dialog = new MessageBox(vueEmploye.getShell(), SWT.ICON_QUESTION | SWT.YES | SWT.NO);
							dialog.setText("Suppression Employe");
							dialog.setMessage("Voulez vous supprimer l'employé " + e.getNom() + " " + e.getPrenom()
							+ " ?\nToutes les affectations liées à cet employé seront supprimées.");
							int buttonID = dialog.open();

							switch (buttonID) {
							case SWT.YES:
								suppEmploye(table);
							}

						} catch (NumberFormatException | SQLException e) {
							System.out.println("erreur dans la supression");
							MessageBox dialog = new MessageBox(vueEmploye.getShell(), SWT.ICON_ERROR | SWT.OK);
							dialog.setText("Erreur Supression");
							dialog.setMessage("Une erreur est survenue lors de la supression de l'employé. " + '\n' + e.getMessage());
							dialog.open();
						}
					}
				});
			}
		}


		//sick leave mode
		if (mode == 1) {
			Button boutonCreer = new Button(selection, SWT.CENTER);
			boutonCreer.setText("Ajouter un arrêt maladie");
			boutonCreer.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent arg0) {
					formViewVue(1,0,1);
					formViewSelection(1,0);
					formViewVue(1,0,1);

					vueEmploye.pack();
					vueEmploye.getParent().pack();
				}
			});

			if (selectedItem != null) {
				// Bouton Modifier
				Button boutonModifier = new Button(selection, SWT.CENTER);
				boutonModifier.setText("Modifier l'arrêt maladie");
				boutonModifier.addSelectionListener(new SelectionAdapter() {
					@Override
					public void widgetSelected(SelectionEvent arg0) {
						formViewVue(1,1,1);
						formViewSelection(1,1);
						formViewVue(1,1,1);

						vueEmploye.pack();
						vueEmploye.getParent().pack();
					}
				});

				// Bouton Supprimer
				Button boutonSupprimer = new Button(selection, SWT.CENTER);
				boutonSupprimer.setText("Supprimer l'arrêt maladie");
				boutonSupprimer.addSelectionListener(new SelectionAdapter() {
					@Override
					public void widgetSelected(SelectionEvent arg0) {
						try {
							if (selectedItem == null) {
								throw new Error("selectedItem est vide");
							}
							SickLeave sl = SickLeave.getSickLeaveById(((SickLeave)selectedItem).getIdSickLeave());

							//on demande validation
							MessageBox dialog = new MessageBox(vueEmploye.getShell(), SWT.ICON_QUESTION | SWT.YES | SWT.NO);
							dialog.setText("Suppression Arrêt maladie");
							dialog.setMessage("Voulez vous supprimer l'arrêt maladie du " + sl.getDateDebut()+" pour l'employé " + Employee.getEmployeById(sl.getIdEmploye()).getNom() + " " + Employee.getEmployeById(sl.getIdEmploye()).getPrenom()
							+ " ?");
							int buttonID = dialog.open();

							switch (buttonID) {
							case SWT.YES:
								suppSL(table);
							}

						} catch (NumberFormatException | SQLException e) {
							System.out.println("erreur dans la supression");
							MessageBox dialog = new MessageBox(vueEmploye.getShell(), SWT.ICON_ERROR | SWT.OK);
							dialog.setText("Erreur Supression");
							dialog.setMessage("Une erreur est survenue lors de la supression de l'arrêt maladie. " + '\n' + e.getMessage());
							dialog.open();

						}

					}
				});
			}
			
			Button boutonRetour = new Button(selection, SWT.CENTER);
			boutonRetour.setText("Retour aux employés");
			boutonRetour.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent arg0) {
					selectedItem = null;
					if (!menu.isDisposed()) {
						menu.dispose();
						menu = new Menu(vueEmploye.getShell(), SWT.POP_UP);
						table.setMenu(menu);
					}
					
					tableViewSelection(0);
					tableViewVue(0);

					vueEmploye.pack();
					vueEmploye.getParent().pack();
				}
			});

		}

		selection.pack();
	}


	public void tableViewVue(int mode) {
		if (!Objects.isNull(vue) && !vue.isDisposed()) {
			vue.dispose();
		}

		RowLayout rowLayoutV = new RowLayout();
		rowLayoutV.type = SWT.VERTICAL;

		vue = new Composite(vueEmploye, SWT.NONE);
		vue.setLayout(rowLayoutV);
		vue.setBackground(Couleur.gris);

		table = new Table(vue, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL | SWT.FULL_SELECTION);
		table.setLinesVisible(true);
		table.setHeaderVisible(true);

		if (mode == 0) {
			table.setLayoutData(new RowData(1047, 450));
			String[] titles = { "Titre  ", "Nom        ", "Prenom     ", "Email             ", "Téléphone", "N° de matricule", "Pointure", "Taille",
					"Date d'arrivée", "Ancienneté", "Nb d'heures", "Remb. Transport", "Remb. Telephone", "Salaire", "Id DB" };

			for (String title : titles) {
				TableColumn column = new TableColumn(table, SWT.NONE);
				column.setText(title);
			}

			final TableColumn[] columns = table.getColumns();
			for (TableColumn col : columns)
				col.pack();

			table.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent e) {
					if (table.getSelectionIndex() != -1) {

						try {
							selectedItem = Employee.getEmployeById(Integer.parseInt(table.getSelection()[0].getText(14)));
						} catch (NumberFormatException | SQLException e1) {
							System.out.println("erreur pour recuperer l'employe selectionne");
							MessageBox dialog = new MessageBox(vueEmploye.getShell(), SWT.ICON_ERROR | SWT.OK);
							dialog.setText("Erreur");
							dialog.setMessage("Une erreur est survenue. " + '\n' + e1.getMessage());
							dialog.open();
						}

						tableViewSelection(0);

						// on ajoute un menu lorsque l'on fait clique droit sur une ligne
						doEmployeeMenu(table);
					} else { // si plus rien n'est selectionner on passe selectedItem a null et on enleve
						// le menu du clic droit et on enleve les boutons pour modifier et supprimer

						selectedItem = null;
						if (!menu.isDisposed()) {
							menu.dispose();
							menu = new Menu(vueEmploye.getShell(), SWT.POP_UP);
							table.setMenu(menu);
						}
						tableViewSelection(0);
					}
				}
			});

			updateEmployeeTable(table);
		}
		//////////////////////////////////////////////////////////////////////////::
		if (mode == 1) {
			table.setLayoutData(new RowData(600, 450));
			// on met les noms des colonnes //espaces dans les titres pour changer la taille des colonnes
			String[] titles = { "Id Employe ", "Nom        ", "Prenom     ", "Date de début    ", "Durée      ", "Motif           ", "Cout     ", "Id Arrêt Maladie"};
			for (String title : titles) {
				TableColumn column = new TableColumn(table, SWT.NONE);
				column.setText(title);
			}

			final TableColumn[] columns = table.getColumns();
			for (TableColumn col : columns)
				col.pack();

			// on ajoute un listener pour modifier l'interface si l'utilisateur clique sur
			// une ligne
			table.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent e) {
					if (table.getSelectionIndex() != -1) {
						try {
							selectedItem = SickLeave.getSickLeaveById(Integer.parseInt(table.getSelection()[0].getText(7)));
						} catch (NumberFormatException | SQLException e1) {
							System.out.println("erreur pour recuperer l'arret maladie");
							MessageBox dialog = new MessageBox(vueEmploye.getShell(), SWT.ICON_ERROR | SWT.OK);
							dialog.setText("Erreur");
							dialog.setMessage("Une erreur est survenue. " + '\n' + e1.getMessage());
							dialog.open();
						}

						tableViewSelection(1);

						doSLMenu(table);
					} else { // si plus rien n'est selectionner on passe selectedItem a null et on enleve
						// le menu du clic droit et on enleve les boutons pour modifier et supprimer

						selectedItem = null;
						if (!menu.isDisposed()) {
							menu.dispose();
							menu = new Menu(vueEmploye.getShell(), SWT.POP_UP);
							table.setMenu(menu);
						}
						tableViewSelection(1);
					}
				}
			});

			updateSLTable(table);

		}
		vue.pack();
	}



	public void doSLMenu (Table table) {

		menu = new Menu(vueEmploye.getShell(), SWT.POP_UP);
		table.setMenu(menu);

		// pour modifier
		MenuItem update = new MenuItem(menu, SWT.PUSH);
		update.setText("Modifier l'arrêt maladie");
		update.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				if (table.getSelection().length != 0) {
					formViewVue(1,1,1);
					formViewSelection(1,1);
					formViewVue(1,1,1);

					vueEmploye.pack();
					vueEmploye.getParent().pack();
				}
			}
		});

		// pour supprimer
		MenuItem delete = new MenuItem(menu, SWT.PUSH);
		delete.setText("Supprimer l'arrêt maladie");
		delete.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				try {
					if (selectedItem == null) {
						throw new Error("selectedItem est vide");
					}
					SickLeave sl = SickLeave.getSickLeaveById(((SickLeave)selectedItem).getIdSickLeave());

					//on demande validation
					MessageBox dialog = new MessageBox(vueEmploye.getShell(), SWT.ICON_QUESTION | SWT.YES | SWT.NO);
					dialog.setText("Suppression Arrêt maladie");
					dialog.setMessage("Voulez vous supprimer l'arrêt maladie du " + sl.getDateDebut()+" pour l'employé " + Employee.getEmployeById(sl.getIdEmploye()).getNom() + " " + Employee.getEmployeById(sl.getIdEmploye()).getPrenom()
					+ " ?");
					int buttonID = dialog.open();

					switch (buttonID) {
					case SWT.YES:
						suppSL(table);
					}

				} catch (NumberFormatException | SQLException e) {
					System.out.println("erreur dans la supression");
					MessageBox dialog = new MessageBox(vueEmploye.getShell(), SWT.ICON_ERROR | SWT.OK);
					dialog.setText("Erreur Supression");
					dialog.setMessage("Une erreur est survenue lors de la supression de l'arrêt maladie. " + '\n' + e.getMessage());
					dialog.open();

				}
			}
		});

	}

	//mode -> employe ou sick leave // type -> modif1 ou crea 0
	public void formViewSelection(int mode, int type ) {
		if (!Objects.isNull(selection) && !selection.isDisposed()) {
			selection.dispose();
		}

		String title = ""; int addSize = 0;
		if (mode == 0 && type == 0) {//creation employe
			title = "Création d'un employé";
			addSize = vue.getSize().x;
			addSize = (addSize - 174)/2;
		}
		else if (mode == 0) {//modif employe
			title = "Modification d'un employé";
			addSize = vue.getSize().x;
			addSize = (addSize - 200)/2;
		}
		else if (mode == 1 &&  type == 0) {//creation sick leave
			title = "Création d'un Arrêt Maladie";
			addSize = vue.getSize().x;
			addSize = (addSize - 208)/2;
		}
		else if (mode == 1) {//modif sick leave
			title = "Modification d'un Arrêt Maladie";
			addSize = vue.getSize().x;
			addSize = (addSize - 231)/2;
		}

		selection = new Composite(vueEmploye, SWT.CENTER);

		FillLayout fillLayout = new FillLayout();
		fillLayout.type = SWT.VERTICAL;
		fillLayout.marginWidth = addSize;
		selection.setLayout(fillLayout);

		// juste pour creer un espace
		Label l1 = new Label(selection, SWT.NONE);
		l1.setText("");
		l1.setBackground(Couleur.bleuFonce);

		selection.setBackground(Couleur.bleuFonce);
		Label HeadLabel = new Label(selection, SWT.TITLE);
		HeadLabel.setText(title);
		Font fontTitle = new Font(HeadLabel.getDisplay(), "Arial", 12, SWT.BOLD);
		HeadLabel.setForeground(Couleur.bleuClair);
		HeadLabel.setFont(fontTitle);
		HeadLabel.setBackground(Couleur.bleuFonce);

		// juste pour creer un espace
		Label l2 = new Label(selection, SWT.NONE);
		l2.setText("");
		l2.setBackground(Couleur.bleuFonce);

		selection.pack();
	}


	//mode employe ou sick leave //type cre ou modif
	public void formViewVue(int mode, int type, int cancel) {
		if (!Objects.isNull(vue) && !vue.isDisposed()) {
			vue.dispose();
		}


		// creation de la vue
		vue = new Composite(vueEmploye, SWT.NONE);
		FillLayout fillLayoutH = new FillLayout();
		fillLayoutH.type = SWT.HORIZONTAL;
		vue.setLayout(fillLayoutH);

		if (mode == 0 && type == 1) {
			showFormulaire(2,((Employee)selectedItem).getTitre(),((Employee)selectedItem).getNom(),((Employee)selectedItem).getPrenom(),((Employee)selectedItem).getMail(),((Employee)selectedItem).getTelephone(),
					((Employee)selectedItem).getNumeroMatricule().toString(),((Employee)selectedItem).getPointure(),((Employee)selectedItem).getTaille(),((Employee)selectedItem).getDateArrivee(),
					((Employee)selectedItem).getNombreHeures().toString(),((Employee)selectedItem).getRemboursementTransport().toString(),((Employee)selectedItem).getRemboursementTelephone().toString(),
					((Employee)selectedItem).getSalaire().toString());
		}
		else if (mode == 0) {
			showFormulaire(1,"","","","","","","","","","","","","");
		}
		else if (mode == 1) {
			try {
				getSLViewContent(type, cancel);
			} catch (SQLException e) {
				System.out.println("erreur dans le formulaire Arret maladie");
				MessageBox dialog = new MessageBox(vueEmploye.getShell(), SWT.ICON_ERROR | SWT.OK);
				dialog.setText("Erreur Supression");
				dialog.setMessage("Une erreur est survenue lors de la création du formulaire arrêt maladie. " + '\n' + e.getMessage());
				dialog.open();
			}
		}

		vue.pack();


	}


	public void getSLViewContent(int type, int cancel) throws SQLException {

		FillLayout fillLayoutV = new FillLayout();
		fillLayoutV.type = SWT.VERTICAL;
		fillLayoutV.marginWidth = 10;
		Composite column1 = new Composite(vue, SWT.BORDER);
		Composite column2 = new Composite(vue, SWT.BORDER);
		column1.setBackground(Couleur.bleuClair);
		column2.setBackground(Couleur.bleuClair);
		column1.setLayout(fillLayoutV);
		column2.setLayout(fillLayoutV);

		// utilisé pour tous les composites des arguments
		FillLayout fillLayoutH5 = new FillLayout();
		fillLayoutH5.marginHeight = 30;
		fillLayoutH5.spacing = 5;
		fillLayoutH5.type = SWT.HORIZONTAL;

		// employee
		Composite employeeComposite = new Composite(column1, SWT.NONE);
		employeeComposite.setBackground(Couleur.bleuClair);
		employeeComposite.setLayout(fillLayoutH5);

		Label employeeLabel = new Label(employeeComposite, SWT.NONE);
		employeeLabel.setBackground(Couleur.bleuClair);
		employeeLabel.setText("Employé* : ");

		Combo employee = new Combo(employeeComposite, SWT.BORDER);
		if (type ==1) {
			if (selectedItem != null) {
				String name = Employee.getEmployeById(((SickLeave)selectedItem).getIdEmploye()).getNom()+" "+Employee.getEmployeById(((SickLeave)selectedItem).getIdEmploye()).getPrenom();
				if (name.length()>30) {
					name = name.substring(0, 30)+"; id : "+((SickLeave)selectedItem).getIdEmploye();
				}
				else {
					name = name+"; id : "+((SickLeave)selectedItem).getIdEmploye();
				}
				employee.setText(name);
				for(Employee e : Employee.getAllEmploye()) {
					if(e.getEmployeId() != ((SickLeave)selectedItem).getIdEmploye()) {
						name = e.getNom()+" "+e.getPrenom();
						if (name.length()>30) {
							name = name.substring(0, 30)+"; id : "+e.getEmployeId();
						}
						else {
							name = name+"; id : "+e.getEmployeId();
						}
						employee.add(name);
					}
				}
			}
		}
		else {
			employee.setText("Selectionner ...");
			String name;
			for(Employee e : Employee.getAllEmploye()) {
				name = e.getNom()+" "+e.getPrenom();
				if (name.length()>30) {
					name = name.substring(0, 30)+"; id : "+e.getEmployeId();
				}
				else {
					name = name+"; id : "+e.getEmployeId();
				}
				employee.add(name);

			}
		}

		// date
		Composite dateComposite = new Composite(column1, SWT.NONE);
		dateComposite.setBackground(Couleur.bleuClair);
		dateComposite.setLayout(fillLayoutH5);

		Label dateLabel = new Label(dateComposite, SWT.NONE);
		dateLabel.setBackground(Couleur.bleuClair);
		dateLabel.setText("Date de début* :                                 ");

		final Text date = new Text(dateComposite, SWT.BORDER);
		if (type == 1) {
			if (selectedItem != null) {
				date.setText(((SickLeave)selectedItem).getDateDebut());
			}
		}
		else {
			date.setText("");
		}
		date.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent me) {
				if (!(date.getText().isEmpty())) {// pour ne pas tester quand l'utilisateur est en train de
					// modifier
					if (date.getText().length() == 2) {
						date.append("/");
					}
					if (date.getText().length() == 5) {
						date.append("/");
					}
				}
			}
		});


		// duration
		Composite durationComposite = new Composite(column1, SWT.NONE);
		durationComposite.setBackground(Couleur.bleuClair);
		durationComposite.setLayout(fillLayoutH5);

		Label durationLabel = new Label(durationComposite, SWT.NONE);
		durationLabel.setBackground(Couleur.bleuClair);
		durationLabel.setText("Durée de l'arrêt : ");

		final Text duration = new Text(durationComposite, SWT.BORDER);
		if (type == 1) {
			if (selectedItem != null) {
				duration.setText(((SickLeave)selectedItem).getDuree());
			}
		}
		else {
			duration.setText("");
		}

		// motive
		Composite motiveComposite = new Composite(column2, SWT.NONE);
		motiveComposite.setBackground(Couleur.bleuClair);
		motiveComposite.setLayout(fillLayoutH5);

		Label motiveLabel = new Label(motiveComposite, SWT.NONE);
		motiveLabel.setBackground(Couleur.bleuClair);
		motiveLabel.setText("Motif de l'arrêt : ");

		final Text motive = new Text(motiveComposite, SWT.BORDER);
		if (type == 1) {
			if (selectedItem != null) {
				motive.setText(((SickLeave)selectedItem).getMotive());
			}
		}
		else {
			motive.setText("");
		}

		// cost
		Composite costComposite = new Composite(column2, SWT.NONE);
		costComposite.setBackground(Couleur.bleuClair);
		costComposite.setLayout(fillLayoutH5);

		Label costLabel = new Label(costComposite, SWT.NONE);
		costLabel.setBackground(Couleur.bleuClair);
		costLabel.setText("Coût eventuel : ");

		final Text cost = new Text(costComposite, SWT.BORDER);
		if (type == 1) {
			if (selectedItem != null) {
				cost.setText(((SickLeave)selectedItem).getCost().toString());
			}
		}
		else {
			cost.setText("");
		}

		// Boutons
		Composite compositeBoutons = new Composite(column2, SWT.CENTER);
		compositeBoutons.setBackground(Couleur.bleuClair);
		compositeBoutons.setLayout(fillLayoutH5);

		Button buttonAnnulation = new Button(compositeBoutons, SWT.BACKGROUND);
		buttonAnnulation.setText("Annuler");
		buttonAnnulation.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				tableViewSelection(cancel);
				tableViewVue(cancel);

				vueEmploye.pack();
				vueEmploye.getParent().pack();
			}
		});

		Button buttonValidation = new Button(compositeBoutons, SWT.BACKGROUND);
		buttonValidation.setText("Valider");
		buttonValidation.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent arg0) {

				try {
					createSL(employee.getText(), date.getText(), duration.getText(), motive.getText(), cost.getText(), cancel);
				} catch (Throwable e) {
					System.out.println("erreur dans la creation SickLeave");
					MessageBox dialog = new MessageBox(vueEmploye.getShell(), SWT.ICON_ERROR | SWT.OK);
					dialog.setText("Erreur Création");
					dialog.setMessage("Une erreur est survenue lors de la création de l'arrêt maladie. " + '\n' + e.getMessage());
					dialog.open();
				}
			}
		});
		vue.pack();
	}


	public void updateSLTable(Table table) {

		table.removeAll();

		// on remplit la table
		try {
			for (SickLeave sl : SickLeave.getAllSickLeave()) {
				// on verifie le status
				if (sl.getStatus().contentEquals("Publié")) {
					TableItem item = new TableItem(table, SWT.NONE);
					item.setText(0, Integer.toString(sl.getIdEmploye()));
					item.setText(1, Employee.getEmployeById(sl.getIdEmploye()).getNom());
					item.setText(2, Employee.getEmployeById(sl.getIdEmploye()).getPrenom());
					item.setText(3, sl.getDateDebut());
					item.setText(4, sl.getDuree());
					item.setText(5, sl.getMotive());
					item.setText(6, Double.toString(sl.getCost()));
					item.setText(7, Integer.toString(sl.getIdSickLeave()));
				}
			}
		} catch (SQLException e) {
			System.out.println("erreur dans la table des Arret Maladie");
			MessageBox dialog = new MessageBox(vueEmploye.getShell(), SWT.ICON_ERROR | SWT.OK);
			dialog.setText("Erreur");
			dialog.setMessage("Une erreur est survenue. " + '\n' + e.getMessage());
			dialog.open();
		}

	}


	public void createSL(String employee, String date, String duration, String motive, String cost, int cancel) {
		//employee
		int employeeId;SickLeave sl;
		try {
			String s = employee.split(";")[1].replace(" ",""); 
			employeeId = Integer.parseInt(s.substring(3,s.length()));
		} catch (Exception e) {
			throw new Error("Merci d'indiquer un employé.");
		}

		if (!(date.isEmpty())) {
			sl = new SickLeave(employeeId, date, "Publié");
		}
		else {
			throw new Error("Merci d'indiquer une date.");
		}

		if (!(duration.isEmpty())) {
			sl.setDuration(duration);
		}

		if (!(motive.isEmpty())) {
			sl.setMotive(motive);
		}

		if (!(cost.isEmpty())) {
			try {
				sl.setCost(Double.parseDouble(cost));
			}catch(Exception e) {
				try {
					sl.setCost(Double.parseDouble(cost+".0"));
				}catch(Exception e1) {
					throw new Error("Le cout est érroné.");
				}
			}
		}

		try {
			sl.insertDatabase();
			System.out.println("on a insere le sick leave !!");
			MessageBox dialog = new MessageBox(vueEmploye.getShell(), SWT.ICON_INFORMATION | SWT.OK);
			dialog.setText("Création réussie");
			dialog.setMessage("L'arrêt maladie a bien été ajouté à la base de données.");
			dialog.open();

			tableViewSelection(cancel);
			tableViewVue(cancel);

			vueEmploye.pack();
			vueEmploye.getParent().pack();
		} catch (SQLException e) {
			throw new Error("Une erreur est survenue lors de la création de l'arrêt maladie.");
		}
	}
	//------------------------------------------------------------------------------------------------------------------------------




	/***
	 * modifie un employe dans la base de données
	 */
	public void validerModification() {
		if (selectedItem == null) {
			throw new Error("selectedItem est vide");
		}

		// on insert dans la base de données
		try {
			((Employee)selectedItem).updateDatabase();
			System.out.println("on a modifie l employe !!");
			MessageBox dialog = new MessageBox(vueEmploye.getShell(), SWT.ICON_INFORMATION | SWT.OK);
			dialog.setText("Modification réussie");
			dialog.setMessage("L'employé a bien été modifié dans la base de données.");
			dialog.open();
			selectedItem = null;
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("erreur dans la modif");
			MessageBox dialog = new MessageBox(vueEmploye.getShell(), SWT.ICON_ERROR | SWT.OK);
			dialog.setText("Erreur Modification");
			dialog.setMessage("Une erreur est survenue lors de la modification de l'employé. " + e.getMessage());
			dialog.open();
		}

		tableViewSelection(0);
		tableViewVue(0);
		vueEmploye.pack();
		vueEmploye.getParent().pack();
	}





	/***
	 * Affiche le formulaire pour une modification ou une creation en fonction du type donne en argument 
	 * @param type : 1 pour une creation, 2 pour une modification
	 * les autres arguments correspondent aux valeurs a afficher dans les champs du formulaire
	 * @param Titre
	 * @param nom
	 * @param prenom
	 * @param mail
	 * @param telephone
	 * @param numeroMatricule
	 * @param pointure
	 * @param taille
	 * @param dateArrivee
	 * @param nbHeures
	 * @param remboursementTransport
	 * @param remboursementTelephone
	 * @param salaire
	 */
	@SuppressWarnings("unlikely-arg-type")
	public void showFormulaire(int type, String Titre, String nom, String prenom, String mail, String telephone, String numeroMatricule, String pointure, String taille, String dateArrivee,
			String nbHeures, String remboursementTransport, String remboursementTelephone, String salaire) {
		// on cree trois colonne pour repartir les champs
		FillLayout fillLayoutV = new FillLayout();
		fillLayoutV.type = SWT.VERTICAL;
		fillLayoutV.marginWidth = 10;
		Composite colonne1 = new Composite(vue, SWT.BORDER);
		Composite colonne2 = new Composite(vue, SWT.BORDER);
		Composite colonne3 = new Composite(vue, SWT.BORDER);
		colonne1.setBackground(Couleur.bleuClair);
		colonne2.setBackground(Couleur.bleuClair);
		colonne3.setBackground(Couleur.bleuClair);
		colonne1.setLayout(fillLayoutV);
		colonne2.setLayout(fillLayoutV);
		colonne3.setLayout(fillLayoutV);

		// utilisé pour tous les composites des arguments
		FillLayout fillLayoutH5 = new FillLayout();
		fillLayoutH5.marginHeight = 30;
		fillLayoutH5.spacing = 5;
		fillLayoutH5.type = SWT.HORIZONTAL;

		// Titre
		Composite compositeTitre = new Composite(colonne1, SWT.NONE);
		compositeTitre.setBackground(Couleur.bleuClair);
		compositeTitre.setLayout(fillLayoutH5);

		Label labelTitre = new Label(compositeTitre, SWT.NONE);
		labelTitre.setBackground(Couleur.bleuClair);
		labelTitre.setText("Titre : ");

		Combo titre = new Combo(compositeTitre, SWT.BORDER);
		if (titre.equals("M")) {
			titre.setText("M");
			titre.add("Mme");
		}
		else if (titre.equals("Mme")) {
			titre.setText("Mme");
			titre.add("M");
		}
		else {
			titre.setText("Mme");
			titre.add("M");
		}

		// Nom
		Composite compositeNom = new Composite(colonne1, SWT.NONE);
		compositeNom.setBackground(Couleur.bleuClair);
		compositeNom.setLayout(fillLayoutH5);

		Label labelNom = new Label(compositeNom, SWT.NONE);
		labelNom.setBackground(Couleur.bleuClair);
		labelNom.setText("Nom* : ");

		final Text textNom = new Text(compositeNom, SWT.BORDER);
		textNom.setText(nom);

		// Prenom
		Composite compositePrenom = new Composite(colonne1, SWT.NONE);
		compositePrenom.setBackground(Couleur.bleuClair);
		compositePrenom.setLayout(fillLayoutH5);

		Label labelPrenom = new Label(compositePrenom, SWT.NONE);
		labelPrenom.setBackground(Couleur.bleuClair);
		labelPrenom.setText("Prenom* : ");

		final Text textPrenom = new Text(compositePrenom, SWT.BORDER);
		textPrenom.setText(prenom);

		// Mail
		Composite compositeMail = new Composite(colonne1, SWT.NONE);
		compositeMail.setBackground(Couleur.bleuClair);
		compositeMail.setLayout(fillLayoutH5);

		Label labelMail = new Label(compositeMail, SWT.NONE);
		labelMail.setBackground(Couleur.bleuClair);
		labelMail.setText("Email : ");

		final Text textMail = new Text(compositeMail, SWT.BORDER);
		textMail.setText(mail);

		// Telephone
		Composite compositeTelephone = new Composite(colonne1, SWT.NONE);
		compositeTelephone.setBackground(Couleur.bleuClair);
		compositeTelephone.setLayout(fillLayoutH5);

		Label labelTelephone = new Label(compositeTelephone, SWT.NONE);
		labelTelephone.setBackground(Couleur.bleuClair);
		labelTelephone.setText("Telephone : ");

		final Text textTelephone = new Text(compositeTelephone, SWT.BORDER);
		textTelephone.setText(telephone);

		// numeroMatricule
		Composite compositeNumeroMatricule = new Composite(colonne2, SWT.NONE);
		compositeNumeroMatricule.setBackground(Couleur.bleuClair);
		compositeNumeroMatricule.setLayout(fillLayoutH5);

		Label labelNumeroMatricule = new Label(compositeNumeroMatricule, SWT.NONE);
		labelNumeroMatricule.setBackground(Couleur.bleuClair);
		labelNumeroMatricule.setText("Numero de Matricule* : ");

		final Text textNumeroMatricule = new Text(compositeNumeroMatricule, SWT.BORDER);
		textNumeroMatricule.setText(numeroMatricule);

		// Pointure
		Composite compositePointure = new Composite(colonne2, SWT.NONE);
		compositePointure.setBackground(Couleur.bleuClair);
		compositePointure.setLayout(fillLayoutH5);

		Label labelPointure = new Label(compositePointure, SWT.NONE);
		labelPointure.setBackground(Couleur.bleuClair);
		labelPointure.setText("Pointure : ");

		final Text textPointure = new Text(compositePointure, SWT.BORDER);
		textPointure.setText(pointure);

		// Taille
		Composite compositeTaille = new Composite(colonne2, SWT.NONE);
		compositeTaille.setBackground(Couleur.bleuClair);
		compositeTaille.setLayout(fillLayoutH5);

		Label labelTaille = new Label(compositeTaille, SWT.NONE);
		labelTaille.setBackground(Couleur.bleuClair);
		labelTaille.setText("Taille : ");

		final Text textTaille = new Text(compositeTaille, SWT.BORDER);
		textTaille.setText(taille);

		// DateArrivee
		Composite compositeDateArrivee = new Composite(colonne2, SWT.NONE);
		compositeDateArrivee.setBackground(Couleur.bleuClair);
		compositeDateArrivee.setLayout(fillLayoutH5);

		Label labelDateArrivee = new Label(compositeDateArrivee, SWT.NONE);
		labelDateArrivee.setBackground(Couleur.bleuClair);
		labelDateArrivee.setText("Date d'arrivée : ");

		final Text textDateArrivee = new Text(compositeDateArrivee, SWT.BORDER);
		textDateArrivee.setText(dateArrivee);
		// pour ajouter les barres / automatiquement
		textDateArrivee.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent me) {
				if (!(textDateArrivee.getText().isEmpty())) {// pour ne pas tester quand l'utilisateur est en train de
					// modifier
					if (textDateArrivee.getText().length() == 2) {
						textDateArrivee.append("/");
					}
					if (textDateArrivee.getText().length() == 5) {
						textDateArrivee.append("/");
					}
				}
			}
		});

		// NombreHeures
		Composite compositeNombreHeures = new Composite(colonne2, SWT.NONE);
		compositeNombreHeures.setBackground(Couleur.bleuClair);
		compositeNombreHeures.setLayout(fillLayoutH5);

		Label labelNombreHeures = new Label(compositeNombreHeures, SWT.NONE);
		labelNombreHeures.setBackground(Couleur.bleuClair);
		labelNombreHeures.setText("Nombre d'heures : ");

		final Text textNombreHeures = new Text(compositeNombreHeures, SWT.BORDER);
		textNombreHeures.setText(nbHeures);

		// RemboursementTransport
		Composite compositeRemboursementTransport = new Composite(colonne3, SWT.NONE);
		compositeRemboursementTransport.setBackground(Couleur.bleuClair);
		compositeRemboursementTransport.setLayout(fillLayoutH5);

		Label labelRemboursementTransport = new Label(compositeRemboursementTransport, SWT.NONE);
		labelRemboursementTransport.setBackground(Couleur.bleuClair);
		labelRemboursementTransport.setText("Remboursement transport : ");

		final Text textRemboursementTransport = new Text(compositeRemboursementTransport, SWT.BORDER);
		textRemboursementTransport.setText(remboursementTransport);

		// RemboursementTelephone
		Composite compositeRemboursementTelephone = new Composite(colonne3, SWT.NONE);
		compositeRemboursementTelephone.setBackground(Couleur.bleuClair);
		compositeRemboursementTelephone.setLayout(fillLayoutH5);

		Label labelRemboursementTelephone = new Label(compositeRemboursementTelephone, SWT.NONE);
		labelRemboursementTelephone.setBackground(Couleur.bleuClair);
		labelRemboursementTelephone.setText("Remboursement telephone : ");

		final Text textRemboursementTelephone = new Text(compositeRemboursementTelephone, SWT.BORDER);
		textRemboursementTelephone.setText(remboursementTelephone);

		// Salaire
		Composite compositeSalaire = new Composite(colonne3, SWT.NONE);
		compositeSalaire.setBackground(Couleur.bleuClair);
		compositeSalaire.setLayout(fillLayoutH5);

		Label labelSalaire = new Label(compositeSalaire, SWT.NONE);
		labelSalaire.setBackground(Couleur.bleuClair);
		labelSalaire.setText("Salaire : ");

		final Text textSalaire = new Text(compositeSalaire, SWT.BORDER);
		textSalaire.setText(salaire);

		// ne s'affiche pas
		// utiliser pour avoir le meme nombre de composite sur chaque colonne et qu'ils
		// prennent donc la meme taille
		Composite test = new Composite(colonne3, SWT.NONE);
		test.setBackground(Couleur.bleuClair);

		// Boutons
		Composite compositeBoutons = new Composite(colonne3, SWT.CENTER);
		compositeBoutons.setBackground(Couleur.bleuClair);
		compositeBoutons.setLayout(fillLayoutH5);

		Button buttonAnnulation = new Button(compositeBoutons, SWT.BACKGROUND);
		buttonAnnulation.setText("Annuler");
		buttonAnnulation.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				tableViewSelection(0);
				tableViewVue(0);

				vueEmploye.pack();
				vueEmploye.getParent().pack();
			}
		});

		Button buttonValidation = new Button(compositeBoutons, SWT.BACKGROUND);
		buttonValidation.setText("Valider");

		//creation
		if (type == 1) {
			buttonValidation.addSelectionListener(new SelectionAdapter() {

				@Override
				public void widgetSelected(SelectionEvent arg0) {

					try {
						validerCreation(titre.getText(), textNom.getText(), textPrenom.getText(),
								textNumeroMatricule.getText(), textMail.getText(), textTelephone.getText(),
								textPointure.getText(), textTaille.getText(), textDateArrivee.getText(),
								textNombreHeures.getText(), textRemboursementTransport.getText(),
								textRemboursementTelephone.getText(), textSalaire.getText());
					} catch (Throwable e) {
						e.printStackTrace();
						System.out.println("erreur dans la creation");
						MessageBox dialog = new MessageBox(vueEmploye.getShell(), SWT.ICON_ERROR | SWT.OK);
						dialog.setText("Erreur Création");
						dialog.setMessage(
								"Une erreur est survenue lors de la création de l'employé. " + '\n' + e.getMessage());
						dialog.open();
					}
				}
			});
		}


		//modification
		if (type == 2) {
			buttonValidation.addSelectionListener(new SelectionAdapter() {

				@Override
				public void widgetSelected(SelectionEvent arg0) {

					try {
						int id = ((Employee)selectedItem).getEmployeId();
						selectedItem = new Employee(id, titre.getText(), textNom.getText(), textPrenom.getText(),
								textMail.getText(), textTelephone.getText(), textNumeroMatricule.getText(),
								textPointure.getText(), textTaille.getText(), textDateArrivee.getText(),
								Double.parseDouble(textNombreHeures.getText()),
								Double.parseDouble(textRemboursementTransport.getText()),
								Double.parseDouble(textRemboursementTelephone.getText()),
								Double.parseDouble(textSalaire.getText()), "Publié");
						validerModification();
					} catch (Throwable e) {
						e.printStackTrace();
						System.out.println("erreur dans la modif");
						MessageBox dialog = new MessageBox(vueEmploye.getShell(), SWT.ICON_ERROR | SWT.OK);
						dialog.setText("Erreur Modification");
						dialog.setMessage(
								"Une erreur est survenue lors de la modification de l'employé. " + '\n' + e.getMessage());
						dialog.open();
					}

				}
			});
		}

	}


	/***
	 * cree un employe a partir du formulaire et l'inserer dans la base de donnees
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
	public void validerCreation(String titre, String textNom, String textPrenom, String textNumeroMatricule,
			String textMail, String textTelephone, String textPointure, String textTaille, String textDateArrivee,
			String textNombreHeures, String textRemboursementTransport, String textRemboursementTelephone,
			String textSalaire) {

		if (textNom.isEmpty() || textPrenom.isEmpty() || textNumeroMatricule.isEmpty()) {
			throw new Error("Merci d'indiquer au moins le nom, le prénom et le numéro de matricule de l'employé.");
		}

		// champs obligatoires
		Employee employe = new Employee(titre, textNom, textPrenom, textNumeroMatricule);
		employe.setStatus("Publié");

		// champs optionels
		if (!(textMail.isEmpty())) {
			employe.setMail(textMail);
		}
		if (!(textTelephone.isEmpty())) {
			employe.setTelephone(textTelephone);
		}
		if (!(textPointure.isEmpty())) {
			employe.setPointure(textPointure);
		}
		if (!(textTaille.isEmpty())) {
			employe.setTaille(textTaille);
		}

		// date
		if (!(textDateArrivee.isEmpty())) {
			employe.setDateArrivee(textDateArrivee);
		}

		// nombre heures
		if (textNombreHeures != "" && textNombreHeures.contains(".")) {
			employe.setNombreHeures(Double.parseDouble(textNombreHeures));
		} else if (textNombreHeures != "" && textNombreHeures.matches(".*\\d.*")) {
			employe.setNombreHeures(Double.parseDouble(textNombreHeures + ".0"));
		}

		// remboursement transport
		if (textRemboursementTransport != "" && textRemboursementTransport.contains(".")) {
			employe.setRemboursementTransport(Double.parseDouble(textRemboursementTransport));
		} else if (textRemboursementTransport != "" && textRemboursementTransport.matches(".*\\d.*")) {
			employe.setRemboursementTransport(Double.parseDouble(textRemboursementTransport + ".0"));
		}

		// remboursement telephone
		if (textRemboursementTelephone != "" && textRemboursementTelephone.contains(".")) {
			employe.setRemboursementTelephone(Double.parseDouble(textRemboursementTelephone));
		} else if (textRemboursementTelephone != "" && textRemboursementTelephone.matches(".*\\d.*")) {
			employe.setRemboursementTelephone(Double.parseDouble(textRemboursementTelephone + ".0"));
		}

		// salaire
		if (textSalaire != "" && textSalaire.contains(".")) {
			employe.setSalaire(Double.parseDouble(textSalaire));
		} else if (textSalaire != "" && textSalaire.matches(".*\\d.*")) {
			employe.setSalaire(Double.parseDouble(textSalaire + ".0"));
		}

		// on insert dans la base de données
		try {
			employe.insertDatabase();
			System.out.println("on a insere l employe !!");
			MessageBox dialog = new MessageBox(vueEmploye.getShell(), SWT.ICON_INFORMATION | SWT.OK);
			dialog.setText("Création réussie");
			dialog.setMessage("L'employé a bien été ajouté à la base de données.");
			dialog.open();

			tableViewSelection(0);
			tableViewVue(0);
			vueEmploye.pack();
			vueEmploye.getParent().pack();
			
		} catch (SQLException e) {
			System.out.println("erreur dans la création");
			MessageBox dialog = new MessageBox(vueEmploye.getShell(), SWT.ICON_ERROR | SWT.OK);
			dialog.setText("Erreur Création");
			dialog.setMessage("Une erreur est survenue lors de la création de l'employé. " + '\n' + e.getMessage());
			dialog.open();
		}
	}



	/***
	 * methode utilisee pour affectation
	 * @param composite : composite ou ajouter la table
	 * @return la table contenant touts les employes publies
	 */
	public static Table getAllEmployer(Composite composite) {

		Table table = new Table(composite, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL | SWT.FULL_SELECTION);
		table.setLayoutData(new RowData(1047, 450));
		table.setLinesVisible(true);
		table.setHeaderVisible(true);

		//on met les noms des colonnes
		String[] titles = { "Titre", "Nom", "Prenom", "Email", "Téléphone", "N° de matricule", "Pointure", "Taille",
				"Date d'arrivée", "Ancienneté", "Nb d'heures", "Remb. Transport", "Remb. Telephone", "Salaire" };
		for (String title : titles) {
			TableColumn column = new TableColumn(table, SWT.NONE);
			column.setText(title);
		}

		//je voulais cacher cette colonne mais ca ne fonctionne pas
		TableColumn column = new TableColumn(table, SWT.NONE);
		column.setText("Id DB");
		column.setWidth(0);
		column.setResizable(false);

		//on remplit la table
		final TableColumn[] columns = table.getColumns();
		try {
			for (Employee e : Employee.getAllEmploye()) {
				// on verifie le status
				if (e.getStatus().contentEquals("Publié")) {
					TableItem item = new TableItem(table, SWT.NONE);
					item.setText(0, e.getTitre());
					item.setText(1, e.getNom());
					item.setText(2, e.getPrenom());
					item.setText(3, e.getMail());
					item.setText(4, e.getTelephone());
					item.setText(5, e.getNumeroMatricule());
					item.setText(6, e.getPointure());
					item.setText(7, e.getTaille());

					// date et anciennete
					if (e.getDateArrivee() != null) {
						item.setText(8, e.getDateArrivee());

						String date = e.getDateArrivee();
						int j1 = Integer.parseInt(date.substring(0, 2));
						int m1 = Integer.parseInt(date.substring(3, 5));
						int a1 = Integer.parseInt(date.substring(6, 10));
						LocalDate currentdate = LocalDate.now();
						int j2 = currentdate.getDayOfMonth();
						int m2 = currentdate.getMonthValue();
						int a2 = currentdate.getYear();

						if (a2 - a1 < 0) {
							item.setText(9, "euuuh ... ");
						} else if (a2 - a1 == 0) {
							item.setText(9, "moins d'un an");
						} else {
							if ((m1 > m2) || (m1 == m2 && j1 > j2)) {
								if (a2 - a1 - 1 == 0) {
									item.setText(9, "");
								} else if (a2 - a1 - 1 == 1) {
									item.setText(9, Integer.toString(a2 - a1 - 1) + " an");
								} else {
									item.setText(9, Integer.toString(a2 - a1 - 1) + " ans");
								}
							} else {
								if (a2 - a1 == 1) {
									item.setText(9, Integer.toString(a2 - a1) + " an");
								} else {
									item.setText(9, Integer.toString(a2 - a1) + " ans");
								}
							}
						}
					} else {
						item.setText(8, "");
						item.setText(9, "");
					}

					item.setText(10, Double.toString(e.getNombreHeures()));
					item.setText(11, Double.toString(e.getRemboursementTransport()));
					item.setText(12, Double.toString(e.getRemboursementTelephone()));
					item.setText(13, Double.toString(e.getSalaire()));
					item.setText(14, Integer.toString(e.getEmployeId()));
				}
			}
		} catch (SQLException e) {
			System.out.println("erreur dans la table des employes");
			MessageBox dialog = new MessageBox(composite.getShell(), SWT.ICON_ERROR | SWT.OK);
			dialog.setText("Erreur");
			dialog.setMessage("Une erreur est survenue. " + '\n' + e.getMessage());
			dialog.open();
		}

		// on pack les colonnes
		for (TableColumn col : columns)
			col.pack();

		return table;
	}


	/***
	 * methode utilisée pour remplir la table affichant les employes
	 * @param table
	 */
	public void updateEmployeeTable(Table table) {

		table.removeAll();

		// on remplit la table
		try {
			for (Employee e : Employee.getAllEmploye()) {
				// on verifie le status
				if (e.getStatus().contentEquals("Publié")) {
					TableItem item = new TableItem(table, SWT.NONE);
					item.setText(0, e.getTitre());
					item.setText(1, e.getNom());
					item.setText(2, e.getPrenom());
					item.setText(3, e.getMail());
					item.setText(4, e.getTelephone());
					item.setText(5, e.getNumeroMatricule());
					item.setText(6, e.getPointure());
					item.setText(7, e.getTaille());

					// date et anciennete
					if (e.getDateArrivee() != null && !e.getDateArrivee().equals("")) {
						item.setText(8, e.getDateArrivee());

						String date = e.getDateArrivee();
						int j1 = Integer.parseInt(date.substring(0, 2));
						int m1 = Integer.parseInt(date.substring(3, 5));
						int a1 = Integer.parseInt(date.substring(6, 10));
						LocalDate currentdate = LocalDate.now();
						int j2 = currentdate.getDayOfMonth();
						int m2 = currentdate.getMonthValue();
						int a2 = currentdate.getYear();

						if (a2 - a1 < 0) {
							item.setText(9, "euuuh ... ");
						} else if (a2 - a1 == 0) {
							item.setText(9, "moins d'un an");
						} else {
							if ((m1 > m2) || (m1 == m2 && j1 > j2)) {
								if (a2 - a1 - 1 == 0) {
									item.setText(9, "");
								} else if (a2 - a1 - 1 == 1) {
									item.setText(9, Integer.toString(a2 - a1 - 1) + " an");
								} else {
									item.setText(9, Integer.toString(a2 - a1 - 1) + " ans");
								}
							} else {
								if (a2 - a1 == 1) {
									item.setText(9, Integer.toString(a2 - a1) + " an");
								} else {
									item.setText(9, Integer.toString(a2 - a1) + " ans");
								}
							}
						}
					} else {
						item.setText(8, "");
						item.setText(9, "");
					}

					item.setText(10, Double.toString(e.getNombreHeures()));
					item.setText(11, Double.toString(e.getRemboursementTransport()));
					item.setText(12, Double.toString(e.getRemboursementTelephone()));
					item.setText(13, Double.toString(e.getSalaire()));
					item.setText(14, Integer.toString(e.getEmployeId()));
				}
			}
		} catch (SQLException e) {
			System.out.println("erreur dans la table des employes");
			MessageBox dialog = new MessageBox(vueEmploye.getShell(), SWT.ICON_ERROR | SWT.OK);
			dialog.setText("Erreur");
			dialog.setMessage("Une erreur est survenue. " + '\n' + e.getMessage());
			dialog.open();
		}

	}

	/***
	 * cree un menu sur la selection de la table des employes lors d'un clic droit
	 * @param table
	 */
	public void doEmployeeMenu(Table table) {
		menu = new Menu(vueEmploye.getShell(), SWT.POP_UP);
		table.setMenu(menu);

		// pour modifier
		MenuItem update = new MenuItem(menu, SWT.PUSH);
		update.setText("Modifier l'employé");
		update.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				if (table.getSelection().length != 0) {
					formViewVue(0,1,0);
					formViewSelection(0,1);
					formViewVue(0,1,0);

					vueEmploye.pack();
					vueEmploye.getParent().pack();
				}
			}
		});

		// pour supprimer
		MenuItem delete = new MenuItem(menu, SWT.PUSH);
		delete.setText("Supprimer l'employé");
		delete.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				try {
					Employee e = Employee.getEmployeById(((Employee)selectedItem).getEmployeId());
					MessageBox dialog = new MessageBox(vueEmploye.getShell(), SWT.ICON_QUESTION | SWT.YES | SWT.NO);
					dialog.setText("Suppression Employe");
					dialog.setMessage("Voulez vous supprimer l'employé " + e.getNom() + " " + e.getPrenom()
					+ " ?\nToutes les affectations liées à cet employé seront supprimées.");
					int buttonID = dialog.open();
					switch (buttonID) {
					case SWT.YES:
						suppEmploye(table);
					}

				} catch (NumberFormatException | SQLException e) {
					System.out.println("erreur pour supprimer l'employe");
					MessageBox dialog = new MessageBox(vueEmploye.getShell(), SWT.ICON_ERROR | SWT.OK);
					dialog.setText("Erreur");
					dialog.setMessage("Une erreur est survenue. " + '\n' + e.getMessage());
					dialog.open();
				}
			}
		});

	}


	/***
	 * va archiver l'employe selectionne et les affections qui lui sont liées
	 * puis afficher la table des employes mise a jour
	 * @param table
	 * @throws SQLException 
	 */
	public void suppEmploye(Table table) throws SQLException {
		if (selectedItem == null) {
			throw new Error("selectedItem est vide");
		}

		Employee e = Employee.getEmployeById(((Employee)selectedItem).getEmployeId());
		e.setStatus("Archivé");
		e.updateDatabase();

		for (Affectation a : Affectation.getAllAffectation()) {
			if (a.getIdEmploye() == e.getEmployeId()) {
				a.setStatus("Archivé");
				a.updateDatabase();
			}
		}
		
		tableViewSelection(0);
		updateEmployeeTable(table);
		
		vueEmploye.pack();
		vueEmploye.getParent().pack();

		selectedItem = null;
	}


	
	
	public void suppSL(Table table) throws SQLException {
		if (selectedItem == null) {
			throw new Error("selectedItem est vide");
		}

		SickLeave sl = SickLeave.getSickLeaveById(((SickLeave)selectedItem).getIdSickLeave());
		
		sl.setStatus("Archivé");
		sl.updateDatabase();

		tableViewSelection(1);
		updateSLTable(table);

		vueEmploye.pack();
		vueEmploye.getParent().pack();

		selectedItem = null;
	}
	

	public Composite getComposite() {
		return this.vueEmploye;

	}


	/***
	 * retourne la partie superieur de vueEmploye
	 * @return le composite selection de vueEmploye
	 */
	public Composite getSelection() {
		return selection;
	}

	/***
	 * retourne la partie principale de vueEmploye
	 * @return la partie vue de vueEmploye
	 */
	public Composite getVue() {
		return vue;
	}

}
