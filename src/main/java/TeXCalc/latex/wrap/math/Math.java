package TeXCalc.latex.wrap.math;

import TeXCalc.config.Config;
import TeXCalc.latex.wrap.Wrapper;

public class Math extends Wrapper{
	public String to(String s )
	{
		if(Config.current.getAutoLeftRight()) {
			s = s.replaceAll("\\(", "\\\\left(");
			s = s.replaceAll("\\)", "\\\\right)");
		}
		return s;
	}

}
