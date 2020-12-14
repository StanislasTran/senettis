package classes;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.time.Month;
import java.time.Year;

import connexion.SQLDatabaseConnection;

public class Rentabilite {
	private Integer chantier;
	private Month mois;
	private Year year;
	private Double ca;
	private Double coutsEmploye;
	private Double livraison;
	private Double materiel;
	private Double comissions;
	private Double coutRevient;
	private Double margeBrut;
	private Double coutFs;
	private Double pourcentage;

	public Rentabilite(Integer chantier, Month mois, Year year, Double ca, Double coutsEmploye, Double livraison,
			Double materiel, Double coutFs, Double comissions, Double coutRevient, Double margeBrut,
			Double pourcentage) {
		super();
		this.chantier = chantier;
		this.mois = mois;
		this.year = year;
		this.ca = ca;
		this.coutsEmploye = coutsEmploye;
		this.livraison = livraison;
		this.materiel = materiel;
		this.comissions = comissions;
		this.coutRevient = coutRevient;
		this.margeBrut = margeBrut;
		this.pourcentage = pourcentage;
		this.coutFs=coutFs;
	}

	public void insert() throws SQLException {
		String reqSql = "Insert INTO Rentabilite (Chantier,Mois,Annee,Status,CoutsEmploye,CoutsLivraison,CoutMateriel,CoutFournituresSanitaires,Comission,CoutDeRevient,MargeBrut,ChiffreAffaire) VALUES(?,?,?,?,?,?,?,?,?,?,?,?);";
		Connection connection = DriverManager.getConnection(new SQLDatabaseConnection().getConnectionUrl());
		PreparedStatement statement = connection.prepareStatement(reqSql);
		statement.setObject(1, this.chantier, Types.INTEGER);
		statement.setObject(2, this.mois.getValue(), Types.INTEGER);

		statement.setObject(3, this.year.getValue(), Types.INTEGER);
		statement.setObject(4, Status.PUBLISHED.getValue(), Types.VARCHAR);

		statement.setObject(5, this.coutsEmploye, Types.DECIMAL);
		statement.setObject(6, this.livraison, Types.DECIMAL);
		statement.setObject(7, this.materiel, Types.DECIMAL);
		statement.setObject(8, this.coutFs, Types.DECIMAL);

		statement.setObject(9, this.comissions, Types.DECIMAL);
		statement.setObject(10, this.coutRevient, Types.DECIMAL);
		statement.setObject(11, this.margeBrut, Types.DECIMAL);
		statement.setObject(12, this.ca, Types.DECIMAL);

		statement.executeUpdate();
		ResultSet result = statement.getResultSet();
	}

	public void update() throws SQLException {

		if (exist()) {
			String reqSql = "update Rentabilite SET  CoutsEmploye=?,CoutsLivraison=?,CoutMateriel=?,CoutFournituresSanitaires=?,Comission=?,CoutDeRevient=?,MargeBrut=?,ChiffreAffaire=? WHERE Chantier=? AND Mois=? AND Annee=? AND Status=?";
			Connection connection = DriverManager.getConnection(new SQLDatabaseConnection().getConnectionUrl());
			PreparedStatement statement = connection.prepareStatement(reqSql);
			statement.setObject(9, this.chantier, Types.INTEGER);
			statement.setObject(10, this.mois.getValue(), Types.INTEGER);

			statement.setObject(11, this.year.getValue(), Types.INTEGER);
			statement.setObject(12, Status.PUBLISHED.getValue(), Types.VARCHAR);

			statement.setObject(1, this.coutsEmploye, Types.DECIMAL);
			statement.setObject(2, this.livraison, Types.DECIMAL);
			statement.setObject(3, this.materiel, Types.DECIMAL);
			
			statement.setObject(4, this.coutFs, Types.DECIMAL);

			statement.setObject(5, this.comissions, Types.DECIMAL);
			statement.setObject(6, this.coutRevient, Types.DECIMAL);
			statement.setObject(7, this.margeBrut, Types.DECIMAL);
			statement.setObject(8, this.ca, Types.DECIMAL);

			statement.executeUpdate();

		} else
			insert();
	}

	public boolean exist() throws SQLException {
		String reqSql = "Select count (*) as count from Rentabilite where Chantier=? AND Mois=? AND Annee=?";
		Connection connection = DriverManager.getConnection(new SQLDatabaseConnection().getConnectionUrl());
		PreparedStatement statement = connection.prepareStatement(reqSql);
		statement.setObject(1, this.chantier, Types.INTEGER);
		statement.setObject(2, this.mois.getValue(), Types.INTEGER);

		statement.setObject(3, this.year.getValue(), Types.INTEGER);

		statement.execute();

		ResultSet result = statement.getResultSet();
		result.next();

		return result.getInt("count") > 0;

	}
}
