package wsy.serverCore.receive;

import java.net.Socket;
import java.util.Date;

import wsy.core.bean.BlackBase;
import wsy.core.bean.ReceiveJMessage;
import wsy.core.redis.JedisService;
import wsy.core.system.SystemConstant;
import wsy.propertiesCofig.PropertiesCofig;
import wsy.serverCore.queue.redisBuffer.DataBase;
import wsy.serverCore.server.SocketChannelAgent;
import wsy.serverCore.server.SocketServerCore;
import wsy.serverCore.system.ServerSystemConstant;
import wsy.serverCore.system.SocketChannelDate;
import wsy.utils.LogAgent;
import wsy.utils.LogOutPut;
/**
 * 消息接收对象
 * @author black
 *
 */
public class Receive extends Thread {
	private final static LogAgent log = new LogAgent(Receive.class);//log   

	/**每个通道公用的对象*/
	private SocketChannelAgent socketChannelAgent;
	private LogOutPut logOutPut =LogOutPut.getInstance();//获得日志存储 
	private boolean close =false;//是否关闭
	private String heartbeatSignal=PropertiesCofig.getHeartbeatSignal();//心跳信号
	
	private PushThread pushThread;
	private volatile DataBase dataBase;
	
	
	/**配置信息*/
	private static String redis_server_key=PropertiesCofig.getSocketServerKey();//redis_server_key
	private static String storage_Mode_Key=PropertiesCofig.getRedisServerManyStorageModeKey();//redisServerManyStorageModeKey
	private static String storage_mode=PropertiesCofig.getRedisServerStorageMode();//redisServerStorageMode
	private static int mode_list_size=PropertiesCofig.getRedisServerManyStorageModeListSize();
	private static boolean data_storage_redis=PropertiesCofig.getServerDataStorageRedis();
	private static boolean redis_is_open=PropertiesCofig.getRedisIsOpen();
	private static boolean push_Thread=PropertiesCofig.getPushThreadIsOpen();
	private static boolean high_Speed_Buffer=PropertiesCofig.getHighSpeedBufferIsOpen();
	private static String speed_Storage_Mode=PropertiesCofig.getHighSpeedBufferStorageMode();
	private static Boolean checkDataIsFullIsOpen =PropertiesCofig.getCheckDataIsFullIsOpen();
	private static String checkDataIsFullKey=PropertiesCofig.getCheckDataIsFullKey();
	public Receive(SocketChannelAgent socketChannelAgent){
		super();
		this.socketChannelAgent=socketChannelAgent;
		/**消息推送线程*/
	   	if(push_Thread&&SystemConstant.Many.equals(speed_Storage_Mode)&&SystemConstant.Many.equals(storage_mode)){
	   		pushThread=new PushThread(this);
	   	}
		/**消息推送线程*/
		/**高速缓存数据存储对象*/
		if(PropertiesCofig.getHighSpeedBufferIsOpen()&&SystemConstant.Many.equals(speed_Storage_Mode)&&SystemConstant.Many.equals(storage_mode)){
			dataBase=new DataBase(PropertiesCofig.getRedisServerManyStorageModeListSize());
		}
		/**高速缓存数据存储对象*/
	}
	
