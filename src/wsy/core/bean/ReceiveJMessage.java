package wsy.core.bean;

import java.net.Socket;
import java.util.Date;

import wsy.core.realizationInterface.CommandExecute;
/**
 * 接收消息实体
 * @author black
 *
 */
public class ReceiveJMessage {
	private Socket socket;
	private String message;
	private CommandExecute commandExecute;//命令执行器
	private Date data=new Date();
	public ReceiveJMessage(Socket socket,String message){
		this.socket =socket;
		this.message=message;
	}
	public ReceiveJMessage(Socket socket,String message,CommandExecute commandExecute){
		this.socket =socket;
		this.message=message;
		this.commandExecute=commandExecute;
	}
	public Socket getSocket() {
		return socket;
	}
	public void setSocket(Socket socket) {
		this.socket = socket;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Date getData() {
		return data;
	}
	public void setData(Date data) {
		this.data = data;
	}	
	public CommandExecute getCommandExecute() {
		return commandExecute;
	}
	public void setCommandExecute(CommandExecute commandExecute) {
		this.commandExecute = commandExecute;
	}
	@Override
	public String toString() {
		return "ReceiveJMessage [socket=" + socket + ", message=" + message + ", data=" + data + "]";
	}
	
}
