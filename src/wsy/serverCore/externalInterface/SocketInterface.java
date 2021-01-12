package wsy.serverCore.externalInterface;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import wsy.core.bean.LinkBase;
import wsy.core.bean.SocketClose;
import wsy.serverCore.server.SocketChannelAgent;
import wsy.serverCore.system.SocketChannelDate;
import wsy.serverCore.system.SocketCloseData;
import wsy.serverCore.system.SocketLikeCountData;

public class SocketInterface {
	
	/***
	 *服务端当前连接数 
	 */
	public static int socketConnectionNum(){
		return SocketChannelDate.socketChannelMapSize();
	}
	
	/**
	 * 获取服务端当前所以的连接嵌套字
	 * */
	@SuppressWarnings("unchecked")
	public static List<String> remoteSocketAddressAll(){
		List<String> list=new ArrayList<String>();
		Iterator<?> iter = SocketChannelDate.socketChannelMapIterator();
		while (iter.hasNext()) {
			Map.Entry<String, Object> entry=(Map.Entry<String, Object>)iter.next();
			String key = entry.getKey();
			list.add(key);
			}
		return list;	
	}
	
	/**
	 * 获取服务端当前所有的连接信息
	 * */
	@SuppressWarnings("unchecked")
	public static List<LinkBase> remoteSocketLinkAll(){
		List<LinkBase> list=new ArrayList<LinkBase>();
		Iterator<?> iter = SocketChannelDate.socketChannelMapIterator();
		while (iter.hasNext()) {
				Map.Entry<String, Object> entry=(Map.Entry<String, Object>)iter.next();
				SocketChannelAgent socketChannelAgent= (SocketChannelAgent) entry.getValue();
				list.add(socketChannelAgent.getLinkBase());
			}
		return list;	
	}
	
	/**
	 * 获取服务端某个ip地址所有的连接信息
	 * */
	@SuppressWarnings("unchecked")
	public static List<LinkBase> remoteSocketLinkAll(String ip){
		Integer ipLinkNum =(Integer)SocketLikeCountData.socketLikeCountMapGet(ip); //查看当前ip的连接数量
		if(null==ipLinkNum||0==ipLinkNum){
			return null;
		}	
		List<LinkBase> list=new ArrayList<LinkBase>();
		Iterator<?> iter = SocketChannelDate.socketChannelMapIterator();
		while (iter.hasNext()) {
			Map.Entry<String, Object> entry=(Map.Entry<String, Object>)iter.next();
			SocketChannelAgent socketChannelAgent= (SocketChannelAgent) entry.getValue();
			if(socketChannelAgent.getLinkBase().getIp().equals(ip)){
				list.add(socketChannelAgent.getLinkBase());	
			}		
			}
		return list;	
	}
	
	/**
	 * 获取服务端某个嵌套字（ip+端口）地址的连接信息
	 * */
	public static LinkBase remoteSocketLinkAll(String ip,int port){
		String rddress="/"+ip+":"+port;
		SocketChannelAgent socketChannelAgent= (SocketChannelAgent) SocketChannelDate.socketChannelMapGet(rddress);
		if(socketChannelAgent!=null){
			return socketChannelAgent.getLinkBase();
		}
		return null;	
	}
	
	/**
	 * 获取某个ip地址的连接数
	 * ip地址
	 */
	public static int iPConnectionNum(String ip){
		return (Integer)SocketLikeCountData.socketLikeCountMapGet(ip);
	}
	
	/**
	 * 断开某个ip的所有连接
	 * Millisecond 断开毫秒数，当millisecond小于0时 永久断开
	 */
	public static Boolean socketClose(String ip,long millisecond){	
		List<String> list= remoteSocketAddressAll();
		if(list.size()!=0){
			for(String i:list){
				SocketChannelAgent socketChannelThread =(SocketChannelAgent)SocketChannelDate.socketChannelMapGet(i);
				if(socketChannelThread.getLinkBase().getIp().equals(ip)){
				try {
					socketChannelThread.close();
					SocketCloseData.socketCloseMapPut(ip, new SocketClose(ip,new Date(),millisecond));
				} catch (Exception e) {		
					e.printStackTrace();
					return false;
				}
				}
			}
			return true;
		}else{
			return null;
		}
	}
	
	/**
	 * 断开某个嵌套字（ip+端口）的连接
	 * Millisecond 断开毫秒数，当millisecond小于0时 永久断开
	 */
	public static boolean socketClose(String ip,int port,long millisecond){
		String rddress="/"+ip+":"+port;
		SocketChannelAgent socketChannelAgent =(SocketChannelAgent)SocketChannelDate.socketChannelMapGet(rddress);
		if(null!=socketChannelAgent){
		try {
			socketChannelAgent.close();
			SocketCloseData.socketCloseMapPut(rddress, new SocketClose(ip,port,new Date(),millisecond));
			return true;
		} catch (Exception e) {		
			e.printStackTrace();
			return false;
		}
		}
		return false;
	}
}
