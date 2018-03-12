package com.iscreate.plat.login.imageCode;



public interface ImageCodeGenerator {

	/**
	 * 生成图片验证码
	 * @return
	 */
	public CodeContainer creatCode();
	
	/**
	 * 验证码的长度
	 * @param codeLength
	 */
	public void setCodeLength(int codeLength);
}
