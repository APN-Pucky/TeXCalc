package TeXCalc.latex.wrap.math;

import TeXCalc.latex.Latex;

public class Align extends Math{
	public Align() {
		super();
		//setBegin(getBegin()+Latex.begin("aligned"));
		//setEnd(Latex.end("aligned")+getEnd());
	}
	public String toStandalone(String s) {
		String ret = "";
		ret +="$"+Latex.begin("aligned");
		ret += to(s);
		ret +=Latex.end("aligned") + "$";
		return (ret);
		
	}
	public String toDocument(String s) {
		String ret = "";
		ret +=Latex.begin("align");
		ret += to(s);
		ret +=Latex.end("align");
		return ret;	
	}
}
