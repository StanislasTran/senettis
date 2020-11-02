package GUI;

import java.sql.SQLException;

import javax.swing.table.TableStringConverter;
import java.util.*;
import java.util.List;

import org.eclipse.swt.*;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
//import javafx.embed.swt.FXCanvas ;
import classes.Produit;

public class VueProduit {
	private Composite listeProduit;
	private Composite selection;
	private Composite vueProduit;

	public VueProduit(Composite composite) throws SQLException {
		this.vueProduit = new Composite(composite, SWT.NONE);
		Couleur.setDisplay(composite.getDisplay());
		vueProduit.setBackground(Couleur.PeterRiver);

		RowLayout rowLayoutV = new RowLayout(SWT.VERTICAL);
		vueProduit.setLayout(rowLayoutV);

		compositeSelection(vueProduit);
		vueProduitAfficher(vueProduit);

		// vueProduit.pack();
		// vueProduit.getParent().pack();

	}

	public Composite getVueProduit() {
		return this.vueProduit;
	}

	public void vueProduitAfficher(Composite composite) throws SQLException {

		List<Produit> allProduct = Produit.getAllProduct();

		RowLayout rowLayoutV = new RowLayout();
		rowLayoutV.type = SWT.VERTICAL;

		listeProduit = new Composite(composite, SWT.BORDER);

		listeProduit.setLayout(rowLayoutV);
		listeProduit.setBackground(Couleur.PeterRiver);

		final Table table = new Table(listeProduit, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL | SWT.FULL_SELECTION);
		table.setLayoutData(new RowData(400, 600));

		table.setLinesVisible(true);
		table.setHeaderVisible(true);

		table.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {

				if (table.getSelectionIndex() != -1) {
					int produitId = Integer.parseInt(table.getSelection()[0].getText());
				compositeSelectionModif(composite, produitId);
				} else {
					System.out.println("erreur");
				}
			}

		});

		// on met les noms des colonnes
		String[] titles = { "ProduitId", "Nom", "Prix", "Commentaires" };

		for (String title : titles) {
			TableColumn column = new TableColumn(table, SWT.NONE);
			column.setText(title);
		}

		final TableColumn[] columns = table.getColumns();

		for (Produit p : allProduct) {
			TableItem item = new TableItem(table, SWT.NONE);
			item.setText(0, Integer.toString(p.getProduitId()));
			item.setText(1, p.getNom());
			item.setText(2, "" + p.getPrix());
			item.setText(3, p.getCommentaires());

		}

		for (TableColumn col : columns)
			col.pack();
		table.setSelection(2);

		vueProduit.setSize(500, 500);
		vueProduit.getParent().setSize(500, 500);
		// listeProduit.pack();

	}

	public void compositeSelection(Composite composite) {
		selection = new Composite(composite, SWT.NONE);
		RowLayout rowLayout = new RowLayout();
		rowLayout.marginWidth = 20;
		selection.setLayout(rowLayout);
		selection.setBackground(Couleur.PeterRiver);

		Button boutonCreer = new Button(selection, SWT.CENTER);

		boutonCreer.setText("Créer");

		boutonCreer.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				System.out.println("bouton creer employe");
				selection.dispose();
				listeProduit.dispose();

				createProduct(composite);

			}
		});

	
		boutonCreer.pack();
		selection.pack();
	}
	

	public void compositeSelectionModif(Composite composite,int produitId) {
		selection.dispose();
		selection = new Composite(composite, SWT.NONE);
		RowLayout rowLayout = new RowLayout();
		rowLayout.marginWidth = 20;
		selection.setLayout(rowLayout);
		selection.setBackground(Couleur.PeterRiver);

		Button boutonCreer = new Button(selection, SWT.CENTER);

		boutonCreer.setText("Créer");

		boutonCreer.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				System.out.println("bouton creer employe");
				selection.dispose();
				listeProduit.dispose();

				createProduct(composite);

			}
		});
		
		Button modifBouton = new Button(selection, SWT.PUSH);
		modifBouton.setText("Modifier");

		modifBouton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {

				try {
					updateProduct(composite, produitId);
					selection.dispose();
					listeProduit.dispose();
					vueProduit.pack();
					vueProduit.getParent().pack();

				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		});
		modifBouton.pack();
	
		boutonCreer.pack();
		selection.pack();
	}

	public void createProduct(Composite composite) {

		composite.setLayout(new RowLayout(SWT.VERTICAL));
		composite.setBackground(Couleur.PeterRiver);

		// Nom part

		Composite compositeNom = new Composite(composite, SWT.NONE);
		compositeNom.setBackground(Couleur.PeterRiver);
		Label labelNom = new Label(compositeNom, SWT.NONE);
		labelNom.setBackground(Couleur.PeterRiver);
		labelNom.setText("Nom");
		labelNom.setBounds(10, 10, 100, 25);
		final Text textNom = new Text(compositeNom, SWT.BORDER);
		textNom.setText("");
		textNom.setBounds(10, 30, 100, 25);

		// Prix part

		Composite compositePrix = new Composite(composite, SWT.BACKGROUND);
		compositePrix.setBackground(Couleur.PeterRiver);
		Label labelPrix = new Label(compositePrix, SWT.NONE);
		labelPrix.setBackground(Couleur.PeterRiver);
		labelPrix.setText("Prix");
		labelPrix.setBounds(10, 10, 20, 25);
		final Text textPrix = new Text(compositePrix, SWT.BORDER);
		textPrix.setText("");
		textPrix.setBounds(10, 30, 30, 25);

		// Commentaire part

		Composite compositeCommentaire = new Composite(composite, SWT.BACKGROUND);
		compositeCommentaire.setBackground(Couleur.PeterRiver);
		Label labelCommentaire = new Label(compositeCommentaire, SWT.NONE);
		labelCommentaire.setBackground(Couleur.PeterRiver);
		labelCommentaire.setText("Commentaires");
		labelCommentaire.setBounds(10, 10, 100, 25);
		final Text textCommentaire = new Text(compositeCommentaire, SWT.CENTER);
		textCommentaire.setText("");
		textCommentaire.setBounds(10, 30, 100, 25);

		// Validation button

		Composite compositeValidation = new Composite(composite, SWT.CENTER);
		compositeValidation.setBackground(Couleur.PeterRiver);
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
				try {
					vueProduitAfficher(vueProduit);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
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

	public void updateProduct(Composite composite, int produitId) throws SQLException {
		Produit produit = Produit.getProductById(produitId);

		composite.setLayout(new RowLayout(SWT.VERTICAL));
		composite.setBackground(Couleur.PeterRiver);

		// Nom part

		Composite compositeNom = new Composite(composite, SWT.NONE);
		compositeNom.setBackground(Couleur.PeterRiver);
		Label labelNom = new Label(compositeNom, SWT.NONE);
		labelNom.setBackground(Couleur.PeterRiver);
		labelNom.setText("Nom");
		labelNom.setBounds(10, 10, 100, 25);
		final Text textNom = new Text(compositeNom, SWT.BORDER);
		textNom.setText("" + produit.getNom());
		textNom.setBounds(10, 30, 100, 25);

		// Prix part

		Composite compositePrix = new Composite(composite, SWT.BACKGROUND);
		compositePrix.setBackground(Couleur.PeterRiver);
		Label labelPrix = new Label(compositePrix, SWT.NONE);
		labelPrix.setBackground(Couleur.PeterRiver);
		labelPrix.setText("Prix");
		labelPrix.setBounds(10, 10, 20, 25);
		final Text textPrix = new Text(compositePrix, SWT.BORDER);
		textPrix.setText("" + produit.getPrix());
		textPrix.setBounds(10, 30, 30, 25);

		// Commentaire part

		Composite compositeCommentaire = new Composite(composite, SWT.BACKGROUND);
		compositeCommentaire.setBackground(Couleur.PeterRiver);
		Label labelCommentaire = new Label(compositeCommentaire, SWT.NONE);
		labelCommentaire.setBackground(Couleur.PeterRiver);
		labelCommentaire.setText("Commentaires");
		labelCommentaire.setBounds(10, 10, 100, 25);
		final Text textCommentaire = new Text(compositeCommentaire, SWT.CENTER);
		textCommentaire.setText("" + produit.getCommentaires());
		textCommentaire.setBounds(10, 30, 100, 25);

		// Validation button

		Composite compositeValidation = new Composite(composite, SWT.CENTER);
		compositeValidation.setBackground(Couleur.PeterRiver);
		Button validationButton = new Button(compositeValidation, SWT.BACKGROUND);
		validationButton.setText("Valider");
		validationButton.setBounds(10, 60, 100, 25);
		validationButton.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent arg0) {

				Produit produit = new Produit(produitId, textNom.getText(), Double.parseDouble(textPrix.getText()),
						textCommentaire.getText(), "Publié");

				try {
					produit.updateDatabase();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				RowLayout rowLayoutV = new RowLayout(SWT.VERTICAL);
				vueProduit.setLayout(rowLayoutV);
				compositeSelection(vueProduit);
				try {
					vueProduitAfficher(vueProduit);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
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
		this.vueProduit = composite;
	}

}
