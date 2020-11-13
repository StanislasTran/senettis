package GUI;

import java.sql.SQLException;

import java.util.List;
import java.util.Objects;

import org.eclipse.swt.*;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
//import javafx.embed.swt.FXCanvas ;
import classes.Product;
import classes.Status;

public class VueProduit {
	private Composite productList;
	private Composite selection;
	private Composite productView;

	/**
	 * Constructeur de la VueProduit
	 * 
	 * @param composite <type>Composite</type>
	 * @throws SQLException
	 */
	public VueProduit(Composite composite) throws SQLException {
		this.productView = new Composite(composite, SWT.NONE);
		Couleur.setDisplay(composite.getDisplay());
		productView.setBackground(Couleur.bleuFonce);

		RowLayout rowLayoutV = new RowLayout(SWT.VERTICAL);
		productView.setLayout(rowLayoutV);

		compositeSelection(this.productView);
		vueProduitAfficher(this.productView);

	}

	/**
	 * getter for vueProduit
	 * 
	 * @return <type> Composite</type> vueProduit
	 */
	public Composite getVueProduit() {
		return this.productView;
	}

	/**
	 * 
	 * Add a table which contains all Product from the database in the composite
	 * entered in parameter
	 * 
	 * @param <type>Composite </type>
	 * @throws SQLException
	 */
	public void vueProduitAfficher(Composite composite) throws SQLException {

		List<Product> allProduct = Product.getAllPublished();

		RowLayout rowLayoutV = new RowLayout();
		rowLayoutV.type = SWT.VERTICAL;

		productList = new Composite(composite, SWT.BORDER);

		productList.setLayout(rowLayoutV);
		productList.setBackground(Couleur.bleuFonce);

		final Table table = new Table(productList, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL | SWT.FULL_SELECTION);
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

		String[] titles = { "ProduitId", "Nom", "Prix", "Commentaires" };

		for (String title : titles) {
			TableColumn column = new TableColumn(table, SWT.NONE);
			column.setText(title);
		}

		final TableColumn[] columns = table.getColumns();

		for (Product p : allProduct) {
			TableItem item = new TableItem(table, SWT.NONE);
			item.setText(0, Integer.toString(p.getProduitId()));
			item.setText(1, p.getName());
			item.setText(2, "" + p.getPrice());
			item.setText(3, p.getComment());

		}

		for (TableColumn col : columns)
			col.pack();
		composite.pack();

	}

