package TeXCalc.compat;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

import TeXCalc.gui.GUI;

public class Compat {

	public static String fix(File f) {
		String s= "";
		try {
			s = FileUtils.readFileToString(f, "UTF-8");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(!s.contains("celllist") && !s.contains("version") && !s.contains("cells") && !(s.contains("latex") && s.contains("top") && s.contains("end")))
		{
			String r = "{\n" + 
					"  \"version\" : \"0.0\",\n" + 
					"  \"celllist\" : {\n" + 
					"    \"cells\" : ";
			r += s.replaceAll("\"env\"", "\"environment\"");
			r += ",\n" + 
					"    \"latex\" : {\n" + 
					"      \"top\" : \"\\\\usepackage{amsfonts}\\n\\\\usepackage{amsmath}\\n\\\\usepackage{amsthm}\\n\\\\usepackage{slashed}\\\\usepackage[compat=1.1.0]{tikz-feynman}\\n\\\\DeclareMathOperator{\\\\Tr}{Tr}\\\\setlength\\\\parindent{0pt}\\\\begin{document}\\n\",\n" + 
					"      \"end\" : \"\\\\end{document}\\n\"\n" + 
					"    }\n" + 
					"  }\n" + 
					"}";
			s = r;
			GUI.log.m("FIXED");
		}
		return s;
	}
}
