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

/**
 * Home page of the app GUI
 *
 */
public class Home {

	private Display display;
	private Shell shell;
	Rectangle rect;

	RowLayout rowLayoutH;
	RowLayout rowLayoutV;
	FillLayout fillLayoutV;

	ScrolledComposite compositeMain;
	Composite compositeMainContent;

	Composite colonneGauche;
	Composite logoSpace;
	Composite menu;

	Composite rightColumn;
	Composite rightColumnContent;
	Composite currentSelection;
	Composite currentVue;

	public void menuBar() {

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
	 * create and fill the mainComposite for Home
	 * 
	 * @throws SQLException
	 */
	public void compositeMain() throws SQLException {
		compositeMain = new ScrolledComposite(shell,
				SWT.H_SCROLL | SWT.V_SCROLL | SWT.SCROLLBAR_OVERLAY | SWT.SCROLL_PAGE | SWT.SCROLL_LINE);

		compositeMain.setExpandHorizontal(true);
		compositeMain.setExpandVertical(true);
		compositeMain.setAlwaysShowScrollBars(true);

		InputStream test4 = getClass().getClassLoader().getResourceAsStream("test4.png");
		Image background = new Image(display, test4);

		compositeMainContent.setBackgroundImage(background);
		RowLayout rl = new RowLayout();
		rl.spacing = 15;
		compositeMainContent.setLayout(rl);

		compositeLeftColumn();
		compositeRightColumn();

		compositeMainContent.pack();

		compositeMain.setContent(compositeMainContent);

		compositeMain.setMinSize(rect.width, rect.height);

		shell.setLayout(new RowLayout());
	}

	/****************************************
	 * 
	 * Manager right column of the home page
	 *
	 ********************************************/

	public void compositeRightColumn() throws SQLException {
		rightColumn = new Composite(compositeMainContent, SWT.CENTER);
		this.rightColumnContent = new Composite(rightColumn, SWT.CENTER);
		rightColumnContent.setLayout(rowLayoutV);
		rightColumnContent.setBackground(MyColor.gris);

		title();
		presentation();

		rightColumnContent.pack();
		rightColumn.pack();
	}

	/**
	 * Create the title
	 */

	public void title() {
		Composite titre = new Composite(rightColumnContent, SWT.CENTER);

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

		Composite presentation = new Composite(rightColumnContent, SWT.CENTER);
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

		presLabel.setBackground(MyColor.gris);
		presLabel.setFont(new Font(presLabel.getDisplay(), "Arial", 13, SWT.NONE));

		presentation.pack();
	}

	/************************
	 * 
	 * Manage left column which contain the logo and button to access differents app
	 * views
	 * 
	 ************************/

	public void compositeLeftColumn() {
		colonneGauche = new Composite(compositeMainContent, SWT.BACKGROUND);
		colonneGauche.setLayout(fillLayoutV);

		compositeLogo();
		compositeMenu();

		colonneGauche.pack();
	}

	/**
	 * create the compositeLogo
	 * 
	 */
	public void compositeLogo() {

		Label logo = new Label(colonneGauche, SWT.NONE);

		InputStream moyenLogo = getClass().getClassLoader().getResourceAsStream("moyenLogo.jpg");
		Image image = new Image(display, moyenLogo);
		logo.setImage(image);

	}

	/**
	 * create the menu composite
	 */
	public void compositeMenu() {

		menu = new Composite(colonneGauche, SWT.NONE);
		menu.setLayout(fillLayoutV);

		Button boutonEmploye = new Button(menu, SWT.NONE);
		boutonEmploye.setText("Employés");
		boutonEmploye.setBackground(MyColor.blanc);

		boutonEmploye.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				if (!rightColumnContent.isDisposed()) {
					rightColumnContent.dispose();
				}
				rightColumnContent = new EmployeeView(rightColumn, display).getComposite();
				rightColumnContent.pack();
				rightColumn.pack();

			}
		});

		Button boutonAffectationMAB = new Button(menu, SWT.NONE);

		boutonAffectationMAB.setText("Affectations mise à blanc");

		boutonAffectationMAB.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				if (!rightColumnContent.isDisposed()) {
					for (Control c : rightColumnContent.getChildren()) {
						if (!c.isDisposed()) {
							c.dispose();
						}
					}
					rightColumnContent.dispose();
				}
				try {
					rightColumnContent = new MABAssignmentView(getColonneDroite()).getVueAffectation();
				} catch (SQLException e) {
					MessageBox msgBox = new MessageBox(currentVue.getShell(), SWT.ERROR);
					msgBox.setMessage("Erreur Base de donnée");
					msgBox.setText("erreur de liée à la base de données : \n" + e.getMessage());
					msgBox.open();
					e.printStackTrace();
				}

				rightColumnContent.pack();
				rightColumn.pack();
				// System.out.println("done");
			}

		});

		Button siteAssignementButton = new Button(menu, SWT.NONE);

		// boutonAffectation.setImage();
		siteAssignementButton.setText("Affectations à un chantier");

		siteAssignementButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				if (!rightColumnContent.isDisposed()) {
					for (Control c : rightColumnContent.getChildren()) {
						if (!c.isDisposed()) {
							c.dispose();
						}
					}
					rightColumnContent.dispose();
				}
				try {
					rightColumnContent = new AssignmentSiteView(getColonneDroite()).getVueAffectation();
				} catch (SQLException e) {
					MessageBox msgBox = new MessageBox(currentVue.getShell(), SWT.ERROR);
					msgBox.setMessage("Erreur Base de donnée");
					msgBox.setText("erreur de liée à la base de données : \n" + e.getMessage());
					msgBox.open();
					e.printStackTrace();
				}

				rightColumnContent.pack();
				rightColumn.pack();

			}

		});

		Button siteButton = new Button(menu, SWT.NONE);
		siteButton.setText("Chantiers");

		siteButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				if (!rightColumnContent.isDisposed()) {
					rightColumnContent.dispose();
				}
				rightColumnContent = new SiteView(rightColumn, display).getComposite();
				rightColumnContent.pack();
				rightColumn.pack();
			}
		});

		Button deliveryButton = new Button(menu, SWT.NONE);
		deliveryButton.setText("Livraison / Fourniture / Matériel");
		deliveryButton.setBackground(MyColor.blanc);

		deliveryButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				if (!rightColumnContent.isDisposed()) {
					rightColumnContent.dispose();
				}
				rightColumnContent = new DeliveryView(rightColumn, display).getComposite();
				rightColumnContent.pack();
				rightColumn.pack();
			}
		});

		Button productButton = new Button(menu, SWT.NONE);
		productButton.setText("Produits");
		productButton.setBackground(MyColor.blanc);
		productButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				if (!rightColumnContent.isDisposed()) {
					for (Control c : rightColumnContent.getChildren()) {
						if (!c.isDisposed()) {
							c.dispose();
						}
					}
					rightColumnContent.dispose();
				}
				try {
					rightColumnContent = new ProductView(getColonneDroite()).getVueProduit();
				} catch (SQLException e) {
					MessageBox msgBox = new MessageBox(currentVue.getShell(), SWT.ERROR);
					msgBox.setMessage("Erreur Base de donnée");
					msgBox.setText("erreur de liée à la base de données : \n" + e.getMessage());
					msgBox.open();
				}

				rightColumnContent.pack();
				rightColumn.pack();
				// System.out.println("done");
			}
		});

		Button comissionButton = new Button(menu, SWT.NONE);
		comissionButton.setText("Comissions");
		comissionButton.setBackground(MyColor.blanc);
		comissionButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				if (!rightColumnContent.isDisposed()) {
					for (Control c : rightColumnContent.getChildren()) {
						if (!c.isDisposed()) {
							c.dispose();
						}
					}
					rightColumnContent.dispose();
				}
				try {
					rightColumnContent = new ComissionView(getColonneDroite()).getVueProduit();
				} catch (SQLException e) {
					MessageBox msgBox = new MessageBox(currentVue.getShell(), SWT.ERROR);
					msgBox.setMessage("Erreur Base de donnée");
					msgBox.setText("erreur de liée à la base de données : \n" + e.getMessage());
					msgBox.open();
					e.printStackTrace();
				}

				rightColumnContent.pack();
				rightColumn.pack();

			}
		});

		Button turnOverButton = new Button(menu, SWT.NONE);
		turnOverButton.setText("Chiffre d'Affaire");
		turnOverButton.setBackground(MyColor.blanc);
		turnOverButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				if (!rightColumnContent.isDisposed()) {
					for (Control c : rightColumnContent.getChildren()) {
						if (!c.isDisposed()) {
							c.dispose();
						}
					}
					rightColumnContent.dispose();
				}
				try {
					rightColumnContent = new TurnOverView(getColonneDroite()).getRecurringCostView();
				} catch (SQLException e) {
					MessageBox msgBox = new MessageBox(currentVue.getShell(), SWT.ERROR);
					msgBox.setMessage("Erreur Base de donnée");
					msgBox.setText("erreur de liée à la base de données : \n" + e.getMessage());
					msgBox.open();
					e.printStackTrace();
				}

				rightColumnContent.pack();
				rightColumn.pack();

			}
		});

		Button buttonRent = new Button(menu, SWT.NONE);
		buttonRent.setText("Rentabilité");
		buttonRent.setBackground(MyColor.blanc);
		buttonRent.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				if (!rightColumnContent.isDisposed()) {
					for (Control c : rightColumnContent.getChildren()) {
						if (!c.isDisposed()) {
							c.dispose();
						}
					}
					rightColumnContent.dispose();
				}
				try {
					rightColumnContent = new RentabilityView(rightColumn, display).getComposite();
				} catch (Exception e) {
					e.printStackTrace();

				}
				rightColumnContent.pack();
				rightColumn.pack();
			}
		});

		menu.pack();
	}

	private Composite getColonneDroite() {
		return this.rightColumn;
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

		Monitor monitor = display.getPrimaryMonitor();

		if (monitor != null) {
			rect = monitor.getClientArea();
		} else {
			rect = display.getBounds();
		}

		menuBar();
		compositeMain();

	}

	public void runHome() {
		shell.setMaximized(true);

		this.shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}
}
