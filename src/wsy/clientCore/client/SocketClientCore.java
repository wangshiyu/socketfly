package wsy.clientCore.client;

import org.apache.log4j.Logger;

import wsy.clientCore.queue.receiveQueue.ReceiveQueue;
import wsy.clientCore.system.SocketClientChannelDate;
import wsy.core.realizationInterface.CommandExecute;
import wsy.propertiesCofig.PropertiesCofig;

/** 
* @author black
* @version 创建时间：2016年9月6日 下午5:30:28 
* 客户端核心
*/
public class SocketClientCore {
	private static Logger log=Logger.getLogger(SocketClientCore.class);  
	private static volatile ReceiveQueue receiveQueue;		//数据处理队列
	private volatile static SocketClientCore instance = null;
	private SocketClientAgent socketClientAgent;
	private SocketClientCore() {
		super();
		this.init();
		log.info("SocketServerCore全部加载完成");		
	}
	
	public SocketClientCore(String serverIp,int serverPort){
		super();
		SocketClientAgent socketClientAgent =new SocketClientAgent(serverIp, serverPort);
		socketClientAgent.link();
		String clientPort =socketClientAgent.getClientPort()+"";
		SocketClientChannelDate.socketClientChannelMapPut(clientPort+"", socketClientAgent);//存储到map里面
		if(PropertiesCofig.getReceiveQueueIsOpen()&&null==receiveQueue){
			 receiveQueue=new ReceiveQueue();
		}
		this.socketClientAgent=socketClientAgent;
		log.info("客户端代理初始划完成,端口为：" + clientPort + "!");
	}
	
	public SocketClientCore(String serverIp,int serverPort,CommandExecute commandExecute){
		super();
		SocketClientAgent socketClientAgent =new SocketClientAgent(serverIp, serverPort);
		socketClientAgent.link();
		socketClientAgent.setCommandExecute(commandExecute);
		String clientPort =socketClientAgent.getClientPort()+"";
		SocketClientChannelDate.socketClientChannelMapPut(clientPort+"", socketClientAgent);//存储到map里面
		if(PropertiesCofig.getReceiveQueueIsOpen()&&null==receiveQueue){
			 receiveQueue=new ReceiveQueue();
		}
		this.socketClientAgent=socketClientAgent;
		log.info("客户端代理初始划完成,端口为：" + clientPort + "!");
	}
	
	public SocketClientCore(String serverIp,int serverPort,int agent){
		super();
		SocketClientAgent socketClientAgent =new SocketClientAgent(serverIp, serverPort);
		socketClientAgent.link();
		String clientPort =socketClientAgent.getClientPort()+"";
		SocketClientChannelDate.socketClientChannelMapPut(clientPort+"", socketClientAgent);//存储到map里面
		if(PropertiesCofig.getReceiveQueueIsOpen()&&null==receiveQueue){
			 receiveQueue=new ReceiveQueue();
		}
		this.socketClientAgent=socketClientAgent;
		log.info("客户端代理初始划完成,端口为：" + clientPort + "!");
	}
	
	public SocketClientCore(String serverIp,int serverPort,int agent,CommandExecute commandExecute){
		super();
		SocketClientAgent socketClientAgent =new SocketClientAgent(serverIp, serverPort);
		socketClientAgent.link();
		socketClientAgent.setCommandExecute(commandExecute);
		String clientPort =socketClientAgent.getClientPort()+"";
		SocketClientChannelDate.socketClientChannelMapPut(clientPort+"", socketClientAgent);//存储到map里面
		if(PropertiesCofig.getReceiveQueueIsOpen()&&null==receiveQueue){
			 receiveQueue=new ReceiveQueue();
		}
		this.socketClientAgent=socketClientAgent;
		log.info("客户端代理初始划完成,端口为：" + clientPort + "!");
	}
	
	public SocketClientCore(String serverIp,int serverPort,int agent,String clientIp,int clientPort){
		super();
		SocketClientAgent socketClientAgent=new SocketClientAgent(serverIp, serverPort, clientIp, clientPort);
		socketClientAgent.link();
		SocketClientChannelDate.socketClientChannelMapPut(clientPort+"", socketClientAgent);//存储到map里面
		if(PropertiesCofig.getReceiveQueueIsOpen()&&null==receiveQueue){
			 receiveQueue=new ReceiveQueue();
		}
		this.socketClientAgent=socketClientAgent;
		log.info("客户端代理初始划完成,端口为：" + clientPort + "!");
	}
	
	public SocketClientCore(String serverIp,int serverPort,int agent,String clientIp,int clientPort,CommandExecute commandExecute){
		super();
		SocketClientAgent socketClientAgent=new SocketClientAgent(serverIp, serverPort, clientIp, clientPort);
		socketClientAgent.link();
		socketClientAgent.setCommandExecute(commandExecute);
		SocketClientChannelDate.socketClientChannelMapPut(clientPort+"", socketClientAgent);//存储到map里面
		if(PropertiesCofig.getReceiveQueueIsOpen()&&null==receiveQueue){
			 receiveQueue=new ReceiveQueue();
		}
		this.socketClientAgent=socketClientAgent;
		log.info("客户端代理初始划完成,端口为：" + clientPort + "!");
	}
	
	public static SocketClientCore getInstance(){
		if(null == instance){
			synchronized (SocketClientCore.class) {
				if(null == instance){
					instance = new SocketClientCore();
				}
			}
		}
		return instance;
	}
	
	public static SocketClientCore getInstance(PropertiesCofig propertiesCofin){
		return  getInstance();
	}	
	
	public static ReceiveQueue getReceiveQueue() {
		return receiveQueue;
	}
	
	private void init(){
		if(PropertiesCofig.getReceiveQueueIsOpen()){
			 receiveQueue=new ReceiveQueue();
			 receiveQueue.start();
		}
		
		String[] clientPortS = PropertiesCofig.getClientPortS().split(",");
		/**服务端口启动，生成代理*/
		if(null !=clientPortS){
			for (String portstr : clientPortS) {
				try {
				SocketClientAgent socketClientAgent =new SocketClientAgent(
															PropertiesCofig.getLinkServerIp(),
															Integer.parseInt(PropertiesCofig.getLinkServerPort()),
															PropertiesCofig.getClientIp(),
															Integer.parseInt(portstr)
															); 
				socketClientAgent.link();
				SocketClientChannelDate.socketClientChannelMapPut(portstr, socketClientAgent);//存储到map里面
					log.info("客户端代理初始划完成,端口为：" + portstr + "!");
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		}
		/**服务端口启动，生成代理*/
	}

	public SocketClientAgent getSocketClientAgent() {
		return socketClientAgent;
	}

	public void setSocketClientAgent(SocketClientAgent socketClientAgent) {
		this.socketClientAgent = socketClientAgent;
	}

	public static void setReceiveQueue(ReceiveQueue receiveQueue) {
		SocketClientCore.receiveQueue = receiveQueue;
	}
}
