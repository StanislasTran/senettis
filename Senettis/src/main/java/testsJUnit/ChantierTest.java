package testsJUnit;
import static org.junit.Assert.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;

import org.junit.After;
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
        puteaux = new Chantier("Puteaux","� Puteaux",100.0,"Publi�");
        boulogne = new Chantier("Boulogne","Publi�");
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

