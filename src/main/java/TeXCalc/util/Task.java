package TeXCalc.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class Task {
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.0'0000Z'");
	private static int running_tasks = 0;
	//public static final Object lockTask = new Object();
	public static final Object lockTimer = new Object();
	private static final ArrayList<Timer> runs = new ArrayList<Timer>();
	
	static {
		start(() -> {
			long old_last_time = System.currentTimeMillis();
			long last_time = System.currentTimeMillis();
			incThreads(-1);//this is not a task
			while(true)
        	{
        		try {
        			long sleep = 1000 - (System.currentTimeMillis()-last_time);
					if(sleep>0)Thread.sleep(sleep);
				} catch (InterruptedException e) {
					e.printStackTrace();}
        		old_last_time = last_time;
        		last_time = System.currentTimeMillis();
        		synchronized(lockTimer)
        		{
        			int i =0;
        			while(i < runs.size())
        			{
        				if(!runs.get(i).run(last_time - old_last_time))
        				{
        					runs.remove(i);
        					i--;
        				}
        				i++;
        			}
        		}
        	}
		});
	}
	
	
	public static String time()
	{
		return sdf.format(Calendar.getInstance().getTime());
	}
	
	public static interface Timer
	{
		public boolean run(long millis);
	}
	
	public static void startUntracked(Runnable run)
	{
		new Thread(run).start();
	}
	
	public static synchronized void start(Runnable run)
	{
		incThreads(1);
		new Thread(() -> {run.run();incThreads(-1);}).start();
	}
	
	public static void tick(Timer run)
	{
		synchronized(lockTimer)
		{
			runs.add(run);
		}
	}
	
	public static synchronized void incThreads(int i) {
		running_tasks += i;
		//TUM.log.d("Current Threads: " + running_tasks,"Task");
	}

	public static synchronized int getThreads() {
		return running_tasks;
	}
	
	public static void sleep(long millis)
	{
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static long id() {
		return  Thread.currentThread().getId();
	}
	
	
	public static void sleepForAll()
	{
		while(getThreads()>0)
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
}
