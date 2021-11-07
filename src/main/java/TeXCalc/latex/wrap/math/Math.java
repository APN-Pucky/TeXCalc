package TeXCalc.latex.wrap.math;

import TeXCalc.config.Config;
import TeXCalc.latex.wrap.Wrapper;
import TeXCalc.util.StringUtils;

public class Math extends Wrapper{
	public String to(String s )
	{
		if(Config.current.getLatex().getAutoLeftRightBracket().getValue()) {
			if(StringUtils.count(s, '(', 0) == StringUtils.count(s, ')', 0)) {
				s = s.replaceAll("(?<!(left)|(right))\\(", "\\\\left(");
				s = s.replaceAll("(?<!(left)|(right))\\)", "\\\\right)");
			}
			if(StringUtils.count(s, '{', 0) == StringUtils.count(s, '}', 0)) {
				s = s.replaceAll("(?<!(left)|(right))\\\\\\{", "\\\\left\\\\{");
				s = s.replaceAll("(?<!(left)|(right))\\\\\\}", "\\\\right\\\\}");
			}
			if(StringUtils.count(s, '[', 0) == StringUtils.count(s, ']', 0)) {
				s = s.replaceAll("(?<!(left)|(right))\\[", "\\\\left[");
				s = s.replaceAll("(?<!(left)|(right))\\]", "\\\\right]");
			}
		}
		if(Config.current.getLatex().getAutoLeftRightAbs().getValue()) {
			if(StringUtils.count(s, '|', 0) %2 == 0 ) {
			String os ;
			do {
				os = s;
				s = s.replaceFirst("(?<!(left)|(right))\\|", "\\\\left|");
				s = s.replaceFirst("(?<!(left)|(right))\\|", "\\\\right|");
			}while(!s.equals(os));
		}
		}
		return s;
	}

}
