package TeXCalc.latex.wrap;

public class Image extends Wrapper {
	public Image() {
		setBegin("\\includegraphics[");
		setEnd("}");
	}
	public String to(String s) {
		if(s.contains(" ")) {
			String[] args = s.split(" ");
			return (args[1])  + "]{"+ (args[0]);
		}
		else {
			return "]{" + s;
		}
		
	}
	public String toStandalone(String s) {
		return getBegin() + to(s) + getEnd() + "\n";
	}
	public String toDocument(String s) {
		if(s.trim().equals(""))return"";
		return getBegin() +  to(s) + getEnd() + "\n";
	}
}
