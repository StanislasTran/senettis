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
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.time.YearMonth;
import java.util.ArrayList;

import java.util.Map;
import java.util.Objects;

import org.eclipse.swt.*;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import classes.Site;
import classes.TurnOver;
import classes.SiteAmortisation;
import classes.Comission;
import classes.Delivery;
import classes.FS;
import classes.Rentability;
import classes.SalaryCostPerSite;

public class RentabilityView {

	private Composite rentabilityView;
	private Composite selection;
	private Composite view;
	private Table rentabilityTable;
	private Composite header;

	// Creation VueLivraison --------------------------------------------------
	/***
	 * Utilisé depuis Home pour créer une vueLivraison
	 * 
	 * @param composite : le composite vueLivraison
	 * @param display
	 * @throws SQLException
	 */
	public RentabilityView(Composite composite, Display display) throws SQLException {

		MyColor.setDisplay(display); // pour utiliser les couleurs du fichier couleur

		rentabilityView = new Composite(composite, SWT.NONE);
		rentabilityView.setLayout(new RowLayout(SWT.VERTICAL));
		rentabilityView.setBackground(MyColor.bleuClair);
		addHeader("Analyse de la rentabilité");
		getSelection();

		LocalDate currentdate = LocalDate.now();
		Month month = currentdate.getMonth();
		int year = currentdate.getYear();
		getVue(month.toString() + " " + year);

		rentabilityView.pack();
	}

