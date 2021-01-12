package wsy.clientCore.send;

import java.io.PrintWriter;

import org.apache.log4j.Logger;

import wsy.clientCore.client.SocketClientAgent;

/**
 * 发送消息
 * @author black
 *	
 */
public class Send {
	private static Logger log=Logger.getLogger(Send.class);  
	private PrintWriter printWriter;
	private SocketClientAgent socketClientAgent;
	public Send(PrintWriter printWriter){
		super();
		this.printWriter=printWriter;
		log.info("Send消息发送对象初始化完成！");
	}
	
	/**
	 * 发送信息，使用同步
	 * @param message
	 * @return
	 * @throws Exception
	 */
	public synchronized boolean sendOut(String message) throws Exception{	     
       try {
    	   printWriter.print(message+"\r\n");
    	   printWriter.flush();  
		} catch (Exception e) {	
			e.printStackTrace();
		}
       return true;
	}

	public SocketClientAgent getSocketClientAgent() {
		return socketClientAgent;
	}

	public void setSocketClientAgent(SocketClientAgent socketClientAgent) {
		this.socketClientAgent = socketClientAgent;
	}
	
}
