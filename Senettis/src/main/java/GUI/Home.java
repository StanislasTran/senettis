package GUI;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.JFileChooser;

import org.eclipse.swt.*;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.TouchEvent;
import org.eclipse.swt.events.TouchListener;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

import classes.Site;
import classes.Employee;
import classes.Product;

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
		
		//employee
		MenuItem menuHeaderEmployee = new MenuItem(menuBar, SWT.CASCADE);
		menuHeaderEmployee.setText("&Employés");
		Menu employeeMenu = new Menu(shell, SWT.DROP_DOWN);
		menuHeaderEmployee.setMenu(employeeMenu);

		MenuItem employeeItem = new MenuItem(employeeMenu, SWT.CASCADE);
		employeeItem.setText("&Gérer les employés");
		
		final Menu subMenuEmployee = new Menu(shell, SWT.DROP_DOWN);
		employeeItem.setMenu(subMenuEmployee);
		
		MenuItem employeeCreation = new MenuItem(subMenuEmployee, SWT.PUSH);
		employeeCreation.setText("&Créer un employé");
		employeeCreation.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				if (!contenuColonneDroite.isDisposed()) {
					contenuColonneDroite.dispose();
				}
				contenuColonneDroite = new VueEmploye(colonneDroite,display).getComposite();
				VueEmploye.titreEmploye();
				VueEmploye.vueEmployeFormulaire();
				contenuColonneDroite.pack();
				colonneDroite.pack();
			}
		});
		MenuItem employeeTable = new MenuItem(subMenuEmployee, SWT.PUSH);
		employeeTable.setText("&Afficher les employés");
		employeeTable.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				if (!contenuColonneDroite.isDisposed()) {
					contenuColonneDroite.dispose();
				}
				contenuColonneDroite = new VueEmploye(colonneDroite,display).getComposite();
				contenuColonneDroite.pack();
				colonneDroite.pack();
			}
		});
		MenuItem employeeGlobalCost = new MenuItem(subMenuEmployee, SWT.PUSH);
		employeeGlobalCost.setText("&Récupérer un ancien employé");
		employeeGlobalCost.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				if (!contenuColonneDroite.isDisposed()) {
					contenuColonneDroite.dispose();
				}
				contenuColonneDroite = new VueEmploye(colonneDroite,display).getComposite();
				VueEmploye.vueRecupEmploye();
				contenuColonneDroite.pack();
				colonneDroite.pack();
			}
		});
		
		//couts
		MenuItem SLItem = new MenuItem(employeeMenu, SWT.CASCADE);
		SLItem.setText("&Gérer les coûts");

		final Menu subMenuSickLeave = new Menu(shell, SWT.DROP_DOWN);
		SLItem.setMenu(subMenuSickLeave);
		
		MenuItem sickLeaveCreation = new MenuItem(subMenuSickLeave, SWT.PUSH);
		sickLeaveCreation.setText("&Modifier les coûts des employés");
		sickLeaveCreation.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				if (!contenuColonneDroite.isDisposed()) {
					contenuColonneDroite.dispose();
				}
				contenuColonneDroite = new VueEmploye(colonneDroite,display).getComposite();
				VueEmploye.vueCompleteCouts();
				contenuColonneDroite.pack();
				colonneDroite.pack();
			}
		});
		MenuItem sickLeaveTable = new MenuItem(subMenuSickLeave, SWT.PUSH);
		sickLeaveTable.setText("&Afficher les couts à amortir");
		sickLeaveTable.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				if (!contenuColonneDroite.isDisposed()) {
					contenuColonneDroite.dispose();
				}
				contenuColonneDroite = new VueEmploye(colonneDroite,display).getComposite();
				VueEmploye.vueAmortissement();
				contenuColonneDroite.pack();
				colonneDroite.pack();
			}
		});
		
		MenuItem sickLeaveTable2 = new MenuItem(subMenuSickLeave, SWT.PUSH);
		sickLeaveTable2.setText("&Créer un cout à amortir");
		sickLeaveTable2.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				if (!contenuColonneDroite.isDisposed()) {
					contenuColonneDroite.dispose();
				}
				contenuColonneDroite = new VueEmploye(colonneDroite,display).getComposite();
				VueEmploye.vueCreationA();
				contenuColonneDroite.pack();
				colonneDroite.pack();
			}
		});
		
		//help
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
				if(file.exists())   {      //checks file exists or not  
					try {
						desktop.open(file);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}              //opens the specified file  
				}  
			}
		});

		shell.setMenuBar(menuBar);
	}


	/***
	 * Affiche le compositeMain à l'ouverture de l'app, c'est le composite principal il regroupe la colonne gauche et la colonne droite
	 * @throws SQLException
	 */
	public void compositeMain() throws SQLException {
		compositeMain = new ScrolledComposite(shell, SWT.H_SCROLL
				| SWT.V_SCROLL|SWT.SCROLLBAR_OVERLAY|SWT.SCROLL_PAGE|SWT.SCROLL_LINE);
		
		compositeMain.setExpandHorizontal(true);
		compositeMain.setExpandVertical(true);
		compositeMain.setAlwaysShowScrollBars(true);
		
		contenuCompositeMain = new Composite(compositeMain, SWT.NONE);
			
		String backgroundLocation=this.getClass().getClassLoader().getResource("test4.png").getPath();

		Image background=new Image(display,backgroundLocation);

		contenuCompositeMain.setBackgroundImage(background);
		RowLayout rl = new RowLayout();
		rl.spacing = 15; //mets un espace entre le menu et le titre 
		contenuCompositeMain.setLayout(rl);

		compositeColonneGauche();
		compositeColonneDroite();
		
		contenuCompositeMain.pack();
		//contenuCompositeMain.setSize(50, 50);
		//contenuCompositeMain.setSize(rect.width, rect.height);// permet au compositeMain d'avoir la taille de l'ecran
		
		compositeMain.setContent(contenuCompositeMain);
		//compositeMain.setMinSize(rect.width-25, rect.height-75);//aidez moi rien ne marche :(
		compositeMain.setMinSize(rect.width, rect.height);
		//compositeMain.setLayout(new RowLayout());
		//compositeMain.pack();
		shell.setLayout(new RowLayout());
	}


	/************************
	 * 
	 * Gestion colonne Droite : composée du titre et d'une présentation de l'application
	 * 
	 ************************/

	public void compositeColonneDroite() throws SQLException {
		colonneDroite = new Composite(contenuCompositeMain, SWT.CENTER);
		this.contenuColonneDroite = new Composite(colonneDroite, SWT.CENTER);
		contenuColonneDroite.setLayout(rowLayoutV);
		contenuColonneDroite.setBackground(Couleur.gris);

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

		//juste pour creer un espace 
		Label l1 = new Label(titre, SWT.NONE);
		l1.setText("");
		l1.setBackground(Couleur.bleuFonce);

		titre.setBackground(Couleur.bleuFonce);
		Label HeadLabel =new Label(titre,SWT.TITLE);
		HeadLabel.setText("Bienvenue sur l'application Senettis");
		Font fontTitle = new Font(HeadLabel.getDisplay(), "Arial", 20, SWT.BOLD);
		HeadLabel.setForeground(Couleur.bleuClair);
		HeadLabel.setFont(fontTitle);
		HeadLabel.setBackground(Couleur.bleuFonce);

		//juste pour creer un espace 
		Label l2 = new Label(titre, SWT.NONE);
		l2.setText("");
		l2.setBackground(Couleur.bleuFonce);

		titre.pack();
	}

	public void presentation () throws SQLException {

		Composite presentation = new Composite(contenuColonneDroite, SWT.CENTER);
		RowLayout rowLayout = new RowLayout();
		rowLayout.type = SWT.VERTICAL;
		rowLayout.marginWidth = 230;

		presentation.setLayout(rowLayout);
		presentation.setBackground(Couleur.gris);

		Label presLabel = new Label(presentation, SWT.BACKGROUND);
		String pres = "\n \nCette application gère actuellement "+Employee.getCountEmploye()+" employés et "+Site.getCountChantier()+" chantiers."+
				'\n'+"Elle permet de stocker les informations des employés, \ndes chantiers ainsi que les produits et les livraisons."+'\n'+'\n'+'\n'
				+"Pour plus d'information, merci de consulter la documentation de \nl'application accessible via le menu Aide.\n \n"+'\n'
				+"SenettisDB a été développé par Laetitia Courgey et Stanislas Tran."+'\n'+'\n'+'\n'+'\n';
		presLabel.setText(pres);
		// nbEmployeLabel.setText("Nombre d'mployé dans la base : "+Employe.getCountEmploye());
		presLabel.setBackground(Couleur.gris);
		presLabel.setFont(new Font(presLabel.getDisplay(), "Arial", 13, SWT.NONE));

		presentation.pack();
	}

	/************************
	 * 
	 * Gestion colonne Gauche : composée du logo de l'entreprise et du menu principal
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
		Image image = new Image(display, "images\\moyenLogo.jpg");
		logo.setImage(image);

		//TODO on peut essayer de remettre le fait que en cliquant sur le logo on revient a l'accueil
		/*logo.addMouseListener(new MouseListener() {

			@Override
			public void mouseDoubleClick(MouseEvent e) {}

			@Override
			public void mouseDown(MouseEvent e) {}

			@Override
			public void mouseUp(MouseEvent e) {
				try {
					compositeMain.dispose();
					compositeMain();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
		});*/

		logo.pack();
	}

	public void compositeMenu() {		
		
		
		
	
		// on ajoute les elements à la colonne menu
		menu = new Composite(colonneGauche, SWT.NONE);
		menu.setLayout(fillLayoutV);

		Button boutonEmploye = new Button(menu, SWT.NONE);
		boutonEmploye.setText("Employés");
		boutonEmploye.setBackground(Couleur.blanc);
		//Image image = new Image(display, "images\\boutonEmploye.png");
		//boutonEmploye.setImage(image);
		boutonEmploye.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				if (!contenuColonneDroite.isDisposed()) {
					contenuColonneDroite.dispose();
				}
				contenuColonneDroite = new VueEmploye(colonneDroite,display).getComposite();
				contenuColonneDroite.pack();
				colonneDroite.pack();

			}
		});

		Button boutonAffectation = new Button(menu, SWT.NONE);

		System.out.println(boutonAffectation.getBounds());
		//boutonAffectation.setImage();
		boutonAffectation.setText("Affectations");
		System.out.println(boutonAffectation.getParent().getDisplay().getBounds());
		boutonAffectation.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				if (!contenuColonneDroite.isDisposed()) {
					for (Control c :contenuColonneDroite.getChildren()) {
						if(!c.isDisposed()) { c.dispose(); }
					}
					contenuColonneDroite.dispose();
				}
				try {
					contenuColonneDroite = new ViewAffectation(getColonneDroite()).getVueAffectation();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				contenuColonneDroite.pack();
				colonneDroite.pack();
				//System.out.println("done");
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
				contenuColonneDroite = new VueChantier(colonneDroite,display).getComposite();
				contenuColonneDroite.pack();
				colonneDroite.pack();
			}
		});
		
		Button boutonLivraison = new Button(menu, SWT.NONE);
		boutonLivraison.setText("Livraisons et autres coûts");
		boutonLivraison.setBackground(Couleur.blanc);

		boutonLivraison.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				if (!contenuColonneDroite.isDisposed()) {
					contenuColonneDroite.dispose();
				}
				contenuColonneDroite = new ViewTurnOver(colonneDroite,display).getComposite();
				contenuColonneDroite.pack();
				colonneDroite.pack();
			}
		});

		Button boutonProduit = new Button(menu, SWT.NONE);
		boutonProduit.setText("Produits");
		boutonProduit.setBackground(Couleur.blanc);
		boutonProduit.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				if (!contenuColonneDroite.isDisposed()) {
					for (Control c :contenuColonneDroite.getChildren()) {
						if(!c.isDisposed()) { c.dispose(); }
					}
					contenuColonneDroite.dispose();
				}
				try {
					contenuColonneDroite = new ViewProduct(getColonneDroite()).getVueProduit();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				contenuColonneDroite.pack();
				colonneDroite.pack();
				//System.out.println("done");
			}
		});
		
		Button boutonTMP = new Button(menu, SWT.NONE);
		boutonTMP.setText("temporaire");
		boutonTMP.setBackground(Couleur.blanc);
		boutonTMP.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				if (!contenuColonneDroite.isDisposed()) {
					for (Control c :contenuColonneDroite.getChildren()) {
						if(!c.isDisposed()) { c.dispose(); }
					}
					contenuColonneDroite.dispose();
				}
				try {
					contenuColonneDroite = new turnOverView(getColonneDroite()).getRecurringCostView();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				contenuColonneDroite.pack();
				colonneDroite.pack();
				//System.out.println("done");
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

		shell.setMaximized(true);// permet de lancer la fenetre en plein ecran

		this.shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}
}
