package wsy.serverCore.server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import wsy.core.bean.DelayConnection;
import wsy.core.realizationInterface.CommandExecute;
import wsy.core.timerun.TimeRunExecute;
import wsy.propertiesCofig.PropertiesCofig;
import wsy.serverCore.objectPool.ObjectPool;
import wsy.serverCore.queue.receiveQueue.ReceiveQueue;
import wsy.serverCore.queue.redisBuffer.HighSpeedBuffer;
import wsy.serverCore.queue.sysMessageQueue.SYSMesageQueue;
import wsy.serverCore.system.ServerSystemConstant;
import wsy.serverCore.system.SocketLikeCountData;
import wsy.serverCore.timeRunTask.AgentObjectPoolTask;
import wsy.serverCore.timeRunTask.HeartbeatTestingTask;
import wsy.serverCore.timeRunTask.SocketCloseTask;
import wsy.serverCore.timeRunTask.SocketLikeCountTask;
import wsy.utils.LogAgent;

/**
 * socket服务核心
 * 2016/08/27
 * @author black
 */
public class SocketServerCore {
	private final static LogAgent log = new LogAgent(SocketServerCore.class);// log
	private static volatile List<SocketServerThread> SocketServerList=new ArrayList<SocketServerThread>();//所有服务端
	private static volatile ReceiveQueue receiveQueue;		//数据处理队列
	private static volatile HighSpeedBuffer highSpeedBuffer;
	private volatile static SocketServerCore instance = null;
	private static int port = 8888;// 默认端口
	private static volatile SYSMesageQueue SYSMesageQueue;
	private static volatile TimeRunExecute timeRunExecute;
	private static volatile ObjectPool agentObjectPool;
	private static DelayConnection[] delayConnectionS=getDelayConnection();//延时连接数组
	private SocketServerCore() {
		super();
		this.core();
		this.init();
		log.info("SocketServerCore全部加载完成");		
	}
	
	public SocketServerCore(int port) throws IOException {
		super();
		this.core();
		SocketServerThread	serverThread = new SocketServerThread(port);
		serverThread.start();
		log.info("初始化Socket服务完成,服务器端口为：" + port + "!");
		SocketServerList.add(serverThread);
		log.info("SocketServerCore全部加载完成");		
	}
	
	public SocketServerCore(int port,int node) throws IOException {
		super();
		this.core();
		SocketServerThread	serverThread = new SocketServerThread(port,node);
		serverThread.start();
		log.info("初始化Socket服务完成,服务器端口为：" + port + "!");
		SocketServerList.add(serverThread);
		log.info("SocketServerCore全部加载完成");		
	}
	
	public SocketServerCore(int port,int node,CommandExecute commandExecute) throws IOException {
		super();
		this.core();
		SocketServerThread	serverThread = new SocketServerThread(port,node);
		serverThread.setCommandExecute(commandExecute);
		serverThread.start();
		log.info("初始化Socket服务完成,服务器端口为：" + port + "!");
		SocketServerList.add(serverThread);
		log.info("SocketServerCore全部加载完成");		
	}
	
	public static SocketServerCore getInstance(){
		if(null == instance){
			synchronized (SocketServerCore.class) {
				if(null == instance){
					instance = new SocketServerCore();
				}
			}
		}
		return instance;
	}
	
	public static SocketServerCore getInstance(PropertiesCofig propertiesCofin){
		return  getInstance();
	}

