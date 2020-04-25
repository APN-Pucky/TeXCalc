package TeXCalc.gui;

import java.awt.Container;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JToolBar;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;

import TeXCalc.latex.Latex;

@JsonIgnoreProperties(ignoreUnknown=true)
public class Cell{
	public static final String[] envs = { "equation", "latex", "align", ""};
	
	protected JTextArea text;
	protected JLabel icon;
	protected String env = "equation";

	public Cell() {
		this("");
	}
	@JsonCreator
	public Cell(@JsonProperty("text") String stext) {
        icon=new JLabel();
        text = new JTextArea(stext,5,50);
        text.addKeyListener((new KeyListener() {

            @Override
            public void keyTyped(KeyEvent e) {
            	queueUpdate();
            }

            @Override
            public void keyReleased(KeyEvent e) {}

            @Override
            public void keyPressed(KeyEvent e) {
                
            }
        }));
        queueUpdate();
	}
	
	public String getEnv() {
		return env;
	}
	
	public void setEnv(String set) {
		this.env = set;
	}
	
	public String getText() {
		return text.getText();
	}
	
	public void setText(String text) {
		this.text.setText(text);
	}

	public String toLatex() {
		return toLatex(false,false);
	}
	public String toLatex(boolean stared, boolean math) {
		if(getText().trim().equals(""))return"";
		
		String ret = "";
		if(!env.equals("latex")) {
		if(math) ret += "$";
		else ret +="\\begin{"+env + (stared?"*":"") +"}\n" ;}
		ret +=getText() ;
		if(!env.equals("latex")) {
		if(math) ret += "$";
		else ret +="\n\\end{"+env+(stared?"*":"") +"}";}
		return ret;
	}
	
	private boolean updating=false,reupdate = false;
	
	public void queueUpdate()
	{
		synchronized(this) {
			if(updating) {
				reupdate = true;
			}
			else {
				reupdate = false;
				updating = true;
				new Thread(() -> update()).start();
			}
		}
	}
	
	public void update() {
		BufferedImage img= Latex.snipImage(toLatex(true,true));
		if ( img != null) {
        ImageIcon icon2=new ImageIcon(img);
        icon.setIcon(icon2);
		}
        synchronized(this) {
        	updating = false;
        	if(reupdate)queueUpdate();
        }
	}
	
	
}
