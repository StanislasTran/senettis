package classes;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import connexion.SQLDatabaseConnection;

public class Product {

	private int productId;
	private String name;
	private Double price;
	private String comment;
	private String status;

	/**
	 * Constructor for Produit
	 * 
	 * @param <type>Double</type> price
	 * @param <type>              String </type> name
	 * @param <type>String</type> comment
	 * @param <type>String</type> status
	 */
	public Product(String name, Double price, String comment, String status) {
		this(name, price, status);
		this.comment = comment;

	}

	/*****************************************
	 * 
	 * Constructors
	 * 
	 ****************************************/

	/***
	 * Constructor
	 * 
	 * @param <type> String </type> name </String>
	 * @param <type> int /<type> price
	 * @param status
	 */
	public Product(String name, Double price, String status) {
		this.name = name;
		this.price = price;
		this.status = status;
	}

	/***
	 * Constructor
	 * 
	 * @param nom
	 * @param prix
	 * @param status
	 */
	public Product(int produitId, String nom, Double prix, String commentaires, String status) {
		this(nom, prix, commentaires, status);
		this.productId = produitId;

	}

	/****************************************
	 * 
	 * Queries
	 * 
	 ***************************************/

	/**
	 * Insert the product into the database
	 * 
	 * @return 0 if the request return no result
	 * 
	 * @throws SQLException
	 */
	public int insertDatabase() throws SQLException {
		String reqSql = "INSERT INTO Produit(Nom,Prix,Commentaires,Status) VALUES (?,?,?,?)";

		Connection connection = DriverManager.getConnection(new SQLDatabaseConnection().getConnectionUrl());
		PreparedStatement statement = connection.prepareStatement(reqSql);
		statement.setObject(1, this.name, Types.VARCHAR);
		statement.setObject(2, this.price, Types.DECIMAL);
		statement.setObject(3, this.comment, Types.VARCHAR);
		statement.setObject(4, this.status, Types.VARCHAR);

		return statement.executeUpdate();
	}

	/**
	 * Select all Product in the database and return the result
	 * 
	 * @return
	 * @throws SQLException
	 */
	private static Statement selectAllProduct() throws SQLException {
		String reqSql = "SELECT * FROM Produit";

		Connection connection = DriverManager.getConnection(new SQLDatabaseConnection().getConnectionUrl());
		Statement statement = connection.createStatement();
		statement.executeQuery(reqSql);
		return statement;
	}

	/**
	 * Select all published product in the database and return the result
	 * 
	 * @return <type> Statement</type> statement which contain the request result
	 * @throws SQLException
	 */
	private static Statement selectAllPublished() throws SQLException {
		String reqSql = "SELECT * FROM Produit WHERE Status='Publi�'";

		Connection connection = DriverManager.getConnection(new SQLDatabaseConnection().getConnectionUrl());
		Statement statement = connection.createStatement();
		statement.executeQuery(reqSql);
		return statement;
	}

	/**
	 * return the list of all published product stored in the database
	 * 
	 * @return <type> List<product> </type>
	 * @throws SQLException
	 */

	public static List<Product> getAllPublished() throws SQLException {

		ResultSet result = selectAllPublished().getResultSet();
		List<Product> allProduct = new ArrayList<Product>();
		while (result.next()) {
			int produitId = result.getInt("ProduitId");
			String name = result.getString("Nom");
			Double price = result.getDouble("Prix");
			String comment = result.getString("Commentaires");
			String status = result.getString("Status");
			allProduct.add(new Product(produitId, name, price, comment, status));

		}

		return allProduct;
	}

	/**
	 * return the list of all product stored in the database
	 * 
	 * @return <type> List<product> </type>
	 * @throws SQLException
	 */

	public static List<Product> getAllProduct() throws SQLException {

		ResultSet result = selectAllProduct().getResultSet();
		List<Product> allProduct = new ArrayList<Product>();
		while (result.next()) {
			int produitId = result.getInt("ProduitId");
			String name = result.getString("Nom");
			Double price = result.getDouble("Prix");
			String comment = result.getString("Commentaires");
			String status = result.getString("Status");
			allProduct.add(new Product(produitId, name, price, comment, status));

		}

		return allProduct;
	}

