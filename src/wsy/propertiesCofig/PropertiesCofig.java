package wsy.propertiesCofig;

import wsy.core.realizationInterface.CommandExecute;
import wsy.core.realizationInterface.DataEncryptionDecrypt;
import wsy.serverCore.encryption.EncryptionDecrypt;
import wsy.serverCore.realizationInterface.BlackList;
import wsy.serverCore.realizationInterface.WhiteList;
import wsy.utils.PropertiesAutoSerialize;

public class PropertiesCofig {
	/**servercore*/
	private static String serviceIp="127.0.0.1";//服务器ip
	private static String servicePortS="8888" ;//端口
	private static Integer checkBlacklist=3;//校验失败几次不允许链接 （系统默认加密方式）
	private static Boolean blacklistBoolean=false;//黑名单是否打开 
	private static Boolean whitelistBoolean=false;//白名单是否打开
	private static Integer ipOpenNumLink=10;//一个ip最大允许链接个数
	private static String encryptionKey="7,18,38,78,224,660,1747,3914,7521,12278,21343,37373,62098,89533,174392,211191"; //加密密钥 （系统默认加密方式）
	private static Integer len=3;//混淆长度（系统默认加密方式）
	private static Boolean encryptionBoolean=false; //是否打开加密（系统默认加密方式）
	private static Boolean checkBoolean=false;//校验是否打开 （系统默认加密方式）
	private static String logPath=getlogPath(); //logPath 日志地址
	private static Boolean socketSendIsOpen=true;//校验是否打开	
	private static Boolean serverHeartbeatTestingIsOpen=false;//心跳检测是否打开
	private static String serverHeartbeatTestingTime="10000";//心跳失活 间隔时间（毫秒） ？*？*？  或者100000毫秒
	private static String heartbeatSignal="hello";//心跳信号
	private static Boolean serverDataStorageRedis =false;//服务端数据存储到redis
	private static String socketServerKey="List_server_socket_message";//redis key	服务端
	private static String redisServerStorageMode="Alone";//redis存储方式(Alone 单集合模式，Many多集合模式)
	private static Integer redisServerManyStorageModeListSize=1000;//多队列时每个队列的大小
	private static String redisServerManyStorageModeKey="List_server_socket_Many_Mum";//服务端多队列已经存储队列号redis—key
	private static Boolean checkDataIsFullIsOpen=false;//校验多队列模式时每个队列是否存满（Many多集合模式）
	private static String checkDataIsFullKey="List_server_socket_is_full";//#校验key值 与socketServerKey一一对应	
	private static Boolean commandExecuteIsOpen;//自定义消息处理器是否打开
	private static Integer serverMaxLinkNum=1000; //服务端最大连接数
	private static Boolean timeRunExecuteIsOpen=false;//任务执行器是否打开
	private static Boolean agentObjectPoolIsOpen=true;//代理对象池是否打开
	private static Integer agentObjectPoolSize=1000;//代理对象池大小
	private static Integer agentObjectPoolEstablish=10;//代理对象低于多少时创建
	private static Boolean delayConnectionIsOpen=false;//延时连接是否打开
	private static String delayConnectionInstall="500:2,1000:4,1500:6,2000:8,2500:10,3000:12,3500:14"; //延时连接设置x:y  x为服务器连接数-代理对象池大小:y为延时毫秒数  这里连接数请按顺序填写	
	private static Boolean highSpeedBufferIsOpen=true;//redis内存式高速缓存区是否打开
	private static String highSpeedBufferStorageMode="Alone";//redis内存式高速缓存区存储方式(Alone 单数据模式，Many多数据模式)
	private static Boolean pushThreadIsOpen=false;//推送线程是否打开（数据量单位时间内达到redisServerManyStorageModeListSize时不用打开）
	private static String pushThreadTime="1000";//推送线程多久执行一次
	
