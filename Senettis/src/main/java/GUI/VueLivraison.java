/***
 * SenettisDB is developed by Laetitia Courgey and Stanislas Tran 
 * Copyright (C) 2020, Laetitia Courgey, Stanislas Tran
 * 
 * This file is part of SenettisDB
 * 
 * 
 * A MODIFIER ------------------------
 * ThaliaDB is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */



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
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.TouchEvent;
import org.eclipse.swt.events.TouchListener;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

import classes.Site;
import classes.AmmortissementChantier;
import classes.AmmortissementEmploye;
import classes.CoutsEmploye;
import classes.AmmortissementChantier;
import classes.Delivery;
import classes.Employee;
import classes.FournitureSanitaire;
import classes.Product;
import classes.ProductByDelivery;

public class VueLivraison {

	private Display display;
	private Composite vueLivraison;
	private Composite vueAutres;
	private Composite vueOnglet;
	private Composite vueFS;
	private Composite selection;
	private Composite vue;
	private Delivery selectedLivraison;
	private Site selectedChantier;
	private FournitureSanitaire selectedFS;
	private AmmortissementChantier selectedAmorti;
	private TabFolder tabFolder;
	private Menu menu ;
	private Table tableLivraison;
	private Table tableChantier;
	private Table tableAmorti;
	private Table tableFS;

