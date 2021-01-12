package wsy.serverCore.externalInterface;

import java.util.ArrayList;
import java.util.List;

import wsy.core.bean.ReceiveJMessage;
import wsy.core.redis.JedisService;
import wsy.propertiesCofig.PropertiesCofig;
import wsy.serverCore.server.SocketServerCore;
import wsy.utils.LogAgent;

public class QueueInterface {
	 private final static LogAgent log = new LogAgent(QueueInterface.class);//log
	/**
	 * 从redis里获得一条数据
	 */
	public static String redisGetMessage(String key){
		if(PropertiesCofig.getRedisIsOpen()){
			String str=JedisService.lpop(key);
			if(null!=str){
			return PropertiesCofig.getDataEncryptionDecrypt().decrypt(str);	//出列并解密
			}
			return null;
		}else{
			log.info("redis未开启！");
			return null;
		}	
	}
	
	/**
	 * 从系统队列里获得一条数据
	 */
	public static String queueGetMessage(){
		if(PropertiesCofig.getReceiveQueueIsOpen()){
			ReceiveJMessage receiveJMessage=(ReceiveJMessage)SocketServerCore.getReceiveQueue().poll();
			if(null!=receiveJMessage){
			return PropertiesCofig.getDataEncryptionDecrypt().decrypt(receiveJMessage.getMessage());	//出列并解密
			}
			return null;
		}else{
			log.info("系统队列未开启！");
			return null;
		}	
	}
	
	/**
	 * 出列redis里所有数据（并删除所有数据）
	 */
	public static List<String> redisGetLiseAll(String key){
		if(PropertiesCofig.getRedisIsOpen()){
		List<String> list=JedisService.lrange(key);
		List<String> messageList= new ArrayList<String>();
			if(list!=null && list.size()!=0){
				for(String i:list){
					String str=PropertiesCofig.getDataEncryptionDecrypt().decrypt(i);
					messageList.add(str);
				}
			}else{
				return null;
			}		
		return messageList;
		}else{
			log.info("redis未开启！");
			return null;
		}	
	}
	
	/**
	 * 获得redis里的对应条数数据（不删除数据）
	 */
	public static List<String> redisGetLisePart(String key,long length){
		if(PropertiesCofig.getRedisIsOpen()){
		List<String> list=JedisService.lrange(key,0,length);
		List<String> messageList= new ArrayList<String>();
			if(list!=null && list.size()!=0){
				for(String i:list){
					String str=PropertiesCofig.getDataEncryptionDecrypt().decrypt(i);
					messageList.add(str);
				}
			}else{
				return null;
			}		
		return messageList;
		}else{
			log.info("redis未开启！");
			return null;
		}	
	}
	
	/**
	 * 从系统队列里获得所有数据
	 */
	public static List<String> queueGetLiseAll(){
		if(PropertiesCofig.getReceiveQueueIsOpen()){
			List<Object> list=SocketServerCore.getReceiveQueue().getList();
			List<String> messageList= new ArrayList<String>();
				if(list!=null && list.size()!=0){
					for(Object i:list){
						ReceiveJMessage receiveJMessage=(ReceiveJMessage)i;
						String str =PropertiesCofig.getDataEncryptionDecrypt().decrypt(receiveJMessage.getMessage());
						messageList.add(str);
					}
				}else{
					return null;
				}		
			return messageList;
		}else{
			log.info("系统队列未开启！");
			return null;
		}
	}
}
