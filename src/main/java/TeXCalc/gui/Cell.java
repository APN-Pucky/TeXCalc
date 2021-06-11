package TeXCalc.gui;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.util.HashMap;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTextArea;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import TeXCalc.config.Config;
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
import lombok.Getter;
import lombok.Setter;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Cell {

	// public static final String[] envs = { "equation", "latex", "aligned", ""};
	public static HashMap<String, TeXable> hm = new HashMap<String, TeXable>();
	static {
		hm.put("aligned", new Align());
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
	protected JLabel icon = new JLabel();
	private Thread cur = null;

	@Getter
	@Setter
	protected String environment = Config.current.getEnvironment();

	@Getter
	@Setter
	@JsonIgnore
	protected Latex latex;

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
		text = GUI.areaLatex(stext, Config.current.getNumberOfLines(), Config.current.getWidthOfLines());
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

	public String getText() {
		return text.getText();
	}

	public void setText(String text) {
		this.text.setText(text);
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
			return hm.get(environment).toDocument(getText(),latex);
		}
		/*
		 * String ret = ""; ret += begin[getEnvIndex(environment)] + "\n"; ret
		 * +=getText() ; ret += end[getEnvIndex(environment)] + "\n"; if(math) { ret =
		 * ret.replaceAll(Pattern.quote(Latex.begin("equation")), "\\$"); ret =
		 * ret.replaceAll(Pattern.quote(Latex.end("equation")), "\\$"); } return ret;
		 */
	}

	public void queueUpdate() {
		final long millis = System.currentTimeMillis();
		if(cur !=null)cur.interrupt();
		(cur = new Thread(() -> {
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
				}
			}
		})).start();
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

			}
		}
		// synchronized(this)
		{
			updating = false;
			if (reupdate)
				queueUpdate();
		}
	}

}
