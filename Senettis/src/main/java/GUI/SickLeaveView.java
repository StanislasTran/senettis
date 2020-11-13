package GUI;

import java.sql.SQLException;
import java.util.Objects;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Text;

import classes.Employee;
import classes.SickLeave;

public class SickLeaveView {
	
	public static void getSLSelectionTitle(Composite vue, Composite selection, Composite vueEmploye) throws SQLException {
		if (!Objects.isNull(selection) && !selection.isDisposed()) { selection.dispose(); }

		String title; int addSize;
		title = "Création d'un Arrêt Maladie";
		addSize = vue.getSize().x;
		addSize = (addSize - 200)/2;
		
		selection = new Composite(vueEmploye, SWT.CENTER);

		FillLayout fillLayout = new FillLayout();
		fillLayout.type = SWT.VERTICAL;
		fillLayout.marginWidth = addSize;
		selection.setLayout(fillLayout);

		// juste pour creer un espace
		Label l1 = new Label(selection, SWT.NONE);
		l1.setText("");
		l1.setBackground(Couleur.bleuFonce);

		selection.setBackground(Couleur.bleuFonce);
		Label HeadLabel = new Label(selection, SWT.TITLE);
		HeadLabel.setText(title);
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
	
	
	public static void getSLViewContent(Composite vue, Composite selection, Composite vueEmploye, Employee selectedEmploye) throws SQLException {
		if (!Objects.isNull(vue) && !vue.isDisposed()) { vue.dispose(); }
		vue = new Composite(vueEmploye, SWT.NONE);
		FillLayout fillLayoutH = new FillLayout();
		fillLayoutH.type = SWT.HORIZONTAL;
		vue.setLayout(fillLayoutH);
		
		FillLayout fillLayoutV = new FillLayout();
		fillLayoutV.type = SWT.VERTICAL;
		fillLayoutV.marginWidth = 10;
		Composite column1 = new Composite(vue, SWT.BORDER);
		Composite column2 = new Composite(vue, SWT.BORDER);
		column1.setBackground(Couleur.bleuClair);
		column2.setBackground(Couleur.bleuClair);
		column1.setLayout(fillLayoutV);
		column2.setLayout(fillLayoutV);

		// utilisé pour tous les composites des arguments
		FillLayout fillLayoutH5 = new FillLayout();
		fillLayoutH5.marginHeight = 30;
		fillLayoutH5.spacing = 5;
		fillLayoutH5.type = SWT.HORIZONTAL;

		// employee
		Composite employeeComposite = new Composite(column1, SWT.NONE);
		employeeComposite.setBackground(Couleur.bleuClair);
		employeeComposite.setLayout(fillLayoutH5);

		Label employeeLabel = new Label(employeeComposite, SWT.NONE);
		employeeLabel.setBackground(Couleur.bleuClair);
		employeeLabel.setText("Employé* : ");

		Combo employee = new Combo(employeeComposite, SWT.BORDER);
		if (selectedEmploye != null) {
			String name = Employee.getEmployeById(selectedEmploye.getEmployeId()).getNom()+" "+Employee.getEmployeById(selectedEmploye.getEmployeId()).getPrenom();
			if (name.length()>30) {
				name = name.substring(0, 30)+"; id : "+selectedEmploye.getEmployeId();
			}
			else {
				name = name+"; id : "+selectedEmploye.getEmployeId();
			}
			employee.setText(name);
			for(Employee e : Employee.getAllEmploye()) {
				if(e.getEmployeId() != selectedEmploye.getEmployeId()) {
					name = e.getNom()+" "+e.getPrenom();
					if (name.length()>30) {
						name = name.substring(0, 30)+"; id : "+e.getEmployeId();
					}
					else {
						name = name+"; id : "+e.getEmployeId();
					}
					employee.add(name);
				}
			}
		}
		else {
			employee.setText("Selectionner ...");
			String name;
			for(Employee e : Employee.getAllEmploye()) {
				name = e.getNom()+" "+e.getPrenom();
				if (name.length()>30) {
					name = name.substring(0, 30)+"; id : "+e.getEmployeId();
				}
				else {
					name = name+"; id : "+e.getEmployeId();
				}
				employee.add(name);
				
			}
		}

		// date
		Composite dateComposite = new Composite(column1, SWT.NONE);
		dateComposite.setBackground(Couleur.bleuClair);
		dateComposite.setLayout(fillLayoutH5);

		Label dateLabel = new Label(dateComposite, SWT.NONE);
		dateLabel.setBackground(Couleur.bleuClair);
		dateLabel.setText("Date de début* :                                 ");

		final Text date = new Text(dateComposite, SWT.BORDER);
		date.setText("");
		date.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent me) {
				if (!(date.getText().isEmpty())) {// pour ne pas tester quand l'utilisateur est en train de
					// modifier
					if (date.getText().length() == 2) {
						date.append("/");
					}
					if (date.getText().length() == 5) {
						date.append("/");
					}
				}
			}
		});


		// duration
		Composite durationComposite = new Composite(column1, SWT.NONE);
		durationComposite.setBackground(Couleur.bleuClair);
		durationComposite.setLayout(fillLayoutH5);

		Label durationLabel = new Label(durationComposite, SWT.NONE);
		durationLabel.setBackground(Couleur.bleuClair);
		durationLabel.setText("Durée de l'arrêt : ");

		final Text duration = new Text(durationComposite, SWT.BORDER);
		duration.setText("");

		// motive
		Composite motiveComposite = new Composite(column2, SWT.NONE);
		motiveComposite.setBackground(Couleur.bleuClair);
		motiveComposite.setLayout(fillLayoutH5);

		Label motiveLabel = new Label(motiveComposite, SWT.NONE);
		motiveLabel.setBackground(Couleur.bleuClair);
		motiveLabel.setText("Motif de l'arrêt : ");

		final Text motive = new Text(motiveComposite, SWT.BORDER);
		motive.setText("");

		// cost
		Composite costComposite = new Composite(column2, SWT.NONE);
		costComposite.setBackground(Couleur.bleuClair);
		costComposite.setLayout(fillLayoutH5);

		Label costLabel = new Label(costComposite, SWT.NONE);
		costLabel.setBackground(Couleur.bleuClair);
		costLabel.setText("Coût eventuel : ");

		final Text cost = new Text(costComposite, SWT.BORDER);
		cost.setText("");

		// Boutons
		Composite compositeBoutons = new Composite(column2, SWT.CENTER);
		compositeBoutons.setBackground(Couleur.bleuClair);
		compositeBoutons.setLayout(fillLayoutH5);

		Button buttonAnnulation = new Button(compositeBoutons, SWT.BACKGROUND);
		buttonAnnulation.setText("Annuler");
		buttonAnnulation.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				//newVueEmploye();
			}
		});

		Button buttonValidation = new Button(compositeBoutons, SWT.BACKGROUND);
		buttonValidation.setText("Valider");
		buttonValidation.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent arg0) {

				try {
					createSL(employee.getText(), date.getText(), duration.getText(), motive.getText(), cost.getText());
				} catch (Throwable e) {
					System.out.println("erreur dans la creation SickLeave");
					MessageBox dialog = new MessageBox(vueEmploye.getShell(), SWT.ICON_ERROR | SWT.OK);
					dialog.setText("Erreur Création");
					dialog.setMessage("Une erreur est survenue lors de la création de l'arrêt maladie. " + '\n' + e.getMessage());
					dialog.open();
				}
			}
		});
	vue.pack();
	}
	
	

	public static void createSL(String employee, String date, String duration, String motive, String cost) {
		//employee
		int employeeId;SickLeave sl;
		try {
			String s = employee.split(";")[1].replace(" ",""); 
			employeeId = Integer.parseInt(s.substring(3,s.length()));
		} catch (Exception e) {
			throw new Error("Merci d'indiquer un employé.");
		}
		
		
		if (!(date.isEmpty())) {
			sl = new SickLeave(employeeId, date, "Publié");
		}
		else {
			throw new Error("Merci d'indiquer une date.");
		}
		
		if (!(duration.isEmpty())) {
			sl.setDuration(duration);
		}
		
		if (!(motive.isEmpty())) {
			sl.setMotive(motive);
		}
		
		if (!(cost.isEmpty())) {
			try {
				sl.setCost(Double.parseDouble(cost));
			}catch(Exception e) {
				try {
					sl.setCost(Double.parseDouble(cost+".0"));
				}catch(Exception e1) {
					throw new Error("Le cout est érroné.");
				}
			}
		}
		
		try {
			sl.insertDatabase();
			System.out.println("on a insere le sick leave !!");
			//MessageBox dialog = new MessageBox(vueEmploye.getShell(), SWT.ICON_INFORMATION | SWT.OK);
			//dialog.setText("Création réussie");
			//dialog.setMessage("L'arrêt maladie a bien été ajouté à la base de données.");
			//dialog.open();
			//newVueEmploye();
		} catch (SQLException e) {
			throw new Error("Une erreur est survenue lors de la création de l'arrêt maladie.");
		}
	}
	
}
