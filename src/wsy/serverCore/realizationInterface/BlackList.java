package wsy.serverCore.realizationInterface;

import java.util.List;

import wsy.core.bean.BlackBase;

/**
 * 黑名单
 * @author black
 *
 */
public interface BlackList {
	/**
	 * 获取白名单
	 * @return
	 */
	public List<BlackBase> getList();
	/**
	 * 保存修改黑名单
	 */
	public boolean saveOrUpdate(BlackBase blackBase);
}
