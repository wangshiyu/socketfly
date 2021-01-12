package wsy.core.realizationInterface;

import wsy.core.bean.ReceiveJMessage;
/**
 * 命令执行器
 * @author Administrator
 *
 */
public interface CommandExecute {

	public void execute(ReceiveJMessage receiveJMessage);
}
