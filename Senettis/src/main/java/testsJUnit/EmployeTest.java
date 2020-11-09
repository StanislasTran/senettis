package testsJUnit;
import static org.junit.Assert.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import classes.Employee;




public class EmployeTest
{
	
    private Employee laetitia;
    private Employee stan;
    private Employee alyssa;

    @Before
    public void setUp() 
    {
        laetitia = new Employee("Mme","C","Laetitia",1);
        alyssa = new Employee("MME","C","Alyssa",2,"20-10-2020","Publié");
        stan = new Employee("M.","T","Stan",3,"20/10/2020","Publié");
    }
    
    @Test
    public void testSet() 
    {
     //laetitia.setMail("laetitia"); //not ok
     laetitia.setMail("l@l.fr"); //ok
     
    }
    /*
    @Test
    public void testGet() 
    {
        laetitia.setDateArrivee(new Date(System.currentTimeMillis()));
        laetitia.setMail("l@l.fr");
        laetitia.setNombreHeures(12.0);
        laetitia.setPointure("38");
        laetitia.setTaille("2m20");
        laetitia.setRemboursementTelephone(0.0);
        laetitia.setRemboursementTransport(0.0);
        laetitia.setSalaire(300000.0);
        laetitia.setTelephone("01.23.45.67.89");
        
	     String s = "";
	     s += laetitia.getTitre() + ", ";
	     s += laetitia.getPrenom() + ", ";
	     s += laetitia.getNom() + ", ";
	     s += laetitia.getNumeroMatricule() + ", ";
	     s += laetitia.getDateArrivee() + ", ";
	     s += laetitia.getMail() + ", ";
	     s += laetitia.getNombreHeures() + ", ";
	     s += laetitia.getRemboursementTransport() + ", ";
	     s += laetitia.getRemboursementTelephone() + ", ";
	     s += laetitia.getSalaire() + ", ";
	     s += laetitia.getTelephone() + ", ";
	     
	     System.out.println(s);
    }
    
    @Test
    public void test() 
    {
        assertEquals(laetitia.getTitre(),alyssa.getTitre());
        assertEquals(stan.getDateArrivee(),alyssa.getDateArrivee());
    }
    
    */
}

