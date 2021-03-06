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

import connexion.SQLDatabaseConnexion;

/**
 * 
 * This object represent a product into the system
 * 
 */
public class Product {

	private int productId;
	private String name;
	private Double price;
	private String comment;
	private String brand;
	private Status status;

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
	public Product(String name, Double price, Status status) {
		this.name = name;
		this.price = price;
		this.status = status;
	}

	/***
	 * Constructor
	 * 
	 * @param name
	 * @param price
	 * @param status
	 */
	public Product(int productId, String name, String brand, Double price, String comment, Status status) {
		this(name, brand, price, comment, status);
		this.productId = productId;

	}

	/**
	 * Constructor for Product
	 * 
	 * @param <type> Double </type> price
	 * @param <type> String </type> name
	 * @param <type> String </type> comment
	 * @param <type> String </type> status
	 */
	public Product(String name, String brand, Double price, String comment, Status status) {
		this(name, price, status);
		this.comment = comment;
		this.brand = brand;

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
		String reqSql = "INSERT INTO Produit(Nom,Marque,Prix,Commentaires,Status) VALUES (?,?,?,?,?)";

		Connection connection = DriverManager.getConnection(new SQLDatabaseConnexion().getConnectionUrl());
		PreparedStatement statement = connection.prepareStatement(reqSql);
		statement.setObject(1, this.name, Types.VARCHAR);
		statement.setObject(2, this.brand, Types.VARCHAR);
		statement.setObject(3, this.price, Types.DECIMAL);
		statement.setObject(4, this.comment, Types.VARCHAR);
		statement.setObject(5, this.status.getValue(), Types.VARCHAR);

		return statement.executeUpdate();
	}

	/**
	 * Select all Product in the database and return the result
	 * 
	 * @return
	 * @throws SQLException
	 */
	private static Statement selectAllProduct() throws SQLException {
		String reqSql = "SELECT * FROM Produit ORDER BY nom";

		Connection connection = DriverManager.getConnection(new SQLDatabaseConnexion().getConnectionUrl());
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
		String reqSql = "SELECT * FROM Produit WHERE Status='Publié' ORDER BY Nom";

		Connection connection = DriverManager.getConnection(new SQLDatabaseConnexion().getConnectionUrl());
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
			String brand = result.getString("Marque");

			Status status = Status.getStatus(result.getString("Status"));
			allProduct.add(new Product(produitId, name, brand, price, comment, status));

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
			String brand = result.getString("Marque");
			Status status = Status.getStatus(result.getString("Status"));
			allProduct.add(new Product(produitId, name, brand, price, comment, status));

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
	 * update the product in the database by using the product attributes
	 * 
	 * @return <type>int</type>
	 * @throws SQLException
	 */
	public int updateDatabase() throws SQLException {
		String reqSql = "UPDATE Produit SET nom=?,Marque=?, prix=?, commentaires=?, status=?  WHERE ProduitId=?;";

		Connection connection = DriverManager.getConnection(new SQLDatabaseConnexion().getConnectionUrl());
		PreparedStatement statement = connection.prepareStatement(reqSql);
		statement.setObject(1, this.name.toString(), Types.VARCHAR);
		statement.setObject(2, this.brand, Types.VARCHAR);
		statement.setObject(3, this.price, Types.DECIMAL);
		statement.setObject(4, this.comment, Types.VARCHAR);
		statement.setObject(5, this.status.getValue(), Types.VARCHAR);

		statement.setObject(6, this.productId, Types.INTEGER);
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

		String selection = "ProduitId,Nom,Prix,Commentaires,Marque,Status";
		String source = "Produit";
		String condition = "ProduitId=?";
		String reqSql = "SELECT " + selection + " FROM " + source + " WHERE " + condition + " ;";
		Connection connection = DriverManager.getConnection(new SQLDatabaseConnexion().getConnectionUrl());
		PreparedStatement statement = connection.prepareStatement(reqSql);
		statement.setObject(1, productId, Types.INTEGER);
		statement.executeQuery();

		ResultSet result = statement.getResultSet();

		if (result.next()) {
			int produitId = result.getInt("ProduitId");
			String name = result.getString("Nom");
			Double price = result.getDouble("Prix");
			String comment = result.getString("Commentaires");
			String brand = result.getString("Marque");
			Status status = Status.getStatus(result.getString("Status"));
			return new Product(produitId, name, brand, price, comment, status);

		} else {
			throw new SQLException("Data not found");
		}

	}

	/**
	 * Change a product status to 'Archivé'
	 * 
	 * @param productId
	 * @return <type>int </type>
	 * @throws SQLException
	 */
	public static int removeById(int productId) throws SQLException {

		String source = "Produit";
		String modif = "status='Archivé'";
		String condition = "ProduitId=?";
		String reqSql = "Update " + source + " SET " + modif + " WHERE " + condition + " ;";
		Connection connection = DriverManager.getConnection(new SQLDatabaseConnexion().getConnectionUrl());
		PreparedStatement statement = connection.prepareStatement(reqSql);
		statement.setObject(1, productId, Types.INTEGER);
		return statement.executeUpdate();

	}

	/***
	 * 
	 * Getters
	 * 
	 */

	/***
	 * 
	 * Getter for the attribute productId
	 * 
	 * @return <type> int</type> productId
	 */
	public int getProduitId() {
		return productId;
	}

	public Status getStatus() {
		return status;
	}

	/**
	 * Getter for the attribute price
	 * 
	 * @return <type>Double </type> Price
	 */
	public Double getPrice() {
		return price;
	}

	/**
	 * Getter for the attribute comment
	 * 
	 * @return <type>String</type> comment
	 */
	public String getComment() {
		return comment;
	}

	/**
	 * getter for the attribute name
	 * 
	 * @return <type>String</type> name
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Getter for the attribute brand
	 * 
	 * @return <type> String </type>
	 */
	public String getBrand() {

		return this.brand;
	}

	/**********************
	 * 
	 * Setters
	 * 
	 *********************/

	/**
	 * setter for the attribute produitId
	 * 
	 * @param <type>int</type> produitId superior or equal to 0;
	 * @throws IllegalArgumentException
	 */
	public void setProduitId(int produitId) {
		if (produitId < 0)
			throw new IllegalArgumentException("produitId can't be negative");
		this.productId = produitId;
	}

	@Override
	public String toString() {

		return "" + this.productId + "|" + this.name + "|" + this.price + "|" + this.comment + "|" + this.status;
	}

}
