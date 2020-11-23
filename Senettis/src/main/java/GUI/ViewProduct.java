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

public class ViewProduct {
	private Composite mainView;
	private Composite selection;
	private Composite productView;
	private Button boutonCreer;
	private Button buttonModify;
	private Button buttonRemove;

	/**
	 * Constructor
	 * 
	 * @param composite <type>Composite</type>
	 * @throws SQLException
	 */
	public ViewProduct(Composite composite) throws SQLException {
		this.productView = new Composite(composite, SWT.BORDER);
		Couleur.setDisplay(composite.getDisplay());
		productView.setBackground(Couleur.bleuClair);

		RowLayout rowLayoutV = new RowLayout(SWT.VERTICAL);
		productView.setLayout(rowLayoutV);

		compositeSelection();
		addCreateButton();
		mainView();
		productViewDisplay();

	}

	private void mainView() {
		if (!Objects.isNull(mainView) && !mainView.isDisposed()) {
			mainView.dispose();
			this.productView.layout(true, true);
		}
		this.mainView = new Composite(this.productView, SWT.NONE);
		this.productView.layout(true, true);
		if (!Objects.isNull(selection) && !selection.isDisposed())
			mainView.moveBelow(selection);

		mainView.pack();
		productView.pack();

	}

	/**
	 * 
	 * Add a table which contains all Product from the database in the mainView
	 * entered in parameter
	 * 
	 * @param <type>Composite </type>
	 * @throws SQLException
	 */
	public void productViewDisplay() throws SQLException {

		List<Product> allProduct = Product.getAllPublished();

		this.mainView();
		RowLayout rowLayoutV = new RowLayout();
		rowLayoutV.type = SWT.VERTICAL;
		final Table table = new Table(mainView, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL | SWT.FULL_SELECTION);
		table.setLayoutData(new RowData(400, 600));

		table.setLinesVisible(true);
		table.setHeaderVisible(true);

		table.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {

				if (table.getSelectionIndex() != -1) {
					int produitId = Integer.parseInt(table.getSelection()[0].getText());
					cleanModifAndRemoveButton();
					addButtonModif(produitId);
					addButtonRemove(produitId);
				} else {
					System.out.println("erreur");
				}
			}

		});

		String[] titles = { "ProduitId", "Nom", "Marque", "Prix", "Commentaires" };

		for (String title : titles) {
			TableColumn column = new TableColumn(table, SWT.NONE);
			column.setText(title);
		}

		final TableColumn[] columns = table.getColumns();

		for (Product p : allProduct) {
			TableItem item = new TableItem(table, SWT.NONE);
			item.setText(0, Integer.toString(p.getProduitId()));
			item.setText(1, p.getName());
			if (Objects.isNull(p.getBrand()) || p.getBrand().isBlank())
				item.setText(2, "Inconnue");
			else
				item.setText(2, p.getBrand());
			item.setText(3, "" + p.getPrice());

			item.setText(4, p.getComment());

		}

		for (TableColumn col : columns)
			col.pack();
		table.pack();
		mainView.pack();
		productView.pack();
		productView.getParent().pack();

	}

	/**
	 * add a <type>Composite </type> composite to create a Product in the database
	 * 
	 * @param composite
	 */
	public void createProduct() {
		this.productView.setLayout(new RowLayout(SWT.VERTICAL));

		if (!Objects.isNull(mainView) && !mainView.isDisposed()) {
			mainView.dispose();
			productView.layout(true, true);

		}
		mainView = new Composite(this.productView, SWT.CENTER);
		FillLayout fillLayout = new FillLayout(SWT.VERTICAL);
		fillLayout.marginWidth = 50;
		mainView.setLayout(fillLayout);
		mainView.setBackground(Couleur.bleuClair);
		productView.setBackground(Couleur.bleuClair);

		productView.layout(true, true);

		addHeader("Creation Produit");

		FillLayout fillLayoutH5 = new FillLayout();
		fillLayoutH5.marginHeight = 30;
		fillLayoutH5.marginWidth = 20;
		fillLayoutH5.spacing = 5;
		fillLayoutH5.type = SWT.HORIZONTAL;

		// Name part

		Composite compositeName = new Composite(mainView, SWT.NONE);
		compositeName.setLayout(fillLayoutH5);
		compositeName.setBackground(Couleur.bleuClair);
		Label labelName = new Label(compositeName, SWT.NONE);
		labelName.setBackground(Couleur.bleuClair);
		labelName.setText("Nom");
		labelName.setBounds(10, 10, 100, 25);
		final Text textName = new Text(compositeName, SWT.BORDER);
		textName.setText("");
		textName.setBounds(10, 30, 100, 25);

		// Brand part

		Composite compositeBrand = new Composite(mainView, SWT.BACKGROUND);
		compositeBrand.setLayout(fillLayoutH5);
		compositeBrand.setBackground(Couleur.bleuClair);
		Label labelBrand = new Label(compositeBrand, SWT.NONE);
		labelBrand.setBackground(Couleur.bleuClair);
		labelBrand.setText("Marque");
		labelBrand.setBounds(10, 10, 100, 25);
		final Text textBrand = new Text(compositeBrand, SWT.CENTER);
		textBrand.setText("");
		textBrand.setBounds(10, 30, 100, 25);

		// Price part

		Composite compositePrice = new Composite(mainView, SWT.BACKGROUND);
		compositePrice.setLayout(fillLayoutH5);
		compositePrice.setBackground(Couleur.bleuClair);
		Label labelPrix = new Label(compositePrice, SWT.NONE);
		labelPrix.setBackground(Couleur.bleuClair);
		labelPrix.setText("Prix");
		labelPrix.setBounds(10, 10, 20, 25);
		final Text textPrice = new Text(compositePrice, SWT.BORDER);
		textPrice.setText("");
		textPrice.setBounds(10, 30, 30, 25);

		// comment part

		Composite compositeComment = new Composite(mainView, SWT.BACKGROUND);
		compositeComment.setLayout(fillLayoutH5);
		compositeComment.setBackground(Couleur.bleuClair);
		Label labelComment = new Label(compositeComment, SWT.NONE);
		labelComment.setBackground(Couleur.bleuClair);
		labelComment.setText("Commentaires");
		labelComment.setBounds(10, 10, 100, 25);
		final Text textCommentaire = new Text(compositeComment, SWT.NONE);
		textCommentaire.setText("");
		textCommentaire.setBounds(10, 30, 100, 25);

		// Validation button

		Composite compositeButtons = new Composite(mainView, SWT.CENTER);
		compositeButtons.setBackground(Couleur.bleuClair);
		compositeButtons.setLayout(fillLayoutH5);
		Button validationButton = new Button(compositeButtons, SWT.BACKGROUND);
		validationButton.setText("Valider");
		validationButton.setBounds(10, 60, 100, 25);
		validationButton.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent arg0) {

				String name = textName.getText();
				String price = textPrice.getText().replace(",",".");
				String comment = textCommentaire.getText();
				String brand = textBrand.getText();
				boolean isChecked = false;
				try {
					isChecked = checkProduct(name, price);
				} catch (IllegalArgumentException argException) {
					
					MessageBox dialog = new MessageBox(productView.getShell(), SWT.ICON_ERROR | SWT.OK);
					dialog.setText("Erreur Cr�ation :");
					dialog.setMessage("Une erreur est survenue lors de la cr�ation du produit. " + '\n'
							+ argException.getMessage());
					dialog.open();
				}

				if (isChecked) {

					try {
						Product produit = new Product(name, brand, Double.parseDouble(price), comment,
								Status.PUBLISHED);

						if (produit.insertDatabase() < 1)
							throw new SQLException(
									"Erreur lors de l'insertion dans la base de donn�e, 0 lignes retourn�es");

						// validationMessageBox

						MessageBox dialog = new MessageBox(productView.getShell(), SWT.ICON_WORKING | SWT.OK);
						dialog.setText("Succes");
						dialog.setMessage("Le produit " + name + " a bien �t� enregistr�");
						dialog.open();

						// view change

						compositeSelection();
						addCreateButton();
						productViewDisplay();

					} catch (SQLException sqlException) {
						MessageBox dialog = new MessageBox(productView.getShell(), SWT.ICON_ERROR | SWT.OK);
						dialog.setText("Erreur Cr�ation :");
						dialog.setMessage(
								"Une erreur est survenue lors de l'insertion du produit dans la base de donn�es. "
										+ '\n' + sqlException.getMessage());
						dialog.open();

					}

					compositeName.dispose();
					compositePrice.dispose();
					compositeComment.dispose();
					compositeButtons.dispose();
					mainView.pack();
					mainView.getParent().pack();
					selection.pack();

					System.out.println("done");
				}

			}

		});

		Button buttonCancel = new Button(compositeButtons, SWT.BACKGROUND);
		buttonCancel.setText("Annuler");
		buttonCancel.setBounds(10, 60, 100, 25);
		buttonCancel.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent arg0) {

				try {

					compositeSelection();
					addCreateButton();
					productViewDisplay();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				compositeName.dispose();
				compositePrice.dispose();
				compositeComment.dispose();
				compositeButtons.dispose();
				mainView.pack();
				mainView.getParent().pack();

			}

		});

		if (!Objects.isNull(selection) && !selection.isDisposed()) {
			mainView.moveBelow(selection);
		}

		labelName.pack();
		labelPrix.pack();
		validationButton.pack();
		labelComment.pack();
		mainView.pack();
		selection.pack();
		productView.pack();
		productView.getParent().pack();

	}

	/**
	 * add a composite to update a product in the composite entered in parameter by
	 * using it's productId
	 * 
	 * @param <type> Composite </type>composite
	 * @param <type> int </type> productId
	 * @throws SQLException
	 */
	public void updateProduct(int productId) throws SQLException {

		Product product = Product.getProductById(productId);

		this.productView.setLayout(new RowLayout(SWT.VERTICAL));

		if (!Objects.isNull(mainView) && !mainView.isDisposed()) {
			mainView.dispose();
			productView.layout(true, true);

		}
		mainView = new Composite(this.productView, SWT.CENTER);
		FillLayout fillLayout = new FillLayout(SWT.VERTICAL);
		fillLayout.marginWidth = 50;
		mainView.setLayout(fillLayout);
		mainView.setBackground(Couleur.bleuClair);
		productView.setBackground(Couleur.bleuClair);

		productView.layout(true, true);

		addHeader("Creation Produit");

		FillLayout fillLayoutH5 = new FillLayout();
		fillLayoutH5.marginHeight = 30;
		fillLayoutH5.marginWidth = 20;
		fillLayoutH5.spacing = 5;
		fillLayoutH5.type = SWT.HORIZONTAL;

		// Name part

		Composite compositeName = new Composite(mainView, SWT.NONE);
		compositeName.setBackground(Couleur.bleuClair);
		compositeName.setLayout(fillLayoutH5);
		Label labelName = new Label(compositeName, SWT.NONE);
		labelName.setBackground(Couleur.bleuClair);
		labelName.setText("Nom");
		labelName.setBounds(10, 10, 100, 25);
		final Text textName = new Text(compositeName, SWT.BORDER);
		textName.setText("" + product.getName());
		textName.setBounds(10, 30, 100, 25);

		// Brand part

		Composite compositeBrand = new Composite(mainView, SWT.BACKGROUND);
		compositeBrand.setBackground(Couleur.bleuClair);
		compositeBrand.setLayout(fillLayoutH5);
		Label labelBrand = new Label(compositeBrand, SWT.NONE);
		labelBrand.setBackground(Couleur.bleuClair);
		labelBrand.setText("Marque");
		labelBrand.setBounds(10, 10, 100, 25);
		final Text textBrand = new Text(compositeBrand, SWT.CENTER);
		textBrand.setText("");
		textBrand.setBounds(10, 30, 100, 25);

		// Price part

		Composite compositePrice = new Composite(mainView, SWT.BACKGROUND);
		compositePrice.setBackground(Couleur.bleuClair);
		compositePrice.setLayout(fillLayoutH5);
		Label labelPrice = new Label(compositePrice, SWT.NONE);
		labelPrice.setBackground(Couleur.bleuClair);
		labelPrice.setText("Prix");
		labelPrice.setBounds(10, 10, 20, 25);
		final Text textPrice = new Text(compositePrice, SWT.BORDER);
		textPrice.setText("" + product.getPrice());
		textPrice.setBounds(10, 30, 30, 25);

		// Comment part

		Composite compositeComment = new Composite(mainView, SWT.BACKGROUND);
		compositeComment.setBackground(Couleur.bleuClair);
		compositeComment.setLayout(fillLayoutH5);
		Label labelComment = new Label(compositeComment, SWT.NONE);
		labelComment.setBackground(Couleur.bleuClair);
		labelComment.setText("Commentaires");
		labelComment.setBounds(10, 10, 100, 25);
		final Text textComment = new Text(compositeComment, SWT.CENTER);
		textComment.setText("" + product.getComment());
		textComment.setBounds(10, 30, 100, 25);

		// Validation button

		Composite compositeButtons = new Composite(mainView, SWT.CENTER);
		compositeButtons.setBackground(Couleur.bleuClair);
		compositeButtons.setLayout(fillLayoutH5);
		Button buttonValidation = new Button(compositeButtons, SWT.BACKGROUND);
		buttonValidation.setText("Valider");
		buttonValidation.setBounds(10, 60, 100, 25);
		buttonValidation.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				String name = textName.getText();
				String price = textPrice.getText().replace(",", ".");
				String comment = textComment.getText();
				String brand = textBrand.getText();

				boolean isChecked = false;
				try {
					isChecked = checkProduct(name, price);
				} catch (IllegalArgumentException argException) {
					System.out.println(argException.getMessage());
					MessageBox dialog = new MessageBox(productView.getShell(), SWT.ICON_ERROR | SWT.OK);
					dialog.setText("Erreur Cr�ation :");
					dialog.setMessage("Une erreur est survenue lors de la mise � jour du produit. " + '\n'
							+ argException.getMessage());
					dialog.open();
				}

				Product produit = new Product(productId, name, brand, Double.parseDouble(price), comment,
						Status.getStatus("Publi�"));

				if (isChecked) {
					try {
						produit.updateDatabase();
						compositeSelection();

						// validationMessageBox

						MessageBox dialog = new MessageBox(productView.getShell(), SWT.ICON_WORKING | SWT.OK);
						dialog.setText("Succes");
						dialog.setMessage("Le produit " + name + " a bien �t� enregistr�");
						dialog.open();

						addCreateButton();
						productViewDisplay();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					compositeName.dispose();
					compositePrice.dispose();
					compositeComment.dispose();
					compositeButtons.dispose();
					mainView.pack();
					mainView.getParent().pack();
				}
			}

		});

		Button buttonCancel = new Button(compositeButtons, SWT.BACKGROUND);
		buttonCancel.setText("Annuler");
		buttonCancel.setBounds(10, 60, 100, 25);
		buttonCancel.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent arg0) {

				try {

					compositeSelection();
					addCreateButton();
					productViewDisplay();
				} catch (SQLException sqlException) {
					MessageBox dialog = new MessageBox(productView.getShell(), SWT.ICON_ERROR | SWT.OK);
					dialog.setText("Erreur Cr�ation :");
					dialog.setMessage("Une erreur est survenue lors de l'insertion du produit dans la base de donn�es. "
							+ '\n' + sqlException.getMessage());
					dialog.open();
				}

				compositeName.dispose();
				compositePrice.dispose();
				compositeComment.dispose();
				compositeButtons.dispose();
				mainView.pack();
				mainView.getParent().pack();

			}

		});

		if (!Objects.isNull(selection) && !selection.isDisposed())
			mainView.moveBelow(selection);

		labelName.pack();
		labelPrice.pack();
		buttonValidation.pack();
		compositeButtons.pack();
		buttonCancel.pack();
		labelComment.pack();
		mainView.pack();
		productView.pack();
		productView.getParent().pack();

	}

	public void setVueProduit(Composite composite) {
		this.productView = composite;
	}

	/********************************************
	 * 
	 * 
	 * Selection Management
	 * 
	 * 
	 ********************************************/

	/**
	 * Create the selection area which is used to display a header title or buttons
	 * to interact The selection area is displayed in the composite entered in
	 * parameter
	 * 
	 * @param <type> Composite </type> composite
	 */
	public void compositeSelection() {

		if (!Objects.isNull(selection) && !selection.isDisposed()) {
			this.selection.dispose();
			this.productView.layout(true, true);

		}
		this.selection = new Composite(productView, SWT.NONE);
		productView.layout(true, true);
		RowLayout rowLayout = new RowLayout();
		rowLayout.marginWidth = 20;
		this.selection.setLayout(rowLayout);
		this.selection.setBackground(Couleur.bleuClair);

		selection.pack();
		productView.pack();
		this.productView.getParent().pack();

	}

	/**
	 * Add the button "Cr�er" which enables to access the creation form
	 * 
	 * @param composite
	 */
	private void addCreateButton() {
		if (!Objects.isNull(boutonCreer) && !boutonCreer.isDisposed()) {
			this.boutonCreer.dispose();
			this.selection.layout(true, true);

		}
		this.boutonCreer = new Button(this.selection, SWT.CENTER);
		this.selection.layout(true, true);
		boutonCreer.setText("Cr�er");

		boutonCreer.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {

				selection.dispose();
				mainView.dispose();

				createProduct();

			}
		});

		this.boutonCreer.pack();
		this.selection.pack();
		this.productView.getParent().pack();
		this.productView.pack();

	}

	/**
	 * Add a header in Selection composite with title <param>header</param>
	 * 
	 * @param <type>String</type> header
	 */
	public void addHeader(String header) {
		if (!this.selection.isDisposed())
			this.selection.dispose();
		this.selection = new Composite(this.productView, SWT.CENTER | SWT.BORDER);
		this.selection.setBackground(Couleur.bleuFonce);
		FillLayout layout = new FillLayout();
		layout.marginWidth = 100;
		this.selection.setLayout(layout);

		Label HeadLabel = new Label(this.selection, SWT.TITLE);

		HeadLabel.setText(header);
		Font fontTitle = new Font(HeadLabel.getDisplay(), "Arial", 12, SWT.BOLD);
		HeadLabel.setForeground(Couleur.bleuClair);
		HeadLabel.setFont(fontTitle);
		HeadLabel.setBackground(Couleur.bleuFonce);
		this.selection.pack();
		HeadLabel.pack();

	}

	/**
	 * 
	 * @param <type>Composite</type >composite
	 * @param <type>int</type>      productId
	 */
	public void addButtonModif(int productId) {
		if (!Objects.isNull(buttonModify) && !buttonModify.isDisposed()) {

			this.buttonModify.dispose();
			selection.layout(true, true);

		}

		this.buttonModify = new Button(selection, SWT.PUSH);

		this.selection.layout(true, true);
		this.buttonModify.setText("Modifier");

		buttonModify.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {

				try {

					selection.dispose();
					mainView.dispose();
					updateProduct(productId);
					productView.pack();
					productView.getParent().pack();

				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		});

		this.buttonModify.pack();

		this.selection.pack();
	}

	/**
	 * this function is used to optimized the refresh of Selection composite
	 */
	public void cleanModifAndRemoveButton() {
		if (!Objects.isNull(buttonModify) && !buttonModify.isDisposed()) {

			this.buttonModify.dispose();
			selection.layout(true, true);

		}
		if (!Objects.isNull(this.buttonRemove) && !buttonRemove.isDisposed()) {

			this.buttonRemove.dispose();// dispose the actual remove button
			selection.layout(true, true);
		}
	}

	/**
	 * 
	 * @param productId
	 */
	public void addButtonRemove(int productId) {
		if (!Objects.isNull(this.buttonRemove) && !buttonRemove.isDisposed()) {

			this.buttonRemove.dispose();// dispose the actual remove button
			selection.layout(true, true);
		}
		this.buttonRemove = new Button(this.selection, SWT.PUSH);
		this.buttonRemove.setText("Supprimer");
		this.selection.layout(true, true);

		this.buttonRemove.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {

				try {
					Product.removeById(productId);
					compositeSelection();
					addCreateButton();
					productViewDisplay();

				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		});

		buttonRemove.pack();
		selection.pack();
	}

	/*******************
	 * 
	 * Check functions
	 * 
	 *****************/

	public boolean checkProduct(String name, String price) {

		if (Objects.isNull(name))
			throw new IllegalArgumentException("L'attribut name ne peut pas �tre null");
		else if (name.isBlank())
			throw new IllegalArgumentException("Le champ nom ne peut pas �tre vide");
		if (Objects.isNull(price))
			throw new IllegalArgumentException("L'attribut price ne peut pas �tre null");
		else if (name.isBlank())
			throw new IllegalArgumentException("L'attribut prix ne peut pas �tre null");

		try {
			Double priceCheck = Double.parseDouble(price);
			if (priceCheck <= 0)
				throw new IllegalArgumentException("Le  prix doit �tre sup�rieur � 0");
		} catch (NumberFormatException parseDoubleException) {
			throw new IllegalArgumentException("Le prix entr� n'est pas valide, veuillez entrer une valeur num�rique");
		}

		return true;
	}

	/*****
	 * 
	 * Getter
	 * 
	 * 
	 */

	/**
	 * getter for vueProduit
	 * 
	 * @return <type> Composite</type> vueProduit
	 */
	public Composite getVueProduit() {
		return this.productView;
	}

}
