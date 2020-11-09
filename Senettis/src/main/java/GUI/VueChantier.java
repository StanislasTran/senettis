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

import classes.Chantier;

public class VueChantier {

	private Composite parent;
	private Display display;
	private Composite vueChantier;
	private Composite selection;
	private Composite vue;
	private Chantier selectedChantier;
	private Menu menu;

	// Creation VueChantier --------------------------------------------------
	/***
	 * Utilisé depuis Home pour créer une vueChantier
	 * 
	 * @param composite : le composite parent
	 * @param display
	 */
	public VueChantier(Composite composite, Display display) {
		this.parent = composite;
		this.display = display;
		Couleur.setDisplay(display); // pour utiliser les couleurs du fichier couleur

		newVueChantier(parent);
	}

	/***
	 * Pour créer une vueChantier : dispose si une vueChantier existe deja, creer le
	 * composite et lui affecte le layout RowLayout Vertical Appelle ensuite les
	 * fonctions compositeSelectionCreer et vueChantierAfficher
	 * 
	 * @param composite : composite parent
	 */
	public void newVueChantier(Composite composite) {
		if (vueChantier != null) {
			vueChantier.dispose();
		}

		vueChantier = new Composite(composite, SWT.NONE);
		RowLayout rowLayout = new RowLayout();
		rowLayout.type = SWT.VERTICAL;
		vueChantier.setLayout(rowLayout);

		compositeSelectionCreer(vueChantier);
		vueChantierAfficher(vueChantier);

		vue.pack();
		selection.pack();
		vueChantier.pack();
		vueChantier.getParent().pack();
	}

