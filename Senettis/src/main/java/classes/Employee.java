
package classes;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.microsoft.sqlserver.jdbc.StringUtils;
import connexion.SQLDatabaseConnexion;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.time.LocalDate;


//to do (comment and translate)
/**
 * Represent an Employee into the System
 *
 */



public class Employee {

	private Integer employeId;
	private Title title;
	private String surname;
	private String firstName;
	private String mail;
	private String phone;
	private String matricule;
	private String shoeSize;
	private String size;
	private String arrivalDate;
	private String status;
	private int seniority; // derniere annee prise en compte

	// Constructeurs-----------------------------------------------
	/***
	 * 
	 * cree un employe a partir des champs employeId, title, surname, prenom,
	 * numeroMatricule, status, dateArrivee, mail, telephone, pointure, taille,
	 * nombreHeures, renboursementTransport, remboursementTelephone, salaire
	 * 
	 * @param employeId              : int
	 * @param t                      : verification faite
	 * @param surname
	 * @param prenom
	 * @param mail                   : verification faite
	 * @param telephone
	 * @param numeroMatricule        : int
	 * @param pointure
	 * @param taille
	 * @param dateArrivee            : string, verification faite
	 * @param nombreHeures           : double
	 * @param remboursementTransport :double
	 * @param remboursementTelephone : double
	 * @param salaire                :double
	 * @param status                 : verification faite
	 */
	public Employee(int employeId, String t, String surname, String firstName, String mail, String phone,
			String matricule, String shoeSize, String size, String arrivalDate, String status) {
		this(t, surname, firstName, mail, phone, matricule, shoeSize, size, arrivalDate, status);

		// id
		if ((Integer) employeId != null) {
			this.employeId = employeId;
		} else {
			throw new Error("L'employeId est vide, merci de sp�cifier un id ou d'utiliser un autre constructeur.");
		}
	}

	public Employee(int employeId, String t, String surname, String firstName, String mail, String phone,
			String matricule, String shoeSize, String size, String arrivalDate, int seniority, String status) {
		this(employeId, t, surname, firstName, mail, phone, matricule, shoeSize, size, arrivalDate, status);

		// id
		if ((Integer) seniority != null) {
			this.seniority = seniority;
		} else {
			throw new Error("L'anciennete est vide, merci de sp�cifier un id ou d'utiliser un autre constructeur.");
		}
	}

	/***
	 * cree un employe a partir des champs title, surname, prenom, numeroMatricule,
	 * status, dateArrivee, mail, telephone, pointure, taille, nombreHeures,
	 * renboursementTransport, remboursementTelephone, salaire
	 * 
	 * @param t               : verification faite
	 * @param surname
	 * @param fristName
	 * @param mail            : verification faite
	 * @param phone
	 * @param matricule : int
	 * @param shoeSize
	 * @param size
	 * @param arrivalDate     : string, verification faite
	 * @param status          : verification faite
	 */
	public Employee(String t, String surname, String firstName, String mail, String phone, String matricule,
			String shoeSize, String size, String arrivalDate, String status) {
		this(t, surname, firstName, matricule, arrivalDate, status);

		// je verifie que l adresse mail est correcte
		if (mail != null) {
			if (mail.isEmpty()) {
				mail = null;
			} else {
				String regex = "^(.+)@(.+)$";
				Pattern pattern = Pattern.compile(regex);
				Matcher matcher = pattern.matcher(mail);
				if (!matcher.matches()) {
					throw new Error("L'adresse mail est incorrecte.");
				}
			}
		}
		this.mail = mail;

		// telephone
		if (phone != null) {
			phone = phone.replace(".", "");
		}
		this.phone = phone;

		// pointure
		this.shoeSize = shoeSize;

		// taille
		this.size = size;

	}

