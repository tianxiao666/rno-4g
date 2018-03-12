package com.iscreate.plat.networkresource.engine.tool;

public class Queue<E> {
	//
	Object[] elements = new Object[20];
	// 进入队列的个数
	int elementCount = 0;
	// 当前指针位置
	int currentIndex = 0;
	// 队尾指针
	int queueIndex = -1;

	public void clear(){
		elements = new Object[20];
		elementCount = 0;
		currentIndex = 0;
		queueIndex = -1;
	}
	
	@SuppressWarnings("unchecked")
	public E pop() {
		// 如果所有的元素已经遍历过, 返回空
		if (elementCount == 0) {
			return null;
		}
		elementCount--;
		Object e = elements[currentIndex];
		// 如果当前指针在队尾,把它移到队头
		currentIndex++;
		if (currentIndex == elements.length) {
			currentIndex = 0;
		}
		return (E) e;
	}

	@SuppressWarnings("unchecked")
	public E curr() {
		// 返回当前的元素
		return (E) elements[currentIndex];
	}

	public void push(E element) {
		// 如果元素的个数与数组的长度相等,增加数组的长度
		if (elementCount == elements.length) {
			arrayIncrease();
		}
		int index = ++queueIndex;
		if (index == elements.length) {
			index = 0;
			queueIndex = 0;
		}
		elements[index] = element;
		// 保存元素在队列的位置
		elementCount++;
	}

	public boolean hasElements() {
		if (elementCount == 0) {
			return false;
		} else {
			return true;
		}
	}

	private void arrayIncrease() {
		int arrayLen = elements.length;
		int increateSize = arrayLen / 2 + 1;
		Object[] es = new Object[(arrayLen + increateSize)];
		// 如果队尾正好在数据尾部,直接将数据的元素复制到新数组上
		if (queueIndex == arrayLen - 1) {
			System.arraycopy(elements, 0, es, 0, arrayLen);
			elements = es;
			// 如果队尾不在数据尾部,新加的长度放在队尾与队头之间
		} else {
			System.arraycopy(elements, 0, es, 0, queueIndex + 1);
			System.arraycopy(elements, queueIndex + 1, es, queueIndex
					+ increateSize + 1, arrayLen - queueIndex - 1);
			elements = es;
			//调整指针位置
			currentIndex = currentIndex + increateSize;
		}
	}
	
//	private void printArrayElement(){
//		for (int i = 0; i < elements.length; i++) {
//			Object e = elements[i];
//			if (e == null)
//				System.out.print("null   ");
//			else
//				System.out.print(e.toString() + "   ");
//		}
//		System.out.println();
//	}
}
