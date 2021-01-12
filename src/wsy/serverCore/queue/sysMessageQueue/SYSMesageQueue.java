package wsy.serverCore.queue.sysMessageQueue;

import java.util.List;

import org.apache.log4j.Logger;

import wsy.propertiesCofig.PropertiesCofig;
/**
 * 系统消息队列是否打开
 * @author Administrator
 *
 */
public class SYSMesageQueue{
	public static Queue queue; 
	private final static Logger log = Logger.getLogger(SYSMesageQueue.class);
	public SYSMesageQueue(){
		queue= new Queue(PropertiesCofig.getSYSMesageQueueSize());
		log.info("系统消息队列初始化完成!");
	}

	/**
	 * 清空队列 清空实例
	 */
	public void close() {
		queue.clear();
		queue = null;
	}

	/**
	 * 判断是否关闭
	 * 
	 * @return
	 */
	public boolean isClose() {
		return queue == null;
	}

	/**
	 * 是否为空
	 * 
	 * @return
	 */
	public boolean isEmpty() {
		return queue != null && queue.isEmpty();
	}

	/**
	 * 消息加入排队机
	 * 
	 * @param obj
	 * @see #run()
	 */
	public synchronized void add(Object obj) {
		if(queue.getListSize()==PropertiesCofig.getSYSMesageQueueSize()){
			poll();
		}else{
			queue.add(obj);
		}		
	}

	/**
	 * 出列
	 * @return
	 */
	public synchronized Object poll() {
		return queue.poll();
	}
	/**
	 * 返回队列里的全部元素，并清空
	 * @return
	 */
	public synchronized List<Object> getList(){
	 	List<Object> list=queue.getList();
	 	queue.setList(new java.util.LinkedList<Object>());
		return list;
	}
}