	public void getSelection() {
		if (!Objects.isNull(selection) && !selection.isDisposed()) {
			selection.dispose();
		}

		selection = new Composite(rentabilityView, SWT.NONE);

		RowLayout rowLayout = new RowLayout(SWT.VERTICAL);
		rowLayout.spacing = 15;
		selection.setLayout(rowLayout);
		selection.setBackground(MyColor.bleuClair);

		Composite selec1 = new Composite(selection, SWT.NONE);
		selec1.setLayout(new RowLayout(SWT.HORIZONTAL));
		selec1.setBackground(MyColor.bleuClair);

		Composite selec2 = new Composite(selection, SWT.NONE);
		selec2.setLayout(new RowLayout(SWT.HORIZONTAL));
		selec2.setBackground(MyColor.bleuClair);
		Label labelPeriode = new Label(selec2, SWT.NONE);
		labelPeriode.setText("Période : ");
		labelPeriode.setBackground(MyColor.bleuClair);
		Combo periode = new Combo(selec2, SWT.BORDER);
		LocalDate currentdate = LocalDate.now();
		Month month = currentdate.getMonth();
		int year = currentdate.getYear();
		periode.setText(month.toString() + " " + year);

		for (int i = 2020; i <= year + 1; i++) {
			for (int j = 1; j <= 12; j++) {
				periode.add(Month.of(j) + " " + i);
			}
		}

		periode.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				try {
					updateTable(periode.getText());
				} catch (SQLException e) {
					MessageBox msgBox = new MessageBox(rentabilityView.getShell(), SWT.ERROR);
					msgBox.setMessage("Erreur Base de donnée");
					msgBox.setText("erreur de liée à la base de données : \n" + e.getMessage());
					msgBox.open();
					e.printStackTrace();
				}
				view.pack();
			}
		});

		Button save = new Button(selection, SWT.NONE);
		save.setText("Mettre à jour données pour powerBI (Sauvegarde toutes les données des années N-1 à N )");
		save.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				try {
					ArrayList<Site> allSite = (ArrayList<Site>) Site.getAllSite();

					for (int yearI = Year.now().getValue() -1; yearI < Year.now().getValue() + 1; yearI++) {

						for (int monthI = 1; monthI <= 12; monthI++) {

							for (Site s : allSite) {
								Map<Integer, Double> comissions = Comission.getAllComissionsAfterMap(Month.of(monthI),
										Year.of(yearI));

								Map<Integer, Double> fsMap = FS.getAllFSterMap(Month.of(monthI), Year.of(yearI));

								Map<Integer, Double> livraisonMap = Delivery
										.getAllLivraisonFilteredMap(Month.of(monthI), Year.of(yearI));

								Map<Integer, Double> amortiMap = SiteAmortisation.getAllACFilteredMap(Month.of(monthI),
										Year.of(yearI));
								Map<Integer, Double> turnOverMap = TurnOver.getTurnOverByDateAndSite(Month.of(monthI),
										Year.of(yearI));

								Map<Integer, Double> salaryCostMap = SalaryCostPerSite
										.computeTotalAffectationCost(Month.of(monthI), Year.of(yearI));

								Double turnOver = 0.00;
								Double totalEmployeCost = 0.00;
								Double total_delivery = 0.00;
								Double total_material = 0.00;
								Double total_fs = 0.00;

								Double priceCost = 0.00;
								Double grossMargin = 0.00;

								Double percent = 0.00;

								// TurnOver

								if (turnOverMap.containsKey(s.getSiteId()))
									turnOver = turnOverMap.get(s.getSiteId());


								//EmployeeCost
								if (salaryCostMap.containsKey(s.getSiteId()))
									totalEmployeCost = salaryCostMap.get(s.getSiteId());

								// Delivery

								if (livraisonMap.containsKey(s.getSiteId()))
									total_delivery = livraisonMap.get(s.getSiteId());

								if (fsMap.containsKey(s.getSiteId()))
									total_fs = fsMap.get(s.getSiteId());

								if (amortiMap.containsKey(s.getSiteId()))
									total_material = amortiMap.get(s.getSiteId());

								// Comission

								Double comission = 0.00;
								if (comissions.containsKey(s.getSiteId()))
									comission = comissions.get(s.getSiteId());

								priceCost = totalEmployeCost + total_material + total_fs + ((turnOver * comission) / 100)
										+ total_delivery;
								grossMargin = turnOver - priceCost;
								percent = (grossMargin * 100) / turnOver;

								new Rentability(s.getSiteId(), Month.of(monthI), Year.of(yearI), turnOver, totalEmployeCost,
										total_delivery, total_material, total_fs, comission, priceCost, grossMargin,
										percent).update();

							}

						}
					}

					MessageBox dialog = new MessageBox(rentabilityView.getShell(), SWT.ICON_INFORMATION | SWT.OK);
					dialog.setText("Succes");
					dialog.setMessage("La base de données a été mise à jour");
					dialog.open();

				} catch (SQLException e1) {

					MessageBox dialog = new MessageBox(rentabilityView.getShell(), SWT.ICON_ERROR | SWT.OK);
					dialog.setText("Erreur");
					dialog.setMessage("Une erreur est survenue. " + '\n' + e1.getMessage());
					dialog.open();
					e1.printStackTrace();
				}

			}

		});
		selec2.pack();

	}

	public void getVue(String periode) throws SQLException {
		if (!Objects.isNull(view) && !view.isDisposed()) {
			view.dispose();
		}
		RowLayout rowLayoutV = new RowLayout();
		rowLayoutV.type = SWT.VERTICAL;

		view = new Composite(rentabilityView, SWT.NONE);
		view.setLayout(rowLayoutV);
		view.setBackground(MyColor.gris);

		// creation de la table
		rentabilityTable = new Table(view, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL | SWT.FULL_SELECTION);
		rentabilityTable.setLayoutData(new RowData(888, 390));
		rentabilityTable.setLinesVisible(true);
		rentabilityTable.setHeaderVisible(true);

		// on met les noms des colonnes
		String[] titles = { "Chantier", "Chifffre d'affaire", "Total Couts Employés", "Livraisons", "Matériels",
				"Fournitures Sanitaires", "Comissions", "Coût de revient", "Marge Brut", "Pourcentage" };
		for (String title : titles) {
			TableColumn column = new TableColumn(rentabilityTable, SWT.NONE);
			column.setText(title);
		}

		// on remplit la table
		final TableColumn[] columns = rentabilityTable.getColumns();

		updateTable(periode);

		// on pack les colonnes
		for (TableColumn col : columns)
			col.pack();

		view.pack();
	}

	public void updateTable(String periode) throws SQLException {
		rentabilityTable.removeAll();

		String[] d2 = periode.split(" ");
		YearMonth date1 = YearMonth.of(Integer.parseInt(d2[1]), Month.valueOf(d2[0]).getValue());
		Month monthFilter = date1.getMonth();
		Year yearFilter = Year.of(date1.getYear());

		Map<Integer, Double> comissions = Comission.getAllComissionsAfterMap(monthFilter, yearFilter);

		Map<Integer, Double> fsMap = FS.getAllFSterMap(monthFilter, yearFilter);

		Map<Integer, Double> livraisonMap = Delivery.getAllLivraisonFilteredMap(monthFilter, yearFilter);

		Map<Integer, Double> amortiMap = SiteAmortisation.getAllACFilteredMap(monthFilter, yearFilter);
		Map<Integer, Double> turnOverMap = TurnOver.getTurnOverByDateAndSite(monthFilter, yearFilter);

		Map<Integer, Double> salaryCostMap = SalaryCostPerSite.computeTotalAffectationCost(monthFilter, yearFilter);
		try {
			for (Site c : Site.getAllSite()) {
				Double grossMargin = 0.0;
				Double priceCost = 0.0;

				TableItem item = new TableItem(rentabilityTable, SWT.NONE);

				item.setText(0, c.getName());

				double turnOver = 0.0;

				/// turnOver///

				try {

					if (turnOverMap.containsKey(c.getSiteId()))
						turnOver = turnOverMap.get(c.getSiteId());

					item.setText(1, "" + turnOver);
				} catch (Exception e) {

					item.setText(1, "0.0");
				}

				Double totalEmployeCost = 0.0;

				if (salaryCostMap.containsKey(c.getSiteId()))
					totalEmployeCost = salaryCostMap.get(c.getSiteId());

				DecimalFormat df = new DecimalFormat("0.00");

				item.setText(2, df.format(totalEmployeCost));

				// delivery//

				double total_delivery = 0.0;
				if (livraisonMap.containsKey(c.getSiteId()))
					total_delivery = livraisonMap.get(c.getSiteId());

				item.setText(3, Double.toString(total_delivery));

				// materiel

				Double total_material = 0.0;
				if (amortiMap.containsKey(c.getSiteId()))
					total_material = amortiMap.get(c.getSiteId());

				item.setText(4, Double.toString(total_material));

				double total_fs = 0.0;

				if (fsMap.containsKey(c.getSiteId()))
					total_fs = fsMap.get(c.getSiteId());

				item.setText(5, Double.toString(total_fs));

				// Comission

				Double comission = 0.00;
				if (comissions.containsKey(c.getSiteId()))
					comission = comissions.get(c.getSiteId());

				item.setText(6, Double.toString(comission));

				priceCost = totalEmployeCost + total_delivery + total_material + total_fs
						+ (turnOver * comission) / 100;

				grossMargin = turnOver - priceCost;
				item.setText(7, Double.toString(priceCost));
				item.setText(8, Double.toString(grossMargin));

				if (turnOver != 0.0) {
					item.setText(9, df.format((grossMargin * 100) / turnOver) + "%");
				}

			}

		} catch (

		SQLException e) {

			MessageBox dialog = new MessageBox(rentabilityView.getShell(), SWT.ICON_ERROR | SWT.OK);
			dialog.setText("Erreur");
			dialog.setMessage("Une erreur est survenue. " + '\n' + e.getMessage());
			dialog.open();
		}
	}

	public Composite getComposite() {
		return this.rentabilityView;
	}

	/**
	 * Add a header in header composite with title <param>header</param>
	 * 
	 * @param <type>String</type> header
	 */
	public void addHeader(String header) {
		if (!Objects.isNull(this.header) && !this.header.isDisposed())
			this.header.dispose();
		this.header = new Composite(this.rentabilityView, SWT.CENTER | SWT.BORDER);
		this.header.setBackground(MyColor.bleuFonce);
		FillLayout layout = new FillLayout();
		layout.marginWidth = 365;
		this.header.setLayout(layout);

		Label HeadLabel = new Label(this.header, SWT.TITLE);

		HeadLabel.setText("\n" + header + "\n \n");
		Font fontTitle = new Font(HeadLabel.getDisplay(), "Arial", 12, SWT.BOLD);
		HeadLabel.setForeground(MyColor.bleuClair);
		HeadLabel.setFont(fontTitle);
		HeadLabel.setBackground(MyColor.bleuFonce);
		this.header.pack();
		HeadLabel.pack();

	}

}
