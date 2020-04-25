package TeXCalc.gui;

import java.awt.Container;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTextArea;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;

import TeXCalc.latex.Latex;

@JsonIgnoreProperties(ignoreUnknown=true)
public class Cell{
	protected JTextArea text;
	protected JLabel icon;

	public Cell() {
		this("");
	}
	@JsonCreator
	public Cell(@JsonProperty("text") String stext) {
		
        icon=new JLabel();
        text = new JTextArea(stext);
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
	
	public String getText() {
		return text.getText();
	}
	
	public void setText(String text) {
		this.text.setText(text);
	}
	
	public String toLatex() {
		if(getText().trim().equals(""))return"";
		String ret = "\\begin{equation}\n" + getText() + "\n\\end{equation}";
		return ret;
	}
	
	public void link(Container m)
	{
		m.add(text);
		m.add(icon);
	}
	
	public void unlink(Container m)
	{
		m.remove(text);
		m.remove(icon);
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
		BufferedImage img= Latex.snipMathImage(text.getText());
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
