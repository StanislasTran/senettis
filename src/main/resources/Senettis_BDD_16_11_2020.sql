

Create table Produit (
    "ProduitId"  INT PRIMARY KEY IDENTITY (1, 1),
    "Nom" VARCHAR (100) NOT NULL,
	"Marque" VARCHAR (100),
    "Prix" decimal (4,2) NOT NULL,
	"Date_de_debut" DateTime, 
	"Date_de_fin" DateTime ,
    "Commentaires" VARCHAR (500),
	"Status" VARCHAR (50) NOT NULL,
	"Date_de_creation" DateTime  NOT NUll Default (GETDATE()), 
	"Date_de_modification" DateTime  NOT NUll Default (GETDATE()), 
	CONSTRAINT check_status_produit CHECK (("Status") IN ('Publié','Brouillon','Archivé')),
	
);

GO

Create TRIGGER Product_Update
ON Produit
AFTER UPDATE
AS
UPDATE
Produit
SET Date_de_modification = GETDATE()
 WHERE ProduitId IN (SELECT DISTINCT ProduitId FROM Inserted);
 GO

Create table Chantier (
    ChantierId INT PRIMARY KEY IDENTITY (1, 1),
    Nom VARCHAR (100) NOT NULL,
    CA decimal,
    adresse VARCHAR (500),
	"Status" VARCHAR (50) NOT NULL,
	"Date_de_creation" DateTime  NOT NUll Default (GETDATE()), 
	"Date_de_modification" DateTime  NOT NUll Default (GETDATE()),
	CONSTRAINT check_status_chantier CHECK (("Status") IN ('Publié','Brouillon','Archivé')),
	 
);

GO

Create TRIGGER Chantier_Update
ON Chantier
AFTER UPDATE
AS
UPDATE
Chantier
SET Date_de_modification = GETDATE()
 WHERE ChantierId IN (SELECT DISTINCT ChantierId FROM Inserted);
 GO



Create table Employe (
    EmployeId INT PRIMARY KEY IDENTITY (1, 1),
    Titre VARCHAR (10),
    Nom VARCHAR (100) NOT NULL,
    Prenom VARCHAR (100) NOT NULL,
    Mail VARCHAR(50),
	Qualifications VARCHAR(50),
    Telephone VARCHAR(15),
    Numero_matricule VARCHAR(40) NOT NULL,
    Pointure VARCHAR(10),
    Taille VARCHAR(10),
    Date_arrivee DATE,
    Nombre_heures decimal,
    Remboursement_transport decimal,
    Remboursement_telephone decimal,
    Salaire decimal,
	"Status" VARCHAR (50) NOT NULL,
	"Date_de_creation" DateTime  NOT NUll Default (GETDATE()), 
	"Date_de_modification" DateTime  NOT NUll Default (GETDATE()), 
	CONSTRAINT check_status_employe CHECK (("Status") IN ('Publié','Brouillon','Archivé')),
    CONSTRAINT check_titre CHECK ((Titre) IN ('M','Mme')),
    constraint check_mail check (Mail like '%_@__%.__%'),
    CONSTRAINT matricule_unique UNIQUE (Numero_matricule),   
);

GO

Create TRIGGER Employe_Update
ON Employe
AFTER UPDATE
AS
UPDATE
Employe
SET Date_de_modification = GETDATE()
 WHERE EmployeId IN (SELECT DISTINCT EmployeId FROM Inserted);
 GO


Create table Livraison (
    LivraisonId INT PRIMARY KEY IDENTITY (1, 1),
    Chantier int NOT NULL,
    Date DATE , 
    FOREIGN KEY (Chantier) REFERENCES Chantier(ChantierId), 
    PrixTotal decimal,
	"Status" VARCHAR (50) NOT NULL,
	"Date_de_creation" DateTime  NOT NUll Default (GETDATE()), 
	"Date_de_modification" DateTime  NOT NUll Default (GETDATE()), 
	CONSTRAINT check_status_livraison CHECK (("Status") IN ('Publié','Brouillon','Archivé')),
	 
);

GO

Create TRIGGER Livraison_Update
ON Livraison
AFTER UPDATE
AS
UPDATE
Livraison
SET Date_de_modification = GETDATE()
 WHERE LivraisonId IN (SELECT DISTINCT LivraisonId FROM Inserted);
 GO


Create table Affectation (
 AffectationId INT PRIMARY KEY IDENTITY (1, 1),
    Chantier int NOT NULL,
    Employe int NOT NULL,
    FOREIGN KEY (Chantier) REFERENCES Chantier(ChantierId), 
    FOREIGN KEY (Employe) REFERENCES Employe(EmployeId),
	Mois int,
	Annee int,
    Nombre_heures decimal,
	"Status" VARCHAR (50) NOT NULL,
	"Date_de_creation" DateTime  NOT NUll Default (GETDATE()), 
	"Date_de_modification" DateTime  NOT NUll Default (GETDATE()), 
	CONSTRAINT check_status_affectation CHECK (("Status") IN ('Publié','Brouillon','Archivé')),
	CONSTRAINT check_moonth CHECK (("Month")>=0 and ("Month")<=12))
	 
);

GO

Create TRIGGER Affectation_Update
ON Affectation
AFTER UPDATE
AS
UPDATE
Affectation
SET Date_de_modification = GETDATE()
 WHERE AffectationId IN (SELECT DISTINCT AffectationId FROM Inserted);
 GO

