package wsy.serverCore.businessbean;

import java.io.Serializable;
import java.util.Date;
/**
 * 数据传输	
 * @author black
 *
 */
public class DataTransmission implements Serializable {
	private static final long serialVersionUID = 1L;
	/**命令类型*/
	private Integer type;
	/**命令*/
	private CommandBase commandBase;	
	/**发送时间*/
	private Date sendDate=new Date() ;
	/**接收时间*/
	private Date receiveDate;
	/**message*/
	public String message;
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public CommandBase getCommandBase() {
		return commandBase;
	}
	public void setCommandBase(CommandBase commandBase) {
		this.commandBase = commandBase;
	}
	public Date getSendDate() {
		return sendDate;
	}
	public void setSendDate(Date sendDate) {
		this.sendDate = sendDate;
	}
	public Date getReceiveDate() {
		return receiveDate;
	}
	public void setReceiveDate(Date receiveDate) {
		this.receiveDate = receiveDate;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	@Override
	public String toString() {
		return "DataTransmission [type=" + type + ", commandBase=" + commandBase + ", sendDate=" + sendDate
				+ ", receiveDate=" + receiveDate + ", message=" + message + "]";
	}
}
