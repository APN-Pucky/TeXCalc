package TeXCalc.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.Spring;
import javax.swing.SpringLayout;
import javax.swing.UIManager;
import javax.swing.text.DefaultCaret;
import javax.swing.text.NumberFormatter;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rsyntaxtextarea.Theme;

import TeXCalc.config.Config;
import TeXCalc.util.Log;
import TeXCalc.util.Task;

public class GUI {
	public static Log log = new Log();
	public static Image icon = null;
	/*static {
	try {
	    icon = ImageIO.read(TU.class.getResourceAsStream("/resources/skull.png"));
	} catch (IOException ex) {
	    ex.printStackTrace();
	}
	}*/
		     public static Theme theme;
		     static {
		    	 try {
					theme = Theme.load(GUI.class.getResourceAsStream(
					   "/org/fife/ui/rsyntaxtextarea/themes/" + Config.current.getGui().getTheme() + ".xml"));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		     }

	
	public static void setUIFont (javax.swing.plaf.FontUIResource f){
	    java.util.Enumeration keys = UIManager.getDefaults().keys();
	    while (keys.hasMoreElements()) {
	      Object key = keys.nextElement();
	      Object value = UIManager.get (key);
	      if (value instanceof javax.swing.plaf.FontUIResource)
	        UIManager.put (key, f);
	      }
	    }
	

	public static boolean confirm(Component c ,String msg,String title) {
		int n = JOptionPane.showConfirmDialog(c,
			    msg,
			    title,
			    JOptionPane.YES_NO_OPTION);
		return n ==JOptionPane.YES_OPTION;
	}
	
	public static void info(String title , String msg) {
		JOptionPane pane = new JOptionPane( new JScrollPane(GUI.area(msg)),JOptionPane.INFORMATION_MESSAGE);
		 // Configure via set methods
		 JDialog dialog = pane.createDialog(null, title);
		 // the line below is added to the example from the docs
		 dialog.setModal(false); // this says not to block background components
		 dialog.setResizable(true);
		 dialog.setSize(new Dimension(300,600));
		 dialog.setVisible(true);
	}
	public static JTextArea areaLatex(String s, int w, int h) {
		RSyntaxTextArea rs = new RSyntaxTextArea(s,w,h);
		theme.apply(rs);
		rs.setBackground(Color.decode(Config.current.getGui().getBackgroundColor()));
		rs.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_LATEX);
		rs.setHighlightCurrentLine(false);

		DefaultCaret caret = (DefaultCaret) rs.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		return rs;
	}
	public static JTextArea areaLatex(String s) {
		RSyntaxTextArea rs = new RSyntaxTextArea(s);
		theme.apply(rs);
		rs.setBackground(Color.decode(Config.current.getGui().getBackgroundColor()));
		rs.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_LATEX);
		rs.setHighlightCurrentLine(false);

