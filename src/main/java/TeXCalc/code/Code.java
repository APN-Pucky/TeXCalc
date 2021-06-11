package TeXCalc.code;

import javax.management.RuntimeErrorException;

import TeXCalc.latex.Latex;
import TeXCalc.latex.TeXable;

public interface Code extends TeXable
{
	public String to(String in, Latex l);
	public default String toStandalone(String s,Latex l) {
		// TODO Auto-generated method stub
		return to(s,l);
	}

	public default String toDocument(String s,Latex l) {
		// TODO Auto-generated method stub
		return to(s,l);
	}

}
