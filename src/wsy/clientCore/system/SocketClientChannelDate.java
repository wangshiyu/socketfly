package wsy.clientCore.system;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/** 
* @author black
* @version 创建时间：2016年8月29日 下午6:07:05 
* 存放所有客户代理
*/
public class SocketClientChannelDate {
	 private static Map<String, Object> socketClientChannelMap = new ConcurrentHashMap<String, Object>(); //存放所有服务端的Socket代理(HashMap非线程安全，不使用)    

	 public static void socketClientChannelMapPut(String key,Object value){
			 socketClientChannelMap.put(key, value);
	 }
	 public static Object socketClientChannelMapGet(String key){
			return socketClientChannelMap.get(key);
	 }
	 public static Object socketClientChannelMapRemove(String key){
			return socketClientChannelMap.remove(key);
	 }
	 public static int socketClientChannelMapSize(){
			return socketClientChannelMap.size();
	 }	 
	 public static Iterator<?> socketClientChannelMapIterator(){
			return socketClientChannelMap.entrySet().iterator();
	 }

}
