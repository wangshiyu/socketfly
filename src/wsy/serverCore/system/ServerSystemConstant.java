package wsy.serverCore.system;
/** 
* @author black
* @version 创建时间：2016年8月29日 下午5:26:02 
* 系统常量
*/
public class ServerSystemConstant {
	/**
	 * 当前连接数
	 */
	public static final String currentNumberConnections="currentNumber";
	/**
	 * 出列并存取
	 */
	public static final String poll_access="poll_access";
	/**
	 * 清空队列
	 */
	public static final String clear="clear";
	/**
	 * 停止存取
	 */
	public static final String stop_access="stop_access";
	/**
	 * 报错
	 */
	public static final String error="error";
	/**
	 * 所有的ip
	 */
	public static final String AllIp="AllIp";
	/**
	 * socket连接map更新
	 * @param ip
	 * @param num
	 */
	public static void socketLikeCountMapUpade(String ip,int num){
		Integer ipCount= (Integer)SocketLikeCountData.socketLikeCountMapGet(ip);
		if(ipCount==null){
			ipCount=0;
		}
		SocketLikeCountData.socketLikeCountMapPut(ip,ipCount+num);
		if(ipCount+num<=0){
			SocketLikeCountData.socketLikeCountMapRemove(ip);
		}
		Integer currentNumber= (Integer)SocketLikeCountData.socketLikeCountMapGet(currentNumberConnections);
		SocketLikeCountData.socketLikeCountMapPut(currentNumberConnections,currentNumber+num);		
	}
}
