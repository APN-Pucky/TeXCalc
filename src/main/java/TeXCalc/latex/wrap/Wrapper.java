package TeXCalc.latex.wrap;

import lombok.Getter;
import lombok.Setter;

public class Wrapper {

	@Getter @Setter
	String begin = "";
	@Getter @Setter
	String end = "";
	public String to(String s) {
		return s;
	}
	public String toStandalone(String s) {
		return to(s);
	}
	public String toDocument(String s) {
		if(s.trim().equals(""))return"";
		return getBegin() +  "\n" + to(s) +   "\n" + getEnd() + "\n";
	}
}
