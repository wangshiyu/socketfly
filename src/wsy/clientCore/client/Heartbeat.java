package wsy.clientCore.client;

import wsy.propertiesCofig.PropertiesCofig;

/** 
* @author black
* @version 创建时间：2016年9月11日 上午1：23
* 心跳线程
*/
public class Heartbeat extends Thread {
	
	private SocketClientAgent socketClientAgent;
	
	public Heartbeat(SocketClientAgent socketClientAgent){
		this.socketClientAgent=socketClientAgent;
	}
	
	public void run() {
		while (true) {
			try {
				sleep(PropertiesCofig.getHeartbeatTime());
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			try {
				socketClientAgent.getSend().sendOut(PropertiesCofig.getClientHeartbeatSignal());
			} catch (Exception e) {
				e.printStackTrace();
			}			
		}		
	}

}
