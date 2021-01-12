package wsy.clientCore.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Date;

import org.apache.log4j.Logger;

import wsy.clientCore.queue.messageQueue.MessageQueue;
import wsy.clientCore.receive.Receive;
import wsy.clientCore.send.Send;
import wsy.core.bean.LinkBase;
import wsy.core.realizationInterface.CommandExecute;
import wsy.core.system.SystemConstant;
import wsy.propertiesCofig.PropertiesCofig;


/** 
* @author black
* @version 创建时间：2016年9月6日 下午4:58:16 
* 客户端代理
*/
public class SocketClientAgent{
	private static Logger log=Logger.getLogger(SocketClientAgent.class);  
	private volatile Socket socket;
	private volatile BufferedReader in;
	private volatile PrintWriter out;
	private String serverIp;//服务端ip
	private int serverPort;//服务端端口
	private String clientIp;//客户端ip
	private int clientPort;//客户端端口
	private volatile Send send;//发送数据对象
	private volatile Receive receive;//发送数据对象
	private volatile MessageQueue messageQueue;		//消息发送队列 
    private LinkBase linkBase;				//连接信息对象
	private CommandExecute commandExecute;//命令执行器
	private Heartbeat heartbeat;//心跳线程
	public SocketClientAgent(String serverIp,int serverPort){
		super();
		this.serverIp=serverIp;
		this.serverPort=serverPort;
	}
	public SocketClientAgent(String serverIp,int serverPort,String clientIp,int clientPort){
		this(serverIp,serverPort);
		this.clientIp=clientIp;
		this.clientPort=clientPort;
	}
	
	public void link(){
		while (true) {
			long time=0;
			try {
				time=new Date().getTime();
				log.info("连接中....");		
				if(this.clientPort!=0){
					if(SystemConstant.localhost.equals(this.clientIp)||SystemConstant.retainIp.equals(this.clientIp)){
						throw new RuntimeException("请勿使用:"+this.clientIp);
					}
					InetAddress localAddr=InetAddress.getByName(this.clientIp);
					this.socket = new Socket(this.serverIp,this.serverPort,localAddr,this.clientPort);	
				}else{
					this.socket = new Socket(this.serverIp,this.serverPort);	
				}
				log.info("连接耗时"+(new Date().getTime()-time)+"毫秒");
				break;
			}catch(java.net.ConnectException e){
				//e.printStackTrace();
					log.info("远程服务器无反应！连接失败，稍后尝试重新连接!");
			} catch (java.net.BindException e) {
				//e.printStackTrace();
				if("Address already in use: JVM_Bind".equals(e.getMessage())){
					throw new RuntimeException("本地端口"+clientPort+"被占用！");
				}
				throw new RuntimeException("无法分配请求的地址,请检测本地ip地址配置!");	
			} catch (java.net.SocketException e) {
				//e.printStackTrace();
				throw new RuntimeException("ip地址配置错误！请检测本地ip地址配置!");
			}catch (IOException e) {
				e.printStackTrace();
			}		
			try {
				Thread.sleep(100l);
			} catch (InterruptedException e) {
			}
		}
		log.info("连接成功！远程ip地址:"+this.serverIp+"端口:"+this.serverPort);
		/**缓存区*/
		try {
			this.in = new BufferedReader(new InputStreamReader(socket.getInputStream(), "utf-8"));
			this.out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), "utf-8"));
		}catch (IOException e) {
			e.printStackTrace();
		}
		/**缓存区*/
		
		/**消息接收发送*/
	    this.receive=new Receive(in,socket);
	    receive.setSocketClientAgent(this);//receive持有代理的引用
	    this.receive.start();
	    this.send =new Send(out);
	    send.setSocketClientAgent(this);
	    /**消息接收发送*/
	    
        /**消息发送队列*/
        if(PropertiesCofig.getMessageQueueIsOpen()){
        	this.messageQueue =new MessageQueue(this.send);
        	this.messageQueue.start();
		}
        /**消息发送队列*/
        
        /**心跳线程*/
        if(PropertiesCofig.getHeartbeatIsOpen()){
        	heartbeat =new Heartbeat(this);
        	heartbeat.start();
        }
        /**心跳线程*/
        
        this.clientPort=socket.getLocalPort();//获取客户端的端口
        this.clientIp=socket.getLocalAddress().toString().replaceAll("/","");//获取客户端的ip
        this.linkBase =new LinkBase(clientIp,clientPort);
	}
	public Socket getSocket() {
		return socket;
	}
	public BufferedReader getIn() {
		return in;
	}
	public PrintWriter getOut() {
		return out;
	}
	public String getServerIp() {
		return serverIp;
	}
	public int getServerPort() {
		return serverPort;
	}
	public String getClientIp() {
		return clientIp;
	}
	public int getClientPort() {
		return clientPort;
	}
	public Send getSend() {
		return send;
	}
	public Receive getReceive() {
		return receive;
	}
	public void setReceive(Receive receive) {
		this.receive = receive;
	}
	public CommandExecute getCommandExecute() {
		return commandExecute;
	}
	public void setCommandExecute(CommandExecute commandExecute) {
		this.commandExecute = commandExecute;
	}
	public MessageQueue getMessageQueue() {
		return messageQueue;
	}
	public void setMessageQueue(MessageQueue messageQueue) {
		this.messageQueue = messageQueue;
	}
	public LinkBase getLinkBase() {
		return linkBase;
	}
	public void setLinkBase(LinkBase linkBase) {
		this.linkBase = linkBase;
	}
	public void setServerIp(String serverIp) {
		this.serverIp = serverIp;
	}
	public void setServerPort(int serverPort) {
		this.serverPort = serverPort;
	}
	public void setClientIp(String clientIp) {
		this.clientIp = clientIp;
	}
	public void setClientPort(int clientPort) {
		this.clientPort = clientPort;
	}
	public void setIn(BufferedReader in) {
		this.in = in;
	}
	public void setOut(PrintWriter out) {
		this.out = out;
	}
	public void setSocket(Socket socket) {
		this.socket = socket;
	}
	public void setSend(Send send) {
		this.send = send;
	}
}



