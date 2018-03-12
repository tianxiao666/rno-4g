package com.iscreate.plat.networkresource.dictionary.server;

import java.util.List;

public abstract class QuickSort<T> {
	@SuppressWarnings("unchecked")
	public void sort(List<T> array) {
		if (array == null || array.isEmpty()) {
			return;
		}
		Object[] a = array.toArray();
		quickSort(a, 0, array.size() - 1);
		array.clear();
		for(int i =0;i<a.length;i++){
			array.add((T) a[i]);
		}
	}

	public void sort(T[] array) {
		if (array == null || array.length == 0) {
			return;
		}
		quickSort(array, 0, array.length - 1);
	}

	/**
	 * 快速排序实现
	 * 
	 * @param array
	 * @param left
	 * @param right
	 */
	@SuppressWarnings("unchecked")
	private void quickSort(Object[] array, int left, int right) {
		int i = left, j = right;
		Object middle, strTemp;

		middle = array[(left + right) / 2];
		do {
			while (compare((T) array[i], (T) middle) < 0 && (i < right))
				i++;
			while (compare((T) array[j], (T) middle) > 0 && (j > left))
				j--;
			if (i <= j) {
				strTemp = array[i];
				array[i] = array[j];
				array[j] = strTemp;
				i++;
				j--;
			}
		} while (i <= j);
		if (left < j) {
			quickSort(array, left, j);
		}

		if (right > i)
			quickSort(array, i, right);
	}

	public abstract int compare(T obja, T objb);
}