	// Modification de la partie Selection
	// --------------------------------------------------
	/***
	 * Creation de la partie Selection (la partie superieure droite) avec uniquement
	 * le bouton Creer
	 * 
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
				vueChantierCreer();
			}
		});
		selection.pack();
	}

	/***
	 * Creation de la partie Selection (la partie superieure droite) avec les
	 * boutons Creer, Modifier et Supprimer
	 * 
	 * @param composite : composite parent
	 */
	public void compositeSelectionModifier(Composite composite) {
		selection = new Composite(composite, SWT.NONE);
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
					Chantier c = Chantier.getChantierById(selectedChantier.getChantierId());
					MessageBox dialog = new MessageBox(parent.getShell(), SWT.ICON_QUESTION | SWT.YES | SWT.NO);
					dialog.setText("Suppression Chantier");
					dialog.setMessage("Voulez vous supprimer le chantier " + c.getNom() + " ?");
					int buttonID = dialog.open();
					switch (buttonID) {
					case SWT.YES:
						c.setStatus("Archivé");
						c.updateDatabase();
						newVueChantier(parent);
						selectedChantier = null;
					}

				} catch (NumberFormatException | SQLException e) {
					System.out.println("erreur dans la supression");
					MessageBox dialog = new MessageBox(parent.getShell(), SWT.ICON_ERROR | SWT.OK);
					dialog.setText("Erreur Supression");
					dialog.setMessage(
							"Une erreur est survenue lors de la supression du chantier. " + '\n' + e.getMessage());
					dialog.open();

				}

			}
		});
		selection.pack();
	}

	// Modification d'un chantier --------------------------------------------------
	/***
	 * Regroupe les fonctions a appeler pour faire une modification La fonction va
	 * dispose pour vue et selection (les deux composantes de droite) et va appeler
	 * les fonctions titreModification et formulaireModification
	 */
	public void vueChantierModifier() {
		vue.dispose();
		selection.dispose();

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
		selection = new Composite(vueChantier, SWT.CENTER);

		FillLayout fillLayout = new FillLayout();
		fillLayout.type = SWT.VERTICAL;
		fillLayout.marginWidth = 7;
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
		labelNom.setText("Nom* : ");

		final Text textNom = new Text(compositeNom, SWT.BORDER);
		textNom.setText(selectedChantier.getNom());

		// Adresse
		Composite compositeAdresse = new Composite(colonne1, SWT.NONE);
		compositeAdresse.setBackground(Couleur.bleuClair);
		compositeAdresse.setLayout(fillLayoutH5);

		Label labelAdresse = new Label(compositeAdresse, SWT.NONE);
		labelAdresse.setBackground(Couleur.bleuClair);
		labelAdresse.setText("Adresse : ");

		final Text textAdresse = new Text(compositeAdresse, SWT.BORDER);
		textAdresse.setText(selectedChantier.getAdresse());

		// CA
		Composite compositeCA = new Composite(colonne1, SWT.NONE);
		compositeCA.setBackground(Couleur.bleuClair);
		compositeCA.setLayout(fillLayoutH5);

		Label labelCA = new Label(compositeCA, SWT.NONE);
		labelCA.setBackground(Couleur.bleuClair);
		labelCA.setText("Chiffre d'affaire : ");

		final Text textCA = new Text(compositeCA, SWT.BORDER);
		textCA.setText(selectedChantier.getCA().toString());

		// Boutons
		Composite compositeBoutons = new Composite(colonne1, SWT.CENTER);
		compositeBoutons.setBackground(Couleur.bleuClair);
		compositeBoutons.setLayout(fillLayoutH5);

		Button buttonAnnulation = new Button(compositeBoutons, SWT.BACKGROUND);
		buttonAnnulation.setText("Annuler");
		buttonAnnulation.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				newVueChantier(parent);
			}
		});

		Button buttonValidation = new Button(compositeBoutons, SWT.BACKGROUND);
		buttonValidation.setText("Valider");
		buttonValidation.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent arg0) {

				try {
					int id = selectedChantier.getChantierId();
					selectedChantier = new Chantier(id, textNom.getText(), textAdresse.getText(),
							Double.parseDouble(textCA.getText()), "Publié");
					validerModification();
				} catch (Throwable e) {
					e.printStackTrace();
					System.out.println("erreur dans la modif");
					MessageBox dialog = new MessageBox(parent.getShell(), SWT.ICON_ERROR | SWT.OK);
					dialog.setText("Erreur Modification");
					dialog.setMessage(
							"Une erreur est survenue lors de la modification du chantier. " + '\n' + e.getMessage());
					dialog.open();
				}

			}
		});
	}

	/***
	 * modifie la base de données
	 */
	public void validerModification() {
		if (selectedChantier == null) {
			throw new Error("selectedChantier est vide");
		}

		// on insert dans la base de données
		try {
			selectedChantier.updateDatabase();
			System.out.println("on a modifie le chantier !!");
			MessageBox dialog = new MessageBox(parent.getShell(), SWT.ICON_INFORMATION | SWT.OK);
			dialog.setText("Modification réussie");
			dialog.setMessage("Le chantier a bien été modifié dans la base de données.");
			dialog.open();
			selectedChantier = null;
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("erreur dans la modif");
			MessageBox dialog = new MessageBox(parent.getShell(), SWT.ICON_ERROR | SWT.OK);
			dialog.setText("Erreur Modification");
			dialog.setMessage("Une erreur est survenue lors de la modification du chantier. " + e.getMessage());
			dialog.open();
		}

		newVueChantier(parent);
	}

	// Création d'un chantier --------------------------------------------------
	/***
	 * va appeler les fonctions qui modifie la partie vue et la partie selection
	 * afin d'afin le formulaire de création appelle titreCreation et
	 * formulaireCreation
	 */
	public void vueChantierCreer() {
		vue.dispose();
		selection.dispose();

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
		selection = new Composite(vueChantier, SWT.CENTER);

		FillLayout fillLayout = new FillLayout();
		fillLayout.type = SWT.VERTICAL;
		fillLayout.marginWidth = 20;
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
		labelNom.setText("Nom* : ");

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

		// CA
		Composite compositeCA = new Composite(colonne1, SWT.NONE);
		compositeCA.setBackground(Couleur.bleuClair);
		compositeCA.setLayout(fillLayoutH5);

		Label labelCA = new Label(compositeCA, SWT.NONE);
		labelCA.setBackground(Couleur.bleuClair);
		labelCA.setText("Chiffre d'affaire : ");

		final Text textCA = new Text(compositeCA, SWT.BORDER);
		textCA.setText("");

		// Boutons
		Composite compositeBoutons = new Composite(colonne1, SWT.CENTER);
		compositeBoutons.setBackground(Couleur.bleuClair);
		compositeBoutons.setLayout(fillLayoutH5);

		Button buttonAnnulation = new Button(compositeBoutons, SWT.BACKGROUND);
		buttonAnnulation.setText("Annuler");
		buttonAnnulation.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				newVueChantier(parent);
			}
		});

		Button buttonValidation = new Button(compositeBoutons, SWT.BACKGROUND);
		buttonValidation.setText("Valider");
		buttonValidation.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent arg0) {

				try {
					validerCreation(textNom.getText(), textAdresse.getText(), textCA.getText());
				} catch (Throwable e) {
					e.printStackTrace();
					System.out.println("erreur dans la creation");
					MessageBox dialog = new MessageBox(parent.getShell(), SWT.ICON_ERROR | SWT.OK);
					dialog.setText("Erreur Création");
					dialog.setMessage(
							"Une erreur est survenue lors de la création d'un chantier. " + '\n' + e.getMessage());
					dialog.open();
				}
			}
		});
	}

	/***
	 * cree un chantier a partir du formulaire et l'inserer dans la base de donnees
	 */
	public void validerCreation(String textNom, String textAdresse, String textCA) {

		if (textNom.isEmpty()) {
			throw new Error("Merci d'indiquer au moins le nom du chantier.");
		}

		// champs obligatoires
		Chantier chantier = new Chantier(textNom, "Publié");

		// champs optionels
		if (!(textAdresse.isEmpty())) {
			chantier.setAdresse(textAdresse);
		}

		if (textCA != null) {
			if (textCA != "" && textCA.contains(".")) {
				chantier.setCA(Double.parseDouble(textCA));
			} else if (textCA != "" && textCA.matches(".*\\d.*")) {
				chantier.setCA(Double.parseDouble(textCA + ".0"));
			}
		}

		// on insert dans la base de données
		try {
			chantier.insertDatabase();
			System.out.println("on a insere le chantier !!");
			MessageBox dialog = new MessageBox(parent.getShell(), SWT.ICON_INFORMATION | SWT.OK);
			dialog.setText("Création réussie");
			dialog.setMessage("Le chantier a bien été ajouté à la base de données.");
			dialog.open();
			newVueChantier(parent);
			vue.pack();
			selection.pack();
			vueChantier.pack();
		} catch (SQLException e) {
			System.out.println("erreur dans la création");
			MessageBox dialog = new MessageBox(parent.getShell(), SWT.ICON_ERROR | SWT.OK);
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
	public void vueChantierAfficher(Composite composite) {

		RowLayout rowLayoutV = new RowLayout();
		rowLayoutV.type = SWT.VERTICAL;

		vue = new Composite(composite, SWT.NONE);
		vue.setLayout(rowLayoutV);
		vue.setBackground(Couleur.gris);

		Table table = getTableAllChantier(this.vue);

		// on ajoute un listener pour modifier l'interface si l'utilisateur clique sur
		// une ligne
		table.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				if (table.getSelectionIndex() != -1) {

					selection.dispose();
					try {
						selectedChantier = Chantier
								.getChantierById(Integer.parseInt(table.getSelection()[0].getText(3)));
					} catch (NumberFormatException | SQLException e1) {
						System.out.println("erreur pour recuperer le chantier selectionne");
						MessageBox dialog = new MessageBox(parent.getShell(), SWT.ICON_ERROR | SWT.OK);
						dialog.setText("Erreur");
						dialog.setMessage("Une erreur est survenue. " + '\n' + e1.getMessage());
						dialog.open();
					}
					compositeSelectionModifier(vueChantier);

					// on ajoute un menu lorsque l'on fait clique droit sur une ligne
					doMenu(table);
				} else { // si plus rien n'est selectionner on passe selectedChantier a null et on enleve
							// le menu du clic droit et on enleve les boutons pour modifier et supprimer
					System.out.println("-1");

					selectedChantier = null;

					menu.dispose();
					menu = new Menu(composite.getShell(), SWT.POP_UP);
					table.setMenu(menu);

					selection.dispose();
					compositeSelectionCreer(vueChantier);
				}
			}
		});

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
		String[] titles = { "Nom", "Adresse", "Chiffre d'affaire" };
		for (String title : titles) {
			TableColumn column = new TableColumn(table, SWT.NONE);
			column.setText(title);
		}

		// je voulais cacher cette colonne mais ca ne fonctionne pas
		TableColumn column = new TableColumn(table, SWT.NONE);
		column.setText("Id Base de données");
		column.setWidth(0);
		column.setResizable(false);

		// on remplit la table
		final TableColumn[] columns = table.getColumns();
		try {
			for (Chantier c : Chantier.getAllChantier()) {
				// on verifie le status
				if (c.getStatus().contentEquals("Publié")) {
					TableItem item = new TableItem(table, SWT.NONE);
					item.setText(0, c.getNom());
					item.setText(1, c.getAdresse());
					item.setText(2, c.getCA().toString());
					item.setText(3, Integer.toString(c.getChantierId()));
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

	public void doMenu(Table table) {
		menu = new Menu(parent.getShell(), SWT.POP_UP);
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
					Chantier c = Chantier.getChantierById(selectedChantier.getChantierId());
					MessageBox dialog = new MessageBox(parent.getShell(), SWT.ICON_QUESTION | SWT.YES | SWT.NO);
					dialog.setText("Suppression Chantier");
					dialog.setMessage("Voulez-vous supprimer le chantier " + c.getNom() + " ?");
					int buttonID = dialog.open();
					switch (buttonID) {
					case SWT.YES:
						c.setStatus("Archivé");
						c.updateDatabase();
						newVueChantier(parent);
						selectedChantier = null;
					}

				} catch (NumberFormatException | SQLException e) {
					System.out.println("erreur pour supprimer le chantier");
					MessageBox dialog = new MessageBox(parent.getShell(), SWT.ICON_ERROR | SWT.OK);
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
