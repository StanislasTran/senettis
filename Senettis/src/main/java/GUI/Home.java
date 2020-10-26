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
	Composite contenuColonneDroite;
	
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
	
	public void compositeMain() throws SQLException {
		compositeMain = new Composite(shell, SWT.NONE);
		compositeMain.setBackground(bleuClair);
		compositeMain.setLayout(rowLayoutH);
		
		compositeColonneGauche();
		
		compositeColonneDroite();
		
		
		
		compositeMain.pack();

		
	}
	
	
	
	/************************
	 * 
	 * Gestion colonne Droite
	 * 
	 ************************/
	
	
	
	public void compositeColonneDroite() throws SQLException {
		colonneDroite = new Composite(compositeMain, SWT.CENTER);
		
		this.contenuColonneDroite=new Composite(colonneDroite,SWT.CENTER);
		contenuColonneDroite.setLayout(rowLayoutV);
		
		Color lightCyan=new Color( display,204,255,255);
		rowLayoutV.marginWidth=150;
		
		contenuColonneDroite.setBackground(lightCyan);
		//compositeSelection();
		//compositeVue();
		Label HeadLabel =new Label(contenuColonneDroite,SWT.TITLE);
		HeadLabel.setText("Bienvenu sur l'application Senettis \n \n");
		HeadLabel.setFont( new Font(HeadLabel.getDisplay(), "Arial", 12, SWT.BOLD) );
		HeadLabel.setBackground(lightCyan);
		Label nbEmployeLabel = new Label(contenuColonneDroite, SWT.BACKGROUND);
		nbEmployeLabel.setText("Nombre d'mployé dans la base : "+ Employe.getCountEmploye());
		nbEmployeLabel.setBackground(lightCyan);
		
		contenuColonneDroite.pack();
		colonneDroite.pack();
		
		
		
	}
	
	/*
	 * CEtte partie doit être mise dans les différentes Vues car les actions sur les bouttons dépendent des vues
	
	public void compositeSelection() {
		Composite selection = new Composite(colonneDroite, SWT.CENTER);
		selection.setLayout(rowLayoutH);
		selection.setBackground(bleuFonce);
		
		
		Button boutonCreer = new Button(selection, SWT.CENTER);
		boutonCreer.setText("Créer");
		boutonCreer.setBackground(bleuFonce);
		
		Button boutonRechercher = new Button(selection, SWT.CENTER);
		boutonRechercher.setText("Rechercher");
		boutonRechercher.setBackground(bleuFonce);
		
	}
	*/
	
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
		
		
		boutonProduit.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				contenuColonneDroite.dispose();
				contenuColonneDroite=new VueProduit(getColonneDroite()).getComposite();
				
			
				contenuColonneDroite.pack();
				colonneDroite.pack();
				System.out.println("done");

			}

			

		});
		
		
		//boutonProduit.setBounds(10, 60, 100, 20);
		//boutonProduit.addSelectionListener(new SelectionAdapter() {});
		
		Button boutonLivraison = new Button(menu, SWT.NONE);
		boutonLivraison.setText("Livraisons");
		boutonLivraison.setBackground(bleuClair);
		//boutonLivraison.setBounds(10, 60, 100, 25);
		//boutonLivraison.addSelectionListener(new SelectionAdapter() {});
		
	}
	private Composite getColonneDroite() {
		
		return this.colonneDroite;
	}
	public Home() throws SQLException {
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
		
		this.shell.setSize(1000,1000);
		this.shell.open();

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}

		display.dispose();
	}
}
