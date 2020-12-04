/***
 * SenettisDB is developed by Laetitia Courgey and Stanislas Tran 
 * Copyright (C) 2020, Laetitia Courgey, Stanislas Tran
 * 
 * This file is part of SenettisDB
 * 
 * 
 * A MODIFIER ------------------------
 * ThaliaDB is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */



package GUI;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Month;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Objects;

import org.eclipse.swt.*;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.TouchEvent;
import org.eclipse.swt.events.TouchListener;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

import classes.Site;
import classes.TurnOver;
import classes.AmmortissementChantier;
import classes.AmmortissementEmploye;
import classes.CoutsEmploye;
import classes.AmmortissementChantier;
import classes.Delivery;
import classes.Employee;
import classes.FournitureSanitaire;
import classes.Product;
import classes.ProductByDelivery;

public class VueRentabilité {

	private Display display;
	private Composite vueRentabilite;
	private Composite selection;
	private Composite vue;
	private Table tableRentabilite;

	//Creation VueLivraison --------------------------------------------------
	/***
	 * Utilisé depuis Home pour créer une vueLivraison
	 * @param composite : le composite vueLivraison
	 * @param display
	 */
	public VueRentabilité (Composite composite,Display display) {
		this.display=display;
		Couleur.setDisplay(display); // pour utiliser les couleurs du fichier couleur

		vueRentabilite=new Composite(composite,SWT.NONE);
		vueRentabilite.setLayout(new RowLayout(SWT.VERTICAL));
		vueRentabilite.setBackground(Couleur.gris);

		getSelection();

		LocalDate currentdate = LocalDate.now();
		Month month = currentdate.getMonth();
		int year = currentdate.getYear();
		getVue(month.toString()+" "+year);

		vueRentabilite.pack();
	}

