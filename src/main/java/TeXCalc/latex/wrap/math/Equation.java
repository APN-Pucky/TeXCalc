package TeXCalc.latex.wrap.math;

import java.util.regex.Pattern;

import TeXCalc.latex.Latex;
import TeXCalc.latex.wrap.Wrapper;
import lombok.Getter;
import lombok.Setter;

public class Equation extends Math{
	public Equation() {
		super();
		//setBegin(getBegin()+Latex.begin("aligned"));
		//setEnd(Latex.end("aligned")+getEnd());
	}
	public String toStandalone(String s) {
		String ret = "";
		ret +="$";
		ret += to(s);
		ret += "$";
		return (ret);
		
	}
	public String toDocument(String s) {
		String ret = "";
		ret +=Latex.begin("equation");
		ret += to(s);
		ret +=Latex.end("equation");
		return ret;	
	}
}
