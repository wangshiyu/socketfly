package wsy.serverCore.internalFunction;

import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import wsy.core.realizationInterface.DataEncryptionDecrypt;
import wsy.propertiesCofig.PropertiesCofig;
import wsy.serverCore.businessbean.DataTransmission;
import wsy.serverCore.server.SocketChannelAgent;
import wsy.serverCore.system.SocketChannelDate;
import wsy.utils.JsonUtil;
import wsy.utils.LogAgent;

/**
 * 系统服务方法类
 * @author black
 *
 */
public class SysServiceFunction	{
	 private final static LogAgent log = new LogAgent(SysServiceFunction.class);//log
	/**
	 * 返回socket的ip和端口
	 */
	protected Map<String,Object> ipAndPort(Socket socket) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("ip", socket.getInetAddress().toString().replaceAll("/",""));
		map.put("port", socket.getPort()+"");
		return map;
	}
	
	/***
     * 通过ip和端口号获得socket代理对象
     * @param ip
     * @param port
     * @return
     */
	protected SocketChannelAgent getSocketChannelThread(String ip,String port){
  	 String str="/"+ip+":"+port;
  	 return	(SocketChannelAgent) SocketChannelDate.socketChannelMapGet(str); 	 
   }
	
	/**
	 * 发送message
	 * @param dataTransmission
	 * @param socket
	 * @throws Exception 
	 */
	protected void sendMessage(DataTransmission dataTransmission,Socket socket) throws Exception{
		DataEncryptionDecrypt dataEncryptionDecrypt=PropertiesCofig.getDataEncryptionDecrypt();
		if(null==dataEncryptionDecrypt){
			throw new Exception("加密接口为空！");
		}
		if(!PropertiesCofig.getSocketSendIsOpen()){
			throw new Exception("系统当前配置不允许发送数据");
		}
		SocketChannelAgent socketChannelAgent= this.getSocketChannelThread(socket.getInetAddress().toString().replaceAll("/",""),socket.getPort()+""); 
		String message=dataEncryptionDecrypt.encryption(JsonUtil.getJson(dataTransmission));
		socketChannelAgent.getSend().sendOut(message);
		socketChannelAgent.getLinkBase().updateSend(message.getBytes().length);//更新信息
	}
	
	/**
	 * 给未建立代理的socket发送信息
	 * @param message
	 * @param socket
	 * @param isSerialize 是否序列化
	 * @param isEncryption 是否加密
	 * @throws Exception
	 */
	protected void sendMessage(String message,Socket socket,boolean isSerialize,boolean isEncryption) throws Exception{
		PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), "utf-8"));			//缓存区写
		String logMessage=message;
		if(isSerialize){
			//暂时没序列化这步
		}
		if(isEncryption){
			message=PropertiesCofig.getDataEncryptionDecrypt().encryption(message);
		}
		try {
			out.print(message+"\r\n");
			out.flush();
			out.close();
		} catch (Exception e) {
			String ip= socket.getInetAddress().toString().replaceAll("/","");
			int port =socket.getPort();
			if(!isEncryption){
				log.info("ip地址:"+ip+"端口:"+port+"系统向其发送信息失败，信息内容"+logMessage);
			}else{
				log.info("ip地址:"+ip+"端口:"+port+"系统向其发送信息失败，信息原内容"+logMessage+"信息加密内容:"+message);
			}
		}
		
	}
	
	
	
	
}
