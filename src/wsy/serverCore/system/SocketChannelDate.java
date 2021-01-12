package wsy.serverCore.system;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/** 
* @author black
* @version 创建时间：2016年8月29日 下午6:07:05 
* 存放所有服务端的Socket代理   
*/
public class SocketChannelDate {
	 private static Map<String, Object> socketChannelMap = new ConcurrentHashMap<String, Object>(); //存放所有服务端的Socket代理(HashMap非线程安全，不使用)    

	 public static void socketChannelMapPut(String key,Object value){
			 socketChannelMap.put(key, value);
	 }
	 public static Object socketChannelMapGet(String key){
			return socketChannelMap.get(key);
	 }
	 public static Object socketChannelMapRemove(String key){
			return socketChannelMap.remove(key);
	 }
	 public static int socketChannelMapSize(){
			return socketChannelMap.size();
	 }	 
	 public static Iterator<?> socketChannelMapIterator(){
			return socketChannelMap.entrySet().iterator();
	 }

}
