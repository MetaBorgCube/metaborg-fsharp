package minifsharp.strategies;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.URI;
import java.nio.file.Paths;

import org.spoofax.interpreter.terms.IStrategoString;
import org.spoofax.interpreter.terms.IStrategoTerm;
import org.spoofax.interpreter.terms.ITermFactory;
import org.strategoxt.lang.Context;
import org.strategoxt.lang.Strategy;

public class strategy_run_cil_0_1 extends Strategy {
    public static strategy_run_cil_0_1 instance = new strategy_run_cil_0_1();
	private final String PROJECT_DIRECTORY = "/home/sander/workspace/master-jaar1/lang-eng/language-engineering-project/spoofax/minifsharp/";
    
	@Override public IStrategoTerm invoke(Context context, IStrategoTerm current, IStrategoTerm path) {
        PrintStream origErr = System.err;
        PrintStream origOut = System.out;
        ByteArrayOutputStream err = new ByteArrayOutputStream();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setErr(new PrintStream(err));
        System.setOut(new PrintStream(out));
        
        
//        removeExecutable(path);
        compileCil(path);
        if (!new File(getPathExe(path)).canExecute())
        	chmodExecutable(path);
        runExecutable(path);
        
        System.setErr(origErr);
        System.setOut(origOut);
        
        // return output collected in both temporary streams
        ITermFactory factory = context.getFactory();
        return factory.makeAppl(factory.makeConstructor("Output", 2), factory.makeString(out.toString()),
            factory.makeString(err.toString()));
	}
	
	private void compileCil(IStrategoTerm path) {
        Runtime r = Runtime.getRuntime();
        Process p;
        
		try {
			p = r.exec("ilasm " + getPathCil(path));
			p.waitFor();
	        
	        BufferedReader b = new BufferedReader(new InputStreamReader(p.getInputStream()));
	        String line = "";

	        while ((line = b.readLine()) != null) {
	        }
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
		
	}
	
	private void runExecutable(IStrategoTerm path) {
        Runtime r = Runtime.getRuntime();
        Process p;
        
		try {
			p = r.exec(getPathExe(path));
	        p.waitFor();
	        
	        BufferedReader b = new BufferedReader(new InputStreamReader(p.getInputStream()));
	        String line = "";

	        while ((line = b.readLine()) != null) {
	        	System.out.print(line);
	        }
	        b.close();
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	private void chmodExecutable(IStrategoTerm path) {
        Runtime r = Runtime.getRuntime();
        Process p;
        
		try {
			p = r.exec("chmod +x " + getPathExe(path));
	        p.waitFor();
	        
	        BufferedReader b = new BufferedReader(new InputStreamReader(p.getInputStream()));
	        String line = "";

	        while ((line = b.readLine()) != null) {
	        	System.out.println(line);
	        }
	        b.close();
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	private void removeExecutable(IStrategoTerm path) {
        Runtime r = Runtime.getRuntime();
        Process p;
        
		try {
			p = r.exec("rm " + getPathExe(path));
	        p.waitFor();
	        
	        BufferedReader b = new BufferedReader(new InputStreamReader(p.getInputStream()));
	        String line = "";

	        while ((line = b.readLine()) != null) {
	        	System.out.println(line);
	        }
	        b.close();
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	private String getPath(IStrategoTerm path) {
		String filename = PROJECT_DIRECTORY + ((IStrategoString) path).stringValue();
		String p = ((IStrategoString) path).stringValue();
		try {
			p = URI.create(p).getPath();
		}
		catch (Exception ex) { }
		
		if (Paths.get(p).isAbsolute())
			filename = p;
		
		return filename;
	}
	
	private String getPathExe(IStrategoTerm path) {
		String filename = getPath(path);
        return filename.substring(0, filename.lastIndexOf('.')) + ".exe";
	}

	private String getPathCil(IStrategoTerm path) {
		String filename = getPath(path);
		
        return filename.substring(0, filename.lastIndexOf('.')) + ".cil";
	}
}