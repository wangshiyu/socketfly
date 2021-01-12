package wsy.serverCore.queue.receiveQueue;

import java.util.List;

import wsy.core.bean.ReceiveJMessage;
import wsy.core.queue.Queue;
import wsy.core.realizationInterface.CommandExecute;
import wsy.propertiesCofig.PropertiesCofig;
import wsy.utils.LogAgent;

/**消息接收队列（用于服务器处理库户端发过来的信息）
 * @author black
 */

public class ReceiveQueue extends Thread{//这里注意初始化的顺序
	private final static LogAgent log = new LogAgent(ReceiveQueue.class);//log 
	private CommandExecute commandExecute=PropertiesCofig.getCommandServerExecute();
	private Queue queue;
	public ReceiveQueue() {
		super();
		queue = new Queue(PropertiesCofig.getReceiveQueueSize());
		log.info("服务端信息接收队列初始化完成！");
	}

	/**
	 * 锁，仅此而已
	 * 
	 * @see #run()
	 * @see #add(Object)
	 */
	private Object mutex = new Object();
	private Object lock = new Object();
	public boolean falg = false;

	/**
	 * 接收循环，它被作为thread runnable的run实现。
	 * 
	 * @see #add(Object)
	 * 
	 */
	@Override
	public void run() {
		while (!isClose()) {
			if (!isEmpty()&&PropertiesCofig.getCommandExecuteIsOpen()) {
				ReceiveJMessage receiveJMessage = (ReceiveJMessage) queue.get();
				try {
					if(null!=receiveJMessage.getCommandExecute()){
						receiveJMessage.getCommandExecute().execute(receiveJMessage);// 命令执行接口
						poll();// 去除头元素
					}else if(this.commandExecute!=null){
						this.commandExecute.execute(receiveJMessage);// 命令执行接口
						poll();// 去除头元素
					}else{
						throw new RuntimeException("请set一个命令处理器！");
					}
				} catch (Exception e) {
					poll();
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
		synchronized (lock) {
		queue.clear();
		queue = null;
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
		synchronized (lock) {
			queue.add(obj);
			synchronized (mutex) {
				mutex.notify();
			}
		}	
	}
	/**
	 * 出列
	 * @return
	 */
	public Object poll() {
		synchronized (lock) {
		return queue.poll();
		}
	}
	/**
	 * 返回队列里的全部元素，并清空
	 * @return
	 */
	public List<Object> getList(){
		synchronized (lock) {
	 	List<Object> list=queue.getList();
	 	queue.setList(new java.util.LinkedList<Object>());
		return list;
		}
	}

}