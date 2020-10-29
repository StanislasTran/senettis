package GUI;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import org.eclipse.swt.*;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

import classes.Employe;
import classes.Produit;

public class VueEmploye {

	private static Display display;
	private Shell shell;
	private static Composite vueEmploye;
	private static Composite selection;
	private static Composite vue;

	

	public VueEmploye (Composite composite, Shell shell,Display display) {
		vueEmploye=new Composite(composite,SWT.NONE);
		this.display=display;
		Couleur.setDisplay(display); // pour utiliser les couleurs du fichier couleur
		
		RowLayout rowLayout = new RowLayout();
		rowLayout.type = SWT.VERTICAL;
		vueEmploye.setLayout(rowLayout);
		
		compositeSelection(vueEmploye);
		vueEmployeAfficher(vueEmploye, shell);
	}
	
	public static void compositeSelection(Composite composite) {
		selection = new Composite(composite, SWT.NONE);
		RowLayout rowLayout = new RowLayout();
		rowLayout.marginWidth = 20;
		selection.setLayout(rowLayout);
		selection.setBackground(Couleur.gris);
		selection.pack();
		
		Button boutonCreer = new Button(selection, SWT.CENTER);
		boutonCreer.setText("Créer");
		boutonCreer.addSelectionListener(new SelectionAdapter() {	
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				System.out.println("bouton creer employe");
				vueEmployeCreer();
				
				RowLayout rowLayout = new RowLayout();
				rowLayout.type = SWT.VERTICAL;
				rowLayout.marginWidth = 441;
				vueEmploye.setLayout(rowLayout);

			}
		});
	}
	
	
	public static void vueEmployeCreer() {
		vue.dispose();
		selection.dispose();
		
		selection = new Composite(vueEmploye, SWT.CENTER);
		
		FillLayout fillLayoutHselec = new FillLayout();
		fillLayoutHselec.type = SWT.HORIZONTAL;
		fillLayoutHselec.marginWidth = 416;
		selection.setLayout(fillLayoutHselec);
		
		selection.setBackground(Couleur.bleuFonce);
		Label HeadLabel =new Label(selection,SWT.TITLE);
		HeadLabel.setText("Creation d'un Employe");
		Font fontTitle = new Font(HeadLabel.getDisplay(), "Arial", 12, SWT.BOLD);
		HeadLabel.setForeground(Couleur.bleuClair);
		HeadLabel.setFont(fontTitle);
		HeadLabel.setBackground(Couleur.bleuFonce);
		
		vue = new Composite(vueEmploye, SWT.NONE);
		FillLayout fillLayoutH = new FillLayout();
		fillLayoutH.type = SWT.HORIZONTAL;
		vue.setLayout(fillLayoutH);
		
		
		FillLayout fillLayoutV = new FillLayout();
		fillLayoutV.type = SWT.VERTICAL;
		fillLayoutV.marginWidth = 10;
		Composite colonne1 = new Composite(vue, SWT.BORDER);
		

		//Label logo = new Label (selection, SWT.NONE);
		//Image image = new Image(display, "images\\trait.png");
		//logo.setImage(image);
		
		Composite colonne2 = new Composite(vue, SWT.BORDER);
		Composite colonne3 = new Composite(vue, SWT.BORDER);
		colonne1.setBackground(Couleur.bleuClair);
		colonne2.setBackground(Couleur.bleuClair);
		colonne3.setBackground(Couleur.bleuClair);
		colonne1.setLayout(fillLayoutV);
		colonne2.setLayout(fillLayoutV);
		colonne3.setLayout(fillLayoutV);
		
		FillLayout rowLayoutV = new FillLayout();
		rowLayoutV.marginHeight = 30;
		rowLayoutV.spacing = 5;
		rowLayoutV.type = SWT.HORIZONTAL;

		//Titre
		Composite compositeTitre = new Composite(colonne1, SWT.NONE);
		compositeTitre.setBackground(Couleur.bleuClair);
		compositeTitre.setLayout(rowLayoutV);
		
		Label labelTitre = new Label(compositeTitre, SWT.NONE);
		labelTitre.setBackground(Couleur.bleuClair);
		labelTitre.setText("Titre : ");
		labelTitre.setBounds(10, 10, 100, 25);
		
		Combo titre = new Combo(compositeTitre, SWT.BORDER);
		titre.add("M");
		titre.add("Mme");
		
		//Nom
		Composite compositeNom = new Composite(colonne1, SWT.NONE);
		compositeNom.setBackground(Couleur.bleuClair);
		compositeNom.setLayout(rowLayoutV);

		Label labelNom = new Label(compositeNom, SWT.NONE);
		labelNom.setBackground(Couleur.bleuClair);
		labelNom.setText("Nom : ");
		labelNom.setBounds(10, 10, 100, 25);

		final Text textNom = new Text(compositeNom, SWT.BORDER);
		textNom.setText("");
		//textNom.setBounds(10, 30, 100, 25);
		
		//Prenom
		Composite compositePrenom = new Composite(colonne1, SWT.NONE);
		compositePrenom.setBackground(Couleur.bleuClair);
		compositePrenom.setLayout(rowLayoutV);

		Label labelPrenom = new Label(compositePrenom, SWT.NONE);
		labelPrenom.setBackground(Couleur.bleuClair);
		labelPrenom.setText("Prenom : ");

		final Text textPrenom = new Text(compositePrenom, SWT.BORDER);
		textPrenom.setText("");
		
		
		//Mail
		Composite compositeMail = new Composite(colonne1, SWT.NONE);
		compositeMail.setBackground(Couleur.bleuClair);
		compositeMail.setLayout(rowLayoutV);

		Label labelMail = new Label(compositeMail, SWT.NONE);
		labelMail.setBackground(Couleur.bleuClair);
		labelMail.setText("Email : ");
		//labelMail.setBounds(10, 10, 100, 25);

		final Text textMail = new Text(compositeMail, SWT.BORDER);
		textMail.setText("");
		//textMail.setBounds(10, 30, 100, 25);
		
		//Telephone
		Composite compositeTelephone = new Composite(colonne1, SWT.NONE);
		compositeTelephone.setBackground(Couleur.bleuClair);
		compositeTelephone.setLayout(rowLayoutV);

		Label labelTelephone = new Label(compositeTelephone, SWT.NONE);
		labelTelephone.setBackground(Couleur.bleuClair);
		labelTelephone.setText("Telephone : ");
		//labelTelephone.setBounds(10, 10, 100, 25);

		final Text textTelephone = new Text(compositeTelephone, SWT.BORDER);
		textTelephone.setText("");

	
		//numeroMatricule
		Composite compositeNumeroMatricule = new Composite(colonne2, SWT.NONE);
		compositeNumeroMatricule.setBackground(Couleur.bleuClair);
		compositeNumeroMatricule.setLayout(rowLayoutV);

		Label labelNumeroMatricule = new Label(compositeNumeroMatricule, SWT.NONE);
		labelNumeroMatricule.setBackground(Couleur.bleuClair);
		labelNumeroMatricule.setText("Numero de Matricule : ");
		labelNumeroMatricule.setBounds(10, 10, 100, 25);

		final Text textNumeroMatricule = new Text(compositeNumeroMatricule, SWT.BORDER);
		textNumeroMatricule.setText("");
		//textMail.setBounds(10, 30, 100, 25);
		
		//Pointure
		Composite compositePointure = new Composite(colonne2, SWT.NONE);
		compositePointure.setBackground(Couleur.bleuClair);
		compositePointure.setLayout(rowLayoutV);

		Label labelPointure = new Label(compositePointure, SWT.NONE);
		labelPointure.setBackground(Couleur.bleuClair);
		labelPointure.setText("Pointure : ");
		labelPointure.setBounds(10, 10, 100, 25);

		final Text textPointure = new Text(compositePointure, SWT.BORDER);
		textPointure.setText("");
		//textMail.setBounds(10, 30, 100, 25);
		
		//Taille
		Composite compositeTaille = new Composite(colonne2, SWT.NONE);
		compositeTaille.setBackground(Couleur.bleuClair);
		compositeTaille.setLayout(rowLayoutV);

		Label labelTaille = new Label(compositeTaille, SWT.NONE);
		labelTaille.setBackground(Couleur.bleuClair);
		labelTaille.setText("Taille : ");
		labelTaille.setBounds(10, 10, 100, 25);

		final Text textTaille = new Text(compositeTaille, SWT.BORDER);
		textTaille.setText("");
		//textMail.setBounds(10, 30, 100, 25);
		
		
		//DateArrivee
		Composite compositeDateArrivee = new Composite(colonne2, SWT.NONE);
		compositeDateArrivee.setBackground(Couleur.bleuClair);
		compositeDateArrivee.setLayout(rowLayoutV);

		Label labelDateArrivee = new Label(compositeDateArrivee, SWT.NONE);
		labelDateArrivee.setBackground(Couleur.bleuClair);
		labelDateArrivee.setText("Date d'arrivée : ");
		labelDateArrivee.setBounds(10, 10, 100, 25);

		final Text textDateArrivee = new Text(compositeDateArrivee, SWT.BORDER);
		textDateArrivee.setText("");
		//textMail.setBounds(10, 30, 100, 25);
		
		//NombreHeures
		Composite compositeNombreHeures = new Composite(colonne2, SWT.NONE);
		compositeNombreHeures.setBackground(Couleur.bleuClair);
		compositeNombreHeures.setLayout(rowLayoutV);

		Label labelNombreHeures = new Label(compositeNombreHeures, SWT.NONE);
		labelNombreHeures.setBackground(Couleur.bleuClair);
		labelNombreHeures.setText("Nombre d'heures : ");
		labelNombreHeures.setBounds(10, 10, 100, 25);

		final Text textNombreHeures = new Text(compositeNombreHeures, SWT.BORDER);
		textNombreHeures.setText("");
		//textMail.setBounds(10, 30, 100, 25);
		
		//RemboursementTransport
		Composite compositeRemboursementTransport = new Composite(colonne3, SWT.NONE);
		compositeRemboursementTransport.setBackground(Couleur.bleuClair);
		compositeRemboursementTransport.setLayout(rowLayoutV);

		Label labelRemboursementTransport = new Label(compositeRemboursementTransport, SWT.NONE);
		labelRemboursementTransport.setBackground(Couleur.bleuClair);
		labelRemboursementTransport.setText("Remboursement transport : ");
		labelRemboursementTransport.setBounds(10, 10, 100, 25);

		final Text textRemboursementTransport = new Text(compositeRemboursementTransport, SWT.BORDER);
		textRemboursementTransport.setText("");
		//textMail.setBounds(10, 30, 100, 25);
		
		//RemboursementTelephone
		Composite compositeRemboursementTelephone = new Composite(colonne3, SWT.NONE);
		compositeRemboursementTelephone.setBackground(Couleur.bleuClair);
		compositeRemboursementTelephone.setLayout(rowLayoutV);

		Label labelRemboursementTelephone = new Label(compositeRemboursementTelephone, SWT.NONE);
		labelRemboursementTelephone.setBackground(Couleur.bleuClair);
		labelRemboursementTelephone.setText("Remboursement telephone : ");
		labelRemboursementTelephone.setBounds(10, 10, 100, 25);

		final Text textRemboursementTelephone = new Text(compositeRemboursementTelephone, SWT.BORDER);
		textRemboursementTelephone.setText("");
		//textMail.setBounds(10, 30, 100, 25);
		
		//Salaire
		Composite compositeSalaire = new Composite(colonne3, SWT.NONE);
		compositeSalaire.setBackground(Couleur.bleuClair);
		compositeSalaire.setLayout(rowLayoutV);

		Label labelSalaire = new Label(compositeSalaire, SWT.NONE);
		labelSalaire.setBackground(Couleur.bleuClair);
		labelSalaire.setText("Salaire : ");
		labelSalaire.setBounds(10, 10, 100, 25);

		final Text textSalaire = new Text(compositeSalaire, SWT.BORDER);
		textSalaire.setText("");

		Composite test = new Composite(colonne3, SWT.NONE);
		test.setBackground(Couleur.bleuClair);
		
		Composite compositeValidation = new Composite(colonne3, SWT.CENTER);
		compositeValidation.setBackground(Couleur.bleuClair);
		compositeValidation.setLayout(rowLayoutV);
		
		Label labelValidation = new Label(compositeValidation, SWT.NONE);
		labelValidation.setBackground(Couleur.bleuClair);
		labelValidation.setText("");
		
		Button buttonValidation = new Button(compositeValidation, SWT.BACKGROUND);
		buttonValidation.setText("Valider");
		buttonValidation.setBounds(10, 60, 100, 25);
		buttonValidation.addSelectionListener(new SelectionAdapter() {
		  
		  @Override public void widgetSelected(SelectionEvent arg0) {
			  
			  Employe employe = new Employe(titre.getText(),textNom.getText(),textPrenom.getText(),textMail.getText(),textTelephone.getText(),
					  Integer.parseInt(textNumeroMatricule.getText()),textPointure.getText(),textTaille.getText(),textDateArrivee.getText(),
					  Double.parseDouble(textNombreHeures.getText()),Double.parseDouble(textRemboursementTransport.getText()),
					  Double.parseDouble(textRemboursementTelephone.getText()),Double.parseDouble(textSalaire.getText()),"Publié");
			  try { 
				  employe.insertDatabase(); 
			  } catch (SQLException e) { 
				 e.printStackTrace(); 
			  } 
		  
		  }
		  
		});
		 

		
		//vueEmploye.layout();
		vueEmploye.layout(true);
		vueEmploye.pack();
		//vueEmploye.redraw();
		
		vueEmploye.getParent().pack();
		
	}
	
	public static void vueEmployeAfficher(Composite composite, Shell shell) {
		
		RowLayout rowLayoutV = new RowLayout();
	    rowLayoutV.type = SWT.VERTICAL;
		
	    vue = new Composite(composite, SWT.NONE);
		vue.setLayout(rowLayoutV);
		vue.setBackground(Couleur.gris);
		
	    final Table table = new Table (vue, SWT.BORDER | SWT.MULTI);
	    table.setLinesVisible (true);
		table.setHeaderVisible (true);
		//Rectangle clientArea = composite.getClientArea ();
		//table.setBounds (clientArea.x, clientArea.y, 200, 200);
		
		
		//on met les noms des colonnes
		String[] titles = {"Titre","Nom","Prenom","Mail","Telephone","numeroMatricule","Pointure","Taille","DateArrivée","NombreHeures","RemboursementTransport","RemboursementTelephone","Salaire"};
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
				DateFormat dateFormat = new SimpleDateFormat("dd/mm/yyyy");
				item.setText(8,dateFormat.format(e.getDateArrivee()));
				item.setText(9,Double.toString(e.getNombreHeures()));
				item.setText(10,Double.toString(e.getRemboursementTransport()));
				item.setText(11,Double.toString(e.getRemboursementTelephone()));
				item.setText(12,Double.toString(e.getSalaire()));
				
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for (TableColumn col : columns)
			col.pack ();
		
		
		Menu menu = new Menu (shell, SWT.POP_UP);
		table.setMenu (menu);
		
		MenuItem delete = new MenuItem (menu, SWT.PUSH);
		delete.setText ("Supprimer l'element");
		//delete.addListener (SWT.Selection, event -> table.remove (table.getSelectionIndices ()));
	    
		MenuItem update = new MenuItem (menu, SWT.PUSH);
		update.setText ("Modifier l'element");
		//update.addListener (SWT.Selection, event -> table.remove (table.getSelectionIndices ()));
		
		//final List list = new List(vue, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL);

		/*
		 * list.add(
		 * "Id|Titre|Nom|Prenom|Mail|Telephone|numeroMatricule|Pointure|Taille|DateArrivée|NombreHeures|RemboursementTransport|RemboursementTelephone|Salaire|Status"
		 * );
		 * 
		 * try { for (Employe e : Employe.getAllEmploye()) { list.add(e.toString()); } }
		 * catch (SQLException e) { e.printStackTrace(); }
		 */

		/*
		 * list.addSelectionListener(new SelectionListener() {
		 * 
		 * public void widgetSelected(SelectionEvent event) { int[] selections =
		 * list.getSelectionIndices(); String outText = ""; for (int loopIndex = 0;
		 * loopIndex < selections.length; loopIndex++) outText += selections[loopIndex]
		 * + " "; System.out.println("You selected: " + outText); }
		 * 
		 * public void widgetDefaultSelected(SelectionEvent event) { int[] selections =
		 * list.getSelectionIndices(); String outText = ""; for (int loopIndex = 0;
		 * loopIndex < selections.length; loopIndex++) outText += selections[loopIndex]
		 * + " "; System.out.println("You selected: " + outText); } });
		 */
		
	}
	
	
	public void vueEmployeCreation() {
		this.display = new Display();
		this.shell = new Shell(display);
		this.shell.setText("Senettis App");

		Composite compositeMain = new Composite(shell, SWT.BACKGROUND);
		Color couleur = new Color(display, 131, 133, 131);
		compositeMain.setBackground(couleur);

		FillLayout fillLayout = new FillLayout();
		fillLayout.type = SWT.VERTICAL;
		compositeMain.setLayout(fillLayout);

		Composite compositeNom = new Composite(compositeMain, SWT.NONE);
		compositeNom.setBackground(couleur);

		Label labelNom = new Label(compositeNom, SWT.NONE);
		labelNom.setBackground(couleur);
		labelNom.setText("Nom");
		labelNom.setBounds(10, 10, 100, 25);

		final Text textNom = new Text(compositeNom, SWT.BORDER);
		textNom.setText("");
		textNom.setBounds(10, 30, 100, 25);

		Composite compositePrix = new Composite(compositeMain, SWT.BACKGROUND);
		compositePrix.setBackground(couleur);

		Label labelPrix = new Label(compositePrix, SWT.NONE);
		labelPrix.setBackground(couleur);
		labelPrix.setText("Prix");
		labelPrix.setBounds(10, 10, 20, 25);

		final Text textPrix = new Text(compositePrix, SWT.BORDER);
		textPrix.setText("");
		textPrix.setBounds(10, 30, 30, 25);

		Composite compositeCommentaire = new Composite(compositeMain, SWT.BACKGROUND);
		compositeCommentaire.setBackground(couleur);

		Label labelCommentaire = new Label(compositeCommentaire, SWT.NONE);
		labelCommentaire.setBackground(couleur);
		labelCommentaire.setText("Commentaires");
		labelCommentaire.setBounds(10, 10, 100, 25);

		final Text textCommentaire = new Text(compositeCommentaire, SWT.CENTER);

		textCommentaire.setText("");
		textCommentaire.setBounds(10, 30, 100, 25);

		Composite compositeValidation = new Composite(compositeMain, SWT.CENTER);
		compositeValidation.setBackground(couleur);
		Button button = new Button(compositeValidation, SWT.BACKGROUND);
		button.setText("Valider");
		button.setBounds(10, 60, 100, 25);
		button.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent arg0) {

				Produit produit = new Produit(textNom.getText(), Double.parseDouble(textPrix.getText()),
						textCommentaire.getText(), "Publié");

				try {
					produit.insertDatabase();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				System.out.println("done");

			}

		});

		compositeMain.setSize(500, 500);
		labelNom.pack();
		labelPrix.pack();
		labelCommentaire.pack();

		this.shell.pack();
		this.shell.open();

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}

		display.dispose();
		labelNom.dispose();
	}
	
	public Composite getComposite() {
		return this.vueEmploye;
	}
	
}