	/***
	 * cree un employe a partir des champs title, surname, prenom, numeroMatricule,
	 * status et dateArrivee
	 * 
	 * @param t               : verification faite
	 * @param surname
	 * @param firstName
	 * @param matricule : int
	 * @param arrivalDate     : string, verification faite
	 * @param status          : verification faite
	 */
	public Employee(String t, String surname, String firstName, String matricule, String arrivalDate, String status) {
		this(t, surname, firstName, matricule);

		// status
		if (status != null) {
			if (!status.isEmpty()) {
				if (status.equals("Publi�") || status.equals("Publi�")) {
					this.status = "Publi�";
				} else if (status.equals("Archiv�") || status.equals("Archiv�")) {
					this.status = "Archiv�";
				} else if (status.equals("Draft") || status.equals("draft")) {
					this.status = "Draft";
				} else {
					throw new Error("Le status indiqu� est incorrect, le status doit �tre Publi�, Archiv� ou draft.");
				}
			} else {
				this.status = null;
			}
		}

		// date arrivee
		if (arrivalDate != null) {
			if (!(arrivalDate.isEmpty())) {
				if (arrivalDate.charAt(2) == '/' || arrivalDate.charAt(2) == '-' || arrivalDate.charAt(2) == '_') {

					arrivalDate = arrivalDate.substring(0, 2) + "/" + arrivalDate.substring(3, 5) + "/"
							+ arrivalDate.substring(6, 10);
				} else if (arrivalDate.charAt(7) == '/' || arrivalDate.charAt(7) == '-'
						|| arrivalDate.charAt(7) == '_') {

					arrivalDate = arrivalDate.substring(8, 10) + "/" + arrivalDate.substring(5, 7) + "/"
							+ arrivalDate.substring(0, 4);
				} else {
					throw new Error(
							"La date d'arriv�e indiqu�e est incorrecte, une date doit �tre indiqu�� selon un des formats suivant : 31-01-2000, 31/01/2000, 2000-01-31 ou 2000/01/31.");
				}
			} else {
				arrivalDate = null;
			}
		}
		this.arrivalDate = arrivalDate;
	}

	/***
	 * cree un employe a partir des champs title, surname, prenom et numeroMatricule
	 * 
	 * @param t               : verification faite
	 * @param surname
	 * @param firstName
	 * @param matricule : int
	 */
	public Employee(String t, String surname, String firstName, String matricule) {
		super();

		// je verifie le title
		if (t != null) {
			if ((t == "M") || (t == "M.")) {
				this.title = Title.M;
			} else if ((t == "Mme") || (t == "MME") || (t == "Mme.") || (t == "MME.")) {
				this.title = Title.Mme;
			} else if (t.contains("me") || t.contains("ME")) {
				this.title = Title.Mme;
			} else {
				this.title = Title.M;
			}
		} else {
			this.title = Title.M;
		}

		// surname
		if (surname != null) {
			this.surname = surname;
		} else {
			throw new Error("Le nom indiqu� est vide.");
		}

		// prenom
		if (firstName != null) {
			this.firstName = firstName;
		} else {
			throw new Error("Le pr�nom indiqu� est vide.");
		}

		// numero matricule
		if (matricule != null) {
			this.matricule = matricule;
		} else {
			throw new Error("Le num�ro de matricule indiqu� est vide.");
		}
		this.seniority = 0;

	}

	// Liens avec la BDD-----------------------------------------------
	/***
	 * permet d'ajouter un employe dans la base de donn�es
	 * 
	 * @return
	 * @throws SQLException
	 */
	public int insertDatabase() throws SQLException {
		String reqSql = "INSERT INTO Employe(titre,nom,prenom,mail,telephone,numero_matricule,pointure,taille,date_arrivee,anciennete,status) VALUES (?,?,?,?,?,?,?,?,?,?,?)";

		Connection connection = DriverManager.getConnection(new SQLDatabaseConnexion().getConnectionUrl());
		PreparedStatement statement = connection.prepareStatement(reqSql);
		statement.setObject(1, this.title.toString(), Types.VARCHAR);
		statement.setObject(2, this.surname, Types.VARCHAR);
		statement.setObject(3, this.firstName, Types.VARCHAR);
		statement.setObject(4, this.mail, Types.VARCHAR);
		statement.setObject(5, this.phone, Types.VARCHAR);
		statement.setObject(6, this.matricule, Types.VARCHAR);
		statement.setObject(7, this.shoeSize, Types.VARCHAR);
		statement.setObject(8, this.size, Types.VARCHAR);
		statement.setObject(9, this.arrivalDate, Types.DATE);
		statement.setObject(10, this.seniority, Types.INTEGER);
		statement.setObject(11, this.status, Types.VARCHAR);

		return statement.executeUpdate();
	}

