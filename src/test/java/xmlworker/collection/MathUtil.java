package xmlworker.collection;

import java.util.Arrays;

public class MathUtil {
	
	public static void main(String[] args) {
		try {
			MathUtil mu = new MathUtil();
			int[] array = {149,38,65,97,76,13,27,49};
//			mu.maopaopaixu_l(array);
//			mu.maopaopaixu_r(array);
			mu.kuaisupaixu(array, 0, array.length);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/** 向左冒泡 **/
	public int[] maopaopaixu_l(int[] array) throws Exception {
		
		int k;
		for (int i = 0; i < array.length; i++) {
			k = 0;	//每次开始冒泡前，初始化 k 值为 0
			for (int j = array.length-1; j > i; j--) {
				if (array[j] > array[j-1]) {
					k = 1;
					int temp = array[j];
					array[j] = array[j-1];
					array[j-1] = temp;
				}
			}
			//如果 k 值为 0，表明表中记录排序完成
			if (k == 0) break;
		}
		
		System.out.println(Arrays.toString(array));
		return array;
	}
	
	/** 向右冒泡 **/
	public int[] maopaopaixu_r(int[] array) throws Exception {
			
		int k;
		for (int i = 0; i < array.length; i++) {
			k = 0;	//每次开始冒泡前，初始化 k 值为 0
			for (int j = 0; j < array.length-1-i; j++) {
				if (array[j] > array[j+1]) {
					k = 1;
					int temp = array[j];
					array[j] = array[j+1];
					array[j+1] = temp;
				}
			}
			//如果 k 值为 0，表明表中记录排序完成
			if (k == 0) break;
		}
		
		System.out.println(Arrays.toString(array));
		return array;
	}
	
	/** 快速排序 **/
	public void kuaisupaixu(int[] array, int left, int right) throws Exception {
		
		if (left > right) {
			return;
		}
		int i = left, j = right;
		int temp = array[i];
		while(i != j) {
			//从右边找
			while(array[j] >= temp && i < j) {
				j--;
			}
			//从左边找
			while(array[i] <= temp && i < j) {
				i++;
			}
			if (i < j) {
				int t = array[i];
				array[i] = array[j];
				array[j] = t;
			}
		}
		array[left] = array[i];
		array[i] = temp;
		
		kuaisupaixu(array, left, i-1);
		kuaisupaixu(array, i+1, right);
		System.out.println(Arrays.toString(array));
	}
	
	
	
	
	
	
	
	
	
	
	
	

}
