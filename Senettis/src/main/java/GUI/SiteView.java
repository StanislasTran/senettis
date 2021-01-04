package GUI;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Objects;

import org.eclipse.swt.*;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

import classes.MABAssignment;
import classes.Delivery;
import classes.Site;
import classes.Status;

/**
 * View for Site management
 * 
 *
 */
public class SiteView {

	private Composite siteView;
	private Composite selection;
	private Composite view;
	private Site selectedSite;
	private Menu menu;

	/***
	 * Constructor
	 * 
	 * @param composite
	 * @param display
	 */
	public SiteView(Composite composite, Display display) {

		MyColor.setDisplay(display);

		siteView = new Composite(composite, SWT.NONE);
		RowLayout rowLayout = new RowLayout();
		rowLayout.type = SWT.VERTICAL;
		siteView.setLayout(rowLayout);
		siteView.setBackground(MyColor.bleuClair);

		compositeSelectionCreation();
		vueChantierAfficher();

		siteView.pack();
		siteView.getParent().pack();
	}

	/***
	 * create siteView
	 * 
	 */
	public void newSiteView() {
		compositeSelectionCreation();
		vueChantierAfficher();

		siteView.pack();
		siteView.getParent().pack();
	}

	/**
	 * create the site creation composite
	 */
	public void compositeSelectionCreation() {
		if (!Objects.isNull(selection) && !selection.isDisposed()) {
			selection.dispose();
		}

		String s;
		int addSize;
		s = "Gestion des chantiers";
		addSize = (474 - 20) / 2;

		selection = new Composite(siteView, SWT.CENTER);

		FillLayout fillLayout = new FillLayout();
		fillLayout.type = SWT.VERTICAL;
		selection.setLayout(fillLayout);
		selection.setBackground(MyColor.bleuClair);

		Composite selection1 = new Composite(selection, SWT.BORDER);

		FillLayout fillLayout2 = new FillLayout();
		fillLayout2.type = SWT.VERTICAL;
		fillLayout2.marginWidth = addSize;
		selection1.setLayout(fillLayout2);

		// juste pour creer un espace
		Label l1 = new Label(selection1, SWT.NONE);
		l1.setText("");
		l1.setBackground(MyColor.bleuFonce);

		selection1.setBackground(MyColor.bleuFonce);
		Label HeadLabel = new Label(selection1, SWT.TITLE);
		HeadLabel.setText(s);
		Font fontTitle = new Font(HeadLabel.getDisplay(), "Arial", 12, SWT.BOLD);
		HeadLabel.setForeground(MyColor.bleuClair);
		HeadLabel.setFont(fontTitle);
		HeadLabel.setBackground(MyColor.bleuFonce);

		// juste pour creer un espace
		Label l2 = new Label(selection1, SWT.NONE);
		l2.setText("");
		l2.setBackground(MyColor.bleuFonce);

		selection1.pack();

		Composite selection2 = new Composite(selection, SWT.NONE);
		RowLayout rowLayout = new RowLayout();
		rowLayout.marginWidth = 20;
		rowLayout.marginTop = 6;
		selection2.setLayout(rowLayout);
		selection2.setBackground(MyColor.bleuClair);

		Button boutonCreer = new Button(selection2, SWT.CENTER);
		boutonCreer.setText("Créer");
		boutonCreer.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				createSiteView();
			}
		});
		selection2.pack();
		selection.pack();
	}

	/***
	 * 
	 * Create modification header
	 * 
	 * @param table : table which contains all sites
	 */
	public void compositeHeaderForModification(Table table) {
		if (!Objects.isNull(selection) && !selection.isDisposed()) {
			selection.dispose();
		}

		String s;
		int addSize;
		s = "Gestion des chantiers";
		addSize = (474 - 20) / 2;

		selection = new Composite(siteView, SWT.CENTER);

		FillLayout fillLayout = new FillLayout();
		fillLayout.type = SWT.VERTICAL;
		selection.setLayout(fillLayout);
		selection.setBackground(MyColor.bleuClair);

		Composite selection1 = new Composite(selection, SWT.BORDER);

		FillLayout fillLayout2 = new FillLayout();
		fillLayout2.type = SWT.VERTICAL;
		fillLayout2.marginWidth = addSize;
		selection1.setLayout(fillLayout2);

		Label l1 = new Label(selection1, SWT.NONE);
		l1.setText("");
		l1.setBackground(MyColor.bleuFonce);

		selection1.setBackground(MyColor.bleuFonce);
		Label HeadLabel = new Label(selection1, SWT.TITLE);
		HeadLabel.setText(s);
		Font fontTitle = new Font(HeadLabel.getDisplay(), "Arial", 12, SWT.BOLD);
		HeadLabel.setForeground(MyColor.bleuClair);
		HeadLabel.setFont(fontTitle);
		HeadLabel.setBackground(MyColor.bleuFonce);

		Label l2 = new Label(selection1, SWT.NONE);
		l2.setText("");
		l2.setBackground(MyColor.bleuFonce);

		selection1.pack();

		Composite selection2 = new Composite(selection, SWT.NONE);
		RowLayout rowLayout = new RowLayout();
		rowLayout.marginWidth = 20;
		rowLayout.marginTop = 6;
		selection2.setLayout(rowLayout);
		selection2.setBackground(MyColor.bleuClair);

		Button createButton = new Button(selection2, SWT.CENTER);
		createButton.setText("Créer");
		createButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				createSiteView();
			}
		});

		// button modify
		Button modifyButton = new Button(selection2, SWT.CENTER);
		modifyButton.setText("Modifier");
		modifyButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				modifySiteView();
			}
		});

		// Button remove
		Button removeButton = new Button(selection2, SWT.CENTER);
		removeButton.setText("Supprimer");
		removeButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				try {
					if (selectedSite == null) {
						throw new Error("selectedChantier est vide");
					}
					Site c = Site.getSiteById(selectedSite.getSiteId());
					MessageBox dialog = new MessageBox(siteView.getShell(), SWT.ICON_QUESTION | SWT.YES | SWT.NO);
					dialog.setText("Suppression Chantier");
					dialog.setMessage("Voulez vous supprimer le chantier " + c.getName()
							+ " ?\nToutes les affectations et livraisons liées à ce chantier seront supprimées.");
					int buttonID = dialog.open();
					switch (buttonID) {
					case SWT.YES:
						suppChantier(table);
					}

				} catch (NumberFormatException | SQLException e) {

					MessageBox dialog = new MessageBox(siteView.getShell(), SWT.ICON_ERROR | SWT.OK);
					dialog.setText("Erreur Supression");
					dialog.setMessage(
							"Une erreur est survenue lors de la supression du chantier. " + '\n' + e.getMessage());
					dialog.open();

				}

			}
		});
		selection2.pack();
		selection.pack();
	}

	/***
	 * Archive the selectionned site
	 * 
	 * @param </type>Tabletable</type> : table which containts sites
	 * @throws SQLException
	 */
	public void suppChantier(Table table) throws SQLException {
		if (selectedSite == null) {
			throw new Error("selectedChantier est vide");
		}

		Site c = Site.getSiteById(selectedSite.getSiteId());
		c.setStatus(Status.ARCHIVED);
		c.updateDatabase();

		for (Delivery d : Delivery.getAllDelivery()) {
			if (d.getIdChantier() == c.getSiteId()) {
				d.setStatus("Archivé");
				d.updateDatabase();
			}
		}

		for (MABAssignment a : MABAssignment.getAllAffectation()) {
			if (a.getIdChantier() == c.getSiteId()) {
				a.setStatus(Status.ARCHIVED);
				a.updateDatabase();
			}
		}

		selectedSite = null;

		compositeSelectionCreation();

		updateTable(table);
	}

	/***
	 * Create the view to modify a site
	 */
	public void modifySiteView() {

		formulaireModification();
		modifyTitle();
		formulaireModification();

		siteView.pack();
		siteView.getParent().pack();

	}

	/***
	 * Modify the Modification part header
	 */
	public void modifyTitle() {
		if (!Objects.isNull(selection) && !selection.isDisposed()) {
			selection.dispose();
		}
		selection = new Composite(siteView, SWT.CENTER);

		FillLayout fillLayout = new FillLayout();
		fillLayout.type = SWT.VERTICAL;

		int addSize = view.getSize().x;
		addSize = (addSize - 200) / 2;// take the size difference between the title and the modify form
		fillLayout.marginWidth = addSize;
		selection.setLayout(fillLayout);

		// just for space
		Label l1 = new Label(selection, SWT.NONE);
		l1.setText("");
		l1.setBackground(MyColor.bleuFonce);

		selection.setBackground(MyColor.bleuFonce);
		Label HeadLabel = new Label(selection, SWT.TITLE);
		HeadLabel.setText("Modification d'un Chantier");
		Font fontTitle = new Font(HeadLabel.getDisplay(), "Arial", 12, SWT.BOLD);
		HeadLabel.setForeground(MyColor.bleuClair);
		HeadLabel.setFont(fontTitle);
		HeadLabel.setBackground(MyColor.bleuFonce);

		// just for space
		Label l2 = new Label(selection, SWT.NONE);
		l2.setText("");
		l2.setBackground(MyColor.bleuFonce);

		selection.pack();
	}

	/***
	 * Create the modify form
	 */
	public void formulaireModification() {
		if (!Objects.isNull(view) && !view.isDisposed()) {
			view.dispose();
		}
		if (selectedSite == null) {
			throw new Error("selectedChantier est vide");
		}

		// view creation
		view = new Composite(siteView, SWT.NONE);
		FillLayout fillLayoutH = new FillLayout();
		fillLayoutH.type = SWT.HORIZONTAL;
		view.setLayout(fillLayoutH);

		// create 3 column to orginize filed
		FillLayout fillLayoutV = new FillLayout();
		fillLayoutV.type = SWT.VERTICAL;
		fillLayoutV.marginWidth = 10;
		Composite colonne1 = new Composite(view, SWT.BORDER);
		colonne1.setBackground(MyColor.bleuClair);
		colonne1.setLayout(fillLayoutV);

		FillLayout fillLayoutH5 = new FillLayout();
		fillLayoutH5.marginHeight = 30;
		fillLayoutH5.spacing = 5;
		fillLayoutH5.type = SWT.HORIZONTAL;

		// name
		Composite compositeName = new Composite(colonne1, SWT.NONE);
		compositeName.setBackground(MyColor.bleuClair);
		compositeName.setLayout(fillLayoutH5);

		Label nameLabel = new Label(compositeName, SWT.NONE);
		nameLabel.setBackground(MyColor.bleuClair);
		nameLabel.setText("Nom* :                                                           ");// space to resize text
																								// field

		final Text nameText = new Text(compositeName, SWT.BORDER);
		nameText.setText(selectedSite.getName());

		// Adress
		Composite compositeAdress = new Composite(colonne1, SWT.NONE);
		compositeAdress.setBackground(MyColor.bleuClair);
		compositeAdress.setLayout(fillLayoutH5);

		Label adressLabel = new Label(compositeAdress, SWT.NONE);
		adressLabel.setBackground(MyColor.bleuClair);
		adressLabel.setText("Adresse : ");

		final Text adressText = new Text(compositeAdress, SWT.BORDER);
		adressText.setText(selectedSite.getAdresse());

		// Boutons
		Composite compositeButtons = new Composite(colonne1, SWT.CENTER);
		compositeButtons.setBackground(MyColor.bleuClair);
		compositeButtons.setLayout(fillLayoutH5);

		Button validationButtons = new Button(compositeButtons, SWT.BACKGROUND);
		validationButtons.setText("Valider");
		validationButtons.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent arg0) {

				try {
					int id = selectedSite.getSiteId();
					selectedSite = new Site(id, nameText.getText(), adressText.getText(), Status.PUBLISHED);
					validerModification();
				} catch (Throwable e) {
					e.printStackTrace();

					MessageBox dialog = new MessageBox(siteView.getShell(), SWT.ICON_ERROR | SWT.OK);
					dialog.setText("Erreur Modification");
					dialog.setMessage(
							"Une erreur est survenue lors de la modification du chantier. " + '\n' + e.getMessage());
					dialog.open();
				}

			}
		});

		Button buttonAnnulation = new Button(compositeButtons, SWT.BACKGROUND);
		buttonAnnulation.setText("Annuler");
		buttonAnnulation.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				newSiteView();
			}
		});

		view.pack();
	}

	/***
	 * modifie le chantier selectionne dans la base de données
	 */
	public void validerModification() {
		if (selectedSite == null) {
			throw new Error("selectedChantier est vide");
		}

		// on insert dans la base de données
		try {
			selectedSite.updateDatabase();

			MessageBox dialog = new MessageBox(siteView.getShell(), SWT.ICON_INFORMATION | SWT.OK);
			dialog.setText("Modification réussie");
			dialog.setMessage("Le chantier a bien été modifié dans la base de données.");
			dialog.open();
			selectedSite = null;
		} catch (SQLException e) {
			e.printStackTrace();

			MessageBox dialog = new MessageBox(siteView.getShell(), SWT.ICON_ERROR | SWT.OK);
			dialog.setText("Erreur Modification");
			dialog.setMessage("Une erreur est survenue lors de la modification du chantier. " + e.getMessage());
			dialog.open();
		}

		newSiteView();
	}

	/***
	 * create the view for create a new Site
	 */
	public void createSiteView() {
		creationTitle();
		creationForm();

		siteView.pack();
		siteView.getParent().pack();

	}

	/***
	 * update the header for creation part
	 * 
	 */
	public void creationTitle() {
		if (!Objects.isNull(selection) && !selection.isDisposed()) {
			selection.dispose();
		}

		selection = new Composite(siteView, SWT.CENTER);

		FillLayout fillLayout = new FillLayout();
		fillLayout.type = SWT.VERTICAL;
		fillLayout.marginWidth = 163;
		selection.setLayout(fillLayout);

		// just for create a space
		Label l1 = new Label(selection, SWT.NONE);
		l1.setText("");
		l1.setBackground(MyColor.bleuFonce);

		selection.setBackground(MyColor.bleuFonce);
		Label HeadLabel = new Label(selection, SWT.TITLE);
		HeadLabel.setText("Creation d'un Chantier");
		Font fontTitle = new Font(HeadLabel.getDisplay(), "Arial", 12, SWT.BOLD);
		HeadLabel.setForeground(MyColor.bleuClair);
		HeadLabel.setFont(fontTitle);
		HeadLabel.setBackground(MyColor.bleuFonce);

		// juste for create a space
		Label l2 = new Label(selection, SWT.NONE);
		l2.setText("");
		l2.setBackground(MyColor.bleuFonce);

		selection.pack();
	}

	/***
	 * create the site creation form
	 */
	public void creationForm() {
		if (!Objects.isNull(view) && !view.isDisposed()) {
			view.dispose();
		}
		view = new Composite(siteView, SWT.NONE);
		FillLayout fillLayoutH = new FillLayout();
		fillLayoutH.type = SWT.HORIZONTAL;
		view.setLayout(fillLayoutH);

		// Create 3 column to organize fields
		FillLayout fillLayoutV = new FillLayout();
		fillLayoutV.type = SWT.VERTICAL;
		fillLayoutV.marginWidth = 10;

		Composite colonne1 = new Composite(view, SWT.BORDER);
		colonne1.setBackground(MyColor.bleuClair);
		colonne1.setLayout(fillLayoutV);

		// Common for the differents fields of the form
		FillLayout fillLayoutH5 = new FillLayout();
		fillLayoutH5.marginHeight = 30;
		fillLayoutH5.marginWidth = 20;
		fillLayoutH5.spacing = 5;
		fillLayoutH5.type = SWT.HORIZONTAL;

		// name
		Composite compositeName = new Composite(colonne1, SWT.NONE);
		compositeName.setBackground(MyColor.bleuClair);
		compositeName.setLayout(fillLayoutH5);

		Label nameLabel = new Label(compositeName, SWT.NONE);
		nameLabel.setBackground(MyColor.bleuClair);
		nameLabel.setText("Nom* :                                                           "); // space to adapt the
																								// composite size

		final Text textName = new Text(compositeName, SWT.BORDER);
		textName.setText("");

		// Adress
		Composite compositeAdress = new Composite(colonne1, SWT.NONE);
		compositeAdress.setBackground(MyColor.bleuClair);
		compositeAdress.setLayout(fillLayoutH5);

		Label adressLabel = new Label(compositeAdress, SWT.NONE);
		adressLabel.setBackground(MyColor.bleuClair);
		adressLabel.setText("Adresse : ");

		final Text adressText = new Text(compositeAdress, SWT.BORDER);
		adressText.setText("");

		// buttons
		Composite compositeButton = new Composite(colonne1, SWT.CENTER);
		compositeButton.setBackground(MyColor.bleuClair);
		compositeButton.setLayout(fillLayoutH5);

		Button validationButton = new Button(compositeButton, SWT.BACKGROUND);
		validationButton.setText("Valider");
		validationButton.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent arg0) {

				try {
					validerCreation(textName.getText(), adressText.getText());
				} catch (Throwable e) {
					e.printStackTrace();

					MessageBox dialog = new MessageBox(siteView.getShell(), SWT.ICON_ERROR | SWT.OK);
					dialog.setText("Erreur Création");
					dialog.setMessage(
							"Une erreur est survenue lors de la création d'un chantier. " + '\n' + e.getMessage());
					dialog.open();
				}
			}
		});

		Button cancelButton = new Button(compositeButton, SWT.BACKGROUND);
		cancelButton.setText("Annuler");
		cancelButton.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				newSiteView();
			}
		});
		view.pack();
	}

	/***
	 * Create a site into the database
	 */
	public void validerCreation(String nameText, String adressText) {

		if (nameText.isEmpty()) {
			throw new Error("Merci d'indiquer au moins le nom du chantier.");
		}

		// mandatory field
		Site site = new Site(nameText, Status.PUBLISHED);

		// optional field
		if (!(adressText.isEmpty())) {
			site.setAdress(adressText);
		}

		// insert into the database
		try {
			site.insertDatabase();

			MessageBox dialog = new MessageBox(siteView.getShell(), SWT.ICON_INFORMATION | SWT.OK);
			dialog.setText("Création réussie");
			dialog.setMessage("Le chantier a bien été ajouté à la base de données.");
			dialog.open();
			newSiteView();
			view.pack();
			selection.pack();
			siteView.pack();
		} catch (SQLException e) {

			MessageBox dialog = new MessageBox(siteView.getShell(), SWT.ICON_ERROR | SWT.OK);
			dialog.setText("Erreur Création");
			dialog.setMessage("Une erreur est survenue lors de la création du chantier. " + '\n' + e.getMessage());
			dialog.open();
		}
	}

	/***
	 * display the list of site
	 */
	public void vueChantierAfficher() {
		if (!Objects.isNull(view) && !view.isDisposed()) {
			view.dispose();
		}

		RowLayout rowLayoutV = new RowLayout();
		rowLayoutV.type = SWT.VERTICAL;

		view = new Composite(siteView, SWT.NONE);
		view.setLayout(rowLayoutV);
		view.setBackground(MyColor.bleuClair);

		final Table table = new Table(view, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL | SWT.FULL_SELECTION);
		table.setLayoutData(new RowData(600, 400));
		table.setLinesVisible(true);
		table.setHeaderVisible(true);

		String[] titles = { "Nom", "Adresse" };
		for (String title : titles) {
			TableColumn column = new TableColumn(table, SWT.NONE);
			column.setText(title);
		}

		// on remplit la table
		final TableColumn[] columns = table.getColumns();
		ArrayList<Integer> listSiteId = new ArrayList<Integer>();

		try {
			for (Site c : Site.getAllSite()) {
				// on verifie le status
				if (c.getStatus().getValue().contentEquals("Publié")) {
					TableItem item = new TableItem(table, SWT.NONE);
					item.setText(0, c.getName());
					item.setText(1, c.getAdresse());
					listSiteId.add(c.getSiteId());
				}
			}
		} catch (SQLException e) {

			MessageBox dialog = new MessageBox(siteView.getShell(), SWT.ICON_ERROR | SWT.OK);
			dialog.setText("Erreur");
			dialog.setMessage("Une erreur est survenue. " + '\n' + e.getMessage());
			dialog.open();
		}

		for (TableColumn col : columns)
			col.pack();

		table.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				if (table.getSelectionIndex() != -1) {

					try {
						selectedSite = Site.getSiteById(listSiteId.get(table.getSelectionIndex()));
					} catch (NumberFormatException | SQLException e1) {

						MessageBox dialog = new MessageBox(siteView.getShell(), SWT.ICON_ERROR | SWT.OK);
						dialog.setText("Erreur");
						dialog.setMessage("Une erreur est survenue. " + '\n' + e1.getMessage());
						dialog.open();
					}
					compositeHeaderForModification(table);

					// on ajoute un menu lorsque l'on fait clique droit sur une ligne
					doMenu(table);
				} else { // si plus rien n'est selectionner on passe selectedChantier a null et on enleve
							// le menu du clic droit et on enleve les boutons pour modifier et supprimer

					selectedSite = null;

					menu.dispose();
					menu = new Menu(siteView.getShell(), SWT.POP_UP);
					table.setMenu(menu);

					compositeSelectionCreation();
				}
			}
		});

		view.pack();

	}

	/**
	 * Add the Table into the composite with all Sites
	 * 
	 * @param <type>int</type>width
	 * @param <type>int</type>height
	 * 
	 * @param <type>composite</type> composite, composite which will contain the
	 *                               table
	 * @return <type> Table </type> table which contains all sites
	 */
	public static Table getTableAllChantier(Composite composite, int width, int height) {
		// creation de la table
		final Table table = new Table(composite, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL | SWT.FULL_SELECTION);
		table.setLayout(new RowLayout());
		table.setLayoutData(new RowData(width, height));
		table.setLinesVisible(true);
		table.setHeaderVisible(true);

		String[] titles = { "Nom", "Adresse" };
		for (String title : titles) {
			TableColumn column = new TableColumn(table, SWT.NONE);
			column.setText(title);
		}

		final TableColumn[] columns = table.getColumns();
		try {
			for (Site c : Site.getAllSite()) {
				// on verifie le status
				if (c.getStatus().getValue().contentEquals("Publié")) {
					TableItem item = new TableItem(table, SWT.NONE);
					item.setText(0, c.getName());
					item.setText(1, c.getAdresse());

				}
			}
		} catch (SQLException e) {

			MessageBox dialog = new MessageBox(composite.getShell(), SWT.ICON_ERROR | SWT.OK);
			dialog.setText("Erreur");
			dialog.setMessage("Une erreur est survenue. " + '\n' + e.getMessage());
			dialog.open();
		}

		for (TableColumn col : columns)
			col.pack();
		return table;
	}

	public void updateTable(Table table) {
		table.removeAll();

		try {
			for (Site c : Site.getAllSite()) {

				if (c.getStatus().getValue().contentEquals("Publié")) {
					TableItem item = new TableItem(table, SWT.NONE);
					item.setText(0, c.getName());
					item.setText(1, c.getAdresse());

				}
			}
		} catch (SQLException e) {

			MessageBox dialog = new MessageBox(siteView.getShell(), SWT.ICON_ERROR | SWT.OK);
			dialog.setText("Erreur");
			dialog.setMessage("Une erreur est survenue. " + '\n' + e.getMessage());
			dialog.open();
		}

	}

	/***
	 * create selection menu
	 * 
	 * @param <type>Table</type>table
	 */
	public void doMenu(Table table) {
		menu = new Menu(siteView.getShell(), SWT.POP_UP);
		table.setMenu(menu);

		// pour modifier
		MenuItem update = new MenuItem(menu, SWT.PUSH);
		update.setText("Modifier l'element");
		update.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				if (table.getSelection().length != 0) {
					modifySiteView();
				}
			}
		});

		MenuItem delete = new MenuItem(menu, SWT.PUSH);
		delete.setText("Supprimer l'element");
		delete.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				try {
					Site c = Site.getSiteById(selectedSite.getSiteId());
					MessageBox dialog = new MessageBox(siteView.getShell(), SWT.ICON_QUESTION | SWT.YES | SWT.NO);
					dialog.setText("Suppression Chantier");
					dialog.setMessage("Voulez-vous supprimer le chantier " + c.getName()
							+ " ?\nToutes les affectations et livraisons liées à ce chantier seront supprimées.");
					int buttonID = dialog.open();
					switch (buttonID) {
					case SWT.YES:
						suppChantier(table);
					}

				} catch (NumberFormatException | SQLException e) {

					MessageBox dialog = new MessageBox(siteView.getShell(), SWT.ICON_ERROR | SWT.OK);
					dialog.setText("Erreur");
					dialog.setMessage("Une erreur est survenue. " + '\n' + e.getMessage());
					dialog.open();
				}
			}
		});

	}

	/**
	 * Getter for the attribute siteView (main composite)
	 * 
	 * @return
	 */
	public Composite getComposite() {
		return this.siteView;
	}

}
