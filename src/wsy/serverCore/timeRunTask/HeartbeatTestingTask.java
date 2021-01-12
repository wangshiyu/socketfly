package wsy.serverCore.timeRunTask;

import java.util.Date;
import java.util.Iterator;
import java.util.Map;

import wsy.propertiesCofig.PropertiesCofig;
import wsy.serverCore.internalInterface.TimeRun;
import wsy.serverCore.server.SocketChannelAgent;
import wsy.serverCore.system.SocketChannelDate;
import wsy.utils.DateUtil;
import wsy.utils.LogAgent;

/** 
* @author black
* @version 创建时间：2016年9月9日 上午11:10:15 
*心跳检测
*/
public class HeartbeatTestingTask extends TimeRun{
	private final static LogAgent log = new LogAgent(HeartbeatTestingTask.class);//log
	public HeartbeatTestingTask(long intervalTime) {
		super(intervalTime);
	}
	
	@Override
	public void executeFunction() {
		long dataTime=new Date().getTime();
		long time=PropertiesCofig.getServerHeartbeatTestingTime();
		Iterator<?> iter = SocketChannelDate.socketChannelMapIterator();
		while (iter.hasNext()) {
			Map.Entry<String, Object> entry=(Map.Entry<String, Object>)iter.next();
			SocketChannelAgent socketChannelAgent= (SocketChannelAgent) entry.getValue();
			if(socketChannelAgent.getLinkBase().getHeartbeatTime().getTime()+time<dataTime){
				log.info("连接"+socketChannelAgent.getSocket().getRemoteSocketAddress()+"  "+DateUtil.formatDuring(time)+"未检测到心跳!");
				socketChannelAgent.close();
			}
		}
	
	}
}
