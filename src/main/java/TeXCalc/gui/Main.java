package TeXCalc.gui;

import java.awt.BorderLayout;
import java.io.File;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;

import com.fasterxml.jackson.databind.ObjectMapper;

public class Main extends JFrame {
	CellList cl = null;
	JScrollPane jsp = null;
	public Main() {
		
		this.setLayout(new BorderLayout());
		

		JMenuBar menubar = new JMenuBar();
		JMenu menu = new JMenu("A menu");
		menubar.add(menu);

		this.setJMenuBar(menubar);
		
		JToolBar toolBar = new JToolBar("Still draggable");
		addButtons(toolBar);
		
		this.add(toolBar, BorderLayout.PAGE_START);
		
		this.add(jsp =new JScrollPane(cl = new CellList()));
		this.setVisible(true);
		this.pack();
	}

	protected void addButtons(JToolBar toolBar) {
		JButton button = null;

		// first button
		button = GUI.buttonSync("Save", () -> save());
		toolBar.add(button);

		// second button
		button = GUI.buttonAsync("Load", () -> load());
		toolBar.add(button);

		button = GUI.buttonAsync("Refresh", () -> update());
		toolBar.add(button);

		button = GUI.buttonAsync("Add Cell", () -> addCell());
		toolBar.add(button);
	}
	
	public void addCell() {
		cl.increase();
		this.pack();
		this.repaint();
	}

	public void save() {

		ObjectMapper objectMapper = new ObjectMapper();
		try {
			objectMapper.writeValue(new File("save.json"),cl);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void load() {
		final JFileChooser fc = new JFileChooser(new File(System.getProperty("user.dir")));
		 int returnVal = fc.showOpenDialog(this);

	        if (returnVal == JFileChooser.APPROVE_OPTION) {
	            File file = fc.getSelectedFile();
	            ObjectMapper objectMapper = new ObjectMapper();
	            try {
					Cell[] cells = objectMapper.readValue(file, Cell[].class);
					cl = new CellList(cells);
					this.remove(jsp);
					this.add(jsp = new JScrollPane(cl));
	            } catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	            //This is where a real application would open the file.
	            GUI.log.m("Opening: " + file.getName() + ".");
	        } else {
	            GUI.log.m("Open command cancelled by user.");
	        }
	}

	public void update() {
		cl.update();
	}
}
