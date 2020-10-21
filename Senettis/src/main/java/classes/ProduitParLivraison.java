package classes;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import connexion.SQLDatabaseConnection;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;

public class ProduitParLivraison {

	private Integer idLivraison;
	private Integer idProduit;
	private int quantite;
	private String status;
	private int produitParLivraisonId;


	
	public int insertDatabase() throws SQLException {
		String reqSql = "INSERT INTO ProduitParLivraison(livraison,produit,quantite,status) VALUES (?,?,?,?)";
		
		Connection connection = DriverManager.getConnection(new SQLDatabaseConnection().getConnectionUrl());
		PreparedStatement statement = connection.prepareStatement(reqSql);
		statement.setObject(1,this.idLivraison,Types.INTEGER);
		statement.setObject(2,this.idProduit,Types.INTEGER);
		statement.setObject(3,this.quantite,Types.DECIMAL);
		statement.setObject(4,this.status,Types.VARCHAR);
		return statement.executeUpdate();
	}

	public int updateDatabase() throws SQLException {
		String reqSql = "UPDATE ProduitParLivraison SET Produit=?, Livraison=?, quantite=?, status=? WHERE produitParLivraisonId=?;";
		
		Connection connection = DriverManager.getConnection(new SQLDatabaseConnection().getConnectionUrl());
		PreparedStatement statement = connection.prepareStatement(reqSql);
		statement.setObject(1,this.idProduit.toString(),Types.INTEGER);
		statement.setObject(2,this.idLivraison,Types.INTEGER);
		statement.setObject(3,this.quantite,Types.DECIMAL);
		statement.setObject(4,this.status,Types.VARCHAR);
		statement.setObject(5,this.produitParLivraisonId,Types.INTEGER);
		
		return statement.executeUpdate();
	}
	
	
	private static Statement selectAllProduitParLivraison() throws SQLException {
		String reqSql = "SELECT * FROM ProduitParLivraison";
		
		Connection connection = DriverManager.getConnection(new SQLDatabaseConnection().getConnectionUrl());
		Statement statement = connection.createStatement();
		statement.executeQuery(reqSql);
		return statement;
	}
	
	public static List<ProduitParLivraison> getAllProduitParLivraison() throws SQLException {

		ResultSet result=selectAllProduitParLivraison().getResultSet();
		List<ProduitParLivraison> allProduitParLivraison=new ArrayList<ProduitParLivraison>();
		System.out.println("Id|ProduitId|LivraisonId|Quantite|Status");
		while(result.next()) {
			int produitParLivraisonId=result.getInt("ProduitParLivraisonId");
			int produitId=result.getInt("Produit");
			int livraisonId=result.getInt("Livraison");
			int quantite=result.getInt("Quantite");
		    String status=result.getString("Status");
		   allProduitParLivraison.add(new ProduitParLivraison(produitParLivraisonId, livraisonId, produitId, quantite, status));
		  
			
		}
		
		return allProduitParLivraison;
	}
	
	public static void printAllProduitParLivraison() throws SQLException {
		
		List<ProduitParLivraison> allProduitParLivraison=getAllProduitParLivraison();
	
		for (ProduitParLivraison produitParLivraison : allProduitParLivraison)	
			System.out.println(produitParLivraison);
	}
	
	@Override
	public String toString() {
		
		return ""+this.produitParLivraisonId+"|"+this.idProduit+"|"+this.idLivraison+"|"+this.quantite+"|"+this.status;
	}

	public Integer getIdLivraison() {
		return idLivraison;
	}



	public void setIdLivraison(Integer idLivraison) {
		if (idLivraison == null) {
			throw new Error("setIdLivraison : le idLivraison indique est vide");
		}
		this.idLivraison = idLivraison;
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



	public Integer getQuantite() {
		return quantite;
	}



	public void setQuantite(Integer quantite) {
		if (quantite == null) {
			throw new Error("setQuantite : le quantite indique est vide");
		}
		this.quantite = quantite;
	}


	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		if (status == null) {
			throw new Error("setStatus : le status indique est vide");
		}
		this.status = status;
	}

	public int getProduitParLivraisonId() {
		return produitParLivraisonId;
	}

	public void setProduitParLivraisonId(Integer produitParLivraisonId) {
		if (produitParLivraisonId == null) {
			throw new Error("setProduitParLivraisonId : le produitParLivraisonId indique est vide");
		}
		this.produitParLivraisonId = produitParLivraisonId;
	}

	public void setQuantite(int quantite) {
		this.quantite = quantite;
	}

	public ProduitParLivraison(Integer produitParLivraisonId, Integer idLivraison, Integer idProduit, Integer quantite,String status) {
		this(idLivraison, idProduit, quantite, status);
		this.produitParLivraisonId = produitParLivraisonId;
		
	}

	public ProduitParLivraison(Integer idLivraison, Integer idProduit, Integer quantite,String status) {
		this(idLivraison, idProduit, status);
		this.quantite = quantite;
		
	}



	public ProduitParLivraison(Integer idLivraison, Integer idProduit,String status) {
		super();
		this.idLivraison = idLivraison;
		this.idProduit = idProduit;
		this.status=status;
		this.quantite = 1;
	}


	
}
