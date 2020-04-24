package TeXCalc.gui;

import java.awt.Component;
import java.awt.Container;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTextArea;

import TeXCalc.latex.Latex;

public class Cell{
	
	protected JTextArea text;
	protected JLabel icon;

	public Cell() {
		this("");
	}
		public Cell(String stext) {
		
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
        update();
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
		BufferedImage img= Latex.toMathImage(text.getText());
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
