package TeXCalc.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import TeXCalc.config.Config;
import TeXCalc.gui.GUI;

public class Task {
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.0'0000Z'");
	private static int running_tasks = 0;
	private static int max_tasks = 4;
	// public static final Object lockTask = new Object();
	public static final Object lockTimer = new Object();
	private static final ArrayList<Timer> runs = new ArrayList<Timer>();

	static {
		start(() -> {
			long old_last_time = System.currentTimeMillis();
			long last_time = System.currentTimeMillis();
			incThreads(-1);// this is not a task
			while (true) {
				try {
					long sleep = 1000 - (System.currentTimeMillis() - last_time);
					if (sleep > 0)
						Thread.sleep(sleep);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				old_last_time = last_time;
				last_time = System.currentTimeMillis();
				synchronized (lockTimer) {
					int i = 0;
					while (i < runs.size()) {
						if (!runs.get(i).run(last_time - old_last_time)) {
							runs.remove(i);
							i--;
						}
						i++;
					}
				}
			}
		});
	}

	public static void stop(Thread t) {
		if (t != null) {
			GUI.log.d("stop " + t.getId(), "Task");
			t.interrupt();
		}
	}

	public static Thread append(Runnable run) {
		return startUntracked(() -> {
			boolean runit = false;
			while (true) {
				synchronized (Task.class) {
					if (getThreads() < Config.current.getLatex().getMaxThreads().getValue() || Config.current.getLatex().getMaxThreads().getValue() <0) {
						incThreads(1);
						runit = true;
					}
				}
				if(runit)
				{
					GUI.log.d("run", "Task");
					run.run();
					GUI.log.d("done", "Task");
					incThreads(-1);
					return;
				}
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					GUI.log.d("interrupted", "Task");
				}

			}
		});

	}

	public static String time() {
		return sdf.format(Calendar.getInstance().getTime());
	}

	public static interface Timer {
		public boolean run(long millis);
	}

	public static Thread startUntracked(Runnable run) {
		Thread r;
		(r = new Thread(run)).start();
		return r;
	}

	public static synchronized Thread start(Runnable run) {
		incThreads(1);
		Thread r;
		(r = new Thread(() -> {
			run.run();
			incThreads(-1);
		})).start();
		return r;
	}

	public static void tick(Timer run) {
		synchronized (lockTimer) {
			runs.add(run);
		}
	}

	public static synchronized void incThreads(int i) {
		running_tasks += i;
		GUI.log.d("Current Threads: " + running_tasks,"Task");
	}

	public static synchronized int getThreads() {
		return running_tasks;
	}

	public static void sleep(long millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static long id() {
		return Thread.currentThread().getId();
	}

	public static void sleepForAll() {
		while (getThreads() > 0)
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
}
