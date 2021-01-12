package wsy.core.bean;
/** 
* @author black
* @version 创建时间：2016年9月9日 下午4:53:46 
* 延时连接基础
*/
public class DelayConnection {
	/**
	 * 连接数
	 */
	public int linkNum;
	/**
	 * 延时时间
	 */
	public long delayedTime;
	public DelayConnection(){
		
	}
	
	public DelayConnection(int linkNum,long delayedTime){
		super();
		this.linkNum=linkNum;
		this.delayedTime=delayedTime;
	}
}
