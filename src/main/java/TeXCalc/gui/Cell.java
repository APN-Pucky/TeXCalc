package TeXCalc.gui;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.util.HashMap;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JToolBar;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import TeXCalc.config.Config;
import TeXCalc.latex.Auto;
import TeXCalc.latex.Latex;
import TeXCalc.latex.TeXable;
import TeXCalc.latex.command.Image;
import TeXCalc.latex.command.Section;
import TeXCalc.latex.command.SubSection;
import TeXCalc.latex.command.SubSubSection;
import TeXCalc.latex.environment.item.Enumerate;
import TeXCalc.latex.environment.item.Itemize;
import TeXCalc.latex.environment.item.Markdown;
import TeXCalc.latex.wrap.Wrapper;
import TeXCalc.latex.wrap.math.Align;
import TeXCalc.latex.wrap.math.Equation;
import TeXCalc.mathematica.Mathematica;
import TeXCalc.python.Python;
import TeXCalc.util.StringUtils;
import TeXCalc.util.Task;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Cell {

	// public static final String[] envs = { "equation", "latex", "aligned", ""};
	public static HashMap<String, TeXable> hm = new HashMap<String, TeXable>();
	static {
		hm.put("aligned", new Align());
		hm.put("auto", new Auto());
		// hm.put("diffalign",new DiffAlign());
		// hm.put("diffalign2",new DiffAlign2());
		// hm.put("simplediffalign",new SimpleDiffAlign());
		hm.put("equation", new Equation());
		hm.put("latex", new Wrapper());
		hm.put("section", new Section());
		hm.put("subsection", new SubSection());
		hm.put("subsubsection", new SubSubSection());
		hm.put("python", new Python());
		hm.put("image", new Image());
		hm.put("itemize", new Itemize());
		hm.put("enumerate", new Enumerate());
		hm.put("markdown", new Markdown());
		hm.put("mathematica", new Mathematica());
	}
	// public static final String[] begin = { Latex.begin("equation"), "",
	// Latex.begin("equation")+Latex.begin("aligned"), ""};
	// public static final String[] end = { Latex.end("equation"), "",
	// Latex.end("aligned")+Latex.end("equation"), ""};

	protected JTextArea text;
	protected JCheckBox export= GUI.check("export");
	protected JLabel icon = new JLabel();
	//protected Bar bar = null;
	JPanel panel;
	JToolBar bar;
	private Thread cur = null;

	@Getter
	@Setter
	protected String environment = Config.current.getLatex().getEnvironment().getValue();

	
	@Getter
	@Setter
	@JsonIgnore
	protected Latex latex;
	
	
	@Getter
	@Setter
	@JsonIgnore
	protected CellList list;
	
	

	private boolean updating = false, reupdate = false;
	
	public Cell() {
		this("");
	}

	@JsonCreator
	public Cell(@JsonProperty("text") String stext) {
		this(stext, null);
	}

	public Cell(Latex l) {
		this("", l);
	}

	public Cell(@JsonProperty("text") String stext, Latex latex) {
		this.latex = latex;
		// environment = e;
		// icon = new JLabel();
		// text = new JTextArea(stext,5,80);
		text = GUI.areaLatex(stext, Config.current.getGui().getNumberOfLines().getValue(), Config.current.getGui().getWidthOfLines().getValue());
		// axTextArea rs = new RSyntaxTextArea(stext,5,100);
		// rs.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_LATEX);
		// rs.setHighlightCurrentLine(false);
		// rs.setAutoscrolls(true);
		// text = rs;

		text.setLineWrap(true);
		// text.setPreferredSize(new Dimension(150,100));
		// text.setLineWrap(true);
		text.addKeyListener((new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {
				re();
				queueUpdate();
			}

			@Override
			public void keyReleased(KeyEvent e) {
			}

			@Override
			public void keyPressed(KeyEvent e) {

			}
		}));
		queueUpdate();
	}
	public void re() {
		int i = 3;
		for(String s : text.getText().split("\n")) {
			i += s.length() / text.getColumns();
		}
		text.setRows(StringUtils.count(text.getText(), '\n', 0)+i);
		list.re();
	}
	public String getText() {
		return text.getText();
	}

	public void setText(String text) {
		this.text.setText(text);
	}
	public boolean getExport() {
		return export.isSelected();
	}
	public void setExport(boolean exp) {
		export.setSelected(exp);
	}
	
	protected void initBar() {
		bar = new JToolBar(JToolBar.HORIZONTAL);
		bar.setFloatable(false);
		addButtons();
	}
	
	protected void addButtons() {
		Cell c = this;
		JButton button = null;
		JPanel toolBarp = new JPanel();

		
		JComboBox<String> petList = new JComboBox<String>(Cell.hm.keySet().toArray(new String[Cell.hm.keySet().size()]));
		toolBarp.add(petList);
		
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
		//toolBar.add(toolBarp);
		//export ;
		export.addItemListener((e) -> { c.queueUpdate();});
		toolBarp.add(export);
		button = GUI.buttonSync("Append", () -> {list.increase(list.cells.indexOf(c)+1);});
		toolBarp.add(button);	


		button = GUI.buttonSync("Up", () -> list.up(c));
		toolBarp.add(button);
		button = GUI.buttonSync("Down", () -> list.down(c));
		toolBarp.add(button);
		button = GUI.buttonSync("Clean", () -> {if (c.getText().equals("") || GUI.confirm(list.panel,"Clean","Clean") ){c.setText(""); c.queueUpdate();}});
		toolBarp.add(button);

		
		button = GUI.buttonSync("Remove", () -> {if (c.getText().equals("") || GUI.confirm(list.panel,"RM","RM") ){list.remove(c);}});
		toolBarp.add(button);	

		

		bar.add(toolBarp);
		
		JPanel tmpp = new JPanel();
		tmpp.setLayout(new BoxLayout(tmpp, BoxLayout.Y_AXIS));
		tmpp.add(bar);
		
		JScrollPane js = new JScrollPane(text, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

		tmpp.add(js);
		
		panel = tmpp;

		//return new Bar(tmpp,toolBar,export);
	}

	public String toLatex() {
		return toLatex(false, false);
	}

	/*
	 * private int getEnvIndex(String env) { for (int i = 0 ; i < envs.length; ++i)
	 * { if(envs[i].equals(env)) { return i; } } return -1; }
	 */
	public String toLatex(boolean stared, boolean math) {
		if (getText().trim().equals(""))
			return "";
		if (environment.equals("image")) {
			latex.cache(getText().split(" ")[0]);
		}
		latex.checkRequirements(hm.get(environment).getRequirements());
		if (math) {
			return hm.get(environment).toStandalone(getText(),latex);
		} else {
			return hm.get(environment).toDocument(getText(),latex) + "\n";
		}
		/*
		 * String ret = ""; ret += begin[getEnvIndex(environment)] + "\n"; ret
		 * +=getText() ; ret += end[getEnvIndex(environment)] + "\n"; if(math) { ret =
		 * ret.replaceAll(Pattern.quote(Latex.begin("equation")), "\\$"); ret =
		 * ret.replaceAll(Pattern.quote(Latex.end("equation")), "\\$"); } return ret;
		 */
	}
	
	public void queueUpdate() {
		queueUpdate(()->{});
	}

	public void queueUpdate(Runnable callback) {
		final long millis = System.currentTimeMillis();
		if(Config.current.getLatex().getAutoExport().getValue() && list != null)
			list.export();
		Task.stop(cur);
		cur = Task.append(() -> {
			BufferedImage img = null;
			if (latex != null)
				img = latex.snipImage(toLatex(true, true));
			final BufferedImage fimg = img;
			synchronized (this) {
				if (updating) {
					reupdate = true;
				} else {
					reupdate = false;
					updating = true;
					new Thread(() -> update(fimg, millis)).start();
					new Thread(callback).start();
				}
			}
		});
	}

	private long lastmillis = 0;

	public synchronized void update(BufferedImage img, long millis) {
		if (latex != null) {
			// BufferedImage img= latex.snipImage(toLatex(true,true));
			if (img != null && millis > lastmillis) {
				// icon = new JLabel();
				// synchronized(this)
				{
					lastmillis = millis;
				}
				ImageIcon icon2 = new ImageIcon(img);
				icon.setIcon(icon2);
				re();

			}
		}
		// synchronized(this)
		{
			updating = false;
			if (reupdate) {
				queueUpdate();
			}
			else if (Config.current.getMath().getExecuteSubsequent().getValue() && (environment.equals("mathematica") /*|| environment.equals("auto") */)
					 // || Config.current.getPython().getExecuteSubsequent() && environment.equals("python")
					){
				// queue updates on cells after this
				list.updateFrom(this);
			}
		}
	}

}
