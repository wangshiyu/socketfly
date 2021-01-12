package wsy.serverCore.server;
   
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.List;

import wsy.core.bean.BlackBase;
import wsy.core.bean.DelayConnection;
import wsy.core.bean.SocketClose;
import wsy.core.bean.WhiteBase;
import wsy.core.realizationInterface.CommandExecute;
import wsy.propertiesCofig.PropertiesCofig;
import wsy.serverCore.system.ServerSystemConstant;
import wsy.serverCore.system.SocketChannelDate;
import wsy.serverCore.system.SocketCloseData;
import wsy.serverCore.system.SocketLikeCountData;
import wsy.serverCore.timeRunTask.SocketCloseTask;
import wsy.utils.DateUtil;
import wsy.utils.LogAgent;
   /**
    * socket服务
    * @author black
    */
   public class SocketServerThread  extends Thread
   {
	 private final static LogAgent log = new LogAgent(SocketServerThread.class);//log
     private ServerSocket serverSocket ;			//socket服务	
     private CommandExecute commandExecute;			//命令执行器
     private int port;								//端口
     private int node=0;//1主节点 0服务节点
     
     public SocketServerThread(int port)  throws IOException
     {
       this.port = port;
       serverSocket=new ServerSocket(port);
     }
     
     public SocketServerThread(int port,int node)  throws IOException
     {
       this.port = port;
       this.node=node;
       serverSocket=new ServerSocket(port);   
     }
   
     public void run()
     {
    	 while(true){ 
       try
       {     
    	   Socket socket = serverSocket.accept();
    	   int size= SocketChannelDate.socketChannelMapSize();
    	   int agentObjectPoolSize =PropertiesCofig.getAgentObjectPoolSize();
    	   if(!PropertiesCofig.getAgentObjectPoolIsOpen()){
    		   agentObjectPoolSize=0;
    	   }
    	   /***延时连接*/
    	   if(PropertiesCofig.getDelayConnectionIsOpen()&&size>agentObjectPoolSize){
    		   for(DelayConnection delayConnection:SocketServerCore.getDelayConnectionS()){
    			   if(size<(agentObjectPoolSize+delayConnection.linkNum)){
    				   Thread.sleep(delayConnection.delayedTime);
    				   break;
    			   }
    		   }
    	   } 	   
    	  
    	   String address = socket.getRemoteSocketAddress().toString();
		   String ip= socket.getInetAddress().toString().replaceAll("/","");
		   int port =socket.getPort();		 

			   boolean allowLink=true;	
			   if(PropertiesCofig.getWhitelistBoolean()){//白名单
				   allowLink= whiteList(allowLink,ip,port,socket);
			   }
			   if(PropertiesCofig.getBlacklistBoolean()){//黑名单
				   allowLink= blackList(allowLink,ip,port,socket);
			   }
			   allowLink= socketClose(allowLink,ip,port,socket);//关闭的对象
			   allowLink= ipOpenNumLink(allowLink,ip,port,socket);//关闭的对象
			   allowLink= serverMaxLinkNum(allowLink,ip,port,socket);//服务端最大连接数
			   if(allowLink){
				   serverLink(address, socket);
			   }else{
				   socket.close();
			   }
		  
		   
       } catch (Exception ex) {
         ex.printStackTrace();
       }
      }
     }  
     
     /**
      * 链接客户端
      * 产生代理 
      * @param address
      * @param socket
      * @throws IOException
      */
     private void serverLink(String address,Socket socket) throws IOException{
    	 SocketChannelAgent socketChannelAgent=null;
    	 if(PropertiesCofig.getAgentObjectPoolIsOpen()){
    		 socketChannelAgent=(SocketChannelAgent) SocketServerCore.getAgentObjectPool().poll();
    		 if(null!=socketChannelAgent){
    			 socketChannelAgent.setSocket(socket);
    			 socketChannelAgent.getLinkBase().setIp(socket.getInetAddress().toString().replaceAll("/",""));
    			 socketChannelAgent.getLinkBase().setPort(socket.getPort());
    			 socketChannelAgent.setIn(new BufferedReader(new InputStreamReader(socket.getInputStream(), "utf-8")));
		       if(PropertiesCofig.getSocketSendIsOpen()){
		    	   socketChannelAgent.setOut(new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), "utf-8")));
		       }
		       socketChannelAgent.init();
    		 }else{
    			 socketChannelAgent =new SocketChannelAgent(socket,port);
    		 }
    	 }else{
    		 socketChannelAgent =new SocketChannelAgent(socket,port);
    	 }
 
    	 if(commandExecute!=null){
    		 socketChannelAgent.setCommandExecute(commandExecute);
    	 }
    	 String ip= socket.getInetAddress().toString().replaceAll("/","");
    	 SocketChannelDate.socketChannelMapPut(address, socketChannelAgent);
    	 ServerSystemConstant.socketLikeCountMapUpade(ip, 1);//更改当前ip连接数
    	 log.info("服务端端口"+port+"获得新链接，链接地址为："+socket.getRemoteSocketAddress());
		 log.info("当前代理socket代理个数为："+SocketChannelDate.socketChannelMapSize());
     }
     
     /**
      * 白名单
      */
     private boolean whiteList(boolean allowLink,String ip,int port,Socket socket) throws IOException{
    	 if(null ==PropertiesCofig.getWhiteList()){
    		 throw new RuntimeException("未set一个白名单实例!");
    	 }
    	 List<WhiteBase> list= PropertiesCofig.getWhiteList().getList();
    	 if(null!=list&&list.size()!=0){
    		 for(WhiteBase i:list){
    			 if(ip.equals(i.getIp())){
    				 if(i.getStartPort()!=0&&i.getEndPort()!=0){
 	    				if(port>=i.getStartPort()&&port<=i.getEndPort()){
 	    					return true;
 	    				}
 	    			}else if(i.getStartPort()!=0&&i.getEndPort()==0){
 	    				if(port>=i.getStartPort()){
 	    					return true;
 	    				}
 	    			}else if(i.getStartPort()==0&&i.getEndPort()!=0){
 	    				if(port<=i.getEndPort()){
 	    					return true;
 	    				}
 	    			}else{
 	    				return true;
 	    			}
    			 }	 
    		 }
    		 }
    	 log.info("ip地址:"+ip+"端口:"+port+"与白名单中的数据不符，系统主动断开当前连接!");
    	 return false; 			  
     }
     
     /**
      * 黑名单
      */
     private boolean blackList(boolean allowLink,String ip,int port,Socket socket) throws IOException{
    	 if(!allowLink){
    		 return false;
    	 }
    	 if(null ==PropertiesCofig.getBlackList()){
    		 throw new RuntimeException("未set一个白名单实例!");
    	 }
    	 List<BlackBase> list= PropertiesCofig.getBlackList().getList();
    	 if(null!=list&&list.size()!=0){
    		 for(BlackBase i:list){
    			 if(ip.equals(i.getIp())){
	    			if(i.getStartPort()!=0&&i.getEndPort()!=0){
	    				if(port>=i.getStartPort()&&port<=i.getEndPort()){
	    					log.info("ip地址:"+ip+"端口："+port+"与黑名单中的数据相符,系统主动断开该连接!");
	    					return false;
	    				}
	    			}else if(i.getStartPort()!=0&&i.getEndPort()==0){
	    				if(port>=i.getStartPort()){
	    					log.info("ip地址:"+ip+"端口："+port+"与黑名单中的数据相符,系统主动断开该连接!");
	    					return false;
	    				}
	    			}else if(i.getStartPort()==0&&i.getEndPort()!=0){
	    				if(port<=i.getEndPort()){
	    					log.info("ip地址:"+ip+"端口："+port+"与黑名单中的数据相符,系统主动断开该连接!");
	    					return false;
	    				}
	    			}else{
	    				log.info("ip地址:"+ip+"端口："+port+"与黑名单中的数据相符,系统主动断开该连接!");
	    				return false;
	    			}
    			 }   			 
    		 }
    	 } 
    	 return true; 			  
     }
     /**
      * 关闭的对象
      */
     private boolean socketClose(boolean allowLink,String ip,int port,Socket socket) throws IOException{
    	 if(!allowLink){
    		 return false;
    	 }
    	 /**
    	  * 防止任务里面操作同一个数据出问题
    	  */
    	 if(null!=SocketServerCore.getTimeRunExecute()&&null!=SocketServerCore.getTimeRunExecute().getTimeRun()&& SocketServerCore.getTimeRunExecute().getTimeRun() instanceof  SocketCloseTask){
    		 while(true){
    			 if(null==SocketServerCore.getTimeRunExecute()||null==SocketServerCore.getTimeRunExecute().getTimeRun()|| !(SocketServerCore.getTimeRunExecute().getTimeRun() instanceof  SocketCloseTask)){
    				System.err.println("当前执行了！");
    				 break; 
    			 } 
    		 }
    	 }
    	 
    	 Date newDate=new Date();
    	 SocketClose socketClose= (SocketClose)SocketCloseData.socketCloseMapGet(ip);
    	 if(null!=socketClose){		 
    		 if(socketClose.getMillisecond()<0){
    			 log.info("ip地址:"+ip+"被系统用户设定为永久断开,系统主动断开该连接!");	 
    			 return false;
    		 }else if((socketClose.getCloseTime().getTime()+socketClose.getMillisecond())>newDate.getTime()){
    			 long surplusMillisecond=socketClose.getMillisecond()-(newDate.getTime()-socketClose.getCloseTime().getTime());
    			 log.info("ip地址:"+ip+"被系统用户设定为断开连接"+DateUtil.formatDuring(socketClose.getMillisecond())+",系统主动断开该连接!请在"+DateUtil.formatDuring(surplusMillisecond)+"后重新连接");	
    			 return false;
    		 }
    		 SocketCloseData.socketCloseMapRemove(ip);//允许连接，清除该关闭信息
    	 }else{
    		 socketClose= (SocketClose)SocketCloseData.socketCloseMapGet(socket.getLocalSocketAddress().toString());
    		 if(null!=socketClose){
    			 if(socketClose.getMillisecond()<0){
        			 log.info("ip地址:"+ip+"被系统用户设定为永久断开,系统主动断开该连接!");	 
        			 return false;
        		 }else if((socketClose.getCloseTime().getTime()+socketClose.getMillisecond())>newDate.getTime()){
        			 long surplusMillisecond=socketClose.getMillisecond()-(newDate.getTime()-socketClose.getCloseTime().getTime());
        			 log.info("ip地址:"+ip+"端口:"+port+"被系统用户设定为断开连接"+DateUtil.formatDuring(socketClose.getMillisecond())+",系统主动断开该连接!请在"+DateUtil.formatDuring(surplusMillisecond)+"后重新连接");	
        			 return false;
        		 }
    			 SocketCloseData.socketCloseMapRemove(socket.getLocalSocketAddress().toString());//允许连接，清除该关闭信息
    		 }
    	 }    	 
    	 return true;
     }
     /**
      * 单个ip最大连接数量
     * @throws IOException 
      */
     private boolean ipOpenNumLink(boolean allowLink,String ip,int port,Socket socket) throws IOException{
    	 if(!allowLink){
    		 return false;
    	 }
    	 Integer ipCount= (Integer)SocketLikeCountData.socketLikeCountMapGet(ip);
    	 if(null==ipCount){
    		 ipCount=0;
    	 }
    	 if(ipCount>=PropertiesCofig.getIpOpenNumLink()){//一个ip最大允许链接个数
    		 log.info("ip地址:"+ip+"已经达到该ip最大链接数:"+PropertiesCofig.getIpOpenNumLink()+",系统主动断开该连接!");
    		 return false;
    	 }
    	 return true;
     }
     
     /**
      * 服务器最大连接数量
     * @throws IOException 
      */
     private boolean serverMaxLinkNum(boolean allowLink,String ip,int port,Socket socket) throws IOException{	  
    	 if(!allowLink){
    		 return false;
    	 }
    	 Integer ipCount= SocketChannelDate.socketChannelMapSize();
    	 if(ipCount>=PropertiesCofig.getServerMaxLinkNum()){//服务器最大连接数
    		 log.info("ip地址:"+ip+"端口:"+port+"请求连接,服务器达到最大链接数:"+PropertiesCofig.getServerMaxLinkNum()+",系统主动断开该连接!");
    		 return false;
    	 }
    	 return true;
     }

	public CommandExecute getCommandExecute() {
		return commandExecute;
	}

	public void setCommandExecute(CommandExecute commandExecute) {
		this.commandExecute = commandExecute;
	}
   }

  