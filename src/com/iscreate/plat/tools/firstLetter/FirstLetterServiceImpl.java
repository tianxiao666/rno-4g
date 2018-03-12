package com.iscreate.plat.tools.firstLetter;

import java.io.UnsupportedEncodingException;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

public class FirstLetterServiceImpl implements FirstLetterService {
	private static final int GB_SP_DIFF = 160; 
	private  static final int[] secPosValueList = { 1601, 1637, 1833, 2078, 2274, 2302, 2433, 2594, 2787, 3106, 3212, 3472, 3635, 3722, 3730, 3858, 4027, 4086, 4390, 4558, 4684, 4925, 
        5249, 5600 };
	private static final char[] firstLetter = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'w', 'x', 'y', 'z' }; 
	
	 // 获取一个汉字的拼音码 
    public  String getFirstLetter(String str) { 
    	StringBuffer buffer = new StringBuffer(); 
    	char ch = str.charAt(0); 
        byte[] uniCode = null; 
        try { 
            uniCode = String.valueOf(ch).getBytes("GBK"); 
        } catch (UnsupportedEncodingException e) { 
            e.printStackTrace(); 
            return null; 
        } 
        if (uniCode[0] < 128 && uniCode[0] > 0) { // 非汉字 
            return null; 
        } else { 
            return  buffer.append(convert(uniCode)).toString(); 
 
        } 
    } 
    
    /**
     * 获取一个汉字的拼音首字母。 GB码两个字节分别减去160，转换成10进制码组合就可以得到区位码
     * 例如汉字“你”的GB码是0xC4/0xE3，分别减去0xA0（160）就是0x24/0x43
     * 0x24转成10进制就是36，0x43是67，那么它的区位码就是3667，在对照表中读音为‘n’
     */ 
   public  char convert(byte[] bytes) { 
        char result = '-'; 
        int secPosValue = 0; 
        int i; 
        for (i = 0; i < bytes.length; i++) { 
            bytes[i] -= GB_SP_DIFF; 
        } 
        secPosValue = bytes[0] * 100 + bytes[1]; 
        for (i = 0; i < 23; i++) { 
            if (secPosValue >= secPosValueList[i] && secPosValue < secPosValueList[i + 1]) { 
                result = firstLetter[i]; 
                break; 
            } 
        } 
        return result; 
    } 

   public static void main(String[] args) {
	   FirstLetterServiceImpl f = new FirstLetterServiceImpl();
	   String n = f.getFirstLetter("泸");
		System.out.println(n);
   }

   /**
    * 获取中文的首个拼音字母
    * @param src - 中文字符串
    * @return 首个拼音字母
    */
   public static String getChinesePinYinFirstLetter ( String src ) {
	   String first_letter = getPinYin(src).substring(0, 1).toUpperCase();
	   return first_letter;
   }
   
   /**
    * 获取中文的拼音
    * @param src - 中文字符串
    * @return
    */
   public static String getPinYin(String src) {
		char[] t1 = null;
		t1 = src.toCharArray();
		String[] t2 = new String[t1.length];
		// 设置汉字拼音输出的格式
		HanyuPinyinOutputFormat t3 = new HanyuPinyinOutputFormat();
		t3.setCaseType(HanyuPinyinCaseType.LOWERCASE);
		t3.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
		t3.setVCharType(HanyuPinyinVCharType.WITH_V);
		String t4 = "";
		int t0 = t1.length;
		try {
			for (int i = 0; i < t0; i++) {
				// 判断能否为汉字字符
				if (Character.toString(t1[i]).matches("[\\u4E00-\\u9FA5]+")) {
					t2 = PinyinHelper.toHanyuPinyinStringArray(t1[i], t3);// 将汉字的几种全拼都存到t2数组中
					t4 += t2[0] + " ";// 取出该汉字全拼的第一种读音并连接到字符串t4后
				} else {
					// 如果不是汉字字符，间接取出字符并连接到字符串t4后
					t4 += Character.toString(t1[i])+" ";
				}
			}
		} catch (BadHanyuPinyinOutputFormatCombination e) {
			e.printStackTrace();
		}
		return t4;
	}


}
