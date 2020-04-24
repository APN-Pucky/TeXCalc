package TeXCalc.gui;

import java.awt.GridLayout;

import javax.swing.JFrame;

public class Main extends JFrame{
	public Main()
	{
		this.setLayout(new GridLayout(0,2));
		new Cell(this);
		new Cell(this);
		new Cell(this);
		this.setVisible(true);
	}
}
