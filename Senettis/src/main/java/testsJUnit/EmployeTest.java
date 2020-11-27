package testsJUnit;
import static org.junit.Assert.*;


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
        laetitia = new Employee("Mme","C","Laetitia","1");
        alyssa = new Employee("MME","C","Alyssa","2","20-10-2020","Publié");
        stan = new Employee("M.","T","Stan","3","20/10/2020","Publié");
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
        laetitia.setDateArrivee("10/02/2020");
        laetitia.setMail("l@l.fr");
        laetitia.setNombreHeures(12.0);
        laetitia.setPointure("38");
        laetitia.setTaille("2m20");
        laetitia.setRemboursementTelephone(0.0);
        laetitia.setRemboursementTransport(0.0);
        laetitia.setSalaire(300000.0);
        laetitia.setTelephone("01.23.45.67.89");
	     
	     System.out.println(laetitia.toString());
    }
    */
    @Test
    public void test() 
    {
        assertEquals(laetitia.getTitre(),alyssa.getTitre());
        assertEquals(stan.getDateArrivee(),alyssa.getDateArrivee());
    }
    
}

