/**
 * 
 */
package boogieamp_tests;

import java.io.File;
import java.util.Collection;
import java.util.LinkedList;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import typechecker.TypeChecker;
import boogie.ProgramFactory;
import boogie.controlflow.DefaultControlFlowFactory;

/**
 * @author schaef
 *
 */
@RunWith(Parameterized.class)
public class TestParseSmack  {
	
	@Parameterized.Parameters (name = "{index}: parse({1})")
	public static Collection<Object[]> data() {
		LinkedList<Object[]> filenames = new LinkedList<Object[]>();
		String dirname = System.getProperty("user.dir")+"/regression/smack";
		  File dir = new File(dirname);

		  File[] directoryListing = dir.listFiles();
		  if (directoryListing != null) {
		    for (File child : directoryListing) {		    	
		    	if (child.getName().endsWith(".bpl")) {
		    		filenames.add(new Object[] {child.getAbsolutePath(), child.getName()});
		    	} else {
		    		//Ignore
		    	}
		    }
		  } else {
		    // Handle the case where dir is not really a directory.
		    // Checking dir.isDirectory() above would not be sufficient
		    // to avoid race conditions with another process that deletes
		    // directories.
		  }				  
	   return filenames;
   }
	
    private String input;
    private String shortname;

    public TestParseSmack(String input, String shortname) {
        this.input = input;
        this.shortname = shortname;
    }

	
	@Test
	public void test() {
		ProgramFactory pf = null;
		try {
			pf = new ProgramFactory(this.input);				
		} catch (Exception e) {		    			
			e.printStackTrace();
			org.junit.Assert.assertTrue("Parse error: " + e.toString(), false);
			return;
		}
		TypeChecker tc = new TypeChecker(pf.getASTRoot(), false);
		try {
			DefaultControlFlowFactory cff = new DefaultControlFlowFactory(pf.getASTRoot(), tc);
			cff.getGlobalAxioms();
		} catch (Exception e) {
			e.printStackTrace();
			org.junit.Assert.assertTrue(false);
		}
		org.junit.Assert.assertTrue(!tc.hasTypeError());
		
	}

}
