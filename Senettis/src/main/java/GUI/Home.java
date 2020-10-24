package GUI;

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
	
	Composite compositeMain;
	RowLayout rowLayoutH;
	RowLayout rowLayoutV;
	FillLayout fillLayoutV;
	GridLayout gridLayout;
	
	Composite colonneGauche;
	Composite espaceLogo;
	Composite menu;
	
	Composite colonneDroite;

	public void menuBar() {
		//creation du menu Aide
		Menu menuBar = new Menu(shell, SWT.BAR);
		MenuItem menuHeader = new MenuItem(menuBar, SWT.CASCADE);
		menuHeader.setText("&Aide");

	    Menu aideMenu = new Menu(shell, SWT.DROP_DOWN);
	    menuHeader.setMenu(aideMenu);

	    MenuItem documentationItem = new MenuItem(aideMenu, SWT.PUSH);
	    documentationItem.setText("&Obtenir la documentation");
	    
	    shell.setMenuBar(menuBar);
	}
	
	public void compositeMain() {
		compositeMain = new Composite(shell, SWT.NONE);
		compositeMain.setBackground(bleuClair);
		compositeMain.setLayout(rowLayoutH);
		
		compositeColonneGauche();
		compositeColonneDroite();
		
		compositeMain.setSize(rect.width, rect.height);
	}
	
	public void compositeColonneDroite() {
		colonneDroite = new Composite(compositeMain, SWT.CENTER);
		//colonneDroite.setSize(700,700); //marche pas
		//colonneDroite.setBackground(blanc);
		colonneDroite.setLayout(rowLayoutV);
		colonneDroite.setSize(500,500);
		
		compositeSelection();
		compositeVue();
		
	}
	
	
	public void compositeSelection() {
		Composite selection = new Composite(colonneDroite, SWT.CENTER);
		selection.setLayout(rowLayoutH);
		selection.setBackground(bleuFonce);
		//selection.setSize(200, 500); //marche pas
		//selection.setBounds(10, 100, 50, 200); //marche pas 
		
		Button boutonCreer = new Button(selection, SWT.CENTER);
		boutonCreer.setText("Créer");
		boutonCreer.setBackground(bleuFonce);
		//boutonCreer.setBounds(10, 60, 100, 20);
		//boutonCreer.addSelectionListener(new SelectionAdapter() {});
		
		Button boutonRechercher = new Button(selection, SWT.CENTER);
		boutonRechercher.setText("Rechercher");
		boutonRechercher.setBackground(bleuFonce);
		//boutonRechercher.setBounds(10, 60, 100, 20);
		//boutonRechercher.addSelectionListener(new SelectionAdapter() {});
	}
	
	
	public void compositeVue() {

		VueEmploye.vueEmployeAfficher(colonneDroite);
		
		
	}
	
	public void compositeColonneGauche() {
		colonneGauche = new Composite(compositeMain, SWT.BACKGROUND);
		colonneGauche.setSize(500, 500);
		colonneGauche.setLayout(fillLayoutV);
		
		/*
		 * colonneGauche.addControlListener(new ControlAdapter() {
		 * 
		 * @Override public void controlResized(ControlEvent e) { //Composite composite
		 * = (Composite) e.widget; //int width = composite.getBounds().width;
		 * ((GridData) espaceLogo.getLayoutData()).widthHint = (int) (0.66 * 10000);
		 * ((GridData) menu.getLayoutData()).widthHint = (int) (0.33 * 100); } });
		 */
		
		compositeLogo();
		compositeMenu();
	}
	
	
	public void compositeLogo() {
		espaceLogo = new Composite(colonneGauche, SWT.NONE);
		espaceLogo.setLayout(fillLayoutV);
		//fillLayoutV.spacing = 5;
		//espaceLogo.setBackground(bleuClair);

		Label logo = new Label (espaceLogo, SWT.NONE);
		Image image = new Image(display, "images\\petitLogo.jpeg");
		logo.setImage(image);
		//logo.setBackground(blanc);
		//logo.setSize(300, 25);
		//logo.pack();
		
	}
	
	public void compositeMenu() {
		
		
		
		//on ajoute les elements à la colonne menu 
		menu = new Composite(colonneGauche, SWT.NONE);
		menu.setLayout(fillLayoutV);
		//menu.setBackground(bleuClair);
		
		
		//Composite espace = new Composite(menu, SWT.NONE);
		//espace.setLayout(rowLayoutV); 
		//espace.setBounds(25,0,1, 1);
		//espace.setBackground(bleuClair);
		 
		
		Button boutonEmploye = new Button(menu, SWT.NONE);
		boutonEmploye.setText("Employés");
		//boutonEmploye.setBounds(10, 60, 100, 20);
		boutonEmploye.setBackground(bleuClair);
		//	boutonEmploye.
		boutonEmploye.addSelectionListener(new SelectionAdapter() {
			
			@Override
			public void widgetSelected(SelectionEvent arg0) {

				VueEmploye.vueEmployeAfficher(colonneDroite);

			}
			
		});
		
		Button boutonChantier = new Button(menu, SWT.NONE);
		boutonChantier.setText("Chantiers");
		boutonChantier.setBackground(bleuClair);
		//boutonChantier.setBounds(10, 60, 100, 20);
		//boutonChantier.addSelectionListener(new SelectionAdapter() {});
		
		Button boutonAffectation = new Button(menu, SWT.NONE);
		boutonAffectation.setText("Affectations");
		boutonAffectation.setBackground(bleuClair);
		//boutonAffectation.setBounds(10, 60, 100, 20);
		//boutonAffectation.addSelectionListener(new SelectionAdapter() {});
		
		Button boutonProduit = new Button(menu, SWT.NONE);
		boutonProduit.setText("Produits");
		boutonProduit.setBackground(bleuClair);
		//boutonProduit.setBounds(10, 60, 100, 20);
		//boutonProduit.addSelectionListener(new SelectionAdapter() {});
		
		Button boutonLivraison = new Button(menu, SWT.NONE);
		boutonLivraison.setText("Livraisons");
		boutonLivraison.setBackground(bleuClair);
		//boutonLivraison.setBounds(10, 60, 100, 25);
		//boutonLivraison.addSelectionListener(new SelectionAdapter() {});
		
	}
	
	public Home() {
		this.display = new Display();
		this.shell = new Shell(display);
		this.shell.setText("Senettis DB - Gestion de la base de données");

		blanc = new Color(display, 254, 254, 254);
		bleuClair = new Color(display, 31, 177, 253);
		bleuFonce = new Color(display, 1, 88, 144);
		
		rowLayoutH = new RowLayout();
		rowLayoutH.type = SWT.HORIZONTAL;
		
	    rowLayoutV = new RowLayout();
	    rowLayoutV.type = SWT.VERTICAL;
	    
	    fillLayoutV = new FillLayout();
		fillLayoutV.type = SWT.VERTICAL;
		
		gridLayout = new GridLayout();
		
		Monitor monitor = display.getPrimaryMonitor();
		if (monitor != null) {
			rect = monitor.getClientArea();
		} else {
			rect = display.getBounds();
		}
		
		menuBar();
		compositeMain();
		
		this.shell.pack();
		this.shell.open();

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}

		display.dispose();
	}
}
