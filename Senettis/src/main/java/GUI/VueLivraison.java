package GUI;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

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

import classes.Chantier;
import classes.Livraison;
import classes.Produit;
import classes.ProduitParLivraison;

public class VueLivraison {

	private Composite parent;
	private Display display;
	private Composite vueLivraison;
	private Composite selection;
	private Composite vue;
	private Livraison selectedLivraison;
	private Menu menu ;

	//Creation VueLivraison --------------------------------------------------
	/***
	 * Utilisé depuis Home pour créer une vueLivraison
	 * @param composite : le composite parent
	 * @param display
	 */
	public VueLivraison (Composite composite,Display display) {
		this.parent = composite;
		this.display=display;
	 	Couleur.setDisplay(display); // pour utiliser les couleurs du fichier couleur
		
		newVueLivraison(parent);
	}
	
	/***
	 * Pour créer une vueLivraison : dispose si une vueLivraison existe deja, creer le composite et lui affecte le layout RowLayout Vertical
	 * Appelle ensuite les fonctions compositeSelectionCreer et vueLivraisonAfficher
	 * @param composite : composite parent
	 */
	public void newVueLivraison (Composite composite) {
		if (vueLivraison != null){
			for (Control c : vueLivraison.getChildren()) {
				c.dispose();
			}
			vueLivraison.dispose();
		}
		
		vueLivraison=new Composite(composite,SWT.NONE);
		RowLayout rowLayout = new RowLayout();
		rowLayout.type = SWT.VERTICAL;
		vueLivraison.setLayout(rowLayout);
		
		compositeSelectionCreer(vueLivraison);
		vueLivraisonAfficher(vueLivraison);
		
		vue.pack(); selection.pack(); vueLivraison.pack();
		vueLivraison.getParent().pack();
	}
	
