package wsy.serverCore.server;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import wsy.core.bean.LinkBase;
import wsy.core.realizationInterface.CommandExecute;
import wsy.core.system.SystemConstant;
import wsy.propertiesCofig.PropertiesCofig;
import wsy.serverCore.queue.messageQueue.MessageQueue;
import wsy.serverCore.receive.Receive;
import wsy.serverCore.send.Send;
import wsy.serverCore.system.ServerSystemConstant;
import wsy.serverCore.system.SocketChannelDate;
import wsy.utils.LogAgent;

   /**
    * socket Channel代理
    * @author black
    *
    */
   public class SocketChannelAgent//这里注意初始化的顺序
   {
	 private final static LogAgent log = new LogAgent(SocketChannelAgent.class);//log
     private Socket socket;					//socket
     private BufferedReader in;				//缓冲区读
     private Receive receive;				//消息接收对象
     private PrintWriter out;				//缓存区写
     private Send send;						//信息发送
     private MessageQueue messageQueue;		//消息发送队列 
     private LinkBase linkBase;				//连接信息对象
     private CommandExecute commandExecute;//命令执行器 
     
     /**配置信息*/
 	private static String storage_mode=PropertiesCofig.getRedisServerStorageMode();//redisServerStorageMode
 	private static boolean push_Thread=PropertiesCofig.getPushThreadIsOpen();
 	private static String speed_Storage_Mode=PropertiesCofig.getHighSpeedBufferStorageMode();

     
     /**
      * 产生代理对象，未设置socket
      */
     public SocketChannelAgent(){
         linkBase=new LinkBase();
         this.receive=new Receive(this);
         if(PropertiesCofig.getSocketSendIsOpen()){
         send =new Send(this);
         }
  	   if(PropertiesCofig.getMessageQueueIsOpen()){
  		   messageQueue =new MessageQueue(this); 
  	   }
     }
     
     public SocketChannelAgent(Socket socket,int port) throws IOException
     {
       this.socket = socket;
       linkBase=new LinkBase(socket.getInetAddress().toString().replaceAll("/",""),socket.getPort());
       in = new BufferedReader(new InputStreamReader(socket.getInputStream(), "utf-8"));
       this.receive=new Receive(this);
       if(PropertiesCofig.getSocketSendIsOpen()){
       out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), "utf-8"));
       send =new Send(this);
       }
	   if(PropertiesCofig.getMessageQueueIsOpen()){
		   messageQueue =new MessageQueue(this); 
	   }
	   init();
     } 
     
     
     public void init() {
    	 /**消息推送线程*/
    	 if(push_Thread&&SystemConstant.Many.equals(speed_Storage_Mode)&&SystemConstant.Many.equals(storage_mode)){
    		 this.receive.getPushThread().start();
    	 }
    	 /**消息推送线程*/
    	 this.receive.start();    	
    	 if(PropertiesCofig.getMessageQueueIsOpen()){
    		 messageQueue.start();
    	 }
    	 
     }
   
     
     /**
	  * 关闭SocketChannelThread代理
	  * @throws Exception
	  */
	 public void close() {	 
		try {
			String address=socket.getRemoteSocketAddress().toString();
			if(null!=this.receive){
				this.receive.interrupt();
			}
			if(null!=this.messageQueue){
				this.messageQueue.destroy();//清空队列
			}
			socket.close();
			if(null!=this.in){
				this.in.close();
			}
			if(null!=this.out){
				this.out.close();
			}		
			ServerSystemConstant.socketLikeCountMapUpade(linkBase.getIp(), -1);
			SocketChannelDate.socketChannelMapRemove(address);
			log.info(address+"关闭连接！");
		} catch (IOException e) {
			e.printStackTrace();
		}
	 }

	public Socket getSocket() {
		return socket;
	}

	public void setSocket(Socket socket) {
		this.socket = socket;
	}

	public BufferedReader getIn() {
		return in;
	}

	public void setIn(BufferedReader in) {
		this.in = in;
	}

	public PrintWriter getOut() {
		return out;
	}

	public void setOut(PrintWriter out) {
		this.out = out;
	}

	public Send getSend() {
		return send;
	}

	public void setSend(Send send) {
		this.send = send;
	}

	public LinkBase getLinkBase() {
		return linkBase;
	}

	public void setLinkBase(LinkBase linkBase) {
		this.linkBase = linkBase;
	}

	public MessageQueue getMessageQueue() {
		return messageQueue;
	}

	public void setMessageQueue(MessageQueue messageQueue) {
		this.messageQueue = messageQueue;
	}

	public CommandExecute getCommandExecute() {
		return commandExecute;
	}

	public void setCommandExecute(CommandExecute commandExecute) {
		this.commandExecute = commandExecute;
	}

	public Receive getReceive() {
		return receive;
	}

	public void setReceive(Receive receive) {
		this.receive = receive;
	}
   }

  