package TeXCalc.latex.wrap;

import TeXCalc.latex.Latex;

public class Aligned extends Wrapper {
	public Aligned() {
		super();
		//setBegin(getBegin()+Latex.begin("aligned"));
		//setEnd(Latex.end("aligned")+getEnd());
	}
	public String toStandalone(String s) {
		String ret = "";
		ret +="$"+Latex.begin("aligned");
		ret += s;
		ret +=Latex.end("aligned") + "$";
		return (ret);
		
	}
	public String toDocument(String s) {
		String ret = "";
		ret +=Latex.begin("align");
		ret += s;
		ret +=Latex.end("align");
		return ret;	
	}
}
