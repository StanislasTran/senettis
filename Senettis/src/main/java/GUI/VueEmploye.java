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

import classes.Employe;

public class VueEmploye {

	private Composite parent;
	private Display display;
	private Composite vueEmploye;
	private Composite selection;
	private Composite vue;
	private Employe selectedEmploye;
	private Menu menu ;

	//Creation VueEmploye --------------------------------------------------
	/***
	 * Utilisé depuis Home pour créer une vueEmploye
	 * @param composite : le composite parent
	 * @param display
	 */
	public VueEmploye (Composite composite,Display display) {
		this.parent = composite;
		this.display=display;
	 	Couleur.setDisplay(display); // pour utiliser les couleurs du fichier couleur
		
		newVueEmploye(parent);
	}
	
	public Composite getSelection() {
		return selection;
	}
	
	public Composite getVue() {
		return vue;
	}
	
	/***
	 * Pour créer une vueEmploye : dispose si une vueEmploye existe deja, creer le composite et lui affecte le layout RowLayout Vertical
	 * Appelle ensuite les fonctions compositeSelectionCreer et vueEmployeAfficher
	 * @param composite : composite parent
	 */
	public void newVueEmploye (Composite composite) {
		if (vueEmploye != null){
			vueEmploye.dispose();
		}
		
		vueEmploye=new Composite(composite,SWT.NONE);
		RowLayout rowLayout = new RowLayout();
		rowLayout.type = SWT.VERTICAL;
		vueEmploye.setLayout(rowLayout);
		
		compositeSelectionCreer(vueEmploye);
		vueEmployeAfficher(vueEmploye);
		
		vue.pack(); selection.pack(); vueEmploye.pack();
		vueEmploye.getParent().pack();
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
				vueEmployeCreer();
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
				vueEmployeCreer();
			}
		});
		
		//Bouton Modifier
		Button boutonModifier = new Button(selection, SWT.CENTER);
		boutonModifier.setText("Modifier");
		boutonModifier.addSelectionListener(new SelectionAdapter() {	
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				vueEmployeModifier();
			}
		});
		
		//Bouton Supprimer
		Button boutonSupprimer = new Button(selection, SWT.CENTER);
		boutonSupprimer.setText("Supprimer");
		boutonSupprimer.addSelectionListener(new SelectionAdapter() {	
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				try {
					if (selectedEmploye == null) {
						throw new Error("selectedEmploye est vide");
					}
					Employe e = Employe.getEmployeById(selectedEmploye.getEmployeId());
					MessageBox dialog = new MessageBox(parent.getShell(), SWT.ICON_QUESTION | SWT.YES | SWT.NO);
			    	dialog.setText("Suppression Employe");
			    	dialog.setMessage("Voulez vous supprimer l'employé "+e.getNom()+" "+e.getPrenom()+" ?");
			    	int buttonID = dialog.open();
			        switch(buttonID) {
			          case SWT.YES:
							e.setStatus("Archivé");
							e.updateDatabase();
							newVueEmploye(parent);
							selectedEmploye = null;
			        }

				} catch (NumberFormatException | SQLException e) {
					System.out.println("erreur dans la supression");
			    	MessageBox dialog = new MessageBox(parent.getShell(), SWT.ICON_ERROR | SWT.OK);
			    	dialog.setText("Erreur Supression");
			    	dialog.setMessage("Une erreur est survenue lors de la supression de l'employé. "+'\n'+e.getMessage());
			    	dialog.open();
					
				}
				
			}
		});
		selection.pack();
	}

	//Modification d'un employe --------------------------------------------------
	/***
	 * Regroupe les fonctions a appeler pour faire une modification
	 * La fonction va dispose pour vue et selection (les deux composantes de droite) 
	 * et va appeler les fonctions titreModification et formulaireModification
	 */
	public void vueEmployeModifier() {
		vue.dispose();
		selection.dispose();
		
		titreModification();		
		formulaireModification();
		
		vueEmploye.pack();
		vueEmploye.getParent().pack();
		
	}
	
	/***
	 * va modifier la partie selection (partie superieure droite) en y mettant un titre pour la modification
	 */
	public void titreModification() {
		selection = new Composite(vueEmploye, SWT.CENTER);
		
		FillLayout fillLayout = new FillLayout();
		fillLayout.type = SWT.VERTICAL;
		fillLayout.marginWidth = 402;
		selection.setLayout(fillLayout);

		//juste pour creer un espace 
		Label l1 = new Label(selection, SWT.NONE);
		l1.setText("");
		l1.setBackground(Couleur.bleuFonce);

		selection.setBackground(Couleur.bleuFonce);
		Label HeadLabel =new Label(selection,SWT.TITLE);
		HeadLabel.setText("Modification d'un Employe");
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
	 * Va modifier la partie Vue (partie inferieure droite) et y ajoutant le formulaire de modification d'un employe
	 * @param item : caractéristiques de l'employe a modifier
	 */
	public void formulaireModification() {
		if (selectedEmploye == null) {
			throw new Error("selectedEmploye est vide");
		}
		
		//creation de la vue
		vue = new Composite(vueEmploye, SWT.NONE);
		FillLayout fillLayoutH = new FillLayout();
		fillLayoutH.type = SWT.HORIZONTAL;
		vue.setLayout(fillLayoutH);
		
		//creation de 3 colonnes afin de repartir les champs du formulaire en trois 
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
		
		//utiliser pour tous les composites des attributs du formulaire
		FillLayout fillLayoutH5 = new FillLayout();
		fillLayoutH5.marginHeight = 30;
		fillLayoutH5.spacing = 5;
		fillLayoutH5.type = SWT.HORIZONTAL;

		//Titre
		Composite compositeTitre = new Composite(colonne1, SWT.NONE);
		compositeTitre.setBackground(Couleur.bleuClair);
		compositeTitre.setLayout(fillLayoutH5);
		
		Label labelTitre = new Label(compositeTitre, SWT.NONE);
		labelTitre.setBackground(Couleur.bleuClair);
		labelTitre.setText("Titre : ");
		
		Combo titre = new Combo(compositeTitre, SWT.BORDER);
		titre.setText(selectedEmploye.getTitre());
		titre.add("M");
		titre.add("Mme");
		
		//Nom
		Composite compositeNom = new Composite(colonne1, SWT.NONE);
		compositeNom.setBackground(Couleur.bleuClair);
		compositeNom.setLayout(fillLayoutH5);

		Label labelNom = new Label(compositeNom, SWT.NONE);
		labelNom.setBackground(Couleur.bleuClair);
		labelNom.setText("Nom* : ");

		final Text textNom = new Text(compositeNom, SWT.BORDER);
		textNom.setText(selectedEmploye.getNom());
		
		//Prenom
		Composite compositePrenom = new Composite(colonne1, SWT.NONE);
		compositePrenom.setBackground(Couleur.bleuClair);
		compositePrenom.setLayout(fillLayoutH5);

		Label labelPrenom = new Label(compositePrenom, SWT.NONE);
		labelPrenom.setBackground(Couleur.bleuClair);
		labelPrenom.setText("Prenom* : ");

		final Text textPrenom = new Text(compositePrenom, SWT.BORDER);
		textPrenom.setText(selectedEmploye.getPrenom());
		
		//Mail
		Composite compositeMail = new Composite(colonne1, SWT.NONE);
		compositeMail.setBackground(Couleur.bleuClair);
		compositeMail.setLayout(fillLayoutH5);

		Label labelMail = new Label(compositeMail, SWT.NONE);
		labelMail.setBackground(Couleur.bleuClair);
		labelMail.setText("Email : ");

		final Text textMail = new Text(compositeMail, SWT.BORDER);
		textMail.setText(selectedEmploye.getMail());
		
		//Telephone
		Composite compositeTelephone = new Composite(colonne1, SWT.NONE);
		compositeTelephone.setBackground(Couleur.bleuClair);
		compositeTelephone.setLayout(fillLayoutH5);

		Label labelTelephone = new Label(compositeTelephone, SWT.NONE);
		labelTelephone.setBackground(Couleur.bleuClair);
		labelTelephone.setText("Telephone : ");

		final Text textTelephone = new Text(compositeTelephone, SWT.BORDER);
		textTelephone.setText(selectedEmploye.getTelephone());
	
		//numeroMatricule
		Composite compositeNumeroMatricule = new Composite(colonne2, SWT.NONE);
		compositeNumeroMatricule.setBackground(Couleur.bleuClair);
		compositeNumeroMatricule.setLayout(fillLayoutH5);

		Label labelNumeroMatricule = new Label(compositeNumeroMatricule, SWT.NONE);
		labelNumeroMatricule.setBackground(Couleur.bleuClair);
		labelNumeroMatricule.setText("Numero de Matricule* : ");

		final Text textNumeroMatricule = new Text(compositeNumeroMatricule, SWT.BORDER);
		textNumeroMatricule.setText(selectedEmploye.getNumeroMatricule().toString());
		
		//Pointure
		Composite compositePointure = new Composite(colonne2, SWT.NONE);
		compositePointure.setBackground(Couleur.bleuClair);
		compositePointure.setLayout(fillLayoutH5);

		Label labelPointure = new Label(compositePointure, SWT.NONE);
		labelPointure.setBackground(Couleur.bleuClair);
		labelPointure.setText("Pointure : ");

		final Text textPointure = new Text(compositePointure, SWT.BORDER);
		textPointure.setText(selectedEmploye.getPointure());
		
		//Taille
		Composite compositeTaille = new Composite(colonne2, SWT.NONE);
		compositeTaille.setBackground(Couleur.bleuClair);
		compositeTaille.setLayout(fillLayoutH5);

		Label labelTaille = new Label(compositeTaille, SWT.NONE);
		labelTaille.setBackground(Couleur.bleuClair);
		labelTaille.setText("Taille : ");

		final Text textTaille = new Text(compositeTaille, SWT.BORDER);
		textTaille.setText(selectedEmploye.getTaille());
		
		//DateArrivee
		Composite compositeDateArrivee = new Composite(colonne2, SWT.NONE);
		compositeDateArrivee.setBackground(Couleur.bleuClair);
		compositeDateArrivee.setLayout(fillLayoutH5);

		Label labelDateArrivee = new Label(compositeDateArrivee, SWT.NONE);
		labelDateArrivee.setBackground(Couleur.bleuClair);
		labelDateArrivee.setText("Date d'arrivée : ");

		final Text textDateArrivee = new Text(compositeDateArrivee, SWT.BORDER);
		if (selectedEmploye.getDateArrivee() != null) {
			textDateArrivee.setText(selectedEmploye.getDateArrivee());
		}
		
		// on ne prends pas item.get(9) car c'est l'anciennete 
		
		//NombreHeures
		Composite compositeNombreHeures = new Composite(colonne2, SWT.NONE);
		compositeNombreHeures.setBackground(Couleur.bleuClair);
		compositeNombreHeures.setLayout(fillLayoutH5);

		Label labelNombreHeures = new Label(compositeNombreHeures, SWT.NONE);
		labelNombreHeures.setBackground(Couleur.bleuClair);
		labelNombreHeures.setText("Nombre d'heures : ");

		final Text textNombreHeures = new Text(compositeNombreHeures, SWT.BORDER);
		textNombreHeures.setText(selectedEmploye.getNombreHeures().toString());
		
		//RemboursementTransport
		Composite compositeRemboursementTransport = new Composite(colonne3, SWT.NONE);
		compositeRemboursementTransport.setBackground(Couleur.bleuClair);
		compositeRemboursementTransport.setLayout(fillLayoutH5);

		Label labelRemboursementTransport = new Label(compositeRemboursementTransport, SWT.NONE);
		labelRemboursementTransport.setBackground(Couleur.bleuClair);
		labelRemboursementTransport.setText("Remboursement transport : ");

		final Text textRemboursementTransport = new Text(compositeRemboursementTransport, SWT.BORDER);
		textRemboursementTransport.setText(selectedEmploye.getRemboursementTransport().toString());
		
		//RemboursementTelephone
		Composite compositeRemboursementTelephone = new Composite(colonne3, SWT.NONE);
		compositeRemboursementTelephone.setBackground(Couleur.bleuClair);
		compositeRemboursementTelephone.setLayout(fillLayoutH5);

		Label labelRemboursementTelephone = new Label(compositeRemboursementTelephone, SWT.NONE);
		labelRemboursementTelephone.setBackground(Couleur.bleuClair);
		labelRemboursementTelephone.setText("Remboursement telephone : ");

		final Text textRemboursementTelephone = new Text(compositeRemboursementTelephone, SWT.BORDER);
		textRemboursementTelephone.setText(selectedEmploye.getRemboursementTelephone().toString());
		
		//Salaire
		Composite compositeSalaire = new Composite(colonne3, SWT.NONE);
		compositeSalaire.setBackground(Couleur.bleuClair);
		compositeSalaire.setLayout(fillLayoutH5);

		Label labelSalaire = new Label(compositeSalaire, SWT.NONE);
		labelSalaire.setBackground(Couleur.bleuClair);
		labelSalaire.setText("Salaire : ");

		final Text textSalaire = new Text(compositeSalaire, SWT.BORDER);
		textSalaire.setText(selectedEmploye.getSalaire().toString());

		//utiliser afin de combler un vide et de ne pas tout decaler
		//ne s'affiche pas 
		Composite test = new Composite(colonne3, SWT.NONE);
		test.setBackground(Couleur.bleuClair);

		//Boutons
		Composite compositeBoutons = new Composite(colonne3, SWT.CENTER);
		compositeBoutons.setBackground(Couleur.bleuClair);
		compositeBoutons.setLayout(fillLayoutH5);

		Button buttonAnnulation = new Button(compositeBoutons, SWT.BACKGROUND);
		buttonAnnulation.setText("Annuler");
		buttonAnnulation.addSelectionListener(new SelectionAdapter() {

			@Override public void widgetSelected(SelectionEvent arg0) {
				newVueEmploye(parent);
			}
		});

		Button buttonValidation = new Button(compositeBoutons, SWT.BACKGROUND);
		buttonValidation.setText("Valider");
		buttonValidation.addSelectionListener(new SelectionAdapter() {

			@Override public void widgetSelected(SelectionEvent arg0) {

				try { 
					int id = selectedEmploye.getEmployeId();
					selectedEmploye = new Employe(id, titre.getText(), textNom.getText(), textPrenom.getText(), textMail.getText(), textTelephone.getText(), Integer.parseInt(textNumeroMatricule.getText()), 
							textPointure.getText(), textTaille.getText(), textDateArrivee.getText(), Double.parseDouble(textNombreHeures.getText()),
						   Double.parseDouble(textRemboursementTransport.getText()), Double.parseDouble(textRemboursementTelephone.getText()), 
						   Double.parseDouble(textSalaire.getText()), "Publié");
				   validerModification();
			    } catch (Throwable e) { 
			    	e.printStackTrace(); 
			    	System.out.println("erreur dans la modif");
			    	MessageBox dialog = new MessageBox(parent.getShell(), SWT.ICON_ERROR | SWT.OK);
			    	dialog.setText("Erreur Modification");
			    	dialog.setMessage("Une erreur est survenue lors de la modification de l'employé. "+'\n'+e.getMessage());
			    	dialog.open();
			    } 
			  
			  
		  }
		});
	}
	
	/***
	 * modifie la base de données
	 */
	public void validerModification() {
		if (selectedEmploye == null) {
			throw new Error("selectedEmploye est vide");
		}
		
	    //on insert dans la base de données
	    try { 
	    	selectedEmploye.updateDatabase(); 
	    	System.out.println("on a modifie l employe !!");
	    	MessageBox dialog = new MessageBox(parent.getShell(), SWT.ICON_INFORMATION | SWT.OK);
	    	dialog.setText("Modification réussie");
	    	dialog.setMessage("L'employé a bien été modifié dans la base de données.");
	    	dialog.open();
	    	selectedEmploye = null;
	    } catch (SQLException e) { 
	    	e.printStackTrace(); 
	    	System.out.println("erreur dans la modif");
	    	MessageBox dialog = new MessageBox(parent.getShell(), SWT.ICON_ERROR | SWT.OK);
	    	dialog.setText("Erreur Modification");
	    	dialog.setMessage("Une erreur est survenue lors de la modification de l'employé. "+e.getMessage());
	    	dialog.open();
	    } 
	
	 	newVueEmploye(parent);  
	}
	
	//Création d'un employe --------------------------------------------------
	/***
	 * va appeler les fonctions qui modifie la partie vue et la partie selection afin d'afin le formulaire de création 
	 * appelle titreCreation et formulaireCreation
	 */	
	public void vueEmployeCreer() {
		vue.dispose();
		selection.dispose();
		
		titreCreation();		
		formulaireCreation();
		
		vueEmploye.pack();
		vueEmploye.getParent().pack();
		
	}
	
	/***
	 * modifie la partie selection (partie superieur droite) en ajoutant un titre de creation
	 */
	public void titreCreation() {
		selection = new Composite(vueEmploye, SWT.CENTER);
		
		FillLayout fillLayout = new FillLayout();
		fillLayout.type = SWT.VERTICAL;
		fillLayout.marginWidth = 416;
		selection.setLayout(fillLayout);

		//juste pour creer un espace 
		Label l1 = new Label(selection, SWT.NONE);
		l1.setText("");
		l1.setBackground(Couleur.bleuFonce);

		selection.setBackground(Couleur.bleuFonce);
		Label HeadLabel =new Label(selection,SWT.TITLE);
		HeadLabel.setText("Creation d'un Employe");
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
	 * cree le formulaire de creation d'un employe
	 */
	public void formulaireCreation() {
		vue = new Composite(vueEmploye, SWT.NONE);
		FillLayout fillLayoutH = new FillLayout();
		fillLayoutH.type = SWT.HORIZONTAL;
		vue.setLayout(fillLayoutH);
		
		//on cree trois colonne pour repartir les champs 
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
		
		//utilisé pour tous les composites des arguments
		FillLayout fillLayoutH5 = new FillLayout();
		fillLayoutH5.marginHeight = 30;
		fillLayoutH5.spacing = 5;
		fillLayoutH5.type = SWT.HORIZONTAL;

		//Titre
		Composite compositeTitre = new Composite(colonne1, SWT.NONE);
		compositeTitre.setBackground(Couleur.bleuClair);
		compositeTitre.setLayout(fillLayoutH5);
		
		Label labelTitre = new Label(compositeTitre, SWT.NONE);
		labelTitre.setBackground(Couleur.bleuClair);
		labelTitre.setText("Titre : ");
		
		Combo titre = new Combo(compositeTitre, SWT.BORDER);
		titre.setText("Mme");
		titre.add("M");
		
		//Nom
		Composite compositeNom = new Composite(colonne1, SWT.NONE);
		compositeNom.setBackground(Couleur.bleuClair);
		compositeNom.setLayout(fillLayoutH5);

		Label labelNom = new Label(compositeNom, SWT.NONE);
		labelNom.setBackground(Couleur.bleuClair);
		labelNom.setText("Nom* : ");

		final Text textNom = new Text(compositeNom, SWT.BORDER);
		textNom.setText("");
		
		//Prenom
		Composite compositePrenom = new Composite(colonne1, SWT.NONE);
		compositePrenom.setBackground(Couleur.bleuClair);
		compositePrenom.setLayout(fillLayoutH5);

		Label labelPrenom = new Label(compositePrenom, SWT.NONE);
		labelPrenom.setBackground(Couleur.bleuClair);
		labelPrenom.setText("Prenom* : ");

		final Text textPrenom = new Text(compositePrenom, SWT.BORDER);
		textPrenom.setText("");
		
		//Mail
		Composite compositeMail = new Composite(colonne1, SWT.NONE);
		compositeMail.setBackground(Couleur.bleuClair);
		compositeMail.setLayout(fillLayoutH5);

		Label labelMail = new Label(compositeMail, SWT.NONE);
		labelMail.setBackground(Couleur.bleuClair);
		labelMail.setText("Email : ");

		final Text textMail = new Text(compositeMail, SWT.BORDER);
		textMail.setText("");
		
		//Telephone
		Composite compositeTelephone = new Composite(colonne1, SWT.NONE);
		compositeTelephone.setBackground(Couleur.bleuClair);
		compositeTelephone.setLayout(fillLayoutH5);

		Label labelTelephone = new Label(compositeTelephone, SWT.NONE);
		labelTelephone.setBackground(Couleur.bleuClair);
		labelTelephone.setText("Telephone : ");

		final Text textTelephone = new Text(compositeTelephone, SWT.BORDER);
		textTelephone.setText("");
	
		//numeroMatricule
		Composite compositeNumeroMatricule = new Composite(colonne2, SWT.NONE);
		compositeNumeroMatricule.setBackground(Couleur.bleuClair);
		compositeNumeroMatricule.setLayout(fillLayoutH5);

		Label labelNumeroMatricule = new Label(compositeNumeroMatricule, SWT.NONE);
		labelNumeroMatricule.setBackground(Couleur.bleuClair);
		labelNumeroMatricule.setText("Numero de Matricule* : ");

		final Text textNumeroMatricule = new Text(compositeNumeroMatricule, SWT.BORDER);
		textNumeroMatricule.setText("");
		
		//Pointure
		Composite compositePointure = new Composite(colonne2, SWT.NONE);
		compositePointure.setBackground(Couleur.bleuClair);
		compositePointure.setLayout(fillLayoutH5);

		Label labelPointure = new Label(compositePointure, SWT.NONE);
		labelPointure.setBackground(Couleur.bleuClair);
		labelPointure.setText("Pointure : ");

		final Text textPointure = new Text(compositePointure, SWT.BORDER);
		textPointure.setText("");
		
		//Taille
		Composite compositeTaille = new Composite(colonne2, SWT.NONE);
		compositeTaille.setBackground(Couleur.bleuClair);
		compositeTaille.setLayout(fillLayoutH5);

		Label labelTaille = new Label(compositeTaille, SWT.NONE);
		labelTaille.setBackground(Couleur.bleuClair);
		labelTaille.setText("Taille : ");

		final Text textTaille = new Text(compositeTaille, SWT.BORDER);
		textTaille.setText("");
		
		//DateArrivee
		Composite compositeDateArrivee = new Composite(colonne2, SWT.NONE);
		compositeDateArrivee.setBackground(Couleur.bleuClair);
		compositeDateArrivee.setLayout(fillLayoutH5);

		Label labelDateArrivee = new Label(compositeDateArrivee, SWT.NONE);
		labelDateArrivee.setBackground(Couleur.bleuClair);
		labelDateArrivee.setText("Date d'arrivée : ");

		final Text textDateArrivee = new Text(compositeDateArrivee, SWT.BORDER);
		textDateArrivee.setText("");
		
		//NombreHeures
		Composite compositeNombreHeures = new Composite(colonne2, SWT.NONE);
		compositeNombreHeures.setBackground(Couleur.bleuClair);
		compositeNombreHeures.setLayout(fillLayoutH5);

		Label labelNombreHeures = new Label(compositeNombreHeures, SWT.NONE);
		labelNombreHeures.setBackground(Couleur.bleuClair);
		labelNombreHeures.setText("Nombre d'heures : ");

		final Text textNombreHeures = new Text(compositeNombreHeures, SWT.BORDER);
		textNombreHeures.setText("");
		
		//RemboursementTransport
		Composite compositeRemboursementTransport = new Composite(colonne3, SWT.NONE);
		compositeRemboursementTransport.setBackground(Couleur.bleuClair);
		compositeRemboursementTransport.setLayout(fillLayoutH5);

		Label labelRemboursementTransport = new Label(compositeRemboursementTransport, SWT.NONE);
		labelRemboursementTransport.setBackground(Couleur.bleuClair);
		labelRemboursementTransport.setText("Remboursement transport : ");

		final Text textRemboursementTransport = new Text(compositeRemboursementTransport, SWT.BORDER);
		textRemboursementTransport.setText("");
		
		//RemboursementTelephone
		Composite compositeRemboursementTelephone = new Composite(colonne3, SWT.NONE);
		compositeRemboursementTelephone.setBackground(Couleur.bleuClair);
		compositeRemboursementTelephone.setLayout(fillLayoutH5);

		Label labelRemboursementTelephone = new Label(compositeRemboursementTelephone, SWT.NONE);
		labelRemboursementTelephone.setBackground(Couleur.bleuClair);
		labelRemboursementTelephone.setText("Remboursement telephone : ");

		final Text textRemboursementTelephone = new Text(compositeRemboursementTelephone, SWT.BORDER);
		textRemboursementTelephone.setText("");
		
		//Salaire
		Composite compositeSalaire = new Composite(colonne3, SWT.NONE);
		compositeSalaire.setBackground(Couleur.bleuClair);
		compositeSalaire.setLayout(fillLayoutH5);

		Label labelSalaire = new Label(compositeSalaire, SWT.NONE);
		labelSalaire.setBackground(Couleur.bleuClair);
		labelSalaire.setText("Salaire : ");

		final Text textSalaire = new Text(compositeSalaire, SWT.BORDER);
		textSalaire.setText("");

		//ne s'affiche pas 
		//utiliser pour avoir le meme nombre de composite sur chaque colonne et qu'ils prennent donc la meme taille
		Composite test = new Composite(colonne3, SWT.NONE);
		test.setBackground(Couleur.bleuClair);

		//Boutons
		Composite compositeBoutons = new Composite(colonne3, SWT.CENTER);
		compositeBoutons.setBackground(Couleur.bleuClair);
		compositeBoutons.setLayout(fillLayoutH5);

		Button buttonAnnulation = new Button(compositeBoutons, SWT.BACKGROUND);
		buttonAnnulation.setText("Annuler");
		buttonAnnulation.addSelectionListener(new SelectionAdapter() {

			@Override public void widgetSelected(SelectionEvent arg0) {
				newVueEmploye(parent);
			}
		});

		Button buttonValidation = new Button(compositeBoutons, SWT.BACKGROUND);
		buttonValidation.setText("Valider");
		buttonValidation.addSelectionListener(new SelectionAdapter() {

			@Override public void widgetSelected(SelectionEvent arg0) {

			  try { 
				  System.out.println("avant de lancer fonction : "+textDateArrivee.getText());
				  validerCreation(titre.getText(), textNom.getText(), textPrenom.getText(), textNumeroMatricule.getText(), textMail.getText(), textTelephone.getText(), textPointure.getText(), textTaille.getText(), textDateArrivee.getText(), textNombreHeures.getText(), textRemboursementTransport.getText(), textRemboursementTelephone.getText(), textSalaire.getText());   
			  } catch (Throwable e) { 
			    	e.printStackTrace(); 
			    	System.out.println("erreur dans la creation");
			    	MessageBox dialog = new MessageBox(parent.getShell(), SWT.ICON_ERROR | SWT.OK);
			    	dialog.setText("Erreur Création");
			    	dialog.setMessage("Une erreur est survenue lors de la création de l'employé. "+'\n'+e.getMessage());
			    	dialog.open();
			    } 
		  }
		});
	}

	/***
	 * cree un employe a partir du formulaire et l'inserer dans la base de donnees
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
	public void validerCreation(String titre,String textNom,String textPrenom,String textNumeroMatricule,String textMail,String textTelephone,String textPointure,String textTaille,String textDateArrivee,String textNombreHeures,String textRemboursementTransport,String textRemboursementTelephone,String textSalaire) {
		
		if (textNom.isEmpty() || textPrenom.isEmpty() || textNumeroMatricule.isEmpty()) {
			throw new Error("Merci d'indiquer au moins le nom, le prénom et le numéro de matricule de l'employé.");
		}
		
		//champs obligatoires
		Employe employe = new Employe(titre,textNom,textPrenom,Integer.parseInt(textNumeroMatricule));
		employe.setStatus("Publié");
		  
		//champs optionels
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

		//date
	    if (!(textDateArrivee.isEmpty())){
			employe.setDateArrivee(textDateArrivee);
	    }

		//nombre heures
	    if (textNombreHeures != "" && textNombreHeures.contains(".")) {
	    	employe.setNombreHeures(Double.parseDouble(textNombreHeures));
	    }
	    else if (textNombreHeures != "" && textNombreHeures.matches(".*\\d.*")) {
	    	employe.setNombreHeures(Double.parseDouble(textNombreHeures+".0"));
	    }

	    //remboursement transport
	    if (textRemboursementTransport != "" && textRemboursementTransport.contains(".")) {
	    	employe.setRemboursementTransport(Double.parseDouble(textRemboursementTransport));
	    }
	    else if (textRemboursementTransport != "" && textRemboursementTransport.matches(".*\\d.*")) {
	    	employe.setRemboursementTransport(Double.parseDouble(textRemboursementTransport+".0"));
	    }

	    //remboursement telephone
	    if (textRemboursementTelephone != "" && textRemboursementTelephone.contains(".")) {
	    	employe.setRemboursementTelephone(Double.parseDouble(textRemboursementTelephone));
	    }
	    else if (textRemboursementTelephone != "" && textRemboursementTelephone.matches(".*\\d.*")) {
	    	employe.setRemboursementTelephone(Double.parseDouble(textRemboursementTelephone+".0"));
	    }
	  
	    //salaire
	    if (textSalaire != "" && textSalaire.contains(".")) {
	    	employe.setSalaire(Double.parseDouble(textSalaire));
	    }
	    else if (textSalaire != "" && textSalaire.matches(".*\\d.*")) {
	    	employe.setSalaire(Double.parseDouble(textSalaire+".0"));
	    }
	  
	    System.out.println("avant d'inserer employe : "+employe.getDateArrivee());
	    
	    //on insert dans la base de données
	    try { 
	    	employe.insertDatabase(); 
	    	System.out.println("on a insere l employe !!");
	    	MessageBox dialog = new MessageBox(parent.getShell(), SWT.ICON_INFORMATION | SWT.OK);
	    	dialog.setText("Création réussie");
	    	dialog.setMessage("L'employé a bien été ajouté à la base de données.");
	    	dialog.open();
		    newVueEmploye(parent);
			vue.pack(); selection.pack(); vueEmploye.pack();
	    } catch (SQLException e) { 
	    	System.out.println("erreur dans la création");
	    	MessageBox dialog = new MessageBox(parent.getShell(), SWT.ICON_ERROR | SWT.OK);
	    	dialog.setText("Erreur Création");
	    	dialog.setMessage("Une erreur est survenue lors de la création de l'employé. "+'\n'+e.getMessage());
	    	dialog.open();
	    } 
	}
	
    //--------------------------------------------------------------------------
	/***
	 * affiche le tableau avec tous les employes dans la base de donnees dont le status est publie
	 */
	public void vueEmployeAfficher(Composite composite) {
		
		RowLayout rowLayoutV = new RowLayout();
	    rowLayoutV.type = SWT.VERTICAL;
		
	    vue = new Composite(composite, SWT.NONE);
		vue.setLayout(rowLayoutV);
		vue.setBackground(Couleur.gris);
		
		//creation de la table
	    final Table table = new Table (vue, SWT.BORDER | SWT.MULTI| SWT.V_SCROLL | SWT.FULL_SELECTION);
	    table.setLayoutData(new RowData(1047, 450));
	    table.setLinesVisible (true);
		table.setHeaderVisible (true);
		
		//on met les noms des colonnes
		String[] titles = {"Titre","Nom","Prenom","Email","Téléphone","N° de matricule","Pointure","Taille","Date d'arrivée","Ancienneté","Nb d'heures","Remb. Transport","Remb. Telephone","Salaire"};
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
			for (Employe e : Employe.getAllEmploye()) {
				//on verifie le status
				if (e.getStatus().contentEquals("Publié")) {
					TableItem item = new TableItem (table, SWT.NONE);
					item.setText(0,e.getTitre());
					item.setText(1,e.getNom());
					item.setText(2,e.getPrenom());
					item.setText(3,e.getMail());
					item.setText(4,e.getTelephone());
					item.setText(5,Integer.toString(e.getNumeroMatricule()));
					item.setText(6,e.getPointure());
					item.setText(7,e.getTaille());
					
					//date et anciennete
					if (e.getDateArrivee() != null) {
						item.setText(8,e.getDateArrivee());
	
						String date = e.getDateArrivee();
						int j1 = Integer.parseInt(date.substring(0,2));
						int m1 = Integer.parseInt(date.substring(3,5));
						int a1 = Integer.parseInt(date.substring(6,10));
						LocalDate currentdate = LocalDate.now();
					    int j2 = currentdate.getDayOfMonth();
					    int m2 = currentdate.getMonthValue();
					    int a2 = currentdate.getYear();
				
					    if (a2-a1 < 0) {item.setText(9,"euuuh ... ");}
					    else if (a2-a1 == 0) {item.setText(9,"moins d'un an");}
						else {
							if ((m1>m2) || (m1==m2 && j1>j2)) {
								if (a2-a1-1 == 0) {item.setText(9,"");}
								else if (a2-a1-1 == 1) {item.setText(9,Integer.toString(a2-a1-1)+" an");}
								else {item.setText(9,Integer.toString(a2-a1-1)+" ans");}
							}
							else {
								if (a2-a1 == 1) {item.setText(9,Integer.toString(a2-a1)+" an");}
								else {item.setText(9,Integer.toString(a2-a1)+" ans");}
							}
						}
					}
					else {
						item.setText(8,"");
						item.setText(9,"");
					}
					
					item.setText(10,Double.toString(e.getNombreHeures()));
					item.setText(11,Double.toString(e.getRemboursementTransport()));
					item.setText(12,Double.toString(e.getRemboursementTelephone()));
					item.setText(13,Double.toString(e.getSalaire()));
					item.setText(14,Integer.toString(e.getEmployeId()));
				}
			}
		} catch (SQLException e) {
	    	System.out.println("erreur dans la table des employes");
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
						selectedEmploye = Employe.getEmployeById(Integer.parseInt(table.getSelection()[0].getText(14)));
					} catch (NumberFormatException | SQLException e1) {
						System.out.println("erreur pour recuperer l'employe selectionne");
				    	MessageBox dialog = new MessageBox(parent.getShell(), SWT.ICON_ERROR | SWT.OK);
				    	dialog.setText("Erreur");
				    	dialog.setMessage("Une erreur est survenue. "+'\n'+e1.getMessage());
				    	dialog.open();
					}
					compositeSelectionModifier(vueEmploye);
					
					//on ajoute un menu lorsque l'on fait clique droit sur une ligne
					doMenu(table);
				}
				else { // si plus rien n'est selectionner on passe selectedEmploye a null et on enleve le menu du clic droit et on enleve les boutons pour modifier et supprimer
					System.out.println("-1");
					
					selectedEmploye = null;
					
					menu.dispose();
					menu = new Menu (composite.getShell(), SWT.POP_UP);
					table.setMenu (menu);
					
					selection.dispose();
					compositeSelectionCreer(vueEmploye);
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
					vueEmployeModifier();
				}
			}
		});


		//pour supprimer
		MenuItem delete = new MenuItem (menu, SWT.PUSH);
		delete.setText ("Supprimer l'element");
		delete.addSelectionListener(new SelectionAdapter() {	
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				try {
					Employe e = Employe.getEmployeById(selectedEmploye.getEmployeId());
					MessageBox dialog = new MessageBox(parent.getShell(), SWT.ICON_QUESTION | SWT.YES | SWT.NO);
			    	dialog.setText("Suppression Employe");
			    	dialog.setMessage("Voulez-vous supprimer l'employé "+e.getNom()+" "+e.getPrenom()+" ?");
			    	int buttonID = dialog.open();
			        switch(buttonID) {
			          case SWT.YES:
							e.setStatus("Archivé");
							e.updateDatabase();
							newVueEmploye(parent);
							selectedEmploye = null;
			        }

				} catch (NumberFormatException | SQLException e) {
					System.out.println("erreur pour supprimer l'employe");
			    	MessageBox dialog = new MessageBox(parent.getShell(), SWT.ICON_ERROR | SWT.OK);
			    	dialog.setText("Erreur");
			    	dialog.setMessage("Une erreur est survenue. "+'\n'+e.getMessage());
			    	dialog.open();
				}
			}
		});
		
	}
	
	public Composite getComposite() {
		return this.vueEmploye;
		
	}
	
}
