package wsy.utils;

import org.apache.log4j.Logger;

import wsy.propertiesCofig.PropertiesCofig;
import wsy.serverCore.server.SocketServerCore;

public class LogAgent {
	private Logger log ;
	public <T> LogAgent(Class<T> clazz){
	super();	
		log=Logger.getLogger(clazz);
	}
	public void info(Object message){
		if(PropertiesCofig.getLogIsOpen()){
		   log.info(message);
		}
	    if(PropertiesCofig.getSYSMesageQueueIsOpen()){
	 	   SocketServerCore.getSYSMesageQueue().add(message);
	    }
	}
	
	public void debug(Object message){
		log.debug(message);
		if(PropertiesCofig.getLogIsOpen()){
			log.debug(message);
		}
		if(PropertiesCofig.getSYSMesageQueueIsOpen()){
			SocketServerCore.getSYSMesageQueue().add(message);
		}
	}
}
