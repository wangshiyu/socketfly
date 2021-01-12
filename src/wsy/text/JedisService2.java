package wsy.text;

import java.util.List;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import wsy.propertiesCofig.PropertiesCofig;
import wsy.utils.LogAgent;

/**
 * 功能模块：Jedis list公共类
 ********************************* 
 * @author v_black
 * @date 2016-8-25 
 * @version 1.0.0
 ********************************* 
 */
public class JedisService2 {
	private final static LogAgent logger = new LogAgent(JedisService2.class);//log 
	private static JedisPool jedisPool = null;// 连接池
	private volatile static String[] listkey=new String[5];
	private volatile static String lock=null;
	private volatile static String storageModeKey=null;
	static {
		if(!PropertiesCofig.getRedisIsOpen()){
			throw new RuntimeException("redis未打开！");
		}
		JedisPoolConfig config = new JedisPoolConfig();
		// 设置最大连接数
		config.setMaxActive(PropertiesCofig.getRedisMaxActive());
		// 设置最大空闲数
		config.setMaxIdle(PropertiesCofig.getRedisMaxIdle());
		// 设置初始化数
		config.setMinIdle(PropertiesCofig.getRedisMinIdle());
		// 设置超时时间
		config.setMaxWait(PropertiesCofig.getRedisMaxWait());
		// 提前进行validate操作，确保获取的jedis实例均是可用
		config.setTestOnBorrow(PropertiesCofig.getRedisTestOnBorrow());
		// redis设置了密码
		jedisPool = new JedisPool(config, PropertiesCofig.getRedisIp(), PropertiesCofig.getRedisPort(), 120000,PropertiesCofig.getRedisPassword(),15);
	}



