package wsy.core.bean;
/**
 * 发送消息实体
 * @author black
 *
 */
public class Message {
 private String message;
 Message(){	 
 }
 /**
  * 发送消息实体
  * @param message 信息
  */
	public Message(String message){
		 super();
		 this.message=message;
	} 
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
}
