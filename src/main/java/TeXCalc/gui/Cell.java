package TeXCalc.gui;

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
	
	JTextArea text;
	JLabel icon;

	public Cell(Main m) {
		
        icon=new JLabel();
        text = new JTextArea();
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
        m.add(text);
        m.add(icon);
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
