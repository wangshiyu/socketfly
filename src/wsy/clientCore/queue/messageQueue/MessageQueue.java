package wsy.clientCore.queue.messageQueue;

import org.apache.log4j.Logger;

import wsy.clientCore.send.Send;
import wsy.core.queue.Queue;
import wsy.propertiesCofig.PropertiesCofig;

/**
 * 客户端发送消息队列
 * 
 * @author black
 */
public class MessageQueue extends Thread {// 这里注意初始化的顺序
	private static Logger log=Logger.getLogger(MessageQueue.class);  
	private Queue queue = new Queue(PropertiesCofig.getMessageQueueSize());
	private Send send;

	public MessageQueue(Send send) {
		super();
		this.send = send;
		log.info("客户端消息发送队列初始化完成！");
	}

	/**
	 * 锁，仅此而已
	 * 
	 * @see #run()
	 * @see #add(Object)
	 */
	private Object mutex = new Object();
	private boolean isRunning = true;

	public void run() {
		
		while (!isClose() && isRunning) {
			if (!isEmpty()) {
				try {
					if(!PropertiesCofig.getSocketSendIsOpen()){
						throw new Exception("系统当前配置不允许发送数据");
					}
					send.sendOut((String) queue.get());
					poll();// 去除头元素
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				synchronized (mutex) {// 等~直到add方法的通知!
					try {
						mutex.wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	/**
	 * 清空队列 清空实例
	 */
	public void close() {
		queue.clear();
		queue = null;
	}
	
	/**
	 * 销毁队列
	 */
	public void destroy() {
		queue.clear();
		queue = null;
		isRunning = false;
       synchronized(mutex){
    	   mutex.notify();
		}
		
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
	public void add(Object obj) {

		queue.add(obj);
		synchronized (mutex) {// 每个代理都有一个队列，不需要同步
			mutex.notify();
		}
	}

	public Object poll() {
		return queue.poll();
	}

	public Send getSend() {
		return send;
	}

	public void setSend(Send send) {
		this.send = send;
	}
}