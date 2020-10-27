package GUI;

import java.sql.SQLException;
import org.eclipse.swt.*;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

import classes.Produit;



public class VueProduit {
	private  Composite ListeProduit;
	private Composite selection;
	private Composite vueProduit;

	public VueProduit(Composite composite) {
		this.vueProduit = new Composite(composite, SWT.NONE);
		Couleur.setDisplay(composite.getDisplay());
		vueProduit.setBackground(Couleur.gris);

		RowLayout rowLayoutV = new RowLayout(SWT.VERTICAL);
		vueProduit.setLayout(rowLayoutV);

		compositeSelection(vueProduit);
		vueProduitAfficher(vueProduit);

		vueProduit.pack();
		vueProduit.getParent().pack();

	}

	public Composite getVueProduit() {
		return this.vueProduit;
	}

	public  void vueProduitAfficher(Composite composite) {

		RowLayout rowLayoutV = new RowLayout();
		rowLayoutV.type = SWT.VERTICAL;

		ListeProduit = new Composite(composite, SWT.NONE);
		ListeProduit.setLayout(rowLayoutV);
		ListeProduit.setBackground(Couleur.gris);

		final Table table = new Table(ListeProduit, SWT.BORDER | SWT.MULTI);
		table.setLinesVisible(true);
		table.setHeaderVisible(true);

		// on met les noms des colonnes
		String[] titles = { "ProduitId", "Nom", "Prix", "Commentaires" };

		for (String title : titles) {
			TableColumn column = new TableColumn(table, SWT.NONE);
			column.setText(title);
		}

		final TableColumn[] columns = table.getColumns();
		try {
			for (Produit p : Produit.getAllProduct()) {
				TableItem item = new TableItem(table, SWT.NONE);
				item.setText(0, Integer.toString(p.getProduitId()));
				item.setText(1, p.getNom());
				item.setText(2, "" + p.getPrix());
				item.setText(3, p.getCommentaires());

			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for (TableColumn col : columns)
			col.pack();
		
		ListeProduit.pack();

	}

	public  void compositeSelection(Composite composite) {
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
				System.out.println("bouton creer employe");
				selection.dispose();
				ListeProduit.dispose();
			
				createProduct(composite);

			}
		});
		
		boutonCreer.pack();
		selection.pack();
	}
	
	public  void createProduct (Composite composite ) {

		
		
		composite.setLayout(new RowLayout(SWT.VERTICAL));
		composite.setBackground(Couleur.bleuFonce);
		
		//Nom part
		
		Composite compositeNom = new Composite(composite, SWT.NONE);
		compositeNom.setBackground(Couleur.gris);
		Label labelNom = new Label(compositeNom, SWT.NONE);
		labelNom.setBackground(Couleur.gris);
		labelNom.setText("Nom");
		labelNom.setBounds(10, 10, 100, 25);
		final Text textNom = new Text(compositeNom, SWT.BORDER);
		textNom.setText("");
		textNom.setBounds(10, 30, 100, 25);

		//Prix part 
		
		Composite compositePrix = new Composite(composite, SWT.BACKGROUND);
		compositePrix.setBackground(Couleur.gris);
		Label labelPrix = new Label(compositePrix, SWT.NONE);
		labelPrix.setBackground(Couleur.gris);
		labelPrix.setText("Prix");
		labelPrix.setBounds(10, 10, 20, 25);
		final Text textPrix = new Text(compositePrix, SWT.BORDER);
		textPrix.setText("");
		textPrix.setBounds(10, 30, 30, 25);
	
		
		
		//Commentaire part
		
		Composite compositeCommentaire = new Composite(composite, SWT.BACKGROUND);
		compositeCommentaire.setBackground(Couleur.gris);
		Label labelCommentaire = new Label(compositeCommentaire, SWT.NONE);
		labelCommentaire.setBackground(Couleur.gris);
		labelCommentaire.setText("Commentaires");
		labelCommentaire.setBounds(10, 10, 100, 25);
		final Text textCommentaire = new Text(compositeCommentaire, SWT.CENTER);
		textCommentaire.setText("");
		textCommentaire.setBounds(10, 30, 100, 25);
		
		
		// Validation button
		
		Composite compositeValidation = new Composite(composite, SWT.CENTER);
		compositeValidation.setBackground(Couleur.gris);
		Button validationButton = new Button(compositeValidation, SWT.BACKGROUND);
		validationButton.setText("Valider");
		validationButton.setBounds(10, 60, 100, 25);
		validationButton.addSelectionListener(new SelectionAdapter() {

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
				
				
				
				RowLayout rowLayoutV = new RowLayout(SWT.VERTICAL);
				vueProduit.setLayout(rowLayoutV);
				compositeSelection(vueProduit);
				vueProduitAfficher(vueProduit);
				compositeNom.dispose();
				compositePrix.dispose();
				compositeCommentaire.dispose();
				compositeValidation.dispose();
				composite.pack();
				composite.getParent().pack();
				
				System.out.println("done");

			}

		});
		
		labelNom.pack();
		labelPrix.pack();
		validationButton.pack();
		labelCommentaire.pack();
		composite.pack();
		composite.getParent().pack();

	}
	
	public void setVueProduit(Composite composite) {
		this.vueProduit=composite;
	}

}
