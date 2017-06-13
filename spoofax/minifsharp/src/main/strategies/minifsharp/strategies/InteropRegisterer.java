package minifsharp.strategies;

import org.strategoxt.lang.JavaInteropRegisterer;
import org.strategoxt.lang.Strategy;

public class InteropRegisterer extends JavaInteropRegisterer {
    public InteropRegisterer() {
        super(new Strategy[] { strategy_generate_cil_0_1.instance, strategy_run_cil_0_1.instance });
    }
}