	/**quere*/
	private static Boolean ReceiveQueueIsOpen=true;//系统数据接收队列是否打开
	private static Integer ReceiveQueueSize=10000;//系统数据接收队列的大小
	private static Boolean MessageQueueIsOpen=false; //系统消息队列是否打开
	private static Integer MessageQueueSize=30; //系统消息集合的大小
	private static Boolean SYSMesageQueueIsOpen=false; //系统消息队列是否打开
	private static Integer SYSMesageQueueSize=100; //系统消息集合的大小
	private static Integer thresholdRate=5;//队列阀值倍率
	private static String securityQueue="poll_access";//安全队列（超出阀值，poll_access:出列并存取，clear:清空队列，stop_access：停止存取，error：报错）
	
	/**system**/
	private static Boolean logIsOpen=true;//log日志是否打开
	
	/**redis*/
	private static Boolean redisIsOpen=false;//redis是否打开
	private static String redisIp="127.0.0.1";//redis IP
	private static Integer redisPort=6379;//redis 端口
	private static String redisPassword=null;//redis 密码
	private static Integer libraryNumber=0;//库编号
	private static Integer redisMaxActive=-1;//redis 最大连接数
	private static Integer redisMaxIdle=-1;//redis 最大空闲数
	private static Integer redisMinIdle=500;//redis 初始化数
	private static Integer redisMaxWait=120000;//redis 超时时间
	private static Boolean redisTestOnBorrow=false;//redis 提前进行validate操作，确保获取的jedis实例均是可用
	
	/**job*/
	private static String timeRunExecuteSleepTime="1000";//任务执行器最小休眠时间（毫秒）？*？*？  或者100000毫秒
	private static String socketCloseTaskTime="1000*60";//定时清理SocketCloseMap 间隔时间（毫秒） ？*？*？  或者100000毫秒
	private static String socketLikeCountTaskTime="1000*60*30";//校验更新socketLikeCountMap连接数 间隔时间（毫秒） ？*？*？  或者100000毫秒
	private static String agentObjectPoolTaskTime="1000*60";//#更细代理对象池 间隔时间（毫秒） ？*？*？  或者100000毫秒
	private static String heartbeatTestingTaskTime="2000";//#心跳检测 间隔时间（毫秒） ？*？*？  或者100000毫秒
	
	/**Interface*/
	private static CommandExecute commandServerExecute;//命令执行器
	private static CommandExecute commandClientExecute;//命令执行器
	private static DataEncryptionDecrypt dataEncryptionDecrypt=new EncryptionDecrypt();//数据加密解密
	private static BlackList blackList;//黑名单
	private static WhiteList whiteList;//白名单
	
	/**clientCore*/
	private static Boolean clientDataStorageRedis =false;//客户端数据存储到redis
	private static String socketClientKey="List_client_socket_message";//redis key	客户端	
	private static String redisClientStorageMode="Alone";//redis存储方式(Alone 单队列模式，Many 多队列模式)
	private static Integer redisClientManyStorageModeListSize=1000;//多队列时每个队列的大小
	private static String redisClientManyStorageModeKey="List_client_socket_Many_Mum";//客户端多队列已经存储队列号redis—key
	private static String clientIp=null;//客户端ip
	private static String clientPortS="13527";//客户端端口
	private static String linkServerIp=null; //连接到服务端ip和端口
	private static String linkServerPort ="8888";//连接到服务端端口
	private static Boolean heartbeatIsOpen=false;//是否开启心跳
	private static String heartbeatTime="1000";//心跳时间
	private static String clientHeartbeatSignal="I'm in turmoil";//心跳信号
	
