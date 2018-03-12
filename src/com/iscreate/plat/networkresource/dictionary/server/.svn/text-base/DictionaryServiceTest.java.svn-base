package com.iscreate.plat.networkresource.dictionary.server;

public class DictionaryServiceTest {

	public static int[] QuickSort0(int[] pData, int left, int right) {
		int i = left, j = right;
		int middle, strTemp;

		middle = pData[(left + right) / 2];
		for (int t = 0; t < pData.length; t++)
			System.out.print(pData[t] + " ");
		System.out.println("");
		do {
			while ((pData[i] < middle) && (i < right))
				i++;
			System.out.println("middle:"+middle+" 小于等于左边第"+i+"位数:"+pData[i]);
			while ((pData[j] > middle) && (j > left))
				j--;
			System.out.println("middle:"+middle+" 大于等于右边第"+j+"位数:"+pData[j]);
			if (i <= j) {
				System.out.print("i 大于等于 j， "+pData[i]+"与"+pData[j]+"互换：");
				strTemp = pData[i];
				pData[i] = pData[j];
				pData[j] = strTemp;
				i++;
				j--;
				for (int t = 0; t < pData.length; t++)
					System.out.print(pData[t] + " ");
				System.out.println("");
			}
			System.out.println("");
		} while (i <= j);
		if (left < j) {
			QuickSort0(pData, left, j);
		}

		if (right > i)
			QuickSort0(pData, i, right);
		return pData;
	}

	public static void main(String[] argv) {
		int[] pData = {1};
		QuickSort0(pData, 0, pData.length - 1);
	}

}
