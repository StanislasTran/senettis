package GUI;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import org.eclipse.swt.*;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

import classes.Employe;
import classes.Produit;

public class Home {

	private Display display;
	private Shell shell;
	Rectangle rect;

	private Color blanc;
	private Color bleuClair;
	private Color bleuFonce;
	private Color lightCyan;

	RowLayout rowLayoutH;
	RowLayout rowLayoutV;
	FillLayout fillLayoutV;

	Composite compositeMain;

	Composite colonneGauche;
	Composite espaceLogo;
	Composite menu;

	Composite colonneDroite;
	Composite contenuColonneDroite;

	public void menuBar() {
		// creation du menu Aide
		Menu menuBar = new Menu(shell, SWT.BAR);
		MenuItem menuHeader = new MenuItem(menuBar, SWT.CASCADE);
		menuHeader.setText("&Aide");

		Menu aideMenu = new Menu(shell, SWT.DROP_DOWN);
		menuHeader.setMenu(aideMenu);

		MenuItem documentationItem = new MenuItem(aideMenu, SWT.PUSH);
		documentationItem.setText("&Obtenir la documentation");

		shell.setMenuBar(menuBar);
	}

	public void compositeMain() throws SQLException {
		compositeMain = new Composite(shell, SWT.NONE);
		// compositeMain.setBackground(bleuClair);

		String file_name = "mainWallPaper.jpg";
		String backgroundLocation = this.getClass().getClassLoader().getResource("test4.png").getPath();

		Image background = new Image(display, backgroundLocation);
		compositeMain.setBackgroundImage(background);
		compositeMain.setLayout(rowLayoutH);

		compositeColonneGauche();
		compositeColonneDroite();

		// compositeMain.pack();
		compositeMain.setSize(rect.width, rect.height);// permet au compositeMain d'avoir la taille de l'ecran
	}

	/************************
	 * 
	 * Gestion colonne Droite
	 * 
	 ************************/

	public void compositeColonneDroite() throws SQLException {
		colonneDroite = new Composite(compositeMain, SWT.CENTER);

		this.contenuColonneDroite = new Composite(colonneDroite, SWT.CENTER);
		contenuColonneDroite.setLayout(rowLayoutV);

		rowLayoutV.marginWidth = 441;

		// contenuColonneDroite.setBackground(lightCyan);
		Label HeadLabel = new Label(contenuColonneDroite, SWT.TITLE);
		HeadLabel.setText("Bienvenu sur l'application Senettis \n \n");
		HeadLabel.setFont(new Font(HeadLabel.getDisplay(), "Arial", 12, SWT.BOLD));
		HeadLabel.setBackground(lightCyan);

		Label nbEmployeLabel = new Label(contenuColonneDroite, SWT.BACKGROUND);
		// nbEmployeLabel.setText("Nombre d'mployé dans la base : "+
		// Employe.getCountEmploye());
		// nbEmployeLabel.setBackground(lightCyan);

		contenuColonneDroite.pack();
		colonneDroite.pack();
	}

	/************************
	 * 
	 * Gestion colonne Gauche
	 * 
	 ************************/

	public void compositeColonneGauche() {
		colonneGauche = new Composite(compositeMain, SWT.BACKGROUND);
		colonneGauche.setLayout(fillLayoutV);

		compositeLogo();
		compositeMenu();
	}

	public void compositeLogo() {
		espaceLogo = new Composite(colonneGauche, SWT.NONE);
		espaceLogo.setLayout(fillLayoutV);

		Label logo = new Label(espaceLogo, SWT.NONE);
		Image image = new Image(display, "images\\petitLogo.jpeg");
		logo.setImage(image);
	}

	public void compositeMenu() {
		// on ajoute les elements à la colonne menu
		menu = new Composite(colonneGauche, SWT.NONE);
		menu.setLayout(fillLayoutV);

		Button boutonEmploye = new Button(menu, SWT.NONE);
		boutonEmploye.setText("Employés");
		boutonEmploye.setBackground(bleuClair);
		boutonEmploye.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				contenuColonneDroite.dispose();
				contenuColonneDroite = new VueEmploye(colonneDroite, display).getComposite();
				contenuColonneDroite.pack();
				colonneDroite.pack();
			}
		});

		Button boutonChantier = new Button(menu, SWT.NONE);
		boutonChantier.setText("Chantiers");
		boutonChantier.setBackground(bleuClair);
		// boutonChantier.addSelectionListener(new SelectionAdapter() {});

		Button boutonAffectation = new Button(menu, SWT.NONE);
		boutonAffectation.setText("Affectations");
		boutonAffectation.setBackground(bleuClair);
		boutonAffectation.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				contenuColonneDroite.dispose();
				try {
					contenuColonneDroite = new VueAffectation(getColonneDroite()).getVueAffectation();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				contenuColonneDroite.pack();
				colonneDroite.pack();
				System.out.println("done");
			}

		});

		Button boutonProduit = new Button(menu, SWT.NONE);
		boutonProduit.setText("Produits");
		boutonProduit.setBackground(bleuClair);
		boutonProduit.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				contenuColonneDroite.dispose();
				try {
					contenuColonneDroite = new VueProduit(getColonneDroite()).getVueProduit();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				contenuColonneDroite.pack();
				colonneDroite.pack();
				System.out.println("done");
			}
		});

		Button boutonLivraison = new Button(menu, SWT.NONE);
		boutonLivraison.setText("Livraisons");
		boutonLivraison.setBackground(bleuClair);
		// boutonLivraison.addSelectionListener(new SelectionAdapter() {});

	}

	private Composite getColonneDroite() {
		return this.colonneDroite;
	}

	public Home() throws SQLException, IOException {
		this.display = new Display();
		this.shell = new Shell(display);
		this.shell.setText("Senettis DB - Gestion de la base de données");

		// Couleurs :
		blanc = new Color(display, 254, 254, 254);
		// bleuClair = new Color(display, 31, 177, 253);
		bleuClair = new Color(display, 213, 234, 253);
		bleuFonce = new Color(display, 1, 88, 144);
		// lightCyan = new Color(display, 114, 112, 170); //-> bleu fonce
		lightCyan = new Color(display, 240, 240, 240);// gris
		// lightCyan = new Color(display,204,255,255);

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

		shell.setMaximized(true);// permet de lancer la fenetre en plein ecran

		this.shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}
}
