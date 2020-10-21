package classes;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import connexion.SQLDatabaseConnection;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class Livraison {

	private Integer idChantier;
	private Integer idProduit;
	private Double prixTotal;
	private Date date;
	private Object status;
	private int livraisonId;


	
	public int insertDatabase() throws SQLException {
		String reqSql = "INSERT INTO Livraison(chantier,produit,date,prixTotal,status) VALUES (?,?,?,?,?)";
		
		Connection connection = DriverManager.getConnection(new SQLDatabaseConnection().getConnectionUrl());
		PreparedStatement statement = connection.prepareStatement(reqSql);
		statement.setObject(1,this.idChantier,Types.INTEGER);
		statement.setObject(2,this.idProduit,Types.INTEGER);
		statement.setObject(3,this.date,Types.DATE);
		statement.setObject(4,this.prixTotal,Types.DECIMAL);
		statement.setObject(5,this.status,Types.VARCHAR);
		
		return statement.executeUpdate();
	}


	public int updateDatabase() throws SQLException {
		String reqSql = "UPDATE Livraison SET Produit=?, Chantier=?, prix_total=?, date=?, status=? WHERE LivraisonId=?;";
		
		Connection connection = DriverManager.getConnection(new SQLDatabaseConnection().getConnectionUrl());
		PreparedStatement statement = connection.prepareStatement(reqSql);
		statement.setObject(1,this.idProduit.toString(),Types.INTEGER);
		statement.setObject(2,this.idChantier,Types.INTEGER);
		statement.setObject(3,this.prixTotal,Types.DECIMAL);
		statement.setObject(4,this.date,Types.DATE);
		statement.setObject(5,this.status,Types.VARCHAR);
		statement.setObject(6,this.livraisonId,Types.INTEGER);
		
		return statement.executeUpdate();
	}
	
	
	private static Statement selectAllLivraison() throws SQLException {
		String reqSql = "SELECT * FROM Livraison";
		
		Connection connection = DriverManager.getConnection(new SQLDatabaseConnection().getConnectionUrl());
		Statement statement = connection.createStatement();
		statement.executeQuery(reqSql);
		return statement;
	}
	
	public static List<Livraison> getAllLivraison() throws SQLException {

		ResultSet result=selectAllLivraison().getResultSet();
		List<Livraison> allLivraison=new ArrayList<Livraison>();
		System.out.println("Id|ProduitId|ChantierId|PrixTotal|Date|Status");
		while(result.next()) {
			int livraisonId=result.getInt("LivraisonId");
			int produitId=result.getInt("Produit");
			int chantierId=result.getInt("Chantier");
			Double prixTotal=result.getDouble("PrixTotal");
			Date date=result.getDate("Date");
		    String status=result.getString("Status");
		   allLivraison.add(new Livraison(livraisonId, chantierId, produitId, prixTotal, date, status));
		  
			
		}
		
		return allLivraison;
	}
	
	public static void printAllLivraison() throws SQLException {
		
		List<Livraison> allLivraison=getAllLivraison();
	
		for (Livraison livraison : allLivraison)	
			System.out.println(livraison);
	}
	
	@Override
	public String toString() {
		
		return ""+this.livraisonId+"|"+this.idProduit+"|"+this.idChantier+"|"+this.prixTotal+"|"+this.date+"|"+this.status;
	}

	public Livraison(Integer idChantier, Integer idProduit, Double prixTotal, String date, String status) {
		this(idChantier, idProduit, prixTotal, date);
		this.status=status;
		
	}

	public Livraison(Integer livraisonId, Integer idChantier, Integer idProduit, Double prixTotal, Date date, String status) {
		this.livraisonId = livraisonId;
		this.idChantier = idChantier;
		this.idProduit = idProduit;
		this.prixTotal = prixTotal;
		this.date = date;
		this.status=status;
		
	}

	public Livraison(Integer idChantier, Integer idProduit, Double prixTotal, String date) {
		super();
		this.idChantier = idChantier;
		this.idProduit = idProduit;
		this.prixTotal = prixTotal;
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
	    Date dateFinale = null;
		try {
			dateFinale = simpleDateFormat.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		this.date = dateFinale;
	}



	public Integer getIdChantier() {
		return idChantier;
	}



	public void setIdChantier(Integer idChantier) {
		if (idChantier == null) {
			throw new Error("setIdChantier : le idChantier indique est vide");
		}
		this.idChantier = idChantier;
	}



	public Integer getIdProduit() {
		return idProduit;
	}



	public void setIdProduit(Integer idProduit) {
		if (idProduit == null) {
			throw new Error("setIdProduit : le idProduit indique est vide");
		}
		this.idProduit = idProduit;
	}



	public Double getPrixTotal() {
		return prixTotal;
	}



	public void setPrixTotal(Double prixTotal) {
		if (prixTotal == null) {
			throw new Error("setPrixTotal : le prixTotal indique est vide");
		}
		this.prixTotal = prixTotal;
	}



	public Date getDate() {
		return date;
	}



	public void setDate(Date date) {
		if (date == null) {
			throw new Error("setDate : le date indique est vide");
		}
		this.date = date;
	}

}
