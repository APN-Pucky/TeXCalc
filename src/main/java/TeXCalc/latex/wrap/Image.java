package TeXCalc.latex.wrap;

public class Image extends Wrapper {
	public Image() {
		setBegin("\\includegraphics{");
		setEnd("}");
	}
	public String toStandalone(String s) {
		return getBegin() + to(s) + getEnd() + "\n";
	}
	public String toDocument(String s) {
		if(s.trim().equals(""))return"";
		return getBegin() + to(s) + getEnd() + "\n";
	}
}
