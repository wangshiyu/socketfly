package wsy.core.timerun;

import java.util.List;

import wsy.serverCore.internalInterface.TimeRun;
import wsy.utils.LogAgent;
/**
 * 按时跑（任务执行器）
 * @author Administrator
 *
 */
public class TimeRunExecute extends Thread {
	 private final static LogAgent log = new LogAgent(TimeRunExecute.class);//log
	 /**
	  * 当前运行的任务
	  */
	 private TimeRun timeRun;
	/**
	 * 休眠时间
	 */
	private long sleepTime;
	/**
	 * sleepTime 休眠时间更具时间情况设定
	 */
	public TimeRunExecute(long sleepTime){
		super();
		this.sleepTime =sleepTime;
		log.info("任务执行器初始化完成，最小休眠时间"+sleepTime+"毫秒！");
	}
	
	 private volatile List<TimeRun> timeRunList = new java.util.LinkedList<TimeRun>();
	 /**
		 * 锁，仅此而已
		 * 
		 * @see #run()
		 * @see #add(Object)
		 */
	 private Object mutex = new Object();
	 public void run(){
		 while(true){
			 if(timeRunList.size()!=0){
				for(TimeRun i:timeRunList){
					i.falg=i.falg+sleepTime;
					timeRun=i;
					if(i.falg>=i.intervalTime){
						i.falg=0;
						try {
							i.executeFunction();
						} catch (Exception e) {
							e.printStackTrace();
						}					
					}
					timeRun=null;
				}			 
				try {
					sleep(sleepTime);//休眠时间
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			 }else{
				 synchronized (mutex) {// 等~直到add方法的通知!
						try {
							mutex.wait();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
			 }
		 }
	 }	
	 
	 public synchronized void add(TimeRun timeRun){
		 this.timeRunList.add(timeRun);
		 synchronized (mutex) {// 每个代理都有一个队列，不需要同步
				mutex.notify();
			}
	 }	 

	public long getSleepTime() {
		return sleepTime;
	}

	public void setSleepTime(long sleepTime) {
		this.sleepTime = sleepTime;
	}

	public TimeRun getTimeRun() {
		return timeRun;
	}
	
}
