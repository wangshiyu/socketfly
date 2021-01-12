package wsy.utils;

import com.alibaba.fastjson.JSON;

public class JsonUtil {

	public static String getJson(Object obj){
		return JSON.toJSONString(obj);
	}
	
	public static <T> T  getBean(String str,Class<T> clazz){
		return  (T) JSON.parseObject(str, clazz); 
	}
}
