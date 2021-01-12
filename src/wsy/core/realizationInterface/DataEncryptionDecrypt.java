package wsy.core.realizationInterface;

/**
 * 加密解密
 * @author black
 *
 */
public interface DataEncryptionDecrypt {
	/**
	 * 加密
	 * @param str
	 * @return
	 */
	public String encryption(String str);
	/**
	 * 解密
	 * @param str
	 * @return
	 */
	public String decrypt(String str);
	/**
	 * 数据校验
	 * @return map
	 * String：str
	 * message（验证成功后返回str，验证不成功返回null）
	 */
	public String dataVerification(String str);
}
