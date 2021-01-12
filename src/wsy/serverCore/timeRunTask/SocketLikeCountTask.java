package wsy.serverCore.timeRunTask;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import wsy.serverCore.internalInterface.TimeRun;
import wsy.serverCore.server.SocketChannelAgent;
import wsy.serverCore.system.SocketChannelDate;
import wsy.serverCore.system.SocketLikeCountData;
import wsy.serverCore.system.ServerSystemConstant;
import wsy.utils.LogAgent;

/** 
* @author black
* @version 创建时间：2016年8月29日 下午5:07:44 
* 校验更新socketLikeCountMap连接数
*/
public class SocketLikeCountTask extends TimeRun {
	private final static LogAgent log = new LogAgent(SocketLikeCountTask.class);//log
	public SocketLikeCountTask(long intervalTime) {
		super(intervalTime);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void executeFunction() {
		Integer currentNumber= (Integer)SocketLikeCountData.socketLikeCountMapGet(ServerSystemConstant.currentNumberConnections);
		if(SocketChannelDate.socketChannelMapSize()!=currentNumber){
			Map<String,Object> map =new HashMap<String,Object>();
		   	Iterator<?> iter = SocketChannelDate.socketChannelMapIterator();
		   	    currentNumber=0;
			while (iter.hasNext()) {
				currentNumber++;
				Map.Entry<String, Object> entry=(Map.Entry<String, Object>)iter.next();
				SocketChannelAgent socketChannelAgent= (SocketChannelAgent) entry.getValue();
				String ip= socketChannelAgent.getLinkBase().getIp();
				Integer num=(Integer) map.get(ip);
				if(num==null){
					num=0;
				}
				map.put(ip, num+1);
			}
			map.put(ServerSystemConstant.currentNumberConnections,currentNumber);
			SocketLikeCountData.setSocketLikeCountMap(map);
			log.info("socketLikeCountMap连接数与总连接数存在差异，系统自动进行更新！");
		}
		log.info("校验更新socketLikeCountMap连接数执行成功！");
	}

}
