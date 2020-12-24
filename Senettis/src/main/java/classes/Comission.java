package classes;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.time.Month;
import java.time.Year;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import connexion.SQLDatabaseConnection;

public class Comission {
	private Integer comissionId;
	private Double comission;
	private Integer chantier;
	private Month startMonth;
	private Year startYear;
	private Status status;

	public Comission(Double comission, Integer chantier, Month startMonth, Year startYear, Status status) {
		super();
		this.comission = comission;
		this.chantier = chantier;
		this.startMonth = startMonth;
		this.startYear = startYear;
		this.status = status;
	}

	public Comission(Integer comissionId, Double comission, Integer chantier, Month startMonth, Year startYear,
			Status status) {
		this(comission, chantier, startMonth, startYear, status);
		this.comissionId = comissionId;
	}

	public static ResultSet getComissionsResultSet(Integer chantier, Month startMonth, Year startYear)
			throws SQLException {
		String reqSql = "Select * FROM Comission Where Status ='Publié' AND chantier=? AND MoisDebut=? AND AnneeDebut=?";

		Connection connection = DriverManager.getConnection(new SQLDatabaseConnection().getConnectionUrl());
		PreparedStatement statement = connection.prepareStatement(reqSql);
		statement.setObject(1, chantier, Types.INTEGER);
		statement.setObject(2, startMonth.getValue(), Types.INTEGER);
		statement.setObject(3, startYear, Types.INTEGER);

		statement.executeUpdate();
		return statement.getResultSet();

	}
	
	/**
	 * 
	 * @param chantier
	 * @param startMonth
	 * @param startYear
	 * @return
	 * @throws SQLException
	 */
	
	public static ResultSet getAllComissionsAfterResultSet( Month month, Year year)
			throws SQLException {
		String reqSql = "select chantier,Sum(comission) as SUM from Comission WHERE status ='Publié' AND AnneeDebut<? OR (AnneeDebut=? AND MoisDebut<=?) GROUP BY chantier;";

		Connection connection = DriverManager.getConnection(new SQLDatabaseConnection().getConnectionUrl());
		PreparedStatement statement = connection.prepareStatement(reqSql);
		statement.setObject(1, year.getValue(), Types.INTEGER);
		
		statement.setObject(2, year.getValue(), Types.INTEGER);
		statement.setObject(3, month.getValue(), Types.INTEGER);
		statement.execute();
		return statement.getResultSet();

	}
	
	/**
	 * return a map with key=siteId and Double the comission if the comission is applied on the date month,year
	 * @param chantier
	 * @param month
	 * @param year
	 * @return
	 * @throws SQLException
	 */
	
	public static Map<Integer, Double> getAllComissionsAfterMap( Month month, Year year)
			throws SQLException {
		ResultSet result = getAllComissionsAfterResultSet( month, year);
		HashMap<Integer,Double> comissions = new HashMap<Integer, Double>();
		while (result.next()) {

			comissions.put(result.getInt("chantier"), result.getDouble("SUM"));

		}

		return comissions;
	}

	


	public static List<Comission> getComissionsList(int chantier, Month startMonth, Year startYear)
			throws SQLException {
		ResultSet result = getComissionsResultSet(chantier, startMonth, startYear);
		List<Comission> comissions = new ArrayList<Comission>();
		while (result.next()) {

			comissions.add(new Comission(result.getDouble("Comission"), result.getInt("chantier"),
					Month.of(result.getInt("MoisDebut")), Year.of(result.getInt("AnneeDebut")),
					Status.valueOf(result.getString("Status"))));

		}

		return comissions;
	}

	public static ResultSet getComissionsResultSet() throws SQLException {
		String reqSql = "Select Comission.comissionId,Comission.comission,Comission.MoisDebut,Comission.AnneeDebut,Chantier.ChantierId,Chantier.Nom FROM Comission JOIN Chantier ON Chantier.ChantierId=Comission.Chantier Where Chantier.Status ='Publié' AND Comission.Status='Publié'";

		Connection connection = DriverManager.getConnection(new SQLDatabaseConnection().getConnectionUrl());
		PreparedStatement statement = connection.prepareStatement(reqSql);

		statement.execute();
		return statement.getResultSet();

	}

	public void insert() throws SQLException {
		String reqSql = "INSERT INTO Comission(Comission,Chantier,MoisDebut,AnneeDebut,Status) values  (?,?,?,?,?);";

		Connection connection = DriverManager.getConnection(new SQLDatabaseConnection().getConnectionUrl());
		PreparedStatement statement = connection.prepareStatement(reqSql);

		
		statement.setObject(1, this.comission, Types.DECIMAL);
		statement.setObject(2, this.chantier, Types.INTEGER);
		statement.setObject(3, this.startMonth.getValue(), Types.INTEGER);
		statement.setObject(4, this.startYear.getValue(), Types.INTEGER);
		statement.setObject(5, this.status.getValue(), Types.VARCHAR);
		statement.executeUpdate();

	}

	public static Double getComissionSum(Integer siteId, Month startMonth, Year startYear) throws SQLException {
		Double totalCommision = 0.00;

		String reqSql = "Select Sum(comission) as Sum FROM Comission where chantier=? AND (AnneeDebut<? OR (AnneeDebut=? AND MoisDebut<=?))";

		Connection connection = DriverManager.getConnection(new SQLDatabaseConnection().getConnectionUrl());
		PreparedStatement statement = connection.prepareStatement(reqSql);
		statement.setObject(1, siteId, Types.INTEGER);
		statement.setObject(2, startYear.getValue(), Types.INTEGER);
		statement.setObject(3, startYear.getValue(), Types.INTEGER);
		statement.setObject(4, startMonth.getValue(), Types.INTEGER);
		statement.execute();
		ResultSet result = statement.getResultSet();
		
		while(result.next()) {
			totalCommision+=result.getDouble("Sum");
		}

		return totalCommision;
	}

	public void Delete() throws SQLException {
		String reqSql = "Update  Comission SET Status='Archivé' WHERE ComissionId= .;";

		Connection connection = DriverManager.getConnection(new SQLDatabaseConnection().getConnectionUrl());
		PreparedStatement statement = connection.prepareStatement(reqSql);

		statement.setObject(1, this.comissionId, Types.INTEGER);

		statement.executeUpdate();

	}

	public static void removeById(int productId) throws SQLException {
		String reqSql = "Update  Comission SET Status='Archivé' WHERE ComissionId= ?;";

		Connection connection = DriverManager.getConnection(new SQLDatabaseConnection().getConnectionUrl());
		PreparedStatement statement = connection.prepareStatement(reqSql);

		statement.setObject(1, productId, Types.INTEGER);

		statement.executeUpdate();

	}

}
