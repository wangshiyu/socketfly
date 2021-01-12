package wsy.utils;

import java.util.List;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class SerializeUtil {

	/**
	 * @function 序列化
	 * @date 2014-12-19 下午3:00:50
	 * @author v_lianghua
	 * @param object
	 * @return
	 * @return byte[]
	 */
	public static String serialize(Object object) {
		return JSON.toJSONString(object, SerializerFeature.WriteClassName,
				SerializerFeature.WriteNullNumberAsZero,
				SerializerFeature.WriteNullStringAsEmpty,
				SerializerFeature.WriteNullBooleanAsFalse);
	}

	/**
	 * @function 反序列化对象
	 * @date 2014-12-19 下午3:00:38
	 * @author v_lianghua
	 * @param bytes
	 * @return
	 * @return Object
	 */
	public static Object unserialize(String serializeString) {
		return JSON.parse(serializeString);
	}

	/**
	 * @function 反序列化单个对象
	 * @date 2014-12-19 下午3:27:51
	 * @author v_lianghua
	 * @param serializeString
	 * @param clazz
	 * @return
	 * @return T
	 */
	public static <T> T unserializeObject(String serializeString, Class<T> clazz) {
		return JSONObject.parseObject(serializeString, clazz);
	}

	/**
	 * @function 反序列化数组
	 * @date 2014-12-19 下午3:46:09
	 * @author v_lianghua
	 * @param serializeString
	 * @param elementType
	 * @return
	 * @return List<T>
	 */
	public static <T> List<T> unserializeList(String serializeString,
			Class<T> elementType) {
		return JSONArray.parseArray(serializeString, elementType);
	}

}