	/**
	 * Create the selection area which contain create and modify button
	 * 
	 * @param <type> Composite </type> composite
	 */
	public void compositeSelection(Composite composite) {
		
		if(!Objects.isNull(selection) && !selection.isDisposed())
			selection.dispose();
		selection = new Composite(composite, SWT.NONE);
		RowLayout rowLayout = new RowLayout();
		rowLayout.marginWidth = 20;
		selection.setLayout(rowLayout);
		selection.setBackground(Couleur.bleuFonce);

		Button boutonCreer = new Button(selection, SWT.CENTER);

		boutonCreer.setText("Créer");

		boutonCreer.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				System.out.println("bouton creer employe");
				selection.dispose();
				productList.dispose();

				createProduct(composite);

			}
		});

		boutonCreer.pack();
		selection.pack();
		composite.pack();
	}

	/**
	 * 
	 * @param <type>Composite</type >composite
	 * @param <type>int</type>      productId
	 */
	public void compositeSelectionModif(Composite composite, int productId) {
		selection.dispose();
		selection = new Composite(composite, SWT.NONE);
		RowLayout rowLayout = new RowLayout();
		rowLayout.marginWidth = 20;
		selection.setLayout(rowLayout);
		selection.setBackground(Couleur.bleuFonce);

		Button boutonCreer = new Button(selection, SWT.CENTER);

		boutonCreer.setText("Créer");

		boutonCreer.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				
				selection.dispose();
				productList.dispose();

				createProduct(composite);

			}
		});

		Button buttonModify = new Button(selection, SWT.PUSH);
		buttonModify.setText("Modifier");

		buttonModify.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {

				try {
				
					selection.dispose();
					productList.dispose();
					updateProduct(composite, productId);
					productView.pack();
					productView.getParent().pack();

				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		});

		Button buttonRemove = new Button(selection, SWT.PUSH);
		buttonRemove.setText("Supprimer");

		buttonRemove.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {

				try {
					Product.removeById(productId);
					productList.dispose();
					selection.dispose();
					compositeSelection(productView);
					vueProduitAfficher(productView);

				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		});

		buttonRemove.pack();

		buttonModify.pack();

		boutonCreer.pack();
		selection.pack();
	}

	/**
	 * add a <type>Composite </type> composite to create a Product in the database
	 * 
	 * @param composite
	 */
	public void createProduct(Composite composite) {

		composite.setLayout(new RowLayout(SWT.VERTICAL));
		composite.setBackground(Couleur.bleuFonce);

		addHeader("Creation Produit");
		
		// Nom part

		Composite compositeNom = new Composite(composite, SWT.NONE);
		compositeNom.setBackground(Couleur.bleuFonce);
		Label labelName = new Label(compositeNom, SWT.NONE);
		labelName.setBackground(Couleur.bleuFonce);
		labelName.setText("Nom");
		labelName.setBounds(10, 10, 100, 25);
		final Text textName = new Text(compositeNom, SWT.BORDER);
		textName.setText("");
		textName.setBounds(10, 30, 100, 25);

		// Prix part

		Composite compositePrix = new Composite(composite, SWT.BACKGROUND);
		compositePrix.setBackground(Couleur.bleuFonce);
		Label labelPrix = new Label(compositePrix, SWT.NONE);
		labelPrix.setBackground(Couleur.bleuFonce);
		labelPrix.setText("Prix");
		labelPrix.setBounds(10, 10, 20, 25);
		final Text textPrice = new Text(compositePrix, SWT.BORDER);
		textPrice.setText("");
		textPrice.setBounds(10, 30, 30, 25);

		// Commentaire part

		Composite compositeComment = new Composite(composite, SWT.BACKGROUND);
		compositeComment.setBackground(Couleur.bleuFonce);
		Label labelComment = new Label(compositeComment, SWT.NONE);
		labelComment.setBackground(Couleur.bleuFonce);
		labelComment.setText("Commentaires");
		labelComment.setBounds(10, 10, 100, 25);
		final Text textCommentaire = new Text(compositeComment, SWT.CENTER);
		textCommentaire.setText("");
		textCommentaire.setBounds(10, 30, 100, 25);

		// Validation button

		Composite compositeButtons = new Composite(composite, SWT.CENTER);
		compositeButtons.setBackground(Couleur.bleuFonce);
		compositeButtons.setLayout(new RowLayout());
		Button validationButton = new Button(compositeButtons, SWT.BACKGROUND);
		validationButton.setText("Valider");
		validationButton.setBounds(10, 60, 100, 25);
		validationButton.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent arg0) {

				Product produit = new Product(textName.getText(), Double.parseDouble(textPrice.getText()),
						textCommentaire.getText(), Status.PUBLISHED);

				try {
					produit.insertDatabase();
					compositeSelection(productView);
					vueProduitAfficher(productView);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				compositeNom.dispose();
				compositePrix.dispose();
				compositeComment.dispose();
				compositeButtons.dispose();
				composite.pack();
				composite.getParent().pack();

				System.out.println("done");

			}

		});

		Button buttonCancel = new Button(compositeButtons, SWT.BACKGROUND);
		buttonCancel.setText("Annuler");
		buttonCancel.setBounds(10, 60, 100, 25);
		buttonCancel.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent arg0) {

				try {

					compositeSelection(productView);
					vueProduitAfficher(productView);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				compositeNom.dispose();
				compositePrix.dispose();
				compositeComment.dispose();
				compositeButtons.dispose();
				composite.pack();
				composite.getParent().pack();


			}

		});

		labelName.pack();
		labelPrix.pack();
		validationButton.pack();
		labelComment.pack();
		composite.pack();
		composite.getParent().pack();

	}

	/**
	 * add an composite to update a product in the composite entered in parameter by
	 * using it's productId
	 * 
	 * @param <type> Composite </type>composite
	 * @param <type> int </type> productId
	 * @throws SQLException
	 */
	public void updateProduct(Composite composite, int productId) throws SQLException {
		Product produit = Product.getProductById(productId);

		
		composite.setLayout(new RowLayout(SWT.VERTICAL));
		composite.setBackground(Couleur.bleuFonce);
		addHeader("Modifier le produit");
	
		// Name part

		Composite compositeNom = new Composite(composite, SWT.NONE);
		compositeNom.setBackground(Couleur.bleuFonce);
		compositeNom.setLayout(new RowLayout());
		Label labelName = new Label(compositeNom, SWT.NONE);
		labelName.setBackground(Couleur.bleuFonce);
		labelName.setText("Nom");
		labelName.setBounds(10, 10, 100, 25);
		final Text textNom = new Text(compositeNom, SWT.BORDER);
		textNom.setText("" + produit.getName());
		textNom.setBounds(10, 30, 100, 25);

		// Price part

		Composite compositePrix = new Composite(composite, SWT.BACKGROUND);
		compositePrix.setBackground(Couleur.bleuFonce);
		compositePrix.setLayout(new RowLayout());
		Label labelPrice = new Label(compositePrix, SWT.NONE);
		labelPrice.setBackground(Couleur.bleuFonce);
		labelPrice.setText("Prix");
		labelPrice.setBounds(10, 10, 20, 25);
		final Text textPrix = new Text(compositePrix, SWT.BORDER);
		textPrix.setText("" + produit.getPrice());
		textPrix.setBounds(10, 30, 30, 25);

		// Comment part

		Composite compositeComment = new Composite(composite, SWT.BACKGROUND);
		compositeComment.setBackground(Couleur.bleuFonce);
		compositeComment.setLayout(new RowLayout());
		Label labelComment = new Label(compositeComment, SWT.NONE);
		labelComment.setBackground(Couleur.bleuFonce);
		labelComment.setText("Commentaires");
		labelComment.setBounds(10, 10, 100, 25);
		final Text textComment = new Text(compositeComment, SWT.CENTER);
		textComment.setText("" + produit.getComment());
		textComment.setBounds(10, 30, 100, 25);

		// Validation button

		Composite compositeButtons = new Composite(composite, SWT.CENTER);
		compositeButtons.setBackground(Couleur.bleuFonce);
		compositeButtons.setLayout(new RowLayout());
		Button buttonValidation = new Button(compositeButtons, SWT.BACKGROUND);
		buttonValidation.setText("Valider");
		buttonValidation.setBounds(10, 60, 100, 25);
		buttonValidation.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent arg0) {

				Product produit = new Product(productId, textNom.getText(), Double.parseDouble(textPrix.getText()),
						textComment.getText(), Status.getStatus("Publié"));

				try {
					produit.updateDatabase();
					compositeSelection(productView);
					vueProduitAfficher(productView);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				compositeNom.dispose();
				compositePrix.dispose();
				compositeComment.dispose();
				compositeButtons.dispose();
				composite.pack();
				composite.getParent().pack();

			}

		});

		Button buttonCancel = new Button(compositeButtons, SWT.BACKGROUND);
		buttonCancel.setText("Annuler");
		buttonCancel.setBounds(10, 60, 100, 25);
		buttonCancel.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent arg0) {

				try {

					compositeSelection(productView);
					vueProduitAfficher(productView);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				compositeNom.dispose();
				compositePrix.dispose();
				compositeComment.dispose();
				compositeButtons.dispose();
				composite.pack();
				composite.getParent().pack();

			}

		});

		labelName.pack();
		labelPrice.pack();
		buttonValidation.pack();
		compositeButtons.pack();
		buttonCancel.pack();
		labelComment.pack();
		composite.pack();
		composite.getParent().pack();

	}

	public void setVueProduit(Composite composite) {
		this.productView = composite;
	}

	public void addHeader(String header) {
		if (!this.selection.isDisposed())
			this.selection.dispose();
		this.selection = new Composite(this.productView, SWT.BORDER);
		this.selection.setBackground(Couleur.bleuFonce);
		RowLayout layout=new RowLayout();
		this.selection.setLayout(layout);
		layout.marginWidth=50;
		
		
		Label HeadLabel = new Label(this.selection, SWT.TITLE);

		HeadLabel.setText(header);
		Font fontTitle = new Font(HeadLabel.getDisplay(), "Arial", 12, SWT.BOLD);
		HeadLabel.setForeground(Couleur.bleuClair);
		HeadLabel.setFont(fontTitle);
		HeadLabel.setBackground(Couleur.bleuFonce);
		this.selection.pack();
		HeadLabel.pack();
	}

}
