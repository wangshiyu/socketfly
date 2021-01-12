package wsy.clientCore.receive;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

import org.apache.log4j.Logger;

import wsy.clientCore.client.SocketClientAgent;
import wsy.clientCore.client.SocketClientCore;
import wsy.clientCore.send.Send;
import wsy.core.bean.ReceiveJMessage;
import wsy.core.redis.JedisService;
import wsy.core.system.SystemConstant;
import wsy.propertiesCofig.PropertiesCofig;
/**
 * 消息接收对象
 * @author black
 *
 */
public class Receive extends Thread {
	private static Logger log=Logger.getLogger(Receive.class);  

	/**每个通道公用的对象*/
	private SocketClientAgent socketClientAgent;
	private Socket socket;	
	private BufferedReader in;
	private boolean close =false;//是否关闭
	public Receive(BufferedReader in,Socket socket){
		super();
		this.in=in;
		this.socket=socket;
		log.info("Receive消息接收对列初始化完成！");
	}
	

	public void run() {
		while(true){			
			if(close){
				break;
			}			
			try {
				String receiveText = in.readLine();
					//log.info("收到客户端"+socket.getRemoteSocketAddress().toString()+"数据为:"+receiveText);
					String message= PropertiesCofig.getDataEncryptionDecrypt().dataVerification(receiveText);
					if(null!=message){
						
						/**数据插入redis*/
						if(PropertiesCofig.getClientDataStorageRedis()&&PropertiesCofig.getRedisIsOpen()){
							redisSave(message);
						}
						/**数据插入redis*/
						
						/**数据插入系统队列*/
						if(PropertiesCofig.getReceiveQueueIsOpen()){
							if(null!=this.socketClientAgent.getCommandExecute()){
								SocketClientCore.getReceiveQueue().add(new ReceiveJMessage(socket,message,socketClientAgent.getCommandExecute()));//添加到队列
							}else{
								SocketClientCore.getReceiveQueue().add(new ReceiveJMessage(socket,message));//添加到队列
							}
						}					
						this.socketClientAgent.getLinkBase().updateReceive(message.getBytes().length);//更新信息		
						/**数据插入系统队列*/
					}
			} catch (java.net.SocketException e) {
				if("Connection reset".equals(e.getMessage())){
					log.info("远程计算机断开连接，系统尝试重新连接");
				}
				try {
					if(socket!=null){
						socket.close();
						socket=null;
					}
					link(socketClientAgent.getServerIp(),socketClientAgent.getServerPort(),socketClientAgent.getClientIp(),socketClientAgent.getClientPort());
					this.in = new BufferedReader(new InputStreamReader(this.socket.getInputStream(), "utf-8"));
					PrintWriter out = new PrintWriter(new OutputStreamWriter(this.socket.getOutputStream(), "utf-8"));
					socketClientAgent.setIn(this.in);
					socketClientAgent.setOut(out);
					socketClientAgent.setSocket(this.socket);
					Send send =new Send(out);
					socketClientAgent.setSend(send);
					if(PropertiesCofig.getMessageQueueIsOpen()){
						socketClientAgent.getMessageQueue().setSend(send);
					}
				} catch (IOException e2) {
					e2.printStackTrace();
				}				
				try {
					Thread.sleep(1000l);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
			}catch (IOException e) {
				e.printStackTrace();
			}	
		}	
	}
	/**
	 * 数据入redis
	 * @param message
	 */
	private synchronized void redisSave(String message){
		boolean fail =false;							
		if(SystemConstant.Many.equals(PropertiesCofig.getRedisClientStorageMode())){
			synchronized (this) {//同步锁
				String num=JedisService.get(PropertiesCofig.getRedisClientManyStorageModeKey());
				if(num!=null){
					Long lean= JedisService.llen(PropertiesCofig.getSocketClientKey()+num);
					if(lean==null ||lean<PropertiesCofig.getRedisClientManyStorageModeListSize()){
						fail= JedisService.rpush(PropertiesCofig.getSocketClientKey()+num,message);//添加到socket队列
					}else{
						JedisService.set(PropertiesCofig.getRedisClientManyStorageModeKey(),(Integer.valueOf(num)+1)+"");
						fail= JedisService.rpush(PropertiesCofig.getSocketClientKey()+(Integer.valueOf(num)+1),message);//添加到socket队列
					}				
				}else{
					JedisService.set(PropertiesCofig.getRedisClientManyStorageModeKey(),"0");
						fail= JedisService.rpush(PropertiesCofig.getSocketClientKey()+"0",message);//添加到socket队列
				}		
			}
		}else if(SystemConstant.Alone.equals(PropertiesCofig.getRedisClientStorageMode())){
			fail= JedisService.rpush(PropertiesCofig.getSocketClientKey(),message);//添加到socket队列
		}else{
			throw new RuntimeException();
		}						
	if(!fail){
		 log.info("数据插入redis服务器失败！");
	}	
	}
	
	public void link(String serverIp,int serverPort,String clientIp,int clientPort){
		try {
			Thread.sleep(10l);
		} catch (InterruptedException e) {
		}
		boolean falg=false;
		while (!falg) {
			try {
				log.info("连接中....");		
				InetAddress localAddr=InetAddress.getByName(clientIp);
				this.socket = new Socket(serverIp,serverPort,localAddr,clientPort);
				falg=true;
			}catch(java.net.ConnectException e){
				log.info("远程服务器无反应！连接失败，稍后尝试重新连接!");
			} catch (java.net.BindException e) {
				if("Address already in use: JVM_Bind".equals(e.getMessage())){
					try {
						throw new Exception("本地端口"+clientPort+"被占用！");
					} catch (Exception e1) {
						continue;
					}
				}
				try {
					throw new Exception("无法分配请求的地址,请检测本地ip地址配置!");
				} catch (Exception e1) {
					continue;
				}
			} catch (java.net.SocketException e) {
				try {
					throw new Exception("ip地址配置错误！请检测本地ip地址配置!");
				} catch (Exception e1) {
					continue;
				}
			}catch (IOException e) {
				e.printStackTrace();
				continue;
			}			
			try {
				Thread.sleep(100l);
			} catch (InterruptedException e) {
			}
		}
		log.info("连接成功！远程ip地址:"+serverIp+"端口:"+serverPort);
	}

	public void setSocketClientAgent(SocketClientAgent socketClientAgent) {
		this.socketClientAgent = socketClientAgent;
	}
}
