package wsy.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import wsy.propertiesCofig.PropertiesCofig;
 
public class LogOutPut {
	private volatile static LogOutPut instance = null;
	private static String logPath = PropertiesCofig.getLogPath(); //日志存储地址
	
	private static SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
	private static SimpleDateFormat logFormat = new SimpleDateFormat("yyyy_MM_dd");
	private File log = null;
	private PrintWriter out = null;
	private String logFileName = null;
	
	private LogOutPut(){
		createLogFile();
		//log4j.info("独立日志存储服务初始化成功！");
	}
	
	public void outLog(String msg){
		createLogFile();
		out.print(timeFormat.format(new Date()) + "    ---->    " + msg+"\r\n");
		out.flush();
	} 
	
	public static LogOutPut getInstance(){
		if(null == instance){
			synchronized (LogOutPut.class) {
				if(null == instance){
					instance = new LogOutPut();
				}
			}
		}
		return instance;
	}
	
	private void createLogFile(){
		String genLogFileName = logFormat.format(new Date());
		if(genLogFileName.equals(logFileName)){
			return;
		}
		File dir = new File(logPath);
		if(!dir.exists())	dir.mkdirs();
		
		logFileName = genLogFileName;
		log = new File(logPath + logFileName + ".log");
		try{
			if(!log.exists())	log.createNewFile();
			if(out != null)		out.close();
			out = new PrintWriter(new OutputStreamWriter(new FileOutputStream(log, true), "UTF-8"), true);
		}catch(IOException e){
			e.printStackTrace();
		}
	}
}