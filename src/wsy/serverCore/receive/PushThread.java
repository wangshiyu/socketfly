package wsy.serverCore.receive;

import wsy.propertiesCofig.PropertiesCofig;
import wsy.serverCore.server.SocketServerCore;
/**
 * 
 * @author Administrator
 *推送线程
 */
public class PushThread extends Thread {
	private Receive receive; 
	private static long pushThreadTime =PropertiesCofig.getPushThreadTime();
	public PushThread(Receive receive){
		this.receive=receive;
	}

	/**
	 * 锁，仅此而已
	 * 
	 * @see #run()
	 * @see #add(Object)
	 */
	private Object mutex = new Object();
	
	public void run() {
		while(true){
			try {
				sleep(pushThreadTime);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if(receive.getDataBase()!=null&&receive.getDataBase().getSize()!=0){
				SocketServerCore.getHighSpeedBuffer().add(receive.getDataBase());
				receive.setDataBase(null);
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
	
	/**
	 * 线程激活
	 */
	public void activation() {
		synchronized (mutex) {
			mutex.notify();
		}
	}	
}
