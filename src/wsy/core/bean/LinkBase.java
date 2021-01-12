package wsy.core.bean;

import java.util.Date;

/** 
* @author black
* @version 创建时间：2016年8月30日 上午11:31:31 
* 连接实体
*/
public class LinkBase {
	/**
	 * ip
	 */
	private String ip;
	/**
	 * 端口
	 */
	private int port;
	/**
	 * 连接时间
	 */
	private Date linkDate =new Date();
	/**
	 * 上次活动时间
	 */
	private Date updateDate=new Date();
	/**
	 * 上次心跳时间
	 */
	private Date heartbeatTime=new Date();
	/**
	 * 发送数据次数
	 */
	private int sendNum;
	/**
	 * 发送数据长度B
	 */
	private long sendLength;
	/**
	 * 接收数据次数
	 */
	private int receiveNum;
	/**
	 * 接收数据长度B
	 */
	private long receiveLength;
	public LinkBase(){
		super();
	}
	
	public LinkBase(String ip,int port){
		super();
		this.ip=ip;
		this.port=port;
	}
	
	public LinkBase(String ip,int port,Date linkDate){
		super();
		this.ip=ip;
		this.port=port;
		this.linkDate=linkDate;	
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	public Date getLinkDate() {
		return linkDate;
	}
	public void setLinkDate(Date linkDate) {
		this.linkDate = linkDate;
	}
	public Date getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}
	public Date getHeartbeatTime() {
		return heartbeatTime;
	}

	public void setHeartbeatTime(Date heartbeatTime) {
		this.heartbeatTime = heartbeatTime;
	}

	public int getSendNum() {
		return sendNum;
	}
	public void setSendNum(int sendNum) {
		this.sendNum = sendNum;
	}
	public long getSendLength() {
		return sendLength;
	}
	public void setSendLength(long sendLength) {
		this.sendLength = sendLength;
	}
	public int getReceiveNum() {
		return receiveNum;
	}
	public void setReceiveNum(int receiveNum) {
		this.receiveNum = receiveNum;
	}
	public long getReceiveLength() {
		return receiveLength;
	}
	public void setReceiveLength(long receiveLength) {
		this.receiveLength = receiveLength;
	} 
	
	public void updateSend(long sendLength){
		this.updateDate=new Date();
		this.sendNum+=1;
		this.sendLength+=sendLength;
	}
	public void updateReceive(long receiveLength){
		this.updateDate=new Date();
		this.heartbeatTime=this.updateDate;
		this.receiveNum+=1;
		this.receiveLength+=receiveLength;
	}

	@Override
	public String toString() {
		return "LinkBase [ip=" + ip + ", port=" + port + ", linkDate=" + linkDate + ", updateDate=" + updateDate
				+ ", heartbeatTime=" + heartbeatTime + ", sendNum=" + sendNum + ", sendLength=" + sendLength
				+ ", receiveNum=" + receiveNum + ", receiveLength=" + receiveLength + "]";
	}
}
