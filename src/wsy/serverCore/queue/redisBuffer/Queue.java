package wsy.serverCore.queue.redisBuffer;

import java.util.List;
/**
 * 单独用户高速缓存
 * @author black
 *
 */
public class Queue {
	private List<Object> list;
	public Queue() {
		super();
		list = new java.util.LinkedList<Object>();
	}
	public Queue getQueue() {
		return this;
	}

	public void add(Object obj) {// 添加
			list.add(obj);
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