	/***
	 * Permet de modifier un employe dans la base de donn�es, pour que la requete
	 * fonctionne il faut que l'employe ai son champs employeId complet� car
	 * l'update se fait a partir de l'id la requete modifiera tous les champs de
	 * l'employe en les remplacant par la valeur actuelle associee a cet employe
	 * pour chaque champs
	 * 
	 * @return
	 * @throws SQLException
	 */
	public int updateDatabase() throws SQLException {
		String reqSql = "UPDATE Employe SET titre=?, nom=?, prenom=?, mail=?, telephone=?, numero_matricule=?, pointure=?, taille=?, date_arrivee=?, status=?, anciennete=? WHERE EmployeId=?;";

		Connection connection = DriverManager.getConnection(new SQLDatabaseConnexion().getConnectionUrl());
		PreparedStatement statement = connection.prepareStatement(reqSql);
		statement.setObject(1, this.title.toString(), Types.VARCHAR);
		statement.setObject(2, this.surname, Types.VARCHAR);
		statement.setObject(3, this.firstName, Types.VARCHAR);
		statement.setObject(4, this.mail, Types.VARCHAR);
		statement.setObject(5, this.phone, Types.VARCHAR);
		statement.setObject(6, this.matricule, Types.VARCHAR);
		statement.setObject(7, this.shoeSize, Types.VARCHAR);
		statement.setObject(8, this.size, Types.VARCHAR);
		statement.setObject(9, this.arrivalDate, Types.DATE);
		statement.setObject(10, this.status, Types.VARCHAR);
		statement.setObject(11, this.seniority, Types.VARCHAR);
		statement.setObject(12, this.employeId, Types.INTEGER);

		return statement.executeUpdate();
	}

	public static int updateAnciennete(int seniority, int employeId) throws SQLException {
		String reqSql = "UPDATE Employe SET anciennete=? WHERE EmployeId=?;";

		Connection connection = DriverManager.getConnection(new SQLDatabaseConnexion().getConnectionUrl());
		PreparedStatement statement = connection.prepareStatement(reqSql);
		statement.setObject(1, seniority, Types.INTEGER);
		statement.setObject(2, employeId, Types.INTEGER);

		return statement.executeUpdate();
	}

	/***
	 * requete la base de donn�es afin de recuperer tous les elements de la table
	 * employe
	 * 
	 * @return
	 * @throws SQLException
	 */
	private static Statement selectAllEmploye() throws SQLException {
		String reqSql = "SELECT * FROM Employe";

		Connection connection = DriverManager.getConnection(new SQLDatabaseConnexion().getConnectionUrl());
		Statement statement = connection.createStatement();
		statement.executeQuery(reqSql);
		return statement;
	}

	/**
	 * Retourne le nombre d'employ� dans la base de donn�es
	 * 
	 * @return
	 * @throws SQLException
	 */
	public static int getCountEmploye() throws SQLException {
		String reqSql = "SELECT count(*) as count FROM Employe";
		Connection connection = DriverManager.getConnection(new SQLDatabaseConnexion().getConnectionUrl());
		Statement statement = connection.createStatement();
		statement.executeQuery(reqSql);
		ResultSet result = statement.getResultSet();
		if (result.next()) {
			if (result.getInt("count") > 0) {
				return result.getInt("count");
			}
		}
		return 0;
	}

