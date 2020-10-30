package GUI;

import java.sql.SQLException;
import java.time.LocalDate;

import org.eclipse.swt.*;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

import classes.Employe;

public class VueEmploye {

	private Display display;
	private Composite vueEmploye;
	private Composite selection;
	private Composite vue;

	public VueEmploye (Composite composite,Display display) {
		vueEmploye=new Composite(composite,SWT.NONE);
		this.display=display;
		Couleur.setDisplay(display); // pour utiliser les couleurs du fichier couleur
		
		RowLayout rowLayout = new RowLayout();
		rowLayout.type = SWT.VERTICAL;
		vueEmploye.setLayout(rowLayout);
		
		compositeSelection(vueEmploye);
		vueEmployeAfficher(vueEmploye);
	}
	
	public void compositeSelection(Composite composite) {
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
	

	public void vueEmployeCreer() {
		vue.dispose();
		selection.dispose();
		
		titreCreation();		
		formulaireCreation();
		
		vueEmploye.pack();
		vueEmploye.getParent().pack();
		
	}
	
	public void titreCreation() {
		selection = new Composite(vueEmploye, SWT.CENTER);
		
		FillLayout fillLayout = new FillLayout();
		fillLayout.type = SWT.HORIZONTAL;
		fillLayout.marginWidth = 416;
		selection.setLayout(fillLayout);
		
		selection.setBackground(Couleur.bleuFonce);
		Label HeadLabel =new Label(selection,SWT.TITLE);
		HeadLabel.setText("Creation d'un Employe");
		Font fontTitle = new Font(HeadLabel.getDisplay(), "Arial", 12, SWT.BOLD);
		HeadLabel.setForeground(Couleur.bleuClair);
		HeadLabel.setFont(fontTitle);
		HeadLabel.setBackground(Couleur.bleuFonce);
		
		selection.pack();
	}

	public void formulaireCreation() {
		vue = new Composite(vueEmploye, SWT.NONE);
		FillLayout fillLayoutH = new FillLayout();
		fillLayoutH.type = SWT.HORIZONTAL;
		vue.setLayout(fillLayoutH);
		
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
		titre.setText("Selectionner ...");
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

		Composite test = new Composite(colonne3, SWT.NONE);
		test.setBackground(Couleur.bleuClair);
		
		Composite compositeValidation = new Composite(colonne3, SWT.CENTER);
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

			  validerCreation(titre, textNom, textPrenom, textNumeroMatricule, textMail, textTelephone, textPointure, textTaille, textDateArrivee, textNombreHeures, textRemboursementTransport, textRemboursementTelephone, textSalaire);

		  }
		});
	}
		
	public void validerCreation(Combo titre,Text textNom,Text textPrenom,Text textNumeroMatricule,Text textMail,Text textTelephone,Text textPointure,Text textTaille,Text textDateArrivee,Text textNombreHeures,Text textRemboursementTransport,Text textRemboursementTelephone,Text textSalaire) {
		
		//champs obligatoires
		String t;
		if (titre.getText() == "M" || titre.getText() == "Mme") {
			t = titre.getText();
		}  
		else {
			t = "M";
		}
		String n = textNom.getText();
		String p = textPrenom.getText();
		Integer nM = Integer.parseInt(textNumeroMatricule.getText());
		Employe employe = new Employe(t,n,p,nM);
		employe.setStatus("Publié");
		  
		//champs optionels
		if (textMail.getText() != "") {
			employe.setMail(textMail.getText());
		}
		if (textTelephone.getText() != "") {
			employe.setTelephone(textTelephone.getText());
		}
		if (textPointure.getText() != "") {
			employe.setPointure(textPointure.getText());
		}
		if (textTaille.getText() != "") {
			employe.setTaille(textTaille.getText());
		}

		//date
	    String d = textDateArrivee.getText();
	    if (d != "") {
	    	//tester la date ?? 
			employe.setDateArrivee(textDateArrivee.getText());
	    }

		//nombre heures
	    if (textNombreHeures.getText() != "" && textNombreHeures.getText().contains(".")) {
	    	employe.setNombreHeures(Double.parseDouble(textNombreHeures.getText()));
	    }
	    else if (textNombreHeures.getText() != "" && textNombreHeures.getText().matches(".*\\d.*")) {
	    	employe.setNombreHeures(Double.parseDouble(textNombreHeures.getText()+".0"));
	    }

	    //remboursement transport
	    if (textRemboursementTransport.getText() != "" && textRemboursementTransport.getText().contains(".")) {
	    	employe.setRemboursementTransport(Double.parseDouble(textRemboursementTransport.getText()));
	    }
	    else if (textRemboursementTransport.getText() != "" && textRemboursementTransport.getText().matches(".*\\d.*")) {
	    	employe.setRemboursementTransport(Double.parseDouble(textRemboursementTransport.getText()+".0"));
	    }

	    //remboursement telephone
	    if (textRemboursementTelephone.getText() != "" && textRemboursementTelephone.getText().contains(".")) {
	    	employe.setRemboursementTelephone(Double.parseDouble(textRemboursementTelephone.getText()));
	    }
	    else if (textRemboursementTelephone.getText() != "" && textRemboursementTelephone.getText().matches(".*\\d.*")) {
	    	employe.setRemboursementTelephone(Double.parseDouble(textRemboursementTelephone.getText()+".0"));
	    }
	  
	    //salaire
	    if (textSalaire.getText() != "" && textSalaire.getText().contains(".")) {
	    	employe.setSalaire(Double.parseDouble(textSalaire.getText()));
	    }
	    else if (textSalaire.getText() != "" && textSalaire.getText().matches(".*\\d.*")) {
	    	employe.setSalaire(Double.parseDouble(textSalaire.getText()+".0"));
	    }
	  
	    //on insert dans la base de données
	    try { 
	    	employe.insertDatabase(); 
	    	System.out.println("on a insere l employe !!");
	    	//mettre un message pour savoir si l'insertion s'est bien passee ? 
	    } catch (SQLException e) { 
	    	e.printStackTrace(); 
	    } 
	
	 	for (Control c : selection.getChildren()) {
	 		c.dispose();
	 	}
	 	for (Control c : vue.getChildren()) {
	 		c.dispose();
	 	}
	 	
	 	compositeSelection(vueEmploye);
	 	vueEmployeAfficher(vueEmploye);
	 	vue.pack();
	 	selection.pack();
	 	vueEmploye.pack();
  
	}
	

	public void vueEmployeAfficher(Composite composite) {
		
		RowLayout rowLayoutV = new RowLayout();
	    rowLayoutV.type = SWT.VERTICAL;
		
	    vue = new Composite(composite, SWT.NONE);
		vue.setLayout(rowLayoutV);
		vue.setBackground(Couleur.gris);
		
	    final Table table = new Table (vue, SWT.BORDER | SWT.MULTI);
	    table.setLinesVisible (true);
		table.setHeaderVisible (true);

		//on met les noms des colonnes
		String[] titles = {"Titre","Nom","Prenom","Email","Téléphone","Numéro de matricule","Pointure","Taille","Date d'arrivée","Ancienneté","Nb d'heures","Remb. Transport","Remb. Telephone","Salaire"};
		for (String title : titles) {
			TableColumn column = new TableColumn (table, SWT.NONE);
			column.setText (title);
		}
		
		final TableColumn [] columns = table.getColumns ();
		try {
			for (Employe e : Employe.getAllEmploye()) {
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
			
					if (a2-a1 <= 0) {item.setText(9,"moins d'un an");}
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
				
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		for (TableColumn col : columns)
			col.pack ();
		
		Menu menu = new Menu (composite.getShell(), SWT.POP_UP);
		table.setMenu (menu);
		
		MenuItem delete = new MenuItem (menu, SWT.PUSH);
		delete.setText ("Supprimer l'element");
		//delete.addListener (SWT.Selection, event -> table.remove (table.getSelectionIndices ()));
	    
		MenuItem update = new MenuItem (menu, SWT.PUSH);
		update.setText ("Modifier l'element");
		//update.addListener (SWT.Selection, event -> table.remove (table.getSelectionIndices ()));
		
		vue.pack();
		vueEmploye.pack();
	}
	
	public Composite getComposite() {
		return this.vueEmploye;
	}
	
}
