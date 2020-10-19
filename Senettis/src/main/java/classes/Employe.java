package classes;

import java.sql.Connection;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import connexion.SQLDatabaseConnection;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class Employe {

	private Titre titre;
	private String nom;
	private String prenom;
	private String mail;
	private String telephone;
	private Integer numeroMatricule;
	private String pointure;
	private String taille;
	private Date dateArrivee;
	private Double nombreHeures;
	private Double remboursementTransport;
	private Double remboursementTelephone;
	private Double salaire;
	
	public String getTitre() {
		return titre.toString();
	}


	public void setTitre(String t) {
		if ((t == "M") || (t == "M.")) {
			this.titre = titre.M;
		}
		else if ((t == "Mme") ||  (t == "MME")||  (t == "Mme.")||  (t == "MME.")) {
			this.titre = titre.Mme;
		}		
		else {
			throw new Error("le titre est incorrect");
		}
	}


	public String getNom() {
		return nom;
	}


	public void setNom(String nom) {
		this.nom = nom;
	}


	public String getPrenom() {
		return prenom;
	}


	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}


	public String getMail() {
		return mail;
	}


	public void setMail(String mail) {
		String regex = "^(.+)@(.+)$";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(mail);
		if (!matcher.matches()) {
			throw new Error("l adresse mail est incorrecte");
		}
		this.mail = mail;
	}


	public String getTelephone() {
		return telephone;
	}


	public void setTelephone(String telephone) {
		telephone = telephone.replace(".", "");
		this.telephone = telephone;
	}


	public Integer getNumeroMatricule() {
		return numeroMatricule;
	}


	public void setNumeroMatricule(Integer numeroMatricule) {
		this.numeroMatricule = numeroMatricule;
	}


	public String getPointure() {
		return pointure;
	}


	public void setPointure(String pointure) {
		this.pointure = pointure;
	}


	public String getTaille() {
		return taille;
	}


	public void setTaille(String taille) {
		this.taille = taille;
	}


	public Date getDateArrivee() {
		return dateArrivee;
	}


	public void setDateArrivee(Date dateArrivee) {
		this.dateArrivee = dateArrivee;
	}


	public Double getNombreHeures() {
		return nombreHeures;
	}


	public void setNombreHeures(Double nombreHeures) {
		this.nombreHeures = nombreHeures;
	}


	public Double getRemboursementTransport() {
		return remboursementTransport;
	}


	public void setRemboursementTransport(Double remboursementTransport) {
		this.remboursementTransport = remboursementTransport;
	}


	public Double getRemboursementTelephone() {
		return remboursementTelephone;
	}


	public void setRemboursementTelephone(Double remboursementTelephone) {
		this.remboursementTelephone = remboursementTelephone;
	}


	public Double getSalaire() {
		return salaire;
	}


	public void setSalaire(Double salaire) {
		this.salaire = salaire;
	}

	
	
	public Employe(String t, String nom, String prenom, String mail, String telephone, Integer numeroMatricule,
			String pointure, String taille, String dateArrivee, Double nombreHeures, Double remboursementTransport,
			Double remboursementTelephone, Double salaire) {
		super();
		
		//je verifie que l adresse mail est correcte 
		String regex = "^(.+)@(.+)$";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(mail);
		if (!matcher.matches()) {
			throw new Error("l adresse mail est incorrecte");
		}

		//je verifie le titre	
		if ((t == "M") || (t == "M.")) {
			this.titre = titre.M;
		}
		else if ((t == "Mme") ||  (t == "MME")||  (t == "Mme.")||  (t == "MME.")) {
			this.titre = titre.Mme;
		}
		else {
			throw new Error("le titre est incorrect");
		}
		this.nom = nom;
		this.prenom = prenom;
		this.mail = mail;
		telephone = telephone.replace(".", "");
		this.telephone = telephone;
		this.numeroMatricule = numeroMatricule;
		this.pointure = pointure;
		this.taille = taille;
		
		//on transforme la date qui est en string vers un type date
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
	    Date dateFinale = null;
		try {
			dateFinale = simpleDateFormat.parse(dateArrivee);
		} catch (ParseException e) {
			simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
			try {
				dateFinale = simpleDateFormat.parse(dateArrivee);
			} catch (ParseException e2) {
				e2.printStackTrace();
			}
		}
		this.dateArrivee = dateFinale;
		
		this.nombreHeures = nombreHeures;
		this.remboursementTransport = remboursementTransport;
		this.remboursementTelephone = remboursementTelephone;
		this.salaire = salaire;
	}

	public Employe(String t, String nom, String prenom,Integer numeroMatricule, String dateArrivee) {
		super();
		
		//je verifie le titre
		if ((t == "M") || (t == "M.")) {
			this.titre = titre.M;
		}
		else if ((t == "Mme") ||  (t == "MME")||  (t == "Mme.")||  (t == "MME.")) {
			this.titre = titre.Mme;
		}
		else {
			throw new Error("le titre est incorrect");
		}
		this.nom = nom;
		this.prenom = prenom;
		this.numeroMatricule = numeroMatricule;
		
		//on transforme la date qui est en string vers un type date
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
	    Date dateFinale = null;
		try {
			dateFinale = simpleDateFormat.parse(dateArrivee);
		} catch (ParseException e) {
			simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
			try {
				dateFinale = simpleDateFormat.parse(dateArrivee);
			} catch (ParseException e2) {
				e2.printStackTrace();
			}
		}
		this.dateArrivee = dateFinale;
	}

	public Employe(String t, String nom, String prenom, Integer numeroMatricule) {
		super();
		
		//je verifie le titre
		if ((t == "M") || (t == "M.")) {
			this.titre = titre.M;
		}
		else if ((t == "Mme") ||  (t == "MME")||  (t == "Mme.")||  (t == "MME.")) {
			this.titre = titre.Mme;
		}		
		else {
			throw new Error("le titre est incorrect");
		}
		
		this.nom = nom;
		this.prenom = prenom;
		this.numeroMatricule = numeroMatricule;
	}


	public int smallInsertDatabase() throws SQLException {
		String reqSql = "INSERT INTO Employe(titre,nom,prenom,numero_matricule) VALUES (?,?,?,?)";
		
		Connection connection = DriverManager.getConnection(new SQLDatabaseConnection().getConnectionUrl());
		PreparedStatement statement = connection.prepareStatement(reqSql);
		statement.setObject(1,this.titre.toString(),Types.VARCHAR);
		statement.setObject(2,this.nom,Types.VARCHAR);
		statement.setObject(3,this.prenom,Types.VARCHAR);
		statement.setObject(4,this.numeroMatricule,Types.DECIMAL);
		
		return statement.executeUpdate();
	}
	
	public int insertDatabase() throws SQLException {
		String reqSql = "INSERT INTO Employe(titre,nom,prenom,mail,telephone,numero_matricule,pointure,taille,date_arrivee,nombre_heures,remboursement_transport,remboursement_telephone,salaire) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)";
		
		Connection connection = DriverManager.getConnection(new SQLDatabaseConnection().getConnectionUrl());
		PreparedStatement statement = connection.prepareStatement(reqSql);
		statement.setObject(1,this.titre.toString(),Types.VARCHAR);
		statement.setObject(2,this.nom,Types.VARCHAR);
		statement.setObject(3,this.prenom,Types.VARCHAR);
		statement.setObject(4,this.mail,Types.VARCHAR);
		statement.setObject(5,this.telephone,Types.VARCHAR);
		statement.setObject(6,this.numeroMatricule,Types.DECIMAL);
		statement.setObject(7,this.pointure,Types.VARCHAR);
		statement.setObject(8,this.taille,Types.VARCHAR);
		statement.setObject(9,this.dateArrivee,Types.DATE);
		statement.setObject(10,this.nombreHeures,Types.DECIMAL);
		statement.setObject(11,this.remboursementTransport,Types.VARCHAR);
		statement.setObject(12,this.remboursementTelephone,Types.VARCHAR);
		statement.setObject(13,this.salaire,Types.DECIMAL);
		
		return statement.executeUpdate();
	}

}