Create table ProduitParLivraison(
 ProduitParLivraisonId INT PRIMARY KEY IDENTITY (1, 1),
    Produit int NOT NULL,
    Livraison int NOT NULL,
    FOREIGN KEY (Livraison) REFERENCES Livraison(LivraisonId),
    FOREIGN KEY (Produit) REFERENCES Produit(ProduitId),
    Quantite int DEFAULT 1,
	"Status" VARCHAR (50) NOT NULL,
	"Date_de_creation" DateTime  NOT NUll Default (GETDATE()), 
	"Date_de_modification" DateTime  NOT NUll Default (GETDATE()), 
	CONSTRAINT check_status_produit_par_livraison CHECK (("Status") IN ('Publié','Brouillon','Archivé')),
	 
);


GO

Create TRIGGER ProduitParLivraison_Update
ON ProduitParLivraison
AFTER UPDATE
AS
UPDATE
ProduitParLivraison
SET Date_de_modification = GETDATE()
 WHERE ProduitParLivraisonId IN (SELECT DISTINCT ProduitParLivraisonId FROM Inserted);
 GO

 
Create table CoutChantier (
    "CoutChantierId"  INT PRIMARY KEY IDENTITY (1, 1),
    "Date" DATE NOT NULL,
	Chantier int NOT NULL,
    FOREIGN KEY (Chantier) REFERENCES Chantier(ChantierId), 
	CA decimal,
	cout_matériel decimal,
	cout_livraison_total decimal, 
	"Status" VARCHAR (50) NOT NULL,
	"Date_de_creation" DateTime  NOT NUll Default (GETDATE()), 
	"Date_de_modification" DateTime  NOT NUll Default (GETDATE()), 
	CONSTRAINT check_status_coutChantier CHECK (("Status") IN ('Publié','Brouillon','Archivé')),
	
);

GO

Create TRIGGER CoutChantier_Update
ON CoutChantier
AFTER UPDATE
AS
UPDATE
CoutChantier
SET Date_de_modification = GETDATE()
 WHERE CoutChantierId IN (SELECT DISTINCT CoutChantierId FROM Inserted);
 GO

Create table CoutEmploye (
    "CoutEmployeId"  INT PRIMARY KEY IDENTITY (1, 1),
    "Date" DATE NOT NULL,
	Employe int NOT NULL,
    FOREIGN KEY (Employe) REFERENCES Employe(EmployeId), 
	mutuelle decimal,
	indemnite_panier decimal,
	salaire_brut decimal, 
	salaire_net decimal, 
	cout_transport decimal, 
	cout_telephone decimal, 
	cout_prime_total decimal, 
	cout_AM_total decimal,
	"Status" VARCHAR (50) NOT NULL,
	"Date_de_creation" DateTime  NOT NUll Default (GETDATE()), 
	"Date_de_modification" DateTime  NOT NUll Default (GETDATE()), 
	CONSTRAINT check_status_coutEmploye CHECK (("Status") IN ('Publié','Brouillon','Archivé')),
	
);

GO

Create TRIGGER CoutEmploye_Update
ON CoutEmploye
AFTER UPDATE
AS
UPDATE
CoutEmploye
SET Date_de_modification = GETDATE()
 WHERE CoutEmployeId IN (SELECT DISTINCT CoutEmployeId FROM Inserted);
 GO

Create table Prime (
    "PrimeId"  INT PRIMARY KEY IDENTITY (1, 1),
    "Date" DATE ,
	Employe int NOT NULL,
    FOREIGN KEY (Employe) REFERENCES Employe(EmployeId), 
    Type_de_prime VARCHAR (100) NOT NULL,
	"montant" decimal,
	"Status" VARCHAR (50) NOT NULL,
	"Date_de_creation" DateTime  NOT NUll Default (GETDATE()), 
	"Date_de_modification" DateTime  NOT NUll Default (GETDATE()), 
	CONSTRAINT check_status_Prime CHECK (("Status") IN ('Publié','Brouillon','Archivé')),
	
);

GO

Create TRIGGER Prime_Update
ON Prime
AFTER UPDATE
AS
UPDATE
Prime
SET Date_de_modification = GETDATE()
 WHERE PrimeId IN (SELECT DISTINCT PrimeId FROM Inserted);
 GO


 Create table ArretMaladie (
    "AMId"  INT PRIMARY KEY IDENTITY (1, 1),
    "Date_de_debut" DATE NOT NULL,
	Employe int NOT NULL,
    FOREIGN KEY (Employe) REFERENCES Employe(EmployeId), 
    "Duree" VARCHAR (100),
    "Motif" VARCHAR (500),
	"cout" decimal,
	"Status" VARCHAR (50) NOT NULL,
	"Date_de_creation" DateTime  NOT NUll Default (GETDATE()), 
	"Date_de_modification" DateTime  NOT NUll Default (GETDATE()), 
	CONSTRAINT check_status_AM CHECK (("Status") IN ('Publié','Brouillon','Archivé')),
	
);

GO

Create TRIGGER AM_Update
ON ArretMaladie
AFTER UPDATE
AS
UPDATE
ArretMaladie
SET Date_de_modification = GETDATE()
 WHERE AMId IN (SELECT DISTINCT AMId FROM Inserted);
 GO
