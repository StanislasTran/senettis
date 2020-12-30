
package classes;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.microsoft.sqlserver.jdbc.StringUtils;

import connexion.SQLDatabaseConnection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Month;
import java.time.Year;

public class TurnOver {

	private Integer chantierId;
	private Month month;
	private Year year;
	private Double menage;
	private Double vitrerie;
	private Double fournituresSanitaires;
	private Double misesBlanc;
	private Double autres;
	private Double CA;
	private String siteName;
	private Status status;

	///////////////////////// a verifier

	public TurnOver(Integer chantierId, Month mois, Year annee, Status status) {
		super();

		this.chantierId = chantierId;
		this.month = mois;
		this.year = annee;
		this.status = status;
	}

	public TurnOver(Integer chantierId, Month mois, Year annee, Double menage, Double vitrerie,
			Double fournituresSanitaires, Double misesBlanc, Double autres, Double CA, Status status) {
		this(chantierId, mois, annee, status);

		this.chantierId = chantierId;
		this.month = mois;
		this.year = annee;
		this.menage = menage;
		this.vitrerie = vitrerie;
		this.fournituresSanitaires = fournituresSanitaires;
		this.misesBlanc = misesBlanc;
		this.autres = autres;
		this.CA = CA;

	}

	public TurnOver(Integer chantierId, Month mois, Year annee, Double menage, Double vitrerie,
			Double fournituresSanitaires, Double misesBlanc, Double autres, Double CA, Status status, String siteName) {
		this(chantierId, mois, annee, menage, vitrerie, fournituresSanitaires, misesBlanc, autres, CA, status);
		this.siteName = siteName;

	}

	// Liens avec la BDD-----------------------------------------------
	public int insertDatabase() throws SQLException {
		String reqSql = "INSERT INTO ChiffreAffaire(Mois, Annee,Chantier,   Menage,Vitrerie,Mise_a_blanc,Fournitures_sanitaires,Autres,CA,Status) VALUES (?,?,?,?,?,?,?,?,?)";

		Connection connection = DriverManager.getConnection(new SQLDatabaseConnection().getConnectionUrl());
		PreparedStatement statement = connection.prepareStatement(reqSql);
		statement.setObject(1, this.month, Types.INTEGER);
		statement.setObject(2, this.year, Types.INTEGER);
		statement.setObject(3, this.chantierId, Types.INTEGER);
		statement.setObject(8, this.menage, Types.DECIMAL);
		statement.setObject(9, this.vitrerie, Types.DECIMAL);
		statement.setObject(10, this.misesBlanc, Types.DECIMAL);
		statement.setObject(11, this.fournituresSanitaires, Types.DECIMAL);
		statement.setObject(12, this.autres, Types.DECIMAL);
		statement.setObject(13, this.status, Types.VARCHAR);

		return statement.executeUpdate();
	}

	/**
	 * return a resultSet which contains CA Data for each site (left joint) When
	 * data don't exist return null
	 * 
	 * @param month
	 * @param year
	 * @return
	 * @throws SQLException
	 */

	public static ResultSet getCAForAllSite(Month month, Year year) throws SQLException {
		String selection = " Chantier.nom,Chantier.chantierId, CA.Mois, CA.Annee,CA.Chantier,  CA.Menage,CA.Vitrerie,CA.Mise_a_blanc,CA.Fournitures_sanitaires,CA.Autres,CA.CA,CA.Status";
		String sources = "Chantier Left join (Select * FROM ChiffreAffaire WHERE Mois=? AND Annee = ?) AS CA ON CA.Chantier=Chantier.ChantierId WHERE Chantier.Status='Publié'";
		Connection connection = DriverManager.getConnection(new SQLDatabaseConnection().getConnectionUrl());
		String reqSql = "Select " + selection + " FROM " + sources;
		PreparedStatement statement = connection.prepareStatement(reqSql);
		statement.setObject(1, month.getValue(), Types.INTEGER);
		statement.setObject(2, year.getValue(), Types.INTEGER);

		statement.execute();

		return statement.getResultSet();

	}

	public static List<TurnOver> getListCAForAllSite(Month month, Year year) throws SQLException {
		ResultSet result = getCAForAllSite(month, year);
		List<TurnOver> allCA = new ArrayList<TurnOver>();

		while (result.next()) {
			Integer chantierId = result.getInt("chantierId");
			Month mois = month;
			Year annee = year;
			Double menage = result.getDouble("Menage");
			Double vitrerie = result.getDouble("Vitrerie");
			Double fournituresSanitaires = result.getDouble("Fournitures_sanitaires");
			Double misesBlanc = result.getDouble("Mise_a_blanc");
			Double autres = result.getDouble("autres");

			Double CA = result.getDouble("CA");

			Status status = Status.DRAFT;
			if (!Objects.isNull(result.getString("status")))

				status = Status.getStatus(result.getString("status"));

			String siteName = result.getString("nom");
			allCA.add(new TurnOver(chantierId, mois, annee, menage, vitrerie, fournituresSanitaires, misesBlanc, autres,
					CA, status, siteName));

		}

		return allCA;
	}
	
	
	