	/**
	 * 向list头插入一个或者多个对象
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public static boolean lpush(String key, String... values) {
		while (null!=lock&&listkey(key)) {}//阻塞
		setListkey(key);
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			jedis.lpush(key, values);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			logger.debug(e.getMessage());
			return false;
		} finally {
			setListkeyNull(key);
			jedisPool.returnResource(jedis);
		}	

	}	
	
	/**
	 * 向已存在list头插入对象
	 * 
	 * @param key
	 * @param value	
	 * @return
	 */
	public static boolean lpushx(String key, String value) {
		while (null!=lock&&listkey(key)) {}//阻塞
		setListkey(key);
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			jedis.lpushx(key, value);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			logger.debug(e.getMessage());
			return false;
		} finally {
			setListkeyNull(key);
			jedisPool.returnResource(jedis);
		}
	}
	
	/**
	 * 向list尾插入一个或者多个对象
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public static boolean rpush(String key, String... values) {
		while (null!=lock&&listkey(key)) {}//阻塞
		setListkey(key);
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			jedis.rpush(key, values);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			logger.debug(e.getMessage());
			return false;
		} finally {
			setListkeyNull(key);
			jedisPool.returnResource(jedis);
		}
	}
	
	/**
	 * 向list尾插入一个或者多个对象
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public static boolean rpush(String key, Object[] values) {
		while (null!=lock&&listkey(key)) {}//阻塞
		setListkey(key);
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			for(Object object:values){
				if(object!=null){
					jedis.rpush(key,(String)object);
				}else{
					break;
				}		
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			logger.debug(e.getMessage());
			return false;
		} finally {
			setListkeyNull(key);
			jedisPool.returnResource(jedis);
		}
	}
	
	/**
	 * 向已存在list尾插入对象
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public static boolean rpushx(String key, String value) {
		while (null!=lock&&listkey(key)) {}//阻塞
		setListkey(key);
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			jedis.rpushx(key, value);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			logger.debug(e.getMessage());
			return false;
		} finally {
			setListkeyNull(key);
			jedisPool.returnResource(jedis);	
		}
	}
	/**
	 * 出列list指定范内所有数据
	 * 
	 * @param key
	 * @param start
	 * @param end
	 * 第一个是key，第二个是起始位置，第三个是结束位置，jedis.llen获取长度 -1表示最后一个元素-2表示倒数第二个元素
	 * @return
	 */
	public static List<String> lrange(String key) {
		while (null!=lock&&listkey(key)) {}//阻塞
		lock="lock";
		setListkey(key);
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			List<String> values = jedis.lrange(key,0,-1);
			jedis.del(key);
			return values;
		} catch (Exception e) {
			e.printStackTrace();
			logger.debug(e.getMessage());
			return null;
		} finally {
			lock=null;
			setListkeyNull(key);
			jedisPool.returnResource(jedis);
		}

	}
	/**
	 * 获取list指定范围内的元素
	 * 
	 * @param key
	 * @param start
	 * @param end
	 * 第一个是key，第二个是起始位置，第三个是结束位置，jedis.llen获取长度 -1表示最后一个元素-2表示倒数第二个元素
	 * @return
	 */
	public static List<String> lrange(String key,long start,long end) {
		while (null!=lock&&listkey(key)) {}//阻塞
		setListkey(key);
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			List<String> values = jedis.lrange(key,start,end);
			return values;
		} catch (Exception e) {
			e.printStackTrace();
			logger.debug(e.getMessage());
			return null;
		} finally {
			setListkeyNull(key);
			jedisPool.returnResource(jedis);
		}
	}
 
	
	/**
	 * 移除并返回列表的第一个元素
	 * 
	 * @param key
	 * @return
	 */
	public static String lpop(String key) {
		while (null!=lock&&listkey(key)) {}//阻塞
		setListkey(key);
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			String value = jedis.lpop(key);
			return value;
		} catch (Exception e) {
			e.printStackTrace();
			logger.debug(e.getMessage());
			return null;
		} finally {
			setListkeyNull(key);
			jedisPool.returnResource(jedis);
		}
	}
	
	/**
	 * 移除并返回列表的最后一个元素
	 * 
	 * @param key
	 * @return
	 */
	public static String rpop(String key) {
		while (null!=lock&&listkey(key)) {}//阻塞
		setListkey(key);
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			String value = jedis.rpop(key);
			return value;
		} catch (Exception e) {
			e.printStackTrace();
			logger.debug(e.getMessage());
			return null;
		} finally {
			setListkeyNull(key);
			jedisPool.returnResource(jedis);
		}
	}
	
	/**
	 * 通过key和索引获取元素
	 * 
	 * @param key
	 * @param index
	 * @return
	 */
	public static String lindex(String key,long index) {
		while (null!=lock&&listkey(key)) {}//阻塞
		setListkey(key);
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			String value = jedis.lindex(key,index);
			return value;
		} catch (Exception e) {
			e.printStackTrace();
			logger.debug(e.getMessage());
			return null;
		} finally {
			setListkeyNull(key);
			jedisPool.returnResource(jedis);
		}
	}
	

	/**
	 * 获取list长度
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public static long llen(String key) {
		//while (null!=lock&&listkey(key)) {}//阻塞
		//setListkey(key);
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			return	jedis.llen(key);
		} catch (Exception e) {
			e.printStackTrace();
			logger.debug(e.getMessage());
			return -1;
		} finally {
			//setListkeyNull(key);
			jedisPool.returnResource(jedis);
		}
	}
	
	/**
	 * 删除list中的对象，根据key
	 * 
	 * @param key
	 * @return
	 */
	public static boolean del(String key) {
		while (null!=lock&&listkey(key)) {}//阻塞
		lock="lock";
		setListkey(key);
		Jedis jedis = null;
		try {		
			jedis = jedisPool.getResource();
			jedis.del(key);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			logger.debug(e.getMessage());
			return false;
		} finally {
			lock=null;
			setListkeyNull(key);
			jedisPool.returnResource(jedis);
		}
	}
	
	
	/**
	 * 向缓存中设置对象
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public static boolean set(String key, String value) {
		while (null!=storageModeKey&&storageModeKey.equals(key)) {}//阻塞
		storageModeKey=key;
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			jedis.set(key,value);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			logger.debug(e.getMessage());
			return false;
		} finally {
			storageModeKey=null;
			jedisPool.returnResource(jedis);
		}
	}
	
	/**
	 * 根据key 获取内容
	 * 
	 * @param key
	 * @return
	 */
	public static String get(String key) {
		while (null!=storageModeKey&&storageModeKey.equals(key)) {}//阻塞
		storageModeKey=key;
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			String value = jedis.get(key);
			return value;
		} catch (Exception e) {
			e.printStackTrace();
			logger.debug(e.getMessage());
			return null;
		} finally {
			storageModeKey=null;
			jedisPool.returnResource(jedis);
		}
	}
	
	
	/**阻塞操作（防止操作同一个key的链表）*/
	public static boolean listkey(String key){//根据key判读是否有操作同一个key
		for(String str:listkey){
			if(str!=null&&str.equals(key)){
				return true;
			}
		}
		return false;
	}
	
	public static void setListkey(String key){//操作的key存储到listkey
		if(null==lock){
			return;
		}
		boolean falg=true;
		for(String str:listkey){
			if(str!=null&&str.equals(key)){
				falg=false;
			}
		}
		if(falg){
			while(true){
				for(String str:listkey){
					if(null==str){
						str=key;
						return;
					}
				}	
			}
		}
	}
	
	public static void setListkeyNull(String key){//操作的key设置为空
		for(String str:listkey){
			if(str!=null&&str.equals(key)){
				str=null;
			}
		}
	}
	/**阻塞操作（防止操作同一个key的链表）*/
}
