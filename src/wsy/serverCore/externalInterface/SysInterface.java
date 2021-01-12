package wsy.serverCore.externalInterface;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import wsy.propertiesCofig.PropertiesCofig;
import wsy.serverCore.server.SocketServerCore;

public class SysInterface {
	private final static Logger log = Logger.getLogger(SysInterface.class);//系统消息相关log全部用log4j
	
	/**
	 * 从系统队列里获得一条数据
	 */
	public static String queueGetMessage(){
		if(PropertiesCofig.getSYSMesageQueueIsOpen()){
			String str=(String)SocketServerCore.getSYSMesageQueue().poll();
			return str;
		}else{
			log.info("系统消息队列未开启！");
			return null;
		}	
	}
	
	/**
	 * 从系统队列里获得所有数据
	 */
	public static List<String> queueGetLiseAll(){
		if(PropertiesCofig.getSYSMesageQueueIsOpen()){
			List<Object> list=SocketServerCore.getSYSMesageQueue().getList();
			List<String> messageList= new ArrayList<String>();
				if(list!=null && list.size()!=0){
					for(Object i:list){
						messageList.add((String)i);
					}
				}else{
					return null;
				}		
			return messageList;
		}else{
			log.info("系统消息队列未开启！");
			return null;
		}
	}
}
