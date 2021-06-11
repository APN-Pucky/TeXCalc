package TeXCalc.latex;

import java.util.ArrayList;

public interface TeXable {
public String toStandalone(String s,Latex l) ;
public String toDocument(String s,Latex l); 
public ArrayList<String> requirements = new ArrayList<String>();
public default String[] getRequirements() {return requirements.toArray(new String[] {});};
public default void require(String s) {requirements.add(s);};
public default void requirePackage(String s) {requirements.add("\\usepackage{" + s + "}");};
}
