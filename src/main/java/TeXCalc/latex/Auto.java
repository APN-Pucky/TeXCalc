package TeXCalc.latex;

import TeXCalc.latex.environment.Environment;
import TeXCalc.latex.environment.item.Markdown;
import TeXCalc.latex.wrap.Wrapper;
import TeXCalc.latex.wrap.math.Align;
import TeXCalc.latex.wrap.math.Equation;
import TeXCalc.mathematica.Mathematica;
import TeXCalc.python.Python;

public class Auto implements TeXable{
	
	public TeXable guess(String s) {
		if(s.contains("\\item")) {
			return new Environment("itemize");
		}
		if(s.contains("&") || s.contains("\\\\")) 
		{
			return new Align();
		}
		if(s.contains("$")) {
			return new Wrapper();
		}
		if(s.contains("%") || s.contains("D[") || s.contains("Integrate["))
		{
			return new Mathematica();
		}
		if(s.contains("import") || s.contains("print("))
		{
			return new Python();
		}
		if(s.contains("^") || s.contains("_")) {
			return new Equation();
		}
		if(s.contains("#"))
		{
			return new Markdown();
		}
		
		return new Wrapper();
	}
	@Override
	public String toStandalone(String s, Latex l) {
		return guess(s).toStandalone(s, l);
	}

	@Override
	public String toDocument(String s, Latex l) {
		return guess(s).toDocument(s, l);
	}

}