	//Modification de la partie Selection --------------------------------------------------
	/***
	 * Creation de la partie Selection (la partie superieure droite) avec uniquement le bouton Creer 
	 * @param composite : composite parent
	 */
	public void compositeSelectionCreer(Composite composite) {
		selection = new Composite(composite, SWT.NONE);
		RowLayout rowLayout = new RowLayout();
		rowLayout.marginWidth = 20;
		selection.setLayout(rowLayout);
		selection.setBackground(Couleur.gris);
		
		Button boutonCreer = new Button(selection, SWT.CENTER);
		boutonCreer.setText("Créer");
		boutonCreer.addSelectionListener(new SelectionAdapter() {	
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				vueLivraisonCreer();
			}
		});
		selection.pack();
	}
	
	/***
	 * Creation de la partie Selection (la partie superieure droite) avec les boutons Creer, Modifier et Supprimer
	 * @param composite : composite parent 
	 */
	public void compositeSelectionModifier(Composite composite) {
		selection = new Composite(composite, SWT.NONE);
		RowLayout rowLayout = new RowLayout();
		rowLayout.marginWidth = 22;
		rowLayout.marginTop = 6;
		selection.setLayout(rowLayout);
		selection.setBackground(Couleur.gris);
		
		//Bouton Creer 
		Button boutonCreer = new Button(selection, SWT.CENTER);
		boutonCreer.setText("Créer");
		boutonCreer.addSelectionListener(new SelectionAdapter() {	
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				vueLivraisonCreer();
			}
		});
		
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
			Livraison l = Livraison.getLivraisonById(selectedLivraison.getLivraisonId());
			
			//on demande une confirmation
			MessageBox dialog = new MessageBox(parent.getShell(), SWT.ICON_QUESTION | SWT.YES | SWT.NO);
	    	dialog.setText("Suppression Livraison");
	    	if (l.getDate() != null) {
	    		dialog.setMessage("Voulez vous supprimer le livraison du "+l.getDate()+" sur le chantier "+Chantier.getChantierById(l.getIdChantier()).getNom()+" ?");
	    	}
	    	else {
	    		dialog.setMessage("Voulez vous supprimer le livraison de "+l.getPrixTotal()+" euros sur le chantier "+Chantier.getChantierById(l.getIdChantier()).getNom()+" ?");
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
						for (ProduitParLivraison p : ProduitParLivraison.getProductByLivraisonByLivraisonId(l.getLivraisonId())) {
							p.setStatus("Archivé");
							p.updateDatabase();
						}
					}catch (Exception e) {}
					
					//on change d'affichage
					newVueLivraison(parent);
					
					selectedLivraison = null;
	        }

		} catch (NumberFormatException | SQLException e) {
			System.out.println("erreur pour supprimer la livraison");
	    	MessageBox dialog = new MessageBox(parent.getShell(), SWT.ICON_ERROR | SWT.OK);
	    	dialog.setText("Erreur");
	    	dialog.setMessage("Une erreur est survenue. "+'\n'+e.getMessage());
	    	dialog.open();
		}
	}
	
	

	//Modification d'une livraison --------------------------------------------------
	/***
	 * Regroupe les fonctions a appeler pour faire une modification
	 * La fonction va dispose pour vue et selection (les deux composantes de droite) 
	 * et va appeler les fonctions titreModification et formulaireModification
	 */
	public void vueLivraisonModifier() {
		vue.dispose();
		selection.dispose();
		
		titreModification();		
		formulaireModification();
		
		vueLivraison.pack();
		vueLivraison.getParent().pack();
		
	}
	
	/***
	 * va modifier la partie selection (partie superieure droite) en y mettant un titre pour la modification
	 */
	public void titreModification() {
		selection = new Composite(vueLivraison, SWT.CENTER);
		
		FillLayout fillLayout = new FillLayout();
		fillLayout.type = SWT.VERTICAL;
		fillLayout.marginWidth = 315;
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
		labelChantier.setText("Chantier* : ");
		
		//on affiche le chantier selectionne
		Combo chantier = new Combo(compositeChantier, SWT.BORDER);
		try {
			chantier.setText(Chantier.getChantierById(selectedLivraison.getIdChantier()).getNom()+"; id :"+selectedLivraison.getIdChantier().toString());
		} catch (SQLException e1) {
			e1.printStackTrace(); 
	    	System.out.println("erreur pour recuperer les chantiers");
	    	MessageBox dialog = new MessageBox(parent.getShell(), SWT.ICON_ERROR | SWT.OK);
	    	dialog.setText("Erreur");
	    	dialog.setMessage("Une erreur est survenue. "+'\n'+e1.getMessage());
	    	dialog.open();
		}
		//on recupere les autres chantiers pour les afficher aussi et pouvoir modifier le chantier actuel
		try {
			for (Chantier c : Chantier.getAllChantier()) {
				if (c.getNom() != Chantier.getChantierById(selectedLivraison.getIdChantier()).getNom()) {
					chantier.add(c.getNom()+"; id :"+((Integer)c.getChantierId()).toString());
				}
			}
		} catch (SQLException e1) {
			e1.printStackTrace(); 
	    	System.out.println("erreur pour recuperer les chantiers");
	    	MessageBox dialog = new MessageBox(parent.getShell(), SWT.ICON_ERROR | SWT.OK);
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
				newVueLivraison(parent);
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
	    tableProduit.setLayoutData(new RowData(345, 141));
	    tableProduit.setLinesVisible (true);
	    tableProduit.setHeaderVisible (true);
	    
	    //pour pouvoir modifier les quantites
	    final TableEditor editor = new TableEditor (tableProduit);
		editor.horizontalAlignment = SWT.LEFT;
		editor.grabHorizontal = true;
		
		//on met les noms des colonnes
		String[] titles = {"Id","Nom","Prix", "Commentaires", "Quantité"};
		for (String title : titles) {
			TableColumn column = new TableColumn (tableProduit, SWT.NONE);
			column.setText (title);
		}
		
		//on remplit la table
		final TableColumn [] columns = tableProduit.getColumns ();
		
		//on remplit d'abord avec les quantites a 0 et on modifiera apres
		try {
			for (Produit p : Produit.getAllProduct()) {
				//on verifie le status
				if (p.getStatus().contentEquals("Publié")) {
					TableItem item = new TableItem (tableProduit, SWT.NONE);
					item.setText(0,((Integer)p.getProduitId()).toString());
					item.setText(1,p.getNom());
					item.setText(2,p.getPrix().toString());
					item.setText(3,p.getComment());
					item.setText(4,"0");
				}
			}
		} catch (SQLException e1) {
	    	System.out.println("erreur dans la table des produits de livraison");
	    	MessageBox dialog = new MessageBox(parent.getShell(), SWT.ICON_ERROR | SWT.OK);
	    	dialog.setText("Erreur");
	    	dialog.setMessage("Une erreur est survenue. "+'\n'+e1.getMessage());
	    	dialog.open();
		}
		
		//on modifie les quantites du tableau en y mettant celles des produits par livraison de la base de donnees
		try {
			if (selectedLivraison.getPrixTotal() > 0) {
				for (ProduitParLivraison p : ProduitParLivraison.getProductByLivraisonByLivraisonId(selectedLivraison.getLivraisonId())) {
					//on verifie le status
					if (p.getStatus().contentEquals("Publié")) {
						for (TableItem i : tableProduit.getItems()) {
							if (Integer.parseInt(i.getText(0)) == p.getIdProduit() ) {
								i.setText(4, p.getQuantite().toString());
							}
						}
					}
				}
			}
		} catch (NumberFormatException | SQLException e1) {
			System.out.println("erreur dans la table des produits de livraison");
			e1.printStackTrace();
			MessageBox dialog = new MessageBox(parent.getShell(), SWT.ICON_ERROR | SWT.OK);
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
				newEditor.setText(item.getText(4));
				newEditor.addModifyListener(new ModifyListener() { 
					public void modifyText(ModifyEvent me) { 
						if (!(newEditor.getText().isEmpty())) {//pour ne pas tester quand l'utilisateur est en train de modifier
							try {
								if (Integer.parseInt(newEditor.getText()) <0) {//on ne peut pas mettre de quantite negative
									item.setText(4,"0"); 
								}
								else {
									item.setText(4,newEditor.getText()); //on modifie la ligne avec la nouvelle quantite
								}
							} catch(Exception e) {
								System.out.println("erreur dans la modif"); MessageBox dialog = new
										MessageBox(parent.getShell(), SWT.ICON_ERROR | SWT.OK);
								dialog.setText("Erreur Editor"); dialog.
								setMessage("La quantite saisie n'est pas valide. "
										+'\n'+e.getMessage()); dialog.open(); 
							}
						}
					}
				});
				newEditor.selectAll(); newEditor.setFocus();
				editor.setEditor(newEditor, item, 4);
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
						Integer quantite = Integer.parseInt(i.getText(4));
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
						Integer quantite = Integer.parseInt(i.getText(4));
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
						MessageBox dialog = new MessageBox(parent.getShell(), SWT.ICON_ERROR | SWT.OK);
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
								quantites.add(Integer.parseInt(i.getText(4)));
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
						MessageBox dialog = new MessageBox(parent.getShell(), SWT.ICON_ERROR | SWT.OK);
						dialog.setText("Erreur Modification"); 
						dialog.setMessage("Une erreur est survenue. "+e.getMessage()); 
						dialog.open(); 
					}	
			  }
		}); 

	}
	
	/***
	 * modifie la base de données
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
			MessageBox dialog = new MessageBox(parent.getShell(), SWT.ICON_ERROR | SWT.OK);
			dialog.setText("Erreur Modification");
			dialog.setMessage("Une erreur est survenue lors de la modification de la livraison. "+e.getMessage());
			dialog.open();
		} 
		
		//on modifie les produits associes à la livraison
	    for(int i = 0 ; i<produits.size() ; i++) {
	    	try {//si le produit est deja associe a la livraison, on le modifie
	    		ProduitParLivraison p = ProduitParLivraison.getProductByLivraisonByLivraisonIdAndProductId(selectedLivraison.getLivraisonId(), produits.get(i));
	    		p.setQuantite(quantites.get(i));
	    		p.updateDatabase();
	    	}catch (Exception e) {//si le produit n'est pas encore associe a la livraison on l'associe si la quantite n'est pas de 0
	    		if (quantites.get(i) != 0) {
		    		ProduitParLivraison p = new ProduitParLivraison(selectedLivraison.getLivraisonId(),  produits.get(i), quantites.get(i), "Publié");
		    		try {
						p.insertDatabase();
					} catch (SQLException e1) {
						toutVaBien = false;
						System.out.println("erreur dans la création");
				    	MessageBox dialog = new MessageBox(parent.getShell(), SWT.ICON_ERROR | SWT.OK);
				    	dialog.setText("Erreur Création");
				    	dialog.setMessage("Une erreur est survenue lors de la création de la livraison. "+'\n'+e1.getMessage());
				    	dialog.open();
					}
		    	}
	    	}
	    }

	    if (toutVaBien) { //s'il n'y a pas eu d'erreur on affiche la pop up de validation
	    	MessageBox dialog = new MessageBox(parent.getShell(), SWT.ICON_INFORMATION | SWT.OK);
	    	dialog.setText("Création réussie");
	    	dialog.setMessage("La livraison a bien été modifiée dans la base de données.");
	    	dialog.open();
	    	
	    	selectedLivraison = null;
	    	newVueLivraison(parent);
	    	vue.pack(); selection.pack(); vueLivraison.pack();
	    }
	}
	
	//Création d'une livraison --------------------------------------------------
	/***
	 * va appeler les fonctions qui modifient la partie vue et la partie selection afin d'afficher le formulaire de création 
	 * appelle titreCreation et formulaireCreation
	 */	
	public void vueLivraisonCreer() {
		vue.dispose();
		selection.dispose();
		
		titreCreation();		
		formulaireCreation();
		
		vueLivraison.pack();
		vueLivraison.getParent().pack();
		
	}
	
	/***
	 * modifie la partie selection (partie superieur droite) en ajoutant un titre de creation
	 */
	public void titreCreation() {
		selection = new Composite(vueLivraison, SWT.CENTER);
		
		FillLayout fillLayout = new FillLayout();
		fillLayout.type = SWT.VERTICAL;
		fillLayout.marginWidth = 328;
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
		vue = new Composite(vueLivraison, SWT.NONE);
		RowLayout rowLayoutH = new RowLayout();
		rowLayoutH.type = SWT.HORIZONTAL;
		vue.setLayout(rowLayoutH);
		
		//creation de 2 colonnes afin de repartir les champs du formulaire 
		FillLayout fillLayoutV = new FillLayout();
		fillLayoutV.type = SWT.VERTICAL;
		fillLayoutV.marginWidth = 10;
		
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
		labelChantier.setText("Chantier* : ");
		
		Combo chantier = new Combo(compositeChantier, SWT.BORDER);
		chantier.setText("Selectionner ...");
		try {
			for (Chantier c : Chantier.getAllChantier()) {
				chantier.add(c.getNom()+"; id :"+((Integer)c.getChantierId()).toString());
			}
		} catch (SQLException e1) {
			e1.printStackTrace(); 
	    	System.out.println("erreur pour recuperer les chantiers");
	    	MessageBox dialog = new MessageBox(parent.getShell(), SWT.ICON_ERROR | SWT.OK);
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
				newVueLivraison(parent);
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
	    tableProduit.setLayoutData(new RowData(345, 141));
	    tableProduit.setLinesVisible (true);
	    tableProduit.setHeaderVisible (true);
	    
	    final TableEditor editor = new TableEditor (tableProduit);
		editor.horizontalAlignment = SWT.LEFT;
		editor.grabHorizontal = true;
		
		//on met les noms des colonnes
		String[] titles = {"Id","Nom","Prix", "Commentaires", "Quantité"};
		for (String title : titles) {
			TableColumn column = new TableColumn (tableProduit, SWT.NONE);
			column.setText (title);
		}
		
		//on remplit la table
		try {
			for (Produit p : Produit.getAllProduct()) {
				if (p.getStatus().contentEquals("Publié")) {
					TableItem item = new TableItem (tableProduit, SWT.NONE);
					item.setText(0,((Integer)p.getProduitId()).toString());
					item.setText(1,p.getNom());
					item.setText(2,p.getPrix().toString());
					item.setText(3,p.getComment());
					item.setText(4,"0");
				}
			}
		} catch (SQLException e1) {
			System.out.println("erreur dans la table des produits de livraison");
			MessageBox dialog = new MessageBox(parent.getShell(), SWT.ICON_ERROR | SWT.OK);
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
				newEditor.setText(item.getText(4));
				newEditor.addModifyListener(new ModifyListener() { 
					public void modifyText(ModifyEvent me) { 
						if (!(newEditor.getText().isEmpty())) {
							try {
								if (Integer.parseInt(newEditor.getText()) <0) {
									item.setText(4,"0"); 
								}
								else {
									item.setText(4,newEditor.getText()); 
								}
							} catch(Exception e) {
								System.out.println("erreur dans la modif"); MessageBox dialog = new
										MessageBox(parent.getShell(), SWT.ICON_ERROR | SWT.OK);
								dialog.setText("Erreur Editor"); dialog.
								setMessage("La quantite saisie n'est pas valide. "
										+'\n'+e.getMessage()); dialog.open(); 
							}
						}
					} 
				});
				newEditor.selectAll(); newEditor.setFocus();
				editor.setEditor(newEditor, item, 4);
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
						Integer quantite = Integer.parseInt(i.getText(4));
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
						Integer quantite = Integer.parseInt(i.getText(4));
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
						MessageBox dialog = new MessageBox(parent.getShell(), SWT.ICON_ERROR | SWT.OK);
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
								quantites.add(Integer.parseInt(i.getText(4)));
							}
							validerCreation(idChantier, produits, quantites, prix.getText(), date.getText());
						}
					} catch (Throwable e) { e.printStackTrace();
						System.out.println("erreur dans la modif"); 
						MessageBox dialog = new MessageBox(parent.getShell(), SWT.ICON_ERROR | SWT.OK);
						dialog.setText("Erreur Modification"); 
						dialog.setMessage("Une erreur est survenue. "+e.getMessage()); 
						dialog.open(); 
					}	
			  }
		});

	}

	/***
	 * cree une livraison a partir du formulaire et l'inserer dans la base de donnees
	 */
	public void validerCreation(Integer idChantier, ArrayList<Integer> produits, ArrayList<Integer> quantites, String prix, String date) {

		//champs obligatoires
		Livraison livraison = new Livraison(idChantier);
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
	    	MessageBox dialog = new MessageBox(parent.getShell(), SWT.ICON_ERROR | SWT.OK);
	    	dialog.setText("Erreur Création");
	    	dialog.setMessage("Une erreur est survenue lors de la création de la livraison. "+'\n'+e.getMessage());
	    	dialog.open();
	    } 
	    
	    for(int i = 0 ; i<produits.size() ; i++) {
	    	if (quantites.get(i) != 0) {
	    		ProduitParLivraison p = new ProduitParLivraison(idLivraison,  produits.get(i), quantites.get(i), "Publié");
	    		try {
					p.insertDatabase();
				} catch (SQLException e) {
					toutVaBien = false;
					System.out.println("erreur dans la création");
			    	MessageBox dialog = new MessageBox(parent.getShell(), SWT.ICON_ERROR | SWT.OK);
			    	dialog.setText("Erreur Création");
			    	dialog.setMessage("Une erreur est survenue lors de la création de la livraison. "+'\n'+e.getMessage());
			    	dialog.open();
				}
	    	}
	    }

	    if (toutVaBien) {
	    	MessageBox dialog = new MessageBox(parent.getShell(), SWT.ICON_INFORMATION | SWT.OK);
	    	dialog.setText("Création réussie");
	    	dialog.setMessage("La livraison a bien été ajoutée à la base de données.");
	    	dialog.open();
	    	newVueLivraison(parent);
	    	vue.pack(); selection.pack(); vueLivraison.pack();
	    }
	    
	}
	
    //--------------------------------------------------------------------------
	/***
	 * affiche le tableau avec tous les livraisons dans la base de donnees dont le status est publie
	 */
	public void vueLivraisonAfficher(Composite composite) {
		
		RowLayout rowLayoutV = new RowLayout();
	    rowLayoutV.type = SWT.VERTICAL;
		
	    vue = new Composite(composite, SWT.NONE);
		vue.setLayout(rowLayoutV);
		vue.setBackground(Couleur.gris);
		
		//creation de la table
	    final Table table = new Table (vue, SWT.BORDER | SWT.MULTI| SWT.V_SCROLL | SWT.FULL_SELECTION);
	    table.setLayoutData(new RowData(300, 390));
	    table.setLinesVisible (true);
		table.setHeaderVisible (true);
		
		//on met les noms des colonnes
		String[] titles = {"Chantier","Date", "Prix Total"};
		for (String title : titles) {
			TableColumn column = new TableColumn (table, SWT.NONE);
			column.setText (title);
		}
		
		//je voulais cacher cette colonne mais ca ne fonctionne pas
		TableColumn column = new TableColumn (table, SWT.NONE);
		column.setText ("Id DB");
		column.setWidth(0);
		column.setResizable(false);
		
		//on remplit la table
		final TableColumn [] columns = table.getColumns ();
		try {
			for (Livraison l : Livraison.getAllLivraison()) {
				//on verifie le status
				if (l.getStatus().contentEquals("Publié")) {
					TableItem item = new TableItem (table, SWT.NONE);
					item.setText(0,Chantier.getChantierById(l.getIdChantier()).getNom());
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
	    	MessageBox dialog = new MessageBox(parent.getShell(), SWT.ICON_ERROR | SWT.OK);
	    	dialog.setText("Erreur");
	    	dialog.setMessage("Une erreur est survenue. "+'\n'+e.getMessage());
	    	dialog.open();
		}
		
		//on pack les colonnes
		for (TableColumn col : columns)
			col.pack ();
		
		//on ajoute un listener pour modifier l'interface si l'utilisateur clique sur une ligne
		table.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				if (table.getSelectionIndex() != -1) {
					System.out.println("not -1");
					
					selection.dispose();
					try {
						System.out.println(Integer.parseInt(table.getSelection()[0].getText(3)));
						selectedLivraison = Livraison.getLivraisonById(Integer.parseInt(table.getSelection()[0].getText(3)));
					} catch (NumberFormatException | SQLException e1) {
						System.out.println("erreur pour recuperer la livraison selectionnée");
				    	MessageBox dialog = new MessageBox(parent.getShell(), SWT.ICON_ERROR | SWT.OK);
				    	dialog.setText("Erreur");
				    	dialog.setMessage("Une erreur est survenue. "+'\n'+e1.getMessage());
				    	dialog.open();
					}
					compositeSelectionModifier(vueLivraison);
					
					//on ajoute un menu lorsque l'on fait clique droit sur une ligne
					doMenu(table);
				}
				else { // si plus rien n'est selectionner on passe selectedLivraison a null et on enleve le menu du clic droit et on enleve les boutons pour modifier et supprimer
					System.out.println("-1");
					
					selectedLivraison = null;
					
					menu.dispose();
					menu = new Menu (composite.getShell(), SWT.POP_UP);
					table.setMenu (menu);
					
					selection.dispose();
					compositeSelectionCreer(vueLivraison);
				}
			}
		});
		
	}
	
	public void doMenu(Table table) {
		menu = new Menu (parent.getShell(), SWT.POP_UP);
		table.setMenu (menu);

		//pour modifier
		MenuItem update = new MenuItem (menu, SWT.PUSH);
		update.setText ("Modifier l'element");
		update.addSelectionListener(new SelectionAdapter() {	
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				if (table.getSelection().length != 0) {
					vueLivraisonModifier();
				}
			}
		});
		
		//pour supprimer
		MenuItem delete = new MenuItem (menu, SWT.PUSH);
		delete.setText ("Supprimer l'element");
		delete.addSelectionListener(new SelectionAdapter() {	
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				suppLivraison();
			}
		});
	}
	
	public Composite getComposite() {
		return this.vueLivraison;
	}
	
}
