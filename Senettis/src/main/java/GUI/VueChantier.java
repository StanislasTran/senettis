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

import classes.AffectationMiseABlanc;
import classes.Delivery;
import classes.Site;
import classes.Status;

public class VueChantier {

	private Composite vueChantier;
	private Composite selection;
	private Composite vue;
	private Site selectedChantier;
	private Menu menu;

	// Creation VueChantier --------------------------------------------------
	/***
	 * Utilisé depuis Home pour créer une vueChantier
	 * 
	 * @param composite : le composite parent
	 * @param display
	 */
	public VueChantier(Composite composite, Display display) {

		Couleur.setDisplay(display); // pour utiliser les couleurs du fichier couleur

		vueChantier = new Composite(composite, SWT.NONE);
		RowLayout rowLayout = new RowLayout();
		rowLayout.type = SWT.VERTICAL;
		vueChantier.setLayout(rowLayout);

		compositeSelectionCreer();
		vueChantierAfficher();

		vueChantier.pack();
		vueChantier.getParent().pack();
	}

	/***
	 * Pour créer une vueChantier : Appelle les fonctions compositeSelectionCreer et
	 * vueChantierAfficher
	 * 
	 */
	public void newVueChantier() {
		compositeSelectionCreer();
		vueChantierAfficher();

		vueChantier.pack();
		vueChantier.getParent().pack();
	}

