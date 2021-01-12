package wsy.core.bean;
/**
 * 白名单基础base
 * @author black
 *
 */
public class WhiteBase {
	/**
	 * ip
	 */
	public String ip;
	/**
	 *开始端口
	 */
	public int startPort;
	/**
	 *结束端口
	 */
	public int endPort;
	/**
	 * 最大连接数
	 */
	public int maxConnectionsNum;
	/**
	 * 允许连接时间
	 */
	public long allowConnectionsTime;
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public int getStartPort() {
		return startPort;
	}
	public void setStartPort(int startPort) {
		this.startPort = startPort;
	}
	public int getEndPort() {
		return endPort;
	}
	public void setEndPort(int endPort) {
		this.endPort = endPort;
	}
	public int getMaxConnectionsNum() {
		return maxConnectionsNum;
	}
	public void setMaxConnectionsNum(int maxConnectionsNum) {
		this.maxConnectionsNum = maxConnectionsNum;
	}
	public long getAllowConnectionsTime() {
		return allowConnectionsTime;
	}
	public void setAllowConnectionsTime(long allowConnectionsTime) {
		this.allowConnectionsTime = allowConnectionsTime;
	}
	

}
