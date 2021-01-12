package wsy.serverCore.objectPool;

import wsy.utils.LogAgent;

/** 
* @author black
* @version 创建时间：2016年9月9日 上午9:39:28 
* 对象池
*/
public class ObjectPool {
	private final static LogAgent log = new LogAgent(ObjectPool.class);//log
	private Queue queue = new Queue();
	public ObjectPool(){
		super();
		log.info("代理池初始化完成!");
	}
	
	/**
	 * 锁，仅此而已
	 * 
	 * @see #run()
	 * @see #add(Object)
	 */
	private Object mutex = new Object();
	
	/**
	 * 添加对象数组
	 * @param obj
	 */
	public void add(Object[] objs) {
		synchronized (mutex) {
			for(Object obj:objs){
				queue.add(obj);
			}
		}
	}
	
	/**
	 * 添加对象数组
	 * @param obj
	 */
	public void add(Object obj) {
		synchronized (mutex) {
			queue.add(obj);
		}
	}
	/**
	 * 对象出列
	 * @return
	 */
	public Object poll() {
		synchronized (mutex) {
		return queue.poll();
		}
	}
	
	/**
	 * 对象池大小
	 * @return
	 */
	public int size() {
		return queue.getListSize();
	}
}