	/***
	 * 
	 * @param employeId
	 * @return l'employe correspondant � l'id indique en argument
	 * @throws SQLException
	 */
	public static Employee getEmployeById(int employeId) throws SQLException {
		String reqSql = "SELECT EmployeId,titre,nom,prenom,mail,telephone,numero_matricule,pointure,taille,date_arrivee,anciennete,Status FROM Employe WHERE EmployeId=?;";
		Connection connection = DriverManager.getConnection(new SQLDatabaseConnexion().getConnectionUrl());
		PreparedStatement statement = connection.prepareStatement(reqSql);
		statement.setObject(1, employeId, Types.INTEGER);
		statement.executeQuery();

		ResultSet result = statement.getResultSet();

		if (result.next()) {
			employeId = result.getInt("EmployeId");
			String title = result.getString("Titre");
			String surname = result.getString("Nom");
			String firstName = result.getString("Prenom");
			String mail = result.getString("mail");
			String phone = result.getString("Telephone");
			String matricule = result.getString("Numero_matricule");
			String shoeSize = result.getString("Pointure");
			String size = result.getString("Taille");
			String arrivalDate = result.getString("Date_arrivee");
			int seniority = result.getInt("anciennete");
			String status = result.getString("status");

			return new Employee(employeId, title, surname, firstName, mail, phone, matricule, shoeSize, size,
					arrivalDate, seniority, status);

		} else {
			throw new SQLException("Data not found");
		}
	}

	public static void retrieveByMatricule(String matricule) throws SQLException {
		String reqSql = "SELECT EmployeId FROM Employe WHERE Numero_matricule=?;";
		Connection connection = DriverManager.getConnection(new SQLDatabaseConnexion().getConnectionUrl());
		PreparedStatement statement = connection.prepareStatement(reqSql);
		statement.setObject(1, matricule, Types.VARCHAR);
		statement.executeQuery();

		ResultSet result = statement.getResultSet();
		int employeId;
		if (result.next()) {
			employeId = result.getInt("EmployeId");

		} else {
			throw new Error("Erreur de matricule, aucun employ� ne correspond.");
		}

		try {
			Employee e = getEmployeById(employeId);
			e.setStatus("Publi�");
			e.updateDatabase();
		} catch (Exception e) {
			throw new Error("Erreur de matricule, aucun employ� ne correspond.");
		}
	}

	public static void retrieveBySurnameFirstName(String surname, String firstName) throws SQLException {
		String reqSql = "SELECT EmployeId FROM Employe WHERE Nom=? and Prenom=?;";
		Connection connection = DriverManager.getConnection(new SQLDatabaseConnexion().getConnectionUrl());
		PreparedStatement statement = connection.prepareStatement(reqSql);
		statement.setObject(1, surname, Types.VARCHAR);
		statement.setObject(2, firstName, Types.VARCHAR);
		statement.executeQuery();

		ResultSet result = statement.getResultSet();
		int employeId;
		if (result.next()) {
			employeId = result.getInt("EmployeId");

		} else {
			throw new SQLException("Aucun employ� ne correspond.");
		}

		try {
			Employee e = getEmployeById(employeId);
			e.setStatus("Publi�");
			e.updateDatabase();
		} catch (Exception e) {
			throw e;
		}
	}

	public static int getIdByMatricule(String matricule) throws SQLException {
		String reqSql = "SELECT EmployeId FROM Employe WHERE numero_matricule=?;";
		Connection connection = DriverManager.getConnection(new SQLDatabaseConnexion().getConnectionUrl());
		PreparedStatement statement = connection.prepareStatement(reqSql);
		statement.setObject(1, matricule, Types.VARCHAR);
		statement.executeQuery();

		ResultSet result = statement.getResultSet();

		if (result.next()) {
			return result.getInt("EmployeId");

		} else {
			throw new SQLException("Data not found");
		}
	}

