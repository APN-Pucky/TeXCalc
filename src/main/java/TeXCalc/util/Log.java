package TeXCalc.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;

import javax.swing.JTabbedPane;
import javax.swing.text.Style;

public class Log
{
	private static final boolean DEBUG_LOGGING =true;
	private static int MAX = 10;
	public static class NoLog extends Log
	{
		@Override
		public void print(String msg, Level l,String[] src) {}
	}
	//private File f = new File("logs/" + new SimpleDateFormat("yyyyMMdd-HH-mm-ss").format(new Date()) +  ".log");
	
	//private BufferedWriter output;
	//private JTabbedPane tabbedPane;
	
	
	
	public Log()
	{
		
		
		/*if(!f.exists()){
			if(f.getParentFile()!= null)f.getParentFile().mkdirs();
			try {
			f.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		try {
			output = new BufferedWriter(new FileWriter(f, true));;
		} catch (IOException e) {
			e.printStackTrace();
		}*/

       
        
	}
	
	public static int getLineNumber() {
	    return Thread.currentThread().getStackTrace()[5].getLineNumber();
	}
	
	public static String getClassName() {
	    return Thread.currentThread().getStackTrace()[5].getClassName();
	}
	
	
	
	public void print(String msg, Level l,String[] src)
	{
		String date = "[" + new SimpleDateFormat("HH:mm:ss").format( new Date() ) + "] " + getClassName() + ":" + getLineNumber() + ": ";
		Style s = null;//hm_style.get(l);
		String src_string = "";
		for(String st : src)src_string += st + ","; 
		
		System.out.println(date + "{" +src_string+"}: "+ msg);
					
	}
	
	public void log(Level l, String msg,String... src)
	{
		print(msg,l,src);
	}
	
	public void e(String msg,String... src)
	{
		log(Level.SEVERE,"(ERROR)"+msg, src);
	}
	
	public void w(String msg,String... src)
	{
		log(Level.WARNING,"(WARNING)"+msg, src);
	}
	
	public void m(String msg,String... src)
	{
		log(Level.INFO,msg, src);
	}
	
	public void d(String msg,String... src)
	{
		if(DEBUG_LOGGING)log(Level.CONFIG,"(DEBUG)"+msg, src);
	}
	
	
}
