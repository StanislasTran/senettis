package classes;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import connexion.SQLDatabaseConnexion;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;

//to do (comment and translate)


/**
 * 
 * Represent product by Delivery into the database
 *
 */
public class ProductByDelivery {

	private Integer idLivraison;
	private Integer idProduit;
	private int quantite;
	private String status;
	private int produitParLivraisonId;

	public int insertDatabase() throws SQLException {
		String reqSql = "INSERT INTO ProduitParLivraison(livraison,produit,quantite,status) VALUES (?,?,?,?)";

		Connection connection = DriverManager.getConnection(new SQLDatabaseConnexion().getConnectionUrl());
		PreparedStatement statement = connection.prepareStatement(reqSql);
		statement.setObject(1, this.idLivraison, Types.INTEGER);
		statement.setObject(2, this.idProduit, Types.INTEGER);
		statement.setObject(3, this.quantite, Types.DECIMAL);
		statement.setObject(4, this.status, Types.VARCHAR);
		return statement.executeUpdate();
	}

	public int updateDatabase() throws SQLException {
		String reqSql = "UPDATE ProduitParLivraison SET Produit=?, Livraison=?, quantite=?, status=? WHERE produitParLivraisonId=?;";

		Connection connection = DriverManager.getConnection(new SQLDatabaseConnexion().getConnectionUrl());
		PreparedStatement statement = connection.prepareStatement(reqSql);
		statement.setObject(1, this.idProduit.toString(), Types.INTEGER);
		statement.setObject(2, this.idLivraison, Types.INTEGER);
		statement.setObject(3, this.quantite, Types.DECIMAL);
		statement.setObject(4, this.status, Types.VARCHAR);
		statement.setObject(5, this.produitParLivraisonId, Types.INTEGER);

		return statement.executeUpdate();
	}

	private static Statement selectAllProduitParLivraison() throws SQLException {
		String reqSql = "SELECT * FROM ProduitParLivraison";

		Connection connection = DriverManager.getConnection(new SQLDatabaseConnexion().getConnectionUrl());
		Statement statement = connection.createStatement();
		statement.executeQuery(reqSql);
		return statement;
	}

	public static List<ProductByDelivery> getAllProduitParLivraison() throws SQLException {

		ResultSet result = selectAllProduitParLivraison().getResultSet();
		List<ProductByDelivery> allProduitParLivraison = new ArrayList<ProductByDelivery>();

		while (result.next()) {
			int produitParLivraisonId = result.getInt("ProduitParLivraisonId");
			int produitId = result.getInt("Produit");
			int livraisonId = result.getInt("Livraison");
			int quantite = result.getInt("Quantite");
			String status = result.getString("Status");
			allProduitParLivraison
					.add(new ProductByDelivery(produitParLivraisonId, livraisonId, produitId, quantite, status));

		}

		return allProduitParLivraison;
	}

	public static List<ProductByDelivery> getProductByLivraisonByLivraisonId(int livraisonId) throws SQLException {
		String reqSql = "SELECT ProduitParLivraisonId,Livraison,Produit,quantite,Status FROM ProduitParLivraison WHERE Livraison=?;";
		Connection connection = DriverManager.getConnection(new SQLDatabaseConnexion().getConnectionUrl());
		PreparedStatement statement = connection.prepareStatement(reqSql);
		statement.setObject(1, livraisonId, Types.INTEGER);
		statement.executeQuery();
		ResultSet result = statement.getResultSet();
		List<ProductByDelivery> results = new ArrayList<>();

		while (result.next()) {
			int produitParLivraisonId = result.getInt("produitParLivraisonId");
			livraisonId = result.getInt("Livraison");
			int produitId = result.getInt("Produit");
			int quantite = result.getInt("quantite");
			String status = result.getString("status");

			results.add(new ProductByDelivery(produitParLivraisonId, livraisonId, produitId, quantite, status));

		}

		if (results.isEmpty()) {
			throw new SQLException("Data not found");
		}
		return results;

	}

	public static ProductByDelivery getProductByLivraisonByLivraisonIdAndProductId(int livraisonId, int produitId)
			throws SQLException {
		String reqSql = "SELECT ProduitParLivraisonId,Livraison,Produit,quantite,Status FROM ProduitParLivraison WHERE Livraison=? and Produit=?;";
		Connection connection = DriverManager.getConnection(new SQLDatabaseConnexion().getConnectionUrl());
		PreparedStatement statement = connection.prepareStatement(reqSql);
		statement.setObject(1, livraisonId, Types.INTEGER);
		statement.setObject(2, produitId, Types.INTEGER);
		statement.executeQuery();
		ResultSet result = statement.getResultSet();

		if (result.next()) {
			int produitParLivraisonId = result.getInt("produitParLivraisonId");
			livraisonId = result.getInt("Livraison");
			produitId = result.getInt("Produit");
			int quantite = result.getInt("quantite");
			String status = result.getString("status");

			return (new ProductByDelivery(produitParLivraisonId, livraisonId, produitId, quantite, status));

		} else {
			throw new SQLException("Data not found");
		}

	}

	public static void printAllProduitParLivraison() throws SQLException {

		List<ProductByDelivery> allProduitParLivraison = getAllProduitParLivraison();

		for (ProductByDelivery produitParLivraison : allProduitParLivraison)
			System.out.println(produitParLivraison);
	}

	@Override
	public String toString() {

		return "" + this.produitParLivraisonId + "|" + this.idProduit + "|" + this.idLivraison + "|" + this.quantite
				+ "|" + this.status;
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

	public ProductByDelivery(Integer produitParLivraisonId, Integer idLivraison, Integer idProduit, Integer quantite,
			String status) {
		this(idLivraison, idProduit, quantite, status);
		this.produitParLivraisonId = produitParLivraisonId;

	}

	public ProductByDelivery(Integer idLivraison, Integer idProduit, Integer quantite, String status) {
		this(idLivraison, idProduit, status);
		this.quantite = quantite;

	}

	public ProductByDelivery(Integer idLivraison, Integer idProduit, String status) {
		super();
		this.idLivraison = idLivraison;
		this.idProduit = idProduit;
		this.status = status;
		this.quantite = 1;
	}

}
