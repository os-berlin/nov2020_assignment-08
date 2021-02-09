package com.coderscampus.olaf.assignment08;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MultithreadingApp {

	public static void main(String[] args) {

// reading the numbers

		Assignment8 asyncService = new Assignment8();
		List<Integer> syncNumbers = Collections.synchronizedList(new ArrayList<>());
		ExecutorService ioBoundTask = Executors.newCachedThreadPool();
		
		System.out.println("processing numbers, please stand by... \n");

		long start = System.currentTimeMillis();
		for (int i = 0; i < 1000; i++) {
			CompletableFuture.supplyAsync(asyncService::getNumbers, ioBoundTask)
							 .thenAcceptAsync(list -> syncNumbers.addAll(list), ioBoundTask);
		}

		while (syncNumbers.size() != 1000000) {
			// pause main thread until all numbers are processed
		}

		System.out.print("\nitems in array: " + syncNumbers.size());
		if (syncNumbers.size() == 1000000) {
			System.out.println(" -> done (yay, no errors)");
		} else
			System.out.println(" -> Error! (list is incomplete)");

		System.out.println("\n----------------------------");

// counting the numbers and summing up

		int sum = 0;
		System.out.println("count of individual numbers: \n");
		for (int i = 0; i <= Collections.max(syncNumbers); i++) {
			if (i != Collections.max(syncNumbers)) {
				System.out.print(i + ": " + countNumbers(syncNumbers, i) + ", ");
			} else {
				System.out.print(i + ": " + countNumbers(syncNumbers, i) + "\n");
			}
			sum += countNumbers(syncNumbers, i);
		}
		System.out.println("----------------------------\n");
		System.out.println("this sums up to: " + sum + " itmes");
		
		long finish = System.currentTimeMillis();
		System.out.println("time elapsed: " + (finish-start) + " milliseconds");
	}

	private static Long countNumbers(List<Integer> syncNumbers, Integer number) {
		return syncNumbers.stream()
					      .filter(x -> x.equals(number))
					      .count();
	}

}
