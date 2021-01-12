package wsy.serverCore.system;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/** 
* @author black
* @version 创建时间：2016年8月29日 下午6:08:02 
* 链接计数器
*/
public class SocketLikeCountData {
	 private static Map<String, Object> socketLikeCountMap = new ConcurrentHashMap<String, Object>(); //链接计数器(HashMap非线程安全，不使用)    

	 public static void socketLikeCountMapPut(String key,Object value){
			 socketLikeCountMap.put(key, value);
	 }
	 public static Object socketLikeCountMapGet(String key){
			return socketLikeCountMap.get(key);
	 }
	 public static Object socketLikeCountMapRemove(String key){
			return socketLikeCountMap.remove(key);
	 }
	 public static int socketLikeCountMapSize(){
			return socketLikeCountMap.size();
	 }
	 
	 public static Iterator<?> socketLikeCountMapIterator(){
			return socketLikeCountMap.entrySet().iterator();
	 }
	public static Map<String, Object> getSocketLikeCountMap() {
			 return socketLikeCountMap;
	}
	public static void setSocketLikeCountMap(Map<String, Object> socketLikeCountMap) {
			 SocketLikeCountData.socketLikeCountMap = socketLikeCountMap;
	} 
}
