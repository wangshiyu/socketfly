package wsy.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Iterator;
import java.util.Properties;

/**
 * Created by wangankang on 16/8/28.
 * 该类用于将Properties文件中的参数,自动的设置到类同名字段中,前提是字段为静态的.
 */
public class PropertiesAutoSerialize implements Serializable{
	private static final long serialVersionUID = 1L;

	/**
     * 待反序列化的类
     */
    private Class<?> clazz;

    /**
     * 待反序列化的Properties文件输入流
     */
    private InputStream propertiesFile;

    /**
     * Properties操作对象
     */
    private Properties p = new Properties();

    /**
     * 私有的构造方法
     * 用于获取Properties文件流和设置待转对象
     * @param path 用于指定Properties文件路径,例如"/config.properties"
     * @param clazz 待反序列化的类
     */
    private PropertiesAutoSerialize(String path, Class<?> clazz){
        this.propertiesFile = PropertiesAutoSerialize.class.getResourceAsStream(path);
        this.clazz = clazz;
        try {
            p.load(propertiesFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取指定key的value值
     * @param key 用于指定获取Properties中的key
     * @return 用于返回Properties中指定key的value
     */
    private String readProperty(String key){
        if(null == key || "".equals(key.trim())) return null;
        return p.getProperty(key);
    }

    /**
     * PropertiesAutoSerialize.init("/config.properties",Const.class);
     * @param path properties路径名
     * @param clazz 需要反序列化的类
     * @throws Exception 
     */
    public static void init(String path, Class<?> clazz) throws Exception{
        new PropertiesAutoSerialize(path, clazz).serializeProperties();
    }

    /**
     * 转换实现原理:
     * 获取Properties中所有的key,并遍历
     * 获取该key对应的value，如果value为空字符串，则跳过
     * 去取待转类中的同名字段,如果没有则跳过
     * 判断这个字段是否是静态字段,如果不是则跳过
     * 判断这个字段是否是final,如果是则跳过
     * 设置该字段为可见
     * 获取Properties中指定的value并trim
     * 执行setField方法,对指定的字段进行设置值
     * @throws Exception 
     */
    private void serializeProperties() throws Exception{
        try{
            Iterator<?> iterator = p.keySet().iterator();
            while (iterator.hasNext()){
                Object obj = iterator.next();
                if(! (obj instanceof String)){
                    continue;
                }
                String key = ((String) obj).trim();
                String value = readProperty(key).trim();
                if(null!=value && "".equals(value)){
                    continue;
                }
                Field field = null;
                if(null == (field = getField(key))){
                    continue;
                }
                if(!Modifier.isStatic(field.getModifiers())){
                    continue;
                }
                if(Modifier.isFinal(field.getModifiers())){
                    continue;
                }
                field.setAccessible(true);
                this.setField(field,value);
            }
        } finally {
            try {
                if(null != propertiesFile)
                    propertiesFile.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 通过反射获取待转类clazz中指定字段名的字段,如果字段不存在则返回null
     * @param fieldName 去查找待转类中的指定字段
     * @return 返回指定的字段
     */
    private Field getField(String fieldName){
        try{
            return clazz.getDeclaredField(fieldName);
        }
        catch(Exception e){}
        return null;
    }

    /**
     * 对指定的字段进行设置值,目前仅支持字段类型:
     * String,boolean,byte,char,short,int,long,float,double
     * @param field 指定的字段
     * @param value 设置值
     * @throws Exception 
     */
    private void setField(Field field,String value) throws Exception{
        Class<?> type = field.getType();
        Object par = null;
        try {
            if(String.class.equals(field.getType())){
                par = value;
            }else if(int.class.equals(type) || Integer.class.equals(type)) {
                par =  Integer.valueOf(value);
            }else if(boolean.class.equals(type) || Boolean.class.equals(type)){
                par =  Boolean.valueOf(value);
            }else if(long.class.equals(type) || Long.class.equals(type)){
                par = Long.valueOf(value);
            }else if(double.class.equals(type) || Double.class.equals(type)){
                par = Double.valueOf(value);
            }else if(float.class.equals(type) || Float.class.equals(type)){
                par = Float.valueOf(value);
            }else if(short.class.equals(type) || Short.class.equals(type)){
                par = Short.valueOf(value);
            }else if(byte.class.equals(type) || Byte.class.equals(type)){
                par = Byte.valueOf(value);
            }else if(char.class.equals(type)){
                par = value.charAt(0);
            }
            if(null != par){
                field.set(null, par);
            }else{
               throw new Exception("Properties转换异常:Class(字段类型不是'八大基本类型和String'):" + clazz.getName() + ",字段名:" + field.getName() + ",字段类型:" + field.getType() + ",value:" + value);
            }
        } catch (IllegalAccessException e) {
        	  throw new Exception("Properties转换异常:Class(IllegalAccessException):" + clazz.getName() + ",字段名:" + field.getName() + ",字段类型:" + field.getType() + ",value:" + value);
        }
    }

}

