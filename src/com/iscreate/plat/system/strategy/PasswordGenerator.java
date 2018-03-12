package com.iscreate.plat.system.strategy;

import java.util.Random;

public class PasswordGenerator {
	private int codeCount = 6;
	private PasswordRule passwordRule = new PasswordRule();

	private Random random = new Random();
	private int maxTryTime = 100;
	char[] letterSequence = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'J', 'K',
			'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X',
			'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k',
			'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x',
			'y', 'z' };
	char[] numberSequence = { '1', '2', '3', '4', '5', '6', '7', '8', '9', '0' };

	public String generatePassword() {
		StringBuffer randomCode = new StringBuffer();
		String strRand = null;

		int letterLength = random.nextInt(codeCount) + 1;// 字符占用的字数
		if (letterLength == 6) {
			letterLength = 5;
		}
//		System.out.println("letterLength = "+letterLength);
		int numberLength = codeCount - letterLength;
		char[][] pool = new char[2][];
		pool[0] = letterSequence;
		pool[1] = numberSequence;

		int letterCount = 0, numberCount = 0, index = 0;
		int alreadyTryTimes = 0;
		boolean getAValidPwd=false;
		do {
			alreadyTryTimes++;
			randomCode.setLength(0);
			for (int i = 0; i < codeCount; i++) {
				if (letterCount >= letterLength) {
					index = 1;
				} else if (numberCount >= numberLength) {
					index = 0;
				} else {
					index = random.nextInt(2);
				}
				strRand = String.valueOf(pool[index][random
						.nextInt(pool[index].length)]);
				if (index == 0) {
					letterCount++;
				} else {
					numberCount++;
				}

				randomCode.append(strRand);
			}

			if(passwordRule.checkIfMeet(randomCode.toString()) == 0){
				getAValidPwd=true;
				break;
			}
		} while (alreadyTryTimes <= maxTryTime);

//		System.out.println("try time :"+alreadyTryTimes);
		if(!getAValidPwd){
			System.out.println("没有生成符合要求的密码，将返回最后一次生成的密码!");
		}
		return randomCode.toString();

	}
	
}
