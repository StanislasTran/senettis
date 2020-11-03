package GUI;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

import org.eclipse.swt.*;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

import classes.Chantier;
import classes.Livraison;
import classes.Produit;

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
				try {
					if (selectedLivraison == null) {
						throw new Error("selectedLivraison est vide");
					}
					Livraison l = Livraison.getLivraisonById(selectedLivraison.getLivraisonId());
					MessageBox dialog = new MessageBox(parent.getShell(), SWT.ICON_QUESTION | SWT.YES | SWT.NO);
			    	dialog.setText("Suppression Livraison");
			    	dialog.setMessage("Voulez vous supprimer le livraison du "+l.getDate()+" ?");
			    	int buttonID = dialog.open();
			        switch(buttonID) {
			          case SWT.YES:
							l.setStatus("Archivé");
							l.updateDatabase();
							newVueLivraison(parent);
							selectedLivraison = null;
			        }

				} catch (NumberFormatException | SQLException e) {
					System.out.println("erreur dans la supression");
			    	MessageBox dialog = new MessageBox(parent.getShell(), SWT.ICON_ERROR | SWT.OK);
			    	dialog.setText("Erreur Supression");
			    	dialog.setMessage("Une erreur est survenue lors de la supression de la livraison. "+'\n'+e.getMessage());
			    	dialog.open();
					
				}
				
			}
		});
		selection.pack();
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
		fillLayout.type = SWT.HORIZONTAL;
		fillLayout.marginWidth = 7;
		selection.setLayout(fillLayout);
		
		selection.setBackground(Couleur.bleuFonce);
		Label HeadLabel =new Label(selection,SWT.TITLE);
		HeadLabel.setText("Modification d'une Livraison");
		Font fontTitle = new Font(HeadLabel.getDisplay(), "Arial", 12, SWT.BOLD);
		HeadLabel.setForeground(Couleur.bleuClair);
		HeadLabel.setFont(fontTitle);
		HeadLabel.setBackground(Couleur.bleuFonce);
		
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
		FillLayout fillLayoutH = new FillLayout();
		fillLayoutH.type = SWT.HORIZONTAL;
		vue.setLayout(fillLayoutH);
		
		//creation de 3 colonnes afin de repartir les champs du formulaire en trois 
		FillLayout fillLayoutV = new FillLayout();
		fillLayoutV.type = SWT.VERTICAL;
		fillLayoutV.marginWidth = 10;
		
		Composite colonne1 = new Composite(vue, SWT.BORDER);
		colonne1.setBackground(Couleur.bleuClair);
		colonne1.setLayout(fillLayoutV);
		
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
		try {
			chantier.setText(Chantier.getChantierById(selectedLivraison.getIdChantier()).getNom()+"; id :"+selectedLivraison.getIdProduit().toString());
		} catch (SQLException e1) {
			e1.printStackTrace(); 
	    	System.out.println("erreur pour recuperer les chantiers");
	    	MessageBox dialog = new MessageBox(parent.getShell(), SWT.ICON_ERROR | SWT.OK);
	    	dialog.setText("Erreur");
	    	dialog.setMessage("Une erreur est survenue. "+'\n'+e1.getMessage());
	    	dialog.open();
		}
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

		//Produit
		Composite compositeProduit = new Composite(colonne1, SWT.NONE);
		compositeProduit.setBackground(Couleur.bleuClair);
		compositeProduit.setLayout(fillLayoutH5);
		
		Label labelProduit = new Label(compositeProduit, SWT.NONE);
		labelProduit.setBackground(Couleur.bleuClair);
		labelProduit.setText("Produit* : ");
		
		Combo produit = new Combo(compositeProduit, SWT.BORDER);
		try {
			produit.setText(Produit.getProductById(selectedLivraison.getIdProduit()).getNom()+"; id :"+selectedLivraison.getIdProduit().toString());
		} catch (SQLException e1) {
			e1.printStackTrace(); 
	    	System.out.println("erreur pour recuperer les produits");
	    	MessageBox dialog = new MessageBox(parent.getShell(), SWT.ICON_ERROR | SWT.OK);
	    	dialog.setText("Erreur");
	    	dialog.setMessage("Une erreur est survenue. "+'\n'+e1.getMessage());
	    	dialog.open();
		}
		try {
			for (Produit p : Produit.getAllProduct()) {
				if (p.getNom() != Produit.getProductById(selectedLivraison.getIdProduit()).getNom()){
					produit.add(p.getNom()+"; id :"+((Integer)p.getProduitId()).toString());
				}
			}
		} catch (SQLException e1) {
			e1.printStackTrace(); 
	    	System.out.println("erreur pour recuperer les produits");
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
		labelDate.setText("Date* : ");

		final Text date = new Text(compositeDate, SWT.BORDER);
		date.setText(selectedLivraison.getDate());
		
		//Prix
		Composite compositePrix = new Composite(colonne1, SWT.NONE);
		compositePrix.setBackground(Couleur.bleuClair);
		compositePrix.setLayout(fillLayoutH5);

		Label labelPrix = new Label(compositePrix, SWT.NONE);
		labelPrix.setBackground(Couleur.bleuClair);
		labelPrix.setText("Prix : ");

		final Text prix = new Text(compositePrix, SWT.BORDER);
		prix.setText(selectedLivraison.getPrixTotal().toString());
		
		//Bouton Valider
		Composite compositeValidation = new Composite(colonne1, SWT.CENTER);
		compositeValidation.setBackground(Couleur.bleuClair);
		compositeValidation.setLayout(fillLayoutH5);
		
		Label labelValidation = new Label(compositeValidation, SWT.NONE);
		labelValidation.setBackground(Couleur.bleuClair);
		labelValidation.setText("");
		
		Button buttonValidation = new Button(compositeValidation, SWT.BACKGROUND);
		buttonValidation.setText("Valider");
		buttonValidation.setBounds(10, 60, 100, 25);
		buttonValidation.addSelectionListener(new SelectionAdapter() {
		  
		  @Override public void widgetSelected(SelectionEvent arg0) {

			   try { 
				   Integer id = selectedLivraison.getLivraisonId();
				   
				   String c = chantier.getText().split(";")[1].replace(" ","");
				   Integer idChantier = Integer.parseInt(c.substring(3,c.length()));
				   System.out.println(idChantier);
				   System.out.println("Voici l'id du chantier : "+idChantier.toString());
				   
				   String p = produit.getText().split(";")[1].replace(" ","");
				   Integer idProduit = Integer.parseInt(p.substring(3,p.length()));
				   
				   selectedLivraison = new Livraison(id, idChantier, idProduit, Double.parseDouble(prix.getText()), date.getText(), "Publié");
				   validerModification();
			    } catch (Throwable e) { 
			    	e.printStackTrace(); 
			    	System.out.println("erreur dans la modif");
			    	MessageBox dialog = new MessageBox(parent.getShell(), SWT.ICON_ERROR | SWT.OK);
			    	dialog.setText("Erreur Modification");
			    	dialog.setMessage("Une erreur est survenue lors de la modification de la livraison. "+'\n'+e.getMessage());
			    	dialog.open();
			    } 
			  
			  
		  }
		});
	}
	
	/***
	 * modifie la base de données
	 */
	public void validerModification() {
		if (selectedLivraison == null) {
			throw new Error("selectedLivraison est vide");
		}
		
	    //on insert dans la base de données
	    try { 
	    	selectedLivraison.updateDatabase(); 
	    	System.out.println("on a modifie la livraison !!");
	    	MessageBox dialog = new MessageBox(parent.getShell(), SWT.ICON_INFORMATION | SWT.OK);
	    	dialog.setText("Modification réussie");
	    	dialog.setMessage("La livraison a bien été modifié dans la base de données.");
	    	dialog.open();
	    	selectedLivraison = null;
	    } catch (SQLException e) { 
	    	e.printStackTrace(); 
	    	System.out.println("erreur dans la modif");
	    	MessageBox dialog = new MessageBox(parent.getShell(), SWT.ICON_ERROR | SWT.OK);
	    	dialog.setText("Erreur Modification");
	    	dialog.setMessage("Une erreur est survenue lors de la modification de la livraison. "+e.getMessage());
	    	dialog.open();
	    } 
	
	 	newVueLivraison(parent);  
	}
	
	//Création d'une livraison --------------------------------------------------
	/***
	 * va appeler les fonctions qui modifie la partie vue et la partie selection afin d'afin le formulaire de création 
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
		fillLayout.type = SWT.HORIZONTAL;
		fillLayout.marginWidth = 20;
		selection.setLayout(fillLayout);
		
		selection.setBackground(Couleur.bleuFonce);
		Label HeadLabel =new Label(selection,SWT.TITLE);
		HeadLabel.setText("Creation d'une Livraison");
		Font fontTitle = new Font(HeadLabel.getDisplay(), "Arial", 12, SWT.BOLD);
		HeadLabel.setForeground(Couleur.bleuClair);
		HeadLabel.setFont(fontTitle);
		HeadLabel.setBackground(Couleur.bleuFonce);
		
		selection.pack();
	}

	/***
	 * cree le formulaire de creation d'une livraison
	 */
	public void formulaireCreation() {
		vue = new Composite(vueLivraison, SWT.NONE);
		FillLayout fillLayoutH = new FillLayout();
		fillLayoutH.type = SWT.HORIZONTAL;
		vue.setLayout(fillLayoutH);
		
		//creation de 3 colonnes afin de repartir les champs du formulaire en trois 
		FillLayout fillLayoutV = new FillLayout();
		fillLayoutV.type = SWT.VERTICAL;
		fillLayoutV.marginWidth = 10;
		
		Composite colonne1 = new Composite(vue, SWT.BORDER);
		colonne1.setBackground(Couleur.bleuClair);
		colonne1.setLayout(fillLayoutV);
		
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

		//Produit
		Composite compositeProduit = new Composite(colonne1, SWT.NONE);
		compositeProduit.setBackground(Couleur.bleuClair);
		compositeProduit.setLayout(fillLayoutH5);
		
		Label labelProduit = new Label(compositeProduit, SWT.NONE);
		labelProduit.setBackground(Couleur.bleuClair);
		labelProduit.setText("Produit* : ");
		
		Combo produit = new Combo(compositeProduit, SWT.BORDER);
		produit.setText("Selectionner ...");
		try {
			for (Produit p : Produit.getAllProduct()) {
				produit.add(p.getNom()+"; id :"+((Integer)p.getProduitId()).toString());
			}
		} catch (SQLException e1) {
			e1.printStackTrace(); 
	    	System.out.println("erreur pour recuperer les produits");
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
		labelDate.setText("Date* : ");

		final Text date = new Text(compositeDate, SWT.BORDER);
		date.setText("");
		
		//Prix
		Composite compositePrix = new Composite(colonne1, SWT.NONE);
		compositePrix.setBackground(Couleur.bleuClair);
		compositePrix.setLayout(fillLayoutH5);

		Label labelPrix = new Label(compositePrix, SWT.NONE);
		labelPrix.setBackground(Couleur.bleuClair);
		labelPrix.setText("Prix : ");

		final Text prix = new Text(compositePrix, SWT.BORDER);
		prix.setText("");
		
		//Bouton Valider
		Composite compositeValidation = new Composite(colonne1, SWT.CENTER);
		compositeValidation.setBackground(Couleur.bleuClair);
		compositeValidation.setLayout(fillLayoutH5);
		
		Label labelValidation = new Label(compositeValidation, SWT.NONE);
		labelValidation.setBackground(Couleur.bleuClair);
		labelValidation.setText("");
		
		Button buttonValidation = new Button(compositeValidation, SWT.BACKGROUND);
		buttonValidation.setText("Valider");
		buttonValidation.setBounds(10, 60, 100, 25);
		buttonValidation.addSelectionListener(new SelectionAdapter() {
		  
		  @Override public void widgetSelected(SelectionEvent arg0) {
			  Integer idChantier = null, idProduit = null ;
			  try { 
				   String c = chantier.getText().split(";")[1].replace(" ","");
				   idChantier = Integer.parseInt(c.substring(3,c.length()));
				   System.out.println("Voici l'id du chantier : "+idChantier.toString());
				   try { 
						  String p = produit.getText().split(";")[1].replace(" ","");
						  idProduit = Integer.parseInt(p.substring(3,p.length()));
						  try {
							  validerCreation(idChantier, idProduit, prix.getText(), date.getText()); 
						  } catch (Throwable e1) { 
						    	e1.printStackTrace(); 
						    	System.out.println("erreur dans la creation");
						    	MessageBox dialog = new MessageBox(parent.getShell(), SWT.ICON_ERROR | SWT.OK);
						    	dialog.setText("Erreur Création");
						    	dialog.setMessage("Une erreur est survenue. "+e1.getMessage());
						    	dialog.open();
						  }
					  } catch (Throwable e2) { 
					    	e2.printStackTrace(); 
					    	System.out.println("erreur dans la creation");
					    	MessageBox dialog = new MessageBox(parent.getShell(), SWT.ICON_ERROR | SWT.OK);
					    	dialog.setText("Erreur Création");
					    	dialog.setMessage("Le produit est incorrect. ");
					    	dialog.open();
					  } 
			  } catch (Throwable e3) { 
			    	e3.printStackTrace(); 
			    	System.out.println("erreur dans la creation");
			    	MessageBox dialog = new MessageBox(parent.getShell(), SWT.ICON_ERROR | SWT.OK);
			    	dialog.setText("Erreur Création");
			    	dialog.setMessage("Le chantier est incorrect. ");
			    	dialog.open();
			    } 
			  
			  
			  
		  }
		});
	}

	/***
	 * cree un livraison a partir du formulaire et l'inserer dans la base de donnees
	 */
	public void validerCreation(Integer idChantier, Integer idProduit, String prix, String date) {
		
		//check les id 
		
		if (date.isEmpty()) {
			throw new Error("Merci d'indiquer une date de livraison.");
		}
		
		//champs obligatoires
		Livraison livraison = new Livraison(idChantier, idProduit, date);
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
	    
	    //on insert dans la base de données
	    try { 
	    	livraison.insertDatabase(); 
	    	System.out.println("on a insere la livraison !!");
	    	MessageBox dialog = new MessageBox(parent.getShell(), SWT.ICON_INFORMATION | SWT.OK);
	    	dialog.setText("Création réussie");
	    	dialog.setMessage("La livraison a bien été ajoutée à la base de données.");
	    	dialog.open();
		    newVueLivraison(parent);
			vue.pack(); selection.pack(); vueLivraison.pack();
	    } catch (SQLException e) { 
	    	System.out.println("erreur dans la création");
	    	MessageBox dialog = new MessageBox(parent.getShell(), SWT.ICON_ERROR | SWT.OK);
	    	dialog.setText("Erreur Création");
	    	dialog.setMessage("Une erreur est survenue lors de la création de la livraison. "+'\n'+e.getMessage());
	    	dialog.open();
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
	    table.setLayoutData(new RowData(400, 400));
	    table.setLinesVisible (true);
		table.setHeaderVisible (true);
		
		//on met les noms des colonnes
		String[] titles = {"Chantier","Produit","Date", "Prix Total"};
		for (String title : titles) {
			TableColumn column = new TableColumn (table, SWT.NONE);
			column.setText (title);
		}
		
		//je voulais cacher cette colonne mais ca ne fonctionne pas
		TableColumn column = new TableColumn (table, SWT.NONE);
		column.setText ("Id Base de données");
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
					item.setText(1,Produit.getProductById(l.getIdProduit()).getNom());
					item.setText(2,l.getDate());
					item.setText(3,l.getPrixTotal().toString());
					item.setText(4,Integer.toString(l.getLivraisonId()));
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
						System.out.println(Integer.parseInt(table.getSelection()[0].getText(4)));
						selectedLivraison = Livraison.getLivraisonById(Integer.parseInt(table.getSelection()[0].getText(4)));
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
		
		//pour supprimer
		MenuItem delete = new MenuItem (menu, SWT.PUSH);
		delete.setText ("Supprimer l'element");
		delete.addSelectionListener(new SelectionAdapter() {	
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				try {
					Livraison l = Livraison.getLivraisonById(selectedLivraison.getLivraisonId());
					MessageBox dialog = new MessageBox(parent.getShell(), SWT.ICON_QUESTION | SWT.YES | SWT.NO);
			    	dialog.setText("Suppression Livraison");
			    	dialog.setMessage("Voulez-vous supprimer le livraison du "+l.getDate()+" ?");
			    	int buttonID = dialog.open();
			        switch(buttonID) {
			          case SWT.YES:
							l.setStatus("Archivé");
							l.updateDatabase();
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
		});
		
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
	}
	
	public Composite getComposite() {
		return this.vueLivraison;
	}
	
}
