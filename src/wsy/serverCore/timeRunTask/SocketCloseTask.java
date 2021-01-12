package wsy.serverCore.timeRunTask;

import java.util.Date;
import java.util.Iterator;
import java.util.Map;

import wsy.core.bean.SocketClose;
import wsy.serverCore.internalInterface.TimeRun;
import wsy.serverCore.system.SocketCloseData;
import wsy.utils.LogAgent;

/** 
* @author black
* @version 创建时间：2016年8月29日 下午4:24:15 
* 定时清理SocketCloseMap（关闭的连接）
*/
public class SocketCloseTask extends TimeRun{
	private final static LogAgent log = new LogAgent(SocketCloseTask.class);//log
	public SocketCloseTask(long intervalTime) {
		super(intervalTime);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void executeFunction() {
	long dataTime=new Date().getTime();
   	Iterator<?> iter = SocketCloseData.socketCloseMapIterator();
	while (iter.hasNext()) {
		Map.Entry<String, Object> entry=(Map.Entry<String, Object>)iter.next();
		SocketClose socketClose= (SocketClose) entry.getValue();
		if(socketClose.getMillisecond()>=0 && (socketClose.getCloseTime().getTime()+socketClose.getMillisecond())<=dataTime){
			SocketCloseData.socketCloseMapRemove(entry.getKey());
			log.info(entry.getKey()+"已经允许连接！");
		}	
	}
	log.info("定时清理SocketCloseMap执行成功！");
	}
}
