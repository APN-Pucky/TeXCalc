package TeXCalc.gui;

import java.awt.GridBagConstraints;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import javax.swing.BoxLayout;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Group;
import javax.swing.GroupLayout.ParallelGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import TeXCalc.latex.Latex;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

public class CellList
{
	@JsonIgnore
	@Getter
	public JPanel panel;
	@JsonIgnore
	@Getter
	private GroupLayout layout;
	private Group hg,vg,hg1,hg2;
	//@JsonValue
	//@JsonProperty("property")
	@JsonSerialize(as=ArrayList.class, contentAs=Cell.class)
	public ArrayList<Cell> cells = new ArrayList<Cell>();
	@JsonIgnore
	private HashMap<Cell,Cell.Bar> barmap= new HashMap<Cell,Cell.Bar>();
	

	//@JsonValue
	//@JsonSerialize
	@Getter
	@Setter
	private Latex latex;

	/*
	@JsonIgnore
	public String FRAMETOP =
			"\\usepackage{amsfonts}\n"+
			"\\usepackage{amsmath}\n" +
			"\\usepackage{amsthm}\n"+
			"\\usepackage{slashed}"+
			"\\usepackage[compat=1.1.0]{tikz-feynman}\n" +
			"\\DeclareMathOperator{\\Tr}{Tr}\n"+
			"\\setlength\\parindent{0pt}\n"+
			"\\begin{document}\n";
	@JsonIgnore
	public String FRAMEEND = "\\end{document}\n";
	*/
	
	private Main main;
	
	public CellList( Cell[] cells) {
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
		layout = new GroupLayout(panel);
		panel.setLayout(layout);
		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);
		hg = layout.createSequentialGroup();
		hg1 = layout.createParallelGroup();
		hg2 = layout.createParallelGroup();
		vg = layout.createSequentialGroup();
		hg.addGroup(hg1);
		hg.addGroup(hg2);

		layout.setHorizontalGroup(hg);
		layout.setVerticalGroup(vg);
		System.out.println("max" + layout.maximumLayoutSize(panel));
		for ( int i = 0 ;  i  < number ; ++i)
		{
			increase();
		}
	}
	
	public void setMain(Main m) {
		main = m;
	}
	
	public void export() {
		if(main != null)
			main.export();
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
	
	public void updateFrom(Cell c) {
		if(cells.indexOf(c) < cells.size()-2)
		{
			final Cell c2 = cells.get(cells.indexOf(c)+1);
			c2.queueUpdate(() -> {});
		}
	}
	
	public void link(int index) {

		Cell cell = cells.get(index);
		cell.setLatex(latex);
		cell.setList(this);
		
		JToolBar tools = new JToolBar(JToolBar.HORIZONTAL);
		tools.setFloatable(false);
		Cell.Bar b = cell.addButtons(tools);
		
		GridBagConstraints c = new GridBagConstraints();
		
		
		JPanel tmpp = new JPanel(layout);
		tmpp.setLayout(new BoxLayout(tmpp, BoxLayout.Y_AXIS));
		tmpp.add(tools);

		//tmpp.add(cell.text);
		///*
		
		
		JScrollPane js = new JScrollPane(cell.text
				,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
	            JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
				);
		
		tmpp.add(js);
				//*/

		ParallelGroup p = layout.createParallelGroup(GroupLayout.Alignment.CENTER);
		vg.addGroup(p);
		p.addComponent(tmpp);
		p.addComponent(cell.icon);
		hg1.addComponent(tmpp);
		hg2.addComponent(cell.icon);
				
		//panel.add(tmpp, c);
		
		//c.fill = GridBagConstraints.HORIZONTAL;
		//GUI.log.m(cell.getText()+ (cell.getLatex()==null));
		//panel.add(cell.icon, c);
		//panel.setPreferredSize(new Dimension(1000,200));
		b.setPanel(tmpp);
		barmap.put(cell, b);	
		
	}
	protected void unlinkAll() {
		for(int i =0; i  < this.cells.size();++i)
		{
			unlink(i);
		}
	}
	protected void re() {
		panel.revalidate();
		panel.repaint();
	}
	protected void rmAll() {
		if(GUI.confirm(panel,"RM All","RM All")) {
			unlinkAll();
			cells.clear();
			panel.revalidate();
			panel.repaint();
		}
	}
	protected void up(Cell c) {
		int i = cells.indexOf(c);
		if(i==0) return;
		Cell tmp = cells.get(i-1);
		cells.set(i-1, c);
		cells.set(i, tmp);
		linkAll();
		re();
	}
	protected void down(Cell c) {
		int i = cells.indexOf(c);
		if(i==cells.size()-1) return;
		Cell tmp = cells.get(i+1);
		cells.set(i+1, c);
		cells.set(i, tmp);
		linkAll();
		re();
	}
	

	
	public void unlink(Cell c) {
		unlink(cells.indexOf(c));
	}
	public void unlink(int index) {
		Cell cell = cells.get(index);
		//panel.remove(cell.text);
		panel.remove(cell.icon);
		if(barmap.get(cell)!= null)
			panel.remove(barmap.get(cell).panel);
		barmap.remove(cell);
	}
	
	public void increase()
	{
		Cell c = new Cell(latex);
		cells.add(c);
		//link(cells.size()-1);
		linkAll();
	}
	public void increase(int index)
	{
		Cell c = new Cell(latex);
		cells.add(index,c);
		//link(cells.size()-1);
		linkAll();
	}
	
	/*public void decrease()
	{
		Cell c = cells.get(cells.size()-1);
		unlink(cells.size()-1);
		cells.remove(c);
	}*/
	
	public String toLaTeX()
	{
		String ret = latex.getDocumentType();
		ret+= latex.getTop();
		for(Cell c : cells)
		{
			if(barmap.get(c).export.isSelected())
			{
				ret += c.toLatex() ;
			}
		}
		ret+= latex.getEnd();
		return ret;
	}
}
