package testsJUnit;
import org.junit.Before;
import org.junit.Test;

import classes.Chantier;

public class ChantierTest
{
    private Chantier puteaux;
    private Chantier boulogne;

    @Before
    public void setUp() 
    {
        puteaux = new Chantier("Puteaux","à Puteaux",100.0,"Publié");
        boulogne = new Chantier("Boulogne","Publié");
        boulogne.setAdresse("à Boulogne");
        boulogne.setCA(200.0);
    }
    
    @Test
    public void testGet() 
    {
	     String s = "";
	     s += puteaux.getNom() + ", ";
	     s += puteaux.getAdresse() + ", ";
	     s += puteaux.getCA() + ", ";
	     
	     System.out.println(s);
    }
    
    @Test
    public void test() 
    {

    	
    }
    
    
}

