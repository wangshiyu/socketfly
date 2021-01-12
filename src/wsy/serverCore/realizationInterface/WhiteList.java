package wsy.serverCore.realizationInterface;

import java.util.List;

import wsy.core.bean.WhiteBase;

/**
 * 白名单
 * @author black
 *
 */
public interface WhiteList {
	/**
	 * 获取白名单
	 * @return
	 */
	public List<WhiteBase> getList();

}
