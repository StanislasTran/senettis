package testsJUnit;
import org.junit.Before;
import org.junit.Test;

import classes.Site;


public class ChantierTest
{
	
    private Site puteaux;
    private Site boulogne;

    @Before
    public void setUp() 
    {
        puteaux = new Site("Puteaux","� Puteaux",100.0,"Publi�");
        boulogne = new Site("Boulogne","Publi�");
        boulogne.setAdresse("� Boulogne");
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

