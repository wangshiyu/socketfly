package wsy.text;

import java.io.IOException;

import wsy.serverCore.externalInterface.QueueInterface;
import wsy.serverCore.externalInterface.SocketInterface;
import wsy.serverCore.externalInterface.SysInterface;
import wsy.serverCore.server.SocketServerCore;

/** 
* @author black
* @version 创建时间：2016年8月30日 下午4:11:30 
* 类说明 
*/
public class Server {

	public static void main(String[] args) throws InterruptedException, IOException {
		// TODO Auto-generated method stub
		SocketServerCore.getInstance();
		//new SocketServerCore(8888);
		
		/*Thread.sleep(60000 *3);
		Thread thread =new Thread(){
			public void run() {
			System.err.println("服务端当前连接数 "+SocketInterface.socketConnectionNum());
			System.err.println("当前队列数据数 "+SocketServerCore.getReceiveQueue().getList().size());
			//System.err.println("服务端当前连接数 "+SocketInterface.socketConnectionNum());
			//System.err.println("获取服务端当前所以的连接嵌套字"+SocketInterface.remoteSocketAddressAll().size());
			//System.err.println("获取服务端当前所有的连接信息"+SocketInterface.remoteSocketLinkAll().size());
			//System.err.println("获取服务端某个ip地址所有的连接信息:"+SocketInterface.remoteSocketLinkAll("192.168.3.186").size());
			//System.err.println("获取某个ip地址的连接数 ip地址:"+SocketInterface.iPConnectionNum("192.168.3.186"));
			//System.err.println("获取某个ip地址的连接数 ip地址"+SocketInterface.remoteSocketLinkAll("192.168.3.191",13527));
			//System.err.println("获取某个ip地址的连接数 ip地址"+SocketInterface.socketClose("192.168.3.191",-1));		
			}
		};
		thread.start();*/
	}

}
