package GUI;

import java.sql.SQLException;

import org.eclipse.swt.*;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

import classes.Produit;

public class Home {

	private Display display;
	private Shell shell;
	
	private Color blanc;
	private Color bleuClair;
	private Color bleuFonce;
	
	Composite compositeMain;
	RowLayout rowLayoutH;
	RowLayout rowLayoutV;
	FillLayout fillLayoutV;
	
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
		
		compositeMain.setSize(800, 500);
	}
	
	public void compositeColonneDroite() {
		colonneDroite = new Composite(compositeMain, SWT.NONE);
		//colonneDroite.setSize(700,700); //marche pas
		//colonneDroite.setBackground(blanc);
		colonneDroite.setLayout(rowLayoutV);
		Composite selection = new Composite(colonneDroite, SWT.NONE);
		//selection.setSize(200, 500); //marche pas
		//selection.setBounds(10, 100, 50, 200); //marche pas 
		Composite vue = new Composite(colonneDroite, SWT.NONE);
		selection.setLayout(rowLayoutH);
		selection.setBackground(bleuFonce);
		vue.setLayout(rowLayoutV);
		vue.setBackground(bleuClair);
		
	}
	
	public void compositeColonneGauche() {
		colonneGauche = new Composite(compositeMain, SWT.BACKGROUND);
		colonneGauche.setSize(500, 500);
		colonneGauche.setLayout(fillLayoutV);
		
		colonneGauche.addControlListener(new ControlAdapter() {
			@Override
			public void controlResized(ControlEvent e) {
			//Composite composite = (Composite) e.widget;
			//int width = composite.getBounds().width;
			((GridData) espaceLogo.getLayoutData()).widthHint = (int) (0.66 * 100);
			((GridData) menu.getLayoutData()).widthHint = (int) (0.33 * 100);
			}
		});
		
		compositeLogo();
		compositeMenu();
	}
	
	
	public void compositeLogo() {
		espaceLogo = new Composite(colonneGauche, SWT.NONE);
		espaceLogo.setLayout(rowLayoutV);
		//espaceLogo.setBackground(blanc);

		Label logo = new Label (espaceLogo, SWT.NONE);
		Image image = new Image(display, "C:\\Users\\lcour\\git\\senettis\\Senettis\\images\\petitLogo.jpeg");
		logo.setImage(image);
		//logo.setBackground(blanc);
		//logo.setSize(300, 25);
		//logo.pack();
		
	}
	
	public void compositeMenu() {
		
		Composite espace = new Composite(colonneGauche, SWT.NONE);
		espace.setLayout(rowLayoutV);
		espace.setBounds(25,0,1, 1);
		espace.setBackground(bleuClair);
		
		//on ajoute les elements à la colonne menu 
		menu = new Composite(colonneGauche, SWT.NONE);
		menu.setLayout(fillLayoutV);
		//menu.setBackground(bleuClair);
		
		Button boutonEmploye = new Button(menu, SWT.NONE);
		boutonEmploye.setText("Employés");
		//boutonEmploye.setBounds(10, 60, 100, 20);
		boutonEmploye.setBackground(bleuClair);
		//	boutonEmploye.
		//boutonEmploye.addSelectionListener(new SelectionAdapter() {});
		
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
