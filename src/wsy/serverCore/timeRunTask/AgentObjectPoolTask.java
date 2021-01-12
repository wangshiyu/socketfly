package wsy.serverCore.timeRunTask;

import wsy.propertiesCofig.PropertiesCofig;
import wsy.serverCore.internalInterface.TimeRun;
import wsy.serverCore.server.SocketChannelAgent;
import wsy.serverCore.server.SocketServerCore;
import wsy.utils.LogAgent;

/** 
* @author black
* @version 创建时间：2016年9月9日 上午11:10:15 
* 定时更新代理对象池
*/
public class AgentObjectPoolTask extends TimeRun{
	private final static LogAgent log = new LogAgent(AgentObjectPoolTask.class);//log
	public AgentObjectPoolTask(long intervalTime) {
		super(intervalTime);
	}
	
	@Override
	public void executeFunction() {
		if(SocketServerCore.getAgentObjectPool().size()<PropertiesCofig.getAgentObjectPoolEstablish()){
			for(int i=0;i<PropertiesCofig.getAgentObjectPoolSize()-SocketServerCore.getAgentObjectPool().size();i++){
				SocketServerCore.getAgentObjectPool().add(new SocketChannelAgent());
			}
			log.info("定时更新代理对象池agentObjectPool执行成功！对象池当前数量:"+SocketServerCore.getAgentObjectPool().size());
		}else{
			log.info("对象池当前数量:"+SocketServerCore.getAgentObjectPool().size()+"暂不更新！");
		}
	
	}
}
