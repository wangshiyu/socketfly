package wsy.core.queue;

import java.util.List;

import wsy.propertiesCofig.PropertiesCofig;
import wsy.serverCore.system.ServerSystemConstant;
import wsy.utils.LogAgent;

public class Queue {
	private final static LogAgent log = new LogAgent(Queue.class);//log
	private List<Object> list;
	private int SIZE = 10;//大小
	private int threshold=SIZE*PropertiesCofig.getThresholdRate();//阀值 

	public Queue() {
		super();
		list = new java.util.LinkedList<Object>();
	}

	public Queue(int size) {
		super();
		list = new java.util.LinkedList<Object>();
		this.SIZE = size;
		this.threshold = size*PropertiesCofig.getThresholdRate();
	}

	public Queue getQueue() {
		return this;
	}

	public boolean isFull() { // 充满
		return list.size() >= SIZE;
	}

	public void add(Object obj) {// 添加
		if(threshold>list.size()){
			list.add(obj);
		}else{
		 String securityQueue=	PropertiesCofig.getSecurityQueue();
		 if(ServerSystemConstant.poll_access.equals(securityQueue)){
			 this.poll();
			 this.list.add(obj);
			 log.info("队列数值超出阀值："+threshold+"自动进行安全队列操作：出列存储");
		 }else if(ServerSystemConstant.clear.equals(securityQueue)){
			 this.clear();
			 log.info("队列数值超出阀值："+threshold+"自动进行安全队列操作：清空队列");
		 }else if(ServerSystemConstant.stop_access.equals(securityQueue)){
			 log.info("队列数值超出阀值："+threshold+"自动进行安全队列操作：停止入列");
		 }else if(ServerSystemConstant.error.equals(securityQueue)){
			 log.info("队列数值超出阀值："+threshold+"自动进行安全队列操作：抛出异常");
			 throw new RuntimeException("超出阀值！");		
		 }else{
			 throw new RuntimeException("无对应配置！");
		 }
		}
	}

	public Object poll() {// 出列
		if(list.size()!=0){
			return (Object) list.remove(0);
		}else{
			return null;
		}	
	}

	public boolean isEmpty() {// 栈空
		return list.isEmpty();
	}

	public void clear() {// 清空
		list.clear();
	}

	public Object get() {
		if(list.size()!=0){
			return (Object) list.get(0);
		}else{
			return null;
		}	
	}

	public int getListSize() {
		return list.size();
	}

	public List<Object> getList() {
		return list;
	}

	public void setList(List<Object> list) {
		this.list = list;
	}
}