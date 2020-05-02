package TeXCalc.debug;

import java.io.IOException;import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import TeXCalc.gui.Cell;
import TeXCalc.gui.CellList;
import TeXCalc.latex.Latex;

public class DebugJson {
	public static void main(String[] args) throws JsonGenerationException, JsonMappingException, IOException
	{
		CellList cl = new CellList(5);
		Cell c = new Cell("test");
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.writeValue(System.out, cl);
		cl = objectMapper.readValue("{\"cells\":[{\"text\":\"heho\",\"env\":\"equation\"},{\"text\":\"\",\"env\":\"equation\"},{\"text\":\"\",\"env\":\"equation\"},{\"text\":\"\",\"env\":\"equation\"},{\"text\":\"\",\"env\":\"equation\"}],\"tex\":{\"top\":\"\\\\usepackage{amsfonts}\\n\\\\usepackage{amsmath}\\n\\\\usepackage{amsthm}\\n\\\\usepackage{slashed}\\\\usepackage[compat=1.1.0]{tikz-feynman}\\n\\\\DeclareMathOperator{\\\\Tr}{Tr}\\\\setlength\\\\parindent{0pt}\\\\begin{document}\\n\",\"end\":\"\\\\end{document}\\n\"}}",CellList.class);
		//Cell[] cells = objectMapper.readValue("[{\"text\":\"keku\"},{\"text\":\"keku\"}]",Cell[].class);
		
		System.out.println(cl.cells.get(0).getText());
	}
}
