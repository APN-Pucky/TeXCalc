package TeXCalc.latex;

public interface DefaultTeXable extends TeXable{
public String to(String s);
public default String toDocument(String s) {return to(s);}
public default String toStandalone(String s) {return to(s);}
}
