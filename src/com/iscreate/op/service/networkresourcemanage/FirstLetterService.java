package com.iscreate.op.service.networkresourcemanage;



public interface FirstLetterService {
	
	 /**
	  *  获取一个汉字的拼音码 
	  */
    public String getFirstLetter(String str);
    
    /**
     * 获取一个汉字的拼音首字母。 GB码两个字节分别减去160，转换成10进制码组合就可以得到区位码
     * 例如汉字“你”的GB码是0xC4/0xE3，分别减去0xA0（160）就是0x24/0x43
     * 0x24转成10进制就是36，0x43是67，那么它的区位码就是3667，在对照表中读音为‘n’
     */ 
   public  char convert(byte[] bytes);
}
