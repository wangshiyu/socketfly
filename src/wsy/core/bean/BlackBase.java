package wsy.core.bean;
/**
 * 黑名单基础base
 * @author black
 *
 */
public class BlackBase {
	/**
	 * ip地址
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
}