	public static TurnOver getTurnOverByDateAndSite(int chantierId, int mois, int annee) throws SQLException {
		String reqSql = "SELECT Mois, Annee,Chantier,Menage,Vitrerie,Mise_a_blanc,Fournitures_sanitaires,Autres,CA,Status FROM ChiffreAffaire WHERE chantier=? and mois=? and annee=?";
		Connection connection = DriverManager.getConnection(new SQLDatabaseConnection().getConnectionUrl());
		PreparedStatement statement = connection.prepareStatement(reqSql);
		statement.setObject(1, chantierId, Types.INTEGER);
		statement.setObject(2, mois, Types.INTEGER);
		statement.setObject(3, annee, Types.INTEGER);
		statement.executeQuery();

		ResultSet result = statement.getResultSet();

		if (result.next()) {
			Double menage = result.getDouble("Menage");
			Double vitrerie = result.getDouble("Vitrerie");
			Double fournituresSanitaires = result.getDouble("Fournitures_sanitaires");
			Double misesBlanc = result.getDouble("Mise_a_blanc");
			Double autres = result.getDouble("autres");

			Double CA = result.getDouble("CA");

			Status status = Status.DRAFT;
			if (!Objects.isNull(result.getString("status"))) {
				status = Status.getStatus(result.getString("status"));
			}

			
			return new TurnOver(chantierId, Month.of(mois), Year.of(annee), menage, vitrerie, fournituresSanitaires, misesBlanc, autres,
					CA, status);
		} else {
			throw new SQLException("Data not found");
		}
	}
	
	
	

	public String getSiteName() {
		return this.siteName;
	}

	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}

	public Month getMois() {
		return month;
	}

	public void setMois(Month mois) {
		this.month = mois;
	}

	public Year getAnnee() {
		return year;
	}

	public double getCa() {
		return this.CA;
	}

	public void setAnnee(Year annee) {
		this.year = annee;
	}

	public Double getMenage() {
		return menage;
	}

	public void setMenage(Double menage) {
		this.menage = menage;
	}

	public Double getVitrerie() {
		return vitrerie;
	}

	public void setVitrerie(Double vitrerie) {
		this.vitrerie = vitrerie;
	}

	public Double getFournituresSanitaires() {
		return fournituresSanitaires;
	}

	public void setFournituresSanitaires(Double fournituresSanitaires) {
		this.fournituresSanitaires = fournituresSanitaires;
	}

	public Double getMisesBlanc() {
		return misesBlanc;
	}

	public void setMisesBlanc(Double misesBlanc) {
		this.misesBlanc = misesBlanc;
	}

	public Double getAutres() {
		return autres;
	}

	public void setAutres(Double autres) {
		this.autres = autres;
	}

	public Integer getChantierId() {
		return this.chantierId;
	}

	public void setCA(Double CA) {
		this.CA = CA;

	}

	public boolean exist() throws SQLException {
		String reqSql = "Select count(*) AS count FROM ChiffreAffaire WHERE mois=? AND annee=? AND chantier=?";
		Connection connection = DriverManager.getConnection(new SQLDatabaseConnection().getConnectionUrl());
		PreparedStatement statement = connection.prepareStatement(reqSql);
		statement.setObject(1, this.month.getValue(), Types.INTEGER);
		statement.setObject(2, this.year.getValue(), Types.INTEGER);
		statement.setObject(3, this.chantierId, Types.INTEGER);

		statement.execute();
		ResultSet result = statement.getResultSet();
	
		if (result.next()) {
			
			return result.getInt("count") > 0;
		} else
			return false;

	}

	public void inserOrUpdateRow() throws SQLException {

		if (!exist()) {
			insert();
		
		} else {
			update();
			
		}

	}

	public void insert() throws SQLException {
		String insert = "ChiffreAffaire (mois,annee,chantier,CA,menage,vitrerie,fournitures_sanitaires,mise_a_blanc,autres,Status)";
		String values = " (?,?,?,?,?,?,?,?,?,?) ";
		String reqSql = "insert INTO " + insert + " VALUES " + values;
		Connection connection = DriverManager.getConnection(new SQLDatabaseConnection().getConnectionUrl());
		PreparedStatement statement = connection.prepareStatement(reqSql);

		statement.setObject(1, this.month.getValue(), Types.INTEGER);
		statement.setObject(2, this.year.getValue(), Types.INTEGER);
		statement.setObject(3, this.chantierId, Types.INTEGER);
		statement.setObject(4, this.CA, Types.DECIMAL);
		statement.setObject(5, this.menage, Types.DECIMAL);
		statement.setObject(6, this.vitrerie, Types.DECIMAL);
		statement.setObject(7, this.fournituresSanitaires, Types.DECIMAL);
		statement.setObject(8, this.misesBlanc, Types.DECIMAL);
		statement.setObject(9, this.autres, Types.DECIMAL);
		statement.setObject(10, "Publié", Types.VARCHAR);

		statement.execute();
	}

	public void update() throws SQLException {

		String set = " CA=?,menage=?,vitrerie=?,fournitures_sanitaires=?,mise_a_blanc=?,autres=?";
		String reqSql = "UPDATE ChiffreAffaire SET " + set + " Where mois=? AND annee=? AND chantier=?";
		Connection connection = DriverManager.getConnection(new SQLDatabaseConnection().getConnectionUrl());
		PreparedStatement statement = connection.prepareStatement(reqSql);

		statement.setObject(1, this.CA, Types.DECIMAL);
		statement.setObject(2, this.menage, Types.DECIMAL);
		statement.setObject(3, this.vitrerie, Types.DECIMAL);
		statement.setObject(4, this.fournituresSanitaires, Types.DECIMAL);
		statement.setObject(5, this.misesBlanc, Types.DECIMAL);
		statement.setObject(6, this.autres, Types.DECIMAL);
		statement.setObject(7, this.month.getValue(), Types.INTEGER);
		statement.setObject(8, this.year.getValue(), Types.INTEGER);
		statement.setObject(9, this.chantierId, Types.INTEGER);
		statement.execute();
	}

}