	// Modification de la partie Selection
	// --------------------------------------------------
	/***
	 * Creation de la partie Selection (la partie superieure droite) avec uniquement
	 * le bouton Creer
	 * 
	 */
	public void compositeSelectionCreer() {
		if (!Objects.isNull(selection) && !selection.isDisposed()) {
			selection.dispose();
		}

		selection = new Composite(vueChantier, SWT.NONE);
		RowLayout rowLayout = new RowLayout();
		rowLayout.marginWidth = 20;
		selection.setLayout(rowLayout);
		selection.setBackground(Couleur.gris);

		Button boutonCreer = new Button(selection, SWT.CENTER);
		boutonCreer.setText("Créer");
		boutonCreer.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				vueChantierCreer();
			}
		});
		selection.pack();
	}

	/***
	 * Creation de la partie Selection (la partie superieure droite) avec les
	 * boutons Creer, Modifier et Supprimer
	 * 
	 * @param table : table contenant tous les chantiers
	 */
	public void compositeSelectionModifier(Table table) {
		if (!Objects.isNull(selection) && !selection.isDisposed()) {
			selection.dispose();
		}

		selection = new Composite(vueChantier, SWT.NONE);
		RowLayout rowLayout = new RowLayout();
		rowLayout.marginWidth = 22;
		rowLayout.marginTop = 6;
		selection.setLayout(rowLayout);
		selection.setBackground(Couleur.gris);

		// Bouton Creer
		Button boutonCreer = new Button(selection, SWT.CENTER);
		boutonCreer.setText("Créer");
		boutonCreer.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				vueChantierCreer();
			}
		});

		// Bouton Modifier
		Button boutonModifier = new Button(selection, SWT.CENTER);
		boutonModifier.setText("Modifier");
		boutonModifier.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				vueChantierModifier();
			}
		});

		// Bouton Supprimer
		Button boutonSupprimer = new Button(selection, SWT.CENTER);
		boutonSupprimer.setText("Supprimer");
		boutonSupprimer.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				try {
					if (selectedChantier == null) {
						throw new Error("selectedChantier est vide");
					}
					Site c = Site.getSiteById(selectedChantier.getSiteId());
					MessageBox dialog = new MessageBox(vueChantier.getShell(), SWT.ICON_QUESTION | SWT.YES | SWT.NO);
					dialog.setText("Suppression Chantier");
					dialog.setMessage("Voulez vous supprimer le chantier " + c.getName()
							+ " ?\nToutes les affectations et livraisons liées à ce chantier seront supprimées.");
					int buttonID = dialog.open();
					switch (buttonID) {
					case SWT.YES:
						suppChantier(table);
					}

				} catch (NumberFormatException | SQLException e) {
					System.out.println("erreur dans la supression");
					MessageBox dialog = new MessageBox(vueChantier.getShell(), SWT.ICON_ERROR | SWT.OK);
					dialog.setText("Erreur Supression");
					dialog.setMessage(
							"Une erreur est survenue lors de la supression du chantier. " + '\n' + e.getMessage());
					dialog.open();

				}

			}
		});
		selection.pack();
	}

	/***
	 * va archiver le chantier selectionne et les affectations et livraisons qui lui
	 * sont liées
	 * 
	 * @param table : table contenant les chantiers
	 * @throws SQLException
	 */
	public void suppChantier(Table table) throws SQLException {
		if (selectedChantier == null) {
			throw new Error("selectedChantier est vide");
		}

		Site c = Site.getSiteById(selectedChantier.getSiteId());
		c.setStatus(Status.ARCHIVED);
		c.updateDatabase();

		for (Delivery d : Delivery.getAllLivraison()) {
			if (d.getIdChantier() == c.getSiteId()) {
				d.setStatus("Archivé");
				d.updateDatabase();
			}
		}

		for (AffectationMiseABlanc a : AffectationMiseABlanc.getAllAffectation()) {
			if (a.getIdChantier() == c.getSiteId()) {
				a.setStatus(Status.ARCHIVED);
				a.updateDatabase();
			}
		}

		selectedChantier = null;

		compositeSelectionCreer();

		updateTable(table);
	}

	// Modification d'un chantier --------------------------------------------------
	/***
	 * Regroupe les fonctions a appeler pour faire une modification La fonction va
	 * appeler les fonctions titreModification et formulaireModification
	 */
	public void vueChantierModifier() {

		formulaireModification();// on le fait une fois d'abord pour recuperer la taille du formulaire et pour
									// creer le titre a la bonne taille
		titreModification();
		formulaireModification();

		vueChantier.pack();
		vueChantier.getParent().pack();

	}

	/***
	 * va modifier la partie selection (partie superieure droite) en y mettant un
	 * titre pour la modification
	 */
	public void titreModification() {
		if (!Objects.isNull(selection) && !selection.isDisposed()) {
			selection.dispose();
		}
		selection = new Composite(vueChantier, SWT.CENTER);

		FillLayout fillLayout = new FillLayout();
		fillLayout.type = SWT.VERTICAL;

		int addSize = vue.getSize().x;
		addSize = (addSize - 200) / 2;// on recupere l'ecrat entre la taille du titre de base (200) et le formulaire
										// de modif
		fillLayout.marginWidth = addSize;
		selection.setLayout(fillLayout);

		// juste pour creer un espace
		Label l1 = new Label(selection, SWT.NONE);
		l1.setText("");
		l1.setBackground(Couleur.bleuFonce);

		selection.setBackground(Couleur.bleuFonce);
		Label HeadLabel = new Label(selection, SWT.TITLE);
		HeadLabel.setText("Modification d'un Chantier");
		Font fontTitle = new Font(HeadLabel.getDisplay(), "Arial", 12, SWT.BOLD);
		HeadLabel.setForeground(Couleur.bleuClair);
		HeadLabel.setFont(fontTitle);
		HeadLabel.setBackground(Couleur.bleuFonce);

		// juste pour creer un espace
		Label l2 = new Label(selection, SWT.NONE);
		l2.setText("");
		l2.setBackground(Couleur.bleuFonce);

		selection.pack();
	}

	/***
	 * Va modifier la partie Vue (partie inferieure droite) et y ajoutant le
	 * formulaire de modification d'un chantier
	 */
	public void formulaireModification() {
		if (!Objects.isNull(vue) && !vue.isDisposed()) {
			vue.dispose();
		}
		if (selectedChantier == null) {
			throw new Error("selectedChantier est vide");
		}

		// creation de la vue
		vue = new Composite(vueChantier, SWT.NONE);
		FillLayout fillLayoutH = new FillLayout();
		fillLayoutH.type = SWT.HORIZONTAL;
		vue.setLayout(fillLayoutH);

		// creation de 3 colonnes afin de repartir les champs du formulaire en trois
		FillLayout fillLayoutV = new FillLayout();
		fillLayoutV.type = SWT.VERTICAL;
		fillLayoutV.marginWidth = 10;
		Composite colonne1 = new Composite(vue, SWT.BORDER);
		colonne1.setBackground(Couleur.bleuClair);
		colonne1.setLayout(fillLayoutV);

		// utiliser pour tous les composites des attributs du formulaire
		FillLayout fillLayoutH5 = new FillLayout();
		fillLayoutH5.marginHeight = 30;
		fillLayoutH5.spacing = 5;
		fillLayoutH5.type = SWT.HORIZONTAL;

		// Nom
		Composite compositeNom = new Composite(colonne1, SWT.NONE);
		compositeNom.setBackground(Couleur.bleuClair);
		compositeNom.setLayout(fillLayoutH5);

		Label labelNom = new Label(compositeNom, SWT.NONE);
		labelNom.setBackground(Couleur.bleuClair);
		labelNom.setText("Nom* :                                                           ");// espaces pour agrandir
																								// le champs texte

		final Text textNom = new Text(compositeNom, SWT.BORDER);
		textNom.setText(selectedChantier.getName());

		// Adresse
		Composite compositeAdresse = new Composite(colonne1, SWT.NONE);
		compositeAdresse.setBackground(Couleur.bleuClair);
		compositeAdresse.setLayout(fillLayoutH5);

		Label labelAdresse = new Label(compositeAdresse, SWT.NONE);
		labelAdresse.setBackground(Couleur.bleuClair);
		labelAdresse.setText("Adresse : ");

		final Text textAdresse = new Text(compositeAdresse, SWT.BORDER);
		textAdresse.setText(selectedChantier.getAdresse());

		// Boutons
		Composite compositeBoutons = new Composite(colonne1, SWT.CENTER);
		compositeBoutons.setBackground(Couleur.bleuClair);
		compositeBoutons.setLayout(fillLayoutH5);

		Button buttonAnnulation = new Button(compositeBoutons, SWT.BACKGROUND);
		buttonAnnulation.setText("Annuler");
		buttonAnnulation.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				newVueChantier();
			}
		});

		Button buttonValidation = new Button(compositeBoutons, SWT.BACKGROUND);
		buttonValidation.setText("Valider");
		buttonValidation.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent arg0) {

				try {
					int id = selectedChantier.getSiteId();
					selectedChantier = new Site(id, textNom.getText(), textAdresse.getText(), Status.PUBLISHED);
					validerModification();
				} catch (Throwable e) {
					e.printStackTrace();
					System.out.println("erreur dans la modif");
					MessageBox dialog = new MessageBox(vueChantier.getShell(), SWT.ICON_ERROR | SWT.OK);
					dialog.setText("Erreur Modification");
					dialog.setMessage(
							"Une erreur est survenue lors de la modification du chantier. " + '\n' + e.getMessage());
					dialog.open();
				}

			}
		});
		vue.pack();
	}

	/***
	 * modifie le chantier selectionne dans la base de données
	 */
	public void validerModification() {
		if (selectedChantier == null) {
			throw new Error("selectedChantier est vide");
		}

		// on insert dans la base de données
		try {
			selectedChantier.updateDatabase();
			System.out.println("on a modifie le chantier !!");
			MessageBox dialog = new MessageBox(vueChantier.getShell(), SWT.ICON_INFORMATION | SWT.OK);
			dialog.setText("Modification réussie");
			dialog.setMessage("Le chantier a bien été modifié dans la base de données.");
			dialog.open();
			selectedChantier = null;
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("erreur dans la modif");
			MessageBox dialog = new MessageBox(vueChantier.getShell(), SWT.ICON_ERROR | SWT.OK);
			dialog.setText("Erreur Modification");
			dialog.setMessage("Une erreur est survenue lors de la modification du chantier. " + e.getMessage());
			dialog.open();
		}

		newVueChantier();
	}

	// Création d'un chantier --------------------------------------------------
	/***
	 * va appeler les fonctions qui modifie la partie vue et la partie selection
	 * afin d'afficher le formulaire de création appelle titreCreation et
	 * formulaireCreation
	 */
	public void vueChantierCreer() {
		titreCreation();
		formulaireCreation();

		vueChantier.pack();
		vueChantier.getParent().pack();

	}

	/***
	 * modifie la partie selection (partie superieur droite) en ajoutant un titre de
	 * creation
	 */
	public void titreCreation() {
		if (!Objects.isNull(selection) && !selection.isDisposed()) {
			selection.dispose();
		}

		selection = new Composite(vueChantier, SWT.CENTER);

		FillLayout fillLayout = new FillLayout();
		fillLayout.type = SWT.VERTICAL;
		fillLayout.marginWidth = 163;
		selection.setLayout(fillLayout);

		// juste pour creer un espace
		Label l1 = new Label(selection, SWT.NONE);
		l1.setText("");
		l1.setBackground(Couleur.bleuFonce);

		selection.setBackground(Couleur.bleuFonce);
		Label HeadLabel = new Label(selection, SWT.TITLE);
		HeadLabel.setText("Creation d'un Chantier");
		Font fontTitle = new Font(HeadLabel.getDisplay(), "Arial", 12, SWT.BOLD);
		HeadLabel.setForeground(Couleur.bleuClair);
		HeadLabel.setFont(fontTitle);
		HeadLabel.setBackground(Couleur.bleuFonce);

		// juste pour creer un espace
		Label l2 = new Label(selection, SWT.NONE);
		l2.setText("");
		l2.setBackground(Couleur.bleuFonce);

		selection.pack();
	}

	/***
	 * cree le formulaire de creation d'un chantier
	 */
	public void formulaireCreation() {
		if (!Objects.isNull(vue) && !vue.isDisposed()) {
			vue.dispose();
		}
		vue = new Composite(vueChantier, SWT.NONE);
		FillLayout fillLayoutH = new FillLayout();
		fillLayoutH.type = SWT.HORIZONTAL;
		vue.setLayout(fillLayoutH);

		// creation de 3 colonnes afin de repartir les champs du formulaire en trois
		FillLayout fillLayoutV = new FillLayout();
		fillLayoutV.type = SWT.VERTICAL;
		fillLayoutV.marginWidth = 10;

		Composite colonne1 = new Composite(vue, SWT.BORDER);
		colonne1.setBackground(Couleur.bleuClair);
		colonne1.setLayout(fillLayoutV);

		// utiliser pour tous les composites des attributs du formulaire
		FillLayout fillLayoutH5 = new FillLayout();
		fillLayoutH5.marginHeight = 30;
		fillLayoutH5.marginWidth = 20;
		fillLayoutH5.spacing = 5;
		fillLayoutH5.type = SWT.HORIZONTAL;

		// Nom
		Composite compositeNom = new Composite(colonne1, SWT.NONE);
		compositeNom.setBackground(Couleur.bleuClair);
		compositeNom.setLayout(fillLayoutH5);

		Label labelNom = new Label(compositeNom, SWT.NONE);
		labelNom.setBackground(Couleur.bleuClair);
		labelNom.setText("Nom* :                                                           "); // j'ai mis des espaces
																								// pour agrandir la
																								// barre de texte

		final Text textNom = new Text(compositeNom, SWT.BORDER);
		textNom.setText("");

		// Adresse
		Composite compositeAdresse = new Composite(colonne1, SWT.NONE);
		compositeAdresse.setBackground(Couleur.bleuClair);
		compositeAdresse.setLayout(fillLayoutH5);

		Label labelAdresse = new Label(compositeAdresse, SWT.NONE);
		labelAdresse.setBackground(Couleur.bleuClair);
		labelAdresse.setText("Adresse : ");

		final Text textAdresse = new Text(compositeAdresse, SWT.BORDER);
		textAdresse.setText("");

		// Boutons
		Composite compositeBoutons = new Composite(colonne1, SWT.CENTER);
		compositeBoutons.setBackground(Couleur.bleuClair);
		compositeBoutons.setLayout(fillLayoutH5);

		Button buttonAnnulation = new Button(compositeBoutons, SWT.BACKGROUND);
		buttonAnnulation.setText("Annuler");
		buttonAnnulation.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				newVueChantier();
			}
		});

		Button buttonValidation = new Button(compositeBoutons, SWT.BACKGROUND);
		buttonValidation.setText("Valider");
		buttonValidation.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent arg0) {

				try {
					validerCreation(textNom.getText(), textAdresse.getText());
				} catch (Throwable e) {
					e.printStackTrace();
					System.out.println("erreur dans la creation");
					MessageBox dialog = new MessageBox(vueChantier.getShell(), SWT.ICON_ERROR | SWT.OK);
					dialog.setText("Erreur Création");
					dialog.setMessage(
							"Une erreur est survenue lors de la création d'un chantier. " + '\n' + e.getMessage());
					dialog.open();
				}
			}
		});
		vue.pack();
	}

	/***
	 * cree un chantier a partir du formulaire et l'insere dans la base de donnees
	 */
	public void validerCreation(String textNom, String textAdresse) {

		if (textNom.isEmpty()) {
			throw new Error("Merci d'indiquer au moins le nom du chantier.");
		}

		// champs obligatoires
		Site chantier = new Site(textNom, Status.PUBLISHED);

		// champs optionels
		if (!(textAdresse.isEmpty())) {
			chantier.setAdress(textAdresse);
		}

		// on insert dans la base de données
		try {
			chantier.insertDatabase();
			System.out.println("on a insere le chantier !!");
			MessageBox dialog = new MessageBox(vueChantier.getShell(), SWT.ICON_INFORMATION | SWT.OK);
			dialog.setText("Création réussie");
			dialog.setMessage("Le chantier a bien été ajouté à la base de données.");
			dialog.open();
			newVueChantier();
			vue.pack();
			selection.pack();
			vueChantier.pack();
		} catch (SQLException e) {
			System.out.println("erreur dans la création");
			MessageBox dialog = new MessageBox(vueChantier.getShell(), SWT.ICON_ERROR | SWT.OK);
			dialog.setText("Erreur Création");
			dialog.setMessage("Une erreur est survenue lors de la création du chantier. " + '\n' + e.getMessage());
			dialog.open();
		}
	}

	// --------------------------------------------------------------------------
	/***
	 * affiche le tableau avec tous les chantiers dans la base de donnees dont le
	 * status est publie
	 */
	public void vueChantierAfficher() {
		if (!Objects.isNull(vue) && !vue.isDisposed()) {
			vue.dispose();
		}

		RowLayout rowLayoutV = new RowLayout();
		rowLayoutV.type = SWT.VERTICAL;

		vue = new Composite(vueChantier, SWT.NONE);
		vue.setLayout(rowLayoutV);
		vue.setBackground(Couleur.gris);

		final Table table = new Table(vue, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL | SWT.FULL_SELECTION);
		table.setLayoutData(new RowData(600, 400));
		table.setLinesVisible(true);
		table.setHeaderVisible(true);

		// on met les noms des colonnes
		String[] titles = { "Nom", "Adresse" };
		for (String title : titles) {
			TableColumn column = new TableColumn(table, SWT.NONE);
			column.setText(title);
		}

		
		// on remplit la table
		final TableColumn[] columns = table.getColumns();
		ArrayList<Integer> listSiteId = new ArrayList<Integer>();

		try {
			for (Site c : Site.getAllChantier()) {
				// on verifie le status
				if (c.getStatus().getValue().contentEquals("Publié")) {
					TableItem item = new TableItem(table, SWT.NONE);
					item.setText(0, c.getName());
					item.setText(1, c.getAdresse());
					listSiteId.add(c.getSiteId());
				}
			}
		} catch (SQLException e) {
			System.out.println("erreur dans la table des chantier");
			MessageBox dialog = new MessageBox(vueChantier.getShell(), SWT.ICON_ERROR | SWT.OK);
			dialog.setText("Erreur");
			dialog.setMessage("Une erreur est survenue. " + '\n' + e.getMessage());
			dialog.open();
		}

		// on pack les colonnes
		for (TableColumn col : columns)
			col.pack();

		// on ajoute un listener pour modifier l'interface si l'utilisateur clique sur
		// une ligne
		table.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				if (table.getSelectionIndex() != -1) {

					try {
						selectedChantier = Site.getSiteById(listSiteId.get(table.getSelectionIndex()));
					} catch (NumberFormatException | SQLException e1) {
						System.out.println("erreur pour recuperer le chantier selectionne");
						MessageBox dialog = new MessageBox(vueChantier.getShell(), SWT.ICON_ERROR | SWT.OK);
						dialog.setText("Erreur");
						dialog.setMessage("Une erreur est survenue. " + '\n' + e1.getMessage());
						dialog.open();
					}
					compositeSelectionModifier(table);

					// on ajoute un menu lorsque l'on fait clique droit sur une ligne
					doMenu(table);
				} else { // si plus rien n'est selectionner on passe selectedChantier a null et on enleve
							// le menu du clic droit et on enleve les boutons pour modifier et supprimer

					selectedChantier = null;

					menu.dispose();
					menu = new Menu(vueChantier.getShell(), SWT.POP_UP);
					table.setMenu(menu);

					compositeSelectionCreer();
				}
			}
		});

		vue.pack();

	}

	/**
	 * Ajoute un table contenant la liste de tous les chantier au composité entré en
	 * paramètre
	 * 
	 * @param <type>composite</type> composite
	 * @return <type> Table </type> table
	 */
	public static Table getTableAllChantier(Composite composite) {
		// creation de la table
		final Table table = new Table(composite, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL | SWT.FULL_SELECTION);
		table.setLayoutData(new RowData(400, 400));
		table.setLinesVisible(true);
		table.setHeaderVisible(true);

		// on met les noms des colonnes
		String[] titles = { "Nom", "Adresse" };
		for (String title : titles) {
			TableColumn column = new TableColumn(table, SWT.NONE);
			column.setText(title);
		}

		// on remplit la table
		final TableColumn[] columns = table.getColumns();
		try {
			for (Site c : Site.getAllChantier()) {
				// on verifie le status
				if (c.getStatus().getValue().contentEquals("Publié")) {
					TableItem item = new TableItem(table, SWT.NONE);
					item.setText(0, c.getName());
					item.setText(1, c.getAdresse());

				}
			}
		} catch (SQLException e) {
			System.out.println("erreur dans la table des chantier");
			MessageBox dialog = new MessageBox(composite.getShell(), SWT.ICON_ERROR | SWT.OK);
			dialog.setText("Erreur");
			dialog.setMessage("Une erreur est survenue. " + '\n' + e.getMessage());
			dialog.open();
		}

		// on pack les colonnes
		for (TableColumn col : columns)
			col.pack();
		return table;
	}

	public void updateTable(Table table) {
		table.removeAll();

		try {
			for (Site c : Site.getAllChantier()) {
				// on verifie le status
				if (c.getStatus().getValue().contentEquals("Publié")) {
					TableItem item = new TableItem(table, SWT.NONE);
					item.setText(0, c.getName());
					item.setText(1, c.getAdresse());

				}
			}
		} catch (SQLException e) {
			System.out.println("erreur dans la table des chantier");
			MessageBox dialog = new MessageBox(vueChantier.getShell(), SWT.ICON_ERROR | SWT.OK);
			dialog.setText("Erreur");
			dialog.setMessage("Une erreur est survenue. " + '\n' + e.getMessage());
			dialog.open();
		}

	}

	/***
	 * cree un menu sur la selection de la table des chantiers lors d'un clic droit
	 * 
	 * @param table
	 */
	public void doMenu(Table table) {
		menu = new Menu(vueChantier.getShell(), SWT.POP_UP);
		table.setMenu(menu);

		// pour modifier
		MenuItem update = new MenuItem(menu, SWT.PUSH);
		update.setText("Modifier l'element");
		update.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				if (table.getSelection().length != 0) {
					vueChantierModifier();
				}
			}
		});

		// pour supprimer
		MenuItem delete = new MenuItem(menu, SWT.PUSH);
		delete.setText("Supprimer l'element");
		delete.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				try {
					Site c = Site.getSiteById(selectedChantier.getSiteId());
					MessageBox dialog = new MessageBox(vueChantier.getShell(), SWT.ICON_QUESTION | SWT.YES | SWT.NO);
					dialog.setText("Suppression Chantier");
					dialog.setMessage("Voulez-vous supprimer le chantier " + c.getName()
							+ " ?\nToutes les affectations et livraisons liées à ce chantier seront supprimées.");
					int buttonID = dialog.open();
					switch (buttonID) {
					case SWT.YES:
						suppChantier(table);
					}

				} catch (NumberFormatException | SQLException e) {
					System.out.println("erreur pour supprimer le chantier");
					MessageBox dialog = new MessageBox(vueChantier.getShell(), SWT.ICON_ERROR | SWT.OK);
					dialog.setText("Erreur");
					dialog.setMessage("Une erreur est survenue. " + '\n' + e.getMessage());
					dialog.open();
				}
			}
		});

	}

	public Composite getComposite() {
		return this.vueChantier;
	}

}