	// -----------------------------------------------
	/***
	 * 
	 * @return une liste contenant tous les employes renvoyes par selectAllEmploye()
	 * @throws SQLException
	 */
	public static List<Employee> getAllEmploye() throws SQLException {

		ResultSet result = selectAllEmploye().getResultSet();
		List<Employee> allEmploye = new ArrayList<Employee>();
		while (result.next()) {
			int employeId = result.getInt("EmployeId");
			String title = result.getString("Titre");
			String surname = result.getString("Nom");
			String firstName = result.getString("Prenom");
			String mail = result.getString("Mail");
			String phone = result.getString("Telephone");
			String matricule = result.getString("Numero_matricule");
			String shoeSize = result.getString("Pointure");
			String size = result.getString("Taille");
			int seniority = result.getInt("anciennete");
			String arrivalDate;
			if (result.getString("Date_arrivee") == null) {
				arrivalDate = null;
			} else {
				String usDate = result.getString("Date_arrivee");
				// on transforme en date au format francais
				arrivalDate = usDate.substring(8, 10) + "/" + usDate.substring(5, 7) + "/" + usDate.substring(0, 4);
			}

			String status = result.getString("Status");

			allEmploye.add(new Employee(employeId, title, surname, firstName, mail, phone, matricule, shoeSize,
					size, arrivalDate, seniority, status));

		}

		return allEmploye;
	}

	public static void printAllEmploye() throws SQLException {

		List<Employee> allEmploye = getAllEmploye();

		for (Employee employe : allEmploye)
			System.out.println(employe);
	}

	@Override
	public String toString() {

		return "" + this.employeId + "|" + this.title + "|" + this.surname + "|" + this.firstName + "|" + this.mail + "|"
				+ this.phone + "|" + this.matricule + "|" + this.shoeSize + "|" + this.size + "|"
				+ this.arrivalDate + "|" + this.status;
	}

	public String getNameString() {

		return "" + this.surname + " " + this.firstName;
	}

