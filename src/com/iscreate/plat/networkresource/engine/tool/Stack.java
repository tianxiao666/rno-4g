package com.iscreate.plat.networkresource.engine.tool;

import java.util.ArrayList;

public class Stack {
	Object[] elements = new Object[20];
	// 当前指针位置
	int currentIndex = 0;

	public Object pop() {
		if (currentIndex == 0) {
			return "";
		}
		currentIndex--;
		return elements[currentIndex];
	}

	public Object curr() {
		// 返回当前的元素
		if (currentIndex == 0) {
			return "";
		}
		return elements[currentIndex - 1];
	}

	public void push(Object element) {
		if (currentIndex == elements.length) {
			arrayIncrease();
		}
		elements[currentIndex++] = element;
	}

	public boolean hasElements() {
		if (currentIndex == 0) {
			return false;
		} else {
			return true;
		}
	}

	private void arrayIncrease() {
		int arrayLen = elements.length;
		int increateSize = arrayLen / 2 + 1;
		Object[] es = new Object[(arrayLen + increateSize)];
		System.arraycopy(elements, 0, es, 0, arrayLen);
		elements = es;
	}

	public ArrayList<Object> getStackElements() {
		ArrayList<Object> els = new ArrayList<Object>();
		for (int i = 0; i < currentIndex; i++) {
			els.add(elements[i]);
		}
		return els;
	}

	public boolean contains(Object element) {
		if (currentIndex == 0) {
			return false;
		}
		if (element == null) {
			return false;
		}
		for (int i = 0; i < currentIndex; i++) {
			if (element.equals(elements[i])) {
				return true;
			}
		}
		return false;
	}

}