	private void core(){
		SocketLikeCountData.socketLikeCountMapPut(ServerSystemConstant.currentNumberConnections, 0);//设置当前连接为0	
		if (PropertiesCofig.getSYSMesageQueueIsOpen()) {// 系统消息队列
			SYSMesageQueue = new SYSMesageQueue();
		}
		
		if(PropertiesCofig.getAgentObjectPoolIsOpen()){
			SocketServerCore.agentObjectPool=new ObjectPool();
			Object[] objs=new Object[PropertiesCofig.getAgentObjectPoolSize()];
			for(int i=0;i<PropertiesCofig.getAgentObjectPoolSize();i++){
				objs[i]=new SocketChannelAgent();
			}
			SocketServerCore.agentObjectPool.add(objs);
		}
		
		if(PropertiesCofig.getTimeRunExecuteIsOpen()){
			if(PropertiesCofig.getTimeRunExecuteSleepTime()<0){
					throw new RuntimeException("任务执行器最小休眠时间应该大于0秒");		
			}else{
				timeRunExecute = new TimeRunExecute(PropertiesCofig.getTimeRunExecuteSleepTime());			
				timeRunExecute.add(new SocketCloseTask(PropertiesCofig.getSocketCloseTaskTime()));
				timeRunExecute.add(new SocketLikeCountTask(PropertiesCofig.getSocketLikeCountTaskTime()));		
				if(PropertiesCofig.getAgentObjectPoolIsOpen()){//定时更新代理对象池
					timeRunExecute.add(new AgentObjectPoolTask(PropertiesCofig.getAgentObjectPoolTaskTime()));
				}
				if(PropertiesCofig.getServerHeartbeatTestingIsOpen()){//心跳检测
					timeRunExecute.add(new HeartbeatTestingTask(PropertiesCofig.getHeartbeatTestingTaskTime()));
				}				
				timeRunExecute.start();
			}	
		}
		/**消息存储队列*/
		if(PropertiesCofig.getReceiveQueueIsOpen()&&null==receiveQueue){
			receiveQueue=new ReceiveQueue();
			receiveQueue.start();
		}
		/**消息存储队列*/
		/**redis内存高速缓存区*/
		if(PropertiesCofig.getHighSpeedBufferIsOpen()&&null==highSpeedBuffer){
			highSpeedBuffer=new HighSpeedBuffer();
			highSpeedBuffer.start();
		}
		/**redis内存高速缓存区*/
	}
	
	private void init(){
		String[] servicePortS = PropertiesCofig.getServicePortS().split(",");
	//	Integer masterNodePort=Integer.parseInt(PropertiesCofig.getMasterNodePort());
		/**服务端口启动，生成代理*/
		if(null !=servicePortS){
			for (String portstr : servicePortS) {
				try {
					port = Integer.parseInt(portstr);
					/*if(PropertiesCofig.getColonyIsOpen()&&PropertiesCofig.getServiceIp().equals(PropertiesCofig.getMasterNodeIp())){
						if(port==masterNodePort){
							throw new RuntimeException("主节点端口与服务端口冲突");
						}
					}*/
					SocketServerThread serverThread = new SocketServerThread(port);
					serverThread.start();
					SocketServerList.add(serverThread);
					log.info("初始化Socket服务完成,服务器端口为：" + port + "!");
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		}
		/**服务端口启动，生成代理*/
		/**主节点端口启动，生成代理*/
		/*if(PropertiesCofig.getColonyIsOpen()&&PropertiesCofig.getServiceIp().equals(PropertiesCofig.getMasterNodeIp())){
			try {
				SocketServerThread	serverThread = new SocketServerThread(masterNodePort,ColonySystemConstant.masterNode);//主节点
				serverThread.start();
				SocketServerList.add(serverThread);
				log.info("集群主节点初始化完成,端口为：" + masterNodePort + "!");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}*/
		/**主节点端口启动，生成代理*/
		
	}
	public static SYSMesageQueue getSYSMesageQueue() {
		return SYSMesageQueue;
	}

	public static TimeRunExecute getTimeRunExecute() {
		return timeRunExecute;
	}

	public static void setTimeRunExecute(TimeRunExecute timeRunExecute) {
		SocketServerCore.timeRunExecute = timeRunExecute;
	}
	
	public static ReceiveQueue getReceiveQueue() {
		return receiveQueue;
	}

	public static void setReceiveQueue(ReceiveQueue receiveQueue) {
		SocketServerCore.receiveQueue = receiveQueue;
	}

	public static ObjectPool getAgentObjectPool() {
		return agentObjectPool;
	}
	
	public static DelayConnection[] getDelayConnection(){//获取延时连接数组
		if(PropertiesCofig.getDelayConnectionIsOpen()){
			try {
				String[] strs=PropertiesCofig.getDelayConnectionInstall().split(",");
				DelayConnection[] delayConnectionS =new DelayConnection[strs.length];
				for(int i=0;i<strs.length;i++){
					String[] a=strs[i].split(":");
					delayConnectionS[i]=new DelayConnection(Integer.valueOf(a[0]), Long.valueOf(a[1]));
				}
				return delayConnectionS;
			} catch (Exception e) {
				throw new RuntimeException("延时连接设置配置错误！");
			}		
		}
		return null;
	}

	
	public static DelayConnection[] getDelayConnectionS() {
		return delayConnectionS;
	}

	public static void setDelayConnectionS(DelayConnection[] delayConnectionS) {
		SocketServerCore.delayConnectionS = delayConnectionS;
	}

	public static HighSpeedBuffer getHighSpeedBuffer() {
		return highSpeedBuffer;
	}

	public static void setHighSpeedBuffer(HighSpeedBuffer highSpeedBuffer) {
		SocketServerCore.highSpeedBuffer = highSpeedBuffer;
	}
}
