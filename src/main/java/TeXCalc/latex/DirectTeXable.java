package TeXCalc.latex;

public interface DirectTeXable extends TeXable{
public default String toStandalone(String s,Latex l) {return toStandalone(s);};
public String toStandalone(String s);
public default String toDocument(String s,Latex l) {return toDocument(s);};
public String toDocument(String s);
}
