package TeXCalc.gui;

import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class Main extends JFrame
{
	public Main()
	{
			CellList cl = null;
			this.add(new JScrollPane(cl =new CellList()));
			this.setVisible(true);
			this.pack();
			cl.increase();
			cl.increase();
			cl.increase();

			cl.increase();
			cl.increase();
			cl.increase();			
	}
}
