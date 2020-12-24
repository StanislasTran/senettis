package classes;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.time.Month;
import java.time.Year;

import java.util.Objects;

import connexion.SQLDatabaseConnection;

/**
 * Salary cost by site proportionnaly computed from employees affectations for a
 * month
 * 
 * @author stan
 *
 */
public class SalaryCostPerSite {

	private Month month;
	private Year year;
	private int siteId;
	private Double AffectationCost;
	private Double totalCost;
	private Double MABCost;

	public SalaryCostPerSite(int siteId, Month month, Year year) throws SQLException {
		this.month = month;
		this.year = year;
		this.siteId = siteId;
		this.AffectationCost = computeAffectationCost(siteId, month, year, "normal");
		this.MABCost = computeAffectationCost(siteId, month, year, "MAB");
		this.totalCost = AffectationCost + MABCost;
	}

	private ResultSet getACjoinCE(int siteId, Month month, Year year) throws SQLException {
		String selection = "Chantier,Employe,AC_nb_heures,MoisDebut,AnneeDebut,mois,annee,mutuelle,indemnite_panier,masse_salariale,cout_transport,cout_telephone,remboursement_prets,saisie_arret,CE_nb_heures";
		String source = "ACjoinCE_View";

		String condition = "ACStatus='Publié' AND CEStatus='Publié' AND Chantier=? AND Mois=? AND Annee=?";
		String reqSql = "Select " + selection + " FROM " + source + " WHERE " + condition + " ;";
		Connection connection = DriverManager.getConnection(new SQLDatabaseConnection().getConnectionUrl());
		PreparedStatement statement = connection.prepareStatement(reqSql);
		statement.setObject(1, siteId, Types.INTEGER);

		statement.setObject(2, month.getValue(), Types.INTEGER);
		statement.setObject(3, year.getValue(), Types.INTEGER);
		statement.execute();
		return statement.getResultSet();
	}

	private ResultSet getACMABjoinCE(int siteId, Month month, Year year) throws SQLException {
		String selection = "Chantier,Employe,AC_nb_heures,mois,annee,mutuelle,indemnite_panier,masse_salariale,cout_transport,cout_telephone,remboursement_prets,saisie_arret,CE_nb_heures";
		String source = "ACjoinCEMAB_View";

		String condition = "ACStatus='Publié' AND CEStatus='Publié' AND Chantier=? AND Mois=? AND Annee=?";
		String reqSql = "Select " + selection + " FROM " + source + " WHERE " + condition + " ;";
		Connection connection = DriverManager.getConnection(new SQLDatabaseConnection().getConnectionUrl());
		PreparedStatement statement = connection.prepareStatement(reqSql);
		statement.setObject(1, siteId, Types.INTEGER);

		statement.setObject(2, month.getValue(), Types.INTEGER);
		statement.setObject(3, year.getValue(), Types.INTEGER);
		statement.execute();
		return statement.getResultSet();
	}

	private Double computeSalaryAffectationCost(Double ACNbHeures, Double mutuelle, Double indemnitePanier,
			Double masseSalariale, Double coutTransport, Double coutTelephone, Double remboursementPrets,
			Double saisieArret, Double CENbHeures) {

		Double salaryCostCE = mutuelle + indemnitePanier + masseSalariale + coutTransport + coutTelephone
				+ remboursementPrets + saisieArret;

		Double coef = ACNbHeures / CENbHeures;

		return salaryCostCE * coef;
	}

