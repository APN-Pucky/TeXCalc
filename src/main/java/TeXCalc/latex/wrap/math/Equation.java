package TeXCalc.latex.wrap.math;

import java.util.regex.Pattern;

import TeXCalc.latex.Latex;
import TeXCalc.latex.wrap.Wrapper;
import lombok.Getter;
import lombok.Setter;

public class Equation extends Wrapper{
	public Equation() {
		setBegin(Latex.begin("equation"));
		setEnd(Latex.end("equation"));
	}
	public String toStandalone(String s) {
		String ret = toDocument(s);
		ret = ret.replaceAll(Pattern.quote(Latex.begin("equation")), "\\$");
		ret = ret.replaceAll(Pattern.quote(Latex.end("equation")), "\\$");
		return ret;
	}
}
