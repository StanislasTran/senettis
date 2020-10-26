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

	private static Display display;;
	private Shell shell;
	private Label label;
	
	static Color bleuClair = new Color(display, 31, 177, 253);
	Color blanc = new Color(display, 254, 254, 254);
	static Color bleuFonce = new Color(display, 1, 88, 144);

	public VueEmploye() {
		
		
		
		
	}
	
	
	public static void compositeSelection(Composite composite) {
		Composite selection = new Composite(composite, SWT.CENTER);
		RowLayout rowLayoutH = new RowLayout();
		rowLayoutH.type = SWT.HORIZONTAL;
		rowLayoutH.marginWidth = 537;
		selection.setLayout(rowLayoutH);
		selection.setBackground(bleuFonce);
		
		
		Button boutonCreer = new Button(selection, SWT.CENTER);
		boutonCreer.setText("Cr�er");
		boutonCreer.setBackground(bleuFonce);
		//boutonCreer.setBounds(10, 60, 100, 20);
		//boutonCreer.addSelectionListener(new SelectionAdapter() {});
		
		//Button boutonRechercher = new Button(selection, SWT.CENTER);
		//boutonRechercher.setText("Rechercher");
		//boutonRechercher.setBackground(bleuFonce);
		//boutonRechercher.setBounds(10, 60, 100, 20);
		//boutonRechercher.addSelectionListener(new SelectionAdapter() {});
	}
	
	
	public static void compositeVue(Composite composite, Shell shell) {

		vueEmployeAfficher(composite, shell);
	}
	
	
	public static void vueEmployeAfficher(Composite composite, Shell shell) {
	
		RowLayout rowLayoutV = new RowLayout();
	    rowLayoutV.type = SWT.VERTICAL;
		
	    Composite vue = new Composite(composite, SWT.NONE);
		vue.setLayout(rowLayoutV);
		vue.setBackground(bleuClair);
		
	    final Table table = new Table (vue, SWT.BORDER | SWT.MULTI);
	    table.setLinesVisible (true);
		table.setHeaderVisible (true);
		//Rectangle clientArea = composite.getClientArea ();
		//table.setBounds (clientArea.x, clientArea.y, 200, 200);
		
		
		//on met les noms des colonnes
		String[] titles = {"Id","Titre","Nom","Prenom","Mail","Telephone","numeroMatricule","Pointure","Taille","DateArriv�e","NombreHeures","RemboursementTransport","RemboursementTelephone","Salaire","Status"};
		for (String title : titles) {
			TableColumn column = new TableColumn (table, SWT.NONE);
			column.setText (title);
		}
		
		final TableColumn [] columns = table.getColumns ();
		try {
			for (Employe e : Employe.getAllEmploye()) {
				TableItem item = new TableItem (table, SWT.NONE);
				item.setText(0,Integer.toString(e.getEmployeId()));
				item.setText(1,e.getTitre());
				item.setText(2,e.getNom());
				item.setText(3,e.getPrenom());
				item.setText(4,e.getMail());
				item.setText(5,e.getTelephone());
				item.setText(6,Integer.toString(e.getNumeroMatricule()));
				item.setText(7,e.getPointure());
				item.setText(8,e.getTaille());
				DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
				item.setText(9,dateFormat.format(e.getDateArrivee()));
				item.setText(10,Double.toString(e.getNombreHeures()));
				item.setText(11,Double.toString(e.getRemboursementTransport()));
				item.setText(12,Double.toString(e.getRemboursementTelephone()));
				item.setText(13,Double.toString(e.getSalaire()));
				item.setText(14,e.getStatus());
				
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
		 * "Id|Titre|Nom|Prenom|Mail|Telephone|numeroMatricule|Pointure|Taille|DateArriv�e|NombreHeures|RemboursementTransport|RemboursementTelephone|Salaire|Status"
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
						textCommentaire.getText(), "Publi�");

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
	
}
