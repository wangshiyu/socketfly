package wsy.serverCore.internalInterface;

public abstract class TimeRun {
	/**
	 * 时间标记
	 */
	public long falg;
	/**
	 * 执行间隔时间(豪秒)
	 */
	public long intervalTime;
	
	public TimeRun(long intervalTime){
		super();
		this.intervalTime=intervalTime;
	}
	/**
	 * 执行方法
	 */
	public abstract void executeFunction();

	/**
	 * 设置间隔时间
	 * @param intervalTime
	 */
	public void setIntervalTime(long intervalTime) {
		this.intervalTime = intervalTime;
	}
}
