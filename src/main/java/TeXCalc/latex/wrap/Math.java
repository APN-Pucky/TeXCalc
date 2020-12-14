package TeXCalc.latex.wrap;

public class Math extends Wrapper{
	public String to(String s )
	{
		s = s.replaceAll("\\(", "\\\\left(");
		s = s.replaceAll("\\)", "\\\\right)");
		return s;
	}

}
