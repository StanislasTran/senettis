package testsJUnit;
import org.junit.Before;
import org.junit.Test;

import classes.Site;
import classes.Status;


public class ChantierTest
{
	
    private Site puteaux;
    private Site boulogne;

    @Before
    public void setUp() 
    {
        puteaux = new Site("Puteaux","� Puteaux",Status.PUBLISHED);
        boulogne = new Site("Boulogne",Status.PUBLISHED);
        boulogne.setAdress("� Boulogne");
       ;
    }
    
    @Test
    public void testGet() 
    {
	     String s = "";
	     s += puteaux.getName() + ", ";
	     s += puteaux.getAdresse() + ", ";
	     
	     
	     System.out.println(s);
    }
    
    @Test
    public void test() 
    {

    	
    }
    
    
}

