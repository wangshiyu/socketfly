package wsy.serverCore.system;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/** 
* @author black
* @version 创建时间：2016年8月29日 下午6:46:08 
* 关闭连接的对象
*/
public class SocketCloseData {
	 private static Map<String, Object> socketCloseMap = new ConcurrentHashMap<String, Object>(); //关闭连接的对象(HashMap非线程安全，不使用)    

	 public static void socketCloseMapPut(String key,Object value){
			 socketCloseMap.put(key, value);
	 }
	 public static Object socketCloseMapGet(String key){
			return socketCloseMap.get(key);
	 }
	 public static Object socketCloseMapRemove(String key){
			return socketCloseMap.remove(key);
	 }
	 public static int socketCloseMapSize(){
			return socketCloseMap.size();
	 }
	 public static Iterator<?> socketCloseMapIterator(){
			return socketCloseMap.entrySet().iterator();
	 }
}