	private Double computeAffectationCost(int siteId, Month month, Year year, String type) throws SQLException {

		ResultSet result = null;
		if (Objects.isNull(type))
			throw new IllegalArgumentException("type can't be null must be equal to 'normal' Or 'MAB'");
		if (type.equals("normal")) {
			result = getACjoinCE(siteId, month, year);
		} else if (type.equals("MAB")) {
			result = getACMABjoinCE(siteId, month, year);
		} else
			throw new IllegalArgumentException("unknown type, type are 'normal' or 'MAB'");

		Double affectationCost = 0.00;

		while (result.next()) {

			Double ACNbHeures = 0.00;
			Double mutuelle = 0.00;
			Double indemnitePanier = 0.00;
			Double masseSalariale = 0.00;
			Double coutTransport = 0.00;
			Double coutTelephone = 0.00;
			Double remboursementPrets = 0.00;
			Double saisieArret = 0.00;
			Double CENbHeures = 0.00;

			if (!Objects.isNull(result.getDouble("AC_nb_heures")))
				ACNbHeures = result.getDouble("AC_nb_heures");
			if (!Objects.isNull(result.getDouble("mutuelle")))
				mutuelle = result.getDouble("mutuelle");
			if (!Objects.isNull(result.getDouble("indemnite_panier")))
				indemnitePanier = result.getDouble("indemnite_panier");
			if (!Objects.isNull(result.getDouble("masse_salariale")))
				masseSalariale = result.getDouble("masse_salariale");
			if (!Objects.isNull(result.getDouble("cout_transport")))
				coutTransport = result.getDouble("cout_transport");
			if (!Objects.isNull(result.getDouble("cout_telephone")))
				coutTelephone = result.getDouble("cout_telephone");

			saisieArret = sumArret(result.getInt("Employe"), month, year);
			
			remboursementPrets = sumRemboursementPret(result.getInt("Employe"), month, year);

			if (!Objects.isNull(result.getDouble("CE_nb_heures")))
				CENbHeures = result.getDouble("CE_nb_heures");
			affectationCost += computeSalaryAffectationCost(ACNbHeures, mutuelle, indemnitePanier, masseSalariale,
					coutTransport, coutTelephone, remboursementPrets, saisieArret, CENbHeures);

		}
		return affectationCost;
	}

	private Double sumArret(int employeId, Month month, Year year) throws SQLException {

		String selection = "Employe ,sum(valeurParMois)  as sumV  ";
		String source = "AmmortissementEmploye ";

		String condition = "status='Publié'  AND Employe=? AND type='Saisie Arret' AND datefromparts(?,?,1) between datefromparts(AmmortissementEmploye.AnneeDepart,AmmortissementEmploye.MoisDepart,1) AND datefromparts(AmmortissementEmploye.AnneeFin,AmmortissementEmploye.MoisFin,1) ";
		String reqSql = "Select " + selection + " FROM " + source + " WHERE " + condition + "group by (Employe) ;";
		Connection connection = DriverManager.getConnection(new SQLDatabaseConnection().getConnectionUrl());
		PreparedStatement statement = connection.prepareStatement(reqSql);
		statement.setObject(1, employeId, Types.INTEGER);

		statement.setObject(2, year.getValue(), Types.INTEGER);
		statement.setObject(3, month.getValue(), Types.INTEGER);

		try {
			statement.execute();

			ResultSet result = statement.getResultSet();
			if (result.next()) {
				;
				return result.getDouble("sumV");
			}

		} catch (Exception e) {

		}
		return 0.00;

	}

	private Double sumRemboursementPret(int employeId, Month month, Year year) throws SQLException {

		String selection = "Employe ,sum(valeurParMois)  as sumV ";
		String source = "AmmortissementEmploye";

		String condition = "status='Publié'  AND Employe=? AND type='Saisie Arret' AND datefromparts(?,?,1) between datefromparts(AmmortissementEmploye.AnneeDepart,AmmortissementEmploye.MoisDepart,1) AND datefromparts(AmmortissementEmploye.AnneeFin,AmmortissementEmploye.MoisFin,1)";
		String reqSql = "Select " + selection + " FROM " + source + " WHERE " + condition + "group by (Employe) ;";
		Connection connection = DriverManager.getConnection(new SQLDatabaseConnection().getConnectionUrl());
		PreparedStatement statement = connection.prepareStatement(reqSql);
		statement.setObject(1, employeId, Types.INTEGER);

		statement.setObject(2, year.getValue(), Types.INTEGER);
		statement.setObject(3, month.getValue(), Types.INTEGER);
		statement.execute();

		ResultSet result = statement.getResultSet();
		try {
			statement.execute();

			if (result.next()) {

				
				return result.getDouble("sumV");
			}

		} catch (Exception e) {

		}
		return 0.00;

	}

	public Double getAffectationCost() {
		return AffectationCost;
	}

	public Double getTotalCost() {
		return totalCost;
	}

	public Double getMABCost() {
		return MABCost;
	}

}
