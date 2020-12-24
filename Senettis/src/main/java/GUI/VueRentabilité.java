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
import classes.AmmortissementChantier;
import classes.Comission;
import classes.Delivery;
import classes.FournitureSanitaire;
import classes.Rentabilite;
import classes.SalaryCostPerSite;

public class VueRentabilité {

	private Composite vueRentabilite;
	private Composite selection;
	private Composite vue;
	private Table tableRentabilite;
	private Composite header;

	// Creation VueLivraison --------------------------------------------------
	/***
	 * Utilisé depuis Home pour créer une vueLivraison
	 * 
	 * @param composite : le composite vueLivraison
	 * @param display
	 * @throws SQLException
	 */
	public VueRentabilité(Composite composite, Display display) throws SQLException {

		Couleur.setDisplay(display); // pour utiliser les couleurs du fichier couleur

		vueRentabilite = new Composite(composite, SWT.NONE);
		vueRentabilite.setLayout(new RowLayout(SWT.VERTICAL));
		vueRentabilite.setBackground(Couleur.bleuClair);
		addHeader("Analyse de la rentabilité");
		getSelection();

		LocalDate currentdate = LocalDate.now();
		Month month = currentdate.getMonth();
		int year = currentdate.getYear();
		getVue(month.toString() + " " + year);

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
		selection.setBackground(Couleur.bleuClair);

		Composite selec1 = new Composite(selection, SWT.NONE);
		selec1.setLayout(new RowLayout(SWT.HORIZONTAL));
		selec1.setBackground(Couleur.bleuClair);

		Composite selec2 = new Composite(selection, SWT.NONE);
		selec2.setLayout(new RowLayout(SWT.HORIZONTAL));
		selec2.setBackground(Couleur.bleuClair);
		Label labelPeriode = new Label(selec2, SWT.NONE);
		labelPeriode.setText("Période : ");
		labelPeriode.setBackground(Couleur.bleuClair);
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
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				vue.pack();
			}
		});

		Button save = new Button(selection, SWT.NONE);
		save.setText("Mettre à jour données pour powerBI (Sauvegarde toutes les données des année N-1 à N )");
		save.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				try {
					ArrayList<Site> allSite = (ArrayList<Site>) Site.getAllChantier();
					for (Site s : allSite) {
						for (int i = 1; i <= 12; i++) {
							for (int j = Year.now().getValue() - 1; j < Year.now().getValue() + 1; j++) {
								Double CA = 0.00;
								Double CoutsEmploye = 0.00;
								Double livraison = 0.00;
								Double materiel = 0.00;
								Double coutsFs = 0.00;
								Double comissions = 0.00;
								Double coutRevient = 0.00;
								Double margeBrut = 0.00;

								Double pourcentage = 0.00;

								// à Gerer
								try {
									TurnOver TO = TurnOver.getTurnOverByDateAndSite(s.getSiteId(), i, j);
									CA = TO.getCa();
								} catch (SQLException sqlException) {

								}
								if (CA > 0) {

									try {
										CoutsEmploye = new SalaryCostPerSite(s.getSiteId(), Month.of(i), Year.of(j))
												.getTotalCost();

									} catch (SQLException sqlException) {

									}

									DecimalFormat df = new DecimalFormat("0.00");

									YearMonth date1 = YearMonth.of(j, i);

									// livraison

									for (Delivery l : Delivery.getAllLivraison()) {
										if (l.getDate() != null) {
											String[] d1 = l.getDate().split("/");
											YearMonth date2 = YearMonth.of(Integer.parseInt(d1[2]),
													Integer.parseInt(d1[1]));

											if (l.getStatus().equals("Publié")) {
												if (l.getIdChantier() == s.getSiteId() && date1.equals(date2)) {
													livraison += l.getPrixTotal();
												}
											}

										}
									}

									for (FournitureSanitaire fs : FournitureSanitaire.getAllFournitureSanitaire()) {
										if (fs.getAnneeD() != null && fs.getMoisD() != null) {// si on a une date de
																								// debut
																								// on regarde
																								// sinon l'ajoute direct
																								// parce que qu'on
																								// considere que c'est
																								// tout
																								// le temps
											YearMonth debut = YearMonth.of(fs.getAnneeD(), fs.getMoisD());
											if (fs.getStatus().equals("Publié")) {
												if (fs.getSiteId() == s.getSiteId()) {
													if (debut.equals(date1) || (debut.isBefore(date1))) {
														coutsFs += fs.getMontantParMois();
													}
												}
											}
										} else {
											coutsFs += fs.getMontantParMois();

										}
									}

									for (AmmortissementChantier ac : AmmortissementChantier
											.getAllAmmortissementChantier()) {
										YearMonth debut = YearMonth.of(ac.getAnneeD(), ac.getMoisD());
										YearMonth fin = YearMonth.of(ac.getAnneeF(), ac.getMoisF());
										if (ac.getStatus().equals("Publié")) {
											if (ac.getSiteId() == s.getSiteId()) {
												if (debut.equals(date1) || fin.equals(date1)
														|| (debut.isBefore(date1) && fin.isAfter(date1))) {
													materiel += ac.getMontantParMois();
												}
											}
										}
									}

									Double comission = Comission.getComissionSum(s.getSiteId(), date1.getMonth(),
											Year.of(date1.getYear()));

									coutRevient = CoutsEmploye + materiel + coutsFs + ((CA * comissions) / 100)
											+ livraison;
									margeBrut = CA - coutRevient;
									pourcentage = (margeBrut * 100) / CA;

									new Rentabilite(s.getSiteId(), Month.of(i), Year.of(j), CA, CoutsEmploye, livraison,
											materiel, coutsFs, comissions, coutRevient, margeBrut, pourcentage)
													.update();

								}

							}
						}

					}

					MessageBox dialog = new MessageBox(vueRentabilite.getShell(), SWT.ICON_INFORMATION | SWT.OK);
					dialog.setText("Succes");
					dialog.setMessage("La base de données a été mise à jour");
					dialog.open();

				} catch (SQLException e1) {

					MessageBox dialog = new MessageBox(vueRentabilite.getShell(), SWT.ICON_ERROR | SWT.OK);
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
		if (!Objects.isNull(vue) && !vue.isDisposed()) {
			vue.dispose();
		}
		RowLayout rowLayoutV = new RowLayout();
		rowLayoutV.type = SWT.VERTICAL;

		vue = new Composite(vueRentabilite, SWT.NONE);
		vue.setLayout(rowLayoutV);
		vue.setBackground(Couleur.gris);

		// creation de la table
		tableRentabilite = new Table(vue, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL | SWT.FULL_SELECTION);
		tableRentabilite.setLayoutData(new RowData(888, 390));
		tableRentabilite.setLinesVisible(true);
		tableRentabilite.setHeaderVisible(true);

		// on met les noms des colonnes
		String[] titles = { "Chantier", "Chifffre d'affaire", "Total Couts Employés", "Livraisons", "Matériels",
				"Fournitures Sanitaires", "Comissions", "Coût de revient", "Marge Brut", "Pourcentage" };
		for (String title : titles) {
			TableColumn column = new TableColumn(tableRentabilite, SWT.NONE);
			column.setText(title);
		}

		// on remplit la table
		final TableColumn[] columns = tableRentabilite.getColumns();

		updateTable(periode);

		// on pack les colonnes
		for (TableColumn col : columns)
			col.pack();

		vue.pack();
	}

	public void updateTable(String periode) throws SQLException {
		tableRentabilite.removeAll();

		String[] d2 = periode.split(" ");
		YearMonth date1 = YearMonth.of(Integer.parseInt(d2[1]), Month.valueOf(d2[0]).getValue());

		Map<Integer, Double> comissions = Comission.getAllComissionsAfterMap(date1.getMonth(),
				Year.of(date1.getYear()));

		Map<Integer, Double> fsMap = FournitureSanitaire.getAllFSterMap(date1.getMonth(), Year.of(date1.getYear()));

		try {
			for (Site c : Site.getAllChantier()) {
				Double margeBrut = 0.0;
				Double CoutRevient = 0.0;

				TableItem item = new TableItem(tableRentabilite, SWT.NONE);
				item.setText(0, c.getName());

				double CA = 0.0;

				/// CA///

				try {
					CA = TurnOver.getTurnOverByDateAndSite(c.getSiteId(), Month.valueOf(d2[0]).getValue(),
							Integer.parseInt(d2[1])).getCa();

					item.setText(1, Double.toString(CA));
				} catch (Exception e) {

					item.setText(1, "0.0");
				}

				Double totalEmployeCost = 0.0;

				///// EMPLOYEECOST///

				try {
					totalEmployeCost = new SalaryCostPerSite(c.getSiteId(), Month.valueOf(d2[0]),
							Year.of(Integer.parseInt(d2[1]))).getTotalCost();

				} catch (SQLException sqlException) {

				}

				DecimalFormat df = new DecimalFormat("0.00");

				item.setText(2, df.format(totalEmployeCost));

				// livraison//

				double total_livraison = 0.0;

				for (Delivery l : Delivery.getAllLivraison()) {
					if (l.getDate() != null) {
						String[] d1 = l.getDate().split("/");
						YearMonth date2 = YearMonth.of(Integer.parseInt(d1[2]), Integer.parseInt(d1[1]));

						if (l.getStatus().equals("Publié")) {
							if (l.getIdChantier() == c.getSiteId() && date1.equals(date2)) {
								total_livraison += l.getPrixTotal();
							}
						}

					}
				}

				item.setText(3, Double.toString(total_livraison));

				// materiel

				Double total_materiel = 0.0;

				for (AmmortissementChantier ac : AmmortissementChantier.getAllAmmortissementChantier()) {
					YearMonth debut = YearMonth.of(ac.getAnneeD(), ac.getMoisD());
					YearMonth fin = YearMonth.of(ac.getAnneeF(), ac.getMoisF());
					if (ac.getStatus().equals("Publié")) {
						if (ac.getSiteId() == c.getSiteId()) {
							if (debut.equals(date1) || fin.equals(date1)
									|| (debut.isBefore(date1) && fin.isAfter(date1))) {
								total_materiel += ac.getMontantParMois();
							}
						}
					}
				}

				item.setText(4, Double.toString(total_materiel));

				// fs
				double total_fs = 0.0;

				if (fsMap.containsKey(c.getSiteId()))
					total_fs = fsMap.get(c.getSiteId());

				item.setText(5, Double.toString(total_fs));

				// Comission

				Double comission = 0.00;
				if (comissions.containsKey(c.getSiteId()))
					comission = comissions.get(c.getSiteId());

				item.setText(6, Double.toString(comission));

				CoutRevient = totalEmployeCost + total_livraison + total_materiel + total_fs + (CA * comission) / 100;

				margeBrut = CA - CoutRevient;
				item.setText(7, Double.toString(CoutRevient));

				item.setText(8, Double.toString(margeBrut));

				if (CA != 0.0) {
					item.setText(9, df.format((margeBrut * 100) / CA) + "%");
				}

			}

		} catch (

		SQLException e) {

			MessageBox dialog = new MessageBox(vueRentabilite.getShell(), SWT.ICON_ERROR | SWT.OK);
			dialog.setText("Erreur");
			dialog.setMessage("Une erreur est survenue. " + '\n' + e.getMessage());
			dialog.open();
		}
	}

	public Composite getComposite() {
		return this.vueRentabilite;
	}

	/**
	 * Add a header in header composite with title <param>header</param>
	 * 
	 * @param <type>String</type> header
	 */
	public void addHeader(String header) {
		if (!Objects.isNull(this.header) && !this.header.isDisposed())
			this.header.dispose();
		this.header = new Composite(this.vueRentabilite, SWT.CENTER | SWT.BORDER);
		this.header.setBackground(Couleur.bleuFonce);
		FillLayout layout = new FillLayout();
		layout.marginWidth = 365;
		this.header.setLayout(layout);

		Label HeadLabel = new Label(this.header, SWT.TITLE);

		HeadLabel.setText("\n" + header + "\n \n");
		Font fontTitle = new Font(HeadLabel.getDisplay(), "Arial", 12, SWT.BOLD);
		HeadLabel.setForeground(Couleur.bleuClair);
		HeadLabel.setFont(fontTitle);
		HeadLabel.setBackground(Couleur.bleuFonce);
		this.header.pack();
		HeadLabel.pack();

	}

}
