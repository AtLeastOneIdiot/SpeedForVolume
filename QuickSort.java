package com.example.speedup;

public class QuickSort {

	private RunData[] array;
	private int length;

	public void sortDistance(RunData[] inputArr) {

		if (inputArr == null || inputArr.length == 0) {
			return;
		}
		this.array = inputArr;
		length = inputArr.length;
		quickSortDistance(0, length - 1);
	}

	public void sortTime(RunData[] inputArr) {

		if (inputArr == null || inputArr.length == 0) {
			return;
		}
		this.array = inputArr;
		length = inputArr.length;
		quickSortTime(0, length - 1);
	}
	
	public void sortSpeed(RunData[] inputArr) {

		if (inputArr == null || inputArr.length == 0) {
			return;
		}
		this.array = inputArr;
		length = inputArr.length;
		quickSortSpeed(0, length - 1);
	}
	
	private void quickSortDistance(int lowerIndex, int higherIndex) {

		int i = lowerIndex;
		int j = higherIndex;
		// calculate pivot number, I am taking pivot as middle index number
		RunData pivot = array[lowerIndex + (higherIndex - lowerIndex) / 2];
		// Divide into two arrays
		while (i <= j) {
			/**
			 * In each iteration, we will identify a number from left side which
			 * is greater then the pivot value, and also we will identify a
			 * number from right side which is less then the pivot value. Once
			 * the search is done, then we exchange both numbers.
			 */
			while (array[i].getDistance() < pivot.getDistance()) {
				i++;
			}
			while (array[j].getDistance() > pivot.getDistance()) {
				j--;
			}
			if (i <= j) {
				exchangeNumbers(i, j);
				// move index to next position on both sides
				i++;
				j--;
			}
		}
		// call quickSort() method recursively
		if (lowerIndex < j)
			quickSortDistance(lowerIndex, j);
		if (i < higherIndex)
			quickSortDistance(i, higherIndex);
	}

	private void quickSortTime(int lowerIndex, int higherIndex) {

		int i = lowerIndex;
		int j = higherIndex;
		// calculate pivot number, I am taking pivot as middle index number
		RunData pivot = array[lowerIndex + (higherIndex - lowerIndex) / 2];
		// Divide into two arrays
		while (i <= j) {
			/**
			 * In each iteration, we will identify a number from left side which
			 * is greater then the pivot value, and also we will identify a
			 * number from right side which is less then the pivot value. Once
			 * the search is done, then we exchange both numbers.
			 */
			while (array[i].getTime() < pivot.getTime()) {
				i++;
			}
			while (array[j].getTime() > pivot.getTime()) {
				j--;
			}
			if (i <= j) {
				exchangeNumbers(i, j);
				// move index to next position on both sides
				i++;
				j--;
			}
		}
		// call quickSort() method recursively
		if (lowerIndex < j)
			quickSortTime(lowerIndex, j);
		if (i < higherIndex)
			quickSortTime(i, higherIndex);
	}
	
	private void quickSortSpeed(int lowerIndex, int higherIndex) {

		int i = lowerIndex;
		int j = higherIndex;
		// calculate pivot number, I am taking pivot as middle index number
		RunData pivot = array[lowerIndex + (higherIndex - lowerIndex) / 2];
		// Divide into two arrays
		while (i <= j) {
			/**
			 * In each iteration, we will identify a number from left side which
			 * is greater then the pivot value, and also we will identify a
			 * number from right side which is less then the pivot value. Once
			 * the search is done, then we exchange both numbers.
			 */
			while (array[i].getSpeed() < pivot.getSpeed()) {
				i++;
			}
			while (array[j].getSpeed() > pivot.getSpeed()) {
				j--;
			}
			if (i <= j) {
				exchangeNumbers(i, j);
				// move index to next position on both sides
				i++;
				j--;
			}
		}
		// call quickSort() method recursively
		if (lowerIndex < j)
			quickSortSpeed(lowerIndex, j);
		if (i < higherIndex)
			quickSortSpeed(i, higherIndex);
	}
	
	private void exchangeNumbers(int i, int j) {
		RunData temp = array[i];
		array[i] = array[j];
		array[j] = temp;
	}

	public static void main(String[] args) {

		QuickSort sorter = new QuickSort();
		RunData[] input = { new RunData(20, 2.47), new RunData(50, 10), new RunData(30, 2), new RunData(15, 5), new RunData(80, 10) };
		sorter.sortDistance(input);
		System.out.println("Distance, Time, Speed (min/mile)");
		for (int i = 0; i < input.length; i++) {
			System.out.println(input[i].toStringDistance());
		}
	}
}