	@SuppressWarnings("finally")
	public void run() {
		while(!close){
			try {

				String receiveText = socketChannelAgent.getIn().readLine();
				if(receiveText==null){//客户端因网络原因端口（不放到黑名单）
					log.info("收到客户端"+socketChannelAgent.getSocket().getRemoteSocketAddress().toString()+"数据为:"+receiveText);
					this.close();//断开客户端
				}else if(heartbeatSignal.equals(receiveText)){
					socketChannelAgent.getLinkBase().setHeartbeatTime(new Date());//更新心跳
				}else{		
					String message;
					if(PropertiesCofig.getCheckBoolean()){
						message= PropertiesCofig.getDataEncryptionDecrypt().dataVerification(receiveText);
					}else{
						message=receiveText;
					}
					if(null!=message){
						/**数据插入redis*/			
						if(high_Speed_Buffer&&data_storage_redis&&redis_is_open){
							/**数据插入redis高速缓存*/
							if(SystemConstant.Many.equals(storage_mode)){
								if(SystemConstant.Alone.equals(speed_Storage_Mode)){
									SocketServerCore.getHighSpeedBuffer().add(message);
								}else if(SystemConstant.Many.equals(speed_Storage_Mode)){
									if(dataBase==null){
										dataBase=new DataBase(mode_list_size);
									}
									int size =dataBase.add(message);
									if(size!=mode_list_size&&push_Thread){
										pushThread.activation();
									}else if(dataBase!=null&&size==mode_list_size){
										SocketServerCore.getHighSpeedBuffer().add(dataBase);
										dataBase=new DataBase(mode_list_size);
									}
								}else{
									throw new RuntimeException("redis内存式高速缓存区存储方式配置出错！");
								}						
							}else if(SystemConstant.Alone.equals(storage_mode)){
								SocketServerCore.getHighSpeedBuffer().add(message);
							}else {
								throw new RuntimeException("redis存储方式配置出错！");
							}
							/**数据插入redis高速缓存*/	
						}else if(data_storage_redis&&redis_is_open){
							/**数据直接插入redis*/	
							redisSave(message);
							/**数据直接插入redis*/
						}
						/**数据插入redis*/
						
						/**数据插入系统队列*/
						if(PropertiesCofig.getReceiveQueueIsOpen()){
							Socket socket =this.socketChannelAgent.getSocket();
							if(null!=this.socketChannelAgent.getCommandExecute()){
								SocketServerCore.getReceiveQueue().add(new ReceiveJMessage(socket,message,this.socketChannelAgent.getCommandExecute()));//添加到队列
							}else{
								SocketServerCore.getReceiveQueue().add(new ReceiveJMessage(socket,message));//添加到队列
							}
						}					
						this.socketChannelAgent.getLinkBase().updateReceive(message.getBytes().length);//更新信息		
						/**数据插入系统队列*/
						
					}else{			
						if(PropertiesCofig.getBlacklistBoolean()){
							BlackBase blackBase=new BlackBase();
							blackBase.setIp(socketChannelAgent.getLinkBase().getIp());
							if(null!=PropertiesCofig.getBlacklistBoolean()){
								PropertiesCofig.getBlackList().saveOrUpdate(blackBase);
							}else{
								throw new RuntimeException("未set一个黑名单服务实例!");
							}							
						}
						try {
							String mes ="收到ip:"+this.socketChannelAgent.getLinkBase().getIp()+"端口:"+this.socketChannelAgent.getLinkBase().getPort()+"异常数据为:"+receiveText;
							logOutPut.outLog(mes);
							log.info(mes);
						} catch (Exception e) {
							log.info("ReceiveQueue独立日志存储出问题");			
						}	
						this.close();			
					}
				}
			} catch (Exception e) {
				String ip=this.socketChannelAgent.getLinkBase().getIp();
				String remoteAddress="/"+ip+":"+this.socketChannelAgent.getLinkBase().getPort();
				try {
					if(null !=this.socketChannelAgent&&null !=this.socketChannelAgent.getMessageQueue()){
						this.socketChannelAgent.getMessageQueue().destroy();//销毁队列线程
					}
					SocketChannelDate.socketChannelMapRemove(remoteAddress);
					this.socketChannelAgent.getSocket().close();
					if(null!=socketChannelAgent && null!=socketChannelAgent.getIn()) socketChannelAgent.getIn().close();
					if(null!=socketChannelAgent && null!=socketChannelAgent.getOut()) socketChannelAgent.getOut().close();
					ServerSystemConstant.socketLikeCountMapUpade(ip, -1);//更改当前ip连接数
					if(socketChannelAgent.getSocket().isClosed()){				
						log.info(remoteAddress+"连接已经关闭！");
					}
				} catch (Exception e2) {
					log.info(remoteAddress+"连接关闭失败！");
				}finally {
					this.close=true;
					this.interrupt();//关闭当前线程
					break;
				}	
			}		
		}	
	}
	