	//Creation VueLivraison --------------------------------------------------
	/***
	 * Utilisé depuis Home pour créer une vueLivraison
	 * @param composite : le composite vueLivraison
	 * @param display
	 */
	public VueLivraison (Composite composite,Display display) {
		this.display=display;
		Couleur.setDisplay(display); // pour utiliser les couleurs du fichier couleur

		tabFolder = new TabFolder(composite, SWT.BORDER);

		TabItem tabLivraison = new TabItem(tabFolder, SWT.NULL);
		tabLivraison.setText("     Livraisons     ");

		TabItem tabFS = new TabItem(tabFolder, SWT.NULL);
		tabFS.setText("     Fournitures Sanitaires     ");

		TabItem tabAutres = new TabItem(tabFolder, SWT.NULL);
		tabAutres.setText("     Matériel et autres couts     ");

		tabFolder.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent event) {
				selectedChantier = null; selectedAmorti = null ; selectedLivraison = null ; selectedFS = null;

				if (!Objects.isNull(vueFS) && !vueFS.isDisposed()) {
					vueFS.dispose();
				}
				if (!Objects.isNull(vueAutres) && !vueAutres.isDisposed()) {
					vueAutres.dispose();
				}
				if (!Objects.isNull(vueLivraison) && !vueLivraison.isDisposed()) {
					vueLivraison.dispose();
				}
				if (!Objects.isNull(vueOnglet) && !vueOnglet.isDisposed()) {
					vueOnglet.dispose();
				}
				RowLayout rowLayout = new RowLayout();
				rowLayout.type = SWT.VERTICAL;
				vueOnglet=new Composite(tabFolder,SWT.NONE);
				vueOnglet.setLayout(rowLayout);
				vueOnglet.setBackground(Couleur.gris);

				if (tabFolder.getSelection()[0].equals(tabLivraison)) {
					vueLivraison=new Composite(vueOnglet,SWT.NONE);
					vueLivraison.setLayout(rowLayout);
					vueLivraison.setBackground(Couleur.gris);

					tabLivraison.setControl(vueOnglet);

					compositeSelectionCreer();
					vueLivraisonAfficher();
					vueLivraison.pack();
				}
				else if (tabFolder.getSelection()[0].equals(tabFS)) {
					vueFS=new Composite(vueOnglet,SWT.NONE);
					vueFS.setLayout(rowLayout);
					vueFS.setBackground(Couleur.gris);

					tabFS.setControl(vueOnglet);
					vueFS();
					vueFS.pack();
				}
				else {
					vueAutres=new Composite(vueOnglet,SWT.NONE); 
					vueAutres.setLayout(rowLayout);
					vueAutres.setBackground(Couleur.gris);

					tabAutres.setControl(vueOnglet);

					vueAmortissement();
					vueAutres.pack();
				}
				vueOnglet.pack();
				tabFolder.pack();
				tabFolder.getParent().pack();
			}
		});

		RowLayout rowLayout = new RowLayout();
		rowLayout.type = SWT.VERTICAL;

		vueOnglet=new Composite(tabFolder,SWT.NONE);
		vueOnglet.setLayout(rowLayout);
		vueOnglet.setBackground(Couleur.gris);
		tabLivraison.setControl(vueOnglet);

		vueLivraison=new Composite(vueOnglet,SWT.NONE);
		vueLivraison.setLayout(rowLayout);
		vueLivraison.setBackground(Couleur.gris);

		compositeSelectionCreer();
		vueLivraisonAfficher();

		vueLivraison.pack();
		vueOnglet.pack();
		tabFolder.pack();
		tabFolder.getParent().pack();
	}

	////////////////////////////////////// FS //////////////////////////////////////////////

	public void vueFS() {

		fsSelection();

		///VUE

		if (!Objects.isNull(vue) && !vue.isDisposed()) {
			vue.dispose();
		}

		vue = new Composite(vueFS, SWT.NONE);
		RowLayout rowLayoutH = new RowLayout();
		rowLayoutH.type = SWT.VERTICAL;
		vue.setLayout(rowLayoutH);

		Composite tables = new Composite(vue,SWT.NONE);
		tables.setLayout(new RowLayout(SWT.HORIZONTAL));

		//creation de la table des produits
		Composite compoChantier = new Composite(tables,SWT.NONE);
		compoChantier.setLayout(new RowLayout(SWT.VERTICAL));

		Label chantier = new Label(compoChantier,SWT.NONE);
		chantier.setText("Choisir un chantier :");

		tableChantier = new Table (compoChantier, SWT.BORDER | SWT.MULTI| SWT.V_SCROLL | SWT.FULL_SELECTION);
		tableChantier.setLayoutData(new RowData(300,300));
		tableChantier.setLinesVisible (true);
		tableChantier.setHeaderVisible (true);
		tableChantier.layout(true,true);

		//on met les noms des colonnes
		String[] titles = {"Id", "Nom"};
		for (String title : titles) {
			TableColumn column = new TableColumn (tableChantier, SWT.NONE);
			column.setText (title);
		}

		updateTableChantierFS();
		compoChantier.pack();


		//creation de la table amorti
		Composite compoFS = new Composite(tables,SWT.NONE);
		compoFS.setLayout(new RowLayout(SWT.VERTICAL));

		Label fs = new Label(compoFS,SWT.NONE);
		fs.setText("Coûts de fournitures sanitaires liés à cet chantier :");

		tableFS = new Table (compoFS, SWT.BORDER | SWT.MULTI| SWT.V_SCROLL | SWT.FULL_SELECTION);
		tableFS.setLayoutData(new RowData(500,380));
		tableFS.setLinesVisible (true);
		tableFS.setHeaderVisible (true);
		tableFS.layout(true,true);

		//on met les noms des colonnes
		String[] titles2 = {"Id", "Début" ,"Montant par mois","Sous Traitant", "Description"};
		for (String title : titles2) {
			TableColumn column = new TableColumn (tableFS, SWT.NONE);
			column.setText (title);
		}

		if (selectedChantier != null) {

			updateTableFS();
			compoFS.pack();
		}

		vue.pack();
		vueFS.pack();
		tabFolder.pack();
		tabFolder.getParent().pack();
	}

	public void fsSelection() {
		///SELECTION
		if (!Objects.isNull(selection) && !selection.isDisposed()) {
			selection.dispose();
		}

		selection = new Composite(vueFS, SWT.NONE);
		RowLayout rowLayout = new RowLayout();
		rowLayout.marginWidth = 5;
		rowLayout.marginTop = 6;
		rowLayout.spacing = 10;
		selection.setLayout(rowLayout);
		selection.setBackground(Couleur.gris);

		RowLayout rl = new RowLayout();
		rl.type = SWT.HORIZONTAL;


		Button boutonCreation = new Button(selection, SWT.CENTER);
		boutonCreation.setText("Créer");
		boutonCreation.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				selectedChantier = null; selectedFS = null;
				vueCreationFS();
			}
		});

		if (selectedFS != null) {
			Button boutonModif = new Button(selection, SWT.CENTER);
			boutonModif.setText("Modifier");
			boutonModif.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent arg0) {
					vueCreationFS();
				}
			});

			Button boutonSupp = new Button(selection, SWT.CENTER);
			boutonSupp.setText("Supprimer");
			boutonSupp.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent arg0) {
					try {
						FournitureSanitaire fs = FournitureSanitaire.getFournitureSanitaireById(selectedFS.getFournitureSanitaireId());
						MessageBox dialog = new MessageBox(vueFS.getShell(), SWT.ICON_QUESTION | SWT.YES | SWT.NO);
						dialog.setText("Suppression Fourniture Sanitaire");
						dialog.setMessage("Voulez vous supprimer le coût de fourniture sanitaire du chantier " + Site.getChantierById(fs.getChantierId()).getNom() + " ?");
						int buttonID = dialog.open();
						switch (buttonID) {
						case SWT.YES:
							fs.setStatus("Archivé");
							fs.updateDatabase();
							updateTableFS();
						}
					} catch (Exception e) {
						System.out.println("erreur pour supprimer l'element");
						MessageBox dialog = new MessageBox(vueFS.getShell(), SWT.ICON_ERROR | SWT.OK);
						dialog.setText("Erreur");
						dialog.setMessage("Une erreur est survenue. " + '\n' + e.getMessage());
						dialog.open();
					}
				}
			});
		}

		selection.pack();
	}

	public void vueCreationFS() {

		vueFormulaireFS();

		if (!Objects.isNull(selection) && !selection.isDisposed()) {
			selection.dispose();
		}
		String s;int addSize;
		if (selectedFS != null) {
			s = "Modification d'un coût de fourniture sanitaire";
			addSize = vue.getSize().x;
			addSize = (addSize - 342)/2;
		}
		else {
			s = "Création d'un coût de fourniture sanitaire";
			addSize = vue.getSize().x;
			addSize = (addSize - 310)/2;
		}

		selection = new Composite(vueFS, SWT.CENTER);

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
		HeadLabel.setText(s);
		Font fontTitle = new Font(HeadLabel.getDisplay(), "Arial", 12, SWT.BOLD);
		HeadLabel.setForeground(Couleur.bleuClair);
		HeadLabel.setFont(fontTitle);
		HeadLabel.setBackground(Couleur.bleuFonce);

		// juste pour creer un espace
		Label l2 = new Label(selection, SWT.NONE);
		l2.setText("");
		l2.setBackground(Couleur.bleuFonce);

		selection.pack();

		vueFormulaireFS();

		vueFS.pack();
		tabFolder.pack();
		tabFolder.getParent().pack();
	}

	public void vueFormulaireFS() {
		if (!Objects.isNull(vue) && !vue.isDisposed()) {
			vue.dispose();
		}
		vue = new Composite(vueFS, SWT.NONE);
		FillLayout fillLayoutH = new FillLayout();
		fillLayoutH.type = SWT.HORIZONTAL;
		vue.setLayout(fillLayoutH);

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


		Composite compositeChantier = new Composite(colonne1, SWT.NONE);
		compositeChantier.setBackground(Couleur.bleuClair);
		compositeChantier.setLayout(fillLayoutH5);

		Label labelChantier = new Label(compositeChantier, SWT.NONE);
		labelChantier.setText("Chantier* : ");
		labelChantier.setBackground(Couleur.bleuClair);
		// Titre
		Combo chantiers = new Combo(compositeChantier, SWT.BORDER);
		if (selectedChantier != null){
			try {
				if (selectedChantier.getNom().length() > 25) {
					chantiers.setText(selectedChantier.getNom().substring(0, 23)+"..."+";id:"+((Integer)selectedChantier.getChantierId()).toString());
				}
				else {
					chantiers.setText(selectedChantier.getNom()+" ; id :"+((Integer)selectedChantier.getChantierId()).toString());
				}
			} catch (Exception e1) {
				e1.printStackTrace(); 
				System.out.println("erreur pour recuperer les chantiers");
				MessageBox dialog = new MessageBox(vueFS.getShell(), SWT.ICON_ERROR | SWT.OK);
				dialog.setText("Erreur");
				dialog.setMessage("Une erreur est survenue. "+'\n'+e1.getMessage());
				dialog.open();
			}
		}
		else {
			chantiers.setText("Selectionner ...");
		}
		try {
			for (Site e : Site.getAllChantier()) {
				if (e.getStatus().equals("Publié")) {
					if (e.getNom().length() > 25) {
						chantiers.add(e.getNom().substring(0, 23)+"..."+";id:"+((Integer)e.getChantierId()).toString());
					}
					else {
						chantiers.add(e.getNom()+" ; id :"+((Integer)e.getChantierId()).toString());
					}
				}
			}
		} catch (SQLException e1) {
			e1.printStackTrace(); 
			System.out.println("erreur pour recuperer les chantiers");
			MessageBox dialog = new MessageBox(vueFS.getShell(), SWT.ICON_ERROR | SWT.OK);
			dialog.setText("Erreur");
			dialog.setMessage("Une erreur est survenue. "+'\n'+e1.getMessage());
			dialog.open();
		}



		Composite compositePeriode = new Composite(colonne1, SWT.NONE);
		compositePeriode.setBackground(Couleur.bleuClair);
		compositePeriode.setLayout(fillLayoutH5);

		Label labelPeriode = new Label(compositePeriode, SWT.NONE);
		labelPeriode.setText("Début : ");
		labelPeriode.setBackground(Couleur.bleuClair);

		Combo periode = new Combo(compositePeriode, SWT.BORDER);
		LocalDate currentdate = LocalDate.now();
		Month month = currentdate.getMonth();
		int year = currentdate.getYear();
		if (selectedFS == null) {
			periode.setText(month.toString()+" "+year);
		}
		else {
			periode.setText(Month.of(selectedFS.getMoisD())+" "+selectedFS.getAnneeD().toString());
		}

		for(int i = 2020; i <= year+1 ; i++) {
			for (int j =1 ; j <= 12 ; j++) {
				periode.add(Month.of(j)+" "+i);
			}
		}

		// valeur
		Composite compositeValeur = new Composite(colonne2, SWT.NONE);
		compositeValeur.setBackground(Couleur.bleuClair);
		compositeValeur.setLayout(fillLayoutH5);

		Label labelValeur = new Label(compositeValeur, SWT.NONE);
		labelValeur.setBackground(Couleur.bleuClair);
		labelValeur.setText("Montant par mois* : ");

		final Text textValeur = new Text(compositeValeur, SWT.BORDER);
		if (selectedFS != null) {
			textValeur.setText(selectedFS.getMontantParMois().toString());
		}
		else {
			textValeur.setText("");
		}

		// sous traitant
		Composite compositeST = new Composite(colonne2, SWT.NONE);
		compositeST.setBackground(Couleur.bleuClair);
		compositeST.setLayout(fillLayoutH5);

		Label labelST = new Label(compositeST, SWT.NONE);
		labelST.setBackground(Couleur.bleuClair);
		labelST.setText("Sous Traitant* : ");

		final Text textST = new Text(compositeST, SWT.BORDER);
		if (selectedFS != null) {
			textST.setText(selectedFS.getSousTraitant());
		}
		else {
			textST.setText("");
		}

		// desc
		Composite compositeDesc = new Composite(colonne2, SWT.NONE);
		compositeDesc.setBackground(Couleur.bleuClair);
		compositeDesc.setLayout(fillLayoutH5);

		Label labelDesc = new Label(compositeDesc, SWT.NONE);
		labelDesc.setBackground(Couleur.bleuClair);
		labelDesc.setText("Description : ");

		final Text textDesc = new Text(compositeDesc, SWT.BORDER);
		if (selectedFS != null) {
			textDesc.setText(selectedFS.getDescription());
		}
		else {
			textDesc.setText("");
		}


		// Boutons
		Composite compositeBoutons = new Composite(colonne2, SWT.CENTER);
		compositeBoutons.setBackground(Couleur.bleuClair);
		compositeBoutons.setLayout(fillLayoutH5);

		Button buttonAnnulation = new Button(compositeBoutons, SWT.BACKGROUND);
		buttonAnnulation.setText("Annuler");
		buttonAnnulation.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				selectedFS = null; selectedChantier = null;selectedAmorti = null;
				vueFS();
			}
		});

		Button buttonValidation = new Button(compositeBoutons, SWT.BACKGROUND);
		buttonValidation.setText("Valider");

		//creation

		buttonValidation.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent arg0) {

				try {
					String id;
					try {
						id = chantiers.getText().split(";")[1].replace(" ",""); 
					} catch (Exception e) {
						throw new Error ("Merci d'indiquer un chantier.");
					}
					Integer chantierId = Integer.parseInt(id.substring(3,id.length()));

					String[] debut = periode.getText().split(" ");

					int moisD = Month.valueOf(debut[0]).getValue();
					int anneeD = Integer.parseInt(debut[1]);

					Double montantParMois;
					try {
						montantParMois = Double.parseDouble(textValeur.getText());
					} catch (Exception e) {
						throw new Error ("Merci d'indiquer le montant.");
					}

					if (textST.getText().isBlank()) {
						throw new Error ("Merci d'indiquer le sous traitant.");
					}

					if (selectedFS != null) {
						FournitureSanitaire fs = new FournitureSanitaire(selectedFS.getFournitureSanitaireId(), chantierId, moisD, anneeD, textDesc.getText(), montantParMois,textST.getText(), "Publié");
						fs.updateDatabase();
						MessageBox dialog = new MessageBox(vueFS.getShell(), SWT.ICON_INFORMATION | SWT.OK);
						dialog.setText("Modification réussie");
						dialog.setMessage("Ce cout de fourniture sanitaire a bien été modifié dans la base de données.");
						dialog.open();
					}
					else {
						FournitureSanitaire fs = new FournitureSanitaire(chantierId, moisD, anneeD,montantParMois, textDesc.getText(),textST.getText(), "Publié");
						fs.insertDatabase();
						MessageBox dialog = new MessageBox(vueFS.getShell(), SWT.ICON_INFORMATION | SWT.OK);
						dialog.setText("Création réussie");
						dialog.setMessage("Ce cout de fourniture sanitaire a bien été ajouté dans la base de données.");
						dialog.open();
					}
					vueFS();
				} catch (Throwable e) {
					e.printStackTrace();
					System.out.println("erreur dans la creation");
					MessageBox dialog = new MessageBox(vueFS.getShell(), SWT.ICON_ERROR | SWT.OK);
					dialog.setText("Erreur Création");
					dialog.setMessage(
							"Une erreur est survenue lors de la création du cout de fourniture sanitaire. " + '\n' + e.getMessage());
					dialog.open();
				}
			}
		});
		selectedFS = null;
		vue.pack();
	}

	public void updateTableChantierFS() {
		if(!Objects.isNull(tableChantier)) {
			tableChantier.removeAll();
		}

		//on remplit la table
		final TableColumn [] columns = tableChantier.getColumns();

		try {
			for (Site e : Site.getAllChantier()) {
				//on verifie le status
				if (e.getStatus().equals("Publié")) {
					TableItem item = new TableItem (tableChantier, SWT.NONE);
					item.setText(0,((Integer)e.getChantierId()).toString());
					item.setText(1,e.getNom());
				}
			}
		} catch (SQLException e1) {
			System.out.println("erreur dans la table des couts chantiers");
			MessageBox dialog = new MessageBox(vueFS.getShell(), SWT.ICON_ERROR | SWT.OK);
			dialog.setText("Erreur");
			dialog.setMessage("Une erreur est survenue. "+'\n'+e1.getMessage());
			dialog.open();
		}

		//on pack les colonnes
		for (TableColumn col : columns)
			col.pack ();

		tableChantier.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				if (tableChantier.getSelectionIndex() != -1) {

					try {
						selectedChantier = Site
								.getChantierById(Integer.parseInt(tableChantier.getSelection()[0].getText(0)));
					} catch (NumberFormatException | SQLException e1) {
						System.out.println("erreur pour recuperer l'chantier selectionne");
						MessageBox dialog = new MessageBox(vueFS.getShell(), SWT.ICON_ERROR | SWT.OK);
						dialog.setText("Erreur");
						dialog.setMessage("Une erreur est survenue. " + '\n' + e1.getMessage());
						dialog.open();
					}

					updateTableFS();

				} else { 

					selectedChantier = null;selectedFS = null;selectedAmorti = null;
					updateTableChantierFS();
					updateTableFS();
				}
			}
		});

		vue.pack();
	}

	public void updateTableFS() {
		if(!Objects.isNull(tableFS)) {
			tableFS.removeAll();
		}
		if (selectedChantier == null) {
			return;
		}

		//on remplit la table
		final TableColumn [] columns = tableFS.getColumns();

		try {
			for (FournitureSanitaire fs : FournitureSanitaire.getAllFournitureSanitaire()) {
				//on verifie le status
				if (fs.getStatus().equals("Publié") && selectedChantier.getChantierId()==fs.getChantierId()) {
					TableItem item = new TableItem (tableFS, SWT.NONE);
					item.setText(0,fs.getFournitureSanitaireId().toString());
					item.setText(1,fs.getMoisD().toString()+'/'+fs.getAnneeD().toString());
					item.setText(2,fs.getMontantParMois().toString());
					item.setText(3,fs.getSousTraitant());
					item.setText(4,fs.getDescription());
				}
			}
		} catch (SQLException e1) {
			System.out.println("erreur dans la table des couts fs");
			MessageBox dialog = new MessageBox(vueFS.getShell(), SWT.ICON_ERROR | SWT.OK);
			dialog.setText("Erreur");
			dialog.setMessage("Une erreur est survenue. "+'\n'+e1.getMessage());
			dialog.open();
		}

		//on pack les colonnes
		for (TableColumn col : columns)
			col.pack ();

		tableFS.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				if (tableFS.getSelectionIndex() != -1) {

					try {
						selectedFS = FournitureSanitaire.getFournitureSanitaireById(Integer.parseInt(tableFS.getSelection()[0].getText(0)));
					} catch (NumberFormatException | SQLException e1) {
						System.out.println("erreur pour recuperer le fs selectionne");
						MessageBox dialog = new MessageBox(vueFS.getShell(), SWT.ICON_ERROR | SWT.OK);
						dialog.setText("Erreur");
						dialog.setMessage("Une erreur est survenue. " + '\n' + e1.getMessage());
						dialog.open();
					}

					fsSelection();
					doMenu(tableFS);
				} else { 

					selectedFS = null;
					fsSelection();

					menu.dispose();
					menu = new Menu(vueFS.getShell(), SWT.POP_UP);
					tableFS.setMenu(menu);
				}
			}
		});


		tableFS.pack();
		vue.pack();
		vue.layout(true,true);
	}




	////////////////////////////////////// COUTS A AMORTIR  //////////////////////////////////////////////

	public void amortiSelection() {
		///SELECTION
		if (!Objects.isNull(selection) && !selection.isDisposed()) {
			selection.dispose();
		}

		selection = new Composite(vueAutres, SWT.NONE);
		RowLayout rowLayout = new RowLayout();
		rowLayout.marginWidth = 5;
		rowLayout.marginTop = 6;
		rowLayout.spacing = 10;
		selection.setLayout(rowLayout);
		selection.setBackground(Couleur.gris);

		RowLayout rl = new RowLayout();
		rl.type = SWT.HORIZONTAL;


		Button boutonCreation = new Button(selection, SWT.CENTER);
		boutonCreation.setText("Créer");
		boutonCreation.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				selectedChantier = null; selectedAmorti = null;selectedFS = null;
				vueCreationA();
			}
		});

		if (selectedAmorti != null) {
			Button boutonModif = new Button(selection, SWT.CENTER);
			boutonModif.setText("Modifier");
			boutonModif.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent arg0) {
					vueCreationA();
				}
			});

			Button boutonSupp = new Button(selection, SWT.CENTER);
			boutonSupp.setText("Supprimer");
			boutonSupp.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent arg0) {
					try {
						AmmortissementChantier ae = AmmortissementChantier.getAmmortissementChantierById(selectedAmorti.getAmmortissementChantierId());
						MessageBox dialog = new MessageBox(vueAutres.getShell(), SWT.ICON_QUESTION | SWT.YES | SWT.NO);
						dialog.setText("Suppression Amortissement Chantier");
						dialog.setMessage("Voulez vous supprimer le coût à amortir du chantier " + Site.getChantierById(ae.getChantierId()).getNom() + " ?");
						int buttonID = dialog.open();
						switch (buttonID) {
						case SWT.YES:
							ae.setStatus("Archivé");
							ae.updateDatabase();
							updateTableAmorti();
						}
					} catch (Exception e) {
						System.out.println("erreur pour supprimer l'element");
						MessageBox dialog = new MessageBox(vueAutres.getShell(), SWT.ICON_ERROR | SWT.OK);
						dialog.setText("Erreur");
						dialog.setMessage("Une erreur est survenue. " + '\n' + e.getMessage());
						dialog.open();
					}
				}
			});
		}

		selection.pack();
	}

	public void vueAmortissement() {

		amortiSelection();

		///VUE

		if (!Objects.isNull(vue) && !vue.isDisposed()) {
			vue.dispose();
		}

		vue = new Composite(vueAutres, SWT.NONE);
		RowLayout rowLayoutH = new RowLayout();
		rowLayoutH.type = SWT.VERTICAL;
		vue.setLayout(rowLayoutH);

		Composite tables = new Composite(vue,SWT.NONE);
		tables.setLayout(new RowLayout(SWT.HORIZONTAL));

		//creation de la table des produits
		Composite compoEmp = new Composite(tables,SWT.NONE);
		compoEmp.setLayout(new RowLayout(SWT.VERTICAL));

		Label emp = new Label(compoEmp,SWT.NONE);
		emp.setText("Choisir un chantier :");

		tableChantier = new Table (compoEmp, SWT.BORDER | SWT.MULTI| SWT.V_SCROLL | SWT.FULL_SELECTION);
		tableChantier.setLayoutData(new RowData(300,300));
		tableChantier.setLinesVisible (true);
		tableChantier.setHeaderVisible (true);
		tableChantier.layout(true,true);

		//on met les noms des colonnes
		String[] titles = {"Id", "Nom"};
		for (String title : titles) {
			TableColumn column = new TableColumn (tableChantier, SWT.NONE);
			column.setText (title);
		}

		updateTableEmp();
		compoEmp.pack();


		//creation de la table amorti
		Composite compoAmorti = new Composite(tables,SWT.NONE);
		compoAmorti.setLayout(new RowLayout(SWT.VERTICAL));

		Label amorti = new Label(compoAmorti,SWT.NONE);
		amorti.setText("Coûts liés à cet chantier :");

		tableAmorti = new Table (compoAmorti, SWT.BORDER | SWT.MULTI| SWT.V_SCROLL | SWT.FULL_SELECTION);
		tableAmorti.setLayoutData(new RowData(500,380));
		tableAmorti.setLinesVisible (true);
		tableAmorti.setHeaderVisible (true);
		tableAmorti.layout(true,true);

		//on met les noms des colonnes
		String[] titles2 = {"Id", "Début" ,"Fin","Montant par mois","Valeur totale", "Type", "Description"};
		for (String title : titles2) {
			TableColumn column = new TableColumn (tableAmorti, SWT.NONE);
			column.setText (title);
		}

		if (selectedChantier != null) {

			updateTableAmorti();
			compoAmorti.pack();
		}

		vue.pack();
		vueAutres.pack();
		tabFolder.pack();
		tabFolder.getParent().pack();
	}

	public void vueCreationA() {

		vueFormulaireAmorti();

		if (!Objects.isNull(selection) && !selection.isDisposed()) {
			selection.dispose();
		}
		String s;int addSize;
		if (selectedAmorti != null) {
			s = "Modification d'un coût à amortir";
			addSize = vue.getSize().x;
			addSize = (addSize - 235)/2;
		}
		else {
			s = "Création d'un coût à amortir";
			addSize = vue.getSize().x;
			addSize = (addSize - 210)/2;
		}

		selection = new Composite(vueAutres, SWT.CENTER);

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
		HeadLabel.setText(s);
		Font fontTitle = new Font(HeadLabel.getDisplay(), "Arial", 12, SWT.BOLD);
		HeadLabel.setForeground(Couleur.bleuClair);
		HeadLabel.setFont(fontTitle);
		HeadLabel.setBackground(Couleur.bleuFonce);

		// juste pour creer un espace
		Label l2 = new Label(selection, SWT.NONE);
		l2.setText("");
		l2.setBackground(Couleur.bleuFonce);

		selection.pack();

		vueFormulaireAmorti();

		vueAutres.pack();
		tabFolder.pack();
		tabFolder.getParent().pack();
	}

	public void vueFormulaireAmorti() {
		if (!Objects.isNull(vue) && !vue.isDisposed()) {
			vue.dispose();
		}
		vue = new Composite(vueAutres, SWT.NONE);
		FillLayout fillLayoutH = new FillLayout();
		fillLayoutH.type = SWT.HORIZONTAL;
		vue.setLayout(fillLayoutH);


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


		Composite compositeChantier = new Composite(colonne1, SWT.NONE);
		compositeChantier.setBackground(Couleur.bleuClair);
		compositeChantier.setLayout(fillLayoutH5);

		Label labelChantier = new Label(compositeChantier, SWT.NONE);
		labelChantier.setText("Chantier* : ");
		labelChantier.setBackground(Couleur.bleuClair);
		// Titre
		Combo chantiers = new Combo(compositeChantier, SWT.BORDER);
		if (selectedChantier != null){
			try {
				if (selectedChantier.getNom().length() > 25) {
					chantiers.setText(selectedChantier.getNom().substring(0, 23)+"..."+";id:"+((Integer)selectedChantier.getChantierId()).toString());
				}
				else {
					chantiers.setText(selectedChantier.getNom()+" ; id :"+((Integer)selectedChantier.getChantierId()).toString());
				}
			} catch (Exception e1) {
				e1.printStackTrace(); 
				System.out.println("erreur pour recuperer les chantiers");
				MessageBox dialog = new MessageBox(vueAutres.getShell(), SWT.ICON_ERROR | SWT.OK);
				dialog.setText("Erreur");
				dialog.setMessage("Une erreur est survenue. "+'\n'+e1.getMessage());
				dialog.open();
			}
		}
		else {
			chantiers.setText("Selectionner ...");
		}
		try {
			for (Site e : Site.getAllChantier()) {
				if (e.getStatus().equals("Publié")) {
					if (e.getNom().length() > 25) {
						chantiers.add(e.getNom().substring(0, 23)+"..."+";id:"+((Integer)e.getChantierId()).toString());
					}
					else {
						chantiers.add(e.getNom()+" ; id :"+((Integer)e.getChantierId()).toString());
					}
				}
			}
		} catch (SQLException e1) {
			e1.printStackTrace(); 
			System.out.println("erreur pour recuperer les chantiers");
			MessageBox dialog = new MessageBox(vueAutres.getShell(), SWT.ICON_ERROR | SWT.OK);
			dialog.setText("Erreur");
			dialog.setMessage("Une erreur est survenue. "+'\n'+e1.getMessage());
			dialog.open();
		}



		Composite compositePeriode = new Composite(colonne1, SWT.NONE);
		compositePeriode.setBackground(Couleur.bleuClair);
		compositePeriode.setLayout(fillLayoutH5);

		Label labelPeriode = new Label(compositePeriode, SWT.NONE);
		labelPeriode.setText("Début* : ");
		labelPeriode.setBackground(Couleur.bleuClair);

		Combo periode = new Combo(compositePeriode, SWT.BORDER);
		LocalDate currentdate = LocalDate.now();
		Month month = currentdate.getMonth();
		int year = currentdate.getYear();
		if (selectedAmorti == null) {
			periode.setText(month.toString()+" "+year);
		}
		else {
			periode.setText(Month.of(selectedAmorti.getMoisD())+" "+selectedAmorti.getAnneeD().toString());
		}

		for(int i = 2020; i <= year+1 ; i++) {
			for (int j =1 ; j <= 12 ; j++) {
				periode.add(Month.of(j)+" "+i);
			}
		}


		// duree
		Composite compositeDuree = new Composite(colonne1, SWT.NONE);
		compositeDuree.setBackground(Couleur.bleuClair);
		compositeDuree.setLayout(fillLayoutH5);

		Label labelDuree = new Label(compositeDuree, SWT.NONE);
		labelDuree.setBackground(Couleur.bleuClair);
		labelDuree.setText("Durée (en mois)* : ");

		final Text textDuree = new Text(compositeDuree, SWT.BORDER);
		if (selectedAmorti != null) {
			textDuree.setText(selectedAmorti.getDuree().toString());
		}
		else {
			textDuree.setText("");
		}


		//pour creer un espace
		Composite compoTest = new Composite(colonne1, SWT.NONE);
		compoTest.setBackground(Couleur.bleuClair);
		compoTest.setLayout(fillLayoutH5);

		Label labelTest = new Label(compoTest, SWT.NONE);
		labelTest.setBackground(Couleur.bleuClair);
		labelTest.setText("");


		// valeur
		Composite compositeValeur = new Composite(colonne2, SWT.NONE);
		compositeValeur.setBackground(Couleur.bleuClair);
		compositeValeur.setLayout(fillLayoutH5);

		Label labelValeur = new Label(compositeValeur, SWT.NONE);
		labelValeur.setBackground(Couleur.bleuClair);
		labelValeur.setText("Montant total* : ");

		final Text textValeur = new Text(compositeValeur, SWT.BORDER);
		if (selectedAmorti != null) {
			textValeur.setText(selectedAmorti.getValeur().toString());
		}
		else {
			textValeur.setText("");
		}


		Composite compositeType = new Composite(colonne2, SWT.NONE);
		compositeType.setBackground(Couleur.bleuClair);
		compositeType.setLayout(fillLayoutH5);

		Label labelType = new Label(compositeType, SWT.NONE);
		labelType.setText("Type* : ");
		labelType.setBackground(Couleur.bleuClair);
		// Titre
		Combo type = new Combo(compositeType, SWT.BORDER);
		if (selectedAmorti != null) {
			type.setText(selectedAmorti.getType());
		}
		else {
			type.setText("Ammortissement matériel");
		}
		type.add("Ammortissement matériel");
		type.add("Coût ponctuel");

		type.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				if (type.getText().equals("Coût ponctuel")) {
					textDuree.setText("1");
				}
			}
		});



		// valeur
		Composite compositeDesc = new Composite(colonne2, SWT.NONE);
		compositeDesc.setBackground(Couleur.bleuClair);
		compositeDesc.setLayout(fillLayoutH5);

		Label labelDesc = new Label(compositeDesc, SWT.NONE);
		labelDesc.setBackground(Couleur.bleuClair);
		labelDesc.setText("Description : ");

		final Text textDesc = new Text(compositeDesc, SWT.BORDER);
		if (selectedAmorti != null) {
			textDesc.setText(selectedAmorti.getDescription());
		}
		else {
			textDesc.setText("");
		}


		// Boutons
		Composite compositeBoutons = new Composite(colonne2, SWT.CENTER);
		compositeBoutons.setBackground(Couleur.bleuClair);
		compositeBoutons.setLayout(fillLayoutH5);

		Button buttonAnnulation = new Button(compositeBoutons, SWT.BACKGROUND);
		buttonAnnulation.setText("Annuler");
		buttonAnnulation.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				selectedAmorti = null; selectedChantier = null;selectedFS = null;
				vueAmortissement();
			}
		});

		Button buttonValidation = new Button(compositeBoutons, SWT.BACKGROUND);
		buttonValidation.setText("Valider");

		//creation

		buttonValidation.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent arg0) {

				try {
					String id;
					try {
						id = chantiers.getText().split(";")[1].replace(" ",""); 
					} catch (Exception e) {
						throw new Error ("Merci d'indiquer un chantier.");
					}
					Integer chantierId = Integer.parseInt(id.substring(3,id.length()));

					String[] debut = periode.getText().split(" ");
					System.out.println(debut[0]+" "+debut[1]);

					int moisD = Month.valueOf(debut[0]).getValue();
					int anneeD = Integer.parseInt(debut[1]);

					int duree;
					try {
						duree = Integer.parseInt(textDuree.getText());
					} catch (Exception e) {
						throw new Error ("Merci d'indiquer une durée.");
					}

					int moisF; int anneeF;

					if (duree < 12) {
						moisF = moisD + duree;  anneeF = anneeD;
					}
					else if (duree == 12) {
						moisF = moisD;  anneeF = anneeD+1;
					}
					else {
						anneeF = anneeD + duree / 12;
						moisF = moisD + (duree % 12);
					}

					Double valeurTotale;
					try {
						valeurTotale = Double.parseDouble(textValeur.getText());
					} catch (Exception e) {
						throw new Error ("Merci d'indiquer le montant total.");
					}

					if (selectedAmorti != null) {
						AmmortissementChantier ae = new AmmortissementChantier(selectedAmorti.getAmmortissementChantierId(), chantierId, moisD, anneeD, moisF, anneeF, textDesc.getText(), valeurTotale/duree, duree, valeurTotale, type.getText(), "Publié");
						ae.updateDatabase();
						MessageBox dialog = new MessageBox(vueAutres.getShell(), SWT.ICON_INFORMATION | SWT.OK);
						dialog.setText("Modification réussie");
						dialog.setMessage("Ce cout a bien été modifié dans la base de données.");
						dialog.open();
					}
					else {
						AmmortissementChantier ae = new AmmortissementChantier(0,chantierId, moisD, anneeD, moisF, anneeF, textDesc.getText(), valeurTotale/duree, duree, valeurTotale, type.getText(), "Publié");
						ae.insertDatabase();
						MessageBox dialog = new MessageBox(vueAutres.getShell(), SWT.ICON_INFORMATION | SWT.OK);
						dialog.setText("Création réussie");
						dialog.setMessage("Ce cout a bien été ajouté dans la base de données.");
						dialog.open();
					}
					vueAmortissement();
				} catch (Throwable e) {
					e.printStackTrace();
					System.out.println("erreur dans la creation");
					MessageBox dialog = new MessageBox(vueAutres.getShell(), SWT.ICON_ERROR | SWT.OK);
					dialog.setText("Erreur Création");
					dialog.setMessage(
							"Une erreur est survenue lors de la création du cout à amortir. " + '\n' + e.getMessage());
					dialog.open();
				}
			}
		});

		vue.pack();
	}

	public void updateTableEmp() {
		if(!Objects.isNull(tableChantier)) {
			tableChantier.removeAll();
		}

		//on remplit la table
		final TableColumn [] columns = tableChantier.getColumns();

		try {
			for (Site e : Site.getAllChantier()) {
				//on verifie le status
				if (e.getStatus().equals("Publié")) {
					TableItem item = new TableItem (tableChantier, SWT.NONE);
					item.setText(0,((Integer)e.getChantierId()).toString());
					item.setText(1,e.getNom());
				}
			}
		} catch (SQLException e1) {
			System.out.println("erreur dans la table des couts chantiers");
			MessageBox dialog = new MessageBox(vueAutres.getShell(), SWT.ICON_ERROR | SWT.OK);
			dialog.setText("Erreur");
			dialog.setMessage("Une erreur est survenue. "+'\n'+e1.getMessage());
			dialog.open();
		}

		//on pack les colonnes
		for (TableColumn col : columns)
			col.pack ();

		tableChantier.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				if (tableChantier.getSelectionIndex() != -1) {

					try {
						selectedChantier = Site
								.getChantierById(Integer.parseInt(tableChantier.getSelection()[0].getText(0)));
					} catch (NumberFormatException | SQLException e1) {
						System.out.println("erreur pour recuperer l'chantier selectionne");
						MessageBox dialog = new MessageBox(vueAutres.getShell(), SWT.ICON_ERROR | SWT.OK);
						dialog.setText("Erreur");
						dialog.setMessage("Une erreur est survenue. " + '\n' + e1.getMessage());
						dialog.open();
					}

					updateTableAmorti();

				} else { 

					selectedChantier = null;selectedAmorti = null; selectedFS = null;
					updateTableEmp();
					updateTableAmorti();

				}
			}
		});

		vue.pack();
	}

	public void updateTableAmorti() {
		if(!Objects.isNull(tableAmorti)) {
			tableAmorti.removeAll();
		}
		if (selectedChantier == null) {
			return;
		}

		//on remplit la table
		final TableColumn [] columns = tableAmorti.getColumns();

		try {
			for (AmmortissementChantier a : AmmortissementChantier.getAllAmmortissementChantier()) {
				//on verifie le status
				if (a.getStatus().equals("Publié") && selectedChantier.getChantierId()==a.getChantierId()) {
					TableItem item = new TableItem (tableAmorti, SWT.NONE);
					item.setText(0,a.getAmmortissementChantierId().toString());
					item.setText(1,a.getMoisD().toString()+'/'+a.getAnneeD().toString());
					item.setText(2,a.getMoisF().toString()+'/'+a.getAnneeF().toString());
					item.setText(3,a.getMontantParMois().toString());
					item.setText(4,a.getValeur().toString());
					item.setText(5,a.getType());
					item.setText(6,a.getDescription());
				}
			}
		} catch (SQLException e1) {
			System.out.println("erreur dans la table des couts amorti chantiers");
			MessageBox dialog = new MessageBox(vueAutres.getShell(), SWT.ICON_ERROR | SWT.OK);
			dialog.setText("Erreur");
			dialog.setMessage("Une erreur est survenue. "+'\n'+e1.getMessage());
			dialog.open();
		}

		//on pack les colonnes
		for (TableColumn col : columns)
			col.pack ();

		tableAmorti.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				if (tableAmorti.getSelectionIndex() != -1) {

					try {
						selectedAmorti = AmmortissementChantier.getAmmortissementChantierById(Integer.parseInt(tableAmorti.getSelection()[0].getText(0)));
					} catch (NumberFormatException | SQLException e1) {
						System.out.println("erreur pour recuperer l'amorti selectionne");
						MessageBox dialog = new MessageBox(vueAutres.getShell(), SWT.ICON_ERROR | SWT.OK);
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
					menu = new Menu(vueAutres.getShell(), SWT.POP_UP);
					tableAmorti.setMenu(menu);
				}
			}
		});


		tableAmorti.pack();
		vue.pack();
		vue.layout(true,true);
	}

	/***
	 * Pour créer une vueLivraison : 
	 * Appelle ensuite les fonctions compositeSelectionCreer et vueLivraisonAfficher
	 */
	public void newVueLivraison () {

		compositeSelectionCreer();
		vueLivraisonAfficher();

		vueLivraison.pack();
		tabFolder.pack();
		tabFolder.getParent().pack();
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////

	//Modification de la partie Selection --------------------------------------------------
	/***
	 * Creation de la partie Selection (la partie superieure droite) avec uniquement le bouton Creer 
	 */
	public void compositeSelectionCreer() {
		if (!Objects.isNull(selection) && !selection.isDisposed()) {
			selection.dispose();
		}
		selection = new Composite(vueLivraison, SWT.NONE);
		RowLayout rowLayout = new RowLayout();
		rowLayout.marginWidth = 20;
		selection.setLayout(rowLayout);
		selection.setBackground(Couleur.gris);

		Composite compositePeriode = new Composite(selection, SWT.NONE);
		compositePeriode.setBackground(Couleur.bleuClair);
		FillLayout fillLayoutH5 = new FillLayout();
		fillLayoutH5.marginHeight = 30;
		fillLayoutH5.spacing = 5;
		fillLayoutH5.type = SWT.HORIZONTAL;
		compositePeriode.setLayout(fillLayoutH5);

		Label labelPeriode = new Label(compositePeriode, SWT.NONE);
		labelPeriode.setText("Début* : ");
		labelPeriode.setBackground(Couleur.bleuClair);

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

		//TODO SELEcTED


		Button boutonCreer = new Button(selection, SWT.CENTER);
		boutonCreer.setText("Créer");
		boutonCreer.addSelectionListener(new SelectionAdapter() {	
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				vueLivraisonCreer();
			}
		});

		if (selectedLivraison != null) {
			//Bouton Modifier
			Button boutonModifier = new Button(selection, SWT.CENTER);
			boutonModifier.setText("Modifier");
			boutonModifier.addSelectionListener(new SelectionAdapter() {	
				@Override
				public void widgetSelected(SelectionEvent arg0) {
					vueLivraisonModifier();
				}
			});

			//Bouton Supprimer
			Button boutonSupprimer = new Button(selection, SWT.CENTER);
			boutonSupprimer.setText("Supprimer");
			boutonSupprimer.addSelectionListener(new SelectionAdapter() {	
				@Override
				public void widgetSelected(SelectionEvent arg0) {
					suppLivraison();
				}
			});
		}
		selection.pack();
	}



	//Suppression d'une livraison ---------------------------------------------------
	/***
	 * fonction utilisee pour supprimer une livraison
	 * cette fonction demande confirmation puis passe en archivé la livraison selectionne et les produits par livraison associes 
	 */
	public void suppLivraison() {
		try {
			//on recupere la livraison selectionnee
			Delivery l = Delivery.getLivraisonById(selectedLivraison.getLivraisonId());

			//on demande une confirmation
			MessageBox dialog = new MessageBox(vueLivraison.getShell(), SWT.ICON_QUESTION | SWT.YES | SWT.NO);
			dialog.setText("Suppression Livraison");
			if (l.getDate() != null) {
				dialog.setMessage("Voulez vous supprimer le livraison du "+l.getDate()+" sur le chantier "+Site.getChantierById(l.getIdChantier()).getNom()+" ?");
			}
			else {
				dialog.setMessage("Voulez vous supprimer le livraison de "+l.getPrixTotal()+" euros sur le chantier "+Site.getChantierById(l.getIdChantier()).getNom()+" ?");
			}
			int buttonID = dialog.open();

			switch(buttonID) {
			case SWT.YES:
				//on archive la livraison
				l.setStatus("Archivé");
				l.updateDatabase();

				//on archive les produits associes
				//on doit mettre dans un try car s'il n'y a pas de produits associes cela provoque une erreur
				try {
					for (ProductByDelivery p : ProductByDelivery.getProductByLivraisonByLivraisonId(l.getLivraisonId())) {
						p.setStatus("Archivé");
						p.updateDatabase();
					}
				}catch (Exception e) {}

				//on change d'affichage
				//newVueLivraison(vueLivraison);

				selectedLivraison = null;


				compositeSelectionCreer();


				updateTable(tableLivraison);
			}

		} catch (NumberFormatException | SQLException e) {
			System.out.println("erreur pour supprimer la livraison");
			MessageBox dialog = new MessageBox(vueLivraison.getShell(), SWT.ICON_ERROR | SWT.OK);
			dialog.setText("Erreur");
			dialog.setMessage("Une erreur est survenue. "+'\n'+e.getMessage());
			dialog.open();
		}
	}



	//Modification d'une livraison --------------------------------------------------
	/***
	 * Regroupe les fonctions a appeler pour faire une modification
	 * La fonction va appeler les fonctions titreModification et formulaireModification
	 */
	public void vueLivraisonModifier() {

		formulaireModification();//on l'appelle d'abord une fois pour pouvoir recuperer sa taille dans titre modification et faire un titre a la bonne taille
		titreModification();	
		formulaireModification();

		vueLivraison.pack();
		tabFolder.pack();
		tabFolder.getParent().pack();

	}

	/***
	 * va modifier la partie selection (partie superieure droite) en y mettant un titre pour la modification
	 */
	public void titreModification() {
		if (!Objects.isNull(selection) && !selection.isDisposed()) {
			selection.dispose();
		}
		selection = new Composite(vueLivraison, SWT.CENTER);

		FillLayout fillLayout = new FillLayout();
		fillLayout.type = SWT.VERTICAL;

		int addSize = vue.getSize().x;
		addSize = (addSize - 215)/2;//on recupere l'ecart entre la taille du titre de base (215) et le formulaire de modif
		fillLayout.marginWidth = addSize;
		selection.setLayout(fillLayout);

		//juste pour creer un espace 
		Label l1 = new Label(selection, SWT.NONE);
		l1.setText("");
		l1.setBackground(Couleur.bleuFonce);

		selection.setBackground(Couleur.bleuFonce);
		Label HeadLabel =new Label(selection,SWT.TITLE);
		HeadLabel.setText("Modification d'une Livraison");
		Font fontTitle = new Font(HeadLabel.getDisplay(), "Arial", 12, SWT.BOLD);
		HeadLabel.setForeground(Couleur.bleuClair);
		HeadLabel.setFont(fontTitle);
		HeadLabel.setBackground(Couleur.bleuFonce);

		//juste pour creer un espace 
		Label l2 = new Label(selection, SWT.NONE);
		l2.setText("");
		l2.setBackground(Couleur.bleuFonce);

		selection.pack();
	}

	/***
	 * Va modifier la partie Vue (partie inferieure droite) et y ajoutant le formulaire de modification d'un livraison
	 */
	public void formulaireModification() {
		if (!Objects.isNull(vue) && !vue.isDisposed()) {
			vue.dispose();
		}
		if (selectedLivraison == null) {
			throw new Error("selectedLivraison est vide");
		}

		//creation de la vue
		vue = new Composite(vueLivraison, SWT.NONE);
		RowLayout rowLayoutH = new RowLayout();
		rowLayoutH.type = SWT.HORIZONTAL;
		vue.setLayout(rowLayoutH);

		//creation de 2 colonnes afin de repartir les champs du formulaire 
		FillLayout fillLayoutV = new FillLayout();
		fillLayoutV.type = SWT.VERTICAL;
		fillLayoutV.marginWidth = 10;
		fillLayoutV.marginHeight = 15;
		fillLayoutV.spacing = 20;

		Composite colonne1 = new Composite(vue, SWT.BORDER);
		colonne1.setBackground(Couleur.bleuClair);
		colonne1.setLayout(fillLayoutV);

		RowLayout rowLayoutV = new RowLayout();
		rowLayoutV.type = SWT.VERTICAL;
		rowLayoutV.marginWidth = 10;

		Composite colonne2 = new Composite(vue, SWT.BORDER);
		colonne2.setBackground(Couleur.bleuClair);
		colonne2.setLayout(rowLayoutV);

		//utiliser pour tous les composites des attributs du formulaire
		FillLayout fillLayoutH5 = new FillLayout();
		fillLayoutH5.marginHeight = 30;
		fillLayoutH5.spacing = 5;
		fillLayoutH5.type = SWT.HORIZONTAL;

		//Chantier
		Composite compositeChantier = new Composite(colonne1, SWT.NONE);
		compositeChantier.setBackground(Couleur.bleuClair);
		compositeChantier.setLayout(fillLayoutH5);

		Label labelChantier = new Label(compositeChantier, SWT.NONE);
		labelChantier.setBackground(Couleur.bleuClair);
		labelChantier.setText("Chantier* :                                                  ");//espaces pour que la barre de texte soit plus grande

		//on affiche le chantier selectionne
		Combo chantier = new Combo(compositeChantier, SWT.BORDER);
		try {
			if (Site.getChantierById(selectedLivraison.getIdChantier()).getStatus().equals("Publié")) {
				String stringChantier = Site.getChantierById(selectedLivraison.getIdChantier()).getNom()+"; id :"+selectedLivraison.getIdChantier().toString();
				if (stringChantier.length() > 30) {
					chantier.setText(stringChantier.substring(0, 23)+"..."+";id:"+((Integer)selectedLivraison.getIdChantier()).toString());
					labelChantier.setText("Chantier* :");//pour pas que ca fasse un trop grand espace et que ca decale le titre
				}
				else {
					chantier.setText(stringChantier);
				}
			}
			else {
				chantier.setText("Selectionner ...");
			}
		} catch (SQLException e1) {
			e1.printStackTrace(); 
			System.out.println("erreur pour recuperer les chantiers");
			MessageBox dialog = new MessageBox(vueLivraison.getShell(), SWT.ICON_ERROR | SWT.OK);
			dialog.setText("Erreur");
			dialog.setMessage("Une erreur est survenue. "+'\n'+e1.getMessage());
			dialog.open();
		}
		//on recupere les autres chantiers pour les afficher aussi et pouvoir modifier le chantier actuel
		try {
			for (Site c : Site.getAllChantier()) {
				if (c.getNom() != Site.getChantierById(selectedLivraison.getIdChantier()).getNom() && c.getStatus().equals("Publié")) {
					String stringChantier =  c.getNom()+"; id :"+((Integer)c.getChantierId()).toString();
					if (stringChantier.length() > 30) {
						chantier.add(stringChantier.substring(0, 23)+"..."+";id:"+((Integer)c.getChantierId()).toString());
						labelChantier.setText("Chantier* :");
					}
					else {
						chantier.add(stringChantier);
					}
				}
			}
		} catch (SQLException e1) {
			e1.printStackTrace(); 
			System.out.println("erreur pour recuperer les chantiers");
			MessageBox dialog = new MessageBox(vueLivraison.getShell(), SWT.ICON_ERROR | SWT.OK);
			dialog.setText("Erreur");
			dialog.setMessage("Une erreur est survenue. "+'\n'+e1.getMessage());
			dialog.open();
		}

		//Date
		Composite compositeDate = new Composite(colonne1, SWT.NONE);
		compositeDate.setBackground(Couleur.bleuClair);
		compositeDate.setLayout(fillLayoutH5);

		Label labelDate = new Label(compositeDate, SWT.NONE);
		labelDate.setBackground(Couleur.bleuClair);
		labelDate.setText("Date : ");

		final Text date = new Text(compositeDate, SWT.BORDER);
		if (selectedLivraison.getDate() == null) { date.setText(""); }
		else { date.setText(selectedLivraison.getDate()); }

		//pour ajouter les barres / automatiquement
		date.addModifyListener(new ModifyListener() { 
			public void modifyText(ModifyEvent me) { 
				if (!(date.getText().isEmpty())) {//pour ne pas tester quand l'utilisateur est en train de modifier
					if (date.getText().length() == 2) {date.append("/");}
					if (date.getText().length() == 5) {date.append("/");}
				}
			}
		});



		//Prix
		Composite compositePrix = new Composite(colonne1, SWT.NONE);
		compositePrix.setBackground(Couleur.bleuClair);
		compositePrix.setLayout(fillLayoutH5);

		Label labelPrix = new Label(compositePrix, SWT.NONE);
		labelPrix.setBackground(Couleur.bleuClair);
		labelPrix.setText("Prix Total : ");

		final Text prix = new Text(compositePrix, SWT.BORDER);
		prix.setText(selectedLivraison.getPrixTotal().toString());

		//Boutons
		Composite compositeBoutons = new Composite(colonne1, SWT.CENTER);
		compositeBoutons.setBackground(Couleur.bleuClair);
		compositeBoutons.setLayout(fillLayoutH5);

		Button buttonAnnulation = new Button(compositeBoutons, SWT.BACKGROUND);
		buttonAnnulation.setText("Annuler");
		buttonAnnulation.addSelectionListener(new SelectionAdapter() {

			@Override public void widgetSelected(SelectionEvent arg0) {
				newVueLivraison();
			}
		});

		Button buttonValidation = new Button(compositeBoutons, SWT.BACKGROUND);
		buttonValidation.setText("Valider");

		//Produit
		Composite compositeProduit = new Composite(colonne2, SWT.NONE);
		compositeProduit.setBackground(Couleur.bleuClair);
		compositeProduit.setLayout(fillLayoutH5);

		Label labelProduit = new Label(compositeProduit, SWT.NONE);
		labelProduit.setBackground(Couleur.bleuClair);
		labelProduit.setText("Produits : ");

		Composite table = new Composite(colonne2, SWT.NONE);
		table.setLayout(rowLayoutV);
		table.setBackground(Couleur.bleuClair);

		//creation de la table des produits
		final Table tableProduit = new Table (table, SWT.BORDER | SWT.MULTI| SWT.V_SCROLL | SWT.FULL_SELECTION);
		tableProduit.setLayoutData(new RowData(400,230));
		tableProduit.setLinesVisible (true);
		tableProduit.setHeaderVisible (true);

		//pour pouvoir modifier les quantites
		final TableEditor editor = new TableEditor (tableProduit);
		editor.horizontalAlignment = SWT.LEFT;
		editor.grabHorizontal = true;

		//on met les noms des colonnes
		String[] titles = {"Id","Nom","Prix","Marque", "Commentaires", "Quantité"};
		for (String title : titles) {
			TableColumn column = new TableColumn (tableProduit, SWT.NONE);
			column.setText (title);
		}

		//on remplit la table
		final TableColumn [] columns = tableProduit.getColumns ();

		//on remplit d'abord avec les quantites a 0 et on modifiera apres
		try {
			for (Product p : Product.getAllProduct()) {
				//on verifie le status
				if (p.getStatus().getValue().contentEquals("Publié")) {
					TableItem item = new TableItem (tableProduit, SWT.NONE);
					item.setText(0,((Integer)p.getProduitId()).toString());
					item.setText(1,p.getName());
					item.setText(2,p.getPrice().toString());
					item.setText(3,p.getBrand());
					item.setText(4,p.getComment());
					item.setText(5,"0");
				}
			}
		} catch (SQLException e1) {
			System.out.println("erreur dans la table des produits de livraison");
			MessageBox dialog = new MessageBox(vueLivraison.getShell(), SWT.ICON_ERROR | SWT.OK);
			dialog.setText("Erreur");
			dialog.setMessage("Une erreur est survenue. "+'\n'+e1.getMessage());
			dialog.open();
		}

		//on modifie les quantites du tableau en y mettant celles des produits par livraison de la base de donnees
		try {
			if (selectedLivraison.getPrixTotal() > 0) {
				for (ProductByDelivery p : ProductByDelivery.getProductByLivraisonByLivraisonId(selectedLivraison.getLivraisonId())) {
					//on verifie le status
					if (p.getStatus().contentEquals("Publié")) {
						for (TableItem i : tableProduit.getItems()) {
							if (Integer.parseInt(i.getText(0)) == p.getIdProduit() ) {
								i.setText(5, p.getQuantite().toString());
							}
						}
					}
				}
			}
		} catch (NumberFormatException | SQLException e1) {
			System.out.println("erreur dans la table des produits de livraison");
			e1.printStackTrace();
			MessageBox dialog = new MessageBox(vueLivraison.getShell(), SWT.ICON_ERROR | SWT.OK);
			dialog.setText("Erreur");
			dialog.setMessage("Une erreur est survenue. "+'\n'+e1.getMessage());
			dialog.open();
		}

		tableProduit.pack();table.pack();

		//on pack les colonnes
		for (TableColumn col : columns)
			col.pack ();

		//quand on clique sur une ligne on peut modifier la quantite de la ligne
		//pour modifier les quantites 
		tableProduit.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				//on dispose l'ancien editor
				Control oldEditor = editor.getEditor();
				if (oldEditor != null) { oldEditor.dispose();}

				//on recupere la ligne selectionne
				TableItem item = (TableItem) e.item;
				if (item == null) { return; }

				//on ajoute l'editor sur la ligne
				Text newEditor = new Text(tableProduit, SWT.NONE);
				newEditor.setText(item.getText(5));
				newEditor.addModifyListener(new ModifyListener() { 
					public void modifyText(ModifyEvent me) { 
						if (!(newEditor.getText().isEmpty())) {//pour ne pas tester quand l'utilisateur est en train de modifier
							try {
								if (Integer.parseInt(newEditor.getText()) <0) {//on ne peut pas mettre de quantite negative
									item.setText(5,"0"); 
								}
								else {
									item.setText(5,newEditor.getText()); //on modifie la ligne avec la nouvelle quantite
								}
							} catch(Exception e) {
								System.out.println("erreur dans la modif"); MessageBox dialog = new
										MessageBox(vueLivraison.getShell(), SWT.ICON_ERROR | SWT.OK);
								dialog.setText("Erreur Editor"); dialog.
								setMessage("La quantite saisie n'est pas valide. "
										+'\n'+e.getMessage()); dialog.open(); 
							}
						}
					}
				});
				newEditor.selectAll(); newEditor.setFocus();
				editor.setEditor(newEditor, item, 5);
			}
		});

		//Bouton calculPrix
		Composite calculPrix = new Composite(colonne2, SWT.CENTER);
		calculPrix.setBackground(Couleur.bleuClair);
		calculPrix.setLayout(fillLayoutH5);

		//juste pour decaler le bouton a droite
		Label l = new Label(calculPrix, SWT.NONE);
		l.setBackground(Couleur.bleuClair);
		l.setText("");

		Button buttonCalculPrix = new Button(calculPrix, SWT.BACKGROUND);
		buttonCalculPrix.setText("Afficher le prix total");
		//buttonCalculPrix.setBounds(10, 60, 200, 30); // -> ne focntionne pas en fillLayout

		buttonCalculPrix.addSelectionListener(new SelectionAdapter() {
			//on calcule le prix total
			@Override public void widgetSelected(SelectionEvent arg0) {
				Double somme = 0.0;
				for (TableItem i : tableProduit.getItems()) {
					Integer quantite = Integer.parseInt(i.getText(5));
					Double prix = Double.parseDouble(i.getText(2));
					somme += quantite * prix;
				}
				prix.setText(somme.toString());
			}
		});



		buttonValidation.addSelectionListener(new SelectionAdapter() {

			@Override public void widgetSelected(SelectionEvent arg0) {
				//d'abord, on recalcule le prix pour etre sur
				Double somme = 0.0;
				for (TableItem i : tableProduit.getItems()) {
					Integer quantite = Integer.parseInt(i.getText(5));
					Double prix = Double.parseDouble(i.getText(2));
					somme += quantite * prix;
				}
				prix.setText(somme.toString());

				Integer idChantier = 0;
				//on verifie si un chantier est saisi
				try { 

					String c = chantier.getText().split(";")[1].replace(" ",""); 
					idChantier = Integer.parseInt(c.substring(3,c.length()));
				} catch (Throwable e) { 
					e.printStackTrace();
					System.out.println("erreur dans la modif"); 
					MessageBox dialog = new MessageBox(vueLivraison.getShell(), SWT.ICON_ERROR | SWT.OK);
					dialog.setText("Erreur Modification"); 
					dialog.setMessage("Merci d'indiquer un chantier."); 
					dialog.open(); 
				}	

				// on recupere les produits et quantites 
				try { 
					if (idChantier != 0) {
						ArrayList<Integer> produits = new ArrayList<Integer>();
						ArrayList<Integer> quantites = new ArrayList<Integer>();

						for(TableItem i : tableProduit.getItems()) {
							produits.add(Integer.parseInt(i.getText(0)));
							quantites.add(Integer.parseInt(i.getText(5)));
						}

						// on met a jour la livraison selectionnee
						selectedLivraison.setIdChantier(idChantier);
						if (!(date.getText().isEmpty())) {
							selectedLivraison.setDate(date.getText());
						}
						selectedLivraison.setPrixTotal(Double.parseDouble(prix.getText()));

						validerModification(produits, quantites);
					}
				} catch (Throwable e) { e.printStackTrace();
				System.out.println("erreur dans la modif"); 
				MessageBox dialog = new MessageBox(vueLivraison.getShell(), SWT.ICON_ERROR | SWT.OK);
				dialog.setText("Erreur Modification"); 
				dialog.setMessage("Une erreur est survenue. "+e.getMessage()); 
				dialog.open(); 
				}	
			}
		}); 
		vue.pack();

	}

	/***
	 * modifie la livraison dans la base de données
	 */
	public void validerModification(ArrayList<Integer> produits, ArrayList<Integer> quantites) {
		if (selectedLivraison == null) {
			throw new Error("selectedLivraison est vide");
		}

		boolean toutVaBien = true ;//utiliser pour s'assurer que quand une erreur a lieu on n'affiche pas la pop de reussite 
		//on modifie la livraison dans la base de données
		try { 
			selectedLivraison.updateDatabase(); 
			System.out.println("on a modifie la livraison !!");
		} catch (SQLException e) { 
			toutVaBien = false;
			e.printStackTrace(); 
			System.out.println("erreur dans la modif");
			MessageBox dialog = new MessageBox(vueLivraison.getShell(), SWT.ICON_ERROR | SWT.OK);
			dialog.setText("Erreur Modification");
			dialog.setMessage("Une erreur est survenue lors de la modification de la livraison. "+e.getMessage());
			dialog.open();
		} 

		//on modifie les produits associes à la livraison
		for(int i = 0 ; i<produits.size() ; i++) {
			try {//si le produit est deja associe a la livraison, on le modifie
				ProductByDelivery p = ProductByDelivery.getProductByLivraisonByLivraisonIdAndProductId(selectedLivraison.getLivraisonId(), produits.get(i));
				p.setQuantite(quantites.get(i));
				p.updateDatabase();
			}catch (Exception e) {//si le produit n'est pas encore associe a la livraison on l'associe si la quantite n'est pas de 0
				if (quantites.get(i) != 0) {
					ProductByDelivery p = new ProductByDelivery(selectedLivraison.getLivraisonId(),  produits.get(i), quantites.get(i), "Publié");
					try {
						p.insertDatabase();
					} catch (SQLException e1) {
						toutVaBien = false;
						System.out.println("erreur dans la création");
						MessageBox dialog = new MessageBox(vueLivraison.getShell(), SWT.ICON_ERROR | SWT.OK);
						dialog.setText("Erreur Création");
						dialog.setMessage("Une erreur est survenue lors de la création de la livraison. "+'\n'+e1.getMessage());
						dialog.open();
					}
				}
			}
		}

		if (toutVaBien) { //s'il n'y a pas eu d'erreur on affiche la pop up de validation
			MessageBox dialog = new MessageBox(vueLivraison.getShell(), SWT.ICON_INFORMATION | SWT.OK);
			dialog.setText("Création réussie");
			dialog.setMessage("La livraison a bien été modifiée dans la base de données.");
			dialog.open();

			selectedLivraison = null;
			newVueLivraison();
		}
	}

	//Création d'une livraison --------------------------------------------------
	/***
	 * va appeler les fonctions qui modifient la partie vue et la partie selection afin d'afficher le formulaire de création 
	 * appelle titreCreation et formulaireCreation
	 */	
	public void vueLivraisonCreer() {

		formulaireCreation();
		titreCreation();		
		formulaireCreation();

		vueLivraison.pack();
		tabFolder.pack();
		tabFolder.getParent().pack();

	}

	/***
	 * modifie la partie selection (partie superieur droite) en ajoutant un titre de creation
	 */
	public void titreCreation() {
		if (!Objects.isNull(selection) && !selection.isDisposed()) {
			selection.dispose();
		}
		selection = new Composite(vueLivraison, SWT.CENTER);

		FillLayout fillLayout = new FillLayout();
		fillLayout.type = SWT.VERTICAL;

		int addSize = vue.getSize().x;
		addSize = (addSize - 187)/2;//on recupere l'ecart entre la taille du titre de base (187) et le formulaire de modif
		fillLayout.marginWidth = addSize;
		selection.setLayout(fillLayout);

		//juste pour creer un espace 
		Label l1 = new Label(selection, SWT.NONE);
		l1.setText("");
		l1.setBackground(Couleur.bleuFonce);

		selection.setBackground(Couleur.bleuFonce);
		Label HeadLabel =new Label(selection,SWT.TITLE);
		HeadLabel.setText("Creation d'une Livraison");
		Font fontTitle = new Font(HeadLabel.getDisplay(), "Arial", 12, SWT.BOLD);
		HeadLabel.setForeground(Couleur.bleuClair);
		HeadLabel.setFont(fontTitle);
		HeadLabel.setBackground(Couleur.bleuFonce);

		//juste pour creer un espace 
		Label l2 = new Label(selection, SWT.NONE);
		l2.setText("");
		l2.setBackground(Couleur.bleuFonce);

		selection.pack();
	}

	/***
	 * cree le formulaire de creation d'une livraison
	 */
	public void formulaireCreation() {
		if (!Objects.isNull(vue) && !vue.isDisposed()) {
			vue.dispose();
		}
		vue = new Composite(vueLivraison, SWT.NONE);
		RowLayout rowLayoutH = new RowLayout();
		rowLayoutH.type = SWT.HORIZONTAL;
		vue.setLayout(rowLayoutH);

		//creation de 2 colonnes afin de repartir les champs du formulaire 
		FillLayout fillLayoutV = new FillLayout();
		fillLayoutV.type = SWT.VERTICAL;
		fillLayoutV.marginWidth = 10;
		fillLayoutV.marginHeight = 15;
		fillLayoutV.spacing = 20;

		RowLayout rowLayoutV = new RowLayout();
		rowLayoutV.type = SWT.VERTICAL;
		rowLayoutV.marginWidth = 10;

		Composite colonne1 = new Composite(vue, SWT.BORDER);
		colonne1.setBackground(Couleur.bleuClair);
		colonne1.setLayout(fillLayoutV);

		Composite colonne2 = new Composite(vue, SWT.BORDER);
		colonne2.setBackground(Couleur.bleuClair);
		colonne2.setLayout(rowLayoutV);

		//utiliser pour tous les composites des attributs du formulaire
		FillLayout fillLayoutH5 = new FillLayout();
		fillLayoutH5.marginHeight = 30;
		fillLayoutH5.spacing = 5;
		fillLayoutH5.type = SWT.HORIZONTAL;

		//Chantier
		Composite compositeChantier = new Composite(colonne1, SWT.NONE);
		compositeChantier.setBackground(Couleur.bleuClair);
		compositeChantier.setLayout(fillLayoutH5);

		Label labelChantier = new Label(compositeChantier, SWT.NONE);
		labelChantier.setBackground(Couleur.bleuClair);
		labelChantier.setText("Chantier* :                                                  ");

		Combo chantier = new Combo(compositeChantier, SWT.BORDER);
		chantier.setText("Selectionner ...");
		try {
			for (Site c : Site.getAllChantier()) {
				if (c.getStatus().equals("Publié")) {
					String stringChantier =  c.getNom();
					if (stringChantier.length() > 21) {
						chantier.add(stringChantier.substring(0, 21)+"..."+";id:"+((Integer)c.getChantierId()).toString());
						labelChantier.setText("Chantier* :");
					}
					else {
						chantier.add(c.getNom()+"; id :"+((Integer)c.getChantierId()).toString());
					}
				}
			}
		} catch (SQLException e1) {
			e1.printStackTrace(); 
			System.out.println("erreur pour recuperer les chantiers");
			MessageBox dialog = new MessageBox(vueLivraison.getShell(), SWT.ICON_ERROR | SWT.OK);
			dialog.setText("Erreur");
			dialog.setMessage("Une erreur est survenue. "+'\n'+e1.getMessage());
			dialog.open();
		}

		//Date
		Composite compositeDate = new Composite(colonne1, SWT.NONE);
		compositeDate.setBackground(Couleur.bleuClair);
		compositeDate.setLayout(fillLayoutH5);

		Label labelDate = new Label(compositeDate, SWT.NONE);
		labelDate.setBackground(Couleur.bleuClair);
		labelDate.setText("Date : ");

		final Text date = new Text(compositeDate, SWT.BORDER);
		date.setText("");
		//pour ajouter les barres / automatiquement
		date.addModifyListener(new ModifyListener() { 
			public void modifyText(ModifyEvent me) { 
				if (!(date.getText().isEmpty())) {//pour ne pas tester quand l'utilisateur est en train de modifier
					if (date.getText().length() == 2) {date.append("/");}
					if (date.getText().length() == 5) {date.append("/");}
				}
			}
		});

		//Prix
		Composite compositePrix = new Composite(colonne1, SWT.NONE);
		compositePrix.setBackground(Couleur.bleuClair);
		compositePrix.setLayout(fillLayoutH5);

		Label labelPrix = new Label(compositePrix, SWT.NONE);
		labelPrix.setBackground(Couleur.bleuClair);
		labelPrix.setText("Prix Total : ");

		final Text prix = new Text(compositePrix, SWT.BORDER);
		prix.setText("");

		//Boutons
		Composite compositeBoutons = new Composite(colonne1, SWT.CENTER);
		compositeBoutons.setBackground(Couleur.bleuClair);
		compositeBoutons.setLayout(fillLayoutH5);

		Button buttonAnnulation = new Button(compositeBoutons, SWT.BACKGROUND);
		buttonAnnulation.setText("Annuler");
		buttonAnnulation.addSelectionListener(new SelectionAdapter() {

			@Override public void widgetSelected(SelectionEvent arg0) {
				newVueLivraison();
			}
		});

		Button buttonValidation = new Button(compositeBoutons, SWT.BACKGROUND);
		buttonValidation.setText("Valider");

		//Produit
		Composite compositeProduit = new Composite(colonne2, SWT.NONE);
		compositeProduit.setBackground(Couleur.bleuClair);
		compositeProduit.setLayout(fillLayoutH5);

		Label labelProduit = new Label(compositeProduit, SWT.NONE);
		labelProduit.setBackground(Couleur.bleuClair);
		labelProduit.setText("Produits : ");

		Composite table = new Composite(colonne2, SWT.NONE);
		table.setLayout(rowLayoutV);
		table.setBackground(Couleur.bleuClair);

		//creation de la table
		final Table tableProduit = new Table (table, SWT.BORDER | SWT.MULTI| SWT.V_SCROLL | SWT.FULL_SELECTION);
		tableProduit.setLayoutData(new RowData(400, 230));
		tableProduit.setLinesVisible (true);
		tableProduit.setHeaderVisible (true);

		final TableEditor editor = new TableEditor (tableProduit);
		editor.horizontalAlignment = SWT.LEFT;
		editor.grabHorizontal = true;

		//on met les noms des colonnes
		String[] titles = {"Id","Nom","Prix","Marque", "Commentaires", "Quantité"};
		for (String title : titles) {
			TableColumn column = new TableColumn (tableProduit, SWT.NONE);
			column.setText (title);
		}

		//on remplit la table
		try {
			for (Product p : Product.getAllProduct()) {
				if (p.getStatus().getValue().contentEquals("Publié")) {
					TableItem item = new TableItem (tableProduit, SWT.NONE);
					item.setText(0,((Integer)p.getProduitId()).toString());
					item.setText(1,p.getName());
					item.setText(2,p.getPrice().toString());
					item.setText(3,p.getBrand());
					item.setText(4,p.getComment());
					item.setText(5,"0");
				}
			}
		} catch (SQLException e1) {
			System.out.println("erreur dans la table des produits de livraison");
			MessageBox dialog = new MessageBox(vueLivraison.getShell(), SWT.ICON_ERROR | SWT.OK);
			dialog.setText("Erreur");
			dialog.setMessage("Une erreur est survenue. "+'\n'+e1.getMessage());
			dialog.open();
		}

		tableProduit.pack();table.pack();

		final TableColumn [] columns = tableProduit.getColumns (); 
		for (TableColumn col : columns) { col.pack ();}

		//pour modifier les quantites 
		tableProduit.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				Control oldEditor = editor.getEditor();
				if (oldEditor != null) { oldEditor.dispose();}

				TableItem item = (TableItem) e.item;
				if (item == null) { return; }

				Text newEditor = new Text(tableProduit, SWT.NONE);
				newEditor.setText(item.getText(5));
				newEditor.addModifyListener(new ModifyListener() { 
					public void modifyText(ModifyEvent me) { 
						if (!(newEditor.getText().isEmpty())) {
							try {
								if (Integer.parseInt(newEditor.getText()) <0) {
									item.setText(5,"0"); 
								}
								else {
									item.setText(5,newEditor.getText()); 
								}
							} catch(Exception e) {
								System.out.println("erreur dans la modif"); MessageBox dialog = new
										MessageBox(vueLivraison.getShell(), SWT.ICON_ERROR | SWT.OK);
								dialog.setText("Erreur Editor"); dialog.
								setMessage("La quantite saisie n'est pas valide. "
										+'\n'+e.getMessage()); dialog.open(); 
							}
						}
					} 
				});
				newEditor.selectAll(); newEditor.setFocus();
				editor.setEditor(newEditor, item, 5);
			}
		});


		//Bouton calculPrix
		Composite calculPrix = new Composite(colonne2, SWT.CENTER);
		calculPrix.setBackground(Couleur.bleuClair);
		calculPrix.setLayout(fillLayoutH5);

		//juste pour decaler le bouton a droite
		Label l = new Label(calculPrix, SWT.NONE);
		l.setBackground(Couleur.bleuClair);
		l.setText("");

		Button buttonCalculPrix = new Button(calculPrix, SWT.BACKGROUND);
		buttonCalculPrix.setText("Afficher le prix total");
		buttonCalculPrix.setBounds(10, 60, 200, 30);

		buttonCalculPrix.addSelectionListener(new SelectionAdapter() {
			@Override public void widgetSelected(SelectionEvent arg0) {
				Double somme = 0.0;
				for (TableItem i : tableProduit.getItems()) {
					Integer quantite = Integer.parseInt(i.getText(5));
					Double prix = Double.parseDouble(i.getText(2));
					somme += quantite * prix;
				}
				prix.setText(somme.toString());
			}
		});



		buttonValidation.addSelectionListener(new SelectionAdapter() {
			@Override public void widgetSelected(SelectionEvent arg0) {
				//d'abord, on calcul le prix pour etre sur
				Double somme = 0.0;
				for (TableItem i : tableProduit.getItems()) {
					Integer quantite = Integer.parseInt(i.getText(5));
					Double prix = Double.parseDouble(i.getText(2));
					somme += quantite * prix;
				}
				prix.setText(somme.toString());

				Integer idChantier = 0;
				try { 
					String c = chantier.getText().split(";")[1].replace(" ",""); 
					idChantier = Integer.parseInt(c.substring(3,c.length()));
				} catch (Throwable e) { 
					e.printStackTrace();
					System.out.println("erreur dans la modif"); 
					MessageBox dialog = new MessageBox(vueLivraison.getShell(), SWT.ICON_ERROR | SWT.OK);
					dialog.setText("Erreur Modification"); 
					dialog.setMessage("Merci d'indiquer un chantier."); 
					dialog.open(); 
				}	

				try { 
					if (idChantier != 0) {
						ArrayList<Integer> produits = new ArrayList<Integer>();
						ArrayList<Integer> quantites = new ArrayList<Integer>();

						for(TableItem i : tableProduit.getItems()) {
							produits.add(Integer.parseInt(i.getText(0)));
							quantites.add(Integer.parseInt(i.getText(5)));
						}
						validerCreation(idChantier, produits, quantites, prix.getText(), date.getText());
					}
				} catch (Throwable e) { e.printStackTrace();
				System.out.println("erreur dans la modif"); 
				MessageBox dialog = new MessageBox(vueLivraison.getShell(), SWT.ICON_ERROR | SWT.OK);
				dialog.setText("Erreur Modification"); 
				dialog.setMessage("Une erreur est survenue. "+e.getMessage()); 
				dialog.open(); 
				}	
			}
		});
		vue.pack();
	}

	/***
	 * cree une livraison a partir du formulaire et l'insere dans la base de donnees
	 */
	public void validerCreation(Integer idChantier, ArrayList<Integer> produits, ArrayList<Integer> quantites, String prix, String date) {

		//champs obligatoires
		Delivery livraison = new Delivery(idChantier);
		livraison.setStatus("Publié");

		//champs optionels
		if (prix != null) {
			if (prix != "" && prix.contains(".")) {
				livraison.setPrixTotal(Double.parseDouble(prix));
			}
			else if (prix != "" && prix.matches(".*\\d.*")) {
				livraison.setPrixTotal(Double.parseDouble(prix+".0"));
			}
		}

		if (!(date.isEmpty())) {
			livraison.setDate(date);
		}

		//on insert dans la base de données
		int idLivraison = 0;
		boolean toutVaBien = true ;
		try { 
			idLivraison = livraison.insertDatabase(); 
			System.out.println("on a insere la livraison !!");
		} catch (SQLException e) { 
			toutVaBien = false;
			System.out.println("erreur dans la création");
			MessageBox dialog = new MessageBox(vueLivraison.getShell(), SWT.ICON_ERROR | SWT.OK);
			dialog.setText("Erreur Création");
			dialog.setMessage("Une erreur est survenue lors de la création de la livraison. "+'\n'+e.getMessage());
			dialog.open();
		} 

		for(int i = 0 ; i<produits.size() ; i++) {
			if (quantites.get(i) != 0) {
				ProductByDelivery p = new ProductByDelivery(idLivraison,  produits.get(i), quantites.get(i), "Publié");
				try {
					p.insertDatabase();
				} catch (SQLException e) {
					toutVaBien = false;
					System.out.println("erreur dans la création");
					MessageBox dialog = new MessageBox(vueLivraison.getShell(), SWT.ICON_ERROR | SWT.OK);
					dialog.setText("Erreur Création");
					dialog.setMessage("Une erreur est survenue lors de la création de la livraison. "+'\n'+e.getMessage());
					dialog.open();
				}
			}
		}

		if (toutVaBien) {
			MessageBox dialog = new MessageBox(vueLivraison.getShell(), SWT.ICON_INFORMATION | SWT.OK);
			dialog.setText("Création réussie");
			dialog.setMessage("La livraison a bien été ajoutée à la base de données.");
			dialog.open();
			newVueLivraison();
		}

	}

	//--------------------------------------------------------------------------
	/***
	 * affiche le tableau avec tous les livraisons dans la base de donnees dont le status est publie
	 */
	public void vueLivraisonAfficher() {
		if (!Objects.isNull(vue) && !vue.isDisposed()) {
			vue.dispose();
		}
		RowLayout rowLayoutV = new RowLayout();
		rowLayoutV.type = SWT.VERTICAL;

		vue = new Composite(vueLivraison, SWT.NONE);
		vue.setLayout(rowLayoutV);
		vue.setBackground(Couleur.gris);

		//creation de la table
		tableLivraison = new Table (vue, SWT.BORDER | SWT.MULTI| SWT.V_SCROLL | SWT.FULL_SELECTION);
		tableLivraison.setLayoutData(new RowData(450, 390));
		tableLivraison.setLinesVisible (true);
		tableLivraison.setHeaderVisible (true);

		//on met les noms des colonnes
		String[] titles = {"Chantier","Date", "Prix Total"};
		for (String title : titles) {
			TableColumn column = new TableColumn (tableLivraison, SWT.NONE);
			column.setText (title);
		}

		//je voulais cacher cette colonne mais ca ne fonctionne pas
		TableColumn column = new TableColumn (tableLivraison, SWT.NONE);
		column.setText ("Id DB");
		column.setWidth(0);
		column.setResizable(false);

		//on remplit la table
		final TableColumn [] columns = tableLivraison.getColumns ();

		updateTable(tableLivraison);

		//on pack les colonnes
		for (TableColumn col : columns)
			col.pack ();

		//on ajoute un listener pour modifier l'interface si l'utilisateur clique sur une ligne
		tableLivraison.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				if (tableLivraison.getSelectionIndex() != -1) {

					try {
						System.out.println(Integer.parseInt(tableLivraison.getSelection()[0].getText(3)));
						selectedLivraison = Delivery.getLivraisonById(Integer.parseInt(tableLivraison.getSelection()[0].getText(3)));
					} catch (NumberFormatException | SQLException e1) {
						System.out.println("erreur pour recuperer la livraison selectionnée");
						MessageBox dialog = new MessageBox(vueLivraison.getShell(), SWT.ICON_ERROR | SWT.OK);
						dialog.setText("Erreur");
						dialog.setMessage("Une erreur est survenue. "+'\n'+e1.getMessage());
						dialog.open();
					}
					compositeSelectionCreer();

					//on ajoute un menu lorsque l'on fait clique droit sur une ligne
					doMenu(tableLivraison);
				}
				else { // si plus rien n'est selectionner on passe selectedLivraison a null et on enleve le menu du clic droit et on enleve les boutons pour modifier et supprimer

					selectedLivraison = null;

					menu.dispose();
					menu = new Menu (vueLivraison.getShell(), SWT.POP_UP);
					tableLivraison.setMenu (menu);

					compositeSelectionCreer();
				}
			}
		});
		vue.pack();
	}

	/***
	 * remplit la table de toutes les livraisons publiees
	 * @param table
	 */
	public void updateTable(Table table) {
		table.removeAll();
		try {
			for (Delivery l : Delivery.getAllLivraison()) {
				//on verifie le status
				if (l.getStatus().contentEquals("Publié")) {
					TableItem item = new TableItem (table, SWT.NONE);
					item.setText(0,Site.getChantierById(l.getIdChantier()).getNom());
					//item.setText(1,Produit.getProductById(l.getIdProduit()).getNom());
					if (l.getDate() == null) {
						item.setText(1,"");
					}
					else {
						item.setText(1,l.getDate());
					}
					item.setText(2,l.getPrixTotal().toString());
					item.setText(3,Integer.toString(l.getLivraisonId()));
				}
			}
		} catch (SQLException e) {
			System.out.println("erreur dans la table des livraisons");
			MessageBox dialog = new MessageBox(vueLivraison.getShell(), SWT.ICON_ERROR | SWT.OK);
			dialog.setText("Erreur");
			dialog.setMessage("Une erreur est survenue. "+'\n'+e.getMessage());
			dialog.open();
		}
	}

	/***
	 * cree un menu sur la selection de la table des livraisons lors d'un clic droit
	 * @param table
	 */	
	public void doMenu(Table table) {
		menu = new Menu(tabFolder.getShell(), SWT.POP_UP);
		table.setMenu(menu);

		if (selectedAmorti != null || selectedLivraison != null || selectedFS != null) {
			// pour modifier
			MenuItem update = new MenuItem(menu, SWT.PUSH);
			update.setText("Modifier l'element");
			update.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent arg0) {
					if (table.getSelection().length != 0) {
						if (selectedAmorti!=null) {
							vueCreationA();
						}
						else if (selectedFS!=null) {
							vueCreationFS();
						}
						else {
							vueLivraisonModifier();
						}
					}
				}
			});
		}

		// pour supprimer
		MenuItem delete = new MenuItem(menu, SWT.PUSH);
		delete.setText("Supprimer l'element");
		delete.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				try {
					if (selectedAmorti!=null) {
						AmmortissementChantier ae = AmmortissementChantier.getAmmortissementChantierById(selectedAmorti.getAmmortissementChantierId());
						MessageBox dialog = new MessageBox(tabFolder.getShell(), SWT.ICON_QUESTION | SWT.YES | SWT.NO);
						dialog.setText("Suppression Amortissement Chantier");
						dialog.setMessage("Voulez vous supprimer le coût à amortir du chantier " + Site.getChantierById(ae.getChantierId()).getNom() + " ?");
						int buttonID = dialog.open();
						switch (buttonID) {
						case SWT.YES:
							ae.setStatus("Archivé");
							ae.updateDatabase();
							updateTableAmorti();
						}
						selectedAmorti = null;
						amortiSelection();
					}
					else if (selectedFS!=null) {
						FournitureSanitaire fs = FournitureSanitaire.getFournitureSanitaireById(selectedFS.getFournitureSanitaireId());
						MessageBox dialog = new MessageBox(tabFolder.getShell(), SWT.ICON_QUESTION | SWT.YES | SWT.NO);
						dialog.setText("Suppression Founiture Sanitaire");
						dialog.setMessage("Voulez vous supprimer le coût de fourniture sanitaire du chantier " + Site.getChantierById(fs.getChantierId()).getNom() + " ?");
						int buttonID = dialog.open();
						switch (buttonID) {
						case SWT.YES:
							fs.setStatus("Archivé");
							fs.updateDatabase();
							updateTableFS();
						}
						selectedFS = null;
						fsSelection();
					}
					else {
						suppLivraison();
					}
					selectedChantier = null; selectedAmorti = null;selectedFS=null;
					menu.dispose();
					menu = new Menu(tabFolder.getShell(), SWT.POP_UP);
					table.setMenu(menu);
				} catch (Exception e) {
					System.out.println("erreur pour supprimer l'element");
					MessageBox dialog = new MessageBox(tabFolder.getShell(), SWT.ICON_ERROR | SWT.OK);
					dialog.setText("Erreur");
					dialog.setMessage("Une erreur est survenue. " + '\n' + e.getMessage());
					dialog.open();
				}
			}
		});

	}


	public Composite getComposite() {
		return this.tabFolder;
	}

}
