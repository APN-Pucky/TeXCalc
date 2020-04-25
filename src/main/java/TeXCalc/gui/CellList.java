package TeXCalc.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JToolBar;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class CellList extends JPanel
{
	@JsonValue
	@JsonProperty("property")
	@JsonDeserialize(as=ArrayList.class, contentAs=Cell.class)
	public ArrayList<Cell> cells = new ArrayList<Cell>();
	private HashMap<Cell,JToolBar> barmap= new HashMap<Cell,JToolBar>();
	
	public CellList(Cell[] cells) {
		this();
		this.cells = new ArrayList<Cell>(Arrays.asList(cells));
		for(int i =0; i  < this.cells.size();++i)
		{
			link(i);
		}
	}
	
	public CellList() {
		this(0);
	}
	
	public CellList(int number) {
		this.setLayout(new GridBagLayout());
		for ( int i = 0 ;  i  < number ; ++i)
		{
			increase();
		}
	}
	
	public void update() {
		for(Cell c : cells)
			c.queueUpdate();
	}
	
	public void link(int index) {

		Cell cell = cells.get(index);
		
		JToolBar tools = new JToolBar(JToolBar.VERTICAL);
		addButtons(tools,cell);
		
		GridBagConstraints c = new GridBagConstraints();
		
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 0.1;
		c.weighty = 0.1;
		c.gridx = 0;
		c.gridy = index;
		add(tools, c);
		
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 0.5;
		c.weighty = 0.1;
		c.gridx = 1;
		c.gridy = index;
		add(cell.text, c);
		
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 0.5;
		c.weighty = 0.1;
		c.gridx = 2;
		c.gridy = index;
		add(cell.icon, c);
		
	}
	
	protected void addButtons(JToolBar toolBar,Cell c) {
		JButton button = null;

		button = GUI.buttonSync("Clean", () -> {c.setText(""); c.queueUpdate();});
		toolBar.add(button);
		
		button = GUI.buttonSync("Remove", () -> {unlink(c);cells.remove(c);this.revalidate();this.repaint();});
		toolBar.add(button);
		
		JComboBox<String> petList = new JComboBox<String>(Cell.envs);
		toolBar.add(petList);
		
		petList.setSelectedItem(c.getEnv());	
		
		petList.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent event) {
			       if (event.getStateChange() == ItemEvent.SELECTED) {
			          String item = (String) event.getItem();
			          // do something with object
			          c.setEnv(item);
			          c.queueUpdate();
			       }
			    }   
		});
		
		barmap.put(c, toolBar);
		
	}
	public void unlink(Cell c) {
		unlink(cells.indexOf(c));
	}
	public void unlink(int index) {
		Cell cell = cells.get(index);
		this.remove(cell.text);
		this.remove(cell.icon);
		this.remove(barmap.get(cell));
		barmap.remove(cell);
	}
	
	public void increase()
	{
		Cell c = new Cell();
		cells.add(c);
		link(cells.size()-1);
	}
	
	public void decrease()
	{
		Cell c = cells.get(cells.size()-1);
		unlink(cells.size()-1);
		cells.remove(c);
	}
	
	public String toLaTeX()
	{
		String ret = "\\documentclass{article}\n";
		ret+= "\\usepackage{amsfonts}\n";
		ret += "\\usepackage{amsmath}\n" ;
		ret += "\\usepackage{amsthm}\n";
		ret += "\\setlength{\\parindent}{0in}";
		ret += "\\begin{document}\n";
		for(Cell c : cells)
		{
			ret += c.toLatex() + "\n";
		}
		return ret + "\\end{document}";
	}
}
