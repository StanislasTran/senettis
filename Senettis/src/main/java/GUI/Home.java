package GUI;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;

import org.eclipse.swt.*;
import org.eclipse.swt.custom.ScrolledComposite;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

import classes.Site;
import classes.Employee;

public class Home {

	private Display display;
	private Shell shell;
	Rectangle rect;

	RowLayout rowLayoutH;
	RowLayout rowLayoutV;
	FillLayout fillLayoutV;

	ScrolledComposite compositeMain;
	Composite contenuCompositeMain;

	Composite colonneGauche;
	Composite espaceLogo;
	Composite menu;

	Composite colonneDroite;
	Composite contenuColonneDroite;
	Composite currentSelection;
	Composite currentVue;

	/***
	 * Affiche le menu situe en haut a gauche permettant d'obtenir la documentation
	 */
	public void menuBar() {
		// creation du menu Aide
		Menu menuBar = new Menu(shell, SWT.BAR);

		// help
		MenuItem menuHeader = new MenuItem(menuBar, SWT.CASCADE);
		menuHeader.setText("&Aide");

		Menu aideMenu = new Menu(shell, SWT.DROP_DOWN);
		menuHeader.setMenu(aideMenu);

		MenuItem documentationItem = new MenuItem(aideMenu, SWT.PUSH);
		documentationItem.setText("&Obtenir la documentation");
		documentationItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				Desktop desktop = Desktop.getDesktop();
				File file = new File("src/main/resources/Documentation_SenettisDB_v1.docx");
				if (file.exists()) { // checks file exists or not
					try {
						desktop.open(file);
					} catch (IOException e) {

						e.printStackTrace();
					} // opens the specified file
				}
			}
		});

		shell.setMenuBar(menuBar);
	}

	/***
	 * Affiche le compositeMain à l'ouverture de l'app, c'est le composite principal
	 * il regroupe la colonne gauche et la colonne droite
	 * 
	 * @throws SQLException
	 */
	public void compositeMain() throws SQLException {
		compositeMain = new ScrolledComposite(shell,
				SWT.H_SCROLL | SWT.V_SCROLL | SWT.SCROLLBAR_OVERLAY | SWT.SCROLL_PAGE | SWT.SCROLL_LINE);

		compositeMain.setExpandHorizontal(true);
		compositeMain.setExpandVertical(true);
		compositeMain.setAlwaysShowScrollBars(true);

		contenuCompositeMain = new Composite(compositeMain, SWT.NONE);
		/*
		 * System.out.println(getClass().getClassLoader().getResource("test4.png").
		 * getPath()); System.out.println(getClass().getResource("").getPath());
		 * System.out.println(getClass().getClassLoader().getResource(""));
		 */
		// String backgroundLocation =
		// getClass().getClassLoader().getResource("test4.png").getPath();
		InputStream test4 = getClass().getClassLoader().getResourceAsStream("test4.png");
		Image background = new Image(display, test4);

		contenuCompositeMain.setBackgroundImage(background);
		RowLayout rl = new RowLayout();
		rl.spacing = 15; // mets un espace entre le menu et le titre
		contenuCompositeMain.setLayout(rl);

		compositeColonneGauche();
		compositeColonneDroite();

		contenuCompositeMain.pack();
		// contenuCompositeMain.setSize(50, 50);
		// contenuCompositeMain.setSize(rect.width, rect.height);// permet au
		// compositeMain d'avoir la taille de l'ecran

		compositeMain.setContent(contenuCompositeMain);
		// compositeMain.setMinSize(rect.width-25, rect.height-75);//aidez moi rien ne
		// marche :(
		compositeMain.setMinSize(rect.width, rect.height);
		// compositeMain.setLayout(new RowLayout());
		// compositeMain.pack();
		shell.setLayout(new RowLayout());
	}

	/************************
	 * 
	 * Gestion colonne Droite : composée du titre et d'une présentation de
	 * l'application
	 * 
	 ************************/

	public void compositeColonneDroite() throws SQLException {
		colonneDroite = new Composite(contenuCompositeMain, SWT.CENTER);
		this.contenuColonneDroite = new Composite(colonneDroite, SWT.CENTER);
		contenuColonneDroite.setLayout(rowLayoutV);
		contenuColonneDroite.setBackground(MyColor.gris);

		titre();
		presentation();

		contenuColonneDroite.pack();
		colonneDroite.pack();
	}

	public void titre() {
		Composite titre = new Composite(contenuColonneDroite, SWT.CENTER);

		FillLayout fillLayout = new FillLayout();
		fillLayout.type = SWT.VERTICAL;
		fillLayout.marginWidth = 236;
		titre.setLayout(fillLayout);

		// juste pour creer un espace
		Label l1 = new Label(titre, SWT.NONE);
		l1.setText("");
		l1.setBackground(MyColor.bleuFonce);

		titre.setBackground(MyColor.bleuFonce);
		Label HeadLabel = new Label(titre, SWT.TITLE);
		HeadLabel.setText("Bienvenue sur l'application Senettis");
		Font fontTitle = new Font(HeadLabel.getDisplay(), "Arial", 20, SWT.BOLD);
		HeadLabel.setForeground(MyColor.bleuClair);
		HeadLabel.setFont(fontTitle);
		HeadLabel.setBackground(MyColor.bleuFonce);

		// juste pour creer un espace
		Label l2 = new Label(titre, SWT.NONE);
		l2.setText("");
		l2.setBackground(MyColor.bleuFonce);

		titre.pack();
	}

	public void presentation() throws SQLException {

		Composite presentation = new Composite(contenuColonneDroite, SWT.CENTER);
		RowLayout rowLayout = new RowLayout();
		rowLayout.type = SWT.VERTICAL;
		rowLayout.marginWidth = 230;

		presentation.setLayout(rowLayout);
		presentation.setBackground(MyColor.gris);

		Label presLabel = new Label(presentation, SWT.BACKGROUND);
		String pres = "\n \nCette application gère actuellement " + Employee.getCountEmploye() + " employés et "
				+ Site.getSiteCount() + " chantiers." + '\n'
				+ "Elle permet de stocker les informations des employés, \ndes chantiers ainsi que les produits et les livraisons."
				+ '\n' + '\n' + '\n'
				+ "Pour plus d'information, merci de consulter la documentation de \nl'application accessible via le menu Aide.\n \n"
				+ '\n' + "SenettisDB a été développée par Laetitia Courgey et Stanislas Tran." + '\n' + '\n' + '\n'
				+ '\n';
		presLabel.setText(pres);
		// nbEmployeLabel.setText("Nombre d'mployé dans la base :
		// "+Employe.getCountEmploye());
		presLabel.setBackground(MyColor.gris);
		presLabel.setFont(new Font(presLabel.getDisplay(), "Arial", 13, SWT.NONE));

		presentation.pack();
	}

	/************************
	 * 
	 * Gestion colonne Gauche : composée du logo de l'entreprise et du menu
	 * principal
	 * 
	 ************************/

	public void compositeColonneGauche() {
		colonneGauche = new Composite(contenuCompositeMain, SWT.BACKGROUND);
		colonneGauche.setLayout(fillLayoutV);

		compositeLogo();
		compositeMenu();

		colonneGauche.pack();
	}

	public void compositeLogo() {

		Label logo = new Label(colonneGauche, SWT.NONE);

		InputStream moyenLogo = getClass().getClassLoader().getResourceAsStream("moyenLogo.jpg");
		Image image = new Image(display, moyenLogo);
		logo.setImage(image);

	}

	public void compositeMenu() {

		// on ajoute les elements à la colonne menu
		menu = new Composite(colonneGauche, SWT.NONE);
		menu.setLayout(fillLayoutV);

		Button boutonEmploye = new Button(menu, SWT.NONE);
		boutonEmploye.setText("Employés");
		boutonEmploye.setBackground(MyColor.blanc);
		// Image image = new Image(display, "images\\boutonEmploye.png");
		// boutonEmploye.setImage(image);
		boutonEmploye.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				if (!contenuColonneDroite.isDisposed()) {
					contenuColonneDroite.dispose();
				}
				contenuColonneDroite = new EmployeeView(colonneDroite, display).getComposite();
				contenuColonneDroite.pack();
				colonneDroite.pack();

			}
		});

		Button boutonAffectationMAB = new Button(menu, SWT.NONE);

		// boutonAffectation.setImage();
		boutonAffectationMAB.setText("Affectations mise à blanc");

		boutonAffectationMAB.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				if (!contenuColonneDroite.isDisposed()) {
					for (Control c : contenuColonneDroite.getChildren()) {
						if (!c.isDisposed()) {
							c.dispose();
						}
					}
					contenuColonneDroite.dispose();
				}
				try {
					contenuColonneDroite = new MABAssignmentView(getColonneDroite()).getVueAffectation();
				} catch (SQLException e) {
					MessageBox msgBox = new MessageBox(currentVue.getShell(), SWT.ERROR);
					msgBox.setMessage("Erreur Base de donnée");
					msgBox.setText("erreur de liée à la base de données : \n" + e.getMessage());
					msgBox.open();
					e.printStackTrace();
				}

				contenuColonneDroite.pack();
				colonneDroite.pack();
				// System.out.println("done");
			}

		});

		Button boutonAffectationSite = new Button(menu, SWT.NONE);

		// boutonAffectation.setImage();
		boutonAffectationSite.setText("Affectations à un chantier");

		boutonAffectationSite.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				if (!contenuColonneDroite.isDisposed()) {
					for (Control c : contenuColonneDroite.getChildren()) {
						if (!c.isDisposed()) {
							c.dispose();
						}
					}
					contenuColonneDroite.dispose();
				}
				try {
					contenuColonneDroite = new AssignmentSiteView(getColonneDroite()).getVueAffectation();
				} catch (SQLException e) {
					MessageBox msgBox = new MessageBox(currentVue.getShell(), SWT.ERROR);
					msgBox.setMessage("Erreur Base de donnée");
					msgBox.setText("erreur de liée à la base de données : \n" + e.getMessage());
					msgBox.open();
					e.printStackTrace();
				}

				contenuColonneDroite.pack();
				colonneDroite.pack();
				// System.out.println("done");
			}

		});

		Button boutonChantier = new Button(menu, SWT.NONE);
		boutonChantier.setText("Chantiers");

		boutonChantier.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				if (!contenuColonneDroite.isDisposed()) {
					contenuColonneDroite.dispose();
				}
				contenuColonneDroite = new SiteView(colonneDroite, display).getComposite();
				contenuColonneDroite.pack();
				colonneDroite.pack();
			}
		});

		Button boutonLivraison = new Button(menu, SWT.NONE);
		boutonLivraison.setText("Livraison / Fourniture / Matériel");
		boutonLivraison.setBackground(MyColor.blanc);

		boutonLivraison.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				if (!contenuColonneDroite.isDisposed()) {
					contenuColonneDroite.dispose();
				}
				contenuColonneDroite = new DeliveryView(colonneDroite, display).getComposite();
				contenuColonneDroite.pack();
				colonneDroite.pack();
			}
		});

		Button boutonProduit = new Button(menu, SWT.NONE);
		boutonProduit.setText("Produits");
		boutonProduit.setBackground(MyColor.blanc);
		boutonProduit.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				if (!contenuColonneDroite.isDisposed()) {
					for (Control c : contenuColonneDroite.getChildren()) {
						if (!c.isDisposed()) {
							c.dispose();
						}
					}
					contenuColonneDroite.dispose();
				}
				try {
					contenuColonneDroite = new ProductView(getColonneDroite()).getVueProduit();
				} catch (SQLException e) {
					MessageBox msgBox = new MessageBox(currentVue.getShell(), SWT.ERROR);
					msgBox.setMessage("Erreur Base de donnée");
					msgBox.setText("erreur de liée à la base de données : \n" + e.getMessage());
					msgBox.open();
				}

				contenuColonneDroite.pack();
				colonneDroite.pack();
				// System.out.println("done");
			}
		});

		Button boutonComission = new Button(menu, SWT.NONE);
		boutonComission.setText("Comissions");
		boutonComission.setBackground(MyColor.blanc);
		boutonComission.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				if (!contenuColonneDroite.isDisposed()) {
					for (Control c : contenuColonneDroite.getChildren()) {
						if (!c.isDisposed()) {
							c.dispose();
						}
					}
					contenuColonneDroite.dispose();
				}
				try {
					contenuColonneDroite = new ComissionView(getColonneDroite()).getVueProduit();
				} catch (SQLException e) {
					MessageBox msgBox = new MessageBox(currentVue.getShell(), SWT.ERROR);
					msgBox.setMessage("Erreur Base de donnée");
					msgBox.setText("erreur de liée à la base de données : \n" + e.getMessage());
					msgBox.open();
					e.printStackTrace();
				}

				contenuColonneDroite.pack();
				colonneDroite.pack();

			}
		});

		Button buttonCA = new Button(menu, SWT.NONE);
		buttonCA.setText("Chiffre d'Affaire");
		buttonCA.setBackground(MyColor.blanc);
		buttonCA.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				if (!contenuColonneDroite.isDisposed()) {
					for (Control c : contenuColonneDroite.getChildren()) {
						if (!c.isDisposed()) {
							c.dispose();
						}
					}
					contenuColonneDroite.dispose();
				}
				try {
					contenuColonneDroite = new TurnOverView(getColonneDroite()).getRecurringCostView();
				} catch (SQLException e) {
					MessageBox msgBox = new MessageBox(currentVue.getShell(), SWT.ERROR);
					msgBox.setMessage("Erreur Base de donnée");
					msgBox.setText("erreur de liée à la base de données : \n" + e.getMessage());
					msgBox.open();
					e.printStackTrace();
				}

				contenuColonneDroite.pack();
				colonneDroite.pack();

			}
		});

		Button buttonRent = new Button(menu, SWT.NONE);
		buttonRent.setText("Rentabilité");
		buttonRent.setBackground(MyColor.blanc);
		buttonRent.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				if (!contenuColonneDroite.isDisposed()) {
					for (Control c : contenuColonneDroite.getChildren()) {
						if (!c.isDisposed()) {
							c.dispose();
						}
					}
					contenuColonneDroite.dispose();
				}
				try {
					contenuColonneDroite = new RentabilityView(colonneDroite, display).getComposite();
				} catch (SQLException e) {
					MessageBox msgBox = new MessageBox(currentVue.getShell(), SWT.ERROR);
					msgBox.setMessage("Erreur Base de donnée");
					msgBox.setText("erreur de liée à la base de données : \n" + e.getMessage());
					msgBox.open();
				}
				contenuColonneDroite.pack();
				colonneDroite.pack();
			}
		});

		menu.pack();
	}

	private Composite getColonneDroite() {
		return this.colonneDroite;
	}

	public Home() throws SQLException, IOException {
		this.display = new Display();
		this.shell = new Shell(display);
		this.shell.setText("Senettis DB - Gestion de la base de données");

		// Layouts :
		rowLayoutH = new RowLayout();
		rowLayoutH.type = SWT.HORIZONTAL;

		rowLayoutV = new RowLayout();
		rowLayoutV.type = SWT.VERTICAL;

		fillLayoutV = new FillLayout();
		fillLayoutV.type = SWT.VERTICAL;

		//
		Monitor monitor = display.getPrimaryMonitor();

		// rect permet de recuperer la taille de l'ecran
		if (monitor != null) {
			rect = monitor.getClientArea();
		} else {
			rect = display.getBounds();
		}

		menuBar(); // ajoute le menu Aide en haut
		compositeMain(); // creer le composite principal

		
	}
	public void runHome() {
		shell.setMaximized(true);// permet de lancer la fenetre en plein ecran

		this.shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}
}