		DefaultCaret caret = (DefaultCaret) rs.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		return rs;
	}
	
	public static JTextArea area()
	{
		return area("");
	}
	
	public static JTextArea area(String msg)
	{
		JTextArea r = new JTextArea(msg);
		r.setLineWrap(true);
		r.setRows(2);
		return r;
	}
	
	public static JTextField text()
	{
		return text("");
	}
	
	public static JTextField text(String msg)
	{
		return text(msg,250);
	}
	
	public static JTextField text(String msg,int width)
	{
		return text(msg,false,width);
	}
	
	public static JTextField textEdit()
	{
		return text("", true,250);
	}
	
	public static JTextField text(String msg,boolean edit,int width)
	{
		JTextField tf = new JTextField(msg);
		tf.setEditable(edit);
		Dimension d = tf.getPreferredSize();
		d.width = width;
		tf.setPreferredSize(d);
		return tf;
	}
	

	public static JLabel textSmall(String msg) {
		return label(msg,150);
	}
	public static JLabel label(String msg,int width) {
		JLabel tf = new JLabel(msg);
		Dimension d = tf.getPreferredSize();
		if(width>0) d.width = 150;
		tf.setPreferredSize(d);
		return tf;
	}

	public static JTextField textEdit(String msg) {
		JTextField tf = new JTextField(msg);
		tf.setEditable(true);
		Dimension d = tf.getPreferredSize();
		d.width = 250;
		tf.setPreferredSize(d);
		return tf;
	}
	
	
	
	
	
	public static GUI.DoubleField doubleEdit(double value) {
		GUI.DoubleField tf = new GUI.DoubleField(value);
		tf.setEditable(true);
		Dimension d = tf.getPreferredSize();
		d.width = 250;
		tf.setPreferredSize(d);
		return tf;
	}
	
	public static GUI.IntegerField numericEdit(int value) {
		GUI.IntegerField tf = new GUI.IntegerField(value);
		tf.setEditable(true);
		Dimension d = tf.getPreferredSize();
		d.width = 250;
		tf.setPreferredSize(d);
		return tf;
	}

	
	
	public static JLabel label(String msg)
	{
		JLabel tf = new JLabel(msg);
		return tf;
	}
	
	public static JCheckBox check(String msg)
	{
		return GUI.check(msg,true);
	}
	
	public static JCheckBox check(String msg, boolean selected)
	{
		JCheckBox cb  = new JCheckBox(msg);
		cb.setSelected(selected);
		return cb;
	}
	
	public static JButton button(String msg)
	{
		JButton cb  = new JButton(msg);
		return cb;
	}
	
	public static JButton buttonAsync(String msg, Runnable run)
	{
		JButton cb  = new JButton(msg);
		
		cb.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				Task.start(run);
			}

		});
		
		return cb;
	}
	
	public static JButton buttonSync(String msg, Runnable run)
	{
		JButton cb  = new JButton(msg);
		
		cb.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				log.d("Pressed '" + msg + "'","GUI","Button");
				run.run();
			}

		});
		
		return cb;
	}
	
	public static Integer[][] toIntegerArray(int[][] arr)
	{
		if(arr.length==0)return new Integer[0][0];
		Integer[][] ret = new Integer[arr.length][arr[0].length];
		for(int i = 0; i < arr.length;i++)
		{
			for(int j = 0; j < arr[0].length;j++)
			{
				ret[i][j] = arr[i][j];
			}
		}
		return ret;
	}
	
	public static void createTextWindow(String title, String msg) {
		JFrame f = new JFrame(title);
		JTextArea jt = new JTextArea(msg,5,50);
		//jt.setLineWrap(true);
		JScrollPane sp = new JScrollPane(jt);
		sp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		sp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		f.add(sp);
		f.pack();
		f.setVisible(true);
	}
	
	public static class IntegerField extends JFormattedTextField
	{
		public IntegerField(int value)
		{
			NumberFormat format = NumberFormat.getNumberInstance();
				
			NumberFormatter formatter = new NumberFormatter(format);
			formatter.setValueClass(Integer.class);
			formatter.setMinimum(0);
			formatter.setMaximum(Integer.MAX_VALUE);
			formatter.setAllowsInvalid(false);
			// If you want the value to be committed on each keystroke instead of focus lost
			formatter.setCommitsOnValidEdit(true);
			
			setFormatter(formatter);
			setValue(new Integer(value));
			setColumns(5);
			addPropertyChangeListener("value",null);
		}
		
		public void setNumber(int i)
		{
			setValue(i);
		}
		
		public int getNumber()
		{
			return (int)getValue();
		}
	}
	
	public static class DoubleField extends JFormattedTextField
	{
		public DoubleField(double value)
		{
			NumberFormat format = DecimalFormat.getInstance();
				
			NumberFormatter formatter = new NumberFormatter(format);
			formatter.setValueClass(Double.class);
			format.setMinimumFractionDigits(2);
			format.setMaximumFractionDigits(10);
			format.setRoundingMode(RoundingMode.HALF_UP);
			//formatter.setMinimum(0);
			//formatter.setMaximum(Integer.MAX_VALUE);
			formatter.setAllowsInvalid(false);
			// If you want the value to be committed on each keystroke instead of focus lost
			//formatter.setCommitsOnValidEdit(true);
			
			setFormatter(formatter);
			setValue(new Double(value));
			setColumns(10);
			addPropertyChangeListener("value",null);
		}
		
		public void setNumber(double i)
		{
			setValue(i);
		}
		
		public double getNumber()
		{
			return (double)getValue();
		}
	}
	

	
	
	public static class LoadBar
	{
		//ProgressMonitor progressMonitor;
		String task;
		String sub_task;
		long startPauseTime = 0;
		long pauseTime = 0;
		long startTime;
		long remainingTime = 0;
		int maximum;
		
		boolean canceled = false;
		boolean paused = false;
		
		JFrame f;
		JPanel main_panel;
		JPanel inline_panel;
		JProgressBar progressBar,il_progressBar;
		JButton cancel,pause,il_cancel,il_pause, il_max, il_min;
		JTextArea taskOutput;
		JTextField eta,il_eta;
		JTextField header, il_header, il_status;
		Timer timer;
		public LoadBar (String msg, int max) {this(msg,max,true); }
		public LoadBar(String msg, int max, boolean vis)
		{
			log.d("Create LoadBar '" + msg + "'","GUI","LoadBar");
			startTime = System.currentTimeMillis();
			task = msg;
			maximum = max;
			
			f = new JFrame();
			
			f.setIconImage(icon);
			f.setTitle(msg);
			main_panel = new JPanel();
			main_panel.setLayout(new BorderLayout());
			
			JPanel tmp_panel = new JPanel();
			tmp_panel.setLayout(new BoxLayout(tmp_panel,BoxLayout.Y_AXIS));
			
			progressBar = new JProgressBar(0, getMaximum());
			progressBar.setValue(0);
	        progressBar.setStringPainted(true);
	        il_progressBar = new JProgressBar(0, getMaximum());
	        il_progressBar.setValue(0);
	        il_progressBar.setStringPainted(true);
	        Dimension d = il_progressBar.getPreferredSize();
	        d.width = 300;
	        il_progressBar.setPreferredSize(d);

	        eta = GUI.text("ETA: ",120);
	        il_eta = GUI.text("ETA: ",120);

	        
	        header  = GUI.text(msg,170);
	        il_header  = GUI.text(msg,250);
	        il_status  = GUI.text("",250);
	        cancel = GUI.buttonSync("cancel", () -> cancel());
	        pause = GUI.buttonSync("pause", () -> pause());
	        il_cancel = GUI.buttonSync("cancel", () -> cancel());
	        il_pause = GUI.buttonSync("pause", () -> pause());
	        il_max = GUI.buttonSync("maximize", () -> maximize());
	        il_min = GUI.buttonSync("minimize", () -> minimize());
	        
	        
	        
	        taskOutput = new JTextArea(5, 20);
	        taskOutput.setMargin(new Insets(5,5,5,5));
	        taskOutput.setEditable(false);
	        DefaultCaret caret = (DefaultCaret)taskOutput.getCaret();
	        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
	        

	        JPanel panel = new JPanel();
	        panel.add(header);
	        panel.add(pause);
	        panel.add(cancel);
	        tmp_panel.add(panel);
	        
	        panel = new JPanel();
	        panel.add(eta);
	        panel.add(progressBar);
	        tmp_panel.add(panel);
	        
	        inline_panel = new JPanel();
	        inline_panel.add(il_header);
	        inline_panel.add(il_max);
	        inline_panel.add(il_min);
	        inline_panel.add(il_pause);
	        inline_panel.add(il_cancel);
	        inline_panel.add(il_eta);
	        inline_panel.add(il_progressBar);
	        inline_panel.add(il_status);
	        //inline_panel.add(new JScrollPane(il_taskOutput));
	        
	        
	        
	        

	        main_panel.add(tmp_panel, BorderLayout.PAGE_START);
	        main_panel.add(new JScrollPane(taskOutput), BorderLayout.CENTER);
	        main_panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
	        
	        f.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

	        Task.tick(timer = new GUI.LoadBar.Timer(this));
	        
	        f.add(main_panel);
	        f.pack();
	        //f.setSize(new Dimension(350,200));
	        f.setVisible(vis);
	        
		}
		
		private static class Timer implements Task.Timer
		{
			public boolean kill = false;
			LoadBar bar;
			public Timer(LoadBar b){bar = b;}
			public void kill(){kill = true;};
			public boolean run(long m){synchronized(bar){if(!bar.isCanceled() && !bar.isPaused())bar.decRemainingTime(m);return !bar.isCanceled() && !kill;}};
		}
		
		public JPanel getInlinePanel()
		{
			return inline_panel;
		}
		
		public void setHeader(String msg)
		{
			header.setText(msg);
			il_header.setText(msg);
			f.setTitle(msg);
		}
		
		public void setProgress(int progress)
		{
			setProgress(progress,"");
		}
		
		public void setProgress(int progress,String msg)
		{
			progress++;
			if(msg.equals("")) msg = String.format( "Completed %d%%.", (progress)*100/getMaximum());
			long passedTime = System.currentTimeMillis()-startTime;
			setRemainingTime((passedTime-pauseTime)/(progress)* (getMaximum()-(progress)));
			
			
			taskOutput.append(String.format("[%d%%]: %s\n", (progress)*100/getMaximum(),msg));
			il_status.setText(msg);
			progressBar.setValue(progress);
			il_progressBar.setValue(progress);
			while(paused){Task.sleep(5000);};
		}
		
		public synchronized void setRemainingTime(long time)
		{
			remainingTime = time;
			eta.setText(String.format("ETA: %s", millisToTime(remainingTime)));
			il_eta.setText(String.format("ETA: %s", millisToTime(remainingTime)));
		}
		
		public synchronized void decRemainingTime(long millis)
		{
			setRemainingTime(remainingTime - millis);
		}
		
		private String millisToTime(long timeInMilliSeconds)
		{
			long seconds = timeInMilliSeconds / 1000;
			long minutes = seconds / 60;
			long hours = minutes / 60;
			long days = hours / 24;
			return days + ":" + hours % 24 + ":" + minutes % 60 + ":" + seconds % 60; 
		}
		
		public void reset()
		{
			synchronized(Task.lockTimer)
			{
				synchronized(this)
				{
					pauseTime = 0;
					startTime = System.currentTimeMillis();
					timer.kill();
					taskOutput.append(String.format("[%d%%]: %s\n", 0,"--Repeat--"));
					il_status.setText("--Repeat--");
					
					setRemainingTime(0);
					//canceled = false;
					progressBar.setValue(0);
					il_progressBar.setValue(0);
					Task.tick(timer = new GUI.LoadBar.Timer(this));
				}
			}
		}
		
		public void close()
		{
			f.dispose();
			timer.kill();
			cancel();
		}
		
		public void cancel()
		{
			eta.setText("canceled");
			il_eta.setText("canceled");
			canceled = true;
			if(paused)pause();
		}
		
		public void pause()
		{
			//eta.setText("canceled");
			synchronized(pause)
			{
				if(paused)
				{
					pauseTime += System.currentTimeMillis() - startPauseTime;
					pause.setText("pause");
					paused = false;
				}
				else
				{
					startPauseTime = System.currentTimeMillis();
					pause.setText("return");
					paused = true;
				}
			}
		}
		
		public void maximize()
		{
			f.setVisible(true);
			f.toFront();
		}
		
		public void minimize()
		{
			f.setVisible(false);
		}
		
		public boolean isCanceled()
		{
			return canceled;
		}
		
		public boolean isPaused()
		{
			return paused;
		}
		
		public int getMaximum()
		{
			return maximum;
		}
		
		public void dispose()
		{
			f.dispose();
		}
		
		/*public static <T> boolean forEach(String title, T[] arr, Function<T,String> action)
		{
			return LoadBar.forEach(title, arr,(t,j) -> {return action.apply(t);});
		}
		
		public static <T> boolean forEach(String title, T[] arr, BiFunction<T,Integer,String> action)
		{
			GUI.LoadBar bar = new LoadBar(title, arr.length);
			int j =0;
			for(int i = 0; i < arr.length;i++)
			{
				String msg ="";
				if(arr[i] !=null)
				{
					msg = action.apply(arr[i],j);
					j++;
				}
				bar.setProgress(i,msg);
				if(bar.isCanceled())break;
			}
			boolean ret = bar.isCanceled();
			bar.close();
			return ret;
		}
		
		public static <T> boolean forEachDouble(String l_title, T[] arr, BiFunction<T,Integer,String> l_action, 
				String h_title, int times,IntFunction<String> h_update)
		{
			GUI.LoadBar l_bar = new LoadBar(l_title, arr.length);
			GUI.LoadBar h_bar = new LoadBar(h_title, times);
			
			for(int k = 0; k < times;k++)
			{	
				int j =0;
				for(int i = 0; i < arr.length;i++)
				{
					String msg ="";
					if(arr[i] !=null)
					{
						msg = l_action.apply(arr[i],j);
						j++;
					}
					l_bar.setProgress(i, msg);
					if(l_bar.isCanceled())break;
				}
				l_bar.reset();
				h_bar.setProgress(k, h_update.apply(k));
				if(l_bar.isCanceled() || h_bar.isCanceled())break;
			}
			boolean ret = l_bar.isCanceled() || h_bar.isCanceled();
			l_bar.close();
			h_bar.close();
			return ret; //true == cancled, else fine
			
		}*/
	}
	
	/**
     * Aligns the first <code>rows</code> * <code>cols</code>
     * components of <code>parent</code> in
     * a grid. Each component is as big as the maximum
     * preferred width and height of the components.
     * The parent is made just big enough to fit them all.
     *
     * @param rows number of rows
     * @param cols number of columns
     * @param initialX x location to start the grid at
     * @param initialY y location to start the grid at
     * @param xPad x padding between cells
     * @param yPad y padding between cells
     */
    public static void makeGrid(Container parent,
                                int rows, int cols,
                                int initialX, int initialY,
                                int xPad, int yPad) {
        SpringLayout layout;
        try {
            layout = (SpringLayout)parent.getLayout();
        } catch (ClassCastException exc) {
            System.err.println("The first argument to makeGrid must use SpringLayout.");
            return;
        }

        Spring xPadSpring = Spring.constant(xPad);
        Spring yPadSpring = Spring.constant(yPad);
        Spring initialXSpring = Spring.constant(initialX);
        Spring initialYSpring = Spring.constant(initialY);
        int max = rows * cols;

        //Calculate Springs that are the max of the width/height so that all
        //cells have the same size.
        Spring maxWidthSpring = layout.getConstraints(parent.getComponent(0)).
                                    getWidth();
        Spring maxHeightSpring = layout.getConstraints(parent.getComponent(0)).
                                    getHeight();
        for (int i = 1; i < max; i++) {
            SpringLayout.Constraints cons = layout.getConstraints(
                                            parent.getComponent(i));

            maxWidthSpring = Spring.max(maxWidthSpring, cons.getWidth());
            maxHeightSpring = Spring.max(maxHeightSpring, cons.getHeight());
        }

        //Apply the new width/height Spring. This forces all the
        //components to have the same size.
        for (int i = 0; i < max; i++) {
            SpringLayout.Constraints cons = layout.getConstraints(
                                            parent.getComponent(i));

            cons.setWidth(maxWidthSpring);
            cons.setHeight(maxHeightSpring);
        }

        //Then adjust the x/y constraints of all the cells so that they
        //are aligned in a grid.
        SpringLayout.Constraints lastCons = null;
        SpringLayout.Constraints lastRowCons = null;
        for (int i = 0; i < max; i++) {
            SpringLayout.Constraints cons = layout.getConstraints(
                                                 parent.getComponent(i));
            if (i % cols == 0) { //start of new row
                lastRowCons = lastCons;
                cons.setX(initialXSpring);
            } else { //x position depends on previous component
                cons.setX(Spring.sum(lastCons.getConstraint(SpringLayout.EAST),
                                     xPadSpring));
            }

            if (i / cols == 0) { //first row
                cons.setY(initialYSpring);
            } else { //y position depends on previous row
                cons.setY(Spring.sum(lastRowCons.getConstraint(SpringLayout.SOUTH),
                                     yPadSpring));
            }
            lastCons = cons;
        }

        //Set the parent's size.
        SpringLayout.Constraints pCons = layout.getConstraints(parent);
        pCons.setConstraint(SpringLayout.SOUTH,
                            Spring.sum(
                                Spring.constant(yPad),
                                lastCons.getConstraint(SpringLayout.SOUTH)));
        pCons.setConstraint(SpringLayout.EAST,
                            Spring.sum(
                                Spring.constant(xPad),
                                lastCons.getConstraint(SpringLayout.EAST)));
    }

    /* Used by makeCompactGrid. */
    private static SpringLayout.Constraints getConstraintsForCell(
                                                int row, int col,
                                                Container parent,
                                                int cols) {
        SpringLayout layout = (SpringLayout) parent.getLayout();
        Component c = parent.getComponent(row * cols + col);
        return layout.getConstraints(c);
    }

    /**
     * Aligns the first <code>rows</code> * <code>cols</code>
     * components of <code>parent</code> in
     * a grid. Each component in a column is as wide as the maximum
     * preferred width of the components in that column;
     * height is similarly determined for each row.
     * The parent is made just big enough to fit them all.
     *
     * @param rows number of rows
     * @param cols number of columns
     * @param initialX x location to start the grid at
     * @param initialY y location to start the grid at
     * @param xPad x padding between cells
     * @param yPad y padding between cells
     */
    public static void makeCompactGrid(Container parent,
                                       int rows, int cols,
                                       int initialX, int initialY,
                                       int xPad, int yPad) {
        SpringLayout layout;
        try {
            layout = (SpringLayout)parent.getLayout();
        } catch (ClassCastException exc) {
            System.err.println("The first argument to makeCompactGrid must use SpringLayout.");
            return;
        }

        //Align all cells in each column and make them the same width.
        Spring x = Spring.constant(initialX);
        for (int c = 0; c < cols; c++) {
            Spring width = Spring.constant(0);
            for (int r = 0; r < rows; r++) {
                width = Spring.max(width,
                                   getConstraintsForCell(r, c, parent, cols).
                                       getWidth());
            }
            for (int r = 0; r < rows; r++) {
                SpringLayout.Constraints constraints =
                        getConstraintsForCell(r, c, parent, cols);
                constraints.setX(x);
                constraints.setWidth(width);
            }
            x = Spring.sum(x, Spring.sum(width, Spring.constant(xPad)));
        }

        //Align all cells in each row and make them the same height.
        Spring y = Spring.constant(initialY);
        for (int r = 0; r < rows; r++) {
            Spring height = Spring.constant(0);
            for (int c = 0; c < cols; c++) {
                height = Spring.max(height,
                                    getConstraintsForCell(r, c, parent, cols).
                                        getHeight());
            }
            for (int c = 0; c < cols; c++) {
                SpringLayout.Constraints constraints =
                        getConstraintsForCell(r, c, parent, cols);
                constraints.setY(y);
                constraints.setHeight(height);
            }
            y = Spring.sum(y, Spring.sum(height, Spring.constant(yPad)));
        }

        //Set the parent's size.
        SpringLayout.Constraints pCons = layout.getConstraints(parent);
        pCons.setConstraint(SpringLayout.SOUTH, y);
        pCons.setConstraint(SpringLayout.EAST, x);
    }
}
