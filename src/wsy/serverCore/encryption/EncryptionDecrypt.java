package wsy.serverCore.encryption;

import java.util.Random;

import it.sauronsoftware.base64.Base64;
import wsy.core.realizationInterface.DataEncryptionDecrypt;
import wsy.propertiesCofig.PropertiesCofig;

public class EncryptionDecrypt implements DataEncryptionDecrypt{

	@Override
	public String encryption(String str) {
		str =encryptionBase64(str);//base64加密
		if(!encryptionBoolean){
			return str;
		}
		
	     Random random=new Random();
			//随机生成0-9a-zA-Z三位混淆码	
			String confusionCode="";
			for(int i=0;i<len;i++){
				int e;
				e=random.nextInt(75)+48;
				if((e>57 && e<65) || (e>90 && e<97)){
					e+=10;
				}
			confusionCode+=(char)e;
			}
		
		//获得int数组，从大到小循环，如果字符串长度大于等于数组元素值，在字符串对应位置添加混淆码
		for(int i=encryption.length-1;i>-1;i--){
			if(str.length()>=encryption[i]){
				StringBuffer stringBuffer = new StringBuffer(str);
				str=stringBuffer.insert(encryption[i],confusionCode).toString();		
			}
		}		
		return str;
	}

	@Override
	public String decrypt(String str) {
		if(!encryptionBoolean){
			return str;
			//return decryptBase64(str);
		}
		//获得int数组，从小到大循环，如果字符串长度不大于等于数组元素值，break，在字符串对应位置删除3未混淆码
		for(int i=0;i<encryption.length;i++){
			if(str.length()>=encryption[i]){
			StringBuffer stringBuffer = new StringBuffer(str);
			str=stringBuffer.delete(encryption[i], encryption[i]+len).toString();
			}else{
				break;
			}
		}
		return decrypt(str);//base64解密
	}
	
	
	private static Boolean encryptionBoolean=PropertiesCofig.getEncryptionBoolean();
	private static int[] encryption=getEncryption(PropertiesCofig.getEncryptionKey());
	private static Integer len=PropertiesCofig.getLen();
	/**
	 * 校验数组（为了安全写死在程序里面）
	 */
	private static String[] check={"JHai","gF3i","8EkN","cI4I",
			"Kf70","LVrp","ehpI","LqGZ","5YVB","vtTD","9hfv",
			"MVq4","JHfB","sXQu","Byg8","jV0F","GXFa","RtBg",
			"Nbfg","ACAT","2jj4","VkJJ","9GFR","GD5s","EOg6",
			"6oYD","digo","iKef","FYJA","Mk7K","tOKe","qGjI"};

	

	/**
	 * 加密
	 * @param str
	 * @return
	 */
	public static String encryptionBase64(String str){  
		return Base64.encode(str,"utf-8");
	}
	/**
	 * 解密
	 * @param str
	 * @return
	 */
	public static String decryptBase64(String str){
		try {
			str= new String(Base64.decode(str,"utf-8"));  
		} catch (Exception e) {
			str=null;
		}
		return str;
	}
	
	/**
	 * 获取encryption加密密匙
	 * @return
	 */
	public static String getEncryption(){
		int[] encryption =new int[16];
		String encryptionStr ="";
		Random random=new Random();
		for (int i = 0; i < encryption.length; i++) {
			if(i==0){
				encryption[i]=5+random.nextInt(5);
			}else if(i<8){
				encryption[i]=encryption[i-1]+encryption[i-1]+random.nextInt(encryption[i-1]);
			}else if(i<13){
				encryption[i]=encryption[i-1]+encryption[i-1]/4+random.nextInt(encryption[i-1]);
			}else {
				encryption[i]=encryption[i-1]+encryption[i-1]/15+random.nextInt(encryption[i-1]);
			}
			if(i< encryption.length-1){
				encryptionStr+=encryption[i]+",";
			}else{
				encryptionStr+=encryption[i];
			}			
		}
		return encryptionStr;
	}	
	
	/**
	 * 字符串转数组
	 * @return
	 */
	public static int[] getEncryption(String encryptionStr){
		String[] elv=encryptionStr.split(",");
		int[] encryption=new int[elv.length];
		for (int i = 0; i < encryption.length; i++) {
			encryption[i]=Integer.parseInt(elv[i]);
		}
		return encryption;
	}
	
	/**
	 * 获取混淆码长度
	 * 获取1-9位奇数
	 * @return
	 */
	public static int getLen(){
		Random random=new Random();
	    int len=random.nextInt(10);
	    if(len%2==0){
	    	len++;
	    }  
		return len;
	}
	
	/**
	 * 获取校验数组
	 * @param len 单个效验码长度
	 * @param length 校验数组长度
	 * @return
	 */
	public static String getCheck(int len,int length){
		String check="";
		//String[] check=new String[32];
		for(int j=0;j<length;j++){
		   Random random=new Random();
			//随机生成0-9a-zA-Z三位混淆码	
			String confusionCode="";			

			for(int i=0;i<len;i++){
				int e;
				e=random.nextInt(75)+48;
				if((e>57 && e<65) || (e>90 && e<97)){
					e+=10;
				}
			confusionCode+=(char)e;
			}
			//check[j]=confusionCode;
			if(j==0){
				check=confusionCode;
			}else{
				check+=","+confusionCode;
			}
		}
		return check;
	}
	
	/**
	 * 数据校验
	 * @param message 
	 * @return
	 */
	@Override
	public  String dataVerification(String str){
			if(str==null||"".equals(str)){
				return null;
			}else if(str.length()<=128){
				return null;
			}
			//注意这个理校验长度128位
			str=str.substring(0, 128);
			String[] d=new String[32];
			int n=0;
			for(int k=0;k<str.length();k=k+4){	
				d[n]=str.substring(k, k+4);
				n++;
			}		
			for(int k=0;k<d.length;k++){
				boolean falg=false;
				for(int m=0;m<check.length;m++){
					if(d[k].equals(check[m])){
						falg=true;
						break;
					}
				}		
				if(!falg){
					return null;
				}		
			}
			return str.substring(128, str.length());
	}
	
	/**
	 * 生成校验头
	 * @return
	 */
	public static String check(){
		Random random=new Random();
		 String encryption ="";
		 int i=0;
		while(encryption.length()!=128){
			
			int b=random.nextInt(32);
			String key= check[b];
			String c="";
			if(i==0){
				c=key;
			}else{
				c=encryption+","+key;
			}
			if(c.length()<127){
				encryption=c;
			}else if(c.length()==128){
				encryption=c;
			}else{
				c=encryption;
			} 
			i++;
		}	
		return encryption;
	}

}