	/**
	 * 数据入redis
	 * @param message
	 */
	private synchronized void redisSave(String message){
			boolean fail =false;							
				if(SystemConstant.Many.equals(storage_mode)){
						String num=JedisService.get(storage_Mode_Key);
						if(num!=null){
							Long lean= JedisService.llen(redis_server_key+num);
							if(lean==null ||lean<mode_list_size){
								fail= JedisService.rpush(redis_server_key+num,message);//添加到socket队列
								if(checkDataIsFullIsOpen){
									JedisService.set(redis_server_key+num,"false");
								}
							}else{
								JedisService.set(storage_Mode_Key,(Long.valueOf(num)+1)+"");
								fail= JedisService.rpush(redis_server_key+(Long.valueOf(num)+1),message);//添加到socket队列
								if(checkDataIsFullIsOpen){
									JedisService.set(checkDataIsFullKey+Long.valueOf(num),"true");
								}
							}				
						}else{
							JedisService.set(storage_Mode_Key,"0");
								fail= JedisService.rpush(redis_server_key+"0",message);//添加到socket队列
								if(checkDataIsFullIsOpen){
									JedisService.set(checkDataIsFullKey+"0","false");
								}
						}
				}else if(SystemConstant.Alone.equals(storage_mode)){
					fail= JedisService.rpush(redis_server_key,message);//添加到socket队列
				}else{
					throw new RuntimeException();
				}						
			if(!fail){
				 log.info("数据插入redis服务器失败！");
			}		
	}
	
	
	/**
	  * 关闭
	  * @param receiveJMessage
	  * @throws Exception
	  */
	 private void close() throws Exception{	 
		String ip=this.socketChannelAgent.getLinkBase().getIp();
		String remoteAddress="/"+ip+":"+this.socketChannelAgent.getLinkBase().getPort();
		if(null !=this.socketChannelAgent&&null !=this.socketChannelAgent.getMessageQueue()){
			this.socketChannelAgent.getMessageQueue().destroy();//清空队列
		}
		if(null!=this.socketChannelAgent && this.socketChannelAgent.getSocket()!=null) this.socketChannelAgent.getSocket().close();
		if(null!=this.socketChannelAgent && null!=this.socketChannelAgent.getIn()) this.socketChannelAgent.getIn().close();
		if(null!=this.socketChannelAgent && null!=this.socketChannelAgent.getOut()) this.socketChannelAgent.getOut().close();
		SocketChannelDate.socketChannelMapRemove(remoteAddress);
		ServerSystemConstant.socketLikeCountMapUpade(ip, -1);//更改当前ip连接数
		if(this.socketChannelAgent.getSocket()!=null && !this.socketChannelAgent.getSocket().isClosed()){
			String message=remoteAddress+"数据校验未成功，连接未断开";
			log.info(message);
			try {
				logOutPut.outLog(message+"\r\n");
			} catch (Exception e) {
				log.info("Receive独立日志存储出问题");
			}
		}else{
			String message=remoteAddress+"数据校验失败，系统主动断开连接成功";
			log.info(message);
			try {
				logOutPut.outLog(message+"\r\n");
			} catch (Exception e) {
				log.info("Receive独立日志存储出问题");
			}
		}
		this.close=true;
		this.interrupt();//关闭当前线程
	 }

	public SocketChannelAgent getSocketChannelAgent() {
		return socketChannelAgent;
	}

	public void setSocketChannelAgent(SocketChannelAgent socketChannelAgent) {
		this.socketChannelAgent = socketChannelAgent;
	}

	public DataBase getDataBase() {
		return dataBase;
	}

	public void setDataBase(DataBase dataBase) {
		this.dataBase = dataBase;
	}

	public PushThread getPushThread() {
		return pushThread;
	}

	public void setPushThread(PushThread pushThread) {
		this.pushThread = pushThread;
	}
}