	/**
	 * print all product in the database
	 * 
	 * @throws SQLException
	 */
	public static void printAllProduct() throws SQLException {

		List<Product> allProduct = getAllProduct();

		for (Product produit : allProduct)
			System.out.println(produit);
	}

	/**
	 * Met � jour le produit dans la base de donn�es en utlisant sont Identifiant
	 * 
	 * @return <type>int</type>
	 * @throws SQLException
	 */
	public int updateDatabase() throws SQLException {
		String reqSql = "UPDATE Produit SET nom=?, prix=?, commentaires=?, status=? WHERE ProduitId=?;";

		Connection connection = DriverManager.getConnection(new SQLDatabaseConnection().getConnectionUrl());
		PreparedStatement statement = connection.prepareStatement(reqSql);
		statement.setObject(1, this.name.toString(), Types.VARCHAR);
		statement.setObject(2, this.price, Types.DECIMAL);
		statement.setObject(3, this.comment, Types.VARCHAR);
		statement.setObject(4, this.status, Types.VARCHAR);
		statement.setObject(5, this.productId, Types.INTEGER);
		return statement.executeUpdate();

	}

	/**
	 * return a product object from a product store ine the database by using
	 * productId
	 * 
	 * @param productId
	 * @return
	 * @throws SQLException
	 */
	public static Product getProductById(int productId) throws SQLException {

		String selection = "ProduitId,Nom,Prix,Commentaires,Status";
		String source = "Produit";
		String condition = "ProduitId=?";
		String reqSql = "SELECT " + selection + " FROM " + source + " WHERE " + condition + " ;";
		Connection connection = DriverManager.getConnection(new SQLDatabaseConnection().getConnectionUrl());
		PreparedStatement statement = connection.prepareStatement(reqSql);
		statement.setObject(1, productId, Types.INTEGER);
		statement.executeQuery();

		ResultSet result = statement.getResultSet();

		if (result.next()) {
			int produitId = result.getInt("ProduitId");
			String name = result.getString("Nom");
			Double price = result.getDouble("Prix");
			String comment = result.getString("Commentaires");
			String status = result.getString("Status");
			return new Product(produitId, name, price, comment, status);

		} else {
			throw new SQLException("Data not found");
		}

	}

	/**
	 * Change a product status to 'Archiv�'
	 * 
	 * @param productId
	 * @return <type>int </type>
	 * @throws SQLException
	 */
	public static int removeById(int productId) throws SQLException {

		String source = "Produit";
		String modif = "status='Archiv�'";
		String condition = "ProduitId=?";
		String reqSql = "Update " + source + " SET " + modif + " WHERE " + condition + " ;";
		Connection connection = DriverManager.getConnection(new SQLDatabaseConnection().getConnectionUrl());
		PreparedStatement statement = connection.prepareStatement(reqSql);
		statement.setObject(1, productId, Types.INTEGER);
		return statement.executeUpdate();

	}

	/***
	 * 
	 * Getter and setter
	 * 
	 */

	public int getProduitId() {
		return productId;
	}

	public void setProduitId(int produitId) {
		this.productId = produitId;
	}

	public String getStatus() {
		return status;
	}

	private void setStatus(String status) {
		this.status = status;
	}

	/**
	 * @return the prix
	 */
	public Double getPrix() {
		return price;
	}

	/**
	 * @param prix the prix to set
	 */
	private void setPrix(Double prix) {
		this.price = prix;
	}

	/**
	 * @return the nom
	 */
	public String getNom() {
		return name;
	}

	/**
	 * @param nom the nom to set
	 */
	private void setNom(String nom) {
		this.name = nom;
	}

	/**
	 * @return the commentaire
	 */
	public String getComment() {
		return comment;
	}

	/**
	 * @param commentaire the commentaire to set
	 */
	private void setComment(String comment) {
		this.comment = comment;
	}

	@Override
	public String toString() {

		return "" + this.productId + "|" + this.name + "|" + this.price + "|" + this.comment + "|" + this.status;
	}

}
