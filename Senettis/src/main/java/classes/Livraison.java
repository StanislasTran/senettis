package classes;

import java.sql.Connection;
import java.util.Date;

import connexion.SQLDatabaseConnection;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
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


	
	public int insertDatabase() throws SQLException {
		String reqSql = "INSERT INTO Livraison(chantier,produit,date,prixTotal) VALUES (?,?,?,?)";
		
		Connection connection = DriverManager.getConnection(new SQLDatabaseConnection().getConnectionUrl());
		PreparedStatement statement = connection.prepareStatement(reqSql);
		statement.setObject(1,this.idChantier,Types.INTEGER);
		statement.setObject(2,this.idProduit,Types.INTEGER);
		statement.setObject(3,this.date,Types.DATE);
		statement.setObject(4,this.prixTotal,Types.DECIMAL);
		
		return statement.executeUpdate();
	}



	public Livraison(Integer idChantier, Integer idProduit, String date) {
		super();
		this.idChantier = idChantier;
		this.idProduit = idProduit;
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
	    Date dateFinale = null;
		try {
			dateFinale = simpleDateFormat.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		this.date = dateFinale;
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
		this.idChantier = idChantier;
	}



	public Integer getIdProduit() {
		return idProduit;
	}



	public void setIdProduit(Integer idProduit) {
		this.idProduit = idProduit;
	}



	public Double getPrixTotal() {
		return prixTotal;
	}



	public void setPrixTotal(Double prixTotal) {
		this.prixTotal = prixTotal;
	}



	public Date getDate() {
		return date;
	}



	public void setDate(Date date) {
		this.date = date;
	}

}
