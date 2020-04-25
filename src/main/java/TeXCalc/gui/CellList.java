package TeXCalc.gui;

import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JPanel;

import org.json.JSONObject;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class CellList extends JPanel
{
	@JsonValue
	@JsonProperty("property")
	@JsonDeserialize(as=ArrayList.class, contentAs=Cell.class)
	public ArrayList<Cell> cells = new ArrayList<Cell>();
	
	public CellList(Cell[] cells) {
		this();
		this.cells = new ArrayList<Cell>(Arrays.asList(cells));
		for(Cell c : cells)
		{
			c.link(this);
		}
	}
	
	public CellList() {
		this(0);
	}
	
	public CellList(int number) {
		this.setLayout(new GridLayout(0,2,0,20));
		for ( int i = 0 ;  i  < number ; ++i)
		{
			increase();
		}
	}
	
	public void update() {
		for(Cell c : cells)
			c.queueUpdate();
	}
	
	
	public void increase()
	{
		Cell c = new Cell();
		c.link(this);
		cells.add(c);
	}
	
	public void decrease()
	{
		Cell c = cells.get(cells.size()-1);
		c.unlink(this);
		cells.remove(c);
	}
	
	public String toLaTeX()
	{
		String ret = "\\documentclass{article}\n\\begin{document}\n";
		for(Cell c : cells)
		{
			ret += c.toLatex() + "\n";
		}
		return ret + "\\end{document}";
	}
}
