package TeXCalc.gui;

import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.JPanel;

public class CellList extends JPanel
{
	public ArrayList<Cell> cells = new ArrayList<Cell>();
	
	public CellList() {
		this(5);
	}
	public CellList(int number) {
		this.setLayout(new GridLayout(0,2,0,20));
		for ( int i = 0 ;  i  < number ; ++i)
		{
			increase();
		}
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
}
