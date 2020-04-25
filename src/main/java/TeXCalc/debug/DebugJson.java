package TeXCalc.debug;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import TeXCalc.gui.Cell;
import TeXCalc.gui.CellList;

public class DebugJson {
	public static void main(String[] args) throws JsonGenerationException, JsonMappingException, IOException
	{
		CellList cl = new CellList(5);
		Cell c = new Cell("test");
		ObjectMapper objectMapper = new ObjectMapper();
		//objectMapper.writeValue(System.out,cl);
		
		Cell[] cells = objectMapper.readValue("[{\"text\":\"keku\"},{\"text\":\"keku\"}]",Cell[].class);
		System.out.println(cells[1].getText());
	}
}
