package TeXCalc.latex.wrap.env.item;

import TeXCalc.latex.wrap.env.Environment;

public class ItemEnvironment extends Environment 
{

	public ItemEnvironment(String name) {
		super(name);
	}
	
	public String to(String s) {
		return getBegin() + "\\item " + s.replaceAll("\n", "\\n\\\\item ") + getEnd();
	}
	

}