	public void getSelection() {
		if (!Objects.isNull(selection) && !selection.isDisposed()) {
			selection.dispose();
		}

		selection = new Composite(vueRentabilite, SWT.NONE);
		RowLayout rowLayout = new RowLayout(SWT.VERTICAL);
		rowLayout.spacing = 15;
		selection.setLayout(rowLayout);
		selection.setBackground(Couleur.gris);

		Composite selec1 = new Composite(selection, SWT.NONE);
		selec1.setLayout(new RowLayout(SWT.HORIZONTAL));

		Label HeadLabel =new Label(selec1,SWT.TITLE);
		HeadLabel.setText("Rentabilité : ");
		Font fontTitle = new Font(HeadLabel.getDisplay(), "Arial", 18, SWT.BOLD);
		HeadLabel.setForeground(Couleur.bleuFonce);
		HeadLabel.setFont(fontTitle);

		Composite selec2 = new Composite(selection, SWT.NONE);
		selec2.setLayout(new RowLayout(SWT.HORIZONTAL));
		Label labelPeriode = new Label(selec2, SWT.NONE);
		labelPeriode.setText("Période : ");

		Combo periode = new Combo(selec2, SWT.BORDER);
		LocalDate currentdate = LocalDate.now();
		Month month = currentdate.getMonth();
		int year = currentdate.getYear();
		periode.setText(month.toString()+" "+year);

		for(int i = 2020; i <= year+1 ; i++) {
			for (int j =1 ; j <= 12 ; j++) {
				periode.add(Month.of(j)+" "+i);
			}
		}

		periode.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				updateTable(periode.getText());
				vue.pack();
			}
		});
		selec2.pack();

	}

	public void getVue(String periode) {
		if (!Objects.isNull(vue) && !vue.isDisposed()) {
			vue.dispose();
		}
		RowLayout rowLayoutV = new RowLayout();
		rowLayoutV.type = SWT.VERTICAL;

		vue = new Composite(vueRentabilite, SWT.NONE);
		vue.setLayout(rowLayoutV);
		vue.setBackground(Couleur.gris);

		//creation de la table
		tableRentabilite = new Table (vue, SWT.BORDER | SWT.MULTI| SWT.V_SCROLL | SWT.FULL_SELECTION);
		tableRentabilite.setLayoutData(new RowData(888, 390));
		tableRentabilite.setLinesVisible (true);
		tableRentabilite.setHeaderVisible (true);

		//on met les noms des colonnes
		String[] titles = {"Chantier","Chifffre d'affaire", "Masse Salariale", "Livraisons", "Matériels", "Fournitures Sanitaires", "Total", "Pourcentage"};
		for (String title : titles) {
			TableColumn column = new TableColumn (tableRentabilite, SWT.NONE);
			column.setText (title);
		}

		//on remplit la table
		final TableColumn [] columns = tableRentabilite.getColumns ();

		updateTable(periode);

		//on pack les colonnes
		for (TableColumn col : columns)
			col.pack ();

		vue.pack();
	}


	public void updateTable(String periode) {
		tableRentabilite.removeAll();

		String[] d2 = periode.split(" ");
		YearMonth date1 = YearMonth.of(Integer.parseInt(d2[1]),Month.valueOf(d2[0]).getValue());

		try {
			for (Site c : Site.getAllChantier()) {
				Double total = 0.0;
				if (c.getStatus().equals("Publié")) {
					TableItem item = new TableItem (tableRentabilite, SWT.NONE);
					item.setText(0,c.getNom());

					double CA = 0.0;
					try {
						CA = TurnOver.getTurnOverByDateAndSite(c.getChantierId(),Month.valueOf(d2[0]).getValue(),Integer.parseInt(d2[1])).getCa();
						item.setText(1,Double.toString(CA));
					} catch (Exception e) {
						item.setText(1,"0.0");
					}

					////masse salariale TODO

					//livraison
					double total_livraison = 0.0;
					for (Delivery l : Delivery.getAllLivraison()) {
						if (l.getDate() != null) {
							String[] d1 = l.getDate().split("/");
							YearMonth date2 = YearMonth.of(Integer.parseInt(d1[2]),Integer.parseInt(d1[1]));

							if (l.getStatus().equals("Publié")) {
								if (l.getIdChantier()==c.getChantierId() && date1.equals(date2) ) { total_livraison += l.getPrixTotal(); }
							}

						}
					}
					item.setText(3,Double.toString(total_livraison));
					total += total_livraison;

					//materiel
					double total_materiel = 0.0;
					for (AmmortissementChantier ac : AmmortissementChantier.getAllAmmortissementChantier()) {
						YearMonth debut = YearMonth.of(ac.getAnneeD(),ac.getMoisD());
						YearMonth fin = YearMonth.of(ac.getAnneeF(),ac.getMoisF());
						if (ac.getStatus().equals("Publié")) {
							if (ac.getChantierId()==c.getChantierId()) {
								if (debut.equals(date1) || fin.equals(date1) || (debut.isBefore(date1) && fin.isAfter(date1))){
									total_materiel += ac.getMontantParMois();
								}
							}
						}
					}
					item.setText(4,Double.toString(total_materiel));
					total += total_materiel;

					//fs
					double total_fs = 0.0;
					for (FournitureSanitaire fs : FournitureSanitaire.getAllFournitureSanitaire()) {
						if (fs.getAnneeD()!=null && fs.getMoisD() != null) {// si on a une date de debut on regarde sinon l'ajoute direct parce que qu'on considere que c'est tout le temps
							YearMonth debut = YearMonth.of(fs.getAnneeD(),fs.getMoisD());
							if (fs.getStatus().equals("Publié")) {
								if (fs.getChantierId()==c.getChantierId()) {
									if (debut.equals(date1) || (debut.isBefore(date1))){
										total_fs += fs.getMontantParMois();
									}
								}
							}
						}
						else {
							total_fs += fs.getMontantParMois();
						}
					}
					item.setText(5,Double.toString(total_fs));
					total += total_fs;
					
					item.setText(6,Double.toString(total));
					
					if (CA != 0.0) {
						item.setText(7,Double.toString((total*100)/CA));
					}
				}
			}



		} catch (SQLException e) {
			System.out.println("erreur dans la table des rentabilités");
			MessageBox dialog = new MessageBox(vueRentabilite.getShell(), SWT.ICON_ERROR | SWT.OK);
			dialog.setText("Erreur");
			dialog.setMessage("Une erreur est survenue. "+'\n'+e.getMessage());
			dialog.open();
		}
	}


	public Composite getComposite() {
		return this.vueRentabilite;
	}

}
