package minifsharp.strategies;

import java.io.File;

import org.spoofax.interpreter.terms.IStrategoTerm;
import org.strategoxt.lang.Context;

public class strategy_generate_cil_0_1 extends CompilerStrategy {
    public static strategy_generate_cil_0_1 instance = new strategy_generate_cil_0_1();

	@Override
	protected void runStrategy(Context context, IStrategoTerm current, IStrategoTerm path) {
		
		// If .exe file exists remove .exe file
        if (new File(getPathExe(path)).exists()) {
        	runCommand("rm " + getPathExe(path));
        }
    	
		// Compile .cil to .exe
		runCommandVerbose("ilasm " + getPathCil(path));
	}
}