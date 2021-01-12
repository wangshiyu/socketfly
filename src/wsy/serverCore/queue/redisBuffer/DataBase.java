package wsy.serverCore.queue.redisBuffer;

import java.util.Arrays;

/** 
* @author black
* @version 创建时间：2016年9月9日 下午7:53:02 
* 数据存储节点 （收到的数据批量存入节点当中，高速缓存批量存储到redis）
*/
public class DataBase {
	/**
	 * 数据
	 */
	private Object[] object;
	/**
	 * 数据个数
	 */
	private int size=0;
	/**
	 * key
	 */
	private String key;
	public DataBase(int length){
		object=new Object[length];
	}
	
	public int add(Object object){
		this.object[size]=object;
		size=size+1;
		return size;
	}
	
	
	public Object[] getObject() {
		return object;
	}
	public void setObject(Object[] object) {
		this.object = object;
	}
	public int getSize() {
		return size;
	}
	public void setSize(int size) {
		this.size = size;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}

	@Override
	public String toString() {
		return "DataBase [object=" + Arrays.toString(object) + ", size=" + size + ", key=" + key + "]";
	}
	

}
