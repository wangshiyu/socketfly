package wsy.serverCore.queue.redisBuffer;

import wsy.core.redis.JedisService;
import wsy.core.system.SystemConstant;
import wsy.propertiesCofig.PropertiesCofig;
import wsy.utils.LogAgent;

/** 
* @author black
* @version 创建时间：2016年9月9日 下午7:53:02 
* 高速缓存对象 ，接收的数据缓存，批量存入到redis
* 存入模式分为（Many ，Alone）
*/
public class HighSpeedBuffer extends Thread{
	private final static LogAgent log = new LogAgent(HighSpeedBuffer.class);//log 
	
	/**配置信息*/
	private static String redis_server_key=PropertiesCofig.getSocketServerKey();
	private static String storage_Mode_Key=PropertiesCofig.getRedisServerManyStorageModeKey();
	private static String storage_mode=PropertiesCofig.getRedisServerStorageMode();
	private static int mode_list_size=PropertiesCofig.getRedisServerManyStorageModeListSize();
	private static String speed_Storage_Mode=PropertiesCofig.getHighSpeedBufferStorageMode();
	private static Boolean checkDataIsFullIsOpen =PropertiesCofig.getCheckDataIsFullIsOpen();
	private static String checkDataIsFullKey=PropertiesCofig.getCheckDataIsFullKey();
	private Queue queue;
	public HighSpeedBuffer() {
		super();
		queue = new Queue();
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
			if (!isEmpty()) {
				if(SystemConstant.Many.equals(speed_Storage_Mode)&& SystemConstant.Many.equals(storage_mode)){
					DataBase dataBase = (DataBase) queue.get();
					try {
						redisSave(dataBase.getObject(),null);
						poll();// 去除头元素
					} catch (Exception e) {
						poll();
						e.printStackTrace();
					}
				}else if(SystemConstant.Alone.equals(speed_Storage_Mode)&& SystemConstant.Many.equals(storage_mode)){
					String num=JedisService.get(storage_Mode_Key);
					if(null ==num){
						num="0";
						JedisService.set(storage_Mode_Key,num);			
					}
					Long lean= JedisService.llen(redis_server_key+num);
					Object[] o=new Object[mode_list_size];
					if(lean>=mode_list_size){
						num=Long.valueOf(num)+1+"";
						JedisService.set(storage_Mode_Key,num);					
						for(int i=0;i<mode_list_size;i++){
							o[i]=poll();
							if(o[i]==null){//读到空数据跳出
								break;
							}
						}
					}else{				
						for(int i=0;i<mode_list_size-lean;i++){
							o[i]=poll();
							if(o[i]==null){//读到空数据跳出
								break;
							}
						}
					}				
					redisSave(o,num);
				}else{
					Queue q = null;
					synchronized (lock) {						
						if(queue.getListSize()!=0){
							q=queue;							
							queue = new Queue();
						}					
					}
					if(q!=null){
						redisSave((Object[])q.getList().toArray(),null);
					}				
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
	 * 存储到redis
	 * @param object
	 * @param key
	 */
	private void redisSave(Object[] object,String key){
		boolean fail =false;							
			if(SystemConstant.Many.equals(speed_Storage_Mode)&& SystemConstant.Many.equals(storage_mode)){
				String num=JedisService.get(storage_Mode_Key);
				if(num!=null){
						JedisService.set(storage_Mode_Key,(Long.valueOf(num)+1)+"");
						fail= JedisService.rpush(redis_server_key+(Long.valueOf(num)+1),object);//插入redis	
						if(checkDataIsFullIsOpen){
							JedisService.set(checkDataIsFullKey+(Long.valueOf(num)+1),"true");
						}				
				}else{
					JedisService.set(storage_Mode_Key,"0");
						fail= JedisService.rpush(redis_server_key+"0",object);//插入redis
						if(checkDataIsFullIsOpen){
							JedisService.set(checkDataIsFullKey+"0","true");
						}
				}		
			}else if(SystemConstant.Alone.equals(speed_Storage_Mode)&& SystemConstant.Many.equals(storage_mode)){
				fail= JedisService.rpush(redis_server_key+key,object);//插入redis
				
				if(checkDataIsFullIsOpen){//数据未存满
					long llen=JedisService.llen(redis_server_key+key);
					if(llen==mode_list_size){
						JedisService.set(checkDataIsFullKey+key,"true");
					}else{
						JedisService.set(checkDataIsFullKey+key,"false");
					}		
				}
			}else if(SystemConstant.Alone.equals(speed_Storage_Mode)
					||SystemConstant.Alone.equals(storage_mode)
					||SystemConstant.Many.equals(speed_Storage_Mode)
					||SystemConstant.Many.equals(storage_mode)){
				fail= JedisService.rpush(redis_server_key,object);//插入redis
			}else{
				throw new RuntimeException("redis存储方式或redis内存式高速缓存区存储方式配置出错");
			}						
		if(!fail){
			 log.info("数据插入redis服务器失败！");
		}		
	}

}