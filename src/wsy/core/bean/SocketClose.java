package wsy.core.bean;

import java.util.Date;
/**
 * 关闭的socket对象
 * @author black
 *
 */
public class SocketClose {
	/**
	 * 断开毫秒数
	 * 当millisecond小于0时 永久断开
	 */
	private long millisecond; 
	/**
	 * 断开时刻
	 */
	private Date closeTime=new Date();
	/**
	 * 断开ip
	 */
	private String ip;	
	/**
	 * 断开端口
	 */
	private int port;
	
	/**
	 * 根据ip地址断开
	 * @param ip
	 * @param closeTime
	 * @param millisecond
	 */
	public SocketClose(String ip,Date closeTime,long millisecond){
		super();
		this.ip=ip;
		this.closeTime=closeTime;
		this.millisecond=millisecond;
	}
	/**
	 * 根据ip和端口断开
	 * @param ip
	 * @param closeTime
	 * @param millisecond
	 */
	public SocketClose(String ip,int port,Date closeTime,long millisecond){
		super();
		this.ip=ip;
		this.port=port;
		this.closeTime=closeTime;
		this.millisecond=millisecond;
	}

	public long getMillisecond() {
		return millisecond;
	}

	public void setMillisecond(long millisecond) {
		this.millisecond = millisecond;
	}

	public Date getCloseTime() {
		return closeTime;
	}

	public void setCloseTime(Date closeTime) {
		this.closeTime = closeTime;
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
}
