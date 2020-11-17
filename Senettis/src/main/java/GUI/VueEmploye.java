package GUI;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Month;
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

import classes.Affectation;
import classes.CoutsEmploye;
import classes.Employee;
import classes.Product;
import classes.ProductByDelivery;
import classes.Status;

public class VueEmploye {

	private Display display;
	private Composite vueEmploye;
	private Composite selection;
	private Composite vue;
	private Employee selectedEmploye;
	private Menu menu;
	private Table tableCouts;
	private ArrayList<TableEditor> editors;

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

		compositeSelectionCreer();
		vueEmployeAfficher();

		vueEmploye.pack();
		vueEmploye.getParent().pack();
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

	/***
	 * Pour créer une vueEmploye : Appelle les
	 * fonctions compositeSelectionCreer et vueEmployeAfficher pour creer les composites 
	 * Vue et selection
	 * 
	 * @param composite : composite vueEmploye
	 */
	public void newVueEmploye() {
		//Label test=new Label(vueEmploye,SWT.NONE);test.pack();

		compositeSelectionCreer();
		vueEmployeAfficher();

		vueEmploye.pack();
		vueEmploye.getParent().pack();
	}


	// Modification de la partie Selection
	// --------------------------------------------------
	/***
	 * Creation de la partie Selection (la partie superieure droite) avec uniquement
	 * le bouton Creer
	 * 
	 */
	public void compositeSelectionCreer() {
		if (!Objects.isNull(selection) && !selection.isDisposed()) {
			selection.dispose();
		}

		selection = new Composite(vueEmploye, SWT.NONE);
		RowLayout rowLayout = new RowLayout();
		rowLayout.marginWidth = 20;
		rowLayout.marginTop = 6;
		selection.setLayout(rowLayout);
		selection.setBackground(Couleur.gris);

		Button boutonCreer = new Button(selection, SWT.CENTER);
		boutonCreer.setText("Créer");
		boutonCreer.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				vueEmployeCreer();
			}
		});

		Button boutonCouts = new Button(selection, SWT.CENTER);
		boutonCouts.setText("Gérer les couts employés");
		boutonCouts.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				vueCompleteCouts("");
			}
		});
		
		Button boutonAncienEmploye = new Button(selection, SWT.CENTER);
		boutonAncienEmploye.setText("Récupérer ancien employé");
		boutonAncienEmploye.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				vueRecup();
			}
			
		});
		selection.pack();
	}

	/***
	 * Creation de la partie Selection (la partie superieure droite) avec les
	 * boutons Creer, Modifier et Supprimer
	 * 
	 * @param composite : composite vueEmploye
	 * @param table : la table affichant tous les employes 
	 */
	public void compositeSelectionModifier(Table table) {
		if (!Objects.isNull(selection) && !selection.isDisposed()) {
			selection.dispose();
		}

		selection = new Composite(vueEmploye, SWT.NONE);
		RowLayout rowLayout = new RowLayout();
		rowLayout.marginWidth = 22;
		rowLayout.marginTop = 6;
		selection.setLayout(rowLayout);
		selection.setBackground(Couleur.gris);

		// Bouton Creer
		Button boutonCreer = new Button(selection, SWT.CENTER);
		boutonCreer.setText("Créer");
		boutonCreer.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				vueEmployeCreer();
			}
		});

		// Bouton Modifier
		Button boutonModifier = new Button(selection, SWT.CENTER);
		boutonModifier.setText("Modifier");
		boutonModifier.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				vueEmployeModifier();
			}
		});

		// Bouton Supprimer
		Button boutonSupprimer = new Button(selection, SWT.CENTER);
		boutonSupprimer.setText("Supprimer");
		boutonSupprimer.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				try {
					if (selectedEmploye == null) {
						throw new Error("selectedEmploye est vide");
					}
					Employee e = Employee.getEmployeById(selectedEmploye.getEmployeId());

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

		Button boutonCouts = new Button(selection, SWT.CENTER);
		boutonCouts.setText("Gérer les couts employés");
		boutonCouts.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				vueCompleteCouts("");
			}
		});
		
		
		Button boutonAncienEmploye = new Button(selection, SWT.CENTER);
		boutonAncienEmploye.setText("Récupérer ancien employé");
		boutonAncienEmploye.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				vueRecup();
			}
			
		});
		selection.pack();
	}

	
	public void vueRecup() {
		if (!Objects.isNull(selection) && !selection.isDisposed()) {
			selection.dispose();
		}
		selection = new Composite(vueEmploye, SWT.CENTER);

		FillLayout fillLayout = new FillLayout();
		fillLayout.type = SWT.VERTICAL;
		fillLayout.marginWidth = 26;
		selection.setLayout(fillLayout);

		// juste pour creer un espace
		Label l1 = new Label(selection, SWT.NONE);
		l1.setText("");
		l1.setBackground(Couleur.bleuFonce);

		selection.setBackground(Couleur.bleuFonce);
		Label HeadLabel = new Label(selection, SWT.TITLE);
		HeadLabel.setText("Récupération d'un Employe");
		Font fontTitle = new Font(HeadLabel.getDisplay(), "Arial", 12, SWT.BOLD);
		HeadLabel.setForeground(Couleur.bleuClair);
		HeadLabel.setFont(fontTitle);
		HeadLabel.setBackground(Couleur.bleuFonce);

		// juste pour creer un espace
		Label l2 = new Label(selection, SWT.NONE);
		l2.setText("");
		l2.setBackground(Couleur.bleuFonce);

		selection.pack();
		///////////////////////////////////////////////////////////////////////////////////////
		if (!Objects.isNull(vue) && !vue.isDisposed()) {
			vue.dispose();
		}

		// creation de la vue
		vue = new Composite(vueEmploye, SWT.NONE);
		FillLayout fillLayoutH = new FillLayout();
		fillLayoutH.type = SWT.HORIZONTAL;
		vue.setLayout(fillLayoutH);
		
		// on cree trois colonne pour repartir les champs
		FillLayout fillLayoutV = new FillLayout();
		fillLayoutV.type = SWT.VERTICAL;
		fillLayoutV.marginWidth = 10;
		Composite colonne1 = new Composite(vue, SWT.BORDER);
		colonne1.setBackground(Couleur.bleuClair);
		colonne1.setLayout(fillLayoutV);

		// utilisé pour tous les composites des arguments
		FillLayout fillLayoutH5 = new FillLayout();
		fillLayoutH5.marginHeight = 30;
		fillLayoutH5.spacing = 5;
		fillLayoutH5.type = SWT.HORIZONTAL;
		
		// titre
		Composite compositeTitre = new Composite(colonne1, SWT.NONE);
		compositeTitre.setBackground(Couleur.bleuClair);
		compositeTitre.setLayout(fillLayoutH5);

		Label labelTitre = new Label(compositeTitre, SWT.NONE);
		labelTitre.setBackground(Couleur.bleuClair);
		labelTitre.setText("Merci de renseigner un matricule ou un nom\net un prénom.");

		// Nom
		Composite compositeNom = new Composite(colonne1, SWT.NONE);
		compositeNom.setBackground(Couleur.bleuClair);
		compositeNom.setLayout(fillLayoutH5);

		Label labelNom = new Label(compositeNom, SWT.NONE);
		labelNom.setBackground(Couleur.bleuClair);
		labelNom.setText("Nom :                            ");

		final Text textNom = new Text(compositeNom, SWT.BORDER);
		textNom.setText("");

		// prenom
		Composite compositePrenom = new Composite(colonne1, SWT.NONE);
		compositePrenom.setBackground(Couleur.bleuClair);
		compositePrenom.setLayout(fillLayoutH5);

		Label labelPrenom = new Label(compositePrenom, SWT.NONE);
		labelPrenom.setBackground(Couleur.bleuClair);
		labelPrenom.setText("Prénom : ");

		final Text textPrenom = new Text(compositePrenom, SWT.BORDER);
		textPrenom.setText("");
		
		// Matricule
		Composite compositeMatricule = new Composite(colonne1, SWT.NONE);
		compositeMatricule.setBackground(Couleur.bleuClair);
		compositeMatricule.setLayout(fillLayoutH5);

		Label labelMatricule = new Label(compositeMatricule, SWT.NONE);
		labelMatricule.setBackground(Couleur.bleuClair);
		labelMatricule.setText("Matricule : ");

		final Text textMatricule = new Text(compositeMatricule, SWT.BORDER);
		textMatricule.setText("");

		// Boutons
		Composite compositeBoutons = new Composite(colonne1, SWT.CENTER);
		compositeBoutons.setBackground(Couleur.bleuClair);
		compositeBoutons.setLayout(fillLayoutH5);

		Button buttonAnnulation = new Button(compositeBoutons, SWT.BACKGROUND);
		buttonAnnulation.setText("Annuler");
		buttonAnnulation.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				newVueEmploye();
			}
		});

		Button buttonValidation = new Button(compositeBoutons, SWT.BACKGROUND);
		buttonValidation.setText("Valider");
		buttonValidation.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent arg0) {

				try {
					if (!textMatricule.getText().isBlank()) {
						Employee.retrieveByMatricule(textMatricule.getText());
					}
					else if (!textNom.getText().isBlank() && !textPrenom.getText().isBlank()) {
						Employee.retrieveByNomPrenom(textNom.getText(),textPrenom.getText());
					}
					else {
						throw new Error("Merci d'indiquer un nom et prénom ou un matricule.");
					}
					System.out.println("on a recuperer employe !!");
					MessageBox dialog = new MessageBox(vueEmploye.getShell(), SWT.ICON_INFORMATION | SWT.OK);
					dialog.setText("Récupération réussie");
					dialog.setMessage("L'employé a été rajouté à la base de données.");
					dialog.open();
					newVueEmploye();
				} catch (Throwable e) {
					e.printStackTrace();
					System.out.println("erreur dans la recup");
					MessageBox dialog = new MessageBox(vueEmploye.getShell(), SWT.ICON_ERROR | SWT.OK);
					dialog.setText("Erreur Récupération");
					dialog.setMessage(
							"Une erreur est survenue. " + '\n' + e.getMessage());
					dialog.open();
				}
			}
		});
		
		vue.pack();
		vueEmploye.pack();
		vueEmploye.getParent().pack();
	}

	public void vueCompleteCouts(String periode) {

		selectionCouts();
		vueCouts();

		vueEmploye.pack();
		vueEmploye.getParent().pack();

	}

	public void selectionCouts() {
		if (!Objects.isNull(selection) && !selection.isDisposed()) {
			selection.dispose();
		}

		selection = new Composite(vueEmploye, SWT.NONE);
		RowLayout rowLayout = new RowLayout();
		rowLayout.marginWidth = 22;
		rowLayout.marginTop = 6;
		rowLayout.spacing = 60;
		selection.setLayout(rowLayout);
		selection.setBackground(Couleur.gris);

		FillLayout fillLayoutH5 = new FillLayout();
		fillLayoutH5.type = SWT.HORIZONTAL;

		Composite compositePeriode = new Composite(selection, SWT.NONE);
		compositePeriode.setLayout(fillLayoutH5);

		Label labelPeriode = new Label(compositePeriode, SWT.NONE);
		labelPeriode.setText("Periode : ");

		Combo periode = new Combo(compositePeriode, SWT.BORDER);
		LocalDate currentdate = LocalDate.now();
		Month month = currentdate.getMonth();
		int year = currentdate.getYear();

		periode.setText(month.toString()+" "+year);

		for(int i = 2020; i <= year+1 ; i++) {
			for (int j =1 ; j <= 12 ; j++) {
				periode.add(Month.of(j)+" "+i);
			}
		}
		periode.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				updateCoutsTable(periode.getText());
			}
		});


		Button boutonMoisPrecedent = new Button(selection, SWT.CENTER);
		boutonMoisPrecedent.setText("Récupérer les valeurs précédentes");
		boutonMoisPrecedent.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				try {
					String[] moisAnnee = periode.getText().split(" ");
					Integer mois = Month.valueOf(moisAnnee[0]).getValue();
					Integer annee = Integer.parseInt(moisAnnee[1]);
					if (mois == 1) {
						mois = 12;
						annee -= 1;
					}
					else {mois -= 1;}
					System.out.println(mois.toString()+" "+annee.toString());
					for(CoutsEmploye ce : CoutsEmploye.getAllCoutEmploye()) {
						if(mois == ce.getMois() && (Integer.compare(annee, ce.getAnnee())==0)) {
							CoutsEmploye newCE = ce;
							if (ce.getMois() <12) { 
								newCE.setMois(ce.getMois()+1); 
								newCE.setAnnee(ce.getAnnee());
							}
							else { 
								newCE.setMois(1);
								newCE.setAnnee(ce.getAnnee()+1);
							}
							try {
								newCE.insertDatabase();
							}catch(Exception e) {
							}
						}
					}
				} catch (SQLException e) {
					System.out.println("erreur dans la table des couts employes");
					MessageBox dialog = new MessageBox(vueEmploye.getShell(), SWT.ICON_ERROR | SWT.OK);
					dialog.setText("Erreur");
					dialog.setMessage("Une erreur est survenue. "+'\n'+e.getMessage());
					dialog.open();
				}
				updateCoutsTable(periode.getText());

			}
		});

		Button boutonGestionCouts = new Button(selection, SWT.CENTER);
		boutonGestionCouts.setText("Gérer les couts à amortir");
		boutonGestionCouts.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				//TODO
			}
		});

		selection.pack();
	}


	public void vueCouts() {
		if (!Objects.isNull(vue) && !vue.isDisposed()) {
			vue.dispose();
		}

		vue = new Composite(vueEmploye, SWT.NONE);
		RowLayout rowLayoutH = new RowLayout();
		rowLayoutH.type = SWT.VERTICAL;
		vue.setLayout(rowLayoutH);

		LocalDate currentdate = LocalDate.now();
		Month month = currentdate.getMonth();
		int year = currentdate.getYear();

		//creation de la table des produits
		tableCouts = new Table (vue, SWT.BORDER | SWT.MULTI| SWT.V_SCROLL | SWT.FULL_SELECTION);
		tableCouts.setLayoutData(new RowData(1045,530));
		tableCouts.setLinesVisible (true);
		tableCouts.setHeaderVisible (true);

		//on met les noms des colonnes
		String[] titles = {"Id", "Nom" ,"Prenom","Matricule" ,"Période", "salaire Net","salaire Brut","nombreHeures",
				"charges patronales","masse salariale","menage","vitrerie","fournituresSanitaires",
				"misesBlanc","Transport","Telephone","mutuelle","paniers", "prets","saisie arret", "autres"};
		for (String title : titles) {
			TableColumn column = new TableColumn (tableCouts, SWT.NONE);
			column.setText (title);
		}

		updateCoutsTable(month.toString()+" "+year);

		menu = new Menu(vueEmploye.getShell(), SWT.POP_UP);
		tableCouts.setMenu(menu);

		// pour supprimer
		MenuItem delete = new MenuItem(menu, SWT.PUSH);
		delete.setText("Supprimer l'element");
		delete.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				try {
					TableItem item = (TableItem) tableCouts.getSelection()[0];
					MessageBox dialog = new MessageBox(vueEmploye.getShell(), SWT.ICON_QUESTION | SWT.YES | SWT.NO);
					dialog.setText("Suppression Cout Employe");
					dialog.setMessage("Voulez vous supprimer les couts de l'employé " + item.getText(1) + " " + item.getText(2)+ " ?");
					int buttonID = dialog.open();
					switch (buttonID) {
					case SWT.YES:
						int id = Integer.parseInt(item.getText(0));
						CoutsEmploye ce = CoutsEmploye.getCoutEmployeById(id);
						ce.setStatus("Archivé");
						ce.updateDatabase();
						
						String[] moisAnnee = item.getText(4).split("/");
						String periode = Month.of(Integer.parseInt(moisAnnee[0])).toString()+" "+((Integer)Integer.parseInt(moisAnnee[1])).toString();
						updateCoutsTable(periode);
					}

				} catch (NumberFormatException | SQLException e1) {
					System.out.println("erreur pour supprimer l'employe");
					MessageBox dialog = new MessageBox(vueEmploye.getShell(), SWT.ICON_ERROR | SWT.OK);
					dialog.setText("Erreur");
					dialog.setMessage("Une erreur est survenue. " + '\n' + e1.getMessage());
					dialog.open();
				}
			}
		});
		
		//validation
		Composite validation = new Composite(vue, SWT.NONE);
		RowLayout rowLayout = new RowLayout();
		rowLayout.marginWidth = 22;
		rowLayout.marginTop = 6;
		rowLayout.spacing = 20;
		validation.setLayout(rowLayout);
		validation.setBackground(Couleur.gris);

		Button boutonAnnuler = new Button(validation, SWT.CENTER);
		boutonAnnuler.setText("    Retour aux employés    ");
		boutonAnnuler.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				newVueEmploye();
			}
		});

		Button boutonValider = new Button(validation, SWT.CENTER);
		boutonValider.setText("     Sauvegarder     ");
		boutonValider.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				for (int ligne = 0; ligne < tableCouts.getItems().length; ligne++){
					System.out.println(tableCouts.getItem(ligne).getText(0));
					Integer id = Integer.parseInt(tableCouts.getItem(ligne).getText(0));
					CoutsEmploye ce;
					try {
						ce = CoutsEmploye.getCoutEmployeById(id);
						ce.setSalaireNet(Double.parseDouble(tableCouts.getItem(ligne).getText(5)));
						ce.setSalaireBrut(Double.parseDouble(tableCouts.getItem(ligne).getText(6)));
						ce.setNombreHeures(Double.parseDouble(tableCouts.getItem(ligne).getText(7)));
						ce.setChargesP(Double.parseDouble(tableCouts.getItem(ligne).getText(8)));
						ce.setMasseS(Double.parseDouble(tableCouts.getItem(ligne).getText(9)));
						ce.setMenage(Double.parseDouble(tableCouts.getItem(ligne).getText(10)));
						ce.setVitrerie(Double.parseDouble(tableCouts.getItem(ligne).getText(11)));
						ce.setFournituresSanitaires(Double.parseDouble(tableCouts.getItem(ligne).getText(12)));
						ce.setMisesBlanc(Double.parseDouble(tableCouts.getItem(ligne).getText(13)));
						ce.setRemboursementTransport(Double.parseDouble(tableCouts.getItem(ligne).getText(14)));
						ce.setRemboursementTelephone(Double.parseDouble(tableCouts.getItem(ligne).getText(15)));
						ce.setMutuelle(Double.parseDouble(tableCouts.getItem(ligne).getText(16)));
						ce.setPaniers(Double.parseDouble(tableCouts.getItem(ligne).getText(17)));
						ce.setPrets(Double.parseDouble(tableCouts.getItem(ligne).getText(18)));
						ce.setAutres(Double.parseDouble(tableCouts.getItem(ligne).getText(20)));
						ce.setSaisieArret(Double.parseDouble(tableCouts.getItem(ligne).getText(19)));
						ce.updateDatabase();
					} catch (SQLException e) {
						System.out.println("erreur dans la table des couts employes");
						MessageBox dialog = new MessageBox(vueEmploye.getShell(), SWT.ICON_ERROR | SWT.OK);
						dialog.setText("Erreur");
						dialog.setMessage("Une erreur est survenue. "+'\n'+e.getMessage());
						dialog.open();
					}
				}

				try {
					String[] moisAnnee = tableCouts.getItem(0).getText(4).split("/");
					String periode = Month.of(Integer.parseInt(moisAnnee[0])).toString()+" "+((Integer)Integer.parseInt(moisAnnee[1])).toString();
					System.out.println("on a modifie les couts employe !!");
					MessageBox dialog = new MessageBox(vueEmploye.getShell(), SWT.ICON_INFORMATION | SWT.OK);
					dialog.setText("Modification réussie");
					dialog.setMessage("Les couts employé ont bien été modifiés dans la base de données.");
					dialog.open();
					updateCoutsTable(periode);
				} catch (Exception e) {
					System.out.println("erreur dans la table des couts employes");
					MessageBox dialog = new MessageBox(vueEmploye.getShell(), SWT.ICON_ERROR | SWT.OK);
					dialog.setText("Erreur");
					dialog.setMessage("Une erreur est survenue. "+'\n'+e.getMessage());
					dialog.open();
				}
			}
		});

		validation.pack();
		vue.pack();
	}


	public void updateCoutsTable(String periode) {
		if(!Objects.isNull(tableCouts)) {
			tableCouts.removeAll();
		}

		//on remplit la table
		final TableColumn [] columns = tableCouts.getColumns();

		String[] moisAnnee = periode.split(" ");
		String mois = moisAnnee[0];
		String annee = moisAnnee[1];
		Month moisInt = Month.valueOf(mois);

		try {
			for (CoutsEmploye ce : CoutsEmploye.getAllCoutEmploye()) {
				//on verifie le status
				if (ce.getStatus().equals("Publié") && ce.getAnnee()==Integer.parseInt(annee) && ce.getMois()==moisInt.getValue()) {
					TableItem item = new TableItem (tableCouts, SWT.NONE);
					item.setText(0,ce.getCoutEmployeId().toString());
					item.setText(1,Employee.getEmployeById(ce.getEmployeId()).getNom());
					item.setText(2,Employee.getEmployeById(ce.getEmployeId()).getPrenom());
					item.setText(3,Employee.getEmployeById(ce.getEmployeId()).getNumeroMatricule());
					String p = ce.getMois().toString()+'/'+ce.getAnnee().toString();
					item.setText(4,p);
					item.setText(5,ce.getSalaireNet().toString());
					item.setText(6,ce.getSalaireBrut().toString());
					item.setText(7,ce.getNombreHeures().toString());
					item.setText(8,ce.getChargesP().toString());
					item.setText(9,ce.getMasseS().toString());
					item.setText(10,ce.getMenage().toString());
					item.setText(11,ce.getVitrerie().toString());
					item.setText(12,ce.getFournituresSanitaires().toString());
					item.setText(13,ce.getMisesBlanc().toString());
					item.setText(14,ce.getRemboursementTransport().toString());
					item.setText(15,ce.getRemboursementTelephone().toString());
					item.setText(16,ce.getMutuelle().toString());
					item.setText(17,ce.getPaniers().toString());
					item.setText(18,ce.getPrets().toString());
					item.setText(19,ce.getSaisieArret().toString());
					item.setText(20,ce.getAutres().toString());
				}
			}
		} catch (SQLException e1) {
			System.out.println("erreur dans la table des couts employes");
			MessageBox dialog = new MessageBox(vueEmploye.getShell(), SWT.ICON_ERROR | SWT.OK);
			dialog.setText("Erreur");
			dialog.setMessage("Une erreur est survenue. "+'\n'+e1.getMessage());
			dialog.open();
		}

		//on pack les colonnes
		for (TableColumn col : columns)
			col.pack ();

		tableCouts.addSelectionListener(getListener());

		//TODO
		//tableCouts.setSelection(1);
		//tableCouts.setFocus();
		//tableCouts.selectAll();
		//tableCouts.select(1);tableCouts.forceFocus();

		vue.pack();
	}


	public SelectionAdapter getListener() {
		editors = new ArrayList<TableEditor>();

		//CREATION DES EDITOR POUR CHAQUE COLONNE
		final TableEditor editorSN = new TableEditor (tableCouts);
		editorSN.horizontalAlignment = SWT.LEFT;editorSN.grabHorizontal = true;editors.add(editorSN);

		final TableEditor editorSB = new TableEditor (tableCouts);
		editorSB.horizontalAlignment = SWT.LEFT;editorSB.grabHorizontal = true;editors.add(editorSB);

		final TableEditor editorNH = new TableEditor (tableCouts);
		editorNH.horizontalAlignment = SWT.LEFT;editorNH.grabHorizontal = true;editors.add(editorNH);

		final TableEditor editorCP = new TableEditor (tableCouts);
		editorCP.horizontalAlignment = SWT.LEFT;editorCP.grabHorizontal = true;editors.add(editorCP);

		final TableEditor editorMS = new TableEditor (tableCouts);
		editorMS.horizontalAlignment = SWT.LEFT;editorMS.grabHorizontal = true;editors.add(editorMS);

		final TableEditor editorMe = new TableEditor (tableCouts);
		editorMe.horizontalAlignment = SWT.LEFT;editorMe.grabHorizontal = true;editors.add(editorMe);

		final TableEditor editorV = new TableEditor (tableCouts);
		editorV.horizontalAlignment = SWT.LEFT;editorV.grabHorizontal = true;editors.add(editorV);

		final TableEditor editorFS = new TableEditor (tableCouts);
		editorFS.horizontalAlignment = SWT.LEFT;editorFS.grabHorizontal = true;editors.add(editorFS);

		final TableEditor editorMB = new TableEditor (tableCouts);
		editorMB.horizontalAlignment = SWT.LEFT;editorMB.grabHorizontal = true;editors.add(editorMB);

		final TableEditor editorTr = new TableEditor (tableCouts);
		editorTr.horizontalAlignment = SWT.LEFT;editorTr.grabHorizontal = true;editors.add(editorTr);

		final TableEditor editorTe = new TableEditor (tableCouts);
		editorTe.horizontalAlignment = SWT.LEFT;editorTe.grabHorizontal = true;editors.add(editorTe);

		final TableEditor editorMu = new TableEditor (tableCouts);
		editorMu.horizontalAlignment = SWT.LEFT;editorMu.grabHorizontal = true;editors.add(editorMu);

		final TableEditor editorPa = new TableEditor (tableCouts);
		editorPa.horizontalAlignment = SWT.LEFT;editorPa.grabHorizontal = true;editors.add(editorPa);

		final TableEditor editorPr = new TableEditor (tableCouts);
		editorPr.horizontalAlignment = SWT.LEFT;editorPr.grabHorizontal = true;editors.add(editorPr);

		final TableEditor editorSA = new TableEditor (tableCouts);
		editorSA.horizontalAlignment = SWT.LEFT;editorSA.grabHorizontal = true;editors.add(editorSA);

		final TableEditor editorA = new TableEditor (tableCouts);
		editorA.horizontalAlignment = SWT.LEFT;editorA.grabHorizontal = true;editors.add(editorA);

		SelectionAdapter listener = new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				//on dispose les anciens editors
				if (editorSN.getEditor() != null) { editorSN.getEditor().dispose();}
				if (editorSB.getEditor() != null) { editorSB.getEditor().dispose();}
				if (editorNH.getEditor() != null) { editorNH.getEditor().dispose();}
				if (editorCP.getEditor() != null) { editorCP.getEditor().dispose();}
				if (editorMS.getEditor() != null) { editorMS.getEditor().dispose();}
				if (editorMe.getEditor() != null) { editorMe.getEditor().dispose();}
				if (editorV.getEditor() != null) { editorV.getEditor().dispose();}
				if (editorFS.getEditor() != null) { editorFS.getEditor().dispose();}
				if (editorMB.getEditor() != null) { editorMB.getEditor().dispose();}
				if (editorTr.getEditor() != null) { editorTr.getEditor().dispose();}
				if (editorTe.getEditor() != null) { editorTe.getEditor().dispose();}
				if (editorMu.getEditor() != null) { editorMu.getEditor().dispose();}
				if (editorPa.getEditor() != null) { editorPa.getEditor().dispose();}
				if (editorPr.getEditor() != null) { editorPr.getEditor().dispose();}
				if (editorSA.getEditor() != null) { editorSA.getEditor().dispose();}
				if (editorA.getEditor() != null) { editorA.getEditor().dispose();}

				//on recupere la ligne selectionne
				TableItem item = (TableItem) e.item;
				if (item == null) { return; }

				//editors --------------------------------------------------------------------------
				Text newEditorSN = new Text(tableCouts, SWT.NONE);
				newEditorSN.setText(item.getText(5));
				newEditorSN.addModifyListener(new ModifyListener() { 
					public void modifyText(ModifyEvent me) { 
						if (!(newEditorSN.getText().isEmpty())) {//pour ne pas tester quand l'utilisateur est en train de modifier
							try {
								item.setText(5,newEditorSN.getText());
							} catch(Exception e) {
								System.out.println("erreur dans la modif"); MessageBox dialog = new MessageBox(vueEmploye.getShell(), SWT.ICON_ERROR | SWT.OK);
								dialog.setText("Erreur Editor"); dialog.setMessage("Saisie invalide."+'\n'+e.getMessage()); dialog.open(); 
							}
						}
					}
				});
				editorSN.setEditor(newEditorSN, item, 5);


				Text newEditorSB = new Text(tableCouts, SWT.NONE);
				newEditorSB.setText(item.getText(6));
				newEditorSB.addModifyListener(new ModifyListener() { 
					public void modifyText(ModifyEvent me) { 
						if (!(newEditorSB.getText().isEmpty())) {//pour ne pas tester quand l'utilisateur est en train de modifier
							try {
								item.setText(6,newEditorSB.getText());
							} catch(Exception e) {
								System.out.println("erreur dans la modif"); MessageBox dialog = new MessageBox(vueEmploye.getShell(), SWT.ICON_ERROR | SWT.OK);
								dialog.setText("Erreur Editor"); dialog.setMessage("Saisie invalide."+'\n'+e.getMessage()); dialog.open(); 
							}
						}
					}
				});
				editorSB.setEditor(newEditorSB, item, 6);


				Text newEditorNH = new Text(tableCouts, SWT.NONE);
				newEditorNH.setText(item.getText(7));
				newEditorNH.addModifyListener(new ModifyListener() { 
					public void modifyText(ModifyEvent me) { 
						if (!(newEditorNH.getText().isEmpty())) {//pour ne pas tester quand l'utilisateur est en train de modifier
							try {
								item.setText(7,newEditorNH.getText());
							} catch(Exception e) {
								System.out.println("erreur dans la modif"); MessageBox dialog = new MessageBox(vueEmploye.getShell(), SWT.ICON_ERROR | SWT.OK);
								dialog.setText("Erreur Editor"); dialog.setMessage("Saisie invalide."+'\n'+e.getMessage()); dialog.open(); 
							}
						}
					}
				});
				editorNH.setEditor(newEditorNH, item, 7);


				Text newEditorCP = new Text(tableCouts, SWT.NONE);
				newEditorCP.setText(item.getText(8));
				newEditorCP.addModifyListener(new ModifyListener() { 
					public void modifyText(ModifyEvent me) { 
						if (!(newEditorCP.getText().isEmpty())) {//pour ne pas tester quand l'utilisateur est en train de modifier
							try {
								item.setText(8,newEditorCP.getText());
							} catch(Exception e) {
								System.out.println("erreur dans la modif"); MessageBox dialog = new MessageBox(vueEmploye.getShell(), SWT.ICON_ERROR | SWT.OK);
								dialog.setText("Erreur Editor"); dialog.setMessage("Saisie invalide."+'\n'+e.getMessage()); dialog.open(); 
							}
						}
					}
				});
				editorCP.setEditor(newEditorCP, item, 8);


				Text newEditorMS = new Text(tableCouts, SWT.NONE);
				newEditorMS.setText(item.getText(9));
				newEditorMS.addModifyListener(new ModifyListener() { 
					public void modifyText(ModifyEvent me) { 
						if (!(newEditorMS.getText().isEmpty())) {//pour ne pas tester quand l'utilisateur est en train de modifier
							try {
								item.setText(9,newEditorMS.getText());
							} catch(Exception e) {
								System.out.println("erreur dans la modif"); MessageBox dialog = new MessageBox(vueEmploye.getShell(), SWT.ICON_ERROR | SWT.OK);
								dialog.setText("Erreur Editor"); dialog.setMessage("Saisie invalide."+'\n'+e.getMessage()); dialog.open(); 
							}
						}
					}
				});
				editorMS.setEditor(newEditorMS, item, 9);


				Text newEditorMe = new Text(tableCouts, SWT.NONE);
				newEditorMe.setText(item.getText(10));
				newEditorMe.addModifyListener(new ModifyListener() { 
					public void modifyText(ModifyEvent me) { 
						if (!(newEditorMe.getText().isEmpty())) {//pour ne pas tester quand l'utilisateur est en train de modifier
							try {
								item.setText(10,newEditorMe.getText());
							} catch(Exception e) {
								System.out.println("erreur dans la modif"); MessageBox dialog = new MessageBox(vueEmploye.getShell(), SWT.ICON_ERROR | SWT.OK);
								dialog.setText("Erreur Editor"); dialog.setMessage("Saisie invalide."+'\n'+e.getMessage()); dialog.open(); 
							}
						}
					}
				});
				editorMe.setEditor(newEditorMe, item, 10);


				Text newEditorV = new Text(tableCouts, SWT.NONE);
				newEditorV.setText(item.getText(11));
				newEditorV.addModifyListener(new ModifyListener() { 
					public void modifyText(ModifyEvent me) { 
						if (!(newEditorV.getText().isEmpty())) {//pour ne pas tester quand l'utilisateur est en train de modifier
							try {
								item.setText(11,newEditorV.getText());
							} catch(Exception e) {
								System.out.println("erreur dans la modif"); MessageBox dialog = new MessageBox(vueEmploye.getShell(), SWT.ICON_ERROR | SWT.OK);
								dialog.setText("Erreur Editor"); dialog.setMessage("Saisie invalide."+'\n'+e.getMessage()); dialog.open(); 
							}
						}
					}
				});
				editorV.setEditor(newEditorV, item, 11);


				Text newEditorFS= new Text(tableCouts, SWT.NONE);
				newEditorFS.setText(item.getText(12));
				newEditorFS.addModifyListener(new ModifyListener() { 
					public void modifyText(ModifyEvent me) { 
						if (!(newEditorFS.getText().isEmpty())) {//pour ne pas tester quand l'utilisateur est en train de modifier
							try {
								item.setText(12,newEditorFS.getText());
							} catch(Exception e) {
								System.out.println("erreur dans la modif"); MessageBox dialog = new MessageBox(vueEmploye.getShell(), SWT.ICON_ERROR | SWT.OK);
								dialog.setText("Erreur Editor"); dialog.setMessage("Saisie invalide."+'\n'+e.getMessage()); dialog.open(); 
							}
						}
					}
				});
				editorFS.setEditor(newEditorFS, item, 12);


				Text newEditorMB= new Text(tableCouts, SWT.NONE);
				newEditorMB.setText(item.getText(13));
				newEditorMB.addModifyListener(new ModifyListener() { 
					public void modifyText(ModifyEvent me) { 
						if (!(newEditorMB.getText().isEmpty())) {//pour ne pas tester quand l'utilisateur est en train de modifier
							try {
								item.setText(13,newEditorMB.getText());
							} catch(Exception e) {
								System.out.println("erreur dans la modif"); MessageBox dialog = new MessageBox(vueEmploye.getShell(), SWT.ICON_ERROR | SWT.OK);
								dialog.setText("Erreur Editor"); dialog.setMessage("Saisie invalide."+'\n'+e.getMessage()); dialog.open(); 
							}
						}
					}
				});
				editorMB.setEditor(newEditorMB, item, 13);


				Text newEditorTr= new Text(tableCouts, SWT.NONE);
				newEditorTr.setText(item.getText(14));
				newEditorTr.addModifyListener(new ModifyListener() { 
					public void modifyText(ModifyEvent me) { 
						if (!(newEditorTr.getText().isEmpty())) {//pour ne pas tester quand l'utilisateur est en train de modifier
							try {
								item.setText(14,newEditorTr.getText());
							} catch(Exception e) {
								System.out.println("erreur dans la modif"); MessageBox dialog = new MessageBox(vueEmploye.getShell(), SWT.ICON_ERROR | SWT.OK);
								dialog.setText("Erreur Editor"); dialog.setMessage("Saisie invalide."+'\n'+e.getMessage()); dialog.open(); 
							}
						}
					}
				});
				editorTr.setEditor(newEditorTr, item, 14);


				Text newEditorTe= new Text(tableCouts, SWT.NONE);
				newEditorTe.setText(item.getText(15));
				newEditorTe.addModifyListener(new ModifyListener() { 
					public void modifyText(ModifyEvent me) { 
						if (!(newEditorTe.getText().isEmpty())) {//pour ne pas tester quand l'utilisateur est en train de modifier
							try {
								item.setText(15,newEditorTe.getText());
							} catch(Exception e) {
								System.out.println("erreur dans la modif"); MessageBox dialog = new MessageBox(vueEmploye.getShell(), SWT.ICON_ERROR | SWT.OK);
								dialog.setText("Erreur Editor"); dialog.setMessage("Saisie invalide."+'\n'+e.getMessage()); dialog.open(); 
							}
						}
					}
				});
				editorTe.setEditor(newEditorTe, item, 15);


				Text newEditorMu= new Text(tableCouts, SWT.NONE);
				newEditorMu.setText(item.getText(16));
				newEditorMu.addModifyListener(new ModifyListener() { 
					public void modifyText(ModifyEvent me) { 
						if (!(newEditorMu.getText().isEmpty())) {//pour ne pas tester quand l'utilisateur est en train de modifier
							try {
								item.setText(16,newEditorMu.getText());
							} catch(Exception e) {
								System.out.println("erreur dans la modif"); MessageBox dialog = new MessageBox(vueEmploye.getShell(), SWT.ICON_ERROR | SWT.OK);
								dialog.setText("Erreur Editor"); dialog.setMessage("Saisie invalide."+'\n'+e.getMessage()); dialog.open(); 
							}
						}
					}
				});
				editorMu.setEditor(newEditorMu, item, 16);


				Text newEditorPa= new Text(tableCouts, SWT.NONE);
				newEditorPa.setText(item.getText(17));
				newEditorPa.addModifyListener(new ModifyListener() { 
					public void modifyText(ModifyEvent me) { 
						if (!(newEditorPa.getText().isEmpty())) {//pour ne pas tester quand l'utilisateur est en train de modifier
							try {
								item.setText(17,newEditorPa.getText());
							} catch(Exception e) {
								System.out.println("erreur dans la modif"); MessageBox dialog = new MessageBox(vueEmploye.getShell(), SWT.ICON_ERROR | SWT.OK);
								dialog.setText("Erreur Editor"); dialog.setMessage("Saisie invalide."+'\n'+e.getMessage()); dialog.open(); 
							}
						}
					}
				});
				editorPa.setEditor(newEditorPa, item, 17);


				Text newEditorPr= new Text(tableCouts, SWT.NONE);
				newEditorPr.setText(item.getText(18));
				newEditorPr.addModifyListener(new ModifyListener() { 
					public void modifyText(ModifyEvent me) { 
						if (!(newEditorPr.getText().isEmpty())) {//pour ne pas tester quand l'utilisateur est en train de modifier
							try {
								item.setText(18,newEditorPr.getText());
							} catch(Exception e) {
								System.out.println("erreur dans la modif"); MessageBox dialog = new MessageBox(vueEmploye.getShell(), SWT.ICON_ERROR | SWT.OK);
								dialog.setText("Erreur Editor"); dialog.setMessage("Saisie invalide."+'\n'+e.getMessage()); dialog.open(); 
							}
						}
					}
				});
				editorPr.setEditor(newEditorPr, item, 18);


				Text newEditorSA= new Text(tableCouts, SWT.NONE);
				newEditorSA.setText(item.getText(19));
				newEditorSA.addModifyListener(new ModifyListener() { 
					public void modifyText(ModifyEvent me) { 
						if (!(newEditorSA.getText().isEmpty())) {//pour ne pas tester quand l'utilisateur est en train de modifier
							try {
								item.setText(19,newEditorSA.getText());
							} catch(Exception e) {
								System.out.println("erreur dans la modif"); MessageBox dialog = new MessageBox(vueEmploye.getShell(), SWT.ICON_ERROR | SWT.OK);
								dialog.setText("Erreur Editor"); dialog.setMessage("Saisie invalide."+'\n'+e.getMessage()); dialog.open(); 
							}
						}
					}
				});
				editorSA.setEditor(newEditorSA, item, 19);


				Text newEditorA= new Text(tableCouts, SWT.NONE);
				newEditorA.setText(item.getText(20));
				newEditorA.addModifyListener(new ModifyListener() { 
					public void modifyText(ModifyEvent me) { 
						if (!(newEditorA.getText().isEmpty())) {//pour ne pas tester quand l'utilisateur est en train de modifier
							try {
								item.setText(20,newEditorA.getText());
							} catch(Exception e) {
								System.out.println("erreur dans la modif"); MessageBox dialog = new MessageBox(vueEmploye.getShell(), SWT.ICON_ERROR | SWT.OK);
								dialog.setText("Erreur Editor"); dialog.setMessage("Saisie invalide."+'\n'+e.getMessage()); dialog.open(); 
							}
						}
					}
				});
				editorA.setEditor(newEditorA, item, 20);
			} 
		};
		return listener;
	}




	// Modification d'un employe --------------------------------------------------
	/***
	 * Regroupe les fonctions a appeler pour faire une modification 
	 * la fonciton va appeler
	 * les fonctions titreModification et formulaireModification
	 */
	public void vueEmployeModifier() {
		titreModification();
		formulaireModification();

		vueEmploye.pack();
		vueEmploye.getParent().pack();

	}

	/***
	 * va modifier la partie selection (partie superieure droite) en y mettant un
	 * titre pour la modification
	 */
	public void titreModification() {
		if (!Objects.isNull(selection) && !selection.isDisposed()) {
			selection.dispose();
		}
		selection = new Composite(vueEmploye, SWT.CENTER);

		FillLayout fillLayout = new FillLayout();
		fillLayout.type = SWT.VERTICAL;
		fillLayout.marginWidth = 182;
		selection.setLayout(fillLayout);

		// juste pour creer un espace
		Label l1 = new Label(selection, SWT.NONE);
		l1.setText("");
		l1.setBackground(Couleur.bleuFonce);

		selection.setBackground(Couleur.bleuFonce);
		Label HeadLabel = new Label(selection, SWT.TITLE);
		HeadLabel.setText("Modification d'un Employe");
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

	/***
	 * Va modifier la partie Vue (partie inferieure droite) et y ajoutant le
	 * formulaire de modification d'un employe grace a la methode showFormulaire
	 * 
	 */
	public void formulaireModification() {
		if (selectedEmploye == null) {
			throw new Error("selectedEmploye est vide");
		}

		if (!Objects.isNull(vue) && !vue.isDisposed()) {
			vue.dispose();
		}

		// creation de la vue
		vue = new Composite(vueEmploye, SWT.NONE);
		FillLayout fillLayoutH = new FillLayout();
		fillLayoutH.type = SWT.HORIZONTAL;
		vue.setLayout(fillLayoutH);

		showFormulaire(2,selectedEmploye.getTitre(),selectedEmploye.getNom(),selectedEmploye.getPrenom(),selectedEmploye.getMail(),selectedEmploye.getTelephone(),
				selectedEmploye.getNumeroMatricule().toString(),selectedEmploye.getPointure(),selectedEmploye.getTaille(),selectedEmploye.getDateArrivee());

		vue.pack();
	}

	/***
	 * modifie un employe dans la base de données
	 */
	public void validerModification() {
		if (selectedEmploye == null) {
			throw new Error("selectedEmploye est vide");
		}

		// on insert dans la base de données
		try {
			selectedEmploye.updateDatabase();
			System.out.println("on a modifie l employe !!");
			MessageBox dialog = new MessageBox(vueEmploye.getShell(), SWT.ICON_INFORMATION | SWT.OK);
			dialog.setText("Modification réussie");
			dialog.setMessage("L'employé a bien été modifié dans la base de données.");
			dialog.open();
			selectedEmploye = null;
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("erreur dans la modif");
			MessageBox dialog = new MessageBox(vueEmploye.getShell(), SWT.ICON_ERROR | SWT.OK);
			dialog.setText("Erreur Modification");
			dialog.setMessage("Une erreur est survenue lors de la modification de l'employé. " + e.getMessage());
			dialog.open();
		}

		newVueEmploye();
	}

	// Création d'un employe --------------------------------------------------
	/***
	 * va appeler les fonctions qui modifie la partie vue et la partie selection
	 * afin d'afficher le formulaire de création 
	 * appelle titreCreation et formulaireCreation
	 */
	public void vueEmployeCreer() {
		titreCreation();
		formulaireCreation();

		vueEmploye.pack();
		vueEmploye.getParent().pack();

	}

	/***
	 * modifie la partie selection (partie superieur droite) en ajoutant un titre de
	 * creation
	 */
	public void titreCreation() {
		if (!Objects.isNull(selection) && !selection.isDisposed()) {
			selection.dispose();
		}
		selection = new Composite(vueEmploye, SWT.CENTER);

		FillLayout fillLayout = new FillLayout();
		fillLayout.type = SWT.VERTICAL;
		fillLayout.marginWidth = 196;
		selection.setLayout(fillLayout);

		// juste pour creer un espace
		Label l1 = new Label(selection, SWT.NONE);
		l1.setText("");
		l1.setBackground(Couleur.bleuFonce);

		selection.setBackground(Couleur.bleuFonce);
		Label HeadLabel = new Label(selection, SWT.TITLE);
		HeadLabel.setText("Creation d'un Employe");
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

	/***
	 * cree la vue de modification et fait appel a showFormulaire pour afficher le formulaire de modification
	 */
	public void formulaireCreation() {
		if (!Objects.isNull(vue) && !vue.isDisposed()) {
			vue.dispose();
		}
		vue = new Composite(vueEmploye, SWT.NONE);
		FillLayout fillLayoutH = new FillLayout();
		fillLayoutH.type = SWT.HORIZONTAL;
		vue.setLayout(fillLayoutH);

		showFormulaire(1,"","","","","","","","","");

		vue.pack();
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
	 */
	@SuppressWarnings("unlikely-arg-type")
	public void showFormulaire(int type, String titre, String nom, String prenom, String mail, String telephone, String numeroMatricule, String pointure, String taille, String dateArrivee) {
		// on cree trois colonne pour repartir les champs
		FillLayout fillLayoutV = new FillLayout();
		fillLayoutV.type = SWT.VERTICAL;
		fillLayoutV.marginWidth = 10;
		Composite colonne1 = new Composite(vue, SWT.BORDER);
		Composite colonne2 = new Composite(vue, SWT.BORDER);
		colonne1.setBackground(Couleur.bleuClair);
		colonne2.setBackground(Couleur.bleuClair);
		colonne1.setLayout(fillLayoutV);
		colonne2.setLayout(fillLayoutV);

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

		Combo comboTitre = new Combo(compositeTitre, SWT.BORDER);
		if (titre.equals("M")) {
			comboTitre.setText("M");
		}
		else if (titre.equals("Mme")) {
			comboTitre.setText("Mme");
		}
		else {
			comboTitre.setText("Selectionner ...");
		}
		comboTitre.add("M");
		comboTitre.add("Mme");

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

		// Boutons
		Composite compositeBoutons = new Composite(colonne2, SWT.CENTER);
		compositeBoutons.setBackground(Couleur.bleuClair);
		compositeBoutons.setLayout(fillLayoutH5);

		Button buttonAnnulation = new Button(compositeBoutons, SWT.BACKGROUND);
		buttonAnnulation.setText("Annuler");
		buttonAnnulation.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				newVueEmploye();
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
						validerCreation(comboTitre.getText(), textNom.getText(), textPrenom.getText(),
								textNumeroMatricule.getText(), textMail.getText(), textTelephone.getText(),
								textPointure.getText(), textTaille.getText(), textDateArrivee.getText());
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
						int id = selectedEmploye.getEmployeId();
						selectedEmploye = new Employee(id, comboTitre.getText(), textNom.getText(), textPrenom.getText(),
								textMail.getText(), textTelephone.getText(), textNumeroMatricule.getText(),
								textPointure.getText(), textTaille.getText(), textDateArrivee.getText(), "Publié");
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
			String textMail, String textTelephone, String textPointure, String textTaille, String textDateArrivee) {

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

		// on insert dans la base de données
		try {
			employe.insertDatabase();
			LocalDate currentDate = LocalDate.now();
			CoutsEmploye ce = new CoutsEmploye(Employee.getIdByMatricule(employe.getNumeroMatricule()),currentDate.getMonthValue(),currentDate.getYear(),"Publié");
			ce.insertDatabase();
			System.out.println("on a insere l employe !!");
			MessageBox dialog = new MessageBox(vueEmploye.getShell(), SWT.ICON_INFORMATION | SWT.OK);
			dialog.setText("Création réussie");
			dialog.setMessage("L'employé a bien été ajouté à la base de données.");
			dialog.open();
			newVueEmploye();
		} catch (SQLException e) {
			System.out.println("erreur dans la création");
			MessageBox dialog = new MessageBox(vueEmploye.getShell(), SWT.ICON_ERROR | SWT.OK);
			dialog.setText("Erreur Création");
			dialog.setMessage("Une erreur est survenue lors de la création de l'employé. " + '\n' + e.getMessage());
			dialog.open();
		}
	}

	// --------------------------------------------------------------------------
	/***
	 * affiche le tableau avec tous les employes dans la base de donnees dont le
	 * status est publie
	 */
	public void vueEmployeAfficher() {
		if (!Objects.isNull(vue) && !vue.isDisposed()) {
			vue.dispose();
		}

		RowLayout rowLayoutV = new RowLayout();
		rowLayoutV.type = SWT.VERTICAL;

		vue = new Composite(vueEmploye, SWT.NONE);
		vue.setLayout(rowLayoutV);
		vue.setBackground(Couleur.gris);

		// creation de la table
		final Table table = new Table(vue, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL | SWT.FULL_SELECTION);
		table.setLayoutData(new RowData(847, 450));
		table.setLinesVisible(true);
		table.setHeaderVisible(true);

		// on met les noms des colonnes //espaces dans les titres pour changer la taille des colonnes
		String[] titles = { "Titre  ", "Nom        ", "Prenom     ", "Email             ", "Téléphone", "N° de matricule", "Pointure", "Taille",
				"Date d'ancienneté", "Ancienneté"};
		for (String title : titles) {
			TableColumn column = new TableColumn(table, SWT.NONE);
			column.setText(title);
		}

		// je voulais cacher cette colonne mais ca ne fonctionne pas
		TableColumn column = new TableColumn(table, SWT.NONE);
		column.setText("Id DB");
		column.setWidth(0);
		column.setResizable(false);

		// on pack les colonnes
		final TableColumn[] columns = table.getColumns();
		for (TableColumn col : columns)
			col.pack();

		// on ajoute un listener pour modifier l'interface si l'utilisateur clique sur
		// une ligne
		table.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				if (table.getSelectionIndex() != -1) {

					try {
						selectedEmploye = Employee
								.getEmployeById(Integer.parseInt(table.getSelection()[0].getText(10)));
					} catch (NumberFormatException | SQLException e1) {
						System.out.println("erreur pour recuperer l'employe selectionne");
						MessageBox dialog = new MessageBox(vueEmploye.getShell(), SWT.ICON_ERROR | SWT.OK);
						dialog.setText("Erreur");
						dialog.setMessage("Une erreur est survenue. " + '\n' + e1.getMessage());
						dialog.open();
					}

					compositeSelectionModifier(table);

					// on ajoute un menu lorsque l'on fait clique droit sur une ligne
					doMenu(table);
				} else { // si plus rien n'est selectionner on passe selectedEmploye a null et on enleve
					// le menu du clic droit et on enleve les boutons pour modifier et supprimer

					selectedEmploye = null;

					menu.dispose();
					menu = new Menu(vueEmploye.getShell(), SWT.POP_UP);
					table.setMenu(menu);

					compositeSelectionCreer();
				}
			}
		});

		updateTable(table);

		vue.pack();

	}


	/***
	 * methode utilisee pour affectation
	 * @param composite : composite ou ajouter la table
	 * @return la table contenant touts les employes publies
	 */
	public static Table getAllEmployer(Composite composite) {

		Table table = new Table(composite, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL | SWT.FULL_SELECTION);
		table.setLayoutData(new RowData(847, 450));
		table.setLinesVisible(true);
		table.setHeaderVisible(true);

		//on met les noms des colonnes
		String[] titles = { "Titre", "Nom", "Prenom", "Email", "Téléphone", "N° de matricule", "Pointure", "Taille",
				"Date d'ancienneté", "Ancienneté", "Id"};
		for (String title : titles) {
			TableColumn column = new TableColumn(table, SWT.NONE);
			column.setText(title);
		}

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
									if ((m1==m2)&&(a2-a1-1==5)) {
										System.out.println("iciciegb");
										item.setForeground(9, Couleur.rouge);
									}
									item.setText(9, Integer.toString(a2 - a1 - 1) + " ans");
								}
							} else {
								if (a2 - a1 == 1) {
									item.setText(9, Integer.toString(a2 - a1) + " an");
								} else {
									if ((m1==m2)&&(a2-a1-1==5)) {
										System.out.println("iciciegb2");
										item.setForeground(9, Couleur.rouge);
									}
									item.setText(9, Integer.toString(a2 - a1) + " ans");
								}
							}
						}
					} else {
						item.setText(8, "");
						item.setText(9, "");
					}

					item.setText(10, Integer.toString(e.getEmployeId()));
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
	public void updateTable(Table table) {

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
									if ((m1==m2)&&(a2-a1-1==5)) {
										//item.setForeground(Couleur.rouge);
										item.setForeground(9, Couleur.rouge);
									}
									item.setText(9, Integer.toString(a2 - a1 - 1) + " ans");
								}
							} else {
								if (a2 - a1 == 1) {
									item.setText(9, Integer.toString(a2 - a1) + " an");
								} else {
									if ((m1==m2)&&(a2-a1==5)) {
										item.setForeground(9, Couleur.rouge);
									}
									item.setText(9, Integer.toString(a2 - a1) + " ans");
								}
							}
						}
					} else {
						item.setText(8, "");
						item.setText(9, "");
					}

					item.setText(10, Integer.toString(e.getEmployeId()));
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
	public void doMenu(Table table) {
		menu = new Menu(vueEmploye.getShell(), SWT.POP_UP);
		table.setMenu(menu);

		// pour modifier
		MenuItem update = new MenuItem(menu, SWT.PUSH);
		update.setText("Modifier l'element");
		update.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				if (table.getSelection().length != 0) {
					vueEmployeModifier();
				}
			}
		});

		// pour supprimer
		MenuItem delete = new MenuItem(menu, SWT.PUSH);
		delete.setText("Supprimer l'element");
		delete.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				try {
					Employee e = Employee.getEmployeById(selectedEmploye.getEmployeId());
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
		if (selectedEmploye == null) {
			throw new Error("selectedEmploye est vide");
		}

		Employee e = Employee.getEmployeById(selectedEmploye.getEmployeId());
		e.setStatus("Archivé");
		e.updateDatabase();

		for (Affectation a : Affectation.getAllAffectation()) {
			if (a.getIdEmploye() == e.getEmployeId()) {
				a.setStatus(Status.ARCHIVED);
				a.updateDatabase();
			}
		}


		// newVueEmploye(); -> marche pas bien je sais pas pourquoi

		compositeSelectionCreer();
		updateTable(table);

		selectedEmploye = null;
	}



	public Composite getComposite() {
		return this.vueEmploye;

	}

}
