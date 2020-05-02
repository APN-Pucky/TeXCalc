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
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JToolBar;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonIgnoreType;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import TeXCalc.latex.Latex;
import lombok.Getter;
import lombok.Setter;

public class CellList
{
	@JsonIgnore
	@Getter
	private JPanel panel;
	//@JsonValue
	//@JsonProperty("property")
	@JsonSerialize(as=ArrayList.class, contentAs=Cell.class)
	public ArrayList<Cell> cells = new ArrayList<Cell>();
	@JsonIgnore
	private HashMap<Cell,JToolBar> barmap= new HashMap<Cell,JToolBar>();

	//@JsonValue
	//@JsonSerialize
	@Getter
	@Setter
	private Latex latex;

	@JsonIgnore
	public String FRAMETOP =
			"\\usepackage{amsfonts}\n"+
			"\\usepackage{amsmath}\n" +
			"\\usepackage{amsthm}\n"+
			"\\usepackage{slashed}"+
			"\\usepackage[compat=1.1.0]{tikz-feynman}\n" +
			"\\DeclareMathOperator{\\Tr}{Tr}"+
			"\\setlength\\parindent{0pt}"+
			"\\begin{document}\n";
	@JsonIgnore
	public String FRAMEEND = "\\end{document}\n";
	
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
		panel = new JPanel();
		latex = new Latex();
		panel.setLayout(new GridBagLayout());
		for ( int i = 0 ;  i  < number ; ++i)
		{
			increase();
		}
	}
	
	public void linkAll() {
		for(int i =0 ; i  < cells.size(); ++i)
		{
			unlink(i);
			link(i);
		}
	}
	
	public void update() {
		for(Cell c : cells)
			c.queueUpdate();
	}
	
	public void link(int index) {

		Cell cell = cells.get(index);
		cell.setLatex(latex);
		
		JToolBar tools = new JToolBar(JToolBar.VERTICAL);
		addButtons(tools,cell);
		
		GridBagConstraints c = new GridBagConstraints();
		
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 0.1;
		c.weighty = 0.1;
		c.gridx = 0;
		c.gridy = index;
		panel.add(tools, c);
		
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 0.5;
		c.weighty = 0.1;
		c.gridx = 1;
		c.gridy = index;
		panel.add(cell.text, c);
		
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 0.5;
		c.weighty = 0.1;
		c.gridx = 2;
		c.gridy = index;
		GUI.log.m(cell.getText()+ (cell.getLatex()==null));
		panel.add(cell.icon, c);
		
	}
	
	protected void addButtons(JToolBar toolBar,Cell c) {
		JButton button = null;

		button = GUI.buttonSync("Clean", () -> {if (c.getText().equals("") || GUI.confirm(panel,"RM","RM") ){c.setText(""); c.queueUpdate();}});
		toolBar.add(button);
		
		button = GUI.buttonSync("Remove", () -> {if (c.getText().equals("") || GUI.confirm(panel,"RM","RM") ){unlink(c);cells.remove(c);panel.revalidate();panel.repaint();}});
		toolBar.add(button);
		
		JComboBox<String> petList = new JComboBox<String>(Cell.envs);
		toolBar.add(petList);
		
		petList.setSelectedItem(c.getEnvironment());	
		
		petList.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent event) {
			       if (event.getStateChange() == ItemEvent.SELECTED) {
			          String item = (String) event.getItem();
			          // do something with object
			          c.setEnvironment(item);
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
		panel.remove(cell.text);
		panel.remove(cell.icon);
		if(barmap.get(cell) != null)
			panel.remove(barmap.get(cell));
		barmap.remove(cell);
	}
	
	public void increase()
	{
		Cell c = new Cell(latex);
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
		String ret = latex.getDocumentType();
		ret+= Latex.FRAMETOP;
		for(Cell c : cells)
		{
			ret += c.toLatex() + "\n";
		}
		ret+= Latex.FRAMEEND;
		return ret;
	}
}
