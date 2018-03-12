package com.iscreate.plat.system.strategy;

/**
 * 密码规则 验证顺序： 长度 -> needNumber ->needChar -> mustAppearChars -> regStr
 * 
 * @author sunny
 * 
 */
public class PasswordRule {

	private int minLength = 6;// 最小长度
	private int maxLength = -1;// 最大长度
	private boolean needNumber = true;// 是否需要数字
	private boolean needChar = true;// 是否需要字母
	private char[] mustAppearChars;// 必须出现的字母或数字
	private String regStr = "";// 正则
	private String prompWords = "请输入至少包含字母、数字且长度不小于6位的密码！";

	public int getMinLength() {
		return minLength;
	}

	public void setMinLength(int minLength) {
		this.minLength = minLength;
	}

	public int getMaxLength() {
		return maxLength;
	}

	public void setMaxLength(int maxLength) {
		this.maxLength = maxLength;
	}

	public String getRegStr() {
		return regStr;
	}

	public void setRegStr(String regStr) {
		this.regStr = regStr;
	}

	public boolean isNeedNumber() {
		return needNumber;
	}

	public void setNeedNumber(boolean needNumber) {
		this.needNumber = needNumber;
	}

	public boolean isNeedChar() {
		return needChar;
	}

	public void setNeedChar(boolean needChar) {
		this.needChar = needChar;
	}

	public char[] getMustAppearChars() {
		return mustAppearChars;
	}

	public void setMustAppearChars(char[] mustAppearChars) {
		this.mustAppearChars = mustAppearChars;
	}

	public String getPrompWords() {
		return prompWords;
	}

	public void setPrompWords(String prompWords) {
		this.prompWords = prompWords;
	}

	/**
	 * 检查输入的密码是否符合要求
	 * 
	 * @param password
	 * @return 0:ok 1:不满足最小长度 2：不满足最大长度 3：不满足needNumber 4：不满足needChar
	 *         5:不满足mustAppearChars 6:不满足regStr
	 * 
	 */
	public int checkIfMeet(String password) {
		if (password == null) {
			return 9;
		}
		int len = password.length();
		if (minLength >0) {
			if (len < minLength) {
				return 1;
			}
		}
		if (maxLength >0 && maxLength>=minLength) {
			if (len > maxLength) {
				return 2;
			}
		}
		if (needNumber) {
			boolean hasNumber=false;
			for(char ch:password.toCharArray()){
				if(Character.isDigit(ch)){
					hasNumber=true;
					break;
				}
			}
			if(!hasNumber){
				return 3;
			}
		}
		if (needChar) {
			boolean hasChar=false;
			for(char ch:password.toCharArray()){
				if(Character.isLetter(ch)){
					hasChar=true;
					break;
				}
			}
			if(!hasChar){
				return 4;
			}
		}
		if (mustAppearChars != null && mustAppearChars.length > 0) {
			int i=0;
			for(char ch:mustAppearChars){
				i++;
				//最多只验证mustAppearChars的前maxLength位数，mustAppearChars里超过maxLength的不验证
				if(maxLength>0 && i>maxLength){
					break;
				}
				if(!password.contains(ch+"")){
					return 5;
				}
			}
		}

		if (regStr != null && !regStr.equals("")) {
			if (!password.matches(regStr)) {
				return 6;
			}
		}

		return 0;
	}
}
