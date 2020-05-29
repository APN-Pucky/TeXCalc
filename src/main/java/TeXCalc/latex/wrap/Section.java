package TeXCalc.latex.wrap;

public class Section extends Wrapper{
	public Section() {
		setBegin("\\section{");
		setEnd("}");
	}
	public String toStandalone(String s) {
		return s;
	}
}
