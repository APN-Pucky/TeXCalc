package TeXCalc.latex.wrap.env;

import TeXCalc.latex.wrap.DefaultTeXable;
import lombok.Getter;
import lombok.Setter;

public class Environment implements DefaultTeXable {
	String name;
	@Getter @Setter
	String begin = "";
	@Getter @Setter
	String end = "";
	public Environment(String name) {
		this.name = name;
		setBegin("\\begin{" + name + "}");
		setEnd("\\end{" + name + "}");
	}
	@Override
	public String to(String s) {
		// TODO Auto-generated method stub
		return getBegin() + s + getEnd();
	}

}
