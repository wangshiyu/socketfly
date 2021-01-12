package wsy.clientCore;

import wsy.clientCore.client.SocketClientCore;

/** 
* @author black
* @version 创建时间：2016年9月7日 上午9:11:40 
* 类说明 
*/
public class test {
	public static void main(String[] args) {
		for(int i=0;i<1000;i++){
		SocketClientCore socketClientCore= new SocketClientCore("192.168.0.106",8888,1,"192.168.0.106",13527+i);
		try {
			socketClientCore.getSocketClientAgent().getSend().sendOut("123456789"+" i:"+i);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}
	}

}
