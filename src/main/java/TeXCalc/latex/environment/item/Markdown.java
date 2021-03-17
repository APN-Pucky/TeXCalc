package TeXCalc.latex.environment.item;

import TeXCalc.latex.environment.Environment;

public class Markdown extends Environment{

	public Markdown() {
		super("markdown");
		requirePackage("markdown");
		
	}
	public String to(String s) {
		//TODO assert usepagake package
		return super.to(s + "\n");
	}
}
