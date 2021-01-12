package wsy.utils;

public class DateUtil {
	public static String formatDuring(long mss) {
        long days = mss / (1000 * 60 * 60 * 24);
        long hours = (mss % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
        long minutes = (mss % (1000 * 60 * 60 * 24) % (1000 * 60 * 60)) / (1000 * 60);
        long seconds = (mss % (1000 * 60 * 60 * 24) % (1000 * 60 * 60)% (1000 * 60)) / 1000;
        long millisecond =(mss % (1000 * 60 * 60 * 24) % (1000 * 60 * 60)% (1000 * 60)) % 1000 ;
        String date="";
        if(days!=0){
        	date+=days+"天";
        }
        if(hours!=0){
        	date+=hours+"时";
        }
        if(minutes!=0){
        	date+=minutes+"分";
        }
        if(seconds!=0){
        	date+=seconds+"秒";
        }
        if(millisecond!=0){
        	date+=millisecond+"毫秒";
        }      
        return date;
    }
}
