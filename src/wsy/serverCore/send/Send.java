package wsy.serverCore.send;

import java.net.Socket;

import wsy.propertiesCofig.PropertiesCofig;
import wsy.serverCore.server.SocketChannelAgent;
import wsy.serverCore.system.ServerSystemConstant;
import wsy.serverCore.system.SocketChannelDate;
import wsy.utils.LogAgent;

/**
 * 发送消息
 * @author black
 *	
 */
public class Send {
	private final static LogAgent log = new LogAgent(Send.class);//log 
	private SocketChannelAgent socketChannelAgent;
	public Send(SocketChannelAgent socketChannelAgent){
		super();
		this.socketChannelAgent=socketChannelAgent;
	}
	
	/**
	 * 发送信息，使用同步
	 * @param message
	 * @return
	 * @throws Exception
	 */
	public synchronized boolean sendOut(String message) throws Exception{	     
       try {
    	   socketChannelAgent.getOut().print(message+"\r\n");
    	   socketChannelAgent.getOut().flush();
		} catch (Exception e) {	
			Socket socket=socketChannelAgent.getSocket();
			String ip=this.socketChannelAgent.getLinkBase().getIp();
			int port =this.socketChannelAgent.getLinkBase().getPort();
			String remoteAddress="/"+ip+":"+port;
			if(null !=this.socketChannelAgent&&null !=this.socketChannelAgent.getReceive()){
				socketChannelAgent.getReceive().interrupt();//销毁信息接收
			}
			if(PropertiesCofig.getMessageQueueIsOpen()){
				this.socketChannelAgent.getMessageQueue().destroy();//销毁队列线程
			}
			if(!socket.isClosed()){
				socket.close();//关闭SocketChannel
				log.info(remoteAddress+"连接已经关闭！");
			}
			if(null!=this.socketChannelAgent && null!=this.socketChannelAgent.getIn()) this.socketChannelAgent.getIn().close();
			if(null!=this.socketChannelAgent && null!=this.socketChannelAgent.getOut()) this.socketChannelAgent.getOut().close();
			SocketChannelDate.socketChannelMapRemove(remoteAddress);
			ServerSystemConstant.socketLikeCountMapUpade(ip, -1);//更改当前ip连接数
		}
       return true;
	}

	public SocketChannelAgent getSocketChannelAgent() {
		return socketChannelAgent;
	}

	public void setSocketChannelAgent(SocketChannelAgent socketChannelAgent) {
		this.socketChannelAgent = socketChannelAgent;
	}

	
}
