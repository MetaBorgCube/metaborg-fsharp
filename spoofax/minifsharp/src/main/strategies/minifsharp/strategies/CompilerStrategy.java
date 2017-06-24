package minifsharp.strategies;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
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

public abstract class CompilerStrategy extends Strategy {

	private final String PROJECT_DIRECTORY = "C:\\Users\\info\\Documents\\GitHub2\\metaborg-fsharp\\spoofax\\minifsharp\\";

	@Override public IStrategoTerm invoke(Context context, IStrategoTerm current, IStrategoTerm path) {
        PrintStream origErr = System.err;
        PrintStream origOut = System.out;
        ByteArrayOutputStream err = new ByteArrayOutputStream();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setErr(new PrintStream(err));
        System.setOut(new PrintStream(out));
        
        runStrategy(context, current, path);
        
        System.setErr(origErr);
        System.setOut(origOut);
        
        // return output collected in both temporary streams
        ITermFactory factory = context.getFactory();
        return factory.makeAppl(factory.makeConstructor("Output", 2), factory.makeString(out.toString()),
            factory.makeString(err.toString()));
	}
	
	protected abstract void runStrategy(Context context, IStrategoTerm current, IStrategoTerm path);

	/**
	 * Returns the real path to the file that needs to be compiled
	 * @param path
	 * @return
	 */
	protected String getPath(IStrategoTerm path) {
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
	
	protected String getPathCil(IStrategoTerm path) {
		String filename = getPath(path);
        return filename.substring(0, filename.lastIndexOf('.')) + ".cil";
	}
	
	protected String getPathExe(IStrategoTerm path) {
		String filename = getPath(path);
        return filename.substring(0, filename.lastIndexOf('.')) + ".exe";
	}

	/**
	 * Run a console command
	 * @param command
	 */
	protected void runCommand(String command) {
        Runtime r = Runtime.getRuntime();
        Process p;
        
		try {
			p = r.exec(command);
			p.waitFor();
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Run a console command where output is printed to the output stream
	 * @param command
	 */
	protected void runCommandVerbose(String command) {
        Runtime r = Runtime.getRuntime();
        Process p;
        
		try {
			p = r.exec(command);
			p.waitFor();
	        
	        BufferedReader b = new BufferedReader(new InputStreamReader(p.getInputStream()));
	        String line = "";

	        while ((line = b.readLine()) != null) {
	        	System.out.print(line);
	        }
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
	}

}