	// Getter and setter-----------------------------------------------
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		if (status != null) {
			if (status == "Publi�" || status == "Publi�") {
				this.status = "Publi�";
			} else if (status == "Archiv�" || status == "Archiv�") {
				this.status = "Archiv�";
			} else if (status == "Draft" || status == "draft") {
				this.status = "Draft";
			} else {
				throw new Error("Le status indiqu� est incorrect, le status doit �tre Publi�, Archiv� ou draft.");
			}
		} else {
			throw new Error("Le status indiqu� est vide.");
		}
	}

	public void setTitle(Title title) {
		if (title == null) {
			throw new Error("setTitle : le titre indique est vide");
		}
		this.title = title;
	}

	public String getTitle() {
		if (title == null) {
			return "";
		}
		return title.toString();
	}

	@SuppressWarnings("static-access")
	public void setTitle(String t) {
		if (title == null) {
			throw new Error("setTitle : le titre indique est vide");
		}
		if (t != null) {
			if ((t == "M") || (t == "M.")) {
				this.title = title.M;
			} else if ((t == "Mme") || (t == "MME") || (t == "Mme.") || (t == "MME.")) {
				this.title = title.Mme;
			} else if (t.contains("me") || t.contains("ME")) {
				this.title = title.Mme;
			} else {
				this.title = title.M;
			}
		}
	}

	public String getSurname() {
		if (surname == null) {
			return "";
		}
		return surname;
	}

	public void setSurname(String surname) {
		if (surname == null) {
			throw new Error("setSurname : le nom indique est vide");
		}
		this.surname = surname;
	}

	public String getFirstName() {
		if (firstName == null) {
			return "";
		}
		return firstName;
	}

	public void setFirstName(String prenom) {
		if (prenom == null) {
			throw new Error("setPrenom : le prenom indique est vide");
		}
		this.firstName = prenom;
	}

	public String getMail() {
		if (mail == null) {
			return "";
		}
		return mail;
	}

	public void setMail(String mail) {
		if (mail == null) {
			throw new Error("setMail : le mail indique est vide");
		}
		String regex = "^(.+)@(.+)$";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(mail);
		if (!matcher.matches()) {
			throw new Error("L'adresse mail est incorrecte.");
		}
		this.mail = mail;
	}

	public String getPhone() {
		if (phone == null) {
			return "";
		}
		return phone;
	}

	public void setTelephone(String telephone) {
		if (telephone == null) {
			throw new Error("setTelephone : le telephone indique est vide");
		}
		telephone = telephone.replace(".", "");
		this.phone = telephone;
	}

	public String getMatricule() {
		if (matricule == null) {
			return "";
		}
		return matricule;
	}

	public void setMatricule(String matricule) {
		if (matricule == null) {
			throw new Error("setMatricule : le numeroMatricule indique est vide");
		}
		this.matricule = matricule;
	}

	public String getShoeSize() {
		if (shoeSize == null) {
			return "";
		}
		return shoeSize;
	}

	public void setShoeSize(String shoeSize) {
		if (shoeSize == null) {
			throw new Error("setShoeSize : la pointure indiquee est vide");
		}
		this.shoeSize = shoeSize;
	}

	public String getSize() {
		if (size == null) {
			return "";
		}
		return size;
	}

	public void setSize(String size) {
		if (size == null) {
			throw new Error("setSize : le taille indique est vide");
		}
		this.size = size;
	}

	public String getArrivalDate() {
		if (arrivalDate == null) {
			return "";
		}
		return arrivalDate;
	}

	public void setArrivalDate(String arrivalDate) {
		// date arrivee
		if (arrivalDate != null) {
			try {
				if (StringUtils.isNumeric(arrivalDate.substring(0, 4))
						&& StringUtils.isNumeric(arrivalDate.substring(8, 10))
						&& StringUtils.isNumeric(arrivalDate.substring(5, 7))) {
					// date anglaise
					// on reecrit en format francais
					arrivalDate = arrivalDate.substring(8, 10) + "/" + arrivalDate.substring(5, 7) + "/"
							+ arrivalDate.substring(0, 4);
				} else if (StringUtils.isNumeric(arrivalDate.substring(6, 10))
						&& StringUtils.isNumeric(arrivalDate.substring(3, 5))
						&& StringUtils.isNumeric(arrivalDate.substring(0, 2))) {
					// date francaise
					// on reecrit en format francais juste pour s'assurer que toutes les dates
					// seront ecrites avec le meme format jj/mm/aaaa
					arrivalDate = arrivalDate.substring(0, 2) + "/" + arrivalDate.substring(3, 5) + "/"
							+ arrivalDate.substring(6, 10);
				}
			} catch (Exception e) {
				throw new Error(
						"La date d'arriv�e indiqu�e est incorrecte, une date doit �tre indiqu� selon un des formats suivant : 31-01-2000, 31/01/2000, 2000-01-31 ou 2000/01/31.\n"
								+ e.getMessage());
			}
		} else {
			throw new Error("La date d'arriv�e indiqu�e est vide.");
		}
		this.arrivalDate = arrivalDate;

	}

	public int getEmployeId() {
		return employeId;
	}

	public void setEmployeId(Integer employeId) {
		if (employeId == null) {
			throw new Error("setEmployeId : le employeId indique est vide");
		}
		this.employeId = employeId;
	}

	public int getSeniority() {
		return seniority;
	}

	public void setSeniority(int seniority) {
		this.seniority = seniority;
	}

	public Integer getComputeSeniority() {
		String date = this.getArrivalDate();

		int m1 = Integer.parseInt(date.substring(3, 5));
		int a1 = Integer.parseInt(date.substring(6, 10));
		LocalDate currentdate = LocalDate.now();

		int m2 = currentdate.getMonthValue();
		int a2 = currentdate.getYear();

		Integer seniority = 0;

		if (a2 - a1 == 0) {
			seniority = 0;

		} else if (a2 - a1 > 0) {

			if (m1 > m2)
				seniority = a2 - a1 - 1;
			else
				seniority = a2 - a1;

		}

		return seniority;
	}

}