	static{
		try {
			PropertiesAutoSerialize.init("/socketfly.properties", PropertiesCofig.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	static String getlogPath(){
		String os = System.getProperty("os.name").toLowerCase();
		if(os.indexOf("windows")>=0){
		return	"c://log//";
		}else if(os.indexOf("linux")>=0){
			return	"/opt/log";
		}
		return null;
	}

	public static String getServiceIp() {
		return serviceIp;
	}

	public static void setServiceIp(String serviceIp) {
		PropertiesCofig.serviceIp = serviceIp;
	}

	public static String getServicePortS() {
		return servicePortS;
	}

	public static void setServicePortS(String servicePortS) {
		PropertiesCofig.servicePortS = servicePortS;
	}

	public static Integer getCheckBlacklist() {
		return checkBlacklist;
	}

	public static void setCheckBlacklist(Integer checkBlacklist) {
		PropertiesCofig.checkBlacklist = checkBlacklist;
	}

	public static Boolean getBlacklistBoolean() {
		return blacklistBoolean;
	}

	public static void setBlacklistBoolean(Boolean blacklistBoolean) {
		PropertiesCofig.blacklistBoolean = blacklistBoolean;
	}

	public static Boolean getWhitelistBoolean() {
		return whitelistBoolean;
	}

	public static void setWhitelistBoolean(Boolean whitelistBoolean) {
		PropertiesCofig.whitelistBoolean = whitelistBoolean;
	}

	public static Integer getIpOpenNumLink() {
		return ipOpenNumLink;
	}

	public static void setIpOpenNumLink(Integer ipOpenNumLink) {
		PropertiesCofig.ipOpenNumLink = ipOpenNumLink;
	}

	public static String getEncryptionKey() {
		return encryptionKey;
	}

	public static void setEncryptionKey(String encryptionKey) {
		PropertiesCofig.encryptionKey = encryptionKey;
	}

	public static Integer getLen() {
		return len;
	}

	public static void setLen(Integer len) {
		PropertiesCofig.len = len;
	}

	public static Boolean getEncryptionBoolean() {
		return encryptionBoolean;
	}

	public static void setEncryptionBoolean(Boolean encryptionBoolean) {
		PropertiesCofig.encryptionBoolean = encryptionBoolean;
	}

	public static Boolean getCheckBoolean() {
		return checkBoolean;
	}

	public static void setCheckBoolean(Boolean checkBoolean) {
		PropertiesCofig.checkBoolean = checkBoolean;
	}

	public static String getLogPath() {
		return logPath;
	}

	public static void setLogPath(String logPath) {
		PropertiesCofig.logPath = logPath;
	}

	public static Boolean getSocketSendIsOpen() {
		return socketSendIsOpen;
	}

	public static void setSocketSendIsOpen(Boolean socketSendIsOpen) {
		PropertiesCofig.socketSendIsOpen = socketSendIsOpen;
	}
	
	public static Boolean getServerHeartbeatTestingIsOpen() {
		return serverHeartbeatTestingIsOpen;
	}

	public static void setServerHeartbeatTestingIsOpen(Boolean serverHeartbeatTestingIsOpen) {
		PropertiesCofig.serverHeartbeatTestingIsOpen = serverHeartbeatTestingIsOpen;
	}

	public static Long getServerHeartbeatTestingTime() {
		Long s=1l;	
		String[] ms=serverHeartbeatTestingTime.split("\\*");
		for(String i:ms){
			s=s*Long.valueOf(i);
		}
		return s;
	}

	public static void setServerHeartbeatTestingTime(String serverHeartbeatTestingTime) {
		PropertiesCofig.serverHeartbeatTestingTime = serverHeartbeatTestingTime;
	}
	
	public static void setServerHeartbeatTestingTime(Long serverHeartbeatTestingTime) {
		PropertiesCofig.serverHeartbeatTestingTime = serverHeartbeatTestingTime.toString();
	}

	public static String getHeartbeatSignal() {
		return heartbeatSignal;
	}

	public static void setHeartbeatSignal(String heartbeatSignal) {
		PropertiesCofig.heartbeatSignal = heartbeatSignal;
	}

	public static Boolean getReceiveQueueIsOpen() {
		return ReceiveQueueIsOpen;
	}

	public static void setReceiveQueueIsOpen(Boolean receiveQueueIsOpen) {
		ReceiveQueueIsOpen = receiveQueueIsOpen;
	}

	public static Integer getReceiveQueueSize() {
		return ReceiveQueueSize;
	}

	public static void setReceiveQueueSize(Integer receiveQueueSize) {
		ReceiveQueueSize = receiveQueueSize;
	}

	public static Boolean getServerDataStorageRedis() {
		return serverDataStorageRedis;
	}

	public static void setServerDataStorageRedis(Boolean serverDataStorageRedis) {
		PropertiesCofig.serverDataStorageRedis = serverDataStorageRedis;
	}

	public static String getSocketServerKey() {
		return socketServerKey;
	}

	public static void setSocketServerKey(String socketServerKey) {
		PropertiesCofig.socketServerKey = socketServerKey;
	}

	public static String getRedisServerStorageMode() {
		return redisServerStorageMode;
	}

	public static void setRedisServerStorageMode(String redisServerStorageMode) {
		PropertiesCofig.redisServerStorageMode = redisServerStorageMode;
	}

	public static Integer getRedisServerManyStorageModeListSize() {
		return redisServerManyStorageModeListSize;
	}

	public static void setRedisServerManyStorageModeListSize(Integer redisServerManyStorageModeListSize) {
		PropertiesCofig.redisServerManyStorageModeListSize = redisServerManyStorageModeListSize;
	}

	public static String getRedisServerManyStorageModeKey() {
		return redisServerManyStorageModeKey;
	}

	public static void setRedisServerManyStorageModeKey(String redisServerManyStorageModeKey) {
		PropertiesCofig.redisServerManyStorageModeKey = redisServerManyStorageModeKey;
	}

	public static Boolean getCheckDataIsFullIsOpen() {
		return checkDataIsFullIsOpen;
	}

	public static void setCheckDataIsFullIsOpen(Boolean checkDataIsFullIsOpen) {
		PropertiesCofig.checkDataIsFullIsOpen = checkDataIsFullIsOpen;
	}

	public static String getCheckDataIsFullKey() {
		return checkDataIsFullKey;
	}

	public static void setCheckDataIsFullKey(String checkDataIsFullKey) {
		PropertiesCofig.checkDataIsFullKey = checkDataIsFullKey;
	}

	public static Boolean getSYSMesageQueueIsOpen() {
		return SYSMesageQueueIsOpen;
	}

	public static void setSYSMesageQueueIsOpen(Boolean sYSMesageQueueIsOpen) {
		SYSMesageQueueIsOpen = sYSMesageQueueIsOpen;
	}

	public static Integer getSYSMesageQueueSize() {
		return SYSMesageQueueSize;
	}

	public static void setSYSMesageQueueSize(Integer sYSMesageQueueSize) {
		SYSMesageQueueSize = sYSMesageQueueSize;
	}

	public static Boolean getMessageQueueIsOpen() {
		return MessageQueueIsOpen;
	}

	public static void setMessageQueueIsOpen(Boolean messageQueueIsOpen) {
		MessageQueueIsOpen = messageQueueIsOpen;
	}

	public static Integer getMessageQueueSize() {
		return MessageQueueSize;
	}

	public static void setMessageQueueSize(Integer messageQueueSize) {
		MessageQueueSize = messageQueueSize;
	}

	public static Boolean getCommandExecuteIsOpen() {
		return commandExecuteIsOpen;
	}

	public static void setCommandExecuteIsOpen(Boolean commandExecuteIsOpen) {
		PropertiesCofig.commandExecuteIsOpen = commandExecuteIsOpen;
	}

	public static Integer getServerMaxLinkNum() {
		return serverMaxLinkNum;
	}

	public static void setServerMaxLinkNum(Integer serverMaxLinkNum) {
		PropertiesCofig.serverMaxLinkNum = serverMaxLinkNum;
	}

	public static Boolean getLogIsOpen() {
		return logIsOpen;
	}

	public static void setLogIsOpen(Boolean logIsOpen) {
		PropertiesCofig.logIsOpen = logIsOpen;
	}

	public static Boolean getRedisIsOpen() {
		return redisIsOpen;
	}

	public static void setRedisIsOpen(Boolean redisIsOpen) {
		PropertiesCofig.redisIsOpen = redisIsOpen;
	}

	public static String getRedisIp() {
		return redisIp;
	}

	public static void setRedisIp(String redisIp) {
		PropertiesCofig.redisIp = redisIp;
	}

	public static Integer getRedisPort() {
		return redisPort;
	}

	public static void setRedisPort(Integer redisPort) {
		PropertiesCofig.redisPort = redisPort;
	}

	public static String getRedisPassword() {
		return redisPassword;
	}

	public static void setRedisPassword(String redisPassword) {
		PropertiesCofig.redisPassword = redisPassword;
	}

	public static Integer getLibraryNumber() {
		return libraryNumber;
	}

	public static void setLibraryNumber(Integer libraryNumber) {
		PropertiesCofig.libraryNumber = libraryNumber;
	}

	public static Integer getRedisMaxActive() {
		return redisMaxActive;
	}

	public static void setRedisMaxActive(Integer redisMaxActive) {
		PropertiesCofig.redisMaxActive = redisMaxActive;
	}

	public static Integer getRedisMaxIdle() {
		return redisMaxIdle;
	}

	public static void setRedisMaxIdle(Integer redisMaxIdle) {
		PropertiesCofig.redisMaxIdle = redisMaxIdle;
	}

	public static Integer getRedisMinIdle() {
		return redisMinIdle;
	}

	public static void setRedisMinIdle(Integer redisMinIdle) {
		PropertiesCofig.redisMinIdle = redisMinIdle;
	}

	public static Integer getRedisMaxWait() {
		return redisMaxWait;
	}

	public static void setRedisMaxWait(Integer redisMaxWait) {
		PropertiesCofig.redisMaxWait = redisMaxWait;
	}

	public static Boolean getRedisTestOnBorrow() {
		return redisTestOnBorrow;
	}

	public static void setRedisTestOnBorrow(Boolean redisTestOnBorrow) {
		PropertiesCofig.redisTestOnBorrow = redisTestOnBorrow;
	}

	public static CommandExecute getCommandServerExecute() {
		return commandServerExecute;
	}

	public static void setCommandServerExecute(CommandExecute commandServerExecute) {
		PropertiesCofig.commandServerExecute = commandServerExecute;
	}

	public static CommandExecute getCommandClientExecute() {
		return commandClientExecute;
	}

	public static void setCommandClientExecute(CommandExecute commandClientExecute) {
		PropertiesCofig.commandClientExecute = commandClientExecute;
	}

	public static DataEncryptionDecrypt getDataEncryptionDecrypt() {
		return dataEncryptionDecrypt;
	}

	public static void setDataEncryptionDecrypt(DataEncryptionDecrypt dataEncryptionDecrypt) {
		PropertiesCofig.dataEncryptionDecrypt = dataEncryptionDecrypt;
	}

	public static BlackList getBlackList() {
		return blackList;
	}

	public static void setBlackList(BlackList blackList) {
		PropertiesCofig.blackList = blackList;
	}

	public static WhiteList getWhiteList() {
		return whiteList;
	}

	public static void setWhiteList(WhiteList whiteList) {
		PropertiesCofig.whiteList = whiteList;
	}

	public static Boolean getTimeRunExecuteIsOpen() {
		return timeRunExecuteIsOpen;
	}

	public static void setTimeRunExecuteIsOpen(Boolean timeRunExecuteIsOpen) {
		PropertiesCofig.timeRunExecuteIsOpen = timeRunExecuteIsOpen;
	}
	

	public static Boolean getAgentObjectPoolIsOpen() {
		return agentObjectPoolIsOpen;
	}

	public static void setAgentObjectPoolIsOpen(Boolean agentObjectPoolIsOpen) {
		PropertiesCofig.agentObjectPoolIsOpen = agentObjectPoolIsOpen;
	}

	public static Integer getAgentObjectPoolSize() {
		return agentObjectPoolSize;
	}

	public static void setAgentObjectPoolSize(Integer agentObjectPoolSize) {
		PropertiesCofig.agentObjectPoolSize = agentObjectPoolSize;
	}

	public static Integer getAgentObjectPoolEstablish() {
		return agentObjectPoolEstablish;
	}

	public static void setAgentObjectPoolEstablish(Integer agentObjectPoolEstablish) {
		PropertiesCofig.agentObjectPoolEstablish = agentObjectPoolEstablish;
	}

	public static Boolean getDelayConnectionIsOpen() {
		return delayConnectionIsOpen;
	}

	public static void setDelayConnectionIsOpen(Boolean delayConnectionIsOpen) {
		PropertiesCofig.delayConnectionIsOpen = delayConnectionIsOpen;
	}

	public static String getDelayConnectionInstall() {
		return delayConnectionInstall;
	}

	public static void setDelayConnectionInstall(String delayConnectionInstall) {
		PropertiesCofig.delayConnectionInstall = delayConnectionInstall;
	}

	public static Boolean getHighSpeedBufferIsOpen() {
		return highSpeedBufferIsOpen;
	}

	public static void setHighSpeedBufferIsOpen(Boolean highSpeedBufferIsOpen) {
		PropertiesCofig.highSpeedBufferIsOpen = highSpeedBufferIsOpen;
	}
	
	public static String getHighSpeedBufferStorageMode() {
		return highSpeedBufferStorageMode;
	}

	public static void setHighSpeedBufferStorageMode(String highSpeedBufferStorageMode) {
		PropertiesCofig.highSpeedBufferStorageMode = highSpeedBufferStorageMode;
	}

	public static Boolean getPushThreadIsOpen() {
		return pushThreadIsOpen;
	}

	public static void setPushThreadIsOpen(Boolean pushThreadIsOpen) {
		PropertiesCofig.pushThreadIsOpen = pushThreadIsOpen;
	}

	public static Long getPushThreadTime() {
		Long s=1l;	
		String[] ms=pushThreadTime.split("\\*");
		for(String i:ms){
			s=s*Long.valueOf(i);
		}
		return s;
	}

	public static void setPushThreadTime(String pushThreadTime) {
		PropertiesCofig.pushThreadTime = pushThreadTime;
	}
	
	public static void setPushThreadTime(Long pushThreadTime) {
		PropertiesCofig.pushThreadTime = pushThreadTime.toString();
	}

	public static Integer getThresholdRate() {
		return thresholdRate;
	}

	public static void setThresholdRate(Integer thresholdRate) {
		PropertiesCofig.thresholdRate = thresholdRate;
	}

	public static String getSecurityQueue() {
		return securityQueue;
	}

	public static void setSecurityQueue(String securityQueue) {
		PropertiesCofig.securityQueue = securityQueue;
	}

	public static Long getTimeRunExecuteSleepTime() {
		Long s=1l;	
		String[] ms=timeRunExecuteSleepTime.split("\\*");
		for(String i:ms){
			s=s*Long.valueOf(i);
		}
		return s;
	}

	public static void setTimeRunExecuteSleepTime(String timeRunExecuteSleepTime) {
		PropertiesCofig.timeRunExecuteSleepTime = timeRunExecuteSleepTime;
	}
	
	public static void setTimeRunExecuteSleepTime(Long timeRunExecuteSleepTime) {
		PropertiesCofig.timeRunExecuteSleepTime = timeRunExecuteSleepTime.toString();
	}

	public static Long getSocketCloseTaskTime() {
		Long s=1l;	
		String[] ms=socketCloseTaskTime.split("\\*");
		for(String i:ms){
			s=s*Long.valueOf(i);
		}
		return s;
	}

	public static void setSocketCloseTaskTime(String socketCloseTaskTime) {
		PropertiesCofig.socketCloseTaskTime = socketCloseTaskTime;
	}
	
	public static void setSocketCloseTaskTime(Long socketCloseTaskTime) {
		PropertiesCofig.socketCloseTaskTime = socketCloseTaskTime.toString();
	}

	public static Long getSocketLikeCountTaskTime() {
		Long s=1l;	
		String[] ms=socketLikeCountTaskTime.split("\\*");
		for(String i:ms){
			s=s*Long.valueOf(i);
		}
		return s;
	}

	public static void setSocketLikeCountTaskTime(String socketLikeCountTaskTime) {
		PropertiesCofig.socketLikeCountTaskTime = socketLikeCountTaskTime;
	}
	
	public static void setSocketLikeCountTaskTime(Long socketLikeCountTaskTime) {
		PropertiesCofig.socketLikeCountTaskTime = socketLikeCountTaskTime.toString();
	}

	public static Long getAgentObjectPoolTaskTime() {
		Long s=1l;	
		String[] ms=agentObjectPoolTaskTime.split("\\*");
		for(String i:ms){
			s=s*Long.valueOf(i);
		}
		return s;
	}

	public static void setAgentObjectPoolTaskTime(String agentObjectPoolTaskTime) {
		PropertiesCofig.agentObjectPoolTaskTime = agentObjectPoolTaskTime;
	}
	
	public static void setAgentObjectPoolTaskTime(Long agentObjectPoolTaskTime) {
		PropertiesCofig.agentObjectPoolTaskTime = agentObjectPoolTaskTime.toString();
	}

	public static Long getHeartbeatTestingTaskTime() {
		Long s=1l;	
		String[] ms=heartbeatTestingTaskTime.split("\\*");
		for(String i:ms){
			s=s*Long.valueOf(i);
		}
		return s;
	}

	public static void setHeartbeatTestingTaskTime(String heartbeatTestingTaskTime) {
		PropertiesCofig.heartbeatTestingTaskTime = heartbeatTestingTaskTime;
	}
	
	public static void setHeartbeatTestingTaskTime(Long heartbeatTestingTaskTime) {
		PropertiesCofig.heartbeatTestingTaskTime = heartbeatTestingTaskTime.toString();
	}

	public static String getClientIp() {
		return clientIp;
	}

	public static void setClientIp(String clientIp) {
		PropertiesCofig.clientIp = clientIp;
	}

	public static String getClientPortS() {
		return clientPortS;
	}

	public static void setClientPortS(String clientPortS) {
		PropertiesCofig.clientPortS = clientPortS;
	}

	public static Boolean getClientDataStorageRedis() {
		return clientDataStorageRedis;
	}

	public static void setClientDataStorageRedis(Boolean clientDataStorageRedis) {
		PropertiesCofig.clientDataStorageRedis = clientDataStorageRedis;
	}

	public static String getSocketClientKey() {
		return socketClientKey;
	}

	public static void setSocketClientKey(String socketClientKey) {
		PropertiesCofig.socketClientKey = socketClientKey;
	}

	public static String getRedisClientStorageMode() {
		return redisClientStorageMode;
	}

	public static void setRedisClientStorageMode(String redisClientStorageMode) {
		PropertiesCofig.redisClientStorageMode = redisClientStorageMode;
	}

	public static Integer getRedisClientManyStorageModeListSize() {
		return redisClientManyStorageModeListSize;
	}

	public static void setRedisClientManyStorageModeListSize(Integer redisClientManyStorageModeListSize) {
		PropertiesCofig.redisClientManyStorageModeListSize = redisClientManyStorageModeListSize;
	}

	public static String getRedisClientManyStorageModeKey() {
		return redisClientManyStorageModeKey;
	}

	public static void setRedisClientManyStorageModeKey(String redisClientManyStorageModeKey) {
		PropertiesCofig.redisClientManyStorageModeKey = redisClientManyStorageModeKey;
	}

	public static String getLinkServerIp() {
		return linkServerIp;
	}

	public static void setLinkServerIp(String linkServerIp) {
		PropertiesCofig.linkServerIp = linkServerIp;
	}

	public static String getLinkServerPort() {
		return linkServerPort;
	}

	public static void setLinkServerPort(String linkServerPort) {
		PropertiesCofig.linkServerPort = linkServerPort;
	}

	public static Boolean getHeartbeatIsOpen() {
		return heartbeatIsOpen;
	}

	public static void setHeartbeatIsOpen(Boolean heartbeatIsOpen) {
		PropertiesCofig.heartbeatIsOpen = heartbeatIsOpen;
	}

	public static Long getHeartbeatTime() {
		Long s=1l;	
		String[] ms=heartbeatTime.split("\\*");
		for(String i:ms){
			s=s*Long.valueOf(i);
		}
		return s;
	}

	public static void setHeartbeatTime(String heartbeatTime) {
		PropertiesCofig.heartbeatTime = heartbeatTime;
	}
	
	public static void setHeartbeatTime(Long heartbeatTime) {
		PropertiesCofig.heartbeatTime = heartbeatTime.toString();
	}

	public static String getClientHeartbeatSignal() {
		return clientHeartbeatSignal;
	}

	public static void setClientHeartbeatSignal(String clientHeartbeatSignal) {
		PropertiesCofig.clientHeartbeatSignal = clientHeartbeatSignal;
	}

}
